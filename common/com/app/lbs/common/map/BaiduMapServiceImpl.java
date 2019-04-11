/*
 * lbsgw
 */
package com.app.lbs.common.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.client.fluent.Request;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.Base64;
import com.app.lbs.common.CodeConst;
import com.app.platform.base.utils.CommonUtil;
import com.app.platform.base.utils.StringUtil;

/**
 * Baidu Map Serivce
 *
 * @author colen
 *
 */
public class BaiduMapServiceImpl extends AbstractMapService {
    
    /** logger */
    private static Logger LOG = Logger.getLogger(BaiduMapServiceImpl.class);

    /** convert url */
    private String cvtUrl = "http://api.map.baidu.com/ag/coord/convert";

    /** gecoder url */
    private String geocoderUrl = "http://api.map.baidu.com/geocoder/v2/";

    /** application key */
    private String appkey = "75ed68bcaf3a44751605754c532aa350";


    @Override
    public P2D lnglatToMap(P2D point) {
/*        P2D newPoint = getLnglatFromCache(point, COORD_TYPE_GAODE);
        if (newPoint != null) {
            return newPoint;
        }*/
        P2D newPoint = null;
        try {

            // coords: 0->wgs84, 2->google(mapabc), 4->baidu
            String uri = this.cvtUrl + "?from=0&to=2&x=" + point.x + "&y=" + point.y;
            String resp = Request.Get(uri).execute().returnContent().asString();
            JSONObject json = JSON.parseObject(resp);
            String x = new String(Base64.decodeFast(json.getString("x")));
            String y = new String(Base64.decodeFast(json.getString("y")));
            newPoint = new P2D(Double.valueOf(x), Double.valueOf(y));
            
            // store to local cache TODO
            // storeLnglatToCache(point, newPoint, COORD_TYPE_GAODE);

        } catch (Exception ioe) {
            LOG.error("Convert LngLat Exception!", ioe);
            // return old
            newPoint = point;
        }

        return newPoint;
    }
    
    
    @Override
    public String getLocation(P2D point) {
        String location = "";
        try {
            StringBuilder bud = new StringBuilder(this.geocoderUrl);
            bud.append("?ak=").append(this.appkey);
            bud.append("&coordtype=").append(CodeConst.COORD_TYPE_WGS84);
            bud.append("&location=").append(point.y).append(",").append(point.x);
            bud.append("&output=json");
            bud.append("&pois=1");

            // execute get
            String resp = Request.Get(bud.toString()).execute().returnContent().asString();
            
            if (LOG.isInfoEnabled()) {
                LOG.info(point.x + "," + point.y + " get location form baidu: " + resp);
            }

            if (!StringUtil.isTrimEmpty(resp)) {
                JSONObject json = JSON.parseObject(resp);
                JSONObject result = json.getJSONObject("result");
                if (result != null) {
                    location = getRigthLocation(result);
                }
            }
        } catch (Exception ioe) {
            LOG.error("get location info error by Point:" + point, ioe);
        }

        return location;
    }


    private String getRigthLocation(JSONObject result) {
        
        JSONArray array = result.getJSONArray("pois");
        List<Poi> poiList = null;
        if (array != null) {
            Poi poi = null;
            JSONObject jsonObj = null;
            poiList = new ArrayList<>();
            for (Object o : array) {
                jsonObj = (JSONObject) o;
                poi = new Poi();
                
                poi.addr = jsonObj.getString("addr");
                poi.name = jsonObj.getString("name");
                poi.distance = Double.valueOf(jsonObj.getString("distance")).intValue();
                
                poiList.add(poi);
            }
            
            Collections.sort(poiList);
        }

        StringBuilder locDesc = new StringBuilder();
        if (!CommonUtil.isEmpty(poiList)) {
            Poi poi = poiList.get(0);
            locDesc.append(poi.addr).append(poi.name).append(poi.distance).append("\u7C73\u9644\u8FD1");
            if (poiList.size() > 1) {
                poi = poiList.get(1);
                locDesc.append("\uFF0C").append(poi.addr).append(poi.name).append(poi.distance).append("\u7C73\u9644\u8FD1");
            }
        } else {
            locDesc.append(result.getString("formatted_address"));
        }
        
        return locDesc.toString();
    }
    
    /**
     * POI
     *
     * @author colen
     *
     */
    private static class Poi implements Comparable<Poi> {
        String addr = null;
        String name = null;
        Integer distance = Integer.MAX_VALUE;

        @Override
        public int compareTo(Poi o) {
            return this.distance.compareTo(o.distance);
        }
    }


    /**
     * @param cvtUrl the cvtUrl to set
     */
    public void setCvtUrl(String cvtUrl) {
        this.cvtUrl = cvtUrl;
    }

    /**
     * @param geocoderUrl the geocoderUrl to set
     */
    public void setGeocoderUrl(String geocoderUrl) {
        this.geocoderUrl = geocoderUrl;
    }


    /**
     * @param appkey the appkey to set
     */
    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

}
