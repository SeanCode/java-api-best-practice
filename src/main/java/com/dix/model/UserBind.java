package com.dix.model;

import com.dix.common.Util;
import com.dix.mapper.UserBindMapper;
import com.dix.mapper.UserMapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.dix.common.Core;
import com.dix.common.ModelApiInterface;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "user_bind", schema = "smartwork_user", catalog = "")
public class UserBind implements ModelApiInterface {

    public static final int TYPE_WEXIN = 1;

    private Long id;
    private Integer type = 0;
    private Long userId = 0L;
    private String outerUserId = "";
    private String data = "";
    private Integer weight = 0;
    private Long createTime;
    private Long updateTime;

    @Id
    @Column(name = "id", nullable = false)
    @JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "type", nullable = false)
    @JsonProperty("type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Basic
    @Column(name = "user_id", nullable = false)
    @JsonProperty("user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "outer_user_id", nullable = false)
    @JsonProperty("outer_user_id")
    public String getOuterUserId() {
        return outerUserId;
    }

    public void setOuterUserId(String outerUserId) {
        this.outerUserId = outerUserId;
    }

    @Basic
    @Column(name = "data", nullable = false, length = 255)
    @JsonProperty("data")
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Basic
    @Column(name = "weight", nullable = false)
    @JsonProperty("weight")
    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Basic
    @Column(name = "create_time", nullable = false)
    @JsonProperty("create_time")
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "update_time", nullable = false)
    @JsonProperty("update_time")
    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserBind that = (UserBind) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (outerUserId != null ? !outerUserId.equals(that.outerUserId) : that.outerUserId != null) return false;
        if (data != null ? !data.equals(that.data) : that.data != null) return false;
        if (weight != null ? !weight.equals(that.weight) : that.weight != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (outerUserId != null ? outerUserId.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        return result;
    }

    public Long save() {
        UserBindMapper userBindMapper = (UserBindMapper) Core.getBean(UserBindMapper.class);

        if (this.getId() == null || this.getId() == 0)
        {
            this.setCreateTime(Util.time());
            this.setUpdateTime(this.getCreateTime());
            return userBindMapper.insert(this);
        }
        else
        {
            this.setUpdateTime(Util.time());
            return userBindMapper.update(this);
        }
    }

    @Transient
    public static String[] getBasicAttrs() {
        return new String[] {
                "id",
                "type",
                "user_id",
                "outer_user_id",
                "data"
        };
    }

    @Transient
    @Override
    public String[] getAttributes() {
        return new String[] {
                "id",
                "type",
                "user_id",
                "outer_user_id",
                "data"
        };
    }

    @Transient
    @Override
    public String[] getBasicAttributes() {
        return new String[] {
                "id",
                "type",
                "user_id",
                "outer_user_id",
                "data"
        };
    }

    @Transient
    @Override
    public String[] getDetailAttributes() {
        return new String[] {
                "id",
                "type",
                "user_id",
                "outer_user_id",
                "data",
                "create_time",
                "update_time"
        };
    }

    @Override
    public Map<String, Object> processModel() {
        return this.processModel(this, null);
    }

    @Override
    public Map<String, Object> processModel(Object model) {
        return this.processModel(model, null);
    }

    @Override
    public Map<String, Object> processModel(String[] keys) {
        return this.processModel(null, keys);
    }

    @Override
    public Map<String, Object> processModel(Object model, String[] keys) {
        Map<String, Object> map = Core.processModel(model, keys);

        if (map != null)
        {

        }

        return map;
    }

    public static Map<String, Object> processModelStatic(Object model, String[] keys) {
        Map<String, Object> map = Core.processModel(model, keys);

        if (map != null)
        {

        }

        return map;
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
