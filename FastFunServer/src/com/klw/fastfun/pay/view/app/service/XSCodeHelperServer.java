/**
 * FastFunServer
 */
package com.klw.fastfun.pay.view.app.service;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.juice.orange.game.handler.HttpRequest;
import com.juice.orange.game.handler.HttpResponse;
import com.juice.orange.game.log.LoggerFactory;
import com.klw.fastfun.pay.common.domain.CodeInfo;
import com.klw.fastfun.pay.common.domain.OrderReqInfo;
import com.klw.fastfun.pay.common.tool.CommonTool;
import com.klw.fastfun.pay.common.tool.DateTool;
import com.klw.fastfun.pay.common.tool.HttpTool;
import com.klw.fastfun.pay.common.tool.ip.IPtest;
import com.klw.fastfun.pay.common.transport.CodeT;
import com.klw.fastfun.pay.view.app.ActionAware;
import com.klw.fastfun.pay.view.app.http.CodeReqHelper;

/**
 * @author klwplayer.com
 *
 *         2015年3月30日
 */
public class XSCodeHelperServer extends ActionAware {
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
	
	
	
	private OrderReqInfo reqParamConver(HttpRequest request) throws Exception {
		
		String cpId = request.getParam("cpid");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String aimsi = request.getParam("aimsi");
		String mobile = request.getParam("mobile");
		String param = request.getParam("cpparam");//透传参数
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
		String spId = request.getParam("sp_id");//指定通道
		String province = request.getParam("province");
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
		
		String iccid = request.getParam("iccid");
		if (province == null) {
			if (iccid != null && iccid.length()>10) {
				String mobileOperatorNum = iccid.substring(4,6);//00、02 移动 ;01 联通;03、06、11 电信
				logger.info("用户访问iccid=" + iccid);
				if ("03".equals(mobileOperatorNum) || "06".equals(mobileOperatorNum)
						|| "11".equals(mobileOperatorNum)) {
					logger.info("用户为电信手机依据ip判定省份" + iccid);
				} else {
					if ("00".equals(mobileOperatorNum) || "02".equals(mobileOperatorNum)) {
						province = provinceMap.get(iccid.substring(8,10));
					} else if ("01".equals(mobileOperatorNum)) {
						province = provinceLTMap.get(iccid.substring(9,11));
					} else {
						logger.info("用户手机归属省份未知iccid="+iccid);
					}
					logger.info("用户归属省份=" + province);
				}
			}
		} else {
			province = URLDecoder.decode(province,"utf-8");
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
		reqInfo.setSpId(spId);
		
		return reqInfo;
	}
	
	
	
	
	
	
	/***
	 * 
	 * http://120.76.230.89/code/xsgetcode
	 */
	public void xsgetcode(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("请求header信息："+request.allHeaders());
		OrderReqInfo reqInfo = reqParamConver(request);//获取请求参数
		
		String province = reqInfo.getProvince();
		String spId = reqInfo.getSpId();
		
		spId = spId == null?"oo01":spId;
		String sms = "error";
		String ffId = null;
		try {
			CodeT ct = commonAction.getPayCodes(reqInfo);//获取对应cpId打开的计费通道
			if (ct == null) {
				logger.warn("CPID=" + reqInfo.getCpId() + " 匹配不到合适省份=" + province + " 计费点="
						+ reqInfo.getPrice() + " 的指令！");
				ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), "oo");
				reqInfo.setSpId(spId);
				reqInfo.setIsSuccess(9001);
			} else {
				
				for (CodeInfo info : ct.getCodes()) {
					if (spId.length()>0 && !"oo01".equals(spId) && !spId.equals(info.getSpId())) continue;
					spId = info.getSpId();
					
					ffId = CommonTool.genratexsOrderNO(DateTool.getMonth(), info.getSpFlag());
					
					reqInfo.setFfId(ffId);
					reqInfo.setSpId(spId);
					reqInfo.setUrl(info.getUrl());
					reqInfo.setUrlNo(info.getUrlNo());
					reqInfo.setReqMethod(info.getReqMethod());
					reqInfo.setKeyword(info.getKeyword());
					reqInfo.setMatchRegex(info.getMatchRegex());
					reqInfo.setFees(info.getFee());
					
					sms = CodeReqHelper.getSMSCode(reqInfo);
					if (!"error".equals(sms)) break;
				}
				
				StringBuilder clog = new StringBuilder();
				clog.append("合作方:").append(ct.getCpName()).append("\n");
				clog.append("通道方:").append(ct.getSpName()).append("\n");
				clog.append("省份:").append(province).append("\n");
				clog.append("计费点:").append(reqInfo.getPrice()).append("\n");
				clog.append("订单号:").append(ffId).append("\n");
				clog.append("指令内容:").append(sms);
				logger.info(clog.toString());
			}
			commonAction.addxsOrderReqInfo(reqInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		} finally {
			response.content(sms).end();
		}
		
		
	}
	
	/***
	 * 
	 * http://120.76.230.89/code/xsgetcodetd
	 */
	public void xsgetcodetd(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("请求header信息："+request.allHeaders());
		
		String url = "http://api.173ttl.com/v2/order/jszfbtd";
		String imsi = request.getParam("imsi");
		String sn = request.getParam("sn");
//		String cpid = request.getParam("cpid");
		
		StringBuilder param = new StringBuilder();
		param.append("appid=ZF0054&sn=").append(sn)
		.append("&imsi=").append(imsi);
		
//		String reqResult = HttpTool.sendGet(url, param.toString());
		String reqResult = HttpTool.sendHttp(url+"?"+param.toString(), "");
		logger.info("xsgetcodetd url=>"+url);
		logger.info("xsgetcodetd请求结果=>"+reqResult);
		
//		commonAction.updateOrderTD(sn);
		
		response.content(reqResult).end();
		
	}
	
	/***
	 * 
	 * http://120.76.230.89/code/xsgetcodejy
	 */
	public void xsgetcodejy(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("请求header信息："+request.allHeaders());
		
		String url = "http://api.173ttl.com/v2/order/jszfbjy";
		String imsi = request.getParam("imsi");
		String sn = request.getParam("sn");
		String agreement_no = request.getParam("agreement_no");
		
		StringBuilder param = new StringBuilder();
		param.append("appid=ZF0054&sn=").append(sn)
		.append("&imsi=").append(imsi)
		.append("&agreement_no=").append(agreement_no);
		
//		String reqResult = HttpTool.sendGet(url, param.toString());
		String reqResult = HttpTool.sendHttp(url+"?"+param.toString(), "");
		logger.info("xsgetcodejy url=>"+url);
		logger.info("xsgetcodejy请求结果=>"+reqResult);
		
		response.content(reqResult).end();
		
	}
	
	
	
	
	
}
