/*
 * LBSAPP
 */
package com.app.lbs.common.exception;

/**
 * Data Format Exception
 *
 * @author luxinxian
 *
 */
public class DataFormatException extends Exception {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 599150159978993223L;

    /**
     * Constructor
     *
     */
    public DataFormatException() {
        super();
    }
    
    /**
     * Constructor
     *
     * @param msgContent Message Content
     */
    public DataFormatException(String msgContent) {
        super(msgContent);
    }
}
