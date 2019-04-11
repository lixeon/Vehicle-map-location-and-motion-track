/*
 * lbsgw
 */
package com.app.lbs.common.cache.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jdbm.PrimaryTreeMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import org.apache.log4j.Logger;

import com.app.lbs.common.cache.ICache;

/**
 * Cache Manager
 *
 * @author colen
 *
 */
public class JdbmCacheManager {

    /** logger */
    private static final Logger LOG = Logger.getLogger(JdbmCacheManager.class);
    
    /** Cache Manager */
    private static JdbmCacheManager instance = null;

    /** Record Manager */
    private RecordManager recMan = null;

    /** cache holder */
    private Map<String, ICache<?, ?>> holder = null;

    /** lock: "can optimize the lock" */
    private static final byte[] lock = new byte[0];

    /**
     * get instance
     *
     * @return CacheManger
     */
    public static synchronized JdbmCacheManager getInstance() {
        if (instance == null) {
            instance = new JdbmCacheManager();
        }
        return instance;
    }


    /**
     * initialize
     *
     * @param path Path
     */
    public void init(String path) {
        try {
            this.recMan = RecordManagerFactory.createRecordManager(path);
            this.holder = new HashMap<String, ICache<?,?>>();
        } catch (IOException ioe) {
            LOG.error("Cannot initialize Cache Manager! " + path, ioe);
        }
    }
    

    /**
     * get or create cache
     *
     * @param cacheName Cache Name
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static ICache<?,?> getCache(String cacheName) {
        if (instance.recMan == null) {
            throw new IllegalStateException("Cache Manager NOT prepared!");
        }

        ICache<?, ?> cache = null;
        synchronized (lock) {
            cache = instance.holder.get(cacheName);
            if (cache == null) {
                PrimaryTreeMap<?, ?> ptm = instance.recMan.treeMap(cacheName);
                cache = new LocalCache(instance.recMan, ptm);
                instance.holder.put(cacheName, cache);
            }
        }

        return cache;
    }

    /*
    public static void main(String[] args) {
        CacheManger cm = CacheManger.getInstance();
        cm.init("C:\\_cache\\test");
        
        final ICache<String, String> cache = (ICache<String, String>) cm.getCache("cachedata");
        final int C = 100000;
        for (int i = 0; i < 2; i++) {
            new Thread() {
                public void run() {
                    int v = C;
                    while (v-- > 0) {
                        cache.put(String.valueOf(v), String.valueOf(v));
                        if (v % 100 == 0) {
                            System.out.println(" save: " + (C - v));
                            //cache.commit();
                        }
                    }
                }
            }.start();
        }
    }*/
}
