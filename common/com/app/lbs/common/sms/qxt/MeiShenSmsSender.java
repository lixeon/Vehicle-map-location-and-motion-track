/*
 * lbspt
 */
package com.app.lbs.common.sms.qxt;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.app.lbs.common.sms.SmsReqMessage;
import com.app.lbs.common.sms.SmsRespMessage;
import com.app.platform.base.utils.StringUtil;
import com.app.platform.base.utils.SystemConfig;

/**
 * MeiShen SMS Sender
 *
 * @author colen
 *
 */
public class MeiShenSmsSender extends AbstractSmsSender {
    
    /** logger */
    private static final Logger LOG = Logger.getLogger(MeiShenSmsSender.class);

    private static final String KEY_USERNAME = "sms.meishen.username";
    private static final String KEY_PASSWORD = "sms.meishen.password";
    private static final String KEY_URL = "sms.meishen.url";
    private static final String KEY_CHECK_URL = "sms.lowfee.check.url";
    private static final String KEY_ENCODE = "sms.meishen.encode";
    private static final String KEY_EXTCODE = "sms.meishen.extcode";
    private static final String KEY_ENABLE = "sms.meishen.enable";

    
    @Override
    public String checkNet() {
        Map<String, String> args = new HashMap<String, String>();
        args.put("username", SystemConfig.get(KEY_USERNAME));
        args.put("scode", SystemConfig.get(KEY_PASSWORD));
        return this.executeSend(SystemConfig.get(KEY_CHECK_URL), args, SystemConfig.get(KEY_ENCODE));
    }

    
    @Override
    public Map<String, String> packetReqMessage(SmsReqMessage smsMsg) {

        Map<String, String> args = new HashMap<String, String>();
        args.put("username", SystemConfig.get(KEY_USERNAME));
        args.put("scode", SystemConfig.get(KEY_PASSWORD));
        args.put("extcode", SystemConfig.get(KEY_EXTCODE));
        args.put("encode", SystemConfig.get(KEY_ENCODE));

        args.put("msgid", smsMsg.getMsgId());
        
        if (!StringUtil.isTrimEmpty(smsMsg.getMsgType())) {
            args.put("msgtype", smsMsg.getMsgType());
        }

        args.put("mobile", smsMsg.getTo());
        args.put("content", smsMsg.getContent());
        
        if (LOG.isInfoEnabled()) {
            LOG.info("Send SMS: " + smsMsg.getContent());
        }
        
        // send time
        if (!StringUtil.isTrimEmpty(smsMsg.getSendTime())) {
            args.put("sendtime", smsMsg.getSendTime());
        }
        
        return args;
    }


    @Override
    public SmsRespMessage parseRespText(String respText) {
        SmsRespMessage smsResp = new SmsRespMessage();
        if (!StringUtil.isTrimEmpty(respText)) {
            if (respText.startsWith("0")) {
                String[] arr = respText.trim().split("#");
                smsResp.setRespCode(SEND_SUCCESS);
                smsResp.setFeeCount(Integer.valueOf(arr[1]));
                smsResp.setNumCount(Integer.valueOf(arr[2]));
            } else {
                LOG.error("send sms error (width error code) : " + respText);
                smsResp.setRespCode(ERROR_902);
            }
        } else {
            LOG.error("send sms error: " + respText);
            smsResp.setRespCode(ERROR_903);
        }

        return smsResp;
    }


    @Override
    public boolean isEnable() {
        return Boolean.valueOf(SystemConfig.get(KEY_ENABLE));
    }

    @Override
    public String getEncode() {
        return SystemConfig.get(KEY_ENCODE);
    }

    @Override
    public String getReqUrl() {
        return SystemConfig.get(KEY_URL);
    }

}
