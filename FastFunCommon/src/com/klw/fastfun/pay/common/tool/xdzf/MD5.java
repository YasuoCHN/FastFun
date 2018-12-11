package com.klw.fastfun.pay.common.tool.xdzf;

import java.security.MessageDigest;

public class MD5 {
    /**
     * MD5方法
     * 
     * @param text 明文
     * @param charset 密钥
     * @return 密文
     * @throws Exception
     */
	public static String md5(String text, String charset) throws Exception {
        if(charset == null || charset.length()==0)
            charset = "UTF-8";

		byte[] bytes = text.getBytes(charset);
		
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.update(bytes);
		bytes = messageDigest.digest();
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < bytes.length; i ++)
		{
			if((bytes[i] & 0xff) < 0x10)
			{
				sb.append("0");
			}

			sb.append(Long.toString(bytes[i] & 0xff, 16));
		}
		
		return sb.toString().toLowerCase();
	}
	
	public static String md532(String text) throws Exception {
		 MessageDigest md = MessageDigest.getInstance("MD5");
         //获取文本明文为字节
         md.update(text.getBytes());
         //创建字节摘要数组
         byte b[] = md.digest();
         //创建 int 类型变量i
         int i;
         //创建StringBuffer容器
         StringBuffer buf = new StringBuffer("");
         for (int j = 0; j < b.length; j++) {    
             i = b[j];
             if (i < 0)                  
                 i += 256;   //md5加密最长32位字符.一个字符占8个字节.所以最长允许256个字节的字符串
             if (i < 16)     //一个字符=8个字节 0-15不足字符俩字符则补0拼接  
             buf.append("0"); 
             buf.append(Integer.toHexString(i));//int类型10进制转16进制                     
         }
         //32位加密
         return buf.toString();
	}
	
	/**
	 * MD5验证方法
	 * 
	 * @param text 明文
	 * @param charset 字符编码
	 * @param md5 密文
	 * @return true/false
	 * @throws Exception
	 */
	public static boolean verify(String text, String charset, String md5) throws Exception {
		String md5Text = md5(text, charset);
		if(md5Text.equalsIgnoreCase(md5))
		{
			return true;
		}

			return false;
	}
}