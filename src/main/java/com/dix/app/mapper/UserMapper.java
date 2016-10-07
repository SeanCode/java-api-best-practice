package com.dix.app.mapper;

import com.dix.app.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by dd on 7/24/16.
 */

@Mapper
@Component
public interface UserMapper {

    User existsById(@Param("user_id") Long userId);

    User getUserByUid(@Param("uid") String uid);

    User existsByName(@Param("name") String name);

    User existsByPhone(@Param("phone") String phone);

    User existsByEmail(@Param("email") String email);

    User existsByUserName(@Param("username") String username);

    User existsByUidIgnoreCase(@Param("uid") String uid);

    User getUserByOuterUserId(@Param("type") Integer type, @Param("outer_user_id") String outerUserId);

    Integer getUserCount();

    Long insert(User user);

    Long update(User user);

    User findById(@Param("id") Long id);

    List<Map> getSomeUsers();

    List<Map> getSomeUsersByQuery(@Param("userIdList") List<Long> userIdList);

    List<Map> getUserList(@Param("limit") Integer limit, @Param("offset") Integer offset);

}