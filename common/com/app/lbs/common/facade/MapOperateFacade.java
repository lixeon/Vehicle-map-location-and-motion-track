/*
 * lbsgw
 */
package com.app.lbs.common.facade;

import com.app.lbs.common.CodeConst;
import com.app.lbs.common.map.MapSerivce;
import com.app.lbs.common.map.P2D;
import com.app.platform.base.utils.AppContextHolder;
import com.app.platform.base.utils.CoordsAdjuster;
import com.app.platform.base.utils.StringUtil;
import com.app.platform.base.utils.SystemConfig;

/**
 * Map Options Facade
 *
 * @author colen
 *
 */
public class MapOperateFacade {

    /** Service Key Prefix String */
    private static final String SVC_KEY_PREFIX = "common_mapService_";

    /**
     * Convert Lnglat to Map display Point.
     *
     * @param point Point
     * @return P2D
     */
    public static P2D lnglatToMap(P2D point) {
        MapSerivce service = getMapService();
        if (service != null) {
            return service.lnglatToMap(point);
        } else {
            return point;
        }
    }


    /**
     * get position info
     *
     * @param point point
     * @return position by point
     */
    public static String getPositionInfo(P2D point) {
        MapSerivce service = getMapService(point);
        if (service == null) {
            return "";
        }

        return service.getLocation(point);
    }
    
    
    /**
     * get position info
     *
     * @param lng
     * @param lat
     * @return position info
     */
    public static String getPositionInfo(String lng, String lat) {
        if (StringUtil.isTrimEmpty(lng) || StringUtil.isTrimEmpty(lat)) {
            return "";
        }
        
        // LngLat
        double dlng = Double.valueOf(lng);
        double dlat = Double.valueOf(lat);

        String positionInfo = "";
        if (dlng != 0.0d && dlat != 0.0d) {
            P2D point = new P2D(dlng, dlat);
            positionInfo = getPositionInfo(point);
        }

        return positionInfo;
    }

    private static MapSerivce getMapService() {
        String type = SystemConfig.get("map.mapsvc.type");
        return (MapSerivce) AppContextHolder.getBean(SVC_KEY_PREFIX + type);
    }
    
    private static MapSerivce getMapService(P2D point) {
        String type = SystemConfig.get("map.mapsvc.type");
        if (CodeConst.MAP_SVCTYPE_GOOGLE.equals(type)) {
            if (CoordsAdjuster.outOfChina(point.toLatLng())) {
                return (MapSerivce) AppContextHolder.getBean(SVC_KEY_PREFIX + type);
            }
        }
        return (MapSerivce) AppContextHolder.getBean(SVC_KEY_PREFIX + CodeConst.MAP_SVCTYPE_BAIDU);
    }
}
