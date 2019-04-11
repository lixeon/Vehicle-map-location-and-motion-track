/*
 * Copyleft 2012 Power by colen.
 *
 * Project: app-crm
 * Date: May 12, 2012
 */
package com.app.platform.base.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;


/**
 * Naming Sql Factory
 *
 * @author colen
 *
 */
public final class NamingSqlFactory {


    /**
     * Constructor
     *
     */
    private NamingSqlFactory() {}

    private static NamingSqlFactory instance = null;
    
    /** initialize */
    private boolean initialize = false;

    /** Application context */
    private ApplicationContext appctx = null;

    /**
     * initialize
     *
     */
    public synchronized void init(String[] sqlfiles) {
        if (this.initialize) {
            throw new java.lang.IllegalStateException("NamingSqlFactory has already bean initialized!");
        }
        
        int idx = 0;
        String[] mySqlfiles = new String[sqlfiles.length];
        for (String sqlfile : sqlfiles) {
            mySqlfiles[idx] = "file:" + sqlfile;
            idx++;
        }

        this.appctx = new FileSystemXmlApplicationContext(mySqlfiles);

        this.initialize = true;
    }

    /**
     * Get Instance.
     *
     * @return instance.
     */
    public synchronized static NamingSqlFactory getInstance() {
        if (instance == null) {
            instance = new NamingSqlFactory();
        }
        return instance;
    }


    /**
     * get naming sql
     *
     * @param sqlId SQL ID
     * @return Naming sql
     */
    public static NamingSql getNamingSql(String sqlId) {
        NamingSql sql = null;
        try {
            sql = (NamingSql) instance.appctx.getBean(sqlId);
        } catch (Exception e) {
            // should not happen
        }
        return sql;
    }
}
