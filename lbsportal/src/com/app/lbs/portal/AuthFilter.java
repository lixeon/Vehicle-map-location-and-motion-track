/*
 * lbspt
 */
package com.app.lbs.portal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.app.lbs.common.filter.CommonFilter;
import com.app.platform.base.utils.AppContextHolder;
import com.app.platform.base.utils.StringUtil;

/**
 * Authorize Filter
 *
 * @author colen
 *
 */
public class AuthFilter extends CommonFilter {
    
    /** logger */
    private static final Logger LOG = Logger.getLogger(AuthFilter.class);
    
    /** ignore urls */
    private Map<String, String> ignoreURIs = null;
    /** Application context path */
    private String ctxPath = "";


    @SuppressWarnings("unchecked")
    @Override
    public void init(FilterConfig config) throws ServletException {
        // initialize
        this.ignoreURIs = new HashMap<String, String>();

        // get config ignore URIs
        Map<String, String> configIngoreURIs = (Map<String, String>)
                AppContextHolder.getBean("config_ignoreURIs");
        // store
        this.ctxPath = config.getServletContext().getContextPath();

        if (configIngoreURIs != null) {
            if ("/".equals(this.ctxPath) || "".equals(this.ctxPath)) {
                this.ignoreURIs.putAll(configIngoreURIs);
            } else {
                for (Entry<String, String> entry : configIngoreURIs.entrySet()) {
                    this.ignoreURIs.put(this.ctxPath + entry.getKey(), "");
                }
            }
        }
    }

    
    @Override
    public void filter(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        // get uri
        String uri = request.getRequestURI();
        if (this.ignoreURIs.containsKey(uri)) {
            chain.doFilter(request, response);
            return;
        }

        String uid = getCookieValue(request, "uid", false);
        String tmnId = getCookieValue(request, "tmnId", false);
        if (StringUtil.isTrimEmpty(uid) && StringUtil.isTrimEmpty(tmnId)) {
            
            if (LOG.isInfoEnabled()) {
                LOG.info("block visit : " + request.getRequestURL()
                        + ", from " + request.getRemoteAddr());
            }
 String loginType = request.getParameter("login_type");
      String indexPage = "/index.html";
   if ((!StringUtil.isTrimEmpty(loginType)) && ("2".equals(loginType)))
   {
    indexPage = "/admin.html";
  }
  
   response.sendRedirect(this.ctxPath + indexPage);
        } else {
            chain.doFilter(request, response);
        }

    }

}
