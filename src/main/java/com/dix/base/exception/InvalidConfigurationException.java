package com.dix.base.exception;


/**
 * Created by dd on 1/27/16.
 */
public class InvalidConfigurationException extends BaseException {
    public InvalidConfigurationException(String param) {
        super(ERROR_INVALID_CONFIGURATION,  String.format("wrong param: %s", param));
    }
}
