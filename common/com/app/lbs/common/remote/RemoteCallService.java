/*
 * LBSAPP
 */
package com.app.lbs.common.remote;

/**
 * Remote Call Service
 *
 * @author colen
 *
 */
public interface RemoteCallService {

    /**
     * Call
     *
     * @param request Call Request
     * @return Call Response
     */
    CallResponse call(CallRequest request);

}
