/*
 * lbspt
 */
package com.app.platform.base.utils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;

/**
 * Http Client Utils
 *
 * @author colen
 *
 */
public class HttpClientUtil {

    /**
     * Post
     *
     * @param url URL
     * @param params parameters
     * @return string
     * @throws IOException 
     */
    public static String post(String url, Map<String, String> params) throws IOException {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> e : params.entrySet()) {
            list.add(new BasicNameValuePair(e.getKey(), e.getValue()));
        }

        String resp = Request.Post(url)
                .bodyForm(list, Charset.forName("UTF-8"))
                .connectTimeout(1000)
                .socketTimeout(1000)
                .execute()
                .returnContent()
                .asString();

        return resp;
    }
   
    /**
     * Get
     *
     * @param url URL
     * @param params parameters
     * @return string
     * @throws IOException 
     */
    public static String get(String url, Map<String, String> params) throws IOException {
        StringBuilder uri = new StringBuilder(url);
        
        if (url.indexOf("?") == -1) {
            uri.append("?r_t_s_dt=" + Math.random());
        }
        
        for (Map.Entry<String, String> e : params.entrySet()) {
            uri.append("&").append(e.getKey()).append("=").append(URLEncoder.encode(e.getValue().toString(), "UTF-8"));
        }

        String resp = Request.Get(uri.toString())
        		.connectTimeout(500)
        		.socketTimeout(500)
                .execute()
                .returnContent()
                .asString();

        return resp;
    }
 }
