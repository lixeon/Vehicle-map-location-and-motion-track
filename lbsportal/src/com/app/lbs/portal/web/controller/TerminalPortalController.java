/*
 * lbspt
 */
package com.app.lbs.portal.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.app.lbs.common.CodeConst;
import com.app.lbs.common.CommonConsts;
import com.app.lbs.common.facade.RemoteInvokeFacade;
import com.app.lbs.common.remote.CallRequest;
import com.app.lbs.common.remote.CallResponse;
import com.app.lbs.common.web.controller.WebCommonController;
import com.app.lbs.portal.web.service.TerminalPortalService;
import com.app.platform.base.utils.DateUtil;
import com.app.platform.base.utils.StringUtil;

/**
 * Terminal Portal Controller
 *
 * @author colen
 *
 */
@Controller(value="portal_TerminalPortalController")
public class TerminalPortalController extends WebCommonController {

    /** logger */
    private static final Logger LOG = Logger.getLogger(TerminalPortalController.class);

    /**
     * open main page
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return model and view
     */
    @RequestMapping("portal/terminal_portal.shtm")
    public ModelAndView showMainPage(HttpServletRequest request,
            HttpServletResponse response) {
        
        LOG.info("open main page (terminal) start...");
        
        // get parameter from request first
        // and then check the parameter from cookie
        String terminalSeialNo = request.getParameter("terminalSerialNo");
        if (StringUtil.isTrimEmpty(terminalSeialNo)) {
            terminalSeialNo = getCookieValue(request, CK_K_TMNID, false);
        }
        
        String cityId = request.getParameter("cityId");
        if (StringUtil.isTrimEmpty(cityId)) {
            cityId = getCookieValue(request, CK_K_CITYID, false);
        }

        if (LOG.isInfoEnabled()) {
            LOG.info("terminalSeialNo:" + terminalSeialNo + ", cityId:" + cityId);
        }

      
        // get city info
        Map<String, Object> city = this.service.getCityInfoById(cityId);

        ModelAndView mv = new ModelAndView();
        mv.addObject("terminal_serial_no", terminalSeialNo);
        // set to page
        mv.addObject("city", city);
        // server time
        mv.addObject("server_time", DateUtil.getDate(DateUtil.DATE_FORMAT_4));

        // map key
        String mapType = request.getParameter("mapType");
        mv.addObject("mapCnf", getMapCnf((String) city.get("city_id"), mapType));

        // show mode
        mv.addObject("mode", request.getParameter("mode"));
        
        // set view
        mv.setViewName("main/p00_tmn_portal.jsp");

        LOG.info("open main page (terminal) succesfully!");
        
        return mv;
    }



