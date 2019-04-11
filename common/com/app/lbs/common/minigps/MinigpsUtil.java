package com.app.lbs.common.minigps;

/*
 * RemoteUtil.java
 * 
 * @date: 2012-11-30
 * @author: Xiaoyu Guo
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author Xiaoyu Guo
 *
 */
public class MinigpsUtil {
	private static final Logger logger = Logger.getLogger(MinigpsUtil.class);
	public static final String METHOD_POST = "POST";
	public static final String METHOD_GET = "GET";
	public static final int CONNECT_TIMEOUT = 500;
	public static final int READ_TIMEOUT = 2000;

	/**
	 * 
	 * @param minigpsurl  minigpsURL
	 * @param params   请求参数
	 * @return
	 */
	public static String get(String minigpsurl, Map<String, String> params) {
		String responseText = "";

		StringBuilder uri = new StringBuilder(minigpsurl);
		if (minigpsurl.indexOf("?") == -1) {
			uri.append("?r_t_s_dt=" + Math.random());
		}
		for (Map.Entry<String, String> e : params.entrySet()) {
			try {
				uri.append("&")	
						.append((String) e.getKey())
						.append("=")
						.append(URLEncoder.encode(
								((String) e.getValue()).toString(), "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				
			}
		}
		long intime = System.currentTimeMillis();
		
		HttpURLConnection conn = null;
		try {
			URL url = new URL(uri.toString());
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);

			conn.setRequestMethod(METHOD_GET);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("User-Agent", "");

			conn.setUseCaches(false);

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "utf-8"));
			String currentLine = "";
			StringBuffer sb = new StringBuffer();
			while ((currentLine = rd.readLine()) != null) {
				sb.append(currentLine);
				sb.append("\n");
			}
			responseText = sb.toString();

			rd.close();
		} catch (Exception ex) {
			if (conn != null) {
				logger.warn("close:" + minigpsurl);
				conn.disconnect();
			}
			logger.error(ex);
			ex.printStackTrace();
		}
		//计算耗时
		long outtime = System.currentTimeMillis();
		double runningtime = (outtime - intime) / 1000.0;
		logger.info("MiniGps Resping time:" + runningtime);
		
		// finally
		// {
		// conn.disconnect();
		// }
		return responseText;
	}

	/**
	 * 
	 * @param minigpsurl  minigpsURL
	 * @param params   请求参数
	 * @return
	 */
	public static String post(String minigpsurl, Map<String, String> params) {
		String responseText = "";

		StringBuilder uri = new StringBuilder(minigpsurl);
		if (minigpsurl.indexOf("?") == -1) {
			uri.append("?r_t_s_dt=" + Math.random());
		}
		for (Map.Entry<String, String> e : params.entrySet()) {
			try {
				uri.append("&")	
						.append((String) e.getKey())
						.append("=")
						.append(URLEncoder.encode(
								((String) e.getValue()).toString(), "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				
			}
		}
		long intime = System.currentTimeMillis();
		HttpURLConnection conn = null;
		try {
			URL url = new URL(uri.toString());
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);

			conn.setRequestMethod(METHOD_GET);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("User-Agent", "");

			conn.setUseCaches(false);

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "utf-8"));
			String currentLine = "";
			StringBuffer sb = new StringBuffer();
			while ((currentLine = rd.readLine()) != null) {
				sb.append(currentLine);
				sb.append("\n");
			}
			responseText = sb.toString();

			rd.close();
		} catch (Exception ex) {
			if (conn != null) {
				logger.warn("close:" + minigpsurl);
				conn.disconnect();
			}
			logger.error(ex);
			ex.printStackTrace();
		}
		//计算耗时
		long outtime = System.currentTimeMillis();
		double runningtime = (outtime - intime) / 1000.0;
		logger.info("MiniGps Resping time:" + runningtime);
		
		// finally
		// {
		// conn.disconnect();
		// }
		return responseText;
	}

}
