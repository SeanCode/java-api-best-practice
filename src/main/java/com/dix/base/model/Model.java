package com.dix.base.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by dd on 21/07/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Model {
    String tableName();
    Class<? extends BaseMapper> mapper();
}
