/*
 * Copyleft 2011 Power by colen.
 *
 * Project: booking
 * Date: Jul 2, 2011
 */
package com.app.lbs.common.resolver;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import com.app.platform.base.exception.BaseBusinessException;
import com.app.platform.base.utils.HttpServletUtil;

/**
 * Global Handler Exception Resolver
 *
 *
 * @author colen
 *
 */
public class UniformHandlerExceptionResolver extends
        AbstractHandlerExceptionResolver {

    /** logger */
    private static final Logger LOG = Logger.getLogger(UniformHandlerExceptionResolver.class);

    /** Ajax */
    private static final String AJAX = "1";
    private static final String ERROR_LEVEL_SYS = "0";
    private static final String ERROR_LEVEL_BIZ = "1";

    private static final String KEY_SUCCESS_STATUS = "success";
    private static final String SUCCESS_STATUS_FALSE = "false";
    private static final String KEY_ERR_LEVEL = "errorLevel";
    private static final String KEY_ERR_MSGS = "errorMsgs";
    private static final String KEY_JUMP_TO = "jumpTo";
    private static final String KEY_ERR_CALLBACK = "errCallback";
    private static final String KEY_OUT_DATA = "outData";

    /** system error page, url, msg */
    private String sysErrPage = null;
    private String sysErrUrl = null;
    private String sysErrMsg = "系统发生异常！";

    @Override
    protected ModelAndView doResolveException(HttpServletRequest req,
            HttpServletResponse resp, Object handler, Exception ex) {
        
        ModelAndView mv = null;
        if (ex instanceof BaseBusinessException) {
            mv = handleBusinessException(req, resp, ex);
        } else {
            LOG.error("Unknow Exception!", ex);
            mv = handleSysException(req, resp, ex);
        }
        
        return mv;
    }

    /**
     * Handle system exception
     *
     * @param req
     * @param resp
     * @param ex
     * @return mv
     */
    private ModelAndView handleSysException(HttpServletRequest req,
            HttpServletResponse resp, Exception ex) {
        // model and view
        ModelAndView mv = new ModelAndView();
        Map<String, String> mmsgs = new HashMap<String, String>();
        mmsgs.put(KEY_SUCCESS_STATUS, SUCCESS_STATUS_FALSE);
        mmsgs.put(KEY_ERR_LEVEL, ERROR_LEVEL_SYS);
        mmsgs.put(KEY_ERR_MSGS, this.sysErrMsg);
        if (isAjaxRequest(req)) {
            mmsgs.put(KEY_JUMP_TO, this.sysErrUrl);
            // write result
            HttpServletUtil.outputJSON(resp, mmsgs);
        } else {
            mv.setViewName(this.sysErrPage);
            mv.addAllObjects(mmsgs);
        }

        return mv;
    }

    /**
     * handle business exception
     *
     * @param req
     * @param resp
     * @param ex
     * @return
     */
    private ModelAndView handleBusinessException(HttpServletRequest req,
            HttpServletResponse resp, Exception ex) {
        BaseBusinessException bizEx = (BaseBusinessException) ex;
        LOG.info("catch bussiness exception key = " + bizEx.getMsgIdList());

        Map<String, Object> mmsgs = new HashMap<String, Object>();
        mmsgs.put(KEY_SUCCESS_STATUS, SUCCESS_STATUS_FALSE);
        mmsgs.put(KEY_ERR_LEVEL, ERROR_LEVEL_BIZ);
        mmsgs.put(KEY_ERR_MSGS, bizEx.getMsgContentList());
        mmsgs.put(KEY_ERR_CALLBACK, bizEx.getExcErrorCallback());
        if (bizEx.getOutData() != null) {
            mmsgs.put(KEY_OUT_DATA, bizEx.getOutData());
        } else {
            mmsgs.put(KEY_OUT_DATA, "");
        }

        // model and view
        ModelAndView mv = new ModelAndView();
        if (isAjaxRequest(req)) {
            HttpServletUtil.outputJSON(resp, mmsgs);
        } else {
            mv.setViewName(this.sysErrPage);
            mv.addAllObjects(mmsgs);
        }

        return mv;
    }

    private boolean isAjaxRequest(HttpServletRequest req) {
        String ajaxSubmit = req.getParameter("ajaxSubmit");
        String ajax = req.getHeader("X-Requested-With");
        if (AJAX.equals(ajaxSubmit) || "XMLHttpRequest".equals(ajax)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Setter
     * @param sysErrPage the sysErrPage to set
     */
    public void setSysErrPage(String sysErrPage) {
        this.sysErrPage = sysErrPage;
    }

}
