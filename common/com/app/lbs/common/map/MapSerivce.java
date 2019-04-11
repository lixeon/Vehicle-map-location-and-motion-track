/*
 * lbsgw
 */
package com.app.lbs.common.map;


/**
 * Map Service
 *
 * @author colen
 *
 */
public interface MapSerivce {

    /** coordinate type: gaode */
    int COORD_TYPE_GAODE = 1;

    /** coordinate type: baidu */
    int COORD_TYPE_BAIDU = 2;

    /**
     * Convert LngLat to Map Display Croods.
     *
     * @param point Point.
     * @return P2D Array
     */
    P2D lnglatToMap(P2D point);
    
    
    /**
     * get location
     *
     * @param point Point
     * @return location
     */
    String getLocation(P2D point);
}