    /**
     * Load Terminal Info
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping("portal/load_terminals.shtm")
    public void loadTerminalInfo(HttpServletRequest request,
            HttpServletResponse response) {
        
        LOG.info("load terminal info start...");
        
        // get terminal serial no
        String terminalSerialNo = request.getParameter("terminalSerialNo");
        
        if (LOG.isInfoEnabled()) {
            LOG.info("load terminal info: " + terminalSerialNo);
        }
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("terminalSerialNo", terminalSerialNo);
        // get terminal info
        this.service.getTerminalInfoByTmnId(params);

        Map<String, Object> output = getDefaultResult();
        output.put("terminal", params.get("terminal"));
        outputJSON(response, output);

        LOG.info("load terminal info successfully!");
    }

    
    /**
     * Get Latest Terminal Status
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping("portal/get_latest_status.shtm")
    public void getLatestTerminalStatus(HttpServletRequest req,
            HttpServletResponse resp) {
        LOG.info("Get Latest Terminal Status start...");
        
        String terminalSerialNo = req.getParameter("terminalSerialNo");
        
        if (LOG.isInfoEnabled()) {
            LOG.info("will get status of terminal : " + terminalSerialNo);
        }

        // get terminal status
        CallRequest request = new CallRequest();
        request.setActType(CommonConsts.ACTTYPE_001);
        request.set("terminalSerialNo", terminalSerialNo);

        CallResponse response = RemoteInvokeFacade.call(request);

        Map<String, Object> status = null;
        if (response != null && response.getData().get("hbTime") != null) {
            status = response.getData();
            // if terminal offline, set the status value as 900
            if (Boolean.FALSE.equals(status.get("login"))) {
                status.put("status", "900");
            }
        } else {
            status = new HashMap<>();
            // if no status got, just set the terminal's status as 900 (offline)
            status.put("status", "900");
            status.put("hbTime", "无法获取");
        }
        
        if (LOG.isInfoEnabled()) {
            LOG.info("status: " + status.get("status"));
        }
        
        outputJSON(resp, status);
        
        LOG.info("Get Latest Terminal Status end.");
    }


    /**
     * Send ACCLOCK Setting Command
     *
     * @param request Request
     * @param response Response
     */
    @RequestMapping("portal/send_protected_cmd.shtm")
    public void sendProtectedCmd(HttpServletRequest request,
            HttpServletResponse response) {
        LOG.info("send Protected command start...");
        
        Map<String, Object> params = getReqParams(request);
        Map<String, String> args = new HashMap<String, String>();
        // default : set protected
        String cmd = CommonConsts.CMD_S12;
        String offon = (String) params.get("offon");
        if (CodeConst.TERMINLA_R_STATUS_UNPT.equals(offon)) {
            cmd = CommonConsts.CMD_S13;
        }
        args.put("cmd", cmd);
        args.put("terminal_serial_no", (String) params.get("terminalId"));

        // send command
        RemoteInvokeFacade.sendTerminalCmd(args);

        outputJSON(response, getDefaultResult());

        LOG.info("send Protected command finished.");
    }


    /**
     * Send locate Command
     *
     * @param request Request
     * @param response Response
     */
    @RequestMapping("portal/send_locate_cmd.shtm")
    public void sendLocateCmd(HttpServletRequest request,
            HttpServletResponse response) {
        LOG.info("send locate command terminal start...");
        
        Map<String, Object> params = getReqParams(request);
        Map<String, String> args = new HashMap<String, String>();
        args.put("cmd", CommonConsts.CMD_S10);
        args.put("terminal_serial_no", (String) params.get("terminalId"));
        
        // send command
        RemoteInvokeFacade.sendTerminalCmd(args);

        Map<String, Object> output = getDefaultResult();
        output.put("sendTime", DateUtil.getDate(DateUtil.DATE_FORMAT_4));
        outputJSON(response, output);

        LOG.info("send locate command terminal finished.");
    }


    /**
     * Query Terminal Configuration Command
     *
     * @param request Request
     * @param response Response
     */
    @RequestMapping("terminal/query_config_cmd.shtm")
    public void queryTerminalConfigCmd(HttpServletRequest request,
            HttpServletResponse response) {
        LOG.info("queryTerminalConfigCmd start...");
        
        Map<String, Object> params = getReqParams(request);
        Map<String, String> args = new HashMap<String, String>();
        args.put("cmd", CommonConsts.CMD_S14);
        args.put("terminal_serial_no", (String) params.get("terminalSerialNo"));
        args.put("pk", (String) params.get("configKey"));

        // send command
        RemoteInvokeFacade.sendTerminalCmd(args);

        Map<String, Object> output = getDefaultResult();
        output.put("queryTime", DateUtil.getDate(DateUtil.DATE_FORMAT_4));
        outputJSON(response, output);

        LOG.info("queryTerminalConfigCmd finished.");
    }

    
    
    /**
     * Restart Terminal
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     */
    @RequestMapping("portal/restart_terminal.shtm")
    public void restartTerminal(HttpServletRequest req,
            HttpServletResponse resp) {
        LOG.info("restartTerminal start...");
        
        String terminalSerialNo = req.getParameter("terminalSerialNo");
        
        if (LOG.isInfoEnabled()) {
            LOG.info("will restart terminal : " + terminalSerialNo);
        }

        // restart terminal
        Map<String, String> args = new HashMap<String, String>();
        args.put("cmd", CommonConsts.CMD_S11);
        args.put("terminal_serial_no", terminalSerialNo);

        // send command
        RemoteInvokeFacade.sendTerminalCmd(args);

        // default success
        Map<String, Object> output = getDefaultResult();
        outputJSON(resp, output);
        
        LOG.info("restartTerminal end.");
    }

