/*
 * Copyleft 2012 Power by colen.
 *
 * Project: app-crm
 * Date: Apr 22, 2012
 */
package com.app.platform.base.dao;

import java.util.List;
import java.util.Map;

/**
 * Naming-SQL DAO Support
 *
 * @author colen
 *
 */
public interface NamingDaoSupport {


    /**
     * query for list by naming-sql
     *
     * @param sqlId SQL ID
     * @param args Arguments
     * @return List
     */
    List<Map<String, Object>> namingQueryForList(String sqlId, Map<String, Object> args);

    /**
     * Update Table by naming-sql
     *
     * @param sqlId SQL ID
     * @param args Arguments
     * @return update rows
     */
    int namingUpdate(String sqlId, Map<String, Object> args);
    
    /**
     * Update Table by naming-sql
     *
     * @param sqlId SQL ID
     * @param args Arguments
     * @param affectNull true: the key's value is null will affect to DB, false NOT.
     * @return update rows
     */
    int namingUpdate(String sqlId, Map<String, Object> args, boolean affectNull);
    
    /**
     * query for Map by naming-sql
     *
     * @param sqlId SQL ID
     * @param args arguments
     * @return Map (if get a list result by the sql, only the first one will be retrun.)
     */
    Map<String, Object> namingQueryForMap(String sqlId, Map<String, Object> args);
    
    
    /**
     * Query For Page
     *
     * @param sqlId SQL ID
     * @param cond query parameters
     * @return Page
     */
    Page namingQueryForPage(String sqlId, PageCond cond);
}
