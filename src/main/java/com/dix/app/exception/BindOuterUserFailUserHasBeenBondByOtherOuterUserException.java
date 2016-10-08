package com.dix.app.exception;

/**
 * Created by dd on 1/27/16.
 */
public class BindOuterUserFailUserHasBeenBondByOtherOuterUserException extends AppBaseException {
    public BindOuterUserFailUserHasBeenBondByOtherOuterUserException() {
        super(ERROR_BIND_USER_BIND_EXISTS, "user has been bind by other outer user");
    }

    public BindOuterUserFailUserHasBeenBondByOtherOuterUserException(String message) {
        super(ERROR_BIND_USER_BIND_EXISTS, message);
    }
}
