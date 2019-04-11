/*
 * Copyleft 2012 Power by colen.
 *
 * Project: app-crm
 * Date: Apr 22, 2012
 */
package com.app.platform.base.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.app.platform.base.exception.BaseSystemException;

/**
 * Naming SQL
 *
 * @author colen
 *
 */
public class NamingSql {

    /** part type: dynamic */
    private static final int PART_TYPE_DYNA = 2;
    /** fill type: must */
    private static final int PART_TYPE_MUST = 1;

    /** raw sql text */
    private String rawSql = null;
    private boolean initialized = false;
    /** sql parts */
    private List<SqlPart> parts = null;

    /**
     * Constructor
     *
     * @param sqltext
     */
    public NamingSql(String sqltext) {
        this.initialize(sqltext);
    }

    /**
     * Initialize
     *
     */
    private void initialize(String sql) {
        this.rawSql = sql;
        this.parts = new ArrayList<NamingSql.SqlPart>();

        String sqlText = this.rawSql.trim().replaceAll("\\s{2,}", " ");
        String[] sqlParts = sqlText.split("<\\[");
        if (sqlParts.length == 1) {
            addMust(this.parts, sqlParts[0], 1);
        } else {
            int idx = 1;
            for (String sqlPart : sqlParts) {
                if (idx == 1) {
                    addMust(this.parts, sqlPart.trim(), idx);
                } else {
                    idx = parseComplex(this.parts, sqlPart.trim(), idx);
                }
                idx++;
            }
        }
        
        this.initialized = true;
    }


    private int parseComplex(List<SqlPart> partList, String sqlPart, int idx) {
        int nIdx = idx;
        if (sqlPart.endsWith("]>")) {
            String tSqlPart = sqlPart.replace("]>", "");
            String key = findKey(tSqlPart);
            addDyna(partList, tSqlPart, key, nIdx);
        } else {
            String[] subParts = sqlPart.split("]>");
            String key = findKey(subParts[0]);
            addDyna(partList, subParts[0], key, nIdx);
            nIdx++;
            addMust(partList, subParts[1], nIdx);
        }
        
        return nIdx;
    }

    private String findKey(String content) {
        Pattern p = Pattern.compile(":[$#a-zA-Z]\\w+");
        Matcher m = p.matcher(content);
        String key = null;
        while (m.find()) {
            String nKey = m.group().replaceAll(":", "");
            if (key == null) {
                key = nKey;
                continue;
            }
            if (!nKey.equals(key)) {
                throw new BaseSystemException("Can not set two different Dynamic Parameters in a same line.");
            }
        }
        return key;
    }

    private void addMust(List<SqlPart> partList, String sqlPart, int idx) {
        SqlPart part = new SqlPart();
        partList.add(part);
        part.content = sqlPart;
        part.index = idx;
        part.type = PART_TYPE_MUST;
    }

    private void addDyna(List<SqlPart> partList, String sqlPart, String key, int idx) {
        SqlPart part = new SqlPart();
        partList.add(part);
        part.key = key;
        part.content = sqlPart;
        part.index = idx;
        part.type = PART_TYPE_DYNA;
    }

    /**
     * Sql Part
     *
     * @author colen
     *
     */
    private class SqlPart implements Comparable<SqlPart> {
        String key = null;
        int index = 0;
        String content = "";
        int type = -1;

        @Override
        public int compareTo(SqlPart dc) {
            return this.index - dc.index;
        }
    }

    private void checkNotInit() {
        if (!this.initialized) {
            throw new BaseSystemException("SQL Not initialized successfully! (" + this.rawSql + ")");
        }
    }

    /**
     * get binded SQL
     *
     * @param params Parameters
     * @return binded SQL
     */
    public String getBindedSql(Map<String, Object> params) {
        // check not initialized
        checkNotInit();

        StringBuilder bud = new StringBuilder();

        for (SqlPart part : this.parts) {
            if (part.type == PART_TYPE_MUST) {
                bud.append(part.content);
            } else {
                if (params.containsKey(part.key)) {
                    bud.append(part.content);
                }
            }
            bud.append(" ");
        }
        
        return bud.toString();
    }

}
