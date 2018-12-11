package com.klw.fastfun.pay.view.app.other.mrutil;


public class SignHelper
{
	// 字符编码格式 ，目前支持  utf-8
	public static String input_charset = "utf-8";
	
	public static boolean verify(String content, String sign, String pubKey)
	{
		// 目前版本，只支持RSA
		return RSA.verify(content, sign, pubKey, input_charset);
	}
	
	public static String sign(String content, String privateKey)
	{
		return RSA.sign(content, privateKey, input_charset);
	}
	
	public static String md5(String s){
		
		return RSA.md5s(s);
		
	}
	
	public static String privateKeySign(String privateKeyStr, String str) {
		String sign = "";
		try {
			/*PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(
					org.apache.commons.codec.binary.Base64.decodeBase64(privateKeyStr.getBytes("utf-8")));
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
	        Signature signature = Signature.getInstance("MD5withRSA");
	        signature.initSign(privateK);
	        signature.update(str.getBytes());
	        sign = org.apache.commons.codec.binary.Base64.encodeBase64String(signature.sign());*/
		}catch(Exception e) {
			System.out.println(e);
		}
        
        return sign;
    }
}
