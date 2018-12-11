/**
 * ChinaMobileMarket
 * com.orange.game.china.mobile.market.utils
 * XMLUtils.java
 */
package com.klw.fastfun.pay.common.tool.mm;

import java.io.ByteArrayInputStream;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @author shaojieque
 * 
 *         2013-12-6
 */
public class XMLUtils {
	private static XMLUtils xml;

	private XMLUtils() {

	}

	public static XMLUtils getInstance() {
		if (xml == null) {
			xml = new XMLUtils();
		}
		return xml;
	}
	
	public Element parseXML2Element(String xmlStr) throws Exception{
		ByteArrayInputStream byteIs = new ByteArrayInputStream(
				xmlStr.getBytes());
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(byteIs);
		Element root = doc.getRootElement();
		return root;
	}

	public SyncAppOrderReq parseXML2AppOrderReq(String xmlStr) throws Exception{
		SyncAppOrderReq req = new SyncAppOrderReq();
		ByteArrayInputStream byteIs = new ByteArrayInputStream(
				xmlStr.getBytes());
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(byteIs);
		Element root = doc.getRootElement();
		//
		req.setTransactionID(root.element("TransactionID").getText());
		req.setMsgType(root.element("MsgType").getText());
		req.setVersion(root.element("Version").getText());
		req.setOrderID(root.element("OrderID").getText());
		req.setCheckID(Integer.parseInt(root.element("CheckID").getText()));
		req.setActionTime(root.element("ActionTime").getText());
		req.setActionID(Integer.parseInt(root.element("ActionID").getText()));
		req.setMSISDN(root.element("MSISDN").getText());
		req.setFeeMSISDN(root.element("FeeMSISDN").getText());
		req.setAppID(root.element("AppID").getText());
		req.setPayCode(root.element("PayCode").getText());
 		req.setTradeID(root.element("TradeID").getText());
		req.setPrice(Integer.parseInt(root.element("Price").getText()));
		req.setTotalPrice(Integer.parseInt(root.element("TotalPrice").getText()));
		req.setSubsNumb(Integer.parseInt(root.element("SubsNumb").getText()));
		req.setSubsSeq(Integer.parseInt(root.element("SubsSeq").getText()));
		req.setChannelID(root.element("ChannelID").getText());
		
		req.setExData(root.element("ExData").getText());
		String orderType = root.element("OrderType").getText();
		if (orderType == null || orderType.length() <= 0)
			orderType = "0";
		req.setOrderType(Integer.parseInt(orderType));
		req.setMD5Sign(root.element("MD5Sign").getText());
//		req.setOrderPayment(Integer.parseInt(root.element("OrderPayment").getText()));
		if (root.element("ReturnStatus") != null)
			req.setReturnStatus(root.element("ReturnStatus").getText());
		return req;
	}

	private String getXMLStr() {
		StringBuilder sql = new StringBuilder();
		sql.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sql.append("<SyncAppOrderReq xmlns=\"http://www.monternet.com/dsmp/schemas/\">");
		sql.append("<TransactionID>CSSP19626560</TransactionID>");
		sql.append("<MsgType>SyncAppOrderReq</MsgType>");
		sql.append("<Version>1.0.0</Version>");
		sql.append("<Send_Address>");
		sql.append("<DeviceType>200</DeviceType>");
		sql.append("<DeviceID>CSSP</DeviceID>");
		sql.append("</Send_Address>");
		sql.append("<Dest_Address>");
		sql.append("<DeviceType>1002</DeviceType>");
		sql.append("<DeviceID>f0_0</DeviceID>");
		sql.append("</Dest_Address>");
		sql.append("<OrderID>11131206000001534798</OrderID>");
		sql.append("<CheckID>550816010</CheckID>");
		sql.append("<ActionTime>20131206000001</ActionTime>");
		sql.append("<ActionID>1</ActionID>");
		sql.append("<MSISDN></MSISDN>");
		sql.append("<FeeMSISDN>AFA07EB7AFED236A</FeeMSISDN>");
		sql.append("<AppID>300007098351</AppID>");
		sql.append("<PayCode>30000709835102</PayCode>");
		sql.append("<TradeID>D31833DFF7FA8EA9D9892D07D930A059</TradeID>");
		sql.append("<Price>200</Price>");
		sql.append("<TotalPrice>200</TotalPrice>");
		sql.append("<SubsNumb>1</SubsNumb>");
		sql.append("<SubsSeq>1</SubsSeq>");
		sql.append("<ChannelID>0000000000</ChannelID>");
		sql.append("<ExData>fkzjh_302584_301_1386259196049_KGYY001</ExData>");
		sql.append("<OrderType>1</OrderType>");
		sql.append("<MD5Sign>40C51DC5BCE6B8BF26EF7131E10CF0F0</MD5Sign>");
		sql.append("<OrderPayment>1</OrderPayment>");
		sql.append("</SyncAppOrderReq>");
		return sql.toString();
	}
	
	public static void main(String[] args) throws Exception {
	/*	System.out.println(
				getInstance().parseXML2AppOrderReq(
						getInstance().getXMLStr()).toString());*/
		System.out.println(getInstance().parseXML2Element(
						getInstance().getXMLStr()).element("Dest_Address").asXML());
	}
}