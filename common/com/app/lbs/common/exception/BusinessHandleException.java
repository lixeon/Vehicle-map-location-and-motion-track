/*
 * LBSAPP
 */
package com.app.lbs.common.exception;

/**
 * Handle Business Exception
 *
 * @author luxinxian
 *
 */
public class BusinessHandleException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 599150159978993223L;

    /**
     * Constructor
     *
     */
    public BusinessHandleException() {
        super();
    }
    
    /**
     * Constructor
     *
     * @param msgContent Message Content
     */
    public BusinessHandleException(String msgContent) {
        super(msgContent);
    }
}
