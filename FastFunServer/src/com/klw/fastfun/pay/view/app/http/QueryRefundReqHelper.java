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
 * 退款查询
 * @author ouyangsu
 *
 */
public class QueryRefundReqHelper {
	
	private static Logger logger = LoggerFactory.getLogger(QueryRefundReqHelper.class);
	
	
	public static String getRefundQueryResult(OrderReqInfo info) {
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
		//  1–审核通过，2–待审核； 等待退款； 3 - 退款中； 4 - 退款成功； 5 - 退款失败； 6 - 审核未通过；
		String result = "{\"result\":\"-1\"}";
		try  {
			
			String keyword = info.getKeyword();
			String matchRegex = info.getMatchRegex();
			String signtype = "MD5";
			String[] tmp = keyword.split("#");
			String[] tmp1 = matchRegex.split("#");
			String appid = tmp[0];
			String appsecret = tmp[1];
			String url = tmp1[2];
			
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
			logger.info("微信H5支付sd25退款申请查询url=>"+url);
			logger.info("微信H5支付sd25退款申请查询param=>"+param.toString());
			logger.info("微信H5支付sd25退款申请查询结果=>"+reqResult);
			if (reqResult == null || reqResult.length() <= 0) return result;
			Map<String, String> repmap = CommonTool.parseYZ(reqResult);
			String restransdata = repmap.get("transdata");
			JSONObject myObj = JSONObject.parseObject(restransdata);
			String rescode = myObj.getString("code");
			if ("200".equals(rescode)) {
				String payresult = myObj.getString("result");
				Map<String, String> map = new HashMap<String, String>();
				if ("1".equals(payresult)) {
					payresult = "2";
				} else if ("2".equals(payresult)) {
					payresult = "1";
				}
				map.put("result", payresult);
				result = JSONObject.toJSONString(map);
			} else {
				result = "{\"result\":\"-1\"}";
				logger.error("微信支付sd25退款申请查询失败订单号："+info.getFfId());
			}
			
		} catch (Exception e) {
			logger.error("微信H5支付sd25支付退款申请查询失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}

}
