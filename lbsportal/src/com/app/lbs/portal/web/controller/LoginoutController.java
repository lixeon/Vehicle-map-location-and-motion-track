/*
 * lbspt
 */
package com.app.lbs.portal.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.lbs.common.CodeConst;
import com.app.lbs.common.exception.CommonBusinessException;
import com.app.lbs.common.web.controller.WebCommonController;
import com.app.lbs.portal.web.service.LoginoutService;
import com.app.platform.base.utils.StringUtil;

/**
 * System Login And Logout Controller
 *
 * @author colen
 *
 */
@Controller(value="portal_LoginoutController")
public class LoginoutController extends WebCommonController {

    /** Logger */
    private static final Logger LOG = Logger.getLogger(LoginoutController.class);

    /**
     * login
     *
     * @param request http request
     * @param response http response
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("portal/login.shtm")
    public void login(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("login start...");
        
        // request parameters
        Map<String, Object> params = getReqParams(request);
        String loginType = (String) params.get("login_type");
        if(CodeConst.LOGIN_TYPE_TMN.equals(loginType))
        {
        	String verifycode = (String) params.get("verifycode");
            HttpSession session = request.getSession();
            if(session!=null)
            {
            	if (!verifycode.equalsIgnoreCase(session.getAttribute("sRand").toString()))
            	{
            		throw new CommonBusinessException("对不起，您输入的验证码不正确！");
            	}
            }else
            {
            	throw new CommonBusinessException("请刷新页面后再登入！");
            }
        }      
        
        this.service.doLoginCheck(params);
        Map<String, Object> user = (Map<String, Object>) params.get("user");

        if (CodeConst.LOGIN_TYPE_MGR.equals(loginType)) {
            forwardManagerPortal(response, user);
        } else {
            forwardTerminalPortal(response, user);
        }

        LOG.info("login successfully!");
    }


    private void forwardTerminalPortal(HttpServletResponse response,
            Map<String, Object> user) {

        // terminal id
        setCookie(response, CK_K_TMNID, user.get("terminal_serial_no").toString(), -1, false);
        // city id
        setCookie(response, CK_K_CITYID, user.get("city_id").toString(), -1, false);

        try {
            String owner = (String) user.get("owner");
            if (!StringUtil.isTrimEmpty(owner)) {
                setCookie(response, CK_K_UNAME, URLEncoder.encode(owner, "utf-8"), -1, false);
            } else {
                setCookie(response, CK_K_UNAME, URLEncoder.encode(user.get("terminal_name").toString(), "utf-8"), -1, false);
            }
        } catch (UnsupportedEncodingException e) {
            // never happen
        }

        // output
        Map<String, Object> output = getDefaultResult();
        output.put("forward", "portal/terminal_portal.shtm");
        outputJSON(response, output);
    }


    private void forwardManagerPortal(HttpServletResponse response,
            Map<String, Object> user) {

        try {
            // user id
            String uid = URLEncoder.encode(user.get("uid").toString(), "utf-8");
            setCookie(response, CK_K_UID, uid, -1, false);

            // city id
            //setCookie(response, CK_K_CITYID, user.get("city_id").toString(), -1, false);

            // user name
            String uname = URLEncoder.encode(user.get("name").toString(), "utf-8");
            setCookie(response, CK_K_UNAME, uname, -1, false);
        } catch (UnsupportedEncodingException e) {
            // never happen
        }

        // role
        String role = user.get("role_id").toString();
        setCookie(response, CK_K_ROLE, role, -1, false);

        // organize
        setCookie(response, CK_K_ORG, user.get("org_id").toString(), -1, false);

        // output
        Map<String, Object> output = getDefaultResult();
        output.put("forward", "portal/mgr_portal.shtm");
        outputJSON(response, output);
    }


    /**
     * logout
     *
     * @param request http request
     * @param response http response
     */
    @RequestMapping("portal/logout.shtm")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("logout start...");
        
        // user id
        deleteCookie(response, CK_K_UID, false);
        // city id
        deleteCookie(response, CK_K_CITYID, false);
        // user name
        deleteCookie(response, CK_K_UNAME, false);
        // user role
        deleteCookie(response, CK_K_ROLE, false);
        // user org
        deleteCookie(response, CK_K_ORG, false);
        // terminal id
        deleteCookie(response, CK_K_TMNID, false);

        Map<String, Object> output = getDefaultResult();

        String loginType = request.getParameter("type");
        if (CodeConst.LOGIN_TYPE_MGR.equals(loginType)) {
            output.put("forward","admin.html");
        } else {
            output.put("forward","index.html");
        }
       outputJSON(response, output);  
       LOG.info("logout successfully.");
    }

    
    
    /**
     * modify password
     *
     * @param request http request
     * @param response http response
     */
    @RequestMapping("portal/modify_pwd.shtm")
    public void modifyPwd(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("modify password start...");
        
        // request parameters
        Map<String, Object> params = getReqParams(request);
        String uid = getCookieValue(request, CK_K_UID, false);
        params.put("uid", uid);

        // check old password and modify
        this.service.doModifyPwd(params);

        Map<String, Object> output = getDefaultResult();
        outputJSON(response, output);

        LOG.info("modify password finished.");
    }
    
    
    /**
     * modify terminal password
     *
     * @param request http request
     * @param response http response
     */
    @RequestMapping("portal/modify_tmn_pwd.shtm")
    public void modifyTerminalPwd(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("modify terminal password start...");
        
        // request parameters
        Map<String, Object> params = getReqParams(request);
        String terminalSerialNo = getCookieValue(request, CK_K_TMNID, false);
        params.put("terminalSerialNo", terminalSerialNo);

        // check old password and modify
        this.service.doModifyTerminalPwd(params);

        Map<String, Object> output = getDefaultResult();
        outputJSON(response, output);

        LOG.info("modify terminal password finished.");
    }
    

    /** Main Service */
    @Resource(name = "portal_LoginoutService")
    private LoginoutService service = null;

}
