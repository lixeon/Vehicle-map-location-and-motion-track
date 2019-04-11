/*
 * LBSAPP
 */
package com.app.lbs.portal.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.app.lbs.portal.web.service.OrgMgrService;
import com.app.platform.base.utils.ConvertUtil;

/**
 * Organization Manager Controller
 *
 * @author colen
 *
 */
@Controller("mgr_OrgMgrController")
public class OrgMgrController extends WebCommonController {

    /** Logger */
    private static final Logger LOG = Logger.getLogger(OrgMgrController.class);


    /**
     * Open Company List Page
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @return 
     */
    @RequestMapping("org/open_company.shtm")
    public ModelAndView openCompany(HttpServletRequest req,
            HttpServletResponse resp) {
        LOG.info("open company list page start...");
        
        ModelAndView mv = createOpenPageMV(req);

        String module = req.getParameter("m");
        if (MODULE_ADD.equals(module)) {
            initAddCompanyPage(mv);
        } else if (MODULE_MODIFY.equals(module)) {
            initModifyCompanyPage(req, mv);
        } else if (MODULE_QUERY.equals(module)) {
            // nothing here.
        } else {
            LOG.warn("no module found!");
        }

        // set view
        mv.setViewName("org/p_company_" + module + ".jsp");

        LOG.info("open company list page end.");
        return mv;
    }


    private void initModifyCompanyPage(HttpServletRequest req, ModelAndView mv) {
        LOG.info("open company modify page start..");

        Map<String, Object> params = getReqParams(req);

        // do query
        Map<String, Object> company = this.service.getCompanyById(params);

        mv.addAllObjects(company);
        
        LOG.info("open company modify page end.");
    }


    private void initAddCompanyPage(ModelAndView mv) {
        String nextCompanyId = this.service.getNextOrgId(CodeConst.TOP_GRPID);
        mv.addObject("nextCompanyId", CodeConst.TOP_GRPID + ConvertUtil.padd(Long.valueOf(nextCompanyId), 4));
    }


    /**
     * Query Company List Data
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @return 
     */
    @RequestMapping("org/query_company_list.shtm")
    public void queryCompanyList(HttpServletRequest req,
            HttpServletResponse resp) {
        LOG.info("query company list page start...");
        
        Map<String, Object> params = getReqParams(req);
        
        // do query
        Map<String, Object> resultMap = this.service.queryCompanyList(params);

        Map<String, Object> output = getDefaultResult();
        output.putAll(resultMap);
        outputJSON(resp, output);
        
        LOG.info("query company list page end.");
    }
    
    
    /**
     * Add Company
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     */
    @RequestMapping("org/add_company.shtm")
    public void addCompany(HttpServletRequest req,
            HttpServletResponse resp) {
        LOG.info("add company start...");
        
        Map<String, Object> params = getReqParams(req);
        // find login user id
        params.put("login_user_id", getCookieValue(req, CK_K_UID, false));
        
        // add a company
        this.service.addCompany(params);
        
        // default success
        Map<String, Object> output = getDefaultResult();
        outputJSON(resp, output);
        
        LOG.info("add company end.");
    }
    
    
    /**
     * Modify Company
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     */
    @RequestMapping("org/modify_company.shtm")
    public void modifyCompany(HttpServletRequest req,
            HttpServletResponse resp) {
        LOG.info("modify company start...");
        
        Map<String, Object> params = getReqParams(req);
        // find login user id
        params.put("login_user_id", getCookieValue(req, CK_K_UID, false));

        // modify a company
        this.service.modifyCompany(params);
        
        // default success
        Map<String, Object> output = getDefaultResult();
        outputJSON(resp, output);

        LOG.info("modify company end.");
    }
    
    
    /**
     * Delete Company
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     */
    @RequestMapping("org/delete_company.shtm")
    public void deleteCompany(HttpServletRequest req,
            HttpServletResponse resp) {
        LOG.info("delete company start...");
        
        Map<String, Object> params = getReqParams(req);
        
        // delete a company
        this.service.deleteCompany(params);
        
        // default success
        Map<String, Object> output = getDefaultResult();
        outputJSON(resp, output);
        
        LOG.info("delete company end.");
    }
    
    
    /**
     * Open Company List Page
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @return 
     */
    @RequestMapping("org/open_group.shtm")
    public ModelAndView openGroup(HttpServletRequest req,
            HttpServletResponse resp) {
        LOG.info("open group page start...");
        
        ModelAndView mv = createOpenPageMV(req);

        String module = req.getParameter("m");
        
        if (LOG.isInfoEnabled()) {
            LOG.info("module  = " + module);
        }
        
        if (MODULE_ADD.equals(module)) {
            initAddGroupPage(req, mv);
        } else if (MODULE_MODIFY.equals(module)) {
            initModifyGroupPage(req, mv);
        } else if (MODULE_QUERY.equals(module)) {
            initQueryGroupPage(req, mv);
        } else if ("bindtmn".equals(module)) {
            initBindTmnPage(req, mv);
        } else {
            LOG.warn("no module found!");
        }

        // set view
        mv.setViewName("org/p_group_" + module + ".jsp");

        LOG.info("open group list page end.");
        return mv;
    }


