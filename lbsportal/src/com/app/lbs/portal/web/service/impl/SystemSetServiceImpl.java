/*
 * LBSAPP
 */
package com.app.lbs.portal.web.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.app.lbs.common.web.service.impl.WebCommonServiceImpl;
import com.app.lbs.portal.web.service.SystemSetService;

/**
 * System Set Service
 *
 * @author colen
 *
 */
public class SystemSetServiceImpl extends WebCommonServiceImpl implements SystemSetService {

    @Override
    public Map<String, Map<String, Object>> getCurSmsSetting() {
        List<Map<String, Object>> confList = getCommonDao()
                .queryForList("select * from T_BSF_SYSCONFIG where CATEGORY=?", CATEGORY);
        Map<String, Map<String, Object>> confMap = new HashMap<>();
        for (Map<String, Object> conf: confList) {
            confMap.put(conf.get("config_id").toString(), conf);
        }
        return confMap;
    }


    @Override
    @Transactional
    public void updateSmsSettings(Map<String, Object> params) {
        Date now = new Date();
        
        Map<String, Object> qps = new HashMap<>();
        qps.put("config_id", CID_LOWLEVEL);
        qps.put("value", params.get("lowlevel"));
        qps.put("update_time", now);
        
        getCommonDao().namingUpdate("sql_common_updateSysConfig", qps);
        
        qps.put("config_id", CID_REMINDER);
        qps.put("value", params.get("reminder"));
        getCommonDao().namingUpdate("sql_common_updateSysConfig", qps);
    }

}
