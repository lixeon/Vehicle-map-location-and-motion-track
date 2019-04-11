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

import com.app.lbs.common.web.controller.WebCommonController;
import com.app.lbs.portal.web.service.AppUserService;

/**
 * APP User Controller
 *
 * @author luxinxian
 *
 */
@Controller("app_AppUserController")
public class AppUserController extends WebCommonController {

    /** Logger */
    private static final Logger LOG = Logger.getLogger(AppUserController.class);

    
    /**
     * Show Page
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return
     */
    @RequestMapping("app/user.shtm")
    public ModelAndView open(HttpServletRequest req, HttpServletResponse resp) {
        LOG.info("open app user page start...");
        
        ModelAndView mv = createOpenPageMV(req);
        
        String module = req.getParameter("m");
        // set view
        mv.setViewName("app/p_user_" + module + ".jsp");

        LOG.info("open app user page finished.");

        return mv;

    }


    /**
     * Query App User
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return
     */
    @RequestMapping("app/user_query.shtm")
    public void query(HttpServletRequest req, HttpServletResponse resp) {
        LOG.info("query app user list start...");
        
        Map<String, Object> params = getReqParams(req);

        // query
        this.service.queryAppUserList(params);
        
        Map<String, Object> output = getDefaultResult();
        output.put("appUserList", params.get("appUserList"));
        output.put("totalCount", params.get("totalCount"));
        outputJSON(resp, output);

        LOG.info("query app user list finished.");
    }

    
    /** App User Service */
    @Resource(name = "manager_AppUserService")
    private AppUserService service = null;


}
