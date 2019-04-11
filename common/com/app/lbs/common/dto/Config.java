/*
 * LBSAPP
 */
package com.app.lbs.common.dto;

import java.io.Serializable;

/**
 * Configuration
 *
 * @author colen
 *
 */
public class Config implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    public static final int SYNC = 1;
    public static final int UNSYNC = 0;
    
    public String terminalSerialNo = null;
    public String configKey = null;
    public String value = null;
    public int status = 0;
    
    public Config copy() {
        Config copy = new Config();
        copy.terminalSerialNo = this.terminalSerialNo;
        copy.configKey = this.configKey;
        copy.value = this.value;
        copy.status = this.status;
        return copy;
    }
}
