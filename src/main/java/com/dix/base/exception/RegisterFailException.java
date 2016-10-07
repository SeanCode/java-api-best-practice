package com.dix.base.exception;

/**
 * Created by dd on 1/27/16.
 */
public class RegisterFailException extends BaseException {
    public RegisterFailException() {
        super(ERROR_REGISTER, "register fail");
    }

    public RegisterFailException(String message) {
        super(ERROR_REGISTER, "register fail: " + message);
    }
}
