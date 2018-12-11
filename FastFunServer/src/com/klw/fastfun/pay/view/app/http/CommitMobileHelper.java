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

public class CommitMobileHelper {
	
	private static Logger logger = LoggerFactory.getLogger(CommitMobileHelper.class);
	
	
	public static String commitMobile(OrderReqInfo info) {
		switch (info.getUrlNo()) {
		case ConstantDefine.URL_NO_YC41_JSON:
			return getOnlineSMS501(info);
		default:
			break;
		}
		return null;
		
	}
	
	//联动优势-魔力小鸟
	private static String getOnlineSMS501(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String callbackurl = info.getCallbackurl();
			
			String[] tmp = keyword.split("#");
			
			String mchid = tmp[0];
			String appsecret = tmp[1];
			String backurl = tmp[3];
			String upversion = tmp[7];
			String upmobile = tmp[9];
			String commitcode = tmp[10];
			url = upmobile;
			
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
				restmap.put("mobileid",mobile);
				restmap.put("orderid",out_trade_id);
				restmap.put("orderdate",time);
				restmap.put("upversion",upversion);
				
				StringBuilder signparam = new StringBuilder();
				signparam.append("merid=").append(mchid)
				.append("&orderid=").append(out_trade_id)
				.append("&orderdate=").append(time)
				.append("&upversion=").append(upversion)
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
						String requrl = commitcode + "?ffid=" + info.getFfId() 
								+ "&cpid=" + info.getCpId() + "&code=";
						payMap.put("status", "0");
						payMap.put("msg", msg);
						payMap.put("url", requrl);
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
