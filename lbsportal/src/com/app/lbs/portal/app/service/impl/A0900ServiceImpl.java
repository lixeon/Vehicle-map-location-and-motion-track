/*
 * LBSAPP
 */
package com.app.lbs.portal.app.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.app.lbs.portal.app.service.AbstractClientAppService;

/**
 * User's feedback
 *
 * @author luxinxian
 *
 */
public class A0900ServiceImpl extends AbstractClientAppService {

    /** logger */
    private static final Logger LOG = Logger.getLogger(A0900ServiceImpl.class);

    @Override
    protected Map<String, Object> execute(Map<String, Object> params) {

        String mobile = (String) params.get("mobile");
        String clientType = (String) params.get("client_type");
        String ver = (String) params.get("ver");
        String content = (String) params.get("content");

        Map<String, Object> args = new HashMap<>();
        args.put("mobile", mobile);
        args.put("clientType", clientType);
        args.put("ver", ver);
        args.put("content", content);
        args.put("extField1", "");
        args.put("extField2", "");

        if (LOG.isInfoEnabled()) {
            LOG.info("add new feedback: " + args);
        }
        
        // add
        getCommonDao().namingUpdate("sql_app_addFeedback", args, true);
        
        return null;
    }


    @Override
    public String serviceKey() {
        return "0900";
    }


    @Override
    protected boolean isAuth() {
        return false;
    }

}
