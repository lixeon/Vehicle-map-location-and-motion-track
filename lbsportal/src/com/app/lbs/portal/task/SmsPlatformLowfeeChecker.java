/*
 * LBSAPP
 */
package com.app.lbs.portal.task;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import com.app.lbs.common.dao.CommonDaoEx;
import com.app.lbs.common.sms.SmsReqMessage;
import com.app.lbs.common.sms.SmsSender;
import com.app.platform.base.utils.AppContextHolder;
import com.app.platform.base.utils.StringUtil;
import com.app.platform.base.utils.SystemConfig;

/**
 * SMS Platform Low Fee Checker (Meisheng)
 *
 * @author colen
 *
 */
public class SmsPlatformLowfeeChecker {

    /** logger */
    private static final Logger LOG = Logger.getLogger(SmsPlatformLowfeeChecker.class);

    private static final String CATEGORY = "sms.ms.check";
    private static final String CID_FEE = "sms.ms.check.fee";
    private static final String CID_LOWLEVEL = "sms.ms.check.lowlevel";
    private static final String CID_REMINDER = "sms.ms.check.reminder";

    /**
     * Check (every day's 9:00 and 15:00)
     *
     */
    @Scheduled(cron = "${sms.lowfee.check.config}")
    public void check() {
        boolean needCheck = SystemConfig.getBoolean("sms.lowfee.check");
        if (!needCheck) {
            LOG.info("needCheck is false!");
            return;
        }

        Map<String, Map<String, Object>> confMap = getCurConfig();
        
        SmsSender smsSender = (SmsSender) AppContextHolder.getBean(SystemConfig.get("sms.default.sender"));
        
        String result = smsSender.checkNet();
        if (StringUtil.isTrimEmpty(result) || !result.startsWith("0")) {
            LOG.warn("Check SMS Net Error! " + result);
            return;
        }

        String[] arr = result.split("#");
        String net = arr[1];
        Map<String, Object> lowlevelMap = confMap.get(CID_LOWLEVEL);
        String lowlevel = (String) lowlevelMap.get("value");
        
        if (LOG.isInfoEnabled()) {
            LOG.info("sms fee: " + net);
        }
        
        if (isNetLower(net, lowlevel)) {
            String reminder = confMap.get(CID_REMINDER).get("value").toString();
            if (!StringUtil.isTrimEmpty(reminder)) {
                SmsReqMessage smsReqMsg = new SmsReqMessage();
                smsReqMsg.setMsgId(String.valueOf(System.currentTimeMillis()));
                smsReqMsg.setTo(reminder);
                smsReqMsg.setContent(MessageFormat.format(SystemConfig.get("sms.lowfee.check.msg"), net));
                // send message.
                smsSender.send(smsReqMsg);
            }
        }

        Map<String, Object> feeMap = confMap.get(CID_FEE);
        feeMap.put("value", net);
        feeMap.put("update_time", new Date());
        this.commonDao.namingUpdate("sql_common_updateSysConfig", feeMap, true);
    }


    private boolean isNetLower(String net, String lowlevel) {
        if (StringUtil.isTrimEmpty(net) || StringUtil.isTrimEmpty(lowlevel)) {
            return false;
        }
        
        double dNet = Double.valueOf(net);
        double dLowlevel = Double.valueOf(lowlevel);
        
        if (dNet < dLowlevel) {
            return true;
        }
        
        return false;
    }


    private Map<String, Map<String, Object>> getCurConfig() {
        List<Map<String, Object>> confList = this.commonDao
                .queryForList("select * from T_BSF_SYSCONFIG where CATEGORY=?", CATEGORY);
        Map<String, Map<String, Object>> confMap = new HashMap<>();
        for (Map<String, Object> conf: confList) {
            confMap.put(conf.get("config_id").toString(), conf);
        }
        return confMap;
    }

    private CommonDaoEx commonDao = null;

    /**
     * setter
     * @param commonDao the commonDao to set
     */
    public void setCommonDao(CommonDaoEx commonDao) {
        this.commonDao = commonDao;
    }
}
