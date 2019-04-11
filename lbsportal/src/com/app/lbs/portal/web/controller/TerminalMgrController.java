/*
 * RX-lbspt 
 */
package com.app.lbs.portal.web.controller;

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

import com.app.lbs.common.web.controller.WebCommonController;
import com.app.lbs.portal.web.service.TerminalMgrService;
import com.app.platform.base.utils.DateUtil;
import com.app.platform.base.utils.StringUtil;
import com.app.platform.base.utils.SystemConfig;

/**
 * Terminal Manager Controller
 *
 * @author colen
 *
 */
@Controller("mgr_TerminalMgrController")
public class TerminalMgrController extends WebCommonController {

    /** Logger */
    private static final Logger LOG = Logger.getLogger(TerminalMgrController.class);
    
    /** terminal configuration key Prefix */
    private static final String CNF_DV_PREFIX = "conf.cmcc.dv.";

    /**
     * Show Page
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return
     */
    @RequestMapping("terminal/open.shtm")
    public ModelAndView open(HttpServletRequest req, HttpServletResponse resp) {
        LOG.info("open terminal MGR page start...");
        
        ModelAndView mv = createOpenPageMV(req);

        String module = req.getParameter("m");
        if (MODULE_ADD.equals(module)) {
            initAddPage(req, mv);
        } else if (MODULE_QUERY.equals(module)) {
            // initQueryPage(req, mv);
        } else if (MODULE_MODIFY.equals(module)) {
            initModifyPage(req, mv);
        } else if ("setting".equals(module)) {
            initSettingPage(req, mv);
        } else if ("statuslist".equals(module)) {
            initStatutListPage(req, mv);
		} else if ("distribution".equals(module)) {
			initDistribution(req, mv);
        } else {
            LOG.warn("no module found!");
        }

        // set view
        mv.setViewName("terminal/p_tmn_" + module + ".jsp");

        LOG.info("open terminal MGR page finished.");

        return mv;

    }
 	private void initDistribution(HttpServletRequest request, ModelAndView mv)
 	{
		String cityId = getCookieValue(request, "ctid", false);

		Map<String, Object> city = this.service.getCityInfoById(cityId);
 
		mv.addObject("city", city);
  
		mv.addObject("server_time", DateUtil.getDate("yyyyMMddHHmmss"));
 
		mv.addObject("mapCnf", getMapCnf(city.get("city_id").toString(), null));
	}


    private void initStatutListPage(HttpServletRequest req, ModelAndView mv) {
        Map<String, Object> params = new HashMap<>();
        String orgId = getCookieValue(req, CK_K_ORG, false);
        params.put("orgId", orgId);

        // execute terminal settings update.
        Map<String, Object> resultMap = this.service.getTerminalCnt(params);
        
        mv.addAllObjects(resultMap);
    }


    private void initSettingPage(HttpServletRequest req, ModelAndView mv) {
        LOG.info("initSettingPage start...");

        String terminalSerialNo = req.getParameter("terminalSerialNo");
        Map<String, Object> params = new HashMap<>();
        params.put("terminalSerialNo", terminalSerialNo);

        LOG.info("will setting terminal: " + terminalSerialNo);

        // get terminal info
        Map<String, Object> terminal = this.service.getTerminalInfoDetial(params);

        mv.addObject("terminal", terminal);

        LOG.info("initSettingPage finished.");
    }


    private void initModifyPage(HttpServletRequest req, ModelAndView mv) {
        LOG.info("init modify terminal start...");

        String terminalSerialNo = req.getParameter("terminalSerialNo");
        Map<String, Object> params = new HashMap<>();
        params.put("terminalSerialNo", terminalSerialNo);

        LOG.info("will modify terminal: " + terminalSerialNo);

        // get terminal info
        Map<String, Object> terminal = this.service.getTerminalInfoDetial(params);

        mv.addObject("terminal", terminal);

        LOG.info("init modify terminal finished.");
    }



    private void initAddPage(HttpServletRequest req, ModelAndView mv) {
        Map<String, String> defaults = SystemConfig.getMapByPrefix(CNF_DV_PREFIX, true);
        mv.addObject("conf", defaults);
    }

    
    
