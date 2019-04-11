/*
 * LBSAPP
 */
package com.app.lbs.portal.web.service;

import java.util.Map;

/**
 * Terminal Alarm Service
 *
 * @author colen
 *
 */
public interface TerminalAlarmService {

    /**
     * query alarm list
     *
     * @param params
     */
    void queryAlarmList(Map<String, Object> params);

    /**
     * Update Notify Status
     *
     * @param params
     */
    void updateNotifyStatus(Map<String, Object> params);

}
