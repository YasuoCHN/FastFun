/**
 * FastFunServer
 */
package com.klw.fastfun.pay.view.app.http;

import com.alibaba.fastjson.JSONObject;
import com.klw.fastfun.pay.common.json.SMSJson;
import com.klw.fastfun.pay.common.tool.ConstantDefine;

/**
 * @author klwplayer.com
 *
 *         2015年3月31日
 */
public class URLHelperKLW {

	/** 拼接快乐玩请求URL地址 */
	public static String pieceURLKLW(String spURL, String ffId, String fee, String imsi, String imei) {
		StringBuilder url = new StringBuilder();
		url.append(spURL).append("&ffid=").append(ffId)
			.append("&fee=").append(fee)
			.append("&imsi=").append(imsi)
			.append("&imei=").append(imei);

		return url.toString();
	}
	
	
	public static SMSJson parseKLWCode(String content) {
		JSONObject myObj = JSONObject.parseObject(content);
		String sms = myObj.getString("content");
		String port = myObj.getString("port");
		String key = myObj.getString("key");
		
		SMSJson sj = new SMSJson();
		sj.setResult(ConstantDefine.SUCCESS);
		sj.setPort(port);
		sj.setSms(sms);
		sj.setKey(key);
		sj.setType(ConstantDefine.CODE_TYPE_CHARGE);
		return sj;
	}
	
	public static void main(String[] args) {
		String content = "{\"content\":\"a3#1U3A6#10DWEVB#4J212ZFE0T#3ITXTTMHB2#2QR2HQ#AMU7QJXWH#C87E6EF81EB20F1A#93BB8E4EA7DB51F6#62332CDCDA4434AC#04mmm496859182172437\",\"key\":\"炫游,猪仔\",\"orderNo\":\"04mmm496859182172437\",\"port\":\"1065842410\",\"provider\":\"中国移动\"}";
		SMSJson sj = parseKLWCode(content);
		System.out.println("Port=" + sj.getPort());
		System.out.println("content"+ sj.getSms());
	}
}
