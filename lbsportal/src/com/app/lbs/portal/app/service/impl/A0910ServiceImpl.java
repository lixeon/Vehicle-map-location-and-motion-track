/*
 * LBSAPP
 */
package com.app.lbs.portal.app.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.app.lbs.portal.app.service.AbstractClientAppService;

/**
 * Check app's Software
 *
 * @author luxinxian
 *
 */
public class A0910ServiceImpl extends AbstractClientAppService {

    /** logger */
    private static final Logger LOG = Logger.getLogger(A0910ServiceImpl.class);

    @Override
    protected Map<String, Object> execute(Map<String, Object> params) {

        String clientType = (String) params.get("client_type");

        Map<String, Object> swInfo = getCommonDao().queryForMap("select * from T_APP_SOFTWARE where CLIENT_TYPE = ?", clientType);

        Map<String, Object> output = new HashMap<String, Object>();
        output.put("ver", swInfo.get("ver"));
        output.put("url", swInfo.get("download_url"));

        if (LOG.isInfoEnabled()) {
            LOG.info("latest version for client type " + clientType + " is " + output);
        }

        return output;
    }


    @Override
    public String serviceKey() {
        return "0910";
    }


    @Override
    protected boolean isAuth() {
        return false;
    }

}
