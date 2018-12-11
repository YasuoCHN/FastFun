package com.klw.fastfun.pay.view.app.other.util;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

import org.apache.http.util.TextUtils;

import com.klw.fastfun.pay.common.tool.Base64;


public class RSAUtil {
	/** */
	/**
	 * 加密算法RSA
	 */
	public static final String KEY_ALGORITHM = "RSA";

	/** */
	/**
	 * 签名算法
	 */
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";



	/** */
	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;
	public static void main(String[] args) {
		String data="{\"ACCNAME\":\"杨用法\",\"ACCNO\":\"6214857809885595\",\"ACCTYPE\":\"0\",\"AMT\":-100,\"BANKNAME\":\"招商银行\",\"BUSITYPE\":\"001\",\"CMDID\":\"4097\",\"MEMO\":\"测试\",\"MERNO\":\"001440153997292\",\"MOBILE\":\"17199927756\",\"ORDERNO\":\"2018012615163705\",\"REQTIME\":\"20180126114146\",\"VERSION\":\"1.0\"}";
		String publicKeyUrl="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDfCEEXAn1RpnSOOrWix3VLBoBfrPpdxoVkosuW1f52Yv/1ENBlKnoefLJPDwbsR5F0OcDhiBojbhx5fU8Gv52csJB4r7yjWvp70hsrXrh7UhulUdA0Rs8gOF8gWQIEsCIw8LRwdPAKENeqvdnSJeq5+qjPP5knoWH32bB1C9vclwIDAQAB";
		System.out.println(encryPublicData(data, publicKeyUrl));
	}

    private static String RSA = "RSA";
    private static int MAX_ENCRYPT_BLOCK=117;
    /**
     * 公钥加密-分段加密
     * 通过字符串获取公钥
     * 每次加密的字节数，不能超过密钥的长度值减去11
     * @param data 明文
     * @param publicKeyUrl 公钥字符串
     * @return 加密后的密文 base64转码
     */
    public static String encryPublicData(String data,String publicKeyUrl ){
        try{
            if (TextUtils.isEmpty(data)|| TextUtils.isEmpty(publicKeyUrl)){
                return null;
            }
            // 从字符串中得到公钥
            PublicKey publicKey = loadPublicKey(publicKeyUrl);
            return encryPublicData(publicKey,data);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    /**
     * 从字符串中加载公钥
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    private static PublicKey loadPublicKey(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 公钥加密
     * @param publicKey 公钥
     * @param eData 需要机密数据
     * @throws Exception
     * @return 密文 base64转码后的密文
     */
    private static String encryPublicData(PublicKey publicKey, String eData) throws Exception{
        byte[] data=eData.getBytes();
        Cipher cipher = Cipher.getInstance(RSA);
        // 编码前设定编码方式及密钥
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        /*int blockSize = cipher.getBlockSize();
        int length = data.length;
        int outputSize = cipher.getOutputSize(length);//获得加密块加密后块大小
        int leavedSize = length % blockSize;
        int blocksSize = leavedSize != 0 ? length / blockSize + 1 : length / blockSize;
        byte[] raw = new byte[outputSize * blocksSize];
        int i = 0;
        while (length - i * blockSize > 0) {
            if (length - i * blockSize > blockSize){
                cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize);
            } else{
                cipher.doFinal(data, i * blockSize, length - i * blockSize, raw, i * outputSize);
            }
            i++;
        }
        return Base64Utils.encode(raw);*/

        /*int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return Base64.encodeToString(encryptedData,Base64.DEFAULT);*/

        int inputLen = data.length;
        List<Byte> output = new ArrayList<Byte>();
        for (int i =0;i<inputLen;i+= MAX_ENCRYPT_BLOCK){
            int size = inputLen-i>MAX_ENCRYPT_BLOCK?MAX_ENCRYPT_BLOCK:inputLen-i;
            byte [] cache = new byte[size];
            System.arraycopy(data,i,cache,0,size);
            byte [] pack = cipher.doFinal(cache);
            for (Byte b:pack){
                output.add(b);
            }
        }
        byte [] result = new byte[output.size()];
        for (int i = 0;i<output.size();i++){
            result[i] = output.get(i);
        }
        return Base64.encode(result);
        //return  Base64.encodeToString(result,Base64.DEFAULT);
    }
	public static byte[] decryptByPrivateKey(byte[] encryptedData,
			String privateKey) throws Exception {
		byte[] keyBytes = Base64.decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());//
		cipher.init(Cipher.DECRYPT_MODE, privateK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher
						.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher
						.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;
	}
}
