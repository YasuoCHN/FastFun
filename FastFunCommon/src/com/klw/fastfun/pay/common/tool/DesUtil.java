package com.klw.fastfun.pay.common.tool;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DesUtil {
	private static final byte[] IV = {6, 6, 3, 3, 8, 7, 2, 3};
	private static final String KEY = "86324958";

	public static String encryptDES(String encryptString){
		try{
			encryptString = StringToByte(encryptString);
			
			IvParameterSpec zeroIv = new IvParameterSpec(IV);
			SecretKeySpec key = new SecretKeySpec(KEY.getBytes(),"DES");
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE,key,zeroIv);
			byte[] encryptedData = cipher.doFinal(encryptString.getBytes());

			return URLEncoder.encode(Base64.encode(encryptedData),"utf-8");
		}catch(Exception e){
		}
		return null;
	}

	public static String decryptDES(String decryptString){
		try{
			decryptString = URLDecoder.decode(decryptString,"utf-8");
			byte decryptedData[] = null;
			new Base64();
			byte[] byteMi = Base64.decode(decryptString);
			IvParameterSpec zeroIv = new IvParameterSpec(IV);
			SecretKeySpec key = new SecretKeySpec(KEY.getBytes(),"DES");
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE,key,zeroIv);
			decryptedData = cipher.doFinal(byteMi);
			decryptString = new String(decryptedData);
			
			return byteToString(decryptString);
		}catch(Exception e){
		}
		return null;
	}

	public static String StringToByte(String str){
		String byteString = "";
		if(("".equalsIgnoreCase(str)) || (str == null)){
			return "";
		}
		byte[] ret = null;
		try{
			ret = str.getBytes("utf-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		for(int i = 0;i < ret.length;i++){
			if(i + 1 == ret.length)
				byteString += ret[i];
			else
				byteString += ret[i] + ",";
		}
		return byteString;
	}

	public static String byteToString(String Str) throws Exception{
		String[] array = Str.replace("\r\n", "").split(",");
		byte[] b = new byte[array.length];
		for(int i = 0;i < array.length;i++){
			b[i] = Byte.valueOf(array[i]);
		}
		
		return new String(b,"utf-8");
	}
}
