package com.app.platform.base.dao;

import java.util.List;
import java.util.Map;

/**
 * Page
 *
 * @author colen
 *
 */
public class Page {

    
    /**
     * total Count
     */
    private int totalCount = 0;
    
    /**
     * result list
     */
    private List<Map<String, Object>> resultList = null;
    

    /**
     * getter
     * @return the totalCount
     */
    public int getTotalCount() {
        return totalCount;
    }
    /**
     * setter
     * @param totalCount the totalCount to set
     */
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    /**
     * getter
     * @return the resultList
     */
    public List<Map<String, Object>> getResultList() {
        return resultList;
    }
    /**
     * setter
     * @param resultList the resultList to set
     */
    public void setResultList(List<Map<String, Object>> resultList) {
        this.resultList = resultList;
    }
}
