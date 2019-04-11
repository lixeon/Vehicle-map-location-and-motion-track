/*
 * LBSAPP
 */
package com.app.lbs.common.util;

import java.math.BigDecimal;

import com.app.platform.base.utils.StringUtil;

/**
 * LBS Business Utils
 *
 * @author colen
 *
 */
public class LbsBizUtils {

    private static final String TP_LOCATION_LOG = "TP_LOCATION_LOG";

    /**
     * Get TP_LOCATION_LOGXX table
     *
     * @param tmnId terminal id
     * @return
     */
    public static String getTPLLTableName(String tmnId) {
        return TP_LOCATION_LOG.concat(tmnId.trim().substring(tmnId.length() - 2));
    }
    
    
    /**
     * Calculate Speed
     *
     * @param rawSpeed Raw Speed Data from GPS
     * @return
     */
     public static String calSpeed(String rawSpeed) {
        String speed = "0.0";
        if (!StringUtil.isTrimEmpty(rawSpeed)) {
            double speedd = Double.valueOf(rawSpeed) * 1.852;
            speed = new BigDecimal(speedd).setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
        }
        
        return speed;
    }
    
    /**
     * Calculate hex string to time
     *
     * @param hex string  0x0F 0x01 0x01 0x01 0x01 0x01  15年1月1日1时1分1秒
     * @return
     */
     //yyyy-MM-dd HH:mm:ss    0x0F 0x01 0x01 0x01 0x01 0x01
    public static String bjbdHexString2time(String hexstring) {
        String hexbytestr = "00";
        String timestr;
        hexbytestr=hexstring.substring(0, 2);
        timestr="20"+Integer.parseInt(hexbytestr, 16);
        hexbytestr=hexstring.substring(2, 4);
        timestr=timestr+"-"+Integer.parseInt(hexbytestr, 16);
        hexbytestr=hexstring.substring(4, 6);
        timestr=timestr+"-"+Integer.parseInt(hexbytestr, 16)+" ";
        hexbytestr=hexstring.substring(6, 8);
        timestr=timestr+Integer.parseInt(hexbytestr, 16)+":";
        hexbytestr=hexstring.substring(8, 10);
        timestr=timestr+Integer.parseInt(hexbytestr, 16)+":";
        hexbytestr=hexstring.substring(10, 12);
        timestr=timestr+Integer.parseInt(hexbytestr, 16);
        return timestr;
    }

    /**
     * Get Table Index
     *
     * @param tmnId
     * @return
     */
    public static int getTableIdx(String tmnId) {
        return Integer.valueOf(tmnId.substring(tmnId.length() - 2));
    }
}
