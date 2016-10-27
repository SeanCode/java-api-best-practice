package com.dix.base.exception;

/**
 * Created by dd on 1/27/16.
 */
public class TokenNotSetException extends BaseException {
    public TokenNotSetException() {
        this("token is not set");
    }

    public TokenNotSetException(String message) {
        super(ERROR_PARAM_NOT_SET, message);
    }
}
