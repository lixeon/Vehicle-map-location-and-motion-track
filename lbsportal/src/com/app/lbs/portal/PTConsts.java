/*
 * lbsgw
 */
package com.app.lbs.portal;


/**
 * Portal Constant
 *
 * @author colen
 *
 */
public class PTConsts {

    /** processor: sync config */
    public static final String PROCESSOR_SYNC_CONFIG = "sync-config-processor";
    
    
    
    /** Response Code : success */
    public static String RESPCD_SUCCESS = "S000";

    /** Response Code : 手机号码已经注册  */
    public static String RESPCD_E001 = "E001";
    /** Response Code : 验证码不正确  */
    public static String RESPCD_E002 = "E002";
    /** Response Code : 旧密码不正确 */
    public static String RESPCD_E003 = "E003";
    /** Response Code : 用户名或密码不正确 */
    public static String RESPCD_E004 = "E004";
    /** Response Code : 手机号和终端ID鉴权不通过 */
    public static String RESPCD_E005 = "E005";
    /** Response Code : 终端不在线或终端数据通道断开 */
    public static String RESPCD_E006 = "E006";
    
    /** Response Code : 终端号不正确 */
    public static String RESPCD_E010 = "E010";


    /** Response Code : 平台处理错误。该错误可能是由客户端参数错误导致的，也可能是平台内部错误导致的 */
    public static String RESPCD_E901 = "E901";
    /** Response Code : 平台处理繁忙 */
    public static String RESPCD_E902 = "E902";
    /** Response Code : 找不到指定的请求动作 */
    public static String RESPCD_E903 = "E903";
    /** Response Code : Token过期，或请求没有Token */
    public static String RESPCD_E904 = "E904";
    /** Response Code : 请求参数不正确 */
    public static String RESPCD_E905 = "E905";
    /** Response Code : 主帐号不正确 */
    public static String RESPCD_E906 = "E906";    
    
}
