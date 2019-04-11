/*
 * Copyleft 2011 Power by colen.
 *
 * Project: booking
 * Date: Jul 2, 2011
 */
package com.app.platform.base.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.app.platform.base.exception.BaseSystemException;
import com.app.platform.base.utils.BeanUtil;
import com.app.platform.base.utils.ConvertUtil;

/**
 * Simple DAO Support
 * 
 * @author colen
 * 
 */
public class DaoSupportEx extends JdbcDaoSupport {

    /**
     * batch update
     *
     * @param sqls SQLS
     * @return
     */
    public int[] batchUpdate(String[] sqls) {
        return getJdbcTemplate().batchUpdate(sqls);
    }


    /**
     * Query For Map
     *
     * @param sql SQL
     * @param args Arguments
     * @return Map
     */
    public Map<String, Object> queryForMap(String sql, Object... args) {
        Map<String, Object> m = getJdbcTemplate().queryForMap(sql, args);
        ConvertUtil.lowerMapkeys(m);
        return m;
    }

    /**
     * Query for Long.
     * 
     * @param sql SQL
     * @param args Arguments
     * @return long value.
     */
    public long queryForLong(String sql, Object... args) {
        return getJdbcTemplate().queryForObject(sql, Long.class, args);
    }

    /**
     * Query For Object.
     *
     * @param sql SQL
     * @param type Class
     * @param args Arguments
     * @return Object.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Object queryForObject(String sql, Class type, Object... args) {
        return getJdbcTemplate().queryForObject(sql, type, args);
    }

    /**
     * Query For Integer
     * 
     * @param sql SQL
     * @param args Arguments
     * @return integer.
     */
    public int queryForInt(String sql, Object... args) {
        return getJdbcTemplate().queryForObject(sql, Integer.class, args);
    }
    
    
    /**
     * Query For Integer
     *
     * @param sql SQL
     * @param args Arguments
     * @return integer.
     */
    public int queryForInt(String sql, Map<String, Object> qps) {
        return getNamedJdbcTemplate().queryForObject(sql, qps, Integer.class);
    }
    

    /**
     * Update Table
     * 
     * @param sql SQL
     * @param args Arguments
     * @return update rows
     */
    public int update(String sql, Object... args) {
        return getJdbcTemplate().update(sql, args);
    }

    /**
     * Update Table (Named)
     * 
     * @param sql SQL
     * @param args Arguments
     * @return update rows
     */
    public int update(String sql, Map<String, Object> args) {
        return getNamedJdbcTemplate().update(sql, args);
    }

    /*    
    *//**
     * ReCount Page Number.
     * 
     * @param totalRows Total Rows
     * @param curPage Current Page.
     * @return Page Number.
     */
    /*
     * private int reCountPageNo(int totalRows, String curPage) { int pageNum =
     * 1; if (!StringUtil.isEmpty(curPage)) { pageNum =
     * Integer.parseInt(curPage); }
     * 
     * // Max Page No by total rows. int tPageNum = 0; if (totalRows %
     * SysConst.PER_PAGE_SIZE == 0) { tPageNum = totalRows /
     * SysConst.PER_PAGE_SIZE; } else { tPageNum = totalRows /
     * SysConst.PER_PAGE_SIZE + 1; }
     * 
     * // OUT return (pageNum < tPageNum) ? pageNum : tPageNum; }
     *//**
     * Query For Page
     * 
     * @param sql SQL
     * @param type Class
     * @param curPage Current Page.
     * @param args Arguments
     * @return Page
     */
    /*
     * @SuppressWarnings("rawtypes") public Page queryForPage(String sql, Class
     * type, String curPage, Object... args) { // do Query String finalSql =
     * StringUtil.cnvtConutSql(sql); int totalRows = queryForInt(finalSql,
     * args); if (totalRows == 0) { return Page.getDefault(); }
     * 
     * // Get Page Number. int pageNum = reCountPageNo(totalRows, curPage);
     * 
     * finalSql = StringUtil.cnvtPageSql(sql, pageNum, SysConst.PER_PAGE_SIZE);
     * // Execute Query List rList = queryForList(finalSql, type, args);
     * 
     * // OUT >>> return new Page(rList, totalRows, pageNum); }
     */

    /**
     * Query for List
     * 
     * @param sql SQL
     * @param args Arguments
     * @return List
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<Map<String, Object>> queryForList(String sql, Object... args) {
        // Execute Query
        List l = getJdbcTemplate().queryForList(sql, args);
        ConvertUtil.lowerListMapKeys(l);
        // OUT >>>
        return l;
    }

    /**
     * Query for List (Named)
     *
     * @param sql SQL
     * @param args Arguments
     * @return List
     */
    public List<Map<String, Object>> queryForList(String sql, Map<String, Object> args) {
        List<Map<String, Object>> l = getNamedJdbcTemplate().queryForList(sql, args);
        ConvertUtil.lowerListMapKeys(l);
        // OUT >>>
        return l;
    }

    /**
     * Convert List
     * 
     * @param rList Result List
     * @param clazz Class
     * @return List
     */
    @SuppressWarnings("unused")
    private List<Object> cnvList(List<Map<String, Object>> rList, Class<?> clazz) {
        // check arguments
        if (clazz == null) {
            throw new BaseSystemException("type can not be null!");
        }

        // Lower keys
        ConvertUtil.lowerListMapKeys(rList);
        List<Object> tList = new ArrayList<Object>();
        Object bean = null;
        for (Map<String, Object> o : rList) {
            bean = BeanUtil.newInstance(clazz);
            ConvertUtil.cnvMap2Bean(bean, o);
            tList.add(bean);
        }

        // Bean List OUT >>>
        return tList;
    }

    /** name parameter jdbc template */
    private NamedParameterJdbcOperations namedJdbcTemplate = null;

    /**
     * @return the namedJdbcTemplate
     */
    public NamedParameterJdbcOperations getNamedJdbcTemplate() {
        return namedJdbcTemplate;
    }

    /**
     * @param namedJdbcTemplate the namedJdbcTemplate to set
     */
    public void setNamedJdbcTemplate(NamedParameterJdbcOperations namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

}
