/**
 * ChinaMobileMarket
 * com.orange.game.china.mobile.market.utils
 * SyncXMLUtils.java
 */
package com.klw.fastfun.pay.common.tool.mm;

import com.thoughtworks.xstream.XStream;


/**
 * @author shaojieque 
 * 2013-10-23
 */
public class SyncXMLUtils {
	/** 标准的XML 头部信息 */
	public final static String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	/**
	 * 生成请求消息体格式
	 * 
	 * @param obj
	 *            封装响应对象, rootName xml根节点名
	 * @throws Exception
	 */
	public String vo2Xml(Object obj, String rootName) throws Exception {
		String voXML = "";
		try {
			// XStream stream = new XStream();
			XStream stream = XStreamFactory.getInstance().createXStream(
					rootName + "Response");
			stream.alias(rootName, obj.getClass());
			String xml = stream.toXML(obj);
			xml = xml.replaceAll("&gt;", ">").replaceAll("&lt;", "<")
					.replaceAll("&amp;", "&");
			StringBuffer sb = new StringBuffer();
			sb.append(XML_HEAD);
			sb.append(xml);
			voXML = sb.toString();
			// logger.debug("生成请求消息体格式 : " + voXML);
		} catch (Exception e) {
			// logger.error("生成请求消息体格式失败！",e);
		}
		return voXML;
	}

	/**
	 * 根据返回的响应信息转换成VO
	 * 
	 * @param xml
	 * @param rootName
	 *            xml根节点名
	 * @param className
	 *            vo类
	 * @throws ClassNotFoundException
	 *             , Exception
	 * @throws Exception
	 */
	public Object xml2Vo(String xml, String rootName, String className)
			throws ClassNotFoundException, Exception {
		XStream stream = XStreamFactory.getInstance().createXStream(
				rootName + "Request");
		stream.alias(rootName, Class.forName(className));
		return stream.fromXML(xml);
	}

	/*public static void main(String ard[]) throws Exception{
		// 返回消息转为vo
		SyncAppOrderResp syncAppOrderResp = new SyncAppOrderResp();
		syncAppOrderResp.setMsgType("SyncAppOrderResp");
		syncAppOrderResp.setVersion("1.0.0");
		syncAppOrderResp.sethRet(0);
		SyncXMLUtils utils = new SyncXMLUtils();
		String result = utils.vo2Xml(syncAppOrderResp, "SyncAppOrderResp");
		System.out.println(result);
		// http发送请求消息，并接收返回消息例子：
		SyncXMLUtils utils = new SyncXMLUtils();
		try {
			// 组装消息体
			AppOrderQueryReq req = new AppOrderQueryReq();
			req.setMsgType("AppOrderQueryReq");
			req.setVersion("1.0.0");
			req.setAppType("0");
			req.setMSISDN("13426222299");
			req.setAppID("300001489323");
			req.setOsID("45");
			Address_Info_Schema schema = new Address_Info_Schema();
			schema.setDeviceID("OASS");
			schema.setDeviceType("500");
			req.setSend_Address(schema);
			Address_Info_Schema schema2 = new Address_Info_Schema();
			schema2.setDeviceID("css");
			schema2.setDeviceType("200");
			req.setDest_Address(schema2);
			String xml = utils.vo2Xml(req, "AppOrderQueryReq");
			System.out.println("vo2xml=" + xml);

			// 发送请求。
			String url = "http://10.1.3.22:8089/css/abs"; // 请求url地址
			String retxml = doPost(url, xml);// 发送请求，得到返回消息
			// 返回消息转为vo
			AppOrderQueryResp _resp = (AppOrderQueryResp) utils.xml2Vo(retxml,
					"AppOrderQueryResp", AppOrderQueryResp.class.getName());
			System.out.println("MsgType=============>" + _resp.gethRet());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String doPost(String url, String inxml) {

		PostMethod postMethod = new PostMethod(url);
		postMethod.setRequestBody(inxml);

		HttpClient httpClient = new HttpClient();
		postMethod.getParams().setParameter(
				HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		System.out.println("接口调用的URL是：" + url);
		System.out.println("接口调用的输入报文：" + inxml);

		try {
			httpClient.executeMethod(postMethod);
			int responseCode = postMethod.getStatusCode();
			System.out.println("postMethod.getStatusCode()=" + responseCode);
			if (postMethod.getStatusCode() == HttpStatus.SC_OK) {
				System.out.println("输出报文："
						+ postMethod.getResponseBodyAsString());
				String outstr = new String(postMethod.getResponseBody(),
						"UTF-8");// 这里设置编码的格式
				if ("".equals(outstr)) {
					outstr = null;
				}
				return outstr;
			} else {
				System.out
						.println("接口调用返回的状态码不正确.postMethod.getStatusCode() faile ");
			}

		} catch (Exception e) {
			System.out.println("接口调用响应失败" + e);

		} finally {
			postMethod.releaseConnection();
		}
		return null;
	}*/
}
