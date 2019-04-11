/*
 * Copyleft 2012 Power by colen.
 *
 * Project: app-crm
 * Date: May 20, 2012
 */
package com.app.lbs.common.web.service;

import java.util.List;
import java.util.Map;

/**
 * Web Widget Service
 *
 * @author colen
 *
 */
public interface WebWidgetService {

    /**
     * get menus
     * 
     * @param params Arguments
     * @return List
     */
    List<Map<String, Object>> queryMenus(Map<String, Object> params);

    
    /**
     * load Organization List
     *
     * @return List
     */
    List<Map<String, Object>> loadOrgList(Map<String, Object> params);


}
