/*
 * LBSAPP
 */
package com.app.lbs.portal.web.service;

import java.util.Map;

import com.app.lbs.common.web.service.WebCommonService;

/**
 * System Set Service
 *
 * @author colen
 *
 */
public interface SystemSetService extends WebCommonService {
    
    String CATEGORY = "sms.ms.check";
    String CID_FEE = "sms.ms.check.fee";
    String CID_LOWLEVEL = "sms.ms.check.lowlevel";
    String CID_REMINDER = "sms.ms.check.reminder";


    /**
     * Get Current SMS Settings
     *
     * @return
     */
    Map<String, Map<String, Object>> getCurSmsSetting();

    
    /**
     * Update Current SMS Settings
     *
     * @param params
     */
    void updateSmsSettings(Map<String, Object> params);

}
