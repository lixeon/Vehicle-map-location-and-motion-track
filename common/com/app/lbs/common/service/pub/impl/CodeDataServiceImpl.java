/*
 * Copyleft 2012 Power by colen.
 *
 * Project: app-crm
 * Date: Mar 17, 2012
 */
package com.app.lbs.common.service.pub.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.app.lbs.common.service.CommonServiceEx;
import com.app.lbs.common.service.pub.CodeDataService;
import com.app.platform.base.utils.BeanUtil;
import com.app.platform.base.utils.CommonUtil;

/**
 * Code Data Cache Service (Can not refresh)
 *
 * @author colen
 */
public class CodeDataServiceImpl extends CommonServiceEx implements CodeDataService
{
    
    /** logger */
    private static final Logger LOG = Logger.getLogger(CodeDataServiceImpl.class);

    
    /** key split */
    private static final String KS = "-";
    
    /** Map Cache */
    private Map<String, List<Map<String, Object>>> cache = null;

    /** fast cache */
    private Map<String, Map<String, Object>> fastCache = null; 

    /** org cache */
    private volatile Map<String, Map<String, Object>> orgCache = null;

    /**
     * initialize data from database
     *
     */
    public void initialize() {
        this.cache = new HashMap<String, List<Map<String,Object>>>();
        this.fastCache = new HashMap<String, Map<String,Object>>();

        // load code data
        List<Map<String, Object>> list = getCommonDao().namingQueryForList("sql_common_codedata_queryAll", null);

        String codeId = null;
        List<Map<String, Object>> codeList = null;
        for (Map<String, Object> code : list) {
            codeId = (String) code.get("code_id");
            codeList = this.cache.get(codeId);
            if (codeList == null) {
                codeList = new ArrayList<>();
                this.cache.put(codeId, codeList);
            }
            codeList.add(code);
            this.fastCache.put(codeId + KS + code.get("value"), code);
        }

        // load province and city
        initProviceAndCityData();
        
        // load org info.
        initOrgData();
        initTerminalTypeData();
        initProviderData();
        // optimize the code below
        Thread refresher = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // delay
                    Thread.sleep(1000 * 60 * 5);
                } catch (InterruptedException e) {
                    // ignore.
                }

