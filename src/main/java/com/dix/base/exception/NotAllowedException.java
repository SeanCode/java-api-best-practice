package com.dix.base.exception;

/**
 * Created by cc on 16/2/28.
 */
public class NotAllowedException extends BaseException {
    public NotAllowedException() {
        super(ERROR_NOT_ALLOWED, "not allowed");
    }

    public NotAllowedException(String message) {
        super(ERROR_NOT_ALLOWED, message);
    }
}
