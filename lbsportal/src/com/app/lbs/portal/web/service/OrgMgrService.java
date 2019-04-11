/*
 * LBSAPP
 */
package com.app.lbs.portal.web.service;

import java.util.Map;

import com.app.lbs.common.web.service.WebCommonService;

/**
 * Organization Manager Service
 *
 * @author colen
 *
 */
public interface OrgMgrService extends WebCommonService {

    /**
     * query company list
     *
     * @param params Parameters
     * @return
     */
    Map<String, Object> queryCompanyList(Map<String, Object> params);

    
    /**
     * get next OrgID
     *
     * @param parentOrgId Parent Org ID
     * @return
     */
    String getNextOrgId(String parentOrgId);


    /**
     * Add Company
     *
     * @param params Parameters
     */
    void addCompany(Map<String, Object> params);


    /**
     * get company by id.
     *
     * @param params Parameters
     * @return
     */
    Map<String, Object> getCompanyById(Map<String, Object> params);


    /**
     * Modify Company
     *
     * @param params Parameters
     */
    void modifyCompany(Map<String, Object> params);


    /**
     * delete company
     *
     * @param params Parameters
     */
    void deleteCompany(Map<String, Object> params);

    
    /**
     * query group info list
     *
     * @param params Parameters
     * @return
     */
    Map<String, Object> queryGroupList(Map<String, Object> params);


    /**
     * Add a Group
     *
     * @param params
     */
    void addGroup(Map<String, Object> params);

    
    /**
     * modify group
     *
     * @param params
     */
    void modifyGroup(Map<String, Object> params);


    /**
     * query bind terminal list
     *
     * @param params
     */
    void queryBindedTerminalList(Map<String, Object> params);


    /**
     * bind terminal
     *
     * @param params
     */
    void bindTerminal(Map<String, Object> params);


    /**
     * delete group
     *
     * @param params
     */
    void deleteGroup(Map<String, Object> params);

}
