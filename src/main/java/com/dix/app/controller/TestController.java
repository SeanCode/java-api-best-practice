package com.dix.app.controller;

import com.dix.base.common.DataResponse;
import com.dix.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    public TestController(@Qualifier("RedisTemplate") RedisTemplate<String, String> template, UserService userService) {
        this.template = template;
        this.userService = userService;
    }

    @RequestMapping("/transaction")
    public DataResponse transaction() {
        // userService.testTransaction();
        return DataResponse.create();
    }

    private final RedisTemplate<String, String> template;

    private final UserService userService;

}
