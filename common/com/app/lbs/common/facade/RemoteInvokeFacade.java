/*
 * LBSAPP
 */
package com.app.lbs.common.facade;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.app.lbs.common.CommonConsts;
import com.app.lbs.common.exception.CommonBusinessException;
import com.app.lbs.common.remote.CallRequest;
import com.app.lbs.common.remote.CallResponse;
import com.app.lbs.common.remote.RemoteCallService;
import com.app.platform.base.utils.AppContextHolder;
import com.app.platform.base.utils.HttpClientUtil;
import com.app.platform.base.utils.StringUtil;
import com.app.platform.base.utils.SystemConfig;

/**
 * Remote Call Facade
 *
 * @author colen
 *
 */
public class RemoteInvokeFacade {

    /** logger */
    private static final Logger LOG = Logger.getLogger(RemoteInvokeFacade.class);
    
    /**
     * Remote Call
     *
     * @param request Request
     * @return Response
     */
    public static CallResponse call(CallRequest request) {
        RemoteCallService remoteService = (RemoteCallService)
                AppContextHolder.getBean("common_GatewayRemoteCallService");
        CallResponse response = null;
        try {
            response = remoteService.call(request);
        } catch (Exception e) {
            LOG.error("remote call error! ", e);
        }

        return response;
    }
    
    
    /**
     * Send Terminal CMD
     *
     * @param args
     * @return
     */
    public static JSONObject sendTerminalCmd(Map<String, String> args) {
        JSONObject json = null;
        try {
            String respMsg = HttpClientUtil.post(SystemConfig.get("plt.cmdsender.url"), args);
            
            LOG.info("RespMessage: " + respMsg);
            
            json = JSON.parseObject(respMsg);
            String errorCode = json.getString(CommonConsts.K_P_ERROR_CODE);
            if (!StringUtil.isTrimEmpty(errorCode)) {
                LOG.warn("Send Command with error: " + errorCode);
                throw new CommonBusinessException("终端数据通道已断！");
            }
        } catch (IOException ioe) {
            LOG.error("Exception occour, when ", ioe);
            throw new CommonBusinessException("通信异常，无法发送请求！");
        }
        
        return json;
    }
}
