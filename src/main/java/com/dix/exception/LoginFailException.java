package com.dix.exception;

/**
 * Created by dd on 1/27/16.
 */
public class LoginFailException extends BaseException {
    public LoginFailException() {
        super(ERROR_LOGIN, "login fail");
    }

    public LoginFailException(String message) {
        super(ERROR_LOGIN, message);
    }
}
