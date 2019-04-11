/*
 * Copyleft 2011 Power by colen.
 *
 * Project: booking
 * Date: Jun 30, 2011
 */
package com.app.platform.base.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Base Context Listener
 *
 * @author colen
 *
 */
public abstract class BaseContextListener implements ServletContextListener {

    /**
     * Initialize Method
     *
     * @param event Argument.
     *
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        // Initialize
        doInit(event.getServletContext());
    }

    /**
     * Register Shutdown Task (Before JVM Shutdown, do the work)
     *
     * @param work Work
     */
    protected void addShutdownWork(Thread work) {
        Runtime.getRuntime().addShutdownHook(work);
    }


    /**
     * Do Initialize
     * @param context Web Application Context.
     */
    public abstract void doInit(ServletContext context);


    /**
     * Destroy Method
     *
     * @param event Argument.
     *
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        doDestroy(event.getServletContext());
    }

    /**
     * Destroy
     *
     * @param context Context.
     */
    public abstract void doDestroy(ServletContext context);
}
