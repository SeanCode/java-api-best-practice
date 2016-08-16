package com.dix.mapper;

import com.dix.model.Token;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TokenMapper {

    Token findToken(String token);

    void updateUserTokenInvalidByUserId(@Param("user_id") Long userId);

    void updateUserTokenInvalidByToken(@Param("token") String token);

    Long insert(Token token);

    Long update(Token token);
}
