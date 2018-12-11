package com.klw.fastfun.pay.common.tool;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * JAVA android 通用的 AES加密解密算法
 * 
 * @author lch
 * 
 */
public class AESUtil {

	private static final String ALGORITHM = "AES/ECB/PKCS5Padding";

	/**
	 * 加密
	 * 
	 * @param secretKey
	 *            密钥,密钥必须为16位
	 * @param content
	 *            待加密明文
	 * @return
	 */
	public static String encrypt(String secretKey, String content) {
		// 加密之后的字节数组,转成16进制的字符串形式输出
		byte[] result = null;
		try {
			byte[] rawkey = getKey(secretKey);
			result = encrypt(rawkey, content.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (result != null) {
			return parseByte2HexStr(result);
		}

		return "";

	}

	/**
	 * 解密
	 * 
	 * @param secretKey
	 *            密钥,密钥必须为16位
	 * @param encrypted
	 *            待解密密文
	 * @return
	 */
	public static String decrypt(String secretKey, String encrypted) {
		// 解密之前,先将输入的字符串按照16进制转成二进制的字节数组,作为待解密的内容输入
		try {
			byte[] rawkey = getKey(secretKey);
			byte[] byteEncrypted = parseHexStr2Byte(encrypted);
			byte[] result = decrypt(rawkey, byteEncrypted);
			return new String(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private static byte[] encrypt(byte[] keyStr, byte[] byteContent) {
		try {
			SecretKeySpec key = new SecretKeySpec(keyStr, "AES");
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] result = cipher.doFinal(byteContent);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static byte[] decrypt(byte[] keyStr, byte[] byteEncrypted) {
		try {
			SecretKeySpec key = new SecretKeySpec(keyStr, "AES");
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] result = cipher.doFinal(byteEncrypted);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static byte[] getKey(String secretKey) {
		byte[] rByte = null;
		if (secretKey != null) {
			rByte = secretKey.getBytes();
		} else {
			rByte = new byte[24];
		}
		return rByte;
	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		if (buf == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr == null || hexStr.length() < 1) {
			return null;
		}
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

}
