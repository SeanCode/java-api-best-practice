package com.dix.base.model;

import com.dix.app.mapper.UserMapper;
import com.dix.base.common.Core;
import com.dix.base.exception.BaseException;
import com.dix.base.exception.NotExistsException;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import static org.jooq.impl.DSL.*;

import java.util.List;
import java.util.Map;

/**
 * Created by dd on 21/07/2017.
 */
public abstract class BaseModel<T> {

    public abstract Long getId();

    protected boolean beforeSave()
    {
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

        if (!beforeSave()) {
            throw new BaseException(-1, getClass().toString() + " beforeSave return false");
        }


        BaseMapper baseMapper = Core.getBean(model.mapper());

        Long id = 0L;
        if (this.getId() == null || this.getId() == 0L) {
             id = baseMapper.insert(this);
        }
        else
        {
            id = baseMapper.update(this);
        }

        afterSave();

        return id;
    }
}
