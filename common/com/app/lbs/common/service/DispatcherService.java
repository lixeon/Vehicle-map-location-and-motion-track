/*
 * LBSAPP
 */
package com.app.lbs.common.service;

import java.util.Map;

/**
 * Dispatcher Service
 *
 * @author luxinxian
 *
 */
public interface DispatcherService {

    /**
     * dispatch Action
     *
     * @param reqObj Parameters 
     * @return
     */
    Map<String, Object> dispatch(Map<String, Object> params);

}
