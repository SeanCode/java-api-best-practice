package com.dix.base.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by dd on 7/24/16.
 */
@Component("Redis")
public class Redis {

    private RedisTemplate<String, String> redisTemplate;

    public Redis(@Qualifier("RedisTemplate") RedisTemplate<String, String> redisTemplate)
    {
        this.redisTemplate = redisTemplate;
    }

    private String getKeyOfLock(String key)
    {
        return String.format("%s.lock.%s", Const.REDIS_KEY_PREFIX, key);
    }

    public boolean lock(String key)
    {
        return lock(key, 60);
    }

    public boolean lock(String key, long expireSeconds)
    {
        String redisKey = getKeyOfLock(key);
        BoundValueOperations valueOperations = redisTemplate.boundValueOps(redisKey);
        Long count = valueOperations.increment(1);
        valueOperations.expire(expireSeconds, TimeUnit.SECONDS);
        return count == 1;
    }

    public void unlock(String key)
    {
        String redisKey = getKeyOfLock(key);
        redisTemplate.delete(redisKey);
    }

    public <T> T get(String key, RedisGetMethodInterface redisGetMethodInterface, Type type, int expireSeconds)
    {
        BoundValueOperations<String, String> valueOperations = redisTemplate.boundValueOps(key);
        String data = valueOperations.get();
        if (Strings.isNullOrEmpty(data))
        {
            Object dataObject = redisGetMethodInterface.method();
            data = Util.jsonEncode(dataObject);
            valueOperations.set(data, expireSeconds, TimeUnit.SECONDS);
        }
        return Util.jsonDecode(data, type);
    }
}
