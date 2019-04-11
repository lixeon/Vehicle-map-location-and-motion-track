/*
 * lbsgw
 */
package com.app.lbs.common.map;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.app.platform.base.utils.HttpClientUtil;
import com.app.platform.base.utils.StringUtil;

/**
 * Mapabc Service
 *
 * @author colen
 *
 */
public class MapabcServiceImpl extends AbstractMapService {
    
    /** logger */
    private static Logger LOG = Logger.getLogger(MapabcServiceImpl.class);
    
    /** Convert URL */
    private String cvtUrl = "http://search1.mapabc.com/sisserver";
    
    /** App KEY */
    private String appKey = "6c78cc9761b853ecf47cdc26dd82a5b7e319d44f8d825acff218001c95985cdb9d0519d6d23b5cbd";

    
    @Override
    public P2D lnglatToMap(P2D point) {
        P2D newPoint = null;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("config", "RGC");
            params.put("flag", "true");
            params.put("resType", "json");
            params.put("cr", "0");
            params.put("coors", point.x + "," + point.y);
            params.put("a_k", this.appKey);
            String resp = HttpClientUtil.post(this.cvtUrl, params);
            JSONObject json = JSON.parseObject(resp);
            JSONArray list = json.getJSONArray("list");
            JSONObject lnglat = list.getJSONObject(0);
            newPoint = new P2D(lnglat.getDoubleValue("x"), lnglat.getDoubleValue("y"));
        } catch (Exception ioe) {
            LOG.error("Exception when Convert Lnglat!", ioe);
            newPoint = point;
        }
        return newPoint;
    }


    /**
     * @param cvtUrl the cvtUrl to set
     */
    public void setCvtUrl(String cvtUrl) {
        this.cvtUrl = cvtUrl;
    }

    /**
     * @param appKey the appKey to set
     */
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }


    @Override
    public String getLocation(P2D point) {
        Map<String, String> args = new HashMap<>();
        args.put("config", "POSDES");
        args.put("x1", point.x);
        args.put("y1", point.y);
        args.put("a_k", this.appKey);

        // Post
        String result = "";
        try{
            result = HttpClientUtil.post(this.cvtUrl, args);
            if (!StringUtil.isEmpty(result)
                    && result.getBytes("gbk").length > 120) {
                int idx = result.indexOf(";");
                if (idx != -1) {
                    result = result.substring(idx + 1);
                }
            }
        } catch (Exception e) {
            LOG.error("invoke mapabc error!");
        }

        // out >>>
        return result;
    }
}
