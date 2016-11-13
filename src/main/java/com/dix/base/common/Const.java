package com.dix.base.common;

/**
 * Created by dd on 1/28/16.
 */
public class Const
{
    public static final String REDIS_KEY_PREFIX = "dix.api";

    public static final int WEIGHT_NORMAL = 0;
    public static final int WEIGHT_DELETED = -1;
    public static final int PAGE_LIMIT = 20;

    public static final String USER_AUTH_KEY_USER_ID = "user_id";
    public static final String USER_AUTH_KEY_USER_TYPE = "user_type";

    public static String getKeyOfUserUidLock(String uid)
    {
        return String.format("%s.user.uid.lock.%s", REDIS_KEY_PREFIX, uid);
    }

    public static String getKeyOfInvitationTokenLock(String token)
    {
        return String.format("%s.invitation.token.lock.%s", REDIS_KEY_PREFIX, token);
    }

}
