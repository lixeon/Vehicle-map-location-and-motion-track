/*
 * Copyleft 2012 Power by colen.
 *
 * Project: app-crm
 * Date: Mar 17, 2012
 */
package com.app.lbs.common.service.pub;

import java.util.List;
import java.util.Map;

import com.app.lbs.common.CodeConst;


/**
 * Code Data Cache Service
 *
 * @author Coeln
 */
public interface CodeDataService extends CacheService
{
    String CDK_PROVINCE = CodeConst.CDK_PROVINCE;
    String CDK_CITY = CodeConst.CDK_CITY;
    String CDK_TTYPE = CodeConst.CDK_TTYPE;
    String CDK_PROVIDER = CodeConst.CDK_PROVIDER;

    /**
     * get code list by code key
     *
     * @param codeKey code key
     * @return List<Map<String,Object>>
     */
    List<Map<String, Object>> getCodeList(String codeKey);

    /**
     * get code name by value (full name)
     *
     * @param codeKey code key
     * @param value code value
     * @return String
     */
    String getNmByValue(String codeKey, String value);

    /**
     * get code name by value (ABBR name)
     *
     * @param codeKey code key
     * @param value code value
     * @return String
     */
    String getAbbrNmByValue(String codeKey, String value);

    /**
     * Get Org Name
     *
     * @param orgId Org ID
     * @return Name
     */
    String getOrgName(String orgId);
    /**
     * Get Teminal type Name
     *
     * @param TerminalTypeId  Type ID
     * @return Name
     */
    String getTerminalTypeName(String TerminalTypeId) ;

}
