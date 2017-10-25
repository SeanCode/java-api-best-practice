package com.dix.app.model;

import com.dix.base.model.BaseModel;
import com.dix.base.model.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.dix.app.mapper.UserMapper;

/**
 * Created by dd on 1/26/16.
 */
@Model(tableName = "user", mapper = UserMapper.class)
public class User extends BaseModel {
    private Long id;
    private String uid;
    private String username = "";
    private String phone = "";
    private String password = "";
    private String email = "";
    private String name = "";
    private String tel = "";
    private Integer gender = 0;
    private Long birthday = 0L;
    private String avatar = "";
    private Integer sourceId = 0;
    private Integer sourceType = 0;
    private Integer sourceClient = 0;
    private Integer weight = 0;
    private Long createTime;
    private Long updateTime;

    @JsonProperty("id")
    public Long ID() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("uid")
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty("phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("tel")
    public String getTel() { return tel; }

    public void setTel(String tel) { this.tel = tel; }

    @JsonProperty("gender")
    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @JsonProperty("birthday")
    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    @JsonProperty("avatar")
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @JsonProperty("source_id")
    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    @JsonProperty("source_type")
    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    @JsonProperty("source_client")
    public Integer getSourceClient() {
        return sourceClient;
    }

    public void setSourceClient(Integer sourceClient) {
        this.sourceClient = sourceClient;
    }

    @JsonProperty("weight")
    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
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
        return new String[] { "id", "uid", "username", "email", "phone", "password", "name", "gender", "birthday", "avatar", "weight", "create_time", "update_time"};
    }

    @Override
    @JsonIgnore
    public String[] getBasicAttributes() {
        return new String[] { "id", "username", "email", "phone", "name", "avatar", };
    }

    @Override
    @JsonIgnore
    public String[] getDetailAttributes() {
        return new String[] { "id", "uid", "username", "email", "phone", "name", "gender", "birthday", "avatar", "create_time", "update_time" };
    }
}
