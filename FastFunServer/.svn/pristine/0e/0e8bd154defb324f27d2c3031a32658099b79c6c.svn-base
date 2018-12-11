package com.klw.fastfun.pay.view.app.http;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.juice.orange.game.log.LoggerFactory;
import com.klw.fastfun.pay.common.domain.OrderReqInfo;
import com.klw.fastfun.pay.common.tool.ConstantDefine;
import com.klw.fastfun.pay.common.tool.HttpTool;

/**
 * 退款
 * @author ouyangsu
 *
 */
public class PreReqHelper {
	
	private static Logger logger = LoggerFactory.getLogger(PreReqHelper.class);
	
	
	public static String getPreReqResult(OrderReqInfo info) {
		switch (info.getUrlNo()) {
		case ConstantDefine.URL_NO_SD31_JSON:
			return getOnlineSMS85(info);
		default:
			break;
		}
		return null;
	}
	
	
	/*public static void main(String[] args) {
		String regex = "\\d+";
		String s = "5336566325632563880123456789";
		System.out.println(s.matches(regex));
	}*/
	public static void main(String[] args) {
		String url = "http://api.onelist.cn/pay/gateway";
		Map<String , String> mapdata = new HashMap<String, String>();
		mapdata.put("service", "jft.weixin.mppay");
		mapdata.put("version", "3.0");
		mapdata.put("charset", "UTF-8");
		mapdata.put("sign_type", "MD5");
		mapdata.put("mch_id", "200540016330");
		mapdata.put("out_trade_no", "c6899e9864f245dc8f3ca36c3bc7d091");
		mapdata.put("body", "0.01元充值");
		mapdata.put("total_fee", "1");
		mapdata.put("notify_url", "http://test.csl2016.cn:7000/sp/notifyFromJFTWXGZH.e");
		mapdata.put("callback_url", "http://test.csl2016.cn:7000/sp/selectJFTWXGZHResult.e");
		mapdata.put("nonce_str", "acf8ccde3e3e4d20a287c8a820ccaba7");
		mapdata.put("sign", "722105af07b251c26d38a7188d34947d");
		String data  = JSONObject.toJSONString(mapdata);
		Map<String , String> map = new HashMap<String, String>();
		map.put("data", data);
		String param = JSONObject.toJSONString(map);
		String result = HttpTool.sendKLWPost(url, param, "utf-8", 5000);
		System.out.println(result);
	}
	private static String getOnlineSMS85(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String matchRegex = info.getMatchRegex();
			String app = info.getApp();
			String goodsname = info.getPropname();
			String phone = info.getMobile();
			String imsi = info.getImsi();
			String imei = info.getImei();
			String iccid = info.getIccid();
			String callbackurl = info.getCallbackurl();
			String[] tmp = keyword.split("#");
			String[] tmp1 = matchRegex.split("#");
			
			String url = tmp1[1];
			String redirect_uri = tmp1[2];
			String appid = tmp[3];
//			String appsecret = tmp1[4];
			
			StringBuilder redirectparam = new StringBuilder();
			redirectparam.append("?cpid=").append(info.getCpId())
			.append("&fee=").append(price)
			.append("&is_raw=0")
			.append("&cpparam=").append(info.getCpParam())
			.append("&goodsname=").append(goodsname);
			if (app != null && app.length() > 0 ) {
				redirectparam.append("&app=").append(app);
			}
			if (callbackurl != null && callbackurl.length() > 0 ) {
				redirectparam.append("&callbackurl=").append(callbackurl);
			}
			if (phone != null && phone.length() > 0 ) {
				redirectparam.append("&phone=").append(phone);
			}
			if (imsi != null && imsi.length() > 0 ) {
				redirectparam.append("&imsi=").append(imsi);
			}
			if (imei != null && imei.length() > 0 ) {
				redirectparam.append("&imei=").append(imei);
			}
			if (iccid != null && iccid.length() > 0 ) {
				redirectparam.append("&iccid=").append(iccid);
			}
			redirectparam.append("&");
			
			StringBuilder param = new StringBuilder();
			param.append("appid=").append(appid)
			.append("&redirect_uri=").append(URLEncoder.encode(redirect_uri + redirectparam.toString(), "utf-8"))
			.append("response_type=code&scope=snsapi_base#wechat_redirect");
			
			String localurl = url + "?" + param.toString();
			result = parseHTML(localurl, info);
			
			logger.info("微信公众号支付准备sd31生成json:"+result);
			
		} catch (Exception e) {
			logger.error("微信公众号支付准备sd31指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
	
	
	
	private static String parseHTML(String url, OrderReqInfo info) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("<!doctype html>");
		sb.append("<html lang=\"en\">");
		sb.append("<head>");
		sb.append("<meta charset=\"UTF-8\">");
		sb.append("<meta name=\"Generator\" content=\"EditPlus?\">");
		sb.append("<meta name=\"Author\" content=\"\">");
		sb.append("<meta name=\"Keywords\" content=\"\">");
		sb.append("<meta name=\"Description\" content=\"\">");
		sb.append("<title>微信支付</title>");
		sb.append("<script language=\"javascript\" type=\"text/javascript\">");
		sb.append("window.location='").append(url).append("'");
		sb.append("</script>");
		sb.append("</head><body> </body></html>");
		
		return sb.toString();
	}

}
