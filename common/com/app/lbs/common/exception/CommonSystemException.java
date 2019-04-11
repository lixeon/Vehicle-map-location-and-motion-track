/*
 * Copyleft 2012 Power by colen.
 *
 * Project: app-crm
 * Date: Apr 22, 2012
 */
package com.app.lbs.common.exception;

import com.app.platform.base.exception.BaseSystemException;

/**
 * Common System Exception
 *
 * @author colen
 *
 */
public class CommonSystemException extends BaseSystemException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;


    /**
     * Constructor
     *
     */
    public CommonSystemException() {
        super();
    }

    /**
     * Constructor
     *
     * @param message Message
     */
    public CommonSystemException(String message) {
        super(message);
    }

    /**
     * Constructor
     *
     * @param t Cause
     */
    public CommonSystemException(Throwable t) {
        super(t);
    }

    /**
     * Constructor
     *
     * @param message Message
     * @param t Cause
     */
    public CommonSystemException(String message, Throwable t) {
        super(message, t);
    }
}
