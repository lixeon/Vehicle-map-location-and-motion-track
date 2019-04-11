/*
 * Copyleft 2011 Power by colen.
 *
 * Project: booking
 * Date: Jul 2, 2011
 */
package com.app.platform.base.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import io.netty.buffer.ByteBuf;

/**
 * String Util Class
 *
 * @author colen
 */
public final class StringUtil extends StringUtils {

    /** Private Constructor */
    private StringUtil() {
    }

    /**
     * To String
     *
     * @param obj Object.
     * @return string
     */
    public static String toStr(Object obj) {
        return (obj != null) ? obj.toString() : null;
    }

    /**
     * Is Trim Empty
     *
     * @param s String
     * @return true\false
     */
    public static boolean isTrimEmpty(String s) {
        return isEmpty(s) ? true : isEmpty(s.trim());
    }


    /**
     * 组装计算数据条数的SQL语句
     *
     * @param sql 传入原始SQL语句
     * @return 组装后的COUNT(*)语句
     */
    public static String cnvtConutSql(String sql) {
        if (isEmpty(sql)) {
            return null;
        }
        StringBuilder finalSql = new StringBuilder();
        finalSql.append("select count(*) c from (");
        finalSql.append(sql);
        finalSql.append(") temp");
        return finalSql.toString();
    }

    /**
     * Convert SQL to Page selector.
     *
     * @param sql SQL
     * @param curPage Current Page.
     * @param pageSize Page size
     * @return String SQL
     */
    public static String cnvtPageSql(String sql, int curPage, int pageSize) {
        // Empty sql
        if (isEmpty(sql)) {
            return sql;
        }

        // Start Row
        String fRow = String.valueOf(pageSize * (curPage - 1) + 1);
        // End Row
        String eRow = String.valueOf(pageSize * curPage);
        // OUT >>>
        return QUERY_TEMPLATE.replace("$P1$", sql).replace("$P2$", fRow)
                .replace("$P3$", eRow);
    }

    /** 分页查询模板 */
    private static final String QUERY_TEMPLATE = "SELECT * FROM (SELECT TB_.*, ROWNUM RN"
            + " FROM ($P1$) TB_ WHERE ROWNUM <= $P3$) WHERE RN >= $P2$";

    /**
     * Remove repeated Element.
     *
     * @param arr Source Array
     * @return string array
     */
    public static String[] removeRepeat(String[] arr) {
        if (CommonUtil.isEmpty(arr)) {
            return null;
        }
        Set<String> set = new LinkedHashSet<String>();
        for (String s : arr) {
            set.add(s);
        }
        // OUT
        return set.toArray(new String[0]);
    }

    /**
     * Make a String by String array
     *
     * @param arr Array.
     * @param spliter Split
     * @return
     */
    public static String makeString(String[] arr, String spliter) {
        StringBuilder bud = new StringBuilder();
        for (String a : arr) {
            bud.append(a).append(spliter);
        }
        return bud.substring(0, bud.length() - spliter.length());
    }

    /**
     * SubStr.
     *
     * @param s
     * @param len
     * @return
     */
    public static String substr(String s, int len) {
        if (isEmpty(s) || s.length() <= len) {
            return s;
        }
        return s.substring(0, len);
    }
    
