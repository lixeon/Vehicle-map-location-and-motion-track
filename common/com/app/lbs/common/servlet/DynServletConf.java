/*
 * lbsgw
 */
package com.app.lbs.common.servlet;

/**
 * Dynamic Servlet Configuration
 *
 * @author colen
 *
 */
public interface DynServletConf {

    /**
     * Get Name
     *
     * @return
     */
    String getName();

    /**
     * get mapping
     *
     * @return
     */
    String getMapping();
}
