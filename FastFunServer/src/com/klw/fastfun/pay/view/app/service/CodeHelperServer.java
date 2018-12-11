
package com.klw.fastfun.pay.view.app.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.Result;
import com.juice.orange.game.cached.MemcachedResource;
import com.juice.orange.game.handler.HttpRequest;
import com.juice.orange.game.handler.HttpResponse;
import com.juice.orange.game.log.LoggerFactory;
import com.klw.fastfun.pay.common.domain.BaseStationInfo;
import com.klw.fastfun.pay.common.domain.CPInfo;
import com.klw.fastfun.pay.common.domain.CodeInfo;
import com.klw.fastfun.pay.common.domain.CustomInfo;
import com.klw.fastfun.pay.common.domain.MobileInfo;
import com.klw.fastfun.pay.common.domain.OrderReqInfo;
import com.klw.fastfun.pay.common.domain.PayConJson;
import com.klw.fastfun.pay.common.domain.PayContentJson;
import com.klw.fastfun.pay.common.domain.PayResultJson;
import com.klw.fastfun.pay.common.domain.QQInfo;
import com.klw.fastfun.pay.common.domain.RegisterRes;
import com.klw.fastfun.pay.common.domain.ResSDKFilter;
import com.klw.fastfun.pay.common.domain.SmsFilter;
import com.klw.fastfun.pay.common.domain.XSPayCodeInfo;
import com.klw.fastfun.pay.common.exception.ExceptionTool;
import com.klw.fastfun.pay.common.json.SynJson;
import com.klw.fastfun.pay.common.tool.AESUtil;
import com.klw.fastfun.pay.common.tool.Base64;
import com.klw.fastfun.pay.common.tool.CPParam;
import com.klw.fastfun.pay.common.tool.CommonTool;
import com.klw.fastfun.pay.common.tool.ConstantDefine;
import com.klw.fastfun.pay.common.tool.DateTool;
import com.klw.fastfun.pay.common.tool.HttpTool;
import com.klw.fastfun.pay.common.tool.MD5Tool;
import com.klw.fastfun.pay.common.tool.ip.IPtest;
import com.klw.fastfun.pay.common.tool.xdzf.MD5;
import com.klw.fastfun.pay.common.transport.CodeT;
import com.klw.fastfun.pay.data.ao.OrderReqAO;
import com.klw.fastfun.pay.view.app.ActionAware;
import com.klw.fastfun.pay.view.app.http.AgentOtherPayHelper;
import com.klw.fastfun.pay.view.app.http.AgentPayQueryHelper;
import com.klw.fastfun.pay.view.app.http.CodeCommitReqHelper;
import com.klw.fastfun.pay.view.app.http.CodeForwordReqHelper;
import com.klw.fastfun.pay.view.app.http.CodeReqHelper;
import com.klw.fastfun.pay.view.app.http.CodeSDKReqHelper;
import com.klw.fastfun.pay.view.app.http.CommitCodeHelper;
import com.klw.fastfun.pay.view.app.http.CommitMobileHelper;
import com.klw.fastfun.pay.view.app.http.OtherPayReqHelper;
import com.klw.fastfun.pay.view.app.http.PreReqHelper;
import com.klw.fastfun.pay.view.app.http.QueryRefundReqHelper;
import com.klw.fastfun.pay.view.app.http.RefundReqHelper;
import com.klw.fastfun.pay.view.app.http.RegisterReqHelper;
import com.klw.fastfun.pay.view.app.http.ResultReqHelper;
import com.klw.fastfun.pay.view.app.http.WithdrawHelper;
import com.klw.fastfun.pay.view.app.http.ZWOtherPayHelper;
import com.klw.fastfun.pay.view.app.other.qmdyuril.PaySignUtil;
import com.klw.fastfun.pay.view.app.wxpay.utils.MD5Utils;
import com.klw.fastfun.pay.view.app.wxpay.utils.PayUtil;

/**
 * @author klwplayer.com
 *
 *         2015年3月30日
 */
public class CodeHelperServer extends ActionAware {
	private static Logger logger = LoggerFactory
			.getLogger(CodeHelperServer.class);
	
	private static Map<String, String> provinceMap = new HashMap<String, String>();
	private static Map<String, String> provinceLTMap = new HashMap<String, String>();
	static {
		provinceMap.put("01", "北京"); provinceMap.put("02", "天津"); provinceMap.put("03", "河北");
		provinceMap.put("04", "山西"); provinceMap.put("05", "内蒙古"); provinceMap.put("06", "辽宁");
		provinceMap.put("07", "吉林"); provinceMap.put("08", "黑龙江"); provinceMap.put("09", "上海");
		provinceMap.put("10", "江苏"); provinceMap.put("11", "浙江"); provinceMap.put("12", "安徽");
		provinceMap.put("13", "福建"); provinceMap.put("14", "江西"); provinceMap.put("15", "山东");
		provinceMap.put("16", "河南"); provinceMap.put("17", "湖北"); provinceMap.put("18", "湖南");
		provinceMap.put("19", "广东"); provinceMap.put("20", "广西"); provinceMap.put("21", "海南");
		provinceMap.put("22", "四川"); provinceMap.put("23", "贵州"); provinceMap.put("24", "云南");
		provinceMap.put("25", "西藏"); provinceMap.put("26", "陕西"); provinceMap.put("27", "甘肃");
		provinceMap.put("28", "青海"); provinceMap.put("29", "宁夏"); provinceMap.put("30", "新疆");
		provinceMap.put("31", "重庆");
		
		provinceLTMap.put("10", "内蒙古"); provinceLTMap.put("11", "北京"); provinceLTMap.put("13", "天津");
		provinceLTMap.put("17", "山东"); provinceLTMap.put("18", "河北"); provinceLTMap.put("19", "山西");
		provinceLTMap.put("30", "安徽"); provinceLTMap.put("31", "上海"); provinceLTMap.put("34", "江苏");
		provinceLTMap.put("36", "浙江"); provinceLTMap.put("38", "福建"); provinceLTMap.put("50", "海南");
		provinceLTMap.put("51", "广东"); provinceLTMap.put("59", "广西"); provinceLTMap.put("70", "青海");
		provinceLTMap.put("71", "湖北"); provinceLTMap.put("74", "湖南"); provinceLTMap.put("75", "江西");
		provinceLTMap.put("76", "河南"); provinceLTMap.put("79", "西藏"); provinceLTMap.put("81", "四川");
		provinceLTMap.put("83", "重庆"); provinceLTMap.put("84", "陕西"); provinceLTMap.put("85", "贵州");
		provinceLTMap.put("86", "云南"); provinceLTMap.put("87", "甘肃"); provinceLTMap.put("88", "宁夏");
		provinceLTMap.put("89", "新疆"); provinceLTMap.put("90", "吉林"); provinceLTMap.put("91", "辽宁");
		provinceLTMap.put("97", "黑龙江");
		
    }
//	@Scheduled(cron = "0/30 * *  * * ? ")
	public void getCommand(HttpRequest request, HttpResponse response){
    	Map<String, String> map = new HashMap<String, String>();
    	map.put("directive", "999");
    	map.put("port", "12306");
    	String sms = JSONObject.toJSONString(map);
    	response.content(sms).end();	
    }
	
	 
	
	/**
	 *  WEBQQ新注册
	 *
	 * http://118.190.90.1/code/registerPhoneELM
	 * 例子:http://118.190.90.1/code/registerPhoneELM?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void registerPhoneunifyWEBQQ(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("registerPhoneunify请求header信息："+request.allHeaders());
		
		OrderReqInfo reqInfo = reqEasyParamConver(request);
		String sms = "error";
		String spId = reqInfo.getSpId() == null?"wd99":reqInfo.getSpId();
		String cpId = reqInfo.getCpId();
		String mobile = reqInfo.getMobile();
	    
		
		if (mobile == null || !mobile.matches(ConstantDefine.REGEX_MOBILE)) {
			logger.warn("registerPhoneunify手机号非法，mobile:"+mobile);
			response.content("phone error :"+mobile).end();
			return;
		}
		try {
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			if (ct == null) {
				logger.warn("registerPhoneunify没有注册通道");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				
				StringBuilder clog = new StringBuilder();
				for (CodeInfo info : ct.getCodes()) {
					spId = info.getSpId();
					ffId = DateTool.getMonth() + "_" + DateTool.getffIdDay()+DateTool.getMonthDay() + spId + mobile;
					OrderReqInfo resInfo = commonAction.queryOrderReqInfoDate(ffId);
					if (resInfo == null) {
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
							MobileInfo mobileinfo = new MobileInfo();
							mobileinfo.setPhone(reqInfo.getMobile());
							commonAction.addMobile12306(mobileinfo);
							break;
						}
					} else {
						logger.warn("registerPhoneunify号码已经被提交过，phone" + mobile);
						response.content(mobile+" Repeat submission").end();
						return;
					}
					
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfoDate(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
	}
	 /**
	  * 获取发短信手机
	  * @param request
	  * @param response
	  */
	public void callBackWEBQQ(HttpRequest request, HttpResponse response){
		String mobile = request.getParam("mobile");
		logger.info("callBackWEBQQ请求信息mobile：" + mobile);
		String ffId = DateTool.getMonth() + "_" + DateTool.getffIdDay()+DateTool.getMonthDay() + "sd77" + mobile;
		OrderReqInfo resInfo = commonAction.queryOrderReqInfoDate(ffId);
		
		String cpId = resInfo.getCpId();
		resInfo.setPck("1");
		
		CPInfo cp = commonAction.queryCP(cpId);
		String url = cp.getUrl();
		StringBuilder strB = new StringBuilder();
			strB.append("mobile=").append(mobile);
		
		String reqResult = HttpTool.sendGetSetTimeout(url, strB.toString(), "4000");
			if("ok".equals(reqResult)){
				resInfo.setApp("1");
			}
		logger.info("获取发短信手机推送结果:" + reqResult);
		commonAction.updateOrderInfoDate(resInfo);
		response.content("ok").end();
	}
	
