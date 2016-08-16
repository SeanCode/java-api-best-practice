package com.dix.controller.client.v1;

import com.dix.common.DataResponse;
import com.dix.model.Token;
import com.dix.model.User;
import com.dix.model.UserBind;
import com.dix.service.TokenService;
import com.dix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

/**
 * Created by dd on 1/25/16.
 */
@RestController("Client.UserController")
@RequestMapping("/client/1/user")
@Component
public class UserController {

    @RequestMapping("/login")
    public DataResponse login(@RequestParam("phone") String phone, @RequestParam("password") String password) throws InvalidKeySpecException, NoSuchAlgorithmException {

        User user = userService.loginByPhone(phone, password);
        Token token = tokenService.generateTokenForUser(user);

        return DataResponse.create()
                .put("user", user.processModel())
                .put("token", token.getToken());
    }

    @RequestMapping("/logout")
    public DataResponse logout() {

        String token = request.getParameter("token");
        tokenService.makeUserTokenInvalidByToken(token);
        return DataResponse.create();
    }

    @RequestMapping("/register")
    public DataResponse register(@RequestParam("phone") String phone, @RequestParam("password") String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        User user = userService.register(phone, password);
        return DataResponse.create()
                .put("user", user);
    }

    @RequestMapping("/detail")
    public DataResponse getUserDetail(@RequestParam(value = "user_id", required = false) Long userId) {
        if (userId == null) {
            userId = (Long) request.getAttribute("userId");
        }
        User user = userService.checkUserById(userId);
        return DataResponse.create()
                .put("user", user.processModel());
    }

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private HttpServletRequest request;
}
