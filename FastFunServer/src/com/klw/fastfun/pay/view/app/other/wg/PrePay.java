package com.klw.fastfun.pay.view.app.other.wg;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.klw.fastfun.pay.common.domain.OrderReqInfo;
import com.klw.fastfun.pay.common.tool.HttpTool;
import com.klw.fastfun.pay.view.app.other.tools.GatewayRequest;
import com.klw.fastfun.pay.view.app.other.tools.ResponseHelper;

public class PrePay {
	
	public static Map<String, String> reqPrePay(OrderReqInfo info) throws Exception {
		Map<String, String> resMap = new HashMap<String, String>();
		
		for(int i=0;i<1;i++){
			GatewayRequest gatewayRequest = new GatewayRequest(null, null,0);
			GatewayRequest sdkRequest = new GatewayRequest(null, null,0);
			
			BigDecimal price = new BigDecimal(info.getFee());
			BigDecimal divideNum = new BigDecimal(100);
			
			String keyword = info.getKeyword();
			String[] keywordarr = keyword.split("#");
			gatewayRequest.setParameter("merchant_no",keywordarr[0]);
			sdkRequest.setParameter("merchant_no",keywordarr[0]);
//			gatewayRequest.setParameter("child_merchant_no","13288827777");
			gatewayRequest.setParameter("terminal_no",keywordarr[1]);
			sdkRequest.setParameter("terminal_no",keywordarr[1]);
			String key = keywordarr[2];
			gatewayRequest.setKey(key);
			sdkRequest.setParameter("child_merchant_no","");
			
			
			
			gatewayRequest.setGatewayUrl(info.getUrl());
			sdkRequest.setGatewayUrl("http://120.31.132.119/preEntry.do");
//		gatewayRequest.setGatewayUrl("http://localhost:8080/payment-gateway/backStageEntry.do");
			gatewayRequest.setParameter("busi_code","PRE_PAY");
			sdkRequest.setParameter("busi_code","PRE_PAY");
//			gatewayRequest.setParameter("busi_code","SEARCH_RATE");
			gatewayRequest.setParameter("order_no",info.getFfId());
//			gatewayRequest.setParameter("bank_code","ALIPAY");
			gatewayRequest.setParameter("bank_code","APPWECHAT");
			gatewayRequest.setParameter("amount",price.divide(divideNum).toString());
//		gatewayRequest.setParameter("auth_code","280448163006692512");
			gatewayRequest.setParameter("currency_type","CNY");
			gatewayRequest.setParameter("sett_currency_type","CNY");
			gatewayRequest.setParameter("product_name","保卫胡啪"+info.getCpId());
//			gatewayRequest.setParameter("product_desc","未来产品");
			
			gatewayRequest.setParameter("notify_url",info.getMatchRegex());
			gatewayRequest.setParameter("return_url",info.getMatchRegex());
			gatewayRequest.setParameter("sign_type","SHA256");
			gatewayRequest.setParameter("access_type","2");
			sdkRequest.setParameter("access_type","2");
			
			String requestUrl = gatewayRequest.getRequestURL();
			
			//应答对象
			ResponseHelper resHelper = new ResponseHelper();
			
			String url = "";
			String param = "";
			if(null != requestUrl) {
				int indexOf = requestUrl.indexOf("?");
				if(-1 != indexOf) {
					url = requestUrl.substring(0,indexOf);
					param = requestUrl.substring(indexOf+1, requestUrl.length());
				} 
			}
			
			String reqRestult = HttpTool.sendPost(url, param);
			//后台调用
			if(reqRestult != null) {//发送post请求并将返回的结果赋值给 postUtil的ResContent
				String xml = reqRestult;
				if(xml!=null&&!xml.startsWith("<?xml version=")){
					System.out.println(xml);
				}else{
					resHelper.setContent(xml); //setContent(xml)并解析XML内容，得到map
					resHelper.setKey(key);
					
					//获取返回码
					String resp_code = resHelper.getParameter("resp_code");
					
					//判断签名及结果
					if(resHelper.verifySign()&& "00".equals(resp_code)) {
						System.out.println("订单查询成功</br>");
						System.out.println("查询接口返回xml报文：</br>"+xml.replaceAll("<","&lt;").replaceAll(">","&gt;")+"</br></br>");
						System.out.println("xml解析后的支付结果：</br>");
						String tokenId = resHelper.getParameter("token_id");
						System.out.println("token_id:"+tokenId);
//						sdkRequest.setParameter("token_id",tokenId);
//						String sdkUrl = sdkRequest.getRequestURL()+"&token_id="+tokenId;
						resMap.put("token_id", tokenId);
						resMap.put("merchant_no", keywordarr[0]);
						resMap.put("terminal_no", keywordarr[1]);
//						resMap.put("sdk_url", sdkUrl);
					} else {
						//错误时，返回结果未签名，记录resp_code、resp_desc看失败详情。
						System.out.println("验证签名失败或业务错误<br/>");
						System.out.println("resp_code:" + resHelper.getParameter("resp_code")+" resp_desc:" + resHelper.getParameter("resp_desc"));
						System.out.println("验证签名失败或业务错误");
						System.out.println("resp_code:" + resHelper.getParameter("resp_code")+" resp_desc:" + resHelper.getParameter("resp_desc"));
					}	
				}
			} else {
				System.out.println("后台调用通信失败");
				System.out.println("后台调用通信失败");
				System.out.println(reqRestult);
				//有可能因为网络原因，请求已经处理，但未收到应答。
			}
			
		}
		return resMap;
	}
	
	public static void main(String[] args) throws Exception {
		OrderReqInfo info = new OrderReqInfo();
		info.setFee(1);
		info.setFfId("02mmz67333442745");
		info.setCpId("test01");
		info.setUrl("http://120.31.132.119/backStageEntry.do");
		info.setMatchRegex("http://120.24.88.90/fee/feeGHTback");
		info.setKeyword("102100000125#20000147#857e6g8y51b5k365f7v954s50u24h14w");
		Map<String, String> resMap = reqPrePay(info);
		String result = JSONObject.toJSONString(resMap);
		System.out.println(result);
		
	}
}
