package com.dix.app.exception;

/**
 * Created by dd on 1/27/16.
 */
public class UserNotExistsException extends AppBaseException {
    public UserNotExistsException() {
        super(ERROR_USER_NOT_EXISTS, "user not exists");
    }
}
