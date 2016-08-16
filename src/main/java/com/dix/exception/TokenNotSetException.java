package com.dix.exception;

/**
 * Created by dd on 1/27/16.
 */
public class TokenNotSetException extends BaseException {
    public TokenNotSetException() {
        super(ERROR_PARAM_NOT_SET, "token is not set");
    }
}
