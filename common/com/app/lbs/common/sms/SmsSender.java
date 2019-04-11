/*
 * lbspt
 */
package com.app.lbs.common.sms;


/**
 * SMS API
 *
 * @author colen
 *
 */
public interface SmsSender {

    /** send success */
    String SEND_SUCCESS = "0";

    /** error 900: SMS Request Format Error. */
    String ERROR_900 = "900";

    /** error 901: SMS Channel Not enable. */
    String ERROR_901 = "901";

    /** error 902: Platform Response with Error Code. */
    String ERROR_902 = "902";
    
    /** error 903: SMS Platform Response Error. */
    String ERROR_903 = "903";
    
    /**
     * Send SMS
     *
     * @param smsReqMsg SmsMessage
     * @return result SmsRespMessage
     */
    SmsRespMessage send(SmsReqMessage smsReqMsg);

    
    /**
     * Check Net
     *
     * @return net
     */
    String checkNet();
    
    
    /**
     * is send enable
     *
     * @return
     */
    boolean isEnable();
}
