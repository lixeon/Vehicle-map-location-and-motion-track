/*
 * LBSAPP
 */
package com.app.lbs.common.process;

import org.apache.log4j.Logger;

import com.app.lbs.common.sms.SmsReqMessage;
import com.app.lbs.common.sms.SmsSender;
import com.app.platform.base.utils.AppContextHolder;
import com.app.platform.base.utils.SystemConfig;

/**
 * SMS Send Task
 *
 * @author luxinxian
 *
 */
public class SMSSendTask extends ProcessTask {

    /** logger */
    private static final Logger LOG = Logger.getLogger(SMSSendTask.class);
    
    private SmsReqMessage smsReqMsg = null;

    /**
     * Default Constructor
     *
     */
    public SMSSendTask() {
        this.smsReqMsg = new SmsReqMessage();
    }


    /**
     * Constructor with target and content.
     *
     * @param to
     * @param content
     */
    public SMSSendTask(String to, String content) {
        this();
        this.smsReqMsg.setMsgId(String.valueOf(System.currentTimeMillis()));
        this.smsReqMsg.setTo(to);
        this.smsReqMsg.setContent(content);
    }

    @Override
    public void process() {
        try {
            SmsSender sender = (SmsSender)
                    AppContextHolder.getBean(SystemConfig.get("sms.default.sender"));
            // send message
            sender.send(this.smsReqMsg);
        } catch (Exception e) {
            LOG.error("Send Message Error!", e);
        }
    }

}
