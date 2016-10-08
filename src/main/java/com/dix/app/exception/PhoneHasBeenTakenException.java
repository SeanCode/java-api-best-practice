package com.dix.app.exception;

/**
 * Created by dd on 1/27/16.
 */
public class PhoneHasBeenTakenException extends AppBaseException {
    public PhoneHasBeenTakenException() {
        super(ERROR_PHONE_HAS_BEEN_TAKEN, "the phone has been taken");
    }
}
