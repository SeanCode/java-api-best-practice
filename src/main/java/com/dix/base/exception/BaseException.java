package com.dix.base.exception;

/**
 * Created by dd on 9/8/15.
 */
public class BaseException extends RuntimeException
{
    public static final int ERROR_404 = -404;
    public static final int ERROR_500 = -500;
    public static final int ERROR = -1;
    public static final int ERROR_IN_INTERCEPTOR = -2;

    public static final int ERROR_PARAM_NOT_SET = 1;
    public static final int ERROR_TOKEN_INVALID = 2;
    public static final int ERROR_LOGIN = 3;
    public static final int ERROR_WRONG_PARAM = 4;
    public static final int ERROR_NOT_EXISTS = 5;
    public static final int ERROR_EXISTS = 6;

    public static final int ERROR_ORG_NOT_EXISTS = 7;
    public static final int ERROR_ORG_MEMBER_NOT_EXISTS = 8;

    public static final int ERROR_REGISTER = 9;
    public static final int ERROR_USER_NOT_EXISTS = 10;
    public static final int ERROR_PHONE_HAS_BEEN_TAKEN = 11;
    public static final int ERROR_BIND_USER_BIND_EXISTS = 12;
    public static final int ERROR_CREATE_INVITATION = 13;
    public static final int ERROR_INVALID_INVITATION = 14;
    public static final int ERROR_ORG_NO_DEFAULT = 15;
    public static final int ERROR_NOT_ALLOWED = 16;

    public static final int ERROR_SAVE_ERROR = 1;
    public static final int ERROR_NOT_PERMITTED = 1;
    public static final int ERROR_CREATE = 1;
    public static final int ERROR_CREATE_ORG_EXIST = 1;
    public static final int ERROR_ORG_MEMBER_EXISTS = 1;

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public BaseException(int code, String message)
    {
        super("" + code + ": " + message);
        this.code = code;
        this.message = message;
    }
}
