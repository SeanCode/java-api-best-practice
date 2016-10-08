package com.dix.app.exception;

/**
 * Created by cc on 16/2/23.
 */
public class CreateInvitationException extends AppBaseException {

    public CreateInvitationException() {
        super(ERROR_CREATE_INVITATION, "create invitation fail");
    }

    public CreateInvitationException(String message) {
        super(ERROR_CREATE_INVITATION, "create invitation fail: " + message);
    }

}
