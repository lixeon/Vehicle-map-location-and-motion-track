package com.app.lbs.common.minigps;


import java.util.ArrayList;
import java.util.List;

public class WifiAddressConverter {
	private static String keys = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static int exponent = keys.length();

    public static String Long2Str(long value)
    {
        String resultNoSplited = "";
        String resultSplited = "";
        do
        {
            int index = (int) (value % exponent);
            resultNoSplited = keys.charAt(index)+ resultNoSplited;
            value = (value - index) / exponent;
        }
        while (value > 0);
        int leftlen = 12 -  resultNoSplited.length();
        for(int i = 0; i < leftlen; ++i)
        {
        	resultNoSplited = "0" + resultNoSplited;
        }
        for (int i = 0; i <= 11; i++)
        {
        	
        	resultSplited = resultSplited +  resultNoSplited.charAt(i) ;
            if (i < 11 && i % 2 == 1)
            {
            	resultSplited =resultSplited + ":" ;
            }
        }
        return resultSplited;
    }

    public static long Str2Long(String value)
    {
    	value = value.toUpperCase();
        value = value.replace(":", "");
        long result = 0;
        for (int i = 0; i < value.length(); i++)
        {
            int x = value.length() - i - 1;
            char ch = value.charAt(i);
            result += keys.indexOf(ch) * Pow(exponent, x);
        }
        return result;
    }
    
    public static long HexStr2Long(String value)
    {
    	value = value.toUpperCase();
        long result = 0;
        for (int i = 0; i < value.length(); i++)
        {
            int x = value.length() - i - 1;
            char ch = value.charAt(i);
            result += keys.indexOf(ch) * Pow(exponent, x);
        }
        return result;
    }
    public static String Long2HexStr(long value)
    {
        String resultNoSplited = "";
        String resultSplited = "";
        do
        {
            int index = (int) (value % exponent);
            resultNoSplited = keys.charAt(index)+ resultNoSplited;
            value = (value - index) / exponent;
        }
        while (value > 0);
        int leftlen = 12 -  resultNoSplited.length();
        for(int i = 0; i < leftlen; ++i)
        {
        	resultNoSplited = "0" + resultNoSplited;
        }
        for (int i = 0; i <= 11; i++)
        {
        	
        	resultSplited = resultSplited +  resultNoSplited.charAt(i) ;
            if (i < 11 && i % 2 == 1)
            {
            	resultSplited =resultSplited + "" ;
            }
        }
        return resultSplited;
    }
    /// <summary>
    /// һ����ݵ�N�η�
    /// </summary>
    /// <param name="x"></param>
    /// <returns></returns>
    private static long Pow(long baseNo, long x)
    {
        long value = 1;
        while (x > 0)
        {
            value = value * baseNo;
            x--;
        }
        return value;
    }
    
    
}
