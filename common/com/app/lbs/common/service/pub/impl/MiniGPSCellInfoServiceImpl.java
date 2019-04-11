/*
 * LBSAPP
 */
package com.app.lbs.common.service.pub.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.app.lbs.common.dao.CommonDaoEx;
import com.app.lbs.common.minigps.JsonUtil;
import com.app.lbs.common.minigps.LocPoiResponse;
import com.app.lbs.common.minigps.LocRadiusPoiResponse;
import com.app.lbs.common.minigps.MinigpsUtil;
import com.app.lbs.common.minigps.RemoteUtil;
import com.app.lbs.common.minigps.ReplaceBytes;
import com.app.lbs.common.minigps.WI;
import com.app.lbs.common.minigps.WifiAddressConverter;
import com.app.lbs.common.minigps.WifiList;
import com.app.lbs.common.service.pub.CellInfoService;
import com.app.platform.base.utils.StringUtil;
import com.app.platform.base.utils.SystemConfig;

/**
 * Default CellInfo Service
 *
 * @author colen
 *
 */
public class MiniGPSCellInfoServiceImpl implements CellInfoService {

    /** logger */
    private static Logger LOG = Logger.getLogger(MiniGPSCellInfoServiceImpl.class);
     
    @Override
    public synchronized LocRadiusPoiResponse getLngLatFromCellId(String cellId,String wifi) {
    	//新协议不需要分割
    	//注释开始
    	String wifis=null;
       // double[] lnglat = new double[2];
        LocRadiusPoiResponse loc = null;    	
       	if(wifi!=null)
       	{
            List<WI> mlist_wi = parseWifi(wifi);
            wifis = getWParameter(mlist_wi);
       	}
       	loc = getPOIFromMinigps(cellId,wifis);
        return loc;
    }
   
    
    //新协议
   // private double[] getFromRemote(String[] cell) {
    	
    private LocPoiResponse getFromRemote(String cell) {    	
        
        if (LOG.isInfoEnabled()) 
        {
            LOG.info("Get LngLat From cellid Remote Server Start...");
        }
        Map<String, String> req = new HashMap<>();
        //double[] lnglat = new double[2];
        LocRadiusPoiResponse loc = null;
    
       req.put("x",cell);
       req.put("p", "1");
       req.put("mt", "0");
       req.put("ta", "0");
       req.put("needaddress","0");
  
       try {
        	String respText;
            respText = MinigpsUtil.get(SystemConfig.get("minigpscell.location.url"), req);
              if (LOG.isInfoEnabled()) 
              {
                LOG.info("Remote Server url: " +SystemConfig.get("minigpscell.location.url")+req.toString());            	  
                LOG.info("Remote Server Response: " + respText);
            }
            if (!StringUtil.isTrimEmpty(respText)) 
            {
    			loc = (LocRadiusPoiResponse) JsonUtil.fromJson(LocRadiusPoiResponse.class, respText);
/*    			
            	JSONObject json = JSON.parseObject(respText);
                if ("OK".equals(json.get("cause"))) 
                {
                   //   JSONObject data = dataArr.getJSONObject(0);
                   lnglat[0] = json.getDoubleValue("lon");
                   lnglat[1] = json.getDoubleValue("lat");
                   json.getDoubleValue("lat");
                }
*/                
            }
        } catch (Exception e) {
            LOG.error("get lnglat from remote server error!", e);
        }
        
        return loc;
    }
    
    
 private LocPoiResponse postFromRemote(String cell,String wifi) {    	
        
        if (LOG.isInfoEnabled()) 
        {
            LOG.info("Get LngLat From cellidwifi Remote Server Start...");
        }
        Map<String, String> req = new HashMap<>();
      //  double[] lnglat = new double[2];
        LocRadiusPoiResponse loc = null;
       req.put("x",cell);
       req.put("p", "1");
       req.put("mt", "0");
       req.put("ta", "0");
       req.put("needaddress","0");
  
       try {
        	String respText;
            respText = MinigpsUtil.post(SystemConfig.get("minigpscell.location.url"), req);
              if (LOG.isInfoEnabled()) 
              {
                LOG.info("Remote Server url: " +SystemConfig.get("minigpscell.location.url")+req.toString());            	  
                LOG.info("Remote Server Response: " + respText);
            }
            if (!StringUtil.isTrimEmpty(respText)) 
            {
    			loc = (LocRadiusPoiResponse) JsonUtil.fromJson(LocRadiusPoiResponse.class, respText);
               // JSONObject json = JSON.parseObject(respText);
               // if ("OK".equals(json.get("cause"))) 
               // {
                   //   JSONObject data = dataArr.getJSONObject(0);
               //    lnglat[0] = json.getDoubleValue("lon");
               //    lnglat[1] = json.getDoubleValue("lat");
                //   getRadius = json.getDoubleValue("radius");              
               //  }
            }
        } catch (Exception e) {
            LOG.error("get lnglat from remote server error!", e);
        }
        
        return loc;
    }

