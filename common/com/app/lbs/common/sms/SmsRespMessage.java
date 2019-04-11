/*
 * lbspt
 */
package com.app.lbs.common.sms;

import java.util.HashMap;
import java.util.Map;

/**
 * SMS Response Message
 *
 * @author colen
 *
 */
public class SmsRespMessage {

    private String msgId = null;
    // respCode = 900: SMS Request Message Error.
    private String respCode = null;
    private int feeCount = 0;
    private int numCount = 0;

    private Map<String, Object> properties = null;

    /**
     * Constructor
     */
    public SmsRespMessage() {
        this.properties = new HashMap<String, Object>();
    };
    
    public Object get(String key) {
        return this.properties.get(key);
    }
    
    public void set(String key, Object value) {
        this.properties.put(key, value);
    }
    
    /**
     * @return the msgId
     */
    public String getMsgId() {
        return msgId;
    }

    /**
     * @param msgId the msgId to set
     */
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    /**
     * @return the respCode
     */
    public String getRespCode() {
        return respCode;
    }

    /**
     * @param respCode the respCode to set
     */
    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    /**
     * @return the feeCount
     */
    public int getFeeCount() {
        return feeCount;
    }

    /**
     * @param feeCount the feeCount to set
     */
    public void setFeeCount(int feeCount) {
        this.feeCount = feeCount;
    }

    /**
     * @return the numCount
     */
    public int getNumCount() {
        return numCount;
    }

    /**
     * @param numCount the numCount to set
     */
    public void setNumCount(int numCount) {
        this.numCount = numCount;
    }
}
