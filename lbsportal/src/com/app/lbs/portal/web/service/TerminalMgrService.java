/*
 * RX-lbspt 
 */
package com.app.lbs.portal.web.service;

import java.util.Map;

import com.app.lbs.common.web.service.WebCommonService;

/**
 * Terminal Manager Service
 *
 * @author colen
 *
 */
public interface TerminalMgrService extends WebCommonService {

    /**
     * check and add a new terminal
     *
     * @param params Parameters
     */
    void checkAndAdd(Map<String, Object> params);


    /**
     * query terminal info list
     *
     * @param params
     * @return
     */
    Map<String, Object> queryTerminalList(Map<String, Object> params);


    /**
     * Get Terminal Info Detail
     *
     * @param params
     * @return
     */
    Map<String, Object> getTerminalInfoDetial(Map<String, Object> params);


    /**
     * delete terminal by terminal serial number
     *
     * @param terminalSerialNo terminal serial number
     */
    void deleteTerminal(String terminalSerialNo);


    /**
     * modify terminal info
     *
     * @param params
     */
    void modifyTerminalInfo(Map<String, Object> params);


    /**
     * Get Terminal Status List
     *
     * @param params
     */
    void getTerminalStatusList(Map<String, Object> params);


    /**
     * Check And Update Settings
     *
     * @param params
     */
    void checkAndUpdateSettings(Map<String, Object> params);


    /**
     * Get Terminal Configuration By ConfigKey
     * @param params
     * @return
     */
    Map<String, Object> getTerminalConfigByKey(Map<String, Object> params);


    /**
     * Get Terminal Count By Org
     *
     * @param params
     * @return
     */
    Map<String, Object> getTerminalCnt(Map<String, Object> params);
    

    /**
     * disconnected terminal
     *
     * @param terminalSerialNo
     */
    void disconnectTerminal(String terminalSerialNo);
}
