package com.dix.base.common;

import com.dix.app.model.User;

import java.util.Map;

/**
 * Created by dd on 7/24/16.
 */
public interface ModelApiInterface {

    // select concat('"', COLUMN_NAME ,'", ') from information_schema.COLUMNS where TABLE_SCHEMA = "DB" and TABLE_NAME = "TABLE"

//    String getTableName();

    String[] getAttributes();

    String[] getBasicAttributes();

    String[] getDetailAttributes();

    default Map<String, Object> processModel() {
        return this.processModel(this, null);
    }

    default Map<String, Object> processModel(String[] keys) {
        return this.processModel(this, keys);
    }

    default Map<String, Object> processModel(Object model) {
        return this.processModel(model, null);
    }

    Map<String, Object> processModel(Object model, String[] keys);

}
