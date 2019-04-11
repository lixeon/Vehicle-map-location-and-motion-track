/*
 * Copyleft 2011 Power by colen.
 *
 * Project: booking
 * Date: Jul 2, 2011
 */
package com.app.platform.base.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * Http Servlet Util
 *
 * @author colen
 *
 */
public final class HttpServletUtil {

    /**
     * Output Response and send content to client
     *
     * @param content Content
     * @param response response
     * @throws IOException IOException.
     */
    public static void output(HttpServletResponse response, String content) {
        PrintWriter wr = null;
        try {
            // Response
            response.setHeader("Cache-Control", "no-cache");
            // Set Encoding.
            response.setContentType("text/xml;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            wr = response.getWriter();
            wr.write(content);
            wr.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (wr != null) {
                wr.close();
            }
        }
    }
    
    
    /**
     * Output object to client in JSON form
     *
     * @param resp
     * @param obj
     */
    public static void outputJSON(HttpServletResponse resp, Object obj) {
        if (obj == null) {
            return;
        }

        if (obj instanceof String) {
            output(resp, (String) obj);
        } else {
            output(resp, ConvertUtil.toJSON(obj));
        }
    }
}
