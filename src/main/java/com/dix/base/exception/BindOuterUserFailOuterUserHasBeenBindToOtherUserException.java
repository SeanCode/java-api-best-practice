package com.dix.base.exception;

/**
 * Created by dd on 1/27/16.
 */
public class BindOuterUserFailOuterUserHasBeenBindToOtherUserException extends BaseException {
    public BindOuterUserFailOuterUserHasBeenBindToOtherUserException() {
        super(ERROR_BIND_USER_BIND_EXISTS, "outer user has been bind to other user");
    }

    public BindOuterUserFailOuterUserHasBeenBindToOtherUserException(String message) {
        super(ERROR_BIND_USER_BIND_EXISTS, message);
    }
}
