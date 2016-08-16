package com.dix.exception;

/**
 * Created by dd on 1/27/16.
 */
public class UserNotExistsException extends BaseException {
    public UserNotExistsException() {
        super(ERROR_USER_NOT_EXISTS, "user not exists");
    }
}
