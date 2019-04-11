/*
 *
 */
package com.app.platform.base.utils;

import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;

/**
 * Web Application Context Holder
 *
 * @author colen
 *
 */
public final class AppContextHolder {

    /** Logger */
    private static Logger LOG = Logger.getLogger(AppContextHolder.class);

    /**
     * Constructor
     *
     */
    private AppContextHolder() {}

    /** AppContextHolder */
    private static AppContextHolder instance = null;


    /**
     * Get Instance.
     *
     * @return instance.
     */
    public synchronized static AppContextHolder getInstance() {
        if (instance == null) {
            instance = new AppContextHolder();
        }
        return instance;
    }


    /**
     * Initialize
     *
     * @param context WebApplicationContext.
     */
    public synchronized void init(WebApplicationContext context) {
        if (this.initialized) {
            LOG.error("AppContextHolder has already inttialized.");
            return;
        }
        this.wac = context;
        this.sc = context.getServletContext();
        this.initialized = true;
    }


    /**
     * Get Bean
     *
     * @param id ID.
     * @return Object.
     */
    public static Object getBean(String id) {
        return instance.wac.getBean(id);
    }

    /**
     * Get Beans of type
     *
     * @param calss
     * @return
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> calss) {
        return instance.wac.getBeansOfType(calss);
    }


    /**
     * get real path
     *
     * @param path path
     * @return
     */
    public static String getRealPath(String path) {
        return instance.sc.getRealPath(path);
    }
    

    /** initialized flag */
    private boolean initialized = false;

    /** Servlet Context */
    private ServletContext sc = null;
    /** WebApplicationContext */
    private WebApplicationContext wac = null;
}
