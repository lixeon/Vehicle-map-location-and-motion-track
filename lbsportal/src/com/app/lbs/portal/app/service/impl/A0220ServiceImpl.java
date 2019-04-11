/**
 * 
 */
package com.app.lbs.portal.app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.app.lbs.portal.app.service.AbstractClientAppService;

/**
 * Get Fences Info by Terminal ID
 *
 * @author luxinxian
 *
 */
public class A0220ServiceImpl extends AbstractClientAppService {

    /** LOG */
    private static final Logger LOG = Logger.getLogger(A0220ServiceImpl.class);

    @Override
    protected Map<String, Object> execute(Map<String, Object> params) {
        
        String terminalId = (String) params.get("terminal_id");

        if (LOG.isInfoEnabled()) {
            LOG.info("will get terminal's fences (" + terminalId + ")");
        }
        
        // will fetch terminal's fences here.
        
        Map<String, Object> output = new HashMap<>();
        output.put("fence_list", new ArrayList<>());

        return output;
    }
    

    @Override
    public String serviceKey() {
        return "0220";
    }

    @Override
    protected boolean isAuth() {
        return true;
    }

}
