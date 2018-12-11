package com.klw.fastfun.pay.view.app.http;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.juice.orange.game.log.LoggerFactory;
import com.klw.fastfun.pay.common.domain.OrderReqInfo;
import com.klw.fastfun.pay.common.tool.CommonTool;
import com.klw.fastfun.pay.common.tool.ConstantDefine;
import com.klw.fastfun.pay.common.tool.HttpTool;
import com.klw.fastfun.pay.common.tool.xdzf.MD5;
import com.klw.fastfun.pay.view.app.other.qmdyuril.PaySignUtil;
import com.klw.fastfun.pay.view.app.other.util.RSASignature;

public class AgentOtherPayHelper {
	
	private static Logger logger = LoggerFactory.getLogger(AgentOtherPayHelper.class);
	
	
	public static String agentPay(OrderReqInfo info) {
		switch (info.getUrlNo()) {
		case ConstantDefine.URL_NO_SDD7_JSON:
			return getOnlineSMS163(info);
		default:
			break;
		}
		return null;
		
	}
	
	//贝付-南京众誊
	private static String getOnlineSMS163(OrderReqInfo info) {
		String result = "error";
		try  {
			String body = info.getBody();
			
			String keyword = info.getKeyword();
			String cpid = info.getCpId();
			String md5mid = cpid.substring(0,cpid.length()-2);
			String key = ConstantDefine.MD5_KEY1 + md5mid + ConstantDefine.MD5_KEY2;
			
			String[] tmp = keyword.split("#");
			
			String mchid = tmp[0];
			
			Map<String, Object> restmap = CommonTool.parseJson2(JSONObject.parseObject(body));
			restmap.remove("cpid");
			restmap.remove("sign");
			restmap.put("account", mchid);
			String url = "http://47.75.32.235/api/v1.0/agentPay";
			String signparam = PaySignUtil.getParamStr(restmap);
			String signature = RSASignature.sign(signparam, ConstantDefine.NJZY_PRI_KEY, "utf-8");
			restmap.put("signature",signature);
			String param = JSONObject.toJSONString(restmap);
			
			String reqResult = HttpTool.sendKLWPost(url, param, "utf-8", 7000);
			logger.info("南京众誉sdD7代付结果:"+reqResult);
			Map<String, Object> resmap = new HashMap<String, Object>();
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				
				String status = jsonobj.getString("respCode");
				String msg = jsonobj.getString("respMsg");
				String orderno = jsonobj.getString("txnld");
				
				resmap = CommonTool.parseJson2(jsonobj);
				String resSign = (String)resmap.get("signature");
				resmap.remove("signature");
				String resSignparam = PaySignUtil.getParamStr(resmap);
				boolean checksign = RSASignature.doCheck(resSignparam,
						resSign, ConstantDefine.NJZY_PUB_KEY, "utf-8");
				if (checksign) {
					resmap.clear();
					resmap.put("status", status);
					resmap.put("msg", msg);
					resmap.put("orderno", orderno);
				} else {
					resmap.clear();
					resmap.put("status", "-2");
					resmap.put("msg", "System error");
				}
				
			} else {
				resmap.clear();
				resmap.put("status", "-1");
				resmap.put("msg", "request time out");
			}
			
			String respSignparam = PaySignUtil.getParamStr(resmap);
			respSignparam = respSignparam + "&key=" + key;
			String sign = MD5.md5(respSignparam, "utf-8");
			resmap.put("sign", sign);
			result = JSONObject.toJSONString(resmap);
			logger.info("南京众誉sdD7代付生成json:"+result);
		} catch (Exception e) {
			logger.error("南京众誉sdD7代付失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
}
