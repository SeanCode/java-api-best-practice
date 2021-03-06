package com.dix.app.exception;

/**
 * Created by dayaa on 16/2/2.
 */
public class OrgMemberNotExistsException extends AppBaseException {

    public OrgMemberNotExistsException() {
        super(ERROR_ORG_MEMBER_NOT_EXISTS, "org member not exists");
    }

    public OrgMemberNotExistsException(String message) {
        super(ERROR_ORG_MEMBER_NOT_EXISTS, "org member not exists: " + message);
    }
}
