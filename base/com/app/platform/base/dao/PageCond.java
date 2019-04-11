package com.app.platform.base.dao;

import java.util.Map;

/**
 * Pagination Condition 
 *
 * @author colen
 *
 */
public class PageCond {

    /**
     * default max Page Number
     */
    public static final int DEFAULT_MAX_PAGE_NUM = 200;
    
    /**
     * Query Parameters
     */
    private Map<String, Object> qps = null;
    
    /**
     * Start row number
     */
    private int start = 0;
    
    /**
     * Page limit
     */
    private int limit = 20;

    /**
     * Constructor
     *
     * @param qps Query Parameters
     * @param start Start
     * @param limit Limit
     */
    public PageCond(Map<String, Object> qps, int start, int limit) {
        this.qps = qps;
        this.start = start;
        this.limit = limit;
    }

    /**
     * getter
     * @return the qps
     */
    public Map<String, Object> getQps() {
        return qps;
    }

    /**
     * setter
     * @param qps the qps to set
     */
    public void setQps(Map<String, Object> qps) {
        this.qps = qps;
    }

  public int getStart()
  {
    return this.start;
  }

  public void setStart(int start)
  {
    this.start = start;
  }

  public int getLimit()
  {
    return this.limit;
  }

  public void setLimit(int limit)
  {
    this.limit = limit;
  }
}
