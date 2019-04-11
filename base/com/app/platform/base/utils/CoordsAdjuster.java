/**
 * 
 */
package com.app.platform.base.utils;

/**
 * Coords Adjuster
 *
 * @author luxinxian
 *
 */
public class CoordsAdjuster {

    private final static double PI = 3.14159265358979324;
    private final static double R = 6378245.0;
    private final static double EE = 0.00669342162296594323;

    /** Adjuster Instance */
    private static final CoordsAdjuster instance = new CoordsAdjuster();
/*    private CoordType from = CoordType.WGS84;
    private CoordType to = CoordType.GCJ02;*/

    /**
     * WGS84 -> GCJ02
     *
     * @param latLng LatLng
     * @return
     */
    public static LatLng wgs2gcj(LatLng latLng) {
        return instance.adjust(latLng);
    }


    /**
     * Adjust LatLng
     *
     * @param latLng
     * @return
     */
    private LatLng adjust(LatLng latLng) {
        if (outOfChina(latLng)) {
            return latLng;
        }
        
        double dLat = adjustLat(latLng.lng - 105.0d, latLng.lat - 35.0d);
        double dLng = adjustLng(latLng.lng - 105.0d, latLng.lat - 35.0d);

        double radLat = latLng.lat / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((R * (1 - EE)) / (magic * sqrtMagic) * PI);
        dLng = (dLng * 180.0) / (R / sqrtMagic * Math.cos(radLat) * PI);
        
        // out >>
        return new LatLng(latLng.lat + dLat, latLng.lng + dLng);
    }
    
    private static double adjustLng(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }
    
    private double adjustLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }
    
    /**
     * Out of China
     *
     * @param latLng
     * @return
     */
    public static boolean outOfChina(LatLng latLng) {
        if (latLng.lng < 73.499413 || latLng.lng > 134.772810) {
            return true;
        }
        if (latLng.lat < 18.153521 || latLng.lat > 53.560973) {
            return true;
        }
        return false;
    }

    /**
     * CoordType
     *
     * @author luxinxian
     *
     */
    public enum CoordType {
        WGS84,GCJ02,BD09
    }

    /**
     * LatLng
     *
     * @author luxinxian
     *
     */
    public static class LatLng {
        public LatLng() {}
        public LatLng(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }
        public double lat = 0.0d;
        public double lng = 0.0d;
    }

}
