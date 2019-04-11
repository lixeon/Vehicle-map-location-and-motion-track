/*
 * LBSAPP
 */
package com.app.lbs.portal.app.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.app.lbs.common.CodeConst;
import com.app.lbs.common.CommonConsts;
import com.app.lbs.common.exception.CommonBusinessException;
import com.app.lbs.common.facade.RemoteInvokeFacade;
import com.app.lbs.common.process.ProcessTask;
import com.app.lbs.common.process.ProcessorFactory;
import com.app.lbs.common.remote.CallRequest;
import com.app.lbs.portal.PTConsts;
import com.app.lbs.portal.app.AppReqException;
import com.app.lbs.portal.app.service.AbstractClientAppService;

/**
 * Set Terminal Configuration
 *
 * @author luxinxian
 *
 */
public class A0270ServiceImpl extends AbstractClientAppService {

    /** logger */
    private static final Logger LOG = Logger.getLogger(A0250ServiceImpl.class);


    @SuppressWarnings("unchecked")
    @Override
    protected Map<String, Object> execute(Map<String, Object> params) {
        
        Map<String, Object> user = (Map<String, Object>) params.get(K_P_AUTHUSER);
        String mobile = (String) user.get("mobile");
        String terminalId = (String) params.get("terminal_id");

        if (LOG.isInfoEnabled()) {
            LOG.info("mobile (" + mobile + ") will modify terminal's configuration");
        }

        // check
        checkMobileAndTreminal(mobile, terminalId);

        String pKey = (String) params.get("p_key");
        String pValue = (String) params.get("p_value");

        if (CommonConsts.TMN_CFG_PROTECTED.equals(pKey)) {
            handleProtectedSettings(terminalId, pKey, pValue);
        } else {
            handleOtherSettings(terminalId, pKey, pValue);
        }

        return null;
    }


    private void handleOtherSettings(final String terminalId, String pKey, String pValue) {
        Map<String, Object> args = new HashMap<>();
        args.put("terminalSerialNo", terminalId);
        args.put("status", CodeConst.C_CONFIGSET_NO);
        args.put("updTime", new Date());
        args.put("configKey", pKey);
        args.put("value", pValue);
        // update
        getCommonDao().namingUpdate("sql_portal_updateTerminalConfig", args, true);

        // sync
        ProcessorFactory.getProcessor(PTConsts.PROCESSOR_SYNC_CONFIG).addTask(new ProcessTask() {
            @Override
            public void process() {
                // send sync-configuration Command.
                CallRequest request = new CallRequest();
                request.setActType(CommonConsts.ACTTYPE_004);
                request.set("terminalSerialNo", terminalId);
                // execute sync
                RemoteInvokeFacade.call(request);
            }
        });
    }


    private void handleProtectedSettings(String terminalId, String pKey, String pValue) {
        Map<String, String> args = new HashMap<String, String>();
        // default : set protected
        String cmd = CommonConsts.CMD_S12;
        if (CodeConst.TERMINLA_R_STATUS_UNPT.equals(pValue)) {
            cmd = CommonConsts.CMD_S13;
        }
        args.put("cmd", cmd);
        args.put("terminal_serial_no", terminalId);

        try {
            // send command
            RemoteInvokeFacade.sendTerminalCmd(args);
        } catch (CommonBusinessException cbe) {
            throw new AppReqException(PTConsts.RESPCD_E005);
        }
    }


    @Override
    public String serviceKey() {
        return "0270";
    }


    @Override
    protected boolean isAuth() {
        return true;
    }

}
