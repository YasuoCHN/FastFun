package com.klw.fastfun.pay.view.app.other.ght.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util 
{
	public static String stringToMD5(String sourceStr, String charset)
	{
		if(sourceStr==null || "".equals(sourceStr))
			return null;
		String destStr=null;
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
	        //字符串
	        md5.update(sourceStr.getBytes(charset));//str.getBytes("utf-8")
	        byte[] md=md5.digest();
	        int j=md.length;
	        char str[] = new char[j * 2];
            int k = 0;
            char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'}; 
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            destStr= new String(str);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return destStr;
	}
}
