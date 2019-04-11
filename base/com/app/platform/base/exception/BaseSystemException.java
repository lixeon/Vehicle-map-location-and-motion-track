/*
 * Copyleft 2011 Power by colen.
 *
 * Project: booking
 * Date: Jul 2, 2011
 */
package com.app.platform.base.exception;

/**
 * System Exception
 *
 * @author colen
 *
 */
public class BaseSystemException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1693654684745509670L;

    /**
     * Constructor
     *
     */
    public BaseSystemException() {
        super();
    }

    /**
     * Constructor
     *
     * @param message Message
     */
    public BaseSystemException(String message) {
        super(message);
    }

    /**
     * Constructor
     *
     * @param t Cause
     */
    public BaseSystemException(Throwable t) {
        super(t);
    }

    /**
     * Constructor
     *
     * @param message Message
     * @param t Cause
     */
    public BaseSystemException(String message, Throwable t) {
        super(message, t);
    }
}
