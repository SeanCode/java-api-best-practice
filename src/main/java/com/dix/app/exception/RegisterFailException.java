package com.dix.app.exception;

/**
 * Created by dd on 1/27/16.
 */
public class RegisterFailException extends AppBaseException {
    public RegisterFailException() {
        super(ERROR_REGISTER, "register fail");
    }

    public RegisterFailException(String message) {
        super(ERROR_REGISTER, "register fail: " + message);
    }
}
