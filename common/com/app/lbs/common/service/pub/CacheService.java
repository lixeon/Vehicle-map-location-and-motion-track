/*
 * Copyleft 2012 Power by colen.
 *
 * Project: app-crm
 * Date: Mar 17, 2012
 */
package com.app.lbs.common.service.pub;

import java.util.Map;

/**
 * Cache Service
 *
 * @author  colen
 */
public interface CacheService {

    /**
     * refresh data cache
     *
     * @param args
     */
    void refresh(Map<String, Object> args);
}
