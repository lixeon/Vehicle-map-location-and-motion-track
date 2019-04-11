/*
 * LBSAPP
 */
package com.app.lbs.portal.app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.app.lbs.common.CommonConsts;
import com.app.lbs.common.dao.CommonDaoEx;
import com.app.lbs.common.service.Service;
import com.app.lbs.portal.PTConsts;
import com.app.lbs.portal.app.AppReqException;
import com.app.platform.base.utils.CommonUtil;
import com.app.platform.base.utils.StringUtil;

/**
 * Abstract Client App Service
 *
 * @author luxinxian
 *
 */
public abstract class AbstractClientAppService implements Service<Map<String, Object>, Map<String, Object>> {

    /** logger */
    private static final Logger LOG = Logger.getLogger(AbstractClientAppService.class);
    
    /** Key for params: authuser */
    protected static final String K_P_AUTHUSER = "K_P_AUTHUSER";
    
    /** Common DAO */
    private CommonDaoEx commonDao = null;
    /** AuthToken Service */
    private AuthTokenService authTokenService = null;

    @Override
    public Map<String, Object> service(Map<String, Object> params) {
        if (isAuth()) {
            checkAuthToken(params);
        }

        return execute(params);
    }

    private void checkAuthToken(Map<String, Object> params) {
        String tokenId = (String) params.get("tokenId");
        if (StringUtil.isTrimEmpty(tokenId)) {
            throw new AppReqException(PTConsts.RESPCD_E904);
        }

        Map<String, Object> user = this.authTokenService.checkAndGetUser(tokenId);
        if (user == null) {
            throw new AppReqException(PTConsts.RESPCD_E904);
        }
        
        if (LOG.isInfoEnabled()) {
            LOG.info("Req mobile:" + user.get("mobile"));
        }

        // hold
        params.put(K_P_AUTHUSER, user);
    }

    /**
     * Execute
     *
     * @param params parameters
     * @return
     */
    protected abstract Map<String, Object> execute(Map<String, Object> params);

    /**
     * is Auth
     *
     * @return
     */
    protected abstract boolean isAuth();

    @Override
    public String serviceModule() {
        return CommonConsts.MOD_APP_COMMAND;
    }

    /**
     * Extract Location List
     *
     * @param historyTrace
     * @return
     */
    @SuppressWarnings("unchecked")
    protected List<Map<String, Object>> extractLocList(
            Map<String, Object> historyTrace) {
        List<Map<String, Object>> posList = new ArrayList<>();
        List<Map<String, Object>> poss = (List<Map<String, Object>>) historyTrace.get("poss");
        
        if (poss != null) {
            Map<String, Object> myPos = null;
            //String ggv = null;
            for (Map<String, Object> pos : poss) {
                myPos = new HashMap<>();
                
                myPos.put("lat", pos.get("lat"));
                myPos.put("lng", pos.get("lng"));
                myPos.put("speed", pos.get("speed"));
                myPos.put("status", pos.get("status"));
                myPos.put("loc_status", pos.get("loc_status"));
                myPos.put("loc_time", pos.get("loc_time"));
                myPos.put("step", pos.get("step"));
                
                String gps = (String) pos.get("gps");
                if (StringUtil.isTrimEmpty(gps)) {
                    myPos.put("gps", CommonConsts.NO_DATA);
                } else {
                    myPos.put("gps", gps);
                }
                String vtg= (String) pos.get("vbat");
                if (StringUtil.isTrimEmpty(vtg)) {
                    myPos.put("vtg", CommonConsts.NO_DATA);
                } else {
                    myPos.put("vtg", vtg);
                }
                // hold
                posList.add(myPos);
            }
        }
        return posList;
    }

    /**
     * Check Mobile And Terminal
     *
     * @param mobile
     * @param terminalId
     */
    protected void checkMobileAndTreminal(String mobile,
            String terminalId) {
        Map<String, Object> qps = new HashMap<>();
        qps.put("terminalSerialNo", terminalId);
        qps.put("configKey", CommonConsts.TMN_CFG_USER);
        qps.put("configKey_login", CommonConsts.TMN_CFG_LOGIN);        
        qps.put("ownerTel", mobile);
        List<Map<String, Object>> terminalList = getCommonDao()
                .namingQueryForList("sql_app_getTerminalByOwner", qps);
        
        if (CommonUtil.isEmpty(terminalList)) {
            LOG.info("it's not ur terminal, mobile:" + mobile + " terminal:" + terminalId);
            throw new AppReqException(PTConsts.RESPCD_E005);
        }

    }

    
    
    
    /**
     * setter
     * @param commonDao the commonDao to set
     */
    public void setCommonDao(CommonDaoEx commonDao) {
        this.commonDao = commonDao;
    }


    /**
     * getter
     * @return the commonDao
     */
    public CommonDaoEx getCommonDao() {
        return commonDao;
    }

    /**
     * getter
     * @return the authTokenService
     */
    public AuthTokenService getAuthTokenService() {
        return authTokenService;
    }

    /**
     * setter
     * @param authTokenService the authTokenService to set
     */
    public void setAuthTokenService(AuthTokenService authTokenService) {
        this.authTokenService = authTokenService;
    }


}
