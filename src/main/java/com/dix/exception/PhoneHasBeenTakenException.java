package com.dix.exception;

/**
 * Created by dd on 1/27/16.
 */
public class PhoneHasBeenTakenException extends BaseException {
    public PhoneHasBeenTakenException() {
        super(ERROR_PHONE_HAS_BEEN_TAKEN, "the phone has been taken");
    }
}
