/*
 * lbsgw
 */
package com.app.lbs.common.servlet;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.app.lbs.common.server.Server;
import com.app.lbs.common.service.Service;
import com.app.platform.base.utils.AppContextHolder;

/**
 * Abstract Servlet Server
 *
 * @author colen
 *
 */
public abstract class AbstractServletServer extends CommonServlet implements Server {


    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /** Service Map */
    private Map<String, Service<?, ?>> serviceMap = null;

        Service<Map<String, Object>, ?> service = null;    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void start(Map<String, Object> args) {
        // holder services that CMCC protocol need.
        this.serviceMap = new HashMap<>();
        Map<String, Service> services = AppContextHolder.getBeansOfType(Service.class);

        String module = getModule();
        String serviceKey = null;
        for (Entry<String, Service> e : services.entrySet()) {
            service = e.getValue();
            // find the right module
            if (module.equals(service.serviceModule())) {
                serviceKey = service.serviceKey();
                if (!this.serviceMap.containsKey(serviceKey)) {
                    this.serviceMap.put(serviceKey, service);
                } else {
                    throw new IllegalArgumentException("duplicate service key, module = "
                            + module + ", servcie key = " + serviceKey);
                }
            }
        }
    }

    /**
     * get module
     *
     * @return module key
     */
    public abstract String getModule();

    @Override
    public void stop(Map<String, Object> args) {
    }

    /** white ip list */
    private Map<String, String> whiteIPs = null;

    /**
     * Check Client IP
     *
     * @param clientIp Client IP
     * @return true/false
     */
    protected boolean checkClientIp(String ip) {
        if (this.whiteIPs == null) {
            return true;
        }

        boolean ok = false;
        if (this.whiteIPs.containsKey(ip)) {
            ok = true; 
        }
        return ok;
    }


    /**
     * @return the whiteIPs
     */
    public Map<String, String> getWhiteIPs() {
        return whiteIPs;
    }


    /**
     * @param whiteIPs the whiteIPs to set
     */
    public void setWhiteIPs(Map<String, String> whiteIPs) {
        this.whiteIPs = whiteIPs;
    }

    /**
     * getter
     * @return the serviceMap
     */
    public Map<String, Service<?, ?>> getServiceMap() {
        return serviceMap;
    }

    /**
     * setter
     * @param serviceMap the serviceMap to set
     */
    public void setServiceMap(Map<String, Service<?, ?>> serviceMap) {
        this.serviceMap = serviceMap;
    }
}
