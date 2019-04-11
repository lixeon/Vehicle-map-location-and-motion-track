/**
 * 
 */
package com.app.lbs.portal.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.app.lbs.common.web.controller.WebCommonController;

/**
 * Manager User Portal
 *
 * @author colen
 *
 */
@Controller(value="mgr_MgrPortalController")
public class ManagerPortalController extends WebCommonController {

    /** logger */
    private static final Logger LOG = Logger.getLogger(TerminalPortalController.class);
    
    /**
     * Show Main Page
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("portal/mgr_portal.shtm")
    public ModelAndView showMainPage(HttpServletRequest request,
            HttpServletResponse response) {
        LOG.info("open main(manager) page start...");
        
        ModelAndView mv = new ModelAndView();
        
        // set view
        mv.setViewName("main/p01_mgr_portal.jsp");
        
        LOG.info("open main(manager) page finished.");
        
        return mv;
        
    }
    
    
    /**
     * Open modify password (Manager)
     *
     * @param request http request
     * @param response http response
     */
    @RequestMapping("manager/open_chg_pwd.shtm")
    public ModelAndView openChgPwdPage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = createOpenPageMV(request);
        mv.setViewName("common/p_chg_pwd.jsp");
        return mv;
    }

}
