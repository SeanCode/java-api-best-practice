package com.dix.common;

import java.util.regex.Pattern;

/**
 * Created by dd on 9/8/15.
 */
public class Config {
    public static final Pattern[] PATH_GUEST_CAN_ACCESS_PATTERN = new Pattern[]{
            Pattern.compile("^/client/1/user/(login|register|password-reset)"),
            Pattern.compile("^/test/.*")
    };
}
