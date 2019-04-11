/**
 * 
 */
package com.app.lbs.common.map;

import java.math.BigDecimal;

import com.app.platform.base.utils.CoordsAdjuster.LatLng;

/**
 * Point2D
 *
 * @author colen
 *
 */
public class P2D {

    /** x, y */
    public String x = "0.0";
    public String y = "0.0";

    /**
     * Constructor.
     *
     * @param x (Longitude)
     * @param y (Latitude)
     */
    public P2D(double x, double y) {
        this.x = (new BigDecimal(x)).setScale(8, BigDecimal.ROUND_DOWN).toPlainString();
        this.y = (new BigDecimal(y)).setScale(8, BigDecimal.ROUND_DOWN).toPlainString();
    }
    
    public String toString() {
        return "x=" + this.x + ",y=" + this.y;
    }


    public String[] toScale(int len) {
        String[] array = new String[2];
        String x = (new BigDecimal(this.x)).setScale(len, BigDecimal.ROUND_DOWN).toPlainString();
        String y = (new BigDecimal(this.y)).setScale(len, BigDecimal.ROUND_DOWN).toPlainString();
        array[0] = x;
        array[1] = y;
        return array;
    }

    
    public P2D addOffset(double lngOffset, double latOffset) {
        double dx = Double.valueOf(this.x);
        double dy = Double.valueOf(this.y);
        return new P2D(dx + lngOffset, dy + latOffset);
    }
    
    
    public LatLng toLatLng() {
        return new LatLng(Double.valueOf(this.y), Double.valueOf(this.x));
    }
}
