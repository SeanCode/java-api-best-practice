package com.dix.app.controller;

import com.dix.app.model.User;
import com.dix.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    public UserController(@Qualifier("RedisTemplate") RedisTemplate<String, String> template, UserService userService) {
        this.template = template;
        this.userService = userService;
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
    public Map redisSet(@Param("key") String key, @Param("value") String value) {

        template.boundValueOps(key).set(value);

        return null;
    }

    @RequestMapping("/redis-get")
    public String redisGet(@RequestParam("key") String key) {

        return template.boundValueOps(key).get();
    }


    private final RedisTemplate<String, String> template;

    private final UserService userService;

}
