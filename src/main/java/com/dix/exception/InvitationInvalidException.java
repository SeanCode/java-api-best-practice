package com.dix.exception;

/**
 * Created by yangcheng on 16/2/23.
 */
public class InvitationInvalidException extends BaseException {

    public InvitationInvalidException() {
        super(ERROR_INVALID_INVITATION, "invalid invitation");
    }

    public InvitationInvalidException( String message) {
        super(ERROR_INVALID_INVITATION, message);
    }
}
