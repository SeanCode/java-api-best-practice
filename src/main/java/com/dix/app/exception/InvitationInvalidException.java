package com.dix.app.exception;

/**
 * Created by yangcheng on 16/2/23.
 */
public class InvitationInvalidException extends AppBaseException {

    public InvitationInvalidException() {
        super(ERROR_INVALID_INVITATION, "invalid invitation");
    }

    public InvitationInvalidException( String message) {
        super(ERROR_INVALID_INVITATION, message);
    }
}
