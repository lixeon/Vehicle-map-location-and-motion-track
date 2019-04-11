/*
 * LBSAPP
 */
package com.app.lbs.portal.app.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.app.lbs.portal.PTConsts;
import com.app.lbs.portal.app.AppReqException;
import com.app.lbs.portal.app.service.AbstractClientAppService;
import com.app.platform.base.utils.DateUtil;
import com.app.platform.base.utils.EncryptUtil;
import com.app.platform.base.utils.StringUtil;

/**
 * User Login
 *
 * @author luxinxian
 *
 */
public class A0130ServiceImpl extends AbstractClientAppService {

    /** logger */
    private static final Logger LOG = Logger.getLogger(A0130ServiceImpl.class);

    @Override
    protected Map<String, Object> execute(Map<String, Object> params) {

        String mobile = StringUtil.trim((String) params.get("mobile"));
        String passwd = StringUtil.trim((String) params.get("passwd"));

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("mobile", mobile);
        args.put("password", EncryptUtil.toSHA256(passwd));
        Map<String, Object> user = getCommonDao().namingQueryForMap("sql_app_getAppUser", args);
        if (user == null) {
            LOG.info("not right mobile or passwd: " + mobile);
            throw new AppReqException(PTConsts.RESPCD_E004);
        }

        // update login date
        args.clear();
        args.put("mobile", mobile);
        Date now = DateUtil.getCurrentTime();
        args.put("loginDate", now);
        args.put("updateDate", now);
        getCommonDao().namingUpdate("sql_app_updateAppUser", args);

        // create token id
        String tokenId = getAuthTokenService().createTokenId(user);

        Map<String, Object> output = new HashMap<>();
        output.put("tokenid", tokenId);

        return output;
    }
    
    @Override
    public String serviceKey() {
        return "0130";
    }

    @Override
    protected boolean isAuth() {
        return false;
    }

}
