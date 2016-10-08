package com.dix.app.exception;

/**
 * Created by cc on 16/2/26.
 */
public class OrgNoDefaultException extends AppBaseException {

    public OrgNoDefaultException() {
        super(ERROR_ORG_NO_DEFAULT, "no default org");
    }

}
