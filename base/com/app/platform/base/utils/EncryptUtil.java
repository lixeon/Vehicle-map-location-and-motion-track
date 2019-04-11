/*
 * Copyleft 2011 Power by colen.
 *
 * Project: booking
 * Date: Jul 2, 2011
 */
package com.app.platform.base.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.app.platform.base.exception.BaseSystemException;

/**
 * Encrypt Util Class
 * 
 * @author colen
 * 
 */
public final class EncryptUtil {

    /** Private Constructor */
    private EncryptUtil() {
    }

    /** Code */
    private static final String CODE = "123456789abcdefghijklmnopqrstuvwxyz"
            + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /** Pwd Code */
    private static final String PWD_CODE = "123456789abcdefghijklmnopqrstuvwxyz";

    /** Secret key */
    private static SecretKey AES_KEY;
    static {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            String seed = "a secret key put here!";
            kgen.init(128, new SecureRandom(seed.getBytes("utf-8")));
            AES_KEY = new SecretKeySpec(kgen.generateKey().getEncoded(), "AES");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Encrypt To MD5
     * 
     * @param src (String ) -> bytes(UTF-8)
     * @return String
     */
    public static String toMd5(String src) {
        try {
            return toMd5(src.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new BaseSystemException(e);
        }
    }

    /**
     * Encrypt To MD5
     * 
     * @param bytes bytes
     * @return String
     */
    public static String toMd5(byte[] bytes) {
        char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            byte[] newArr = md.digest();
            int len = newArr.length;
            char[] arr = new char[len * 2];
            int k = 0;
            for (int i = 0; i < len; i++) {
                byte byte0 = newArr[i];
                arr[k++] = hexDigits[byte0 >>> 4 & 0xf];
                arr[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(arr).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new BaseSystemException(e);
        }
    }

    /**
     * Get Data MD5
     * 
     * @param len length.
     * @return MD5 Code.
     */
    public static String getDataMd5(int len) {
        int cLen = CODE.length();
        Random r = new Random();
        StringBuilder bud = new StringBuilder();
        for (int i = 0; i < len; i++) {
            bud.append(CODE.charAt(r.nextInt(cLen)));
        }
        return bud.toString();
    }

    /**
     * Get Data MD5
     * 
     * @param len length.
     * @return MD5 Code.
     */
    public static String getRandPwd(int len) {
        int cLen = PWD_CODE.length();
        Random r = new Random();
        StringBuilder bud = new StringBuilder();
        for (int i = 0; i < len; i++) {
            bud.append(PWD_CODE.charAt(r.nextInt(cLen)));
        }
        return bud.toString();
    }

    private static final String PWD_CODE_DIGIT = "123456789";

    /**
     * Get Random Password
     * 
     * @param len
     * @return
     */
    public static String getDigitRandPwd(int len) {
        int cLen = PWD_CODE_DIGIT.length();
        Random r = new Random();
        StringBuilder bud = new StringBuilder();
        for (int i = 0; i < len; i++) {
            bud.append(PWD_CODE_DIGIT.charAt(r.nextInt(cLen)));
        }
        return bud.toString();
    }

    /**
     * Get Data MD5
     * 
     * @param len
     * @param oldVal
     * @return
     */
    public static String getDataMd5(int len, String oldVal) {
        String tv = null;
        while (true) {
            tv = getDataMd5(len);
            if (!tv.equals(oldVal))
                break;
        }
        return tv;
    }

    /**
     * convert HEX String to bytes
     * 
     * @param hex
     * @return
     */
    private static byte[] hex2byte(String hex) {
        int len = hex.length();
        if (len < 1) {
            return null;
        }
        byte[] b = new byte[len / 2];
        for (int i = 0; i != len / 2; i++) {
            b[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
        }
        return b;
    }

    /**
     * convert byte to hex string
     * 
     * @param bytes Bytes
     * @return
     */
    private static String byte2hex(byte[] bytes) {
        StringBuilder bud = new StringBuilder();
        String tmp = null;
        for (int n = 0; n < bytes.length; n++) {
            tmp = (Integer.toHexString(bytes[n] & 0XFF));
            if (tmp.length() == 1) {
                bud.append("0");
            }
            bud.append(tmp);
        }
        return bud.toString().toUpperCase();
    }

    /**
     * Encrypt to AES
     *
     * @param info info
     * @return
     */
    public static String encrypt2AES(String info) {
        try {
            // cipher
            Cipher cipher = Cipher.getInstance("AES");
            // encrypt
            cipher.init(Cipher.ENCRYPT_MODE, AES_KEY);
            byte[] result = cipher.doFinal(info.getBytes("utf-8"));
            return byte2hex(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * decrypt to AES
     *
     * @param info info
     * @return
     */
    public static String decrypt2AES(String hexinfo) {
        try {
            // cipher
            Cipher cipher = Cipher.getInstance("AES");
            // encrypt
            cipher.init(Cipher.DECRYPT_MODE, AES_KEY);
            byte[] result = cipher.doFinal(hex2byte(hexinfo));
            return new String(result, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String toSHA256(String input) {
        MessageDigest md = null;
        String result = null;
        String encName = "SHA-256";
        byte[] bt = input.getBytes();
        try {
            md = MessageDigest.getInstance(encName);
            md.update(bt);
            result = byte2hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return result;
    }

    /**
     * TEST
     * 
     * @param args
     */
    public static void main(String[] args) throws Exception {
        
        System.out.println(toMd5("1"));
        System.out.println(toSHA256("1"));
        
        /*
        String info = "auth_code";
        System.out.println("1 info :" + info);

        // generate a key
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128, new SecureRandom("a secret key put here!".getBytes("utf-8")));
        SecretKey key = new SecretKeySpec(kgen.generateKey().getEncoded(), "AES");

        // cipher
        Cipher cipher = Cipher.getInstance("AES");

        // encrypt
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] result = cipher.doFinal(info.getBytes("utf-8"));
        String hexResult = byte2hex(result);
        System.out.println("2 info :" + hexResult);

        // decrypt
        cipher.init(Cipher.DECRYPT_MODE, key);
        result = cipher.doFinal(hex2byte(hexResult));
        System.out.println("3 info :" + new String(result, "utf-8"));
        
        System.out.println("1 info :" + info);
        System.out.println("2 info :" + EncryptUtil.encrypt2AES(info));
        System.out.println("3 info :" + EncryptUtil.decrypt2AES(EncryptUtil.encrypt2AES(info)));
        */
    }


}
