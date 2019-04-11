/*
 * LBSAPP
 */
package com.app.lbs.portal.web.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.app.lbs.common.CodeConst;
import com.app.lbs.common.web.controller.WebCommonController;
import com.app.lbs.portal.web.service.TerminalAlarmService;
import com.app.platform.base.utils.StringUtil;

/**
 * Terminal Alarm Controller
 *
 * @author colen
 *
 */
@Controller("mgr_TerminalAlarmController")
public class TerminalAlarmController extends WebCommonController {

    /** Logger */
    private static final Logger LOG = Logger.getLogger(TerminalAlarmController.class);
    
    /**
     * Open Alarm Page
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @return 
     */
    @RequestMapping("alarm/open.shtm")
    public ModelAndView open(HttpServletRequest req,
            HttpServletResponse resp) {
        LOG.info("open Alarm page start...");
        
        ModelAndView mv = createOpenPageMV(req);
        
        String module = req.getParameter("m");

        if (LOG.isInfoEnabled()) {
            LOG.info("module = " + module);
        }
        
        if (MODULE_QUERY.equals(module)) {
            // nothing here.
        } else {
            LOG.warn("no module found!");
        }

        // set view
        mv.setViewName("terminal/p_alarm_" + module + ".jsp");

        LOG.info("open Alarm page end.");
        return mv;
    }
    
    
    /**
     * Query Alarm list
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @return 
     */
    @RequestMapping("alarm/query.shtm")
    public void query(HttpServletRequest req, HttpServletResponse resp) {
        LOG.info("open Alarm query start...");

        Map<String, Object> params = getReqParams(req);
        params.put("roleId", getCookieValue(req, CK_K_ROLE, false));
        //params.put("orgId", getCookieValue(req, CK_K_ORG, false));

        String orgId = (String) params.get("orgId");
        if (StringUtil.isTrimEmpty(orgId)) {
            params.put("orgId", getCookieValue(req, CK_K_ORG, false));
        }
        
        // query
        this.service.queryAlarmList(params);
        
        Map<String, Object> output = getDefaultResult();
        output.put("alarmList", params.get("alarmList"));
        output.put("totalCount", params.get("totalCount"));
        outputJSON(resp, output);
        
        LOG.info("open Alarm query end.");
    }

    
    /**
     * Query Alarm list
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @return 
     */
    @RequestMapping("alarm/query_smslist.shtm")
    public void querySmsList(HttpServletRequest req, HttpServletResponse resp) {
        LOG.info("querySmsList start...");
        
        Map<String, Object> params = getReqParams(req);
        params.put("roleId", getCookieValue(req, CK_K_ROLE, false));
        params.put("orgId", getCookieValue(req, CK_K_ORG, false));
        
        // send SMS : yes
        params.put("smsFlag", CodeConst.SMS_FLAG_Y);

        // query
        this.service.queryAlarmList(params);
        
        Map<String, Object> output = getDefaultResult();
        output.put("alarmList", params.get("alarmList"));
        output.put("totalCount", params.get("totalCount"));
        outputJSON(resp, output);
        
        LOG.info("querySmsList end.");
    }
    
    
    /**
     * Update Alarm Notify Status
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @return 
     */
    @RequestMapping("alarm/update_notify_status.shtm")
    public void updateNotifyStatus(HttpServletRequest req, HttpServletResponse resp) {
        LOG.info("updateNotifyStatus start...");
        
        Map<String, Object> params = getReqParams(req);

        // update
        this.service.updateNotifyStatus(params);
        
        Map<String, Object> output = getDefaultResult();
        outputJSON(resp, output);
        
        LOG.info("updateNotifyStatus end.");
    }
    
    
    @Resource(name = "manager_TerminalAlarmService")
    private TerminalAlarmService service = null;
}
