package com.klw.fastfun.pay.view.app.http;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.juice.orange.game.log.LoggerFactory;
import com.klw.fastfun.pay.common.domain.LTPayResultH5Info;
import com.klw.fastfun.pay.common.domain.OrderReqInfo;
import com.klw.fastfun.pay.common.tool.CommonTool;
import com.klw.fastfun.pay.common.tool.ConstantDefine;
import com.klw.fastfun.pay.common.tool.HttpsTool;
import com.klw.fastfun.pay.common.tool.xdzf.MD5;

/**
 * 退款
 * @author ouyangsu
 *
 */
public class RefundReqHelper {
	
	private static Logger logger = LoggerFactory.getLogger(RefundReqHelper.class);
	
	
	public static String getRefundResult(OrderReqInfo info) {
		switch (info.getUrlNo()) {
		case ConstantDefine.URL_NO_SD25_JSON:
			return getOnlineSMS63(info);
		/*case ConstantDefine.URL_NO_SD26_JSON:
			return getOnlineSMS73(info);
		case ConstantDefine.URL_NO_SD27_JSON:
			return getOnlineSMS75(info);*/
		default:
			break;
		}
		return null;
	}
	
	
	
	private static String getOnlineSMS63(OrderReqInfo info) {
		// 1–支付成功； 2–待支付； 3 - 支付失败； 4 - 取消订单； 5 - 支付超时； 6 - 退款； 9 - 其他错误
		String result = "{\"result\":\"-1\"}";
		try  {
			
			String keyword = info.getKeyword();
			String matchRegex = info.getMatchRegex();
			String signtype = "MD5";
			String[] tmp = keyword.split("#");
			String[] tmp1 = matchRegex.split("#");
			String appid = tmp[0];
			String appsecret = tmp[1];
			String url = tmp1[1];
			
			LTPayResultH5Info hinfo = new LTPayResultH5Info();
			hinfo.setAppid(appid);
			hinfo.setTransid(info.getTraid());
			
			StringBuilder signparam = new StringBuilder();
			signparam.append("appid=").append(hinfo.getAppid())
			.append("&transid=").append(info.getTraid())
			.append("&key=").append(appsecret);
			String sign = MD5.md5(signparam.toString(), "utf-8");
			
			StringBuilder param = new StringBuilder();
			param.append("transdata=").append(URLEncoder.encode(JSONObject.toJSONString(hinfo),"utf-8"))
			.append("&sign=").append(URLEncoder.encode(sign,"utf-8"))
			.append("&signtype=").append(URLEncoder.encode(signtype,"utf-8"));
			
			String reqResult = HttpsTool.sendPost(url, param.toString(),8000);
			logger.info("微信H5支付sd25退款申请url=>"+url);
			logger.info("微信H5支付sd25退款申请param=>"+param.toString());
			logger.info("微信H5支付sd25退款申请结果=>"+reqResult);
			if (reqResult == null || reqResult.length() <= 0) return result;
			Map<String, String> repmap = CommonTool.parseYZ(reqResult);
			String restransdata = repmap.get("transdata");
			JSONObject myObj = JSONObject.parseObject(restransdata);
			String rescode = myObj.getString("code");
			if ("200".equals(rescode)) {
				String payresult = myObj.getString("result");
				Map<String, String> map = new HashMap<String, String>();
				payresult = "6";
				map.put("result", payresult);
				result = JSONObject.toJSONString(map);
			} else {
				result = "{\"result\":\""+rescode+"\"}";
				logger.error("微信支付sd25支付退款申请失败订单号："+info.getFfId());
			}
			
		} catch (Exception e) {
			logger.error("微信H5支付sd25支付退款申请失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}

}
