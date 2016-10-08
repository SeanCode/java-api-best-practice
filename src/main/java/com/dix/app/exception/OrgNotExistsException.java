package com.dix.app.exception;

/**
 * Created by dayaa on 16/2/1.
 */
public class OrgNotExistsException extends AppBaseException {
    public OrgNotExistsException() {
        super(ERROR_ORG_NOT_EXISTS, "org not exists");
    }

    public OrgNotExistsException(String message) {
        super(ERROR_ORG_NOT_EXISTS, "org not exists: " + message);
    }
}