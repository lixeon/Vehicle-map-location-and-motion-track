/*
 * LBSAPP
 */
package com.app.lbs.portal.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.app.lbs.common.web.controller.WebCommonController;
import com.app.platform.base.dao.Page;
import com.app.platform.base.dao.PageCond;
import com.app.platform.base.utils.DateUtil;
import com.app.platform.base.utils.StringUtil;

/**
 * APP Feedback Controller
 *
 * @author luxinxian
 *
 */
@Controller("app_AppFeedbackController")
public class AppFeedbackController extends WebCommonController {

    /** Logger */
    private static final Logger LOG = Logger.getLogger(AppUserController.class);

    /**
     * Show Page
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return
     */
    @RequestMapping("app/feedback.shtm")
    public ModelAndView open(HttpServletRequest req, HttpServletResponse resp) {
        LOG.info("open app feedback page start...");
        
        ModelAndView mv = createOpenPageMV(req);
        
        String module = req.getParameter("m");
        
        if (LOG.isInfoEnabled()) {
            LOG.info("module = " + module);
        }

        // set view
        mv.setViewName("app/p_feedback_" + module + ".jsp");

        LOG.info("open app feedback page finished.");

        return mv;

    }
    
    /**
     * Query Feedback info.
     *
     * @param req
     * @param resp
     */
    @RequestMapping("app/feedback_list.shtm")
    public void query(HttpServletRequest req, HttpServletResponse resp) {
        LOG.info("query feedback start...");
        
        Map<String, Object> params = getReqParams(req);
        
        Map<String, Object> qps = extractPageCond(params);
        
        // query page condition
        PageCond cond = new PageCond(qps,
                Integer.valueOf((String) params.get("start")),
                Integer.valueOf((String) params.get("limit")));

        // query for page
        Page page = getCommonDao().namingQueryForPage("sql_manager_queryFeedbackList", cond);

        // output
        Map<String, Object> output = getDefaultResult();
        output.put("feedbackList", page.getResultList());
        output.put("totalCount", page.getTotalCount());
        
        outputJSON(resp, output);
        
        LOG.info("query feedback finished.");
    }

    private Map<String, Object> extractPageCond(Map<String, Object> params) {
        Map<String, Object> qps = new HashMap<>();
        qps.putAll(params);

        String stdate = (String) qps.get("createDateSt");
        if (!StringUtil.isTrimEmpty(stdate)) {
            qps.put("createDateSt", DateUtil.toDate(stdate + " 00:00:00", DateUtil.DATE_FORMAT_1));
        }

        String etdate = (String) qps.get("createDateEt");
        if (!StringUtil.isTrimEmpty(etdate)) {
            qps.put("createDateEt", DateUtil.toDate(etdate + " 23:59:59", DateUtil.DATE_FORMAT_1));
        }

        return qps;
    }
    
    
    /**
     * Export Feedback Data
     *
     * @param req
     * @param resp
     */
    @RequestMapping("app/feedback_export.shtm")
    public void exportFeedback(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, Object> params = getReqParams(req);
        
        Map<String, Object> qps = extractPageCond(params);

        // Max 3000 rows
        PageCond cond = new PageCond(qps, 0, 3000);

        // query for page
        Page page = getCommonDao().namingQueryForPage("sql_manager_queryFeedbackList", cond);

        String[] keys = new String[] {"mobile", "client_type_name", "ver", "create_date", "content"};
        String[] titles = new String[] {"手机号码", "客户端类型", "软件版本", "反馈日期", "内容"};
        try {
            resp.setContentType("application/vnd.ms-excel;charset=UTF-8"); 
            resp.setHeader("Content-Disposition", "attachment;filename=Feedback_Export.xls");
            
            OutputStream os = resp.getOutputStream();
            
            //WritableWorkbook workbook = Workbook.createWorkbook(os);
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("data");
            
            // first row
            HSSFRow row = sheet.createRow(0);
            int idx = 0;
            HSSFCell cell = null;
            for (String title : titles) {
                cell = row.createCell(idx++);
                cell.setCellValue(title);
            }

            List<Map<String, Object>> dataList = page.getResultList();
            int rowIdx = 1;
            String value = null;
            for (Map<String, Object> data : dataList) {
                row = sheet.createRow(rowIdx++);
                idx = 0;
                for (String key : keys) {
                    cell = row.createCell(idx++);
                    value = StringUtil.toStr(data.get(key));
                    cell.setCellValue(value == null ? "" : value);
                }
            }
            
            wb.write(os);
            os.close();
        } catch (IOException ioe) {
            LOG.error(ioe);
        }
        
    }
}
