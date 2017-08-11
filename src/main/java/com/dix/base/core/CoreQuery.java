package com.dix.base.core;

import com.dix.base.exception.BaseException;
import com.dix.base.exception.NotExistsException;
import com.dix.base.model.Model;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.*;

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
                .where(field(colName).eq(colValue)
                        .and(field("weight").greaterOrEqual(0)))
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

    public <T> void updateCol(Class<T> c, Long id, String colName, Object colValue) {
        DSLContext dsl = getDSLContext();

        logger.info("update col: {} -> {}, id: {}", colName, colValue, id);

        dsl.update(table(getTableName(c)))
                .set(field(colName), colValue)
                .where(field("id").eq(id))
                .execute();
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

    public int count(SelectQuery<Record> query) {
        DSLContext dsl = getDSLContext();
        return dsl.fetchCount(query);
    }

    public <T> T queryOne(Class<T> c, SelectQuery<Record> query) {
        Map<String, Object> map = queryOne(query);
        return objectMapper.convertValue(map, c);
    }

    public Map<String, Object> queryOne(SelectQuery<Record> query) {
        List<Map<String, Object>> maps = executeQuery(query);
        if (maps.size() == 0)
        {
            return null;
        }

        return maps.get(0);
    }

    public List<Map<String, Object>> queryAll(SelectQuery<Record> query) {
        return executeQuery(query);
    }

    public <T> List<T> queryAll(Class<T> c, SelectQuery<Record> query) {
        List<Map<String, Object>> maps = executeQuery(query);
        return maps.stream().map(m -> objectMapper.convertValue(m, c)).collect(Collectors.toList());
    }

    public Object queryCol(SelectQuery<Record> query, String colName) {
        Map<String, Object> map = queryOne(query);
        if (map.containsKey(colName)) {
            return map.get(colName);
        }

        return null;
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
