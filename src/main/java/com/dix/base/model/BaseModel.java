package com.dix.base.model;

import com.dix.base.common.Util;
import com.dix.base.core.Core;
import com.dix.base.exception.BaseException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by dd on 21/07/2017.
 */
public abstract class BaseModel {

    private static Logger logger = LoggerFactory.getLogger(BaseModel.class);

    public abstract Long ID();

    @JsonIgnore
    public abstract String[] getAttributes();

    @JsonIgnore
    public abstract String[] getBasicAttributes();

    @JsonIgnore
    public abstract String[] getDetailAttributes();



    private void invokeSet(Object target, String key, Object value)
    {
        String[] keyParts = StringUtils.split(key, "_");
        StringBuilder methodName = new StringBuilder("set");
        for (String keyPart : keyParts) {
            methodName.append(StringUtils.capitalize(keyPart));
        }

        // logger.info("set methodName: {}, value: {}", methodName, value);

        for (Method method : this.getClass().getMethods())
        {
            if (method.getName().equals(methodName.toString()))
            {
                try {
                    method.invoke(target, value);
                } catch (IllegalAccessException e) {
                    logger.info("invoke {}.{} throws IllegalAccessException: {}", this.getClass(), methodName.toString(), e.getMessage());
                } catch (InvocationTargetException e) {
                    logger.info("invoke {}.{} throws InvocationTargetException: {}", this.getClass(), methodName.toString(), e.getMessage());
                }

                return;
            }
        }
    }

    private Object invokeGet(Object target, String key)
    {
        String[] keyParts = StringUtils.split(key, "_");
        StringBuilder methodName = new StringBuilder("get");
        for (String keyPart : keyParts) {
            methodName.append(StringUtils.capitalize(keyPart));
        }

        // logger.info("get methodName: {}", methodName);

        for (Method method : this.getClass().getMethods())
        {
            if (method.getName().equals(methodName.toString()))
            {
                try {
                    return method.invoke(target);
                } catch (IllegalAccessException e) {
                    logger.info("invoke {}.{} throws IllegalAccessException: {}", this.getClass(), methodName.toString(), e.getMessage());
                } catch (InvocationTargetException e) {
                    logger.info("invoke {}.{} throws InvocationTargetException: {}", this.getClass(), methodName.toString(), e.getMessage());
                }
            }
        }

        return null;
    }

    protected boolean beforeSave(boolean setTime)
    {
        Long time = Util.time();
        if (isNew() && setTime)
        {
            invokeSet(this, "create_time", time);
        }

        if (setTime)
        {
            invokeSet(this, "update_time", time);
        }

        return true;
    }

    protected void afterSave()
    {

    }

    public Long save() {
        Model model = getClass().getAnnotation(Model.class);
        if (model == null) {
            throw new BaseException(-1, "" + getClass().toString() + " must have annotation: Model");
        }

        if (!beforeSave(true)) {
            throw new BaseException(-1, getClass().toString() + " beforeSave return false");
        }

        BaseMapper baseMapper = Core.getBean(model.mapper());

        Long id;
        if (this.isNew()) {
             id = baseMapper.insert(this);
        }
        else
        {
            id = baseMapper.update(this);
        }

        afterSave();

        return id;
    }

    public void updateCol(String colName, Object colValue)
    {
        Core.Q().updateCol(this.getClass(), this.ID(), colName, colValue);
    }

    public void fetch()
    {
        Object latestModel = Core.Q().findById(this.getClass(), this.ID());

        String[] keys = getAttributes();
        for (String key : keys)
        {
            logger.info("fetch set: {} -> {}", key, invokeGet(latestModel, key));
            invokeSet(this, key, invokeGet(latestModel, key));
        }
    }







    @JsonIgnore
    public boolean isNew()
    {
        return this.ID() == null || this.ID() == 0;
    }

    public Map<String, Object> process() {
        return this.process(this, getBasicAttributes());
    }

    public Map<String, Object> process(String[] keys) {
        return this.process(this, keys);
    }

    public Map<String, Object> process(Object model) {
        return this.process(model, getBasicAttributes());
    }

    public Map<String, Object> process(Object model, String[] keys) {
        return Core.processModel(model, keys);
    }
}
