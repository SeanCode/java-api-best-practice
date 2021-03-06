package com.dix.app.service;

import com.dix.base.common.Util;
import com.dix.app.exception.UserNotExistsException;
import com.dix.app.mapper.TokenMapper;
import com.dix.app.model.Token;
import com.dix.app.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by dd on 9/7/15.
 */
@Component
public class TokenService {

    private static Logger logger = LoggerFactory.getLogger(TokenService.class);

    private final UserService userService;

    private final TokenMapper tokenMapper;

    @Autowired
    public TokenService(TokenMapper tokenMapper, UserService userService) {
        this.tokenMapper = tokenMapper;
        this.userService = userService;
    }

    public Token getToken(String token)
    {
        return tokenMapper.findToken(token);
    }

    public Token generateTokenForUser(User user)
    {
        if (user == null)
        {
            throw new UserNotExistsException();
        }

        Token token = new Token();
        token.setToken(Token.makeToken());
        token.setStatus(Token.STATUS_VALID);
        token.setUserId(user.ID());

        Long time = Util.time();
        token.setExpireTime(0L);
        token.setCreateTime(time);
        token.setUpdateTime(time);

        token.save();

        return token;
    }

    public Token generateTokenForUser(String uid)
    {
        User user = userService.findByUid(uid);
        return generateTokenForUser(user);
    }

    public void makeUserTokenInvalidByUserId(Long userId)
    {
        tokenMapper.updateUserTokenInvalidByUserId(userId);
    }

    public void makeUserTokenInvalidByToken(String token)
    {
        tokenMapper.updateUserTokenInvalidByToken(token);
    }
}
