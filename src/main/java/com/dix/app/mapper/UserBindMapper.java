package com.dix.app.mapper;

import com.dix.app.model.UserBind;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface UserBindMapper {

    UserBind existsBindByUser(@Param("type") Integer type, @Param("user_id") Long userId);

    UserBind existsBindByOuterUser(@Param("type") Integer type, @Param("outer_user_id") String outerUserId);

    List<Map> getUserBindList(@Param("user_id") Long userId);

    void deleteUserBindOfType(@Param("type") Integer type, @Param("user_id") Long userId);

    Long insert(UserBind userBind);

    Long update(UserBind userBind);
}
