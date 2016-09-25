package com.dix.service;

import com.dix.common.Util;
import com.dix.exception.UserNotExistsException;
import com.dix.mapper.TokenMapper;
import com.dix.model.Token;
import com.dix.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
        token.setUserId(user.getId());

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
