/*
 * Copyleft 2011 Power by colen.
 *
 * Project: booking
 * Date: Jul 2, 2011
 */
package com.app.platform.base.utils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.app.platform.base.exception.BaseSystemException;

/**
 * Convert Util
 *
 * @author colen
 *
 */
public class ConvertUtil {

    /** Logger */
    private static final Logger LOG = Logger.getLogger(ConvertUtil.class);

    /** Private Constructor */
    private ConvertUtil() {}

    /**
     * Convert Bean to Map
     *
     * @param map Map
     * @param bean bean
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    public static void cnvBean2Map(Map map, Object bean) {
        // Null value
        if (bean == null || map == null) {
            return;
        }

        try {
            Map describe = BeanUtils.describe(bean);
            describe.remove("class");
            map.putAll(describe);
        } catch (IllegalAccessException ie) {
            LOG.debug("Can not set the property by IllegalAccessException!");
            throw new BaseSystemException(ie);
        } catch (InvocationTargetException ie) {
            throw new BaseSystemException(ie);
        } catch (NoSuchMethodException ne) {
            throw new BaseSystemException(ne);
        }
    }


    /**
     * Convert Map to Bean
     *
     * @param bean Bean
     * @param map Map
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void cnvMap2Bean(Object bean, Map map) {
        // Null value
        if (bean == null || map == null) {
            return;
        }

        Iterator<Entry<String, ?>> entrys = map.entrySet().iterator();
        Entry<String, ?> entry = null;
        Object value = null;
        while (entrys.hasNext()) {
            entry = entrys.next();
            value = entry.getValue();
            if (value == null) { continue; }
            BeanUtil.setProperty(bean, cnvDBCol2FieldNm(entry.getKey()), value);
        }
    }

    /**
     * Convert DB Column to bean property name
     *
     * @param key key name
     * @return column
     */
    private static String cnvDBCol2FieldNm(String key) {
        // Empty key
        if (StringUtil.isEmpty(key)) { return key; }

        StringBuilder bud = new StringBuilder();
        char[] arr = key.toLowerCase().toCharArray();
        char ch = 0;
        int length = arr.length;
        for (int i = 0; i < length; i++) {
            ch = arr[i];
            if ('_' == ch) {
                if ((i + 1) < length) {
                    bud.append(String.valueOf(arr[++i]).toUpperCase());
                }
            } else {
                bud.append(ch);
            }
        }

        // OUT >>>
        return bud.toString();
    }
    
    /**
     * Lower Map keys in this list
     *
     * @param l list
     */
    public static void lowerListMapKeys(List<Map<String, Object>> l) {
        // Empty list OUT >>>
        if (isEmpty(l)) { return; }

        for (Map<String, Object> m : l) {
            lowerMapkeys(m);
        }
    }


    /**
     * Lower Map Keys
     *
     * @param m Map
     */
    @SuppressWarnings({ "rawtypes", "unchecked"})
    public static void lowerMapkeys(Map<String, Object> m) {
        // Null Map OUT >>>
        if (m == null) { return; }

        Map tm = new LinkedHashMap();
        Iterator<Map.Entry<String, Object>> kv = m.entrySet().iterator();
        Map.Entry<String, Object> e = null;
        while (kv.hasNext()) {
            e = kv.next();
            tm.put(e.getKey().toLowerCase(), e.getValue());
        }
        m.clear();
        m.putAll(tm);
    }

    /**
     * Is Empty List
     *
     * @param l list
     * @return true/false
     */
    @SuppressWarnings({ "rawtypes" })
    public static boolean isEmpty(List l) {
        return (l == null || l.isEmpty()) ? true : false;
    }

    /**
     * Is Empty Array
     *
     * @param objs Objects
     * @return boolean
     */
    public static boolean isEmpty(Object[] objs) {
        return (objs == null || objs.length == 0) ? true : false;
    }

    /**
     * 补零
     *
     * @param val value
     * @param len length
     * @return string value
     */
    public static String padd(long val, int len) {
        String sVal = String.valueOf(val);
        StringBuilder bud = new StringBuilder(sVal);
        int slen = sVal.length();
        if (slen < len) {
            for (; slen < len; slen++) {
                bud.insert(0, "0");
            }
        }
        // OUT >>>
        return bud.toString();
    }


    /**
     * To JSON String
     *
     * @param map Map
     * @return JSON String
     */
    public static String toJSON(Object obj) {
        // Empty value
        if (obj == null) {
            return null;
        }

        String json = JSON.toJSONString(obj);
        
        // OUT >>>
        return json;
    }

    /**
     * Round
     *
     * @param x double value
     * @param scale Scale
     * @return string value.
     */
    public static String round(double x, int scale) {
        BigDecimal bdX = new BigDecimal(x);
        return bdX.setScale(scale, BigDecimal.ROUND_DOWN).toPlainString();
    }

    /**
     * Round
     *
     * @param x double value
     * @param scale Scale
     * @return string value.
     */
    public static double round2(double x, int scale) {
        BigDecimal bdX = new BigDecimal(x);
        return bdX.setScale(scale, BigDecimal.ROUND_DOWN).doubleValue();
    }
}
