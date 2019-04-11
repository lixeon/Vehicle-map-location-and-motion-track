/*
 * LBSAPP
 */
package com.app.lbs.portal.app.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.app.lbs.common.CommonConsts;
import com.app.lbs.common.service.Service;
import com.app.lbs.common.servlet.AbstractServletServer;
import com.app.lbs.portal.PTConsts;
import com.app.lbs.portal.app.AppReqException;
import com.app.platform.base.utils.DateUtil;

/**
 * Client APP Server
 *
 * @author luxinxian
 *
 */
public class ClientAppServer extends AbstractServletServer {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /** logger */
    private static final Logger LOG = Logger.getLogger(ClientAppServer.class);

    /** request header 'x-app-authtoken' */
  //  private static final String HEADER_AUTHTOKEN = "x-app-authtoken";
    private static final String K_R_RESPCODE = "respcode";
    private static final String K_R_TM = "timestamp";

    @SuppressWarnings("unchecked")
    @Override
    protected void handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> params = this.toolkits.getReqParams(request);
      //  String tokenId = request.getHeader(HEADER_AUTHTOKEN);
     //   params.put("tokenId", tokenId);

     //   if (LOG.isInfoEnabled()) {
     //       LOG.info("App-Req: " + params);
     ///   }

        String action = (String) params.get("action");
        Service<Map<String, Object>, Map<String, Object>> service =
                (Service<Map<String, Object>, Map<String, Object>>) this.getServiceMap().get(action);

        Map<String, Object> respMap = getDefaultResp();;
        if (service == null) {
            LOG.warn("Cannot find the service for this action : " + action);
            respMap.put(K_R_RESPCODE, PTConsts.RESPCD_E903);
        } else {
            try {
                // execute service
                Map<String, Object> resp = service.service(params);
                if (resp != null) {
                    // copy
                    respMap.putAll(resp);
                }
            } catch (Exception e) {
                if (e instanceof AppReqException) {
                    LOG.warn("AppReqException: " + e.getMessage());
                    respMap.put(K_R_RESPCODE, e.getMessage());
                } else {
                    LOG.error("Service execute Error!", e);
                    respMap.put(K_R_RESPCODE, PTConsts.RESPCD_E901);
                }
            }
        }

    //    if (LOG.isInfoEnabled()) {
    //        LOG.info("Plat-Resp: " + respMap);
     //   }

        // hold tokenId
        this.toolkits.outputJSON(response, respMap);
    }

    
    @Override
    public String getModule() {
        return CommonConsts.MOD_APP_COMMAND;
    }


    /**
     * get default response.
     *
     * @return default
     */
    public Map<String, Object> getDefaultResp() {
        Map<String, Object> resp = new HashMap<String, Object>();
        resp.put(K_R_RESPCODE, PTConsts.RESPCD_SUCCESS);
        resp.put(K_R_TM, DateUtil.getDate(DateUtil.DATE_FORMAT_1));
        return resp;
    }
}
