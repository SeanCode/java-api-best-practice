package com.dix.base.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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

    private static Logger logger = LoggerFactory.getLogger(Redis.class);


    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.port}")
    private Integer port;

    @Value("${spring.redis.database}")
    private Integer database;

    @Value("${spring.redis.pool.max-active}")
    private Integer poolMaxActive;

    @Value("${spring.redis.pool.max-idle}")
    private Integer poolMaxIdle;

    @Value("${spring.redis.pool.max-wait}")
    private Long poolMaxWait;

    @Value("${spring.redis.pool.min-idle}")
    private Integer poolMinIdle;

    private Jedis jedis;
    private JedisPool jedisPool;

    public Redis()
    {
    }

    @PostConstruct
    public void init()
    {
        password = Strings.isNullOrEmpty(password) ? null : password;

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMinIdle(poolMinIdle);
        jedisPoolConfig.setMaxIdle(poolMaxIdle);
        jedisPoolConfig.setMaxTotal(poolMaxActive);
        jedisPoolConfig.setMaxWaitMillis(poolMaxWait);
        jedisPool = new JedisPool(jedisPoolConfig, host, port, Protocol.DEFAULT_TIMEOUT, password);
    }

    @PreDestroy
    public void onDestroy()
    {
        if (jedisPool != null)
        {
            jedisPool.destroy();
        }
    }

    public Jedis getClient()
    {
        return jedisPool.getResource();
    }

    private String getKeyOfLock(String key)
    {
        return String.format("%s.lock.%s", Const.REDIS_KEY_PREFIX, key);
    }

    public boolean lock(String key)
    {
        return lock(key, 60);
    }

    public boolean lock(String key, int expireSeconds)
    {
        try (Jedis jedis = getClient()) {
            String redisKey = getKeyOfLock(key);
            Long count = jedis.incrBy(redisKey, 1);
            jedis.expire(redisKey, expireSeconds);
            return count == 1;
        }
    }

    public void unlock(String key)
    {
        try (Jedis jedis = getClient()) {
            String redisKey = getKeyOfLock(key);
            jedis.del(redisKey);
        }
    }

    public <T> T get(String key, RedisGetMethodInterface redisGetMethodInterface, Type type, int expireSeconds)
    {
        try (Jedis jedis = getClient()) {
            String data = jedis.get(key);
            if (Strings.isNullOrEmpty(data))
            {
                Object dataObject = redisGetMethodInterface.method();
                data = Util.jsonEncode(dataObject);
                jedis.setex(key, expireSeconds, data);
            }
            return Util.jsonDecode(data, type);
        }
    }
}
