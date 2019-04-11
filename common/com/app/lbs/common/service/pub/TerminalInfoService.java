/*
 * LBSAPP
 */
package com.app.lbs.common.service.pub;

import java.util.Map;

/**
 * Terminal Info Service
 *
 * @author luxinxian
 *
 */
public interface TerminalInfoService {

    /** sql for get latest trace */
    String SQL_GETLATESTTRACE = "select * from (select LOC_STATUS," +
    		"TMN_STATUS AS STATUS, " +
    		"GPS, " +
    		"RSSI, " +
    		"VBAT, " +
    		"SPEED, " +
    		"STEP, " +
    		"LNG, " +
    		"LAT, " +
    		"DATE_FORMAT(LOC_TIME, '%Y-%m-%d %T') as LOC_TIME, " +
    		"ACTION " +
    		"from #tableName# " +
    		"where TMN_ID = :tmnId " +
    		"and LOC_TIME >= :last_time " +
    		"order by LOC_TIME desc limit 5) t order by t.LOC_TIME";

    
    /**
     * Get Terminal Latest Trace
     *
     * @param qps Query Parameters
     * @return
     */
    Map<String, Object> getTerminalLatestTrace(Map<String, Object> qps);
    
    
    
    /** sql for get history locations */
    String SQL_GETHISLOCATIONS = "select LOC_STATUS, " +
    		"TMN_STATUS AS STATUS, " +
    		"GPS, " +
    		"RSSI, " +
    		"VBAT, " +
    		"SPEED, " +
    		"STEP, " +
    		"LNG, " +
    		"LAT, " +
    		"ACTION, " +    		
    		"DATE_FORMAT(LOC_TIME, '%Y-%m-%d %T') as LOC_TIME " +
    		"from #tableName# " +
    		"where TMN_ID = :tmnId " +
    		"and LOC_TIME >:st " +
    		"and LOC_TIME <=:et " +
    		"order by LOC_TIME limit :maxCnt";


    /**
     * Get Terminal History Trace
     *
     * @param qps Query Parameters
     * @return
     */
    Map<String, Object> getTerminalHistoryTrace(Map<String, Object> qps);

    
    /** sql for get latest location */
    String SQL_GETLATEST_P = "select LOC_STATUS, " +
    		"TMN_STATUS AS STATUS, " +
    		"GPS, " +
    		"RSSI, " +
    		"VBAT, " +
    		"SPEED, " +
    		"STEP, " +
    		"LNG, " +
    		"LAT, " +
    		"DATE_FORMAT(LOC_TIME, '%Y-%m-%d %T') as LOC_TIME, " +
    		"DATE_FORMAT(REC_TIME, '%Y-%m-%d %T') as REC_TIME, " +
    		"ACTION from #tableName# " +
    		"where TMN_ID = :tmnId " +
    		/*"and REC_TIME >:startTime " +*/
    		"order by LOC_TIME desc limit 1";

    /**
     * Get Terminal Latest Location.
     *
     * @param qps
     * @return
     */
    Map<String, Object> getTerminalLatestLocation(Map<String, Object> qps);

}
