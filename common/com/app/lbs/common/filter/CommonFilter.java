/*
 * lbspt
 */
package com.app.lbs.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.platform.base.utils.WebToolKits;

/**
 * Common Filter
 *
 * @author colen
 *
 */
public abstract class CommonFilter extends WebToolKits implements Filter {

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        filter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }
    
    /**
     * Filter
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param chain FilterChain
     * @throws IOException
     * @throws ServletException
     */
    public abstract void filter(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException;

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

}
