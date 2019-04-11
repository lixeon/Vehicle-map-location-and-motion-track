/*
 * Copyleft 2012 Power by colen.
 *
 * Project: app-crm
 * Date: May 20, 2012
 */
package com.app.lbs.common.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.app.lbs.common.web.service.WebWidgetService;

/**
 * Web Widget Service Implementation
 *
 * @author colen
 *
 */
public class WebWidgetServiceImpl extends WebCommonServiceImpl implements WebWidgetService {


    @Override
    public List<Map<String, Object>> queryMenus(Map<String, Object> params) {
        // String uid = (String) params.get("uid");
        return getCommonDao().namingQueryForList("sql_common_codedata_queryMenus", params);
    }


    @Override
    public List<Map<String, Object>> loadOrgList(Map<String, Object> params) {
        Map<String, Object> qps = new HashMap<String, Object>();
        qps.putAll(params);

        // parent organization ID
        List<Map<String, Object>> list = getCommonDao().namingQueryForList("sql_common_queryOrgList", qps);
        for (Map<String, Object> org : list) {
            if (((String) org.get("org_id")).length() == 12) {
                // TODO do not show group
                org.put("leaf", false);
            } else {
                org.put("leaf", true);
            }
            org.put("checked", false);
        }
        
        return list;
    }


}
