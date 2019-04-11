/*
 * LBSAPP
 */
package com.app.lbs.portal.web.controller;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.app.lbs.common.web.controller.WebCommonController;
import com.app.lbs.portal.web.service.OrgCacheService;
import com.app.lbs.portal.web.service.SystemSetService;
import com.app.platform.base.utils.DateUtil;

/**
 * System Set Controller
 *
 * @author colen
 *
 */
@Controller("mgr_SystemSetController")
public class SystemSetController extends WebCommonController {

    /** Logger */
    private static final Logger LOG = Logger.getLogger(SystemSetController.class);
    
    /**
     * Open System Set Page
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @return 
     */
    @RequestMapping("sysset/open.shtm")
    public ModelAndView open(HttpServletRequest req,
            HttpServletResponse resp) {
        LOG.info("Open System Set Page start...");
        
        ModelAndView mv = createOpenPageMV(req);
        
        String module = req.getParameter("m");

        if (LOG.isInfoEnabled()) {
            LOG.info("module = " + module);
        }

        
        if (MODULE_QUERY.equals(module)) {
            // nothing here.
        } else if ("sms_setting".equals(module)) {
            initSMSSetting(mv);
        } else {
            LOG.warn("no module found!");
        }

        // set view
        mv.setViewName("sysset/p_" + module + ".jsp");

        LOG.info("Open System Set Page end.");
        return mv;
    }


    private void initSMSSetting(ModelAndView mv) {
        Map<String, Map<String, Object>> confMap = this.service.getCurSmsSetting();
        mv.addObject("fee", confMap.get("sms.ms.check.fee").get("value"));
        mv.addObject("fee_update_time", DateUtil.format((Date) confMap.get("sms.ms.check.fee").get("update_time"), DateUtil.DATE_FORMAT_1));
        mv.addObject("lowlevel", confMap.get("sms.ms.check.lowlevel").get("value"));
        mv.addObject("reminder", confMap.get("sms.ms.check.reminder").get("value"));
    }
    
    
    /**
     * Open System Set Page
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @return 
     */
    @RequestMapping("sysset/update_sms_settings.shtm")
    public void updateSmsSettings(HttpServletRequest req,
            HttpServletResponse resp) {

        Map<String, Object> params = getReqParams(req);
        
        this.service.updateSmsSettings(params);
        
        // default success
        Map<String, Object> output = getDefaultResult();
        outputJSON(resp, output);
    }
    
    
	@RequestMapping({"sysset/refresh_terminalgroup.shtm"})
  	public void refreshTerminalOrgCache(HttpServletRequest req, HttpServletResponse resp)
 	{
		this.orgCacheService.doRefresh();
 		Map<String, Object> output = getDefaultResult();
		outputJSON(resp, output);
  	}

    @Resource(name = "manager_SystemSetService")
    private SystemSetService service = null;
  	@Resource(name="pub_OrgCacheService")
	private OrgCacheService orgCacheService = null;
}
