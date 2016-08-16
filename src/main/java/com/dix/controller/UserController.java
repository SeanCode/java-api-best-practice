package com.dix.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.dix.model.User;
import com.dix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/mapper-user")
    public Map getUserByMapper() {
        try {
            User user = userService.findById(2L);
            return user.processModel();

        } catch (Exception exception) {
            return null;
        }
    }

    @RequestMapping("/detail-by-phone")
    public Map getUserDetailByPhone() {
        try {
            User user = userService.findByPhone("13164403207");
            return user.processModel();

        } catch (Exception exception) {
            return null;
        }
    }



    @RequestMapping("/redis-set")
    public Map redisSet(@Param("key") String key, @Param("value") String value) {

        template.boundValueOps(key).set(value);

        return null;
    }

    @RequestMapping("/redis-get")
    public String redisGet(@Param("key") String key) {

        return template.boundValueOps(key).get();
    }

    @Autowired
    @Qualifier("RedisTemplate")
    private RedisTemplate<String, String> template;

    @Autowired
    private UserService userService;

}