    /**
     * Get Terminal Configuration By ConfigKEY
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     */
    @RequestMapping("terminal/get_config.shtm")
    public void getTerminalConfigByKey(HttpServletRequest req, HttpServletResponse resp) {
        LOG.info("getTerminalConfigByKey start...");

        Map<String, Object> params = getReqParams(req);

        // get terminal info
        Map<String, Object> configObj = this.service.getTerminalConfigByKey(params);

        Map<String, Object> output = getDefaultResult();
        output.put("CONFIG", configObj);
        outputJSON(resp, output);

        LOG.info("getTerminalConfigByKey finished.");
    }
    
    
    /**
     * Query Terminal
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping("terminal/query.shtm")
    public void query(HttpServletRequest req, HttpServletResponse resp) {
        if (LOG.isInfoEnabled()) {
            LOG.info("query terminal start...");
        }

        Map<String, Object> params = getReqParams(req);
        String selectOrgId = (String) params.get("orgId");
        if (StringUtil.isTrimEmpty(selectOrgId)) {
            String orgId = getCookieValue(req, CK_K_ORG, false);
            params.put("rootOrgId", orgId);
        }

        // do query
        Map<String, Object> resultMap = this.service.queryTerminalList(params);

        Map<String, Object> output = getDefaultResult();
        output.put("terminalInfos", resultMap.get("terminalInfos"));
        output.put("totalCount", resultMap.get("totalCount"));
        outputJSON(resp, output);

        if (LOG.isInfoEnabled()) {
            if (resultMap.get("terminalInfos") != null) {
                LOG.info("query result is :" + ((List) resultMap.get("terminalInfos")).size());
            }
            LOG.info("query terminal end.");
        }
    }


    /**
     * Modify Terminal
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     */
    @RequestMapping("terminal/modify.shtm")
    public void modify(HttpServletRequest req, HttpServletResponse resp) {
        LOG.info("modify terminal start...");
        
        Map<String, Object> params = getReqParams(req);
        
        // modify
        this.service.modifyTerminalInfo(params);

        // default success
        Map<String, Object> output = getDefaultResult();
        outputJSON(resp, output);
        
        LOG.info("modify terminal end.");
    }
    
    
    /**
     * Delete Terminal
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @return
     */
    @RequestMapping("terminal/delete.shtm")
    public void delete(HttpServletRequest req, HttpServletResponse resp) {
        if (LOG.isInfoEnabled()) {
            LOG.info("delete terminal start...");
        }
        
        // terminal serial no
        String terminalSerialNo = req.getParameter("terminalSerialNo");
        
        if (LOG.isInfoEnabled()) {
            LOG.info("delete terminal :" + terminalSerialNo);
        }

        // execute delete
        this.service.deleteTerminal(terminalSerialNo);
        
        // default success
        Map<String, Object> output = getDefaultResult();
        outputJSON(resp, output);
        
        if (LOG.isInfoEnabled()) {
            LOG.info("delete terminal end.");
        }
    }
    
    
    /**
     * Add Terminal
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @return
     */
    @RequestMapping("terminal/add.shtm")
    public void add(HttpServletRequest req, HttpServletResponse resp) {
        if (LOG.isInfoEnabled()) {
            LOG.info("add terminal start...");
        }
        
        Map<String, Object> params = getReqParams(req);
        
        String uid = getCookieValue(req, CK_K_UID, false);
        params.put("uid", uid);

        if (LOG.isInfoEnabled()) {
            LOG.info("uid=" + uid);
        }
        
        // do some input check
        checkTerminalInfo(params);
        
        // check and add a new terminal
        this.service.checkAndAdd(params);

        // default success
        Map<String, Object> output = getDefaultResult();
        outputJSON(resp, output);

        if (LOG.isInfoEnabled()) {
            LOG.info("add terminal end.");
        }
    }


    private void checkTerminalInfo(Map<String, Object> params) {
        // Nothing
    }



    /**
     * Get Terminal Status List
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     */
    @RequestMapping("terminal/query_statuslist.shtm")
    public void getTerminalStatusList(HttpServletRequest req,
            HttpServletResponse resp) {
        LOG.info("getTerminalStatusList start...");
        
        Map<String, Object> params = getReqParams(req);

        String orgId = (String) params.get("orgId");
        if (StringUtil.isTrimEmpty(orgId)) {
            params.put("orgId", getCookieValue(req, CK_K_ORG, false));
        }

        // execute get
        this.service.getTerminalStatusList(params);

        Map<String, Object> output = getDefaultResult();
        output.put("terminalInfos", params.get("terminalInfos"));
        output.put("totalCount", params.get("totalCount"));
        outputJSON(resp, output);

        LOG.info("getTerminalStatusList end.");
    }


    /**
     * Update Terminal Settings
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     */
    @RequestMapping("terminal/update_settings.shtm")
    public void updateSettings(HttpServletRequest req,
            HttpServletResponse resp) {
        LOG.info("updateSettings start...");
        
        Map<String, Object> params = getReqParams(req);
        
        // execute terminal settings update.
        this.service.checkAndUpdateSettings(params);
        
        // default success
        Map<String, Object> output = getDefaultResult();
        outputJSON(resp, output);

        LOG.info("updateSettings end.");
    }
    
    
    /**
     * Refresh Terminal Count
     *
     * @param req
     * @param resp
     */
    @RequestMapping("terminal/refresh_terminal_count.shtm")
    public void refrenTerminalCnt(HttpServletRequest req,
            HttpServletResponse resp) {
        LOG.info("refrenTerminalCnt start...");
        
        Map<String, Object> params = new HashMap<>();
        
        String orgId = getCookieValue(req, CK_K_ORG, false);
        params.put("orgId", orgId);

        // execute terminal settings update.
        Map<String, Object> resultMap = this.service.getTerminalCnt(params);

        // default success
        Map<String, Object> output = getDefaultResult();
        output.putAll(resultMap);
        outputJSON(resp, output);

        LOG.info("refrenTerminalCnt end.");
    }
    
    
    
    @Resource(name = "manager_TerminalMgrService")
    private TerminalMgrService service = null;
}
