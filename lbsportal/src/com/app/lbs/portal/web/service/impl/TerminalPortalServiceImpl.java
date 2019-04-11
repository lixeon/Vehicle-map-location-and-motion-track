/*
 * lbspt
 */
package com.app.lbs.portal.web.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.app.lbs.common.CodeConst;
import com.app.lbs.common.CommonConsts;
import com.app.lbs.common.exception.CommonBusinessException;
import com.app.lbs.common.facade.RemoteInvokeFacade;
import com.app.lbs.common.remote.CallRequest;
import com.app.lbs.common.remote.CallResponse;
import com.app.lbs.common.service.pub.TerminalInfoService;
import com.app.lbs.common.util.LbsBizUtils;
import com.app.lbs.common.web.service.impl.WebCommonServiceImpl;
import com.app.lbs.portal.web.service.TerminalPortalService;
import com.app.platform.base.utils.DateUtil;
import com.app.platform.base.utils.StringUtil;
import com.app.platform.base.utils.SystemConfig;

/**
 * Platform Portal Service
 * 
 * @author colen
 * 
 */
public class TerminalPortalServiceImpl extends WebCommonServiceImpl implements
        TerminalPortalService  {
    
    /** Logger */
    private static final Logger LOG = Logger.getLogger(TerminalPortalServiceImpl.class);

    /** terminal info service */
    private TerminalInfoService terminalInfoService = null;
	  

    @Override
    public void getTerminalInfoByTmnId(Map<String, Object> params) {
        
        String terminalSerialNo = (String) params.get("terminalSerialNo");
        
        if (LOG.isInfoEnabled()) {
            LOG.info("params.terminalSerialNo:" + terminalSerialNo);
        }

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("terminalSerialNo", terminalSerialNo);
        
        // query terminal from DB.
        Map<String, Object> terminal = getCommonDao().namingQueryForMap("sql_portal_loadTerminalInfoByTmnId", args);
        
        // store
        params.put("terminal", terminal);

        // get terminal status
        CallRequest request = new CallRequest();
        request.setActType(CommonConsts.ACTTYPE_001);
        request.set("terminalSerialNo", terminalSerialNo);

        CallResponse response = RemoteInvokeFacade.call(request);

        Map<String, Object> status = new HashMap<>();
        if (response != null && response.getData().get("hbTime") != null) {
            Map<String, Object> myStatus = response.getData();
            status.putAll(myStatus);
            // if terminal offline
            if (StringUtil.isTrimEmpty((String) status.get("status"))
                    || Boolean.FALSE.equals(status.get("login"))) {
                status.put("status", "900");
            }
        } else {
            // if no status got, just set the terminal's status as 900 (offline)
            status.put("status", "900");
            status.put("hbTime", "无法获取");
        }
        terminal.put("status", status);
        
        // get track status
        List<Map<String, Object>> configList = getCommonDao()
                .namingQueryForList("sql_portal_loadTerminalConfigs", args);
        for (Map<String, Object> conf : configList) {
            if (CommonConsts.TMN_CFG_TRACE.equals(conf.get("config_key"))) {
                terminal.put("track", conf.get("value"));
                break;
            }
        }

        // default find last 7days' point
        args.put("startTime", DateUtil.getDateS(-7));
        args.put("locStatus", CodeConst.LOC_STATUS_SUCCESS);

        // get last position
        args.put("tmnId", terminalSerialNo);
        
        // store
        terminal.put("pos", this.terminalInfoService.getTerminalLatestLocation(args));

    }


    @Override
    public void getLatestTrace(Map<String, Object> params) {
        
        if (LOG.isInfoEnabled()) {
            LOG.info("lastTime:" + params.get("lastTime")
                    + ", terminalId:" + params.get("terminalId"));
        }
        
        String lastTime = (String) params.get("lastTime");
        if (StringUtil.isTrimEmpty(lastTime)) {
            lastTime = DateUtil.getTime(DateUtil.DATE_FORMAT_4, Calendar.SECOND, -60);
        }

        Map<String, Object> qps = new HashMap<String, Object>();
        String tmnId = (String) params.get("terminalId");
        qps.put("tmnId", tmnId);
        Date dateLT = DateUtil.toDate(lastTime, DateUtil.DATE_FORMAT_4);
        qps.put("last_time", dateLT);

        Map<String, Object> locs = this.terminalInfoService.getTerminalLatestTrace(qps);
        // store
        params.put("locs", locs);
    }



    @Override
    public void getHistoryLocations(Map<String, Object> params) {
        Map<String, Object> qps = new HashMap<String, Object>();
        //qps.put("terminal_serial_no", params.get("terminalId"));
        String tmnId = (String) params.get("terminalId");
        
        LOG.info("terminal Id: " + tmnId);
        
        qps.put("tmnId", params.get("terminalId"));

        String date = (String) params.get("date");
        
        String et = (String) params.get("et");
        if (StringUtil.isTrimEmpty(et)) {
            et = "23:59:59";
        } else {
            et = et.concat(":59");
        }
        qps.put("et", DateUtil.toDate(date + " " + et, DateUtil.DATE_FORMAT_1));
        String lastTime = (String) params.get("lastTime");
        if (!StringUtil.isTrimEmpty(lastTime)) {
            Date dateLT = DateUtil.toDate(lastTime, DateUtil.DATE_FORMAT_4);
            qps.put("st", dateLT);
        } else {
            String st = (String) params.get("st");
            if (StringUtil.isTrimEmpty(st)) {
                st = "00:00:00";
            } else {
                st = st.concat(":00");
            }
            qps.put("st", DateUtil.toDate(date + " " + st, DateUtil.DATE_FORMAT_1));
        }
        
        long batch = SystemConfig.getLong("histrace.query.batch");
        qps.put("maxCnt", batch);
        qps.put("action", "T3");

        Map<String, Object> locs = this.terminalInfoService.getTerminalHistoryTrace(qps);

        // store
        params.put("locs", locs);
        
    }

    
    
    /** sql for getMonthDataHint */
    private static final String SQL_GETMONTHHINT = "select count(1) as cnt, t_t_t.gdate"
            + " from (select DATE_FORMAT(LOC_TIME, '%Y-%m-%d') as gdate from #tableName# t where t.TMN_ID = :terminalSerialNo and t.ACTION = :action and t.LOC_TIME >= :st and t.LOC_TIME <= :et) t_t_t"
            + " group by t_t_t.gdate";

    @Override
    public void getMonthDataHint(Map<String, Object> params) {
        String terminalSerialNo = (String) params.get("terminalSerialNo");
        String yearMon = (String) params.get("yearMon");

        Date st = DateUtil.toDate(yearMon + "01", DateUtil.DATE_FORMAT_FS);
        Date et = null;
        String curYm = DateUtil.getDate(DateUtil.DATE_FORMAT_FYM, 0);
        if (yearMon.compareTo(curYm) < 0) {
            // last or older month
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(st);
            // get net month
            calendar.add(Calendar.MONTH, 1);
            // last date
            calendar.add(Calendar.DATE, -1);
            
            // got et now
            et = DateUtil.toDate(DateUtil.format(calendar.getTime(), DateUtil.DATE_FORMAT_F) + " 23:59:59", DateUtil.DATE_FORMAT_1);
        } else {
            // this month
            et = DateUtil.toDate(DateUtil.getDate(DateUtil.DATE_FORMAT_F, 0) + " 23:59:59", DateUtil.DATE_FORMAT_1);
        }

        // table name
        String tableName = LbsBizUtils.getTPLLTableName(terminalSerialNo);
        
        Map<String, Object> qps = new HashMap<>();
        qps.put("terminalSerialNo", terminalSerialNo);
        qps.put("st", st);
        qps.put("et", et);
        qps.put("action", "T3");

        // query
        List<Map<String, Object>> list = getCommonDao()
                .queryForList(SQL_GETMONTHHINT.replace("#tableName#", tableName), qps);
        
        // store
        params.put("monthDataHint", list);
    }

    
    /** sql for 'getDateDataHint' */
    private static final String SQL_GETDATEHINT = "select count(1) as cnt, t_t_t.gdate"
            + " from (select DATE_FORMAT(LOC_TIME, '%Y%m%d%H%i') as gdate from #tableName# t where t.TMN_ID = :terminalSerialNo and t.ACTION = :action and t.LOC_TIME >= :st and t.LOC_TIME <= :et) t_t_t"
            + " group by t_t_t.gdate"
            + " order by t_t_t.gdate";

    @Override
    public void getDateDataHint(Map<String, Object> params) {
        String terminalSerialNo = (String) params.get("terminalSerialNo");
        String date = (String) params.get("date");
        
        Date st = DateUtil.toDate(date + " 00:00:00", DateUtil.DATE_FORMAT_1);
        Date et = DateUtil.toDate(date + " 23:59:59", DateUtil.DATE_FORMAT_1);
        
        // table name
        String tableName = LbsBizUtils.getTPLLTableName(terminalSerialNo);
        
        Map<String, Object> qps = new HashMap<>();
        qps.put("terminalSerialNo", terminalSerialNo);
        qps.put("st", st);
        qps.put("et", et);
        qps.put("action", "T3");
        
        // query
        List<Map<String, Object>> list = getCommonDao()
                .queryForList(SQL_GETDATEHINT.replace("#tableName#", tableName), qps);
        
        // store
        params.put("dateDataHint", list);
    }
 @Transactional
  public void modifyTerminalName(Map<String, Object> params)
  {
   String terminalSerialNo = params.get("terminalSerialNo").toString();
 String terminalName = params.get("terminalName").toString();
    

  Map<String, Object> qps = new HashMap<String, Object>();
  qps.put("terminalSerialNo", terminalSerialNo);
   qps.put("terminalName", terminalName);
   
    List<Map<String, Object>> terminals = getCommonDao().namingQueryForList("sql_manager_getTerminalInfoByNo", qps);
   
  Object tmnNo = null;Object tmnName = null;
    for (Map<String, Object> tmn : terminals) {
     tmnNo = tmn.get("terminal_serial_no");
     tmnName = tmn.get("terminal_name");
     if ((!tmnNo.equals(terminalSerialNo)) && (tmnName.equals(terminalName))) {
        LOG.info("终端昵称已经存在" + tmnName);
        throw new CommonBusinessException("昵称已经存在，请更换昵称！");
     }
   }
    

   getCommonDao().namingUpdate("sql_portal_updateTerminalById", qps, true);
 }


    /**
     * setter
     * @param terminalInfoService the terminalInfoService to set
     */
    public void setTerminalInfoService(TerminalInfoService terminalInfoService) {
        this.terminalInfoService = terminalInfoService;
    }


	@Override
	public void getCityInfo(Map<String, Object> params) {
		
	}

}
