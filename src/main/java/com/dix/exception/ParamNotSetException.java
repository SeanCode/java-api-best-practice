package com.dix.exception;


/**
 * Created by dd on 1/27/16.
 */
public class ParamNotSetException extends BaseException {
    public ParamNotSetException(String param) {
        super(ERROR_PARAM_NOT_SET,  String.format("param not set: %s", param));
    }
}
