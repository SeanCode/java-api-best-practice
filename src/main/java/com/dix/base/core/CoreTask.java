package com.dix.base.core;

import com.dix.base.common.Const;
import com.dix.base.common.Util;
import com.dix.base.redis.Redis;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoreTask {
    private static Logger logger = LoggerFactory.getLogger(CoreTask.class);

    private static final HashMap<String, Method> methodCache = new HashMap<String, Method>();


    private static String makeId() {
        Redis redis = Core.getBean(Redis.class);
        try (Jedis redisClient = redis.getClient()) {
            Long count = redisClient.incr(Const.getKeyOfTodayTaskCount());
            return DigestUtils.md5Hex(System.currentTimeMillis() + "-" + count);
        }
    }

    public static String submit(Class cls, Map<String, Object> data) {
        String className = cls.getName();
        Long time = Util.time();
        String id = makeId();

        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("executor", className);
        map.put("time", "" + time);
        map.put("data", Util.jsonEncode(data));

        Redis redis = Core.getBean(Redis.class);
        try (Jedis redisClient = redis.getClient()) {
            redisClient.hmset(Const.getKeyOfTaskId(id), map);
            redisClient.lpush(Const.getKeyOfPendingExecuteTaskIdList(), id);
        }

        return id;
    }

    public static void loop() {
        Redis redis = Core.getBean(Redis.class);
        while (true) {
            logger.info("tick");

            try (Jedis redisClient = redis.getClient()) {
                List<String> resultList = redisClient.brpop(9, Const.getKeyOfPendingExecuteTaskIdList());
                if (resultList != null && resultList.size() == 2) {
                    String taskId = resultList.get(1);
                    logger.info("pop {}", taskId);

                    Map<String, String> task = redisClient.hgetAll(Const.getKeyOfTaskId(taskId));
                    if (!Util.containsKey(task, "id", "executor", "time", "data")) {
                        logger.info("invalid task {}", Util.jsonEncode(task));
                        break;
                    }

                    String className = task.get("executor");
                    Map<String, Object> data = Util.jsonDecode(task.get("data"), new TypeReference<Map<String, Object>>(){});

                    Method method = null;

                    if (methodCache.containsKey(className)) {
                        method = methodCache.get(className);
                    } else {
                        try {
                            Class cls = Class.forName(className);
                            method = cls.getMethod("execute", Map.class);
                            methodCache.put(className, method);
                        } catch (ClassNotFoundException e) {
                            logger.info("get method error: ClassNotFoundException, className: {}", className);
                        } catch (NoSuchMethodException e) {
                            logger.info("get method error: NoSuchMethodException, className: {}", className);
                        }
                    }

                    if (method == null) {
                        break;
                    }

                    try {
                        method.invoke(null, data);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        logger.info("execute task error: {}", e.getMessage());
                    }
                }
            }
        }
    }

}
