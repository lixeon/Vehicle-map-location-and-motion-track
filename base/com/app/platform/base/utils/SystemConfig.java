/*
 * Copyleft 2011 Power by colen.
 *
 * Project: booking
 * Date: Jul 2, 2011
 */
package com.app.platform.base.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;


/**
 * System Configure (can auto reload)
 *
 * @author colen
 *
 */
public class SystemConfig {

    /** LOG */
    private static final Logger LOG = Logger.getLogger(SystemConfig.class);

    /** Constructor */
    private SystemConfig() {}
    /** Instance. */
    private static SystemConfig instance = new SystemConfig();
    /** Properties */
    private volatile Properties props = null;
    /** configure file */
    private String filePath = null;
    /** Reload. */
    private boolean reload = false;
    /** Is initialized */
    private boolean initialized = false;
    /** Loader */
    private Reloadable loader = null;

    /** Sleep time. */
    private static final long SLEEP_TIME = 1000L * 60;
    
    /**
     * Get Instance.
     *
     * @return instance.
     */
    public static SystemConfig getInstance() {
        return instance;
    }

    
    /**
     * Initialized
     *
     * @param f sysconfig.properties
     * @param reload
     */
    public boolean init(String filePath, boolean reload) {
        if (this.initialized) {
            LOG.info("SystemConfig already initialized!!");
            return false; 
        }
        if (StringUtil.isTrimEmpty(filePath)) {
            LOG.info("SystemConfig File Path is EMPTY!!");
            return false;
        }

        this.filePath = filePath;
        this.reload = reload;
        this.initialized = loadFile();

        if (this.initialized && this.reload) {
            this.loader = this.new Reloadable();
            Thread t = new Thread(this.loader);
            t.setDaemon(true);
            t.start();
        }
        
        // out >>>
        return true;
    }

    /**
     * Destroy.
     *
     */
    public void destroy() {
        if (instance.loader != null) {
            instance.loader.setAlive(false);
        }
    }

    /**
     * Load File.
     *
     * @return success
     */
    private boolean loadFile() {
        File f = new File(this.filePath);
        if (f == null || !f.exists()) {
            LOG.error("Can not find File by Path ("
                    + this.filePath + "), Initialized Fail!!");
            return false;
        }

        InputStream inStream = null;
        try {
            // Properties
            Properties myprops = new Properties();
            inStream = new FileInputStream(f);
            myprops.load(inStream);

            // refresh
            this.props = myprops;
        } catch (IOException e) {
            LOG.error("Error occurred when Configure"
                    + "(" + f + ")!Please check the file exist.");
            return false;
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {}
            }
        }
        
        // out >>>
        return true;
    }


    /**
     * Get Value By Key
     *
     * @param key Key.
     * @return value
     */
    public static String getValue(String key) {
        if (!instance.initialized) {
            LOG.error("System Configure did not initialized, but illegal invoke!");
            throw new IllegalStateException("System Configure did not Initialized!");
        }
        return (String) instance.props.get(key);
    }
    
    
    /**
     * Get Value By Key And
     *
     * @param key
     * @param args
     * @return
     */
    public static String get(String key, Object ... args) {
        String value = getValue(key);
        if (args != null && args.length > 0) {
            return MessageFormat.format(value, args);
        } else {
            return value;
        }
    }
    
    
    
    /**
     * get long value
     *
     * @param key
     * @return
     */
    public static long getLong(String key) {
        return Long.valueOf(getValue(key));
    }
    
    /**
     * get boolean value
     *
     * @param key
     * @return
     */
    public static boolean getBoolean(String key) {
        return Boolean.valueOf(getValue(key));
    }

    /**
     * Get vlaue as list
     *
     * @param key
     * @return
     */
    public static List<String> getValList(String key, String separtor) {
        String value = getValue(key);
        List<String> list = new ArrayList<String>();
        if (value != null && value.length() > 0) {
            String[] arrVal = value.split(separtor);
            for (String val : arrVal) {
                list.add(StringUtil.trim(val));
            }
        }
        // out >>>
        return list;
    }
    
    
    /**
     * get list by prefix
     * for example (conf.cmcc.dv.LOGIN=0 --> map {_key:LOGIN, _value:0})
     *
     * @param prefix prefix string
     * @param stripPrefix
     * @return
     */
    public static List<Map<String, Object>> getListByPrefix(String prefix, boolean stripPrefix) {
        if (!instance.initialized) {
            LOG.error("System Configure did not initialized, but illegal invoke!");
            throw new IllegalStateException("System Configure did not Initialized!");
        }

        Iterator<Map.Entry<Object, Object>> it = instance.props.entrySet().iterator();
        Entry<Object, Object> entry = null;
        String key = null;
        
        List<Map<String, Object>> list = null;
        Map<String, Object> map = null;

        while (it.hasNext()) {
            entry = it.next();
            key = (String) entry.getKey();
            if (key.startsWith(prefix)) {
                map = new HashMap<String, Object>();
                if (stripPrefix) {
                    map.put("_key", key.replaceAll(prefix, "").trim());
                    map.put("_value", entry.getValue());
                } else {
                    map.put("_key", key);
                    map.put("_value", entry.getValue());
                }
                
                if (list == null) {
                    list = new ArrayList<Map<String,Object>>();
                }
                
                list.add(map);
            }
        }

        return list;
    }
    
    public static Map<String, String> getMapByPrefix(String prefix, boolean stripPrefix) {
        if (!instance.initialized) {
            LOG.error("System Configure did not initialized, but illegal invoke!");
            throw new IllegalStateException("System Configure did not Initialized!");
        }
        
        Iterator<Map.Entry<Object, Object>> it = instance.props.entrySet().iterator();
        Entry<Object, Object> entry = null;
        String key = null;
        
        Map<String, String> map = null;
        
        while (it.hasNext()) {
            entry = it.next();
            key = (String) entry.getKey();
            if (key.startsWith(prefix)) {
                if (map == null) {
                    map = new HashMap<String, String>();
                }
                if (stripPrefix) {
                    map.put(key.replaceAll(prefix, "").trim(), entry.getValue().toString());
                } else {
                    map.put(key, entry.getValue().toString());
                }
            }
        }

        return map;
    }

    /**
     * Reload Runnable.
     *
     * @author colen
     */
    private class Reloadable implements Runnable {
        /** Constructor */
        public Reloadable() { this.alive = true; }
        /** Runner */
        public void run() {
            while (isAlive()) {
                loadFile();
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {}
            }
            LOG.info("Reload Task Stop!");
        }

        private boolean alive = false;
        public synchronized boolean isAlive() {
            return alive;
        }
        public synchronized void setAlive(boolean alive) {
            this.alive = alive;
        }
    }
}
