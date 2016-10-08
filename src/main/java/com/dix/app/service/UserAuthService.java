package com.dix.app.service;

import com.dix.app.model.Token;
import com.dix.base.common.Const;
import com.dix.base.exception.TokenInvalidException;
import com.dix.base.interceptor.SecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dd on 08/10/2016.
 */
@Component
public class UserAuthService implements SecurityInterceptor.UserAuth {

    private final TokenService tokenService;

    @Autowired
    public UserAuthService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Map<String, Object> getUserByToken(String token) {

        Token tokenModel = tokenService.getToken(token);
        if (tokenModel == null)
        {
            throw new TokenInvalidException();
        }

        HashMap<String, Object> user = new HashMap<>();
        user.put(Const.USER_AUTH_KEY_USER_ID, tokenModel.getUserId());
        return user;
    }
}
