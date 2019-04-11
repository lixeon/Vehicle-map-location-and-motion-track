/*
 * LBSAPP
 */
package com.app.lbs.portal.app.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.app.lbs.portal.PTConsts;
import com.app.lbs.portal.app.AppReqException;
import com.app.lbs.portal.app.service.AbstractClientAppService;
import com.app.lbs.portal.app.service.AuthCodeManagerService;
import com.app.platform.base.utils.DateUtil;
import com.app.platform.base.utils.EncryptUtil;

/**
 * Update User's Password (AuthCode User)
 *
 * @author luxinxian
 *
 */
public class A0150ServiceImpl extends AbstractClientAppService {

    /** logger */
    private static final Logger LOG = Logger.getLogger(A0110ServiceImpl.class);

    @Override
    protected Map<String, Object> execute(Map<String, Object> params) {

        String mobile = (String) params.get("mobile");
        String authCode = (String) params.get("authcode");
        
        if (LOG.isInfoEnabled()) {
            LOG.info("Verify AuthCode, mobile = " + mobile + ", authCode = " + authCode);
        }

        boolean right = this.authCodeManager.isRigth(mobile, authCode);

        if (!right) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Not right AuthCode, " + authCode);
            }
            
            throw new AppReqException(PTConsts.RESPCD_E002);
        }
        
        if (LOG.isInfoEnabled()) {
            LOG.info("Auth User update password: mobile=" + mobile);
        }

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("mobile", mobile);
        // set new password
        String pwd = (String) params.get("pwd");
        args.put("password", EncryptUtil.toSHA256(pwd));
        args.put("updateDate", DateUtil.getCurrentDate());

        // update
        getCommonDao().namingUpdate("sql_app_updateAppUser", args);

        return null;
    }


    @Override
    public String serviceKey() {
        return "0150";
    }

    @Override
    protected boolean isAuth() {
        return false;
    }

    /** AuthCode Manager */
    private AuthCodeManagerService authCodeManager = null;

    /**
     * setter
     * @param authCodeManager the authCodeManager to set
     */
    public void setAuthCodeManager(AuthCodeManagerService authCodeManager) {
        this.authCodeManager = authCodeManager;
    }
}
