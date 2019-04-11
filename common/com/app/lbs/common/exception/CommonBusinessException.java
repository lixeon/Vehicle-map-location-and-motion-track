/*
 * Copyleft 2012 Power by colen.
 *
 * Project: app-crm
 * Date: Apr 22, 2012
 */
package com.app.lbs.common.exception;

import com.app.platform.base.exception.BaseBusinessException;

/**
 * Common business Exception
 *
 * @author colen
 *
 */
public class CommonBusinessException extends BaseBusinessException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /** Default Constructor. */
    public CommonBusinessException() {}

    /**
     * Constructor no Id, but Contents.
     *
     * @param msgContent Message Contents.
     */
    public CommonBusinessException(String msgContent) {
        super(msgContent);
    }

    /**
     * Constructor
     *
     * @param msgId Message Id
     * @param msgArgs Message Arguments
     */
    public CommonBusinessException(String msgId, Object... msgArgs) {
        super(msgId, msgArgs);
    }
}
