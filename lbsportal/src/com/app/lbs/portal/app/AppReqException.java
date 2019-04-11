/*
 * lbsgw
 */
package com.app.lbs.portal.app;

/**
 * App Request Handle Exception
 *
 * @author colen
 *
 */
public class AppReqException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * constructor
     *
     * @param message error message
     */
    public AppReqException(String message) {
        super(message);
    }
    
    /**
     * constructor
     *
     * @param cause error message
     */
    public AppReqException(Throwable cause) {
        super(cause);
    }
    
    /**
     * constructor
     *
     * @param message error message
     * @param cause error message
     */
    public AppReqException(String message, Throwable cause) {
        super(message, cause);
    }
}
