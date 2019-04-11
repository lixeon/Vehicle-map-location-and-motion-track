/*
 * LBSAPP
 */
package com.app.lbs.portal.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.app.lbs.common.web.service.impl.WebCommonServiceImpl;
import com.app.lbs.portal.web.service.TerminalAlarmService;
import com.app.platform.base.dao.Page;
import com.app.platform.base.dao.PageCond;
import com.app.platform.base.utils.DateUtil;
import com.app.platform.base.utils.StringUtil;

/**
 * Terminal Alarm Service
 *
 * @author colen
 *
 */
public class TerminalAlarmServiceImpl extends WebCommonServiceImpl
    implements TerminalAlarmService {

    @Override
    public void queryAlarmList(Map<String, Object> params) {
        Map<String, Object> qps = new HashMap<>();
        qps.putAll(params);
        
        // reset orgId
        qps.put("orgId", qps.get("orgId") + "%");
        
        String stdate = (String) qps.get("alarmTimeSt");
        if (!StringUtil.isTrimEmpty(stdate)) {
            qps.put("alarmTimeSt", DateUtil.toDate(stdate + " " + qps.get("st") + ":00", DateUtil.DATE_FORMAT_1));
        }

        String etdate = (String) qps.get("alarmTimeEt");
        if (!StringUtil.isTrimEmpty(etdate)) {
            qps.put("alarmTimeEt", DateUtil.toDate(etdate + " " + qps.get("et") + ":59", DateUtil.DATE_FORMAT_1));
        }

        // query page condition
        PageCond cond = new PageCond(qps, Integer.valueOf((String)
                params.get("start")),
                Integer.valueOf((String) params.get("limit")));

        // query for page
        Page page = getCommonDao().namingQueryForPage("sql_manager_queryAlarmList", cond);

        params.put("totalCount", page.getTotalCount());
        params.put("alarmList", page.getResultList());
    }


    @Override
    public void updateNotifyStatus(Map<String, Object> params) {
        Map<String, Object> qps = new HashMap<>();
        qps.putAll(params);
        
        getCommonDao().namingUpdate("sql_manager_updateAlarmNotifyStatus", qps);
    }

}
