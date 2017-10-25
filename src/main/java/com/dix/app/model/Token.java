package com.dix.app.model;

import com.dix.app.mapper.TokenMapper;
import com.dix.base.model.BaseModel;
import com.dix.base.model.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.DigestUtils;

import java.util.UUID;

@Model(tableName = "token", mapper = TokenMapper.class)
public class Token extends BaseModel {

    public static final int STATUS_INVALID = 0;
    public static final int STATUS_VALID = 1;

    public static final int TYPE_PUBLIC = 1;
    public static final int TYPE_PRIVATE = 2;

    private Long id;
    private String token;
    private Integer type = TYPE_PUBLIC;
    private Integer status;
    private Long userId;
    private Long expireTime;
    private Long createTime;
    private Long updateTime;

    public static String makeToken()
    {
        String token = System.currentTimeMillis() + ":" + Math.random() + ":" + Math.random() + ':' + UUID.randomUUID().toString();
        return DigestUtils.md5DigestAsHex(token.getBytes());
    }

    @JsonProperty("id")
    public Long ID() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @JsonProperty("type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @JsonProperty("status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @JsonProperty("user_id")
    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    @JsonProperty("expire_time")
    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    @JsonProperty("create_time")
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @JsonProperty("update_time")
    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }


    @Override
    @JsonIgnore
    public String[] getAttributes() {
        return new String[] { "id", "token", "type", "status", "user_id", "expire_time", "create_time", "update_time" };
    }

    @Override
    @JsonIgnore
    public String[] getBasicAttributes() {
        return new String[] { "id", "token", "type", "status", "user_id", "expire_time" };
    }

    @Override
    @JsonIgnore
    public String[] getDetailAttributes() {
        return new String[] { "id", "token", "type", "status", "user_id", "expire_time", "create_time", "update_time" };
    }

}
