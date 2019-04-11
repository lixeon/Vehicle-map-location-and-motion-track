/*
 * Copyleft 2011 Power by colen.
 *
 * Project: booking
 * Date: Jul 2, 2011
 */
package com.app.platform.base.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.app.platform.base.exception.BaseSystemException;


/**
 * Bean Operation Class
 *
 * @author colen
 *
 */
public final class BeanUtil {

    /** Logger */
    private static Logger logger = Logger.getLogger(BeanUtil.class);

    /** Private Constructor */
    private BeanUtil() {}

    /**
     * New Instance by specified class
     *
     * @param clazz Class
     * @param initDefault Initialize as default Object.
     * @return Object
     * @throws BaseSystemException
     */
    @SuppressWarnings({"rawtypes" })
    public static Object newInstance(Class clazz, boolean... initDefault) {
        try {
            Object bean = clazz.newInstance();
            return bean;
        } catch (InstantiationException e) {
            logger.error("newInstance Error (InstantiationException)");
            throw new BaseSystemException(e);
        } catch (IllegalAccessException e) {
            logger.error("newInstance Error (IllegalAccessException)");
            throw new BaseSystemException(e);
        }
    }

    /**
     * Clone bean (Deep)
     *
     * @param obj Object.
     * @return Object.
     */
    public static Object cloneBean(Object obj) {
        Object copy = null;
        ByteArrayOutputStream bout = null;
        ObjectOutputStream out = null;
        ByteArrayInputStream bin = null;
        ObjectInputStream oin = null;

        try {
            bout = new ByteArrayOutputStream();
            out = new ObjectOutputStream(bout);
            out.writeObject(obj);
            out.close();

            bin = new ByteArrayInputStream(bout.toByteArray());
            oin = new ObjectInputStream(bin);
            copy = oin.readObject();
            oin.close();
        } catch (IOException e) {
            logger.error("Clone Object Error (IOException)");
            throw new BaseSystemException(e);
        } catch (ClassNotFoundException e) {
            logger.error("Clone Object Error (ClassNotFoundException)");
            throw new BaseSystemException(e);
        } finally {
            CommonUtil.close(oin);
            CommonUtil.close(bin);
            CommonUtil.close(out);
            CommonUtil.close(bout);
        }

        // OUT >>>
        return copy;
    }


    /**
     * Set Property to Bean
     *
     * @param bean bean
     * @param name field Name
     * @param value value
     */
    public static void setProperty(Object bean, String name, Object value) {
        try {
            BeanUtils.setProperty(bean, name, value);
        } catch (IllegalAccessException ie) {
            logger.error("Can not set the property by access problem!");
            throw new BaseSystemException(ie);
        } catch (InvocationTargetException ine) {
            throw new BaseSystemException(ine);
        }
    }

}
