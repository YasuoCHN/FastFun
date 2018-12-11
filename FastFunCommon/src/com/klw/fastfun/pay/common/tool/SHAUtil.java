package com.klw.fastfun.pay.common.tool;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;



/**
 * JAVA android 通用的 AES加密解密算法
 * 
 * @author lch
 * 
 */
public class SHAUtil {

	public static final String HMAC_SHA1 = "HmacSHA1";
	public static final String UTF8 = "UTF-8";
	/**
	 * 通过源串和openKey进行HmacSHA1
	 */
	public static byte[] getHmacSHA1(String source, String openKey) {
	try {
			byte[] data = source.getBytes(UTF8);
			byte[] key = (openKey + "&").getBytes(UTF8);
			SecretKeySpec sigKey = new SecretKeySpec(key, HMAC_SHA1);
			Mac mac;
			mac = Mac.getInstance(HMAC_SHA1);
			mac.init(sigKey);
			return mac.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * HMAC-SHA1加密字节数组生成BASE64字符串
	 */
	public static String getSign(byte[] hmac) {
		String sign = Base64.encode(hmac);
		return sign;
	}


}
