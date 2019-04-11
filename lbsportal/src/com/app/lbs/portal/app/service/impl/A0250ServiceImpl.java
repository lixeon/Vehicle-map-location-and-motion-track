/*
 * LBSAPP
 */
package com.app.lbs.portal.app.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.app.lbs.common.CommonConsts;
import com.app.lbs.common.exception.CommonBusinessException;
import com.app.lbs.common.facade.RemoteInvokeFacade;
import com.app.lbs.portal.PTConsts;
import com.app.lbs.portal.app.AppReqException;
import com.app.lbs.portal.app.service.AbstractClientAppService;

/**
 * Restart Terminal
 *
 * @author luxinxian
 *
 */
public class A0250ServiceImpl extends AbstractClientAppService {

    /** logger */
    private static final Logger LOG = Logger.getLogger(A0250ServiceImpl.class);


    @SuppressWarnings("unchecked")
    @Override
    protected Map<String, Object> execute(Map<String, Object> params) {

        Map<String, Object> user = (Map<String, Object>) params.get(K_P_AUTHUSER);
        String mobile = (String) user.get("mobile");
        String terminalId = (String) params.get("terminal_id");

        if (LOG.isInfoEnabled()) {
            LOG.info("mobile " + mobile + " will restart terminal " + terminalId);
        }

        // check
        checkMobileAndTreminal(mobile, terminalId);

        // restart terminal
        Map<String, String> args = new HashMap<String, String>();
        args.put("cmd", CommonConsts.CMD_S11);
        args.put("terminal_serial_no", terminalId);

        try {
            // send command
            RemoteInvokeFacade.sendTerminalCmd(args);
        } catch (CommonBusinessException cbe) {
            throw new AppReqException(PTConsts.RESPCD_E005);
        }

        return null;
    }


    @Override
    public String serviceKey() {
        return "0250";
    }


    @Override
    protected boolean isAuth() {
        return true;
    }

}
