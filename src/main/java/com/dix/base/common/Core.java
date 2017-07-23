package com.dix.base.common;

import com.dix.base.core.CoreQuery;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by dd on 7/24/16.
 */
@Component
public class Core {

    private static final Logger logger = LoggerFactory.getLogger(Core.class);

    private static ApplicationContext context;
    private static DataSource         dataSource;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void setContext(ApplicationContext context)
    {
        Core.context = context;
    }

    public static void setDataSource(DataSource dataSource)
    {
        Core.dataSource = dataSource;
    }

    public static DataSource getDataSource()
    {
        return Core.dataSource;
    }

    private static boolean isSpringBean(Class type)
    {
        return type.isAnnotationPresent(Component.class);
    }

    public static <T> T getBean(Class<T> type)
    {
        T bean = null;
        if (isSpringBean(type))
        {
            try {
                bean = context.getBean(type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return bean;
    }

    public static Map<String, Object> processModel(Object model) {
        return processModel(model, (String[]) null);
    }

    public static Map<String, Object> processModel(Object model, Class<?> modelClass) {
        String[] keys = null;

        try
        {
            Object modelInstance = modelClass.newInstance();
            Method getKeysMethod = modelClass.getMethod("getBasicAttributes");
            if (getKeysMethod != null)
            {
                Object keysResult = getKeysMethod.invoke(modelInstance);
                if (keysResult instanceof String[]) {
                    keys = (String[]) keysResult;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return processModel(model, keys);
    }

    public static Map<String, Object> processModel(Object model, String[] keys) {
        if (keys == null && model instanceof ModelApiInterface)
        {
            keys = ((ModelApiInterface) model).getBasicAttributes();
        }

        Map map;
        if (model instanceof Map)
        {
            map = (Map) model;
        }
        else
        {
            map = objectMapper.convertValue(model, Map.class);
        }

        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        if (keys != null)
        {
            for (String key : keys)
            {
                if (map.containsKey(key))
                {
                    resultMap.put(key, map.get(key));
                }
            }
        }

        return resultMap;
    }

    public static List<Map> processModelList(List modelList, Class modelClass)
    {
        return processModelList(modelList, modelClass, null, null);
    }

    public static List<Map> processModelList(List modelList, Class modelClass, Object invoker)
    {
        return processModelList(modelList, modelClass, invoker, null);
    }

    public static List<Map> processModelList(List modelList, Class<?> modelClass, Object invoker, String[] keys)
    {
        if (modelList == null || modelClass == null)
        {
            return null;
        }

        Method processModelMethod = null;
        Object[] methodArgs = null;
        try {
            Object modelInstance = modelClass.newInstance();

            if (keys == null)
            {
                Method getKeysMethod = modelClass.getMethod("getBasicAttributes");
                if (getKeysMethod != null)
                {
                    Object keysResult = getKeysMethod.invoke(modelInstance);
                    if (keysResult instanceof String[])
                    {
                        keys = (String[]) keysResult;
                    }
                }
            }

            if (keys == null)
            {
                return null;
            }


            String methodName = null;
            if (invoker instanceof String)
            {
                methodName = (String) invoker;
            }
            else if (invoker instanceof Invoker)
            {
                Invoker ivk = (Invoker) invoker;
                methodName = ivk.getName();
                methodArgs = ivk.getParams();
            }

            if (methodName == null)
            {
                methodName = "processModel";
            }

            if (methodArgs == null)
            {
                processModelMethod = modelClass.getMethod(methodName, Object.class, String[].class);
            }
            else
            {
                processModelMethod = modelClass.getMethod(methodName, Object.class, String[].class, Object[].class);
            }

            if (processModelMethod == null)
            {
                return null;
            }

            ArrayList<Map> resultList = new ArrayList<Map>();
            for (Object model : modelList)
            {
                Object processModelResult = null;
                if (methodArgs == null)
                {
                    processModelResult = processModelMethod.invoke(modelInstance, model, keys);
                }
                else
                {
                    processModelResult = processModelMethod.invoke(modelInstance, model, keys, methodArgs);
                }

                if (processModelResult != null && processModelResult instanceof Map)
                {
                    resultList.add((Map)processModelResult);
                }
            }

            return  resultList;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Map> processModelList(List modelList, ProcessModelInterface processModelInterface, String[] keys)
    {
        ArrayList<Map> resultList = new ArrayList<Map>();
        for (Object model : modelList)
        {
            Map map = processModelInterface.processModel(model, keys);
            if (map != null)
            {
                resultList.add(map);
            }
        }

        return  resultList;
    }

    public static List<Map> processModelList(List modelList, ProcessModelWithArgsInterface processModelWithArgsInterface, Object[] args, String[] keys)
    {
        ArrayList<Map> resultList = new ArrayList<Map>();
        for (Object model : modelList)
        {
            Map map = processModelWithArgsInterface.processModel(model, keys, args);
            if (map != null)
            {
                resultList.add(map);
            }
        }

        return  resultList;
    }

    public static CoreQuery Q()
    {
        return CoreQuery.getInstance();
    }
}