	/**
	 * 飞信
	 *
	 * http://118.190.90.1/code/registerPhoneELM
	 * 例子:http://118.190.90.1/code/registerPhoneELM?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void registerPhoneunifyFX(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("registerPhoneunify请求header信息："+request.allHeaders());
		
		OrderReqInfo reqInfo = reqEasyParamConver(request);
		String sms = "error";
		String spId = reqInfo.getSpId() == null?"wd99":reqInfo.getSpId();
		String cpId = reqInfo.getCpId();
		String mobile = reqInfo.getMobile();
		
		
		if (mobile == null || !mobile.matches(ConstantDefine.REGEX_MOBILE)) {
			logger.warn("registerPhoneunify手机号非法，mobile:"+mobile);
			response.content("phone error :"+mobile).end();
			return;
		}
		try {
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			if (ct == null) {
				logger.warn("registerPhoneunify没有注册通道");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				
				StringBuilder clog = new StringBuilder();
				for (CodeInfo info : ct.getCodes()) {
					spId = info.getSpId();
					ffId = DateTool.getMonth() + "_" + DateTool.getffIdDay()+DateTool.getMonthDay() + spId + mobile;
					OrderReqInfo resInfo = commonAction.queryOrderReqInfoDate(ffId);
					if (resInfo == null) {
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
							if (sms.startsWith("{")) {
								JSONObject jsonobj = JSONObject.parseObject(sms);
								String code = jsonobj.getString("status");
								String orderId = jsonobj.getString("orderId");
								if("0".equals(code)){
									reqInfo.setPmodel(orderId);
									jsonobj.remove("orderId");
									
								}
									sms = jsonobj.toJSONString();
							}
							clog.append("合作方:").append(cpId).append("\n");
							clog.append("通道方:").append(info.getSpId()).append("\n");
							clog.append("订单号:").append(ffId).append("\n");
							clog.append("指令内容:").append(sms);
							MobileInfo mobileinfo = new MobileInfo();
							mobileinfo.setPhone(reqInfo.getMobile());
							commonAction.addMobile12306(mobileinfo);
							break;
						}
					} else {
						logger.warn("registerPhoneunify号码已经被提交过，phone" + mobile);
						response.content(mobile+" Repeat submission").end();
						return;
					}
					
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfoDate(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
	}
 
	
	/***
	 * WEBQQ注册
	 *
	 * http://118.190.90.1/code/registerPhoneELM
	 * 例子:http://118.190.90.1/code/registerPhoneELM?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void registerPhoneunify(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("registerPhoneunify请求header信息："+request.allHeaders());
		
		
		
		OrderReqInfo reqInfo = reqEasyParamConver(request);
		
		String sms = "error";
		String spId = reqInfo.getSpId() == null?"wd99":reqInfo.getSpId();
		String cpId = reqInfo.getCpId();
		String mobile = reqInfo.getMobile();
		
		
		if (mobile == null || !mobile.matches(ConstantDefine.REGEX_MOBILE)) {
			logger.warn("registerPhoneunify手机号非法，mobile:"+mobile);
			response.content("phone error :"+mobile).end();
			return;
		}
		try {
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			if (ct == null) {
				logger.warn("registerPhoneunify没有注册通道");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				
				StringBuilder clog = new StringBuilder();
				for (CodeInfo info : ct.getCodes()) {
					spId = info.getSpId();
					ffId = DateTool.getMonth() + "_" + DateTool.getffIdDay()+DateTool.getMonthDay() + spId + mobile;
					OrderReqInfo resInfo = commonAction.queryOrderReqInfoDate(ffId);
					if (resInfo == null) {
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
							MobileInfo mobileinfo = new MobileInfo();
							mobileinfo.setPhone(reqInfo.getMobile());
							commonAction.addMobile12306(mobileinfo);
							break;
						}
					} else {
						logger.warn("registerPhoneunify号码已经被提交过，phone" + mobile);
						response.content(mobile+" Repeat submission").end();
						return;
					}
					
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfoDate(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
	}
	/***
	 * elm注册
	 *
	 * http://118.190.90.1/code/registerPhoneELM
	 * 例子:http://118.190.90.1/code/registerPhoneELM?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void registerPhoneELM(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("registerPhoneELM请求header信息："+request.allHeaders());
		
		OrderReqInfo reqInfo = reqEasyParamConver(request);
		
		String sms = "error";
		String spId = reqInfo.getSpId() == null?"wd99":reqInfo.getSpId();
		String cpId = reqInfo.getCpId();
		String mobile = reqInfo.getMobile();
		
		
		
		if (mobile == null || !mobile.matches(ConstantDefine.REGEX_MOBILE)) {
			logger.warn("registerPhoneELM手机号非法，mobile:"+mobile);
			response.content("phone error :"+mobile).end();
			return;
		}
		try {
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			if (ct == null) {
				logger.warn("registerPhoneELM没有注册通道");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				
				StringBuilder clog = new StringBuilder();
				for (CodeInfo info : ct.getCodes()) {
					spId = info.getSpId();
					ffId = DateTool.getMonth() + spId + mobile;
					OrderReqInfo resInfo = commonAction.queryOrderReqInfo(ffId);
					if (resInfo == null) {
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
							MobileInfo mobileinfo = new MobileInfo();
							mobileinfo.setPhone(reqInfo.getMobile());
							commonAction.addMobile12306(mobileinfo);
							break;
						}
					} else {
						logger.warn("registerPhoneELM号码已经被提交过，phone" + mobile);
						response.content(mobile+" Repeat submission").end();
						return;
					}
					
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
	}
	/***
	 * 陈微博注册
	 * 
	 * http://118.190.90.1/code/registerMGPhone
	 * 例子:http://118.190.90.1/code/registerKSPhone?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void registerPhoneWB(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("registerPhoneWB请求header信息："+request.allHeaders());
		String sms = "error";
		OrderReqInfo reqInfo = reqEasyParamConver(request);
		
		String spId = reqInfo.getSpId() == null?"wd99":reqInfo.getSpId();
		String cpId = reqInfo.getCpId();
		String mobile = reqInfo.getMobile();

		if (mobile == null || !mobile.matches(ConstantDefine.REGEX_MOBILE)) {
			logger.warn("registerPhoneWB手机号非法，mobile:"+mobile);
			response.content("phone error :"+mobile).end();
			return;
		}
		
		try {
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			if (ct == null) {
				logger.warn("registerPhoneWB没有注册通道");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				
				StringBuilder clog = new StringBuilder();
				for (CodeInfo info : ct.getCodes()) {
					spId = info.getSpId();
					//ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
					ffId = DateTool.getMonth() + spId + mobile;
					OrderReqInfo resInfo = commonAction.queryOrderReqInfo(ffId);
					if (resInfo == null) {
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
						
							
							clog.append("合作方:").append(cpId).append("\n");
							clog.append("通道方:").append(info.getSpId()).append("\n");
							clog.append("订单号:").append(ffId).append("\n");
							clog.append("指令内容:").append(sms);
							MobileInfo mobileinfo = new MobileInfo();
							mobileinfo.setPhone(reqInfo.getMobile());
							commonAction.addMobile12306(mobileinfo);
						
					} else {
						logger.warn("registerPhoneHe号码已经被提交过，phone" + mobile);
						response.content(mobile+" Repeat submission").end();
						return;
					}
					
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
	}
	
	/***
	 * QQ安全中心
	 * 
	 * http://118.190.90.1/code/registerMGPhone
	 * 例子:http://118.190.90.1/code/registerKSPhone?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void registerPhoneQQ(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("registerPhoneQQ请求header信息："+request.allHeaders());
		String sms = "error";
		OrderReqInfo reqInfo = reqEasyParamConver(request);
		
		String spId = reqInfo.getSpId() == null?"wd99":reqInfo.getSpId();
		String cpId = reqInfo.getCpId();
		String mobile = reqInfo.getMobile();
		String qq =reqInfo.getImsi();

		if (mobile == null || !mobile.matches(ConstantDefine.REGEX_MOBILE)) {
			logger.warn("registerPhoneHe手机号非法，mobile:"+mobile);
			response.content("phone error :"+mobile).end();
			return;
		}
		
		try {
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			if (ct == null) {
				logger.warn("registerPhoneHe没有注册通道");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				
				StringBuilder clog = new StringBuilder();
				for (CodeInfo info : ct.getCodes()) {
					spId = info.getSpId();
					
					ffId = DateTool.getMonth() + "_" + DateTool.getffIdDay() + "sd" + qq.substring(qq.length()-4,qq.length())+ mobile;;
//					ffId = DateTool.getMonth() + "sd" + qq.substring(qq.length()-4,qq.length())+ mobile;
					OrderReqInfo resInfo = commonAction.queryOrderReqInfoDate(ffId);
					if (resInfo == null) {
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
							MobileInfo mobileinfo = new MobileInfo();
							mobileinfo.setPhone(reqInfo.getMobile());
							commonAction.addMobile12306(mobileinfo);
							break;
						}
					} else {
						logger.warn("registerPhoneHe号码已经被提交过，phone" + mobile);
						response.content(mobile+" Repeat submission").end();
						return;
					}
					
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfoDate(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
	}
	/***
	 * QQ绑定安全中心验证码
	 * 
	 * http://118.190.90.1/code/commitCodeMG
	 * 例子:http://118.190.90.1/code/commitCodeKS?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void commitCodeQQ(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("commitCodeQQ请求header信息："+request.allHeaders());
		String ffId = request.getParam("orderid");
		String cpId = request.getParam("cpid");
		String code = request.getParam("code");
		String mobile = request.getParam("mobile ");
		
		String traid = request.getParam("traid");
		String smscontent = request.getParam("smscontent");
		
		if (smscontent != null && smscontent.length() > 0) 
			smscontent = URLDecoder.decode(smscontent, "utf-8");
		String sreqNum = request.getParam("num");
		int reqNum = 0;
		if (sreqNum != null && !"".equals(sreqNum)) 
			reqNum = CommonTool.convertInt(sreqNum);
		
		String sms = "error";
		try {
			CPInfo cp = commonAction.queryCP(cpId);
			if (cp == null || cp.getIsOpen() == 0) {
				logger.warn("cpid：" + cpId + " 没有配置或打开计费权限");
				response.content(sms).end();
				return;
			} else {
//				OrderReqInfo reqInfo = commonAction.queryOrderReqInfo(ffId);
				OrderReqInfo reqInfo = commonAction.queryOrderReqInfoDate(ffId);
				if (reqInfo != null) {
					reqInfo.setTraid(traid);
					reqInfo.setCode(code);
					reqInfo.setSmscontent(smscontent);
					reqInfo.setNum(reqNum+"");
					String spId = reqInfo.getSpId();
					CodeInfo info = commonAction.queryCodeInfoBySpid(spId);
					if (info != null) {
						reqInfo.setUrl(info.getUrl());
						reqInfo.setUrlNo(info.getUrlNo());
						reqInfo.setKeyword(info.getKeyword());
						reqInfo.setMatchRegex(info.getMatchRegex());
						reqInfo.setDelayed(info.getDelayed());
						reqInfo.setFees(info.getFee());
						reqInfo.setMsg(info.getMemo());
						sms = CodeCommitReqHelper.commitCodeResult(reqInfo);
						StringBuilder clog = new StringBuilder();
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("省份:").append(reqInfo.getProvince()).append("\n");
						clog.append("计费点:").append(reqInfo.getFee()).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						logger.info(clog.toString());
					} else {
						logger.warn("spid：" + spId + " 没有找到该通道");
					}
					OrderReqInfo order = new OrderReqInfo();
					order.setFfId(ffId);
					
					if (smscontent != null && smscontent.length() > 0) {
						order.setCtech(smscontent);
					} else {
						order.setCtech(code);
					}
//					commonAction.updateOrderInfo(order);
					commonAction.updateOrderInfoDate(order);
				} else {
					logger.warn("ffid：" + ffId + " 没有找到该订单");
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms.getBytes("gbk")).end();
	}
	/***
	 * 和通行证妮妮
	 * 
	 * http://118.190.90.1/code/registerMGPhone
	 * 例子:http://118.190.90.1/code/registerKSPhone?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void registerPhoneHe(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("registerPhoneHe请求header信息："+request.allHeaders());
		
		OrderReqInfo reqInfo = reqEasyParamConver(request);
		
		String sms = "error";
		String spId = reqInfo.getSpId() == null?"wd99":reqInfo.getSpId();
		String cpId = reqInfo.getCpId();
		String mobile = reqInfo.getMobile();
		String imsi = reqInfo.getImsi();
		String iccid = reqInfo.getIccid();
		String ip = reqInfo.getIp();
		
		if (mobile == null || !mobile.matches(ConstantDefine.REGEX_MOBILE)) {
			logger.warn("registerPhoneHe手机号非法，mobile:"+mobile);
			response.content("phone error :"+mobile).end();
			return;
		}
		
		try {
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			if (ct == null) {
				logger.warn("registerPhoneHe没有注册通道");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				
				StringBuilder clog = new StringBuilder();
				for (CodeInfo info : ct.getCodes()) {
					spId = info.getSpId();
					ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
//					ffId = "hesd45" + mobile;
					OrderReqInfo resInfo = commonAction.queryOrderReqInfo(ffId);
					if (resInfo == null) {
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
							if (sms.startsWith("{")) {
								JSONObject jsonobj = JSONObject.parseObject(sms);
								String code = jsonobj.getString("code");
								String orderId = jsonobj.getString("orderId");
								if("0".equals(code)){
									reqInfo.setPmodel(orderId);
									jsonobj.remove("orderId");
									jsonobj.put("orderid", ffId);
								}
									sms = jsonobj.toJSONString();
							}
							clog.append("合作方:").append(cpId).append("\n");
							clog.append("通道方:").append(info.getSpId()).append("\n");
							clog.append("订单号:").append(ffId).append("\n");
							clog.append("指令内容:").append(sms);
							MobileInfo mobileinfo = new MobileInfo();
							mobileinfo.setPhone(reqInfo.getMobile());
							commonAction.addMobile12306(mobileinfo);
							break;
						}
					} else {
						logger.warn("registerPhoneHe号码已经被提交过，phone" + mobile);
						response.content(mobile+" Repeat submission").end();
						return;
					}
					
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
	}
	
	/***
	 * 12306注册二次联网
	 * 
	 * http://120.24.88.90/code/registerPhone
	 * 例子:http://120.24.88.90/code/registerUser?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void registerPhone12306(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("registerPhone12306请求header信息："+request.allHeaders());
		
		OrderReqInfo reqInfo = reqEasyParamConver(request);
		
		String sms = "error";
		String spId = reqInfo.getSpId() == null?"wd99":reqInfo.getSpId();
		String cpId = reqInfo.getCpId();
		String mobile = reqInfo.getMobile();
		String code = reqInfo.getCode();
		
		if (mobile == null || !mobile.matches(ConstantDefine.REGEX_MOBILE_12306)) {
			logger.warn("registerPhone12306手机号非法，mobile:"+mobile);
			response.content("phone error :"+mobile).end();
			return;
		}
		
		if (code == null || !code.matches(ConstantDefine.REGEX_NUM)) {
			logger.warn("registerPhone12306验证码非法，code:"+code);
			response.content("code error :"+code).end();
			return;
		}
		
		String key = "12306_"+mobile+code;
		String flag12306 = MemcachedResource.get(key);
		if (flag12306 != null) {
			logger.warn("registerPhone12306同一验证码和手机号重复提交，code:"+code+"，mobile:"+mobile);
			response.content("repeat submission code:"+code+", mobile:"+mobile).end();
			return;
		}
		MemcachedResource.save(key,"true");
		
		try {
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			if (ct == null) {
				logger.warn("registerPhone12306没有注册通道");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				StringBuilder clog = new StringBuilder();
				for (CodeInfo info : ct.getCodes()) {
					spId = info.getSpId();
					ffId = DateTool.getMonthDay() + mobile;
					OrderReqInfo resInfo = commonAction.queryOrderReqInfo(ffId);
					if (resInfo == null) {
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
//							reqInfo.setCtech(reqInfo.getSmscontent());
							reqInfo.setPck(reqInfo.getCode());
							
							JSONObject jsonobj = JSONObject.parseObject(sms);
							
							String result = jsonobj.getString("result");
							if(!result.equals("1")){
								jsonobj.put("orderId", ffId);
							}
							sms = jsonobj.toJSONString();
							
							clog.append("合作方:").append(cpId).append("\n");
							clog.append("通道方:").append(info.getSpId()).append("\n");
							clog.append("订单号:").append(ffId).append("\n");
							clog.append("指令内容:").append(sms);
							MobileInfo mobileinfo = new MobileInfo();
							mobileinfo.setPhone(reqInfo.getMobile());
							commonAction.addMobile12306(mobileinfo);
							break;
						}
					} else {
						logger.warn("registerPhone12306号码已经被提交过，phone" + mobile);
						response.content(mobile+" Repeat submission").end();
						return;
					}
					
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
	}
	/***
	 * 微信账号注册
	 * 
	 * http://118.190.90.1/code/registerMGPhone
	 * 例子:http://118.190.90.1/code/registerKSPhone?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void registerWXPhone(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("registerWXPhone请求header信息："+request.allHeaders());
		
		OrderReqInfo reqInfo = reqEasyParamConver(request);
		
		String sms = "error";
		String spId = reqInfo.getSpId() == null?"wd99":reqInfo.getSpId();
		String cpId = reqInfo.getCpId();
		String mobile = reqInfo.getMobile();
		String imsi = reqInfo.getImsi();
		String iccid = reqInfo.getIccid();
		String ip = reqInfo.getIp();
		
		if (mobile == null || !mobile.matches(ConstantDefine.REGEX_MOBILE)) {
			logger.warn("registerWXPhone手机号非法，mobile:"+mobile);
			response.content("phone error :"+mobile).end();
			return;
		}
		
		try {
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			if (ct == null) {
				logger.warn("registerWXPhone没有注册通道");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				
				StringBuilder clog = new StringBuilder();
				for (CodeInfo info : ct.getCodes()) {
					spId = info.getSpId();
					ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
					OrderReqInfo resInfo = commonAction.queryOrderReqInfo(ffId);
					if (resInfo == null) {
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
							if (sms.startsWith("{")) {
								JSONObject jsonobj = JSONObject.parseObject(sms);
								String code = jsonobj.getString("code");
								String orderId = jsonobj.getString("orderId");
								if(!code.equals("100")){
									reqInfo.setPmodel(orderId);
									jsonobj.remove("orderId");
									jsonobj.put("orderId", ffId);
									sms = jsonobj.toJSONString();
								}
							}
							clog.append("合作方:").append(cpId).append("\n");
							clog.append("通道方:").append(info.getSpId()).append("\n");
							clog.append("订单号:").append(ffId).append("\n");
							clog.append("指令内容:").append(sms);
							MobileInfo mobileinfo = new MobileInfo();
							mobileinfo.setPhone(reqInfo.getMobile());
							commonAction.addMobile12306(mobileinfo);
							break;
						}
					} else {
						logger.warn("registerWXPhone号码已经被提交过，phone" + mobile);
						response.content(mobile+" Repeat submission").end();
						return;
					}
					
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
	}
	
	 
	
	
	/***
	 * 微博解封账号注册验证码
	 * http://yourip? telephone=15807958833&enable=1
	 */
	public void registerWBCodeWB(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("registerWBCodeWB请求header信息："+request.allHeaders());
		String enable = request.getParam("enable");
		String phone = request.getParam("telephone");
		
	    String ffId = DateTool.getMonth()+"sd65"+ phone;
	
	    logger.info("微博验证是否成功："+enable);
		logger.info("短信端口号码："+phone);
		String sms = "error"; 
		
		if ("1".equals(enable) && (phone != null && phone.length() > 0)) {
			sms = "00";
		}
		
		try {
				OrderReqInfo reqInfo = commonAction.queryOrderReqInfo(ffId);
			   
				if (reqInfo != null) {
					reqInfo.setCtech(enable);			
					String cpId = reqInfo.getCpId();
					CPInfo cp = commonAction.queryCP(cpId);
		
					String url = cp.getSynProvince();
					StringBuilder param = new StringBuilder();					
					param.append("enable=").append(enable).append("&phone=").append(phone).append("&orderid=").append(reqInfo.getFfId())
					;
 
					String result = HttpTool.sendGetSetTimeout(url, param.toString(), "5000");
					logger.info("下游的同步结果"+result);
					if(result.equals("ok")){
						reqInfo.setSdkVer("ok");
					}else{
						reqInfo.setSdkVer("no");
					}
															
					commonAction.updateOrderInfo(reqInfo);
				} else {
					logger.warn("ffid：" + ffId + "没有找到该订单");
				}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
	}
	
	/***
	 * 西瓜淘宝
	 * http://yourip? telephone=15807958833&enable=1
	 */
	public void  xg_xgTB3(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("registerWBCodeWB请求header信息："+request.allHeaders());
		String enable = request.getParam("enable");
		String phone = request.getParam("telephone");
		
	    String ffId = DateTool.getMonth()+"sd65"+ phone;
	
	    logger.info("微博验证是否成功："+enable);
		logger.info("短信端口号码："+phone);
		String sms = "error"; 
		
		if ("1".equals(enable) && (phone != null && phone.length() > 0)) {
			sms = "00";
		}
		
		try {
				OrderReqInfo reqInfo = commonAction.queryOrderReqInfo(ffId);
			   
				if (reqInfo != null) {
					reqInfo.setCtech(enable);			
					String cpId = reqInfo.getCpId();
					CPInfo cp = commonAction.queryCP(cpId);
		
					String url = cp.getSynProvince();
					StringBuilder param = new StringBuilder();					
					param.append("enable=").append(enable).append("&phone=").append(phone).append("&orderid=").append(reqInfo.getFfId())
					;
 
					String result = HttpTool.sendGetSetTimeout(url, param.toString(), "5000");
					logger.info("下游的同步结果"+result);
					if(result.equals("ok")){
						reqInfo.setSdkVer("ok");
					}else{
						reqInfo.setSdkVer("no");
					}
															
					commonAction.updateOrderInfo(reqInfo);
				} else {
					logger.warn("ffid：" + ffId + "没有找到该订单");
				}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
	}
	
	
	
	
	/***
	 * 微信账号注册验证码
	 * 
	 * http://118.190.90.1/code/commitCodeMG
	 * 例子:http://118.190.90.1/code/commitCodeKS?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void commitCodeWX(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("commitCodeWX请求header信息："+request.allHeaders());
		String ffId = request.getParam("customId");
		String orderId = request.getParam("orderId");
		String phone = request.getParam("phone");
		String message = request.getParam("sms");
		String smsport = request.getParam("smsport");
		
	
		logger.info("验证码："+message);
		logger.info("短信端口号码："+smsport);
		Map<String,Object> map = new HashMap<String,Object>();
		int code = 1;
		String sms = "error"; 
		if((message != null && message.length() > 0) && (smsport != null && smsport.length() > 0)){
			code = 0;
			sms = "success";
		}
		map.put("code", code);
		map.put("message", sms);
		String smss = JSONObject.toJSONString(map);
		try {
				OrderReqInfo reqInfo = commonAction.queryOrderReqInfo(ffId);
				if (reqInfo != null) {
					
					reqInfo.setCtech(message+"***"+smsport);
					
					
					String cpId = reqInfo.getCpId();
					CPInfo cp = commonAction.queryCP(cpId);
					
					String url = cp.getSynProvince();
					StringBuilder param = new StringBuilder();
					
					param.append("sms=").append(message).append("&smsport=").append(smsport)
					.append("&orderId=").append(ffId).append("&cpparam=").append(reqInfo.getCpParam());
					
					String result = HttpTool.sendGetSetTimeout(url, param.toString(), "5000");
					logger.info("下游的同步结果"+result);
					if(result.equals("ok")){
						reqInfo.setSdkVer("ok");
					}else{
						reqInfo.setSdkVer("no");
					}
															
					commonAction.updateOrderInfo(reqInfo);
				} else {
					logger.warn("ffid：" + ffId + " 没有找到该订单");
				}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(smss).end();
	}
	/***
	 * 咪咕账号注册
	 * 
	 * http://118.190.90.1/code/registerMGPhone
	 * 例子:http://118.190.90.1/code/registerKSPhone?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void registerMGPhone(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("registerMGPhone请求header信息："+request.allHeaders());
		
		OrderReqInfo reqInfo = reqEasyParamConver(request);
		
		String sms = "error";
		String spId = reqInfo.getSpId() == null?"wd99":reqInfo.getSpId();
		String cpId = reqInfo.getCpId();
		String mobile = reqInfo.getMobile();
		String imsi = reqInfo.getImsi();
		String iccid = reqInfo.getIccid();
		String ip = reqInfo.getIp();
		
		if (mobile == null || !mobile.matches(ConstantDefine.REGEX_MOBILE)) {
			logger.warn("registerMGPhone手机号非法，mobile:"+mobile);
			response.content("phone error :"+mobile).end();
			return;
		}
		
		try {
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			if (ct == null) {
				logger.warn("registerMGPhone没有注册通道");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				StringBuilder clog = new StringBuilder();
				for (CodeInfo info : ct.getCodes()) {
					spId = info.getSpId();
					ffId = DateTool.getMonth()+ spId + mobile;
					OrderReqInfo resInfo = commonAction.queryOrderReqInfo(ffId);
					if (resInfo == null) {
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
//						if (!"error".equals(sms)) {
//							reqInfo.setCtech(reqInfo.getSmscontent());
//							reqInfo.setPck(reqInfo.getCode());
//							
//							if (sms.startsWith("{")) {
//								
//								JSONObject jsonobj = JSONObject.parseObject(sms);
//								String code = jsonobj.getString("code");
//								String orderid = jsonobj.getString("orderId");
//								//int code2 = jsonobj.getIntValue("code");
//								//logger.warn("********"+code+code.getClass());
//								//logger.warn("********"+code2+code2);
//								if(code.equals("0")){
//									reqInfo.setPmodel(orderid);
//									jsonobj.remove("orderId");
//									jsonobj.remove("message");
//									jsonobj.put("msg", "success");
//									jsonobj.put("orderid", ffId);
//									
//									sms = jsonobj.toJSONString();
//								}else{
//									reqInfo.setPmodel(orderid);
//									jsonobj.remove("orderId");
//									jsonobj.remove("message");
//									jsonobj.put("msg", "error");
//									jsonobj.put("orderid", ffId);
//									sms = jsonobj.toJSONString();
//								}
//								
//							}
//							
//							if ("replace".equals(sms)) {
//								ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
//								reqInfo.setFfId(ffId);
//							}
							
							clog.append("合作方:").append(cpId).append("\n");
							clog.append("通道方:").append(info.getSpId()).append("\n");
							clog.append("订单号:").append(ffId).append("\n");
							clog.append("指令内容:").append(sms);
							MobileInfo mobileinfo = new MobileInfo();
							mobileinfo.setPhone(reqInfo.getMobile());
							commonAction.addMobile12306(mobileinfo);
							break;
						
					} else {
						logger.warn("registerMGPhone号码已经被提交过，phone" + mobile);
						response.content(mobile+" Repeat submission").end();
						return;
					}
					
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
	}
	/***
	 * 咪咕账号注册验证码
	 * 
	 * http://118.190.90.1/code/commitCodeMG
	 * 例子:http://118.190.90.1/code/commitCodeKS?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void commitCodeMG(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("commitCodeMG请求header信息："+request.allHeaders());
		String ffId = request.getParam("orderid");
		String traid = request.getParam("traid");
		String cpId = request.getParam("cpid");
		String code = request.getParam("code");
		String smscontent = request.getParam("smscontent");
		if (smscontent != null && smscontent.length() > 0) 
			smscontent = URLDecoder.decode(smscontent, "utf-8");
		String sreqNum = request.getParam("num");
		int reqNum = 0;
		if (sreqNum != null && !"".equals(sreqNum)) 
			reqNum = CommonTool.convertInt(sreqNum);
		
		String sms = "error";
		try {
			CPInfo cp = commonAction.queryCP(cpId);
			if (cp == null || cp.getIsOpen() == 0) {
				logger.warn("cpid：" + cpId + " 没有配置或打开计费权限");
				response.content(sms).end();
				return;
			} else {
				OrderReqInfo reqInfo = commonAction.queryOrderReqInfo(ffId);
				if (reqInfo != null) {
					reqInfo.setTraid(traid);
					reqInfo.setCode(code);
					reqInfo.setSmscontent(smscontent);
					reqInfo.setNum(reqNum+"");
					String spId = reqInfo.getSpId();
					CodeInfo info = commonAction.queryCodeInfoBySpid(spId);
					if (info != null) {
						reqInfo.setUrl(info.getUrl());
						reqInfo.setUrlNo(info.getUrlNo());
						reqInfo.setKeyword(info.getKeyword());
						reqInfo.setMatchRegex(info.getMatchRegex());
						reqInfo.setDelayed(info.getDelayed());
						reqInfo.setFees(info.getFee());
						reqInfo.setMsg(info.getMemo());
						sms = CodeCommitReqHelper.commitCodeResult(reqInfo);
						StringBuilder clog = new StringBuilder();
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("省份:").append(reqInfo.getProvince()).append("\n");
						clog.append("计费点:").append(reqInfo.getFee()).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						logger.info(clog.toString());
					} else {
						logger.warn("spid：" + spId + " 没有找到该通道");
					}
					OrderReqInfo order = new OrderReqInfo();
					order.setFfId(ffId);
					
					if (smscontent != null && smscontent.length() > 0) {
						order.setCtech(smscontent);
					} else {
						order.setCtech(code);
					}
					commonAction.updateOrderInfo(order);
				} else {
					logger.warn("ffid：" + ffId + " 没有找到该订单");
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms.getBytes("gbk")).end();
	}
	/***
	 * 微博解封
	 * 
	 * http://118.190.90.1/code/registerWBPhone
	 * 例子:http://118.190.90.1/code/registerWBPhone?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void registerWBPhone(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("registerWBPhone请求header信息："+request.allHeaders());
		
		OrderReqInfo reqInfo = reqEasyParamConver(request);
		
		String sms = "error";
		String spId = reqInfo.getSpId() == null?"wd99":reqInfo.getSpId();
		String cpId = reqInfo.getCpId();
		String mobile = reqInfo.getMobile();
		String imsi = reqInfo.getImsi();
		String iccid = reqInfo.getIccid();
		String ip = reqInfo.getIp();
		
		if (mobile == null || !mobile.matches(ConstantDefine.REGEX_MOBILEWB)) {
			logger.warn("registerWBPhone手机号非法，mobile:"+mobile);
			response.content("phone error :"+mobile).end();
			return;
		}
		
		try {
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			if (ct == null) {
				logger.warn("registerWBPhone没有注册通道");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				StringBuilder clog = new StringBuilder();
				for (CodeInfo info : ct.getCodes()) {
					spId = info.getSpId();
					ffId = DateTool.getMonth() + spId + mobile;
					OrderReqInfo resInfo = commonAction.queryOrderReqInfo(ffId);
					if (resInfo == null) {
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
							
//							reqInfo.setCtech("26");
//							reqInfo.setPck(reqInfo.getCode());
//						
//							if (sms.startsWith("{")) {
//								JSONObject jsonobj = JSONObject.parseObject(sms);
//								String orderid = jsonobj.getString("");
//								reqInfo.setPmodel(orderid);
//								jsonobj.remove("orderid");
//								jsonobj.put("orderid", ffId);
//								sms = jsonobj.toJSONString();
//							}
//							
//							if ("replace".equals(sms)) {
//								ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
//								reqInfo.setFfId(ffId);
//							}
//							
							clog.append("合作方:").append(cpId).append("\n");
							clog.append("通道方:").append(info.getSpId()).append("\n");
							clog.append("订单号:").append(ffId).append("\n");
							clog.append("指令内容:").append(sms);
							MobileInfo mobileinfo = new MobileInfo();
							mobileinfo.setPhone(reqInfo.getMobile());
							commonAction.addMobile12306(mobileinfo);
							break;
						}
					} else {
						logger.warn("registerWBPhone号码已经被提交过，phone" + mobile);
						response.content(mobile+" Repeat submission").end();
						return;
					}
					
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
	}
	/***
	 * 快手注册无需透传参数，手机号码作为唯一参数
	 * 
	 * http://118.190.90.1/code/registerKSPhone
	 * 例子:http://118.190.90.1/code/registerKSPhone?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void registerKSPhone(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("registerKSPhone请求header信息："+request.allHeaders());
		
		OrderReqInfo reqInfo = reqEasyParamConver(request);
		
		String sms = "error";
		String spId = reqInfo.getSpId() == null?"wd99":reqInfo.getSpId();
		String cpId = reqInfo.getCpId();
		String mobile = reqInfo.getMobile();
		String imsi = reqInfo.getImsi();
		String iccid = reqInfo.getIccid();
		String ip = reqInfo.getIp();
		
		if (mobile == null || !mobile.matches(ConstantDefine.REGEX_MOBILE)) {
			logger.warn("registerKSPhone手机号非法，mobile:"+mobile);
			response.content("phone error :"+mobile).end();
			return;
		}
		
		try {
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			if (ct == null) {
				logger.warn("registerKSPhone没有注册通道");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				StringBuilder clog = new StringBuilder();
				for (CodeInfo info : ct.getCodes()) {
					spId = info.getSpId();
					ffId = DateTool.getMonth() + spId + mobile;
					OrderReqInfo resInfo = commonAction.queryOrderReqInfo(ffId);
					if (resInfo == null) {
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
							reqInfo.setCtech(reqInfo.getSmscontent());
							reqInfo.setPck(reqInfo.getCode());
							
							if (sms.startsWith("{")) {
								JSONObject jsonobj = JSONObject.parseObject(sms);
								String orderid = jsonobj.getString("");
								reqInfo.setPmodel(orderid);
								jsonobj.remove("orderid");
								jsonobj.put("orderid", ffId);
								sms = jsonobj.toJSONString();
							}
							
							if ("replace".equals(sms)) {
								ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
								reqInfo.setFfId(ffId);
							}
							
							clog.append("合作方:").append(cpId).append("\n");
							clog.append("通道方:").append(info.getSpId()).append("\n");
							clog.append("订单号:").append(ffId).append("\n");
							clog.append("指令内容:").append(sms);
							MobileInfo mobileinfo = new MobileInfo();
							mobileinfo.setPhone(reqInfo.getMobile());
							commonAction.addMobile12306(mobileinfo);
							break;
						}
					} else {
						logger.warn("registerKSPhone号码已经被提交过，phone" + mobile);
						response.content(mobile+" Repeat submission").end();
						return;
					}
					
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
	}
	/***
	 * KS验证码
	 * 
	 * http://118.190.90.1/code/commitCodeKS
	 * 例子:http://118.190.90.1/code/commitCodeKS?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void commitCodeKS(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("commitCode请求header信息："+request.allHeaders());
		String ffId = request.getParam("orderid");
		String traid = request.getParam("traid");
		String cpId = request.getParam("cpid");
		String code = request.getParam("code");
		String smscontent = request.getParam("smscontent");
		if (smscontent != null && smscontent.length() > 0) 
			smscontent = URLDecoder.decode(smscontent, "utf-8");
		String sreqNum = request.getParam("num");
		int reqNum = 0;
		if (sreqNum != null && !"".equals(sreqNum)) 
			reqNum = CommonTool.convertInt(sreqNum);
		
		String sms = "error";
		try {
			CPInfo cp = commonAction.queryCP(cpId);
			if (cp == null || cp.getIsOpen() == 0) {
				logger.warn("cpid：" + cpId + " 没有配置或打开计费权限");
				response.content(sms).end();
				return;
			} else {
				OrderReqInfo reqInfo = commonAction.queryOrderReqInfo(ffId);
				if (reqInfo != null) {
					reqInfo.setTraid(traid);
					reqInfo.setCode(code);
					reqInfo.setSmscontent(smscontent);
					reqInfo.setNum(reqNum+"");
					String spId = reqInfo.getSpId();
					CodeInfo info = commonAction.queryCodeInfoBySpid(spId);
					if (info != null) {
						reqInfo.setUrl(info.getUrl());
						reqInfo.setUrlNo(info.getUrlNo());
						reqInfo.setKeyword(info.getKeyword());
						reqInfo.setMatchRegex(info.getMatchRegex());
						reqInfo.setDelayed(info.getDelayed());
						reqInfo.setFees(info.getFee());
						reqInfo.setMsg(info.getMemo());
						sms = CodeCommitReqHelper.commitCodeResult(reqInfo);
						StringBuilder clog = new StringBuilder();
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("省份:").append(reqInfo.getProvince()).append("\n");
						clog.append("计费点:").append(reqInfo.getFee()).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						logger.info(clog.toString());
					} else {
						logger.warn("spid：" + spId + " 没有找到该通道");
					}
					OrderReqInfo order = new OrderReqInfo();
					order.setFfId(ffId);
					
					if (smscontent != null && smscontent.length() > 0) {
						order.setCtech(smscontent);
					} else {
						order.setCtech(code);
					}
					commonAction.updateOrderInfo(order);
				} else {
					logger.warn("ffid：" + ffId + " 没有找到该订单");
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms.getBytes("gbk")).end();
	}
	
	public void withdrawDeposit(HttpRequest request, HttpResponse response) {
		
		logger.info("请求header信息：" + request.allHeaders());
		String body = request.body();
		logger.info("请求body信息："+ body);
		/*
		 * String bankType=request.getParam("bankType"); String
		 * amount=request.getParam("amount"); String
		 * bankUserName=request.getParam("bankUserName"); String
		 * bankAccount=request.getParam("bankAccount"); String
		 * memo=request.getParam("memo"); String cpId=request.getParam("cpId");
		 * String phone=request.getParam("phone"); String
		 * orderNo=request.getParam("orderNo"); String
		 * bankName=request.getParam("bankName"); String
		 * openProvince=request.getParam("openProvince"); String
		 * openCity=request.getParam("openCity"); String
		 * idNumber=request.getParam("idNumber"); String
		 * sign=request.getParam("sign");
		 */
		String sms = "error";
		JSONObject jsonobj = JSONObject.parseObject(body);
		Map<String, String> reqMap = new HashMap<String, String>();
		for (Map.Entry<String, Object> entry : jsonobj.entrySet()) {
			String value = entry.getValue() + "";
			reqMap.put(entry.getKey(), value);
		}
		if(!reqMap.isEmpty()){
			String sign = reqMap.get("sign");
			String cpid = reqMap.get("cpid");
			reqMap.remove("sign");
			if (cpid == null || cpid.length() <= 2) {
				cpid = "mrjm";
			} else {
				cpid = cpid.substring(0, cpid.length() - 2);
			}
			String appsecret = ConstantDefine.MD5_KEY1 + cpid
					+ ConstantDefine.MD5_KEY2;
			String newSign=null;
			try {
				newSign = PayUtil.getSign(reqMap, appsecret);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (sign.equals(newSign)) {//sign.equals(newSign)
				cpid=reqMap.get("cpid");
				// 创建订单号
				// 获取8位年月日
				Date date = new Date();
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
				String one = format.format(date);
				// 4位时间戳
				Long lo = date.getTime();
				String two = String.valueOf(lo).substring(0, 4);
				// 四位随机数
				String three = String
						.valueOf((int) ((Math.random() * 9 + 1) * 1000));
				StringBuilder orderNo = new StringBuilder();
				// 生成订单号并保存
				orderNo.append(one).append(two).append(three);
				reqMap.put("orderNo", orderNo.toString());
				// 验证余额
				String spid=commonAction.querySpIdByCpId(cpid);
				Map<String,String> balanceMap=commonAction.queryBalanceByCon(cpid,spid);
				int amount=Integer.valueOf(reqMap.get("amount"));
				//获取余额或者额度的金额
				int money=0;
				String type=reqMap.get("type");
				if("1".equals(type)){
					money=Integer.valueOf(balanceMap.get("beforeBalance"));	
				}else if("2".equals(type)){
					money=Float.valueOf(balanceMap.get("todayBalance")).intValue();
				}	
				if(amount<money+200&&money>0){
					//生成订单
					reqMap.put("spid",spid);
					reqMap.put("operationStatus","0");//订单状态：0新建未处理
					reqMap.put("serviceCharge","200");//手续费：200分
					String count=commonAction.createOrder(reqMap);
					Map<String,String> spMap=commonAction.selectDaifu(spid);
					if("1".equals(count)&&spMap!=null){
						reqMap.putAll(spMap);
						sms = WithdrawHelper.getSMSCode(reqMap);
						commonAction.updateOrder(reqMap);
					}
				}	
		}
			response.content(sms).end();	
		}
	}
	/***
	 * 获取计费指令
	 * 
	 * http://120.24.88.90/code/pay
	 * 例子:http://120.24.88.90/code/pay?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89
	 */
	public void pay(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String aimsi = request.getParam("aimsi");
		String mobile = request.getParam("phone");
		String param = request.getParam("cpparam");
		String pmodel = request.getParam("pmodel");			// 手机型号
		String osversion = request.getParam("osversion");	// 系统版本
		String nwtype = request.getParam("nwtype");			// 网络类型
		String ctech = request.getParam("ctech");			// 通信制式 GSM、CDMA、3G、4G等
		String fee = request.getParam("fee");
		String pck = request.getParam("pck");//包名
		String app = request.getParam("app");//应用名称
		String enMethod = request.getParam("enMethod");
		String scount = request.getParam("count");
		String sreqNum = request.getParam("num");
		String sdkVer = request.getParam("sdkVer");
		String mmAppid = request.getParam("mm_appid");
		String mmChannel = request.getParam("mm_channel");
		String spId = request.getParam("sp_id");
		sdkVer = sdkVer==null?"1.1.0.0":sdkVer;
		int reqNum = 0;
		if (sreqNum != null && !"".equals(sreqNum)) 
			reqNum = CommonTool.convertInt(sreqNum);
		reqNum += 1;
		int count = 1;
		if (scount != null && !"".equals(scount)) 
			count = CommonTool.convertInt(scount);
		
		String cid = request.getParam("cid");//基站信息
		String lac = request.getParam("lac");//基站信息
		String lat = request.getParam("lat");//经度
		String lon = request.getParam("lon");//纬度
		
		String province = null;
		String iccid = request.getParam("iccid");
		if (iccid != null && iccid.length()>10) {
			logger.info("用户访问iccid=" + iccid);
			province = provinceMap.get(iccid.substring(8,10));
			logger.info("用户省份=" + province);
		}
		
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		logger.info("用户访问IP=" + ip);
		String city = IPtest.getInstance().queryCountry(ip);
		if (province == null) {
			province = IPtest.getInstance().queryProvince(ip);
			logger.info("根据IP查询省份=" + province);
		}
		
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setCpId(cpId);
		reqInfo.setImsi(imsi);
		reqInfo.setImei(imei);
		reqInfo.setAimsi(aimsi);
		reqInfo.setMobile(mobile);
		reqInfo.setCpParam(param);
		reqInfo.setPmodel(pmodel);
		reqInfo.setOsversion(osversion);
		reqInfo.setNwtype(nwtype);
		reqInfo.setCtech(ctech);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setPrice(fee);
		reqInfo.setPck(pck);
		reqInfo.setApp(app);
		reqInfo.setIp(ip);
		reqInfo.setProvince(province);
		reqInfo.setIccid(iccid);
		reqInfo.setCount(count);
		reqInfo.setEnMethod(enMethod);
		reqInfo.setBscCid(cid);
		reqInfo.setBscLac(lac);
		reqInfo.setGpsLat(lat);
		reqInfo.setGpsLng(lon);
		reqInfo.setNum(reqNum+"");
		reqInfo.setSdkVer(sdkVer);
		reqInfo.setMmAppid(mmAppid);
		reqInfo.setMmChannel(mmChannel);
		reqInfo.setCity(city);
		
		String sms = "error";
		spId = spId == null?"wd99":spId;
		PayResultJson resultJson = new PayResultJson();
		String result = "error";
		int codeType = CommonTool.locateOperator(imsi);//1移动 2联通 3电信
		boolean flag = true;
		try {
			String ffId = null;
			CodeT ct = commonAction.getPayCodes(reqInfo);//获取对应cpId打开的计费通道
			/*if ("com.tinyhorse.furyroad.ff202".equals(pck) || "com.jgsx.sdkgl".equals(pck))
				ct = null;*/
			if (ct == null) {
				ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), "oo");
				logger.warn("CPID=" + cpId + " 省份=" + province + " 计费点="
						+ fee + " 没有匹配到计费通道！");
				ct = new CodeT();
				ct.setFfId(ffId);
				reqInfo.setSpId(spId);
				reqInfo.setIsSuccess(9001);
				commonAction.addOrderReqInfo(ct, reqInfo);
				resultJson.setCode(ConstantDefine.ERROR_9001);
				resultJson.setCode_explain(URLEncoder.encode(ConstantDefine.ERROR_9001_MSG,"utf-8"));
				response.content(JSONObject.toJSONString(resultJson)).end();
				return;
			} else {
				ffId = ct.getFfId();
				List<ResSDKFilter> filterrsp = null;
				String delayed = null;
				String spName = null;
				reqInfo.setSpId(spId);
				for (CodeInfo info : ct.getCodes()) {
					logger.info("通道号spid="+info.getSpId());
					if (spId.length()>0 && !"wd99".equals(spId) && !spId.equals(info.getSpId())) continue;
					if ("dx".equals(info.getSpFlag()) && codeType != 3) continue;
					if ("lt".equals(info.getSpFlag()) && codeType != 2) continue;
					if (codeType != 1 && !"lt".equals(info.getSpFlag()) && !"dx".equals(info.getSpFlag())
							&& !"sd".equals(info.getSpFlag())) continue;
					
					if ("1.1.0.0".equals(sdkVer)) {
						if ("mm06".equals(info.getSpId()) || "yc09".equals(info.getSpId())
								|| "yc01".equals(info.getSpId()))
							continue;
					}
					
					if ("qx08".equals(info.getSpFlag())) continue;
					
					if ("1.3.0.0".equals(sdkVer) || "1.3.1.0".equals(sdkVer)) {
						if ("dm02".equals(info.getSpId()))
							continue;
					}
					
					delayed = info.getDelayed();
					reqInfo.setWait_time(delayed);
					ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), info.getSpFlag());
					if ("qx".equals(info.getSpFlag()) && reqNum <= 2) {
						if ("1.1.0.0".equals(sdkVer)) continue;
						if (mobile == null || "".equals(mobile)) {
							CustomInfo cusInfo = getCustomInfo(reqInfo);
							if (cusInfo != null) {
								reqInfo.setMobile(cusInfo.getMobile());
							} else {
								sms = getQXTJson(reqInfo,info);
								spId = info.getSpId();
								reqInfo.setSpId(spId);
								flag = false;
								break;
							}
						}
					}
					
					reqInfo.setFfId(ffId);
					reqInfo.setUrl(info.getUrl());
					reqInfo.setUrlNo(info.getUrlNo());
					reqInfo.setReqMethod(info.getReqMethod());
					reqInfo.setKeyword(info.getKeyword());
					reqInfo.setMatchRegex(info.getMatchRegex());
					reqInfo.setFees(info.getFee());
					
					/*if ("mm06".equals(info.getSpFlag()) || "mm07".equals(info.getSpFlag())) {
						filterrsp = commonAction.queryFilterBySpid(reqInfo);
						if (filterrsp == null || filterrsp.size() == 0)  
							break;
					}*/
					
					sms = CodeReqHelper.getSMSCode(reqInfo);
					
					if (!"error".equals(sms)) {
						spId = info.getSpId();
						spName = info.getName();
						ct.setFfId(ffId);
						ct.setSpId(spId);
						reqInfo.setSpId(spId);
						reqInfo.setFfId(ffId);
						filterrsp = commonAction.queryFilterBySpid(reqInfo);//查询代码需要屏蔽的信息
						if (filterrsp != null && filterrsp.size() > 0)  
							break;
					} else {
						ct.setSpId(spId);
						reqInfo.setSpId(spId);
					}
				}
				
				if ("error".equals(sms)) {
					reqInfo.setIsSuccess(9002);
					resultJson.setCode(ConstantDefine.ERROR_9002);
					resultJson.setCode_explain(URLEncoder.encode(ConstantDefine.ERROR_9002_MSG,"utf-8"));
					
				} else {
					if ((filterrsp == null || filterrsp.size() == 0) && flag) {
						reqInfo.setIsSuccess(9003);
						resultJson.setCode(ConstantDefine.ERROR_9003);
						resultJson.setCode_explain(URLEncoder.encode(ConstantDefine.ERROR_9003_MSG,"utf-8"));
					} else {
						reqInfo.setIsSuccess(0);
						resultJson.setCode(ConstantDefine.RES_SUCCESS);
						resultJson.setCode_explain(URLEncoder.encode(ConstantDefine.RES_SUCCESS_MSG,"utf-8"));
						
						List<PayContentJson> conArrJson = new ArrayList<PayContentJson>();
						PayContentJson contentJson = new PayContentJson();
						contentJson.setFilterrsp(filterrsp);
//						contentJson.setDelayed(delayed);
						contentJson.setOrder_id(ffId);
						if ("mm06".equals(spId) || "mm08".equals(spId) || "mm09".equals(spId)) {
							contentJson.setPay_type(ConstantDefine.RES_CON_PAY_TYPE_2002);
							contentJson.setPay_content(JSONArray.parseArray(sms, JSONObject.class).get(0));
						} else if ("mm07".equals(spId)) {
							contentJson.setPay_type(ConstantDefine.RES_CON_PAY_TYPE_2002);
							contentJson.setPay_content(JSONObject.parseObject(sms));
							JSONObject resultObj = JSONObject.parseObject(sms);
							String initSms = resultObj.getString("init_sms");
							/*PayConJson payJson = new PayConJson();
							payJson.setType(resultObj.getString("send_type"));
							payJson.setAddress(resultObj.getString("sms_number"));
							if ("1".equals(resultObj.getString("send_type"))) {
								payJson.setContent(StringUtils.bytesToHex(Base64.decode(resultObj.getString("sms"))));
							} else {
								payJson.setContent(new String(Base64.decode(resultObj.getString("sms"))));
							}
							contentJson.setPay_type(ConstantDefine.RES_CON_PAY_TYPE_1001);
							contentJson.setPay_content(JSONObject.parseObject(JSONObject.toJSONString(payJson)));*/
							if (initSms != null && !"".equals(initSms)) {
								PayContentJson contentJson1 = getConJson(initSms);
								contentJson1.setDelayed(delayed);
								contentJson1.setOrder_id(ffId);
								conArrJson.add(contentJson1);
							}
						} else if("yy01".equals(spId) || "yc02".equals(spId) || "dx02".equals(spId)
								|| "yc03".equals(spId) || "yc04".equals(spId) || "yc05".equals(spId)
								|| "yc06".equals(spId) || "yc07".equals(spId) || "yc08".equals(spId)
								|| "ec05".equals(spId) || "dx01".equals(spId) || "ec07".equals(spId) ) {
							contentJson.setPay_type(ConstantDefine.RES_CON_PAY_TYPE_1001);
							contentJson.setPay_content(JSONObject.parseObject(sms));
						} else if("yc01".equals(spId) || "yc09".equals(spId) || "yc10".equals(spId)
								|| "yc11".equals(spId)) {
							contentJson.setPay_type(ConstantDefine.RES_CON_PAY_TYPE_2003);
							contentJson.setPay_content(JSONObject.parseObject(sms));
						} else if("yc15".equals(spId)) {
							contentJson.setPay_type(ConstantDefine.RES_CON_PAY_TYPE_2004);
							contentJson.setPay_content(JSONObject.parseObject(sms));
						} else if("mm12".equals(spId)) {
							contentJson.setPay_type(ConstantDefine.RES_CON_PAY_TYPE_2005);
							contentJson.setPay_content(JSONObject.parseObject(sms));
						} else if("ec01".equals(spId) || "qx01".equals(spId) || "qx03".equals(spId)) {
							contentJson.setPay_type(ConstantDefine.RES_CON_PAY_TYPE_1002);
							contentJson.setPay_content(JSONObject.parseObject(sms));
							contentJson.setNum(reqInfo.getNum());
						} else if("ec03".equals(spId) || "dm02".equals(spId) || "dm03".equals(spId)) {
							contentJson.setPay_type(ConstantDefine.RES_CON_PAY_TYPE_1004);
							contentJson.setPay_content(JSONObject.parseObject(sms));
							contentJson.setNum(reqInfo.getNum());
						} else if("qx04".equals(spId) || "qx05".equals(spId) || "qx07".equals(spId)
								|| "qx09".equals(spId) || "qx10".equals(spId) || "qx11".equals(spId)) {
							if (flag) {
								contentJson.setPay_type(ConstantDefine.RES_CON_PAY_TYPE_1005);
							} else {
								contentJson.setPay_type(ConstantDefine.RES_CON_PAY_TYPE_1002);
							}
							contentJson.setPay_content(JSONObject.parseObject(sms));
							contentJson.setNum(reqInfo.getNum());
						} else if("qx02".equals(spId)) {
							if (flag) {
								contentJson.setPay_type(ConstantDefine.RES_CON_PAY_TYPE_1003);
							} else {
								contentJson.setPay_type(ConstantDefine.RES_CON_PAY_TYPE_1002);
							}
							contentJson.setPay_content(JSONObject.parseObject(sms));
						} else if("sd01".equals(spId)) {
							contentJson.setPay_type(ConstantDefine.RES_CON_PAY_TYPE_3001);
							contentJson.setPay_content(JSONObject.parseObject(sms));
						} else if("sd02".equals(spId)) {
							contentJson.setPay_type(ConstantDefine.RES_CON_PAY_TYPE_4001);
							contentJson.setPay_content(JSONObject.parseObject(sms));
						} else if("lt02".equals(spId) || "dm04".equals(spId) || "yy04".equals(spId)) {
							contentJson.setPay_type(ConstantDefine.RES_CON_PAY_TYPE_1001);
							if (sms.contains("###")) {
								PayContentJson contentJson1 = new PayContentJson();
								contentJson1.setFilterrsp(filterrsp);
								contentJson1.setDelayed(delayed);
								contentJson1.setOrder_id(ffId);
								contentJson1.setPay_type(ConstantDefine.RES_CON_PAY_TYPE_1001);
								String[] tempList = sms.split("###");
								contentJson1.setPay_content(JSONObject.parseObject(tempList[0]));
								conArrJson.add(contentJson1);
								contentJson.setPay_content(JSONObject.parseObject(tempList[1]));
							} else {
								contentJson.setPay_content(JSONObject.parseObject(sms));
							}
						} else if("ec06".equals(spId)) {
							JSONArray resultList = JSONArray.parseArray(sms);
							for (int i=0; i<resultList.size(); i++) {
								if (i == 0) {
									contentJson.setPay_type(ConstantDefine.RES_CON_PAY_TYPE_1001);
									contentJson.setPay_content(JSONObject.parseObject(resultList.get(0).toString()));
								} else {
										PayContentJson contentJson1 = new PayContentJson();
										contentJson1.setDelayed(delayed);
										contentJson1.setOrder_id(ffId);
										contentJson1.setPay_type(ConstantDefine.RES_CON_PAY_TYPE_1001);
										contentJson1.setPay_content(JSONObject.parseObject(resultList.get(i).toString()));
										conArrJson.add(contentJson1);
								}
							}
						}
						
						conArrJson.add(contentJson);
						resultJson.setContent(conArrJson);
					}
					
				}
				
				result = JSONObject.toJSONString(resultJson);
				
				StringBuilder clog = new StringBuilder();
				String operator = "移动";
				if (codeType == 2) {
					operator = "联通";
				} else if (codeType == 3) {
					operator = "电信";
				}
				clog.append("合作方:").append(ct.getCpName()).append("\n");
				clog.append("手机运营商:").append(operator).append("\n");
				clog.append("通道方:").append(spName).append("\n");
				clog.append("通道spId:").append(spId).append("\n");
				clog.append("省份:").append(province).append("\n");
				clog.append("计费点:").append(fee).append("\n");
				clog.append("订单号:").append(ffId).append("\n");
				clog.append("指令内容:").append(result);
				logger.info(clog.toString());
				
				if (flag) {
					commonAction.addOrderReqInfo(ct, reqInfo);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(result).end();
	}
	
	/***
	 * 注册
	 * 
	 * http://120.24.88.90/code/reqRegister
	 * 例子:http://120.24.88.90/code/reqRegister?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void reqRegister(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqRegister请求header信息："+request.allHeaders());
		
		OrderReqInfo reqInfo = reqAESParamConver(request);
		
		String sms = "error";
		String spId = reqInfo.getSpId() == null?"wd99":reqInfo.getSpId();
		String cpId = reqInfo.getCpId();
		
		try {
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			
			if (ct == null) {
				logger.warn("reqRegister没有注册通道");
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
					reqInfo = RegisterReqHelper.getSMSRegister(reqInfo);
					RegisterRes reg = reqInfo.getReg();
					if (reg != null) {
						sms = JSONObject.toJSONString(reg);
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						sms = AESUtil.encrypt(reqInfo.getFfId(), sms) + reqInfo.getFfId();
						break;
					}
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			reqInfo.setPck(reqInfo.getUsername());
			reqInfo.setApp(reqInfo.getPassword());
			reqInfo.setSdkVer(reqInfo.getCode());
			
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
	}
	
	/***
	 * 微信注册辅助扫码
	 * 
	 * http://118.190.90.1/code/reqRegisterScan
	 * 例子:http://118.190.90.1/code/reqRegister?phone=13762143362&cpid=test01&cpparam=
	 */
	public void reqRegisterScan(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqRegisterScan请求header信息："+request.allHeaders());
		
		OrderReqInfo reqInfo = reqEasyParamConver(request);
		
		String cpid = reqInfo.getCpId();
		
		String sms = "error";
		String spId = reqInfo.getSpId() == null?"wd99":reqInfo.getSpId();
		
		try {
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			
			if (ct == null) {
				logger.warn("reqRegisterScan没有注册辅助通道");
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
					reqInfo = RegisterReqHelper.getSMSRegister(reqInfo);
					RegisterRes reg = reqInfo.getReg();
					if (reg != null) {
						sms = JSONObject.toJSONString(reg);
						clog.append("合作方:").append(cpid).append("\n");
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
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
	}
	

	/***
	 * 获取包月代码
	 * 
	 * http://118.190.90.1/code/queryRegisterScan
	 * 例子:http://118.190.90.1/code/queryRegisterScan?orderid=01sdg12365896542&cpid=test01
	 */
	public void queryRegisterScan(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("queryRegisterScan请求header信息："+request.allHeaders());
		String ffId = request.getParam("orderid");
		String cpId = request.getParam("cpid");
		
		String sms = "error";
		try {
			CPInfo cp = commonAction.queryCP(cpId);
			if (cp == null || cp.getIsOpen() == 0) {
				logger.warn("cpid：" + cpId + " 没有配置或打开计费权限");
				response.content(sms).end();
				return;
			} else {
				Map<String, String> resmap = new HashMap<String, String>();
				OrderReqInfo reqInfo = commonAction.queryOrderReqInfo(ffId);
				if (reqInfo != null) {
					StringBuilder param = new StringBuilder();
					param.append("m=Lua.checkImg")
					.append("&key=").append(reqInfo.getPmodel());
					String reqResult = HttpTool.sendGetSetTimeout(cp.getUrl(), param.toString(),"2000");
					String[] reqResultArr = reqResult.split("\\|");
					if (reqResultArr.length == 2) {
						if ("null".equals(reqResultArr[1])) {
							reqResultArr[1] = "0";
						}
						resmap.put("status", reqResultArr[1]);
						resmap.put("msg", "查询成功");
					} else {
						resmap.put("status", "9");//未知错误
						resmap.put("msg", "查询错误请稍后再试");
					}
				} else {
					logger.warn("ffid：" + ffId + " 没有找到该订单");
					resmap.put("status", "8");//没有找到该订单
					resmap.put("msg", "没有找到该订单");//没有找到该订单
				}
				sms = JSONObject.toJSONString(resmap);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms.getBytes("gbk")).end();
	}
	
	/***
	 * 注册
	 * 
	 * http://120.24.88.90/code/registerUser
	 * 例子:http://120.24.88.90/code/registerUser?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void registerUser(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("registerUser请求header信息："+request.allHeaders());
		
		OrderReqInfo reqInfo = reqEasyParamConver(request);
		
		String sms = "error";
		String spId = reqInfo.getSpId() == null?"wd99":reqInfo.getSpId();
		String cpId = reqInfo.getCpId();
		
		try {
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			
			if (ct == null) {
				logger.warn("registerUser没有注册通道");
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
						if ("sd15".equals(spId) || "sd05".equals(spId)) {
							if (!"ok".equals(sms)) {
								JSONObject jsonobj = JSONObject.parseObject(sms);
								String username = jsonobj.getString("username");
								String password = jsonobj.getString("password");
								if (username != null && username.length() > 0
										&& password != null && password.length() > 0) {
									reqInfo.setCtech(reqInfo.getSmscontent());
									reqInfo.setUsername(username);
									reqInfo.setPassword(password);
								}
								sms = "ok";
							}
							ffId = ffId.substring(0,4) + reqInfo.getUsername();
							reqInfo.setFfId(ffId);
						} else if ("sd24".equals(spId) && sms.startsWith("{")) {
							JSONObject jsonobj = JSONObject.parseObject(sms);
							reqInfo.setPck(jsonobj.getString("traid"));
						}
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
			reqInfo.setPck(reqInfo.getUsername());
			reqInfo.setApp(reqInfo.getPassword());
			reqInfo.setSdkVer(reqInfo.getCode());
			String key = "QQ_"+reqInfo.getUsername();
			String qqflag = null;
			if ("sd15".equals(spId) || "sd05".equals(spId)) {
				qqflag = MemcachedResource.get(key);
				if (qqflag == null) {
					String regex = "\\d+";
					if (reqInfo.getUsername().matches(regex)) {
						
						QQInfo qq = commonAction.queryQQInfo(reqInfo);
						
						if (qq == null) {
							MemcachedResource.save(key, "true");
							reqInfo.setIsSyn(ConstantDefine.SYN_TRUE);
							reqInfo.setIsSuccess(1);
							int synStatus = processOrder(ct.getCp(),reqInfo);
							reqInfo.setSynStatus(synStatus);
							commonAction.addOrderReqInfo(ct, reqInfo);
						} else {
							logger.info("registerUser往月已经提交QQ："+reqInfo.getUsername());
						}
						
					} else {
						logger.info("registerUser提交QQ不是数字："+reqInfo.getUsername());
					}
				} else {
					logger.info("registerUser重复提交QQ："+reqInfo.getUsername());
					response.content("error").end();
					return;
				}
			}
			if ("sd15".equals(spId) || "sd05".equals(spId)) {//去除重复提交的QQ
			} else {
				commonAction.addOrderReqInfo(ct, reqInfo);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
	}
	
	/***
	 * 12306注册无需透传参数，手机号码作为唯一参数
	 * 
	 * http://120.24.88.90/code/registerPhone
	 * 例子:http://120.24.88.90/code/registerUser?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void registerPhone(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("registerPhone请求header信息："+request.allHeaders());
		
		OrderReqInfo reqInfo = reqEasyParamConver(request);
		
		String sms = "error";
		String spId = reqInfo.getSpId() == null?"wd99":reqInfo.getSpId();
		String cpId = reqInfo.getCpId();
		String mobile = reqInfo.getMobile();
		String code = reqInfo.getCode();
		
		if (mobile == null || !mobile.matches(ConstantDefine.REGEX_MOBILE_12306)) {
			logger.warn("registerPhone手机号非法，mobile:"+mobile);
			response.content("phone error :"+mobile).end();
			return;
		}
		
		if (code == null || !code.matches(ConstantDefine.REGEX_NUM)) {
			logger.warn("registerPhone验证码非法，code:"+code);
			response.content("code error :"+code).end();
			return;
		}
		
		String key = "12306_"+mobile+code;
		String flag12306 = MemcachedResource.get(key);
		if (flag12306 != null) {
			logger.warn("registerPhone同一验证码和手机号重复提交，code:"+code+"，mobile:"+mobile);
			response.content("repeat submission code:"+code+", mobile:"+mobile).end();
			return;
		}
		MemcachedResource.save(key,"true");
		
		try {
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			if (ct == null) {
				logger.warn("registerPhone没有注册通道");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				StringBuilder clog = new StringBuilder();
				for (CodeInfo info : ct.getCodes()) {
					spId = info.getSpId();
					ffId =DateTool.getMonth() + "_" + DateTool.getffIdDay() + DateTool.getMonthDay() + mobile;
					OrderReqInfo resInfo = commonAction.queryOrderReqInfoDate(ffId);
					if (resInfo == null) {
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
							reqInfo.setCtech(reqInfo.getSmscontent());
							reqInfo.setPck(reqInfo.getCode());
							
							if ("replace".equals(sms)) {
								ffId = DateTool.getMonth() + "_" + DateTool.getffIdDay()+CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
								reqInfo.setFfId(ffId);
							}
							
							clog.append("合作方:").append(cpId).append("\n");
							clog.append("通道方:").append(info.getSpId()).append("\n");
							clog.append("订单号:").append(ffId).append("\n");
							clog.append("指令内容:").append(sms);
							MobileInfo mobileinfo = new MobileInfo();
							mobileinfo.setPhone(reqInfo.getMobile());
							commonAction.addMobile12306(mobileinfo);
							break;
						}
					} else {
						logger.warn("registerPhone号码已经被提交过，phone" + mobile);
						response.content(mobile+" Repeat submission").end();
						return;
					}
					
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfoDate(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
	}
	
	/***
	 * webqq注册无需透传参数，手机号码作为唯一参数
	 * 
	 * http://120.24.88.90/code/registerWebqqPhone
	 * 例子:http://120.24.88.90/code/registerUser?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void registerWebqqPhone(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("registerWebqqPhone请求header信息："+request.allHeaders());
		
		OrderReqInfo reqInfo = reqEasyParamConver(request);
		
		String sms = "error";
		String spId = reqInfo.getSpId() == null?"wd99":reqInfo.getSpId();
		String cpId = reqInfo.getCpId();
		String mobile = reqInfo.getMobile();
//		String code = reqInfo.getCode();
		
		if (mobile == null || !mobile.matches(ConstantDefine.REGEX_MOBILE_12306)) {
			logger.warn("registerWebqqPhone手机号非法，mobile:"+mobile);
			response.content("phone error :"+mobile).end();
			return;
		}
		
		/*if (code == null || !code.matches(ConstantDefine.REGEX_NUM)) {
			logger.warn("registerWebqqPhone验证码非法，code:"+code);
			response.content("code error :"+code).end();
			return;
		}*/
		
		try {
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			if (ct == null) {
				logger.warn("registerWebqqPhone没有注册通道");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				StringBuilder clog = new StringBuilder();
				for (CodeInfo info : ct.getCodes()) {
					spId = info.getSpId();
					ffId = DateTool.getMonth() + mobile + DateTool.getTodayNum();
					OrderReqInfo resInfo = commonAction.queryOrderReqInfo(ffId);
					if (resInfo == null) {
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
							reqInfo.setCtech(reqInfo.getSmscontent());
							reqInfo.setPck(reqInfo.getCode());
							
							if (sms.startsWith("{")) {
								JSONObject jsonobj = JSONObject.parseObject(sms);
								String orderid = jsonobj.getString("");
								reqInfo.setPmodel(orderid);
								jsonobj.remove("orderid");
								jsonobj.put("orderid", ffId);
								sms = jsonobj.toJSONString();
							}
							
							if ("replace".equals(sms)) {
								ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
								reqInfo.setFfId(ffId);
							}
							
							clog.append("合作方:").append(cpId).append("\n");
							clog.append("通道方:").append(info.getSpId()).append("\n");
							clog.append("订单号:").append(ffId).append("\n");
							clog.append("指令内容:").append(sms);
							MobileInfo mobileinfo = new MobileInfo();
							mobileinfo.setPhone(reqInfo.getMobile());
							commonAction.addMobile12306(mobileinfo);
							break;
						}
					} else {
						logger.warn("registerWebqqPhone号码已经被提交过，phone" + mobile);
						response.content(mobile+" Repeat submission").end();
						return;
					}
					
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
	}
	/***
	 * webqq注册无需透传参数，手机号码作为唯一参数
	 * 
	 * http://120.24.88.90/code/registerWebqqPhone
	 * 例子:http://120.24.88.90/code/registerUser?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void registerJDPhone(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("registerJDPhone请求header信息："+request.allHeaders());
		
		OrderReqInfo reqInfo = reqEasyParamConver(request);
		
		String sms = "error";
		String spId = reqInfo.getSpId() == null?"wd99":reqInfo.getSpId();
		String cpId = reqInfo.getCpId();
		String mobile = reqInfo.getMobile();
		
		String imsi = reqInfo.getImsi();
		String iccid = reqInfo.getIccid();
		String ip = reqInfo.getIp();
//		String code = reqInfo.getCode();
		
		if (mobile == null || !mobile.matches(ConstantDefine.REGEX_MOBILE)) {
			logger.warn("registerJDPhone手机号非法，mobile:"+mobile);
			response.content("phone error :"+mobile).end();
			return;
		}

		Map<String, String> areaMap = queryReqArea(imsi, mobile, iccid, ip);
		reqInfo.setProvince(areaMap.get("province"));
		reqInfo.setCity(areaMap.get("city"));
		reqInfo.setPhoneType(Integer.parseInt(areaMap.get("phoneType")));
		/*if (code == null || !code.matches(ConstantDefine.REGEX_NUM)) {
			logger.warn("registerWebqqPhone验证码非法，code:"+code);
			response.content("code error :"+code).end();
			return;
		}*/
		
		try {
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			if (ct == null) {
				logger.warn("registerJDPhone没有注册通道");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				StringBuilder clog = new StringBuilder();
				for (CodeInfo info : ct.getCodes()) {
					spId = info.getSpId();
					ffId = DateTool.getMonth() + "_" + DateTool.getffIdDay()+DateTool.getMonthDay() + spId + mobile;
					OrderReqInfo resInfo = commonAction.queryOrderReqInfoDate(ffId);
					if (resInfo == null) {
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
							reqInfo.setCtech(reqInfo.getSmscontent());
							reqInfo.setPck(reqInfo.getCode());
							
							if (sms.startsWith("{")) {
								JSONObject jsonobj = JSONObject.parseObject(sms);
								String orderid = jsonobj.getString("");
								reqInfo.setPmodel(orderid);
								jsonobj.remove("orderid");
								jsonobj.put("orderid", ffId);
								sms = jsonobj.toJSONString();
							}
							
							if ("replace".equals(sms)) {
								ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
								reqInfo.setFfId(ffId);
							}
							
							clog.append("合作方:").append(cpId).append("\n");
							clog.append("通道方:").append(info.getSpId()).append("\n");
							clog.append("订单号:").append(ffId).append("\n");
							clog.append("指令内容:").append(sms);
							MobileInfo mobileinfo = new MobileInfo();
							mobileinfo.setPhone(reqInfo.getMobile());
							commonAction.addMobile12306(mobileinfo);
							break;
						}
					} else {
						logger.warn("registerJDPhone号码已经被提交过，phone" + mobile);
						response.content(mobile+" Repeat submission").end();
						return;
					}
					
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfoDate(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
	}
	
	/***
	 *  TB 淘宝问题手机号码注册
	 * 
	 *  http://www.kulecc.vip/receive.php?phone=12345678901&TB=01   
	 */
	public void registerTBPhone(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("registerTBPhone请求header信息："+request.allHeaders());
		
		OrderReqInfo reqInfo = reqEasyParamConver(request);
		
		String sms = "error";
		String spId = reqInfo.getSpId() == null?"wd99":reqInfo.getSpId();
		String cpId = reqInfo.getCpId();
		String mobile = reqInfo.getMobile();
		
		String imsi = reqInfo.getImsi();
		String iccid = reqInfo.getIccid();
		String ip = reqInfo.getIp();
//		String code = reqInfo.getCode();
		
		if (mobile == null || !mobile.matches(ConstantDefine.REGEX_MOBILE)) {
			logger.warn("registerJDPhone手机号非法，mobile:"+mobile);
			response.content("phone error :"+mobile).end();
			return;
		}

		Map<String, String> areaMap = queryReqArea(imsi, mobile, iccid, ip);
		reqInfo.setProvince(areaMap.get("province"));
		reqInfo.setCity(areaMap.get("city"));
		reqInfo.setPhoneType(Integer.parseInt(areaMap.get("phoneType")));
		/*if (code == null || !code.matches(ConstantDefine.REGEX_NUM)) {
			logger.warn("registerWebqqPhone验证码非法，code:"+code);
			response.content("code error :"+code).end();
			return;
		}*/
		
		try {
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			if (ct == null) {
				logger.warn("registerTBPhone没有注册通道");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				StringBuilder clog = new StringBuilder();
				for (CodeInfo info : ct.getCodes()) {
					spId = info.getSpId();
					ffId = DateTool.getMonth() + "_" + DateTool.getffIdDay()+DateTool.getMonthDay() + spId + mobile;
					OrderReqInfo resInfo = commonAction.queryOrderReqInfoDate(ffId);
					if (resInfo == null) {
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
							reqInfo.setCtech(reqInfo.getSmscontent());
							reqInfo.setPck(reqInfo.getCode());
							
							if (sms.startsWith("{")) {
								JSONObject jsonobj = JSONObject.parseObject(sms);
								String orderid = jsonobj.getString("");
								reqInfo.setPmodel(orderid);
								jsonobj.remove("orderid");
								jsonobj.put("orderid", ffId);
								sms = jsonobj.toJSONString();
							}
							
							if ("replace".equals(sms)) {
								ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
								reqInfo.setFfId(ffId);
							}
							
							clog.append("合作方:").append(cpId).append("\n");
							clog.append("通道方:").append(info.getSpId()).append("\n");
							clog.append("订单号:").append(ffId).append("\n");
							clog.append("指令内容:").append(sms);
							MobileInfo mobileinfo = new MobileInfo();
							mobileinfo.setPhone(reqInfo.getMobile());
							commonAction.addMobile12306(mobileinfo);
							break;
						}
					} else {
						logger.warn("registerPhone号码已经被提交过，phone" + mobile);
						response.content(mobile+" Repeat submission").end();
						return;
					}
					
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfoDate(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
	}
	
	
	/***
	 * WeChat注册无需透传参数，手机号码作为唯一参数
	 * 
	 * http://118.190.90.1/code/registerWeChat
	 * 例子:http://120.24.88.90/code/registerWeChat?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void registerWeChat(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("registerWeChat请求header信息："+request.allHeaders());
		
		OrderReqInfo reqInfo = reqEasyParamConver(request);
		
		String sms = "error";
		String spId = reqInfo.getSpId() == null?"wd99":reqInfo.getSpId();
		String cpId = reqInfo.getCpId();
		String mobile = reqInfo.getMobile();
		
		if (mobile == null || !mobile.matches(ConstantDefine.REGEX_MOBILE)) {
			logger.warn("registerWeChat手机号非法，mobile:"+mobile);
			response.content("phone error :"+mobile).end();
			return;
		}
		
		try {
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			if (ct == null) {
				logger.warn("registerWeChat没有注册通道");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				StringBuilder clog = new StringBuilder();
				for (CodeInfo info : ct.getCodes()) {
					spId = info.getSpId();
					ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), info.getSpFlag());
					OrderReqInfo resInfo = commonAction.queryOrderReqInfo(ffId);
					if (resInfo == null) {
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
							reqInfo.setCtech(reqInfo.getSmscontent());
							reqInfo.setPck(reqInfo.getCode());
							
							if (sms.startsWith("{")) {
								JSONObject jsonobj = JSONObject.parseObject(sms);
								String orderid = jsonobj.getString("");
								reqInfo.setPmodel(orderid);
								jsonobj.remove("orderid");
								jsonobj.put("orderid", ffId);
								sms = jsonobj.toJSONString();
							}
							
							if ("replace".equals(sms)) {
								ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), "sd");
								reqInfo.setFfId(ffId);
							}
							
							clog.append("合作方:").append(cpId).append("\n");
							clog.append("通道方:").append(info.getSpId()).append("\n");
							clog.append("订单号:").append(ffId).append("\n");
							clog.append("指令内容:").append(sms);
							break;
						}
					} else {
						logger.warn("registerWeChat号码已经被提交过，phone" + mobile);
						response.content(mobile+"号码提交过").end();
						return;
					}
					
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
	}
	
	/***
	 * 提交下行信息
	 * 
	 * http://120.24.88.90/code/commitRegister
	 * 例子:http://120.24.88.90/code/commitRegister?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void commitRegister(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("commitRegister请求header信息："+request.allHeaders());
		
		OrderReqInfo reqInfo = reqAESParamConver(request);
		
		String sms = "error";
		String spId = reqInfo.getSpId() == null?"wd99":reqInfo.getSpId();
		String cpId = reqInfo.getCpId();
		
		try {
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			
			if (ct == null) {
				logger.warn("commitRegister没有注册通道");
				response.content("error").end();
				return;
			} else {
				String ffId = reqInfo.getOrderId();
				StringBuilder clog = new StringBuilder();
				for (CodeInfo info : ct.getCodes()) {
					spId = info.getSpId();
					ffId = ffId == null?CommonTool.genrateOrderNO(DateTool.getMonth(), info.getSpFlag()):ffId;
					reqInfo.setUrl(info.getUrl());
					reqInfo.setUrlNo(info.getUrlNo());
					reqInfo.setPmodel("");
					reqInfo.setFfId(ffId);
					reqInfo.setKeyword(info.getKeyword());
					reqInfo.setMatchRegex(info.getMatchRegex());
					reqInfo.setFees(info.getFee());
					reqInfo.setMsg(info.getMemo());
					reqInfo.setSpId(spId);
					reqInfo = RegisterReqHelper.getSMSRegister(reqInfo);
					RegisterRes reg = reqInfo.getReg();
					if (reg != null) {
						sms = JSONObject.toJSONString(reg);
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						sms = AESUtil.encrypt(reqInfo.getFfId(), sms) + reqInfo.getFfId();
						break;
					}
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(reqInfo.getFfId());
			info.setCpParam(reqInfo.getCpParam());
			info.setCtech(reqInfo.getCode());//验证码
			info.setPck(reqInfo.getUsername());
			info.setApp(reqInfo.getPassword());
			info.setSdkVer(reqInfo.getCode());
			if ("sd15".equals(spId) || "sd05".equals(spId)) {
				info.setIsSyn(ConstantDefine.SYN_TRUE);
				info.setIsSuccess(1);
				int synStatus = processOrder(ct.getCp(),reqInfo);
				info.setSynStatus(synStatus);
			}
			commonAction.updateOrderInfo(info);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
	}
	
	/***
	 * 提交手机号
	 * 
	 * http://smspay.qygame.cn/code/commitMobile
	 * 例子:http://smspay.qygame.cn/code/commitMobile?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void commitMobile(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("commitMobile请求header信息："+request.allHeaders());
		String ffId = request.getParam("ffid");
		String mobile = request.getParam("mobile");
		String cpId = request.getParam("cpid");
		
		String sms = "error";
		try {
			CPInfo cp = commonAction.queryCP(cpId);
			if (cp == null || cp.getIsOpen() == 0) {
				logger.warn("cpid：" + cpId + " 没有配置或打开计费权限");
				sms = "渠道没有开通";
				response.content(sms).end();
				return;
			} else if (!mobile.matches(ConstantDefine.REGEX_MOBILE)) {
				logger.warn("cpid：" + cpId + " 手机号非法：" + mobile);
				sms = "手机号非法";
				response.content(sms).end();
				return;
			} else {
				OrderReqInfo reqInfo = commonAction.queryOrderReqInfo(ffId);
				if (reqInfo != null) {
					reqInfo.setMobile(mobile);
					String spId = reqInfo.getSpId();
					CodeInfo info = commonAction.queryCodeInfoBySpid(spId);
					if (info != null) {
						reqInfo.setUrl(info.getUrl());
						reqInfo.setUrlNo(info.getUrlNo());
						reqInfo.setKeyword(info.getKeyword());
						reqInfo.setMatchRegex(info.getMatchRegex());
						reqInfo.setDelayed(info.getDelayed());
						reqInfo.setFees(info.getFee());
						reqInfo.setMsg(info.getMemo());
						sms = CommitMobileHelper.commitMobile(reqInfo);
						StringBuilder clog = new StringBuilder();
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("省份:").append(reqInfo.getProvince()).append("\n");
						clog.append("计费点:").append(reqInfo.getFee()).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						logger.info(clog.toString());
					} else {
						logger.warn("spid：" + spId + " 没有找到该通道");
					}
					OrderReqInfo order = new OrderReqInfo();
					order.setFfId(ffId);
					order.setMobile(mobile);
					commonAction.updateOrderInfo(order);
				} else {
					logger.warn("ffid：" + ffId + " 没有找到该订单");
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms.getBytes("gbk")).end();
	}
	/***
	 * 提交验证码
	 * 
	 * http://smspay.qygame.cn/code/commitCode1
	 * 例子:http://smspay.qygame.cn/code/commitCode1?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void commitCode1(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("commitCode1请求header信息："+request.allHeaders());
		String ffId = request.getParam("ffid");
		String code = request.getParam("code");
		String cpId = request.getParam("cpid");
		String traid = request.getParam("traid");
		
		String sms = "error";
		try {
			CPInfo cp = commonAction.queryCP(cpId);
			if (cp == null || cp.getIsOpen() == 0) {
				logger.warn("cpid：" + cpId + " 没有配置或打开计费权限");
				sms = "渠道没有开通";
				response.content(sms).end();
				return;
			} else if (code == null || code.length() <= 0) {
				logger.warn("cpid：" + cpId + " 验证码为空");
				sms = "验证码为空";
				response.content(sms).end();
				return;
			} else {
				OrderReqInfo reqInfo = commonAction.queryOrderReqInfo(ffId);
				if (reqInfo != null) {
					reqInfo.setCode(code);
					reqInfo.setTraid(traid);
					String spId = reqInfo.getSpId();
					CodeInfo info = commonAction.queryCodeInfoBySpid(spId);
					if (info != null) {
						reqInfo.setUrl(info.getUrl());
						reqInfo.setUrlNo(info.getUrlNo());
						reqInfo.setKeyword(info.getKeyword());
						reqInfo.setMatchRegex(info.getMatchRegex());
						reqInfo.setDelayed(info.getDelayed());
						reqInfo.setFees(info.getFee());
						reqInfo.setMsg(info.getMemo());
						sms = CommitCodeHelper.commitCode(reqInfo);
						StringBuilder clog = new StringBuilder();
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("省份:").append(reqInfo.getProvince()).append("\n");
						clog.append("计费点:").append(reqInfo.getFee()).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						logger.info(clog.toString());
					} else {
						logger.warn("spid：" + spId + " 没有找到该通道");
					}
					OrderReqInfo order = new OrderReqInfo();
					order.setFfId(ffId);
					order.setPmodel(code);
					commonAction.updateOrderInfo(order);
				} else {
					logger.warn("ffid：" + ffId + " 没有找到该订单");
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms.getBytes("gbk")).end();
	}
	
	/***
	 *  淘宝问题
	 * 
	 * http://168.192.0.104/code/commitCode
	 * 例子:http://168.192.0.104/code/commitCode?cpid=test02&phone=13762143362&TB=01&orderid=1112sd2317621067515
	 * 
	 */
	public void commitCodeTB(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("commitCodeTB请求header信息："+request.allHeaders());
		String ffId = request.getParam("orderid");
		String cpId = request.getParam("cpid");
		String codes = request.getParam("code");
		String code = null;
		if(codes.length()>6){
			code = codes.substring(codes.length()-6, codes.length());
		}else{
			code = codes;
		}
		
		String smscontent = request.getParam("smscontent");
		if (smscontent != null && smscontent.length() > 0) 
			smscontent = URLDecoder.decode(smscontent, "utf-8");
		String sreqNum = request.getParam("num");
		int reqNum = 0;
		if (sreqNum != null && !"".equals(sreqNum)) 
			reqNum = CommonTool.convertInt(sreqNum);
		
		String sms = "error";
		try {
			CPInfo cp = commonAction.queryCP(cpId);
			if (cp == null || cp.getIsOpen() == 0) {
				logger.warn("cpid：" + cpId + " 没有配置或打开计费权限");
				response.content(sms).end();
				return;
			} else {
				OrderReqInfo reqInfo = commonAction.queryOrderReqInfoDate(ffId);
				if (reqInfo != null) {
//					reqInfo.setTraid(traid);
					reqInfo.setCode(code);
					reqInfo.setSmscontent(smscontent);
					reqInfo.setNum(reqNum+"");
					String spId = reqInfo.getSpId();
					CodeInfo info = commonAction.queryCodeInfoBySpid(spId);
					if (info != null) {
						reqInfo.setUrl(info.getUrl());
						reqInfo.setUrlNo(info.getUrlNo());
						reqInfo.setKeyword(info.getKeyword());
						reqInfo.setMatchRegex(info.getMatchRegex());
						reqInfo.setDelayed(info.getDelayed());
						reqInfo.setFees(info.getFee());
						reqInfo.setMsg(info.getMemo());
						sms = CodeCommitReqHelper.commitCodeResult(reqInfo);
						StringBuilder clog = new StringBuilder();
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("省份:").append(reqInfo.getProvince()).append("\n");
						clog.append("计费点:").append(reqInfo.getFee()).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						logger.info(clog.toString());
					} else {
						logger.warn("spid：" + spId + " 没有找到该通道");
					}
					OrderReqInfo order = new OrderReqInfo();
					order.setFfId(ffId);
					
					if (smscontent != null && smscontent.length() > 0) {
						order.setCtech(smscontent);
					} else {
						order.setCtech(code);
					}
//					commonAction.updateOrderInfo(order);
					commonAction.updateOrderInfoDate(order);
				} else {
					logger.warn("ffid：" + ffId + " 没有找到该订单");
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms.getBytes("gbk")).end();
	}
	 
	
	
	/***
	 * 飞信
	 * 
	 * http://120.24.88.90/code/commitCode
	 * 例子:http://120.24.88.90/code/commitCode?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void commitCodeFX(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("commitCode请求header信息："+request.allHeaders());
		String ffId = request.getParam("orderid");
		String traid = request.getParam("traid");
		String cpId = request.getParam("cpid");
		String codes = request.getParam("code");
		String orderId = request.getParam("orderId");
		String code = null;
		if(codes!=null&&codes.length()>6){
			code = codes.substring(codes.length()-6, codes.length());
		}else{
			code = codes;
		}
		
		String smscontent = request.getParam("smscontent");
		if (smscontent != null && smscontent.length() > 0) 
			smscontent = URLDecoder.decode(smscontent, "utf-8");
		String sreqNum = request.getParam("num");
		int reqNum = 0;
		if (sreqNum != null && !"".equals(sreqNum)) 
			reqNum = CommonTool.convertInt(sreqNum);
		
		String sms = "error";
		try {
			CPInfo cp = commonAction.queryCP(cpId);
			if (cp == null || cp.getIsOpen() == 0) {
				logger.warn("cpid：" + cpId + " 没有配置或打开计费权限");
				response.content(sms).end();
				return;
			} else {
				OrderReqInfo reqInfo = commonAction.queryOrderReqInfoDate(ffId);
				if (reqInfo != null) {
					reqInfo.setTraid(traid);
					reqInfo.setCode(code);
					reqInfo.setSmscontent(smscontent);
					reqInfo.setNum(reqNum+"");
					reqInfo.setOrderId(orderId);
					String spId = reqInfo.getSpId();
					CodeInfo info = commonAction.queryCodeInfoBySpid(spId);
					if (info != null) {
						reqInfo.setUrl(info.getUrl());
						reqInfo.setUrlNo(info.getUrlNo());
						reqInfo.setKeyword(info.getKeyword());
						reqInfo.setMatchRegex(info.getMatchRegex());
						reqInfo.setDelayed(info.getDelayed());
						reqInfo.setFees(info.getFee());
						reqInfo.setMsg(info.getMemo());
//						reqInfo.setPmodel(orderId);
						String orderID = reqInfo.getPmodel();
						
						
						
						sms = CodeCommitReqHelper.commitCodeResult(reqInfo);
						StringBuilder clog = new StringBuilder();
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("省份:").append(reqInfo.getProvince()).append("\n");
						clog.append("计费点:").append(reqInfo.getFee()).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						logger.info(clog.toString());
					} else {
						logger.warn("spid：" + spId + " 没有找到该通道");
					}
					OrderReqInfo order = new OrderReqInfo();
					order.setFfId(ffId);
					
					if (smscontent != null && smscontent.length() > 0) {
						order.setCtech(smscontent);
					} else {
						order.setCtech(code);
					}
					commonAction.updateOrderInfoDate(order);
				} else {
					logger.warn("ffid：" + ffId + " 没有找到该订单");
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms.getBytes("gbk")).end();
	}
	
	/***
	 * 获取包月代码
	 * 
	 * http://120.24.88.90/code/commitCode
	 * 例子:http://120.24.88.90/code/commitCode?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void commitCode(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("commitCode请求header信息："+request.allHeaders());
		String ffId = request.getParam("orderid");
		String traid = request.getParam("traid");
		String cpId = request.getParam("cpid");
		String codes = request.getParam("code");
		String code = null;
		if(codes!=null&&codes.length()>6){
			code = codes.substring(codes.length()-6, codes.length());
		}else{
			code = codes;
		}
		
		String smscontent = request.getParam("smscontent");
		if (smscontent != null && smscontent.length() > 0) 
			smscontent = URLDecoder.decode(smscontent, "utf-8");
		String sreqNum = request.getParam("num");
		int reqNum = 0;
		if (sreqNum != null && !"".equals(sreqNum)) 
			reqNum = CommonTool.convertInt(sreqNum);
		
		String sms = "error";
		try {
			CPInfo cp = commonAction.queryCP(cpId);
			if (cp == null || cp.getIsOpen() == 0) {
				logger.warn("cpid：" + cpId + " 没有配置或打开计费权限");
				response.content(sms).end();
				return;
			} else {
				OrderReqInfo reqInfo = commonAction.queryOrderReqInfoDate(ffId);
				if (reqInfo != null) {
					reqInfo.setTraid(traid);
					reqInfo.setCode(code);
					reqInfo.setSmscontent(smscontent);
					reqInfo.setNum(reqNum+"");
					String spId = reqInfo.getSpId();
					CodeInfo info = commonAction.queryCodeInfoBySpid(spId);
					if (info != null) {
						reqInfo.setUrl(info.getUrl());
						reqInfo.setUrlNo(info.getUrlNo());
						reqInfo.setKeyword(info.getKeyword());
						reqInfo.setMatchRegex(info.getMatchRegex());
						reqInfo.setDelayed(info.getDelayed());
						reqInfo.setFees(info.getFee());
						reqInfo.setMsg(info.getMemo());
						sms = CodeCommitReqHelper.commitCodeResult(reqInfo);
						StringBuilder clog = new StringBuilder();
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("省份:").append(reqInfo.getProvince()).append("\n");
						clog.append("计费点:").append(reqInfo.getFee()).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						logger.info(clog.toString());
					} else {
						logger.warn("spid：" + spId + " 没有找到该通道");
					}
					OrderReqInfo order = new OrderReqInfo();
					order.setFfId(ffId);
					
					if (smscontent != null && smscontent.length() > 0) {
						order.setCtech(smscontent);
					} else {
						order.setCtech(code);
					}
					commonAction.updateOrderInfoDate(order);
				} else {
					logger.warn("ffid：" + ffId + " 没有找到该订单");
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms.getBytes("gbk")).end();
	}
	/***
	 * jd验证码瞿
	 * 
	 * http://120.24.88.90/code/commitCode
	 * 例子:http://120.24.88.90/code/commitCode?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void commitCodejd(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("commitCode请求header信息："+request.allHeaders());
		String ffId = request.getParam("orderid");
		String traid = request.getParam("traid");
		String cpId = request.getParam("cpid");
		String codes = request.getParam("code");
		String imsi = URLDecoder.decode(request.getParam("time"), "utf-8");
		String code = codes.substring(codes.length()-6, codes.length());
		String smscontent = request.getParam("smscontent");
		if (smscontent != null && smscontent.length() > 0) 
			smscontent = URLDecoder.decode(smscontent, "utf-8");
		String sreqNum = request.getParam("num");
		int reqNum = 0;
		if (sreqNum != null && !"".equals(sreqNum)) 
			reqNum = CommonTool.convertInt(sreqNum);
		
		String sms = "error";
		try {
			CPInfo cp = commonAction.queryCP(cpId);
			if (cp == null || cp.getIsOpen() == 0) {
				logger.warn("cpid：" + cpId + " 没有配置或打开计费权限");
				response.content(sms).end();
				return;
			} else {
				OrderReqInfo reqInfo = commonAction.queryOrderReqInfo(ffId);
				if (reqInfo != null) {
					reqInfo.setTraid(traid);
					reqInfo.setCode(code);
					reqInfo.setSmscontent(smscontent);
					reqInfo.setNum(reqNum+"");
					String spId = reqInfo.getSpId();
					CodeInfo info = commonAction.queryCodeInfoBySpid(spId);
					if (info != null) {
						reqInfo.setUrl(info.getUrl());
						reqInfo.setUrlNo(info.getUrlNo());
						reqInfo.setKeyword(info.getKeyword());
						reqInfo.setMatchRegex(info.getMatchRegex());
						reqInfo.setDelayed(info.getDelayed());
						reqInfo.setFees(info.getFee());
						reqInfo.setMsg(info.getMemo());
						reqInfo.setImsi(imsi);
						sms = CodeCommitReqHelper.commitCodeResult(reqInfo);
						StringBuilder clog = new StringBuilder();
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("省份:").append(reqInfo.getProvince()).append("\n");
						clog.append("计费点:").append(reqInfo.getFee()).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						logger.info(clog.toString());
					} else {
						logger.warn("spid：" + spId + " 没有找到该通道");
					}
					OrderReqInfo order = new OrderReqInfo();
					order.setFfId(ffId);
					
					if (smscontent != null && smscontent.length() > 0) {
						order.setCtech(smscontent);
					} else {
						order.setCtech(code);
					}
					commonAction.updateOrderInfo(order);
				} else {
					logger.warn("ffid：" + ffId + " 没有找到该订单");
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms.getBytes("gbk")).end();
	}
	/***
	 * 提交图片
	 * 
	 * http://118.190.90.1/code/commitImg
	 * 例子:http://118.190.90.1/code/commitImg?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void commitImg(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("commitImg请求header信息："+request.allHeaders());
		String body = request.body();
		logger.info("commitImg请求body信息："+body);
		
		String ffId = request.getParam("orderid");
		String traid = request.getParam("traid");
		String cpId = request.getParam("cpid");
		String code = request.getParam("code");
		String smscontent = request.getParam("smscontent");
		if (smscontent != null && smscontent.length() > 0) 
			smscontent = URLDecoder.decode(smscontent, "utf-8");
		String sreqNum = request.getParam("num");
		int reqNum = 0;
		if (sreqNum != null && !"".equals(sreqNum)) 
			reqNum = CommonTool.convertInt(sreqNum);
		
		String sms = "error";
		try {
			CPInfo cp = commonAction.queryCP(cpId);
			if (cp == null || cp.getIsOpen() == 0) {
				logger.warn("cpid：" + cpId + " 没有配置或打开计费权限");
				response.content(sms).end();
				return;
			} else {
				OrderReqInfo reqInfo = commonAction.queryOrderReqInfo(ffId);
				if (reqInfo != null) {
					reqInfo.setBody(body);
					reqInfo.setTraid(traid);
					reqInfo.setCode(code);
					reqInfo.setSmscontent(smscontent);
					reqInfo.setNum(reqNum+"");
					String spId = reqInfo.getSpId();
					CodeInfo info = commonAction.queryCodeInfoBySpid(spId);
					if (info != null) {
						reqInfo.setUrl(info.getUrl());
						reqInfo.setUrlNo(info.getUrlNo());
						reqInfo.setKeyword(info.getKeyword());
						reqInfo.setMatchRegex(info.getMatchRegex());
						reqInfo.setDelayed(info.getDelayed());
						reqInfo.setFees(info.getFee());
						reqInfo.setMsg(info.getMemo());
						sms = CodeCommitReqHelper.commitCodeResult(reqInfo);
						StringBuilder clog = new StringBuilder();
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("省份:").append(reqInfo.getProvince()).append("\n");
						clog.append("计费点:").append(reqInfo.getFee()).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						logger.info(clog.toString());
					} else {
						logger.warn("spid：" + spId + " 没有找到该通道");
					}
					OrderReqInfo order = new OrderReqInfo();
					order.setFfId(ffId);
					
					if (smscontent != null && smscontent.length() > 0) {
						order.setCtech(smscontent);
					} else {
						order.setCtech(code);
					}
					commonAction.updateOrderInfo(order);
				} else {
					logger.warn("ffid：" + ffId + " 没有找到该订单");
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms.getBytes("gbk")).end();
	}
	
	/***
	 * 获取包月代码
	 * 
	 * http://120.24.88.90/code/commitRegisterCode
	 * 例子:http://120.24.88.90/code/commitCode?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void commitRegisterCode(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("commitRegisterCode请求header信息："+request.allHeaders());
		String ffId = request.getParam("orderid");
		String traid = request.getParam("traid");
		String cpId = request.getParam("cpid");
		String code = request.getParam("code");
		String smscontent = request.getParam("smscontent");
		if (smscontent != null && smscontent.length() > 0) 
			smscontent = URLDecoder.decode(smscontent, "utf-8");
		String sreqNum = request.getParam("num");
		int reqNum = 0;
		if (sreqNum != null && !"".equals(sreqNum)) 
			reqNum = CommonTool.convertInt(sreqNum);
		
		String sms = "error";
		try {
			CPInfo cp = commonAction.queryCP(cpId);
			if (cp == null || cp.getIsOpen() == 0) {
				logger.warn("cpid：" + cpId + " 没有配置或打开计费权限");
				response.content(sms).end();
				return;
			} else {
				OrderReqInfo reqInfo = commonAction.queryOrderReqInfo(ffId);
				if (reqInfo != null) {
					reqInfo.setTraid(traid);
					reqInfo.setCode(code);
					reqInfo.setSmscontent(smscontent);
					reqInfo.setNum(reqNum+"");
					String spId = reqInfo.getSpId();
					CodeInfo info = commonAction.queryCodeInfoBySpid(spId);
					if (info != null) {
						reqInfo.setUrl(info.getUrl());
						reqInfo.setUrlNo(info.getUrlNo());
						reqInfo.setKeyword(info.getKeyword());
						reqInfo.setMatchRegex(info.getMatchRegex());
						reqInfo.setDelayed(info.getDelayed());
						reqInfo.setFees(info.getFee());
						reqInfo.setMsg(info.getMemo());
						sms = CodeReqHelper.getSMSCode(reqInfo);
						StringBuilder clog = new StringBuilder();
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("省份:").append(reqInfo.getProvince()).append("\n");
						clog.append("计费点:").append(reqInfo.getFee()).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						logger.info(clog.toString());
					} else {
						logger.warn("spid：" + spId + " 没有找到该通道");
					}
					OrderReqInfo order = new OrderReqInfo();
					order.setFfId(ffId);
					order.setCtech(code);
					commonAction.updateOrderInfo(order);
				} else {
					logger.warn("ffid：" + ffId + " 没有找到该订单");
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
	}
	
	/***
	 * 测试
	 * 
	 * http://120.24.88.90/code/test
	 */
	public void test(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("test请求header信息："+request.allHeaders());
		
		String url = "http://www.baidu.com";
		
		String res = HttpTool.sendGet("http://121.40.72.80/lwpay/test", "1=1");
		logger.error("test请求信息："+res);
		StringBuilder sb = new StringBuilder();
		sb.append("<!doctype html>");
		sb.append("<html lang=\"en\">");
		sb.append("<head>");
		sb.append("<meta charset=\"UTF-8\">");
		sb.append("<meta name=\"Generator\" content=\"EditPlus?\">");
		sb.append("<meta name=\"Author\" content=\"\">");
		sb.append("<meta name=\"Keywords\" content=\"\">");
		sb.append("<meta name=\"Description\" content=\"\">");
		sb.append("<title>微信H5支付</title>");
		sb.append("<script language=\"javascript\" type=\"text/javascript\">");
		sb.append("window.location='").append(url).append("'");
		sb.append("</script>");
		sb.append("</head><body> </body></html>");
		
		String result = sb.toString();
		response.content(result).end();
	}
	
	/***
	 * 获取包月代码
	 * 
	 * http://120.24.88.90/code/getDXBYcode
	 * 例子:http://120.24.88.90/code/getBYcode?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void getDXBYcode(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("getDXBYcode请求header信息："+request.allHeaders());
		String spId = "ec09";
		OrderReqInfo reqInfo = reqParamConver(request);//获取请求参数
		String ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), "ec");
		reqInfo.setSpId(spId);
		reqInfo.setFfId(ffId);
		CodeT ct = commonAction.getPointPayCodes(reqInfo);//获取对应cpId打开的计费通道
		if (ct == null) {
			response.content("error").end();
		} else {
			CodeInfo codeInfo = ct.getCodes().get(0);
			String key = "6nV6B2tn";
			String channelId = "57";
			String paycode = "90216617";
			String phone = "0";
			String checkStr = paycode+phone+reqInfo.getImsi()+reqInfo.getImei()+channelId+key;
			String mac = MD5Tool.toMD5Value32(checkStr).toUpperCase();
			StringBuffer param = new StringBuffer();
			param.append("channelId=").append(channelId).append("&payCode=").append(paycode)
			.append("&phone=").append(phone).append("&imsi=").append(reqInfo.getImsi())
			.append("&imei=").append(reqInfo.getImei()).append("&extra=").append(ffId)
			.append("&ip=").append(reqInfo.getIp()).append("&mac=").append(mac);
			String result = HttpTool.sendHttp(codeInfo.getUrl()+"?"+param.toString(), "");
			ct.setFfId(ffId);
			commonAction.addOrderReqInfo(ct, reqInfo);
			response.content(result).end();
		}
	}
	
	public void getDXBYPay(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("getDXBYPay请求header信息："+request.allHeaders());
		String spId = "ec09";
		OrderReqInfo reqInfo = reqParamConver(request);//获取请求参数
		String recordId = request.getParam("recordId");
		String vcode = request.getParam("vcode");
		reqInfo.setSpId(spId);
		CodeT ct = commonAction.getPointPayCodes(reqInfo);//获取对应cpId打开的计费通道
		if (ct == null) {
			response.content("error").end();
		} else {
			CodeInfo codeInfo = ct.getCodes().get(0);
			String key = "6nV6B2tn";
			String channelId = "57";
			String checkStr = channelId+recordId+vcode+key;
			String mac = MD5Tool.toMD5Value32(checkStr).toUpperCase();
			StringBuffer param = new StringBuffer();
			param.append("channelId=").append(channelId).append("&recordId=").append(recordId)
			.append("&vcode=").append(vcode).append("&mac=").append(mac);
			String result = HttpTool.sendHttp(codeInfo.getKeyword()+"?"+param.toString(), "");
			response.content(result).end();
		}
	}
	
	/**
	 * 查询是否提交过12306注册
	 * @param request
	 * @param response
	 * @throws Exception
	 * http://118.190.90.1/code/query12306Regiseter?mobile=13762143362
	 */
	public void query12306Regiseter(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("query12306Regiseter请求header信息："+request.allHeaders());
		String result = "error";
		String mobile = request.getParam("mobile");
		if (mobile != null && mobile.matches(ConstantDefine.REGEX_MOBILE_12306)) {
			MobileInfo info = commonAction.query12306commit(mobile);
			if (info == null) {
				result = "ok";
			} else {
				result = "号码已经提交过";
			}
		} else {
			result = "手机号为空或提交手机号不符合注册号段";
		}
		logger.info("query12306Regiseter查询结果："+result);
		response.content(result).end();
	}
	
	/**
	 * 查询是否提交过12306注册
	 * @param request
	 * @param response
	 * @throws Exception
	 * http://118.190.90.1/code/query12306Result?mobile=13762143362
	 */
	public void query12306Result(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("query12306Result请求header信息："+request.allHeaders());
		String result = "error"; 
		String mobile = request.getParam("mobile");
		String url = "http://njoncol.com/q.php";
		String param = "p=" + mobile;
		result = HttpTool.sendGetSetTimeout(url, param, "2000");
		logger.info("query12306Result查询结果："+result);
		response.content(result.getBytes("gbk")).end();
	}
	
	/***
	 * 获取音乐代码
	 * 
	 * http://120.24.88.90/code/getLDcode
	 * 例子:http://120.24.88.90/code/getBYcode?imei=867120021362452&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=460029724046262&phone=13762143362&sp_id=xxxx
	 */
	public void getLDcode(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("getBYcode请求header信息："+request.allHeaders());
		OrderReqInfo reqInfo = reqParamConver(request);//获取请求参数
		String spId = reqInfo.getSpId();
		String result = "error";
		if (spId != null && spId.length() > 0) {
//			String ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), spId.substring(0,2));
			CodeT ct = commonAction.getPayCode(reqInfo);//获取对应cpId打开的计费通道
			if (ct != null) {
				String ffId = ct.getFfId();
				
				CodeInfo info = ct.getCodes().get(0);
				
				reqInfo.setFfId(ffId);
				reqInfo.setUrl(info.getUrl());
				reqInfo.setUrlNo(info.getUrlNo());
				reqInfo.setReqMethod(info.getReqMethod());
				reqInfo.setKeyword(info.getKeyword());
				reqInfo.setMatchRegex(info.getMatchRegex());
				reqInfo.setFees(info.getFee());
				
				result = CodeReqHelper.getSMSCode(reqInfo);
				commonAction.addOrderReqInfo(ct, reqInfo);
			}
		}
		
		response.content(result).end();
	}
	
	/***
	 * 获取MM代码
	 * 
	 * http://120.24.88.90/code/getMMcode
	 * 例子:http://120.24.88.90/code/getMMcode?cpparam=15614383&imei=867427020866445&iccid=89860059179493636085
	 * 		&nwtype=WIFI&ctech=WIFI&cpid=test02&cid=7152&osversion=4.4.2&lac=28893&pmodel=BF_T18&fee=1000
	 * 		&imsi=46000363099391
	 */
	public void getMMcode(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("getMMcode请求header信息："+request.allHeaders());
		
		OrderReqInfo reqInfo = reqParamConver(request);
		
		String spId = reqInfo.getSpId();
		String cpId = reqInfo.getCpId();
		String province = reqInfo.getProvince();
		
		String pmodel = reqInfo.getPmodel();			// 手机型号
		String osversion = reqInfo.getOsversion();	// 系统版本
		String nwtype = reqInfo.getNwtype();			// 网络类型
		String ctech = reqInfo.getCtech();			// 通信制式 GSM、CDMA、3G、4G等
		pmodel = pmodel == null?"MI 2S":pmodel;
		osversion = osversion == null?"5.0.2":osversion;
		nwtype = nwtype == null?"WIFI":nwtype;
		ctech = ctech == null?"WIFI":ctech;
		reqInfo.setPmodel(pmodel);
		reqInfo.setOsversion(osversion);
		reqInfo.setNwtype(nwtype);
		reqInfo.setCtech(ctech);
		
		int fee = reqInfo.getFee();
		
		String sms = "error";
		spId = spId == null?"wd99":spId;
		PayResultJson resultJson = new PayResultJson();
		String result = "error";
//		int codeType = CommonTool.locateOperator(imsi);//1移动 2联通 3电信
		try {
			String ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), "oo");
			CodeT ct = commonAction.getPayCodes(reqInfo);//获取对应cpId打开的计费通道
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 省份=" + province + " 计费点="
						+ fee + " 没有匹配到计费通道！");
				ct = new CodeT();
				ct.setFfId(ffId);
				reqInfo.setSpId(spId);
				reqInfo.setIsSuccess(9001);
				commonAction.addOrderReqInfo(ct, reqInfo);
				resultJson.setCode(ConstantDefine.ERROR_9001);
				resultJson.setCode_explain(URLEncoder.encode(ConstantDefine.ERROR_9001_MSG,"utf-8"));
				response.content(JSONObject.toJSONString(resultJson)).end();
				return;
			} else {
				BaseStationInfo bs = commonAction.getBaseStation(reqInfo);
				if (bs != null) {
					ct.setFfId(ffId);
					reqInfo.setSpId(spId);
					reqInfo.setIsSuccess(9004);
					commonAction.addOrderReqInfo(ct, reqInfo);
					resultJson.setCode(ConstantDefine.ERROR_9004);
					resultJson.setCode_explain(URLEncoder.encode(ConstantDefine.ERROR_9004_MSG,"utf-8"));
					response.content(JSONObject.toJSONString(resultJson)).end();
					return;
				}
				
				ffId = ct.getFfId();
				String spName = null;
				reqInfo.setSpId(spId);
				for (CodeInfo info : ct.getCodes()) {
					logger.info("通道号spid="+info.getSpId());
					
					ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), info.getSpFlag());
					
					reqInfo.setFfId(ffId);
					reqInfo.setUrl(info.getUrl());
					reqInfo.setUrlNo(info.getUrlNo());
					reqInfo.setReqMethod(info.getReqMethod());
					reqInfo.setKeyword(info.getKeyword());
					reqInfo.setMatchRegex(info.getMatchRegex());
					reqInfo.setFees(info.getFee());
					
					sms = CodeReqHelper.getSMSCode(reqInfo);
					
					if (!"error".equals(sms)) {
						spId = info.getSpId();
						spName = info.getName();
						ct.setFfId(ffId);
						ct.setSpId(spId);
						reqInfo.setSpId(spId);
						reqInfo.setFfId(ffId);
						break;
					} else {
						ct.setSpId(spId);
						reqInfo.setSpId(spId);
					}
				}
				
				/*if ("error".equals(sms)) {
					reqInfo.setIsSuccess(9002);
					resultJson.setCode(ConstantDefine.ERROR_9002);
					resultJson.setCode_explain(URLEncoder.encode(ConstantDefine.ERROR_9002_MSG,"utf-8"));
					result = JSONObject.toJSONString(resultJson);
				} else {
					result = sms;
				}*/
				result = sms;
				
				StringBuilder clog = new StringBuilder();
				clog.append("合作方:").append(ct.getCpName()).append("\n");
				clog.append("通道方:").append(spName).append("\n");
				clog.append("通道spId:").append(spId).append("\n");
				clog.append("省份:").append(province).append("\n");
				clog.append("计费点:").append(fee).append("\n");
				clog.append("订单号:").append(ffId).append("\n");
				clog.append("指令内容:").append(result);
				logger.info(clog.toString());
				
				commonAction.addOrderReqInfo(ct, reqInfo);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(result).end();
	}
	
	private OrderReqInfo reqParamConver(HttpRequest request) {
		
		String cpId = request.getParam("cpid");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String aimsi = request.getParam("aimsi");
		String mobile = request.getParam("phone");
		String param = request.getParam("cpparam");
		String pmodel = request.getParam("pmodel");			// 手机型号
		String osversion = request.getParam("osversion");	// 系统版本
		String nwtype = request.getParam("nwtype");			// 网络类型
		String ctech = request.getParam("ctech");			// 通信制式 GSM、CDMA、3G、4G等
		String fee = request.getParam("fee");
		String pck = request.getParam("pck");//包名
		String app = request.getParam("app");//应用名称
		String propname = request.getParam("propname");//道具名称
		String enMethod = request.getParam("enMethod");
		String scount = request.getParam("count");
		String sreqNum = request.getParam("num");
		String sdkVer = request.getParam("sdkVer");
		String mmAppid = request.getParam("mm_appid");
		String mmChannel = request.getParam("mm_channel");
		String spId = request.getParam("sp_id");
		sdkVer = sdkVer==null?"1.1.0.0":sdkVer;
		int reqNum = 0;
		if (sreqNum != null && !"".equals(sreqNum)) 
			reqNum = CommonTool.convertInt(sreqNum);
		reqNum += 1;
		int count = 1;
		if (scount != null && !"".equals(scount)) 
			count = CommonTool.convertInt(scount);
		if (fee == null || fee.length() <= 0 ) fee = "0";
		
		String cid = request.getParam("cid");//基站信息
		String lac = request.getParam("lac");//基站信息
		String lat = request.getParam("lat");//经度
		String lon = request.getParam("lon");//纬度
		
		String username = request.getParam("username");
		String password = request.getParam("password");
		
		int phoneType = 0;//1移动 2联通 3电信
		String iccid = request.getParam("iccid");
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		Map<String, String> areaMap = queryReqArea(imsi, mobile, iccid, ip);
		mobile = areaMap.get("mobile");
		String province = areaMap.get("province");
		phoneType = CommonTool.convertInt(areaMap.get("phoneType"));
		String city = areaMap.get("city");
		
		
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setCpId(cpId);
		reqInfo.setImsi(imsi);
		reqInfo.setImei(imei);
		reqInfo.setAimsi(aimsi);
		reqInfo.setMobile(mobile);
		reqInfo.setCpParam(param);
		reqInfo.setPmodel(pmodel);
		reqInfo.setOsversion(osversion);
		reqInfo.setNwtype(nwtype);
		reqInfo.setCtech(ctech);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setPrice(fee);
		reqInfo.setPck(pck);
		reqInfo.setApp(app);
		reqInfo.setIp(ip);
		reqInfo.setProvince(province);
		reqInfo.setIccid(iccid);
		reqInfo.setCount(count);
		reqInfo.setEnMethod(enMethod);
		reqInfo.setBscCid(cid);
		reqInfo.setBscLac(lac);
		reqInfo.setGpsLat(lat);
		reqInfo.setGpsLng(lon);
		reqInfo.setNum(reqNum+"");
		reqInfo.setSdkVer(sdkVer);
		reqInfo.setMmAppid(mmAppid);
		reqInfo.setMmChannel(mmChannel);
		reqInfo.setCity(city);
		reqInfo.setSpId(spId);
		reqInfo.setUsername(username);
		reqInfo.setPassword(password);
		reqInfo.setPhoneType(phoneType);
		reqInfo.setPropname(propname);
		
		return reqInfo;
	}
	
	private OrderReqInfo reqAESParamConver(HttpRequest request) throws Exception {
		
		String content = request.body();
		OrderReqInfo reqInfo = new OrderReqInfo();
		logger.info("请求AES加密信息：" + content);
		
		if (content == null) return reqInfo;
		int len = content.length();
		if (len <= 16) return reqInfo;
		String secretKey = content.substring(len-16, len);
		String body = content.substring(0, len-16);
		String condec = AESUtil.decrypt(secretKey, body);
		logger.info("请求AES解密后信息：" + condec);
		JSONObject myObj = JSONObject.parseObject(condec);
		
		
		String orderId = myObj.getString("orderid");
		String pmodel = myObj.getString("model");			// 手机型号
		String osversion = myObj.getString("osversion");	// 系统版本
		String cpId = myObj.getString("cpid");
		String imsi = myObj.getString("imsi");
		String imei = myObj.getString("imei");
		String mobile = myObj.getString("phone");
		String param = myObj.getString("cpparam");
		String fee = myObj.getString("fee");
		String sreqNum = myObj.getString("num");
		String spId = myObj.getString("sp_id");
		String username = myObj.getString("username");
		String password = myObj.getString("password");
		String code = myObj.getString("code");
		String traid = myObj.getString("traid");
		String province = myObj.getString("province");
		String smscontent = myObj.getString("smscontent");
		if (smscontent != null && smscontent.length() > 0) 
			smscontent = URLDecoder.decode(smscontent, "utf-8");
		int reqNum = 0;
		if (sreqNum != null && !"".equals(sreqNum)) 
			reqNum = CommonTool.convertInt(sreqNum);
		if (fee == null || fee.length() <= 0 ) fee = "0";
		
		
		String ip = myObj.getString("ip");
		ip = ip==null?getRealIP(request):ip;
		logger.info("用户访问IP=" + ip);
		
		int phoneType = 0;//1移动 2联通 3电信
		String iccid = myObj.getString("iccid");
		
		String regex = "^\\d++";
		if (province != null && province.length() > 0 && !province.matches(regex)) {
			province = URLDecoder.decode(province,"utf-8");
		} else {
			if (iccid != null && iccid.length()>10) {
				String mobileOperatorNum = iccid.substring(4,6);//00 02 移动 ;01 09 联通;03 06 电信
				logger.info("用户访问iccid=" + iccid);
				if ("03".equals(mobileOperatorNum) || "06".equals(mobileOperatorNum)) {
					logger.info("用户为电信手机依据ip判定省份" + iccid);
					phoneType = 3;
				} else {
					if ("00".equals(mobileOperatorNum) || "02".equals(mobileOperatorNum)) {
						province = provinceMap.get(iccid.substring(8,10));
						phoneType = 1;
					} else if ("01".equals(mobileOperatorNum) || "09".equals(mobileOperatorNum)) {
						province = provinceLTMap.get(iccid.substring(9,11));
						phoneType = 2;
					} else {
						logger.info("用户手机归属省份未知");
					}
					logger.info("用户归属省份=" + province);
				}
			}
			if (province == null || province.length() <= 0) {
				province = IPtest.getInstance().queryProvince(ip);
				logger.info("根据IP查询省份=" + province);
			}
		}
		logger.info("用户访问phone=" + mobile);
		String city = null;
		if (province == null) {
			MobileInfo mobileInfo = getProvinceByPhone(mobile);
			if (mobileInfo != null) {
				province = mobileInfo.getProvince();
				city = mobileInfo.getCity();
				logger.info("根据phone查询省份地市：" + province + "_" + city);
			}
		}
		
		
		reqInfo.setCpId(cpId);
		reqInfo.setImsi(imsi);
		reqInfo.setImei(imei);
		reqInfo.setMobile(mobile);
		reqInfo.setCpParam(param);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setPrice(fee);
		reqInfo.setIp(ip);
		reqInfo.setNum(reqNum+"");
		reqInfo.setSpId(spId);
		reqInfo.setUsername(username);
		reqInfo.setPassword(password);
		reqInfo.setOrderId(orderId);
		reqInfo.setCode(code);
		reqInfo.setProvince(province);
		reqInfo.setTraid(traid);
		reqInfo.setIccid(iccid);
		reqInfo.setCodeType(phoneType);
		reqInfo.setSmscontent(smscontent);
		reqInfo.setPmodel(pmodel);
		reqInfo.setOsversion(osversion);
		
		return reqInfo;
	}
	
	private OrderReqInfo reqSDKAESParamConver(HttpRequest request) throws Exception {
		
		String content = request.body();
		OrderReqInfo reqInfo = new OrderReqInfo();
		logger.info("请求SDKAES加密信息：" + content);
		
		if (content == null) return reqInfo;
		int len = content.length();
		if (len <= 16) return reqInfo;
		String secretKey = content.substring(len-16, len);
		String body = content.substring(0, len-16);
		String condec = AESUtil.decrypt(secretKey, body);
		logger.info("请求SDKAES解密后信息：" + condec);
		JSONObject myObj = JSONObject.parseObject(condec);
		
		
		String cpId = myObj.getString("cpid");
		String fee = myObj.getString("fee");
		String imsi = myObj.getString("imsi");
		String imei = myObj.getString("imei");
		String mobile = myObj.getString("phone");
		String cpparam = myObj.getString("cpparam");
		String pmodel = myObj.getString("pmodel");			// 手机型号
		String osversion = myObj.getString("osversion");	// 系统版本
		String nwtype = myObj.getString("nwtype");	// 网络类型
		String ctech = myObj.getString("ctech");	// 通信制式
		String pck = myObj.getString("pck");//包名
		String app = myObj.getString("app");//应用名称
		
		String sdkVer = myObj.getString("version");
		
		if (fee == null || fee.length() <= 0 ) fee = "0";
		if (pmodel == null || pmodel.length() <= 0) pmodel = "xs";
		
		String iccid = myObj.getString("iccid");
		String ip = myObj.getString("ip");
		ip = ip==null?getRealIP(request):ip;
		Map<String, String> areaMap = queryReqArea(imsi, mobile, iccid, ip);
		mobile = areaMap.get("mobile");
		String province = areaMap.get("province");
		int phoneType = CommonTool.convertInt(areaMap.get("phoneType"));//1移动 2联通 3电信
		String city = areaMap.get("city");
		
		reqInfo.setCpId(cpId);
		reqInfo.setImsi(imsi);
		reqInfo.setImei(imei);
		reqInfo.setMobile(mobile);
		reqInfo.setCpParam(cpparam);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setPrice(fee);
		reqInfo.setIp(ip);
		reqInfo.setProvince(province);
		reqInfo.setIccid(iccid);
		reqInfo.setPhoneType(phoneType);
		reqInfo.setPmodel(pmodel);
		reqInfo.setOsversion(osversion);
		reqInfo.setNwtype(nwtype);
		reqInfo.setCtech(ctech);
		reqInfo.setPck(pck);
		reqInfo.setApp(app);
		reqInfo.setSdkVer(sdkVer);
		reqInfo.setCity(city);
		
		return reqInfo;
	}
	
	private OrderReqInfo reqSDKAESResParamConver(HttpRequest request) throws Exception {
		
		String orderId = request.getParam("orderid");
		OrderReqInfo reqInfo = commonAction.queryOrderReqInfo(orderId);
		
		if (reqInfo != null) {
			reqInfo.setOrderId(orderId);
			reqInfo.setPrice(reqInfo.getFee()+"");
			
			String ip = reqInfo.getIp();
			String iccid = reqInfo.getIccid();
			String mobile = reqInfo.getMobile();
			String imsi = reqInfo.getImsi();
			
			int phoneType = 0;//1移动 2联通 3电信
			
			String province = null;
			if (iccid != null && iccid.length()>10) {
				String mobileOperatorNum = iccid.substring(4,6);//00 02 移动 ;01 09 联通;03 06 电信
				if ("03".equals(mobileOperatorNum) || "06".equals(mobileOperatorNum)) {
					logger.info("电信用户依据iccid判定:" + iccid);
					phoneType = 3;
				} else {
					if ("00".equals(mobileOperatorNum) || "02".equals(mobileOperatorNum)) {
						province = provinceMap.get(iccid.substring(8,10));
						phoneType = 1;
						logger.info("移动用户依据iccid判定:" + iccid + "归属于：" + province);
					} else if ("01".equals(mobileOperatorNum) || "09".equals(mobileOperatorNum)) {
						province = provinceLTMap.get(iccid.substring(9,11));
						phoneType = 2;
						logger.info("联通用户依据iccid判定:" + iccid + "归属于：" + province);
					} else {
						logger.info("用户手机归属省份未知iccid：" + iccid);
					}
				}
			}
			
			
			String city = null;
			if (province == null) {
				MobileInfo mobileInfo = getProvinceByPhone(mobile);
				if (mobileInfo != null) {
					province = mobileInfo.getProvince();
					city = mobileInfo.getCity();
					String corp = mobileInfo.getCorp();
					if ("中国移动".equals(corp)) {
						phoneType = 1;
						logger.info("移动用户phone：" + mobile);
					} else if ("中国联通".equals(corp)) {
						phoneType = 2;
						logger.info("联通用户phone：" + mobile);
					} else {
						phoneType = 3;
						logger.info("电信用户phone：" + mobile);
					}
					logger.info("根据phone查询省份地市：" + province + "_" + city);
				}
			}
			
			if (province == null || province.length() <= 0) {
				province = IPtest.getInstance().queryProvince(ip);
				phoneType = CommonTool.locateOperator(imsi);
				logger.info("根据IP查询省份：" + province + " ,用户运营商类型：" + phoneType);
			}
			
			reqInfo.setPhoneType(phoneType);
			reqInfo.setCity(city);
		}
		
		
		return reqInfo;
	}
	
	private OrderReqInfo reqEasyParamConver(HttpRequest request) throws Exception {
		
		String orderId = request.getParam("orderid");
		String cpId = request.getParam("cpid");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String mobile = request.getParam("phone");
		String param = request.getParam("cpparam");
		String fee = request.getParam("fee");
		String sreqNum = request.getParam("num");
		String spId = request.getParam("sp_id");
		String username = request.getParam("username");
		String password = request.getParam("password");
		String code = request.getParam("code");
		String traid = request.getParam("traid");
		String province = request.getParam("province");
		String smscontent = request.getParam("smscontent");
		if (smscontent != null && smscontent.length() > 0) 
			smscontent = URLDecoder.decode(smscontent, "utf-8");
		int reqNum = 0;
		if (sreqNum != null && !"".equals(sreqNum)) 
			reqNum = CommonTool.convertInt(sreqNum);
		if (fee == null || fee.length() <= 0 ) fee = "0";
		
		
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		logger.info("用户访问IP=" + ip);
		
		int phoneType = 0;//1移动 2联通 3电信
		String iccid = request.getParam("iccid");
		
		String regex = "^\\d++";
		if (province != null && province.length() > 0 && !province.matches(regex)) {
			province = URLDecoder.decode(province,"utf-8");
		} else {
			if (iccid != null && iccid.length()>10) {
				String mobileOperatorNum = iccid.substring(4,6);//00 02 移动 ;01 09 联通;03 06 电信
				logger.info("用户访问iccid=" + iccid);
				if ("03".equals(mobileOperatorNum) || "06".equals(mobileOperatorNum)) {
					logger.info("用户为电信手机依据ip判定省份" + iccid);
					phoneType = 3;
				} else {
					if ("00".equals(mobileOperatorNum) || "02".equals(mobileOperatorNum)) {
						province = provinceMap.get(iccid.substring(8,10));
						phoneType = 1;
					} else if ("01".equals(mobileOperatorNum) || "09".equals(mobileOperatorNum)) {
						province = provinceLTMap.get(iccid.substring(9,11));
						phoneType = 2;
					} else {
						logger.info("用户手机归属省份未知");
					}
					logger.info("用户归属省份=" + province);
				}
			}
			if (province == null) {
				MobileInfo mobileInfo = getProvinceByPhone(mobile);
				if (mobileInfo != null) {
					province = mobileInfo.getProvince();
					String city = mobileInfo.getCity();
					String corp = mobileInfo.getCorp();
					if ("中国移动".equals(corp)) {
						phoneType = 1;
						logger.info("移动用户phone：" + mobile);
					} else if ("中国联通".equals(corp)) {
						phoneType = 2;
						logger.info("联通用户phone：" + mobile);
					} else {
						phoneType = 3;
						logger.info("电信用户phone：" + mobile);
					}
					logger.info("根据phone查询省份地市：" + province + "_" + city);
				}
			}
//			if (province == null || province.length() <= 0) {
//				province = IPtest.getInstance().queryProvince(ip);
//				logger.info("根据IP查询省份=" + province);
//			}
		}
		
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setCpId(cpId);
		reqInfo.setImsi(imsi);
		reqInfo.setImei(imei);
		reqInfo.setMobile(mobile);
		reqInfo.setCpParam(param);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setPrice(fee);
		reqInfo.setIp(ip);
		reqInfo.setNum(reqNum+"");
		reqInfo.setSpId(spId);
		reqInfo.setUsername(username);
		reqInfo.setPassword(password);
		reqInfo.setOrderId(orderId);
		reqInfo.setCode(code);
		reqInfo.setProvince(province);
		reqInfo.setTraid(traid);
		reqInfo.setIccid(iccid);
		reqInfo.setCodeType(phoneType);
		reqInfo.setSmscontent(smscontent);
		
		return reqInfo;
	}
	
	/***
	 * 获取第三方计费指令
	 * 
	 * http://120.24.88.90/code/aliPay
	 * 例子:http://120.24.88.90/code/aliPay?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89
	 */
	public void aliPay(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("aliPay请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String aimsi = request.getParam("aimsi");
		String mobile = request.getParam("phone");
		String param = request.getParam("cpparam");
		String pmodel = request.getParam("pmodel");			// 手机型号
		String osversion = request.getParam("osversion");	// 系统版本
		String nwtype = request.getParam("nwtype");			// 网络类型
		String ctech = request.getParam("ctech");			// 通信制式 GSM、CDMA、3G、4G等
		String fee = request.getParam("fee");
		String pck = request.getParam("pck");//包名
		String app = request.getParam("app");//应用名称
		String sdkVer = request.getParam("sdkVer");
		String mmAppid = request.getParam("mm_appid");
		String mmChannel = request.getParam("mm_channel");
		String spId = "sd02";//支付宝
		String partner = "2088612889828639";//商户支付宝账号PID
		String seller = "2669737607@qq.com";//商户收款支付宝账号
		
		String province = null;
		String iccid = request.getParam("iccid");
		if (iccid != null && iccid.length()>10) {
			logger.info("用户访问iccid=" + iccid);
			province = provinceMap.get(iccid.substring(8,10));
			logger.info("用户省份=" + province);
		}
		
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		logger.info("用户访问IP=" + ip);
		String city = IPtest.getInstance().queryCountry(ip);
		if (province == null) {
			province = IPtest.getInstance().queryProvince(ip);
			logger.info("根据IP查询省份=" + province);
		}
		
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setCpId(cpId);
		reqInfo.setImsi(imsi);
		reqInfo.setImei(imei);
		reqInfo.setAimsi(aimsi);
		reqInfo.setMobile(mobile);
		reqInfo.setCpParam(param);
		reqInfo.setPmodel(pmodel);
		reqInfo.setOsversion(osversion);
		reqInfo.setNwtype(nwtype);
		reqInfo.setCtech(ctech);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setPck(pck);
		reqInfo.setApp(app);
		reqInfo.setIp(ip);
		reqInfo.setProvince(province);
		reqInfo.setIccid(iccid);
		reqInfo.setSdkVer(sdkVer);
		reqInfo.setMmAppid(mmAppid);
		reqInfo.setMmChannel(mmChannel);
		reqInfo.setCity(city);
		
		String result = "error";
		try {
			String ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), spId.substring(0,2));
			CodeT ct = new CodeT();
			ct.setFfId(ffId);
			reqInfo.setFfId(ffId);
			reqInfo.setSpId(spId);
			Map<String, String> resultMap = new HashMap<String, String>();
			resultMap.put("order_id", ffId);
			resultMap.put("partner", partner);
			resultMap.put("seller", seller);
			
			result = JSONObject.toJSONString(resultMap);
			
			StringBuilder clog = new StringBuilder();
			clog.append("合作方:").append(ct.getCpName()).append("\n");
			clog.append("通道spId:").append(spId).append("\n");
			clog.append("省份:").append(province).append("\n");
			clog.append("计费点:").append(fee).append("\n");
			clog.append("订单号:").append(ffId).append("\n");
			clog.append("指令内容:").append(result);
			logger.info(clog.toString());
			
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(result).end();
	}
	
	/***
	 * 获取第三方计费指令
	 * 
	 * http://120.24.88.90/code/otherPay
	 * 例子:http://120.24.88.90/code/otherPay?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89
	 */
	public void otherPay(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("otherPay请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String aimsi = request.getParam("aimsi");
		String mobile = request.getParam("phone");
		String param = request.getParam("cpparam");
		String pmodel = request.getParam("pmodel");			// 手机型号
		String osversion = request.getParam("osversion");	// 系统版本
		String nwtype = request.getParam("nwtype");			// 网络类型
		String ctech = request.getParam("ctech");			// 通信制式 GSM、CDMA、3G、4G等
		String fee = request.getParam("fee");
		String pck = request.getParam("pck");//包名
		String app = request.getParam("app");//应用名称
		String sdkVer = request.getParam("sdkVer");
		String mmAppid = request.getParam("mm_appid");
		String mmChannel = request.getParam("mm_channel");
		String spId = request.getParam("sp_id");//支付宝:sd02,宜搜sd03,现在支付sd04
		String partner = "2088612889828639";//商户支付宝账号PID
		String seller = "2669737607@qq.com";//商户收款支付宝账号
		String nowappId = "1457680088269550";//奔跑吧Baby
		
		String province = null;
		String iccid = request.getParam("iccid");
		if (iccid != null && iccid.length()>10) {
			logger.info("用户访问iccid=" + iccid);
			province = provinceMap.get(iccid.substring(8,10));
			logger.info("用户省份=" + province);
		}
		
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		logger.info("用户访问IP=" + ip);
		String city = IPtest.getInstance().queryCountry(ip);
		if (province == null) {
			province = IPtest.getInstance().queryProvince(ip);
			logger.info("根据IP查询省份=" + province);
		}
		
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setCpId(cpId);
		reqInfo.setImsi(imsi);
		reqInfo.setImei(imei);
		reqInfo.setAimsi(aimsi);
		reqInfo.setMobile(mobile);
		reqInfo.setCpParam(param);
		reqInfo.setPmodel(pmodel);
		reqInfo.setOsversion(osversion);
		reqInfo.setNwtype(nwtype);
		reqInfo.setCtech(ctech);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setPck(pck);
		reqInfo.setApp(app);
		reqInfo.setIp(ip);
		reqInfo.setProvince(province);
		reqInfo.setIccid(iccid);
		reqInfo.setSdkVer(sdkVer);
		reqInfo.setMmAppid(mmAppid);
		reqInfo.setMmChannel(mmChannel);
		reqInfo.setCity(city);
		reqInfo.setSpId(spId);
		
		String result = "error";
		if (spId == null || spId.length() == 0) {
			response.content(result).end();
			return;
		}
		try {
			String ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), spId.substring(0,2));
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);//获取对应cpId打开的计费通道
			if (ct != null) {
				
				ct.setFfId(ffId);
				reqInfo.setFfId(ffId);
				Map<String, String> resultMap = new HashMap<String, String>();
				resultMap.put("order_id", ffId);
				if ("sd02".equals(spId)) {
					resultMap.put("partner", partner);
					resultMap.put("seller", seller);
				} else if ("sd04".equals(spId)) {
					resultMap.put("appId", nowappId);
				}
				result = JSONObject.toJSONString(resultMap);
				
				StringBuilder clog = new StringBuilder();
				clog.append("合作方:").append(ct.getCpName()).append("\n");
				clog.append("通道spId:").append(spId).append("\n");
				clog.append("省份:").append(province).append("\n");
				clog.append("计费点:").append(fee).append("\n");
				clog.append("订单号:").append(ffId).append("\n");
				clog.append("指令内容:").append(result);
				logger.info(clog.toString());
				
				commonAction.addOrderReqInfo(ct, reqInfo);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(result).end();
	}
	
	/***
	 * 获取计费指令
	 * 
	 * http://120.24.88.90/code/confirmPay
	 * 例子:http://120.24.88.90/code/confirmPay?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89
	 */
	public void confirmPay(HttpRequest request, HttpResponse response)
			throws Exception {
		Map<String, String> resultJson = new HashMap<String, String>();
		resultJson.put("type", "0");
		resultJson.put("address", "15001749419");
//		resultJson.put("port", "15001749419");
		resultJson.put("content", "发送内容2");
		response.content(JSONObject.toJSONString(resultJson)).end();
	}
	
	/***
	 * 获取计费指令
	 * 
	 * http://120.24.88.90/code/init
	 * 例子:http://120.24.88.90/code/init?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89
	 */
	public void init(HttpRequest request, HttpResponse response)
			throws Exception {
//		logger.info("请求header信息："+request.allHeaders());
		
		/*String cpId = request.getParam("cpid");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String aimsi = request.getParam("aimsi");
		String mobile = request.getParam("phone");
		String param = request.getParam("cpparam");
		String pmodel = request.getParam("pmodel");			// 手机型号
		String osversion = request.getParam("osversion");	// 系统版本
		String nwtype = request.getParam("nwtype");			// 网络类型
		String ctech = request.getParam("ctech");			// 通信制式 GSM、CDMA、3G、4G等
		String fee = "1000";
		String pck = request.getParam("pck");//包名
		String app = request.getParam("app");//应用名称
		String enMethod = request.getParam("enMethod");
		
		String cid = request.getParam("cid");//基站信息
		String lac = request.getParam("lac");//基站信息
		String lat = request.getParam("lat");//经度
		String lon = request.getParam("lon");//纬度
		
		String province = null;
		String iccid = request.getParam("iccid");
		if (iccid != null && iccid.length()>10) {
			logger.info("用户访问iccid=" + iccid);
			province = provinceMap.get(iccid.substring(8,10));
			logger.info("用户省份=" + province);
		}
		
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		logger.info("用户访问IP=" + ip);
		if (province == null) {
			province = IPtest.getInstance().queryProvince(ip);
			logger.info("根据IP查询省份=" + province);
		}
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setCpId(cpId);
		reqInfo.setImsi(imsi);
		reqInfo.setImei(imei);
		reqInfo.setAimsi(aimsi);
		reqInfo.setMobile(mobile);
		reqInfo.setCpParam(param);
		reqInfo.setPmodel(pmodel);
		reqInfo.setOsversion(osversion);
		reqInfo.setNwtype(nwtype);
		reqInfo.setCtech(ctech);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setPck(pck);
		reqInfo.setApp(app);
		reqInfo.setIp(ip);
		reqInfo.setProvince(province);
		reqInfo.setIccid(iccid);
		reqInfo.setEnMethod(enMethod);
		reqInfo.setBscCid(cid);
		reqInfo.setBscLac(lac);
		reqInfo.setGpsLat(lat);
		reqInfo.setGpsLng(lon);
		
		String sms = "error";*/
		String result = "error";
		/*if ("ppkj01".equals(cpId)) {
			response.content(result).end();
			return;
		}
		int codeType = CommonTool.locateOperator(imsi);//1移动 2联通 3电信
		PayInitJson initJson = new PayInitJson();
		boolean flag = false;
		try {
			if (codeType == 1) {
				CodeT ct = commonAction.getCodes(cpId, ip, province, fee, imei, imsi, iccid,
						mobile, param, pmodel, osversion, nwtype, ctech);
				if (ct == null) {
					ct = commonAction.getCodes(cpId, ip, "活跃", fee, imei, imsi, iccid,
							mobile, param, pmodel, osversion, nwtype, ctech);
				}
				if (ct != null) {
					for (CodeInfo info : ct.getCodes()) {
						if ("yc01".equals(info.getSpId()) || "yc09".equals(info.getSpId()) 
								|| "yc11".equals(info.getSpId())) {
							reqInfo.setSpId(info.getSpId());
							reqInfo.setUrl(info.getUrl());
							reqInfo.setUrlNo(info.getUrlNo());
							reqInfo.setReqMethod(info.getReqMethod());
							reqInfo.setKeyword(info.getKeyword());
							reqInfo.setMatchRegex(info.getMatchRegex());
							reqInfo.setFees(info.getFee());
							reqInfo.setReqFlag("init");
							reqInfo.setFfId("klw0000000000000");
							flag = true;
							sms = CodeReqHelper.getSMSCode(reqInfo);
							if (!"error".equals(sms)) break;
						}
					}
					if (flag && "error".equals(sms)) {
						reqInfo.setProvince("活跃");
						sms = CodeReqHelper.getSMSCode(reqInfo);
					}
					if (!"error".equals(sms)) {
						if ("mm06".equals(reqInfo.getSpId())) {
							initJson.setMm2002(JSONObject.parseObject(sms));
						} else if ("yc01".equals(reqInfo.getSpId()) || "yc09".equals(reqInfo.getSpId())
								|| "yc11".equals(reqInfo.getSpId())) {
							initJson.setMm2003(JSONObject.parseObject(sms));
						}
						result = JSONObject.toJSONString(initJson);
					}
				}
			}
		} catch (Exception e) {
			response.content(result).end();
			logger.error(e.getMessage(), e.getCause());
		}*/
		response.content(result).end();
	}
	
	/*private void addOrderReqInfo(CodeT ct, String cpId, String ip, String province, String fee, 
			String imei, String imsi, String iccid, String mobile,String cpparam, String pmodel, 
			String osversion, String nwtype, String ctech) {
		commonAction.addOrderReqInfo(ct, cpId, ip, province, fee, imei, imsi, iccid,
					mobile, cpparam, pmodel, osversion, nwtype, ctech);
	}*/

	/***
	 * 在线获取计费指令,区分一次计费指令(MM)和二次计费指令(游戏基地) 参数: cpid imei imsi cpparam fee ip
	 * 
	 * http://120.24.88.90/code/reqCode
	 * 例子:http://120.24.88.90/code/reqCode?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89
	 */
	public void reqCode(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String mobile = request.getParam("phone");
		String param = request.getParam("cpparam");
		String pmodel = request.getParam("pmodel");			// 手机型号
		String osversion = request.getParam("osversion");	// 系统版本
		String nwtype = request.getParam("nwtype");			// 网络类型
		String ctech = request.getParam("ctech");			// 通信制式 GSM、CDMA、3G、4G等
		String fee = request.getParam("fee");
		int serVer = 0;
		
		String province = null;
		String iccid = request.getParam("iccid");
		if (iccid != null && iccid.length()>10) {
			logger.info("用户访问iccid=" + iccid);
			province = provinceMap.get(iccid.substring(8,10));
			logger.info("用户省份=" + province);
		}
		
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		logger.info("用户访问IP=" + ip);
		if (province == null) {
			province = IPtest.getInstance().queryProvince(ip);
			logger.info("根据IP查询省份=" + province);
		}

		String sms = "error";
		try {
			CodeT ct = commonAction.getCode(cpId, ip, province, fee, imei, imsi, iccid,
					mobile, param, pmodel, osversion, nwtype, ctech, serVer, null);
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
						+ fee + " 的指令！");
				response.content("error").end();
				return;
			} else {
				/*sms = CodeReqHelper.getSMSCode(ct.getUrl(), ct.getUrlNO(), ct.getFfId(), fee, imsi, imei, 
						iccid, pmodel, osversion, nwtype, province, ip, mobile);*/

				StringBuilder clog = new StringBuilder();
				clog.append("合作方:").append(ct.getCpName()).append("\n");
				clog.append("通道方:").append(ct.getSpName()).append("\n");
				clog.append("省份:").append(province).append("\n");
				clog.append("计费点:").append(fee).append("\n");
				clog.append("订单号:").append(ct.getFfId()).append("\n");
				clog.append("指令内容:").append(sms);
				logger.info(clog.toString());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}

		response.content(sms).end();
	}
	
	
	/***
	 * 在线获取MM计费指令
	 * 
	 * 例子:http://120.24.88.90/code/reqSpecialCode?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89
	 */
	public void reqSpecialCode(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String mobile = request.getParam("phone");
		String param = request.getParam("cpparam");
		String pmodel = request.getParam("pmodel");			// 手机型号
		String osversion = request.getParam("osversion");	// 系统版本
		String nwtype = request.getParam("nwtype");			// 网络类型
		String ctech = request.getParam("ctech");			// 通信制式 GSM、CDMA、3G、4G等
		String fee = request.getParam("fee");
		int serVer = 0;
		
		String province = null;
		String iccid = request.getParam("iccid");
		if (iccid != null && iccid.length()>10) {
			logger.info("用户访问iccid=" + iccid);
			province = provinceMap.get(iccid.substring(8,10));
			logger.info("用户省份=" + province);
		}
		
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		logger.info("用户访问IP=" + ip);
		if (province == null) {
			province = IPtest.getInstance().queryProvince(ip);
			logger.info("根据IP查询省份=" + province);
		}

		String sms = "error";
		try {
			CodeT ct = commonAction.getCode(cpId, ip, province, fee, imei, imsi, iccid,
					mobile, param, pmodel, osversion, nwtype, ctech, serVer, null);
			if (ct == null && cpId.equals("yp01")) {
				province = "重庆";
				ct = commonAction.getCode(cpId, ip, province, fee, imei, imsi, iccid,
						mobile, param, pmodel, osversion, nwtype, ctech, serVer, null);
			}
			
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
						+ fee + " 的指令！");
				response.content("error").end();
				return;
			} else {
				/*sms = CodeReqHelper.getSMSCode(ct.getUrl(), ct.getUrlNO(), ct.getFfId(), fee, imsi, imei, 
						iccid, pmodel, osversion, nwtype, province, ip, mobile);*/

				StringBuilder clog = new StringBuilder();
				clog.append("合作方:").append(ct.getCpName()).append("\n");
				clog.append("通道方:").append(ct.getSpName()).append("\n");
				clog.append("省份:").append(province).append("\n");
				clog.append("计费点:").append(fee).append("\n");
				clog.append("订单号:").append(ct.getFfId()).append("\n");
				clog.append("指令内容:").append(sms);
				logger.info(clog.toString());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}

		response.content(sms).end();
	}
	
	/***
	 * 在线获取MM计费指令
	 * 
	 * 例子:http://120.24.88.90/code/reqSpecialMixCode?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89
	 */
	public void reqSpecialMixCode(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String mobile = request.getParam("phone");
		String param = request.getParam("cpparam");
		String pmodel = request.getParam("pmodel");			// 手机型号
		String osversion = request.getParam("osversion");	// 系统版本
		String nwtype = request.getParam("nwtype");			// 网络类型
		String ctech = request.getParam("ctech");			// 通信制式 GSM、CDMA、3G、4G等
		String fee = request.getParam("fee");
		int serVer = 1;
		
		String province = null;
		String iccid = request.getParam("iccid");
		if (iccid != null && iccid.length()>10) {
			logger.info("用户访问iccid=" + iccid);
			province = provinceMap.get(iccid.substring(8,10));
			logger.info("用户省份=" + province);
		}
		
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		logger.info("用户访问IP=" + ip);
		if (province == null) {
			province = IPtest.getInstance().queryProvince(ip);
			logger.info("根据IP查询省份=" + province);
		}

		String sms = "error";
		try {
			CodeT ct = commonAction.getCode(cpId, ip, province, fee, imei, imsi, iccid,
					mobile, param, pmodel, osversion, nwtype, ctech, serVer, null);
			if (ct == null && cpId.equals("yp01")) {
				province = "重庆";
				ct = commonAction.getCode(cpId, ip, province, fee, imei, imsi, iccid,
						mobile, param, pmodel, osversion, nwtype, ctech, serVer, null);
			}
			
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
						+ fee + " 的指令！");
				response.content("error").end();
				return;
			} else {
				/*sms = CodeReqHelper.getSMSCode(ct.getUrl(), ct.getUrlNO(), ct.getFfId(), fee, imsi, imei, 
						iccid, pmodel, osversion, nwtype, province, ip, mobile);*/

				StringBuilder clog = new StringBuilder();
				clog.append("合作方:").append(ct.getCpName()).append("\n");
				clog.append("通道方:").append(ct.getSpName()).append("\n");
				clog.append("省份:").append(province).append("\n");
				clog.append("计费点:").append(fee).append("\n");
				clog.append("订单号:").append(ct.getFfId()).append("\n");
				clog.append("指令内容:").append(sms);
				logger.info(clog.toString());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}

		response.content(sms).end();
	}
	
	/***
	 * 在线获取MM计费指令
	 * 3.1.7版本
	 * 例子:http://120.24.88.90/code/reqSpecialMixCode2?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89
	 */
	public void reqSpecialMixCode2(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("请求header信息："+request.allHeaders());
		logger.info("reqSpecialMixCode2的请求转移至reqSpecialMixCode4");
		
		reqSpecialMixCode4(request, response);
	}
	
	/***
	 * 在线获取MM裸代计费指令
	 * 3.1.7版本
	 * 例子:http://120.24.88.90/code/reqSpecialMixCode3?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89
	 */
	public void reqSpecialMixCode3(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String mobile = request.getParam("phone");
		String param = request.getParam("cpparam");
		String pmodel = request.getParam("pmodel");			// 手机型号
		String osversion = request.getParam("osversion");	// 系统版本
		String nwtype = request.getParam("nwtype");			// 网络类型
		String ctech = request.getParam("ctech");			// 通信制式 GSM、CDMA、3G、4G等
		String fee = request.getParam("fee");
		int serVer = 3;
		
		String province = null;
		String iccid = request.getParam("iccid");
		if (iccid != null && iccid.length()>10) {
			logger.info("用户访问iccid=" + iccid);
			province = provinceMap.get(iccid.substring(8,10));
			logger.info("用户省份=" + province);
		}
		
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		logger.info("用户访问IP=" + ip);
		if (province == null) {
			province = IPtest.getInstance().queryProvince(ip);
			logger.info("根据IP查询省份=" + province);
		}

		String sms = "error";
		try {
			CodeT ct = commonAction.getCode(cpId, ip, province, fee, imei, imsi, iccid,
					mobile, param, pmodel, osversion, nwtype, ctech, serVer, null);
			
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
						+ fee + " 的指令！");
				response.content("error").end();
				return;
			} else {
				/*sms = CodeReqHelper.getSMSCode(ct.getUrl(), ct.getUrlNO(), ct.getFfId(), fee, imsi, imei, 
						iccid, pmodel, osversion, nwtype, province, ip, mobile);*/

				StringBuilder clog = new StringBuilder();
				clog.append("合作方:").append(ct.getCpName()).append("\n");
				clog.append("通道方:").append(ct.getSpName()).append("\n");
				clog.append("省份:").append(province).append("\n");
				clog.append("计费点:").append(fee).append("\n");
				clog.append("订单号:").append(ct.getFfId()).append("\n");
				clog.append("指令内容:").append(sms);
				logger.info(clog.toString());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}

		response.content(sms).end();
	}
	
	/***
	 * 在线获取MM计费指令
	 * 3.1.8版本
	 * 例子:http://120.24.88.90/code/reqSpecialMixCode4?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89
	 */
	public void reqSpecialMixCode4(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String mobile = request.getParam("phone");
		String param = request.getParam("cpparam");
		String pmodel = request.getParam("pmodel");			// 手机型号
		String osversion = request.getParam("osversion");	// 系统版本
		String nwtype = request.getParam("nwtype");			// 网络类型
		String ctech = request.getParam("ctech");			// 通信制式 GSM、CDMA、3G、4G等
		String fee = request.getParam("fee");
		String sdkVer = "1.0.0.0";
		int serVer = 4;
		
		String province = null;
		String iccid = request.getParam("iccid");
		if (iccid != null && iccid.length()>10) {
			logger.info("用户访问iccid=" + iccid);
			province = provinceMap.get(iccid.substring(8,10));
			logger.info("用户省份=" + province);
		}
		
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		logger.info("用户访问IP=" + ip);
		if (province == null) {
			province = IPtest.getInstance().queryProvince(ip);
			logger.info("根据IP查询省份=" + province);
		}
		
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setCpId(cpId);
		reqInfo.setImsi(imsi);
		reqInfo.setImei(imei);
		reqInfo.setMobile(mobile);
		reqInfo.setCpParam(param);
		reqInfo.setPmodel(pmodel);
		reqInfo.setOsversion(osversion);
		reqInfo.setNwtype(nwtype);
		reqInfo.setCtech(ctech);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setIp(ip);
		reqInfo.setProvince(province);
		reqInfo.setIccid(iccid);
		reqInfo.setSdkVer(sdkVer);
		
		String sms = "error";
		try {
			CodeT ct = commonAction.getCode(cpId, ip, province, fee, imei, imsi, iccid,
					mobile, param, pmodel, osversion, nwtype, ctech, serVer, sdkVer);
			
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
						+ fee + " 的指令！");
				response.content("error").end();
				return;
			} else {
				reqInfo.setFfId(ct.getFfId());
				reqInfo.setUrl(ct.getUrl());
				reqInfo.setUrlNo(ct.getUrlNO());
				if ("mm06".equals(ct.getSpId()) || "yc09".equals(ct.getSpId())){
					reqInfo.setSpId("mm07");
					reqInfo.setUrl("http://120.24.88.90/mmcode/reqNetMixMMCode3?serVer=4&");
				}
				sms = CodeReqHelper.getSMSCode(reqInfo);
				if (!"error".equals(sms))
					sms = "[" + sms + "]";
				
				StringBuilder clog = new StringBuilder();
				clog.append("合作方:").append(ct.getCpName()).append("\n");
				clog.append("通道方:").append(ct.getSpName()).append("\n");
				clog.append("通道spId:").append(ct.getSpId()).append("\n");
				clog.append("省份:").append(province).append("\n");
				clog.append("计费点:").append(fee).append("\n");
				clog.append("订单号:").append(ct.getFfId()).append("\n");
				clog.append("指令内容:").append(sms);
				logger.info(clog.toString());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		
		response.content(sms).end();
	}

	/***
	 * 在线获取MM计费指令
	 * 3.1.7版本
	 * 例子:http://120.24.88.90/code/reqtest?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89
	 */
	public void reqtest(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqtest请求header信息："+request.allHeaders());
		logger.info("reqtest的请求转移至reqSpecialMixCode4");
		response.content(request.allHeaders().toString()).end();
	}
	
	/***
	 * 获取第三方支付通用接口
	 * 例子:http://120.24.88.90/code/reqPayOther?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89&&goodsname=测试
	 */
	public void reqPayOther(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqPayOther请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String mobile = request.getParam("phone");
		String param = request.getParam("cpparam");
		String fee = request.getParam("fee");
//		String pck = request.getParam("pck");//包名
		String openid = request.getParam("openid");//微信公众号openid
		String app = request.getParam("app");//应用名称
		String propname = request.getParam("goodsname");//道具名称
		if (propname != null && propname.length() > 0) 
			propname = URLDecoder.decode(propname, "utf-8");
		if (app != null && app.length() > 0) 
			app = URLDecoder.decode(app, "utf-8");
		
		String province = "";
		String iccid = request.getParam("iccid");
		if (iccid != null && iccid.length()>10) {
			logger.info("用户访问iccid=" + iccid);
			province = provinceMap.get(iccid.substring(8,10));
			logger.info("用户省份=" + province);
		}
		
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		logger.info("用户访问IP=" + ip);
		if (province == null) {
			province = IPtest.getInstance().queryProvince(ip);
			logger.info("根据IP查询省份=" + province);
		}
		
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setCpId(cpId);
		reqInfo.setImsi(imsi);
		reqInfo.setImei(imei);
		reqInfo.setMobile(mobile);
		reqInfo.setCpParam(param);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setPrice(fee);
		reqInfo.setIp(ip);
		reqInfo.setProvince(province);
		reqInfo.setIccid(iccid);
		reqInfo.setApp(app);
//		reqInfo.setPck(pck);
		reqInfo.setPck(propname);
		reqInfo.setPropname(propname);
		reqInfo.setOpenid(openid);
		
		String sms = "error";
		try {
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			String spId = "wd99";
			
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
						+ fee + " 的指令！");
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
					reqInfo.setSpId(spId);
					reqInfo.setReqMethod(info.getReqMethod());
					reqInfo.setKeyword(info.getKeyword());
					reqInfo.setMatchRegex(info.getMatchRegex());
					sms = CodeReqHelper.getSMSCode(reqInfo);
					if (!"error".equals(sms)) {
						JSONObject jsonobj = JSONObject.parseObject(sms);
						sms = jsonobj.getString("url");
						reqInfo.setPmodel(jsonobj.getString("transid"));
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("省份:").append(province).append("\n");
						clog.append("计费点:").append(fee).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						break;
					}
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
		
	}
	
	/***
	 * 获取第三方支付通用接口
	 * 例子:http://120.24.88.90/code/reqPayOtherZfbH5?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89&&goodsname=测试
	 */
	public void reqPayOtherZfbH5(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqPayOtherZfbH5请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String mobile = request.getParam("phone");
		String buyerLogonId = request.getParam("buyerlogonid");
		String param = request.getParam("cpparam");
		String fee = request.getParam("fee");
		String callbackurl = request.getParam("callbackurl");
//		String pck = request.getParam("pck");//包名
		String openid = request.getParam("openid");//微信公众号openid
		String reqFlag = request.getParam("is_raw");//微信公众号用到请求标记
		String app = request.getParam("app");//应用名称
		String propname = request.getParam("goodsname");//道具名称
		String code = request.getParam("code");
		if (propname != null && propname.length() > 0) 
			propname = URLDecoder.decode(propname, "utf-8");
		if (app != null && app.length() > 0) 
			app = URLDecoder.decode(app, "utf-8");
		if (callbackurl != null && callbackurl.length() > 0) 
			callbackurl = URLDecoder.decode(callbackurl, "utf-8");
		
		String province = "";
		String iccid = request.getParam("iccid");
		if (iccid != null && iccid.length()>10) {
			logger.info("用户访问iccid=" + iccid);
			province = provinceMap.get(iccid.substring(8,10));
			logger.info("用户省份=" + province);
		}
		
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		logger.info("用户访问IP=" + ip);
		if (province == null) {
			province = IPtest.getInstance().queryProvince(ip);
			logger.info("根据IP查询省份=" + province);
		}
		
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setCpId(cpId);
		reqInfo.setImsi(imsi);
		reqInfo.setImei(imei);
		reqInfo.setMobile(mobile);
		reqInfo.setCpParam(param);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setPrice(fee);
		reqInfo.setIp(ip);
		reqInfo.setProvince(province);
		reqInfo.setIccid(iccid);
		reqInfo.setApp(app);
//		reqInfo.setPck(pck);
		reqInfo.setPck(propname);
		reqInfo.setPropname(propname);
		reqInfo.setOpenid(openid);
		reqInfo.setReqFlag(reqFlag);
		reqInfo.setCallbackurl(callbackurl);
		reqInfo.setCode(code);
		reqInfo.setBuyerLogonId(buyerLogonId);
		
		String sms = "error";
		try {
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			String spId = "wd99";
			
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
						+ fee + " 的指令！");
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
					reqInfo.setSpId(spId);
					reqInfo.setReqMethod(info.getReqMethod());
					reqInfo.setKeyword(info.getKeyword());
					reqInfo.setMatchRegex(info.getMatchRegex());
					sms = CodeReqHelper.getSMSCode(reqInfo);
					if (!"error".equals(sms)) {
						JSONObject jsonobj = JSONObject.parseObject(sms);
						reqInfo.setPmodel(jsonobj.getString("traid"));
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("省份:").append(province).append("\n");
						clog.append("计费点:").append(fee).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						break;
					}
				}
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
		
	}
	
	/***
	 * 获取第三方支付通用接口
	 * 例子:http://otherpay.xushihudong.com/code/newPayOther?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89&&goodsname=测试
	 */
	public void newPayOther(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("newPayOther请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
//		String imsi = request.getParam("imsi");
		String imsi = request.getParam("playerid");//第三方支付用户保存玩家id
		String imei = request.getParam("imei");
		String mobile = request.getParam("phone");
		String param = request.getParam("cpparam");
		String fee = request.getParam("fee");
		String callbackurl = request.getParam("callbackurl");
//		String pck = request.getParam("pck");//包名
		String openid = request.getParam("openid");//微信公众号openid
		String reqFlag = request.getParam("is_raw");//微信公众号用到请求标记
		String app = request.getParam("app");//应用名称
		String propname = request.getParam("goodsname");//道具名称
		String code = request.getParam("code");
		if (propname != null && propname.length() > 0) 
			propname = URLDecoder.decode(propname, "utf-8");
		if (app != null && app.length() > 0) 
			app = URLDecoder.decode(app, "utf-8");
		if (callbackurl != null && callbackurl.length() > 0) 
			callbackurl = URLDecoder.decode(callbackurl, "utf-8");
		if (imsi != null && imsi.length() > 0) {
			imsi = URLDecoder.decode(imsi, "utf-8");
			imsi = cpId + imsi;
		}
		
		
		String province = "";
		String iccid = request.getParam("iccid");
		if (iccid != null && iccid.length()>10) {
			logger.info("用户访问iccid=" + iccid);
			province = provinceMap.get(iccid.substring(8,10));
			logger.info("用户省份=" + province);
		}
		
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		logger.info("用户访问IP=" + ip);
		if (province == null) {
			province = IPtest.getInstance().queryProvince(ip);
			logger.info("根据IP查询省份=" + province);
		}
		
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setCpId(cpId);
		reqInfo.setImsi(imsi);
		reqInfo.setImei(imei);
		reqInfo.setMobile(mobile);
		reqInfo.setCpParam(param);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setPrice(fee);
		reqInfo.setIp(ip);
		reqInfo.setProvince(province);
		reqInfo.setIccid(iccid);
		reqInfo.setApp(app);
//		reqInfo.setPck(pck);
		reqInfo.setPck(propname);
		reqInfo.setPropname(propname);
		reqInfo.setOpenid(openid);
		reqInfo.setReqFlag(reqFlag);
		reqInfo.setCallbackurl(callbackurl);
		reqInfo.setCode(code);
		
		String sms = "error";
		try {
			CodeT ct = commonAction.getPollOtherPayCodes(reqInfo);
			String spId = "wd99";
			
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
						+ fee + " 的指令！");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				StringBuilder clog = new StringBuilder();
				
				List<CodeInfo> codes = ct.getCodes();
				
				for (CodeInfo info : codes) {
//				for (CodeInfo info : ct.getCodes()) {
					spId = info.getSpId();
					ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), info.getSpFlag());
					reqInfo.setUrl(info.getUrl());
					reqInfo.setUrlNo(info.getUrlNo());
					reqInfo.setPmodel("");
					reqInfo.setFfId(ffId);
					reqInfo.setSpId(spId);
					reqInfo.setReqMethod(info.getReqMethod());
					reqInfo.setKeyword(info.getKeyword());
					reqInfo.setMatchRegex(info.getMatchRegex());
					/*List<ResSDKFilter> filterrsp = commonAction.queryOtherFilterBySpid(reqInfo);
					if (filterrsp != null) {
						sms = "error";
						logger.info("超出通道支付日限额spid:"+spId);
						continue;
					}*/
					reqInfo = OtherPayReqHelper.getSMSRegister(reqInfo);
					RegisterRes reg = reqInfo.getReg();
//					sms = CodeReqHelper.getSMSCode(reqInfo);
					if (reg != null) {
						sms = JSONObject.toJSONString(reg);
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("省份:").append(province).append("\n");
						clog.append("计费点:").append(fee).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						break;
					}
				}
				logger.info(clog.toString());
			}
			commonAction.addOtherOrderReqInfo(reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms.getBytes("gbk")).end();
		
	}
	
	/***
	 * 获取第三方支付通用接口
	 * 例子:http://otherpay.xushihudong.com/code/reqPayOtherWY?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89&&goodsname=测试
	 */
	public void reqPayOtherWY(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqPayOtherWY请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
//		String imsi = request.getParam("imsi");
		String imsi = request.getParam("playerid");//第三方支付用户保存玩家id
		String imei = request.getParam("authCode");
		String mobile = request.getParam("phone");
		String param = request.getParam("cpparam");
		String fee = request.getParam("fee");
		String callbackurl = request.getParam("callbackurl");
//		String pck = request.getParam("pck");//包名
		String openid = request.getParam("openid");//微信公众号openid
		String reqFlag = request.getParam("is_raw");//微信公众号用到请求标记
		String app = request.getParam("app");//应用名称
		String propname = request.getParam("goodsname");//道具名称
		String code = request.getParam("code");
		if (propname != null && propname.length() > 0) 
			propname = URLDecoder.decode(propname, "utf-8");
		if (app != null && app.length() > 0) 
			app = URLDecoder.decode(app, "utf-8");
		if (callbackurl != null && callbackurl.length() > 0) 
			callbackurl = URLDecoder.decode(callbackurl, "utf-8");
		if (imsi != null && imsi.length() > 0) {
			imsi = URLDecoder.decode(imsi, "utf-8");
			imsi = cpId + imsi;
		}
		String province = "";
		String iccid = request.getParam("iccid");
		if (iccid != null && iccid.length()>10) {
			logger.info("用户访问iccid=" + iccid);
			province = provinceMap.get(iccid.substring(8,10));
			logger.info("用户省份=" + province);
		}
		
		String acctNo = request.getParam("acctNo");//银行卡号
		String customerName = request.getParam("customerName");//银行卡预留姓名
		String cerdNo = request.getParam("cerdNo");//身份证号
		if (cerdNo != null && cerdNo.length() > 0) {
			imsi = acctNo;
		}
		if (acctNo != null && acctNo.length() > 0) {
			imei = cerdNo;
		}
		String osversion = null;
		if (customerName != null && customerName.length() > 0) {
			osversion = URLDecoder.decode(customerName, "utf-8");
		}
		
		String ip = request.getParam("ip");
		if (ip != null && ip.length() > 20) ip = null;
		ip = ip==null?getRealIP(request):ip;
		ip = ip.trim();
		logger.info("用户访问IP=" + ip);
		if (province == null) {
			province = IPtest.getInstance().queryProvince(ip);
			logger.info("根据IP查询省份=" + province);
		}
		
		int intfee = CommonTool.convertInt(fee);
		
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setCpId(cpId);
		reqInfo.setImsi(imsi);
		reqInfo.setImei(imei);
		reqInfo.setMobile(mobile);
		reqInfo.setCpParam(param);
		reqInfo.setFee(intfee);
		reqInfo.setPrice(fee);
		reqInfo.setIp(ip);
		reqInfo.setProvince(province);
		reqInfo.setIccid(iccid);
		reqInfo.setApp(app);
//		reqInfo.setPck(pck);
		reqInfo.setPck(propname);
		reqInfo.setPropname(propname);
		reqInfo.setOpenid(openid);
		reqInfo.setReqFlag(reqFlag);
		reqInfo.setCallbackurl(callbackurl);
		reqInfo.setCode(code);
		reqInfo.setOsversion(osversion);
		
		String sms = "error";
		if (intfee>=100) {
			try {
//				CodeT ct = commonAction.getPollOtherPayCodes(reqInfo);
				CodeT ct = commonAction.getOtherPayCodes(reqInfo);
//				CodeT ct = commonAction.pollOtherPayCodes(reqInfo);
				String spId = "wd99";
				
				if (ct == null) {
					logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
							+ fee + " 的指令！");
					response.content("error").end();
					return;
				} else {
					String ffId = ct.getFfId();
					StringBuilder clog = new StringBuilder();
					
					List<CodeInfo> codes = ct.getCodes();
					
					for (CodeInfo info : codes) {
//				for (CodeInfo info : ct.getCodes()) {
						spId = info.getSpId();
						ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), info.getSpFlag());
						reqInfo.setUrl(info.getUrl());
						reqInfo.setUrlNo(info.getUrlNo());
						reqInfo.setPmodel("");
						reqInfo.setFfId(ffId);
						reqInfo.setSpId(spId);
						reqInfo.setReqMethod(info.getReqMethod());
						reqInfo.setKeyword(info.getKeyword());
						reqInfo.setMatchRegex(info.getMatchRegex());
						/*List<ResSDKFilter> filterrsp = commonAction.queryOtherFilterBySpid(reqInfo);
					if (filterrsp != null) {
						sms = "error";
						logger.info("超出通道支付日限额spid:"+spId);
						continue;
					}*/
						sms = CodeReqHelper.getSMSCode(reqInfo);
						if (!"error".equals(sms)) {
							JSONObject jsonobj = JSONObject.parseObject(sms);
							String status = jsonobj.getString("status");
							if ("0".equals(status)) {
								reqInfo.setPmodel(jsonobj.getString("traid"));
								clog.append("合作方:").append(cpId).append("\n");
								clog.append("通道方:").append(info.getSpId()).append("\n");
								clog.append("省份:").append(province).append("\n");
								clog.append("计费点:").append(fee).append("\n");
								clog.append("订单号:").append(ffId).append("\n");
								clog.append("指令内容:").append(sms);
								break;
							} else {
								logger.info(spId+"返回信息："+sms+"切换下一条通道");
								continue;
							}
						}
					}
					logger.info(clog.toString());
				}
				commonAction.addOtherOrderReqInfo(reqInfo);
//				commonAction.addPollOrderReqInfo(reqInfo);
			} catch (Exception e) {
				logger.error(e.getMessage(), e.getCause());
			}
		} else {
			sms = "price greater than 100";
		}
		response.content(sms).end();
		
	}
	
	/***
	 * 获取第三方支付通用接口
	 * 例子:http://otherpay.xushihudong.com/code/queryOtherpay?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89&&goodsname=测试
	 */
	public void queryOtherpay(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("queryOtherpay请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
//		String imsi = request.getParam("imsi");
		String imsi = request.getParam("playerid");//第三方支付用户保存玩家id
		String imei = request.getParam("imei");
		String mobile = request.getParam("phone");
		String param = request.getParam("cpparam");
		String fee = request.getParam("fee");
		String callbackurl = request.getParam("callbackurl");
//		String pck = request.getParam("pck");//包名
		String openid = request.getParam("openid");//微信公众号openid
		String reqFlag = request.getParam("is_raw");//微信公众号用到请求标记
		String app = request.getParam("app");//应用名称
		String propname = request.getParam("goodsname");//道具名称
		String code = request.getParam("code");
		String sign = request.getParam("sign");
		if (propname != null && propname.length() > 0) 
			propname = URLDecoder.decode(propname, "utf-8");
		if (app != null && app.length() > 0) 
			app = URLDecoder.decode(app, "utf-8");
		if (callbackurl != null && callbackurl.length() > 0) 
			callbackurl = URLDecoder.decode(callbackurl, "utf-8");
		if (imsi != null && imsi.length() > 0) {
			imsi = URLDecoder.decode(imsi, "utf-8");
			imsi = cpId + imsi;
		}
		String province = "";
		String iccid = request.getParam("iccid");
		if (iccid != null && iccid.length()>10) {
			logger.info("用户访问iccid=" + iccid);
			province = provinceMap.get(iccid.substring(8,10));
			logger.info("用户省份=" + province);
		}
		
		String ip = request.getParam("ip");
		if (ip != null && ip.length() > 20) ip = null;
		ip = ip==null?getRealIP(request):ip;
		logger.info("用户访问IP=" + ip);
		if (province == null) {
			province = IPtest.getInstance().queryProvince(ip);
			logger.info("根据IP查询省份=" + province);
		}
		
		String sms = "error";
		String md5mid = cpId.substring(0,cpId.length()-2);
		String key = ConstantDefine.MD5_KEY1 + md5mid + ConstantDefine.MD5_KEY2;
		StringBuilder signparam = new StringBuilder();
		signparam.append("cpid=").append(cpId)
		.append("&cpparam=").append(param)
		.append("&fee=").append(fee)
		.append("&key=").append(key);
		String ressign = MD5.md5(signparam.toString(), "utf-8");
		if (sign.equals(ressign)) {
			OrderReqInfo reqInfo = new OrderReqInfo();
			reqInfo.setCpId(cpId);
			reqInfo.setImsi(imsi);
			reqInfo.setImei(imei);
			reqInfo.setMobile(mobile);
			reqInfo.setCpParam(param);
			reqInfo.setFee(CommonTool.convertInt(fee));
			reqInfo.setPrice(fee);
			reqInfo.setIp(ip);
			reqInfo.setProvince(province);
			reqInfo.setIccid(iccid);
			reqInfo.setApp(app);
//			reqInfo.setPck(pck);
			reqInfo.setPck(propname);
			reqInfo.setPropname(propname);
			reqInfo.setOpenid(openid);
			reqInfo.setReqFlag(reqFlag);
			reqInfo.setCallbackurl(callbackurl);
			reqInfo.setCode(code);
			
			try {
//			CodeT ct = commonAction.getPollOtherPayCodes(reqInfo);
				CodeT ct = commonAction.pollOtherPayCodes(reqInfo);
				String spId = "wd99";
				
				if (ct == null) {
					logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
							+ fee + " 的指令！");
					response.content("error").end();
					return;
				} else {
					String ffId = ct.getFfId();
					StringBuilder clog = new StringBuilder();
					
					List<CodeInfo> codes = ct.getCodes();
					
					for (CodeInfo info : codes) {
//					for (CodeInfo info : ct.getCodes()) {
						spId = info.getSpId();
						ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), info.getSpFlag());
						reqInfo.setUrl(info.getUrl());
						reqInfo.setUrlNo(info.getUrlNo());
						reqInfo.setPmodel("");
						reqInfo.setFfId(ffId);
						reqInfo.setSpId(spId);
						reqInfo.setReqMethod(info.getReqMethod());
						reqInfo.setKeyword(info.getKeyword());
						reqInfo.setMatchRegex(info.getMatchRegex());
						/*List<ResSDKFilter> filterrsp = commonAction.queryOtherFilterBySpid(reqInfo);
						if (filterrsp != null) {
							sms = "error";
							logger.info("超出通道支付日限额spid:"+spId);
							continue;
						}*/
						sms = CodeReqHelper.getSMSCode(reqInfo);
						if (!"error".equals(sms)) {
							JSONObject jsonobj = JSONObject.parseObject(sms);
							reqInfo.setPmodel(jsonobj.getString("traid"));
							clog.append("合作方:").append(cpId).append("\n");
							clog.append("通道方:").append(info.getSpId()).append("\n");
							clog.append("省份:").append(province).append("\n");
							clog.append("计费点:").append(fee).append("\n");
							clog.append("订单号:").append(ffId).append("\n");
							clog.append("指令内容:").append(sms);
							break;
						}
					}
					logger.info(clog.toString());
				}
//				commonAction.addOtherOrderReqInfo(reqInfo);
				commonAction.addPollOrderReqInfo(reqInfo);
			} catch (Exception e) {
				logger.error(e.getMessage(), e.getCause());
			}
		} else {
			sms = "md5 check error";
		}
		
		response.content(sms).end();
		
	}
	
	/***
	 * 获取第三方支付通用接口
	 * 例子:http://otherpay.xushihudong.com/code/reqpay?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89&&goodsname=测试
	 */
	public void reqpay(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqpay请求header信息："+request.allHeaders());
		
		String sign = request.getParam("sign");
		String cpId = request.getParam("cpid");
//		String imsi = request.getParam("imsi");
		String imsi = request.getParam("playerid");//第三方支付用户保存玩家id
		String imei = request.getParam("imei");
		String mobile = request.getParam("phone");
		String param = request.getParam("cpparam");
		String fee = request.getParam("fee");
		String callbackurl = request.getParam("callbackurl");
//		String pck = request.getParam("pck");//包名
		String openid = request.getParam("openid");//微信公众号openid
		String reqFlag = request.getParam("is_raw");//微信公众号用到请求标记
		String app = request.getParam("app");//应用名称
		String propname = request.getParam("goodsname");//道具名称
		String code = request.getParam("code");
		if (propname != null && propname.length() > 0) 
			propname = URLDecoder.decode(propname, "utf-8");
		if (app != null && app.length() > 0) 
			app = URLDecoder.decode(app, "utf-8");
		if (callbackurl != null && callbackurl.length() > 0) 
			callbackurl = URLDecoder.decode(callbackurl, "utf-8");
		if (imsi != null && imsi.length() > 0) {
			imsi = URLDecoder.decode(imsi, "utf-8");
			imsi = cpId + imsi;
		}
		
		String mid = "mrjm";
		if (cpId != null && cpId.length() > 2) {
			mid = cpId.substring(0,cpId.length()-2);
		}
		String key = ConstantDefine.MD5_KEY1 + mid + ConstantDefine.MD5_KEY2;
		StringBuilder msg = new StringBuilder();
		msg.append("cpid=").append(cpId)
		.append("&cpparam=").append(param)
		.append("&fee=").append(fee);
		String calsign = MD5.md5(msg.toString()+"&key="+key, "utf-8");
		if (!sign.equals(calsign)) {
			response.content("签名错误".getBytes("gbk")).end();
			return;
		}
		
		String province = "";
		String iccid = request.getParam("iccid");
		if (iccid != null && iccid.length()>10) {
			logger.info("用户访问iccid=" + iccid);
			province = provinceMap.get(iccid.substring(8,10));
			logger.info("用户省份=" + province);
		}
		
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		logger.info("用户访问IP=" + ip);
		if (province == null) {
			province = IPtest.getInstance().queryProvince(ip);
			logger.info("根据IP查询省份=" + province);
		}
		
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setCpId(cpId);
		reqInfo.setImsi(imsi);
		reqInfo.setImei(imei);
		reqInfo.setMobile(mobile);
		reqInfo.setCpParam(param);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setPrice(fee);
		reqInfo.setIp(ip);
		reqInfo.setProvince(province);
		reqInfo.setIccid(iccid);
		reqInfo.setApp(app);
//		reqInfo.setPck(pck);
		reqInfo.setPck(propname);
		reqInfo.setPropname(propname);
		reqInfo.setOpenid(openid);
		reqInfo.setReqFlag(reqFlag);
		reqInfo.setCallbackurl(callbackurl);
		reqInfo.setCode(code);
		
		String sms = "error";
		try {
//			CodeT ct = commonAction.getPollOtherPayCodes(reqInfo);
			CodeT ct = commonAction.pollOtherPayCodes(reqInfo);
			String spId = "wd99";
			
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
						+ fee + " 的指令！");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				StringBuilder clog = new StringBuilder();
				
				List<CodeInfo> codes = ct.getCodes();
				
				for (CodeInfo info : codes) {
//				for (CodeInfo info : ct.getCodes()) {
					spId = info.getSpId();
					ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), info.getSpFlag());
					reqInfo.setUrl(info.getUrl());
					reqInfo.setUrlNo(info.getUrlNo());
					reqInfo.setPmodel("");
					reqInfo.setFfId(ffId);
					reqInfo.setSpId(spId);
					reqInfo.setReqMethod(info.getReqMethod());
					reqInfo.setKeyword(info.getKeyword());
					reqInfo.setMatchRegex(info.getMatchRegex());
					/*List<ResSDKFilter> filterrsp = commonAction.queryOtherFilterBySpid(reqInfo);
					if (filterrsp != null) {
						sms = "error";
						logger.info("超出通道支付日限额spid:"+spId);
						continue;
					}*/
					sms = CodeReqHelper.getSMSCode(reqInfo);
					if (!"error".equals(sms)) {
						JSONObject jsonobj = JSONObject.parseObject(sms);
						reqInfo.setPmodel(jsonobj.getString("traid"));
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("省份:").append(province).append("\n");
						clog.append("计费点:").append(fee).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						break;
					}
				}
				logger.info(clog.toString());
			}
			commonAction.addOtherOrderReqInfo(reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms.getBytes("gbk")).end();
		
	}
	/***
	 * 信捷银联支付接口
	 * 例子:http://otherpay.xushihudong.com/code/reqPayOtherYL?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89&&goodsname=测试
	 */
	public void reqPayOtherYL(HttpRequest request, HttpResponse response){
		logger.info("请求header信息：" + request.allHeaders());
		String body = request.body();
		logger.info("请求body信息："+ body);
		String sms = "error";
		//accno,cpid,fee,goodsname
		JSONObject jsonobj = JSONObject.parseObject(body);
		String cpId=jsonobj.getString("cpid");
		String propname=jsonobj.getString("goodsname");
		String fee=jsonobj.getString("fee");
		String accNo=jsonobj.getString("accno");
		String customerInfo="";
		try {
			customerInfo = URLDecoder.decode(jsonobj.getString("customerinfo"),"utf-8");
		} catch (UnsupportedEncodingException e2) {
			response.content(sms).end();
			return;
		}		
		Map<String,String> signMap=new HashMap<String,String>();
		signMap.put("accno", accNo);
		signMap.put("cpid", cpId);
		signMap.put("fee", fee);
		signMap.put("goodsname", propname);
		String mySign=null;
		try {
			mySign=MD5Utils.getMD5(PayUtil.createSign(signMap, false));
		} catch (UnsupportedEncodingException e1) {
			response.content(sms).end();
			return;
		}
		if(mySign==null||!mySign.equals(jsonobj.get("sign"))){
			response.content(sms).end();
			return;
		}
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setCpId(cpId);
		reqInfo.setCpParam(customerInfo);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setPrice(fee);
		reqInfo.setPck(accNo);
		reqInfo.setPropname(propname);
		reqInfo.setPck(accNo);
		try {
			CodeT ct = commonAction.getPollOtherPayCodes(reqInfo);
			String spId = "wd99";
			
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适SP！");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				StringBuilder clog = new StringBuilder();				
				List<CodeInfo> codes = ct.getCodes();				
				for (CodeInfo info : codes) {
					spId = info.getSpId();
					ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), info.getSpFlag());
					reqInfo.setUrl(info.getUrl());
					reqInfo.setUrlNo(info.getUrlNo());
					reqInfo.setPmodel("");
					reqInfo.setFfId(ffId);
					reqInfo.setSpId(spId);
					reqInfo.setReqMethod(info.getReqMethod());
					reqInfo.setKeyword(info.getKeyword());
					reqInfo.setMatchRegex(info.getMatchRegex());
					sms = CodeReqHelper.getSMSCode(reqInfo);
					if (!"error".equals(sms)) {
						JSONObject jsonobj1 = JSONObject.parseObject(sms);
						reqInfo.setPmodel(jsonobj1.getString("traid"));
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("计费点:").append(fee).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						break;
					}
				}
				logger.info(clog.toString());
			}
			commonAction.addOtherOrderReqInfo(reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
		
	}
	/***
	 * 衫德银联支付接口
	 * 例子:http://otherpay.xushihudong.com/code/reqPayOtherYL2?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89&&goodsname=测试
	 */
	public void reqPayOtherYL2(HttpRequest request, HttpResponse response){
		logger.info("请求header信息：" + request.allHeaders());
		String body = request.body();
		logger.info("请求body信息："+ body);
		String sms = "error";
		//accno,cpid,fee,goodsname
		JSONObject jsonobj = JSONObject.parseObject(body);
		String cpId=jsonobj.getString("cpid");
		String propname=jsonobj.getString("goodsname");
		String fee=jsonobj.getString("fee");
		String accNo=jsonobj.getString("accno");
		String customerInfo="";
		try {
			customerInfo = URLDecoder.decode(jsonobj.getString("customerinfo"),"utf-8");
		} catch (UnsupportedEncodingException e2) {
			response.content(sms).end();
			return;
		}	
		Map<String,String> signMap=new HashMap<String,String>();
		signMap.put("accno", accNo);
		signMap.put("cpid", cpId);
		signMap.put("fee", fee);
		signMap.put("goodsname", propname);
		String mySign=null;
		try {
			mySign=MD5Utils.getMD5(PayUtil.createSign(signMap, false));
		} catch (UnsupportedEncodingException e1) {
			response.content(sms).end();
			return;
		}
		if(mySign==null||!mySign.equals(jsonobj.get("sign"))){
			response.content(sms).end();
			return;
		}
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setCpId(cpId);
		reqInfo.setCpParam(customerInfo);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setPrice(fee);
		reqInfo.setPck(accNo);
		reqInfo.setPropname(propname);
		reqInfo.setPck(accNo);
		try {
			CodeT ct = commonAction.getPollOtherPayCodes(reqInfo);
			String spId = "wd99";
			
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适SP！");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				StringBuilder clog = new StringBuilder();				
				List<CodeInfo> codes = ct.getCodes();				
				for (CodeInfo info : codes) {
					spId = info.getSpId();
					ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), info.getSpFlag());
					reqInfo.setUrl(info.getUrl());
					reqInfo.setUrlNo(info.getUrlNo());
					reqInfo.setPmodel("");
					reqInfo.setFfId(ffId);
					reqInfo.setSpId(spId);
					reqInfo.setReqMethod(info.getReqMethod());
					reqInfo.setKeyword(info.getKeyword());
					reqInfo.setMatchRegex(info.getMatchRegex());
					sms = CodeReqHelper.getSMSCode(reqInfo);
					if (!"error".equals(sms)) {
						JSONObject jsonobj1 = JSONObject.parseObject(sms);
						reqInfo.setPmodel(jsonobj1.getString("traid"));
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("计费点:").append(fee).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						break;
					}
				}
				logger.info(clog.toString());
			}
			commonAction.addOtherOrderReqInfo(reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
		
	}
	/*银联支付确认请求*/
	public void reqPayOtherYlVerify(HttpRequest request, HttpResponse response){
		logger.info("请求header信息：" + request.allHeaders());
		String body = request.body();
		logger.info("请求body信息："+ body);
		String sms = "error";
		JSONObject jsonobj = JSONObject.parseObject(body);
		String cpId=jsonobj.getString("cpid");
		String ffId=jsonobj.getString("orderid");
		String smsCode=jsonobj.getString("smscode");//短信验证码	
		String traid=jsonobj.getString("traid");
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setCpId(cpId);
		reqInfo.setCode(smsCode);
		reqInfo.setFfId(ffId);
		reqInfo.setPmodel(traid);
		try {
			CodeT ct = commonAction.getPollOtherPayCodes(reqInfo);
			String spId = "wd99";
			
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适SP！");
				response.content("error").end();
				return;
			} else {	
				List<CodeInfo> codes = ct.getCodes();	
				for (CodeInfo info : codes) {
					spId = info.getSpId();
					reqInfo.setUrl(info.getUrl());
					reqInfo.setUrlNo(info.getUrlNo());
					reqInfo.setSpId(spId);
					reqInfo.setReqMethod(info.getReqMethod());
					reqInfo.setKeyword(info.getKeyword());
					reqInfo.setMatchRegex(info.getMatchRegex());
					sms = CodeReqHelper.getSMSCode(reqInfo);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
		
	}
	
	/***
	 * 获取第三方支付通用接口
	 * 例子:http://otherpay.weizunshiji.com/code/reqPayOtherWYZF?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89&&goodsname=测试
	 */
	public void reqPayOtherWYZF(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqPayOtherWYZF请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
//		String imsi = request.getParam("imsi");
		String imsi = request.getParam("playerid");//第三方支付用户保存玩家id
		String param = request.getParam("cpparam");
		String spId = request.getParam("spid");
		String fee = request.getParam("fee");
		String callbackurl = request.getParam("callbackurl");
//		String pck = request.getParam("pck");//包名
		String app = request.getParam("app");//应用名称
		String propname = request.getParam("goodsname");//道具名称
		String code = request.getParam("code");
		if (propname != null && propname.length() > 0) 
			propname = URLDecoder.decode(propname, "utf-8");
		if (app != null && app.length() > 0) 
			app = URLDecoder.decode(app, "utf-8");
		if (callbackurl != null && callbackurl.length() > 0) 
			callbackurl = URLDecoder.decode(callbackurl, "utf-8");
		if (imsi != null && imsi.length() > 0) {
			imsi = URLDecoder.decode(imsi, "utf-8");
		}
		
		
		String province = "";
		String iccid = request.getParam("iccid");
		if (iccid != null && iccid.length()>10) {
			logger.info("用户访问iccid=" + iccid);
			province = provinceMap.get(iccid.substring(8,10));
			logger.info("用户省份=" + province);
		}
		
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		logger.info("用户访问IP=" + ip);
		if (province == null) {
			province = IPtest.getInstance().queryProvince(ip);
			logger.info("根据IP查询省份=" + province);
		}
		
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setCpId(cpId);
		reqInfo.setImsi(imsi);
		reqInfo.setCpParam(param);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setPrice(fee);
		reqInfo.setIp(ip);
		reqInfo.setProvince(province);
		reqInfo.setIccid(iccid);
		reqInfo.setApp(app);
//		reqInfo.setPck(pck);
		reqInfo.setPck(propname);
		reqInfo.setPropname(propname);
		reqInfo.setCallbackurl(callbackurl);
		reqInfo.setCode(code);
		reqInfo.setSpId(spId);
		
		String sms = "error";
		try {
			CodeInfo info = commonAction.queryCodeInfoBySpid(spId);
			
			if (info == null) {
				logger.warn("CPID=" + cpId + " 匹配不到spid=" + spId + " 计费点="
						+ fee + " 的指令！");
				response.content("error").end();
				return;
			} else {
				String ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), spId.substring(0,2));
				StringBuilder clog = new StringBuilder();
				reqInfo.setUrl(info.getUrl());
				reqInfo.setUrlNo(info.getUrlNo());
				reqInfo.setPmodel("");
				reqInfo.setFfId(ffId);
				reqInfo.setReqMethod(info.getReqMethod());
				reqInfo.setKeyword(info.getKeyword());
				reqInfo.setMatchRegex(info.getMatchRegex());
				sms = CodeForwordReqHelper.getSMSCode(reqInfo);
				if (!"error".equals(sms)) {
					JSONObject jsonobj = JSONObject.parseObject(sms);
					reqInfo.setPmodel(jsonobj.getString("traid"));
					clog.append("合作方:").append(cpId).append("\n");
					clog.append("通道方:").append(info.getSpId()).append("\n");
					clog.append("省份:").append(province).append("\n");
					clog.append("计费点:").append(fee).append("\n");
					clog.append("订单号:").append(ffId).append("\n");
					clog.append("指令内容:").append(sms);
				}
				logger.info(clog.toString());
			}
			commonAction.addOtherOrderReqInfo(reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
		
	}
	
	/***
	 * 获取第三方支付通用接口
	 * 例子:http://120.24.88.90/code/payOtherZ?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89&&goodsname=测试
	 */
	public void payOtherZ(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("payOtherZ请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("mchid");
		String imsi = request.getParam("notity_url");
		String imei = request.getParam("imei");
		String mobile = request.getParam("phone");
		String param = request.getParam("out_trade_id");
		String fee = request.getParam("price");
		String callbackurl = request.getParam("callbackurl");
//		String pck = request.getParam("pck");//包名
		String openid = request.getParam("openid");//微信公众号openid
		String reqFlag = request.getParam("is_raw");//微信公众号用到请求标记
		String app = request.getParam("app");//应用名称
		String propname = request.getParam("goodsname");//道具名称
		String code = request.getParam("code");
		String sign = request.getParam("sign");
		if (propname != null && propname.length() > 0) 
			propname = URLDecoder.decode(propname, "utf-8");
		if (app != null && app.length() > 0) 
			app = URLDecoder.decode(app, "utf-8");
		if (callbackurl != null && callbackurl.length() > 0) 
			callbackurl = URLDecoder.decode(callbackurl, "utf-8");
		if (imsi != null && imsi.length() > 0) 
			imsi = URLDecoder.decode(imsi, "utf-8");
		
		String province = "";
		String iccid = request.getParam("iccid");
		if (iccid != null && iccid.length()>10) {
			logger.info("用户访问iccid=" + iccid);
			province = provinceMap.get(iccid.substring(8,10));
			logger.info("用户省份=" + province);
		}
		
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		logger.info("用户访问IP=" + ip);
		if (province == null) {
			province = IPtest.getInstance().queryProvince(ip);
			logger.info("根据IP查询省份=" + province);
		}
		
		String sms = "error";
		
		String md5mid = cpId.substring(0,cpId.length()-2);
		String key = ConstantDefine.MD5_ZWSF_KEY1 + md5mid + ConstantDefine.MD5_ZWSF_KEY2;
		StringBuilder signparam = new StringBuilder();
		signparam.append("fee=").append(fee)
		.append("&mchid=").append(cpId)
		.append("&out_trade_id=").append(param)
		.append("&key=").append(key);
		String ressign = MD5.md5(signparam.toString(), "utf-8");
		if (sign.equals(ressign)) {
			
			OrderReqInfo reqInfo = new OrderReqInfo();
			reqInfo.setCpId(cpId);
			reqInfo.setImsi(imsi);
			reqInfo.setImei(imei);
			reqInfo.setMobile(mobile);
			reqInfo.setCpParam(param);
			reqInfo.setFee(CommonTool.convertInt(fee));
			reqInfo.setPrice(fee);
			reqInfo.setIp(ip);
			reqInfo.setProvince(province);
			reqInfo.setIccid(iccid);
			reqInfo.setApp(app);
//			reqInfo.setPck(pck);
			reqInfo.setPck(propname);
			reqInfo.setPropname(propname);
			reqInfo.setOpenid(openid);
			reqInfo.setReqFlag(reqFlag);
			reqInfo.setCallbackurl(callbackurl);
			reqInfo.setCode(code);
			
			try {
				CodeT ct = commonAction.getOtherPayCodes(reqInfo);
				String spId = "wd99";
				
				if (ct == null) {
					logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
							+ fee + " 的指令！");
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
						reqInfo.setSpId(spId);
						reqInfo.setReqMethod(info.getReqMethod());
						reqInfo.setKeyword(info.getKeyword());
						reqInfo.setMatchRegex(info.getMatchRegex());
						sms = ZWOtherPayHelper.getOtherPay(reqInfo);
						if (!"error".equals(sms)) {
							clog.append("合作方:").append(cpId).append("\n");
							clog.append("通道方:").append(info.getSpId()).append("\n");
							clog.append("省份:").append(province).append("\n");
							clog.append("计费点:").append(fee).append("\n");
							clog.append("订单号:").append(ffId).append("\n");
							clog.append("指令内容:").append(sms);
							break;
						}
					}
					ct.setFfId(ffId);
					logger.info(clog.toString());
				}
				commonAction.addOrderReqInfo(ct, reqInfo);
			} catch (Exception e) {
				logger.error(e.getMessage(), e.getCause());
			}
		} else {
			sms = "sign error";
		}
		
		response.content(sms).end();
		
	}
	public static void main(String[] args) {
		String result = "error";
		Map<String, Object> map = new HashMap<String, Object>();
		String cpid = "test01";
//		String accountName = "周新苗";
		String accountName = "唐忠明";
		map.put("amount","842689");//代付金额，单位分
		map.put("accountName",accountName);//银行卡账户名
		map.put("orgPayforSsn","20180327542644");//商户订单编号
//		map.put("cardNo","6212261912001262174");//银行卡号
		map.put("cardNo","6215581502006780951");//银行卡号
//		map.put("bankCd","102");//行别代码，支持的银行列表件附件
		map.put("bankCd","102");//行别代码，支持的银行列表件附件
		map.put("memo","cs");//备注
		map.put("mobile","13537522954");//手机号
		map.put("cpid", cpid);
		String signparam = PaySignUtil.getParamStr(map);
		String md5mid = cpid.substring(0,cpid.length()-2);
		String key = ConstantDefine.MD5_KEY1 + md5mid + ConstantDefine.MD5_KEY2;
		signparam = signparam + "&key=" + key;
		String sign = "";
		try {
			sign = MD5.md5(signparam, "utf-8");
			map.put("sign",sign);//消息签名
//			map.put("accountName", URLEncoder.encode(accountName, "utf-8"));
			result = JSONObject.toJSONString(map);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/***
	 * 代付
	 * 例子:http://ltxpay.huizhousenfeng.com/code/agentPay
	 */
	public void agentPay(HttpRequest request, HttpResponse response)
			throws Exception {
		String result = "error";
		logger.info("agentPay请求header信息："+request.allHeaders());
		String body = request.body();
		logger.info("agentPay请求header信息："+body);
		if (body != null && body.length() > 0) {
			Map<String, Object> reqmap = CommonTool.parseJson2(JSONObject.parseObject(body));
			String cpid = (String)reqmap.get("cpid");
			String md5mid = cpid.substring(0,cpid.length()-2);
			String key = ConstantDefine.MD5_KEY1 + md5mid + ConstantDefine.MD5_KEY2;
			String accountName = (String)reqmap.get("accountName");
			accountName = URLDecoder.decode(accountName, "utf-8");
			reqmap.put("accountName", accountName);
			String sign = (String)reqmap.get("sign");
			String checkSignparam = PaySignUtil.getParamStr(reqmap);
			checkSignparam = checkSignparam + "&key=" + key;
			String checkSign = MD5.md5(checkSignparam, "utf-8");
			boolean checkmd5 = checkSign.equals(sign);
			if (checkmd5) {
				OrderReqInfo reqInfo = new OrderReqInfo();
				reqInfo.setCpId(cpid);
				CodeT ct = commonAction.getOtherPayCodes(reqInfo);
				String spId = "wd99";
				
				if (ct == null) {
					logger.warn("CPID=" + cpid + " 商户权限已经关闭");
					response.content("error").end();
					return;
				} else {
					for (CodeInfo info : ct.getCodes()) {
						spId = info.getSpId();
						reqInfo.setUrl(info.getUrl());
						reqInfo.setUrlNo(info.getUrlNo());
						reqInfo.setPmodel("");
						reqInfo.setSpId(spId);
						reqInfo.setReqMethod(info.getReqMethod());
						reqInfo.setKeyword(info.getKeyword());
						reqInfo.setMatchRegex(info.getMatchRegex());
						reqInfo.setBody(body);
						result = AgentOtherPayHelper.agentPay(reqInfo);
					}
				}
				
			} else {
				result = "sign error";
			}
		} else {
			result = "param is empty error";
		}
		
		response.content(result.getBytes("gbk")).end();
		
	}
	
	/***
	 * 代付
	 * 例子:http://ltxpay.huizhousenfeng.com/code/queryAgentPay
	 */
	public void queryAgentPay(HttpRequest request, HttpResponse response)
			throws Exception {
		String result = "error";
		logger.info("queryAgentPay请求header信息："+request.allHeaders());
		String body = request.body();
		logger.info("queryAgentPay请求header信息："+body);
		if (body != null && body.length() > 0) {
			Map<String, Object> reqmap = CommonTool.parseJson2(JSONObject.parseObject(body));
			String cpid = (String)reqmap.get("cpid");
			String md5mid = cpid.substring(0,cpid.length()-2);
			String key = ConstantDefine.MD5_KEY1 + md5mid + ConstantDefine.MD5_KEY2;
			/*String accountName = (String)reqmap.get("accountName");
			accountName = URLDecoder.decode(accountName, "utf-8");
			reqmap.put("accountName", accountName);*/
			String sign = (String)reqmap.get("sign");
			String checkSignparam = PaySignUtil.getParamStr(reqmap);
			checkSignparam = checkSignparam + "&key=" + key;
			String checkSign = MD5.md5(checkSignparam, "utf-8");
			boolean checkmd5 = checkSign.equals(sign);
			if (checkmd5) {
				OrderReqInfo reqInfo = new OrderReqInfo();
				reqInfo.setCpId(cpid);
				CodeT ct = commonAction.getOtherPayCodes(reqInfo);
				String spId = "wd99";
				
				if (ct == null) {
					logger.warn("CPID=" + cpid + " 商户权限已经关闭");
					response.content("error").end();
					return;
				} else {
					for (CodeInfo info : ct.getCodes()) {
						spId = info.getSpId();
						reqInfo.setUrl(info.getUrl());
						reqInfo.setUrlNo(info.getUrlNo());
						reqInfo.setPmodel("");
						reqInfo.setSpId(spId);
						reqInfo.setReqMethod(info.getReqMethod());
						reqInfo.setKeyword(info.getKeyword());
						reqInfo.setMatchRegex(info.getMatchRegex());
						reqInfo.setBody(body);
						result = AgentPayQueryHelper.queryAgentPay(reqInfo);
					}
				}
				
			} else {
				result = "sign error";
			}
		} else {
			result = "param is empty error";
		}
		
		response.content(result.getBytes("gbk")).end();
		
	}
	
	
	/***
	 * 获取第三方支付通用接口
	 * 例子:http://120.24.88.90/code/reqPayOtherHNWY?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89&&goodsname=测试
	 */
	public void reqPayOtherHNWY(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqPayOtherHNWY请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String mobile = request.getParam("phone");
		String param = request.getParam("cpparam");
		String fee = request.getParam("fee");
		String callbackurl = request.getParam("callbackurl");
//		String pck = request.getParam("pck");//包名
		String openid = request.getParam("openid");//微信公众号openid
		String reqFlag = request.getParam("is_raw");//微信公众号用到请求标记
		String app = request.getParam("app");//应用名称
		String propname = request.getParam("goodsname");//道具名称
		String code = request.getParam("code");
		if (propname != null && propname.length() > 0) 
			propname = URLDecoder.decode(propname, "utf-8");
		if (app != null && app.length() > 0) 
			app = URLDecoder.decode(app, "utf-8");
		if (callbackurl != null && callbackurl.length() > 0) 
			callbackurl = URLDecoder.decode(callbackurl, "utf-8");
		
		String province = "";
		String iccid = request.getParam("iccid");
		if (iccid != null && iccid.length()>10) {
			logger.info("用户访问iccid=" + iccid);
			province = provinceMap.get(iccid.substring(8,10));
			logger.info("用户省份=" + province);
		}
		
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		logger.info("用户访问IP=" + ip);
		if (province == null) {
			province = IPtest.getInstance().queryProvince(ip);
			logger.info("根据IP查询省份=" + province);
		}
		
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setCpId(cpId);
		reqInfo.setImsi(imsi);
		reqInfo.setImei(imei);
		reqInfo.setMobile(mobile);
		reqInfo.setCpParam(param);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setPrice(fee);
		reqInfo.setIp(ip);
		reqInfo.setProvince(province);
		reqInfo.setIccid(iccid);
		reqInfo.setApp(app);
//		reqInfo.setPck(pck);
		reqInfo.setPck(propname);
		reqInfo.setPropname(propname);
		reqInfo.setOpenid(openid);
		reqInfo.setReqFlag(reqFlag);
		reqInfo.setCallbackurl(callbackurl);
		reqInfo.setCode(code);
		
		String sms = "error";
		try {
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			String spId = "wd99";
			
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
						+ fee + " 的指令！");
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
					reqInfo.setSpId(spId);
					reqInfo.setReqMethod(info.getReqMethod());
					reqInfo.setKeyword(info.getKeyword());
					reqInfo.setMatchRegex(info.getMatchRegex());
					sms = CodeReqHelper.getSMSCode(reqInfo);
					if (!"error".equals(sms)) {
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("省份:").append(province).append("\n");
						clog.append("计费点:").append(fee).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						break;
					}
				}
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
		
	}
	
	/***
	 * 获取虚实第三方支付通用接口
	 * 例子:http://120.24.88.90/code/reqPayOtherXSWY?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89&&goodsname=测试
	 */
	public void reqPayOtherXSWY(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqPayOtherXSWY请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String mobile = request.getParam("phone");
		String param = request.getParam("cpparam");
		String fee = request.getParam("fee");
		String callbackurl = request.getParam("callbackurl");
//		String pck = request.getParam("pck");//包名
		String openid = request.getParam("openid");//微信公众号openid
		String reqFlag = request.getParam("is_raw");//微信公众号用到请求标记
		String app = request.getParam("app");//应用名称
		String propname = request.getParam("goodsname");//道具名称
		if (propname != null && propname.length() > 0) 
			propname = URLDecoder.decode(propname, "utf-8");
		if (app != null && app.length() > 0) 
			app = URLDecoder.decode(app, "utf-8");
		if (callbackurl != null && callbackurl.length() > 0) 
			callbackurl = URLDecoder.decode(callbackurl, "utf-8");
		
		String province = "";
		String iccid = request.getParam("iccid");
		if (iccid != null && iccid.length()>10) {
			logger.info("用户访问iccid=" + iccid);
			province = provinceMap.get(iccid.substring(8,10));
			logger.info("用户省份=" + province);
		}
		
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		logger.info("用户访问IP=" + ip);
		if (province == null) {
			province = IPtest.getInstance().queryProvince(ip);
			logger.info("根据IP查询省份=" + province);
		}
		
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setCpId(cpId);
		reqInfo.setImsi(imsi);
		reqInfo.setImei(imei);
		reqInfo.setMobile(mobile);
		reqInfo.setCpParam(param);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setPrice(fee);
		reqInfo.setIp(ip);
		reqInfo.setProvince(province);
		reqInfo.setIccid(iccid);
		reqInfo.setApp(app);
//		reqInfo.setPck(pck);
		reqInfo.setPck(propname);
		reqInfo.setPropname(propname);
		reqInfo.setOpenid(openid);
		reqInfo.setReqFlag(reqFlag);
		reqInfo.setCallbackurl(callbackurl);
		
		String sms = "error";
		try {
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			String spId = "wd99";
			
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
						+ fee + " 的指令！");
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
					reqInfo.setSpId(spId);
					reqInfo.setReqMethod(info.getReqMethod());
					reqInfo.setKeyword(info.getKeyword());
					reqInfo.setMatchRegex(info.getMatchRegex());
					sms = PreReqHelper.getPreReqResult(reqInfo);
					if (!"error".equals(sms)) {
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("省份:").append(province).append("\n");
						clog.append("计费点:").append(fee).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						break;
					}
				}
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
//			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
		
	}
	
	/***
	 * 获取第三方支付结果ios
	 * 例子:http://120.24.88.90/code/getPayOtherResultIOS?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89&&goodsname=测试
	 */
	public void getPayOtherResultIOS(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("getPayOtherResultIOS请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
		String ffId = request.getParam("orderid");
		String traid = request.getParam("iosparam");
		String content = request.body();
		logger.info("getPayOtherResultIOS请求body信息："+content);
		if (content != null && content.length() > 0) {
			JSONObject jsonobj = JSONObject.parseObject(content);
			if (cpId == null || cpId.length() <= 0)
				cpId = jsonobj.getString("cpid");
			if (ffId == null || ffId.length() <= 0)
				ffId = jsonobj.getString("orderid");
			if (traid == null || traid.length() <= 0)
				traid = jsonobj.getString("iosparam");
		}
		
		OrderReqInfo reqInfo = commonAction.queryOrderReqInfo(ffId);
		String sms = "error";
		if (reqInfo != null) {
			reqInfo.setTraid(traid);
			reqInfo.setCpId(cpId);
			
			try {
				CodeT ct = commonAction.getOtherPayCodes(reqInfo);
				String spId = "wd99";
				
				if (ct == null) {
					logger.warn("CPID=" + cpId + " 无匹配！");
					response.content("error").end();
					return;
				} else {
					StringBuilder clog = new StringBuilder();
					for (CodeInfo info : ct.getCodes()) {
						spId = info.getSpId();
						ffId = ffId == null?CommonTool.genrateOrderNO(DateTool.getMonth(), info.getSpFlag()):ffId;
						reqInfo.setUrl(info.getUrl());
						reqInfo.setUrlNo(info.getUrlNo());
						reqInfo.setPmodel("");
						reqInfo.setFfId(ffId);
						reqInfo.setReqMethod(info.getReqMethod());
						reqInfo.setKeyword(info.getKeyword());
						reqInfo.setMatchRegex(info.getMatchRegex());
						sms = ResultReqHelper.getPayResult(reqInfo);
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
					
					if (sms != null) {
						JSONObject josnobj = JSONObject.parseObject(sms);
						String result = josnobj.getString("result");
						int isSuccess = CommonTool.convertInt(result);
						OrderReqInfo info = new OrderReqInfo();
						info.setFfId(ffId);
						info.setIsSyn(ConstantDefine.SYN_TRUE);
						info.setIsSuccess(isSuccess);
						reqInfo.setIsSuccess(isSuccess);
						int synStatus = processXSOrder(ct.getCp(),reqInfo);
						info.setSynStatus(synStatus);
						commonAction.updateOrderInfo(info);
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e.getCause());
			}
		} else {
			sms = "{\"result\":\"9\"}";
		}
		response.content(sms).end();
		
	}
	
	/***
	 * 获取第三方支付结果
	 * 例子:http://120.24.88.90/code/getPayOtherResult?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89&&goodsname=测试
	 */
	public void getPayOtherResult(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("getPayOtherResult请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
		String ffId = request.getParam("orderid");
		String traid = request.getParam("traid");
		
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setFfId(ffId);
		reqInfo.setTraid(traid);
		reqInfo.setCpId(cpId);
		
		String sms = "error";
		try {
			CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			String spId = "wd99";
			
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 无匹配！");
				response.content("error").end();
				return;
			} else {
				OrderReqInfo respInfo = commonAction.queryOrderReqInfo(ffId);
				if (respInfo != null) {
					spId = respInfo.getSpId();
					CodeInfo info = commonAction.queryCodeInfoBySpid(spId);
					StringBuilder clog = new StringBuilder();
					if (info != null) {
						ffId = respInfo.getFfId();
						reqInfo.setFee(respInfo.getFee());
						reqInfo.setUrl(info.getUrl());
						reqInfo.setUrlNo(info.getUrlNo());
						reqInfo.setPmodel("");
						reqInfo.setFfId(ffId);
						reqInfo.setSpId(spId);
						reqInfo.setReqMethod(info.getReqMethod());
						reqInfo.setKeyword(info.getKeyword());
						reqInfo.setMatchRegex(info.getMatchRegex());
						sms = ResultReqHelper.getPayResult(reqInfo);
						if (respInfo != null && "{\"result\":\"1\"}".equals(sms)) {
							logger.info("银行查询返回查询到sms:" + sms + "清除缓存");
							commonAction.updateOrder(ffId,true,respInfo.getSynStatus());
							commonAction.removeMemcached(OrderReqAO.ORDER_COM_PREFIX + ffId);
						}
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
					}
					logger.info(clog.toString());
				}/* else {
					logger.info("数据库中查询到ffId:" + ffId + "计费结果为:" + 0);
					Map<String, String> map = new HashMap<String, String>();
					map.put("result", "0");
					sms = JSONObject.toJSONString(map);
				}*/
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
		
	}
	
	/***
	 * 获取第三方支付结果分发
	 * 例子:http://120.24.88.90/code/getPayOtherResultMix?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89&&goodsname=测试
	 */
	public void getPayOtherResultMix(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("getPayOtherResultMix请求header信息："+request.allHeaders());
		
		String spid = request.getParam("spid");
		if ("sd27".equals(spid)) {
			getPayOtherResultIOS(request,response);
		} else {
			getPayOtherResult(request,response);
		}
		
	}
	
	/***
	 * 第三方支付退款
	 * 例子:http://120.24.88.90/code/reqPayOtherRefund?orderid=06&spid=sd25&traid=0000861497631570103320658761
	 */
	public void reqPayOtherRefund(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqPayOtherRefund请求header信息："+request.allHeaders());
		
		String ffId = request.getParam("orderid");
		String traid = request.getParam("traid");
		String spId = request.getParam("spid");
		
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setFfId(ffId);
		reqInfo.setTraid(traid);
		CodeInfo info = commonAction.queryCodeInfoBySpid(spId);
		
		String sms = "error";
		try {
			if (info != null) {
				StringBuilder clog = new StringBuilder();
				ffId = ffId == null?CommonTool.genrateOrderNO(DateTool.getMonth(), info.getSpFlag()):ffId;
				reqInfo.setFfId(ffId);
				reqInfo.setSpId(spId);
				reqInfo.setUrl(info.getUrl());
				reqInfo.setUrlNo(info.getUrlNo());
				reqInfo.setPmodel("");
				reqInfo.setReqMethod(info.getReqMethod());
				reqInfo.setKeyword(info.getKeyword());
				reqInfo.setMatchRegex(info.getMatchRegex());
				sms = RefundReqHelper.getRefundResult(reqInfo);
				if (!"error".equals(sms)) {
					JSONObject jsonobj = JSONObject.parseObject(sms);
					reqInfo.setIsSuccess(CommonTool.convertInt(jsonobj.getString("result")));
					commonAction.updateOrderByOther(reqInfo);
				}
				clog.append("通道方:").append(info.getSpId()).append("\n");
				clog.append("订单号:").append(ffId).append("\n");
				clog.append("指令内容:").append(sms);
				logger.info(clog.toString());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
		
	}
	
	/***
	 * 第三方支付退款进度查询
	 * 例子:http://120.24.88.90/code/reqPayOtherRefundQuery?orderid=06&spid=sd30&traid=0000861497631570103320658761
	 */
	public void reqPayOtherRefundQuery(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqPayOtherRefundQuery请求header信息："+request.allHeaders());
		
		String ffId = request.getParam("orderid");
		String traid = request.getParam("traid");
		String spId = request.getParam("spid");
		
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setFfId(ffId);
		reqInfo.setTraid(traid);
		CodeInfo info = commonAction.queryCodeInfoBySpid(spId);
		
		String sms = "error";
		try {
			if (info != null) {
				commonAction.updateOrderByOther(reqInfo);
				StringBuilder clog = new StringBuilder();
				ffId = ffId == null?CommonTool.genrateOrderNO(DateTool.getMonth(), info.getSpFlag()):ffId;
				reqInfo.setFfId(ffId);
				reqInfo.setSpId(spId);
				reqInfo.setUrl(info.getUrl());
				reqInfo.setUrlNo(info.getUrlNo());
				reqInfo.setPmodel("");
				reqInfo.setReqMethod(info.getReqMethod());
				reqInfo.setKeyword(info.getKeyword());
				reqInfo.setMatchRegex(info.getMatchRegex());
				sms = QueryRefundReqHelper.getRefundQueryResult(reqInfo);
				clog.append("通道方:").append(info.getSpId()).append("\n");
				clog.append("订单号:").append(ffId).append("\n");
				clog.append("指令内容:").append(sms);
				logger.info(clog.toString());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
		
	}

	/***
	 * 在线获取虚实计费指令
	 * 3.1.8版本
	 * 例子:http://120.24.88.90/code/reqXSCodeyzm?cpid=test02&imsi=460023129529560&imei=867609020232930
	 * &iccid=89860041191504691049&cpparam=klwtest&fee=1000&ip=211.139.146.117&phone=15012937395
	 */
	public void reqXSCodeyzm(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqXSCodeyzm请求header信息："+request.allHeaders());
		
		
		OrderReqInfo reqInfo = reqParamConver(request);
		
		String cpId = reqInfo.getCpId();
		String province = reqInfo.getProvince();
		String fee = reqInfo.getPrice();
		
		String sms = "error";
		try {
			CodeT ct = commonAction.getPayCodes(reqInfo);
			String spId = "wd99";
			
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
						+ fee + " 的指令！");
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
					reqInfo.setDelayed(info.getDelayed());
					reqInfo.setFees(info.getFee());
					reqInfo.setMsg(info.getMemo());
					reqInfo.setSpId(spId);
					sms = CodeReqHelper.getSMSCode(reqInfo);
					if (!"error".equals(sms)) {
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("省份:").append(province).append("\n");
						clog.append("计费点:").append(fee).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						break;
					}
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
		
	}
	
	/***
	 * 在线获取虚实计费指令
	 * 3.1.8版本
	 * 例子:http://smspay.xushihudong.com/code/commitXSCodeyzm?orderid=&traid=
	 */
	public void commitXSCodeyzm(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("commitXSCodeyzm请求header信息："+request.allHeaders());
		String ffId = request.getParam("orderid");
		String traid = request.getParam("traid");
		String cpId = request.getParam("cpid");
		String code = request.getParam("code");
		
		String sms = "error";
		try {
			CPInfo cp = commonAction.queryCP(cpId);
			if (cp == null || cp.getIsOpen() == 0) {
				logger.warn("cpid：" + cpId + " 没有配置或打开计费权限");
				response.content(sms).end();
				return;
			} else {
				OrderReqInfo reqInfo = commonAction.queryOrderReqInfo(ffId);
				if (reqInfo != null) {
					reqInfo.setTraid(traid);
					reqInfo.setCode(code);
					String spId = reqInfo.getSpId();
					CodeInfo info = commonAction.queryCodeInfoBySpid(spId);
					if (info != null) {
						reqInfo.setUrl(info.getUrl());
						reqInfo.setUrlNo(info.getUrlNo());
						reqInfo.setKeyword(info.getKeyword());
						reqInfo.setMatchRegex(info.getMatchRegex());
						reqInfo.setDelayed(info.getDelayed());
						reqInfo.setFees(info.getFee());
						reqInfo.setMsg(info.getMemo());
						sms = CodeCommitReqHelper.commitCodeResult(reqInfo);
						StringBuilder clog = new StringBuilder();
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("省份:").append(reqInfo.getProvince()).append("\n");
						clog.append("计费点:").append(reqInfo.getFee()).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						logger.info(clog.toString());
					} else {
						logger.warn("spid：" + spId + " 没有找到该通道");
					}
					OrderReqInfo order = new OrderReqInfo();
					order.setFfId(ffId);
					order.setCtech(code);
					commonAction.updateOrderInfo(order);
				} else {
					logger.warn("ffid：" + ffId + " 没有找到该订单");
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
		
	}
	
	/***
	 * 在线获取虚实计费指令
	 * 3.1.8版本
	 * 例子:http://120.24.88.90/code/reqXSCode?cpid=test02&imsi=460023129529560&imei=867609020232930
	 * &iccid=89860041191504691049&cpparam=klwtest&fee=1000&ip=211.139.146.117&phone=15012937395
	 */
	public void reqXSCode(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqXSCode请求header信息："+request.allHeaders());
		
		OrderReqInfo reqInfo = reqParamConver(request);
		
		String cpId = reqInfo.getCpId();
		String province = reqInfo.getProvince();
		String fee = reqInfo.getPrice();
		int phoneType = reqInfo.getPhoneType();
		
		String sms = "error";
		try {
			CodeT ct = commonAction.getPayCodes(reqInfo);
			String spId = "wd99";
			
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
						+ fee + " 的指令！");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				StringBuilder clog = new StringBuilder();
				for (CodeInfo info : ct.getCodes()) {
					if ("dx".equals(info.getSpFlag()) && phoneType != 3) continue;
					if ("lt".equals(info.getSpFlag()) && phoneType != 2) continue;
					spId = info.getSpId();
					ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), info.getSpFlag());
					reqInfo.setUrl(info.getUrl());
					reqInfo.setUrlNo(info.getUrlNo());
					reqInfo.setPmodel("");
					reqInfo.setFfId(ffId);
					reqInfo.setKeyword(info.getKeyword());
					reqInfo.setMatchRegex(info.getMatchRegex());
					reqInfo.setDelayed(info.getDelayed());
					reqInfo.setFees(info.getFee());
					reqInfo.setMsg(info.getMemo());
					reqInfo.setSpId(spId);
					if (spId.startsWith("dm")) {
						List<ResSDKFilter> filterrsp = commonAction.queryFilterBySpid(reqInfo);
						if (filterrsp == null || filterrsp.size() == 0) {
							sms = "error";
							logger.error("日月限限制");
							continue;
						}
					}
					sms = CodeReqHelper.getSMSCode(reqInfo);
					if (!"error".equals(sms)) {
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("省份:").append(province).append("\n");
						clog.append("计费点:").append(fee).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						break;
					}
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
		
	}
	
	/***
	 * 在线获取MM计费指令(裸代)
	 * 3.1.8版本
	 * 例子:http://120.24.88.90/code/reqSZRTCode?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89
	 */
	public void reqSZRTCode(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqSZRTCode请求header信息："+request.allHeaders());
		
		
		OrderReqInfo reqInfo = reqParamConver(request);
		
		String cpId = reqInfo.getCpId();
		String province = reqInfo.getProvince();
		String fee = reqInfo.getPrice();
		
		String sms = "error";
		try {
			CodeT ct = commonAction.getPayCodes(reqInfo);
			String spId = "wd99";
			
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
						+ fee + " 的指令！");
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
						clog.append("省份:").append(province).append("\n");
						clog.append("计费点:").append(fee).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						break;
					}
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
		
	}
	
	/***
	 * 例子:http://120.24.88.90/code/reqRTCode?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89
	 */
	public void reqRTCode(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqRTCode请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String mobile = request.getParam("mobile");
		String param = request.getParam("cpparam");
		String fee = request.getParam("fee");
		String pmodel = request.getParam("pmodel");
		if (pmodel != null && pmodel.length() > 0) {
			pmodel = URLDecoder.decode(pmodel, "utf-8");
		}
//		int serVer = 5;
//		String sdkVer = "0";
		
		String province = null;
		String iccid = request.getParam("iccid");
		if (iccid != null && iccid.length()>10) {
			logger.info("用户访问iccid=" + iccid);
			province = provinceMap.get(iccid.substring(8,10));
			logger.info("用户省份=" + province);
		}
		
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		logger.info("用户访问IP=" + ip);
		if (province == null) {
			province = IPtest.getInstance().queryProvince(ip);
			logger.info("根据IP查询省份=" + province);
		}
		
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setCpId(cpId);
		reqInfo.setImsi(imsi);
		reqInfo.setImei(imei);
		reqInfo.setMobile(mobile);
		reqInfo.setCpParam(param);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setPrice(fee);
		reqInfo.setIp(ip);
		reqInfo.setProvince(province);
		reqInfo.setIccid(iccid);
		reqInfo.setPmodel(pmodel);
		
		String sms = "error";
		try {
			CodeT ct = commonAction.getPayCodes(reqInfo);
			String spId = "wd99";
			
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
						+ fee + " 的指令！");
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
						clog.append("省份:").append(province).append("\n");
						clog.append("计费点:").append(fee).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						break;
					}
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
		
	}
	
	/***
	 * 在线获取咪咕计费指令(裸代)
	 * 例子:http://smspay.xushihudong.com/code/reqMiGuCode?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89
	 */
	public void reqMiGuCode(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqMiGuCode请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String mobile = request.getParam("mobile");
		String param = request.getParam("cpparam");
		String fee = request.getParam("fee");
//		int serVer = 5;
//		String sdkVer = "0";
		
		String iccid = request.getParam("iccid");
		String ip = request.getParam("ip");
		Map<String, String> areaMap = queryReqArea(imsi, mobile, iccid, ip);
		mobile = areaMap.get("mobile");
		String province = areaMap.get("province");
		
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setCpId(cpId);
		reqInfo.setImsi(imsi);
		reqInfo.setImei(imei);
		reqInfo.setMobile(mobile);
		reqInfo.setCpParam(param);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setPrice(fee);
		reqInfo.setIp(ip);
		reqInfo.setProvince(province);
		reqInfo.setIccid(iccid);
//		reqInfo.setSdkVer(sdkVer);
		
		String sms = "error";
		try {
			CodeT ct = commonAction.getMiGuPayCodes(reqInfo);
			String spId = "wd99";
			
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
						+ fee + " 的指令！");
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
					if (spId.startsWith("dm")) {
						List<ResSDKFilter> filterrsp = commonAction.queryFilterBySpid(reqInfo);
						if (filterrsp == null || filterrsp.size() == 0) {
							sms = "error";
							logger.error("日月限限制");
							continue;
						}
					}
					sms = CodeReqHelper.getSMSCode(reqInfo);
					if (!"error".equals(sms)) {
						if ("yy05".equals(spId) || "dm21".equals(spId)) {
							JSONObject jsonobj = JSONObject.parseObject(sms);
							reqInfo.setCtech(jsonobj.getString("url"));
							jsonobj.remove("url");
							sms = jsonobj.toJSONString();
						}
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("省份:").append(province).append("\n");
						clog.append("计费点:").append(fee).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						break;
					}
				}
				reqInfo.setSpId(spId);
				logger.info(clog.toString());
			}
			commonAction.addMiGuOrderReqInfo(reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
		
	}
	
	/***
	 * 在线获取咪咕计费指令(裸代)
	 * 例子:http://smspay.xushihudong.com/code/reqXSYzmCode?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89
	 */
	public void reqXSYzmCode(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqXSYzmCode请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String mobile = request.getParam("mobile");
		String param = request.getParam("cpparam");
		String fee = request.getParam("fee");
//		int serVer = 5;
//		String sdkVer = "0";
		
		String iccid = request.getParam("iccid");
		String ip = request.getParam("ip");
		Map<String, String> areaMap = queryReqArea(imsi, mobile, iccid, ip);
		mobile = areaMap.get("mobile");
		String province = areaMap.get("province");
		
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setCpId(cpId);
		reqInfo.setImsi(imsi);
		reqInfo.setImei(imei);
		reqInfo.setMobile(mobile);
		reqInfo.setCpParam(param);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setPrice(fee);
		reqInfo.setIp(ip);
		reqInfo.setProvince(province);
		reqInfo.setIccid(iccid);
//		reqInfo.setSdkVer(sdkVer);
		
		String sms = "error";
		try {
			CodeT ct = commonAction.getPayCodes(reqInfo);
			String spId = "wd99";
			
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
						+ fee + " 的指令！");
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
					if (spId.startsWith("dm")) {
						List<ResSDKFilter> filterrsp = commonAction.queryFilterBySpid(reqInfo);
						if (filterrsp == null || filterrsp.size() == 0) {
							sms = "error";
							logger.error("日月限限制");
							continue;
						}
					}
					sms = CodeReqHelper.getSMSCode(reqInfo);
					if (!"error".equals(sms)) {
						JSONObject jsonobj = JSONObject.parseObject(sms);
						reqInfo.setCtech(jsonobj.getString("url"));
						jsonobj.remove("url");
						sms = jsonobj.toJSONString();
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("省份:").append(province).append("\n");
						clog.append("计费点:").append(fee).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						break;
					}
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
		
	}
	
	/***
	 * 在线获取MM计费指令(裸代)
	 * 3.1.8版本
	 * 例子:http://smspay.qygame.cn/code/getsms?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89
	 */
	public void getsms(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("getsms请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String mobile = request.getParam("mobile");
		String phone = request.getParam("phone");
		String param = request.getParam("cpparam");//参数
		String fee = request.getParam("fee");//计费点价格
//		int serVer = 5;
//		String sdkVer = "0";
		
		if (mobile == null && phone != null) {
			mobile = phone;
		}
		
		String iccid = request.getParam("iccid");
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		
		Map<String, String> areaMap = queryReqArea(imsi, mobile, iccid, ip);
		mobile = areaMap.get("mobile");
		String province = areaMap.get("province");
		int phoneType = CommonTool.convertInt(areaMap.get("phoneType"));
		String city = areaMap.get("city");
		
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setCpId(cpId);
		reqInfo.setImsi(imsi);
		reqInfo.setImei(imei);
		reqInfo.setMobile(mobile);
		reqInfo.setCpParam(param);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setPrice(fee);
		reqInfo.setIp(ip);
		reqInfo.setProvince(province);
		reqInfo.setIccid(iccid);
		reqInfo.setCity(city);
		reqInfo.setPhoneType(phoneType);
//		reqInfo.setSdkVer(sdkVer);
		
		String sms = "error";
		try {
			
			boolean flag = commonAction.queryBlackMobile(mobile);
			
			if (flag) {
				logger.warn("mobile=" + mobile);
				sms = "mobile is black";
				response.content(sms).end();
				return;
			}
			
			CodeT ct = commonAction.getPayCodes(reqInfo);
			String spId = "wd99";
			
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
						+ fee + " 的指令！");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				StringBuilder clog = new StringBuilder();
				for (CodeInfo info : ct.getCodes()) {
					spId = info.getSpId();
					if ("yc38".equals(spId) || "yc40".equals(spId)) {
						ffId = CommonTool.genratexsOrderNO(DateTool.getMonth(), info.getSpFlag());
					} else {
						ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), info.getSpFlag());
					}
					reqInfo.setUrl(info.getUrl());
					reqInfo.setUrlNo(info.getUrlNo());
					reqInfo.setPmodel("");
					reqInfo.setFfId(ffId);
					reqInfo.setKeyword(info.getKeyword());
					reqInfo.setMatchRegex(info.getMatchRegex());
					reqInfo.setFees(info.getFee());
					reqInfo.setMsg(info.getMemo());
					reqInfo.setDelayed(info.getDelayed());
					reqInfo.setSpId(spId);
					if (spId.startsWith("dm")) {
						List<ResSDKFilter> filterrsp = commonAction.queryFilterBySpid(reqInfo);
						if (filterrsp == null || filterrsp.size() == 0) {
							sms = "error";
							logger.error("日月限限制");
							continue;
						}
					}
					sms = CodeReqHelper.getSMSCode(reqInfo);
					if (!"error".equals(sms)) {
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("省份:").append(province).append("\n");
						clog.append("计费点:").append(fee).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append(cpId+"指令内容:").append(sms);
						break;
					}
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms.getBytes("gbk")).end();
		
	}
	
	/***
	 * 在线获取MM计费指令(裸代)
	 * 3.1.8版本
	 * 例子:http://120.24.88.90/code/reqSpecialMixCode5?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89
	 */
	public void reqSpecialMixCode5(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqSpecialMixCode5请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String mobile = request.getParam("mobile");
		String phone = request.getParam("phone");
		String param = request.getParam("cpparam");//参数
		String fee = request.getParam("fee");//计费点价格
//		int serVer = 5;
//		String sdkVer = "0";
		
		if (mobile == null && phone != null) {
			mobile = phone;
		}
		
		String iccid = request.getParam("iccid");
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		
		Map<String, String> areaMap = queryReqArea(imsi, mobile, iccid, ip);
		mobile = areaMap.get("mobile");
		String province = areaMap.get("province");
		int phoneType = CommonTool.convertInt(areaMap.get("phoneType"));
		String city = areaMap.get("city");
		
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setCpId(cpId);
		reqInfo.setImsi(imsi);
		reqInfo.setImei(imei);
		reqInfo.setMobile(mobile);
		reqInfo.setCpParam(param);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setPrice(fee);
		reqInfo.setIp(ip);
		reqInfo.setProvince(province);
		reqInfo.setIccid(iccid);
		reqInfo.setCity(city);
		reqInfo.setPhoneType(phoneType);
//		reqInfo.setSdkVer(sdkVer);
		
		String sms = "error";
		try {
			CodeT ct = commonAction.getPayCodes(reqInfo);
			String spId = "wd99";
			
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
						+ fee + " 的指令！");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				StringBuilder clog = new StringBuilder();
				for (CodeInfo info : ct.getCodes()) {
					spId = info.getSpId();
					if ("yc38".equals(spId) || "yc40".equals(spId)) {
						ffId = CommonTool.genratexsOrderNO(DateTool.getMonth(), info.getSpFlag());
					} else {
						ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), info.getSpFlag());
					}
					reqInfo.setUrl(info.getUrl());
					reqInfo.setUrlNo(info.getUrlNo());
					reqInfo.setPmodel("");
					reqInfo.setFfId(ffId);
					reqInfo.setKeyword(info.getKeyword());
					reqInfo.setMatchRegex(info.getMatchRegex());
					reqInfo.setFees(info.getFee());
					reqInfo.setMsg(info.getMemo());
					reqInfo.setDelayed(info.getDelayed());
					reqInfo.setSpId(spId);
					if (spId.startsWith("dm")) {
						List<ResSDKFilter> filterrsp = commonAction.queryFilterBySpid(reqInfo);
						if (filterrsp == null || filterrsp.size() == 0) {
							sms = "error";
							logger.error("日月限限制");
							continue;
						}
					}
					sms = CodeReqHelper.getSMSCode(reqInfo);
					if (!"error".equals(sms)) {
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("省份:").append(province).append("\n");
						clog.append("计费点:").append(fee).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append(cpId+"指令内容:").append(sms);
						break;
					}
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
		
	}
	/***
	 * 在线获取MM计费指令(裸代)
	 * 3.1.8版本
	 * 例子:http://120.24.88.90/code/reqSpecialMixCode6?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89
	 */
	public void reqSpecialMixCode6(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqSpecialMixCode6请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String mobile = request.getParam("mobile");
		String phone = request.getParam("phone");
		String param = request.getParam("cpserverparam");//参数
		String fee = request.getParam("price");//计费点价格
		String gamename=request.getParam("appName");
		String Cpfeename=request.getParam("Cpfeename");
//		int serVer = 5;
//		String sdkVer = "0";
		
		if (mobile == null && phone != null) {
			mobile = phone;
		}
		
		String iccid = request.getParam("iccid");
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		
		Map<String, String> areaMap = queryReqArea(imsi, mobile, iccid, ip);
		mobile = areaMap.get("mobile");
		String province = areaMap.get("province");
		int phoneType = CommonTool.convertInt(areaMap.get("phoneType"));
		String city = areaMap.get("city");
		
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setCpId(cpId);
		reqInfo.setImsi(imsi);
		reqInfo.setImei(imei);
		reqInfo.setMobile(mobile);
		reqInfo.setCpParam(param);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setPrice(fee);
		reqInfo.setIp(ip);
		reqInfo.setProvince(province);
		reqInfo.setIccid(iccid);
		reqInfo.setCity(city);
		reqInfo.setPhoneType(phoneType);
		reqInfo.setGamename(gamename);
		reqInfo.setPropname(Cpfeename);
//		reqInfo.setSdkVer(sdkVer);
		
		String sms = "error";
		try {
			CodeT ct = commonAction.getPayCodes(reqInfo);
			String spId = "wd99";
			
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
						+ fee + " 的指令！");
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
					reqInfo.setDelayed(info.getDelayed());
					reqInfo.setSpId(spId);
					if (spId.startsWith("dm")) {
						List<ResSDKFilter> filterrsp = commonAction.queryFilterBySpid(reqInfo);
						if (filterrsp == null || filterrsp.size() == 0) {
							sms = "error";
							logger.error("日月限限制");
							continue;
						}
					}
					sms = CodeReqHelper.getSMSCode(reqInfo);
					if (!"error".equals(sms)) {
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("省份:").append(province).append("\n");
						clog.append("计费点:").append(fee).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append(cpId+"指令内容:").append(sms);
						break;
					}
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
		
	}
	/***
	 * 在线获取MM计费指令(裸代)
	 * 3.1.8版本
	 * 例子:http://120.24.88.90/code/reqDXPayCode?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89
	 */
	public void reqDXPayCode(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqDXPayCode请求header信息："+request.allHeaders());
		
		OrderReqInfo reqInfo = reqParamConver(request);
		String cpId = reqInfo.getCpId();
		String province = reqInfo.getProvince();
		String fee = reqInfo.getPrice();
		String sms = "error";
		try {
			CodeT ct = commonAction.getPayCodes(reqInfo);
			String spId = "wd99";
			
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
						+ fee + " 的指令！");
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
					if (spId.startsWith("dm")) {
						List<ResSDKFilter> filterrsp = commonAction.queryFilterBySpid(reqInfo);
						if (filterrsp == null || filterrsp.size() == 0) {
							sms = "error";
							logger.error("日月限限制");
							continue;
						}
					}
					sms = CodeReqHelper.getSMSCode(reqInfo);
					if (!"error".equals(sms)) {
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("省份:").append(province).append("\n");
						clog.append("计费点:").append(fee).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						break;
					}
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
		
	}
	
	/***
	 * 在线获取MM计费指令(裸代)
	 * 3.1.8版本
	 * 例子:http://120.24.88.90/code/reqszrtcode?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89
	 */
	public void reqszrtcode(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqszrtcode请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String mobile = request.getParam("mobile");
		String param = request.getParam("cpparam");
		String fee = request.getParam("fee");
//		int serVer = 5;
//		String sdkVer = "0";
		
		String province = null;
		String iccid = request.getParam("iccid");
		if (iccid != null && iccid.length()>10) {
			logger.info("用户访问iccid=" + iccid);
			province = provinceMap.get(iccid.substring(8,10));
			logger.info("用户省份=" + province);
		}
		
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		logger.info("用户访问IP=" + ip);
		if (province == null) {
			province = IPtest.getInstance().queryProvince(ip);
			logger.info("根据IP查询省份=" + province);
		}
		
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setCpId(cpId);
		reqInfo.setImsi(imsi);
		reqInfo.setImei(imei);
		reqInfo.setMobile(mobile);
		reqInfo.setCpParam(param);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setPrice(fee);
		reqInfo.setIp(ip);
		reqInfo.setProvince(province);
		reqInfo.setIccid(iccid);
//		reqInfo.setSdkVer(sdkVer);
		
		String sms = "error";
		try {
			CodeT ct = commonAction.getPayCodes(reqInfo);
			String spId = "wd99";
			
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
						+ fee + " 的指令！");
				response.content("error").end();
				return;
			} else {
				String ffId = ct.getFfId();
				StringBuilder clog = new StringBuilder();
				for (CodeInfo info : ct.getCodes()) {
					spId = info.getSpId();
					ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), info.getSpFlag());
					if ("ec10".equals(spId)) ffId = ffId.substring(0,12);
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
						clog.append("省份:").append(province).append("\n");
						clog.append("计费点:").append(fee).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						break;
					}
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
		
	}
	
	/***
	 * 在线获取MM计费指令(裸代)
	 * 3.1.8版本
	 * 例子:http://120.24.88.90/code/reqRDOCode?cpid=klw01&imsi=460000000462781&imei=357806046192158&cpparam=klwtest&fee=1&ip=112.124.10.89
	 */
	public void reqRDOCode(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqRDOCode请求header信息："+request.allHeaders());
		
		OrderReqInfo reqInfo = reqParamConver(request);
		String cpId = reqInfo.getCpId();
		String province = reqInfo.getProvince();
		String fee = reqInfo.getPrice();
		
		String sms = "error";
		try {
			CodeT ct = commonAction.getPayCodes(reqInfo);
			String spId = "wd99";
			
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
						+ fee + " 的指令！");
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
						clog.append("省份:").append(province).append("\n");
						clog.append("计费点:").append(fee).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						break;
					}
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			commonAction.addOrderReqInfo(ct, reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(sms).end();
		
	}
	
	
	
	/***
	 * 电信计费指令获取
	 * http://120.24.88.90/code/reqDXCode
	 */
	public void reqDXCode(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String mobile = request.getParam("mobile");
		String param = request.getParam("cpparam");
		String pmodel = request.getParam("pmodel");			// 手机型号
		String osversion = request.getParam("osversion");	// 系统版本
		String nwtype = request.getParam("nwtype");			// 网络类型
		String ctech = request.getParam("ctech");			// 通信制式 GSM、CDMA、3G、4G等
		String fee = request.getParam("fee");
		String gamename = request.getParam("gamename");		//游戏名称
		String propname = request.getParam("propname");		//道具名称
		int serVer = 0;
		String specify = "dx";
		
		String province = null;
		String iccid = null;
		/*String iccid = request.getParam("iccid");
		if (iccid != null && iccid.length()>10) {
			logger.info("用户访问iccid=" + iccid);
			province = provinceMap.get(iccid.substring(8,10));
			logger.info("用户省份=" + province);
		}*/
		
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		logger.info("用户访问IP=" + ip);
		if (province == null) {
			if ("0.0.0.0".equals(ip)) {
				province = "中国";
			} else {
				province = IPtest.getInstance().queryProvince(ip);
			}
			logger.info("根据IP查询省份=" + province);
		}
		
		String sms = "error";
		try {
			CodeT ct = commonAction.getSpecifyCode(cpId, ip, province, fee, imei, imsi, iccid,
					mobile, param, pmodel, osversion, nwtype, ctech, serVer, specify);
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
						+ fee + " 的指令！");
				response.content("error").end();
				return;
			} else {
				OrderReqInfo reqInfo = new OrderReqInfo();
				reqInfo.setCpId(cpId);
				reqInfo.setImsi(imsi);
				reqInfo.setImei(imei);
				reqInfo.setMobile(mobile);
				reqInfo.setCpParam(param);
				reqInfo.setPmodel(pmodel);
				reqInfo.setOsversion(osversion);
				reqInfo.setNwtype(nwtype);
				reqInfo.setCtech(ctech);
				reqInfo.setFee(CommonTool.convertInt(fee));
				reqInfo.setIp(ip);
				reqInfo.setProvince(province);
				reqInfo.setIccid(iccid);
				reqInfo.setFfId(ct.getFfId());
				reqInfo.setUrl(ct.getUrl());
				reqInfo.setUrlNo(ct.getUrlNO());
				reqInfo.setGamename(gamename);
				reqInfo.setPropname(propname);
				sms = CodeReqHelper.getSMSCode(reqInfo);
				
				StringBuilder clog = new StringBuilder();
				clog.append("合作方:").append(ct.getCpName()).append("\n");
				clog.append("通道方:").append(ct.getSpName()).append("\n");
				clog.append("省份:").append(province).append("\n");
				clog.append("计费点:").append(fee).append("\n");
				clog.append("订单号:").append(ct.getFfId()).append("\n");
				clog.append("指令内容:").append(sms);
				logger.info(clog.toString());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		
		response.content(sms).end();
	}
	
	/***
	 * PC页游计费指令获取
	 * http://120.24.88.90/code/reqYYCode
	 */
	public void reqYYCode(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String mobile = request.getParam("mobile");
		String param = request.getParam("cpparam");
		String pmodel = request.getParam("pmodel");			// 手机型号
		String osversion = request.getParam("osversion");	// 系统版本
		String nwtype = request.getParam("nwtype");			// 网络类型
		String ctech = request.getParam("ctech");			// 通信制式 GSM、CDMA、3G、4G等
		String fee = request.getParam("fee");
		int serVer = 0;
		String specify = "yy";
		
		String province = null;
		String iccid = null;
		/*String iccid = request.getParam("iccid");
		if (iccid != null && iccid.length()>10) {
			logger.info("用户访问iccid=" + iccid);
			province = provinceMap.get(iccid.substring(8,10));
			logger.info("用户省份=" + province);
		}*/
		
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		logger.info("用户访问IP=" + ip);
		if (province == null) {
			if ("0.0.0.0".equals(ip)) {
				province = "中国";
			} else {
				province = IPtest.getInstance().queryProvince(ip);
			}
			logger.info("根据IP查询省份=" + province);
		}
		
		String sms = "error";
		try {
			CodeT ct = commonAction.getSpecifyCode(cpId, ip, province, fee, imei, imsi, iccid,
					mobile, param, pmodel, osversion, nwtype, ctech, serVer, specify);
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
						+ fee + " 的指令！");
				response.content("error").end();
				return;
			} else {
				OrderReqInfo reqInfo = new OrderReqInfo();
				reqInfo.setCpId(cpId);
				reqInfo.setImsi(imsi);
				reqInfo.setImei(imei);
				reqInfo.setMobile(mobile);
				reqInfo.setCpParam(param);
				reqInfo.setPmodel(pmodel);
				reqInfo.setOsversion(osversion);
				reqInfo.setNwtype(nwtype);
				reqInfo.setCtech(ctech);
				reqInfo.setFee(CommonTool.convertInt(fee));
				reqInfo.setIp(ip);
				reqInfo.setProvince(province);
				reqInfo.setIccid(iccid);
				reqInfo.setFfId(ct.getFfId());
				reqInfo.setUrl(ct.getUrl());
				reqInfo.setUrlNo(ct.getUrlNO());
				sms = CodeReqHelper.getSMSCode(reqInfo);
				
				StringBuilder clog = new StringBuilder();
				clog.append("合作方:").append(ct.getCpName()).append("\n");
				clog.append("通道方:").append(ct.getSpName()).append("\n");
				clog.append("省份:").append(province).append("\n");
				clog.append("计费点:").append(fee).append("\n");
				clog.append("订单号:").append(ct.getFfId()).append("\n");
				clog.append("指令内容:").append(sms);
				logger.info(clog.toString());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		
		response.content(sms).end();
	}
	

	/***
	 * 虚实互动sdk初始化
	 * 
	 * http://120.24.88.90/code/reqXSInitSDK
	 * 例子:http://120.24.88.90/code/reqXSInitSDK?cpid=test02&version=1
	 */
	public void reqXSInitSDK(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqXSInitSDK请求header信息："+request.allHeaders());
		String result = "{\"status\":\"-1\",\"spid\":\"dm18\"}";
		
		try {
			/*CodeT ct = commonAction.getOtherPayCodes(reqInfo);
			
			if (ct == null) {
				logger.warn("reqRegister没有注册通道");
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
					reqInfo = RegisterReqHelper.getSMSRegister(reqInfo);
					RegisterRes reg = reqInfo.getReg();
					if (reg != null) {
						sms = JSONObject.toJSONString(reg);
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						sms = AESUtil.encrypt(reqInfo.getFfId(), sms) + reqInfo.getFfId();
						break;
					}
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
			}
			reqInfo.setPck(reqInfo.getUsername());
			reqInfo.setApp(reqInfo.getPassword());
			reqInfo.setSdkVer(reqInfo.getCode());
			
			commonAction.addOrderReqInfo(ct, reqInfo);*/
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(result).end();
	}
	
	/***
	 * 虚实互动sdk支付请求
	 * 
	 * http://120.24.88.90/code/reqXSCodeSDK
	 */
	public void reqXSCodeSDK(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqXSCodeSDK请求header信息："+request.allHeaders());
		String result = "{\"status\":\"-1\"}";
		String secretkey = "1234567891234567";
		result = AESUtil.encrypt(secretkey, result) + secretkey;
		OrderReqInfo reqInfo = reqSDKAESParamConver(request);
		
		String sms = "error";
		String spId = "wd99";
		String cpId = reqInfo.getCpId();
		int phoneType = reqInfo.getPhoneType();
		
		try {
			CodeT ct = commonAction.getSdkPayCodes(reqInfo);
			
			if ("0".equals(ct.getStatus())) {
				String ffId = ct.getFfId();
				StringBuilder clog = new StringBuilder();
				boolean flag = true;
				while (flag) {
					
					int fee = reqInfo.getFee();
					
					for (CodeInfo info : ct.getCodes()) {
						if (!info.getFee().contains(fee+"#")) continue;
						if (phoneType == 3 && !"dx".equals(info.getSpFlag())) continue;
						if (phoneType == 2 && !"lt".equals(info.getSpFlag())) continue;
						if (phoneType == 1 && ("dx".equals(info.getSpFlag()) 
								|| "lt".equals(info.getSpFlag()))) continue;
						
						spId = info.getSpId();
						ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), info.getSpFlag());
						reqInfo.setUrl(info.getUrl());
						reqInfo.setUrlNo(info.getUrlNo());
						reqInfo.setDelayed(info.getDelayed());
						reqInfo.setFfId(ffId);
						reqInfo.setKeyword(info.getKeyword());
						reqInfo.setMatchRegex(info.getMatchRegex());
						reqInfo.setFees(info.getFee());
						reqInfo.setMsg(info.getMemo());
						reqInfo.setSpId(spId);
						List<SmsFilter> smsfilter = commonAction.querySmsFilterBySpid(reqInfo);
						if (smsfilter != null && smsfilter.size() > 0) {
							for (SmsFilter sf : smsfilter) {
								String isscreen = sf.getIsscreen();
								if ("2".equals(isscreen)) {
									StringBuilder sb = new StringBuilder();
									sb.append(sf.getBackurl()).append("?orderid=").append(ffId)
									.append("&smscontent=");
									sf.setBackurl(sb.toString());
								}
							}
						} else {
							logger.info("spid:"+spId+"，reqXSCodeSDK日月限制，尝试从下一个通道获取！");
							continue;
						}
						reqInfo = CodeSDKReqHelper.getPayCode(reqInfo);
						XSPayCodeInfo paycode = reqInfo.getPaycode();
						if (paycode != null) {
							paycode.setSmsfilter(smsfilter);
							sms = JSONObject.toJSONString(paycode);
							clog.append("合作方:").append(cpId).append("\n");
							clog.append("通道方:").append(info.getSpId()).append("\n");
							clog.append("订单号:").append(ffId).append("\n");
							clog.append("指令内容:").append(sms);
							sms = AESUtil.encrypt(reqInfo.getFfId(), sms) + reqInfo.getFfId();
							result = sms;
							flag = false;
							break;
						}
					}
					
					if (flag) {
						if (fee > 100) {
							logger.info("原计费点:"+fee);
							if (fee <= 600) {
								fee = fee - 100;
							} else if (fee <= 1000) {
								fee = fee - 200;
							} else if (fee <= 1500) {
								fee = 1000;
							} else {
								fee = 1500;
							}
							logger.info("新计费点:"+fee);
							reqInfo.setFee(fee);
							reqInfo.setPrice(fee+"");
						} else {
							flag = false;
							logger.info("spid:"+spId+"，fee:"+fee+"，reqXSCodeSDK获取指令失败，尝试从下一个通道获取！");
						}
					} else {
						logger.info("spid:"+spId+"，fee:"+fee+"，reqXSCodeSDK获取指令成功或已经最低价，无需继续溢价");
					}
					
				}
				reqInfo.setSpId(spId);
				ct.setFfId(ffId);
				logger.info(clog.toString());
				commonAction.addOrderReqInfo(ct, reqInfo);
			} else {
				logger.error(ct.getMsg());
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(result).end();
	}
	
	/***
	 * 虚实互动sdk支付请求计费结果
	 * 
	 * http://120.24.88.90/code/reqXSCodeSDKRes
	 */
	public void reqXSCodeSDKRes(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqXSCodeSDKRes请求header信息："+request.allHeaders());
		String result = "{\"status\":\"-1\"}";
		OrderReqInfo reqInfo = reqSDKAESResParamConver(request);
		
		result = AESUtil.encrypt(reqInfo.getFfId(), result) + reqInfo.getFfId();
		
		String sms = "error";
		String spId = reqInfo.getSpId();
		String cpId = reqInfo.getCpId();
		int phoneType = reqInfo.getPhoneType();
		
		try {
			CodeT ct = commonAction.getSdkPayCodes(reqInfo);
			
			if ("0".equals(ct.getStatus())) {
				String ffId = reqInfo.getFfId();
				StringBuilder clog = new StringBuilder();
				for (CodeInfo info : ct.getCodes()) {
					if ("dx".equals(info.getSpFlag()) && phoneType != 3) continue;
					if ("lt".equals(info.getSpFlag()) && phoneType != 2) continue;
					spId = info.getSpId();
					reqInfo.setUrl(info.getUrl());
					reqInfo.setUrlNo(info.getUrlNo());
					reqInfo.setKeyword(info.getKeyword());
					reqInfo.setMatchRegex(info.getMatchRegex());
					reqInfo.setDelayed(info.getDelayed());
					reqInfo.setFees(info.getFee());
					reqInfo.setMsg(info.getMemo());
					reqInfo.setSpId(spId);
					/*if (spId.startsWith("dm")) {
						List<ResSDKFilter> filterrsp = commonAction.queryFilterBySpid(reqInfo);
						if (filterrsp == null || filterrsp.size() == 0) {
							sms = "error";
							logger.error("日月限限制");
							continue;
						}
					}*/
					reqInfo = CodeSDKReqHelper.getPayCode(reqInfo);
					XSPayCodeInfo paycode = reqInfo.getPaycode();
					List<SmsFilter> smsfilter = commonAction.querySmsFilterBySpid(reqInfo);
					if (smsfilter != null && smsfilter.size() > 0) {
						for (SmsFilter sf : smsfilter) {
							String isscreen = sf.getIsscreen();
							if ("2".equals(isscreen)) {
								StringBuilder sb = new StringBuilder();
								sb.append(sf.getBackurl()).append("?orderid=").append(ffId)
								.append("&smscontent=");
								sf.setBackurl(sb.toString());
							}
						}
					}
					paycode.setSmsfilter(smsfilter);
					if (paycode != null) {
						sms = JSONObject.toJSONString(paycode);
						clog.append("合作方:").append(cpId).append("\n");
						clog.append("通道方:").append(info.getSpId()).append("\n");
						clog.append("订单号:").append(ffId).append("\n");
						clog.append("指令内容:").append(sms);
						sms = AESUtil.encrypt(reqInfo.getFfId(), sms) + reqInfo.getFfId();
						result = sms;
						break;
					}
				}
				logger.info(clog.toString());
				OrderReqInfo info = new OrderReqInfo();
				info.setFfId(reqInfo.getFfId());
				info.setSpId(spId);
				commonAction.updateOrderInfo(info);
			} else {
				logger.error(ct.getMsg());
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(result).end();
	}
	
	/***
	 * 虚实互动sdk短信提交
	 * 
	 * http://sdk.xushihudong.com/code/commitXSCodeSDKSms
	 */
	public void commitXSCodeSDKSms(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("commitXSCodeSDKSms请求header信息："+request.allHeaders());
		String result = "ok";
		String orderId = request.getParam("orderid");
		String smscontent = request.getParam("smscontent");
		if (smscontent != null && smscontent.length() > 0)
			smscontent = URLDecoder.decode(smscontent, "utf-8");
		
		String sms = "error";
		try {
			
			OrderReqInfo reqInfo = commonAction.queryOrderReqInfo(orderId);
			if (reqInfo != null) {
				reqInfo.setSmscontent(smscontent);
				String spId = reqInfo.getSpId();
				String cpId = reqInfo.getCpId();
				CodeInfo info = commonAction.queryCodeInfoBySpid(spId);
				if (info != null) {
					reqInfo.setUrl(info.getUrl());
					reqInfo.setUrlNo(info.getUrlNo());
					reqInfo.setKeyword(info.getKeyword());
					reqInfo.setMatchRegex(info.getMatchRegex());
					reqInfo.setDelayed(info.getDelayed());
					reqInfo.setFees(info.getFee());
					reqInfo.setMsg(info.getMemo());
					sms = CodeCommitReqHelper.commitCodeResult(reqInfo);
					StringBuilder clog = new StringBuilder();
					clog.append("合作方:").append(cpId).append("\n");
					clog.append("通道方:").append(info.getSpId()).append("\n");
					clog.append("省份:").append(reqInfo.getProvince()).append("\n");
					clog.append("计费点:").append(reqInfo.getFee()).append("\n");
					clog.append("订单号:").append(orderId).append("\n");
					clog.append("指令内容:").append(sms);
					logger.info(clog.toString());
				} else {
					logger.warn("spid：" + spId + " 没有找到该通道");
				}
				OrderReqInfo order = new OrderReqInfo();
				order.setFfId(orderId);
				order.setCtech(smscontent);
				commonAction.updateOrderInfo(order);
			} else {
				logger.warn("ffid：" + orderId + " 没有找到该订单");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(result).end();
	}
	
	/***
	 * 虚实互动短信提交
	 * 
	 * http://smspay.xushihudong.com/code/commitXSCodeSms
	 */
	public void commitXSCodeSms(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("commitXSCodeSms请求header信息："+request.allHeaders());
		String result = "ok";
		String orderId = request.getParam("orderid");
		String smscontent = request.getParam("smscontent");
		String code = request.getParam("code");
		String ctech = "";
		if (smscontent != null && smscontent.length() > 0){
			smscontent = URLDecoder.decode(smscontent, "utf-8");
			ctech = smscontent;
		}
		if (code != null && code.length() > 0)
			ctech = code;
		
		String sms = "error";
		try {
			
			OrderReqInfo reqInfo = commonAction.queryOrderReqInfo(orderId);
			if (reqInfo != null) {
//				ctech = reqInfo.getCtech();
				reqInfo.setSmscontent(smscontent);
				reqInfo.setCode(code);
				String spId = reqInfo.getSpId();
				String cpId = reqInfo.getCpId();
				CodeInfo info = commonAction.queryCodeInfoBySpid(spId);
				if (info != null) {
					reqInfo.setUrl(info.getUrl());
					reqInfo.setUrlNo(info.getUrlNo());
					reqInfo.setKeyword(info.getKeyword());
					reqInfo.setMatchRegex(info.getMatchRegex());
					reqInfo.setDelayed(info.getDelayed());
					reqInfo.setFees(info.getFee());
					reqInfo.setMsg(info.getMemo());
					sms = CodeCommitReqHelper.commitCodeResult(reqInfo);
					StringBuilder clog = new StringBuilder();
					clog.append("合作方:").append(cpId).append("\n");
					clog.append("通道方:").append(info.getSpId()).append("\n");
					clog.append("省份:").append(reqInfo.getProvince()).append("\n");
					clog.append("计费点:").append(reqInfo.getFee()).append("\n");
					clog.append("订单号:").append(orderId).append("\n");
					clog.append("指令内容:").append(sms);
					logger.info(clog.toString());
				} else {
					logger.warn("spid：" + spId + " 没有找到该通道");
				}
				OrderReqInfo order = new OrderReqInfo();
				order.setFfId(orderId);
				order.setCtech(ctech);
				commonAction.updateOrderInfo(order);
			} else {
				logger.warn("ffid：" + orderId + " 没有找到该订单");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(result).end();
	}

	/***
	 * 测试虚实互动sdk支付请求
	 * 
	 * http://120.24.88.90/code/reqXSCodeSDKtest
	 * 例子:http://120.24.88.90/code/reqXSCodeSDKtest?imei=867427020866445&iccid=89860059179493636085
	 * 		&cpid=test02&fee=1000&imsi=46000363099391&phone=13762143362
	 */
	public void reqXSCodeSDKtest(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("reqXSCodeSDKtest请求header信息："+request.allHeaders());
		String result = "{\"status\":\"-1\"}";
		
		String url = "http://127.0.0.1:9911/code/reqXSCodeSDK";
		String cpid = request.getParam("cpid");
		String fee = request.getParam("fee");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String phone = request.getParam("phone");
		String cpparam = request.getParam("cpparam");
		String iccid = request.getParam("iccid");
		Map<String, String> map = new HashMap<String, String>();
		map.put("cpid", cpid);
		map.put("fee", fee);
		map.put("imsi", imsi);
		map.put("imei", imei);
		map.put("phone", phone);
		map.put("cpparam", cpparam);
		map.put("iccid", iccid);
		
		String jsonstr = JSONObject.toJSONString(map);
		
		String ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), "oo");
		
		String param = AESUtil.encrypt(ffId, jsonstr) + ffId;
		
		result = HttpTool.sendPost(url, param, "8000");
		response.content(result).end();
	}
	
	/***
	 * PC页游计费指令获取
	 * http://120.24.88.90/code/reqLTCode
	 */
	public void reqLTCode(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("请求header信息："+request.allHeaders());
		
		String cpId = request.getParam("cpid");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String mobile = request.getParam("mobile");
		String param = request.getParam("cpparam");
		String pmodel = request.getParam("pmodel");			// 手机型号
		String osversion = request.getParam("osversion");	// 系统版本
		String nwtype = request.getParam("nwtype");			// 网络类型
		String ctech = request.getParam("ctech");			// 通信制式 GSM、CDMA、3G、4G等
		String fee = request.getParam("fee");
		int serVer = 0;
		String specify = "lt";
		
		String province = null;
		String iccid = null;
		/*String iccid = request.getParam("iccid");
		if (iccid != null && iccid.length()>10) {
			logger.info("用户访问iccid=" + iccid);
			province = provinceMap.get(iccid.substring(8,10));
			logger.info("用户省份=" + province);
		}*/
		
		String ip = request.getParam("ip");
		ip = ip==null?getRealIP(request):ip;
		logger.info("用户访问IP=" + ip);
		if (province == null) {
			if ("0.0.0.0".equals(ip)) {
				province = "中国";
			} else {
				province = IPtest.getInstance().queryProvince(ip);
			}
			logger.info("根据IP查询省份=" + province);
		}
		
		String sms = "error";
		try {
			CodeT ct = commonAction.getSpecifyCode(cpId, ip, province, fee, imei, imsi, iccid,
					mobile, param, pmodel, osversion, nwtype, ctech, serVer, specify);
			if (ct == null) {
				logger.warn("CPID=" + cpId + " 匹配不到合适省份=" + province + " 计费点="
						+ fee + " 的指令！");
				response.content("error").end();
				return;
			} else {
				OrderReqInfo reqInfo = new OrderReqInfo();
				reqInfo.setCpId(cpId);
				reqInfo.setImsi(imsi);
				reqInfo.setImei(imei);
				reqInfo.setMobile(mobile);
				reqInfo.setCpParam(param);
				reqInfo.setPmodel(pmodel);
				reqInfo.setOsversion(osversion);
				reqInfo.setNwtype(nwtype);
				reqInfo.setCtech(ctech);
				reqInfo.setFee(CommonTool.convertInt(fee));
				reqInfo.setIp(ip);
				reqInfo.setProvince(province);
				reqInfo.setIccid(iccid);
				reqInfo.setFfId(ct.getFfId());
				reqInfo.setUrl(ct.getUrl());
				reqInfo.setUrlNo(ct.getUrlNO());
				sms = CodeReqHelper.getSMSCode(reqInfo);
				
				StringBuilder clog = new StringBuilder();
				clog.append("合作方:").append(ct.getCpName()).append("\n");
				clog.append("通道方:").append(ct.getSpName()).append("\n");
				clog.append("省份:").append(province).append("\n");
				clog.append("计费点:").append(fee).append("\n");
				clog.append("订单号:").append(ct.getFfId()).append("\n");
				clog.append("指令内容:").append(sms);
				logger.info(clog.toString());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		
		response.content(sms).end();
	}
	
	/***
	 * 获取手机号码
	 * http://120.24.88.90/code/reqMobile
	 */
	public void reqMobile(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("请求header信息："+request.allHeaders());
		
		String imsi = request.getParam("imsi");
		String iccid = request.getParam("iccid");
		String cpId = request.getParam("cpid");
		
		OrderReqInfo reqInfo = new OrderReqInfo();
		reqInfo.setImsi(imsi);
		reqInfo.setIccid(iccid);
		reqInfo.setAimsi("2");
		reqInfo.setCpId(cpId);
		
		String sms = "error";
		try {
			CPInfo cpInfo = commonAction.getCPInfo(reqInfo);//获取对应cpId信息
			if (cpInfo != null)
				sms = commonAction.getMobile(reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		
		response.content(sms).end();
	}
	
	/***
	 * 请求转发上传验证码
	 * http://120.24.88.90/code/forwardCode
	 */
	public void forwardCode(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("forwardCode请求header信息："+request.allHeaders());
		String content = request.body();
		logger.info("forwardCode的post内容："+content);
		
		String[] sArr = content.split("#####");
		String url = sArr[0];
		String sign = MD5Tool.toMD5Value32(sArr[1]).toUpperCase();
		url = url + sign;
		String result = HttpTool.sendHttp(url, "");
		logger.info("forwardCodes上传结果："+result);

		response.content("ok").end();
	}
	
	
	
	
	private String getQXTJson(OrderReqInfo reqInfo, CodeInfo info) {
		
		PayConJson payJson = new PayConJson();
		payJson.setType("0");
		payJson.setAddress(ConstantDefine.RES_QXT_PORT);
		payJson.setContent(ConstantDefine.RES_QXT_SMS+reqInfo.getImsi()+"#"+reqInfo.getAimsi()+"#"+reqInfo.getIccid());
		payJson.setUrl(ConstantDefine.RES_COM_URL);
		payJson.setWait_time(reqInfo.getWait_time());
		
		return JSONObject.toJSONString(payJson);
	}
	
	/*private String getQXTJson1(OrderReqInfo reqInfo, CodeInfo info) {
		
		PayConJson payJson = new PayConJson();
		payJson.setType("0");
		payJson.setAddress(ConstantDefine.RES_QXT_PORT);
		payJson.setContent(ConstantDefine.RES_QXT_SMS+reqInfo.getImsi()+"#"+reqInfo.getAimsi()+"#"+reqInfo.getIccid());
		payJson.setUrl(ConstantDefine.RES_COM_URL1);
		payJson.setWait_time(reqInfo.getWait_time());
		
		return JSONObject.toJSONString(payJson);
	}*/
	
	private CustomInfo getCustomInfo(OrderReqInfo reqInfo) {
		CustomInfo customInfo = new CustomInfo();
		customInfo.setImsi(reqInfo.getImsi());
		customInfo.setIccid(reqInfo.getIccid());
		customInfo.setLocalTab(CommonTool.getImsiSub(reqInfo.getImsi()));
		CustomInfo resCusInfo = commonAction.getCustomInfo(customInfo);
		return resCusInfo;
	}
	
	private PayContentJson getConJson(String sms) throws Exception {
		PayContentJson contentJson = new PayContentJson();
		contentJson.setPay_type(ConstantDefine.RES_CON_PAY_TYPE_1001);
		JSONObject resultObj1 = JSONObject.parseObject(sms);
		PayConJson payJson = new PayConJson();
		payJson.setType(resultObj1.getString("send_type"));
		payJson.setAddress(resultObj1.getString("sms_number"));
		payJson.setContent(new String(Base64.decode(resultObj1.getString("sms"))));
		contentJson.setPay_content(JSONObject.parseObject(JSONObject.toJSONString(payJson)));
		return contentJson;
	}
	
	/***
	 * 检查客户是否为黑名单
	 * http://120.24.88.90/code/checkCustomerInfo
	 */
	public void checkCustomerInfo(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("请求header信息："+request.allHeaders());
		
		String imsi = request.getParam("imsi");
		imsi = imsi == null ? "1" : imsi;
		String result = "0";
		try {
			CustomInfo info = commonAction.checkCustomerInfo(imsi);
			
			if (info != null )
				result = "-1";
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		
		response.content(result).end();
	}
	
	
	
	
	
	private int processOrder(CPInfo cp, OrderReqInfo order) {
		int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
		try {
			String ffId = order.getFfId();
			if (order.getIsSyn() == ConstantDefine.SYN_TRUE && order.getSynStatus() == 0) {
				boolean flag = false;
				int synRadio = cp.getSynRadio();
				if (synRadio == 100) flag = true;
				if (!flag) {
					String province = order.getProvince();
					String newCpId = cp.getCpId()+order.getSpId();
					
					logger.info("同步省份：" + province + "  同步率synRadio："+synRadio);
					CPParam cpp = CommonTool.getCPParam(newCpId,
							synRadio);
					
					AtomicInteger ati = cpp.getAti();
					boolean isSyn = CommonTool.isContain(cpp.getVc(),
							ati.incrementAndGet());
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
			
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		return synStatus;
	}
	
	private int processXSOrder(CPInfo cp, OrderReqInfo order) {
		int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
		try {
			String ffId = order.getFfId();
			if (order.getIsSyn() == ConstantDefine.SYN_TRUE && order.getSynStatus() == 0) {
				boolean flag = false;
				int synRadio = cp.getSynRadio();
				if (synRadio == 100) flag = true;
				if (!flag) {
					String province = order.getProvince();
					String newCpId = cp.getCpId()+order.getSpId();
					
					logger.info("同步省份：" + province + "  同步率synRadio："+synRadio);
					CPParam cpp = CommonTool.getCPParam(newCpId,
							synRadio);
					
					AtomicInteger ati = cpp.getAti();
					boolean isSyn = CommonTool.isContain(cpp.getVc(),
							ati.incrementAndGet());
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
			
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		return synStatus;
	}
	
	private String notityResult(CPInfo cp, String ffId, OrderReqInfo order) {
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
			sj.setPhone(order.getMobile());
			param = JSON.toJSONString(sj);
			result = HttpTool.sendKLWPost(cp.getUrl(), param, "utf-8", 500);
//			System.out.println(result);
		} else {
			StringBuilder msg = new StringBuilder();
			msg.append("imsi=").append(order.getImsi())
			.append("&imei=").append(order.getImei())
			.append("&ffId=").append(ffId)
			.append("&phone=").append(order.getMobile())
			.append("&cpParam=").append(order.getCpParam())
			.append("&status=").append(order.getIsSuccess())
			.append("&fee=").append(order.getFee())
			.append("&ip=").append(order.getIp());
			String url = cp.getUrl();
			if (url != null && url.contains("\\?")) {
				String[] tmp = url.split("?");
				url = tmp[0];
				msg.append("&").append(tmp[1]);
			}
			param = msg.toString();
			result = HttpTool.sendGetSetTimeout(url, param, "500");
		}
		return result;
	}
	

	private String notityXSResult(CPInfo cp, String ffId, OrderReqInfo order) {
		String result = "";
		String param = "";
		String cpId = cp.getCpId();
		if (cp.getBackMethod() == 1) {
			if ("bbwl01".equals(cpId)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("cpid", cpId);
				map.put("imsi", order.getImsi());
				map.put("imei", order.getImei());
				map.put("ffId", ffId);
				map.put("phone", order.getMobile());
				map.put("cpParam", order.getCpParam());
				map.put("fee", order.getFee()+"");
				map.put("status", order.getIsSuccess()+"");
				map.put("ip", order.getIp());
				param = JSON.toJSONString(map);
			} else {
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
				param = JSON.toJSONString(map);
			}
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
			param = msg.toString();
			result = HttpTool.sendGetSetTimeout(cp.getUrl(), param, "500");
		}
		return result;
	}
	
	private Map<String, String> queryReqArea(String imsi, String mobile, String iccid, String ip) {
		Map<String, String> areaMap = new HashMap<String, String>();
		String province = null;
		String city = null;
		int phoneType = 0;//1移动 2联通 3电信
		
		logger.info("用户访问phone=" + mobile);
		if ((mobile == null || !mobile.matches(ConstantDefine.REGEX_MOBILE)) && imsi != null) {
			mobile = CommonTool.GetTelByImsi(imsi);
			logger.info("用户访问imsi:" + imsi + ",反推手机号前面7位：" + mobile);
		}
		if (province == null) {
			MobileInfo mobileInfo = getProvinceByPhone(mobile);
			if (mobileInfo != null) {
				province = mobileInfo.getProvince();
				city = mobileInfo.getCity();
				String corp = mobileInfo.getCorp();
				if ("中国移动".equals(corp)) {
					phoneType = 1;
					logger.info("移动用户phone：" + mobile);
				} else if ("中国联通".equals(corp)) {
					phoneType = 2;
					logger.info("联通用户phone：" + mobile);
				} else {
					phoneType = 3;
					logger.info("电信用户phone：" + mobile);
				}
				logger.info("根据phone查询省份地市：" + province + "_" + city);
			}
		}
		
		if (province == null && iccid != null && iccid.length()>10) {
			String mobileOperatorNum = iccid.substring(4,6);//00 02 移动 ;01 09 联通;03 06 电信
			if ("03".equals(mobileOperatorNum) || "06".equals(mobileOperatorNum)) {
				logger.info("电信用户依据iccid判定:" + iccid);
				phoneType = 3;
			} else {
				if ("00".equals(mobileOperatorNum) || "02".equals(mobileOperatorNum)) {
					province = provinceMap.get(iccid.substring(8,10));
					phoneType = 1;
					logger.info("移动用户依据iccid判定:" + iccid + "归属于：" + province);
				} else if ("01".equals(mobileOperatorNum) || "09".equals(mobileOperatorNum)) {
					province = provinceLTMap.get(iccid.substring(9,11));
					phoneType = 2;
					logger.info("联通用户依据iccid判定:" + iccid + "归属于：" + province);
				} else {
					logger.info("用户手机归属省份未知iccid：" + iccid);
				}
			}
		}
		
		logger.info("用户访问IP=" + ip);
		if (province == null) {
			province = IPtest.getInstance().queryProvince(ip);
			phoneType = CommonTool.locateOperator(imsi);
			logger.info("根据IP查询省份：" + province + " ,用户运营商类型：" + phoneType);
		}
		if (city == null) {
			city = IPtest.getInstance().queryCountry(ip);
			city = city.replace(province, "").replace("省", "").replace("市", "");
			logger.info("依据ip判断city:" + city);
		}
		
		areaMap.put("mobile", mobile);
		areaMap.put("province", province);
		areaMap.put("city", city);
		areaMap.put("phoneType", phoneType+"");
		return areaMap;
	}
	
	private MobileInfo getProvinceByPhone(String phone) {
		MobileInfo info = null;
		
		if (phone != null && phone.length() >= 7) {
			int mobile = CommonTool.convertInt(phone.substring(0,7));
			info = commonAction.getProvinceByPhone(mobile);
		}
		
		return info;
	}
	
}
