/*
 * LBSAPP
 */
package com.app.lbs.common.cache.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple Cache
 *
 * @author colen
 *
 */
public class SimpleCache {

    /** Gsm Area -> Lnglat Mapping */
    public static Map<String, double[]> GSM_AREA_LNGLAT = new ConcurrentHashMap<>();
    /** CDMA Area -> Lnglat Mapping */
    public static Map<String, double[]> CDMA_AREA_LNGLAT = new ConcurrentHashMap<>();    
}
