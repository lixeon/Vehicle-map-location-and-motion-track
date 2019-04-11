/*
 * RX-lbspt 
 */
package com.app.lbs.portal.web.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.app.lbs.common.web.service.impl.WebCommonServiceImpl;
import com.app.lbs.portal.web.service.TerminalMgrService;
import com.app.platform.base.dao.Page;
import com.app.platform.base.dao.PageCond;
import com.app.platform.base.utils.CommonUtil;
import com.app.platform.base.utils.DateUtil;
import com.app.platform.base.utils.EncryptUtil;
import com.app.platform.base.utils.StringUtil;
import com.app.platform.base.utils.SystemConfig;


/**
 * Terminal Manager Service Implementation
 *
 * @author colen
 *
 */
public class TerminalMgrServiceImpl extends WebCommonServiceImpl
    implements TerminalMgrService {

    /** logger */
    private static final Logger LOG = Logger.getLogger(TerminalMgrServiceImpl.class);


    @Override
    @Transactional
    public void checkAndAdd(Map<String, Object> params) {
        // terminal serial no from page
        String terminalSerialNo = params.get("terminalSerialNo").toString().trim();
        String terminalOpNo = params.get("terminalOpNo").toString().trim();
        String terminalIMEI = params.get("terminalIMEI").toString().trim();
		String terminalName = params.get("terminalName").toString();
        // check terminal serial no
        // check IMEI no
        Map<String, Object> qps = new HashMap<>();
        qps.put("terminalSerialNo", terminalSerialNo);
        qps.put("terminalOpNo", terminalOpNo);
      //  qps.put("terminalIMEI", terminalIMEI);
		qps.put("terminalName", terminalName);
        // qps.put("terminalIMSI", params.get("terminalIMSI"));
        List<Map<String, Object>> terminals = getCommonDao()
                .namingQueryForList("sql_manager_getTerminalInfoByNo", qps);
        Object tv = null;
        for (Map<String, Object> tmn : terminals) {
            tv = tmn.get("terminal_serial_no");
            if (terminalSerialNo.equals(tv)) {
                LOG.info("终端编号已经存在，请确认！" + tv);
                throw new CommonBusinessException("终端编号已经存在，请确认！");
            }
            
            tv = tmn.get("terminal_op_no");
            if (terminalOpNo.equals(tv)) {
                LOG.info("终端SIM卡号码已经存在，请确认！" + tv);
                throw new CommonBusinessException("终端SIM卡号码已经存在，请确认！");
            }

            tv = tmn.get("terminal_name");
            if (terminalName.equals(tv)) {
                LOG.info("昵称已经存在，请确认！" + tv);
                throw new CommonBusinessException("昵称已经存在，请更换昵称！");
            }
        }

		checkTerminalNameUnique(terminalName);
        Map<String, Object> args = new HashMap<>();
        // insert T_TERMINAL

        Date now = DateUtil.getCurrentDate();
        args.put("terminalSerialNo", terminalSerialNo);

        // if mobile is not empty default password is the last 6 digits of the mobile
        // else default password is the last 6 digits of the SIM Card Number
        String mobile = params.get("mobile").toString().trim();
        if (!StringUtil.isTrimEmpty(mobile)) {
            args.put("password", EncryptUtil.toSHA256(mobile.substring(5)));
        } else {
            args.put("password", EncryptUtil.toSHA256(terminalOpNo.substring(5)));
        }
        
        args.put("userType", params.get("userType"));
        args.put("terminalOpNo", terminalOpNo);
        args.put("terminalName", terminalName);
        args.put("terminalIMEI", terminalIMEI);
        args.put("terminalIMSI", params.get("terminalIMSI"));
        args.put("terminalType", params.get("terminalType"));
        args.put("owner", params.get("owner"));
        
        args.put("ownerTel", params.get("ownerTel"));
        args.put("ownerAddr", params.get("ownerAddr"));
        args.put("ownerVehiceNo", params.get("ownerVehiceNo"));
        args.put("ownerVcbodyNo", params.get("ownerVcbodyNo"));
        args.put("ownerVcmotorNo", params.get("ownerVcmotorNo"));
        args.put("ownerInsurance", params.get("ownerInsurance"));
        args.put("ownerRemark", params.get("ownerRemark"));

        args.put("provider", params.get("provider"));
        args.put("regDate", now);
        args.put("serviceEndDate", DateUtil.toDate((String) params.get("serviceEndDate"), DateUtil.DATE_FORMAT_F));
        String provinceId = (String) params.get("provinceId");
        if (StringUtil.isTrimEmpty(provinceId)) {
            // default for JiangSu
            provinceId = "10";
        }
        args.put("provinceId", provinceId);
        String cityId = (String) params.get("cityId");
        if (StringUtil.isTrimEmpty(cityId)) {
            // default for 'NJ'
            cityId = "073";
        }
        args.put("cityId", cityId);
        args.put("status", params.get("tmnServiceStatus"));
        args.put("creator", params.get("uid"));
        args.put("createDate", now);

        if (LOG.isInfoEnabled()) {
            LOG.info("Add Terminal, " + args);
        }
        
        // execute add
        getCommonDao().namingUpdate("sql_manager_addTerminal", args, true);

        // insert T_TERMINAL_CONFIG
        List<Map<String, Object>> defaultConfigs = SystemConfig.getListByPrefix("conf.cmcc.dv.", true);
        // clear old value
        args.clear();
        
        // phone
        params.put("PHONE", terminalOpNo);
        // user
        params.put("USER", mobile);
        
        int idx = 1;
        String key = null;
        for (Map<String, Object> config : defaultConfigs) {
            key = (String) config.get("_key");
            if (params.containsKey(key)) {
                config.put("_value", params.get(key));
            }
            args.put("ck" + idx, key);
            args.put("ck" + idx + "value", config.get("_value"));
            args.put("ck" + idx + "status", CodeConst.C_CONFIGSET_YES);
            idx++;
        }
        // terminal serial no
        args.put("terminalSerialNo", terminalSerialNo);
        // update time
        args.put("updTime", DateUtil.toDate(DateUtil.getTime(DateUtil.DATE_FORMAT_F,
                Calendar.HOUR_OF_DAY, -1), DateUtil.DATE_FORMAT_F));

        // execute add
        getCommonDao().namingUpdate("sql_manager_addTerminalConfigs", args, true);

		args.clear();
		//getCommonDao().update("insert into T_TERMINAL_LATESTLOC (TERMINAL_SERIAL_NO) values (?)", new Object[] { terminalSerialNo });
  
        // insert T_ORG_TMN_RL
        args.clear();
        args.put("orgId", params.get("orgId"));
        args.put("terminalSerialNo", terminalSerialNo);

        // execute add
        getCommonDao().namingUpdate("sql_manager_addOrgTerminalRL", args);
    }
 private void checkTerminalNameUnique(String terminalName)
  {
   	int cnt = getCommonDao().queryForInt("select count(1) as CNT from T_TERMINAL where TERMINAL_SERIAL_NO=?", new Object[] { terminalName });
 	if (cnt != 0) {
  	LOG.info("昵称已经存在，请更换昵称！" + terminalName);
    throw new CommonBusinessException("昵称已经存在，请更换昵称！");
   }
}


    @Override
    public Map<String, Object> queryTerminalList(
            Map<String, Object> params) {
        Map<String, Object> qps = new HashMap<>();
        qps.putAll(params);

        qps.put("user_mobile_key", CommonConsts.TMN_CFG_USER);
        // reset ORGID
        //qps.put("orgId", qps.get("orgId"));

        String owner = (String) params.get("owner");
        if (!StringUtil.isTrimEmpty(owner)) {
            qps.put("owner", "%" + owner + "%");
        }

        PageCond cond = new PageCond(qps, Integer.valueOf((String) params.get("start")),
                Integer.valueOf((String) params.get("limit")));

        // query for page
        Page page = getCommonDao().namingQueryForPage("sql_manager_queryTerminalList", cond);
        
        for (Map<String, Object> terminal : page.getResultList()) {
            terminal.put("city_disp_nm", getCdNmByValue(CodeConst.CDK_CITY, terminal.get("city_id")));
            terminal.put("status_disp_nm", getCdNmByValue(CodeConst.CDD_CD0001, terminal.get("status")));
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("totalCount", page.getTotalCount());
        resultMap.put("terminalInfos", page.getResultList());
        return resultMap;
    }


    @Override
    public Map<String, Object> getTerminalInfoDetial(Map<String, Object> params) {

        Map<String, Object> qps = new HashMap<>();
        qps.put("terminalSerialNo", params.get("terminalSerialNo"));
        qps.put("rootOrgId", CodeConst.TOP_GRPID);
        
        qps.put("user_mobile_key", CommonConsts.TMN_CFG_USER);
        
        // get terminal
        Map<String, Object> terminal = getCommonDao().namingQueryForMap("sql_manager_queryTerminalList", qps);
        
        if (terminal == null) {
            throw new CommonBusinessException("终端已经不存在，请重新查询一次！");
        }

        List<Map<String, Object>> confList = getCommonDao()
                .namingQueryForList("sql_portal_loadTerminalConfigs", qps);
        Map<String, Object> config = new HashMap<String, Object>();
        for (Map<String, Object> conf : confList) {
            config.put(conf.get("config_key").toString(), conf);
        }

        terminal.put("CONF", config);

        return terminal;
    }


    @Override
    @Transactional
    public void deleteTerminal(String terminalSerialNo) {

        // disconnect terminal
        disconnectTerminal(terminalSerialNo);

        // delete T_ORG_TMN_RL
        getCommonDao().update("delete from T_ORG_TMN_RL where TERMINAL_SERIAL_NO=?", terminalSerialNo);
        
        // delete T_TERMINAL_CONFIG
        getCommonDao().update("delete from T_TERMINAL_CONFIG where TERMINAL_SERIAL_NO=?", terminalSerialNo);
        
  		getCommonDao().update("delete from T_TERMINAL_LATESTLOC where TERMINAL_SERIAL_NO=?", new Object[] { terminalSerialNo });
        // delete T_TERMINAL
        getCommonDao().update("delete from T_TERMINAL where TERMINAL_SERIAL_NO=?", terminalSerialNo);
    }


    @Override
    @Transactional
    public void modifyTerminalInfo(Map<String, Object> params) {

        String terminalSerialNo = params.get("terminalSerialNo").toString().trim();
        String terminalOpNo = params.get("terminalOpNo").toString().trim();
        String mobile = params.get("mobile").toString().trim();
		String terminalName = params.get("terminalName").toString().trim();
        // check PHONE
       checkUniqueNo(terminalSerialNo, terminalOpNo, terminalName);

        // update terminal info
        params.put("status", params.get("tmnServiceStatus"));
        String resetPwd = (String) params.get("resetPwd");
        if (CommonConsts.CHECK_ON.equals(resetPwd)) {
            // if mobile is not empty default password is the last 6 digits of the mobile
            // else default password is the last 6 digits of the SIM Card Number
            if (!StringUtil.isTrimEmpty(mobile)) {
                params.put("password", EncryptUtil.toSHA256(mobile.substring(5)));
            } else {
                params.put("password", EncryptUtil.toSHA256(terminalOpNo.substring(5)));
            }
        }
        
        if (LOG.isInfoEnabled()) {
            LOG.info("update Terminal: " + params);
        }

        getCommonDao().namingUpdate("sql_portal_updateTerminalById", params, true);

        Map<String, Object> qps = new HashMap<>();
        qps.put("terminalSerialNo", terminalSerialNo);
        qps.put("k1", CommonConsts.TMN_CFG_USER);
        qps.put("k2", CommonConsts.TMN_CFG_PHONE);
        List<Map<String, Object>> configlist = this.getCommonDao()
                .namingQueryForList("sql_gateway_queryTerminalConfigByKeys", qps);

        Map<String, Object> args = new HashMap<>();
        args.put("terminalSerialNo", terminalSerialNo);
        args.put("status", CodeConst.C_CONFIGSET_NO);
        args.put("updTime", new Date());
        boolean willSync = false;
        for (Map<String, Object> config : configlist) {
            if (config.get("config_key").equals(CommonConsts.TMN_CFG_USER)
                    && !config.get("value").equals(mobile)) {
                willSync = true;
                args.put("configKey", CommonConsts.TMN_CFG_USER);
                args.put("value", mobile);
                getCommonDao().namingUpdate("sql_portal_updateTerminalConfig", args, true);
            }
            
            if (config.get("config_key").equals(CommonConsts.TMN_CFG_PHONE)
                    && !config.get("value").equals(terminalOpNo)) {
                willSync = true;
                args.put("configKey", CommonConsts.TMN_CFG_PHONE);
                args.put("value", terminalOpNo);
                getCommonDao().namingUpdate("sql_portal_updateTerminalConfig", args, true);
            }
        }
        
        if (willSync) {
            // send sync Command, config changed
            sendSyncConfigRequest(terminalSerialNo);
        }
        
        String orgId = (String) params.get("orgId");
        String oldOrgId = (String) params.get("orgId_old");
        if (!oldOrgId.equals(orgId)) {
            // delete T_ORG_TMN_RL
            getCommonDao().update("delete from T_ORG_TMN_RL where TERMINAL_SERIAL_NO=?", terminalSerialNo);
            // add a new one
            args.clear();
            args.put("orgId", orgId);
            args.put("terminalSerialNo", terminalSerialNo);
            getCommonDao().namingUpdate("sql_manager_addOrgTerminalRL", args);
        }
    }


    private void checkUniqueNo(String terminalSerialNo, String terminalOpNo,String terminalName) {
        Map<String, Object> qps = new HashMap<>();
        // check terminal SIM card No.
        // check terminal serial no
        qps.put("terminalSerialNo", terminalSerialNo);
        qps.put("terminalOpNo", terminalOpNo);
		qps.put("terminalName", terminalName);
        List<Map<String, Object>> terminals = getCommonDao()
                .namingQueryForList("sql_manager_getTerminalInfoByNo", qps);
        Object tmnNo = null, simNo = null, tmnName = null;
        for (Map<String, Object> tmn : terminals) {
            tmnNo = tmn.get("terminal_serial_no");
            simNo = tmn.get("terminal_op_no");
            if (!tmnNo.equals(terminalSerialNo) && simNo.equals(terminalOpNo)) {
                LOG.info("终端SIM卡号码已经存在，请确认！" + simNo);
                throw new CommonBusinessException("终端SIM卡号码已经存在，请确认！");
            }
		tmnName = tmn.get("terminal_name");
    	if ((!tmnNo.equals(terminalSerialNo)) && (tmnName.equals(terminalName))) {
      		LOG.info("昵称已经存在，请更换昵称！" + tmnName);
     		 throw new CommonBusinessException("昵称已经存在，请更换昵称！");
       }
    }
  	 if (!terminalSerialNo.equals(terminalName)) {
     	checkTerminalNameUnique(terminalName);
		}
	}


    @SuppressWarnings("unchecked")
    @Override
    public void getTerminalStatusList(Map<String, Object> params) {
        Map<String, Object> qps = new HashMap<>();
        qps.putAll(params);
        // reset orgId value for LIKE SQL.
        String orgId = qps.get("orgId").toString();
        qps.put("orgId", orgId + "%");
        qps.put("login_status_key", CommonConsts.TMN_CFG_LOGIN);
        qps.put("user_mobile_key", CommonConsts.TMN_CFG_USER);
        qps.put("orgLength", CommonConsts.LENGTH_OF_COMPANY_ORGID);
        if (orgId.length() > 8) {
            qps.put("orgLength", CommonConsts.LENGTH_OF_GROUP_ORGID);
        }

        PageCond cond = new PageCond(qps,
                Integer.valueOf((String) params.get("start")),
                Integer.valueOf((String) params.get("limit")));

        // query for page
        Page page = getCommonDao().namingQueryForPage("sql_manager_getTerminalStatusList", cond);

        // get status detail
        List<Map<String, Object>> resultList = page.getResultList();
        // get terminal status
        CallRequest request = new CallRequest();
        request.setActType(CommonConsts.ACTTYPE_001);
        
        List<String> terminalIdList = new ArrayList<>(resultList.size());
        for (Map<String, Object> terminal : resultList) {
            terminalIdList.add(terminal.get("terminal_serial_no").toString());
        }
        
        request.set("terminalIdList", terminalIdList);
        CallResponse response = RemoteInvokeFacade.call(request);
        List<Map<String, Object>> terminalRunInfoList = null;
        if (response != null && response.getData() != null) {
            terminalRunInfoList = (List<Map<String, Object>>)
                    response.getData().get("terminalRunInfoList");
        }
        
        Map<String, Object> terminal = null;
        Map<String, Object> terminalRunInfo = null;
        for (int i = 0; i < resultList.size(); i++) {
            terminal = resultList.get(i);
            if (terminalRunInfoList != null) {
                terminalRunInfo = terminalRunInfoList.get(i);
            }
            
            orgId = (String) terminal.get("org_id");
            if (orgId.length() > 8) {
                orgId = orgId.substring(0, 8);
            }
            terminal.put("org_name", getCodeDataService().getOrgName(orgId));
            
            if (terminalRunInfo != null
                    && terminalRunInfo.get("hbTime") != null) {
                terminal.put("tmn_status", terminalRunInfo.get("status"));
                terminal.put("speed", terminalRunInfo.get("speed"));
                terminal.put("gps", terminalRunInfo.get("gps"));
                terminal.put("gsm", terminalRunInfo.get("rssi"));
                // notify status
                //terminal.put("notify_status", "");
                terminal.put("last_update_time", terminalRunInfo.get("hbTime"));
            } else {
                terminal.put("tmn_status", CodeConst.TERMINLA_R_STATUS_NODATA);
                terminal.put("speed", "");
                terminal.put("gps", CommonConsts.NO_DATA);
                terminal.put("gsm", CommonConsts.NO_DATA);
                //terminal.put("notify_status", "");
                terminal.put("last_update_time", CommonConsts.NO_DATA);
            }
        }
        
        // hold
        params.put("totalCount", page.getTotalCount());
        params.put("terminalInfos", resultList);
        
    }

    @Override
    @Transactional
    public void checkAndUpdateSettings(Map<String, Object> params) {
        String terminalSerialNo = (String) params.get("terminalSerialNo");
        String terminalOpNo = (String) params.get("PHONE");
   		String terminalName = (String)params.get("terminalName");
        
        // check terminal SIM Card No.
        checkUniqueNo(terminalSerialNo, terminalOpNo, terminalName);

        Map<String, Object> qps = new HashMap<>();
        qps.put("terminalSerialNo", terminalSerialNo);
        qps.put("terminalOpNo", terminalOpNo);
        // find modified configuration item.
        List<Map<String, Object>> updateConfigList = new ArrayList<>();
        List<Map<String, Object>> allConfigList = getCommonDao()
                .namingQueryForList("sql_portal_loadTerminalConfigs", qps);
        String confKey = null;
        List<String> ignoreKeyList = SystemConfig.getValList("terminal.conf.ignorekey", ",");
        for (Map<String, Object> conf : allConfigList) {
            confKey = conf.get("config_key").toString();

            // ignore key
            if (!ignoreKeyList.contains(confKey)) {
            // if parameters pass by page contains the key and the value is different
            if (params.containsKey(confKey)
                    && !params.get(confKey).equals(conf.get("value"))) {
                // new value
                conf.put("new_value", params.get(confKey));
                updateConfigList.add(conf);
            }
            }


        }

        // settings modified
        if (!CommonUtil.isEmpty(updateConfigList)) {
            Map<String, Object> args = new HashMap<>();
            args.put("terminalSerialNo", terminalSerialNo);
            args.put("status", CodeConst.C_CONFIGSET_NO);
            args.put("updTime", new Date());
            for (Map<String, Object> conf : updateConfigList) {
                args.put("configKey", conf.get("config_key"));
                args.put("value", conf.get("new_value"));
                // update
                getCommonDao().namingUpdate("sql_portal_updateTerminalConfig", args, true);

                if (LOG.isInfoEnabled()) {
                    LOG.info("update terminal config, KEY:" + conf.get("config_key")
                            + ", VALUE:" + conf.get("new_value"));
                }
                
            }
        }

        qps.put("owner", params.get("owner"));
        getCommonDao().namingUpdate("sql_portal_updateTerminalById", qps, true);

        if (!CommonUtil.isEmpty(updateConfigList)) {
            sendSyncConfigRequest(terminalSerialNo);
        }
    }


    private void sendSyncConfigRequest(final String terminalSerialNo) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000 * 2);
                } catch (InterruptedException e) {
                    // ignore
                }

                // send sync-configuration Command.
                CallRequest request = new CallRequest();
                request.setActType(CommonConsts.ACTTYPE_004);
                request.set("terminalSerialNo", terminalSerialNo);
                
                // execute sync
                RemoteInvokeFacade.call(request);
            }
        }).start();
        
    }


    @Override
    public Map<String, Object> getTerminalConfigByKey(Map<String, Object> params) {
        Map<String, Object> qps = new HashMap<>();
        qps.put("terminalSerialNo", params.get("terminalSerialNo"));
        qps.put("configKey", params.get("configKey"));
        
        Map<String, Object> configObj = getCommonDao()
                .namingQueryForMap("sql_portal_loadTerminalConfigs", qps);

        String updTime = DateUtil.chgFmt((String) configObj.get("upd_time"), DateUtil.DATE_FORMAT_1, DateUtil.DATE_FORMAT_4);
        String queryTime = (String) params.get("queryTime");
        if (queryTime.compareTo(updTime) <= 0) {
            return configObj;
        }
        
        return null;
    }


    @Override
    public Map<String, Object> getTerminalCnt(Map<String, Object> params) {

        String orgId = params.get("orgId").toString();
        Map<String, Object> qps = new HashMap<>();

        if (CodeConst.TOP_GRPID.equals(orgId)) {
            qps.put("orgLength", CommonConsts.LENGTH_OF_COMPANY_ORGID);
        } else {
            qps.put("orgId", orgId);
        }
        
        // get all terminal count
        Map<String, Object> mapCnt = getCommonDao().namingQueryForMap("sql_manager_queryCompanyTmnCountAll", qps);
        Long allCnt = Long.valueOf(mapCnt.get("total").toString());

        qps.put("configKey", CommonConsts.TMN_CFG_LOGIN);
        qps.put("configValue", CodeConst.C_LOGIN_YES);

        // get on line terminal count
        mapCnt = getCommonDao().namingQueryForMap("sql_manager_queryCompanyTmnCount", qps);
        Long onCnt = Long.valueOf(mapCnt.get("total").toString());

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("terminal_all_cnt", allCnt.toString());
        resultMap.put("terminal_on_cnt", onCnt.toString());
        resultMap.put("terminal_off_cnt", allCnt - onCnt);

        return resultMap;
    }


    @Override
    public void disconnectTerminal(String terminalSerialNo) {
        CallRequest request = new CallRequest();
        request.setActType(CommonConsts.ACTTYPE_003);
        request.set("terminalSerialNo", terminalSerialNo);
        RemoteInvokeFacade.call(request);
    }


}
