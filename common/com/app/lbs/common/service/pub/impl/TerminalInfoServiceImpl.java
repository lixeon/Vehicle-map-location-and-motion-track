/*
 * LBSAPP
 */
package com.app.lbs.common.service.pub.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.app.lbs.common.service.CommonServiceEx;
import com.app.lbs.common.service.pub.TerminalInfoService;
import com.app.lbs.common.util.LbsBizUtils;
import com.app.platform.base.utils.CommonUtil;
import com.app.platform.base.utils.DateUtil;

/**
 * Terminal Info Service
 *
 * @author luxinxian
 *
 */
public class TerminalInfoServiceImpl extends CommonServiceEx implements TerminalInfoService {


    @Override
    public Map<String, Object> getTerminalLatestTrace(Map<String, Object> qps) {
        
        String tmnId = (String) qps.get("tmnId");
        
        // query
        List<Map<String, Object>> list = getCommonDao()
                .queryForList(SQL_GETLATESTTRACE.replace("#tableName#",
                        LbsBizUtils.getTPLLTableName(tmnId)), qps);

        Map<String, Object> locs = new HashMap<>();
        if (!CommonUtil.isEmpty(list)) {
            locs.put("poss", list);
            String lt = (String) list.get(list.size() - 1).get("loc_time");
            locs.put("lastTime", DateUtil.chgFmt(lt, DateUtil.DATE_FORMAT_1, DateUtil.DATE_FORMAT_4));
        }
        
        return locs;
    }
    
    

    @Override
    public Map<String, Object> getTerminalHistoryTrace(Map<String, Object> qps) {
        
        String tmnId = (String) qps.get("tmnId");
        // query
        List<Map<String, Object>> list = getCommonDao()
                .queryForList(SQL_GETHISLOCATIONS.replace("#tableName#", LbsBizUtils.getTPLLTableName(tmnId)), qps);
        
        Map<String, Object> locs = new HashMap<>();
        // default as true
        locs.put("hasNext", "0");
        if (!CommonUtil.isEmpty(list)) {
            locs.put("poss", list);
            String lt = (String) list.get(list.size() - 1).get("loc_time");
            locs.put("lastTime", DateUtil.chgFmt(lt, DateUtil.DATE_FORMAT_1, DateUtil.DATE_FORMAT_4));
            if (list.size() >= Long.valueOf(qps.get("maxCnt").toString())) {
                locs.put("hasNext", "1");
            }
        }
        
        return locs;
    }



    @Override
    public Map<String, Object> getTerminalLatestLocation(Map<String, Object> qps) {
        
        String tmnId = (String) qps.get("tmnId");

        // get last position
        StringBuilder sql = new StringBuilder(SQL_GETLATEST_P);
        List<Map<String, Object>> posList = getCommonDao().queryForList(
                sql.toString().replace("#tableName#", LbsBizUtils.getTPLLTableName(tmnId)), qps);

        if (CommonUtil.isEmpty(posList)) {
            return new HashMap<>();
        } else {
            return posList.get(0);
        }
    }

    
}
