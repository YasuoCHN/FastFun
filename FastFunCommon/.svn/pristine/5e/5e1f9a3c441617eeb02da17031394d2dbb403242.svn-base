/**
 * ChinaMobileMarket
 * com.orange.game.china.mobile.market.utils
 * XStreamFactory.java
 */
package com.klw.fastfun.pay.common.tool.mm;

import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.xstream.XStream;

/**
 * 一个类型的消息请求始终只用到一个xstream对象
 * 
 * @author shaojieque 
 * 2013-10-23
 */
public class XStreamFactory {
	private static XStreamFactory xStreamFactory = null;
	private Map<String, XStream> map = new HashMap<String, XStream>();

	private XStreamFactory() {

	}

	public static XStreamFactory getInstance() {
		if (xStreamFactory == null) {
			xStreamFactory = new XStreamFactory();
		}
		return xStreamFactory;
	}

	/**
	 * 从MAP中获取XStream对象
	 * 
	 * @param type
	 * @return
	 */
	public XStream createXStream(String type) {
		if (type == null || type.length() == 0) {
			type = "default";
		}
		//
		XStream xStream = (XStream) map.get(type);
		if (xStream == null) {
			xStream = new XStream();
			map.put(type, xStream);
		}
		return xStream;
	}
}
