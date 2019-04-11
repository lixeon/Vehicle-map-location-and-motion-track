/*
 * LBSAPP
 */
package com.app.lbs.portal.app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.app.lbs.portal.PTConsts;
import com.app.lbs.portal.app.AppReqException;
import com.app.lbs.portal.app.service.AbstractClientAppService;
import com.app.platform.base.utils.CommonUtil;
import com.app.platform.base.utils.StringUtil;

/**
 * Query Terminal Configurations
 *
 * @author luxinxian
 *
 */
public class A0260ServiceImpl extends AbstractClientAppService {
    

    /** LOG */
    private static final Logger LOG = Logger.getLogger(A0200ServiceImpl.class);

    
    @Override
    protected Map<String, Object> execute(Map<String, Object> params) {

        String terminalId = (String) params.get("terminal_id");
        String pKeys = (String) params.get("p_keys");

        if (StringUtil.isTrimEmpty(pKeys)) {
            LOG.warn("empty p_keys");
            throw new AppReqException(PTConsts.RESPCD_E905);
        }

        String[] keys = pKeys.split(",");
        List<String> keyList = new ArrayList<>();
        for (String key : keys) {
            if (!StringUtil.isTrimEmpty(key)) {
                keyList.add(key.trim());
            }
        }

        if (CommonUtil.isEmpty(keyList)) {
            LOG.warn("empty p_keys");
            throw new AppReqException(PTConsts.RESPCD_E905);
        }

        Map<String, Object> qps = new HashMap<>();
        qps.put("terminalSerialNo", terminalId);
        int idx = 1;
        for (String key : keyList) {
            qps.put("k" + (idx++), key);
        }
        List<Map<String, Object>> configlist = this.getCommonDao()
                .namingQueryForList("sql_gateway_queryTerminalConfigByKeys", qps);

        List<Map<String, Object>> paramList = new ArrayList<>();
        if (!CommonUtil.isEmpty(configlist)) {
            Map<String, Object> param = null;
            for (Map<String, Object> config : configlist) {
                param = new HashMap<>();
                param.put("pk", config.get("config_key"));
                param.put("pv", config.get("value"));
                param.put("status", config.get("status"));
                // hold
                paramList.add(param);
            }
        }

        Map<String, Object> output = new HashMap<>();
        output.put("params", paramList);
        return output;
    }

    @Override
    public String serviceKey() {
        return "0260";
    }

    @Override
    protected boolean isAuth() {
        return true;
    }

}
