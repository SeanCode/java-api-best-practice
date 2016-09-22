package com.dix.controller;

import com.dix.common.DataResponse;
import com.dix.model.Token;
import com.dix.model.User;
import com.dix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

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
        userService.testTransaction();
        return DataResponse.create();
    }

    private final RedisTemplate<String, String> template;

    private final UserService userService;

}
