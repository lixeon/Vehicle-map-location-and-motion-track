/*
 * LBSAPP
 */
package com.app.lbs.portal.app.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.app.lbs.portal.app.service.AbstractClientAppService;

/**
 * Modify User's Info
 *
 * @author luxinxian
 *
 */
public class A0110ServiceImpl extends AbstractClientAppService {

    /** logger */
    private static final Logger LOG = Logger.getLogger(A0110ServiceImpl.class);


    @SuppressWarnings("unchecked")
    @Override
    protected Map<String, Object> execute(Map<String, Object> params) {
        
        Map<String, Object> user = (Map<String, Object>) params.get(K_P_AUTHUSER);
        String mobile = (String) user.get("mobile");

        if (LOG.isInfoEnabled()) {
            LOG.info("will modify user info, mobile = " + mobile);
        }

        Map<String, Object> args = new HashMap<>();
        args.putAll(params);
        args.put("mobile", mobile);

        // update
        getCommonDao().namingUpdate("sql_app_updateAppUser", args, true);

        return null;
    }

    @Override
    public String serviceKey() {
        return "0110";
    }

    @Override
    protected boolean isAuth() {
        return true;
    }

}
