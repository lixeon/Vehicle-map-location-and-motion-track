/*
 * Copyleft 2011 Power by colen.
 *
 * Project: booking
 * Date: Jul 2, 2011
 */
package com.app.platform.base.exception;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Bussiness Exception
 *
 * @author colen
 *
 */
public class BaseBusinessException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 8145444765839215973L;
    
    /** invoke call back  */
    protected static final String CALL_CB_Y = "1";
    /** don't invoke call back  */
    protected static final String CALL_CB_N = "0";

    /** Message Container  */
    private static final MessageContainer MESSAGE_CONTAINER = new MessageContainer();

    /** Message Id */
    private List<String> msgIdList = new LinkedList<String>();
    /** Message Arguments */
    private List<Object[]> msgArgsList = new LinkedList<Object[]>();
    /** Message Content */
    private List<String> msgContentList = new LinkedList<String>();
    /** Output Data */
    private Map<String, Object> outData = null;
    /** Execute Error Call Back Exception */
    private String excErrorCallback = CALL_CB_Y;

    /**
     * Initialize Business exception message container.
     *
     * @param props
     */
    @SuppressWarnings("rawtypes")
    public static void init(Map props) {
        MESSAGE_CONTAINER.init(props);
    }

    /** Default Constructor. */
    public BaseBusinessException() {}

    /**
     * Constructor no Id, but Contents.
     *
     * @param msgContent Message Contents.
     */
    public BaseBusinessException(String msgContent) {
        super();
        addItem(msgContent);
    }

    /**
     * Constructor
     *
     * @param msgId Message Id
     * @param msgArgs Message Arguments
     */
    public BaseBusinessException(String msgId, Object... msgArgs) {
        super();
        addItem(msgId, msgArgs);
    }

    /**
     * Add Exception Item.
     *
     * @param msgId Message Id
     * @param msgArgs Message Arguments
     */
    public void addItem(String msgId, Object... msgArgs) {
        this.msgIdList.add(msgId);
        this.msgArgsList.add(msgArgs);
        this.msgContentList.add(MESSAGE_CONTAINER.getMsgContent(msgId, msgArgs));
    }

    /**
     * Add Exception Item.
     *
     * @param msgContent Message Arguments
     */
    public void addItem(String msgContent) {
        this.msgIdList.add("default");
        this.msgContentList.add(msgContent);
    }

    /**
     * Getter msgIdList
     * @return the msgIdList
     */
    public List<String> getMsgIdList() {
        return msgIdList;
    }


    /**
     * Getter msgArgsList
     * @return the msgArgsList
     */
    public List<Object[]> getMsgArgsList() {
        return msgArgsList;
    }


    /**
     * Getter msgContentList
     * @return the msgContentList
     */
    public List<String> getMsgContentList() {
        return msgContentList;
    }

    /**
     * @return the outData
     */
    public Map<String, Object> getOutData() {
        return outData;
    }

    /**
     * @param outData the outData to set
     */
    public void setOutData(Map<String, Object> outData) {
        this.outData = outData;
    }

    /**
     * @return the excErrorCallback
     */
    public String getExcErrorCallback() {
        return excErrorCallback;
    }

    /**
     * @param excErrorCallback the excErrorCallback to set
     */
    public void setExcErrorCallback(String excErrorCallback) {
        this.excErrorCallback = excErrorCallback;
    }
    

    /**
     * Message Container
     *
     *
     * @author colen
     *
     */
    private static class MessageContainer {

        /** message real container */
        private Map<String, String> container = new HashMap<String, String>();
        
        /**
         * initialize
         * @param props
         */
        @SuppressWarnings({ "unchecked", "rawtypes" })
        public void init(Map props) {
            this.container.putAll(props);
        }

        /**
         * get message content
         *
         * @param msgId message id
         * @param args arguments
         * @return full message
         */
        public String getMsgContent(String msgId, Object... args) {
            String msgbody = this.container.get(msgId);
            if (msgbody == null || "".equals(msgbody.trim())) {
                return null;
            }
            return msgId + "：" + MessageFormat.format(msgbody, args);
        }
    }
}
