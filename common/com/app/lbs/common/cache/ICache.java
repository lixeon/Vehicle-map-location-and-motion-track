/*
 * lbsgw
 */
package com.app.lbs.common.cache;

import java.util.Iterator;
import java.util.Map.Entry;

/**
 * ICache
 *
 * @author colen
 *
 */
public interface ICache<K, V> {

    /**
     * get value
     *
     * @param key key
     * @return value
     */
    V get(K key);
    
    
    /**
     * store (not commit)
     *
     * @param key key
     * @param value value
     */
    void put(K key, V value);

    /**
     * commit
     */
    void commit();
    
    /**
     * store (and commit)
     *
     * @param key key
     * @param value value
     */
    void save(K key, V value);
    
    
    /**
     * Iterator
     *
     * @return Iterator
     */
    Iterator<Entry<K, V>> iterator();
    
    
    /**
     * Clear
     */
    void clear();
}
