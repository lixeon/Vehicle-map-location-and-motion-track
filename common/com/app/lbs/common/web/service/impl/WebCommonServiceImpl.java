/*
 * Copyleft 2011 Power by colen.
 *
 * Project: booking
 * Date: Jul 2, 2011
 */
package com.app.lbs.common.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.app.lbs.common.service.CommonServiceEx;
import com.app.lbs.common.service.pub.CodeDataService;
import com.app.lbs.common.web.service.WebCommonService;
import com.app.platform.base.utils.StringUtil;


/**
 * Common Service
 *
 * @author colen
 *
 */
public class WebCommonServiceImpl extends CommonServiceEx implements WebCommonService {

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
    /**
     * get name by code value
     *
     * @param codeKey
     * @param value
     * @return
     */
    protected String getCdNmByValue(String codeKey, Object value) {
        if (value == null) {
            return null;
        }

        return this.codeDataService.getNmByValue(codeKey, value.toString());
    }
    
    /**
     * get name by code value (ABBR)
     *
     * @param codeKey
     * @param value
     * @return
     */
    protected String getCdAbbrNmByValue(String codeKey, String value) {
        if (value == null) {
            return null;
        }
        
        return this.codeDataService.getAbbrNmByValue(codeKey, value.toString());
    }
    
    /** Code Data Service */
    private CodeDataService codeDataService = null;

    /**
     * Getter
     * 
     * @return the codeDataService
     */
    public CodeDataService getCodeDataService() {
        return codeDataService;
    }

    /**
     * @param codeDataService
     */
    public void setCodeDataService(CodeDataService codeDataService) {
        this.codeDataService = codeDataService;
    }

}
