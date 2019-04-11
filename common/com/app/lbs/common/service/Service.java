/*
 * lbsgw
 */
package com.app.lbs.common.service;


/**
 * Service
 *
 * @author colen
 *
 */
public interface Service<T, R> {

    /**
     * Service
     *
     * @param params Parameters
     */
    R service(T params);

    /**
     * get service type
     *
     * @return service type
     */
    String serviceModule();

    /**
     * get service key
     *
     * @return service type
     */
    String serviceKey();
}