    private void initBindTmnPage(HttpServletRequest req, ModelAndView mv) {
        Map<String, Object> params = getReqParams(req);

        // query terminal list
        this.service.queryBindedTerminalList(params);

        mv.addObject("tmn_list_company", ConvertUtil.toJSON(params.get("tmn_list_company")));
        mv.addObject("tmn_list_group", ConvertUtil.toJSON(params.get("tmn_list_group")));
        mv.addObject("orgId", params.get("orgId"));
    }


    private void initModifyGroupPage(HttpServletRequest req, ModelAndView mv) {
        Map<String, Object> params = getReqParams(req);
        // do query
        Map<String, Object> company = this.service.getCompanyById(params);
        mv.addAllObjects(company);
    }


    private void initAddGroupPage(HttpServletRequest req, ModelAndView mv) {
        Map<String, Object> params = getReqParams(req);
        mv.addObject("companyId", params.get("companyId"));
        mv.addObject("companyName", params.get("companyName"));
        
        String nextCompanyId = this.service.getNextOrgId((String) params.get("companyId"));
        mv.addObject("nextGroupId", ConvertUtil.padd(Long.valueOf(nextCompanyId), 12));
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void initQueryGroupPage(HttpServletRequest req, ModelAndView mv) {
        String orgId = getCookieValue(req, CK_K_ORG, false);
        String roleId = getCookieValue(req, CK_K_ROLE, false);
        
        List<Map<String, Object>> companyList = null;
        Map<String, Object> params = new HashMap<>();
        if (CodeConst.TOP_GRPID.equals(orgId)) {
            params.put("page", "1");
            params.put("start", "0");
            params.put("limit", "9999");
            params = this.service.queryCompanyList(params);
            companyList = (List) params.get("companys");
        } else if (CodeConst.ROLE_COMAPANY.equals(roleId)) {
            params = new HashMap<>();
            params.put("orgId", orgId);
            Map<String, Object> company = this.service.getCompanyById(params);
            companyList = new ArrayList<>();
            companyList.add(company);
        } else {
            LOG.error("Error Role Open this Page!");
        }

        mv.addObject("company_list", ConvertUtil.toJSON(companyList));
    }

    
    /**
     * Query Group List Data
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @return 
     */
    @RequestMapping("org/query_group_list.shtm")
    public void queryGroupList(HttpServletRequest req,
            HttpServletResponse resp) {
        LOG.info("query group list page start...");
        
        Map<String, Object> params = getReqParams(req);
        
        // do query
        Map<String, Object> resultMap = this.service.queryGroupList(params);

        Map<String, Object> output = getDefaultResult();
        output.putAll(resultMap);
        outputJSON(resp, output);

        LOG.info("query group list page end.");
    }
    
    /**
     * Add Group
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     */
    @RequestMapping("org/add_group.shtm")
    public void addGroup(HttpServletRequest req,
            HttpServletResponse resp) {
        LOG.info("add Group start...");
        
        Map<String, Object> params = getReqParams(req);
        // find login user id
        params.put("login_user_id", getCookieValue(req, CK_K_UID, false));
        
        // add a Group
        this.service.addGroup(params);
        
        // default success
        Map<String, Object> output = getDefaultResult();
        outputJSON(resp, output);
        
        LOG.info("add Group end.");
    }
    
    /**
     * Modify Group
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     */
    @RequestMapping("org/modify_group.shtm")
    public void modifyGroup(HttpServletRequest req,
            HttpServletResponse resp) {
        LOG.info("modify group start...");
        
        Map<String, Object> params = getReqParams(req);
        // find login user id
        params.put("login_user_id", getCookieValue(req, CK_K_UID, false));

        // modify a group
        this.service.modifyGroup(params);
        
        // default success
        Map<String, Object> output = getDefaultResult();
        outputJSON(resp, output);

        LOG.info("modify group end.");
    }

    
    /**
     * Bind Terminal
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     */
    @RequestMapping("org/bind_terminal.shtm")
    public void bindTerminal(HttpServletRequest req, HttpServletResponse resp) {
        LOG.info("bindTerminal start...");
        
        Map<String, Object> params = getReqParams(req);
        
        // bind Terminal
        this.service.bindTerminal(params);

        // default success
        Map<String, Object> output = getDefaultResult();
        outputJSON(resp, output);
        
        LOG.info("bindTerminal end.");
    }
    
    
    /**
     * Delete Group
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     */
    @RequestMapping("org/delete_group.shtm")
    public void deleteGroup(HttpServletRequest req,
            HttpServletResponse resp) {
        LOG.info("deleteGroup start...");
        
        Map<String, Object> params = getReqParams(req);
        
        // delete a company
        this.service.deleteGroup(params);

        // default success
        Map<String, Object> output = getDefaultResult();
        outputJSON(resp, output);
        
        LOG.info("deleteGroup end.");
    }
    
    
    
    @Resource(name = "manager_OrgMgrService")
    private OrgMgrService service = null;
    
}
