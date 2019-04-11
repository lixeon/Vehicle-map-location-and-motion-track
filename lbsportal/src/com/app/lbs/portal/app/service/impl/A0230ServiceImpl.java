/*
 * LBSAPP
 */
package com.app.lbs.portal.app.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.app.lbs.common.service.pub.TerminalInfoService;
import com.app.lbs.portal.app.service.AbstractClientAppService;
import com.app.platform.base.utils.DateUtil;
import com.app.platform.base.utils.StringUtil;

/**
 * Get Latest Location Trace
 *
 * @author luxinxian
 *
 */
public class A0230ServiceImpl extends AbstractClientAppService {

    /** logger */
    private static final Logger LOG = Logger.getLogger(A0230ServiceImpl.class);

    
    /** terminal info service */
    private TerminalInfoService terminalInfoService = null;
    
    
    @Override
    protected Map<String, Object> execute(Map<String, Object> params) {
        
        String terminalId = (String) params.get("terminal_id");
        String lastTime = (String) params.get("last_time");
        
        if (LOG.isInfoEnabled()) {
            LOG.info("lastTime:" + lastTime + ", terminalId:" + terminalId);
        }
        
        
        // if there is no last time, just get last 60sec's data.
        if (StringUtil.isTrimEmpty(lastTime)) {
            lastTime = DateUtil.getTime(DateUtil.DATE_FORMAT_4, Calendar.SECOND, -60);
        }
        
        Map<String, Object> qps = new HashMap<String, Object>();
        qps.put("tmnId", terminalId);
        Date dateLT = DateUtil.toDate(lastTime, DateUtil.DATE_FORMAT_4);
        qps.put("last_time", dateLT);

        // query
        Map<String, Object> latestTrace = this.terminalInfoService.getTerminalLatestTrace(qps);
        
        List<Map<String, Object>> posList = extractLocList(latestTrace);

        Map<String, Object> output = new HashMap<>();
        output.put("pos_list", posList);
        output.put("last_time", latestTrace.get("lastTime"));

        return output;
    }

    
    @Override
    public String serviceKey() {
        return "0230";
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
