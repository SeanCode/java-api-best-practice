package com.dix.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dd on 9/7/15.
 */

public class DataResponse {
    private int code;
    private Map<String, Object> data;

    public DataResponse()
    {
        this(0);
    }

    public DataResponse(int code)
    {
        this.code = code;
        this.data = new HashMap<>();
    }

    public static DataResponse create()
    {
        return new DataResponse();
    }

    public DataResponse put(String key, Object object)
    {
        return set(key, object);
    }

    public DataResponse set(String key, Object object)
    {
        this.data.put(key, object);
        return this;
    }

    public int getCode() {
        return code;
    }

    public DataResponse setCode(int code)
    {
        this.code = code;
        return this;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Map<String, Object> getData() {
        return data;
    }


}
