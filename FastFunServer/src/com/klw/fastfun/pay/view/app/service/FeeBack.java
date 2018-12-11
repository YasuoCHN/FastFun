/**
 * FastFunServer
 */
package com.klw.fastfun.pay.view.app.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jnewsdk.util.SignUtil;
import com.juice.orange.game.exception.JuiceException;
import com.juice.orange.game.handler.HttpRequest;
import com.juice.orange.game.handler.HttpResponse;
import com.juice.orange.game.log.LoggerFactory;
import com.klw.fastfun.pay.common.domain.CPInfo;
import com.klw.fastfun.pay.common.domain.CodeInfo;
import com.klw.fastfun.pay.common.domain.CodeOnlyInfo;
import com.klw.fastfun.pay.common.domain.CustomInfo;
import com.klw.fastfun.pay.common.domain.MMPayInfo;
import com.klw.fastfun.pay.common.domain.MobileInfo;
import com.klw.fastfun.pay.common.domain.OrderReqInfo;
import com.klw.fastfun.pay.common.domain.ResSDKReport;
import com.klw.fastfun.pay.common.domain.SDKUpdateInfo;
import com.klw.fastfun.pay.common.exception.ExceptionTool;
import com.klw.fastfun.pay.common.json.SynJson;
import com.klw.fastfun.pay.common.json.SynOtherJson;
import com.klw.fastfun.pay.common.tool.Base64;
import com.klw.fastfun.pay.common.tool.CPParam;
import com.klw.fastfun.pay.common.tool.CommonTool;
import com.klw.fastfun.pay.common.tool.ConstantDefine;
import com.klw.fastfun.pay.common.tool.DateTool;
import com.klw.fastfun.pay.common.tool.HttpTool;
import com.klw.fastfun.pay.common.tool.HttpsTool;
import com.klw.fastfun.pay.common.tool.MD5Tool;
import com.klw.fastfun.pay.common.tool.StringUtils;
import com.klw.fastfun.pay.common.tool.mm.SyncAppOrderReq;
import com.klw.fastfun.pay.common.tool.mm.SyncAppOrderResp;
import com.klw.fastfun.pay.common.tool.mm.SyncXMLUtils;
import com.klw.fastfun.pay.common.tool.mm.XMLUtils;
import com.klw.fastfun.pay.common.tool.xdzf.MD5;
import com.klw.fastfun.pay.common.transport.CodeT;
import com.klw.fastfun.pay.common.transport.OrderT;
import com.klw.fastfun.pay.view.app.ActionAware;
import com.klw.fastfun.pay.view.app.http.CodeReqHelper;
import com.klw.fastfun.pay.view.app.other.ght.model.pkg8583;
import com.klw.fastfun.pay.view.app.other.ght.util.Util;
import com.klw.fastfun.pay.view.app.other.qmdyuril.PaySignUtil;
import com.klw.fastfun.pay.view.app.other.util.HMacMD5;
import com.klw.fastfun.pay.view.app.other.util.RSASignature;
import com.klw.fastfun.pay.view.app.wxpay.utils.MD5Utils;
import com.klw.fastfun.pay.view.app.wxpay.utils.PayUtil;
import com.klw.fastfun.pay.view.app.wxpay.utils.XmlUtil;

/**
 * @author klwplayer.com
 *
 *         2015年3月31日
 */
public class FeeBack extends ActionAware {
	private static Logger logger = LoggerFactory.getLogger(FeeBack.class);
	
	public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALq3jFZhW8us7o4opJMpnZLSwv1SCdAL6/0ip8DB1u0EfuTHPy6XS49PlbR3sVDFJvRzNF2Fc7tylWYDoOTztyapdV8Y2LnLI9W8B/aWtn0fhAzLyr2IFLhHmP6VcGrCWY0DYnL12wTo5jgvbqCLH/Q3DBvekD6q13pmIbxw5uJDAgMBAAECgYEAg+PlgJrS8cMc21QANGeAA9dWnwPVJJ1XO/6/ylviCphTXh0UF0ANwpRv5gNqP+iThLbf9XOv9WeR+nZzr2YsJzB4wJQHjF1LrH1HFdFwhul19BSjEXkzcyCl0/tfeIsxvhdNYp7IhznlitGDAvOZEb4a1+e+L5Y0Uhx3kQLC8VkCQQDrLg3IK9HXKLaUg3BtSs1H3p5yDXs8ytNemfiVVwB2dyBG/q+RGjUpgwXbgEG8OeuGgYXcoYClJ+OSl/jWrrn1AkEAyz8qiNTw6C9s8e8z4MyCdTDiKXTCmA/du07AXC08NK3VOvw27N4b/5NNt7WyR7N/vQTxSx8d6P8uPRzvynDwVwJAf1+/GSYajcTANmmF77uuuPkqqa0BjShxGHCeAQxQ9NiKQ8lW/2jJWEVdW8f4UmCwXnYyMQ/LpCeZiuECZgvWLQJBAK76lL30xeq/WcX4L+urZe1Kxor2UMnlbvnhVM6Gyzx3JAqCNS88EVK5GMG+ldwQ9zpeVPZdtbxwZPiMPw1EqkUCQGph1+LoYCffaaCZw4aCPitO1t8Dfa2V8aBedhVqyXb74EQ7rSJIDcG2SDua/U4s+hEFQ+dOepC3gQ300xLWI80=";
	
	private static Map<String, String> testImsiMap = new HashMap<String, String>();
	static {
		testImsiMap.put("460001261929455", "true");
		testImsiMap.put("460079005047370", "true");//乐朋
		testImsiMap.put("460007082891181", "true");//掌信拓维
		testImsiMap.put("460000383148352", "true");//掌信拓维
		testImsiMap.put("460027141464450", "true");//掌信拓维
		testImsiMap.put("460005734382225", "true");//掌信拓维
		testImsiMap.put("460023107582358", "true");//有乐通
		testImsiMap.put("460005180413361", "true");//明日空间
		testImsiMap.put("460078027658863", "true");//虚实
		testImsiMap.put("460020398641270", "true");//酱油
		testImsiMap.put("460008247093133", "true");//酱油
		testImsiMap.put("460007082891181", "true");//搜游互动
		testImsiMap.put("460016111035704", "true");//考拉
		testImsiMap.put("460030495440302", "true");//考拉
		testImsiMap.put("460017086211144", "true");//艾阁
		testImsiMap.put("460001161954388", "true");//聚游堂
		testImsiMap.put("460007142548903", "true");//聚游堂
		testImsiMap.put("460009091826976", "true");//微游戏
		testImsiMap.put("460008453168042", "true");//中至
		testImsiMap.put("460026103504395", "true");//魔信
		testImsiMap.put("460023960772137", "true");//魔信
		testImsiMap.put("460077741063758", "true");//博尚
		testImsiMap.put("460004711479985", "true");//博尚
		testImsiMap.put("460027074960627", "true");//博尚
		testImsiMap.put("460078097386088", "true");//悦游
		testImsiMap.put("460023960772137", "true");//悦游
		testImsiMap.put("460024891466634", "true");//指尖昆仑
		testImsiMap.put("460023103977270", "true");//指尖昆仑
		testImsiMap.put("460021731183219", "true");//铭晨科技
		testImsiMap.put("460002032235266", "true");//聚娱
		testImsiMap.put("460008701832412", "true");//聚娱
	}
	public void feeback(HttpRequest request, HttpResponse response){
		System.out.println("1");
	}
	
