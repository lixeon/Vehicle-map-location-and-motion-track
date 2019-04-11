/*
 * lbspt
 */
package com.app.lbs.portal.web.service;

import java.util.Map;

import com.app.lbs.common.web.service.WebCommonService;

/**
 * User Login and Logout Service
 *
 * @author colen
 *
 */
public interface LoginoutService extends WebCommonService {

    /**
     * do login action
     * check user
     *
     * @param params Parameters
     */
    void doLoginCheck(Map<String, Object> params);


    /**
     * do modify password
     *
     * @param params Parameters
     */
    void doModifyPwd(Map<String, Object> params);


    /**
     * do modify terminal password
     *
     * @param params
     */
    void doModifyTerminalPwd(Map<String, Object> params);

}
