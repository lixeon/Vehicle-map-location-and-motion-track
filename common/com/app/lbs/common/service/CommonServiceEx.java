/*
 * Copyleft 2012 Power by colen.
 *
 * Project: app-crm
 * Date: May 12, 2012
 */
package com.app.lbs.common.service;

import com.app.lbs.common.dao.CommonDaoEx;

/**
 * Common Service Extend
 *
 * @author colen
 *
 */
public class CommonServiceEx {

    /** Common DAO */
    private CommonDaoEx commonDao = null;

    /**
     * @return the commonDao
     */
    public CommonDaoEx getCommonDao() {
        return commonDao;
    }

    /**
     * @param commonDao the commonDao to set
     */
    public void setCommonDao(CommonDaoEx commonDao) {
        this.commonDao = commonDao;
    }
}
