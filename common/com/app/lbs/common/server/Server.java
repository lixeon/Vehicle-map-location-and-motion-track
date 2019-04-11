/*
 * lbsgw
 */
package com.app.lbs.common.server;

import java.util.Map;

/**
 * Server
 *
 * @author colen
 *
 */
public interface Server {

    /**
     * Start Server
     *
     * @param args Arguments
     */
    void start(Map<String, Object> args);


    /**
     * Stop Server
     *
     * @param args Arguments
     */
    void stop(Map<String, Object> args);

}
