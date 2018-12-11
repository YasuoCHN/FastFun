package com.klw.fastfun.pay.view.app.http;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.juice.orange.game.log.LoggerFactory;
import com.klw.fastfun.pay.common.domain.OrderReqInfo;
import com.klw.fastfun.pay.common.domain.RegisterRes;
import com.klw.fastfun.pay.common.tool.ConstantDefine;
import com.klw.fastfun.pay.common.tool.HttpTool;
import com.klw.fastfun.pay.common.tool.HttpsTool;
import com.klw.fastfun.pay.common.tool.xdzf.MD5;
import com.klw.fastfun.pay.view.app.other.qmdyuril.PaySignUtil;

public class OtherPayReqHelper {
	
	private static Logger logger = LoggerFactory.getLogger(OtherPayReqHelper.class);
	
	
	public static OrderReqInfo getSMSRegister(OrderReqInfo info) {
		switch (info.getUrlNo()) {
		case ConstantDefine.URL_NO_SDZ1_JSON:
			return getOnlineSMS301(info);
		case ConstantDefine.URL_NO_SDZ2_JSON:
			return getOnlineSMS302(info);
		default:
			break;
		}
		return null;
	}
	

	public static void main(String[] args) {
		String s = "ok|3568db56021d4663";
		String[] ss = s.split("\\|");
		System.out.println(ss[1]);
		System.out.println(ss.length);
	}
	
	private static OrderReqInfo getOnlineSMS302(OrderReqInfo info) {
		try  {
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();
			
			BigDecimal b1 = new BigDecimal(price);
			BigDecimal b2 = new BigDecimal(100);
			price = b1.divide(b2).toString();
			
			String[] tmp = keyword.split("#");
			
			String mchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String backurl = tmp[3];
			String paytype = tmp[4];
			
			if (callbackurl == null || callbackurl.length() <= 0) {
				callbackurl = backurl;
			}
			
			RegisterRes reg = new RegisterRes();
			reg.setOrderid(info.getFfId());
			reg.setStatus("1");//0为成功其他为失败
			
			Map<String, Object> restmap = new HashMap<String, Object>();
			restmap.put("appKey",mchid);
			restmap.put("notifyUrl",notityurl);
			restmap.put("bussOrderNum",info.getFfId());
			restmap.put("payMoney",price);
			restmap.put("orderName",goodsname);
			restmap.put("ip",info.getIp());
			restmap.put("returnUrl",callbackurl);
			restmap.put("remark",info.getSpId());
			restmap.put("channel",paytype);
			String sign = PaySignUtil.getSign(restmap,appsecret);
			String md5Param=PaySignUtil.getParamStr(restmap)+"&sign="+sign;
			md5Param = URLEncoder.encode(md5Param, "utf-8");
			String param = "paramStr="+md5Param;
			
			String reqRestult = HttpsTool.sendPost(url, param, 5000);
			
			if (reqRestult != null && reqRestult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqRestult);
				String resultCode = jsonobj.getString("resultCode");
				if ("200".equals(resultCode)) {
					String data = jsonobj.getString("Data");
					JSONObject jsonobj1 = JSONObject.parseObject(data);
					String requrl = jsonobj1.getString("qr_code");;
					reg.setUrl(requrl);
					reg.setStatus("0");
					reg.setOrderid(info.getFfId());
					reg.setSpid(info.getSpId());
					info.setReg(reg);
				} else {
					if (!"0".equals(resultCode))
						reg.setStatus(resultCode);
				}
			}
			
		} catch (Exception e) {
			logger.error("sdZ2指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return info;
		
	}
	
	private static OrderReqInfo getOnlineSMS301(OrderReqInfo info) {
		try  {
			
			String url = info.getUrl();
			String keyword = info.getKeyword();
			
			RegisterRes reg = new RegisterRes();
			reg.setOrderid(info.getFfId());
			reg.setStatus("0");//0为成功其他为失败
			
			String reqkey = HttpTool.sendGetSetTimeout(url, "m=Lua.getRand","2000");
			String[] reqkeyarr = reqkey.split("\\|");
			if (reqkeyarr.length == 2) {
				String key = reqkeyarr[1];
				info.setPmodel(key);
				String reqtokenparam = "m=Lua.getUserToken&" + keyword;
				String reqtoken = HttpTool.sendGetSetTimeout(url, reqtokenparam,"2000");
				String[] reqtokenarr = reqtoken.split("\\|");
				if (reqtokenarr.length == 2) {
					String token = reqtokenarr[1];
					StringBuilder param = new StringBuilder();
					param.append("m=Lua.chuanImgByPost2")
					.append("&token=").append(token)
					.append("&phone=").append(info.getMobile())
					.append("&key=").append(key)
					.append("&img=").append(info.getFfId() + ".jpg");
					String requrl = url + "?" + param.toString();
					reg.setUrl(requrl);
					
					StringBuilder resparam = new StringBuilder();
					resparam.append("m=Lua.setImg")
					.append("&key=").append(key)
					.append("&token=").append(token)
					.append("&status=");
					info.setCtech(url + "?" + resparam.toString());
				} else {
					reg.setStatus("2");//1获取token失败
				}
			} else {
				reg.setStatus("1");//1获取key失败
			}
			
			info.setReg(reg);
			
		} catch (Exception e) {
			logger.error("sd18指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return info;
		
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
