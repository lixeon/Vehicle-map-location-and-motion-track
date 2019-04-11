/*
 * LBSAPP
 */
package com.app.lbs.common.remote;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Call Data Entity
 *
 * @author colen
 *
 */
public class CallEntity implements Serializable{

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Action Type
     */
    private String actType = null;
    
    /**
     * Data
     */
    private Map<String, Object> data = null;

    /**
     * get
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        if (data == null) {
            return null;
        }
        return this.data.get(key);
    }
    
    /**
     * getter
     * @return the actType
     */
    public String getActType() {
        return actType;
    }

    /**
     * setter
     * @param actType the actType to set
     */
    public void setActType(String actType) {
        this.actType = actType;
    }

    /**
     * getter
     * @return the data
     */
    public Map<String, Object> getData() {
        return data;
    }

    /**
     * setter
     * @param data the data to set
     */
    public void setData(Map<String, Object> data) {
        this.data = data;
    }


    /**
     * set
     *
     * @param key key
     * @param value value
     */
    public void set(String key, Object value) {
        if (data == null) {
            data = new HashMap<>();
        }
        this.data.put(key, value);
    }

}
