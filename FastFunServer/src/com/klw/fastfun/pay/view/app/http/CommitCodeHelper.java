package com.klw.fastfun.pay.view.app.http;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.juice.orange.game.log.LoggerFactory;
import com.klw.fastfun.pay.common.domain.OrderReqInfo;
import com.klw.fastfun.pay.common.tool.CommonTool;
import com.klw.fastfun.pay.common.tool.ConstantDefine;
import com.klw.fastfun.pay.common.tool.DateTool;
import com.klw.fastfun.pay.common.tool.HttpTool;
import com.klw.fastfun.pay.common.tool.xdzf.MD5;
import com.klw.fastfun.pay.view.app.other.qmdyuril.PaySignUtil;
import com.klw.fastfun.pay.view.app.other.util.RSASignature;

public class CommitCodeHelper {
	
	private static Logger logger = LoggerFactory.getLogger(CommitCodeHelper.class);
	
	
	public static String commitCode(OrderReqInfo info) {
		switch (info.getUrlNo()) {
		case ConstantDefine.URL_NO_YC41_JSON:
			return getOnlineSMS501(info);
		case ConstantDefine.URL_NO_LT12_JSON:
			return getOnlineSMS502(info);
		default:
			break;
		}
		return null;
		
	}
	
	private static String getOnlineSMS502(OrderReqInfo info) {
		String result = "error";
		try  {
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String traid = info.getTraid();
			String code = info.getCode();
			
			keyword = keyword.replace("apply", "confirm");
			
			String mobile = info.getMobile();
			if (mobile != null && mobile.length() == 11) {
				
				StringBuilder param = new StringBuilder();
				param.append(keyword)
				.append("&phone=").append(mobile)
				.append("&price=").append(price)
				.append("&orderId=").append(traid)
				.append("&verifyCode=").append(code)
				.append("&clientIP=").append(info.getIp());
				
				String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"4000");
				logger.info("叶慧lt12请求结果："+reqResult);
				Map<String, String> payMap = new HashMap<String, String>();
				if (reqResult != null && reqResult.length() > 0) {
					JSONObject jsonobj = JSONObject.parseObject(reqResult);
					String returnCode = jsonobj.getString("status");
					String msg = jsonobj.getString("message");
					payMap.put("orderid", info.getFfId());
					if ("000000".equals(returnCode)) {
						payMap.put("msg", msg);
						payMap.put("status", "0");
					} else {
						payMap.put("msg", msg);
						payMap.put("status", returnCode);
					}
					result = JSONObject.toJSONString(payMap);
				}
			}
		} catch (Exception e) {
			logger.error("叶慧lt12获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		logger.info("叶慧lt12返回结果："+result);
		return result;
	}
	
	//联动优势-魔力小鸟
	private static String getOnlineSMS501(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String matchRegex = info.getMatchRegex();
			String callbackurl = info.getCallbackurl();
			String code = info.getCode();
			
			String[] tmp = keyword.split("#");
			
			String mchid = tmp[0];
			String appsecret = tmp[1];
			String backurl = tmp[3];
			String upversion = tmp[7];
			String upcode = tmp[11];
			url = upcode;
			
			if (callbackurl == null || callbackurl.length() <= 0) {
				callbackurl = backurl;
			}
			
			String mobile = info.getMobile();
			if (mobile != null && mobile.length() == 11) {
				String out_trade_id = info.getSpId()+info.getFfId();
				
				String timestamp = DateTool.getNow();
				String time = timestamp.substring(0,8);
				
				Map<String, Object> restmap = new HashMap<String, Object>();
				restmap.put("merid",mchid);
				restmap.put("verifycode",code);
				restmap.put("orderid",out_trade_id);
				restmap.put("orderdate",time);
				restmap.put("upversion",upversion);
				
				StringBuilder signparam = new StringBuilder();
				signparam.append("merid=").append(mchid)
				.append("&orderid=").append(out_trade_id)
				.append("&orderdate=").append(time)
				.append("&upversion=").append(upversion)
				.append("&verifycode=").append(code)
				.append(appsecret);
				String sign = MD5.md5(signparam.toString(), "utf-8");
				
				String param = PaySignUtil.getParamStr(restmap);
				param = param + "&sign=" + sign;
				
				String reqResult = HttpTool.sendPost(url, param, "7000");
				logger.info("魔力小鸟yc41号码提交结果:"+reqResult);
				Map<String, String> payMap = new HashMap<String, String>();
				payMap.put("ffid", info.getFfId());
				if (reqResult != null && reqResult.length() > 0) {
					JSONObject jsonobj = JSONObject.parseObject(reqResult);
					String resultCode = jsonobj.getString("retCode");
					String msg = jsonobj.getString("retMsg");
					if ("0000".equals(resultCode)) {
						payMap.put("status", "0");
						payMap.put("msg", msg);
					} else {
						payMap.put("status", "2");
						payMap.put("msg", msg);
					}
				} else {
					payMap.put("status", "1");
					payMap.put("msg", "request time out");
				}
				result = JSONObject.toJSONString(payMap);
				logger.info("魔力小鸟yc41号码提交生成json:"+result);
			}
			
		} catch (Exception e) {
			logger.error("魔力小鸟yc41号码提交失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
}
