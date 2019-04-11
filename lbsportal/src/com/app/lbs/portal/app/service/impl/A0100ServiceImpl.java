/*
 * LBSAPP
 */
package com.app.lbs.portal.app.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.app.lbs.common.CommonConsts;
import com.app.lbs.common.facade.RemoteInvokeFacade;
import com.app.lbs.common.remote.CallRequest;
import com.app.lbs.common.remote.CallResponse;
import com.app.lbs.portal.PTConsts;
import com.app.lbs.portal.app.AppReqException;
import com.app.lbs.portal.app.service.AbstractClientAppService;
import com.app.lbs.portal.app.service.AuthCodeManagerService;
import com.app.platform.base.utils.EncryptUtil;
import com.app.platform.base.utils.SystemConfig;

/**
 * User Register Service
 *
 * @author luxinxian
 *
 */
public class A0100ServiceImpl extends AbstractClientAppService {

    /** logger */
    private static final Logger LOG = Logger.getLogger(A0130ServiceImpl.class);

    @Override
    protected Map<String, Object> execute(Map<String, Object> params) {

        String mobile = (String) params.get("mobile");
        String authCode = (String) params.get("authcode");
        // check AuthCode
        if (!this.authCodeManager.isRigth(mobile, authCode)) {
            LOG.info("Not rigth AuthCode: " + mobile + ", " + authCode);
            throw new AppReqException(PTConsts.RESPCD_E002);
        }
        
        // check mobile
        int cnt = getCommonDao().queryForInt("select count(1) as total from T_APP_USER where MOBILE=?", mobile);
        if (cnt > 0) {
            LOG.info("Mobile exists: " + mobile);
            throw new AppReqException(PTConsts.RESPCD_E001);
        }

        // create app user
        CallRequest request = new CallRequest();
        request.setActType(CommonConsts.ACTTYPE_GET_SEQ);
        request.set("seqId", SystemConfig.get("seq.appuid"));
        CallResponse response = RemoteInvokeFacade.call(request);
        if (response == null) {
            throw new AppReqException(PTConsts.RESPCD_E901);
        }
        
        Map<String, Object> user = new HashMap<String, Object>();
        user.put("appuid", Long.valueOf(response.get("nextSeqValue").toString()));
        user.put("mobile", mobile);
        user.put("name", mobile);
        user.put("password", EncryptUtil.toSHA256((String) params.get("passwd")));

        //Date now = DateUtil.getCurrentDate();
        //user.put("create_date", now);
        //user.put("update_date", now);

        // store
        getCommonDao().namingUpdate("sql_app_addAppUser", user);

        return null;
    }


    @Override
    protected boolean isAuth() {
        return false;
    }


    @Override
    public String serviceKey() {
        return "0100";
    }

    private AuthCodeManagerService authCodeManager = null;

    /**
     * setter
     * @param authCodeManager the authCodeManager to set
     */
    public void setAuthCodeManager(AuthCodeManagerService authCodeManager) {
        this.authCodeManager = authCodeManager;
    }
}
