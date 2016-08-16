package com.dix.exception;

/**
 * Created by dayaa on 16/2/2.
 */
public class NotExistsException extends BaseException {

    public NotExistsException() {
        super(ERROR_NOT_EXISTS, "not exists");
    }

    public NotExistsException(String message) {
        super(ERROR_NOT_EXISTS, message);
    }
}
