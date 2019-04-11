/*
 * lbspt
 */
package com.app.lbs.portal.web.service;

import java.util.Map;

import com.app.lbs.common.web.service.WebCommonService;

/**
 * Platform Portal Service
 *
 * @author colen
 *
 */
public interface TerminalPortalService extends WebCommonService {

    /**
     * get city info
     *
     * @param params Parameters
     */
    void getCityInfo(Map<String, Object> params);

    /**
     * get terminal info by terminal id
     *
     * @param params Parameters
     */
    void getTerminalInfoByTmnId(Map<String, Object> params);

    
    /**
     * get latest trace infos
     *
     * @param params Parameters
     */
    void getLatestTrace(Map<String, Object> params);

    
    /**
     * query history locations
     *
     * @param params
     */
    void getHistoryLocations(Map<String, Object> params);

    /**
     * get month data hint
     *
     * @param params
     */
    void getMonthDataHint(Map<String, Object> params);

    /**
     * get date data hint
     *
     * @param params
     */
    void getDateDataHint(Map<String, Object> params);

  	void modifyTerminalName(Map<String, Object> paramMap);
}