    /**
     * cookie存储中文时，可先把存储的中文用utf-8编码一下
     * @param str
     */
    public static String encodeByUTF8(String str) {
    	try {
			str = URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {}
    	return str;
    }
    
    
    /**
     * encode xml
     *
     * @param data
     * @return
     */
    public static String encodeXml(String data) {
        return data.replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&apos;");
    }
    
    
    /**
     * decode xml
     *
     * @param data
     * @return
     */
    public static String decodeXml(String data) {
        return data.replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("&quot;", "\"")
                .replaceAll("&amp;", "&")
                .replaceAll("&apos;", "'");
    }
    
    /**  
     * 把16进制字符串转换成字节数组  
     * @param hex  
     * @return  
    */   
    /**
     * bytes字符串转换为Byte值
     * @param src String Byte字符串，每个Byte之间没有分隔符(字符范围:0-9 A-F)
     * @return byte[]
     */
    public static byte[] hexStr2Bytes(String src){
        /*对输入值进行规范化整理*/
        src = src.trim().replace(" ", "").toUpperCase(Locale.US);
        //处理值初始化
        int m=0,n=0;
        int iLen=src.length()/2; //计算长度
        byte[] ret = new byte[iLen]; //分配存储空间
        
        for (int i = 0; i < iLen; i++){
            m=i*2+1;
            n=m+1;
            ret[i] = (byte)(Integer.decode("0x"+ src.substring(i*2, m) + src.substring(m,n)) & 0xFF);
        }
        return ret;
    } 
  
 /** *//**  
     * 把字节数组转换成16进制字符串  
     * @param bArray  
     * @return  
     */   
 public static final String bytesToHexString(byte[] bArray) {   
     StringBuffer sb = new StringBuffer(bArray.length);   
     String sTemp;   
     for (int i = 0; i < bArray.length; i++) {   
     sTemp = Integer.toHexString(0xFF & bArray[i]);   
     if (sTemp.length() < 2)   
    sb.append(0);   
     sb.append(sTemp.toUpperCase());   
     }   
     return sb.toString();   
 }  
 
 
   
 /** *//**  
     * 把字节数组转换为对象  
     * @param bytes  
     * @return  
     * @throws IOException  
     * @throws ClassNotFoundException  
     */   
 public static final Object bytesToObject(byte[] bytes) throws IOException, ClassNotFoundException {   
     ByteArrayInputStream in = new ByteArrayInputStream(bytes);   
     ObjectInputStream oi = new ObjectInputStream(in);   
     Object o = oi.readObject();   
     oi.close();   
     return o;   
 }  
   
 /** *//**  
     * 把可序列化对象转换成字节数组  
     * @param s  
     * @return  
     * @throws IOException  
     */   

   
 public static final String objectToHexString(Object s) throws IOException{   
     return bytesToHexString(Object2Bytes(s));   
 }  
   
 
 /// 将对象转换为byte数组  
    /// </summary>  
    /// <param name="obj">被转换对象</param>  
    /// <returns>转换后byte数组</returns>  
    public static byte[] Object2Bytes(Object obj)  
    {  
		ByteBuf buf = (ByteBuf) obj;
		if (buf.readableBytes() <= 0) {
			return null;
		}

		byte[] bs = new byte[buf.readableBytes()];
		buf.readBytes(bs);
		return bs;
    } 


 
   
 /** *//**  
     * @函数功能: BCD码转为10进制串(阿拉伯数据)  
     * @输入参数: BCD码  
     * @输出结果: 10进制串  
     */   
 public static String bcd2Str(byte[] bytes){   
     StringBuffer temp=new StringBuffer(bytes.length*2);  
   
     for(int i=0;i<bytes.length;i++){   
      temp.append((byte)((bytes[i]& 0xf0)>>>4));   
      temp.append((byte)(bytes[i]& 0x0f));   
     }   
     return temp.toString().substring(0,1).equalsIgnoreCase("0")?temp.toString().substring(1):temp.toString();   
 }  
   
 /** *//**  
     * @函数功能: 10进制串转为BCD码  
     * @输入参数: 10进制串  
     * @输出结果: BCD码  
     */   
 public static byte[] str2Bcd(String asc) {   
     int len = asc.length();   
    int mod = len % 2;  
   
     if (mod != 0) {   
      asc = "0" + asc;   
      len = asc.length();   
     }  
   
     byte abt[] = new byte[len];   
     if (len >= 2) {   
      len = len / 2;   
    }  
   
     byte bbt[] = new byte[len];   
     abt = asc.getBytes();   
     int j, k;  
   
     for (int p = 0; p < asc.length()/2; p++) {   
      if ( (abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {   
       j = abt[2 * p] - '0';   
 } else if ( (abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {   
 j = abt[2 * p] - 'a' + 0x0a;   
 } else {   
 j = abt[2 * p] - 'A' + 0x0a;   
 }  

 if ( (abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {   
 k = abt[2 * p + 1] - '0';   
 } else if ( (abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {   
 k = abt[2 * p + 1] - 'a' + 0x0a;   
 }else {   
 k = abt[2 * p + 1] - 'A' + 0x0a;   
 }  

 int a = (j << 4) + k;   
 byte b = (byte) a;   
 bbt[p] = b;   
 }   
 return bbt;   
 }   


    
    public static void main(String[] args) {
        String orgData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><StaffBindRsp><HEAD><CODE>StaffBind</CODE><SID>:sid</SID><TIMESTAMP>:timestamp</TIMESTAMP><SERVICEID>:serviceid</SERVICEID></HEAD><BODY>#msg_body#</BODY></StaffBindRsp>";
        System.out.println("orgdata: " + orgData);
        
        String encodeData = encodeXml(orgData);
        System.out.println("encodeData: " + encodeData);
        
        String decodeData = decodeXml(encodeData);
        System.out.println("decodeData: " + decodeData);
    }
}


