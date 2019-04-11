/**
 * 
 */
package com.app.lbs.common.map;

import org.apache.http.client.fluent.Request;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.app.platform.base.utils.CoordsAdjuster;
import com.app.platform.base.utils.CoordsAdjuster.LatLng;
import com.app.platform.base.utils.StringUtil;

/**
 * Google Map Service
 *
 * @author luxinxian
 *
 */
public class GoogleMapServiceImpl extends AbstractMapService {

    /** logger */
    private static Logger LOG = Logger.getLogger(GoogleMapServiceImpl.class);
    
    /** gecoder url */
    private String geocoderUrl = "http://maps.googleapis.com/maps/api/geocode/json";

    /** application key */
    private String appkey = "AIzaSyCAZSFY6BUkHiIV7XSNp-omPnGM_57V9eg";
    
    
    @Override
    public P2D lnglatToMap(P2D point) {
        double lng = Double.valueOf(point.x);
        double lat = Double.valueOf(point.y);
        
        // convert
        LatLng latLng = new LatLng(lat, lng);
        latLng = CoordsAdjuster.wgs2gcj(latLng);
        
        return new P2D(latLng.lng, latLng.lat);
    }


    @Override
    public String getLocation(P2D point) {
        String location = "";
        try {
            P2D newPoint = this.lnglatToMap(point);
            StringBuilder bud = new StringBuilder(this.geocoderUrl);
            bud.append("?latlng=").append(newPoint.y);
            bud.append(",").append(newPoint.x);
            bud.append("&sensor=false");

            // execute get
            String resp = Request.Get(bud.toString()).execute().returnContent().asString();
            
            if (LOG.isInfoEnabled()) {
                LOG.info(point.x + "," + point.y + " get location form baidu: " + resp);
            }

            if (!StringUtil.isTrimEmpty(resp)) {
                JSONObject json = JSON.parseObject(resp);
                if ("OK".equals(json.getString("status"))) {
                    JSONArray results = json.getJSONArray("results");
                    if (results != null && !results.isEmpty()) {
                        JSONObject result = (JSONObject) results.get(0);
                        location = result.getString("formatted_address");
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("get location info error by Point:" + point, e);
        }

        return location;
    }


    /**
     * Setter
     * @param geocoderUrl the geocoderUrl to set
     */
    public void setGeocoderUrl(String geocoderUrl) {
        this.geocoderUrl = geocoderUrl;
    }


    /**
     * Setter
     * @param appkey the appkey to set
     */
    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

}
