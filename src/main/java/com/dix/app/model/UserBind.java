package com.dix.app.model;

import com.dix.base.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.dix.base.core.Core;

import java.util.Map;


public class UserBind extends BaseModel {

    public static final int TYPE_WEXIN = 1;

    private Long id;
    private Integer type = 0;
    private Long userId = 0L;
    private String outerUserId = "";
    private String data = "";
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

    @JsonProperty("type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @JsonProperty("user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @JsonProperty("outer_user_id")
    public String getOuterUserId() {
        return outerUserId;
    }

    public void setOuterUserId(String outerUserId) {
        this.outerUserId = outerUserId;
    }

    @JsonProperty("data")
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
        return new String[] { "id", "type", "user_id", "outer_user_id", "data" };
    }

    @Override
    @JsonIgnore
    public String[] getBasicAttributes() {
        return new String[] { "id", "type", "user_id", "outer_user_id", "data" };
    }

    @Override
    @JsonIgnore
    public String[] getDetailAttributes() {
        return new String[] { "id", "type", "user_id", "outer_user_id", "data", "create_time", "update_time" };
    }

    public Map<String, Object> processModelForClient(Object model, String[] keys, Object[] params) {
        Map<String, Object> map = Core.processModel(model, keys);

        if (map != null)
        {
            map.put("client", "dix");
            map.put("params", params);
        }

        return map;
    }
}
