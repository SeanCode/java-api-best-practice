package com.dix.app.exception;

import com.dix.base.exception.BaseException;

/**
 * Created by dd on 9/8/15.
 */
public class AppBaseException extends BaseException
{
    public static final int ERROR_ORG_NOT_EXISTS = 101;
    public static final int ERROR_ORG_MEMBER_NOT_EXISTS = 102;

    public static final int ERROR_REGISTER = 103;
    public static final int ERROR_USER_NOT_EXISTS = 104;
    public static final int ERROR_PHONE_HAS_BEEN_TAKEN = 105;
    public static final int ERROR_BIND_USER_BIND_EXISTS = 106;
    public static final int ERROR_CREATE_INVITATION = 107;
    public static final int ERROR_INVALID_INVITATION = 108;
    public static final int ERROR_ORG_NO_DEFAULT = 109;
    public static final int ERROR_NOT_ALLOWED = 110;

    public static final int ERROR_SAVE_ERROR = 111;
    public static final int ERROR_NOT_PERMITTED = 112;
    public static final int ERROR_CREATE = 113;
    public static final int ERROR_CREATE_ORG_EXIST = 114;
    public static final int ERROR_ORG_MEMBER_EXISTS = 115;

    public AppBaseException(int code, String message)
    {
        super(code, message);
    }
}