    /**
     * Switch Trace Terminal
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping("portal/switch_trace.shtm")
    public void switchTraceTerminal(HttpServletRequest request,
            HttpServletResponse response) {
        LOG.info("switch trace terminal start...");
        
        Map<String, Object> params = getReqParams(request);

        Map<String, String> args = new HashMap<String, String>();
        args.put("cmd", CommonConsts.CMD_S2);
        args.put("terminal_serial_no", (String) params.get("terminalId"));
        args.put("pk", CommonConsts.TMN_CFG_TRACE);
        args.put("pv", (String) params.get("offon"));

        // send command
        RemoteInvokeFacade.sendTerminalCmd(args);

        outputJSON(response, getDefaultResult());
        
        LOG.info("switch trace terminal finished.");
    }


    /**
     * Switch Trace Terminal
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping("portal/get_latest_trace.shtm")
    public void getLatestTraceLocation(HttpServletRequest request,
            HttpServletResponse response) {
        LOG.info("get latest trace log start...");

        // parameters
        Map<String, Object> params = getReqParams(request);

        // query trace
        this.service.getLatestTrace(params);

        Map<String, Object> output = getDefaultResult();

        // store
        output.put("locs", params.get("locs"));
        outputJSON(response, output);
        
        LOG.info("get latest trace log finished.");
    }
    
    
    /**
     * get history locations
     *
     * @param request
     * @param response
     */
    @RequestMapping("portal/get_history_locations.shtm")
    public void getHistoryLocations(HttpServletRequest request,
            HttpServletResponse response) {
        LOG.info("get history locations start.. ");
        
        // parameters
        Map<String, Object> params = getReqParams(request);

        // get history locations
        this.service.getHistoryLocations(params);

        Map<String, Object> output = getDefaultResult();

        // store
        output.put("locs", params.get("locs"));
        outputJSON(response, output);
        
        LOG.info("get history locations finished.");
    }

    
    /**
     * get month data hint
     *
     * @param request
     * @param response
     */
    @RequestMapping("portal/load_month_data_hint.shtm")
    public void getMonthDataHint(HttpServletRequest request,
            HttpServletResponse response) {
        LOG.info("get month data start.. ");
        
        // parameters
        Map<String, Object> params = getReqParams(request);
        
        // get history locations
        this.service.getMonthDataHint(params);

        Map<String, Object> output = getDefaultResult();
        // store
        output.put("monthDataHint", params.get("monthDataHint"));
        outputJSON(response, output);

        LOG.info("get month data end. ");
    }
    
    
    /**
     * get Date data hint
     *
     * @param request
     * @param response
     */
    @RequestMapping("portal/load_date_data_hint.shtm")
    public void getDateDataHint(HttpServletRequest request,
            HttpServletResponse response) {
        LOG.info("get date data start.. ");
        
        // parameters
        Map<String, Object> params = getReqParams(request);
        
        // get history locations
        this.service.getDateDataHint(params);

        Map<String, Object> output = getDefaultResult();
        // store
        output.put("dateDataHint", params.get("dateDataHint"));
        outputJSON(response, output);

        LOG.info("get date data end. ");
    }
    
    
 @RequestMapping({"portal/modify_nickname.shtm"})
public void modifyName(HttpServletRequest req, HttpServletResponse resp)
 {
 		LOG.info("modify terminal nickname start...");
   
 		Map<String, Object> params = getReqParams(req);
 		String terminalSeialNo = getCookieValue(req, "tmnId", false);
		params.put("terminalSerialNo", terminalSeialNo);
  

 		this.service.modifyTerminalName(params);
  

		Map<String, Object> output = getDefaultResult();
	 	outputJSON(resp, output);

		LOG.info("modify terminal nickname end.");
	}
    /** Main Service */
    @Resource(name = "portal_TerminalPortalService")
    private TerminalPortalService service = null;

}
