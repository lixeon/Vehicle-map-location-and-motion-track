/*
 * LBSAPP
 */
package com.app.lbs.portal.app.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.app.lbs.common.service.pub.TerminalInfoService;
import com.app.lbs.portal.app.service.AbstractClientAppService;
import com.app.platform.base.utils.DateUtil;
import com.app.platform.base.utils.StringUtil;
import com.app.platform.base.utils.SystemConfig;

/**
 * Get Terminal's History Locations
 *
 * @author luxinxian
 *
 */
public class A0240ServiceImpl extends AbstractClientAppService {

    /** logger */
    private static final Logger LOG = Logger.getLogger(A0240ServiceImpl.class);

    
    /** terminal info service */
    private TerminalInfoService terminalInfoService = null;
    
    
    @Override
    protected Map<String, Object> execute(Map<String, Object> params) {
        
        String terminalId = (String) params.get("terminal_id");
        String lastTime = (String) params.get("last_time");
        String st = (String) params.get("st");
        String et = (String) params.get("et");
        
        if (LOG.isInfoEnabled()) {
            LOG.info("terminalId:" + terminalId + ", st:" + st
                    + ", et:" + et + ", lastTime:" + lastTime);
        }

        Map<String, Object> qps = new HashMap<String, Object>();
        qps.put("tmnId", terminalId);
        qps.put("st", DateUtil.toDate(st, DateUtil.DATE_FORMAT_1));
        if (!StringUtil.isTrimEmpty(lastTime)) {
            Date dateLT = DateUtil.toDate(lastTime, DateUtil.DATE_FORMAT_4);
            qps.put("st", dateLT);
        }
        
        qps.put("et", DateUtil.toDate(et, DateUtil.DATE_FORMAT_1));
        
        long batch = SystemConfig.getLong("histrace.query.batch");
        qps.put("maxCnt", batch);
        //qps.put("action", "T3");
        
        // query
        Map<String, Object> historyTrace = this.terminalInfoService.getTerminalHistoryTrace(qps);
        
        List<Map<String, Object>> posList = extractLocList(historyTrace);

        Map<String, Object> output = new HashMap<>();
        output.put("pos_list", posList);
        output.put("last_time", historyTrace.get("lastTime"));
        output.put("has_next", historyTrace.get("hasNext"));

        return output;
    }


    @Override
    public String serviceKey() {
        return "0240";
    }

    
    @Override
    protected boolean isAuth() {
        return true;
    }


    /**
     * setter
     * @param terminalInfoService the terminalInfoService to set
     */
    public void setTerminalInfoService(TerminalInfoService terminalInfoService) {
        this.terminalInfoService = terminalInfoService;
    }

}
