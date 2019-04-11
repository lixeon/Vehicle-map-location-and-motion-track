/*
 * LBSAPP
 */
package com.app.lbs.common.service.pub.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.app.lbs.common.cache.impl.SimpleCache;
import com.app.lbs.common.dao.CommonDaoEx;
import com.app.lbs.common.minigps.LocRadiusPoiResponse;
import com.app.lbs.common.service.pub.CellInfoService;
import com.app.platform.base.utils.HttpClientUtil;
import com.app.platform.base.utils.StringUtil;
import com.app.platform.base.utils.SystemConfig;

/**
 * Default CellInfo Service
 *
 * @author colen
 *
 */
public class DefaultCellInfoServiceImpl implements CellInfoService {

    /** logger */
    private static Logger LOG = Logger.getLogger(DefaultCellInfoServiceImpl.class);
    /**
     *  this method should optimize in concurrent situation.
     *
     * @param cellId
     * @return
     */
    @Override
    public synchronized LocRadiusPoiResponse getLngLatFromCellId(String cellId,String wifi) {
    	String msplit = SystemConfig.get("cell.multi_cellid.split");
    	String split  = SystemConfig.get("cell.cellid.split");
    	String[] marr = cellId.split("\\" + msplit);
    	double[][] mlnglat = new double[8][2]; 
    	int cellnum  = marr.length;
    	int validnum = 0;
        LocRadiusPoiResponse loc=null;
    	double[] fin_lnglat = new double[2];     	
    	
    	LOG.info("multi cell data :" + cellId + ", Number of cell : " + cellnum);
    	cellnum=1;
    	for(int i = 0; i < cellnum; i++)
    	{	    		
	        String[] arr = marr[i].split("\\" + split);
	        String CellPakage;
	        if (arr == null || arr.length <4) 
	        {
	            arr = cellId.split("\\:");
	            if (arr == null || arr.length < 4) {
	                LOG.warn("Error format Cell Info: " + marr[i]);
	                continue;
	            }
	        }
	        CellPakage=arr[0]+"\\:"+arr[1]+"\\:"+arr[2]+"\\:"+arr[3];
            // get from cache.
	        double[] lnglat; 
	        if(arr[0].equals("000"))
	        {
	        	 lnglat = SimpleCache.CDMA_AREA_LNGLAT.get(CellPakage);
	        }
	        else
	        {
	        	 lnglat = SimpleCache.GSM_AREA_LNGLAT.get(CellPakage);	
	        }
	        if (lnglat != null) 
	        {
	        	mlnglat[i] = lnglat;
	        	validnum++;
	        	continue;
	        }
	        
	        // get from DB.
	        lnglat = new double[2];
	        List<Map<String, Object>> list = getCommonDao().queryForList("select * from T_CELL_INFO where CELL_ID=?",CellPakage);
	        if (list != null && !list.isEmpty()) {
	            Map<String, Object> cellInfo = list.get(0);
	            lnglat[0] = Double.valueOf(cellInfo.get("lng").toString());
	            lnglat[1] = Double.valueOf(cellInfo.get("lat").toString());
	            
	            // hold to cache
	            if(arr[0].equals("000"))
	            	SimpleCache.CDMA_AREA_LNGLAT.put(CellPakage, lnglat);
	            else
	            	SimpleCache.GSM_AREA_LNGLAT.put(CellPakage, lnglat);
	            mlnglat[i] = lnglat;
	            validnum++;
	        	continue;
	        }
	        
	        // get from remote invoke.
	        loc = getFromRemote(arr);
	        if (lnglat[0] == 0.0d && lnglat[1] == 0.0d) 
	        {	        	
	        	continue;
	        }
	        
	        // hold to cache
	        if(arr[0].equals("000"))
            	SimpleCache.CDMA_AREA_LNGLAT.put(marr[i], lnglat);
	        else
            	SimpleCache.GSM_AREA_LNGLAT.put(marr[i], lnglat);
	        // save to DB
	        getCommonDao().update("insert into T_CELL_INFO values (?,?,?)",CellPakage, lnglat[0], lnglat[1]);
    	}
    	
    	for(int i = 0; i < marr.length; i++)
    	{    		
    		fin_lnglat[0] += mlnglat[i][0];
    		fin_lnglat[1] += mlnglat[i][1];
    	}	
    	if(validnum==0)
    		validnum=1;
    	fin_lnglat[0] /= validnum;
    	fin_lnglat[1] /= validnum;
    	
        return loc;
    }
    
    

    private LocRadiusPoiResponse getFromRemote(String[] cell) {
        
        if (LOG.isInfoEnabled()) 
        {
            LOG.info("Get LngLat From Remote Server Start...");
        }
        
        double[] lnglat = new double[2];
        LocRadiusPoiResponse loc=null;
        boolean cdmacell=false; 
        Map<String, String> params = new HashMap<>();
        //http://v.juhe.cn/cell/get?mnc=0&cell=28655&lac=17695&key=APPKEY
        //http://v.juhe.cn/cdma/?sid=14123&cellid=1099&nid=73&key=APPKEY
        if(cell[0].equals("000"))
        	cdmacell=true;
        if(cdmacell)
        {
        	params.put("sid", Integer.valueOf(cell[1]).toString());
        	params.put("nid", Integer.valueOf(cell[2]).toString());
        	params.put("cellid", Integer.valueOf(cell[3]).toString());
        	params.put("dtype", "json");
        	params.put("key", SystemConfig.get("cell.cdmalocation.key"));
        }
        else
        {
        	params.put("mnc", Integer.valueOf(cell[1]).toString());
        	params.put("lac", Integer.valueOf(cell[2]).toString());
        	params.put("cell", Integer.valueOf(cell[3]).toString());
        	params.put("dtype", "json");
        	params.put("key", SystemConfig.get("cell.location.key"));
        }

        try {
        	String respText;
            if(cdmacell)
            	respText = HttpClientUtil.get(SystemConfig.get("cdmacell.location.url"), params);
            else
            	respText= HttpClientUtil.get(SystemConfig.get("cell.location.url"), params);
            
            if (LOG.isInfoEnabled()) {
                LOG.info("Remote Server Response: " + respText);
            }
            
            if (!StringUtil.isTrimEmpty(respText)) {
                JSONObject json = JSON.parseObject(respText);
                if ("200".equals(json.get("resultcode"))) 
                {
                    JSONObject result = json.getJSONObject("result");
                    if (result != null) 
                    {
                       if(cdmacell)
                       {
                    	//JSONArray dataArr = result.getJSONArray("data");
                       // if (dataArr != null && dataArr.size() > 0) 
                        {
                         //   JSONObject data = dataArr.getJSONObject(0);
                            lnglat[0] = result.getDoubleValue("lon");
                            lnglat[1] = result.getDoubleValue("lat");
                        }
                       }
                       else
                       {
                         	JSONArray dataArr = result.getJSONArray("data");
                            if (dataArr != null && dataArr.size() > 0) 
                            {
                                JSONObject data = dataArr.getJSONObject(0);
                                lnglat[0] = data.getDoubleValue("LNG");
                                lnglat[1] = data.getDoubleValue("LAT");
                            }
                       }
                       
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("get lnglat from remote server error!", e);
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
