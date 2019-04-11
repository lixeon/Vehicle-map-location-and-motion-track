/*
 * lbsgw
 */
package com.app.lbs.common.cache.impl;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import jdbm.PrimaryMap;
import jdbm.RecordManager;

import org.apache.log4j.Logger;

import com.app.lbs.common.cache.ICache;

/**
 * Local Cache
 *
 * @author colen
 *
 */
public class LocalCache<K, V> implements ICache<K, V> {

    /** logger */
    private static Logger LOG = Logger.getLogger(LocalCache.class);
    
    /** holder */
    private PrimaryMap<K, V> holder = null;
    private RecordManager recMan = null;
    
    /**
     * Constructor
     *
     * @param holder
     */
    public LocalCache(RecordManager recMan, PrimaryMap<K, V> holder) {
        this.holder = holder;
        this.recMan = recMan;
    }

    @Override
    public V get(K key) {
        try {
            return this.holder.find(key);
        } catch (IOException ioe) {
            LOG.error("Can not access local cache! get", ioe);
            return null;
        }
    }

    @Override
    public void put(K key, V value) {
        this.holder.put(key, value);
    }

    @Override
    public void commit() {
        try {
            this.recMan.commit();
        } catch (IOException ioe) {
            LOG.error("Can not access local cache! commit.", ioe);
        }
    }

    @Override
    public void save(K key, V value) {
        put(key, value);
        commit();
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return this.holder.entrySet().iterator();
    }

    @Override
    public void clear() {
        this.holder.clear();
        commit();
    }
}
