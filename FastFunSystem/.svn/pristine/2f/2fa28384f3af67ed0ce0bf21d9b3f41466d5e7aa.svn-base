package com.klw.fastfun.pay.data.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.juice.orange.game.log.LoggerFactory;
import com.klw.fastfun.pay.common.domain.PayCodeInfo;
import com.klw.fastfun.pay.common.domain.PayReqInfo;
import com.klw.fastfun.pay.common.tool.HttpTool;
import com.klw.fastfun.pay.common.tool.MD5Tool;

public class OnlineSMSTool {
	private static Logger logger = LoggerFactory.getLogger(OnlineSMSTool.class);
	
	public static PayCodeInfo getOnlineSMS2(PayCodeInfo payInfo, PayReqInfo info) {
		PayCodeInfo resultPayCode = new PayCodeInfo();
		
		
		try {
			
			String param = pieceURL(payInfo.getUrl(), info.getImei(), info.getImsi(), info.getFfId(), 
					info.getPrice().replace("00", ""), info.getIp());
			String result = HttpTool.sendPost(payInfo.getUrl(), param);
			
			JSONObject resultObj = JSONObject.parseObject(result);
			String resultCode = resultObj.getString("resultCode");
			if (resultCode == null || !resultCode.equals("0000")) return null;
			
			String content = resultObj.getString("code");
			String smsport = resultObj.getString("longCode");
			
			resultPayCode.setDeductionnum(smsport);
			resultPayCode.setDeductioncommand(content);
			resultPayCode.setNeedmt(payInfo.getNeedmt());
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return resultPayCode;
	}
	
	public static String pieceURL(String url, String imsi, String imei, String ext,
			String price, String ip){
		
		String key = "6nV6B2tn";
		String mac = null;
		String gameName = null;
		String chargeName = null;
		try {
			gameName = URLEncoder.encode("测试游戏", "utf-8");
			chargeName = URLEncoder.encode("测试道具", "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuilder md5Param = new StringBuilder();
		md5Param.append("57").append(price).append(ip).append(ext).append(gameName)
			.append(chargeName).append(key);
		mac = MD5Tool.toMD5Value32(md5Param.toString()).toUpperCase();
		
		StringBuilder param = new StringBuilder();
		param.append("channelId=57")
			.append("&imsi=").append(imsi)
			.append("&fee=").append(price)
			.append("&ip=").append(ip)
			.append("&extra=").append(ext)
			.append("&gameName=").append(gameName)
			.append("&chargeName=").append(chargeName)
			.append("&mac=").append(mac);
		
		return param.toString();
	}

}
