package com.dix.base.model;

import com.dix.app.model.Token;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by dd on 21/07/2017.
 */
public interface BaseMapper<T> {
    Long insert(T model);

    Long update(T model);
}
