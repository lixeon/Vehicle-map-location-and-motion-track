/*
 * LBSAPP
 */
package com.app.lbs.common.cache;


/**
 * Cache Manager
 *
 * @author colen
 *
 */
public interface CacheManager {


    ICache<?, ?> getCache(String cacheName);

}
