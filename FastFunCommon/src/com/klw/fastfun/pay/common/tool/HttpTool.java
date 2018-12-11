/**
 * KLWPayServer
 */
package com.klw.fastfun.pay.common.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.juice.orange.game.log.LoggerFactory;

/**
 * @author klwplayer.com
 *
 * 2014年10月31日
 */
public class HttpTool {
	private static Logger logger = LoggerFactory.getLogger(HttpTool.class);
	
	
	public static String sendKLWPostTest(String urlStr, byte[] bt, String charset, int timeout, String signature) {
		logger.info("sendKLWPost指令url=" + urlStr);
//		logger.info("sendKLWPost指令param=" + httpReq);
		StringBuffer temp = new StringBuffer();
		try {
			String _url = urlStr;
			URL url = new URL(_url);
			// 请求配置，可根据实际情况采用灵活配置
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setReadTimeout(timeout);
			connection.setConnectTimeout(timeout);
			// 请求的方法 Get or Post
			connection.setRequestMethod("POST");//POST
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			// 相关请求头
			connection.setRequestProperty("Authorization", signature);
			connection.setRequestProperty("Content-Length", bt.length+"");
			connection.setRequestProperty("Host", "lewan.oss-cn-hangzhou.aliyuncs.com");
//			connection.setRequestProperty("Date", "Thu, 17 Nov 2015 18:49:58 GMT");
//			connection.setRequestProperty("Charsert", charset);
//			connection.setRequestProperty("Charsert", charset);
//			connection.setRequestProperty("Charsert", charset);
			connection.setRequestProperty("Content-Type", "application/octet-stream");
//			connection.setRequestProperty("Content-MD5", "eB5eJF1ptWaXm4bijSPyxw==");
			// 写入请求实体
			connection.getOutputStream().write(bt);
			connection.getOutputStream().flush();
			connection.getOutputStream().close();
			// 进去连接
			connection.connect();
			// 响应
			InputStream in = connection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(in, charset));
			String line = bufferedReader.readLine();
			while (line != null) {
				temp.append(line);
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
			connection.disconnect();
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		logger.info("指令结果=" + temp.toString());
		return temp.toString();
	}
	/**
	 * QQ绑定安全中心
	 * 
	 */
	public static String sendQQPost(String urlStr, String httpReq,int timeout) {
		logger.info("sendQQPost指令url:" + urlStr);
		logger.info("sendQQPost指令param:" + httpReq);
		StringBuffer temp = new StringBuffer();
		String charset = "utf-8";
		try {
			String _url = urlStr;
			URL url = new URL(_url);
			// 请求配置，可根据实际情况采用灵活配置
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setReadTimeout(timeout);
			connection.setConnectTimeout(timeout);
			// 请求的方法 Get or Post
			connection.setRequestMethod("POST");//POST
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			// 相关请求头
			
//			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("X-Parse-Application-Id", "10001");
			connection.setRequestProperty("X-Parse-Session-Token", "r:45da23d1e4ba9bbdfb3c3e90249f3ae8");
			// 写入请求实体
			connection.getOutputStream().write(httpReq.getBytes());
			connection.getOutputStream().flush();
			connection.getOutputStream().close();
			// 进去连接
			connection.connect();
			// 响应
			InputStream in = connection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(in, charset));
			String line = bufferedReader.readLine();
			while (line != null) {
				temp.append(line);
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
			connection.disconnect();
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		logger.info("指令结果=" + temp.toString());
		return temp.toString();
	}
	public static String sendTLPost(String urlStr, String httpReq, String charset, int timeout) {
		logger.info("sendTLPost指令url:" + urlStr);
		logger.info("sendTLPost指令param:" + httpReq);
		StringBuffer temp = new StringBuffer();
		try {
			String _url = urlStr;
			URL url = new URL(_url);
			// 请求配置，可根据实际情况采用灵活配置
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setReadTimeout(timeout);
			connection.setConnectTimeout(timeout);
			// 请求的方法 Get or Post
			connection.setRequestMethod("POST");//POST
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			// 相关请求头
			connection.setRequestProperty("Charsert", charset);
//			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 写入请求实体
			connection.getOutputStream().write(httpReq.getBytes());
			connection.getOutputStream().flush();
			connection.getOutputStream().close();
			// 进去连接
			connection.connect();
			// 响应
			InputStream in = connection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(in, charset));
			String line = bufferedReader.readLine();
			while (line != null) {
				temp.append(line);
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
			connection.disconnect();
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		logger.info("指令结果=" + temp.toString());
		return temp.toString();
	}
	
	public static String sendKLWPost(String urlStr, String httpReq, String charset, int timeout) {
		logger.info("sendKLWPost指令url:" + urlStr);
		logger.info("sendKLWPost指令param:" + httpReq);
		StringBuffer temp = new StringBuffer();
		try {
			String _url = urlStr;
			URL url = new URL(_url);
			// 请求配置，可根据实际情况采用灵活配置
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setReadTimeout(timeout);
			connection.setConnectTimeout(timeout);
			// 请求的方法 Get or Post
			connection.setRequestMethod("POST");//POST
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			// 相关请求头
			connection.setRequestProperty("Charsert", charset);
			connection.setRequestProperty("Content-Type", "application/json");
			// 写入请求实体
			connection.getOutputStream().write(httpReq.getBytes());
			connection.getOutputStream().flush();
			connection.getOutputStream().close();
			// 进去连接
			connection.connect();
			// 响应
			InputStream in = connection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(in, charset));
			String line = bufferedReader.readLine();
			while (line != null) {
				temp.append(line);
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
			connection.disconnect();
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		logger.info("指令结果=" + temp.toString());
		return temp.toString();
	}
	/**
	 * webqq绑定
	 * @param urlStr
	 * @param httpReq
	 * @param charset
	 * @param timeout
	 * @return
	 */
	public static String sendWEBQQBDPost(String urlStr, String httpReq, String charset, int timeout) {
		logger.info("sendKLWPost指令url:" + urlStr);
		logger.info("sendKLWPost指令param:" + httpReq);
		StringBuffer temp = new StringBuffer();
		try {
			String _url = urlStr;
			URL url = new URL(_url);
			// 请求配置，可根据实际情况采用灵活配置
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setReadTimeout(timeout);
			connection.setConnectTimeout(timeout);
			// 请求的方法 Get or Post
			connection.setRequestMethod("POST");//POST
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			// 相关请求头
			connection.setRequestProperty("Charsert", charset);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("X-Parse-Application-Id", "10001");
			connection.setRequestProperty("X-Parse-Session-Token", "r:4427fbb8dc3f0192adca2e4b9a8e3155");
			// 写入请求实体
			connection.getOutputStream().write(httpReq.getBytes());
			connection.getOutputStream().flush();
			connection.getOutputStream().close();
			// 进去连接
			connection.connect();
			// 响应
			InputStream in = connection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(in, charset));
			String line = bufferedReader.readLine();
			while (line != null) {
				temp.append(line);
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
			connection.disconnect();
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		logger.info("指令结果=" + temp.toString());
		return temp.toString();
	}
	
	public static String sendYZWHPost(String urlStr, String httpReq, int timeout) {
		logger.info("sendYZWHPost指令url:" + urlStr);
		logger.info("sendYZWHPost指令param:" + httpReq);
		String charset = "utf-8";
		StringBuffer temp = new StringBuffer();
		try {
			String _url = urlStr;
			URL url = new URL(_url);
			// 请求配置，可根据实际情况采用灵活配置
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setReadTimeout(timeout);
			connection.setConnectTimeout(timeout);
			// 请求的方法 Get or Post
			connection.setRequestMethod("POST");//POST
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			// 相关请求头
			connection.setRequestProperty("Charsert", charset);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// 写入请求实体
			connection.getOutputStream().write(httpReq.getBytes());
			connection.getOutputStream().flush();
			connection.getOutputStream().close();
			// 进去连接
			connection.connect();
			// 响应
			InputStream in = connection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(in, charset));
			String line = bufferedReader.readLine();
			while (line != null) {
				temp.append(line);
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
			connection.disconnect();
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		logger.info("指令结果:" + temp.toString());
		return temp.toString();
	}
	public static String sendPost(String urlStr, int timeout) {
		logger.info("sendYZWHPost指令url:" + urlStr);
//		logger.info("sendYZWHPost指令param:" + httpReq);
		String charset = "utf-8";
		StringBuffer temp = new StringBuffer();
		try {
			String _url = urlStr;
			URL url = new URL(_url);
			// 请求配置，可根据实际情况采用灵活配置
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setReadTimeout(timeout);
			connection.setConnectTimeout(timeout);
			// 请求的方法 Get or Post
			connection.setRequestMethod("POST");//POST
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			// 相关请求头
			connection.setRequestProperty("Charsert", charset);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// 写入请求实体
//			connection.getOutputStream().write(httpReq.getBytes());
			connection.getOutputStream().flush();
			connection.getOutputStream().close();
			// 进去连接
			connection.connect();
			// 响应
			InputStream in = connection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(in, charset));
			String line = bufferedReader.readLine();
			while (line != null) {
				temp.append(line);
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
			connection.disconnect();
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		logger.info("指令结果:" + temp.toString());
		return temp.toString();
	}
	
	public static String sendHttp(String urlStr, String httpReq) {
		return sendHttp(urlStr, httpReq, "utf8");
	}
	
	public static String sendHttp(String urlStr, String httpReq, String charset) {
		logger.info("指令url=" + urlStr);
		StringBuffer temp = new StringBuffer();
		try {
			String _url = urlStr;
			URL url = new URL(_url);
			// 请求配置，可根据实际情况采用灵活配置
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setReadTimeout(1000 * 60);
			connection.setConnectTimeout(1000 * 60);
			// 请求的方法 Get or Post
			connection.setRequestMethod("POST");//POST
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			// 相关请求头
			connection.setRequestProperty("Charsert", charset);
			connection.setRequestProperty("Content-Type", "text/plain");
			// 写入请求实体
			connection.getOutputStream().write(httpReq.getBytes());
			connection.getOutputStream().flush();
			connection.getOutputStream().close();
			// 进去连接
			connection.connect();
			// 响应
			InputStream in = connection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(in, charset));
			String line = bufferedReader.readLine();
			while (line != null) {
				temp.append(line);
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
			connection.disconnect();
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		logger.info("指令结果=" + temp.toString());
		return temp.toString();
	}
	
	/** 沃勤特殊处理*/
	public static String sendHttp2(String addr, String content, String ip) {
		URL url;
		StringBuffer returnxml = new StringBuffer();
		try {
			url = new URL(addr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
			conn.setRequestProperty("referer", ip);
//			conn.setRequestProperty("host", ip);
//			conn.setRequestProperty("X-Forwarded-For", ip);
//			conn.setRequestProperty("X-Real-IP", ip);
//			conn.setRequestProperty("remote-host-c", ip);
			// String timeout = XmlTool.getInstance().getValue("timeout");
			String timeout = "3000";
			conn.setConnectTimeout(Integer.parseInt(timeout));
			conn.setReadTimeout(Integer.parseInt(timeout));
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.getOutputStream().write(content.getBytes("UTF-8"));
			conn.getOutputStream().flush();
			conn.getOutputStream().close();
			InputStream in = conn.getInputStream();
			InputStreamReader ireader = new InputStreamReader(in, "UTF-8");
			java.io.BufferedReader reader = new java.io.BufferedReader(ireader);
			String s = "";
			while ((s = reader.readLine()) != null) {
				returnxml.append(s + "\n");
			}
			reader.close();
		} catch (MalformedURLException e) {
			return "";
		} catch (IOException e) {
			return "";
		}
		return returnxml.toString().trim();
	}
	
	/** 沃勤特殊处理*/
	public static String sendHttp1(String addr, String content, String ip) {
		URL url;
		StringBuffer returnxml = new StringBuffer();
		try {
			url = new URL(addr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
			conn.setRequestProperty("remote-host-c", ip);
			// String timeout = XmlTool.getInstance().getValue("timeout");
			String timeout = "3000";
			conn.setConnectTimeout(Integer.parseInt(timeout));
			conn.setReadTimeout(Integer.parseInt(timeout));
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.getOutputStream().write(content.getBytes("UTF-8"));
			conn.getOutputStream().flush();
			conn.getOutputStream().close();
			InputStream in = conn.getInputStream();
			InputStreamReader ireader = new InputStreamReader(in, "UTF-8");
			java.io.BufferedReader reader = new java.io.BufferedReader(ireader);
			String s = "";
			while ((s = reader.readLine()) != null) {
				returnxml.append(s + "\n");
			}
			reader.close();
		} catch (MalformedURLException e) {
			return "";
		} catch (IOException e) {
			return "";
		}
		return returnxml.toString().trim();
	}
	
	/** 表单post提交*/
	public static String sendHttp2(String addr, String content) {
		URL url;
		StringBuffer returnxml = new StringBuffer();
		try {
			url = new URL(addr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
			// String timeout = XmlTool.getInstance().getValue("timeout");
			String timeout = "3000";
			conn.setConnectTimeout(Integer.parseInt(timeout));
			conn.setReadTimeout(Integer.parseInt(timeout));
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.getOutputStream().write(content.getBytes("UTF-8"));
			conn.getOutputStream().flush();
			conn.getOutputStream().close();
			InputStream in = conn.getInputStream();
			InputStreamReader ireader = new InputStreamReader(in, "UTF-8");
			java.io.BufferedReader reader = new java.io.BufferedReader(ireader);
			String s = "";
			while ((s = reader.readLine()) != null) {
				returnxml.append(s + "\n");
			}
			reader.close();
		} catch (MalformedURLException e) {
			return "";
		} catch (IOException e) {
			return "";
		}
		return returnxml.toString().trim();
	}
	
	/** 表单post提交*/
	public static String sendFormGet(String addr, String content, int timeout) {
		URL url;
		StringBuffer returnxml = new StringBuffer();
		logger.info("sendFormGet指令url:" + addr);
    	logger.info("sendFormGet同步内容:" + content);
		try {
			url = new URL(addr + "?" + content);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
			// String timeout = XmlTool.getInstance().getValue("timeout");
			conn.setConnectTimeout(timeout);
			conn.setReadTimeout(timeout);
//			conn.setDoOutput(true);
//			conn.setDoInput(true);
			conn.setUseCaches(false);
			/*conn.getOutputStream().write(content.getBytes("UTF-8"));
			conn.getOutputStream().flush();
			conn.getOutputStream().close();*/
			InputStream in = conn.getInputStream();
			InputStreamReader ireader = new InputStreamReader(in, "UTF-8");
			java.io.BufferedReader reader = new java.io.BufferedReader(ireader);
			String s = "";
			while ((s = reader.readLine()) != null) {
				returnxml.append(s + "\n");
			}
			reader.close();
		} catch (MalformedURLException e) {
			return "";
		} catch (IOException e) {
			return "";
		}
		return returnxml.toString().trim();
	}
	
	public static String simpleSendHttp(String addr,String charset){    
        String result = "";    
        try {    
            URL url = new URL(addr);    
            URLConnection connection = url.openConnection();    
            connection.connect();    
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),charset));    
            String line;    
            while((line = reader.readLine())!= null){     
                result += line;    
                result += "\n";
            }
        } catch (Exception e) {    
            e.printStackTrace();    
            return "";
        }
        return result;
    }
	
	public static void main(String[] args) throws Exception {
		/*String url = "http://120.24.88.90/code/reqSpecialMMCode?appId=kk&model=MI 2";
		
		String _url = URLEncoder.encode(url, "utf-8");
		
		System.out.println(_url);*/
		String s = "dfagfggggrgdrgsgsdfg";
		System.out.println(s.substring(0,16));
	}
	
	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是json的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		return sendPost(url,param,"6000");
	} 
	
	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是表单application/x-www-form-urlencoded的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost2(String url, String param, String timeout) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		logger.info("指令url=" + url);
		logger.info("同步内容=" + param);
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setConnectTimeout(Integer.parseInt(timeout));
			conn.setReadTimeout(Integer.parseInt(timeout));
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		//使用finally块来关闭输出流、输入流
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return result;
	}
	public static String sendPost(String url, String param, String timeout) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		logger.info("指令url=" + url);
		logger.info("同步内容=" + param);
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setConnectTimeout(Integer.parseInt(timeout));
			conn.setReadTimeout(Integer.parseInt(timeout));
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		//使用finally块来关闭输出流、输入流
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是表单application/x-www-form-urlencoded的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param, String charset, int timeout) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		logger.info("指令url=" + url);
		logger.info("同步内容=" + param);
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setConnectTimeout(timeout);
			conn.setReadTimeout(timeout);
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			
			conn.setRequestProperty("Charsert", charset);
			
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream(), charset));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		//使用finally块来关闭输出流、输入流
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return result;
	}
	
	/**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数应该是表单application/x-www-form-urlencoded的形式。
     * @param AESSecret 密钥
     * @param timeout 超时时间
     * @return 所代表远程资源的响应结果
     */
    public static String sendAESPost(String url, String param, String AESSecret, String timeout) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        logger.info("指令url=" + url);
        logger.info("同步内容=" + param);
        param = AESUtil.encrypt(AESSecret, param) + AESSecret;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setConnectTimeout(Integer.parseInt(timeout));
			conn.setReadTimeout(Integer.parseInt(timeout));
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    } 
    
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
    	String result = "";
    	BufferedReader in = null;
    	logger.info("指令url=" + url);
    	logger.info("同步内容=" + param);
    	try {
    		String urlNameString = url + "?" + param;
    		URL realUrl = new URL(urlNameString);
    		// 打开和URL之间的连接
    		URLConnection connection = realUrl.openConnection();
    		// 设置通用的请求属性
    		connection.setRequestProperty("accept", "*/*");
    		connection.setRequestProperty("connection", "Keep-Alive");
    		connection.setRequestProperty("user-agent",
    				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
    		String timeout = "5000";
    		connection.setConnectTimeout(Integer.parseInt(timeout));
    		connection.setReadTimeout(Integer.parseInt(timeout));
    		// 建立实际的连接
    		connection.connect();
    		// 获取所有响应头字段
    		Map<String, List<String>> map = connection.getHeaderFields();
    		// 遍历所有的响应头字段
    		for (String key : map.keySet()) {
    			System.out.println(key + "--->" + map.get(key));
    		}
    		// 定义 BufferedReader输入流来读取URL的响应
    		in = new BufferedReader(new InputStreamReader(
    				connection.getInputStream()));
    		String line;
    		while ((line = in.readLine()) != null) {
    			result += line;
    		}
    	} catch (Exception e) {
    		System.out.println("发送GET请求出现异常！" + e);
    		e.printStackTrace();
    	}
    	// 使用finally块来关闭输入流
    	finally {
    		try {
    			if (in != null) {
    				in.close();
    			}
    		} catch (Exception e2) {
    			e2.printStackTrace();
    		}
    	}
    	return result;
    }
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGets(String url, String param) {
    	String result = "";
    	BufferedReader in = null;
    	logger.info("指令url=" + url);
    	logger.info("同步内容=" + param);
    	try {
    		String urlNameString = url + "?" + param;
    		URL realUrl = new URL(urlNameString);
    		// 打开和URL之间的连接
    		URLConnection connection = realUrl.openConnection();
    		// 设置通用的请求属性
    		
    		connection.setRequestProperty("HTTP/1.0","200 OK");
    		connection.setRequestProperty("Content-Type", "text/html");
//    		connection.setRequestProperty("accept", "*/*");
    		connection.setRequestProperty("connection", "false");
//    		connection.setRequestProperty("user-agent",
//    				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
    		String timeout = "5000";
    		connection.setConnectTimeout(Integer.parseInt(timeout));
    		connection.setReadTimeout(Integer.parseInt(timeout));
    		// 建立实际的连接
    		connection.connect();
    		// 获取所有响应头字段
    		Map<String, List<String>> map = connection.getHeaderFields();
    		// 遍历所有的响应头字段
    		for (String key : map.keySet()) {
    			System.out.println(key + "--->" + map.get(key));
    		}
    		// 定义 BufferedReader输入流来读取URL的响应
    		in = new BufferedReader(new InputStreamReader(
    				connection.getInputStream()));
    		String line;
    		while ((line = in.readLine()) != null) {
    			result += line;
    		}
    	} catch (Exception e) {
    		System.out.println("发送GET请求出现异常！" + e);
    		e.printStackTrace();
    	}
    	// 使用finally块来关闭输入流
    	finally {
    		try {
    			if (in != null) {
    				in.close();
    			}
    		} catch (Exception e2) {
    			e2.printStackTrace();
    		}
    	}
    	return result;
    }
    
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGetSetTime(String url,  String timeout) {
    	String result = "";
    	BufferedReader in = null;
    	logger.info("指令url:" + url);
    	try {
    		URL realUrl = new URL(url);
    		// 打开和URL之间的连接
    		URLConnection connection = realUrl.openConnection();
    		// 设置通用的请求属性
    		connection.setRequestProperty("accept", "*/*");
    		connection.setRequestProperty("connection", "Keep-Alive");
    		connection.setRequestProperty("user-agent",
    				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
    		connection.setConnectTimeout(Integer.parseInt(timeout));
    		connection.setReadTimeout(Integer.parseInt(timeout));
    		// 建立实际的连接
    		connection.connect();
    		// 获取所有响应头字段
    		Map<String, List<String>> map = connection.getHeaderFields();
    		// 遍历所有的响应头字段
    		for (String key : map.keySet()) {
    			System.out.println(key + "--->" + map.get(key));
    		}
    		// 定义 BufferedReader输入流来读取URL的响应
    		in = new BufferedReader(new InputStreamReader(
    				connection.getInputStream()));
    		String line;
    		while ((line = in.readLine()) != null) {
    			result += line;
    		}
    	} catch (Exception e) {
    		System.out.println("发送GET请求出现异常！" + e);
    		e.printStackTrace();
    	}
    	// 使用finally块来关闭输入流
    	finally {
    		try {
    			if (in != null) {
    				in.close();
    			}
    		} catch (Exception e2) {
    			e2.printStackTrace();
    		}
    	}
    	return result;
    }
    
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGetSetTimeout(String url, String param, String timeout) {
    	String result = "";
    	BufferedReader in = null;
    	logger.info("指令url:" + url);
    	logger.info("同步内容:" + param);
    	try {
    		String urlNameString = url + "?" + param;
    		URL realUrl = new URL(urlNameString);
    		// 打开和URL之间的连接
    		URLConnection connection = realUrl.openConnection();
    		// 设置通用的请求属性
    		connection.setRequestProperty("accept", "*/*");
    		connection.setRequestProperty("connection", "Keep-Alive");
    		connection.setRequestProperty("user-agent",
    				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
    		connection.setConnectTimeout(Integer.parseInt(timeout));
    		connection.setReadTimeout(Integer.parseInt(timeout));
    		// 建立实际的连接
    		connection.connect();
    		/*// 获取所有响应头字段
    		Map<String, List<String>> map = connection.getHeaderFields();
    		// 遍历所有的响应头字段
    		for (String key : map.keySet()) {
    			System.out.println(key + "--->" + map.get(key));
    		}*/
    		// 定义 BufferedReader输入流来读取URL的响应
    		in = new BufferedReader(new InputStreamReader(
    				connection.getInputStream()));
    		String line;
    		while ((line = in.readLine()) != null) {
    			result += line;
    		}
    	} catch (Exception e) {
    		System.out.println("发送GET请求出现异常！" + e);
    		e.printStackTrace();
    	}
    	// 使用finally块来关闭输入流
    	finally {
    		try {
    			if (in != null) {
    				in.close();
    			}
    		} catch (Exception e2) {
    			e2.printStackTrace();
    		}
    	}
    	return result;
    }
    
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGetSetTimeout(String url, String param, String timeout, String charset) {
    	String result = "";
    	BufferedReader in = null;
    	logger.info("指令url:" + url);
    	logger.info("同步内容:" + param);
    	try {
    		String urlNameString = url + "?" + param;
    		URL realUrl = new URL(urlNameString);
    		// 打开和URL之间的连接
    		URLConnection connection = realUrl.openConnection();
    		// 设置通用的请求属性
    		connection.setRequestProperty("accept", "*/*");
    		connection.setRequestProperty("connection", "Keep-Alive");
    		connection.setRequestProperty("user-agent",
    				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
    		connection.setConnectTimeout(Integer.parseInt(timeout));
    		connection.setReadTimeout(Integer.parseInt(timeout));
    		// 建立实际的连接
    		connection.connect();
    		/*// 获取所有响应头字段
    		Map<String, List<String>> map = connection.getHeaderFields();
    		// 遍历所有的响应头字段
    		for (String key : map.keySet()) {
    			System.out.println(key + "--->" + map.get(key));
    		}*/
    		// 定义 BufferedReader输入流来读取URL的响应
    		in = new BufferedReader(new InputStreamReader(
    				connection.getInputStream(),charset));
    		String line;
    		while ((line = in.readLine()) != null) {
    			result += line;
    		}
    	} catch (Exception e) {
    		System.out.println("发送GET请求出现异常！" + e);
    		e.printStackTrace();
    	}
    	// 使用finally块来关闭输入流
    	finally {
    		try {
    			if (in != null) {
    				in.close();
    			}
    		} catch (Exception e2) {
    			e2.printStackTrace();
    		}
    	}
    	return result;
    }
    
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet1(String url) {
        String result = "";
        BufferedReader in = null;
        logger.info("指令url=" + url);
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            String timeout = "3000";
            connection.setConnectTimeout(Integer.parseInt(timeout));
            connection.setReadTimeout(Integer.parseInt(timeout));
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
}
