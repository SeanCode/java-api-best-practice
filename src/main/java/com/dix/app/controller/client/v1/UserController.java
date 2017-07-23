package com.dix.app.controller.client.v1;

import com.dix.base.common.DataResponse;
import com.dix.app.model.Token;
import com.dix.app.model.User;
import com.dix.app.service.TokenService;
import com.dix.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by dd on 1/25/16.
 */
@RestController("Client.UserController")
@RequestMapping("/client/1/user")
@Component
public class UserController {

    private final UserService userService;

    private final TokenService tokenService;

    private final HttpServletRequest request;

    @Autowired
    public UserController(HttpServletRequest request, TokenService tokenService, UserService userService) {
        this.request = request;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @RequestMapping("/login")
    public DataResponse login(@RequestParam("phone") String phone, @RequestParam("password") String password) throws InvalidKeySpecException, NoSuchAlgorithmException {

        User user = userService.loginByPhone(phone, password);
        Token token = tokenService.generateTokenForUser(user);

        return DataResponse.create()
                .put("user", user.process())
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
                .put("user", user.process());
    }
}
