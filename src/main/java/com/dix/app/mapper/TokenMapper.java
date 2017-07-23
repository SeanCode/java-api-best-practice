package com.dix.app.mapper;

import com.dix.app.model.Token;
import com.dix.base.model.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TokenMapper extends BaseMapper<Token> {

    @Override
    Long insert(Token token);

    @Override
    Long update(Token token);


    Token findToken(String token);

    void updateUserTokenInvalidByUserId(@Param("user_id") Long userId);

    void updateUserTokenInvalidByToken(@Param("token") String token);


}
