/*
 * Copyleft 2012 Power by colen.
 *
 * Project: app-crm
 * Date: Mar 17, 2012
 */
package com.app.lbs.common.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.lbs.common.CodeConst;
import com.app.lbs.common.service.pub.CodeDataService;
import com.app.lbs.common.web.service.WebWidgetService;
import com.app.platform.base.utils.CommonUtil;

/**
 * Web Widget Controller
 *
 * @author  colen
 */
@Controller(value="common_webWidgetController")
public class WebWidgetController extends WebCommonController
{

    /** logger */
    private static final Logger LOG = Logger.getLogger(WebWidgetController.class);

    /**
     * load Combo Box Data
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     */
    @RequestMapping("common/combo.shtm")
    public void loadCombo(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, Object> params = getReqParams(req);
        // combo box ID
        String comboId = (String) params.get("comboId");

        if (LOG.isInfoEnabled()) {
            LOG.info("load combo by Id:" + comboId);
        }

        // load code list
        List<Map<String, Object>> list = this.codeDataService.getCodeList(comboId);

        // support blank row
        String hasBlank = (String) params.get("hasBlank");
        if ("true".equals(hasBlank) && !CommonUtil.isEmpty(list)) {
            Map<String, Object> blank = new HashMap<String, Object>();
            blank.put("codeKey", comboId);
            blank.put("value", "");
            blank.put("full_name", " ");
            blank.put("abbr_name", " ");
            blank.put("disp_no", 0);
            list.add(0, blank);
        }

        // out put
        Map<String, Object> output = getDefaultResult();
        output.put("comboDataList", list);
        outputJSON(resp, output);
    }

    /**
     * load initialize menus
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     */
    @RequestMapping("common/loadinitmenus.shtm")
    public void loadInitMenus(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("uid", getCookieValue(req, CK_K_UID, false));
        args.put("parentId", "ROOT");
        args.put("roleId", getCookieValue(req, CK_K_ROLE, false));

        // load top menus
        List<Map<String, Object>> topMenus = this.webWidgetService.queryMenus(args);

        // out put
        Map<String, Object> output = getDefaultResult();
        output.put("topMenus", topMenus);
        outputJSON(resp, output);
    }


    /**
     * load menus
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     */
    @RequestMapping("common/loadmenus.shtm")
    public void loadMenus(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("uid", getCookieValue(req, CK_K_UID, false));
        args.put("parentId", req.getParameter("parentId"));
        args.put("roleId", getCookieValue(req, CK_K_ROLE, false));

        // load
        List<Map<String, Object>> menus = this.webWidgetService.queryMenus(args);

        // out put
        Map<String, Object> output = getDefaultResult();
        output.put("menus", menus);
        outputJSON(resp, output);
    }


    /**
     * Load Organization List
     *
     * @param req
     * @param resp
     */
    @RequestMapping("common/load_orglist.shtm")
    public void loadOrgList(HttpServletRequest req, HttpServletResponse resp) {
        String parentOrgId = req.getParameter("parentOrgId");
        if (LOG.isInfoEnabled()) {
            LOG.info("loadTmnGrpList: parentOrgId=" + parentOrgId);
        }

        Map<String, Object> qps = new HashMap<>();
        qps.put("parentOrgId", parentOrgId);
        
        String orgId = getCookieValue(req, CK_K_ORG, false);
        if (!CodeConst.TOP_GRPID.equals(orgId)) {
            qps.put("orgId", orgId);
        }
        
        // query
        List<Map<String, Object>> orgList = this.webWidgetService.loadOrgList(qps);

        // out put
        Map<String, Object> output = getDefaultResult();
        output.put("orgList", orgList);
        outputJSON(resp, output);
    }
    

    /** Code Data Service */
    @Resource(name = "common_codeDataService")
    private CodeDataService codeDataService = null;

    /** Code Data Service */
    @Resource(name = "common_webWidgetService")
    private WebWidgetService webWidgetService = null;
}
