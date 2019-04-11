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
import com.app.platform.base.utils.DateUtil;
import com.app.platform.base.utils.EncryptUtil;

/**
 * Update User's Password (Login User)
 *
 * @author luxinxian
 *
 */
public class A0140ServiceImpl extends AbstractClientAppService {

    /** logger */
    private static final Logger LOG = Logger.getLogger(A0110ServiceImpl.class);

    @SuppressWarnings("unchecked")
    @Override
    protected Map<String, Object> execute(Map<String, Object> params) {
        
        Map<String, Object> user = (Map<String, Object>) params.get(K_P_AUTHUSER);
        String mobile = (String) user.get("mobile");
        String oldpwd = (String) params.get("oldpwd");
        String newpwd = (String) params.get("newpwd");

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("mobile", mobile);
        args.put("password", EncryptUtil.toSHA256(oldpwd));
        Map<String, Object> gotUser = getCommonDao().namingQueryForMap("sql_app_getAppUser", args);
        if (gotUser == null) {
            LOG.info("Old password not right: " + mobile);
            throw new AppReqException(PTConsts.RESPCD_E003);
        }

        // set new password
        args.put("password", EncryptUtil.toSHA256(newpwd));
        args.put("updateDate", DateUtil.getCurrentDate());
        getCommonDao().namingUpdate("sql_app_updateAppUser", args);

        return null;
    }


    @Override
    public String serviceKey() {
        return "0140";
    }

    @Override
    protected boolean isAuth() {
        return true;
    }

}
