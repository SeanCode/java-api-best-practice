package com.dix.app.controller;

import com.dix.app.model.User;
import com.dix.app.service.UserService;
import com.dix.base.redis.Redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final Redis redis;
    private final UserService userService;

    @Autowired
    public UserController(UserService userService, Redis redis) {
        this.userService = userService;
        this.redis = redis;
    }

    @RequestMapping("/mapper-user")
    public Map getUserByMapper() {
        try {
            User user = userService.findById(2L);
            return user.process();

        } catch (Exception exception) {
            return null;
        }
    }

    @RequestMapping("/detail-by-phone")
    public Map getUserDetailByPhone() {
        try {
            User user = userService.findByPhone("13164403207");
            return user.process();

        } catch (Exception exception) {
            return null;
        }
    }



    @RequestMapping("/redis-set")
    public Map redisSet(@RequestParam("key") String key, @RequestParam("value") String value) {

        try (Jedis jedis = redis.getClient()){
            jedis.set(key, value);
        }

        return null;
    }

    @RequestMapping("/redis-get")
    public String redisGet(@RequestParam("key") String key) {
        try (Jedis jedis = redis.getClient()){
            return jedis.get(key);
        }
    }
}
