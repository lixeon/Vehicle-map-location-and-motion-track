/*
 * Copyleft 2011 Power by colen.
 *
 * Project: app-crm
 * Date: Jul 2, 2011
 */
package com.app.platform.base.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * Common Util
 * 
 * @author colen
 */
public class CommonUtil {

    /**
     * Close Stream
     * 
     * @param closeable Closeable
     */
    public static void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
        }
    }

    /**
     * Is Empty Object array.
     *
     * @param args Arguments
     * @return boolean
     */
    public static boolean isEmpty(Object[] args) {
        return ConvertUtil.isEmpty(args);
    }

    /**
     * Is Empty Object
     *
     * @param obj source Object
     * @return boolean
     */
    public static boolean isEmpty(Object obj) {
        return (obj == null) ? true : false;
    }

    /**
     * Is Empty Object
     *
     * @param list source Object
     * @return boolean
     */
    public static boolean isEmpty(List<?> list) {
        return ConvertUtil.isEmpty(list);
    }

    /**
     * load properties
     *
     * @param fileName
     * @return
     * @throws IOException 
     */
    public static Properties loadProps(String fileName) throws IOException {
        Properties props = null;
        InputStream inStream = null;
        try {
            // Properties
            props = new Properties();
            File cf = new File(fileName);
            inStream = new FileInputStream(cf);
            props.load(inStream);
        } finally {
            close(inStream);
        }

        // out >>>
        return props;
    }
}