    static private String getWParameter(List<WI> ws)
    {
        String w = null;
        if (ws != null && ws.size() > 0)
        {
            WifiList wlist = new WifiList();
            wlist.setWs(ws);
            w = JsonUtil.toJson(wlist);
        }
        return w;
    }
    
    private List<WI> parseWifi(String wifi)
    {
       /*   
    	Xilele
    	25
    	d842acd4ad68HK-Inside
    	e
    	0022aaad11c0HK-Guest
    	d
    	0022aaad11c1
    	*/
     	// 解析出多个wifi
        String[] str_wifi = wifi.split("\r");
        if(str_wifi.length<=1)
        	str_wifi= wifi.split("\n");
        List<WI> mlist_wi = new ArrayList<WI>();
        // String[] str_wifi = {"TP-LINK-15D0\r1c\rbc46997f15d0","NETGEAR\r16\r204e7f252082"};
        List<String> list = new ArrayList<String>();
        for (int i = 0; str_wifi != null && i < str_wifi.length; i++)
        {
            String str = str_wifi[i];
            System.out.println("第" + i + "位：" + str_wifi[i]);
            if (i!=0 && i % 2 == 0 && i!=str_wifi.length-1)
            {
                String s = str_wifi[i];
                String mac = s.substring(0, 12);
                String name = s.substring(12);
                list.add(mac);
                list.add(name);
            }
            else
            {
                list.add(str);
            }
        }
        for(int i = 0 ;list!=null &&  list.size()%3==0&& i<list.size();i+=3)
        {
            try
            {
                String testmac = list.get(i+2);
                long testlong = WifiAddressConverter.HexStr2Long(testmac);
                // long m = WifiAddressConverter.Str2Long(mac);
                String str_r =list.get(i+1);
                int r = Integer.valueOf(str_r, 16);
                String theRealSSID = list.get(i);
                String replacedSSID = new String(ReplaceBytes.replace(theRealSSID.getBytes("utf-8")), "utf-8");
                WI wi = new WI();
                wi.setM(testlong);
                wi.setR(r);
                wi.setS(replacedSSID);
                mlist_wi.add(wi);
            }
            catch (Exception e)
            {
                
                if (LOG.isInfoEnabled()) 
                {
                  LOG.info("WIFI 解析失败"+wifi);
                }
                continue;
            }
        }
        return mlist_wi.size() == 0 ? null : mlist_wi;
    }
    
  
    static public LocRadiusPoiResponse getPOIFromMinigps(String cell, String w)
    {
    	LocRadiusPoiResponse loc = null;
    	String result = null;
    	//double[] lnglat = new double[2];
    	String url=SystemConfig.get("minigpscellwifi.location.url");
    	url = url+"p=1&needaddress=0&mt=0";
        if (LOG.isInfoEnabled()) 
        {
          LOG.info("Wifi=>>"+w+"Cell=>>"+cell);
        }
    	if (cell != null)
    	{
    		url = url + "&x=" + cell;
    	}
    	if (w != null)
    	{
    		result = RemoteUtil.request(url, "POST", "application/json;charset=utf-8", w);
    	}
    	else
    	{
    		result = RemoteUtil.request(url, "GET", "application/json;charset=utf-8", null);
    	}
    	if (result != null)
    	{
    		loc = (LocRadiusPoiResponse)JsonUtil.fromJson(LocRadiusPoiResponse.class, result);
    		if (loc==null)
    		{
    		 LOG.info("minigps 转换失败");	
    		}
    		else if (LOG.isInfoEnabled()) 
            {
              LOG.info("minigps 转换结果:"+loc.getCause()+"Lat:"+loc.getLat()+"Lon:"+loc.getLon()+"Radius:"+loc.getRadius());
            }
    	}
    	return loc;
    }
   
    private CommonDaoEx commonDao = null;


    /**
     * getter
     * @return the commonDao
     */
    public CommonDaoEx getCommonDao() {
        return commonDao;
    }



    /**
     * setter
     * @param commonDao the commonDao to set
     */
    public void setCommonDao(CommonDaoEx commonDao) {
        this.commonDao = commonDao;
    }
    
}

