/*
 * LBSAPP
 */
package com.app.lbs.portal.web.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.app.lbs.common.dao.CommonDaoEx;
import com.app.lbs.common.exception.CommonBusinessException;
import com.app.lbs.common.web.controller.WebCommonController;
import com.app.platform.base.utils.CommonUtil;
import com.app.platform.base.utils.DateUtil;

/**
 * App Client Controller
 *
 * @author luxinxian
 *
 */
@Controller("app_AppClientController")
public class AppClientController extends WebCommonController {

    /** Logger */
    private static final Logger LOG = Logger.getLogger(AppUserController.class);

    
    /**
     * Show Page
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return
     */
    @RequestMapping("app/client.shtm")
    public ModelAndView open(HttpServletRequest req, HttpServletResponse resp) {
        LOG.info("open app client page start...");

        ModelAndView mv = createOpenPageMV(req);

        String module = req.getParameter("m");
        
        if (LOG.isInfoEnabled()) {
            LOG.info("module = " + module);
        }

        if (MODULE_MODIFY.equals(module)) {
            initModifyAppClient(req, mv);
        }

        // set view
        mv.setViewName("app/p_client_" + module + ".jsp");

        LOG.info("open app client page finished.");

        return mv;

    }


    private void initModifyAppClient(HttpServletRequest req, ModelAndView mv) {
        String clientType = req.getParameter("clientType");
        Map<String, Object> existClient = getAppClientById(clientType);
        mv.addAllObjects(existClient);
    }


    /**
     * Query Client info.
     *
     * @param req
     * @param resp
     */
    @RequestMapping("app/client_list.shtm")
    public void query(HttpServletRequest req, HttpServletResponse resp) {
        LOG.info("query app client page start...");
        
        String sql = "select * from T_APP_SOFTWARE order by CLIENT_TYPE";
        
        List<Map<String, Object>> list = getCommonDao().queryForList(sql);

        if (!CommonUtil.isEmpty(list)) {
            Date updateDate = null;
            for (Map<String, Object> client : list) {
                updateDate = (Date) client.get("update_date");
                client.put("update_date", DateUtil.format(updateDate, DateUtil.DATE_FORMAT_1));
            }
        }

        // output
        Map<String, Object> output = getDefaultResult();
        output.put("clientList", list);
        output.put("totalCount", list.size());
        
        outputJSON(resp, output);
        
        LOG.info("query app client page finished.");
    }
    
    
    /**
     * Delete Client.
     *
     * @param req
     * @param resp
     */
    @RequestMapping("app/client_delete.shtm")
    public void delete(HttpServletRequest req, HttpServletResponse resp) {
        LOG.info("delete app client start...");
        
        Map<String, Object> params = getReqParams(req);
        String clientType = (String) params.get("clientType");
        if (LOG.isInfoEnabled()) {
            LOG.info("will delete app client, " + clientType);
        }
        
        String sql = "delete from T_APP_SOFTWARE where CLIENT_TYPE = ?";
        
        // delete
        getCommonDao().update(sql, clientType);

        // output
        Map<String, Object> output = getDefaultResult();
        outputJSON(resp, output);

        LOG.info("delete app client finished.");
    }
    
    
    /**
     * Add Client.
     *
     * @param req
     * @param resp
     */
    @RequestMapping("app/client_add.shtm")
    public void add(HttpServletRequest req, HttpServletResponse resp) {
        LOG.info("add app client start...");
        
        Map<String, Object> params = getReqParams(req);

        if (LOG.isInfoEnabled()) {
            LOG.info("will add a app client, " + params);
        }

        // check
        CommonDaoEx daoEx = getCommonDao();
        Map<String, Object> existClient = getAppClientById((String) params.get("clientType"));
        if (existClient != null) {
            throw new CommonBusinessException("该客户端ID已经存在，请重新指定！");
        }

        String sql = "insert into T_APP_SOFTWARE values (:clientType,:clientTypeName,:ver,:downloadUrl,:updatecontent,now())";
        // add
        daoEx.update(sql, params);

        // output
        Map<String, Object> output = getDefaultResult();
        outputJSON(resp, output);

        LOG.info("add app client finished.");
    }

    
    /**
     * Modify Client.
     *
     * @param req
     * @param resp
     */
    @RequestMapping("app/client_modify.shtm")
    public void modify(HttpServletRequest req, HttpServletResponse resp) {
        LOG.info("modify app client start...");
        
        Map<String, Object> params = getReqParams(req);

        if (LOG.isInfoEnabled()) {
            LOG.info("will modify a app client, " + params);
        }

        // check
        String sql = "update T_APP_SOFTWARE set CLIENT_TYPE_NAME=:clientTypeName, VER=:ver, DOWNLOAD_URL=:downloadUrl, UPDATE_CONTENT=:updatecontent, UPDATE_DATE=now() where CLIENT_TYPE = :clientType";
        // modify
        getCommonDao().update(sql, params);

        // output
        Map<String, Object> output = getDefaultResult();
        outputJSON(resp, output);

        LOG.info("modify app client finished.");
    }
    

    private Map<String, Object> getAppClientById(String clientType) {
        String sql = "select * from T_APP_SOFTWARE where CLIENT_TYPE = ?";
        List<Map<String, Object>> list = getCommonDao().queryForList(sql, clientType);
        if (CommonUtil.isEmpty(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }
    

}
