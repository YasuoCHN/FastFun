package com.klw.fastfun.pay.common.tool;

import java.util.Arrays;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class StringUtils {
	
	private static final String ALGORITHM = "RSA";

	private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	private static final String DEFAULT_CHARSET = "UTF-8";
	

	/**/
	public static String bytesToHex(byte[] data) {
		final char hexArray[] = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[data.length * 2];
		for (int j = 0; j < data.length; j++) {
			int v = data[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
	
	public static String subAddfourth(int num){
		String str  = num+"";
		if(str.length()<4)
			while(str.length()<4)
				str = "0"+str;
		else{
			str ="0000"; 
		}
		return str;
	}
	
	
	public static String sign(String content, String privateKey) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(DEFAULT_CHARSET));

			byte[] signed = signature.sign();

			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static void main(String[] args) {
		String s = "E29B0C069BCD4031E233592CDE4034A56C784D06B55A264E39AB319FD6252D36A3D5B4B26AB059C521A143AC3546AC3391B9E82A4D9CCFC86AE76B767BD1CAEE629A3A9E65E3E67651CCCCA9A9B9B8ED35BB435F3266568F87DBAE52656D35D48FEF4D62109F7C6BB5C473AF1793CD6835DB0D9783C564335ACD06";
		byte[] b = hexStringToBytes(s);
		System.out.println(Arrays.toString(b));
		for (byte a : b) {
			System.out.print(a);
		}
	}
}
