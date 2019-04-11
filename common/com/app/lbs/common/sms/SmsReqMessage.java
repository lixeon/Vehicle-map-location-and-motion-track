/*
 * lbspt
 */
package com.app.lbs.common.sms;

import java.util.HashMap;
import java.util.Map;

/**
 * SMS Request Message
 *
 * @author colen
 *
 */
public class SmsReqMessage {

    private String msgId = null;
    private String content = null;
    private String msgType = null;
    // YYYYMMDDHHmmSS
    private String sendTime = null;

    private String from = null;
    private String to = null;

    private Map<String, Object> properties = null;
    
    /**
     * Constructor
     */
    public SmsReqMessage() {
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
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the sendTime
     */
    public String getSendTime() {
        return sendTime;
    }

    /**
     * @param sendTime the sendTime to set
     */
    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * @return the to
     */
    public String getTo() {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * @return the msgType
     */
    public String getMsgType() {
        return msgType;
    }

    /**
     * @param msgType the msgType to set
     */
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    
}