	/*
	 *京东
	 * 
	 * */
	public void  feeBack_jingdong(HttpRequest request, HttpResponse response)
			throws Exception {
		String sms = "error";
		try {
			logger.info("feeBack_jingdong请求header信息：" + request.allHeaders());
			String mobile = request.getParam("phone");
			String state = request.getParam("status");
           System.out.println("得到的手机号码是"+mobile);
            String ffId = DateTool.getMonth() + "_" + DateTool.getffIdDay()+DateTool.getMonthDay() + "sd78" + mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			info.setMobile(mobile);
			if ("1".equals(state)) {
				info.setIsSuccess(1);
			} else {
				info.setIsSuccess(0);
			}
			info.setMobile(mobile);
			processxsBYOrder2(info);
//			if(ffId != null && ffId.length() > 0 &&state != null && state.length() > 0 ){
				sms="ok";
//			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	
	/*
	 * webqq绑定
	 *  post 请求
	 * */
	public void  feeBack_webqqbdsd77(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeBack_webqqbdsd77请求header信息：" + request.allHeaders());
			String mobile = request.getParam("mobile");
			String status = request.getParam("status");
		
            String ffId = DateTool.getMonth() + "_" + DateTool.getffIdDay()+DateTool.getMonthDay() + "sd77" + mobile;

            OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			info.setMobile(mobile);
			if ("1".equals(status)) {
				info.setIsSuccess(1);
			} else {
				info.setIsSuccess(CommonTool.convertInt(status));
			}

			processxsBYOrder2(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	/*
	 * 河北移动端
	 *  post 请求
	 * */
	public void  feeBack_hebeiMove(HttpRequest request, HttpResponse response)
			throws Exception {
		    String sms = "error";
		try {
            logger.info("河北移动端  请求header信息：" + request.allHeaders());
      
			String body = request.body();
			//String body =URLDecoder.decode(request.body(),"utf-8");
			logger.info("feeSZLTzcjdsd31请求body信息：" + body);
			Map<String, String> reqmap = CommonTool.parseYZ(body);
			String state = reqmap.get("status");
			String mobile = reqmap.get("phone");
//			String code = reqmap.get("code");
			String orderId = reqmap.get("orderId");
//			String orderId = reqmap.get("orderId");
			String regist_time = reqmap.get("regist_time");
		
            String ffId = DateTool.getMonth() + "_" + DateTool.getffIdDay()+DateTool.getMonthDay() + "sd76" + mobile;
			
            Map<String, Object> resultMap = new HashMap<String, Object>();
            if (state != null && mobile != null) {
            	resultMap.put("code", 0);
        		resultMap.put("msg", "success");
        		sms = JSONObject.toJSONString(resultMap);
			} else {
				resultMap.put("code", 1);
        		resultMap.put("msg", "fail");
        		sms = JSONObject.toJSONString(resultMap);
			}
            
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			info.setMobile(mobile);
  
			if ("Y".equals(state)) {
				info.setIsSuccess(1);
			} else {
				info.setIsSuccess(0);
			}
			info.setMobile(mobile);
			processxsBYOrder2(info);
//			if(ffId != null && ffId.length() > 0 &&state != null && state.length() > 0 ){
 				sms="ok";
//			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
	}
	/*
	 * 飞信
	 *  post 请求
	 * */
	public void  feeBack_feixin(HttpRequest request, HttpResponse response)
			throws Exception {
		String sms = "error";
		try {
			logger.info("feixin请求header信息：" + request.allHeaders());
			String body = request.body();
			//String body =URLDecoder.decode(request.body(),"utf-8");
			logger.info("feeSZLTzcjdsd31请求body信息：" + body);
			Map<String, String> reqmap = CommonTool.parseYZ(body);
			String state = reqmap.get("status");
			String mobile = reqmap.get("phone");
			String code = reqmap.get("code");
			String orderId = reqmap.get("orderId");
//			String orderId = reqmap.get("orderId");
			String regist_time = reqmap.get("regist_time");
		
            String ffId = DateTool.getMonth() + "_" + DateTool.getffIdDay()+DateTool.getMonthDay() + "sd75" + mobile;
			
            Map<String, Object> resultMap = new HashMap<String, Object>();
            if (state != null && mobile != null) {
            	resultMap.put("code", 0);
        		resultMap.put("msg", "success");
        		sms = JSONObject.toJSONString(resultMap);
			} else {
				resultMap.put("code", 1);
        		resultMap.put("msg", "fail");
        		sms = JSONObject.toJSONString(resultMap);
			}
            
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			info.setMobile(mobile);
  
			if ("1".equals(state)) {
				info.setIsSuccess(1);
			} else {
				info.setIsSuccess(0);
			}
			info.setMobile(mobile);
			processxsBYOrder2(info);
//			if(ffId != null && ffId.length() > 0 &&state != null && state.length() > 0 ){
//				sms="ok";
//			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	/*
	 * 西瓜淘宝5
	 * 
	 * */
	public void  feeBack_XGTB5(HttpRequest request, HttpResponse response)
			throws Exception {
		String sms = "error";
		try {
			logger.info("feeBack_XGTB请求header信息：" + request.allHeaders());
			String content = request.body();
			logger.info("西瓜淘宝："+content);
			Map<String, String> myObj = parseYZ(content);
			
			String mobile = myObj.get("phone");
			String state = myObj.get("result");  
			String extra = myObj.get("extra");
			String linkid = myObj.get("linkid");
            String ffId = DateTool.getMonth() + "_" + DateTool.getffIdDay()+DateTool.getMonthDay() + "sd72" + mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			info.setMobile(mobile);
			if ("DELIVRD".equals(state)) {
				info.setIsSuccess(1);
			} else {
				info.setIsSuccess(0);
			}
			info.setMobile(mobile);
			processxsBYOrder2(info);
			if(ffId != null && ffId.length() > 0 &&state != null && state.length() > 0 ){
				sms="ok";
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	/*
	 * 西瓜淘宝
	 * 
	 * */
	public void  feeBack_XGTB(HttpRequest request, HttpResponse response)
			throws Exception {
		String sms = "error";
		try {
			logger.info("feeBack_XGTB请求header信息：" + request.allHeaders());
//			String content = request.body();
//			logger.info("西瓜淘宝："+content);
//			Map<String, String> myObj = parseYZ(content);
			
			String mobile = request.getParam("phone");
			String state = request.getParam("result");  
			String extra = request.getParam("extra");
			String linkid = request.getParam("linkid");
			String sp = request.getParam("sp");
            String ffId = DateTool.getMonth() + "_" + DateTool.getffIdDay()+DateTool.getMonthDay() + "sd73" + mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			info.setMobile(mobile);
			info.setIccid(linkid);
			if ("DELIVRD".equals(state)) {
				info.setIsSuccess(1);
			} else {
				info.setIsSuccess(0);
			}
			info.setMobile(mobile);
			processxsBYOrder2(info);
			if(ffId != null && ffId.length() > 0 &&state != null && state.length() > 0 ){
				sms="ok";
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	
	/*
	 * 何全淘宝—
	 * 
	 * */
	public void  feeBack_HQTB(HttpRequest request, HttpResponse response)
			throws Exception {
		String sms = "error";
		try {
			logger.info("feeBack_XGTB请求header信息：" + request.allHeaders());
			
			String mobile = request.getParam("mobile");
			String state = request.getParam("status");  
			String extra = request.getParam("tcstr");
            String ffId = DateTool.getMonth() + "_" + DateTool.getffIdDay()+DateTool.getMonthDay() + "sd74" + mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			info.setMobile(mobile);
			info.setCode(extra);
			if ("ok".equals(state)) {
				info.setIsSuccess(1);
			} else {
				info.setIsSuccess(0);
			}
			 
			processxsBYOrder2(info);
			if(ffId != null && ffId.length() > 0 &&state != null && state.length() > 0 ){
				sms="ok";
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	
	
	
	
	/*
	 * 探探
	 * 
	 *  http://118.190.90.1/fee/feeSZLTzclWtcsdTT
	 * */
	
	public void feeSZLTzclWtcsdTT(HttpRequest request, HttpResponse response)
			throws Exception {
		String sms = "error";
		try {
			logger.info("请求header信息：" + request.allHeaders());
			
			String mobile =  request.getParam("phone");
			String status =  request.getParam("status");
      		System.out.println(mobile + "mobile" +status);
//			String ffId = DateTool.getMonthDay() + "sd70" + mobile;//正式环境下是 sd70   测试环境是23
      		String ffId = DateTool.getMonth() + "_" + DateTool.getffIdDay()+DateTool.getMonthDay() + "sd72" + mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			info.setMobile(mobile);
			if("1".equals(status)){
				info.setIsSuccess(1);
			}else{
				info.setIsSuccess(CommonTool.convertInt(status));
			}
//			processRTBYOrder(info);
			processxsBYOrder2(info);
			if(ffId != null && ffId.length() > 0 &&status != null && status.length() > 0 ){
				sms="ok";
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content(sms).end();
	}
	 
 
	 
	  
	
	/***
	 * 淘宝问题——2018-11-12
	 * http://118.190.90.1/fee/feeSZLTzclWtcsdTB
	 */
	
	public void feeSZLTzclWtcsdTB(HttpRequest request, HttpResponse response)
			throws Exception {
		String sms = "error";
		try {
			logger.info("请求header信息：" + request.allHeaders());
			
			String mobile =  request.getParam("phone");
			String status =  request.getParam("status");
      		System.out.println(mobile + "mobile" +status);
//			String ffId = DateTool.getMonthDay() + "sd70" + mobile;//正式环境下是 sd70   测试环境是23
      		String ffId = DateTool.getMonth() + "_" + DateTool.getffIdDay()+DateTool.getMonthDay() + "sd70" + mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			info.setMobile(mobile);
			if("1".equals(status)){
				info.setIsSuccess(1);
			}else{
				info.setIsSuccess(CommonTool.convertInt(status));
			}
//			processRTBYOrder(info);
			processxsBYOrder2(info);
			if(ffId != null && ffId.length() > 0 &&status != null && status.length() > 0 ){
				sms="ok";
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content(sms).end();
	}
	 
	  
	
	/***
	 * 微博解封——2018-11-8
	 * http://120.24.88.90/telephone=17621067516
	 */
	public void WBJFLocalHostWB(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("WBJFLocalHostWB请求header信息：" + request.allHeaders());
			
			String mobile = request.getParam("telephone");
//			String linkid = request.getParam("linkid");
//			String msg = request.getParam("msg");
//			String svcnum = request.getParam("svcnum");

			if(mobile!=null&&mobile.length()>0){
				response.content("ok").end();
			}
			
			String ffId = DateTool.getMonth() + "_" + DateTool.getffIdDay()+DateTool.getMonthDay() + "sd65" + mobile;
			
//			String ffId =DateTool.getMonth() + "sd65"+mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			info.setIsSuccess(1);
				
			info.setMobile(mobile);
//			info.setImsi(linkid);
//			info.setIccid(svcnum);
//			info.setCtech(msg);
			
//			processRTBYOrder(info);
//			processBYOrdersd40(info);
			processxsBYOrder2(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
	}
	
	
	
	
	/***
	 * 贝壳找房网
	 * http://120.24.88.90/fee/feeSZLTzclWtcsd40
	 */
	public void feeszrtyfwebqqbacksd60(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeszrtyfwebqqbacksd60请求header信息：" + request.allHeaders());
			
			String mobile = request.getParam("phone");
			String linkid = request.getParam("linkid");
			String msg = request.getParam("msg");
			String svcnum = request.getParam("svcnum");
			
			if(mobile!=null&&mobile.length()>0){
				response.content("ok").end();
			}
			
			
			String ffId =DateTool.getMonth() + "sd60"+mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			info.setIsSuccess(1);
				
			info.setMobile(mobile);
			info.setImsi(linkid);
			info.setIccid(svcnum);
			info.setCtech(msg);
			
//			processRTBYOrder(info);
			processBYOrdersd40(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
	}
	/***
	 * webQQ汉城
	 * http://120.24.88.90/fee/feeSZLTzclWtcsd40
	 */
	public void feeszrtyfwebqqback(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeSZLTzclWtcsd58请求header信息：" + request.allHeaders());
			
			String mobile = request.getParam("phone");
			String linkid = request.getParam("linkid");
			String msg = request.getParam("msg");
			String svcnum = request.getParam("svcnum");
			
			if(mobile!=null&&mobile.length()>0){
				response.content("ok").end();
			}
			
			
			String ffId =DateTool.getMonth() + "sd58"+mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			info.setIsSuccess(1);
				
			info.setMobile(mobile);
			info.setImsi(linkid);
			info.setIccid(svcnum);
			info.setCtech(msg);
			
//			processRTBYOrder(info);
			processBYOrdersd40(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
	}
	
	
	
	/***
	 * TB
	 * http://120.24.88.90/fee/feeSZLTzclWtcsd40
	 */
	public void feeSZLTzclWtcsd54(HttpRequest request, HttpResponse response)
			throws Exception {
		String sms = "error";
		try {
			logger.info("feeSZLTzclWtcsd54请求header信息：" + request.allHeaders());
			
			
			String mobile =  request.getParam("phone");
			
			String status =  request.getParam("status");
			
			String ffId = DateTool.getMonth() + "sd54" + mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			info.setMobile(mobile);
			if("1".equals(status)){
				info.setIsSuccess(1);
			}else{
				info.setIsSuccess(CommonTool.convertInt(status));
			}
//			processRTBYOrder(info);
			processBYOrder(info);
			if(ffId != null && ffId.length() > 0 &&status != null && status.length() > 0 ){
				sms="ok";
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content(sms).end();
	}
	/***
	 * 瞿jd2
	 * http://120.24.88.90/fee/feeSZLTzclWtcsd40
	 */
	public void feeSZLTzclWtcsd56(HttpRequest request, HttpResponse response)
			throws Exception {
		String sms = "error";
		try {
			logger.info("feeSZLTzclWtcsd56请求header信息：" + request.allHeaders());
			
			
			String mobile =  request.getParam("phone");
			
			String status =  request.getParam("status");
			
			String ffId = DateTool.getMonth() + "_" + DateTool.getffIdDay()+DateTool.getMonthDay() + "sd56" + mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			info.setMobile(mobile);
			if("1".equals(status)){
				info.setIsSuccess(1);
			}else{
				info.setIsSuccess(CommonTool.convertInt(status));
			}
//			processRTBYOrder(info);
			processBYOrdersd46Date(info);
			if(ffId != null && ffId.length() > 0 &&status != null && status.length() > 0 ){
				sms="ok";
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content(sms).end();
	}
	
	/***
	 * tb胜达
	 * http://120.24.88.90/fee/feeSZLTzclWtcsd40
	 */
	public void feeSZLTzclWtcsd74(HttpRequest request, HttpResponse response)
			throws Exception {
		String sms = "error";
		try {
			logger.info("feeSZLTzclWtcsd56请求header信息：" + request.allHeaders());
			
			
			String mobile =  request.getParam("phone");
			
			String status =  request.getParam("status");
			
			String ffId = DateTool.getMonth() + "_" + DateTool.getffIdDay()+DateTool.getMonthDay() + "sd74" + mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			info.setMobile(mobile);
			if("1".equals(status)){
				info.setIsSuccess(1);
			}else{
				info.setIsSuccess(CommonTool.convertInt(status));
			}
//			processRTBYOrder(info);
//			processBYOrdersd46Date(info);
			
			processxsBYOrder2(info);
			if(ffId != null && ffId.length() > 0 &&status != null && status.length() > 0 ){
				sms="ok";
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content(sms).end();
	}
	/***
	 * ELM
	 * http://120.24.88.90/fee/feeSZLTzclWtcsd40
	 */
	public void feeSZLTzclWtcsd53(HttpRequest request, HttpResponse response)
			throws Exception {
		String sms = "error";
		try {
			logger.info("feeSZLTzclWtcsd53请求header信息：" + request.allHeaders());
			
			
			String mobile =  request.getParam("phone");
			
			String status =  request.getParam("status");
			
			String ffId = DateTool.getMonth() + "sd53" + mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			info.setMobile(mobile);
			if("1".equals(status)){
				info.setIsSuccess(1);
			}else{
				info.setIsSuccess(CommonTool.convertInt(status));
			}
//			processRTBYOrder(info);
			processBYOrder(info);
			if(ffId != null && ffId.length() > 0 &&status != null && status.length() > 0 ){
				sms="ok";
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content(sms).end();
	}
	/***
	 * 瞿QQ
	 * http://120.24.88.90/fee/feeSZLTzclWtcsd40
	 */
	public void feeSZLTzclWtcsd46(HttpRequest request, HttpResponse response)
			throws Exception {
		String sms = "error";
		try {
			logger.info("feeSZLTzclWtcsd46请求header信息：" + request.allHeaders());
			
			
			String mobile =  request.getParam("phone");
			String qq =  request.getParam("qq");
			String status =  request.getParam("status");
			String ffId = DateTool.getMonth() + "_" + DateTool.getffIdDay() + "sd" + qq.substring(qq.length()-4,qq.length())+ mobile;
//			String ffId = DateTool.getMonth() + "sd" + qq.substring(qq.length()-4,qq.length())+ mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			info.setMobile(mobile);
			if("1".equals(status)){
				info.setIsSuccess(1);
			}else{
				info.setIsSuccess(2);
			}
//			processRTBYOrder(info);
//			processBYOrdersd46(info);
			processBYOrdersd46Date(info);
			if(ffId != null && ffId.length() > 0 &&status != null && status.length() > 0 ){
				sms="ok";
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content(sms).end();
	}
	
	/***
	 * 和通行证sd45
	 * http://120.24.88.90/fee/feeSZLTzclWtcsd40
	 */
	public void feeSZLTzclWtcsd45(HttpRequest request, HttpResponse response)
			throws Exception {
		String sms = "error";
		try {
			logger.info("feeSZLTzclWtcsd45请求header信息：" + request.allHeaders());
			
//			String body = request.body();
			String body =URLDecoder.decode(request.body(),"utf-8");

			logger.info("feeSZLTzclWtcsd45请求body信息：" + body);
			
			Map<String, String> mapobj = CommonTool.parseYZ(body);
			
			String orderId = mapobj.get("orderId");
			String mobile =  mapobj.get("phone");
			String status =  mapobj.get("status");
			String ffId = mapobj.get("customId");
			String time = mapobj.get("regist_time");
			
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			info.setMobile(mobile);
			if("Y".equals(status)){
				info.setIsSuccess(1);
			}else{
				info.setIsSuccess(2);
			}
//			processRTBYOrder(info);
			processBYOrdersd40(info);
			if(ffId != null && ffId.length() > 0 &&status != null && status.length() > 0 ){
				sms="ok";
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		Map<String, String> resMap = new HashMap<String, String>();
		if("ok".equals(sms)){
			resMap.put("code", "0");
			resMap.put("message", "seccess");
		}else{
			resMap.put("code", "1");
			resMap.put("message", "error");
		}
		String s = JSONObject.toJSONString(resMap);
		response.content(s).end();
	}
	public void feeBackSMSby(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			String cid = request.getParam("cid");//网关标识
			String stype = request.getParam("stype");//计费代码
			String scode = request.getParam("spCode");//sp长号
			String mobile = request.getParam("mobile");//用户手机号码
			String msg = request.getParam("cmd");//下行内容
			String lid = CommonTool.getxsOrderNO("only02");
			String time = request.getParam("time");//下行时间
			String fee = request.getParam("fee");//信息资费(单位：分)
			String report = request.getParam("sta");//状态报告
			
			OrderReqInfo info = new OrderReqInfo();
			info.setCpParam(cid+"&"+stype+"&"+scode+"&"+msg+"&"+time);
			info.setMobile(mobile);
			info.setFfId(lid);
			info.setFee(new Integer(fee));
			info.setSpId("only02");
			// 处理订单
			boolean isSuc = false;
			//1表用户定制包月彩信,2表示用户退订,10表示用户计费成功
			info.setIsSuccess(-1);
			if ("10".equals(report)) {
				info.setIsSuccess(0);
				isSuc = true;
			} else if ("1".equals(report)) {
				info.setIsSuccess(1);//户定制包月彩信
			} else if ("2".equals(report)) {
				info.setIsSuccess(2);//用户退订
			}
			processOnlyCPOrder(isSuc, info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("OK").end();
		
	}
	
	public void feeBackSMS(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			String cid = request.getParam("cid");//网关标识
			String stype = request.getParam("stype");//计费代码
			String scode = request.getParam("scode");//sp长号
			String mobile = request.getParam("mobile");//用户手机号码
			String msg = request.getParam("msg");//下行内容
			String lid = request.getParam("lid");//linkId
			String time = request.getParam("time");//下行时间
			String fee = request.getParam("fee");//信息资费(单位：分)
			String report = request.getParam("report");//状态报告
			
			OrderReqInfo info = new OrderReqInfo();
			info.setCpParam(cid+"&"+stype+"&"+scode+"&"+msg+"&"+time);
			info.setMobile(mobile);
			info.setFfId(lid);
			info.setFee(new Integer(fee));
			info.setSpId("only01");
			// 处理订单
			boolean isSuc = false;
			//DELIVRD			表示移动短信成功下行状态
			//0  					表示联通短信成功下行状态
			//1000  				表示彩信成功下行状态
			//DeliveredToTerminal	表示电信成功下行状态
			if ("DELIVRD".equals(report) || "0".equals(report) || "1000".equals(report) ||
					"DeliveredToTerminal".equals(report)) {
				info.setIsSuccess(0);
				isSuc = true;
			} else {
				info.setIsSuccess(-1);
			}
			processOnlyCPOrder(isSuc, info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("OK").end();
		
	}
	
	/**
	 * 处理无透传参数订单
	 */
	private void processOnlyCPOrder(boolean isSuc, OrderReqInfo order) {
		try {
			int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
			String spId = order.getSpId();
			
			CodeOnlyInfo onlyInfo = commonAction.queryCodeOnlyBySpid(spId);
			
			if (onlyInfo != null) {
				if (isSuc) {
					String newCpId = spId;
					int synRadio = onlyInfo.getSynRadio();
					
					logger.info("同步率synRadio："+synRadio);
					CPParam cpp = CommonTool.getCPParam(newCpId,
							synRadio);
					
					AtomicInteger ati = cpp.getAti();
					boolean isSyn = CommonTool.isContain(cpp.getVc(),
							ati.incrementAndGet());
					if (isSyn) {
						if (onlyInfo.getBackUrl() != null && onlyInfo.getBackUrl().length() > 0) {
							// 同步
							String result = notityResult(onlyInfo,order,isSuc);
							
							logger.info("给CP的同步结果："+result);
							if (result.equals("success")) {
								synStatus = ConstantDefine.SYN_STATUS_TRUE;
							} else {
								synStatus = ConstantDefine.SYN_STATUS_FALSE;
							}
						} else {
							synStatus = ConstantDefine.SYN_STATUS_FALSE;
						}
					} else {
						synStatus = ConstantDefine.SYN_STATUS_Buckle;
					}
					
					if (ati.get() == 100)
						ati.set(0);
					cpp.setAti(ati);
//					CommonTool.updateCPParam(cp.getCpId(), cpp);
					CommonTool.updateCPParam(newCpId, cpp);
				} else {
					String result = notityResult(onlyInfo,order,isSuc);
					
					logger.info("给CP的同步结果："+result);
					if (result.equals("success")) {
						synStatus = ConstantDefine.SYN_STATUS_TRUE;
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				}
			}
			order.setSynStatus(synStatus);
			commonAction.addOnlyOrder(order);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
	}
	
	private String notityResult(CodeOnlyInfo info, OrderReqInfo order,boolean isSyn) {
		String result = "";
		String param = "";
		if (info.getReqMethod() == 2) {
			SynJson sj = new SynJson();
			sj.setFfId(order.getFfId());
			sj.setImei(order.getImei());
			sj.setImsi(order.getImsi());
			sj.setFee(order.getFee());
			sj.setIp(order.getIp());
			sj.setStatus(order.getIsSuccess()+"");
			param = JSON.toJSONString(sj);
			result = HttpTool.sendKLWPost(info.getBackUrl(), param, "utf-8", 500);
//			System.out.println(result);
		} else {
			StringBuilder msg = new StringBuilder();
			msg.append("imsi=").append(order.getImsi())
			.append("&imei=").append(order.getImei())
			.append("&ffId=").append(order.getFfId())
			.append("&fee=").append(order.getFee())
			.append("&ip=").append(order.getIp())
			.append("&status=").append(order.getIsSuccess()+"");
			param = msg.toString();
			result = HttpTool.sendGetSetTimeout(info.getBackUrl(), param, "500");
		}
		return result;
	}
	
	
	/**
	 * 深圳点之行RDO 回调通知
	 * http://120.24.88.90/fee/feedzxrdo
	 */
	public void feedzxrdo(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feedzxrdo请求header信息："+request.allHeaders());
			String result = request.getParam("status");
			String ffId = request.getParam("msg");
			ffId = ffId.substring(ffId.length()-16, ffId.length());
			// 处理订单
			boolean isSuc = false;
			if ("DELIVRD".equals(result)) 
				isSuc = true;
			processOrder(ffId, isSuc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/**
	 * 破晓 回调通知
	 * http://smspay.xushihudong.com/fee/feexspx
	 */
	public void feexspx(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexspx请求header信息："+request.allHeaders());
			String result = request.getParam("result");
			String ffId = request.getParam("param");
			ffId = DateTool.getMonth() + "yc" + ffId.substring(2,14);
			// 处理订单
			boolean isSuc = false;
			if ("1".equals(result)) 
				isSuc = true;
			processXSOrder(ffId, isSuc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/**
	 * 深圳点之行RDO 回调通知
	 * http://smspay.xushihudong.com/fee/feexsmm
	 */
	public void feexsmm(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexsmm请求header信息："+request.allHeaders());
			String result = request.getParam("status");
			String ffId = request.getParam("cpparam");
			// 处理订单
			boolean isSuc = false;
			if ("1".equals(result)) 
				isSuc = true;
			processOrder(ffId, isSuc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/**
	 * 悦人MM 回调通知
	 * http://smspay.qygame.cn/fee/feeqyyuerenmm
	 */
	public void feeqyyuerenmm(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feeqyyuerenmm请求header信息："+request.allHeaders());
			String result = request.getParam("status");
			String out_trade_no = request.getParam("msg");
			String ffId = out_trade_no.substring(0,2) + "yc" + out_trade_no.substring(2);
			// 处理订单
			boolean isSuc = false;
			if ("0".equals(result)) 
				isSuc = true;
			processXSOrder(ffId, isSuc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}

	/**
	 * 家医MM 回调通知
	 * http://smspay.xushihudong.com/fee/feexsjyback
	 */
	public void feexsjyback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexsjyback请求header信息："+request.allHeaders());
			String result = request.getParam("paystatus");
			String ffId = request.getParam("tradeId");
			// 处理订单
			boolean isSuc = false;
			if ("1".equals(result)) 
				isSuc = true;
			processOrder(ffId, isSuc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/**
	 * 掌中飞扬RDO 掌中飞扬回调通知
	 * http://118.190.90.1/fee/feertfzsj
	 */
	public void feertfzsj(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feertfzsj请求header信息："+request.allHeaders());
			String result = request.getParam("status");
			String ffId = request.getParam("msg");
			ffId = ffId.substring(ffId.length()-16, ffId.length());
			// 处理订单
			boolean isSuc = false;
			if ("0".equals(result)) 
				isSuc = true;
			processOrder(ffId, isSuc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/**
	 * 掌中飞扬RDO 掌中飞扬回调通知
	 * http://118.190.90.1/fee/feezzfyrdo
	 */
	public void feezzfyrdo(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feezzfyrdo请求header信息："+request.allHeaders());
			String result = request.getParam("stat");
			String ffId = request.getParam("CPParam");
			ffId = ffId.substring(ffId.length()-12, ffId.length());
			// 处理订单
			boolean isSuc = false;
			if ("1".equals(result)) 
				isSuc = true;
			processOrder(ffId, isSuc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/**
	 * 苏州乐麟RDO 苏州乐米回调通知
	 * http://120.24.88.90/fee/feeszlm
	 */
	public void feeszlm(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			String result = request.getParam("result");
			String ffId = request.getParam("extparams");
			
			// 处理订单
			boolean isSuc = false;
			if ("0".equals(result)) 
				isSuc = true;
			processOrder(ffId, isSuc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/**
	 * 厚衡 回调通知
	 * http://smspay.xushihudong.com/fee/feexshhback
	 */
	public void feexshhback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			String result = "0";
			String ffId = request.getParam("cpparam");
			
			// 处理订单
			boolean isSuc = false;
			if ("0".equals(result)) 
				isSuc = true;
			processXSOrder(ffId, isSuc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
		
	
	/**
	 * 苏州全真回调通知
	 * http://smspay.xushihudong.com/fee/feexsszqzback
	 */
	public void feexsszqzback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexsszqzback请求header信息："+request.allHeaders());
			String content = request.body();
			logger.info("feexsszqzback苏州全真回调信息："+content);
			JSONObject myObj = JSONObject.parseObject(content);
			String result = myObj.getString("status");
			String ffId = myObj.getString("info1");
			
			// 处理订单
			boolean isSuc = false;
			if ("0".equals(result)) 
				isSuc = true;
			processXSOrder(ffId, isSuc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/**
	 * 竹语回调通知
	 * http://otherpay.xushihudong.com/fee/feexszyh5back
	 */
	public void feexszyh5back(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexszyh5back请求header信息："+request.allHeaders());
			String result = request.getParam("result");
			String ffId = request.getParam("passInfo");
			
			// 处理订单
			boolean isSuc = false;
			if ("1".equals(result)) 
				isSuc = true;
			processOtherOrder(ffId, isSuc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/**
	 * 云支付回调通知
	 * http://otherpay.xushihudong.com/fee/feexsyzfback
	 */
	public void feexsyzfback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexsyzfback请求header信息："+request.allHeaders());
			String result = request.getParam("result");
			String ffId = request.getParam("custom");
			String order_no = request.getParam("order_no");
			String pay_amt = request.getParam("pay_amt");
			String sign = request.getParam("sign");
			
			if (ffId != null && ffId.length()>16) {
				String channel = ffId.substring(ffId.length()-4,ffId.length());
				ffId = ffId.substring(0,16);
				CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(channel);
				String appsecret = ConstantDefine.LTH5WX_PAY_KEY;
				if (codeInfo != null) {
					String matchRegex = codeInfo.getMatchRegex();
					String[] tmp = matchRegex.split("##");
					appsecret = tmp[1];
				}
				
				StringBuilder signparam = new StringBuilder();
				signparam.append("order_no=").append(order_no)
				.append("&order_amt=").append(pay_amt)
				.append("&key=").append(appsecret);
				String newkey = MD5.md5(signparam.toString(), "utf-8").toLowerCase();
				if (newkey.equals(sign.toLowerCase())) {
					// 处理订单
					boolean isSuc = false;
					if ("1".equals(result)) 
						isSuc = true;
					processOtherOrder(ffId, isSuc);
				}
			}
			
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/**
	 * 订单通知接口
	 * <p>
	 * Market平台 ---> 开发者服务器 开发者服务器
	 * </p>
	 * 
	 * http://smspay.xushihudong.com/fee/feeMM
	 */
	public void feeMM(HttpRequest request, HttpResponse response)
			throws JuiceException {
		logger.info("feeMM请求header信息："+request.allHeaders());
		String content = request.body();
		String ip = getRealIP(request);
		logger.info("read xmlStr: " + content + "; ip:" + ip);
		SyncXMLUtils utils = new SyncXMLUtils();
		try {
			SyncAppOrderReq syncAppOrderReq = XMLUtils.getInstance()
					.parseXML2AppOrderReq(content);
			logger.info("syncAppOrderReq:" + syncAppOrderReq.getAppID());
			
			/*String channelid = syncAppOrderReq.getChannelID();
			if ("2200198494".equals(channelid)) {
				String utl = "http://mmroute.coujiaowa.com/mmroute/dealmm.php";
				String result = HttpTool.sendPost(utl, content);
				logger.info("回传2200198494结果result:"+result);
			} else if ("2200202910".equals(channelid) || "2200207557".equals(channelid)) {
				String utl = "http://x.smilegames.cn:8080/M_Market/SyncAppOrderServlet";
				String result = HttpTool.sendPost(utl, content);
				logger.info("回传2200202910和2200207557结果result:"+result);
			} else if ("2200203416".equals(channelid)) {
				String utl = "http://mmroute.coujiaowa.com/mmroute/dealmm.php";
				String result = HttpTool.sendPost(utl, content);
				logger.info("回传2200203416结果result:"+result);
			} else {
				//
			}*/
			saveOrder(syncAppOrderReq);
			// 返回消息转为vo
			SyncAppOrderResp syncAppOrderResp = new SyncAppOrderResp();
			syncAppOrderResp.setMsgType("SyncAppOrderResp");
			syncAppOrderResp.setVersion("1.0.0");
			syncAppOrderResp.sethRet(0);
			String result = utils.vo2Xml(syncAppOrderResp, "SyncAppOrderResp");
			response.content(result).end();
			
			// 处理订单
			boolean isSuc = false;
			if (syncAppOrderReq.getPrice() > 0 && !"00000000000000000000".equals(syncAppOrderReq.getOrderID())) {
				isSuc = true;
//					commonAction.saveExt(syncAppOrderReq.getExData());
			}
			processOrder(syncAppOrderReq.getExData(), isSuc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}

	}

	/**
	 * 保持数据
	 */
	private void saveOrder(SyncAppOrderReq sao) {
		try {
			MMPayInfo order = new MMPayInfo();
			order.setOrderId(sao.getOrderID());
			order.setTradeId(sao.getTradeID());
			order.setAppId(sao.getAppID());
			order.setChannelId(sao.getChannelID());
			order.setActionId(sao.getActionID());
			order.setActionTime(DateTool.convertString(sao.getActionTime()));
			order.setMsisdn(sao.getMSISDN());
			order.setMsisdnFee(sao.getFeeMSISDN());
			order.setPayCode(sao.getPayCode());
			order.setPrice(sao.getPrice());
			order.setNum(sao.getSubsNumb());
			order.setTotalPrice(sao.getTotalPrice());
			order.setExt(sao.getExData());
			order.setResultStatus(sao.getReturnStatus());

			commonAction.addMMPayInfo(order);
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	/**
	 * 客户端id
	 * <p>
	 * Market平台 ---> 开发者服务器 开发者服务器
	 * </p>
	 * 
	 * http://120.24.88.90/fee/newreport
	 */
	public void newreport(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("请求header信息："+request.allHeaders());
			
			String reqJson = request.getParam("json");
			System.out.println(reqJson.length());
			JSONObject myObj = JSONObject.parseObject(reqJson);
			
			ResSDKReport reqInfo = new ResSDKReport();
			String orderid = myObj.getString("orderid");
			String errcode = myObj.getString("errcode");
			reqInfo.setOrderid(orderid);
			reqInfo.setErrcode(errcode);
			reqInfo.setImsi(myObj.getString("imsi"));
			reqInfo.setImei(myObj.getString("imei"));
			reqInfo.setAppkey(myObj.getString("appkey"));
			reqInfo.setSdkver(myObj.getString("sdkver"));
			reqInfo.setApn(myObj.getString("apn"));
			reqInfo.setOsver(myObj.getString("osver"));
			reqInfo.setProduct(myObj.getString("product"));
			reqInfo.setChannel(myObj.getString("channel"));
			reqInfo.setIsanto(myObj.getString("isanto"));
			
			boolean isSuc = false;
			if ("0".equals(errcode)) isSuc = true;
			commonAction.updateOrder(orderid, isSuc, 0);
			
			commonAction.addResSDKReport(reqInfo);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/**
	 * http://120.24.88.90/fee/getExt
	 */
	public void getExt(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "ok";
		/*String result = "0";
		try {
			logger.info("请求header信息："+request.allHeaders());                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
			
			String ffid = request.getParam("ffid");
			if (ffid != null && ffid.length() > 0) {
				String ext = commonAction.queryExt(ffid);
				if (ffid.equals(ext)) result = "1";
			}
			
		} catch (Exception e) {
			response.content(result).end();
			logger.error(ExceptionTool.getExceptionMessage(e));
		}*/
		response.content(result).end();
	}
	
	/**
	 * 客户端id
	 * <p>
	 * Market平台 ---> 开发者服务器 开发者服务器
	 * </p>
	 * 
	 * http://120.24.88.90/fee/feeSDKback
	 */
	public void feeSDKback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("请求header信息："+request.allHeaders());
			
			String ffId = request.getParam("ffid");
			String sdkResult = request.getParam("sdk_result");
			
			commonAction.updateSDKResult(ffId,sdkResult);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	public void feeSW(HttpRequest request, HttpResponse response)
			throws JuiceException {
		response.content("ok").end();
	}
	
	/**
	 * 乐米支付宝H5回调通知
	 * http://otherpay.xushihudong.com/fee/feezfbH5back
	 */
	public void feezfbH5back(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feezfbH5back请求header信息：" + request.allHeaders());
			
			String content = request.body();
			logger.info("feezfbH5back请求content信息：" + content);
			if (content != null && content.length() > 0) {
				JSONObject myObj = JSONObject.parseObject(content);
				
				String out_trade_no = myObj.getString("out_trade_no");
				String appid = myObj.getString("appid");
				String attch = myObj.getString("attch");
				String channel = myObj.getString("channel");
				String type = myObj.getString("type");
				String result = myObj.getString("result");
				String gooddesc = myObj.getString("gooddesc");
				String total_fee = myObj.getString("total_fee");
				String create_time = myObj.getString("create_time");
				String sign = myObj.getString("sign");
				
				String appsecret = ConstantDefine.LTH5WX_PAY_KEY;
				if (channel != null && channel.length() > 0) {
					CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(channel);
					if (codeInfo != null) {
						String keyword = codeInfo.getKeyword();
						String[] tmp = keyword.split("#");
						appsecret = tmp[1];
					}
				}
				
				/*TreeMap<String, String> treeMap = new TreeMap<String, String>();
				treeMap.put("out_trade_no", out_trade_no);
				treeMap.put("appid",appid);
				treeMap.put("attch", attch);
				treeMap.put("channel", channel);
				treeMap.put("type",type);
				treeMap.put("gooddesc",gooddesc);
				treeMap.put("total_fee",total_fee);
				treeMap.put("result",result);
				treeMap.put("create_time", create_time);
				StringBuilder param = new StringBuilder();
				for (String keString : treeMap.keySet()) {
					param.append(keString).append("=").append(treeMap.get(keString)).append("&");
				}
				String md5param = param.append("key=").append(appsecret).toString().toUpperCase();*/
				
				StringBuilder signparam = new StringBuilder();
				signparam.append("appid=").append(appid)
				.append("&attch=").append(attch)
				.append("&channel=").append(channel)
				.append("&create_time=").append(create_time)
				.append("&gooddesc=").append(gooddesc)
				.append("&out_trade_no=").append(out_trade_no)
				.append("&result=").append(result)
				.append("&total_fee=").append(total_fee)
				.append("&type=").append(type)
				.append("&key=").append(appsecret);
				String signstr = signparam.toString().toUpperCase();
				logger.info("feezfbH5back请求signparam信息：" + signstr);
				String klwsign = MD5.md5(signstr, "utf-8");
				if (sign.equals(klwsign)) {
					logger.info("feezfbH5back签名验证通过：" + klwsign);
					boolean isSuc = false;
					if ("0".equals(result))
						isSuc = true;
					processOtherOrder(attch, isSuc);
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 加冕加回调通知
	 * http://otherpay.xushihudong.com/fee/feejmjQQback
	 */
	public void feejmjQQback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feejmjQQback请求header信息：" + request.allHeaders());
			
			String content = request.body();
			logger.info("feejmjQQback请求content信息：" + content);
			if (content != null && content.length() > 0) {
				Map<String, String> myObj = CommonTool.parseYZ(content);
				
				String app_id = myObj.get("app_id");
				String order_id = myObj.get("order_id");
				String pay_seq = myObj.get("pay_seq");
				String pay_amt = myObj.get("pay_amt");
				String pay_result = myObj.get("pay_result");
//				String result_desc = myObj.getString("result_desc");
				String ext = myObj.get("extends");
				String sign = myObj.get("sign");
				
				String channel = ext;
				String attch = order_id;
				
				String appsecret = ConstantDefine.LTH5WX_PAY_KEY;
				if (channel != null && channel.length() > 0) {
					CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(channel);
					if (codeInfo != null) {
						String keyword = codeInfo.getKeyword();
						String[] tmp = keyword.split("#");
						appsecret = tmp[1];
					}
				}
				
				StringBuilder signparam = new StringBuilder();
				signparam.append("app_id=").append(app_id)
				.append("&order_id=").append(order_id)
				.append("&pay_seq=").append(pay_seq)
				.append("&pay_amt=").append(pay_amt)
				.append("&pay_result=").append(pay_result)
				.append("&key=").append(MD5.md5(appsecret, "utf-8"));
				logger.info("feejmjQQback请求signparam信息：" + signparam.toString());
				String klwsign = MD5.md5(signparam.toString(), "utf-8");
				if (sign.equals(klwsign)) {
					logger.info("feejmjQQback签名验证通过：" + klwsign);
					boolean isSuc = false;
					if ("20".equals(pay_result))
						isSuc = true;
					processOtherOrder(attch, isSuc);
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 金海哲 回调通知
	 * http://otherpay.xushihudong.com/fee/feejhzback
	 */
	public void feejhzback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feejhzback请求header信息：" + request.allHeaders());
			
			String content = request.body();
			logger.info("feejhzback请求content信息：" + content);
			if (content != null && content.length() > 0) {
				Map<String, String> myObj = CommonTool.parseYZ(content);
				
				String ret = myObj.get("ret");
				String msg = myObj.get("msg");
				String sign = myObj.get("sign");
				
				JSONObject jsonobj = JSONObject.parseObject(ret);
				String code = jsonobj.getString("code");
				JSONObject jsonobj1 = JSONObject.parseObject(msg);
				String no = jsonobj1.getString("no");
				
				String attch = no.substring(no.length()-16);
				
				String appsecret = ConstantDefine.JHZ_P_KEY;
				
				StringBuilder signparam = new StringBuilder();
				signparam.append(ret).append("|").append(msg);
				boolean checkresult = RSASignature.verify(signparam.toString(), sign, appsecret);
				
				logger.info("feejhzback请求signparam信息：" + signparam.toString());
				if (checkresult) {
					boolean isSuc = false;
					if ("1000".equals(code))
						isSuc = true;
					processOtherOrder(attch, isSuc);
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("SUCCESS").end();
	}
	
	/**
	 * 迅派 回调通知
	 * http://otherpay.xushihudong.com/fee/feeltxxpzfb
	 */
	public void feeltxxpzfb(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feeltxxpzfb请求header信息：" + request.allHeaders());
			
			String orderid = request.getParam("orderid");
			String opstate = request.getParam("opstate");
			String ovalue = request.getParam("ovalue");
			
			String spid = orderid.substring(0,4);
			String ffId = orderid.substring(4);
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spid);
			if (codeInfo != null) {
				String keyword = codeInfo.getKeyword();
				String[] tmp = keyword.split("#");
				String appsecret = tmp[1];
				StringBuilder md5param = new StringBuilder();
				md5param.append("orderid=").append(orderid)
				.append("&opstate=").append(opstate)
				.append("&ovalue=").append(ovalue);
				String ressign = MD5.md5(md5param + appsecret, "GB2312").toLowerCase();
				String sign = request.getParam("sign");
				String status = opstate;
				String traid = request.getParam("sysorderid");
				if(ressign.equals(sign)){
					boolean isSuc = false;
					if ("0".equals(status)) 
						isSuc = true;
					processOtherOrder(ffId, isSuc, traid);
					result="opstate=0";
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content(result).end();
	}
	
	/**
	 * 乐米微信H5回调通知
	 * http://otherpay.xushihudong.com/fee/feexsszlmH5back
	 */
	public void feexsszlmH5back(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexsszlmH5back请求header信息：" + request.allHeaders());
			
			String content = request.body();
			logger.info("feexsszlmH5back请求content信息：" + content);
			if (content != null && content.length() > 0) {
				JSONObject myObj = JSONObject.parseObject(content);
				
				String out_trade_no = myObj.getString("out_trade_no");
				String appid = myObj.getString("appid");
				String attch = myObj.getString("attch");
				String type = myObj.getString("type");
				String result = myObj.getString("result");
				String channel = myObj.getString("channel");
				String gooddesc = myObj.getString("gooddesc");
				String total_fee = myObj.getString("total_fee");
				String create_time = myObj.getString("create_time");
				String sign = myObj.getString("sign");
				
				String transaction_id = myObj.getString("transaction_id");
				
				String appsecret = ConstantDefine.LTH5WX_PAY_KEY;
				if (channel != null && channel.length() > 0) {
					CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(channel);
					if (codeInfo != null) {
						String matchRegex = codeInfo.getMatchRegex();
						String[] tmp = matchRegex.split("##");
						appsecret = tmp[1];
					}
				}
				
				StringBuilder signparam = new StringBuilder();
				signparam.append("appid=").append(appid)
				.append("&attch=").append(attch)
				.append("&channel=").append(channel)
				.append("&create_time=").append(create_time)
				.append("&gooddesc=").append(gooddesc)
				.append("&out_trade_no=").append(out_trade_no)
				.append("&result=").append(result)
				.append("&total_fee=").append(total_fee)
				.append("&type=").append(type)
				.append("&key=").append(appsecret);
				String signstr = signparam.toString().toUpperCase();
				logger.info("feexsszlmH5back请求signparam信息：" + signstr);
				String klwsign = MD5.md5(signstr, "utf-8");
//				if (sign.equals(klwsign)) {
				logger.info("feexsszlmH5back签名验证通过：" + klwsign);
				boolean isSuc = false;
				if ("0".equals(result))
					isSuc = true;
				processOtherOrder(attch, isSuc, transaction_id);
//				}
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 阿信微信H5回调通知
	 * http://otherpay.xushihudong.com/fee/feexsaxH5back
	 */
	public void feexsaxH5back(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexsaxH5back请求header信息：" + request.allHeaders());
			
			String content = request.body();
			logger.info("feexsaxH5back请求content信息：" + content);
			if (content != null && content.length() > 0) {
				JSONObject myObj = JSONObject.parseObject(content);
				Map<String, String> resMap = new HashMap<String, String>();
				for (Map.Entry<String, Object> entry : myObj.entrySet()) {
					String value = entry.getValue() + "";
					resMap.put(entry.getKey(), value);
				}
				
				String ressign = resMap.get("sign");
				String channel = resMap.get("extra");
				resMap.remove("sign");
				String appsecret = ConstantDefine.LTH5WX_PAY_KEY;
				if (channel != null && channel.length() > 0) {
					CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(channel);
					if (codeInfo != null) {
						String keyword = codeInfo.getKeyword();
						String[] tmp = keyword.split("#");
						appsecret = tmp[1];
					}
				}
				String resbacksign = PayUtil.getSign(resMap, appsecret);
				logger.info("feexsaxH5back签名结果：" + resbacksign);
				if (resbacksign.equals(ressign)) {
					boolean isSuc = false;
					String status = resMap.get("status");
					String ffId = resMap.get("outorderno");
					String transaction_id = resMap.get("outtransactionid");
					if ("1".equals(status))
						isSuc = true;
					processOtherOrder(ffId, isSuc, transaction_id);
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 朗天H5回调通知
	 * http://120.24.88.90/fee/feeH5back
	 */
	public void feeH5back(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feeH5back请求header信息：" + request.allHeaders());
			
			String content = request.body();
			logger.info("feeH5back请求content信息：" + content);
			Map<String, String> repmap = CommonTool.parseYZ(content);
			if (repmap != null && repmap.size() > 0) {
				String transdata = repmap.get("transdata");
				String sign = repmap.get("sign");
//				String signtype = repmap.get("signtype");
				JSONObject myObj = JSONObject.parseObject(transdata);
				
				String appid = myObj.getString("appid");
				String cporderid = myObj.getString("cporderid");
				String currency = myObj.getString("currency");
				String fact_money = myObj.getString("fact_money");
				String feetype = myObj.getString("feetype");
				String goodsid = myObj.getString("goodsid");
				String money = myObj.getString("money");
				String paytype = myObj.getString("paytype");
				String pc_priv_info = myObj.getString("pc_priv_info");
				String pcuserid = myObj.getString("pcuserid");
				String result = myObj.getString("result");
				String transid = myObj.getString("transid");
				String transtime = myObj.getString("transtime");
				String transtype = myObj.getString("transtype");
				
				String appsecret = ConstantDefine.LTH5WX_PAY_KEY;
				if (pc_priv_info != null && pc_priv_info.length() > 0) {
					CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(pc_priv_info);
					if (codeInfo != null) {
						String keyword = codeInfo.getKeyword();
						String[] tmp = keyword.split("#");
						appsecret = tmp[1];
					}
				}
					
				StringBuilder signparam = new StringBuilder();
				signparam.append("appid=").append(appid)
				.append("&cporderid=").append(cporderid)
				.append("&currency=").append(currency)
				.append("&fact_money=").append(fact_money)
				.append("&feetype=").append(feetype)
				.append("&goodsid=").append(goodsid)
				.append("&money=").append(money)
				.append("&paytype=").append(paytype)
				.append("&pc_priv_info=").append(pc_priv_info)
				.append("&pcuserid=").append(pcuserid)
				.append("&result=").append(result)
				.append("&transid=").append(transid)
				.append("&transtime=").append(transtime)
				.append("&transtype=").append(transtype)
				.append("&key=").append(appsecret);
				logger.info("feeH5back请求signparam信息：" + signparam.toString());
				String klwsign = MD5.md5(signparam.toString(), "utf-8");
				if (sign.equals(klwsign)) {
					boolean isSuc = false;
					if ("1".equals(result))
						isSuc = true;
					processOtherOrder(cporderid, isSuc);
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("SUCCESS").end();
	}
	
	/**
	 * 高汇通回调通知
	 * http://120.24.88.90/fee/feeGHTback
	 */
	public void feeGHTback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feeGHTback请求header信息：" + request.allHeaders());
			
			String content = request.body();
			logger.info("feeGHTback请求content信息：" + content);
			JSONObject myObj = JSONObject.parseObject(content);
			
			String isSuccess = myObj.getString("resp_code");
			String ffId = myObj.getString("order_no");
			
			boolean isSuc = false;
			if ("0".equals(isSuccess))
				isSuc = true;
			processOtherOrder(ffId, isSuc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 
	 * http://120.24.88.90/fee/feebacklp
	 */
	public void feebacklp(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feebacklp请求header信息：" + request.allHeaders());
			
			String ffId = request.getParam("extParm");
			String isSuccess = request.getParam("result");
			
			boolean isSuc = false;
			if ("0".equals(isSuccess))
				isSuc = true;
			processOrder(ffId, isSuc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 
	 * http://120.24.88.90/fee/feertzyback
	 */
	public void feertzyback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feertzyback请求header信息：" + request.allHeaders());
			
			String ffId = request.getParam("ccpara");
			String isSuccess = request.getParam("delivrd");
			
			boolean isSuc = false;
			if ("delivrd".equals(isSuccess.toLowerCase()))
				isSuc = true;
			processOrder(ffId, isSuc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 润土12306注册
	 * http://118.190.90.1/fee/feertzcback
	 */
	public void feertzcback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feertzcback请求header信息：" + request.allHeaders());
			
			String ffId = request.getParam("order");
			String isSuccess = request.getParam("status");
			
			boolean isSuc = false;
			if ("1".equals(isSuccess.toLowerCase()))
				isSuc = true;
			processXSOrder(ffId, isSuc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 
	 * http://120.24.88.90/fee/feertzrback
	 */
	public void feertzrback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feertzrback请求header信息：" + request.allHeaders());
			
			String ffId = request.getParam("appid");
			String isSuccess = request.getParam("ServiceAction");
			ffId = ffId.substring(ffId.length()-16,ffId.length());
			
			boolean isSuc = false;
			if ("0".equals(isSuccess))
				isSuc = true;
			processOrder(ffId, isSuc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 润土兆荣联通
	 * http://118.190.90.1/fee/feertzrltback
	 */
	public void feertzrltback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feertzrltback请求header信息：" + request.allHeaders());

			String ffId = request.getParam("transParam");
			String isSuccess = request.getParam("hRet");
			ffId = ffId.substring(ffId.length()-16,ffId.length());

			boolean isSuc = false;
			if ("0".equals(isSuccess))
				isSuc = true;
			processOrder(ffId, isSuc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}

		response.content("ok").end();
	}

	/**
	 * 迅鸿联通 http://smspay.xushihudong.com/fee/feexhltback
	 */
	public void feexhback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			// 解析XML数据
			logger.info("feexhltback请求header信息：" + request.allHeaders());
			String content = request.body();
			logger.info("feexhltback请求body信息：" + content);
			Map<String, String> restmap = XmlUtil.xmlParse(content);
			logger.info("feexhltback请求header信息：" + request.allHeaders());
			String ffId = restmap.get("transParam");
			String isSuccess = restmap.get("status");
			boolean isSuc = false;
			if ("0".equals(isSuccess))
				isSuc = true;
			processOrder(ffId, isSuc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("<?xml version=\"1.0\" encoding=\"utf-8\"?><ROOT><returnCode >0</returnCode></ROOT>").end();
	}
	
	
	public void feeNotity(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feeNotity请求header信息：" + request.allHeaders());
			
			String ffId = request.getParam("ffid");
			String isSuccess = request.getParam("issuccess");
			
			boolean isSuc = false;
			if ("1".equals(isSuccess))
				isSuc = true;
			processOrder(ffId, isSuc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	
	public void feeForward(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feeForward请求header信息：" + request.allHeaders());
			
			String json = request.getParam("json");
			logger.info("feeForward请求json：" + json);
			
			commonAction.reqForward(json);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	

	/**
	 * QQ注册同步接口
	 * http://120.24.88.90/fee/feexsqqzcback
	 */
	public void feexsqqzcback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			String ffId = request.getParam("cpParam");
			
			// 处理订单
			boolean isSuc = true;
			Thread.sleep(1000);
			processOrder(ffId, isSuc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
	}
	
	/**
	 * QQ注册同步接口
	 * http://120.24.88.90/fee/feeqqzcback
	 */
	public void feeqqzcback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			String ffId = request.getParam("ccpara");
			
			// 处理订单
			boolean isSuc = true;
			
			processOrder(ffId, isSuc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
	}
	
	/**
	 * 微信H5支付同步
	 * http://otherpay.xushihudong.com/fee/feewxh5back
	 */
	public void feewxh5back(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "error";
		try {
			logger.info("feewxh5back请求header信息：" + request.allHeaders());
			String content = request.body();
			logger.info("feewxh5back请求body信息：" + content);
			Map<String, String> restmap = XmlUtil.xmlParse(content);
			// 处理订单
			boolean isSuc = false;
			String ffId = restmap.get("out_trade_no"); // 商户订单号
			// 通过商户订单判断是否该订单已经处理 如果处理跳过 如果未处理先校验sign签名 再进行订单业务相关的处理
			
			String sing = restmap.get("sign"); // 返回的签名
			restmap.remove("sign");
			String channel = restmap.get("attach");
			String traid = restmap.get("transaction_id");
			
			String appsecret = ConstantDefine.LTH5WX_PAY_KEY;
			if (channel != null && channel.length() > 0) {
				CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(channel);
				if (codeInfo != null) {
					String keyword = codeInfo.getKeyword();
					String[] tmp = keyword.split("#");
					appsecret = tmp[1];
				}
			}
			
			String signnow = PayUtil.getSign(restmap, appsecret);
			if (signnow.equals(sing) && "SUCCESS".equals(restmap.get("return_code"))) {
				isSuc = true;
			}
			processOtherOrder(ffId, isSuc, traid);
			Map<String, String> map = new HashMap<String, String>();
			map.put("return_code", "SUCCESS");
			map.put("return_msg", "OK");
			result = XmlUtil.xmlFormat(map,true);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	
	/**
	 * 微信H5支付同步
	 * http://otherpay.xushihudong.com/fee/test01
	 *//*
	public void test01(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "error";
		try {
			logger.info("feewxh5back请求header信息：" + request.allHeaders());
			String content = request.body();
			logger.info("feewxh5back请求body信息：" + content);
			Map<String, String> restmap = XmlUtil.xmlParse(content);
			// 处理订单
			boolean isSuc = true;
			String ffId = restmap.get("out_trade_no"); // 商户订单号
			// 通过商户订单判断是否该订单已经处理 如果处理跳过 如果未处理先校验sign签名 再进行订单业务相关的处理
			
			String sing = restmap.get("sign"); // 返回的签名
			restmap.remove("sign");
			String channel = restmap.get("attach");
			String traid = restmap.get("transaction_id");
			
			processOtherOrder(ffId, isSuc, traid);
			Map<String, String> map = new HashMap<String, String>();
			map.put("return_code", "SUCCESS");
			map.put("return_msg", "OK");
			result = XmlUtil.xmlFormat(map,true);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}*/
	
	/**
	 * xshd微信公众号支付同步
	 * http://120.24.88.90/fee/feexshdwxgzhback
	 */
	public void feexshdwxgzhback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feexshdwxgzhback请求header信息：" + request.allHeaders());
			String status = request.getParam("result_code");
//			String mch_id = request.getParam("mch_id");
			String out_trade_no = request.getParam("out_trade_no");
			String feepoint = request.getParam("feepoint");
//			String fee = request.getParam("fee");
//			String out_transaction_id = request.getParam("out_transaction_id");
			String sign = request.getParam("sign");
			// 处理订单
			boolean isSuc = false;
			if ("0".equals(status)) {
				int len = out_trade_no.length();
				String spId = out_trade_no.substring(len-4,len);
				CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spId);
				if (codeInfo != null) {
					String keyword = codeInfo.getKeyword();
					String[] tmp = keyword.split("#");
					String appsecret = tmp[1];
					
					StringBuilder signparam = new StringBuilder();
					signparam.append(feepoint).append("&")
					.append(out_trade_no).append("&")
					.append(appsecret);
					String signnow = MD5Tool.toMD5Value32(signparam.toString());
					logger.info("feexshdwxgzhback需签名字符串：" + signparam.toString());
					logger.info("feexshdwxgzhback生成签名：" + signnow);
					
					String ffId = out_trade_no.substring(tmp[0].length(),tmp[0].length()+16);
					if (signnow.equals(sign)) {
						result = "success";
						isSuc = true;
					}
					processXSOrder(ffId, isSuc);
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	
	/**
	 * 海南支付宝支付同步
	 * http://120.24.88.90/fee/feehnzfbback
	 */
	public void feehnzfbback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feehnzfbback请求header信息：" + request.allHeaders());
			String content = request.body();
			logger.info("feehnzfbback请求content信息：" + content);
			Map<String, String> restmap = CommonTool.parseYZ(content);
			// 处理订单
			boolean isSuc = false;
			String payResult = restmap.get("payResult");
			if ("1".equals(payResult)) {
				// 订单支付成功 业务处理
				String merchantOutOrderNo = restmap.get("merchantOutOrderNo"); // 商户订单号
				// 通过商户订单判断是否该订单已经处理 如果处理跳过 如果未处理先校验sign签名 再进行订单业务相关的处理
				String ffId = merchantOutOrderNo.substring(0,16);
				String spId = "sd" + merchantOutOrderNo.substring(16,18);
				String sign = restmap.get("sign"); // 返回的签名
				CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spId);
				if (codeInfo != null) {
					String keyword = codeInfo.getKeyword();
					String[] tmp = keyword.split("#");
					String appsecret = tmp[1];
					restmap.remove("sign");
					restmap.remove("key");
					String signnow = PayUtil.getSign(restmap, appsecret);
					logger.info("appsecret:"+appsecret);
					logger.info("sign:"+sign);
					logger.info("signnow:"+signnow);
					if (signnow.equals(sign.toUpperCase())) {
						result = "success";
						isSuc = true;
					}
					processOrder(ffId, isSuc);
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	
	/**
	 * 加冕加支付宝扫码支付同步
	 * http://otherpay.xushihudong.com/fee/feexszfbsmback
	 */
	public void feexszfbsmback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feexszfbsmback请求header信息：" + request.allHeaders());
			String content = request.body();
			logger.info("feexszfbsmback请求body信息：" + content);
			Map<String, String> restmap = XmlUtil.xmlParse(content);
			// 处理订单
			boolean isSuc = false;
			result = "success";
			if ("0".equals(restmap.get("status")) && "0".equals(restmap.get("result_code"))) {
				// 订单支付成功 业务处理
				String out_trade_no = restmap.get("out_trade_no"); // 商户订单号
				String ffId = out_trade_no.substring(4);
				String attach = out_trade_no.substring(0, 4);
				// 通过商户订单判断是否该订单已经处理 如果处理跳过 如果未处理先校验sign签名 再进行订单业务相关的处理
				
				String sing = restmap.get("sign"); // 返回的签名
				CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(attach);
				if (codeInfo != null) {
					String keyword = codeInfo.getKeyword();
					String[] tmp = keyword.split("#");
					String appsecret = tmp[1];
					restmap.remove("sign");
					String signnow = PayUtil.getSign(restmap, appsecret);
					if (signnow.equals(sing) && "0".equals(restmap.get("pay_result"))) {
						isSuc = true;
					}
					String traid = restmap.get("transaction_id");
					processOtherOrder(ffId, isSuc, traid);
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	
	/**
	 * 赵飞 支付宝扫码支付同步
	 * http://otherpay.xushihudong.com/fee/feexszfbzfback
	 */
	public void feexszfbzfback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feexszfbzfback请求header信息：" + request.allHeaders());
			String content = request.body();
			logger.info("feexszfbzfback请求body信息：" + content);
			Map<String, String> restmap = CommonTool.parseYZ(content);
			// 处理订单
			boolean isSuc = false;
			result = "ok";
			// 订单支付成功 业务处理
			String ffId = restmap.get("pay_product_name"); // 商户订单号
			// 通过商户订单判断是否该订单已经处理 如果处理跳过 如果未处理先校验sign签名 再进行订单业务相关的处理
			
			String sing = restmap.get("sign"); // 返回的签名
			String attach = restmap.get("pay_remark");
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(attach);
			if (codeInfo != null) {
				String keyword = codeInfo.getKeyword();
				String[] tmp = keyword.split("#");
				String appsecret = tmp[1];
				restmap.remove("sign");
				String signnow = PayUtil.getSign(restmap, appsecret);
				if (signnow.equals(sing)) {
					isSuc = true;
				}
				String traid = restmap.get("pay_order_id");
				processOtherOrder(ffId, isSuc, traid);
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	
	
	/**
	 * 加冕加支付宝扫码支付同步
	 * http://otherpay.xushihudong.com/fee/feexsqmdyback
	 */
	public void feexsqmdyback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feexsqmdyback请求header信息：" + request.allHeaders());
			String content = request.body();
			logger.info("feexsqmdyback请求body信息：" + content);
			Map<String, Object> restmap = CommonTool.parseObj(content);
			// 处理订单
			boolean isSuc = false;
			result = "SUCCESS";
			String sign = (String)restmap.get("sign"); // 返回的签名
			String attach = (String)restmap.get("remark");
			String result_code = (String)restmap.get("result_code");
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(attach);
			if (codeInfo != null) {
				String keyword = codeInfo.getKeyword();
				String[] tmp = keyword.split("#");
				String appsecret = tmp[1];
//				restmap.put("appKey", tmp[0]);
//				restmap.put("sign", sign);
				boolean flag = PaySignUtil.checkSign(restmap,appsecret);
				if (flag && "200".equals(result_code)) {
					isSuc = true;
				}
				String traid = (String)restmap.get("order_num");
				String ffId = (String)restmap.get("buss_order_num");
				processOtherOrder(ffId, isSuc, traid);
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	/**
	 * 外放-加冕-QQ扫码回调通知
	 * http://smspay.xushihudong.com/fee/feexsjmback
	 */
	public void feexsjmback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feexsjmback加冕-QQ扫码请求header信息："+request.allHeaders());
			String content = request.body();
			logger.info("feexsjmback请求body信息：" + content);
			Map<String,String> restmap = CommonTool.parseYZ(content);
			String orderNo = restmap.get("orderNo");
			String spid = orderNo.substring(0,4);
			String ffId = orderNo.substring(4);
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spid);
			if (codeInfo != null) {
				String keyword = codeInfo.getKeyword();
				String[] tmp = keyword.split("#");
				String appsecret = tmp[3];
				String param=PaySignUtil.getParamStr2(restmap)+"&"+appsecret;
				String sign = restmap.get("sign");
				String status = restmap.get("status");
				String traid = restmap.get("flowNo");
				String succAmount=restmap.get("succAmount");
				if(MD5.md5(param, "utf-8").toUpperCase().equals(sign)){
					boolean isSuc = false;
					if ("1".equals(status)&&succAmount!=null) 
						isSuc = true;
					processOtherOrder(ffId, isSuc, traid);
					result="{\"code\":\"SUCCESS\",\"msg\":\"ok\"}";
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	/**
	 * 利兴回调通知
	 * http://ltxpay.huizhousenfeng.com/fee/feelx
	 */
	public void feelx(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feelx请求header信息："+request.allHeaders());
			String content = request.body();
			logger.info("feelx请求body信息：" + content);
			JSONObject jsonobj = JSONObject.parseObject(content);
			Map<String,String> restmap = CommonTool.parseJson(jsonobj);
			
			String out_trade_no = restmap.get("out_trade_no");
			String spid = out_trade_no.substring(0,4);
			String ffId = out_trade_no.substring(4);
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spid);
			if (codeInfo != null) {
				String keyword = codeInfo.getKeyword();
				String[] tmp = keyword.split("#");
				
				String appsecret = tmp[1];
				String sign = restmap.get("sign");
				restmap.remove("sign");
				String resSign = PayUtil.getSign(restmap, appsecret).toUpperCase();
				boolean checksign = resSign.equals(sign);
				
				String status = restmap.get("status");
				String traid = restmap.get("traid");
				if(checksign){
					boolean isSuc = false;
					if ("1".equals(status)) 
						isSuc = true;
					processOtherOrder(ffId, isSuc, traid);
					result="ok";
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	/**
	 * 南京众誉回调通知
	 * http://ltxpay.huizhousenfeng.com/fee/feejmqnjzyqq
	 */
	public void feejmqnjzyqq(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feejmqnjzyqq南京众誉请求header信息："+request.allHeaders());
			String content = request.body();
			logger.info("feejmqnjzyqq南京众誉请求body信息：" + content);
			JSONObject jsonobj = JSONObject.parseObject(content);
			Map<String,String> restmap = CommonTool.parseJson(jsonobj);
			
			String orderNo = restmap.get("orgOrderNo");
			String spid = orderNo.substring(0,4);
			String ffId = orderNo.substring(4);
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spid);
			if (codeInfo != null) {
				
				String resSign = restmap.get("signature");
				restmap.remove("signature");
				resSign = URLDecoder.decode(resSign, "utf-8");
				String resSignparam = PaySignUtil.getParamStr2(restmap);
				boolean checksign = RSASignature.doCheck(resSignparam,
						resSign, ConstantDefine.NJZY_PUB_KEY, "utf-8");
				
				String status = restmap.get("paySt");
				String traid = restmap.get("orderNo");
				if(checksign){
					boolean isSuc = false;
					if ("2".equals(status)) 
						isSuc = true;
					processOtherOrder(ffId, isSuc, traid);
					result="SUCCESS";
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	/**
	 * 智慧银联回调通知
	 * http://ltxpay.huizhousenfeng.com/fee/feeltxzhylkj
	 */
	public void feeltxzhylkj(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feeltxzhylkj请求header信息："+request.allHeaders());
			String content = request.body();
			logger.info("feeltxzhylkj请求body信息：" + content);
			Map<String,String> restmap = CommonTool.parseAll(content);
			String signMethod = restmap.get("signMethod");
			String signature = restmap.get("signature");
			String respMsg = restmap.get("respMsg");
			respMsg = new String(Base64.decode(respMsg));
			restmap.remove("signMethod");
			restmap.remove("signature");
			restmap.put("respMsg", respMsg);
			
			String orderNo = restmap.get("merOrderId");
			String spid = orderNo.substring(0,4);
			String ffId = orderNo.substring(4);
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spid);
			if (codeInfo != null) {
				String keyword = codeInfo.getKeyword();
				String[] tmp = keyword.split("#");
				String appsecret = tmp[1];
				String signParam = PaySignUtil.getParamStrAll(restmap);
				String sign = SignUtil.sign(signMethod, signParam, appsecret, "UTF-8");
				String status = restmap.get("respCode");
				String traid = null;
				if(signature.equals(sign)){
					boolean isSuc = false;
					if ("1001".equals(status)) {
						isSuc = true;
						traid = restmap.get("txnTime");
					}
					processOtherOrder(ffId, isSuc, traid);
					result="success";
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	/**
	 * zwsf叶信支付宝回调通知
	 * http://47.75.86.221/fee/feeszrtyxzfb
	 */
	public void feeszrtyxzfb(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feeszrtyxzfb请求header信息："+request.allHeaders());
			String content = request.body();
			logger.info("feeszrtyxzfb请求body信息：" + content);
			Map<String,Object> restmap = CommonTool.parseJson2(JSONObject.parseObject(content));
			
			
			String ffId = restmap.get("outorderno") + "";
			String spid = restmap.get("extra") + "";
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spid);
			if (codeInfo != null) {
				String keyword = codeInfo.getKeyword();
				String[] tmp = keyword.split("#");
				String appsecret = tmp[1];
				String status = restmap.get("status") + "";
				String traid = restmap.get("outtransactionid") + "";
				if(PaySignUtil.checkSign(restmap, appsecret)){
					boolean isSuc = false;
					if ("1".equals(status)) 
						isSuc = true;
					processOtherOrder(ffId, isSuc, traid);
					result="SUCCESS";
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	/**
	 * 博腾微信h5回调通知
	 * http://ltxpay.huizhousenfeng.com/fee/feeltxbtwxh5
	 */
	public void feeltxbtwxh5(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feeltxbtwxh5请求header信息："+request.allHeaders());
			String orderNo = request.getParam("siteorderid");
			String siteid = request.getParam("siteid");
			String typeid = request.getParam("typeid");
			String status = request.getParam("stat");
			String paymoney = request.getParam("paymoney");
			String myorderid = request.getParam("myorderid");
			String over_datetime = request.getParam("over_datetime");
			String md5key = request.getParam("md5key");
			
			String spid = orderNo.substring(0,4);
			String ffId = orderNo.substring(4);
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spid);
			if (codeInfo != null) {
				String keyword = codeInfo.getKeyword();
				String[] tmp = keyword.split("#");
				String appsecret = tmp[1];
				
				StringBuilder signParam = new StringBuilder();
				signParam.append(siteid).append(typeid).append(status)
				.append(paymoney).append(myorderid).append(orderNo)
				.append(over_datetime).append(appsecret);
				String sign = MD5.md5(signParam.toString(), "utf-8");
				
				String traid = myorderid;
				if(md5key.equals(sign)){
					boolean isSuc = false;
					if ("DELIVRD".equals(status)) 
						isSuc = true;
					processOtherOrder(ffId, isSuc, traid);
					result="ok";
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	/**
	 * 长盈QQ回调通知
	 * http://smspay.xushihudong.com/fee/feeltxcyqq
	 */
	public void feeltxcyqq(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feeltxcyqq请求header信息："+request.allHeaders());
			String content = request.body();
			logger.info("feeltxcyqq请求body信息：" + content);
			Map<String,String> restmap = CommonTool.parseYZ(content);
			String ressign = restmap.get("sign");
			restmap.remove("sign");
			
			
			String orderNo = restmap.get("out_trade_no");
			String spid = orderNo.substring(0,4);
			String ffId = orderNo.substring(4);
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spid);
			if (codeInfo != null) {
				String keyword = codeInfo.getKeyword();
				String[] tmp = keyword.split("#");
				String appsecret = tmp[1];
				String sign = PayUtil.getSign(restmap, appsecret);
				String status = restmap.get("order_status");
				String traid = restmap.get("trade_no");
				if(ressign.equals(sign)){
					boolean isSuc = false;
					if ("3".equals(status)) 
						isSuc = true;
					processOtherOrder(ffId, isSuc, traid);
					result="SUCCESS";
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	/**
	 * 高汇通回调通知
	 * http://smspay.xushihudong.com/fee/feeltxghtzfb
	 */
	public void feeltxghtzfb(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feeltxghtzfb请求header信息："+request.allHeaders());
			String content = request.body();
			logger.info("feeltxghtzfb请求body信息：" + content);
			JSONObject jsonobj = JSONObject.parseObject(content);
			Map<String,String> restmap = CommonTool.parseJson(jsonobj);
			String ressign = restmap.get("field128");
			restmap.remove("field128");
			
			pkg8583 pkg = new pkg8583();
			pkg.setField003(restmap.get("field003"));
			pkg.setField041(restmap.get("field041"));
			pkg.setField042(restmap.get("field042"));
			pkg.setField055(restmap.get("field055"));
			pkg.setField062(restmap.get("field062"));
			pkg.setTxcode(restmap.get("txcode"));
			pkg.setTxdate(restmap.get("txdate"));
			pkg.setTxtime(restmap.get("txtime"));
			pkg.setVersion(restmap.get("version"));
			
			String field055 = restmap.get("field055");
			String[] tmp1 = field055.split("\\|");
			String orderNo = tmp1[0];
			String spid = orderNo.substring(0,4);
			String ffId = orderNo.substring(4);
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spid);
			if (codeInfo != null) {
				String keyword = codeInfo.getKeyword();
				String[] tmp = keyword.split("#");
				String appsecret = tmp[1];
				String strForSign=pkg.toSignStr()+appsecret;
				String sign=Util.stringToMD5(strForSign,"utf-8").toUpperCase();
				sign = sign.substring(0,16);
				String status = tmp1[1];
				String traid = restmap.get("field062");
				if(ressign.equals(sign)){
					boolean isSuc = false;
					if ("00".equals(status)) 
						isSuc = true;
					processOtherOrder(ffId, isSuc, traid);
					result="SUCCESS";
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	/**
	 * 迅捷聚合支付回调通知
	 * http://smspay.xushihudong.com/fee/feexsxjjhback
	 */
	public void feexsxjjhback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feexsxjjhback请求header信息："+request.allHeaders());
			String content = request.body();
			logger.info("feexsxjjhbackMMSP回调信息："+content);
			JSONObject myObj = JSONObject.parseObject(content);
			String sign=myObj.getString("sign");
			String orderNo=myObj.getString("merch_order_no");
			String spid = orderNo.substring(0,4);
			String ffId = orderNo.substring(4);
			// 处理订单
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spid);
			if(codeInfo!=null){
				String key = codeInfo.getKeyword().split("#")[5];
				Map<String, String> resMap = new HashMap<String, String>();
				for (Map.Entry<String, Object> entry : myObj.entrySet()) {
					String value = entry.getValue() + "";
					resMap.put(entry.getKey(), value);
				}
				resMap.remove("sign");
				String mySign = PayUtil.getSign(resMap,key).toLowerCase();
				
				if(mySign.equals(sign)){
					boolean isSuc = false;
					String status=myObj.getString("retcode");
					if ("00".equals(status)) 
					isSuc = true;
					String traid=myObj.getString("up_channel_order_no");
					processOtherOrder(ffId, isSuc, traid);
					result="success";
				}
			}				
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();}
	/**
	 * mmsp回调通知
	 * http://smspay.xushihudong.com/fee/feexsmmspback
	 */
	public void feexsmmspback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feexsmmspback请求header信息："+request.allHeaders());
			String content = request.body();
			logger.info("feexsmmspbackMMSP回调信息："+content);
			JSONObject myObj = JSONObject.parseObject(content);
			String sign=myObj.getString("Sign");
			String Param=myObj.getString("Body");
			JSONObject bodyObj=JSONObject.parseObject(Param);
			String orderNo=bodyObj.getString("MERORDERID");
			String spid = orderNo.substring(0,4);
			String ffId = orderNo.substring(4);
			// 处理订单
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spid);
			if(codeInfo!=null){
				String match = codeInfo.getMatchRegex();
				String[] tmp = match.split("#");
				String md5Param=Param+"&key="+tmp[1];
				if(MD5.md5(md5Param,"utf-8").toUpperCase().equals(sign)){
					boolean isSuc = false;
					String status=bodyObj.getString("STATUS");
					if ("1".equals(status)) 
						isSuc = true;
					String traid=bodyObj.getString("ORDERNO");
					processOtherOrder(ffId, isSuc, traid);
					result="SUCCESS";
				}
			}				
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	/**
	 * 蒋道理回调通知
	 * http://smspay.xushihudong.com/fee/feexsjdlback
	 */
	public void feexsjdlback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feexsjdlback请求header信息："+request.allHeaders());
			String content = request.body();
			logger.info("feexsjdlback蒋道理回调信息："+content);
			JSONObject myObj = JSONObject.parseObject(content);
			String amount=myObj.getString("amount");
			String app_id=myObj.getString("app_id");
			String extend=myObj.getString("extends");
			String ffId = myObj.getString("mc_order");
			String traid = myObj.getString("ppay_order");
			String status = myObj.getString("status");
			String sign=myObj.getString("sign");
			// 处理订单
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(extend);
			if(codeInfo!=null){
				StringBuilder param=new StringBuilder();
				param.append("amount=").append(amount)
				.append("&app_id=").append(app_id)
				.append("&extends=").append(extend)
				.append("&mc_order").append(ffId)
				.append("&ppay_order=").append(traid)
				.append("&status=").append(status);
				String newSign=HMacMD5.getHmacMd5Str(codeInfo.getKeyword().split("#")[2],param.toString());
				if(sign.equals(newSign)){
					boolean isSuc = false;
					if ("0".equals(status)) 
						isSuc = true;
					processOtherOrder(ffId, isSuc, traid);
					result="ok";
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	
	public void feeqyback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feeqyback请求header信息："+request.allHeaders());
			String content = request.body();
			logger.info("feeqyback回调信息："+content);
			Map<String, Object> map = CommonTool.parseObj(content);
			
			String remark = (String)map.get("remark");
			
			// 处理订单
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(remark);
			if(codeInfo!=null){
				String keyword = codeInfo.getKeyword();
				String[] tmp = keyword.split("#");
				String appsecret = tmp[1];
				Boolean isOK = PaySignUtil.checkSign(map, appsecret);
				if(isOK){
					boolean isSuc = false;
					String status = (String)map.get("result_code");
					if ("200".equals(status)) 
						isSuc = true;
					String traid = (String)map.get("order_num");
					String ffid = (String)map.get("buss_order_num");
					processOtherOrder(ffid, isSuc, traid);
					result="SUCCESS";
				}
			}				
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	/**
	 * 奇客QQ钱包sdE1
	 * http://ltxpay.huizhousenfeng.com/fee/feejmqqkqq166
	 * @param request
	 * @param response
	 * @throws JuiceException
	 */
	public void feejmqqkqq166(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feejmqqkqq166请求header信息："+request.allHeaders());
			String content = request.body();
			logger.info("feejmqqkqq166回调信息："+content);
			JSONObject jsonobj1 = JSONObject.parseObject(content);
			String resbody = jsonobj1.getString("Body");
			
			JSONObject jsonobj = JSONObject.parseObject(resbody);
			
		
			
			
			String out_trade_no = jsonobj.getString("MERORDERID");
			String spid = out_trade_no.substring(0,4);
			String ffid = out_trade_no.substring(4);
			// 处理订单
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spid);
			if(codeInfo!=null){
				String keyword = codeInfo.getKeyword();
				String[] tmp = keyword.split("#");
				String appsecret = tmp[1];
				
				String traid = jsonobj.getString("ORDERNO");
				String resBodystr = PayUtil.GenSignBody(jsonobj);
				String resSignStr = resBodystr.replace("/", "\\/") +"&key="+ appsecret;
				String resSign = MD5.md5(resSignStr, "utf-8").toUpperCase();
				if (resSign.equals(jsonobj1.get("Sign"))) {
					String status = jsonobj.getString("STATUS");
					boolean isSuc = false;
					if ("1".equals(status))
						isSuc = true;
					processOtherOrder(ffid, isSuc, traid);
					result = "SUCCESS";
				}
			}				
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	/**
	 * zwsf微信
	 * http://ltxpay.huizhousenfeng.com/fee/feeltxzwsfwx
	 * @param request
	 * @param response
	 * @throws JuiceException
	 */
	public void feeltxzwsfwx(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feeltxzwsfwx请求header信息："+request.allHeaders());
//			JSONObject jsonobj = JSONObject.parseObject(content);
			String orderid = request.getParam("orderid");
			String mchid = request.getParam("mchid");
			String out_trade_id = request.getParam("out_trade_id");
			String status = request.getParam("status");
			String price = request.getParam("price");
			String sign = request.getParam("sign");
			String traid = request.getParam("transid");
			if (traid == null || traid.length() <= 0)
				traid = orderid;
			
			String spid = out_trade_id.substring(0,4);
			String ffid = out_trade_id.substring(4);
			// 处理订单
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spid);
			if(codeInfo!=null){
				String keyword = codeInfo.getKeyword();
				String[] tmp = keyword.split("#");
				String appsecret = tmp[1];
				
				StringBuilder resSignparam = new StringBuilder();
				resSignparam.append("mchid=").append(mchid)
				.append("&orderid=").append(orderid)
				.append("&out_trade_id=").append(out_trade_id)
				.append("&price=").append(price)
				.append("&key=").append(appsecret);
				String resSign = MD5.md5(resSignparam.toString(), "utf-8");
				
				if (resSign.equals(sign)) {
					boolean isSuc = false;
					if ("1".equals(status))
						isSuc = true;
					processOtherOrder(ffid, isSuc, traid);
					result = "ok";
				}
			}				
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	
	/**
	 * 八班银联
	 * http://ltxpay.huizhousenfeng.com/fee/feeltxbbyl
	 * @param request
	 * @param response
	 * @throws JuiceException
	 */
	public void feeltxbbyl(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feeltxbbyl请求header信息："+request.allHeaders());
			String content = request.body();
			logger.info("feeltxbbyl请求body信息："+content);
			
			JSONObject jsonobj = JSONObject.parseObject(content);
			String order_id = jsonobj.getString("order_id");
			String orderNo = jsonobj.getString("orderNo");
			String money = jsonobj.getString("money");
			String mch = jsonobj.getString("mch");
			String pay_type = jsonobj.getString("pay_type");
			String time = jsonobj.getString("time");
			String sign = jsonobj.getString("sign");
			String status = jsonobj.getString("status");
			String traid = jsonobj.getString("transactionId");
			if (traid == null || traid.length() <= 0)
				traid = order_id;
			
			String spid = order_id.substring(0,4);
			String ffid = order_id.substring(4);
			// 处理订单
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spid);
			if(codeInfo!=null){
				String keyword = codeInfo.getKeyword();
				String[] tmp = keyword.split("#");
				String appsecret = tmp[1];
				
				StringBuilder resSignparam = new StringBuilder();
				resSignparam.append(order_id).append(orderNo)
				.append(money).append(mch)
				.append(pay_type).append(time)
				.append(MD5.md5(appsecret, "utf-8"));
				String resSign = MD5.md5(resSignparam.toString(), "utf-8");
				
				if (resSign.equals(sign)) {
					boolean isSuc = false;
					if ("1".equals(status))
						isSuc = true;
					processOtherOrder(ffid, isSuc, traid);
					result = "SUCCESS";
				}
			}				
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	
	/**
	 * 八班支付宝
	 * http://ltxpay.huizhousenfeng.com/fee/feeltxbbzfh
	 * @param request
	 * @param response
	 * @throws JuiceException
	 */
	public void feeltxbbzfh(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feeltxbbzfh请求header信息："+request.allHeaders());
			String content = request.body();
			logger.info("feeltxbbzfh请求body信息："+content);
			
			Map<String, String> resmap = CommonTool.parseYZ(content);
			String order_id = resmap.get("order_id");
			String orderNo = resmap.get("orderNo");
			String money = resmap.get("money");
			String mch = resmap.get("mch");
			String pay_type = resmap.get("pay_type");
			String time = resmap.get("time");
			String sign = resmap.get("sign");
			String status = resmap.get("status");
			String traid = resmap.get("transactionId");
			if (traid == null || traid.length() <= 0 || "未返回".equals(traid))
				traid = order_id;
			
			String spid = order_id.substring(0,4);
			String ffid = order_id.substring(4);
			// 处理订单
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spid);
			if(codeInfo!=null){
				String keyword = codeInfo.getKeyword();
				String[] tmp = keyword.split("#");
				String appsecret = tmp[1];
				
				StringBuilder resSignparam = new StringBuilder();
				resSignparam.append(order_id).append(orderNo)
				.append(money).append(mch)
				.append(pay_type).append(time)
				.append(MD5.md5(appsecret, "utf-8"));
				String resSign = MD5.md5(resSignparam.toString(), "utf-8");
				
				if (resSign.equals(sign)) {
					boolean isSuc = false;
					if ("1".equals(status))
						isSuc = true;
					processOtherOrder(ffid, isSuc, traid);
					result = "SUCCESS";
				}
			}				
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	/**
	 * 美美QQ钱包sdD9
	 * http://ltxpay.huizhousenfeng.com/fee/feejmqmmqq165
	 * @param request
	 * @param response
	 * @throws JuiceException
	 */
	public void feejmqmmqq165(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feejmqmmqq165请求header信息："+request.allHeaders());
			String content = request.body();
			logger.info("feejmqmmqq165回调信息："+content);
//			JSONObject jsonobj = JSONObject.parseObject(content);
			Map<String, Object> resmap = CommonTool.parseObj(content);
			
			String out_trade_no = (String)resmap.get("r2_OrderNo");
			String spid = out_trade_no.substring(0,4);
			String ffid = out_trade_no.substring(4);
			// 处理订单
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spid);
			if(codeInfo!=null){
				String keyword = codeInfo.getKeyword();
				String[] tmp = keyword.split("#");
				String appsecret = tmp[1];
				String sign = (String)resmap.get("sign");
				String traid = (String)resmap.get("r5_Status");
				
				StringBuilder resSignparam = new StringBuilder();
				resSignparam.append(resmap.get("r1_MerchantNo"))
				.append(resmap.get("r2_OrderNo")).append(resmap.get("r3_Amount"))
				.append(resmap.get("r4_Cur")).append(resmap.get("r5_Status"))
				.append(resmap.get("ra_PayTime")).append(resmap.get("rb_DealTime"))
				.append(appsecret);
				String resSign = MD5.md5(resSignparam.toString(), "utf-8");
				
				if (resSign.equals(sign)) {
					String status = (String)resmap.get("r5_Status");
					boolean isSuc = false;
					if ("100".equals(status))
						isSuc = true;
					processOtherOrder(ffid, isSuc, traid);
					result = "SUCCESS";
				}
			}				
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	/**
	 * 通联一诺
	 * http://ltxpay.huizhousenfeng.com/fee/feeltxtlylkj
	 * @param request
	 * @param response
	 * @throws JuiceException
	 */
	public void feeltxtlylkj(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feeltxtlylkj请求header信息："+request.allHeaders());
			String content = request.body();
			logger.info("feeltxtlylkj回调信息："+content);
//			JSONObject jsonobj = JSONObject.parseObject(content);
			Map<String, String> resmap = CommonTool.parseYZ(content);
			
			String out_trade_no = resmap.get("orderId");
			String spid = out_trade_no.substring(0,4);
			String ffid = out_trade_no.substring(4);
			// 处理订单
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spid);
			if(codeInfo!=null){
				String keyword = codeInfo.getKeyword();
				String[] tmp = keyword.split("#");
				String appsecret = tmp[1];
				String sign = resmap.get("signData");
				String traid = out_trade_no;
				
				StringBuilder resSignparam = new StringBuilder();
				resSignparam.append("versionId=").append(resmap.get("versionId")==null?"":resmap.get("versionId"))
				.append("&businessType=").append(resmap.get("businessType")==null?"":resmap.get("businessType"))
				.append("&insCode=").append(resmap.get("insCode")==null?"":resmap.get("insCode"))
				.append("&merId=").append(resmap.get("merId")==null?"":resmap.get("merId"))
				.append("&transDate=").append(resmap.get("transDate")==null?"":resmap.get("transDate"))
				.append("&transAmount=").append(resmap.get("transAmount")==null?"":resmap.get("transAmount"))
				.append("&transCurrency=").append(resmap.get("transCurrency")==null?"":resmap.get("transCurrency"))
				.append("&transChanlName=").append(resmap.get("transChanlName")==null?"":resmap.get("transChanlName"))
				.append("&openBankName=").append(resmap.get("openBankName")==null?"":resmap.get("openBankName"))
				.append("&orderId=").append(resmap.get("orderId")==null?"":resmap.get("orderId"))
				.append("&payStatus=").append(resmap.get("payStatus")==null?"":resmap.get("payStatus"))
				.append("&payMsg=").append(resmap.get("payMsg")==null?"":resmap.get("payMsg"))
				.append("&pageNotifyUrl=").append(resmap.get("pageNotifyUrl")==null?"":resmap.get("pageNotifyUrl"))
				.append("&backNotifyUrl=").append(resmap.get("backNotifyUrl")==null?"":resmap.get("backNotifyUrl"))
				.append("&orderDesc=").append(resmap.get("orderDesc")==null?"":resmap.get("orderDesc"))
				.append("&dev=").append(resmap.get("dev")==null?"":resmap.get("dev")).append(appsecret);
				String resSign = MD5.md5(resSignparam.toString(), "utf-8").toUpperCase();
				
				if (resSign.equals(sign)) {
					String status = resmap.get("payStatus");
					boolean isSuc = false;
					if ("00".equals(status))
						isSuc = true;
					processOtherOrder(ffid, isSuc, traid);
					result = "OK";
				}
			}				
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	//支付宝sdD8
	public void feejmqzfbsdD8(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feejmqzfbsdD8请求header信息："+request.allHeaders());
			String content = request.body();
			logger.info("feejmqzfbsdD8回调信息："+content);
			JSONObject jsonobj = JSONObject.parseObject(content);
			Map<String, Object> resmap = CommonTool.parseJson2(jsonobj);
			
			String out_trade_no = (String)resmap.get("out_trade_no");
			String spid = out_trade_no.substring(0,4);
			String ffid = out_trade_no.substring(4);
			// 处理订单
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spid);
			if(codeInfo!=null){
				String keyword = codeInfo.getKeyword();
				String[] tmp = keyword.split("#");
				String appsecret = tmp[1];
				Boolean isOK = PaySignUtil.checkSign(resmap, appsecret);
				if(isOK){
					boolean isSuc = true;
					String traid = (String)resmap.get("platform_order_sn");
					processOtherOrder(ffid, isSuc, traid);
					result="SUCCESS";
				}
			}				
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	
	/**
	 * 付易通微信H5回调通知
	 * http://smspay.xushihudong.com/fee/feexsfytback
	 */
	public void feexsfytback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feexsyftback易付通请求header信息：" + request.allHeaders());
			String content = request.body();
			logger.info("feexsyftback请求body信息：" + content);
			Map<String, String> restmap = CommonTool.parseYZ(content);
			String attach = restmap.get("attach");
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(attach);
			if (codeInfo != null) {
				restmap.put("key", codeInfo.getKeyword().split("#")[3]);
				String param = PaySignUtil.getParamStr2(restmap);
				String status = restmap.get("status");
				String ffId = restmap.get("out_trade_no");
				String traid = restmap.get("out_transaction_id");
				String sign = restmap.get("sign");
				if (MD5.md5(param, "utf-8").equals(sign)) {
					boolean isSuc = false;
					if ("0".equals(status))
						isSuc = true;
					processOtherOrder(ffId, isSuc, traid);
					result = "success";
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	/**
	 * 聚成支付微信H5回调通知 http://smspay.xushihudong.com/fee/feexsjcback
	 */
	public void feexsjcback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feexsjcback聚成支付请求header信息：" + request.allHeaders());
			String content = request.body();
			logger.info("feexsjcback请求body信息：" + content);
			@SuppressWarnings("unchecked")
			Map<String, String> restmap = (Map<String, String>) JSON
					.parse(content);
			String spId = restmap.get("remark");
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spId);
			if (codeInfo != null) {
				String param = PaySignUtil.getParamStr2(restmap)
						+ "&paySecret=" + codeInfo.getKeyword().split("#")[4];
				;
				String sign = restmap.get("sign");
				if (MD5.md5(param, "utf-8").toUpperCase().equals(sign)) {
					String status = restmap.get("tradeStatus");
					String ffId = restmap.get("outTradeNo");
					String traid = restmap.get("trxNo");

					boolean isSuc = false;
					if ("SUCCESS".equals(status))
						isSuc = true;
					processOtherOrder(ffId, isSuc, traid);
					result = "SUCCESS";
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	/**
	 * 鸿游第三方支付同步
	 * http://otherpay.xushihudong.com/fee/feexshyback
	 */
	public void feexshyback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feexshyback请求header信息：" + request.allHeaders());
			String ffId = request.getParam("cpparam");
			String status = request.getParam("status");
			String traid = request.getParam("ffid");
			
			// 处理订单
			boolean isSuc = false;
			if ("1".equals(status)) {
				isSuc = true;
			}
			
			result = "ok";
			processOtherOrder(ffId, isSuc, traid);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	/**
	 * 盈华迅方第三方支付同步 http://otherpay.xushihudong.com/fee/feexsyhxfback
	 */
	public void feexsyhxfback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feexsyhxfback请求header信息：" + request.allHeaders());
			// String oid=request.getParam("oid");
			String ffId = request.getParam("sporder");
			String spId = request.getParam("spid");
			String mz = request.getParam("mz");
			String traid = request.getParam("oid");
			String md5 = request.getParam("md5");
			String zdy = request.getParam("zdy");
			// 处理订单
			// md5=oid+sporder+spid+mz+sppwd
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(zdy);
			if (codeInfo != null) {
				//获取sppwd  分配的18位数据
				String sppwd = codeInfo.getKeyword().split("#")[3];
				StringBuilder key = new StringBuilder();
				key.append(traid).append(ffId).append(spId).append(mz)
						.append(sppwd);
				//验签
				if (!MD5.md5(key.toString(), "utf-8").toUpperCase().equals(md5)) {
					//验证支付状态
					boolean isSuc = false;
					if (Float.valueOf(mz) > 0) {
						isSuc = true;
					}
					result = "ok";
					processOtherOrder(ffId, isSuc, traid);
				}
					
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}

	/**
	 * 惠付第三方支付同步 http://otherpay.xushihudong.com/fee/feexshfback
	 */
	public void feexshfback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feexshfback请求header信息：" + request.allHeaders());
			String content = request.body();
			logger.info("feexshfback请求body信息：" + content);
			Map<String, String> restmap = CommonTool.parseYZ(content);
			String ffId = restmap.get("order_id");
			String money = restmap.get("money");
			String traid = restmap.get("orderNo");
			String status = restmap.get("status");
			String sign = restmap.get("sign");
			String mch = restmap.get("mch");
			String pay_type = restmap.get("pay_type");
			String time = restmap.get("time");
			String spId = restmap.get("extra");
			// 处理订单
			// md5(订单号码+系统订单+支付金额+商务号+支付类型+时间戳+md5(下单传过来的key))
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spId);
			if (codeInfo != null) {
				// 获取key
				String key = codeInfo.getKeyword().split("#")[1];
				StringBuilder md5 = new StringBuilder();
				md5.append(ffId).append(traid).append(money).append(mch)
						.append(pay_type).append(time)
						.append(MD5.md5(key, "utf-8"));
				//验签
				if (MD5.md5(md5.toString(), "utf-8").equals(sign)) {
					//验证金额和支付状态
					boolean isSuc = false;
					if (Integer.valueOf(money) > 0 && "1".equals(status))
						isSuc = true;
					result = "SUCCESS";
					processOtherOrder(ffId, isSuc, traid);
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	
	/**
	 * http://127.0.0.1:9911/fee/budan?ffid=
	 * @param request
	 * @param response
	 * @throws JuiceException
	 */
	/*public void budan(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String ffId = request.getParam("ffid");
		boolean isSuc = true;
		String traid = "";
		processOtherOrder(ffId, isSuc, traid);
		response.content("ok").end();
	}*/
	
	/**
	 * 富有第三方支付同步
	 * http://otherpay.xushihudong.com/fee/feexsfyback
	 */
	public void feexsfyback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feexsfyback请求header信息：" + request.allHeaders());
			String content = request.body();
			logger.info("feexsfyback请求body信息：" + content);
			JSONObject jsonobj = JSONObject.parseObject(content);
			Map<String, String> restmap = new HashMap<String, String>();
			for (Map.Entry<String,Object> entry :  jsonobj.entrySet()) {
				String value = entry.getValue() + "";
				restmap.put( entry.getKey(), value);
			}
			// 处理订单
			boolean isSuc = false;
			
			String attach = (String)restmap.get("additional_info");
			String result_code = (String)restmap.get("code");
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(attach);
			if (codeInfo != null) {
				String keyword = codeInfo.getKeyword();
				String[] tmp = keyword.split("#");
				String appsecret = tmp[1];
				
				String ressign = (String)restmap.get("sign");
				restmap.remove("sign");
				
				String resbacksign = PayUtil.getSign(restmap, appsecret);
				boolean flag = ressign.equals(resbacksign);
				logger.info("feexsfyback验签结果：" + flag);
				if (flag && "0000".equals(result_code)) {
					isSuc = true;
				}
				String traid = (String)restmap.get("platform_order_no");
				String ffId = (String)restmap.get("merchant_order_no");
				result = "success";
				processOtherOrder(ffId, isSuc, traid);
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	
	/**
	 * 云贝支付第三方支付同步
	 * http://otherpay.xushihudong.com/fee/feexsybzfback
	 */
	public void feexsybzfback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feexsybzfback请求header信息：" + request.allHeaders());
			
			String userOrderId = request.getParam("userOrderId");
//			String orderId = request.getParam("orderId");
			String traid = request.getParam("outOrderId");
			String fee = request.getParam("fee");
			String payResult = request.getParam("payResult");
			String sign = request.getParam("sign");
			
			// 处理订单
			boolean isSuc = false;
			
			String spid = userOrderId.substring(0,4);
			String ffId = userOrderId.substring(4);
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spid);
			if (codeInfo != null) {
				String keyword = codeInfo.getKeyword();
				String[] tmp = keyword.split("#");
				String appsecret = tmp[1];
				StringBuilder signparam = new StringBuilder();
				signparam.append(tmp[0])
				.append("_").append(userOrderId)
				.append("_").append(fee)
				.append("_").append(appsecret);
				
				String resbacksign = MD5Tool.toMD5Value(signparam.toString()).toLowerCase();
				boolean flag = sign.equals(resbacksign);
				logger.info("feexsybzfback验签结果：" + flag);
				if (flag && "0".equals(payResult)) {
					isSuc = true;
				}
				result = "ok";
				processOtherOrder(ffId, isSuc, traid);
			}
			
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	/**
	 * 钱匣子第三方支付同步
	 * http://otherpay.xushihudong.com/fee/feexsqxzback
	 */
	public void feexsqxzback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feexsqxzback钱匣子QQ钱包请求header信息：" + request.allHeaders());
			String content = request.body();
			logger.info("feexsqxzback钱匣子QQ钱包请求body信息：" + content);
			@SuppressWarnings("unchecked")
//			Map<String, String> restmap = (Map<String, String>) JSON
//					.parse(content);
//			Map<String, String> restmap = CommonTool.parseYZ(content);
			String strs=content.replace("\n","").replace("\r","").replace(" ","");
			String str[]=strs.split(":");
			Map<String,String> restmap=new HashMap<String,String>();
			for (int i = 1; i < str.length; i++) {
				int num1=str[i].indexOf("\"");
				int num2=str[i].lastIndexOf("\"");
				int num3=str[i].indexOf("---");
				String key=str[i].substring(num1+1,num2);
				String value=str[i].substring(num2+1,num3);
				restmap.put(key, value);
			}
			String userOrderId = restmap.get("sdorderno");
			String spId = userOrderId.substring(0, 4);
			String ffId = userOrderId.substring(4);
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spId);
			if (codeInfo != null) {
				String keyword = codeInfo.getKeyword();
				String[] tmp = keyword.split("#");
				String appsecret = tmp[1];
				StringBuilder signparam = new StringBuilder();
				signparam.append("customerid=").append(restmap.get("customerid"))
						.append("&status=").append(restmap.get("status"))
						.append("&sdpayno=").append(restmap.get("sdpayno"))
						.append("&sdorderno=").append(restmap.get("sdorderno"))
						.append("&total_fee=").append(restmap.get("total_fee"))
						.append("&paytype=").append(restmap.get("paytype"))
						.append("&").append(appsecret);
				String mySign = MD5Utils.getMD5(signparam.toString())
						.toLowerCase();
				String sign = restmap.get("sign");
				if (mySign.equals(sign)) {
					if ("1".equals(restmap.get("status"))) {
						String traid = restmap.get("sdpayno");
						boolean isSuc = true;
						processOtherOrder(ffId, isSuc, traid);
						result = "ok";
					}
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	/**
	 * 优乐微信同步
	 * http://otherpay.xushihudong.com/fee/feexsylback
	 */
	public void feexsylback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feexsylback请求header信息：" + request.allHeaders());
			String content = request.body();
			logger.info("feexsylback请求body信息：" + content);
			@SuppressWarnings("unchecked")
			Map<String, String> restmap = (Map<String, String>) JSON
					.parse(content);
			String sign =restmap.get("sign");
			restmap.remove("sign");
			String userOrderId = restmap.get("merchantOrderNo");
			String spId = userOrderId.substring(0, 4);
			String ffId = userOrderId.substring(4);
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spId);
			if (codeInfo != null) {
				String keyword = codeInfo.getKeyword();
				String[] tmp = keyword.split("#");
				String appsecret = tmp[1];
				String mySign = PayUtil.getSign(restmap, appsecret);
				if(mySign.equals(sign)&&"1".equals(restmap.get("orderPayStatus"))){
					String traid = restmap.get("merchantCode");
					processOtherOrder(ffId, true, traid);
					result = "ok";
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	/**
	 * 战火QQ扫码同步
	 * http://otherpay.xushihudong.com/fee/feexszhback
	 */
	public void feexszhback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feexszhback请求header信息：" + request.allHeaders());
			String content = request.body();
			logger.info("feexszhback请求body信息：" + content);
			Map<String, String> restmap = CommonTool.parseYZ(content);
			String sign =restmap.get("sign");
			restmap.remove("sign");
			String spId=restmap.get("params");
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spId);
			StringBuilder sb=new StringBuilder();
			String appsecret=codeInfo.getKeyword().split("#")[1];
			String mySignParam=sb.append("mch_id=").append(restmap.get("mch_id")).append("&mch_order_num=").append(restmap.get("mch_order_num")).append("&total_amount=").append(restmap.get("total_amount")).append("&pay_type=").append(restmap.get("pay_type")).append("&sign_type=").append(restmap.get("sign_type")).append("&key=").append(appsecret).toString();		
			String mySign =MD5Tool.toMD5Value(mySignParam).toLowerCase();
			if(mySign.equals(sign)&&"2".equals(restmap.get("status"))){
				processOtherOrder(restmap.get("mch_order_num"), true, restmap.get("order_num"));
				result="0000";
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	/**
	 * 全名点游QQ钱包同步
	 * http://otherpay.xushihudong.com/fee/feexsfyback
	 */
	public void feexsqmdyqqback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feexsqmdyqqback请求header信息：" + request.allHeaders());
			String content = request.body();
			logger.info("feexsqmdyqqback请求body信息：" + content);
			
			String sign=request.getParam("sign");
			//channelOrderId&key&orderId&timeStamp&totalFee
			//获取透传参数
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid("attach");
			String ffId=request.getParam("channelOrderId");
			String key=codeInfo.getKeyword().split("#")[4];
			String traid=request.getParam("orderId");
			String timeStamp=request.getParam("timeStamp");
			String totalFee=request.getParam("totalFee");
			StringBuilder sb=new StringBuilder();
			sb.append("channelOrderId=").append(ffId).append("&key=").append(key).append("&orderId=").append(traid).append("$timeStamp=").append(timeStamp).append("&totalFee=").append(totalFee);
			if(MD5.md5(sb.toString(),"utf-8").equals(sign)){
				// 处理订单
				boolean isSuc = false;
				if ("0".equals("return_code")) {
					result = "SUCCESS";
					processOtherOrder(ffId, isSuc, traid);
				}
			}
			
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}

	/**
	 * 我赞第三方支付同步
	 * http://otherpay.xushihudong.com/fee/feexswzdsfback
	 */
	public void feexswzdsfback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "success";
		try {
			logger.info("feexswzdsfback请求header信息：" + request.allHeaders());
			String content = request.body();
			logger.info("feexswzdsfback请求body信息：" + content);
			Map<String, String> restmap = CommonTool.parseYZ(content);
//			String appId = restmap.get("appId");
			String payNo = restmap.get("payNo");
			String ffId = restmap.get("orderNo");
			String userName = restmap.get("userName");
			String status = restmap.get("result");
			String pay_channel = restmap.get("pay_channel");
			String amount = restmap.get("amount");
			String pay_time = restmap.get("pay_time");
			String spId = restmap.get("userpara");
			String sign = restmap.get("sign");
			String traid = payNo;
			
			userName = userName==null?"":userName;
			
			// 处理订单
			boolean isSuc = false;
			if ("1".equals(status)) {
				CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spId);
				if (codeInfo != null) {
					String keyword = codeInfo.getKeyword();
					String[] tmp = keyword.split("#");
					String appsecret = tmp[1];
					String singparam = ffId + userName + status + amount+ pay_time + pay_channel + appsecret;
					String newsign = MD5.md5(singparam, "utf-8");
					if (sign.equals(newsign)) {
						isSuc = true;
					}
					processOtherOrder(ffId, isSuc, traid);
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	
	
	/**
	 * 小之麦田同步接口
	 * http://120.24.88.90/fee/feexzmtltbyback
	 */
	public void feexzmtltbyback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexzmtltbyback请求header信息：" + request.allHeaders());
			
			String content = request.body();
			logger.info("小之麦田："+content);
			if (content != null && content.length() > 0) {
				Map<String, String> myObj = parseYZ(content);
				
				String mobile = myObj.get("phone");
				String pid = myObj.get("servcode");
				
				String status = "1";
				
				int isSuccess = 0;
				if ("1".equals(status)) {
					isSuccess = 1;
				} else {
					isSuccess = CommonTool.convertInt(status);
				}
				int fee = 1000;
				OrderReqInfo info = new OrderReqInfo();
				info.setMobile(mobile);
				info.setSpId("wd99");
				info.setIsSuccess(isSuccess);
				info.setFee(fee);
				info.setCpParam("WS");
				
				processWlwWtcOrder(pid,info);
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 润土同步接口
	 * http://118.190.90.1/fee/feexzmtltbyRtback
	 */
	public void feexzmtltbyRtback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexzmtltbyRtback请求header信息：" + request.allHeaders());
			String mobile = request.getParam("mobile");
			String momsg = request.getParam("momsg");
			String spnumber = request.getParam("spnumber");
			String linkid = request.getParam("linkid");
			String flag = request.getParam("flag");
			
			int isSuccess = 0;
			if ("delivrd".equals(flag))
				isSuccess=1;
			OrderReqInfo info = new OrderReqInfo();
			info.setMobile(mobile);
			info.setSpId("wd99");
			info.setIsSuccess(isSuccess);
			info.setFee(0);
			info.setCpParam(linkid);
			info.setOrderId(linkid);
			info.setSms(momsg);
			info.setSmscontent(spnumber);
			String pid = momsg + "_" + spnumber;
			
			processWlwWtcRtOrder(pid,info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 润土远帆webqq同步接口
	 * http://118.190.90.1/fee/feeszrtyfwebqqback
	 */
	public void feeszrtyfwebqqback1(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feeszrtyfwebqqback请求header信息：" + request.allHeaders());
			String phone = request.getParam("phone");
			String mobile = request.getParam("mobile");
			String momsg = request.getParam("msg");
			String spnumber = request.getParam("spnumber");
			String linkid = request.getParam("linkid");
			String status = request.getParam("status");
			if(mobile == null||"".equals(mobile)){
				mobile = phone;
			}
			int isSuccess = 2;
			if ("DELIVRD".equals(status)) isSuccess = 1;
			
			String ffId =DateTool.getMonth() + mobile + DateTool.getTodayNum();
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			info.setIsSuccess(isSuccess);
			info.setFee(0);
			info.setMobile(mobile);
			info.setCpParam(linkid);
			info.setOrderId(linkid);
			info.setSms(momsg);
			info.setSmscontent(spnumber);
//			processBYOrder(info);
			processWEBQQOrder(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 润土webqq同步接口
	 * http://118.190.90.1/fee/feeszrtwebqqback
	 */
	public void feeszrtwebqqback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feeszrtwebqqback请求header信息：" + request.allHeaders());
			String mobile = request.getParam("mobile");
			String momsg = request.getParam("content");
			String spnumber = request.getParam("spnumber");
			String linkid = request.getParam("linkid");
			int isSuccess = 1;
			
			String ffId =DateTool.getMonth() + mobile + DateTool.getTodayNum();
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			info.setIsSuccess(isSuccess);
			info.setFee(0);
			info.setMobile(mobile);
			info.setCpParam(linkid);
			info.setOrderId(linkid);
			info.setSms(momsg);
			info.setSmscontent(spnumber);
			processBYOrder(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 润土积分同步接口
	 * http://118.190.90.1/fee/feexzmtltjfRtback
	 */
	public void feexzmtltjfRtback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexzmtltjfRtback请求header信息：" + request.allHeaders());
			String mobile = request.getParam("mobile");
			String momsg = request.getParam("momsg");
			String spnumber = request.getParam("spnumber");
			String linkid = request.getParam("orderId");
			String flag = request.getParam("flag");
			String price = request.getParam("price");
			
			int isSuccess = 0;
			if ("delivrd".equals(flag))
				isSuccess=1;
			OrderReqInfo info = new OrderReqInfo();
			info.setMobile(mobile);
			info.setSpId("wd99");
			info.setIsSuccess(isSuccess);
			info.setFee(CommonTool.convertInt(price));
			info.setCpParam(linkid);
			info.setOrderId(linkid);
			info.setSms(momsg);
			info.setSmscontent(spnumber);
			String pid = momsg + "_" + spnumber;
			
			processWlwWtcRtOrder(pid,info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 润土同步接口
	 * http://120.24.88.90/fee/feeszrtbyback
	 */
	public void feeszrtbyback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexzmtltbyback请求header信息：" + request.allHeaders());
			
			String mobile = request.getParam("phone");
			String pid = request.getParam("cpParam");
			
			String status = "1";
			
			int isSuccess = 0;
			if ("1".equals(status)) {
				isSuccess = 1;
			} else {
				isSuccess = CommonTool.convertInt(status);
			}
			int fee = 1000;
			OrderReqInfo info = new OrderReqInfo();
			info.setMobile(mobile);
			info.setSpId("wd99");
			info.setIsSuccess(isSuccess);
			info.setFee(fee);
			info.setCpParam("WS");
			
			processWlwWtcOrder(pid,info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 润土同步接口
	 * http://120.24.88.90/fee/feeghbdxdbback
	 */
	public void feeghbdxdbback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feeghbdxdbback请求header信息：" + request.allHeaders());
			
			String mobile = request.getParam("phone");//手机号
			String status = request.getParam("status");//状态  0：订购  3：退订
//			String addtime = request.getParam("ordertime");//订购/退订时间
			String pid = request.getParam("spcmd");//指令
			
			
			int isSuccess = 0;
			if ("0".equals(status)) {
				isSuccess = 1;
			} else if ("1".equals(status)) {
				isSuccess = 2;
			} else {
				isSuccess = CommonTool.convertInt(status);
			}
			int fee = 500;
			OrderReqInfo info = new OrderReqInfo();
			info.setMobile(mobile);
			info.setSpId("dx90");
			info.setIsSuccess(isSuccess);
			info.setFee(fee);
			info.setCpParam(pid);
			
			processWlwWtcOrder(pid,info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 心期天 同步接口
	 * http://118.190.90.1/fee/feexqtdbback
	 */
	public void feexqtdbback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexqtdbback请求header信息：" + request.allHeaders());
			
			String mobile = request.getParam("mobileid");//手机号
			String status = request.getParam("retcode");//状态  DELIVRD：成功
			String price = request.getParam("feecode");//资费
			String pid = request.getParam("pid");//
			
			if (price == null || price.length() == 0) price="0";
			
			int isSuccess = 0;
			if ("0000".equals(status)) {
				isSuccess = 1;
			} else {
				isSuccess = 2;
			}
			int fee = CommonTool.convertInt(price);
			OrderReqInfo info = new OrderReqInfo();
			info.setMobile(mobile);
			info.setSpId("yc91");
			info.setIsSuccess(isSuccess);
			info.setFee(fee);
			info.setCpParam(pid);
			
			processWlwWtcOrder(pid,info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 兆荣绿通点播 同步接口
	 * http://118.190.90.1/fee/feezrltdbback
	 */
	public void feezrltdbback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feezrltdbback请求header信息：" + request.allHeaders());
			
			String mobile = request.getParam("phone");//手机号
			String status = request.getParam("report");//状态  DELIVRD：成功
			String linkid = request.getParam("linkid");
			String content = request.getParam("content");//指令
			String destnumber = request.getParam("destnumber");//端口号
			String pid = content + "_" + destnumber;
			
			
			int isSuccess = 0;
			if ("DELIVRD".equals(status)) {
				isSuccess = 1;
			} else {
				isSuccess = 2;
			}
			int fee = 100;
			OrderReqInfo info = new OrderReqInfo();
			info.setMobile(mobile);
			info.setSpId("yc90");
			info.setIsSuccess(isSuccess);
			info.setFee(fee);
			info.setCpParam(pid);
			info.setOrderId(linkid);
			info.setSms(destnumber);
			info.setSmscontent(content);
			
			processWlwWtcOrder(pid,info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 无透传通用 同步接口
	 * http://120.24.88.90/fee/feewtccomback
	 */
	public void feexswtcback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feewtccomback请求header信息：" + request.allHeaders());
			
			String mobile = request.getParam("mobile");//手机号
			String status = request.getParam("status");//状态  DELIVRD：成功
			String price = request.getParam("price");//
			String pid = request.getParam("pid");//短信内容
			
			
			int isSuccess = 0;
			if ("DELIVRD".equals(status)) {
				isSuccess = 1;
			} else {
				isSuccess = 2;
			}
			int fee = CommonTool.convertInt(price);
			OrderReqInfo info = new OrderReqInfo();
			info.setMobile(mobile);
			info.setSpId("yc95");
			info.setIsSuccess(isSuccess);
			info.setFee(fee);
			info.setCpParam(pid);
			
			processWlwWtcOrder(pid,info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 吉祥ivr 同步接口
	 * http://120.24.88.90/fee/feejxivrback
	 */
	public void feejxivrback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feejxivrback请求header信息：" + request.allHeaders());
			String content = request.body();
			logger.info("feejxivrback下行短信："+content);
			
			JSONObject jsonObj = JSONObject.parseObject(content);
			
			String price = jsonObj.getString("fee");//
			String mobile = jsonObj.getString("caller");//
			String pid = jsonObj.getString("called");//
			
			
			int isSuccess = 1;
			
			int fee = 0;
			if (price != null && price.length() > 0) fee = CommonTool.convertInt(price);
			OrderReqInfo info = new OrderReqInfo();
			info.setMobile(mobile);
			info.setSpId("wd99");
			info.setIsSuccess(isSuccess);
			info.setFee(fee);
			info.setCpParam(pid);
			
			processIVRWlwWtcOrder(pid,info,content);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	
	/**
	 * IVR无联网无透传参数订单同步处理
	 * @param pid
	 * @param info
	 * @throws Exception 
	 */
	private void processIVRWlwWtcOrder(String pid, OrderReqInfo info,String content) throws Exception {
		String cpId = commonAction.getCpid(pid);
		CPInfo cp = commonAction.queryCP(cpId);
		
		String spid = cp.getLocalSpId();
		int isSuccess = info.getIsSuccess();
		String ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), spid);
		int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
		
		String newCpId = cp.getCpId()+spid;
		int synRadio = cp.getSynRadio();
		logger.info("同步率synRadio："+synRadio);
		CPParam cpp = CommonTool.getCPParam(newCpId,
				synRadio);
		
		info.setCpId(cpId);
		info.setFfId(ffId);
		info.setSpId(spid);
		info.setIp("127.0.0.1");
		info.setProvince("局域");
		info.setIsSyn(1);
		
		boolean flag = true;
		if (synRadio == 100) flag = false;
		if (isSuccess == 1 && flag) {
			AtomicInteger ati = cpp.getAti();
			boolean isSyn = CommonTool.isContain(cpp.getVc(),
					ati.incrementAndGet());
			if (checkOrderNum(cp)) isSyn = true;
			if (isSyn) {
				if (cp.getUrl() != null && cp.getUrl().length() > 0) {
					// 同步
					String result = notityIVRWlwWtcResult(cp,content,info);
					
					logger.info("给CP的同步结果："+result);
					if (result.equals("ok")) {
						synStatus = ConstantDefine.SYN_STATUS_TRUE;
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				} else {
					synStatus = ConstantDefine.SYN_STATUS_FALSE;
				}
			} else {
				synStatus = ConstantDefine.SYN_STATUS_Buckle;
			}
			
			if (ati.get() == 100)
				ati.set(0);
			cpp.setAti(ati);
			CommonTool.updateCPParam(newCpId, cpp);
		} else {
			// 同步
			String result = notityIVRWlwWtcResult(cp,content,info);
			logger.info("给CP的同步结果："+result);
			if (result.equals("ok")) {
				synStatus = ConstantDefine.SYN_STATUS_TRUE;
			} else {
				synStatus = ConstantDefine.SYN_STATUS_FALSE;
			}
		}
		
		info.setSynStatus(synStatus);
		commonAction.addOrderReqInfoAll(info);
	}
	
	/**
	 * IVR无联网无透传参数数据同步通知
	 * @param pid
	 * @param order
	 * @return
	 * @throws Exception 
	 */
	private String notityIVRWlwWtcResult(CPInfo cp, String content, OrderReqInfo order) throws Exception {
		
		String result = "error";
		String param = "";
		if (cp.getBackMethod() == 1) {
			result = HttpTool.sendKLWPost(cp.getUrl(), content, "utf-8", 500);
		} else {
			JSONObject jsonobj = JSONObject.parseObject(content);
			String caller = jsonobj.getString("caller");
			String called = jsonobj.getString("called");
			String btime = jsonobj.getString("btime");
			String etime = jsonobj.getString("etime");
			String time = jsonobj.getString("time");
			String minute = jsonobj.getString("minute");
			String fee = jsonobj.getString("fee");
			String linkid = jsonobj.getString("linkid");
			String type = jsonobj.getString("type");
			if ("xk01".equals(cp.getCpId())) {
				time = fee.replace("00", "");
			}
			StringBuilder msg = new StringBuilder();
			msg.append("caller=").append(caller)
			.append("&called=").append(called)
			.append("&btime=").append(URLEncoder.encode(btime,"utf-8"))
			.append("&etime=").append(URLEncoder.encode(etime,"utf-8"))
			.append("&time=").append(time)
			.append("&minute=").append(minute)
			.append("&fee=").append(fee)
			.append("&linkid=").append(linkid)
			.append("&type=").append(type);
			param = msg.toString();
			result = HttpTool.sendGetSetTimeout(cp.getUrl(), param, "500");
		}
		return result;
	}
	
	/**
	 * 刷卡 同步接口
	 * http://120.24.88.90/fee/feeskback
	 */
	public void feeskback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feeskback请求header信息：" + request.allHeaders());
			
			String data = request.getParam("data");//手机号
			JSONObject jsonObj = JSONObject.parseObject(data);
			
			String status = jsonObj.getString("status");//
			String mobile = jsonObj.getString("mobile");//
			String price = jsonObj.getString("fee");//
			String pid = "SK";//指令
				
			
			int isSuccess = 0;
			if ("0000".equals(status)) {
				isSuccess = 1;
			} else {
				isSuccess = 2;
			}
			int fee = 10;
			if (price != null && price.length() > 0) fee = CommonTool.convertInt(price);
			OrderReqInfo info = new OrderReqInfo();
			info.setMobile(mobile);
			info.setSpId("yc92");
			info.setIsSuccess(isSuccess);
			info.setFee(fee);
			info.setCpParam(pid);
			
			processWlwWtcOrder(pid,info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 无联网无透传参数润土订单同步处理
	 * @param pid
	 * @param info
	 */
	private void processWlwWtcRtOrder(String pid, OrderReqInfo info) {
		String cpId = commonAction.getCpid(pid);
		
		String spid = info.getSpId();
		int isSuccess = info.getIsSuccess();
		String ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), spid);
		CPInfo cp = commonAction.queryCP(cpId);
		int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
		
		String newCpId = cp.getCpId()+spid;
		int synRadio = cp.getSynRadio();
		logger.info("同步率synRadio："+synRadio);
		CPParam cpp = CommonTool.getCPParam(newCpId,
				synRadio);
		
		info.setCpId(cpId);
		info.setFfId(ffId);
		info.setSpId(spid);
		info.setIp("127.0.0.1");
		info.setProvince("局域");
		info.setIsSyn(1);
		
		boolean flag = true;
		if (synRadio == 100) flag = false;
		if (isSuccess == 1 && flag) {
			AtomicInteger ati = cpp.getAti();
			boolean isSyn = CommonTool.isContain(cpp.getVc(),
					ati.incrementAndGet());
			if (checkOrderNum(cp)) isSyn = true;
			if (isSyn) {
				if (cp.getUrl() != null && cp.getUrl().length() > 0) {
					// 同步
					String result = notityWlwWtcRtResult(cp,pid,info);
					
					logger.info("给CP的同步结果："+result);
					if (result.equals("ok")) {
						synStatus = ConstantDefine.SYN_STATUS_TRUE;
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				} else {
					synStatus = ConstantDefine.SYN_STATUS_FALSE;
				}
			} else {
				synStatus = ConstantDefine.SYN_STATUS_Buckle;
			}
			
			if (ati.get() == 100)
				ati.set(0);
			cpp.setAti(ati);
			CommonTool.updateCPParam(newCpId, cpp);
		} else {
			// 同步
			String result = notityWlwWtcRtResult(cp,pid,info);
			logger.info("给CP的同步结果："+result);
			if (result.equals("ok")) {
				synStatus = ConstantDefine.SYN_STATUS_TRUE;
			} else {
				synStatus = ConstantDefine.SYN_STATUS_FALSE;
			}
		}
		
		info.setSynStatus(synStatus);
		commonAction.addOrderReqInfoAll(info);
	}
	
	/**
	 * 无联网无透传参数订单同步处理
	 * @param pid
	 * @param info
	 */
	private void processWlwWtcOrder(String pid, OrderReqInfo info) {
		String cpId = commonAction.getCpid(pid);
		
		String spid = info.getSpId();
		int isSuccess = info.getIsSuccess();
		String ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), spid);
		CPInfo cp = commonAction.queryCP(cpId);
		int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
		
		String newCpId = cp.getCpId()+spid;
		int synRadio = cp.getSynRadio();
		logger.info("同步率synRadio："+synRadio);
		CPParam cpp = CommonTool.getCPParam(newCpId,
				synRadio);
		
		info.setCpId(cpId);
		info.setFfId(ffId);
		info.setSpId(spid);
		info.setIp("127.0.0.1");
		info.setProvince("局域");
		info.setIsSyn(1);
		
		boolean flag = true;
		if (synRadio == 100) flag = false;
		if (isSuccess == 1 && flag) {
			AtomicInteger ati = cpp.getAti();
			boolean isSyn = CommonTool.isContain(cpp.getVc(),
					ati.incrementAndGet());
			if (checkOrderNum(cp)) isSyn = true;
			if (isSyn) {
				if (cp.getUrl() != null && cp.getUrl().length() > 0) {
					// 同步
					String result = notityWlwWtcResult(cp,pid,info);
					
					logger.info("给CP的同步结果："+result);
					if (result.equals("ok")) {
						synStatus = ConstantDefine.SYN_STATUS_TRUE;
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				} else {
					synStatus = ConstantDefine.SYN_STATUS_FALSE;
				}
			} else {
				synStatus = ConstantDefine.SYN_STATUS_Buckle;
			}
			
			if (ati.get() == 100)
				ati.set(0);
			cpp.setAti(ati);
			CommonTool.updateCPParam(newCpId, cpp);
		} else {
			// 同步
			String result = notityWlwWtcResult(cp,pid,info);
			logger.info("给CP的同步结果："+result);
			if (result.equals("ok")) {
				synStatus = ConstantDefine.SYN_STATUS_TRUE;
			} else {
				synStatus = ConstantDefine.SYN_STATUS_FALSE;
			}
		}
		
		info.setSynStatus(synStatus);
		commonAction.addOrderReqInfoAll(info);
	}
	
	/**
	 * 无联网无透传参数润土数据同步通知
	 * @param pid
	 * @param order
	 * @return
	 */
	private String notityWlwWtcRtResult(CPInfo cp, String pid, OrderReqInfo order) {
		
		String result = "error";
		String param = "";
		if (cp.getBackMethod() == 1) {
			SynJson sj = new SynJson();
			sj.setPhone(order.getMobile());
			sj.setStatus(order.getIsSuccess()+"");
			sj.setOrderId(order.getOrderId());
			param = JSON.toJSONString(sj);
			result = HttpTool.sendKLWPost(cp.getUrl(), param, "utf-8", 500);
		} else {
			StringBuilder msg = new StringBuilder();
			msg.append("mobile=").append(order.getMobile())
			.append("&momsg=").append(order.getSms())
			.append("&spnumber=").append(order.getSmscontent())
			.append("&status=").append(order.getIsSuccess())
			.append("&orderid=").append(order.getOrderId());
			param = msg.toString();
			result = HttpTool.sendGetSetTimeout(cp.getUrl(), param, "500");
		}
		return result;
	}
	
	/**
	 * 无联网无透传参数数据同步通知
	 * @param pid
	 * @param order
	 * @return
	 */
	private String notityWlwWtcResult(CPInfo cp, String pid, OrderReqInfo order) {
		
		String result = "error";
		String param = "";
		if (cp.getBackMethod() == 1) {
			SynJson sj = new SynJson();
			sj.setFfId(order.getFfId());
			sj.setCpParam(order.getCpParam());
			sj.setImei(order.getImei());
			sj.setImsi(order.getImsi());
			sj.setFee(order.getFee());
			sj.setIp(order.getIp());
			sj.setPhone(order.getMobile());
			sj.setStatus(order.getIsSuccess()+"");
			sj.setOrderId(order.getOrderId());
			param = JSON.toJSONString(sj);
			result = HttpTool.sendKLWPost(cp.getUrl(), param, "utf-8", 500);
		} else {
			StringBuilder msg = new StringBuilder();
			msg.append("imsi=").append(order.getImsi())
			.append("&imei=").append(order.getImei())
			.append("&ffId=").append(order.getFfId())
			.append("&phone=").append(order.getMobile())
			.append("&cpParam=").append(order.getCpParam())
			.append("&fee=").append(order.getFee())
			.append("&status=").append(order.getIsSuccess())
			.append("&orderId=").append(order.getOrderId())
			.append("&smsport=").append(order.getSms())
			.append("&smscontent=").append(order.getSmscontent())
			.append("&ip=").append(order.getIp());
			param = msg.toString();
			result = HttpTool.sendGetSetTimeout(cp.getUrl(), param, "500");
		}
		return result;
	}
	
	
	/**
	 * 北京翼联电信同步接口
	 * http://120.24.88.90/fee/feebjylback
	 */
	public void feebjylback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feebjylback请求header信息：" + request.allHeaders());
			
			String ffId = request.getParam("cpparam");
			String isSuccess = request.getParam("status");
			String mobile = request.getParam("phone");
			String spcmd = URLDecoder.decode(request.getParam("spcmd"),"utf-8");
			if (spcmd != null && spcmd.contains("klbw@")) {
				String[] temp = spcmd.split("@");
				if (temp.length >= 2) {
					String cpid = temp[1];
					commitlocal(request,cpid);
				} else {
					logger.error("feebjylback错误指令spcmd："+spcmd);
				}
			} else if (spcmd != null && spcmd.contains("jyfsjdxby1949")) {
				String cpid = "afq01";
				commitlocal(request,cpid);
			} else if (spcmd != null && spcmd.contains("jyfsjdxby")) {
				commitback(request);
			} else {
				if (ffId == null || ffId.length() <= 0) {
					if (spcmd != null && spcmd.length() > 0) {
						ffId = spcmd.substring(spcmd.length()-16,spcmd.length());
					}
				}
				OrderReqInfo info = new OrderReqInfo();
				info.setFfId(ffId);
				if ("0".equals(isSuccess)) {
					info.setIsSuccess(1);
				} else if ("1".equals(isSuccess)) {
					info.setIsSuccess(2);
				} else if ("3".equals(isSuccess)) {
					info.setIsSuccess(3);
				} else {
					info.setIsSuccess(9);
				}
				info.setMobile(mobile);
				processBYOrder(info);
			}
			
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 透传带cpid 同步接口
	 * http://smspay.xushihudong.com/fee/feexsdfback1
	 */
	public void feexsdfback1(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexsdfback1请求header信息：" + request.allHeaders());
			
			String momsg = request.getParam("momsg");
			String spnumber = request.getParam("spnumber");
			String status = request.getParam("status");
			String mobile = request.getParam("mobile");
			String amount = request.getParam("amount");
			String cpid = request.getParam("cpid");
			String cpparam = request.getParam("cpparam");
			String spcmd = URLDecoder.decode(request.getParam("momsg"),"utf-8");
			
			int isSuccess = 0;
			if ("1".equals(status)) isSuccess=1;
			
			if (cpid != null && cpid.length() > 0 && !"null".equals(cpid)) {
				commitdflocal(mobile, isSuccess, amount, cpid, momsg, spnumber, cpparam);
			} else {
				if (spcmd != null) {
					String[] temp = spcmd.split("#xs");
					if (temp.length >= 2) {
						cpid = temp[1];
						commitdflocal(mobile, isSuccess, amount, cpid, momsg, spnumber, cpparam);
					} else {
						OrderReqInfo info = new OrderReqInfo();
						info.setMobile(mobile);
						info.setSpId("wt99");
						info.setIsSuccess(isSuccess);
						info.setFee(CommonTool.convertInt(amount));
						info.setSms(spnumber);
						info.setSmscontent(momsg);
						info.setDelayed(cpparam);
						
						processxsWlwWtcOrder(momsg+"_"+spnumber,info);
					}
				}
			}
			
			
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 北京翼联电信同步接口
	 * http://smspay.xushihudong.com/fee/feexsbjylback
	 */
	public void feexsbjylback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexsbjylback请求header信息：" + request.allHeaders());
			
			String isSuccess = request.getParam("status");
			String mobile = request.getParam("phone");
			String ffId = URLDecoder.decode(request.getParam("spcmd"),"utf-8");
			
			OrderReqInfo info = new OrderReqInfo();
			if ("0".equals(isSuccess)) {
				info.setIsSuccess(1);
			} else if ("1".equals(isSuccess)) {
				info.setIsSuccess(2);
			} else if ("3".equals(isSuccess)) {
				info.setIsSuccess(3);
			} else {
				info.setIsSuccess(9);
			}
			info.setMobile(mobile);
			
			if (ffId != null && ffId.length() > 16) {
				ffId = ffId.substring(ffId.length()-16,ffId.length());
				info.setFfId(ffId);
				processxsBYOrder(info);
			} else {
				logger.info("feexsbjylback请求ffId信息：" + ffId);
				
			}
			
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 广州友趣 同步接口
	 * http://smspay.xushihudong.com/fee/feexsgzyqback
	 */
	public void feexsgzyqback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexsgzyqback请求header信息：" + request.allHeaders());
			
			String isSuccess = request.getParam("status");
//			String mobile = request.getParam("paymentUser");
			String ffId = request.getParam("extra");
			
			boolean isSucc = false;
			if ("1".equals(isSuccess)) isSucc = true;
			processXSOrder(ffId,isSucc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("0").end();
	}
	
	/**
	 * 叶慧联通 同步接口
	 * http://smspay.qygame.cn/fee/feeqyyhlt
	 */
	public void feeqyyhlt(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feeqyyhlt请求header信息：" + request.allHeaders());
			
			String isSuccess = request.getParam("state");
//			String mobile = request.getParam("paymentUser");
			String ffId = request.getParam("exData");
			
			boolean isSucc = false;
			if ("1".equals(isSuccess)) isSucc = true;
			processXSOrder(ffId,isSucc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 魔力小鸟 同步接口
	 * http://smspay.qygame.cn/fee/feeqymlxnldys
	 */
	public void feeqymlxnldys(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "error";
		try {
			logger.info("feeqymlxnldys请求header信息：" + request.allHeaders());
			
			String merid = request.getParam("merid");
			String goodsid = request.getParam("goodsid");
			String orderid = request.getParam("orderid");
			String orderdate = request.getParam("orderdate");
			String retCode = request.getParam("retCode");
			String retMsg = request.getParam("retMsg");
			String upversion = request.getParam("upversion");
			String isSuccess = request.getParam("retCode");
//			String mobile = request.getParam("paymentUser");
			
			String spid = orderid.substring(0,4);
			String ffId = orderid.substring(4);
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spid);
			if (codeInfo != null) {
				String keyword = codeInfo.getKeyword();
				String[] tmp = keyword.split("#");
				String appsecret = tmp[1];
				StringBuilder signparam = new StringBuilder();
				signparam.append(merid).append("|").append(goodsid).append("|").append(orderid)
				.append("|").append(orderdate).append("|").append(retCode).append("|").append(retMsg)
				.append("|").append(upversion);
				
				String sign = MD5.md5(signparam + appsecret, "utf-8");
				String param = signparam + "|" + sign;
				result = "<META NAME=\"MobilePayPlatform\" CONTENT =\"" + param + "\">";
				boolean isSucc = false;
				if ("0000".equals(isSuccess)) isSucc = true;
				processXSOrder(ffId,isSucc);
			}
			
			
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content(result).end();
	}
	
	/**
	 * 广州普石 同步接口
	 * http://smspay.xushihudong.com/fee/feexsgzpsback
	 */
	public void feexsgzpsback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexsgzpsback请求header信息：" + request.allHeaders());
			String isSuccess = request.getParam("mmStates");
//			String mobile = request.getParam("paymentUser");
			String ffId = request.getParam("extension");
			
			boolean isSucc = false;
			if ("0".equals(isSuccess)) isSucc = true;
			processXSOrder(ffId,isSucc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 杭州电喵 同步接口
	 * http://smspay.xushihudong.com/fee/feexshzdmback
	 */
	public void feexshzdmback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexshzdmback请求header信息：" + request.allHeaders());
			
			String isSuccess = request.getParam("code");
//			String mobile = request.getParam("paymentUser");
			String ffId = request.getParam("cpparam");
			
			boolean isSucc = false;
			if ("1".equals(isSuccess)) isSucc = true;
			processXSOrder(ffId,isSucc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("0").end();
	}
	
	/**
	 * 上海省开 同步接口
	 * http://smspay.xushihudong.com/fee/feexsshskback
	 */
	public void feexsshskback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexsshskback请求header信息：" + request.allHeaders());
			
			String isSuccess = request.getParam("status");
//			String mobile = request.getParam("paymentUser");
			String ffId = request.getParam("note");
			
			boolean isSucc = false;
			if ("DELIVRD".equals(isSuccess)) isSucc = true;
			processXSOrder(ffId,isSucc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("0").end();
	}
	
	/**
	 * 稻香联通 同步接口
	 * http://smspay.xushihudong.com/fee/feexsdxltback
	 */
	public void feexsdxltback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexsdxltback请求header信息：" + request.allHeaders());
			
			String isSuccess = request.getParam("chargeResultCode");
//			String mobile = request.getParam("paymentUser");
			String ffId = request.getParam("extData");
			
			boolean isSucc = false;
			if ("0".equals(isSuccess)) isSucc = true;
			processXSOrder(ffId,isSucc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("0").end();
	}
	
	/**
	 * 厦门杨顺 同步接口
	 * http://smspay.xushihudong.com/fee/feexsxmysback
	 */
	public void feexsxmysback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexsxmysback请求header信息：" + request.allHeaders());
			
			String isSuccess = request.getParam("result");
//			String mobile = request.getParam("paymentUser");
			String ffId = request.getParam("cpparam");
			
			boolean isSucc = false;
			if ("0".equals(isSuccess)) isSucc = true;
			processXSOrder(ffId,isSucc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("0").end();
	}
	
	/**
	 * 朱云烧烤 同步接口
	 * http://smspay.xushihudong.com/fee/feexszyskback
	 */
	public void feexszyskback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexszyskback请求header信息：" + request.allHeaders());
			
			String isSuccess = request.getParam("code");
//			String mobile = request.getParam("paymentUser");
			String ffId = request.getParam("cpparam");
			
			ffId = ffId.substring(ffId.length()-16,ffId.length());
			
			boolean isSucc = false;
			if ("1".equals(isSuccess)) isSucc = true;
			processXSOrder(ffId,isSucc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("0").end();
	}
	
	/**
	 * 深圳凯耀达 同步接口
	 * http://smspay.xushihudong.com/fee/feexsszkydback
	 */
	public void feexsszkydback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexsszkydback请求header信息：" + request.allHeaders());
			
			String isSuccess = "0";
//			String mobile = request.getParam("paymentUser");
			String ffId = request.getParam("cpparam");
			
			boolean isSucc = false;
			if ("0".equals(isSuccess)) isSucc = true;
			processXSOrder(ffId,isSucc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("0").end();
	}
	
	/**
	 * 乐奕 同步接口
	 * http://smspay.xushihudong.com/fee/feeltxlyzfb
	 */
	public void feeltxlyzfb(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "fail";
		try {
			logger.info("feeltxlyzfb请求header信息："+request.allHeaders());
			String content = request.body();
			logger.info("feeltxlyzfb回调信息："+content);
			Map<String, String> map = CommonTool.parseYZ(content);
			
			String out_trade_no = map.get("orderId");
			String remark = out_trade_no.substring(0, 4);
			String ffid = out_trade_no.substring(4);
			// 处理订单
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(remark);
			if(codeInfo!=null){
				String sign = map.get("sign");
				map.remove("sign");
				String keyword = codeInfo.getKeyword();
				String[] tmp = keyword.split("#");
				String appsecret = tmp[1];
				String md5Param = PayUtil.createQXSign(map,false,",");
				String ressign = MD5.md5(md5Param + "," + appsecret, "utf-8");
				
				Boolean isOK = ressign.equals(sign);
				if(isOK){
					boolean isSuc = false;
					String status = map.get("status");
					if ("200".equals(status)) 
						isSuc = true;
					String traid = map.get("serverOrder");
					if (traid == null || traid.length() <= 0) traid = ffid;
					processOtherOrder(ffid, isSuc, traid);
					result="SUCCESS";
				}
			}				
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result).end();
	}
	
	/**
	 * 玉品天成 同步接口
	 * http://smspay.xushihudong.com/fee/feexsyptcback
	 */
	public void feexsyptcback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexsyptcback请求header信息：" + request.allHeaders());
			
			String isSuccess = request.getParam("status");
//			String mobile = request.getParam("paymentUser");
			String ffId = request.getParam("cpparam");
			
			boolean isSucc = false;
			if ("0".equals(isSuccess)) isSucc = true;
			processXSOrder(ffId,isSucc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("0").end();
	}
	
	/**
	 * 竹语 同步接口
	 * http://smspay.xushihudong.com/fee/feexszyback
	 */
	public void feexszyback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexszyback请求header信息：" + request.allHeaders());
			
			String isSuccess = request.getParam("status");
//			String mobile = request.getParam("paymentUser");
			String ffId = request.getParam("cppram");
			
			boolean isSucc = false;
			if ("0".equals(isSuccess)) isSucc = true;
			processXSOrder(ffId,isSucc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("0").end();
	}
	
	/**
	 * 讯鸿咪咕 同步接口
	 * http://smspay.xushihudong.com/fee/feexsxhmgback
	 */
	public void feexsxhmgback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexsxhmgback请求header信息：" + request.allHeaders());
			String content = request.body();
			logger.info("feexsxhmgback请求content信息："+content);
			Element root = XMLUtils.getInstance().parseXML2Element(content);
			
			String isSuccess = root.element("status").getText();
			String ffId = root.element("cpparam").getText();
			
			boolean isSucc = false;
			if ("0".equals(isSuccess)) isSucc = true;
			processXSOrder(ffId,isSucc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("<?xml version=\"1.0\" encoding=\"utf-8\"?><ROOT>< returnCode >0</ returnCode ></ROOT>").end();
	}
	
	/**
	 * 讯鸿咪咕单机 同步接口
	 * http://smspay.xushihudong.com/fee/feexsxhmgdjback
	 */
	public void feexsxhmgdjback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexsxhmgdjback请求header信息：" + request.allHeaders());
			String content = request.body();
			logger.info("feexsxhmgdjback请求content信息："+content);
			Element root = XMLUtils.getInstance().parseXML2Element(content);
			
			String isSuccess = root.element("status").getText();
			String ffId = root.element("cpparam").getText();
			
			boolean isSucc = false;
			if ("0".equals(isSuccess)) isSucc = true;
			processXSOrder(ffId,isSucc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("<?xml version=\"1.0\" encoding=\"utf-8\"?><ROOT>< returnCode >0</ returnCode ></ROOT>").end();
	}
	
	/**
	 * 易周文化阅读 同步接口
	 * http://smspay.xushihudong.com/fee/feexsyzwhydback
	 */
	public void feexsyzwhydback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexsyzwhydback请求header信息：" + request.allHeaders());
			String content = request.body();
			logger.info("feexsyzwhydback请求content信息："+content);
			Map<String, String> map = XmlUtil.xmlParsebytag(content, "request");
			
			String isSuccess = map.get("Status");
			String ffId = map.get("OutOrderID");
			ffId = ffId.substring(ffId.length()-16,ffId.length());
			
			boolean isSucc = false;
			if ("1".equals(isSuccess))
				isSucc = true;
			processXSOrder(ffId,isSucc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 易周文化 同步接口
	 * http://smspay.xushihudong.com/fee/feexsyzwhback
	 */
	public void feexsyzwhback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexsyzwhback请求header信息：" + request.allHeaders());
			String content = request.body();
			logger.info("feexsyzwhback请求content信息："+content);
			Map<String, String> map = XmlUtil.xmlParsebytag(content, "request");
			
			String isSuccess = map.get("hRet");
			String ffId = map.get("cpparam");
			
			boolean isSucc = false;
			if ("0".equals(isSuccess)) {
				isSucc = true;
				
				OrderReqInfo info = commonAction.queryOrderReqInfo(ffId);
				if (info != null && info.getApp() != null && info.getApp().length() > 0) {
					String spid = info.getSpId();
					if (!ffId.substring(2, 4).equals(spid.substring(0,2))) spid = "dm19";
					CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spid);
					
					String matchregex = codeInfo.getMatchRegex();
					String[] tmp = matchregex.split("##");
					String[] tmp1= info.getApp().split("##");
					String guid = tmp1[tmp1.length-1];
					String version = "2.0.0";
					String msgID = "3";
					Map<String, String> mapparam3 = new HashMap<String, String>();
					mapparam3.put("guid", guid);
					mapparam3.put("backdata", content);
					String data3 = Base64.encode(JSONObject.toJSONString(mapparam3).getBytes());
					
					Map<String, String> paramap3 = new HashMap<String, String>();
					paramap3.put("version", version);
					paramap3.put("msgID", msgID);
					paramap3.put("data", data3);
					String param3 = JSONObject.toJSONString(paramap3);
					String reqResult3 = HttpTool.sendYZWHPost(tmp[0], param3, 5000);
					logger.info("易周文化dm19请求msgID"+msgID+"结果:"+reqResult3);
				}
			}
			processXSOrder(ffId,isSucc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	private void commitlocal(HttpRequest request, String cpid) throws Exception {
		String spid = "dx07";
		String ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), spid);
		String mobile = request.getParam("phone");
		String status = request.getParam("status");
		String amount = request.getParam("amount");
		String spcmd = URLDecoder.decode(request.getParam("spcmd"),"utf-8");
		
		int fee = 0;
		if (amount != null && amount.length() > 0){
			fee = CommonTool.convertInt(amount);
		}
		
		int isSuccess = 0;
		if ("0".equals(status)) {
			isSuccess = 1;//订购成功
		} else if ("1".equals(status)) {
			if (spcmd != null && spcmd.startsWith("jyf")) {
				isSuccess = 1;
			} else {
				isSuccess = 2; //订购失败
			}
		} else if ("3".equals(status)) {
			isSuccess = 3; //退订
		}
		
		int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
		
		OrderReqInfo info = new OrderReqInfo();
		info.setMobile(mobile);
		info.setIsSuccess(isSuccess);
		
		
		CPInfo cp = commonAction.queryCP(cpid);
		OrderReqInfo order = commonAction.queryOrderByMobile(mobile, ffId);
		if (spcmd.contains("yldjdlb12sj")) order = null;//点播业务同一号码可以订购多个
		
		if (order == null) {
			String newCpId = cp.getCpId()+spid;
			int synRadio = cp.getSynRadio();
			logger.info("同步率synRadio："+synRadio);
			CPParam cpp = CommonTool.getCPParam(newCpId,
					synRadio);
			if (isSuccess == 1) {
				AtomicInteger ati = cpp.getAti();
				boolean isSyn = CommonTool.isContain(cpp.getVc(),
						ati.incrementAndGet());
				if (checkOrderNum(cp)) isSyn = true;
				if (isSyn) {
					if (cp.getUrl() != null && cp.getUrl().length() > 0) {
						// 同步
						String result = notityBYResult(cp,request);
						
						logger.info("给CP的同步结果："+result);
						if (result.equals("ok")) {
							synStatus = ConstantDefine.SYN_STATUS_TRUE;
						} else {
							synStatus = ConstantDefine.SYN_STATUS_FALSE;
						}
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				} else {
					synStatus = ConstantDefine.SYN_STATUS_Buckle;
				}
				
				if (ati.get() == 100)
					ati.set(0);
				cpp.setAti(ati);
				CommonTool.updateCPParam(newCpId, cpp);
			} else {
				// 同步
				String result = notityBYResult(cp,request);
				logger.info("给CP的同步结果："+result);
				if (result.equals("ok")) {
					synStatus = ConstantDefine.SYN_STATUS_TRUE;
				} else {
					synStatus = ConstantDefine.SYN_STATUS_FALSE;
				}
			}
			
			info.setSynStatus(synStatus);
			info.setFfId(ffId);
			info.setCpId(cpid);
			info.setSpId(spid);
			info.setFee(fee);
			info.setIp("127.0.0.1");
			info.setProvince("局域");
			info.setIsSyn(0);
			commonAction.addOrderReqInfoAll(info);
		} else {
			
			if (order.getSynStatus() != 4) {
				String result = notityBYResult(cp,request);
				logger.info("给CP的同步结果："+result);
			}
			
			info.setFfId(order.getFfId());
			info.setSynStatus(order.getSynStatus());
			commonAction.updateOrderInfo(info);
		}
		
	}
	
	private void commitdflocal(String mobile, int isSuccess, String amount, String cpid, String momsg,
			String spnumber, String cpparam) throws Exception {
		
		CPInfo cp = commonAction.queryCP(cpid);
		if (cp != null) {
			String spid = cp.getLocalSpId();
			String ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), spid);
			int fee = 0;
			if (amount != null && amount.length() > 0){
				fee = CommonTool.convertInt(amount);
			}
			
			int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setMobile(mobile);
			info.setIsSuccess(isSuccess);
			
//			OrderReqInfo order = commonAction.queryOrderByMobile(mobile, ffId);
			info.setSms(spnumber);
			info.setSmscontent(momsg);
			info.setFee(fee);
			info.setCpId(cpid);
			info.setFfId(ffId);
			info.setCpParam(momsg);
			info.setIp("127.0.0.1");
			info.setOrderId(ffId);
			info.setDelayed(cpparam);
			
			String newCpId = cp.getCpId()+spid;
			int synRadio = cp.getSynRadio();
			logger.info("同步率synRadio："+synRadio);
			CPParam cpp = CommonTool.getCPParam(newCpId,
					synRadio);
			if (isSuccess == 1) {
				AtomicInteger ati = cpp.getAti();
				boolean isSyn = CommonTool.isContain(cpp.getVc(),
						ati.incrementAndGet());
				if (checkOrderNum(cp)) isSyn = true;
				if (isSyn) {
					if (cp.getUrl() != null && cp.getUrl().length() > 0) {
						// 同步
						String result = notitydfBYResult(cp,info);
						
						logger.info("给CP的同步结果："+result);
						if (result.equals("ok")) {
							synStatus = ConstantDefine.SYN_STATUS_TRUE;
						} else {
							synStatus = ConstantDefine.SYN_STATUS_FALSE;
						}
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				} else {
					synStatus = ConstantDefine.SYN_STATUS_Buckle;
				}
				
				if (ati.get() == 100)
					ati.set(0);
				cpp.setAti(ati);
				CommonTool.updateCPParam(newCpId, cpp);
			} else {
				// 同步
				String result = notitydfBYResult(cp,info);
				logger.info("给CP的同步结果："+result);
				if (result.equals("ok")) {
					synStatus = ConstantDefine.SYN_STATUS_TRUE;
				} else {
					synStatus = ConstantDefine.SYN_STATUS_FALSE;
				}
			}
			
			info.setSynStatus(synStatus);
			info.setFfId(ffId);
			info.setCpId(cpid);
			info.setSpId(spid);
			info.setFee(fee);
			info.setIp("127.0.0.1");
			info.setProvince("局域");
			info.setIsSyn(0);
			info.setCpParam(momsg+"_"+spnumber);
			commonAction.addOrderReqInfoAll(info);
		}
		
		
	}
	
	private void commitback(HttpRequest request) throws Exception {
		String admount = request.getParam("admount");//金额 单位：分
		String productid = request.getParam("productid");//产品id
		String mobile = request.getParam("phone");//手机号
		String isStatus = request.getParam("status");//状态  0：订购  3：退订
		String addtime = request.getParam("ordertime");//订购/退订时间
		String spnum = request.getParam("spnum");//长号码
		String spcmd = request.getParam("spcmd");//指令
		
		String result = "";
		String param = "";
		String url = "http://121.40.72.80/lwpay/feeBYNotity15";
		StringBuilder msg = new StringBuilder();
		msg.append("admount=").append(admount)
		.append("&productid=").append(productid)
		.append("&phone=").append(mobile)
		.append("&status=").append(isStatus)
		.append("&ordertime=").append(URLEncoder.encode(addtime,"utf-8"))
		.append("&spnum=").append(spnum)
		.append("&spcmd=").append(spcmd);
		param = msg.toString();
		result = HttpTool.sendHttp(url+"?"+param, "", "utf-8");
		logger.info(result);
	}
	
	/**
	 * 北京翼联电信验证码提交
	 * http://120.24.88.90/fee/commitVerifyCode
	 */
	public void commitVerifyCode(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("commitVerifyCode请求header信息：" + request.allHeaders());
			
			String code = request.getParam("code");
			String orderid = request.getParam("orderid");
			String url = "http://123.57.17.32:700/YiLianXinRui/SubmitVCode.aspx";
			
			StringBuilder param = new StringBuilder();
			param.append("code").append(code)
			.append("&orderid=").append(orderid);
			
			String result = HttpTool.sendGet(url, param.toString());
			logger.info("commitVerifyCode验证码提交结果：" + result);
			
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}

	/**
	 * 北京翼联电信验证码提交
	 * http://ltxpay.huizhousenfeng.com/fee/test
	 */
	public void test(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("test请求header信息：" + request.allHeaders());

			logger.info("test请求body：" + request.body());
			
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}

		response.content("ok").end();
	}
	
	/**
	 * 处理订单
	 */
	private void processOrder(String ffId, boolean isSuc) {
		try {
			int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
			if (!isSuc) {
//				commonAction.updateOrder(ffId, isSuc, synStatus);
				return;
			} else {
				commonAction.saveExt(ffId);
			}
			
			OrderT ot = commonAction.queryOrderT(ffId);
			if (ot == null) {
				logger.warn("找不到对应的订单号=" + ffId);
				return;
			}
			
			OrderReqInfo order = ot.getOrder();
			CPInfo cp = ot.getCp();
			
			if (order.getIsSyn() == ConstantDefine.SYN_TRUE && order.getSynStatus() == 0) {
				order.setIsSuccess(1);
				String flag = testImsiMap.get(order.getImsi());
				int synRadio = cp.getSynRadio();
				if (synRadio == 100) flag = "true";
				if (!"true".equals(flag)) {
					String province = order.getProvince();
					String newCpId = cp.getCpId()+order.getSpId()+province;
					
					synRadio = getSynRadio(cp, province, synRadio);
					
					logger.info("同步省份：" + province + "  同步率synRadio："+synRadio);
					CPParam cpp = CommonTool.getCPParam(newCpId,
							synRadio);
					
					AtomicInteger ati = cpp.getAti();
					boolean isSyn = CommonTool.isContain(cpp.getVc(),
							ati.incrementAndGet());
					if (checkOrderNum(cp)) isSyn = true;
					if (isSyn) {
						if (cp.getUrl() != null && cp.getUrl().length() > 0) {
							// 同步
							String result = notityResult(cp,ffId,order);
							
							logger.info("给CP的同步结果："+result);
							if (result.equals("ok")) {
								synStatus = ConstantDefine.SYN_STATUS_TRUE;
							} else {
								synStatus = ConstantDefine.SYN_STATUS_FALSE;
							}
						} else {
							synStatus = ConstantDefine.SYN_STATUS_FALSE;
						}
					} else {
						synStatus = ConstantDefine.SYN_STATUS_Buckle;
					}
					
					if (ati.get() == 100)
						ati.set(0);
					cpp.setAti(ati);
//					CommonTool.updateCPParam(cp.getCpId(), cpp);
					CommonTool.updateCPParam(newCpId, cpp);
				} else {
					String result = notityResult(cp,ffId,order);
					
					logger.info("给CP的同步结果："+result);
					if (result.equals("ok")) {
						synStatus = ConstantDefine.SYN_STATUS_TRUE;
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				}
			}
			if (order.getSynStatus() == 0) {
				commonAction.updateOrder(ffId, isSuc, synStatus);
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
	}
	
	private int getSynRadio(CPInfo cp, String province, int synRadio) {
		int synOpen = cp.getSynOpen();
		if (synOpen == 1) {
			String synProvinces = cp.getSynProvince();
			String synNums = cp.getSynNum();
			if (synProvinces != null && synProvinces.length() > 0 &&
					synNums != null && synNums.length() > 0) {
				String[] synProvinceArr = synProvinces.split("#");
				String[] synNumArr = synNums.split("#");
				if (synNumArr.length == synProvinceArr.length) {
					for (int i=0; i<synProvinceArr.length; i++) {
						if (synProvinceArr[i].equals(province)) {
							synRadio = synRadio + CommonTool.convertInt(synNumArr[i]);
							break;
						}
					}
				}
			}
		}
		
		return synRadio;
	}
	
	/**
	 * 处理订单
	 */
	private void processOtherOrder(String ffId, boolean isSuc) {
		try {
			int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
			
			OrderT ot = commonAction.queryOrderT(ffId);
			if (ot == null) {
				logger.warn("找不到对应的订单号=" + ffId);
				return;
			}
			
			OrderReqInfo order = ot.getOrder();
			CPInfo cp = ot.getCp();
			if (cp == null) {
				logger.warn("对应cp已关闭计费功能cpid=" + ot.getCp());
				return;
			}
			
			if (isSuc) order.setIsSuccess(1);
			String result = notityOtherResult(cp,ffId,order);
			int count = 1;
			
			logger.info("给CP的同步结果："+result);
			if ("ok".equals(result)) {
				synStatus = ConstantDefine.SYN_STATUS_TRUE;
			} else {
//				if (isSuc && "gzjc01".equals(cp.getCpId())) {
				if (isSuc) {
					do {
						Thread.sleep(3000);
						result = notityOtherResult(cp,ffId,order);
						count += 1;
						logger.error(ffId + "订单第" + count + "次同步" );
					} while (count < 5 && !"ok".equals(result));
					if ("ok".equals(result)) {
						synStatus = ConstantDefine.SYN_STATUS_TRUE;
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				} else {
					synStatus = ConstantDefine.SYN_STATUS_FALSE;
				}
			}
			commonAction.updateOrder(ffId, isSuc, synStatus);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
	}
	
	/**
	 * 处理订单
	 */
	private void processOtherOrder(String ffId, boolean isSuc, String traid) {
		try {
			int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
			
			OrderT ot = commonAction.queryOrderT(ffId);
			if (ot == null) {
				logger.warn("找不到对应的订单号=" + ffId);
				return;
			}
			
			OrderReqInfo order = ot.getOrder();
			CPInfo cp = ot.getCp();
			if (cp == null) {
				logger.warn("对应cp已关闭计费功能cpid=" + ot.getCp());
				return;
			}
			
			if (isSuc) order.setIsSuccess(1);
			order.setPmodel(traid);
			
			String result = notityOtherResult(cp,ffId,order);
			int count = 1;
			
			logger.info("给CP的同步结果："+result);
			if ("ok".equals(result)) {
				synStatus = ConstantDefine.SYN_STATUS_TRUE;
			} else {
//				if (isSuc && "gzjc01".equals(cp.getCpId())) {
				if (isSuc) {
					do {
						Thread.sleep(3000);
						result = notityOtherResult(cp,ffId,order);
						count += 1;
						logger.error(ffId + "订单第" + count + "次同步" );
					} while (count < 5 && !"ok".equals(result));
					if ("ok".equals(result)) {
						synStatus = ConstantDefine.SYN_STATUS_TRUE;
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				} else {
					synStatus = ConstantDefine.SYN_STATUS_FALSE;
				}
			}
//			commonAction.updateOrder(ffId, isSuc, synStatus);
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			info.setIsSuccess(order.getIsSuccess());
			info.setCtech(order.getCtech());
			if (synStatus == -1) {
				synStatus = ConstantDefine.SYN_STATUS_RESTART;
			}
			info.setSynStatus(synStatus);
			info.setPmodel(traid);
			commonAction.updateOrderInfo(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
	}

	/**
	 * 处理订单
	 */
	private void processRTBYOrder(OrderReqInfo reqInfo) {
		try {
			String ffId = reqInfo.getFfId();
			int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
			
			OrderReqInfo order = commonAction.queryOrderByCondition(reqInfo);
			if (order == null) {
				logger.warn("找不到对应的订单号=" + reqInfo.getFfId());
				return;
			}
			
			ffId = order.getFfId();
			reqInfo.setFfId(ffId);
			if (order.getSynStatus() == 4) {
				order.setIsSyn(0);
				synStatus = 4;
			}
			CPInfo cp = commonAction.queryCP(order.getCpId());
			if (cp == null) {
				logger.warn("对应cp的通道已经关闭=" + order.getCpId());
				return;
			}
			
			order.setIsSuccess(reqInfo.getIsSuccess());
			if (order.getIsSyn() == ConstantDefine.SYN_TRUE) {
				String flag = testImsiMap.get(order.getImsi());
				int synRadio = cp.getSynRadio();
				if (reqInfo.getIsSuccess() != 1 || synRadio == 100) flag = "true";
				
				if (!"true".equals(flag)) {
					String newCpId = cp.getCpId()+order.getSpId();
					
					logger.info("同步率synRadio："+synRadio);
					CPParam cpp = CommonTool.getCPParam(newCpId,
							synRadio);
					
					AtomicInteger ati = cpp.getAti();
					boolean isSyn = CommonTool.isContain(cpp.getVc(),
							ati.incrementAndGet());
					if (checkOrderNum(cp)) isSyn = true;
					if (isSyn) {
						if (cp.getUrl() != null && cp.getUrl().length() > 0) {
							// 同步
							String result = notityBYResult(cp,ffId,order);
							
							logger.info("给CP的同步结果："+result);
							if (result.equals("ok")) {
								synStatus = ConstantDefine.SYN_STATUS_TRUE;
							} else {
								synStatus = ConstantDefine.SYN_STATUS_FALSE;
							}
						} else {
							synStatus = ConstantDefine.SYN_STATUS_FALSE;
						}
					} else {
						synStatus = ConstantDefine.SYN_STATUS_Buckle;
					}
					
					if (ati.get() == 100)
						ati.set(0);
					cpp.setAti(ati);
//					CommonTool.updateCPParam(cp.getCpId(), cpp);
					CommonTool.updateCPParam(newCpId, cpp);
				} else {
					String result = notityBYResult(cp,ffId,order);
					
					logger.info("给CP的同步结果："+result);
					if (result.equals("ok")) {
						synStatus = ConstantDefine.SYN_STATUS_TRUE;
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				}
			}
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(reqInfo.getFfId());
			info.setMobile(reqInfo.getMobile());
			info.setIsSuccess(reqInfo.getIsSuccess());
			info.setSynStatus(synStatus);
//			info.setNewffid(newffid);
			commonAction.updateOrderInfo(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
	}

	private void processNRTBYOrder(OrderReqInfo reqInfo) {
		String ffId = reqInfo.getFfId();
		
		try {
			int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
			/*if (!isSuc) {
//				commonAction.updateOrder(ffId, isSuc, synStatus);
				return;
			} else {
				commonAction.saveExt(ffId);
			}*/
			
			OrderT ot = commonAction.queryOrderT(ffId);
			if (ot == null) {
				logger.warn("找不到对应的订单号=" + ffId);
				return;
			}
			
			OrderReqInfo order = ot.getOrder();
			order.setMobile(reqInfo.getMobile());
			CPInfo cp = ot.getCp();
			if (order.getSynStatus() == 4) {
				order.setIsSyn(0);
				synStatus = 4;
			}
			
			if (order.getIsSuccess() != 0) {
				logger.warn("屏蔽重复同步，订单号：" + order.getFfId());
				return;
			}
			if (cp == null) {
				logger.warn("对应cp的通道已经关闭=" + ot.getCp());
				return;
			}
			
			
			String newffid =  reqInfo.getNewffid();
			order.setIsSuccess(reqInfo.getIsSuccess());
			if (order.getIsSyn() == ConstantDefine.SYN_TRUE) {
				String flag = testImsiMap.get(order.getImsi());
				
				int synRadio = cp.getSynRadio();
				if (reqInfo.getIsSuccess() != 1 || synRadio == 100) flag = "true";
				
				
				if (newffid != null && newffid.length() > 0) ffId = newffid;
				
				if (!"true".equals(flag)) {
					String newCpId = cp.getCpId()+order.getSpId();
					
					logger.info("同步率synRadio："+synRadio);
					CPParam cpp = CommonTool.getCPParam(newCpId,
							synRadio);
					
					AtomicInteger ati = cpp.getAti();
					boolean isSyn = CommonTool.isContain(cpp.getVc(),
							ati.incrementAndGet());
					if (checkOrderNum(cp)) isSyn = true;
					if (isSyn) {
						if (cp.getUrl() != null && cp.getUrl().length() > 0) {
							// 同步
							String result = notityBYResult(cp,ffId,order);
							
							logger.info("给CP的同步结果："+result);
							if (result.equals("ok")) {
								synStatus = ConstantDefine.SYN_STATUS_TRUE;
							} else {
								synStatus = ConstantDefine.SYN_STATUS_FALSE;
							}
						} else {
							synStatus = ConstantDefine.SYN_STATUS_FALSE;
						}
					} else {
						synStatus = ConstantDefine.SYN_STATUS_Buckle;
//						String result = notityBYResultBuckle(cp, newffid, order);
//						logger.info("给CP的同步结果B："+result);
					}
					
					if (ati.get() == 100)
						ati.set(0);
					cpp.setAti(ati);
//					CommonTool.updateCPParam(cp.getCpId(), cpp);
					CommonTool.updateCPParam(newCpId, cpp);
				} else {
					String result = notityBYResult(cp,ffId,order);
					
					logger.info("给CP的同步结果："+result);
					if (result.equals("ok")) {
						synStatus = ConstantDefine.SYN_STATUS_TRUE;
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				}
			}
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(reqInfo.getFfId());
			info.setMobile(reqInfo.getMobile());
			info.setIsSuccess(reqInfo.getIsSuccess());
			info.setSynStatus(synStatus);
			info.setNewffid(newffid);
			info.setCtech(reqInfo.getCtech());
			info.setApp(reqInfo.getApp());
			info.setPck(reqInfo.getPck());
			
			commonAction.updateOrderInfo(info);
			
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
	}
	/**
	 * 微信账号注册处理订单
	 * @return 
	 */
	private void processWXNRTBYOrder(OrderReqInfo reqInfo) {
		String ffId = reqInfo.getFfId();
		
		try {
			int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
			/*if (!isSuc) {
//				commonAction.updateOrder(ffId, isSuc, synStatus);
				return;
			} else {
				commonAction.saveExt(ffId);
			}*/
			
			OrderT ot = commonAction.queryOrderT(ffId);
			if (ot == null) {
				logger.warn("找不到对应的订单号=" + ffId);
				return;
			}
			
			OrderReqInfo order = ot.getOrder();
			order.setMobile(reqInfo.getMobile());
			order.setPck(reqInfo.getPck());
			CPInfo cp = ot.getCp();
			if (order.getSynStatus() == 4) {
				order.setIsSyn(0);
				synStatus = 4;
			}
			
			if (order.getIsSuccess() != 0) {
				logger.warn("屏蔽重复同步，订单号：" + order.getFfId());
				return;
			}else if (cp == null) {
				logger.warn("对应cp的通道已经关闭=" + ot.getCp());
				return;
			}
			
			
			String newffid =  reqInfo.getNewffid();
			order.setIsSuccess(reqInfo.getIsSuccess());
			if (order.getIsSyn() == ConstantDefine.SYN_TRUE) {
				String flag = testImsiMap.get(order.getImsi());
				
				int synRadio = cp.getSynRadio();
				if (reqInfo.getIsSuccess() != 1 || synRadio == 100) flag = "true";
				
				
				if (newffid != null && newffid.length() > 0) ffId = newffid;
				
				if (!"true".equals(flag)) {
					String newCpId = cp.getCpId()+order.getSpId();
					
					logger.info("同步率synRadio："+synRadio);
					CPParam cpp = CommonTool.getCPParam(newCpId,
							synRadio);
					
					AtomicInteger ati = cpp.getAti();
					boolean isSyn = CommonTool.isContain(cpp.getVc(),
							ati.incrementAndGet());
					if (checkOrderNum(cp)) isSyn = true;
					if (isSyn) {
						if (cp.getUrl() != null && cp.getUrl().length() > 0) {
							// 同步
							String result = notityBYResultWX(cp,ffId,order);
							
							logger.info("给CP的同步结果："+result);
							if (result.equals("ok")) {
								synStatus = ConstantDefine.SYN_STATUS_TRUE;
							} else {
								synStatus = ConstantDefine.SYN_STATUS_FALSE;
							}
						} else {
							synStatus = ConstantDefine.SYN_STATUS_FALSE;
						}
					} else {
						synStatus = ConstantDefine.SYN_STATUS_Buckle;
					}
					
					if (ati.get() == 100)
						ati.set(0);
					cpp.setAti(ati);
//					CommonTool.updateCPParam(cp.getCpId(), cpp);
					CommonTool.updateCPParam(newCpId, cpp);
				} else {
					String result = notityBYResultWX(cp,ffId,order);
					
					logger.info("给CP的同步结果："+result);
					if (result.equals("ok")) {
						synStatus = ConstantDefine.SYN_STATUS_TRUE;
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				}
			}
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(reqInfo.getFfId());
			info.setMobile(reqInfo.getMobile());
			info.setIsSuccess(reqInfo.getIsSuccess());
			info.setSynStatus(synStatus);
			info.setNewffid(newffid);
			info.setCtech(reqInfo.getCtech());
			info.setApp(reqInfo.getApp());
			info.setPck(reqInfo.getPck());
			
			commonAction.updateOrderInfo(info);
			
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
	}
	
	/**
	 * MG处理订单
	 * @return 
	 */
	private void processMGNRTBYOrder(OrderReqInfo reqInfo) {
		String ffId = reqInfo.getFfId();
	
		try {
			int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
			/*if (!isSuc) {
//				commonAction.updateOrder(ffId, isSuc, synStatus);
				return;
			} else {
				commonAction.saveExt(ffId);
			}*/
			
			OrderT ot = commonAction.queryOrderT(ffId);
			if (ot == null) {
				logger.warn("找不到对应的订单号=" + ffId);
				return;
			}
			
			OrderReqInfo order = ot.getOrder();
			order.setMobile(reqInfo.getMobile());
			CPInfo cp = ot.getCp();
			if (order.getSynStatus() == 4) {
				order.setIsSyn(0);
				synStatus = 4;
			}
			
			if (order.getIsSuccess() != 0) {
				logger.warn("屏蔽重复同步，订单号：" + order.getFfId());
				return;
			}else if (cp == null) {
				logger.warn("对应cp的通道已经关闭=" + ot.getCp());
				return;
			}
			
			
			String newffid =  reqInfo.getNewffid();
			order.setIsSuccess(reqInfo.getIsSuccess());
			if (order.getIsSyn() == ConstantDefine.SYN_TRUE) {
				String flag = testImsiMap.get(order.getImsi());
				
				int synRadio = cp.getSynRadio();
				if (reqInfo.getIsSuccess() != 1 || synRadio == 100) flag = "true";
				
				
				if (newffid != null && newffid.length() > 0) ffId = newffid;
				
				if (!"true".equals(flag)) {
					String newCpId = cp.getCpId()+order.getSpId();
					
					logger.info("同步率synRadio："+synRadio);
					CPParam cpp = CommonTool.getCPParam(newCpId,
							synRadio);
					
					AtomicInteger ati = cpp.getAti();
					boolean isSyn = CommonTool.isContain(cpp.getVc(),
							ati.incrementAndGet());
					if (checkOrderNum(cp)) isSyn = true;
					if (isSyn) {
						if (cp.getUrl() != null && cp.getUrl().length() > 0) {
							// 同步
							String result = notityBYResult(cp,ffId,order);
							
							logger.info("给CP的同步结果："+result);
							if (result.equals("ok")) {
								synStatus = ConstantDefine.SYN_STATUS_TRUE;
							} else {
								synStatus = ConstantDefine.SYN_STATUS_FALSE;
							}
						} else {
							synStatus = ConstantDefine.SYN_STATUS_FALSE;
						}
					} else {
						synStatus = ConstantDefine.SYN_STATUS_Buckle;
					}
					
					if (ati.get() == 100)
						ati.set(0);
					cpp.setAti(ati);
//					CommonTool.updateCPParam(cp.getCpId(), cpp);
					CommonTool.updateCPParam(newCpId, cpp);
				} else {
					String result = notityBYResult(cp,ffId,order);
					
					logger.info("给CP的同步结果："+result);
					if (result.equals("ok")) {
						synStatus = ConstantDefine.SYN_STATUS_TRUE;
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				}
			}
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(reqInfo.getFfId());
			info.setMobile(reqInfo.getMobile());
			info.setIsSuccess(reqInfo.getIsSuccess());
			info.setSynStatus(synStatus);
			info.setNewffid(newffid);
			info.setCtech(reqInfo.getCtech());
			info.setApp(reqInfo.getApp());
			info.setPck(reqInfo.getPck());
			
			commonAction.updateOrderInfo(info);
			
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
	}
	/**
	 * 处理订单sd46Date
	 */
	private void processBYOrdersd46Date(OrderReqInfo reqInfo) {
		String ffId = reqInfo.getFfId();
		try {
			int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
			/*if (!isSuc) {
//				commonAction.updateOrder(ffId, isSuc, synStatus);
				return;
			} else {
				commonAction.saveExt(ffId);
			}*/
			
//			OrderT ot = commonAction.queryOrderT(ffId);
			OrderT ot = commonAction.queryOrderTDate(ffId);
			if (ot == null) {
				logger.warn("找不到对应的订单号=" + ffId);
				return;
			}
			
			OrderReqInfo order = ot.getOrder();
			order.setMobile(reqInfo.getMobile());
			CPInfo cp = ot.getCp();
			if (order.getSynStatus() == 4) {
				order.setIsSyn(0);
				synStatus = 4;
			}
			if(order.getIsSuccess() != 0){
				logger.warn("屏蔽重复同步，订单号：" + ffId);
				return;
			}
			if (cp == null) {
				logger.warn("对应cp的通道已经关闭=" + ot.getCp());
				return;
			}
			
			String newffid =  reqInfo.getNewffid();
			order.setIsSuccess(reqInfo.getIsSuccess());
			if (order.getIsSyn() == ConstantDefine.SYN_TRUE) {
				String flag = testImsiMap.get(order.getImsi());
				
				int synRadio = cp.getSynRadio();
				if (reqInfo.getIsSuccess() != 1 || synRadio == 100) flag = "true";
				
				
				if (newffid != null && newffid.length() > 0) ffId = newffid;
				
				if (!"true".equals(flag)) {
					String newCpId = cp.getCpId()+order.getSpId();
					
					logger.info("同步率synRadio："+synRadio);
					CPParam cpp = CommonTool.getCPParam(newCpId,
							synRadio);
					
					AtomicInteger ati = cpp.getAti();
					boolean isSyn = CommonTool.isContain(cpp.getVc(),
							ati.incrementAndGet());
					if (checkOrderNum(cp)) isSyn = true;
					if (isSyn) {
						if (cp.getUrl() != null && cp.getUrl().length() > 0) {
							// 同步
							String result = notityBYResult(cp,ffId,order);
							
							logger.info("给CP的同步结果："+result);
							if (result.equals("ok")) {
								synStatus = ConstantDefine.SYN_STATUS_TRUE;
							} else {
								synStatus = ConstantDefine.SYN_STATUS_FALSE;
							}
						} else {
							synStatus = ConstantDefine.SYN_STATUS_FALSE;
						}
					} else {
						synStatus = ConstantDefine.SYN_STATUS_Buckle;
//						String result = notityBYResultBuckle(cp,ffId,order);
//						logger.info("给CP的同步结果###："+result);
					}
					
					if (ati.get() >= 100)
						ati.set(0);
					cpp.setAti(ati);
//					CommonTool.updateCPParam(cp.getCpId(), cpp);
					CommonTool.updateCPParam(newCpId, cpp);
					
				} else {
					String result = notityBYResult(cp,ffId,order);
					
					logger.info("给CP的同步结果："+result);
					if (result.equals("ok")) {
						synStatus = ConstantDefine.SYN_STATUS_TRUE;
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				}
			}
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(reqInfo.getFfId());
			info.setIsSuccess(reqInfo.getIsSuccess());
			info.setSynStatus(synStatus);
			
			commonAction.updateOrderInfoDate(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
	}
	/**
	 * 处理订单sd46Date
	 */
	private void processBYOrdersd59Date(OrderReqInfo reqInfo) {
		String ffId = reqInfo.getFfId();
		try {
			int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
			/*if (!isSuc) {
//				commonAction.updateOrder(ffId, isSuc, synStatus);
				return;
			} else {
				commonAction.saveExt(ffId);
			}*/
			
//			OrderT ot = commonAction.queryOrderT(ffId);
			OrderT ot = commonAction.queryOrderTDate(ffId);
			if (ot == null) {
				logger.warn("找不到对应的订单号=" + ffId);
				return;
			}
			
			OrderReqInfo order = ot.getOrder();
			order.setMobile(reqInfo.getMobile());
			CPInfo cp = ot.getCp();
			if (order.getSynStatus() == 4) {
				order.setIsSyn(0);
				synStatus = 4;
			}
			if(order.getIsSuccess() != 0){
				logger.warn("屏蔽重复同步，订单号：" + ffId);
				return;
			}
			if (cp == null) {
				logger.warn("对应cp的通道已经关闭=" + ot.getCp());
				return;
			}
			
			String newffid =  reqInfo.getNewffid();
			order.setIsSuccess(reqInfo.getIsSuccess());
			if (order.getIsSyn() == ConstantDefine.SYN_TRUE) {
				String flag = testImsiMap.get(order.getImsi());
				
				int synRadio = cp.getSynRadio();
				if (reqInfo.getIsSuccess() != 1 || synRadio == 100) flag = "true";
				
				
				if (newffid != null && newffid.length() > 0) ffId = newffid;
				
				if (!"true".equals(flag)) {
					String newCpId = cp.getCpId()+order.getSpId();
					
					logger.info("同步率synRadio："+synRadio);
					CPParam cpp = CommonTool.getCPParam(newCpId,
							synRadio);
					
					AtomicInteger ati = cpp.getAti();
					boolean isSyn = CommonTool.isContain(cpp.getVc(),
							ati.incrementAndGet());
					if (checkOrderNum(cp)) isSyn = true;
					if (isSyn) {
						if (cp.getUrl() != null && cp.getUrl().length() > 0) {
							// 同步
							String result = notityBYResult(cp,ffId,order);
							
							logger.info("给CP的同步结果："+result);
							if (result.equals("ok")) {
								synStatus = ConstantDefine.SYN_STATUS_TRUE;
							} else {
								synStatus = ConstantDefine.SYN_STATUS_FALSE;
							}
						} else {
							synStatus = ConstantDefine.SYN_STATUS_FALSE;
						}
					} else {
						synStatus = ConstantDefine.SYN_STATUS_Buckle;
						String result = notityBYResultBuckle(cp,ffId,order);
						logger.info("给CP的同步结果###："+result);
					}
					
					if (ati.get() >= 100)
						ati.set(0);
					cpp.setAti(ati);
//					CommonTool.updateCPParam(cp.getCpId(), cpp);
					CommonTool.updateCPParam(newCpId, cpp);
					
				} else {
					String result = notityBYResult(cp,ffId,order);
					
					logger.info("给CP的同步结果："+result);
					if (result.equals("ok")) {
						synStatus = ConstantDefine.SYN_STATUS_TRUE;
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				}
			}
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(reqInfo.getFfId());
			info.setIsSuccess(reqInfo.getIsSuccess());
			info.setSynStatus(synStatus);
			
			commonAction.updateOrderInfoDate(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
	}
	/**
	 * 处理订单sd46Date
	 */
	private void processBYOrdersd46DateGrade(OrderReqInfo reqInfo) {
		String ffId = reqInfo.getFfId();
		try {
			int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
			/*if (!isSuc) {
//				commonAction.updateOrder(ffId, isSuc, synStatus);
				return;
			} else {
				commonAction.saveExt(ffId);
			}*/
			
//			OrderT ot = commonAction.queryOrderT(ffId);
			OrderT ot = commonAction.queryOrderTDate(ffId);
			if (ot == null) {
				logger.warn("找不到对应的订单号=" + ffId);
				return;
			}
			
			OrderReqInfo order = ot.getOrder();
			order.setMobile(reqInfo.getMobile());
			CPInfo cp = ot.getCp();
			if (order.getSynStatus() == 4) {
				order.setIsSyn(0);
				synStatus = 4;
			}
			if(order.getIsSuccess() != 0){
				logger.warn("屏蔽重复同步，订单号：" + ffId);
				return;
			}
			if (cp == null) {
				logger.warn("对应cp的通道已经关闭=" + ot.getCp());
				return;
			}
			
			String newffid =  reqInfo.getNewffid();
			order.setIsSuccess(reqInfo.getIsSuccess());
			order.setPck(reqInfo.getPck());
			if (order.getIsSyn() == ConstantDefine.SYN_TRUE) {
				String flag = testImsiMap.get(order.getImsi());
				
				int synRadio = cp.getSynRadio();
				if (reqInfo.getIsSuccess() != 1 || synRadio == 100) flag = "true";
				
				
				if (newffid != null && newffid.length() > 0) ffId = newffid;
				
				if (!"true".equals(flag)) {
					String newCpId = cp.getCpId()+order.getSpId();
					
					logger.info("同步率synRadio："+synRadio);
					CPParam cpp = CommonTool.getCPParam(newCpId,
							synRadio);
					
					AtomicInteger ati = cpp.getAti();
					boolean isSyn = CommonTool.isContain(cpp.getVc(),
							ati.incrementAndGet());
					if (checkOrderNum(cp)) isSyn = true;
					if (isSyn) {
//						a.incrementAndGet();
						if (cp.getUrl() != null && cp.getUrl().length() > 0) {
							// 同步
							String result = notityBYResultGrade(cp,ffId,order);
							
							logger.info("给CP的同步结果："+result);
							if (result.equals("ok")) {
								synStatus = ConstantDefine.SYN_STATUS_TRUE;
							} else {
								synStatus = ConstantDefine.SYN_STATUS_FALSE;
							}
						} else {
							synStatus = ConstantDefine.SYN_STATUS_FALSE;
						}
					} else {
//						b.incrementAndGet();
						synStatus = ConstantDefine.SYN_STATUS_Buckle;
//						String result = notityBYResultBuckle(cp,ffId,order);
//						logger.info("给CP的同步结果###："+result);
					}
					
					if (ati.get() == 100)
						ati.set(0);
					cpp.setAti(ati);
//					CommonTool.updateCPParam(cp.getCpId(), cpp);
					CommonTool.updateCPParam(newCpId, cpp);
					
				} else {
					String result = notityBYResultGrade(cp,ffId,order);
					
					logger.info("给CP的同步结果："+result);
					if (result.equals("ok")) {
						synStatus = ConstantDefine.SYN_STATUS_TRUE;
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				}
			}
//			logger.info("aa:bb"+a+","+b);
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(reqInfo.getFfId());
			info.setIsSuccess(reqInfo.getIsSuccess());
			info.setSynStatus(synStatus);
			info.setPck(reqInfo.getPck());
			commonAction.updateOrderInfoDate(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
	}
	/**
	 * 处理订单sd46Date
	 */
	private void processBYOrdersd46DateGrades(OrderReqInfo reqInfo) {
		String ffId = reqInfo.getFfId();
		try {
			int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
			/*if (!isSuc) {
//				commonAction.updateOrder(ffId, isSuc, synStatus);
				return;
			} else {
				commonAction.saveExt(ffId);
			}*/
			
//			OrderT ot = commonAction.queryOrderT(ffId);
			OrderT ot = commonAction.queryOrderTDate(ffId);
			if (ot == null) {
				logger.warn("找不到对应的订单号=" + ffId);
				return;
			}
			
			OrderReqInfo order = ot.getOrder();
			order.setMobile(reqInfo.getMobile());
			CPInfo cp = ot.getCp();
			if (order.getSynStatus() == 4) {
				order.setIsSyn(0);
				synStatus = 4;
			}
			if(order.getIsSuccess() != 0){
				logger.warn("屏蔽重复同步，订单号：" + ffId);
				return;
			}
			if (cp == null) {
				logger.warn("对应cp的通道已经关闭=" + ot.getCp());
				return;
			}
			
			String newffid =  reqInfo.getNewffid();
			order.setIsSuccess(reqInfo.getIsSuccess());
			order.setPck(reqInfo.getPck());
			if (order.getIsSyn() == ConstantDefine.SYN_TRUE) {
				String flag = testImsiMap.get(order.getImsi());
				
				int synRadio = cp.getSynRadio();
				if (reqInfo.getIsSuccess() != 1 || synRadio == 100) flag = "true";
				
				
				if (newffid != null && newffid.length() > 0) ffId = newffid;
				
				if (!"true".equals(flag)) {
					String newCpId = cp.getCpId()+order.getSpId();
					
					logger.info("同步率synRadio："+synRadio);
					CPParam cpp = CommonTool.getCPParam(newCpId,
							synRadio);
					
					AtomicInteger ati = cpp.getAti();
					boolean isSyn = CommonTool.isContain(cpp.getVc(),
							ati.incrementAndGet());
					if (checkOrderNum(cp)) isSyn = true;
					if (isSyn) {
//						a.incrementAndGet();
						if (cp.getUrl() != null && cp.getUrl().length() > 0) {
							// 同步
							String result = notityBYResultGrade(cp,ffId,order);
							
							logger.info("给CP的同步结果："+result);
							if (result.equals("ok")) {
								synStatus = ConstantDefine.SYN_STATUS_TRUE;
							} else {
								synStatus = ConstantDefine.SYN_STATUS_FALSE;
							}
						} else {
							synStatus = ConstantDefine.SYN_STATUS_FALSE;
						}
					} else {
//						b.incrementAndGet();
						synStatus = ConstantDefine.SYN_STATUS_Buckle;
//						String result = notityBYResultBuckle(cp,ffId,order);
//						logger.info("给CP的同步结果###："+result);
					}
					
					if (ati.get() == 100)
						ati.set(0);
					cpp.setAti(ati);
//					CommonTool.updateCPParam(cp.getCpId(), cpp);
					CommonTool.updateCPParam(newCpId, cpp);
					
				} else {
					String result = notityBYResultGrade(cp,ffId,order);
					
					logger.info("给CP的同步结果："+result);
					if (result.equals("ok")) {
						synStatus = ConstantDefine.SYN_STATUS_TRUE;
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				}
			}
//			logger.info("aa:bb"+a+","+b);
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(reqInfo.getFfId());
			info.setIsSuccess(reqInfo.getIsSuccess());
			info.setSynStatus(synStatus);
			info.setPck(reqInfo.getPck());
			commonAction.updateOrderInfoDate(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
	}
	/**
	 * 处理订单sd46
	 */
	private void processBYOrdersd46(OrderReqInfo reqInfo) {
		String ffId = reqInfo.getFfId();
		try {
			int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
			/*if (!isSuc) {
//				commonAction.updateOrder(ffId, isSuc, synStatus);
				return;
			} else {
				commonAction.saveExt(ffId);
			}*/
			
			OrderT ot = commonAction.queryOrderT(ffId);
			if (ot == null) {
				logger.warn("找不到对应的订单号=" + ffId);
				return;
			}
			
			OrderReqInfo order = ot.getOrder();
			order.setMobile(reqInfo.getMobile());
			CPInfo cp = ot.getCp();
			if (order.getSynStatus() == 4) {
				order.setIsSyn(0);
				synStatus = 4;
			}
			if(order.getIsSuccess() != 0){
				logger.warn("屏蔽重复同步，订单号：" + ffId);
				return;
			}
			if (cp == null) {
				logger.warn("对应cp的通道已经关闭=" + ot.getCp());
				return;
			}
			
			String newffid =  reqInfo.getNewffid();
			order.setIsSuccess(reqInfo.getIsSuccess());
			if (order.getIsSyn() == ConstantDefine.SYN_TRUE) {
				String flag = testImsiMap.get(order.getImsi());
				
				int synRadio = cp.getSynRadio();
				if (reqInfo.getIsSuccess() != 1 || synRadio == 100) flag = "true";
				
				
				if (newffid != null && newffid.length() > 0) ffId = newffid;
				
				if (!"true".equals(flag)) {
					String newCpId = cp.getCpId()+order.getSpId();
					
					logger.info("同步率synRadio："+synRadio);
					CPParam cpp = CommonTool.getCPParam(newCpId,
							synRadio);
					
					AtomicInteger ati = cpp.getAti();
					boolean isSyn = CommonTool.isContain(cpp.getVc(),
							ati.incrementAndGet());
					if (checkOrderNum(cp)) isSyn = true;
					if (isSyn) {
						if (cp.getUrl() != null && cp.getUrl().length() > 0) {
							// 同步
							String result = notityBYResult(cp,ffId,order);
							
							logger.info("给CP的同步结果："+result);
							if (result.equals("ok")) {
								synStatus = ConstantDefine.SYN_STATUS_TRUE;
							} else {
								synStatus = ConstantDefine.SYN_STATUS_FALSE;
							}
						} else {
							synStatus = ConstantDefine.SYN_STATUS_FALSE;
						}
					} else {
						synStatus = ConstantDefine.SYN_STATUS_Buckle;
					}
					
					if (ati.get() == 100)
						ati.set(0);
					cpp.setAti(ati);
//					CommonTool.updateCPParam(cp.getCpId(), cpp);
					CommonTool.updateCPParam(newCpId, cpp);
				} else {
					String result = notityBYResult(cp,ffId,order);
					
					logger.info("给CP的同步结果："+result);
					if (result.equals("ok")) {
						synStatus = ConstantDefine.SYN_STATUS_TRUE;
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				}
			}
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(reqInfo.getFfId());
			info.setIsSuccess(reqInfo.getIsSuccess());
			info.setSynStatus(synStatus);
			
			
			commonAction.updateOrderInfo(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
	}
	/**
	 * 处理订单sd40
	 */
	private void processBYOrdersd40(OrderReqInfo reqInfo) {
		String ffId = reqInfo.getFfId();
		try {
			int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
			/*if (!isSuc) {
//				commonAction.updateOrder(ffId, isSuc, synStatus);
				return;
			} else {
				commonAction.saveExt(ffId);
			}*/
			
			OrderT ot = commonAction.queryOrderT(ffId);
			if (ot == null) {
				logger.warn("找不到对应的订单号=" + ffId);
				return;
			}
			
			OrderReqInfo order = ot.getOrder();
//			order.setMobile(reqInfo.getMobile());
			CPInfo cp = ot.getCp();
			if (order.getSynStatus() == 4) {
				order.setIsSyn(0);
				synStatus = 4;
			}
			if(order.getIsSuccess() != 0){
				logger.warn("屏蔽重复同步，订单号：" + ffId);
				return;
			}
			if (cp == null) {
				logger.warn("对应cp的通道已经关闭=" + ot.getCp());
				return;
			}
			
			String newffid =  reqInfo.getNewffid();
			order.setIsSuccess(reqInfo.getIsSuccess());
			if (order.getIsSyn() == ConstantDefine.SYN_TRUE) {
				String flag = testImsiMap.get(order.getImsi());
				
				int synRadio = cp.getSynRadio();
				if (reqInfo.getIsSuccess() != 1 || synRadio == 100) flag = "true";
				
				
				if (newffid != null && newffid.length() > 0) ffId = newffid;
				
				if (!"true".equals(flag)) {
					String newCpId = cp.getCpId()+order.getSpId();
					
					logger.info("同步率synRadio："+synRadio);
					CPParam cpp = CommonTool.getCPParam(newCpId,
							synRadio);
					
					AtomicInteger ati = cpp.getAti();
					boolean isSyn = CommonTool.isContain(cpp.getVc(),
							ati.incrementAndGet());
					if (checkOrderNum(cp)) isSyn = true;
					if (isSyn) {
						if (cp.getUrl() != null && cp.getUrl().length() > 0) {
							// 同步
							String result = notityBYResult(cp,ffId,order);
							
							logger.info("给CP的同步结果："+result);
							if (result.equals("ok")) {
								synStatus = ConstantDefine.SYN_STATUS_TRUE;
							} else {
								synStatus = ConstantDefine.SYN_STATUS_FALSE;
							}
						} else {
							synStatus = ConstantDefine.SYN_STATUS_FALSE;
						}
					} else {
						synStatus = ConstantDefine.SYN_STATUS_Buckle;
					}
					
					if (ati.get() == 100)
						ati.set(0);
					cpp.setAti(ati);
//					CommonTool.updateCPParam(cp.getCpId(), cpp);
					CommonTool.updateCPParam(newCpId, cpp);
				} else {
					String result = notityBYResult(cp,ffId,order);
					
					logger.info("给CP的同步结果："+result);
					if (result.equals("ok")) {
						synStatus = ConstantDefine.SYN_STATUS_TRUE;
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				}
			}
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(reqInfo.getFfId());
			info.setMobile(reqInfo.getMobile());
			info.setIsSuccess(reqInfo.getIsSuccess());
			info.setSynStatus(synStatus);
			info.setNewffid(newffid);
			
			info.setImsi(reqInfo.getImsi());
			info.setIccid(reqInfo.getIccid());
			info.setCtech(reqInfo.getCtech());
			
			commonAction.updateOrderInfo(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
	}
	/**
	 * 处理订单
	 */
	private void processBYOrder(OrderReqInfo reqInfo) {
		String ffId = reqInfo.getFfId();
		try {
			int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
			/*if (!isSuc) {
//				commonAction.updateOrder(ffId, isSuc, synStatus);
				return;
			} else {
				commonAction.saveExt(ffId);
			}*/
			
			OrderT ot = commonAction.queryOrderT(ffId);
			if (ot == null) {
				logger.warn("找不到对应的订单号=" + ffId);
				return;
			}
			
			
			OrderReqInfo order = ot.getOrder();
			order.setMobile(reqInfo.getMobile());
			CPInfo cp = ot.getCp();
			if(order.getIsSuccess() != 0){
				logger.warn("屏蔽重复同步，订单号=" + ffId);
				return;
			}
			if (order.getSynStatus() == 4) {
				order.setIsSyn(0);
				synStatus = 4;
			}
			
			if (cp == null) {
				logger.warn("对应cp的通道已经关闭=" + ot.getCp());
				return;
			}
			
			String newffid =  reqInfo.getNewffid();
			order.setIsSuccess(reqInfo.getIsSuccess());
			if (order.getIsSyn() == ConstantDefine.SYN_TRUE) {
				String flag = testImsiMap.get(order.getImsi());
				
				int synRadio = cp.getSynRadio();
				if (reqInfo.getIsSuccess() != 1 || synRadio == 100) flag = "true";
				
				
				if (newffid != null && newffid.length() > 0) ffId = newffid;
				
				if (!"true".equals(flag)) {
					String newCpId = cp.getCpId()+order.getSpId();
					
					logger.info("同步率synRadio："+synRadio);
					CPParam cpp = CommonTool.getCPParam(newCpId,
							synRadio);
					
					AtomicInteger ati = cpp.getAti();
					boolean isSyn = CommonTool.isContain(cpp.getVc(),
							ati.incrementAndGet());
					if (checkOrderNum(cp)) isSyn = true;
					if (isSyn) {
						if (cp.getUrl() != null && cp.getUrl().length() > 0) {
							// 同步
							String result = notityBYResult(cp,ffId,order);
							
							logger.info("给CP的同步结果："+result);
							if (result.equals("ok")) {
								synStatus = ConstantDefine.SYN_STATUS_TRUE;
							} else {
								synStatus = ConstantDefine.SYN_STATUS_FALSE;
							}
						} else {
							synStatus = ConstantDefine.SYN_STATUS_FALSE;
						}
					} else {

						synStatus = ConstantDefine.SYN_STATUS_Buckle;
//						order.setIsSuccess(4);
//						String result = notityBYResultBuckle(cp,ffId,order); 
//						logger.info("给CP的同步结果#："+result);
					}
					
					if (ati.get() == 100)
						ati.set(0);
					cpp.setAti(ati);
//					CommonTool.updateCPParam(cp.getCpId(), cpp);
					CommonTool.updateCPParam(newCpId, cpp);
				} else {
					String result = notityBYResult(cp,ffId,order);
					
					logger.info("给CP的同步结果："+result);
					if (result.equals("ok")) {
						synStatus = ConstantDefine.SYN_STATUS_TRUE;
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				}
			}
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(reqInfo.getFfId());
			info.setMobile(reqInfo.getMobile());
			info.setIsSuccess(reqInfo.getIsSuccess());
			info.setSynStatus(synStatus);
			info.setNewffid(newffid);
			commonAction.updateOrderInfo(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
	}
	
	/**
	 * 处理订单
	 */
	private void processWEBQQOrder(OrderReqInfo reqInfo) {
		String ffId = reqInfo.getFfId();
		try {
			int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
			/*if (!isSuc) {
//				commonAction.updateOrder(ffId, isSuc, synStatus);
				return;
			} else {
				commonAction.saveExt(ffId);
			}*/
			
			OrderT ot = commonAction.queryOrderT(ffId);
			if (ot == null) {
				logger.warn("找不到对应的订单号=" + ffId);
				return;
			}
			
			OrderReqInfo order = ot.getOrder();
			if (order.getIsSuccess() != 0 && order.getIsSuccess() != reqInfo.getIsSuccess()) {
				logger.warn(ffId + "，已经同步过原同步状态为：" + order.getIsSuccess() + "，现在状态：" + reqInfo.getIsSuccess());
				return;
			}
			order.setMobile(reqInfo.getMobile());
			CPInfo cp = ot.getCp();
			if (order.getSynStatus() == 4) {
				order.setIsSyn(0);
				synStatus = 4;
			}
			
			if (cp == null) {
				logger.warn("对应cp的通道已经关闭=" + ot.getCp());
				return;
			}
			
			String newffid =  reqInfo.getNewffid();
			order.setIsSuccess(reqInfo.getIsSuccess());
			if (order.getIsSyn() == ConstantDefine.SYN_TRUE) {
				String flag = testImsiMap.get(order.getImsi());
				int synRadio = cp.getSynRadio();
				if (reqInfo.getIsSuccess() != 1 || synRadio == 100) flag = "true";
				
				
				if (newffid != null && newffid.length() > 0) ffId = newffid;
				
				if (!"true".equals(flag)) {
					String newCpId = cp.getCpId()+order.getSpId();
					
					logger.info("同步率synRadio："+synRadio);
					CPParam cpp = CommonTool.getCPParam(newCpId,
							synRadio);
					
					AtomicInteger ati = cpp.getAti();
					boolean isSyn = CommonTool.isContain(cpp.getVc(),
							ati.incrementAndGet());
					if (checkOrderNum(cp)) isSyn = true;
					if (isSyn) {
						if (cp.getUrl() != null && cp.getUrl().length() > 0) {
							// 同步
							String result = notityBYResult(cp,ffId,order);
							
							logger.info("给CP的同步结果："+result);
							if (result.equals("ok")) {
								synStatus = ConstantDefine.SYN_STATUS_TRUE;
							} else {
								synStatus = ConstantDefine.SYN_STATUS_FALSE;
							}
						} else {
							synStatus = ConstantDefine.SYN_STATUS_FALSE;
						}
					} else {
						synStatus = ConstantDefine.SYN_STATUS_Buckle;
					}
					
					if (ati.get() == 100)
						ati.set(0);
					cpp.setAti(ati);
//					CommonTool.updateCPParam(cp.getCpId(), cpp);
					CommonTool.updateCPParam(newCpId, cpp);
				} else {
					String result = notityBYResult(cp,ffId,order);
					
					logger.info("给CP的同步结果："+result);
					if (result.equals("ok")) {
						synStatus = ConstantDefine.SYN_STATUS_TRUE;
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				}
			}
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(reqInfo.getFfId());
			info.setMobile(reqInfo.getMobile());
			info.setIsSuccess(reqInfo.getIsSuccess());
			info.setSynStatus(synStatus);
			info.setNewffid(newffid);
			commonAction.updateOrderInfo(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
	}
	public static void main(String[] args) {
		String url ="http://sp.hzbaina.com/sms_qynet.php";
		String param = "{\"cpParam\":\"4test4testGypelJu0rN8ABxVoO\",\"fee\":0,\"ffId\":\"04yct39932618947\",\"imei\":\"352584063215633\",\"imsi\":\"460001161954388\",\"ip\":\"115.199.100.118\",\"phone\":\"13762143362\",\"status\":\"1\"}";
		String result = null;
//		result = HttpTool.sendKLWPost(url, param, "utf-8", 5000);
		System.out.println(result);
		
		
		StringBuilder msg = new StringBuilder();
		msg.append("imsi=").append("460001161954388")
		.append("&imei=").append("352584063215633")
		.append("&ffid=").append("04yct39932618947")
		.append("&cpparam=").append("mh10")
		.append("&fee=").append(1000)
		.append("&phone=").append("13762143362")
		.append("&ip=").append("115.199.100.118")
		.append("&cpid=").append("bbkj01")
		.append("&status=1");
		param = msg.toString();
		result = HttpTool.sendGetSetTimeout(url, param, "500");
		System.out.println(result);
		
	}
	private String notityBYResultBuckle(CPInfo cp, String ffId, OrderReqInfo order) {
		String result = "";
		String param = "";
		if (cp.getBackMethod() == 1) {
			SynJson sj = new SynJson();
			sj.setFfId(ffId);
			sj.setCpParam(order.getCpParam());
			sj.setImei(order.getImei());
			sj.setImsi(order.getImsi());
			sj.setFee(order.getFee());
			sj.setIp(order.getIp());
			sj.setStatus("4");
			sj.setPhone(order.getMobile());
			sj.setCpid(cp.getCpId());
			param = JSON.toJSONString(sj);
			result = HttpTool.sendKLWPost(cp.getUrl(), param, "utf-8", 2000);
			
//			System.out.println(result);
		} else {
			StringBuilder msg = new StringBuilder();
			msg.append("imsi=").append(order.getImsi())
			.append("&imei=").append(order.getImei())
			.append("&ffId=").append(ffId)
			.append("&cpParam=").append(order.getCpParam())
			.append("&fee=").append(order.getFee())
			.append("&phone=").append(order.getMobile())
			.append("&ip=").append(order.getIp())
			.append("&cpid=").append(cp.getCpId())
			.append("&status=").append("4");
			String url = cp.getUrl();
			if (url != null && url.contains("\\?")) {
				String[] tmp = url.split("?");
				url = tmp[0];
				msg.append("&").append(tmp[1]);
			}
			param = msg.toString();
			result = HttpTool.sendGetSetTimeout(url, param, "2000");
		}
		return result;
	}
	
	
	private String notityBYResult(CPInfo cp, String ffId, OrderReqInfo order) {
		String result = "";
		String param = "";
		if (cp.getBackMethod() == 1) {
			SynJson sj = new SynJson();
			sj.setFfId(ffId);
			sj.setCpParam(order.getCpParam());
			sj.setImei(order.getImei());
			sj.setImsi(order.getImsi());
			sj.setFee(order.getFee());
			sj.setIp(order.getIp());
			sj.setStatus(order.getIsSuccess()+"");
			sj.setPhone(order.getMobile());
			sj.setCpid(cp.getCpId());
			param = JSON.toJSONString(sj);
			result = HttpTool.sendKLWPost(cp.getUrl(), param, "utf-8", 2000);
			
//			System.out.println(result);
		} else {
			StringBuilder msg = new StringBuilder();
			msg.append("imsi=").append(order.getImsi())
			.append("&imei=").append(order.getImei())
			.append("&ffId=").append(ffId)
			.append("&cpParam=").append(order.getCpParam())
			.append("&fee=").append(order.getFee())
			.append("&phone=").append(order.getMobile())
			.append("&ip=").append(order.getIp())
			.append("&cpid=").append(cp.getCpId())
			.append("&status=").append(order.getIsSuccess());
			String url = cp.getUrl();
			if (url != null && url.contains("\\?")) {
				String[] tmp = url.split("?");
				url = tmp[0];
				msg.append("&").append(tmp[1]);
			}
			param = msg.toString();
			result = HttpTool.sendGetSetTimeout(url, param, "2000");
		}
		return result;
	}
	private String notityBYResultGrade(CPInfo cp, String ffId, OrderReqInfo order) {
		String result = "";
		String param = "";
		if (cp.getBackMethod() == 1) {
			SynJson sj = new SynJson();
			sj.setFfId(ffId);
			sj.setCpParam(order.getCpParam());
			sj.setImei(order.getImei());
			sj.setImsi(order.getImsi());
			sj.setFee(order.getFee());
			sj.setIp(order.getIp());
			sj.setStatus(order.getIsSuccess()+"");
			sj.setPhone(order.getMobile());
			sj.setCpid(cp.getCpId());
			param = JSON.toJSONString(sj);
			result = HttpTool.sendKLWPost(cp.getUrl(), param, "utf-8", 2000);
			
//			System.out.println(result);
		} else {
			StringBuilder msg = new StringBuilder();
			msg.append("imsi=").append(order.getImsi())
			.append("&imei=").append(order.getImei())
			.append("&ffId=").append(ffId)
			.append("&cpParam=").append(order.getCpParam())
			.append("&fee=").append(order.getFee())
			.append("&phone=").append(order.getMobile())
			.append("&ip=").append(order.getIp())
			.append("&cpid=").append(cp.getCpId())
			.append("&status=").append(order.getIsSuccess())
			.append("&grade=").append(order.getPck());
			String url = cp.getUrl();
			if (url != null && url.contains("\\?")) {
				String[] tmp = url.split("?");
				url = tmp[0];
				msg.append("&").append(tmp[1]);
			}
			param = msg.toString();
			result = HttpTool.sendGetSetTimeout(url, param, "2000");
		}
		return result;
	}
	/**
	 * 微信账号注册回调
	 * @param cp
	 * @param ffId
	 * @param order
	 * @return
	 */
	private String notityBYResultWX(CPInfo cp, String ffId, OrderReqInfo order) {
		String result = "";
		String param = "";
		if (cp.getBackMethod() == 1) {
			SynJson sj = new SynJson();
			sj.setFfId(ffId);
			sj.setCpParam(order.getCpParam());
			sj.setImei(order.getImei());
			sj.setImsi(order.getImsi());
			sj.setFee(order.getFee());
			sj.setIp(order.getIp());
			sj.setStatus(order.getPck()+"");
			sj.setPhone(order.getMobile());
			sj.setCpid(cp.getCpId());
			param = JSON.toJSONString(sj);
			result = HttpTool.sendKLWPost(cp.getUrl(), param, "utf-8", 2000);
			
//			System.out.println(result);
		} else {
			StringBuilder msg = new StringBuilder();
			msg.append("imsi=").append(order.getImsi())
			.append("&imei=").append(order.getImei())
			.append("&ffId=").append(ffId)
			.append("&cpParam=").append(order.getCpParam())
			.append("&fee=").append(order.getFee())
			.append("&phone=").append(order.getMobile())
			.append("&ip=").append(order.getIp())
			.append("&cpid=").append(cp.getCpId())
			.append("&status=").append(order.getPck());
			String url = cp.getUrl();
			if (url != null && url.contains("\\?")) {
				String[] tmp = url.split("?");
				url = tmp[0];
				msg.append("&").append(tmp[1]);
			}
			param = msg.toString();
			result = HttpTool.sendGetSetTimeout(url, param, "2000");
		}
		return result;
	}
	
	/**
	 * 无联网回调通知
	 * @param cp
	 * @param ffId
	 * @param order
	 * @return
	 * @throws Exception 
	 */
	private String notitydfBYResult(CPInfo cp, OrderReqInfo order) 
			throws Exception {
		String result = "";
		String param = "";
		String content = order.getSmscontent();
		if (content != null && content.length() > 0) {
			content = URLEncoder.encode(content, "utf-8");
		}
		String cpparam = order.getDelayed();
		if (cpparam != null && cpparam.length() > 0) {
			cpparam = URLEncoder.encode(cpparam, "utf-8");
		}
		StringBuilder msg = new StringBuilder();
		msg.append("smsport=").append(order.getSms())
		.append("&smscontent=").append(content)
		.append("&phone=").append(order.getMobile())
		.append("&fee=").append(order.getFee())
		.append("&cpid=").append(order.getCpId())
		.append("&ffid=").append(order.getFfId())
		.append("&cpparam=").append(cpparam)
		.append("&status=").append(order.getIsSuccess());
		param = msg.toString();
		result = HttpTool.sendGetSetTimeout(cp.getUrl(), param, "500");
		return result;
	}
	
	/**
	 * 无联网回调通知
	 * @param cp
	 * @param ffId
	 * @param order
	 * @return
	 * @throws Exception 
	 */
	private String notityBYResult(CPInfo cp, HttpRequest request) throws Exception {
		String admount = request.getParam("admount");//金额 单位：分
		String productid = request.getParam("productid");//产品id
		String mobile = request.getParam("phone");//手机号
		String isStatus = request.getParam("status");//状态  0：订购  3：退订
		String addtime = request.getParam("ordertime");//订购/退订时间
		String spnum = request.getParam("spnum");//长号码
		String spcmd = request.getParam("spcmd");//指令
		if (addtime == null) addtime = "";
		
		String result = "";
		String param = "";
		StringBuilder msg = new StringBuilder();
		msg.append("admount=").append(admount)
		.append("&productid=").append(productid)
		.append("&phone=").append(mobile)
		.append("&status=").append(isStatus)
		.append("&ordertime=").append(URLEncoder.encode(addtime,"utf-8"))
		.append("&spnum=").append(spnum)
		.append("&spcmd=").append(spcmd);
		param = msg.toString();
		result = HttpTool.sendGetSetTimeout(cp.getUrl(), param, "500");
		return result;
	}
	
	/**
	 * 无联网回调通知
	 * @param cp
	 * @param ffId
	 * @param order
	 * @return
	 * @throws Exception 
	 */
	private String notityBYResultJHDL(CPInfo cp, HttpRequest request) throws Exception {
		String admount = request.getParam("admount");//金额 单位：分
		String productid = request.getParam("productid");//产品id
		String mobile = request.getParam("mobile");//用户手机号
		String isStatus = request.getParam("status");//状态  0：订购  3：退订
		String addtime = request.getParam("timestamp");//unix时间戳
		String spnum = request.getParam("spnum");//长号码
		String spcmd = request.getParam("sms_order");//上行指令，由我方分配
		
		String product_type = request.getParam("product_type");//产品类型1包月 2点播
		String state = request.getParam("state");//计费状态 0成功1失败
		String op_type = request.getParam("op_type");//操作类型 0 订购 1退订
		if ("1".equals(product_type)) {
			if ("0".equals(op_type)) {
				isStatus = "0";
			} else if ("1".equals(op_type)) {
				isStatus = "3";
			} else {
				isStatus = "2";
			}
		} else {
			if ("0".equals(state)) {
				isStatus = "0";
			} else {
				isStatus = "2";
			}
		}
		
		if (addtime == null) addtime = "";
		
		String result = "";
		String param = "";
		StringBuilder msg = new StringBuilder();
		msg.append("admount=").append(admount)
		.append("&productid=").append(productid)
		.append("&phone=").append(mobile)
		.append("&status=").append(isStatus)
		.append("&ordertime=").append(URLEncoder.encode(addtime,"utf-8"))
		.append("&spnum=").append(spnum)
		.append("&spcmd=").append(spcmd);
		param = msg.toString();
		result = HttpTool.sendGetSetTimeout(cp.getUrl(), param, "500");
		return result;
	}
	
	private String notityOtherResult(CPInfo cp, String ffId, OrderReqInfo order) {
		String result = "";
		try {
			String param = "";
			String pmodel = order.getPmodel();
			String cpid = cp.getCpId();
			String imsi = order.getImsi();
			if (imsi != null && imsi.length() > 0) {
				imsi = URLEncoder.encode(imsi, "utf-8");
			}
			if (cpid == null || cpid.length() <= 2) {
				cpid = "mrjm";
			} else {
				cpid = cpid.substring(0,cpid.length()-2);
			}
			String key = ConstantDefine.MD5_KEY1 + cpid + ConstantDefine.MD5_KEY2;
			StringBuilder msg = new StringBuilder();
			msg.append("cpid=").append(cp.getCpId())
			.append("&cpparam=").append(order.getCpParam())
			.append("&fee=").append(order.getFee())
			.append("&ffid=").append(ffId);
			String sign = MD5.md5(msg.toString()+"&key="+key, "utf-8");
			if (cp.getBackMethod() == 1) {
				SynOtherJson sj = new SynOtherJson();
				sj.setFfid(ffId);
				sj.setCpparam(order.getCpParam());
				sj.setFee(order.getFee());
				sj.setIp(order.getIp());
				sj.setStatus(order.getIsSuccess()+"");
				sj.setCpid(cp.getCpId());
				sj.setSign(sign);
				if (pmodel != null && pmodel.length() > 0) {
					sj.setTransid(order.getPmodel());
				}
				param = JSON.toJSONString(sj);
				if (cp.getUrl().startsWith("https")) {
					result = HttpsTool.sendPost(cp.getUrl(), param, 3000);
				} else {
					result = HttpTool.sendKLWPost(cp.getUrl(), param, "utf-8", 5000);
				}
	//			System.out.println(result);
			} else {
				msg.append("&status=").append(order.getIsSuccess())
				.append("&ip=").append(order.getIp())
				.append("&sign=").append(sign);
				if (pmodel != null && pmodel.length() > 0) {
					msg.append("&transid=").append(pmodel);
				}
				
				param = msg.toString();
				/*if ("dskj01".equals(cp.getCpId())) {
					result = HttpTool.sendFormGet(cp.getUrl(), param, 3000);
				} else {
				}*/
				if (cp.getUrl().startsWith("https")) {
					result = HttpsTool.sendGet(cp.getUrl()+"?"+param, 5000);
				} else {
					result = HttpTool.sendGetSetTimeout(cp.getUrl(), param, "5000");
				}
			}
			order.setCtech(cp.getUrl()+"?"+param);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		return result;
	}
	
	private String notityResult(CPInfo cp, String ffId, OrderReqInfo order) {
		String result = "";
		String param = "";
		String cpId = cp.getCpId();
		if (cp.getBackMethod() == 1) {
			SynJson sj = new SynJson();
			sj.setFfId(ffId);
			sj.setCpParam(order.getCpParam());
			sj.setImei(order.getImei());
			sj.setImsi(order.getImsi());
			sj.setFee(order.getFee());
			sj.setIp(order.getIp());
			sj.setPhone(order.getMobile());
			sj.setStatus(order.getIsSuccess()+"");
			sj.setCpid(cp.getCpId());
			param = JSON.toJSONString(sj);
			if ("sz01".equals(cpId) || "ylt01".equals(cpId) || "zh01".equals(cpId)
					|| "zxtw01".equals(cpId) || "xs02".equals(cpId) || "mr01".equals(cpId)) {
				result = HttpTool.sendPost(cp.getUrl(), param, "2000");
			} else {
				result = HttpTool.sendKLWPost(cp.getUrl(), param, "utf-8", 500);
			}
//			System.out.println(result);
		} else {
			StringBuilder msg = new StringBuilder();
			if (cpId.contains("kxy") || cpId.contains("shyy") 
					|| cpId.contains("mx02") || cpId.contains("hwd")) {
				msg.append("imsi=").append(order.getImsi())
				.append("&cpid=").append(cp.getCpId())
				.append("&imei=").append(order.getImei())
				.append("&ffId=").append(ffId)
				.append("&cpParam=").append(order.getCpParam())
				.append("&fee=").append(order.getFee())
				.append("&status=").append(order.getIsSuccess())
				.append("&phone=").append(order.getMobile())
				.append("&ip=").append(order.getIp());
			} else {
				msg.append("imsi=").append(order.getImsi())
				.append("&cpid=").append(cp.getCpId())
				.append("&imei=").append(order.getImei())
				.append("&ffid=").append(ffId)
				.append("&cpparam=").append(order.getCpParam())
				.append("&fee=").append(order.getFee())
				.append("&status=").append(order.getIsSuccess())
				.append("&phone=").append(order.getMobile())
				.append("&ip=").append(order.getIp());
			}
			param = msg.toString();
			result = HttpTool.sendGetSetTimeout(cp.getUrl(), param, "500");
		}
		return result;
	}
	
	private String notityZCResult(CPInfo cp,String phone,String ffId,String status) {
		String result = "";
		String param = "";
		String cpId = cp.getCpId();
		StringBuilder msg = new StringBuilder();
		msg.append("phone=").append(phone)
		.append("&linked=").append(ffId)
		.append("&cpid=").append(cpId)
		.append("&status=").append(status);
		param = msg.toString();
		result = HttpTool.sendGetSetTimeout(cp.getUrl(), param, "500");
		return result;
	}
	
	private String notityResultKL(CPInfo cp, String ffId, OrderReqInfo order) {
		String result = "";
		String param = "";
		if (cp.getBackMethod() == 1) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("ffId", ffId);
			map.put("cpParam", order.getCpParam());
			map.put("fee", order.getFee()+"");
			map.put("mobile", order.getMobile());
			param = JSON.toJSONString(map);
			result = HttpTool.sendKLWPost(cp.getUrl(), param, "utf-8", 500);
		} else {
			StringBuilder msg = new StringBuilder();
			msg.append("ffId=").append(ffId)
			.append("&cpParam=").append(order.getCpParam())
			.append("&fee=").append(order.getFee())
			.append("&mobile=").append(order.getMobile());
			param = msg.toString();
			result = HttpTool.sendGetSetTimeout(cp.getUrl(), param, "500");
		}
		return result;
	}
	
	/*public static void main(String[] args) {
		String url = "http://122.114.57.133:8001/tykj/xkmm.aspx";
		String param = "{\"cpParam\":\"16021442\",\"fee\":1000,\"ffId\":\"05dxb59627215428\",\"imei\":\"99000535611401\",\"imsi\":\"460030813377603\",\"ip\":\"110.156.211.109\"}";
		String result = HttpTool.sendKLWPost(url, param, "utf-8", 500);
		System.out.println(result);
	}*/
	/*public static void main(String[] args) {
		String url = "http://120.24.88.90/fee/feeLTDXBY";
		String param = "phone=18802135388&state=3&extra=01ecl25590138841";
		String result = HttpTool.sendKLWPost(url, param, "utf-8", 1500);
		System.out.println(result);
		List<OrderReqInfo> list = new ArrayList<OrderReqInfo>();
		OrderReqInfo info = new OrderReqInfo();
		info.setOrderId("1111");
		info.setApp("a1");
		OrderReqInfo info1 = new OrderReqInfo();
		info1.setOrderId("2222");
		info1.setApp("a2");
		list.add(info);
		list.add(info1);
		System.out.println(JSONArray.toJSONString(list));
	}*/
	
	/***
	 * 订单回调
	 * http://smspay.xushihudong.com/fee/feeLTDXBY
	 */
	public void feeLTDXBY(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeLTDXBY请求header信息：" + request.allHeaders());
			String content = request.body();
			logger.info("朗天电信下行短信："+content);
			Map<String, String> myObj = parseYZ(content);
			
			String mobile = myObj.get("mobile");
			String state = myObj.get("status");//0-订购 3-退订
//			String ddate = myObj.get("ddate");//订购时间
//			String tddate = myObj.get("tddate");//退订时间
//			String command = myObj.get("command");//订购指令
			String extra = myObj.get("extra");
//			String fee = myObj.get("fee");
//			String type = myObj.get("type");//业务类型
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(extra);
			if ("00".equals(state)) {
				info.setIsSuccess(1);
			} else if ("3".equals(state)) {
				info.setIsSuccess(3);
			} else {
				info.setIsSuccess(9);
			}
			info.setMobile(mobile);
			processxsBYOrder(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("success").end();
		
	}
	
	/***
	 * 订单回调 动漫视频包月
	 * http://120.24.88.90/fee/feeBYdmback
	 */
	public void feeBYdmback(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeBYdmback请求header信息：" + request.allHeaders());
			
			String ffId = request.getParam("cp_param");
			String isSuccess = request.getParam("result");
			String mobile = request.getParam("mobile");
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if ("succ".equals(isSuccess)) {
				info.setIsSuccess(1);//订购
			} else if ("3".equals(isSuccess)) {
				info.setIsSuccess(3);
			} else {
				info.setIsSuccess(9);
			}
			info.setMobile(mobile);
			processBYOrder(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/***
	 * 订单回调 摩宝视频包月
	 * http://120.24.88.90/fee/feertbymbback
	 */
	public void feertbymbback(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feertbymbback请求header信息：" + request.allHeaders());
			
			String ffId = request.getParam("PHGH1");
			String isSuccess = request.getParam("status");
			
			boolean issuc = false;
			if ("DELIVRD".equals(isSuccess)) {
				issuc = true;//订购
			}
			processOrder(ffId, issuc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("{\"status\": 200} ").end();
		
	}
	
	/***
	 * 订单回调
	 * http://120.24.88.90/fee/feeSZLTsfyj
	 */
	public void feeSZLTsfyj(HttpRequest request, HttpResponse response)
			throws Exception {
		String MchNo = request.getParam("MchNo");
		try {
			logger.info("feeSZLTsfyj请求header信息：" + request.allHeaders());
			
			String ffId = request.getParam("CPParam");
			String isSuccess = request.getParam("RetCode");
			String mobile = request.getParam("Mobile");
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if ("000000".equals(isSuccess)) {
				info.setIsSuccess(1);
			} else {
				info.setIsSuccess(CommonTool.convertInt(isSuccess));
			}
			info.setMobile(mobile);
			processBYOrder(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("RetCode", "000000");
		map.put("RetMsg", "充值成功");
		map.put("MchNo", MchNo);
		response.content(JSONObject.toJSONString(map)).end();
		
	}
	
	/***
	 * 订单回调
	 * http://118.190.90.1/fee/feewxzcsendsms
	 */
	public void feewxzcsendsms(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feewxzcsendsms请求header信息：" + request.allHeaders());
			
			String ffId = request.getParam("ext");
			String sms = request.getParam("msg");
			String smsport = request.getParam("port");
			
			OrderT ot = commonAction.queryOrderT(ffId);
			if (ot == null) {
				logger.warn("找不到对应的订单号=" + ffId);
				response.content("error").end();
				return;
			}
			
			OrderReqInfo info = ot.getOrder();
			CPInfo cp = ot.getCp();
			
			String url = cp.getSynProvince();
			StringBuilder param = new StringBuilder();
			param.append("sms=").append(sms)
			.append("&smsport=").append(smsport)
			.append("&cpparam=").append(info.getCpParam())
			.append("&orderid=").append(ffId);
			String result = HttpTool.sendGetSetTimeout(url, param.toString(), "200");
			logger.info("feewxzcsendsms发送结果：" + result);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/***微信注册发送短信接口
	 * 订单回调
	 * http://register.qygame.cn/fee/feewxzcsendsmsqy
	 */
	public void feewxzcsendsmsqy(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feewxzcsendsmsqy请求header信息：" + request.allHeaders());
			
			String ffId = request.getParam("cpparam");
			String sms = request.getParam("sms");
			String smsport = request.getParam("smsport");
			
			OrderT ot = commonAction.queryOrderT(ffId);
			if (ot == null) {
				logger.warn("找不到对应的订单号=" + ffId);
				response.content("error").end();
				return;
			}
			
			OrderReqInfo info = ot.getOrder();
			CPInfo cp = ot.getCp();
			
			String url = cp.getSynProvince();
			StringBuilder param = new StringBuilder();
			param.append("sms=").append(sms)
			.append("&smsport=").append(smsport)
			.append("&cpparam=").append(info.getCpParam())
			.append("&orderid=").append(ffId);
			String result = HttpTool.sendGetSetTimeout(url, param.toString(), "200");
			logger.info("feewxzcsendsms发送结果：" + result);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/***
	 * 订单回调
	 * http://118.190.90.1/fee/feeszrtjrmg
	 */
	public void feeszrtjrmg(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeszrtjrmg请求header信息：" + request.allHeaders());
			String body = request.body();
			logger.info("feeszrtjrmg请求body信息：" + body);
			Map<String, String> mapobj = CommonTool.parseYZ(body);
			String ffId = mapobj.get("customId");
//			String isSuccess = mapobj.get("status");
			String mobile = mapobj.get("phone");
			String errorCode = mapobj.get("errorCode");
			
			if (ffId != null && ffId.length() >= 16) {
				ffId = ffId.substring(ffId.length()-16);
			}
			
			if (errorCode == null || errorCode.length() <= 0) errorCode = "9";
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if ("0".equals(errorCode)) {//成功
				info.setIsSuccess(1);
			} else if("1".equals(errorCode)) {//手机号码重复提交
				info.setIsSuccess(8);
			} else if("2".equals(errorCode)) {//验证码超时
				info.setIsSuccess(3);
			} else if("3".equals(errorCode)) {//手机号为空
				info.setIsSuccess(10);
			} else if("4".equals(errorCode)) {//其他错误
				info.setIsSuccess(5);
			} else if("5".equals(errorCode)) {//用户已存在
				info.setIsSuccess(11);
			} else {
				info.setIsSuccess(CommonTool.convertInt(errorCode));
			}
			info.setMobile(mobile);
			processBYOrder(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("{\"code\":0,\"message\":\"ok\"}").end();
		
	}
	
	/***
	 * 订单回调
	 * http://118.190.90.1/fee/feeSZLT10086
	 */
	public void feeSZLT10086(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeSZLT10086请求header信息：" + request.allHeaders());
			
			String ffId = request.getParam("customId");
			String isSuccess = request.getParam("errorCode");
			String mobile = request.getParam("phone");
			
			if (ffId != null && ffId.length() >= 16) {
				ffId = ffId.substring(ffId.length()-16);
			}
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if ("0".equals(isSuccess)) {
				info.setIsSuccess(1);
			} else if ("1".equals(isSuccess)) {
				info.setIsSuccess(7);
			} else {
				info.setIsSuccess(CommonTool.convertInt(isSuccess));
			}
			info.setMobile(mobile);
			processBYOrder(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("{\"code\":0,\"message\":\"ok\"}").end();
		
	}
	
	/***
	 * 订单回调
	 * http://118.190.90.1/fee/feeSZLTzc
	 */
	public void feeSZLTzc(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeSZLTzc请求header信息：" + request.allHeaders());
			
			String ffId = request.getParam("cpparam");
			String isSuccess = request.getParam("status");
			String mobile = request.getParam("phone");
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if ("2".equals(isSuccess)) {
				info.setIsSuccess(1);
			} else {
				info.setIsSuccess(CommonTool.convertInt(isSuccess));
			}
			info.setMobile(mobile);
			processBYOrder(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/***
	 * 订单回调
	 * http://120.24.88.90/fee/feeSZLTzclymWtc
	 */
	public void feeSZLTzclymWtc(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeSZLTzclymWtc请求header信息：" + request.allHeaders());
			
			String isSuccess = request.getParam("status");
			String mobile = request.getParam("phone");
			String ffId = DateTool.getMonth() + "zcl" + mobile;
			
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if ("2".equals(isSuccess)) {//更新ffidl,
				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
				info.setNewffid(newffid);
				info.setIsSuccess(5);
			} else {
				info.setIsSuccess(CommonTool.convertInt(isSuccess));
			}
			info.setMobile(mobile);
			processBYOrder(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/***
	 * 维新伟联订单回调 12306注册无透传
	 */
	public void feeSZLTzclWtcwxwl(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeSZLTzclWtcwxwl请求header信息：" + request.allHeaders());
			
			response.content("ok").end();
			
			String isSuccess = request.getParam("state");
			String mobile = request.getParam("mobile");
			String code = request.getParam("code");
			String ffId = DateTool.getMonth() + mobile + code;
			
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if ("3".equals(isSuccess)) {
				info.setIsSuccess(1);
			} else if ("4".equals(isSuccess)) {
				info.setIsSuccess(3);
			} else if ("1".equals(isSuccess) || "2".equals(isSuccess)) {
//				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
//				info.setNewffid(newffid);
				info.setIsSuccess(5);
			} else {
				info.setIsSuccess(CommonTool.convertInt(isSuccess));
			}
			info.setMobile(mobile);
			info.setCode(code);
//			processRTBYOrder(info);
			processNRTBYOrder(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
	}
	
	/***
	 * jd注册无透传 sd29
	 */
	public void feeSZLTzcjd(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeSZLTzcjd请求header信息：" + request.allHeaders());
			
			response.content("ok").end();
			
			String isSuccess = request.getParam("status");
			String mobile = request.getParam("mobile");
			String phone = request.getParam("phone");
			String code = request.getParam("code");
			if (mobile == null || mobile.length() <= 0) {
				mobile = phone;
			}
			String ffId = DateTool.getMonth() + "sd33" + mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if ("2".equals(isSuccess)) {
				info.setIsSuccess(1);
			} else if ("1".equals(isSuccess)) {
				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
				info.setNewffid(newffid);
				info.setIsSuccess(2);
			} else {
				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
				info.setNewffid(newffid);
				info.setIsSuccess(CommonTool.convertInt(isSuccess));
			}
			info.setMobile(mobile);
			info.setCode(code);
//			processRTBYOrder(info);
			processNRTBYOrder(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
	}
	/***
	 * ks注册无透传 sd34
	 */
	public void feeSZLTzckssd34(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeSZLTzckssd34请求header信息：" + request.allHeaders());
			
			response.content("ok").end();
			
			String isSuccess = request.getParam("status");
			String mobile = request.getParam("mobile");
			String phone = request.getParam("phone");
			String code = request.getParam("code");
			if (mobile == null || mobile.length() <= 0) {
				mobile = phone;
			}
			String ffId = DateTool.getMonth() + "sd34" + mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if ("1".equals(isSuccess)) {
				info.setIsSuccess(1);
			} else if ("2".equals(isSuccess)) {
				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
				info.setNewffid(newffid);
				info.setIsSuccess(2);
			} else {
				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
				info.setNewffid(newffid);
				info.setIsSuccess(CommonTool.convertInt(isSuccess));
			}
			info.setMobile(mobile);
			info.setCode(code);
//			processRTBYOrder(info);
			processNRTBYOrder(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
	}
	/**
	 * 微博解封mtk
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void feeSZLTzckssd35(HttpRequest request, HttpResponse response)
			throws Exception {
		String state="error";
		try {
			logger.info("feeSZLTzckssd35请求header信息：" + request.allHeaders());
			
			
			String pck = request.getParam("svcnum");//长号码30060
			String ctech = request.getParam("linkid");//上下行关联标识6cea7063f54247a8885a8fe954a5a265
			String app = request.getParam("msg");//上行短信内容1174
			
			String mobile = request.getParam("mobile");
			String phone = request.getParam("phone");
			
			if (mobile == null || mobile.length() <= 0) {
				mobile = phone;
			}
			String ffId = DateTool.getMonth() + "sd35" + mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			info.setIsSuccess(1);
			info.setMobile(mobile);
			info.setCtech(ctech);
			info.setApp(app);
			info.setPck(pck);
//			info.setCode(code);
//			processRTBYOrder(info);
			processNRTBYOrder(info);
			state="ok";
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(state).end();
	}
	/**
	 * 微博解封android
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void feeSZLTzckssd37(HttpRequest request, HttpResponse response)
			throws Exception {
		String state="error";
		try {
			logger.info("feeSZLTzckssd37请求header信息：" + request.allHeaders());
			
			
			String pck = request.getParam("svcnum");//长号码30060
			String ctech = request.getParam("linkid");//上下行关联标识6cea7063f54247a8885a8fe954a5a265
			String app = request.getParam("msg");//上行短信内容1174
			
			String mobile = request.getParam("mobile");
			String phone = request.getParam("phone");
			
			if (mobile == null || mobile.length() <= 0) {
				mobile = phone;
			}
			String ffId = DateTool.getMonth() + "sd37" + mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			info.setIsSuccess(1);
			info.setMobile(mobile);
			info.setCtech(ctech);
			info.setApp(app);
			info.setPck(pck);
//			info.setCode(code);
//			processRTBYOrder(info);
			processNRTBYOrder(info);
			state="ok";
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(state).end();
	}
	/***
	 * MG账号注册sd38
	 */
	
	public void feeSZLTzckssd38(HttpRequest request, HttpResponse response)
			throws Exception {
		
		
		try {
			logger.info("feeSZLTzckssd38请求header信息：" + request.allHeaders());
			
			String body = request.body();
			logger.info("******"+body);
			
			logger.info("feeSZLTzckssd38请求body信息：" + body);
			
			Map<String, String> mapobj = CommonTool.parseYZ(body);
			if(mapobj.isEmpty()){
				mapobj.put("code", "1");
				mapobj.put("message", "error");
				String sms = JSONObject.toJSONString(mapobj);
				response.content(sms).end();
			}else{
				mapobj.put("code", "0");
				mapobj.put("message", "success");
				String sms = JSONObject.toJSONString(mapobj);
				response.content(sms).end();
			}
			
			String orderId = mapobj.get("orderId");
			String phone = mapobj.get("phone");
			String isSuccess = mapobj.get("status");
			String regist_time = mapobj.get("regist_time");
			String ffId = mapobj.get("customId");
			
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if ("Y".equals(isSuccess)) {
				info.setIsSuccess(1);
			} else if ("N".equals(isSuccess)) {
				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
				info.setNewffid(newffid);
				info.setIsSuccess(2);
			} 
			info.setMobile(phone);
			info.setPmodel(orderId);
			info.setApp(regist_time);
			
//			info.setCode(code);
//			processRTBYOrder(info);
			Map<String, String> resMap = new HashMap<String, String>();
			processMGNRTBYOrder(info);
			
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
			
		}
//		
	}
	/***
	 * MG账号注册sd54
	 */
	
	public void feeSZLTzckssd55(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeSZLTzcjd请求header信息：" + request.allHeaders());
			
			response.content("ok").end();
			
			String isSuccess = request.getParam("status");
			String mobile = request.getParam("mobile");
			String phone = request.getParam("phone");
			String code = request.getParam("code");
			if (mobile == null || mobile.length() <= 0) {
				mobile = phone;
			}
			String ffId = DateTool.getMonth() + "sd55" + mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if ("1".equals(isSuccess)) {
				info.setIsSuccess(1);
			} else if ("2".equals(isSuccess)) {
				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
				info.setNewffid(newffid);
				info.setIsSuccess(2);
			} else {
				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
				info.setNewffid(newffid);
				info.setIsSuccess(CommonTool.convertInt(isSuccess));
			}
			info.setMobile(mobile);
			info.setCode(code);
//			processRTBYOrder(info);
			processNRTBYOrder(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
//		
	}
	/***
	 * 微信账号注册sd39同步验证码
	 */
	
	public void feeSZLTzckssd39(HttpRequest request, HttpResponse response)
			throws Exception {

		String smms = "error";
		
		try {
			logger.info("feeSZLTzckssd39请求header信息：" + request.allHeaders());
		

			String ffId = request.getParam("customId");
			String orderId = request.getParam("orderId");
			String phone = request.getParam("phone");
			String isSuccess = request.getParam("status");
			String errorCode = request.getParam("errorCode");
			Map<String,Object> map = new HashMap<String,Object>();
			int code = 1;
			String sms = "error";
			
			if((ffId != null && ffId.length() > 0) && (isSuccess != null && isSuccess.length() > 0)){
				code = 0;
				sms = "success";
			}
			map.put("code", code);
			map.put("message", sms);
			smms = JSONObject.toJSONString(map);
			
			
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if ("Y".equals(isSuccess)) {
				info.setIsSuccess(1);
			} else if ("N".equals(isSuccess)) {
				info.setIsSuccess(2);
			} 
			info.setPck(errorCode);
			info.setMobile(phone);
//			info.setPmodel(orderId);
//			info.setOsversion(regist_time);
			
//			info.setCode(code);
//			processRTBYOrder(info);
			Map<String, String> resMap = new HashMap<String, String>();
			processWXNRTBYOrder(info);
		
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
			
		}
		response.content(smms).end();
	}
	/***
	 * 订单回调 12306注册张项目一sd47
	 * http://120.24.88.90/fee/feeSZLTzclWtcsd40
	 */
	public void feeSZLTzclWtcsd47(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeSZLTzclWtcsd47请求header信息：" + request.allHeaders());
			
			String phone = request.getParam("phone");
			String mobile = request.getParam("mobile");
//			String code = request.getParam("code");
			String status = request.getParam("status");
//			String ext = request.getParam("ext");
			
			if (mobile == null && mobile.length() <= 0) {
				mobile=phone;
			}
			if(mobile!=null&&mobile.length()>0){
				response.content("ok").end();
			}
			
			
			String ffId = DateTool.getMonthDay() + mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if("DELIVRD".equals(status)){
				info.setIsSuccess(1);
			}else{
				info.setIsSuccess(2);
			}
//			processRTBYOrder(info);
			processBYOrdersd40(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
	}
	/***
	 * 订单回调 12306注册张项目一sd44
	 * http://120.24.88.90/fee/feeSZLTzclWtcsd40
	 */
	public void feeSZLTzclWtcsd44(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeSZLTzclWtcsd44请求header信息：" + request.allHeaders());
			
			String mobile = request.getParam("phone");
			String code = request.getParam("code");
			String status = request.getParam("status");
			String ext = request.getParam("ext");
			
			
			if(mobile!=null&&mobile.length()>0){
				response.content("ok").end();
			}
			
			
			String ffId = DateTool.getMonthDay() + mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if("1".equals(status)){
				info.setIsSuccess(1);
			}else{
				info.setIsSuccess(2);
			}
//			processRTBYOrder(info);
			processBYOrdersd40(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
	}
	/***
	 * 订单回调 12306注册张项目一sd43
	 * http://120.24.88.90/fee/feeSZLTzclWtcsd40
	 */
	public void feeSZLTzclWtcsd43(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeSZLTzclWtcsd43请求header信息：" + request.allHeaders());
			
			String mobile = request.getParam("phone");
			String code = request.getParam("code");
			String time = request.getParam("time");
			String zt = request.getParam("zt");
			String mi = request.getParam("mi");
			
			String is_success = URLDecoder.decode(zt,"UTF-8");
			
			if(mobile!=null&&mobile.length()>0){
				response.content("ok").end();
			}
			
			
			String ffId = DateTool.getMonthDay() + mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if("成功".equals(is_success)){
				info.setIsSuccess(1);
			}
//			processRTBYOrder(info);
			processBYOrdersd40(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
	}
	/***
	 * 订单回调 12306注册sd40
	 * http://120.24.88.90/fee/feeSZLTzclWtcsd40
	 */
	public void feeSZLTzclWtcsd40(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeSZLTzclWtcsd40请求header信息：" + request.allHeaders());
			
			String mobile = request.getParam("phone");
			String linkid = request.getParam("linkid");
			String msg = request.getParam("msg");
			String svcnum = request.getParam("svcnum");
			
			if(mobile!=null&&mobile.length()>0){
				response.content("ok").end();
			}
			
			
			String ffId = DateTool.getMonthDay() + mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			info.setIsSuccess(1);
				
			info.setMobile(mobile);
			info.setImsi(linkid);
			info.setIccid(svcnum);
			info.setCtech(msg);
			
//			processRTBYOrder(info);
			processBYOrdersd40(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
	}
	/**
	 * 12306
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	public int feeSZLTzckssd36(HttpRequest request, HttpResponse response)
			throws Exception {
		int Code = 0;
		try {
			logger.info("feeSZLTzckssd36请求header信息：" + request.allHeaders());
			
			response.content("ok").end();
			
			//String type = request.getParam("type");
			
			String state = request.getParam("status");
			String mobile = request.getParam("mobile");
			String phone = request.getParam("phone");
			String code = request.getParam("code");
			
			if (mobile == null || mobile.length() <= 0) {
				mobile = phone;
			}
			//String ffId = DateTool.getMonth() + "sd36" + mobile;
			String ffId = DateTool.getMonth() + mobile + code;
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if ("1".equals(state)) {//注册12306账号成功
				info.setIsSuccess(1);
			} else if ("2".equals(state)) {//手机号已被使用
				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
				info.setNewffid(newffid);
				info.setIsSuccess(2);
			}else if (state.equals("3")) {//验证码错误或者超时
				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
				info.setNewffid(newffid);
				info.setIsSuccess(3);
			}else{
				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
				info.setNewffid(newffid);
				info.setIsSuccess(CommonTool.convertInt(state));
			}
			
			info.setMobile(mobile);
//			info.setCode(code);
//			processRTBYOrder(info);
			processNRTBYOrder(info);
			return Code =101;
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		return Code;
	}
	/***
	 * http://118.190.90.1/fee/feeSZLTsd30
	 * 微博注册无透传 sd30
	 */
	public void feeSZLTsd30(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeSZLTsd30请求header信息：" + request.allHeaders());
			
			response.content("ok").end();
			
			String isSuccess = request.getParam("status");
			String mobile = request.getParam("mobile");
			String phone = request.getParam("phone");
			String code = request.getParam("code");
			if (mobile == null || mobile.length() <= 0) {
				mobile = phone;
			}
			String ffId = DateTool.getMonth() + "sd30" + mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if ("2".equals(isSuccess)) {
				info.setIsSuccess(1);
			} else if ("1".equals(isSuccess)) {
				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
				info.setNewffid(newffid);
				info.setIsSuccess(2);
			} else {
				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
				info.setNewffid(newffid);
				info.setIsSuccess(CommonTool.convertInt(isSuccess));
			}
			info.setMobile(mobile);
			info.setCode(code);
//			processRTBYOrder(info);
			processBYOrder(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
	}
	/***
	 * http://118.190.90.1/fee/feeSZLTzcjdsd31
	 * wb注册无透传 sd31
	 */
	public void feeSZLTzcwbsd49(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeSZLTzcwbsd49请求header信息：" + request.allHeaders());
			
			response.content("ok").end();
			
			String isSuccess = request.getParam("status");
			String mobile = request.getParam("mobile");
			String phone = request.getParam("phone");
			String code = request.getParam("code");
			if (mobile == null || mobile.length() <= 0) {
				mobile = phone;
			}
			String ffId = DateTool.getMonth() + "sd49" + mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if ("1".equals(isSuccess)) {
				info.setIsSuccess(1);
			} else if ("2".equals(isSuccess)) {
				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
				info.setNewffid(newffid);
				info.setIsSuccess(2);
			} else {
				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
				info.setNewffid(newffid);
				info.setIsSuccess(CommonTool.convertInt(isSuccess));
			}
			info.setMobile(mobile);
			info.setCode(code);
//			processRTBYOrder(info);
			processBYOrder(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
	}
	/***
	 * http://118.190.90.1/fee/feeSZLTzcjdsd31
	 * 瞿jd注册sd50
	 */
	public void feeSZLTzcwbsd50(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeSZLTzcwbsd50请求header信息：" + request.allHeaders());
			
			response.content("ok").end();
			
			String isSuccess = request.getParam("status");
			String mobile = request.getParam("mobile");
			String phone = request.getParam("phone");
			String code = request.getParam("code");
			if (mobile == null || mobile.length() <= 0) {
				mobile = phone;
			}
			String ffId = DateTool.getMonth() + "_" + DateTool.getffIdDay()+DateTool.getMonthDay() + "sd50" + mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if ("2".equals(isSuccess)) {
				info.setIsSuccess(1);
			} else if ("1".equals(isSuccess)) {
				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
				info.setNewffid(newffid);
				info.setIsSuccess(2);
			} else {
				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
				info.setNewffid(newffid);
				info.setIsSuccess(CommonTool.convertInt(isSuccess));
			}
			info.setMobile(mobile);
			info.setCode(code);
//			processRTBYOrder(info);
			processBYOrdersd46Date(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
	}
	/***
	 * http://118.190.90.1/fee/feeSZLTzcjdsd31
	 * wb注册无透传 sd48
	 */
	public void feeSZLTzcwbsd48(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeSZLTzcwbsd48请求header信息：" + request.allHeaders());
			
			response.content("ok").end();
			
			String isSuccess = request.getParam("status");
			String mobile = request.getParam("mobile");
			String phone = request.getParam("phone");
			String code = request.getParam("code");
			if (mobile == null || mobile.length() <= 0) {
				mobile = phone;
			}
			String ffId = DateTool.getMonth() + "sd48" + mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if ("2".equals(isSuccess)) {
				info.setIsSuccess(1);
			} else if ("1".equals(isSuccess)) {
				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
				info.setNewffid(newffid);
				info.setIsSuccess(2);
			} else {
				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
				info.setNewffid(newffid);
				info.setIsSuccess(CommonTool.convertInt(isSuccess));
			}
			info.setMobile(mobile);
			info.setCode(code);
//			processRTBYOrder(info);
			processBYOrder(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
	}
	/***
	 * http://118.190.90.1/fee/feeSZLTzcjdsd31
	 * jd注册无透传 sd31
	 */
	public void feeSZLTzcjdsd31(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeSZLTzcjdsd31请求header信息：" + request.allHeaders());
			
			response.content("ok").end();
			String body = request.body();
			logger.info("feeSZLTzcjdsd31请求body信息：" + body);
			Map<String, String> reqmap = CommonTool.parseYZ(body);
			String isSuccess = reqmap.get("status");
			String mobile = reqmap.get("mobile");
			String phone = reqmap.get("phone");
			String code = reqmap.get("code");
			if (mobile == null || mobile.length() <= 0) {
				mobile = phone;
			}
//			String ffId = DateTool.getMonthDay() + "sd31" + mobile;
			String ffId = DateTool.getMonth() + "_" + DateTool.getffIdDay()+DateTool.getMonthDay() + "sd31" + mobile;
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if ("2".equals(isSuccess)) {
				info.setIsSuccess(1);
			} else if ("1".equals(isSuccess)) {
				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
				info.setNewffid(newffid);
				info.setIsSuccess(2);
			} else {
				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
				info.setNewffid(newffid);
				info.setIsSuccess(CommonTool.convertInt(isSuccess));
			}
			info.setMobile(mobile);
			info.setCode(code);
//			processRTBYOrder(info);
			processBYOrdersd46Date(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
	}
	/***
	 * http://118.190.90.1/fee/feeSZLTzcjdsd71
	 * jd注册无透传 sd71
	 */
	public void feeSZLTzcjdsd71(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeSZLTzcjdsd71请求header信息：" + request.allHeaders());
			
			response.content("ok").end();
			String body = request.body();
			logger.info("feeSZLTzcjdsd71请求body信息：" + body);
			Map<String, String> reqmap = CommonTool.parseYZ(body);
			String isSuccess = reqmap.get("status");
			String mobile = reqmap.get("mobile");
			String phone = reqmap.get("phone");
			String code = reqmap.get("code");
			String grade = reqmap.get("grade");
			if (mobile == null || mobile.length() <= 0) {
				mobile = phone;
			}
//			if ("".equals(grade)||grade==null) {
//				grade="3";
//			}
//			String ffId = DateTool.getMonthDay() + "sd31" + mobile;
			String ffId = DateTool.getMonth() + "_" + DateTool.getffIdDay()+DateTool.getMonthDay() + "sd71" + mobile;
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if ("2".equals(isSuccess)) {
				info.setIsSuccess(1);
			} else if ("1".equals(isSuccess)) {
				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
				info.setNewffid(newffid);
				info.setIsSuccess(2);
			} else {
				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
				info.setNewffid(newffid);
				info.setIsSuccess(CommonTool.convertInt(isSuccess));
			}
			info.setMobile(mobile);
			info.setCode(code);
			info.setPck(grade);
//			processRTBYOrder(info);
			processBYOrdersd46DateGrade(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
	}
	
	/***
	 * http://118.190.90.1/fee/feeSZLTzcjdsd32
	 * jd注册无透传 sd32
	 */
	public void feeSZLTzcjdsd32(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeSZLTzcjdsd32请求header信息：" + request.allHeaders());
			
			response.content("ok").end();
			
			String isSuccess = request.getParam("status");
			String mobile = request.getParam("mobile");
			String phone = request.getParam("phone");
			String code = request.getParam("code");
			if (mobile == null || mobile.length() <= 0) {
				mobile = phone;
			}
			String ffId = DateTool.getMonth() + "sd32" + mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if ("2".equals(isSuccess)) {
				info.setIsSuccess(1);
			} else if ("1".equals(isSuccess)) {
				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
				info.setNewffid(newffid);
				info.setIsSuccess(2);
			} else {
				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
				info.setNewffid(newffid);
				info.setIsSuccess(CommonTool.convertInt(isSuccess));
			}
			info.setMobile(mobile);
			info.setCode(code);
//			processRTBYOrder(info);
			processBYOrder(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
	}
	
	/***
	 * 订单回调 12306注册无透传
	 * http://120.24.88.90/fee/feeSZLTzclWtc
	 */
	public  void feeSZLTzclWtc(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeSZLTzclWtc请求header信息：" + request.allHeaders());
			
			response.content("ok").end();
			
			String isSuccess = request.getParam("status");
			String mobile = request.getParam("phone");
			String code = request.getParam("code");
//			String ffId = DateTool.getMonthDay() + mobile;
			String ffId =DateTool.getMonth() + "_" + DateTool.getffIdDay() + DateTool.getMonthDay() + mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if ("2".equals(isSuccess)) {
				info.setIsSuccess(1);
			} else if ("1".equals(isSuccess)) {
				info.setIsSuccess(2);
			} else if ("3".equals(isSuccess) || "5".equals(isSuccess)) {
//				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
//				info.setNewffid(newffid);
				info.setIsSuccess(CommonTool.convertInt(isSuccess));
			} else {
				info.setIsSuccess(CommonTool.convertInt(isSuccess));
			}
			info.setMobile(mobile);
			info.setCode(code);
//			processRTBYOrder(info);
			processBYOrdersd59Date(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
	}

	/***
	 * 订单回调 微信注册无透传
	 * http://118.190.90.1/fee/feeSZLTwcWtc
	 */
	public void feeSZLTwcWtc(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeSZLTwcWtc请求header信息：" + request.allHeaders());
			
			String isSuccess = request.getParam("status");
			String mobile = request.getParam("phone");
			String ffId = DateTool.getMonth() + "wc" + mobile;
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if ("2".equals(isSuccess)) {
				info.setIsSuccess(1);
			} else if ("1".equals(isSuccess)) {
				info.setIsSuccess(2);
			} else if ("3".equals(isSuccess) || "5".equals(isSuccess)) {
				String newffid = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
				info.setNewffid(newffid);
				info.setIsSuccess(CommonTool.convertInt(isSuccess));
			} else {
				info.setIsSuccess(CommonTool.convertInt(isSuccess));
			}
			info.setMobile(mobile);
			processBYOrder(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/***
	 * TB 注册
	 * http://118.190.90.1/fee/feeSZLTtbzc
	 */
	public void feeSZLTtbzc(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeSZLTtbzc请求header信息：" + request.allHeaders());
			
//			String isSuccess = request.getParam("status");
//			String mobile = request.getParam("phone");
			String ffId = request.getParam("ext");
			
			processXSOrder(ffId, true);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/***
	 * 订单回调
	 * http://118.190.90.1/fee/feeszrt12306
	 */
	public void feeszrt12306(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeszrt12306请求header信息：" + request.allHeaders());
			
			String isSuccess = request.getParam("status");
			String mobile = request.getParam("phone");
			String ffId = request.getParam("cpParam");
			if (ffId == null || ffId.length() <= 0)
				ffId = request.getParam("ffId");
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			/*if ("2".equals(isSuccess)) {
				info.setIsSuccess(1);
			} else if ("1".equals(isSuccess)) {
				info.setIsSuccess(2);
			} else {
			}*/
			info.setIsSuccess(CommonTool.convertInt(isSuccess));
			info.setMobile(mobile);
			processBYOrder(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/***
	 * 订单回调
	 * http://register.xushihudong.com/fee/feeSZLTzc12306
	 */
	public void feeSZLTzc12306(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feeSZLTzc12306请求header信息：" + request.allHeaders());
			
			String isSuccess = request.getParam("status");
			String mobile = request.getParam("phone");
			String ffId = request.getParam("cpParam");
			if (ffId == null || ffId.length() <= 0)
				ffId = request.getParam("ffId");
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			/*if ("2".equals(isSuccess)) {
				info.setIsSuccess(1);
			} else if ("1".equals(isSuccess)) {
				info.setIsSuccess(2);
			} else {
			}*/
			info.setIsSuccess(CommonTool.convertInt(isSuccess));
			info.setMobile(mobile);
			processxsBYOrder(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/***
	 * 订单回调
	 * http://120.76.230.89/fee/feewjZFB
	 */
	public void feewjZFB(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("请求header信息：" + request.allHeaders());

			String ffId = request.getParam("cpparam");
			String cpid = request.getParam("cpid");
			String price = request.getParam("price");
			String state = request.getParam("state");
			String sn = request.getParam("sn");
			String imsi = request.getParam("imsi");
			String tel = request.getParam("tel");
			String agreement_no = request.getParam("agreement_no");
			int isSuc = 0;
			if ("0".equals(state)) {
				isSuc = 1;
			} else if("3".equals(state)) {
				isSuc = 5;
			}

			int synStatus = 0;

			OrderT ot = commonAction.queryOrderT(ffId);
			if (ot == null) {
				logger.warn("feewjZFB找不到对应的订单号=" + ffId);
				return;
			}

			OrderReqInfo order = ot.getOrder();
			CPInfo cp = ot.getCp();

			if ((cp.getUrl() != null) && (cp.getUrl().length() > 0)) {
				String result = "";
				String param = "";
				StringBuilder msg = new StringBuilder();
				msg.append("imsi=").append(imsi)
						.append("&cpid=").append(cpid)
						.append("&price=").append(price)
						.append("&state=").append(state)
						.append("&sn=").append(sn)
						.append("&tel=").append(tel)
						.append("&agreement_no=").append(agreement_no)
						.append("&cpparam=").append(order.getCpParam())
						.append("&ip=").append(order.getIp());
				param = msg.toString();
				result = HttpTool.sendGet(cp.getUrl(), param);

				logger.info("feewjZFB给CP的同步结果：" + result);
				if (result.equals("success"))
					synStatus = 1;
				else
					synStatus = -1;
			} else {
				synStatus = -1;
			}


			commonAction.updateOrderZFB(ffId, agreement_no, sn, isSuc, synStatus);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	public void feeZFB(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("请求header信息：" + request.allHeaders());

			String ffId = request.getParam("ffid");
			String isSuccess = request.getParam("issuccess");
			boolean isSuc = false;
			if ((isSuccess != null) && (isSuccess.length() != 0))
				isSuc = true;

			int synStatus = 0;

			OrderT ot = commonAction.queryOrderT(ffId);
			if (ot == null) {
				logger.warn("feeZFB找不到对应的订单号=" + ffId);
				return;
			}

			OrderReqInfo order = ot.getOrder();
			CPInfo cp = ot.getCp();

			if (order.getIsSyn() == 1) {
				if ((cp.getUrl() != null) && (cp.getUrl().length() > 0)) {
					String result = "";
					String param = "";
					if (cp.getBackMethod() == 1) {
						SynJson sj = new SynJson();
						sj.setFfId(ffId);
						sj.setCpParam(order.getCpParam());
						sj.setImei(order.getImei());
						sj.setImsi(order.getImsi());
						sj.setFee(order.getFee());
						sj.setIp(order.getIp());
						param = JSON.toJSONString(sj);
						result = HttpTool.sendKLWPost(cp.getUrl(), param, "utf-8", 1000);
					} else {
						StringBuilder msg = new StringBuilder();
						msg.append("imsi=").append(order.getImsi())
								.append("&imei=").append(order.getImei())
								.append("&ffId=").append(ffId)
								.append("&cpParam=").append(order.getCpParam())
								.append("&fee=").append(order.getFee())
								.append("&ip=").append(order.getIp());
						param = msg.toString();
						result = HttpTool.sendGet(cp.getUrl(), param);
					}

					logger.info("feeZFB给CP的同步结果：" + result);
					if (result.equals("success"))
						synStatus = 1;
					else
						synStatus = -1;
				} else {
					synStatus = -1;
				}

			}

			commonAction.updateOrder(ffId, isSuc, synStatus);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
	}

	/**
	 * 提取号码码接口
	 * http://120.24.88.90/fee/getMobile
	 */
	public void getMobile(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String sms = "error";
		try {
			logger.info("getMobile请求header信息：" + request.allHeaders());
			
			String pid = request.getParam("pid");
			String cpId = commonAction.getCpid(pid);
			String count = request.getParam("count");
			if (count == null || count.length() <= 0) count = "1";
			int codetype = 1;
			
			OrderReqInfo reqInfo = new OrderReqInfo();
			reqInfo.setCpId(cpId);
			reqInfo.setCodeType(codetype);
			reqInfo.setCount(CommonTool.convertInt(count));
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			
			String spId = "oo11";
			if (ct == null) {
				logger.warn("没有开通注册通道");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				StringBuilder clog = new StringBuilder();
				for (CodeInfo info : ct.getCodes()) {
					spId = info.getSpId();
					ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), info.getSpFlag());
					reqInfo.setUrl(info.getUrl());
					reqInfo.setUrlNo(info.getUrlNo());
					reqInfo.setPmodel("");
					reqInfo.setFfId(ffId);
					reqInfo.setKeyword(info.getKeyword());
					reqInfo.setMatchRegex(info.getMatchRegex());
					reqInfo.setFees(info.getFee());
					reqInfo.setMsg(info.getMemo());
					reqInfo.setSpId(spId);
					sms = CodeReqHelper.getSMSCode(reqInfo);
					if (!"error".equals(sms)) {
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						break;
					}
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(sms).end();
		
	}

	/**
	 * 提取号码和验证码接口
	 * http://120.24.88.90/fee/getMobileAndCode
	 */
	public void getMobileAndCode(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String sms = "error";
		try {
			logger.info("getMobileAndCode请求header信息：" + request.allHeaders());
			
			String pid = request.getParam("pid");
			String cpId = commonAction.getCpid(pid);
			String messageId = cpId;
			String phones = request.getParam("phones");
			if (phones != null) {
				phones = URLDecoder.decode(phones, "utf-8");
			}
			JSONArray jsonArr1 = JSONArray.parseArray(phones);
			int count = jsonArr1.size();
			int codetype = 2;
			
			OrderReqInfo reqInfo = new OrderReqInfo();
			reqInfo.setCpId(cpId);
			reqInfo.setCodeType(codetype);
			reqInfo.setCount(count);
			reqInfo.setMobile(phones);
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			
			String spId = "oo11";
			if (ct == null) {
				logger.warn("没有开通注册通道");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				StringBuilder clog = new StringBuilder();
				for (CodeInfo info : ct.getCodes()) {
					spId = info.getSpId();
					ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), info.getSpFlag());
					reqInfo.setUrl(info.getUrl());
					reqInfo.setUrlNo(info.getUrlNo());
					reqInfo.setPmodel("");
					reqInfo.setFfId(ffId);
					reqInfo.setKeyword(info.getKeyword());
					reqInfo.setMatchRegex(info.getMatchRegex());
					reqInfo.setFees(info.getFee());
					reqInfo.setMsg(info.getMemo());
					reqInfo.setSpId(spId);
					sms = CodeReqHelper.getSMSCode(reqInfo);
					if (!"error".equals(sms)) {
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						break;
					}
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			
			if (!"error".equals(sms)) {
				JSONArray jsonArr = JSONArray.parseArray(sms);
				for (int i=0 ; i<jsonArr.size(); i++) {
					JSONObject jsonObj = JSONObject.parseObject(jsonArr.getString(i));
					String mobile = jsonObj.getString("mobile");
					String code = jsonObj.getString("code");
					CustomInfo info = new CustomInfo();
					info.setMessageId(messageId);
					info.setSmsMessage(code);
					info.setMobile(mobile);
					info.setLocalTab("00");
					if (mobile != null) {
						info.setLocalTab(CommonTool.getImsiSub(mobile));
					}
					commonAction.addCustomInfo(info);
				}
			}
			
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(sms).end();
		
	}
	
	public void getCustomInfo(HttpRequest request, HttpResponse response)
			throws JuiceException {
		StringBuffer result = new StringBuffer();
		try {
			logger.info("请求header信息：" + request.allHeaders());

			String content = URLDecoder.decode(request.body(), "utf-8");
			logger.info("短信服务信息：" + content);

			Map<String,String> myMap = new HashMap<String, String>();
			String[] strArr = content.split("&");
			for (String s : strArr) {
				String[] tempList = s.split("=");
				if (tempList.length == 2) {
					myMap.put(tempList[0], tempList[1]);
				}
			}
			String MessageId = (String) myMap.get("MessageId");
			String SmsMessage = (String) myMap.get("SmsMessage");
			String Mobile = (String) myMap.get("Mobile");
			String SrcNumber = (String) myMap.get("SrcNumber");
			String City = (String) myMap.get("City");
			String Pre = (String) myMap.get("Pre");
			String PortType = (String) myMap.get("PortType");

			result.append("{\"MessageId\":\"").append(MessageId)
					.append("\",\"SmsMessage\":\"").append(SmsMessage)
					.append("\",\"Mobile\":\"").append(Mobile)
					.append("\",\"SrcNumber\":\"").append(SrcNumber)
					.append("\",\"Pre\":\"").append(Pre).append("\"}");

			CustomInfo info = new CustomInfo();
			info.setMessageId(MessageId);
			info.setSmsMessage(SmsMessage);
			info.setMobile(Mobile);
			info.setSrcNumber(SrcNumber);
			info.setCity(City);
			info.setPre(Pre);
			info.setPortType(PortType);
			info.setLocalTab("00");

			String[] params = SmsMessage.split("\\#");
			if (params.length == 3) {
				info.setImsi(params[0]);
				info.setImsiFlag(params[1]);
				info.setIccid(params[2]);
				info.setLocalTab(CommonTool.getImsiSub(params[0]));
			}

			commonAction.addCustomInfo(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(result.toString()).end();
	}

	public void checkCustomNum(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "error";
		try {
			logger.info("请求header信息：" + request.allHeaders());

			String imsi = request.getParam("imsi");

			CustomInfo info = new CustomInfo();
			info.setImsi(imsi);
			info.setLocalTab(imsi);

			CustomInfo resultInfo = commonAction.getCustomInfo(info);
			if (resultInfo != null)
				result = resultInfo.getMobile();
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}

		response.content(result).end();
	}

	public void refreshCache(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "error";
		try {
			logger.info("refreshCache请求header信息：" + request.allHeaders());

			commonAction.refreshCache();
			result = "ok";
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}

		response.content(result).end();
	}

	public void updateklwsdk(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "error";
		try {
			logger.info("refreshCache请求header信息：" + request.allHeaders());

			String id = request.getParam("id");
			int iid = id == null ? 0 : CommonTool.convertInt(id);

			SDKUpdateInfo reqInfo = new SDKUpdateInfo();
			reqInfo.setId(iid);
			SDKUpdateInfo info = commonAction.getSDKUpdateParam(reqInfo);
			if (info != null)
				result = JSONObject.toJSONString(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}

		response.content(result).end();
	}

	public void getzfbkey(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "error";
		try {
			logger.info("getzfbkey请求header信息：" + request.allHeaders());

			String orderInfo = request.getParam("orderInfo");
			result = StringUtils
					.sign(orderInfo,
							"MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALq3jFZhW8us7o4opJMpnZLSwv1SCdAL6/0ip8DB1u0EfuTHPy6XS49PlbR3sVDFJvRzNF2Fc7tylWYDoOTztyapdV8Y2LnLI9W8B/aWtn0fhAzLyr2IFLhHmP6VcGrCWY0DYnL12wTo5jgvbqCLH/Q3DBvekD6q13pmIbxw5uJDAgMBAAECgYEAg+PlgJrS8cMc21QANGeAA9dWnwPVJJ1XO/6/ylviCphTXh0UF0ANwpRv5gNqP+iThLbf9XOv9WeR+nZzr2YsJzB4wJQHjF1LrH1HFdFwhul19BSjEXkzcyCl0/tfeIsxvhdNYp7IhznlitGDAvOZEb4a1+e+L5Y0Uhx3kQLC8VkCQQDrLg3IK9HXKLaUg3BtSs1H3p5yDXs8ytNemfiVVwB2dyBG/q+RGjUpgwXbgEG8OeuGgYXcoYClJ+OSl/jWrrn1AkEAyz8qiNTw6C9s8e8z4MyCdTDiKXTCmA/du07AXC08NK3VOvw27N4b/5NNt7WyR7N/vQTxSx8d6P8uPRzvynDwVwJAf1+/GSYajcTANmmF77uuuPkqqa0BjShxGHCeAQxQ9NiKQ8lW/2jJWEVdW8f4UmCwXnYyMQ/LpCeZiuECZgvWLQJBAK76lL30xeq/WcX4L+urZe1Kxor2UMnlbvnhVM6Gyzx3JAqCNS88EVK5GMG+ldwQ9zpeVPZdtbxwZPiMPw1EqkUCQGph1+LoYCffaaCZw4aCPitO1t8Dfa2V8aBedhVqyXb74EQ7rSJIDcG2SDua/U4s+hEFQ+dOepC3gQ300xLWI80=");
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}

		response.content(result).end();
	}

	public void getxdkey(HttpRequest request, HttpResponse response)
			throws JuiceException {
		String result = "error";
		try {
			logger.info("getxdkey请求header信息：" + request.allHeaders());

			String content = request.body();
			String appkey = "8oV2czsLo5oUet0d8xXLd7gEMlFCMprx";
			content = URLDecoder.decode(content, "utf-8");
			logger.info("getxdkey传递post内容：" + content);
			result = MD5.md5(content + "&" + MD5.md5(appkey, "utf-8"), "utf-8");
			result = "mhtSignature=" + result + "&mhtSignType=MD5";
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}

		response.content(result).end();
	}
	
	/**
	 * 处理订单
	 */
	private void processXSOrder(String ffId, boolean isSuc) {
		try {
			int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
			if (!isSuc) {
//				commonAction.updateOrder(ffId, isSuc, synStatus);
				return;
			} else {
				commonAction.saveExt(ffId);
			}
			
			OrderT ot = commonAction.queryOrderT(ffId);
			if (ot == null) {
				logger.warn("找不到对应的订单号=" + ffId);
				return;
			}
			
			OrderReqInfo order = ot.getOrder();
			CPInfo cp = ot.getCp();
			
			if (order.getIsSyn() == ConstantDefine.SYN_TRUE && order.getSynStatus() == 0) {
				order.setSmsport(order.getCpId());
				order.setSmscontent(order.getSpId());
				order.setIsSuccess(1);
				String flag = testImsiMap.get(order.getImsi());
				int synRadio = cp.getSynRadio();
				if (synRadio == 100) flag = "true";
				if (!"true".equals(flag)) {
					String province = order.getProvince();
					String newCpId = cp.getCpId()+order.getSpId()+province;
					
					synRadio = getSynRadio(cp, province, synRadio);
					
					logger.info("同步省份：" + province + "  同步率synRadio："+synRadio);
					CPParam cpp = CommonTool.getCPParam(newCpId,
							synRadio);
					
					AtomicInteger ati = cpp.getAti();
					boolean isSyn = CommonTool.isContain(cpp.getVc(),
							ati.incrementAndGet());
//					logger.info("同步计算结果："+isSyn+",newCpId："+newCpId+",ati:"+ati.get()+",vc:"+cpp.getVc());
					
					if (checkOrderNum(cp)) isSyn = true;
					
					if (isSyn) {
						if (cp.getUrl() != null && cp.getUrl().length() > 0) {
							// 同步
							String result = notityXSResult(cp,ffId,order);
							
							logger.info("给CP的同步结果："+result);
							if (result.equals("ok")) {
								synStatus = ConstantDefine.SYN_STATUS_TRUE;
							} else {
								synStatus = ConstantDefine.SYN_STATUS_FALSE;
							}
						} else {
							synStatus = ConstantDefine.SYN_STATUS_FALSE;
						}
					} else {
						synStatus = ConstantDefine.SYN_STATUS_Buckle;
					}
					
					if (ati.get() == 100)
						ati.set(0);
					cpp.setAti(ati);
//					CommonTool.updateCPParam(cp.getCpId(), cpp);
					CommonTool.updateCPParam(newCpId, cpp);
				} else {
					String result = notityXSResult(cp,ffId,order);
					
					logger.info("给CP的同步结果："+result);
					if (result.equals("ok")) {
						synStatus = ConstantDefine.SYN_STATUS_TRUE;
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				}
			}
			if (order.getSynStatus() == 0) {
				commonAction.updateOrder(ffId, isSuc, synStatus);
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
	}
	
	/**
	 * 范范 同步接口
	 * http://smspay.xushihudong.com/fee/feexsffback
	 */
	public void feexsffback(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feexsffback请求header信息：" + request.allHeaders());
			
			String ffId = request.getParam("param");
			String isSuccess = request.getParam("status");
			String mobile = request.getParam("phone");
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if ("DELIVRD".equals(isSuccess) && !"0".equals(mobile)) {
				info.setIsSuccess(1);//订购
			} else if ("3".equals(isSuccess)) {
				info.setIsSuccess(3);
			} else {
				info.setIsSuccess(9);
			}
			info.setMobile(mobile);
			processxsBYOrder(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/**
	 * 代付包月 同步接口
	 * http://smspay.xushihudong.com/fee/feexsdfbyback
	 */
	public void feexsdfbyback(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feexsdfbyback请求header信息：" + request.allHeaders());
			
			String ffId = request.getParam("param");
			String spnumber = request.getParam("spnumber");
			String momsg = request.getParam("momsg");
			String isSuccess = request.getParam("flag");
			String mobile = request.getParam("mobile");
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if ("DELIVRD".equals(isSuccess) && !"0".equals(mobile)) {
				info.setIsSuccess(1);//订购
			} else if ("3".equals(isSuccess)) {
				info.setIsSuccess(3);
			} else {
				info.setIsSuccess(9);
			}
			info.setMobile(mobile);
			processxsBYOrder(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/**
	 * 虚实彩信 同步接口
	 * http://120.24.88.90/fee/feexscxbyback
	 */
	public void feexscxbyback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexscxbyback请求header信息：" + request.allHeaders());
			
			String mobile = request.getParam("mobile");
			String status = request.getParam("status");
			String fee = request.getParam("fee");
			String smsport = request.getParam("smsport");
			String sms = request.getParam("sms");
			String linkid = request.getParam("linkid");
			String date = request.getParam("date");
			String pid = request.getParam("pid");
			String tab = request.getParam("tab");
			
			if (date != null && date.length() > 0) 
				date = URLDecoder.decode(date, "utf-8");
			
			int isSuccess = CommonTool.convertInt(status);
			
			OrderReqInfo info = new OrderReqInfo();
			info.setMobile(mobile);
			info.setSpId("wt01");
			info.setIsSuccess(isSuccess);
			info.setFee(CommonTool.convertInt(fee));
			info.setSms(smsport);
			info.setSmscontent(sms);
			info.setStartTime(date);
			info.setCpParam(linkid);
			info.setTab(tab);
			
			processxsWlwWtcByOrder(pid,info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 苏州润土视频包月 同步接口
	 * http://120.24.88.90/fee/feexsszrtback
	 */
	public void feexsszrtback(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feexsszrtback请求header信息：" + request.allHeaders());
			
			String ffId = request.getParam("cpparam");
			String status = request.getParam("status");
			boolean isSucc = false;
			
			if ("1".equals(status))
				isSucc = true;
			
			processXSOrder(ffId, isSucc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/**
	 * 掌上时代MDO 同步接口
	 * http://smspay.xushihudong.com/fee/feexszssdback
	 */
	public void feexszssdback(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feexszssdback请求header信息：" + request.allHeaders());
			
			String ffId = request.getParam("cp_param");
			String status = request.getParam("state");
			boolean isSucc = false;
			
			if ("0".equals(status))
				isSucc = true;
			
			processXSOrder(ffId, isSucc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/**
	 * 微信辅助 同步接口
	 * http://118.190.90.1/fee/feertwxfzback
	 */
	public void feertwxfzback(HttpRequest request, HttpResponse response)
			throws Exception {
		String sms = "error";
		try {
			logger.info("feertwxfzback请求header信息：" + request.allHeaders());
			
			String ffId = request.getParam("orderid");
			String status = request.getParam("status");
			boolean isSucc = false;
			
			if ("1".equals(status))
				isSucc = true;
			
			OrderT ot = commonAction.queryOrderT(ffId);
			int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
			if (ot != null) {
				OrderReqInfo order = ot.getOrder();
				String spid = order.getSpId();
				CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spid);
				String url = codeInfo.getMatchRegex();
				String keyword = codeInfo.getKeyword();
				String[] tmp = keyword.split("#");
				String account = tmp[0];
				String username = tmp[1];
				String password = tmp[2];
				String date = DateTool.getNow().substring(0,8);
				
				String token = MD5Tool.toMD5Value(username+password+date);
				
				StringBuilder param = new StringBuilder();
				param.append("account=").append(account)
				.append("&Token=").append(token)
				.append("&fname=").append(ffId);
				
				if (isSucc) {
					String reqResult = HttpTool.sendPost(url, param.toString(), "5000");
					sms = reqResult;
					synStatus = ConstantDefine.SYN_STATUS_TRUE;
				} else {
					url = url.replace("record_notify", "del_record");
					String reqResult = HttpTool.sendPost(url, param.toString(), "5000");
					sms = reqResult;
					synStatus = ConstantDefine.SYN_STATUS_TRUE;
				}
			} else {
				logger.info(ffId+"，订单号不存在");
				sms = "订单号不存在";
			}
			commonAction.updateOrder(ffId, isSucc, synStatus);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content(sms.getBytes()).end();
		
	}
	
	/**
	 * 朱云MM同步接口
	 * http://smspay.xushihudong.com/fee/feexszymmback
	 */
	public void feexszymmback(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feexszymmback请求header信息：" + request.allHeaders());
			
			String ffId = request.getParam("cp_param");
			String status = request.getParam("state");
			boolean isSucc = false;
			
			if ("1".equals(status))
				isSucc = true;
			
			processXSOrder(ffId, isSucc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/**
	 * 柒颜文化MM 同步接口
	 * http://smspay.xushihudong.com/fee/feexsqrwhback
	 */
	public void feexsqrwhback(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feexsqrwhback请求header信息：" + request.allHeaders());
			
			String ffId = request.getParam("momsg");
			String status = "1";
			boolean isSucc = false;
			
			if ("1".equals(status))
				isSucc = true;
			
			processXSOrder(ffId, isSucc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/**
	 * 第七城市 同步接口
	 * http://120.24.88.90/fee/feexsdqcsback
	 */
	public void feexsdqcsback(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feexsdqcsback请求header信息：" + request.allHeaders());
			
			String ffId = request.getParam("extData");
			boolean isSucc = true;
			
			processXSOrder(ffId, isSucc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/**
	 * 点通 同步接口
	 * http://120.24.88.90/fee/feexsdtback
	 */
	public void feexsdtback(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("feexsdtback请求header信息：" + request.allHeaders());
			String content = request.body();
			Map<String, String> restmap = XmlUtil.xmlParsebytag(content,"request");
			
			String ffId = restmap.get("cpparam");
			String isSuccess = restmap.get("hret");
			String mobile = restmap.get("mobile");
			
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			if ("0".equals(isSuccess)) {
				info.setIsSuccess(1);//订购
			} else if ("2".equals(isSuccess)) {
				info.setIsSuccess(3);//退订
			} else {
				info.setIsSuccess(9);
			}
			info.setMobile(mobile);
			processxsBYOrder(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	/**
	 * 上海池乐 同步接口
	 * http://120.24.88.90/fee/feexsshclback
	 */
	public void feexsshclback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexsshclback请求header信息：" + request.allHeaders());
			String ffId = request.getParam("cp_param");
			String status = request.getParam("status");
			String province = request.getParam("province_name");
			
			if (status == null || status.length() <= 0) {
				// 处理订单
				boolean isSuc = true;
				
				processXSOrder(ffId, isSuc);
			} else {
				logger.error("上海池乐计费通道"+province+"达到计费上限请切换通道！");
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
	}
	
	/**
	 * 上海弘龙 同步接口
	 * http://120.24.88.90/fee/feexsshhlback
	 */
	public void feexsshhlback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexsshhlback请求header信息：" + request.allHeaders());
			String ffId = request.getParam("cpparam");
			String status = request.getParam("status");
			
			if (status != null && status.length() > 0) {
				// 处理订单
				boolean isSuc = true;
				if (!"0".equals(status)) {
					isSuc = false;
				}
				
				processXSOrder(ffId, isSuc);
			}
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
	}
	
	/**
	 * 虚实彩信点播 同步接口
	 * http://120.24.88.90/fee/feexscxdbback
	 */
	public void feexscxdbback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feexscxdbback请求header信息：" + request.allHeaders());
			
			String mobile = request.getParam("mobile");
			String status = request.getParam("status");
			String fee = request.getParam("fee");
			String smsport = request.getParam("smsport");
			String sms = request.getParam("sms");
			String date = request.getParam("date");
			String pid = request.getParam("pid");
			
			int isSuccess = CommonTool.convertInt(status);
			
			OrderReqInfo info = new OrderReqInfo();
			info.setMobile(mobile);
			info.setSpId("yc99");
			info.setIsSuccess(isSuccess);
			info.setFee(CommonTool.convertInt(fee));
			info.setSms(smsport);
			info.setSmscontent(sms);
			info.setStartTime(date);
			info.setCpParam(pid);
			
			processxsWlwWtcOrder(pid,info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		
		response.content("ok").end();
	}
	
	/**
	 * 苏州乐米 回调通知
	 * http://120.24.88.90/fee/feeszlmmm
	 */
	public void feeszlmmm(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("feeszlmmm请求header信息："+request.allHeaders());
			String result = request.getParam("result");
			String ffId = request.getParam("extparams");
			// 处理订单
			boolean isSuc = false;
			if ("0".equals(result)) 
				isSuc = true;
			processXSOrder(ffId, isSuc);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();
		
	}
	
	
	
	
	
	
	
	/**
	 * 虚实无联网无透传参数订单同步处理
	 * @param pid
	 * @param info
	 * @throws Exception 
	 */
	private void processxsWlwWtcOrder(String pid, OrderReqInfo info) throws Exception {
		CPInfo cpinfo = commonAction.getCpByPid(pid);
		String cpId = cpinfo.getCpId();
		String spid = cpinfo.getLocalSpId();
		
		int isSuccess = info.getIsSuccess();
		String ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), spid.substring(0,2));
		CPInfo cp = commonAction.queryCP(cpId);
		int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
		
		String newCpId = cp.getCpId()+spid;
		int synRadio = cp.getSynRadio();
		logger.info("同步率synRadio："+synRadio);
		CPParam cpp = CommonTool.getCPParam(newCpId,
				synRadio);
		
		String province = "局域";
		
		String mobile = info.getMobile();
		MobileInfo mobileInfo = getProvinceByPhone(mobile);
		if (mobileInfo != null) {
			province = mobileInfo.getProvince();
			String corp = mobileInfo.getCorp();
			if ("中国移动".equals(corp)) {
				logger.info("移动用户phone：" + mobile);
			} else if ("中国联通".equals(corp)) {
				logger.info("联通用户phone：" + mobile);
			} else {
				logger.info("电信用户phone：" + mobile);
			}
			logger.info("根据phone查询省份地市：" + province);
		}
		
		info.setCpId(cpId);
		info.setFfId(ffId);
		info.setSpId(spid);
		info.setIp("127.0.0.1");
		info.setProvince(province);
		info.setIsSyn(1);
		
		boolean flag = true;
		if (synRadio == 100) flag = false;
		if (isSuccess == 1 && flag) {
			AtomicInteger ati = cpp.getAti();
			boolean isSyn = CommonTool.isContain(cpp.getVc(),
					ati.incrementAndGet());
			if (checkOrderNum(cp)) isSyn = true;
			if (isSyn) {
				if (cp.getUrl() != null && cp.getUrl().length() > 0) {
					// 同步
					String result = notityxsWlwWtcResult(cp,pid,info);
					
					logger.info("给CP的同步结果："+result);
					if (result.equals("ok")) {
						synStatus = ConstantDefine.SYN_STATUS_TRUE;
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				} else {
					synStatus = ConstantDefine.SYN_STATUS_FALSE;
				}
			} else {
				synStatus = ConstantDefine.SYN_STATUS_Buckle;
			}
			
			if (ati.get() == 100)
				ati.set(0);
			cpp.setAti(ati);
			CommonTool.updateCPParam(newCpId, cpp);
		} else {
			// 同步
			String result = notityxsWlwWtcResult(cp,pid,info);
			logger.info("给CP的同步结果："+result);
			if (result.equals("ok")) {
				synStatus = ConstantDefine.SYN_STATUS_TRUE;
			} else {
				synStatus = ConstantDefine.SYN_STATUS_FALSE;
			}
		}
		
		info.setSynStatus(synStatus);
		info.setCpParam(pid);
		commonAction.addOrderReqInfoAll(info);
	}
	
	/**
	 * 虚实无联网无透传参数包月订单同步处理
	 * @param pid
	 * @param info
	 * @throws Exception 
	 */
	private void processxsWlwWtcByOrder(String pid, OrderReqInfo info) throws Exception {
		CPInfo cpinfo = commonAction.getCpByPid(pid);
		String cpId = cpinfo.getCpId();
		
		int isSuccess = info.getIsSuccess();
		CPInfo cp = commonAction.queryCP(cpId);
		if (cp == null) {
			logger.error("processxsWlwWtcByOrder中cpid："+cpId+"没有打开权限！");
			return;
		}
		String spid = cpinfo.getLocalSpId();
		String ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), spid.substring(0,2));
		String tab = info.getTab();
		if (tab != null && tab.length() > 0) ffId = tab;
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setSpId(spid);
		reqInfo.setFfId(ffId);
		reqInfo.setMobile(info.getMobile());
		OrderReqInfo order = commonAction.queryOrderByCondition(reqInfo);
		int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
		String newCpId = cp.getCpId()+spid;
		int synRadio = cp.getSynRadio();
		logger.info("同步率synRadio："+synRadio);
		CPParam cpp = CommonTool.getCPParam(newCpId,
				synRadio);
		if (order != null && !cp.getCpId().equals(order.getCpId())) {
			if (isSuccess == 1) {
				if (order.getIsSuccess() == 3) {
					order = null;
				} else {
					return;
				}
			} else {
				cpId = order.getCpId();
				cp = commonAction.queryCP(cpId);
			}
		}
		if (order != null) {
			ffId = order.getFfId();
			info.setFfId(ffId);
			synStatus = order.getSynStatus();
			if (synStatus == 0) {
				AtomicInteger ati = cpp.getAti();
				boolean isSyn = CommonTool.isContain(cpp.getVc(),
						ati.incrementAndGet());
				if (checkOrderNum(cp)) isSyn = true;
				if (isSyn) {
					if (cp.getUrl() != null && cp.getUrl().length() > 0) {
						// 同步
						String result = "no";
						if (1 == isSuccess) {
							result = notityxsWlwWtcResult(cp,pid,info);
						}
						
						logger.info("给CP的同步结果："+result);
						if (result.equals("ok")) {
							synStatus = ConstantDefine.SYN_STATUS_TRUE;
						} else {
							synStatus = ConstantDefine.SYN_STATUS_FALSE;
						}
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				} else {
					synStatus = ConstantDefine.SYN_STATUS_Buckle;
				}
				
				if (ati.get() == 100)
					ati.set(0);
				cpp.setAti(ati);
				CommonTool.updateCPParam(newCpId, cpp);
			} else if (synStatus != 4) {
				String result = "no";
				if (1 == isSuccess) {
					result = notityxsWlwWtcResult(cp,pid,info);
				}
				logger.info("给CP的同步结果："+result);
				if (result.equals("ok")) {
					synStatus = ConstantDefine.SYN_STATUS_TRUE;
				} else {
					synStatus = ConstantDefine.SYN_STATUS_FALSE;
				}
			}
			info.setSynStatus(synStatus);
			commonAction.updateOrderInfo(info);
		} else {
			
			String province = "局域";
			String mobile = info.getMobile();
			MobileInfo mobileInfo = getProvinceByPhone(mobile);
			if (mobileInfo != null) {
				province = mobileInfo.getProvince();
				String corp = mobileInfo.getCorp();
				if ("中国移动".equals(corp)) {
					logger.info("移动用户phone：" + mobile);
				} else if ("中国联通".equals(corp)) {
					logger.info("联通用户phone：" + mobile);
				} else {
					logger.info("电信用户phone：" + mobile);
				}
				logger.info("根据phone查询省份地市：" + province);
			}
			
			info.setCpId(cpId);
			info.setFfId(ffId);
			info.setSpId(spid);
			info.setIp("127.0.0.1");
			info.setProvince(province);
			info.setIsSyn(1);
			
			boolean flag = true;
			if (synRadio == 100) flag = false;
			if (isSuccess == 1 && flag) {
				AtomicInteger ati = cpp.getAti();
				boolean isSyn = CommonTool.isContain(cpp.getVc(),
						ati.incrementAndGet());
				if (checkOrderNum(cp)) isSyn = true;
				if (isSyn) {
					if (cp.getUrl() != null && cp.getUrl().length() > 0) {
						// 同步
						String result = "no";
						if (1 == isSuccess) {
							result = notityxsWlwWtcResult(cp,pid,info);
						}
						
						logger.info("给CP的同步结果："+result);
						if (result.equals("ok")) {
							synStatus = ConstantDefine.SYN_STATUS_TRUE;
						} else {
							synStatus = ConstantDefine.SYN_STATUS_FALSE;
						}
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				} else {
					synStatus = ConstantDefine.SYN_STATUS_Buckle;
				}
				
				if (ati.get() == 100)
					ati.set(0);
				cpp.setAti(ati);
				CommonTool.updateCPParam(newCpId, cpp);
			} else {
				// 同步
				String result = "1";
				if (isSuccess == 1) {
					result = notityxsWlwWtcResult(cp,pid,info);
				}
				logger.info("给CP的同步结果："+result);
				if (result.equals("ok")) {
					synStatus = ConstantDefine.SYN_STATUS_TRUE;
				} else {
					synStatus = ConstantDefine.SYN_STATUS_FALSE;
				}
			}
			
			info.setSynStatus(synStatus);
			commonAction.addOrderReqInfoAll(info);
		}
		
	}
	
	/**
	 * 处理订单
	 */
	private void processxsBYOrder(OrderReqInfo reqInfo) {
		String ffId = reqInfo.getFfId();
		try {
			int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
			/*if (!isSuc) {
//				commonAction.updateOrder(ffId, isSuc, synStatus);
				return;
			} else {
				commonAction.saveExt(ffId);
			}*/
			
			OrderT ot = commonAction.queryOrderT(ffId);
			if (ot == null) {
				logger.warn("找不到对应的订单号=" + ffId);
				return;
			}
			
			OrderReqInfo order = ot.getOrder();
			order.setMobile(reqInfo.getMobile());
			CPInfo cp = ot.getCp();
			if (order.getSynStatus() == 4) {
				order.setIsSyn(0);
				synStatus = 4;
			}
			
			if (cp == null) {
				logger.warn("对应cp的通道已经关闭=" + ot.getCp());
				return;
			}
			
			order.setIsSuccess(reqInfo.getIsSuccess());
			if (order.getIsSyn() == ConstantDefine.SYN_TRUE) {
				String flag = testImsiMap.get(order.getImsi());
				int synRadio = cp.getSynRadio();
				if (reqInfo.getIsSuccess() != 1 || synRadio == 100) flag = "true";
				if (!"true".equals(flag)) {
					String province = order.getProvince();
					String newCpId = cp.getCpId()+order.getSpId()+province;
					
					synRadio = getSynRadio(cp, province, synRadio);
					
					logger.info("同步省份：" + province + "  同步率synRadio："+synRadio);
					CPParam cpp = CommonTool.getCPParam(newCpId,
							synRadio);
					
					AtomicInteger ati = cpp.getAti();
					boolean isSyn = CommonTool.isContain(cpp.getVc(),
							ati.incrementAndGet());
					if (checkOrderNum(cp)) isSyn = true;
					if (isSyn) {
						if (cp.getUrl() != null && cp.getUrl().length() > 0) {
							// 同步
							String result = notityXSResult(cp,ffId,order);
							
							logger.info("给CP的同步结果："+result);
							if (result.equals("success")) {
								synStatus = ConstantDefine.SYN_STATUS_TRUE;
							} else {
								synStatus = ConstantDefine.SYN_STATUS_FALSE;
							}
						} else {
							synStatus = ConstantDefine.SYN_STATUS_FALSE;
						}
					} else {
						synStatus = ConstantDefine.SYN_STATUS_Buckle;
					}
					
					if (ati.get() == 100)
						ati.set(0);
					cpp.setAti(ati);
//					CommonTool.updateCPParam(cp.getCpId(), cpp);
					CommonTool.updateCPParam(newCpId, cpp);
				} else {
					String result = notityXSResult(cp,ffId,order);
					
					logger.info("给CP的同步结果："+result);
					if (result.equals("success")) {
						synStatus = ConstantDefine.SYN_STATUS_TRUE;
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				}
			}
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			info.setMobile(reqInfo.getMobile());
			info.setIsSuccess(reqInfo.getIsSuccess());
			info.setSynStatus(synStatus);
			commonAction.updateOrderInfo(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
	}
	/**
	 * 处理订单
	 */
	private void processxsBYOrder2(OrderReqInfo reqInfo) {
		String ffId = reqInfo.getFfId();
		try {
			int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
			/*if (!isSuc) {
//				commonAction.updateOrder(ffId, isSuc, synStatus);
				return;
			} else {
				commonAction.saveExt(ffId);
			}*/
			
			OrderT ot = commonAction.queryOrderTDate(ffId);
			if (ot == null) {
				logger.warn("找不到对应的订单号=" + ffId);
				return;
			}
			
			OrderReqInfo order = ot.getOrder();
			order.setMobile(reqInfo.getMobile());
			CPInfo cp = ot.getCp();
			if (order.getSynStatus() == 4) {
				order.setIsSyn(0);
				synStatus = 4;
			}
			
			if (cp == null) {
				logger.warn("对应cp的通道已经关闭=" + ot.getCp());
				return;
			}
			if (order.getIsSuccess() != 0) {
				logger.warn("屏蔽重复同步" + order.getFfId());
				return;
			}
		
			
			order.setIsSuccess(reqInfo.getIsSuccess());
			if (order.getIsSyn() == ConstantDefine.SYN_TRUE) {
				String flag = testImsiMap.get(order.getImsi());
				int synRadio = cp.getSynRadio();
				if (reqInfo.getIsSuccess() != 1 || synRadio == 100) flag = "true";
				if (!"true".equals(flag)) {
					String province = order.getProvince();
					String newCpId = cp.getCpId()+order.getSpId()+province;
					
					synRadio = getSynRadio(cp, province, synRadio);
					
					logger.info("同步省份：" + province + "  同步率synRadio："+synRadio);
					CPParam cpp = CommonTool.getCPParam(newCpId,
							synRadio);
					
					AtomicInteger ati = cpp.getAti();
					boolean isSyn = CommonTool.isContain(cpp.getVc(),
							ati.incrementAndGet());
					if (checkOrderNum(cp)) isSyn = true;
					if (isSyn) {
						if (cp.getUrl() != null && cp.getUrl().length() > 0) {
							// 同步
							String result = notityXSResult(cp,ffId,order);
							
							logger.info("给CP的同步结果："+result);
							if (result.equals("ok")) {
								synStatus = ConstantDefine.SYN_STATUS_TRUE;
							} else {
								synStatus = ConstantDefine.SYN_STATUS_FALSE;
							}
						} else {
							synStatus = ConstantDefine.SYN_STATUS_FALSE;
						}
					} else {
						synStatus = ConstantDefine.SYN_STATUS_Buckle;
					}
					
					if (ati.get() == 100)
						ati.set(0);
					cpp.setAti(ati);
//					CommonTool.updateCPParam(cp.getCpId(), cpp);
					CommonTool.updateCPParam(newCpId, cpp);
				} else {
					String result = notityXSResult(cp,ffId,order);
					
					logger.info("给CP的同步结果："+result);
					if (result.equals("ok")) {
						synStatus = ConstantDefine.SYN_STATUS_TRUE;
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				}
			}
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(ffId);
			info.setMobile(reqInfo.getMobile());
			info.setIsSuccess(reqInfo.getIsSuccess());
			info.setSynStatus(synStatus);
			commonAction.updateOrderInfoDate(info);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
	}
	/**
	 * 虚实无联网无透传参数数据同步通知
	 * @param pid
	 * @param order
	 * @return
	 * @throws Exception 
	 */
	private String notityxsWlwWtcResult(CPInfo cp, String pid, OrderReqInfo order) throws Exception {
		
		String result = "error";
		String param = "";
		if (order.getIsSuccess() == 6) {
			order.setIsSuccess(3);
		}
		String content = order.getSmscontent();
		if (content != null && content.length() > 0) {
			content = URLEncoder.encode(content, "utf-8");
		}
		String cpparam = order.getDelayed();
		if (cpparam != null && cpparam.length() > 0) {
			cpparam = URLEncoder.encode(cpparam, "utf-8");
		}
		StringBuilder msg = new StringBuilder();
		msg.append("smsport=").append(order.getSms())
		.append("&smscontent=").append(content)
		.append("&phone=").append(order.getMobile())
		.append("&fee=").append(order.getFee())
		.append("&cpid=").append(order.getCpId())
		.append("&ffid=").append(order.getFfId())
		.append("&cpparam=").append(cpparam)
		.append("&status=").append(order.getIsSuccess());
		param = msg.toString();
		result = HttpTool.sendGetSetTimeout(cp.getUrl(), param, "500");
		return result;
	}

	private String notityXSResult(CPInfo cp, String ffId, OrderReqInfo order) {
		String result = "";
		String param = "";
		String cpId = cp.getCpId();
		String smscontent = order.getSmscontent();
		String smsport = order.getSmsport();
		if (cp.getBackMethod() == 1) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("cpid", cpId);
			map.put("imsi", order.getImsi());
			map.put("imei", order.getImei());
			map.put("ffid", ffId);
			map.put("phone", order.getMobile());
			map.put("cpparam", order.getCpParam());
			map.put("fee", order.getFee()+"");
			map.put("status", order.getIsSuccess()+"");
			map.put("ip", order.getIp());
			if (smsport != null) {
				map.put("smsport", smsport);
			}
			if (smscontent != null) {
				map.put("smscontent", smscontent);
			}
			param = JSON.toJSONString(map);
			result = HttpTool.sendKLWPost(cp.getUrl(), param, "utf-8", 500);
		} else {
			StringBuilder msg = new StringBuilder();
			msg.append("imsi=").append(order.getImsi())
			.append("&cpid=").append(cpId)
			.append("&imei=").append(order.getImei())
			.append("&ffid=").append(ffId)
			.append("&cpparam=").append(order.getCpParam())
			.append("&fee=").append(order.getFee())
			.append("&status=").append(order.getIsSuccess())
			.append("&phone=").append(order.getMobile())
			.append("&ip=").append(order.getIp());
			if (smsport != null) {
				msg.append("&smsport=").append(smsport);
			}
			if (smscontent != null) {
				msg.append("&smscontent=").append(smscontent);
			}
			param = msg.toString();
			result = HttpTool.sendGetSetTimeout(cp.getUrl(), param, "500");
		}
		return result;
	}
	
	private Map<String, String> parseYZ(String content) {
		Map<String, String> params = new HashMap<String, String>();
		try {
			StringTokenizer st = new StringTokenizer(content, "&");
			while (st.hasMoreTokens()) {
				String[] pair = st.nextToken().split("=");
				String key = URLDecoder.decode(pair[0], "UTF-8");
				String value = pair.length == 1 ? null : URLDecoder.decode(
						pair[1], "UTF-8");
				params.put(key, value);
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Couldn't parse query string: "
					+ content, e);
		}
		return params;
	}
	
	
	private boolean checkOrderNum(CPInfo cp) {
		boolean isSyn = false;
		CPParam totalcpp = CommonTool.getCPParam(cp.getCpId(),
				cp.getSynRadio());
		AtomicInteger totalati = totalcpp.getAti();
		int currnum = totalati.incrementAndGet();
		CommonTool.updateCPParam(cp.getCpId(), totalcpp);
		if (currnum<8) {
			isSyn = true;
			logger.info("前8个订单全部同步cpid：" + cp.getCpId() + ",当前第"+currnum+"个订单");
		}
		
		return isSyn;
	}
	
	private MobileInfo getProvinceByPhone(String phone) {
		MobileInfo info = null;
		
		if (phone != null && phone.length() > 7) {
			int mobile = CommonTool.convertInt(phone.substring(0,7));
			info = commonAction.getProvinceByPhone(mobile);
		}
		
		return info;
	}
	
}