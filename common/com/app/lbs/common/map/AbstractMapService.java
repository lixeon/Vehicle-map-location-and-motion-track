/*
 * lbsgw
 */
package com.app.lbs.common.map;

import org.apache.log4j.Logger;

import com.app.lbs.common.cache.ICache;
import com.app.lbs.common.cache.impl.JdbmCacheManager;
import com.app.platform.base.utils.StringUtil;

/**
 * Abstract Map Service
 *
 * @author colen
 *
 */
public abstract class AbstractMapService implements MapSerivce {

    /** logger */
    private static final Logger LOG = Logger.getLogger(AbstractMapService.class);

    private static final String CACHE_NAME = "map-lnglat-";
    private static final String PK_PLNG = "n";
    private static final String PK_PLAT = "a";
    
    
    /**
     * get LngLat From local Cache
     *
     * @param point point
     * @param type type
     * @return P2D
     */
    @SuppressWarnings("unchecked")
    protected P2D getLnglatFromCache(P2D point, int coordType) {
        ICache<String, String> cache = (ICache<String, String>)
                JdbmCacheManager.getCache(CACHE_NAME + coordType);

        String lng = cache.get(PK_PLNG + point.x);
        String lat = cache.get(PK_PLAT + point.y);
        if (StringUtil.isTrimEmpty(lng) || StringUtil.isTrimEmpty(lat)) {
            return null;
        }

        if (LOG.isInfoEnabled()) {
            LOG.info("hit cache success: ox=" + point.x + ", y="
                    + point.y + "; x=" + lng + ", y=" + lat);
        }

        return new P2D(Double.valueOf(lng), Double.valueOf(lat));
    }


    /**
     * Save Converted LngLat to Cache
     *
     * @param point
     * @param cvtPoint
     * @param coordType
     */
    @SuppressWarnings("unchecked")
    protected void storeLnglatToCache(P2D point, P2D cvtPoint, int coordType) {
        ICache<String, String> cache = (ICache<String, String>)
                JdbmCacheManager.getCache(CACHE_NAME + coordType);
        // store LNG
        cache.put(PK_PLNG + point.x, cvtPoint.x);
        // store LAT
        cache.put(PK_PLAT + point.y, cvtPoint.y);
        
        // commit;
        cache.commit();
    }
}