                while (true) {
                    try {
                        refresh(null);
                    } catch (Exception e) {
                        LOG.error(e);
                    } finally {
                        try {
                            Thread.sleep(1000 * 60 * 5);
                        } catch (InterruptedException e) {
                            // ignore.
                        }
                    }
                }
            }
        });
        refresher.setDaemon(true);
        refresher.start();
    }


    private void initProviceAndCityData() {
        List<Map<String, Object>> list;
        List<Map<String, Object>> codeList;
        list = getCommonDao().namingQueryForList("sql_common_queryProvinceAndCity", null);
        List<Map<String, Object>> provinceList = new ArrayList<>();
        this.cache.put(CDK_PROVINCE, provinceList);
        Map<String, String> tmpMap = new HashMap<>();

        int pidx = 1;
        int cidx = 1;
        String provinceKey = null;
        Map<String, Object> province = null;
        Map<String, Object> ncity = null;
        for (Map<String, Object> city : list) {
            provinceKey = (String) city.get("province_id");
            if (tmpMap.containsKey(provinceKey)) {
                codeList = this.cache.get(provinceKey + "." + CDK_CITY);
                ncity = new HashMap<>();
                codeList.add(ncity);
                ncity.put("code_id", provinceKey + "." + CDK_CITY);
                ncity.put("value", city.get("city_id"));
                ncity.put("full_name", city.get("city_name"));
                ncity.put("abbr_name", city.get("city_name"));
                ncity.put("sort_no", cidx);
                
                // hold in fast cache
                this.fastCache.put(CDK_CITY + KS + city.get("city_id"), ncity);
                
                cidx++;
            } else {
                tmpMap.put(provinceKey, "");
                province = new HashMap<>();
                provinceList.add(province);
                province.put("code_id", CDK_PROVINCE);
                province.put("value", provinceKey);
                province.put("full_name", city.get("province_name"));
                province.put("abbr_name", city.get("province_name"));
                province.put("sort_no", pidx);
                
                // hold in fast cache
                this.fastCache.put(CDK_PROVINCE + KS + provinceKey, province);
                
                pidx++;

                cidx = 1;
                codeList = new ArrayList<>();
                this.cache.put(provinceKey + "." + CDK_CITY, codeList);
                ncity = new HashMap<>();
                codeList.add(ncity);
                ncity.put("code_id", provinceKey + "." + CDK_CITY);
                ncity.put("value", city.get("city_id"));
                ncity.put("full_name", city.get("city_name"));
                ncity.put("abbr_name", city.get("city_name"));
                ncity.put("sort_no", cidx);
                
                // hold in fast cache
                this.fastCache.put(CDK_CITY + KS + city.get("city_id"), ncity);

                cidx++;
            }
        }
    }


    private void initOrgData() {
        this.orgCache = new HashMap<>();
        
        Map<String, Map<String, Object>> tmpCache = loadOrgData();
        
        // hold
        if (tmpCache != null) {
            this.orgCache.putAll(tmpCache);
        }
    }
    
    private void initTerminalTypeData() {
        int cidx = 1;
        List<Map<String, Object>> codeList;
        Map<String, Object> ntype = null;
        List<Map<String, Object>> TerminalTypeList = getCommonDao().queryForList("select * from T_BSF_TETYPE");
       	codeList = new ArrayList<>();  
    	this.cache.put(CDK_TTYPE, codeList);
        for (Map<String, Object> type : TerminalTypeList) {
        	ntype = new HashMap<>();
        	codeList.add(ntype);
        	ntype.put("code_id",CDK_TTYPE);
        	ntype.put("value", type.get("type_id"));
        	ntype.put("full_name", type.get("type_name"));
        	ntype.put("abbr_name", type.get("type_name"));
        	ntype.put("sort_no", cidx);
        	cidx++;
        }
    }

    private void initProviderData() {
        int cidx = 1;
        List<Map<String, Object>> codeList;
        Map<String, Object> ntype = null;
        List<Map<String, Object>> TerminalTypeList = getCommonDao().queryForList("select * from T_BSF_PROVIDER");
       	codeList = new ArrayList<>();  
    	this.cache.put(CDK_PROVIDER, codeList);
        for (Map<String, Object> type : TerminalTypeList) {
        	ntype = new HashMap<>();
        	codeList.add(ntype);
        	ntype.put("code_id",CDK_PROVIDER);
        	ntype.put("value", type.get("provider_id"));
        	ntype.put("full_name", type.get("provider_name"));
        	ntype.put("abbr_name", type.get("provider_name"));
        	ntype.put("sort_no", cidx);
        	cidx++;
        }
    }

    private Map<String, Map<String, Object>> loadOrgData() {
        List<Map<String, Object>> orgList = getCommonDao().queryForList("select * from T_BSF_ORG");
        if (CommonUtil.isEmpty(orgList)) {
            return null;
        }
        
        Map<String, Map<String, Object>> tmpCache = new HashMap<>();
        for (Map<String, Object> org : orgList) {
            tmpCache.put(org.get("org_id").toString(), org);
        }
        return tmpCache;
    }
 
    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> getCodeList(String codeKey) {
        List<Map<String, Object>> list = getList(codeKey);
        return (List<Map<String, Object>>) BeanUtil.cloneBean(list);
    }

    private List<Map<String, Object>> getList(String codeKey) {
        List<Map<String, Object>> list = this.cache.get(codeKey);
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }

    /**
     * get code name by value (full name)
     *
     * @param codeKey code key
     * @param value code value
     * @return String
     */
    public String getNmByValue(String codeKey, String value) {
        return getName(codeKey, value, "full_name");
    }
    
    /**
     * get code name by value (ABBR Name)
     *
     * @param codeKey code key
     * @param value code value
     * @return String
     */
    public String getAbbrNmByValue(String codeKey, String value) {
        return getName(codeKey, value, "abbr_name");
    }
    
    private String getName(String codeKey, String value, String pKey) {
        Map<String, Object> code = this.fastCache.get(codeKey + KS + value);
        if (code == null) {
            return null;
        } else {
            return (String) code.get(pKey);
        }
    }


    @Override
    public void refresh(Map<String, Object> args) {
        Map<String, Map<String, Object>> tmpCache = loadOrgData();
        // hold
        if (tmpCache != null) {
            this.orgCache = tmpCache;
        }
    }


    @Override
    public String getOrgName(String orgId) {
        Map<String, Object> org = this.orgCache.get(orgId);
        String orgName = "";
        if (org != null) {
            orgName = org.get("org_name").toString();
        }
        return orgName;
    }
    @Override
    public String getTerminalTypeName(String TerminalTypeId) {
    	List<Map<String, Object>> list=this.cache.get(CDK_TTYPE);
        String Name = "";
        for (Map<String, Object> type : list)
        {
        	if(type.get("type_id").equals(TerminalTypeId))
        		Name = type.get("type_name").toString();
        }
        return Name;
    }    
}
