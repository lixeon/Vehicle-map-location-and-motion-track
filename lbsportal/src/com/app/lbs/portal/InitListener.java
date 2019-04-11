/**
 * 
 */
package com.app.lbs.portal;

import java.util.Enumeration;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.app.lbs.common.listener.CommonContextListener;
import com.app.platform.base.dao.NamingSqlFactory;
import com.app.platform.base.utils.AppContextHolder;
import com.app.platform.base.utils.SystemConfig;

/**
 * Portal  Initialize Listener.
 *
 * @author colen
 *
 */
public class InitListener extends CommonContextListener {
    
    /** Logger */
    private static final Logger LOG = Logger.getLogger(InitListener.class);


    @Override
    public void doInit(ServletContext context) {
        LOG.info("system booting...");

        long st = System.currentTimeMillis();

        // initialize system configure properties
        String sysFilePath = getConfigRealPath(context, "sysconfig.properties");
        SystemConfig.getInstance().init(sysFilePath, true);

        // initialize SQL beans
        initNamingSqlBeans(context);

        // initialize Spring Context
        this.contextLoader = new ContextLoader();
        WebApplicationContext webAppCtx = this.contextLoader.initWebApplicationContext(context);
        
        // initialize context holder
        AppContextHolder.getInstance().init(webAppCtx);

        // start dynamic servlets.
        startDynServlets(context);
        
        // nothing here now.
        long et = System.currentTimeMillis();

        LOG.info("system booted in " + ((et - st)) + "ms.");
    }


    private void initNamingSqlBeans(ServletContext context) {
        LOG.info("parse Namingsql start...");
        
        long st = System.currentTimeMillis();

        // sql files
        String[] sqlfiles = getConfigRealPathWithSuffix(context, ".sql.xml");

        // initialize Naming SQL Factory
        NamingSqlFactory.getInstance().init(sqlfiles);

        LOG.info("parse Namingsql finished. (" + (System.currentTimeMillis() -st) + "ms)");
    }


    @Override
    public void doDestroy(ServletContext context) {
        LOG.info("system stopping..");

        // nothing here now..
        SystemConfig.getInstance().destroy();
        
        if (contextLoader != null) {
            contextLoader.closeWebApplicationContext(context);
        }
        cleanupAttributes(context);

        LOG.info("system stoped.");
    }

    private void cleanupAttributes(ServletContext sc) {
        for (Enumeration<?> attrNames = sc.getAttributeNames(); attrNames.hasMoreElements();) {
            String attrName = (String) attrNames.nextElement();
            if (attrName.startsWith("org.springframework.")) {
                Object attrValue = sc.getAttribute(attrName);
                if (attrValue instanceof DisposableBean)
                    try {
                        ((DisposableBean) attrValue).destroy();
                    } catch (Throwable ex) {
                        LOG.error((new StringBuilder("Couldn't invoke destroy method of attribute with name '"))
                                .append(attrName).append("'").toString(), ex);
                    }
            }
        }
    }

    /** Context loader */
    private ContextLoader contextLoader = null;
}
