/*
 * Copyleft 2011 Power by colen.
 *
 * Project: booking
 * Date: Jul 2, 2011
 */
package com.app.lbs.common.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.app.lbs.common.dao.CommonDaoEx;
import com.app.platform.base.utils.AppContextHolder;
import com.app.platform.base.utils.StringUtil;
import com.app.platform.base.utils.SystemConfig;
import com.app.platform.base.utils.WebToolKits;

/**
 * common controller
 *
 * @author colen
 *
 */
public class WebCommonController extends WebToolKits {

   private static final Logger LOG = Logger.getLogger(WebCommonController.class);
    protected static final String MODULE_QUERY = "query";
    protected static final String MODULE_ADD = "add";
    protected static final String MODULE_MODIFY = "modify";
    protected static final String MODULE_MAIN = "main";

    
    protected static final String CK_K_TMNID = "tmnId";
    protected static final String CK_K_UID = "uid";
    protected static final String CK_K_CITYID = "ctid";
    protected static final String CK_K_UNAME = "uname";
    protected static final String CK_K_ROLE = "role";
    protected static final String CK_K_ORG = "org";
    
    
    /**
     * Create Model and view (Open Page)
     *
     * @param req HttpServletRequest
     * @return mv
     */
    protected ModelAndView createOpenPageMV(HttpServletRequest req) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("menuId", req.getParameter("menuId"));
        return mv;
    }

    /**
     * Get Common DAO
     *
     * @return
     */
    protected CommonDaoEx getCommonDao() {
        return (CommonDaoEx) AppContextHolder.getBean("common_commonDaoEx");
    }
   protected Map<String, Object> getMapCnf(String cityId, String mapType)
   {
    Map<String, Object> mapCnf = new HashMap<String, Object>();
   if (StringUtil.isTrimEmpty(mapType)) {
      if (Integer.valueOf(cityId).intValue() > 1000) {
        mapCnf.put("type", "google-overseas");
        mapCnf.put("key4locate", SystemConfig.getValue("map.google.key"));
        mapCnf.put("engine", "google");
      } else {
        mapCnf.put("type", "baidu");
        mapCnf.put("key4locate", SystemConfig.getValue("map.baidu.key"));
       mapCnf.put("engine", "baidu");
       }
    } else if ("baidu".equals(mapType)) {
       mapCnf.put("type", "baidu");
       mapCnf.put("key4locate", SystemConfig.getValue("map.baidu.key"));
       mapCnf.put("engine", "baidu");
    } else if ("google".equals(mapType)) {
       if (Integer.valueOf(cityId).intValue() > 1000) {
        mapCnf.put("type", "google-overseas");
      } else {
        mapCnf.put("type", "google-china");
      }
       mapCnf.put("key4locate", SystemConfig.getValue("map.google.key"));
       mapCnf.put("engine", "google");
     } else {
      LOG.error("Unkonwn mapType: " + mapType);
    }
    
   return mapCnf;
  }

  public Map<String, Object> getCityInfoById(String cityId)
  {
   String myCityId = cityId;
    if (StringUtil.isTrimEmpty(myCityId))
    {
       myCityId = "073";
    }

    Map<String, Object> args = new HashMap<String, Object>();
    args.put("cityId", myCityId);
   
    Map<String, Object> city = getCommonDao().namingQueryForMap("sql_portal_cityInfo", args);
   

    return city;
  }
 }
