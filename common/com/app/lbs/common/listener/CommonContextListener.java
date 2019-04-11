/*
 * Copyleft 2011 Power by colen.
 *
 * Project: booking
 * Date: Jul 2, 2011
 */
package com.app.lbs.common.listener;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.apache.log4j.Logger;

import com.app.lbs.common.server.Server;
import com.app.lbs.common.servlet.DynServletConf;
import com.app.platform.base.listener.BaseContextListener;
import com.app.platform.base.utils.AppContextHolder;

/**
 * Common Use Context Listener. Can put all common operations here.
 *
 * @author colen
 *
 */
public abstract  class CommonContextListener extends BaseContextListener {


    /** logger */
    private static final Logger LOG = Logger.getLogger(CommonContextListener.class);

    /**
     * Get Properties Configure.
     *
     * @param fileNm
     * @return Properties
     */
    protected Properties getConfigProps(ServletContext context, String fileNm) {
        String path = getConfigRealPath(context, fileNm);
        Properties props = null;
        InputStream inStream = null;
        try {
            // Properties
            props = new Properties();
            File cf = new File(path);
            inStream = new FileInputStream(cf);
            props.load(inStream);
        } catch (IOException e) {
            LOG.error("Error occurred when Configure"
                    + "(" + fileNm + ")!Please check the file exist.");
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) { }
            }
        }
        // out >>>
        return props;
    }


    /**
     * Get Configure file real path.
     *
     * @param context
     * @param fileNm
     * @return
     */
    protected String getConfigRealPath(ServletContext context, String fileNm) {
        return context.getRealPath("") + File.separator + "WEB-INF"
                + File.separator + "config" + File.separator + fileNm;
    }
    
    /**
     * get configure files real path by suffix.
     *
     * @param context
     * @param suffix
     * @return files
     */
    protected String[] getConfigRealPathWithSuffix(ServletContext context, String suffix) {
        String configPath = context.getRealPath("") + File.separator + "WEB-INF" + File.separator + "config";
        // context.getr
        File configFile = new File(configPath);

        final String mysuffix = suffix;
        File[] sqlfiles = configFile.listFiles(new FileFilter() {
            public boolean accept(File file) {
                if (file.isFile() && file.getName().endsWith(mysuffix)) {
                    return true;
                } else {
                    return false;
                }
            }
        });

        String[] sqlfilesname = new String[sqlfiles.length];
        for (int i = 0; i < sqlfiles.length; i++) {
            sqlfilesname[i] = sqlfiles[i].getAbsolutePath();
        }

        return sqlfilesname;
    }
    
    
    /**
     * Start Dynamic Servlet
     *
     * @param context
     */
    protected void startDynServlets(ServletContext context) {
        LOG.info("startDynServlets begin...");
        Map<String, DynServletConf> servlets =  AppContextHolder.getBeansOfType(DynServletConf.class);
        Iterator<Entry<String, DynServletConf>> i = servlets.entrySet().iterator();
        DynServletConf servlet = null;
        while (i.hasNext()) {
            servlet = i.next().getValue();
            try {
                ((Server) servlet).start(new HashMap<String, Object>());
                ServletRegistration regInfo = context.addServlet(servlet.getName(), (Servlet) servlet);
                regInfo.addMapping(servlet.getMapping().split(","));
                
                LOG.info("servlet " + servlet.getName() + " registered!");
                
            } catch (Exception e) {
                LOG.error("exception", e);
            }
        }

        LOG.info("startDynServlets successfully.");
    }
}
