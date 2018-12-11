package com.klw.fastfun.pay.view.app.other.mrutil;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;


public class RSA
{
	private static final String  SIGN_ALGORITHMS = "MD5WithRSA";

	/**
	 * RSA验签名检查
	 * @param content 待签名数据
	 * @param sign 签名值
	 * @param jft_pub_key  捷付通公钥
	 * @param input_charset 编码格式
	 * @return 布尔值
	 */
	public static String str;
	public static boolean verify(String content, String sign, String jft_pub_key, String input_charset)
	{
		try
		{
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decode(jft_pub_key);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));


			Signature signature = Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initVerify(pubKey);
			signature.update( content.getBytes(input_charset) );

			return signature.verify( Base64.decode(sign) );

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * RSA签名
	 * @param content 待签名数据
	 * @param privateKey 商户私钥
	 * @param input_charset 编码格式
	 * @return 签名值
	 */
	public static String sign(String content, String privateKey, String input_charset)
	{
		try
		{
			PKCS8EncodedKeySpec priPKCS8 	= new PKCS8EncodedKeySpec( Base64.decode(privateKey) );
			KeyFactory          keyf        = KeyFactory.getInstance("RSA");
			PrivateKey          priKey 		= keyf.generatePrivate(priPKCS8);
			Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
			signature.initSign(priKey);
			signature.update( content.getBytes(input_charset) );
			byte[] signed = signature.sign();
			return Base64.encode(signed);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}


	public static String md5s(String plainText) {
		String buff = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			buff =buf.toString();
			Base64.encode(buff.getBytes());
			System.out.println("base64:"+ Base64.encode(buff.getBytes()));
			str = buf.toString();
			System.out.println("result: " + buf.toString());// 32位的加密
			System.out.println("result: " + buf.toString().substring(8, 24));// 16位的加密
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return Base64.encode(buff.getBytes());




	}

	/**
	 * 使用公钥对明文进行加密，返回BASE64编码的字符串
	 * @param jft_pub_key
	 * @param plainText
	 * @return
	 */
	public static String encrypt(String jft_pub_key,String plainText){
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//			byte[] encodedKey = org.apache.commons.codec.binary.Base64.decodeBase64(jft_pub_key);
			byte[] encodedKey = Base64.decode(jft_pub_key);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			byte[] enBytes = cipher.doFinal(plainText.getBytes());
//			return org.apache.commons.codec.binary.Base64.encodeBase64String(enBytes);
			return Base64.encode(enBytes);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
