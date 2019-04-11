/*
 * LBSAPP
 */
package com.app.lbs.portal.app.service.impl;

import java.util.Map;

import org.apache.log4j.Logger;

import com.app.lbs.portal.PTConsts;
import com.app.lbs.portal.app.AppReqException;
import com.app.lbs.portal.app.service.AbstractClientAppService;
import com.app.lbs.portal.app.service.AuthCodeManagerService;

/**
 * Verify AuthCode.
 *
 * @author luxinxian
 *
 */
public class A0010ServiceImpl extends AbstractClientAppService {

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

        return null;
    }


    @Override
    public String serviceKey() {
        return "0010";
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
