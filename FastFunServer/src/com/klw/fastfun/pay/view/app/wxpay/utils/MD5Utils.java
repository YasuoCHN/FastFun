package com.klw.fastfun.pay.view.app.wxpay.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.klw.fastfun.pay.view.app.other.mrutil.Base64;

/**
 *
 * 此类是MD5加密算法的实现， 采用了java内置算法，需要引用java.security.MessageDigest
 *
 * @author airland.congs
 * @version $Revision: 1.1 $
 *
 */
public class MD5Utils {
	private static MessageDigest MD5 = null;
	 
    static {
        try {
            MD5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            
        }
    }
	public static void main(String[] args) {
		//String content = encode("accNo=6210817200019488012&accessMode=01&accessType=0&backUrl=http://192.168.11.212:8780/payTest/CallBackNotifyServlet&body=测试&currency=CNY&customerinfo={\"certifTp\":\"01\",\"certify_id\":\"511602199501276396\",\"customerNm\":\"杨用法\",\"phoneNo\":\"15989858715\"}&instNo=10000052&merId=100000520000024&merOrderId=02sdy58581615346&payType=0002&subject=测试&txnAmt=1000&txnSubType=01&txnTime=20180205180836&txnType=01&version=1.0.0f6fd2f3d3387499a93388329a863c681");
		String content = getMd524("aa");
		System.out.println(content);
	}
	public static String getMd524(String value) {
        String result = "";
        if (value == null) {
            return result;
        }
        try {
            result = Base64.encode(MD5.digest(value.getBytes("utf-8")));
        } catch (Exception ex) {
        }
        return result;
    }
	// 小写的字符串
	private static char[] DigitLower = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	// 大写的字符串
	private static char[] DigitUpper = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * 默认构造函数
	 *
	 */
	public MD5Utils() {
	}

	/**
	 * 加密之后的字符串全为小写
	 *
	 * @param srcStr
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NullPointerException
	 */
	public static String getMD5Lower(String srcStr)
			throws NoSuchAlgorithmException {
		String sign = "lower";
		return processStr(srcStr, sign);
	}

	/**
	 * 加密之后的字符串全为大写
	 *
	 * @param srcStr
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NullPointerException
	 */
	public static String getMD5Upper(String srcStr)
			throws NoSuchAlgorithmException {
		String sign = "upper";
		return processStr(srcStr, sign);
	}

	private static String processStr(String srcStr, String sign)
			throws NoSuchAlgorithmException, NullPointerException {
		MessageDigest digest;
		// 定义调用的方法
		String algorithm = "MD5";
		// 结果字符串
		String result = "";
		// 初始化并开始进行计算
		digest = MessageDigest.getInstance(algorithm);
		digest.update(srcStr.getBytes());
		byte[] byteRes = digest.digest();

		// 计算byte数组的长度
		int length = byteRes.length;

		// 将byte数组转换成字符串
		for (int i = 0; i < length; i++) {
			result = result + byteHEX(byteRes[i], sign);
		}

		return result;
	}

	/**
	 * 将btye数组转换成字符串
	 *
	 * @param bt
	 * @return
	 */
	private static String byteHEX(byte bt, String sign) {

		char[] temp = null;
		if (sign.equalsIgnoreCase("lower")) {
			temp = DigitLower;
		} else if (sign.equalsIgnoreCase("upper")) {
			temp = DigitUpper;
		} else {
			throw new java.lang.RuntimeException("加密缺少必要的条件");
		}
		char[] ob = new char[2];

		ob[0] = temp[(bt >>> 4) & 0X0F];

		ob[1] = temp[bt & 0X0F];

		return new String(ob);
	}

	 public static String getMD5(String content) {
	        MessageDigest messageDigest = null;
	        try {
	            messageDigest = MessageDigest.getInstance("MD5");
	            messageDigest.reset();
	            messageDigest.update(content.getBytes("UTF-8"));
	        } catch (Exception e) {
	        	e.printStackTrace();
	        } 

	        byte[] byteArray = messageDigest.digest();

	        StringBuffer md5StrBuff = new StringBuffer();

	        for (int i = 0; i < byteArray.length; i++) {
	            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
	                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
	            else
	                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
	        }

	        return md5StrBuff.toString();
	    }



}
