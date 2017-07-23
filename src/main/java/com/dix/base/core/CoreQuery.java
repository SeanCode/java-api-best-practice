package com.dix.base.core;

import com.dix.app.model.User;
import com.dix.base.common.Core;
import com.dix.base.common.CoreConfig;
import com.dix.base.exception.BaseException;
import com.dix.base.exception.NotExistsException;
import com.dix.base.model.Model;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.jooq.impl.DSL.field;

/**
 * Created by dd on 21/07/2017.
 */
public class CoreQuery {

    private static Logger logger = LoggerFactory.getLogger(CoreQuery.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static volatile CoreQuery instance;
    public static CoreQuery getInstance() {
        if (instance == null) {
            synchronized (CoreQuery.class) {
                if (instance == null) {
                    instance = new CoreQuery();
                }
            }
        }
        return instance;
    }

    private DSLContext getDSLContext()
    {
        return DSL.using(Core.getDataSource(), SQLDialect.MYSQL);
    }

    private String getTableName(Class<?> modelClass) {

        Model model = modelClass.getAnnotation(Model.class);

        if (model == null) {
            throw new BaseException(-1, "" + modelClass.toString() + " must have annotation: Model");
        }

        return model.tableName();
    }

    public <T> T findById(Class<T> c, Long id) {
        return findByCol(c,"id", id);
    }

    public <T> T findByCol(Class<T> c, String colName, Object colValue) {
        DSLContext dsl = getDSLContext();

        Result<Record> result = dsl.select()
                .from(getTableName(c))
                .where(field(colName).eq(colValue))
                .fetch();

        List<Map<String, Object>> maps = result.intoMaps();
        if (maps.size() == 0)
        {
            return null;
        }

        Map<String, Object> map = maps.get(0);
        return objectMapper.convertValue(map, c);
    }

    public <T> T findOrFail(Class<T> c, Long id) {
        T map = findById(c, id);
        if (map == null) {
            throw new NotExistsException();
        }

        return map;
    }

    public <T> SelectQuery<Record> createQuery(Class<T> c) {
        DSLContext dsl = getDSLContext();
        return dsl.select().from(getTableName(c)).getQuery();
    }

    public <T> SelectQuery<Record> createQueryByPage(Class<T> c, int limit, int offset) {
        SelectQuery<Record> query = createQuery(c);
        query.addLimit(limit);
        query.addOffset(offset);
        return query;
    }

    public List<Map<String, Object>> executeQuery(SelectQuery<Record> query) {
        DSLContext dsl = getDSLContext();

        if (query == null) {
            throw new BaseException(-1, "query must no be null");
        }

        Result<Record> result = query.fetch();
        List<Map<String, Object>> maps = result.intoMaps();
        return maps;
    }





    public Map<String, Object> getRawById(Class<?> modelClass, Long id) {
        return getRawByCol(modelClass, "id", id);
    }

    public Map<String, Object> getRawOrFail(Class<?> modelClass, Long id) {
        Map<String, Object> map = getRawById(modelClass, id);
        if (map == null) {
            throw new NotExistsException();
        }

        return map;
    }

    public Map<String, Object> getRawByCol(Class<?> modelClass, String colName, Object colValue) {
        DSLContext dsl = getDSLContext();

        Result<Record> result = dsl.select()
                .from(getTableName(modelClass))
                .where(field(colName).eq(colValue))
                .fetch();

        List<Map<String, Object>> maps = result.intoMaps();
        if (maps.size() == 0)
        {
            return null;
        }

        return maps.get(0);
    }
}
