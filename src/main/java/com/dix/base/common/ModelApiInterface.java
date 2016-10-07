package com.dix.base.common;

import javax.persistence.Transient;
import java.util.Map;

/**
 * Created by dd on 7/24/16.
 */
public interface ModelApiInterface {

    // select concat('"', COLUMN_NAME ,'", ') from information_schema.COLUMNS where TABLE_SCHEMA = "DB" and TABLE_NAME = "TABLE"

    @Transient
    String[] getAttributes();

    @Transient
    String[] getBasicAttributes();

    @Transient
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
