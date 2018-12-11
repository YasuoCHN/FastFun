package com.klw.fastfun.pay.view.app.http;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.juice.orange.game.log.LoggerFactory;
import com.klw.fastfun.pay.common.domain.OrderReqInfo;
import com.klw.fastfun.pay.common.tool.CommonTool;
import com.klw.fastfun.pay.common.tool.ConstantDefine;
import com.klw.fastfun.pay.common.tool.HttpsTool;
import com.klw.fastfun.pay.view.app.other.qmdyuril.PaySignUtil;
import com.klw.fastfun.pay.view.app.wxpay.utils.PayUtil;

public class ZWOtherPayHelper {
	
	private static Logger logger = LoggerFactory.getLogger(ZWOtherPayHelper.class);
	
	
	public static String getOtherPay(OrderReqInfo info) {
		switch (info.getUrlNo()) {
		case ConstantDefine.URL_ZWSF_SD01_JSON:
			return getOnlineSMS001(info);
		default:
			break;
		}
		return null;
	}
	

	
	
	private static String getOnlineSMS001(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();
			
			String[] tmp = keyword.split("#");
			
			String mchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String backurl = tmp[3];
			String method = tmp[4];
			String paytype = tmp[5];
			String signtype = tmp[6];
			String charset = tmp[7];
			
			if (callbackurl == null || callbackurl.length() <= 0) {
				callbackurl = backurl;
			}
			
			Map<String, Object> restmap = new HashMap<String, Object>();
			restmap.put("method",method);
			restmap.put("mchid",mchid);
			restmap.put("orderno",info.getFfId());
			restmap.put("orderip",info.getIp());
			restmap.put("totalfee",price);
			restmap.put("paytype",paytype);
			restmap.put("body",info.getSpId()+info.getFfId());
			restmap.put("signtype",signtype);
			restmap.put("charset",charset);
			restmap.put("notifyUrl",notityurl);
			restmap.put("randstr",PayUtil.getNonceStr());
			restmap.put("extra",info.getSpId());
			restmap.put("sign",PaySignUtil.getSign(restmap,appsecret));
			
			String param = JSONObject.toJSONString(restmap);
			String reqResult = HttpsTool.sendPost(url, param, 8000);
			
			logger.info("zwsfsd01请求结果:"+reqResult);
			Map<String, String> payMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				Map<String, Object> resmap = CommonTool.parseJson2(jsonobj);
				String resultCode = (String)resmap.get("status");
				if ("1".equals(resultCode)) {
					if (PaySignUtil.checkSign(resmap, appsecret)) {
						
						String requrl = (String)resmap.get("payurl");;
						payMap.put("url", requrl);
						payMap.put("status", "0");
						payMap.put("orderid", info.getFfId());
						payMap.put("spid", info.getSpId());
					} else {
						payMap.put("status", "3");
						payMap.put("msg", "back sign error "+resultCode);
					}
				} else {
					payMap.put("status", "2");
					payMap.put("msg", "back error "+resultCode);
				}
			} else {
				payMap.put("status", "1");
				payMap.put("msg", "request time out");
			}
			
			result = JSONObject.toJSONString(payMap);
			logger.info("zwsfsd01生成json:"+result);
			
		} catch (Exception e) {
			logger.error("zwsfsd01指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
	
	/*private static String getCommitUrl(OrderReqInfo info) {
		StringBuilder resUrl = new StringBuilder();
		resUrl.append("http://118.190.90.1/code/commitRegister?")
		.append("cpid=").append(info.getCpId())
		.append("&num=").append("1")
		.append("&phone=").append(info.getMobile())
		.append("&cpparam=").append(info.getCpParam())
		.append("&orderid=").append(info.getFfId())
		.append("&smscontent=");
		return resUrl.toString();
	}*/

}
