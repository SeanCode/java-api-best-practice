package com.dix.base.exception;

/**
 * Created by dd on 1/27/16.
 */
public class TokenInvalidException extends BaseException {
    public TokenInvalidException() {
        this("token is invalid");
    }

    public TokenInvalidException(String message) {
        super(ERROR_TOKEN_INVALID, message);
    }
}
