/*
 * lbspt
 */
package com.app.platform.base.utils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Web Tool Kits
 *
 * @author colen
 *
 */
public class WebToolKits {


    /**
     * Write Cookie
     *
     * @param resp HttpServletResponse
     * @param key cookie key
     * @param value cookie value
     * @param maxAge Max Age
     * @param encrypt encrypt
     */
    public void setCookie(HttpServletResponse response, String key,
            String value, int maxAge, boolean encrypt) {
        Cookie ck = null;
        if (encrypt) {
            ck = new Cookie(EncryptUtil.encrypt2AES(key), EncryptUtil.encrypt2AES(value));
        } else {
            ck = new Cookie(key, value);
        }
        ck.setPath(SystemConfig.getValue("app.cxtpath"));
        ck.setMaxAge(maxAge);
        response.addCookie(ck);
    }

    /**
     * delete cookie
     * @param response HTTP response
     * @param key cookie key
     * @param encrypt encrypt
     */
    protected void deleteCookie(HttpServletResponse response, String key, boolean encrypt) {
        setCookie(response, key, "", 0, encrypt);
    }

    /**
     * get cookie value
     *
     * @param request HttpServletRequest
     * @param key cookie key
     * @param decrypt decrypt
     * @return cookie value
     */
    protected String getCookieValue(HttpServletRequest request, String key,
            boolean decrypt) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null || cookies.length == 0) {
            return null;
        }

        String mykey = key;
        if (decrypt) {
            mykey = EncryptUtil.encrypt2AES(key);
        }

        String value = null;
        for (Cookie ck : cookies) {
            if (mykey.equals(ck.getName())) {
                value = ck.getValue();
                if (decrypt) {
                    value = EncryptUtil.decrypt2AES(value);
                }
                break;
            }
        }

        return value;
    }


    /**
     * Output Response.
     *
     * @param content Content
     * @param resp response
     */
    public void output(HttpServletResponse resp, String content) {
        HttpServletUtil.output(resp, content);
    }


    /**
     * Out put JSON.
     *
     * @param obj object.
     * @param resp HttpResponse
     */
    public void outputJSON(HttpServletResponse resp, Object obj) {
        HttpServletUtil.outputJSON(resp, obj);
    }
    
    /**
     * output default success
     * 
     * @param resp HttpResponse
     */
    public void outputJSONDefault(HttpServletResponse resp) {
        Map<String, Object> output = getDefaultResult();
        outputJSON(resp, output);
    }

    /**
     * Get Request Parameters.
     *
     * @param req HttpServletRequest
     * @return Map.
     */
    public Map<String, Object> getReqParams(HttpServletRequest req) {
        try {
            req.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {}

        // Result ARG.
        Map<String, Object> args = new LinkedHashMap<String, Object>();
        Iterator<Entry<String, String[]>> pIt = req.getParameterMap().entrySet().iterator();
        Entry<String, String[]> te = null;
        String key = null;
        String[] vals = null;
        while (pIt.hasNext()) {
            te = (Entry<String, String[]>) pIt.next();
            key = te.getKey();
            vals = te.getValue();

            if (vals == null) {
                continue;
            }
            
            if (vals.length == 1) {
                args.put(key, StringUtil.trim(vals[0]));
            } else {
                args.put(key, vals);
            }
        }

        // OUT >>>
        return args;
    }
    

    /**
     * get default result
     *
     * @return
     */
    public Map<String, Object> getDefaultResult() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", "true");
        return map;
    }
}
