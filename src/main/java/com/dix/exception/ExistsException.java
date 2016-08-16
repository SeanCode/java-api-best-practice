package com.dix.exception;

/**
 * Created by dayaa on 16/2/2.
 */
public class ExistsException extends BaseException {
    public ExistsException() {
        super(ERROR_EXISTS, "exists");
    }
}
