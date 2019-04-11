/*
 * LBSAPP
 */
package com.app.lbs.portal.app.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.app.lbs.portal.app.service.AbstractClientAppService;

/**
 * Get User's Info
 *
 * @author luxinxian
 *
 */
public class A0120ServiceImpl extends AbstractClientAppService {


    /** logger */
    private static final Logger LOG = Logger.getLogger(A0110ServiceImpl.class);
    
    
    @SuppressWarnings("unchecked")
    @Override
    protected Map<String, Object> execute(Map<String, Object> params) {
        
        Map<String, Object> user = (Map<String, Object>) params.get(K_P_AUTHUSER);
        String mobile = (String) user.get("mobile");

        if (LOG.isInfoEnabled()) {
            LOG.info("will get user info, mobile = " + mobile);
        }
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("mobile", mobile);
        Map<String, Object> gotUser = getCommonDao().namingQueryForMap("sql_app_getAppUser", args);

        Map<String, Object> output = new HashMap<>();
        output.put("mobile", mobile);
        output.put("name", gotUser.get("name"));
        output.put("gender", gotUser.get("gender"));
        output.put("province", gotUser.get("province_id"));
        output.put("city", gotUser.get("city_id"));

        if (LOG.isInfoEnabled()) {
            LOG.info("userinfo = " + output);
        }
        
        return output;
    }

    @Override
    public String serviceKey() {
        return "0120";
    }

    @Override
    protected boolean isAuth() {
        return true;
    }

}
