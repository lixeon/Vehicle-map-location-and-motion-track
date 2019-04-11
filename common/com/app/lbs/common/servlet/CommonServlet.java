/*
 * lbsgw
 */
package com.app.lbs.common.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.platform.base.utils.WebToolKits;

/**
 * Common Servlet
 *
 * @author colen
 *
 */
public abstract class CommonServlet extends HttpServlet implements DynServletConf {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /** tool kits */
    protected WebToolKits toolkits = new WebToolKits();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        handleRequest(req, resp);
    }


    /**
     * Handle Request
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    protected abstract void handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException;
    

    private String name = null;
    private String mapping = null;


    /**
     * @return the name
     */
    public String getName() {
        return name;
    }


    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * @return the mapping
     */
    public String getMapping() {
        return mapping;
    }


    /**
     * @param mapping the mapping to set
     */
    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

}
