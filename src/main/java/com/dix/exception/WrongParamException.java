package com.dix.exception;


/**
 * Created by dd on 1/27/16.
 */
public class WrongParamException extends BaseException {
    public WrongParamException(String param) {
        super(ERROR_WRONG_PARAM,  String.format("wrong param: %s", param));
    }
}
