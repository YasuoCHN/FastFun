package com.klw.fastfun.pay.view.app.http;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.juice.orange.game.log.LoggerFactory;
import com.klw.fastfun.pay.common.domain.OrderReqInfo;
import com.klw.fastfun.pay.common.domain.XSPayCodeInfo;
import com.klw.fastfun.pay.common.tool.Base64;
import com.klw.fastfun.pay.common.tool.CommonTool;
import com.klw.fastfun.pay.common.tool.ConstantDefine;
import com.klw.fastfun.pay.common.tool.DateTool;
import com.klw.fastfun.pay.common.tool.HttpTool;
import com.klw.fastfun.pay.common.tool.MD5Tool;
import com.klw.fastfun.pay.common.tool.xdzf.MD5;
import com.klw.fastfun.pay.view.app.wxpay.utils.XmlUtil;

public class CodeSDKReqHelper {
	
	private static Logger logger = LoggerFactory.getLogger(CodeSDKReqHelper.class);
	public static final String SER_CODESDK_REQ = "sercodesdreq_";
	
	private static Map<String, String> provinceMap = new HashMap<String, String>();
	private static Map<String, String> provinceYZWHMap = new HashMap<String, String>();
	private static Map<String, String> provinceXMYSMap = new HashMap<String, String>();
	static {
		provinceMap.put("上海", "1"); provinceMap.put("云南", "2"); provinceMap.put("内蒙", "3");
		provinceMap.put("北京", "4"); provinceMap.put("吉林", "5"); provinceMap.put("四川", "6");
		provinceMap.put("天津", "7"); provinceMap.put("宁夏", "8"); provinceMap.put("安徽", "9");
		provinceMap.put("山东", "10"); provinceMap.put("山西", "11"); provinceMap.put("广东", "12");
		provinceMap.put("广西", "13"); provinceMap.put("新疆", "14"); provinceMap.put("江苏", "15");
		provinceMap.put("江西", "16"); provinceMap.put("河北", "17"); provinceMap.put("河南", "18");
		provinceMap.put("浙江", "19"); provinceMap.put("海南", "20"); provinceMap.put("湖北", "21");
		provinceMap.put("湖南", "22"); provinceMap.put("甘肃", "23"); provinceMap.put("福建", "24");
		provinceMap.put("西藏", "25"); provinceMap.put("贵州", "26"); provinceMap.put("辽宁", "27");
		provinceMap.put("重庆", "28"); provinceMap.put("陕西", "29"); provinceMap.put("青海", "30");
		provinceMap.put("黑龙", "31");
		
		provinceYZWHMap.put("北京", "100"); provinceYZWHMap.put("天津", "220"); provinceYZWHMap.put("河北", "311");
		provinceYZWHMap.put("山西", "351"); provinceYZWHMap.put("内蒙古", "471"); provinceYZWHMap.put("内蒙", "471");
		provinceYZWHMap.put("辽宁", "240"); provinceYZWHMap.put("吉林", "431"); provinceYZWHMap.put("黑龙江", "451");
		provinceYZWHMap.put("黑龙", "451"); provinceYZWHMap.put("上海", "210"); provinceYZWHMap.put("江苏", "250");
		provinceYZWHMap.put("安徽", "551"); provinceYZWHMap.put("福建", "591"); provinceYZWHMap.put("江西", "791");
		provinceYZWHMap.put("山东", "531"); provinceYZWHMap.put("河南", "371"); provinceYZWHMap.put("湖北", "270");
		provinceYZWHMap.put("湖南", "731"); provinceYZWHMap.put("广东", "200"); provinceYZWHMap.put("广西", "771");
		provinceYZWHMap.put("海南", "898"); provinceYZWHMap.put("重庆", "230"); provinceYZWHMap.put("浙江", "571");
		provinceYZWHMap.put("四川", "280"); provinceYZWHMap.put("贵州", "851"); provinceYZWHMap.put("云南", "871");
		provinceYZWHMap.put("西藏", "891"); provinceYZWHMap.put("陕西", "290"); provinceYZWHMap.put("甘肃", "931");
		provinceYZWHMap.put("青海", "971"); provinceYZWHMap.put("宁夏", "951"); provinceYZWHMap.put("新疆", "991");
		
		provinceXMYSMap.put("北京", "0001"); provinceXMYSMap.put("上海", "0002"); provinceXMYSMap.put("天津", "0003"); 
		provinceXMYSMap.put("重庆", "0004"); provinceXMYSMap.put("黑龙", "0005"); provinceXMYSMap.put("吉林", "0006"); 
		provinceXMYSMap.put("辽宁", "0007"); provinceXMYSMap.put("内蒙", "0008"); provinceXMYSMap.put("河北", "0009"); 
		provinceXMYSMap.put("河南", "0010"); provinceXMYSMap.put("广东", "0011"); provinceXMYSMap.put("湖北", "0012"); 
		provinceXMYSMap.put("山东", "0013"); provinceXMYSMap.put("浙江", "0014"); provinceXMYSMap.put("安徽", "0015"); 
		provinceXMYSMap.put("江苏", "0016"); provinceXMYSMap.put("江西", "0017"); provinceXMYSMap.put("云南", "0018"); 
		provinceXMYSMap.put("宁夏", "0019"); provinceXMYSMap.put("青海", "0020"); provinceXMYSMap.put("山西", "0021"); 
		provinceXMYSMap.put("陕西", "0022"); provinceXMYSMap.put("湖南", "0023"); provinceXMYSMap.put("福建", "0024"); 
		provinceXMYSMap.put("甘肃", "0025"); provinceXMYSMap.put("四川", "0026"); provinceXMYSMap.put("广西", "0027"); 
		provinceXMYSMap.put("贵州", "0028"); provinceXMYSMap.put("海南", "0029"); provinceXMYSMap.put("西藏", "0030"); 
		provinceXMYSMap.put("新疆", "0031"); provinceXMYSMap.put("未知", "0000"); 
	}
	
	
	public static OrderReqInfo getPayCode(OrderReqInfo info) {
		switch (info.getUrlNo()) {
		case ConstantDefine.URL_NO_WX:
			return getOnlineSMS2(info);
		case ConstantDefine.URL_NO_DX02_JSON:
			return getOnlineSMS12(info);
		case ConstantDefine.URL_NO_YY05_JSON:
			return getOnlineSMS46(info);
		case ConstantDefine.URL_NO_DM06_JSON:
			return getOnlineSMS67(info);
		case ConstantDefine.URL_NO_DM07_JSON:
			return getOnlineSMS68(info);
		case ConstantDefine.URL_NO_YC19_JSON:
			return getOnlineSMS69(info);
		case ConstantDefine.URL_NO_YC20_JSON:
			return getOnlineSMS70(info);
		case ConstantDefine.URL_NO_DM08_JSON:
			return getOnlineSMS74(info);
		case ConstantDefine.URL_NO_DM09_JSON:
			return getOnlineSMS76(info);
		case ConstantDefine.URL_NO_LT05_JSON:
			return getOnlineSMS78(info);
		case ConstantDefine.URL_NO_YC23_JSON:
			return getOnlineSMS80(info);
		case ConstantDefine.URL_NO_DM12_JSON:
			return getOnlineSMS83(info);
		case ConstantDefine.URL_NO_LT06_JSON:
			return getOnlineSMS88(info);
		case ConstantDefine.URL_NO_DM17_JSON:
			return getOnlineSMS91(info);
		case ConstantDefine.URL_NO_WT01_JSON:
			return getOnlineSMS97(info);
		case ConstantDefine.URL_NO_YC28_JSON:
			return getOnlineSMS98(info);
		case ConstantDefine.URL_NO_DX10_JSON:
			return getOnlineSMS99(info);
		case ConstantDefine.URL_NO_LT08_JSON:
			return getOnlineSMS100(info);
		case ConstantDefine.URL_NO_DM18_JSON:
			return getOnlineSMS101(info);
		case ConstantDefine.URL_NO_DM19_JSON:
			return getOnlineSMS102(info);
		case ConstantDefine.URL_NO_DM20_JSON:
			return getOnlineSMS103(info);
		case ConstantDefine.URL_NO_YC29_JSON:
			return getOnlineSMS104(info);
		case ConstantDefine.URL_NO_YY09_JSON:
			return getOnlineSMS109(info);
		case ConstantDefine.URL_NO_YC31_JSON:
			return getOnlineSMS110(info);
		case ConstantDefine.URL_NO_YC32_JSON:
			return getOnlineSMS111(info);
		case ConstantDefine.URL_NO_YC33_JSON:
			return getOnlineSMS113(info);
		case ConstantDefine.URL_NO_YY10_JSON:
			return getOnlineSMS112(info);
		case ConstantDefine.URL_NO_YC35_JSON:
			return getOnlineSMS125(info);
		case ConstantDefine.URL_NO_YC36_JSON:
			return getOnlineSMS128(info);
		case ConstantDefine.URL_NO_WT30_JSON:
			return getOnlineSMS129(info);
		case ConstantDefine.URL_NO_LT11_JSON:
			return getOnlineSMS143(info);
		case ConstantDefine.URL_NO_YC39_JSON:
			return getOnlineSMS147(info);
		case ConstantDefine.URL_NO_999_JSON:
			return getOnlineSMS999(info);
		default:
			break;
		}
		return info;
	}
	
	
	
	

	/*public static void main(String[] args) {
		String s = "{\"isParallel\":\"Y\",\"requestid\":\"800000003976517\",\"sendNum\":1,\"sendInterval\":2000,\"verifyFeeInfo\":\"%5B%5D\",\"smsFeeInfo\":{\"feeInterval\":2000,\"sendNum\":2,\"smsArray\":[{\"orderId\":1,\"sendNum\":1,\"feeIndex\":1,\"adress\":\"10658422\",\"smsText\":\"42554240547C3838383838383838306B38383840386B316C49434B73624932364D40383835406964414E5740417472756D656540396135336D45333540333632383630383940323232383639383432374032403032383036303836373034343436374035313635304034353836323638353232383934\",\"servtype\":\"ANGSMS\",\"port\":0,\"smsType\":0},{\"orderId\":2,\"sendNum\":1,\"feeIndex\":1,\"adress\":\"1065842232\",\"smsText\":\"424E325B626262787A4676766E7A5B3B30787A5233636C464F635F7A6262464446393F3854626C2A48754C485A677A387A42314872423E784B5162753762344E4F62636C6C47466246623F6C622630626262506C327A727A7D6B5F4C356D4234377E42584C576D56694E31266C353D\",\"servtype\":\"ANGSMS\",\"port\":0,\"smsType\":0}]},\"returnCode\":\"00\",\"inerceptSmsInfos\":\"33353030303030302A38363430302A3130363538383939353523E592AAE59295E6B8B8E6888F2C313030383623E592AAE59295E4BA92E5A8B140E782B9E695B0\",\"returnMsg\":\"放开计费\"}";
		
		JSONObject jsonobj = JSONObject.parseObject(s);
		String returnCode = jsonobj.getString("returnCode");
		if ("00".equals(returnCode)) {
			String smsFeeInfo = jsonobj.getString("smsFeeInfo");
			JSONObject jsonobj1 = JSONObject.parseObject(smsFeeInfo);
			String smsArray = jsonobj1.getString("smsArray");
			
			JSONArray jsonarr = JSONArray.parseArray(smsArray);
			JSONObject jsonobj2 = jsonarr.getJSONObject(0);
			String sms1 = jsonobj2.getString("smsText");
			String smsport1 = jsonobj2.getString("adress");
			String sendtype1 = jsonobj2.getString("smsType");
			JSONObject jsonobj3 = jsonarr.getJSONObject(1);
			String sms2 = jsonobj3.getString("smsText");
			String smsport2 = jsonobj3.getString("adress");
			String sendtype2 = jsonobj3.getString("smsType");
		}
	}*/
	
	private static OrderReqInfo getOnlineSMS147(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String url = info.getUrl();
			String price = info.getPrice();
			String keyword = info.getKeyword();
			String payCode = getPayCode(info.getFees(), keyword, price, "#");
			String matchregex = info.getMatchRegex();
			
			String ffId = info.getFfId();
			ffId = ffId.substring(4);
			
			StringBuilder param = new StringBuilder();
			param.append(matchregex).append(payCode).append(ffId)
			.append("&ip=").append(info.getIp())
			.append("&imsi=").append(info.getImsi())
			.append("&imei=").append(info.getImei())
			.append("&iccid=").append(info.getIccid())
			.append("&price=").append(price);
			
			String mobile = info.getMobile();
			if (mobile != null && mobile.length() == 11) {
				param.append("&mb=").append(mobile);
			}
			
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"4000");
			logger.info("破晓yc39请求结果："+reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String returnCode = jsonobj.getString("result");
				if ("0".equals(returnCode)) {
					String sms1 = jsonobj.getString("smsmsg");
					String smsport1 = jsonobj.getString("smsport");
					String sendtype1 = "0";
					String port = jsonobj.getString("smsdest");
					if (sms1 == null || sms1.length() <= 0) {
						sms1 = jsonobj.getString("smsbin");
						sendtype1 = "1";
						resinfo.setSendport1("0");
					}
					if ("0".equals(sendtype1)) {
						sms1 = new String(Base64.decode(sms1));
						sendtype1 = "3";
					}
					
					resinfo.setOrderid(info.getFfId());
					resinfo.setStatus("0");//成功
					resinfo.setSendtype1(sendtype1);
					resinfo.setSms1(sms1);
					resinfo.setSmsport1(smsport1);
					if (port != null) {
						resinfo.setSendport1(port);
					}
					resinfo.setSendtype2("0");
					resinfo.setMsg("success");
					info.setPaycode(resinfo);
				}
			} else {
				resinfo.setMsg("failed to obtain sms");
			}
		} catch (Exception e) {
			logger.error("破晓yc39获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return info;
	}
	
	//迅鸿联通
	private static OrderReqInfo getOnlineSMS143(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("0");//成功
			resinfo.setOrderid(info.getFfId());
			
			String imsi = info.getImsi();
			String imei = info.getImei();
			String mobile=info.getMobile();//手机号
			String cid=info.getKeyword().split("#")[0];//厂家提供id
			String price=String.valueOf(info.getFee());//计费金额
			String vercode=info.getKeyword().split("#")[1];//验证码
			String ip = info.getIp();//手机ip
			String appName=info.getGamename();//游戏名称
			String Cpfeename=info.getPropname();//道具名称
			String iccid=info.getIccid();//iccid
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("imsi",imsi);
			map.put("imei",imei);
			map.put("mobile",mobile);
			map.put("cid",cid);
			map.put("price",price);
			map.put("vercode",vercode);
			map.put("ip",ip);
			map.put("appName",appName);
			map.put("Cpfeename",Cpfeename);
			map.put("iccid",iccid);
			map.put("cpserverparam",info.getFfId());
			String reqResult = HttpTool.sendPost(info.getUrl(),JSON.toJSONString(map));
			logger.error(reqResult);
			JSONObject resultObj = JSONObject.parseObject(reqResult);
			String status = resultObj.getString("returnCode");
			if("00".equals(status)){
//					result=resultObj.toString();
			}
			
			info.setPaycode(resinfo);
		} catch (Exception e) {
			logger.error("从迅鸿获取一次指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return info;
	}

	private static OrderReqInfo getOnlineSMS129(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("0");//成功
			resinfo.setOrderid(info.getFfId());
			String url = info.getUrl();
			String payCode = info.getKeyword();
			String matchRegex = info.getMatchRegex();
			String[] tmp = matchRegex.split("#");
			
			String smsport1 = tmp[0];
			String sendtype1 = "3";
			String sendtype2 = "0";
			
			resinfo.setSendtype1(sendtype1);
			resinfo.setSms1(payCode);
			resinfo.setSmsport1(smsport1);
			resinfo.setDelaytime(info.getDelayed());
			resinfo.setSendtype2(sendtype2);
			resinfo.setMsg("success");
			
			if (url != null && url.length() > 0) {
				resinfo.setUrldelaytime("120");
				resinfo.setUrl(url+"?orderid="+info.getFfId());
			}
			
			info.setPaycode(resinfo);
			
		} catch (Exception e) {
			logger.error("短信包月wt获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return info;
	}
	

	private static OrderReqInfo getOnlineSMS128(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String payCode = getPayCode(info.getFees(), keyword, price, "#");
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			
			StringBuilder param = new StringBuilder();
			param.append(tmp[0])
			.append("&paycode=").append(payCode)
			.append("&fee=").append(price)
			.append("&iccid=").append(info.getIccid())
			.append("&ipaddr=").append(info.getIp())
			.append("&imsi=").append(info.getImsi())
			.append("&imei=").append(info.getImei())
			.append("&extension=").append(info.getFfId());
			
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"5000");
			logger.info("普石yc36请求结果=>"+reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String returnCode = jsonobj.getString("retCode");
				if ("1".equals(returnCode)) {
					String sms1 = jsonobj.getString("mo");
					String smsport1 = jsonobj.getString("called");
					String sendtype1 = jsonobj.getString("smstype");
					String port = jsonobj.getString("smsdest");
					
					if ("1".equals(sendtype1)) {
						sendtype1 = "0";
					} else if ("2".equals(sendtype1)) {
						sendtype1 = "1";
					}
					
					if ("0".equals(sendtype1)) {
						sendtype1 = "2";
					} else {
						resinfo.setSendport1("0");
					}
					
					resinfo.setOrderid(info.getFfId());
					resinfo.setStatus("0");//成功
//					resinfo.setIsscreen("1");//需要拦截下行
//					resinfo.setIsreply("0");//无需回复拦截的下行
//					resinfo.setKeyport(tmp[0]);
//					resinfo.setKeyword(tmp[1]);
					resinfo.setSendtype1(sendtype1);
					resinfo.setSms1(sms1);
					resinfo.setSmsport1(smsport1);
					if (port != null) {
						resinfo.setSendport1(port);
					}
					resinfo.setSendtype2("0");
					if (tmp.length > 2 && tmp[2].length() > 0) {
						resinfo.setUrldelaytime("120");
						resinfo.setUrl(tmp[2]+"?orderid="+info.getFfId());
					}
					resinfo.setMsg("success");
					info.setPaycode(resinfo);
				}
			} else {
				resinfo.setMsg("failed to obtain sms");
			}
		} catch (Exception e) {
			logger.error("家医yc28获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return info;
	}

	private static OrderReqInfo getOnlineSMS125(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String payCode = getPayCode(info.getFees(), keyword, price, "#");
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			price = info.getPrice().replace("00", "");
			
			String url = tmp[0];
			
			String province = info.getProvince();
			province = province.replace("内蒙古", "内蒙").replace("黑龙江", "黑龙").replace("省", "").replace("市", "");
			String pro = provinceMap.get(province);
			if (pro == null || pro.length() <= 0) pro = "1";
			
			String operator = "CMCC";
			int phoneType = info.getPhoneType();//1移动 2联通 3电信
			if (phoneType == 2) {
				operator = "CUC";
			} else if (phoneType == 3) {
				operator = "CNC";
			}
			
			StringBuilder param = new StringBuilder();
			param.append(tmp[1]).append(payCode).append(info.getFfId())
			.append("&imei=").append(info.getImei())
			.append("&imsi=").append(info.getImsi())
			.append("&iccid=").append(info.getIccid())
			.append("&operator=").append(operator)
			.append("&ip=").append(info.getIp())
			.append("&pro=").append(pro)
			.append("&fee=").append(price)
			.append("&appName=").append(URLEncoder.encode("保卫胡帕", "utf-8"))
			.append("&payCode=").append(URLEncoder.encode("新手礼包", "utf-8"));
			
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"4000");
			logger.info("卓翌yc35请求结果:"+reqResult);
			resinfo.setOrderid(info.getFfId());
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String state = jsonobj.getString("result");
				if (state != null && "0".equals(state)) {
					
					String sms1 = jsonobj.getString("command");
					String port1 = jsonobj.getString("port");
					String type1 = jsonobj.getString("netWorkingType");
					resinfo.setStatus("0");//成功
					
					if ("1".equals(type1)) {
						type1 = "3";
					}
					resinfo.setSendtype1(type1);
					if (!"0".equals(type1)) {
						resinfo.setSms1(sms1);
						resinfo.setSmsport1(port1);
					}
					if (tmp.length > 2 && tmp[2].length() > 0) {
						resinfo.setUrldelaytime("120");
						resinfo.setUrl(tmp[2]+"?orderid="+info.getFfId());
					}
					resinfo.setMsg("success");
					resinfo.setDelaytime(info.getDelayed());
					info.setPaycode(resinfo);
				}
			} else {
				resinfo.setMsg("timeout or error");//请求超时或错误
			}
			
		} catch (Exception e) {
			logger.error("卓翌yc35指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return info;
		
	}
	
	private static OrderReqInfo getOnlineSMS999(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String url = info.getUrl();
			String price = info.getPrice();
			String keyword = info.getKeyword();
			String mobile = info.getMobile();
			
			String resultMsgKey = null;
			String resultCodeKey = null;
			String resultCodeVaule = null;
			String sms1Key = null;
			String smsport1Key = null;
			String sendtype1Key = null;
			String sms2Key = null;
			String smsport2Key = null;
			String sendtype2Key = null;
			String traidKey = null;
			String codeKey = null;
			String defSendtypeValue = null;
			
			
			Map<String, String> paramap = CommonTool.parseYZ(keyword);
			StringBuilder param = new StringBuilder();
			for (Map.Entry<String, String> entry : paramap.entrySet()) {  
				
//				System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
				String key = entry.getKey();
				String value = entry.getValue();
				if ("imsi".equals(key)) {
					param.append("&").append(value).append("=").append(info.getImsi());
				} else if ("imei".equals(key)) {
					param.append("&").append(value).append("=").append(info.getImei());
				} else if ("iccid".equals(key)) {
					param.append("&").append(value).append("=").append(info.getIccid());
				} else if ("mobile".equals(key)) {
					if (mobile != null && mobile.length() > 7) {
						param.append("&").append(value).append("=").append(mobile);
					}
				} else if ("cpparam".equals(key)) {//无额外追加开头的透传参数
					param.append("&").append(value).append("=").append(info.getFfId());
				} else if ("param".equals(key)) {//有额外追加开头的透传参数
					String headparam = paramap.get("headparam");
					param.append("&").append(value).append("=").append(headparam+info.getFfId());
				} else if ("ip".equals(key)) {
					param.append("&").append(value).append("=").append(info.getIp());
				} else if ("price".equals(key)) {//价格分为单位
					param.append("&").append(value).append("=").append(info.getPrice());
				} else if ("timestamp".equals(key)) {//时间戳
					String timestamp = DateTool.getTimestamp(new Date());
					param.append("&").append(value).append("=").append(timestamp);
				} else if ("fomatime".equals(key)) {//最简格式化时间秒
					String fomatime = DateTool.getNow();
					param.append("&").append(value).append("=").append(fomatime);
				} else if ("fee".equals(key)) {//元为单位
					DecimalFormat df2 =new DecimalFormat("0.00");
					BigDecimal b1 = new BigDecimal(price);
					BigDecimal b2 = new BigDecimal(100);
					price = df2.format(b1.divide(b2));
					param.append("&").append(value).append("=").append(price);
				} else if ("paycodes".equals(key)) {//计费点
					String payCode = getPayCode(info.getFees(), value, price, "#");
					param.append("&").append(paramap.get("paycode")).append("=").append(payCode);
				} else if ("resultmsgkey".equals(key)) {//合作方请求结果描述参数
					resultMsgKey = value;
				} else if ("resultcodekey".equals(key)) {//合作方请求结果成功标识参数名称
					resultCodeKey = value;
				} else if ("resultcodevaule".equals(key)) {//合作方请求结果成功标识内容
					resultCodeVaule = value;
				} else if ("sms1key".equals(key)) {//第一条短信内容
					sms1Key = value;
				} else if ("smsport1key".equals(key)) {//第一条短信端口号
					smsport1Key = value;
				} else if ("sendtype1key".equals(key)) {//第一条短信发送类型
					sendtype1Key = value;
				} else if ("sms2key".equals(key)) {//第二条短信内容
					sms2Key = value;
				} else if ("smsport2key".equals(key)) {//第二条短信端口号
					smsport2Key = value;
				} else if ("sendtype2key".equals(key)) {//第二条短信发送类型
					sendtype2Key = value;
				} else if ("traidkey".equals(key)) {//提交验证码需要用到的订单号
					traidKey = value;
				} else if ("codekey".equals(key)) {//自己的发送类型
					codeKey = value;
				} else if ("defsendtypevalue".equals(key)) {//对应合作方的发送类型
					defSendtypeValue = value;
				} 
			
			}  
			
			
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			
			String requrl = tmp[0];
//			String appsecret = tmp[1];
			String comparam = null;
			String commiturl = null;
			int len = tmp.length;
			if (len >= 3) {
				commiturl = tmp[2];
			} else if (len >= 2) {
				comparam = tmp[1];
			}
			
			boolean flag = true;
			String neetphone = paramap.get("phone");
			if (neetphone != null) {
				if (mobile == null || mobile.length() <= 7) {
					flag = false;
				}
			}
			
			if (flag) {
				if (comparam != null && comparam.length() > 0 && !"BL".equals(comparam)) {//存在固定值
					param.append("&").append(comparam);
				} 
				String reqparam = param.toString().substring(1);
				String reqResult = HttpTool.sendGetSetTimeout(requrl, reqparam,"8000");
				logger.info("通用请求结果:"+reqResult);
				if (reqResult != null && reqResult.length() > 0) {
					JSONObject jsonobj = JSONObject.parseObject(reqResult);
					String resultCode = jsonobj.getString(resultCodeKey);
					String resultMsg = jsonobj.getString(resultMsgKey);
					if (resultCode != null && resultCodeVaule.equals(resultCode)) {
						String traid = jsonobj.getString(traidKey);
						String sms1 = jsonobj.getString(sms1Key);
						String smsport1 = jsonobj.getString(smsport1Key);
						String sendtype1 = jsonobj.getString(sendtype1Key);
						String sms2 = jsonobj.getString(sms2Key);
						String smsport2 = jsonobj.getString(smsport2Key);
						String sendtype2 = jsonobj.getString(sendtype2Key);
						
						String[] deftmp = defSendtypeValue.split("#");
						for (int i=0; i<deftmp.length; i++) {
							if (sendtype1 != null && sendtype1.length() > 0 
									&& sendtype1.equals(deftmp[i])) {
								sendtype1 = i + "";
							}
							if (sendtype2 != null && sendtype2.length() > 0 
									&& sendtype2.equals(deftmp[i])) {
								sendtype2 = i + "";
							}
						}
						
						if (sms1 == null || sms1.length() <= 0) {
							sendtype1 = "0";
							sms1 = null;
							smsport1 = null;
						} else {
							if ("4".equals(sendtype1)) {
								sendtype1 = "1";
								sms1 = Base64.encode(sms1.getBytes("utf-8"));
							}
						}
						
						if (sms2 == null || sms2.length() <= 0) {
							sendtype2 = "0";
							sms2 = null;
							smsport2 = null;
						} else {
							if ("4".equals(sendtype2)) {
								sendtype2 = "1";
								sms2 = Base64.encode(sms2.getBytes("utf-8"));
							}
						}
						
						resinfo.setOrderid(info.getFfId());
						resinfo.setStatus("0");//成功
						resinfo.setSendtype1(sendtype1);
						resinfo.setSms1(sms1);
						resinfo.setSmsport1(smsport1);
						
						resinfo.setSendtype2(sendtype2);
						resinfo.setSms2(sms2);
						resinfo.setSmsport2(smsport2);
						if (url != null && url.length() > 0) {
							resinfo.setUrldelaytime("120");
							resinfo.setUrl(url+"?orderid="+info.getFfId());
						}
						resinfo.setDelaytime(info.getDelayed());
						resinfo.setMsg("success");
						
						if (commiturl != null && commiturl.startsWith("http")) {
							StringBuilder commitparam = new StringBuilder();
							commitparam.append("orderid=").append(traid)
							.append("&").append(codeKey).append("=");
							
							commiturl = commiturl + commitparam.toString();
							info.setCtech(commiturl);
						}
						
						info.setApp(info.getApp()+"##"+traid);
						
						info.setPaycode(resinfo);
					} else {
						if (!"0".equals(resultCode)) {
							resinfo.setStatus("1");
							resinfo.setMsg(resultMsg);
						}
					}
				} else {
					resinfo.setMsg("failed to obtain sms");
				}
			}
			
		} catch (Exception e) {
			logger.error("获取通用指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return info;
	}
	
	private static OrderReqInfo getOnlineSMS113(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String url = info.getUrl();
			String price = info.getPrice();
			String keyword = info.getKeyword();
			String payCode = getPayCode(info.getFees(), keyword, price, "#");
			
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			
			String requrl = tmp[0];
//			String appsecret = tmp[1];
			String comparam = null;
			String commiturl = null;
			int len = tmp.length;
			if (len >= 3) {
				commiturl = tmp[2];
			} else if (len >= 2) {
				comparam = tmp[1];
			}
			
			
			StringBuilder param = new StringBuilder();
			param.append("paycode=").append(payCode)
			.append("&imsi=").append(info.getImsi())
			.append("&cpparam=").append(comparam+info.getFfId());
			
			String reqResult = HttpTool.sendGetSetTimeout(requrl, param.toString(),"8000");
			logger.info("烧烤yc33请求结果:"+reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String resultCode = jsonobj.getString("code");
				if (resultCode != null && "1".equals(resultCode)) {
					JSONObject jsonobj1 = JSONObject.parseObject(jsonobj.getString("result"));
					String sms1 = jsonobj1.getString("command");
					String smsport1 = "1065842232";
					String sendtype1 = "1";
					String sendtype2 = "0";
					
					sms1 = Base64.encode(sms1.getBytes("utf-8"));
					
					resinfo.setOrderid(info.getFfId());
					resinfo.setStatus("0");//成功
					resinfo.setSendtype1(sendtype1);
					resinfo.setSms1(sms1);
					resinfo.setSmsport1(smsport1);
					resinfo.setSendtype2(sendtype2);
					if (url != null && url.length() > 0) {
						resinfo.setUrldelaytime("120");
						resinfo.setUrl(url+"?orderid="+info.getFfId());
					}
					resinfo.setMsg("success");
					info.setPaycode(resinfo);
				} else {
					resinfo.setMsg("failed to obtain sms");
				}
			}
			
		} catch (Exception e) {
			logger.error("烧烤yc33指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return info;
	}
	
	private static OrderReqInfo getOnlineSMS112(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String url = info.getUrl();
			String price = info.getPrice();
			String keyword = info.getKeyword();
			String payCode = getPayCode(info.getFees(), keyword, price, "#");
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			
			String requrl = tmp[0];
			String appsecret = tmp[1];
			String commiturl = tmp[2];
			String appid = tmp[3];
			
			String mobile = info.getMobile();
			
			if (mobile != null && mobile.length() > 7) {
				
				String time = DateTool.getTimestamp(new Date());
				String sign = MD5.md5(appid+payCode+appsecret, "utf-8");
				
				StringBuilder param = new StringBuilder();
				param.append("&imsi=").append(info.getImsi())
				.append("&imei=").append(info.getImei())
				.append("&pid=").append(payCode)
				.append("&appid=").append(appid)
				.append("&mobile=").append(mobile)
				.append("&iccid=").append(info.getIccid())
				.append("&ip=").append(info.getIp())
				.append("&corp_type=1&ver=1.0&time=").append(time)
				.append("&sign=").append(sign)
				.append("&extra=").append(info.getFfId());
				
				String reqResult = HttpTool.sendGetSetTimeout(requrl, param.toString(),"5000");
				logger.info("广州友趣yy10请求结果:"+reqResult);
				if (reqResult != null && reqResult.length() > 0) {
					JSONObject jsonobj = JSONObject.parseObject(reqResult);
					String resultCode = jsonobj.getString("code");
					if (resultCode != null && "0".equals(resultCode)) {
						String traid = jsonobj.getString("orderid");
						String sendtype1 = "0";
						String sendtype2 = "0";
						
						resinfo.setOrderid(info.getFfId());
						resinfo.setStatus("0");//成功
						resinfo.setSendtype1(sendtype1);
						resinfo.setSendtype2(sendtype2);
						if (url != null && url.length() > 0) {
							resinfo.setUrldelaytime("120");
							resinfo.setUrl(url+"?orderid="+info.getFfId());
						}
						resinfo.setMsg("success");
						
						StringBuilder commitparam = new StringBuilder();
						commitparam.append("orderid=").append(traid)
						.append("&appid=").append(appid)
						.append("&verify_code=");
						
						commiturl = commiturl + "?" + commitparam.toString();
						info.setCtech(commiturl);
						
						info.setApp(info.getApp()+"##"+traid);
						info.setPaycode(resinfo);
					} else {
						resinfo.setMsg("failed to obtain sms");
					}
				}
				
			}
			
		} catch (Exception e) {
			logger.error("厦门扬顺yc32指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return info;
	}
	
	private static OrderReqInfo getOnlineSMS111(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String url = info.getUrl();
			String price = info.getPrice();
			String keyword = info.getKeyword();
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			
			String requrl = tmp[0];
			String notityurl = tmp[1];
			String smsport = tmp[2];
			
			String province = info.getProvince();
			province = province.substring(0,2);
			
			StringBuilder param = new StringBuilder();
			param.append(keyword)
			.append("&imsi=").append(info.getImsi())
			.append("&imei=").append(info.getImei())
			.append("&price=").append(price)
			.append("&nyurl=").append(Base64.encode(notityurl.getBytes()))
			.append("&provinceCode=").append(provinceXMYSMap.get(province))
			.append("&cpparam=").append(info.getFfId());
			
			String reqResult = HttpTool.sendGetSetTimeout(requrl, param.toString(),"5000");
			logger.info("厦门扬顺yc32请求结果:"+reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String resultCode = jsonobj.getString("result");
				if (resultCode != null && "0".equals(resultCode)) {
					String sms1 = jsonobj.getString("sms");
					String smsport1 = smsport;
					String sendtype1 = "1";
					String sms2 = jsonobj.getString("sms");
					String smsport2 = smsport;
					String sendtype2 = "3";
					
					sms1 = Base64.encode(sms1.getBytes("utf-8"));
					
					resinfo.setOrderid(info.getFfId());
					resinfo.setStatus("0");//成功
					resinfo.setSendtype1(sendtype1);
					resinfo.setSms1(sms1);
					resinfo.setSmsport1(smsport1);
					resinfo.setSendtype2(sendtype2);
					resinfo.setSms2(sms2);
					resinfo.setSmsport2(smsport2);
					if (url != null && url.length() > 0) {
						resinfo.setUrldelaytime("120");
						resinfo.setUrl(url+"?orderid="+info.getFfId());
					}
					resinfo.setDelaytime(info.getDelayed());
					resinfo.setMsg("success");
					info.setPaycode(resinfo);
				} else {
					resinfo.setMsg("failed to obtain sms");
				}
			}
			
		} catch (Exception e) {
			logger.error("厦门扬顺yc32指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return info;
	}

	private static OrderReqInfo getOnlineSMS110(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String url = info.getUrl();
			String price = info.getPrice();
			String keyword = info.getKeyword();
			String payCode = getPayCode(info.getFees(), keyword, price, "#");
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			
			String requrl = tmp[0];
			String channel = tmp[1];
			
			StringBuilder param = new StringBuilder();
			param.append("channelid=").append(channel)
			.append("&consumecode=").append(payCode)
			.append("&imsi=").append(info.getImsi())
			.append("&imei=").append(info.getImei())
			.append("&ip=").append(info.getIp())
			.append("&iccid=").append(info.getIccid())
			.append("&cpparam=").append(info.getFfId());
			String mobile = info.getMobile();
			if (mobile != null && mobile.length() > 7) {
				param.append("&tel=").append(info.getMobile());
			}
			
			String reqResult = HttpTool.sendGetSetTimeout(requrl, param.toString(),"5000");
			logger.info("玉品天成yc31请求结果:"+reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String resultCode = jsonobj.getString("resultCode");
				if (resultCode != null && "0".equals(resultCode)) {
					String sms1 = jsonobj.getString("sms");
					String smsport1 = jsonobj.getString("accessNo");
					String sendtype1 = jsonobj.getString("sendType");
					String sms2 = jsonobj.getString("sms2");
					String smsport2 = jsonobj.getString("accessNo2");
					String sendtype2 = jsonobj.getString("sendType2");
					if ("0".equals(sendtype1)) {
						sendtype1 = "2";
					}
					if ("0".equals(sendtype2)) {
						sendtype2 = "2";
					}
					
					resinfo.setOrderid(info.getFfId());
					resinfo.setStatus("0");//成功
					resinfo.setSendtype1(sendtype1);
					resinfo.setSms1(sms1);
					resinfo.setSmsport1(smsport1);
					resinfo.setSendtype2(sendtype2);
					resinfo.setSms2(sms2);
					resinfo.setSmsport2(smsport2);
					if (url != null && url.length() > 0) {
						resinfo.setUrldelaytime("120");
						resinfo.setUrl(url+"?orderid="+info.getFfId());
					}
					resinfo.setDelaytime(info.getDelayed());
					resinfo.setMsg("success");
					info.setPaycode(resinfo);
				} else {
					resinfo.setMsg("failed to obtain sms");
				}
			}
			
		} catch (Exception e) {
			logger.error("玉品天成yc31指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return info;
	}
	
	private static OrderReqInfo getOnlineSMS109(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String url = info.getUrl();
//			String price = info.getPrice().replace("00", "");
			String keyword = info.getKeyword();
			String matchRegex = info.getMatchRegex();
			String[] tmp = matchRegex.split("##");
			
			String requrl = tmp[0];
//			String xscommiturl = tmp[1];
			String commiturl = tmp[2];
			
			Map<String, String> pzmap = CommonTool.parseYZ(keyword);
			
			String mobile = info.getMobile();
			if (mobile != null && mobile.length() > 7) {
				
				StringBuilder param = new StringBuilder();
				param.append("a=").append(pzmap.get("a"))
				.append("&tel=").append(info.getMobile())
				.append("&cpparam=").append(pzmap.get("cid"))
				.append(info.getFfId());
				
				String reqResult = HttpTool.sendGetSetTimeout(requrl, param.toString(), "8000");
				logger.info("南京贝桥yy09请求结果："+reqResult);
				if (reqResult != null && reqResult.length() > 0) {
					JSONObject jsonobj = JSONObject.parseObject(reqResult);
					String returnCode = jsonobj.getString("code");
					if ("200000".equals(returnCode)) {
						String sid = jsonobj.getString("sid");
						
						String sendtype1 = "0";
						String sendtype2 = "0";
						
						resinfo.setOrderid(info.getFfId());
						resinfo.setStatus("0");//成功
						resinfo.setSendtype1(sendtype1);
						resinfo.setSendtype2(sendtype2);
						if (url != null && url.length() > 0) {
							resinfo.setUrldelaytime("120");
							resinfo.setUrl(url+"?orderid="+info.getFfId());
						}
						resinfo.setMsg("success");
						
						StringBuilder commitparam = new StringBuilder();
						commitparam.append("sid=").append(sid)
						.append("&verifycode=");
						
						commiturl = commiturl + "?" + commitparam.toString();
						info.setCtech(commiturl);
						info.setPaycode(resinfo);
						
					} 
				} else {
					resinfo.setMsg("failed to obtain sms");
				}
				
			}
			
		} catch (Exception e) {
			logger.error("南京贝桥yy09指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return info;
		
	}
	
	private static OrderReqInfo getOnlineSMS104(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String url = info.getUrl();
			String price = info.getPrice();
			price = price.replace("00", "");
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			
			StringBuilder param = new StringBuilder();
			param.append("gameNo=1&operator=CMCC")
			.append("&cpid=").append(tmp[1])
			.append("&fee=").append(price)
			.append("&ip=").append(info.getIp())
			.append("&phone=").append(info.getMobile())
			.append("&imsi=").append(info.getImsi())
			.append("&imei=").append(info.getImei())
			.append("&cppram=").append(info.getFfId());
			
			String reqResult = HttpTool.sendGetSetTimeout(tmp[0], param.toString(),"4000");
			logger.info("竹语yc29请求结果=>"+reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String returnCode = jsonobj.getString("result");
				if ("0".equals(returnCode)) {
					String sms1 = jsonobj.getString("command");
					String smsport1 = jsonobj.getString("port");
					String sendtype1 = jsonobj.getString("type");
					
					if ("1".equals(sendtype1)) {
						sendtype1 = "0";
					} else if ("2".equals(sendtype1)) {
						sendtype1 = "0";
						sms1 = new String(Base64.decode(sms1));
					} else if ("3".equals(sendtype1)) {
						sendtype1 = "1";
					} else if ("4".equals(sendtype1)) {
						sendtype1 = "1";
						sms1 = Base64.encode(sms1.getBytes());
					}
					
					resinfo.setOrderid(info.getFfId());
					resinfo.setStatus("0");//成功
					resinfo.setSendtype1(sendtype1);
					resinfo.setSms1(sms1);
					resinfo.setSmsport1(smsport1);
					resinfo.setSendtype2("0");
					if (url != null && url.length() > 0) {
						resinfo.setUrldelaytime("120");
						resinfo.setUrl(url+"?orderid="+info.getFfId());
					}
					resinfo.setMsg("success");
					info.setPaycode(resinfo);
				}
			} else {
				resinfo.setMsg("failed to obtain sms");
			}
			
		} catch (Exception e) {
			logger.error("竹语yc29获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return info;
	}
	
	private static OrderReqInfo getOnlineSMS103(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String paycode = getPayCode(info.getFees(), keyword, price, "#");
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			
			String mobile = info.getMobile();
			
			if (mobile != null && mobile.length() > 0) {
				StringBuilder param = new StringBuilder();
				param.append("partnerId=").append(tmp[2])
				.append("&appId=").append(tmp[3])
				.append("&appFeeCodeId=").append(paycode)
				.append("&cpOrderId=").append(info.getFfId())
				.append("&mobile=").append(mobile)
				.append("&mobileImsi=").append(info.getImsi())
				.append("&mobileImei=").append(info.getImei())
				.append("&clientIp=").append(info.getIp());
				
				String reqResult = HttpTool.sendGetSetTimeout(tmp[0], param.toString(),"4000");
				logger.info("酷客dm20请求结果:"+reqResult);
				if (reqResult != null && reqResult.length() > 0) {
					Map<String, String> respMap = XmlUtil.xmlParsebytag(reqResult, "root");
					String returnCode = respMap.get("resultCode");
					if ("0".equals(returnCode)) {
						
						String sid = respMap.get("sid");
						
						info.setCtech(sid);
						String sendtype1 = "0";
						
						resinfo.setOrderid(info.getFfId());
						resinfo.setStatus("0");//成功
						resinfo.setSendtype1(sendtype1);
//					resinfo.setSms1(sms1);
//					resinfo.setSmsport1(smsport1);
						resinfo.setSendtype2("0");
						if (url != null && url.length() > 0) {
							resinfo.setUrldelaytime("120");
							resinfo.setUrl(url+"?orderid="+info.getFfId());
						}
						resinfo.setMsg("success");
						info.setPaycode(resinfo);
					}
				} else {
					resinfo.setMsg("failed to obtain sms");
				}
			}
		} catch (Exception e) {
			logger.error("酷客dm20获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return info;
	}
	
	private static OrderReqInfo getOnlineSMS102(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String paycode = getPayCode(info.getFees(), keyword, price, "#");
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			
			String iccid = info.getIccid();
			String imsi = info.getImsi();
			String imei = info.getImei();
			String province = info.getProvince();
			
			if (iccid != null && iccid.length() > 10) {
				
				String nwtype = info.getNwtype();
				if (!"WIFI".equals(nwtype)) {
					nwtype = "CMWAP";
				}
				
				String version = "2.0.0";
				String msgID = "0";
				
//				String regex = "[\\d]{11}";
				if (imsi != null && imsi.length() > 0
						&& imei != null && imei.length() > 0) {
					
					Map<String, String> mapparam = new HashMap<String, String>();
					mapparam.put("consumercode", paycode);
					mapparam.put("chid", tmp[1]);
					mapparam.put("imsi", imsi);
					mapparam.put("imei", imei);
					mapparam.put("provinceId", provinceYZWHMap.get(province));
					mapparam.put("iccid", iccid);
					mapparam.put("cpparam", info.getFfId());
					mapparam.put("network", nwtype);
					mapparam.put("passwd", "");
					mapparam.put("clientIp", info.getIp());
					String data = Base64.encode(JSONObject.toJSONString(mapparam).getBytes());
					
					Map<String, String> paramap = new HashMap<String, String>();
					paramap.put("version", version);
					paramap.put("msgID", msgID);
					paramap.put("data", data);
					String param = JSONObject.toJSONString(paramap);
					
					String reqResult = HttpTool.sendYZWHPost(tmp[0], param, 18000);
					logger.info("易周文化dm19请求msgID"+msgID+"结果:"+reqResult);
					if (reqResult != null && reqResult.length() > 0) {
						JSONObject jsonobj = JSONObject.parseObject(reqResult);
						String returnCode = jsonobj.getString("error_code");
						if ("0".equals(returnCode)) {
							String guid = jsonobj.getString("guid");
							String sms1 = jsonobj.getString("smsreg");
							String smsport1 = jsonobj.getString("smsregport");
							String sendtype1 = jsonobj.getString("smsregtype");
							String sms2 = jsonobj.getString("smsorder");
							String smsport2 = jsonobj.getString("smsorderport");
							String sendtype2 = jsonobj.getString("smsordertype");
							/*if (sms1 != null && sms1.length() > 0) {
								sms1 = CommonTool.hexStringToStr(sms1);
							}
							if (sms2 != null && sms2.length() > 0) {
								sms2 = CommonTool.hexStringToStr(sms2);
							}*/
							if ("0".equals(sendtype1)) {
								sendtype1 = "3";
							} else if ("1".equals(sendtype1)) {
//								sms1 = Base64.encode(sms1.getBytes());
								resinfo.setSendport1("0");
							}
							if ("0".equals(sendtype2)) {
								sendtype2 = "3";
							} else if ("1".equals(sendtype2)) {
//								sms2 = Base64.encode(sms2.getBytes());
								resinfo.setSendport2("0");
							}
							
							resinfo.setOrderid(info.getFfId());
							resinfo.setStatus("0");//成功
							resinfo.setSendtype1(sendtype1);
							resinfo.setSms1(sms1);
							resinfo.setSmsport1(smsport1);
							resinfo.setSendtype2(sendtype2);
							resinfo.setSms2(sms2);
							resinfo.setSmsport2(smsport2);
							if (url != null && url.length() > 0) {
								resinfo.setUrldelaytime("120");
								resinfo.setUrl(url+"?orderid="+info.getFfId());
							}
							resinfo.setMsg("success");
							
							msgID = "1";
							Map<String, String> mapparam1 = new HashMap<String, String>();
							mapparam1.put("guid", guid);
							String data1 = Base64.encode(JSONObject.toJSONString(mapparam1).getBytes());
							
							Map<String, String> paramap1 = new HashMap<String, String>();
							paramap1.put("version", version);
							paramap1.put("msgID", msgID);
							paramap1.put("data", data1);
							String param1 = JSONObject.toJSONString(paramap1);
							String reqResult1 = HttpTool.sendYZWHPost(tmp[0], param1, 300);
							logger.info("易周文化dm19请求msgID"+msgID+"结果:"+reqResult1);
							
							/*msgID = "3";
							Map<String, String> mapparam3 = new HashMap<String, String>();
							mapparam3.put("guid", guid);
							String data3 = Base64.encode(JSONObject.toJSONString(mapparam3).getBytes());
							
							Map<String, String> paramap3 = new HashMap<String, String>();
							paramap3.put("version", version);
							paramap3.put("msgID", msgID);
							paramap3.put("data", data1);
							String param3 = JSONObject.toJSONString(paramap3);
							String reqResult3 = HttpTool.sendYZWHPost(tmp[0], param3, 15000);
							logger.info("易周文化dm19请求msgID"+msgID+"结果:"+reqResult3);*/
							
							info.setApp(info.getApp()+"##"+guid);
							info.setPaycode(resinfo);
						}
					} else {
						resinfo.setMsg("failed to obtain sms");
					}
				}
			}
		} catch (Exception e) {
			logger.error("易周文化dm19获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return info;
	}
	
	private static OrderReqInfo getOnlineSMS101(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			
			String mobile = info.getMobile();
			String iccid = info.getIccid();
			
			if (iccid != null && iccid.length() > 10) {
				
				String nwtype = info.getNwtype();
				if ("WIFI".equals(nwtype)) {
					nwtype = "wifi";
				} else {
					nwtype = "cmwap";
				}
				String app = info.getApp();
				if (app == null) app = "";
				
				String regex = "[\\d]{11}";
				
				Map<String, String> mapparam = CommonTool.parseYZ(keyword);
				if (mobile != null && mobile.matches(regex))
					mapparam.put("mobile", mobile);
				mapparam.put("imsi", info.getImsi());
				mapparam.put("iccid", iccid);
				mapparam.put("imei", info.getImei());
				mapparam.put("price", price);
				mapparam.put("ip", info.getIp());
//				mapparam.put("ip", "211.139.146.117");
				mapparam.put("cpserverparam", info.getFfId());
				mapparam.put("appName", URLEncoder.encode(app, "utf-8"));
				mapparam.put("Cpfeename", "xushiVIP");
				mapparam.put("connetway", nwtype);
				String param = JSONObject.toJSONString(mapparam);
				
				String reqResult = HttpTool.sendPost(tmp[0], param, "5000");
				logger.info("讯鸿dm18请求结果:"+reqResult);
				if (reqResult != null && reqResult.length() > 0) {
					JSONObject jsonobj = JSONObject.parseObject(reqResult);
					String returnCode = jsonobj.getString("returnCode");
					if ("00".equals(returnCode)) {
						String smsFeeInfo = jsonobj.getString("smsFeeInfo");
						JSONObject jsonobj1 = JSONObject.parseObject(smsFeeInfo);
						String smsArray = jsonobj1.getString("smsArray");
						
						JSONArray jsonarr = JSONArray.parseArray(smsArray);
						JSONObject jsonobj2 = jsonarr.getJSONObject(0);
						String sms1 = jsonobj2.getString("smsText");
						String smsport1 = jsonobj2.getString("adress");
						String sendtype1 = jsonobj2.getString("smsType");
						JSONObject jsonobj3 = jsonarr.getJSONObject(1);
						String sms2 = jsonobj3.getString("smsText");
						String smsport2 = jsonobj3.getString("adress");
						String sendtype2 = jsonobj3.getString("smsType");
						if (sms1 != null && sms1.length() > 0) {
							sms1 = CommonTool.hexStringToStr(sms1);
						}
						if (sms2 != null && sms2.length() > 0) {
							sms2 = CommonTool.hexStringToStr(sms2);
						}
						if ("0".equals(sendtype1)) {
							sendtype1 = "3";
						} else if ("1".equals(sendtype1)) {
							sms1 = Base64.encode(sms1.getBytes());
						}
						if ("0".equals(sendtype2)) {
							sendtype2 = "3";
						} else if ("1".equals(sendtype2)) {
							sms2 = Base64.encode(sms2.getBytes());
						}
						
						resinfo.setOrderid(info.getFfId());
						resinfo.setStatus("0");//成功
						resinfo.setSendtype1(sendtype1);
						resinfo.setSms1(sms1);
						resinfo.setSmsport1(smsport1);
						resinfo.setSendtype2(sendtype2);
						resinfo.setSms2(sms2);
						resinfo.setSmsport2(smsport2);
						if (url != null && url.length() > 0) {
							resinfo.setUrldelaytime("120");
							resinfo.setUrl(url+"?orderid="+info.getFfId());
						}
						resinfo.setMsg("success");
						info.setPaycode(resinfo);
					}
				} else {
					resinfo.setMsg("failed to obtain sms");
				}
			}
		} catch (Exception e) {
			logger.error("讯鸿dm18获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return info;
	}

	private static OrderReqInfo getOnlineSMS100(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			
			String mobile = info.getMobile();
			
			if (mobile != null && mobile.length() > 0) {
				StringBuilder param = new StringBuilder();
				param.append(keyword)
				.append("&paymentUser=").append(mobile)
				.append("&price=").append(price)
				.append("&extData=").append(info.getFfId());
				
				String reqResult = HttpTool.sendGetSetTimeout(tmp[0], param.toString(),"4000");
				logger.info("稻香lt08请求结果:"+reqResult);
				if (reqResult != null && reqResult.length() > 0) {
					JSONObject jsonobj = JSONObject.parseObject(reqResult);
					String returnCode = jsonobj.getString("resultCode");
					if ("0".equals(returnCode)) {
						String outTradeNo = jsonobj.getString("outTradeNo");
						info.setCtech(outTradeNo);
						String sendtype1 = "0";
						
						resinfo.setOrderid(info.getFfId());
						resinfo.setStatus("0");//成功
						resinfo.setSendtype1(sendtype1);
//					resinfo.setSms1(sms1);
//					resinfo.setSmsport1(smsport1);
						resinfo.setSendtype2("0");
						if (url != null && url.length() > 0) {
							resinfo.setUrldelaytime("120");
							resinfo.setUrl(url+"?orderid="+info.getFfId());
						}
						resinfo.setMsg("success");
						info.setPaycode(resinfo);
					}
				} else {
					resinfo.setMsg("failed to obtain sms");
				}
			}
		} catch (Exception e) {
			logger.error("稻香lt08获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return info;
	}
	
	private static OrderReqInfo getOnlineSMS99(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("0");//成功
			resinfo.setOrderid(info.getFfId());
			String url = info.getUrl();
			String paycodes = info.getKeyword();
			String price = info.getPrice();
			String payCode = getPayCode(info.getFees(), paycodes, price, "#");
			String matchRegex = info.getMatchRegex();
			String[] tmp = matchRegex.split("#");
			
			String smsport1 = tmp[0];
			String sendtype1 = "3";
			String sendtype2 = "0";
			
			resinfo.setSendtype1(sendtype1);
			resinfo.setSms1(payCode+info.getFfId());
			resinfo.setSmsport1(smsport1);
			resinfo.setDelaytime(info.getDelayed());
			resinfo.setSendtype2(sendtype2);
//			resinfo.setSendtype2(sendtype1);
//			resinfo.setSms2(payCode);
//			resinfo.setSmsport2(smsport1);
			resinfo.setMsg("success");
			
			if (url != null && url.length() > 0) {
				resinfo.setUrldelaytime("120");
				resinfo.setUrl(url+"?orderid="+info.getFfId());
			}
			
			info.setPaycode(resinfo);
		} catch (Exception e) {
			logger.error("短信包月wt获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return info;
	}
	
	private static OrderReqInfo getOnlineSMS98(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String payCode = getPayCode(info.getFees(), keyword, price, "#");
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			String nwtype = info.getNwtype();
			if (!"WIFI".equals(nwtype)) nwtype = "GPRS";
			String proid = "1";
			String protype = "1";
			
			String imsi = info.getImsi();
			String imei = info.getImei();
			if (imsi == null || "0000".equals(imsi) || imei == null || imei.length() <= 0) return info;
			
			StringBuilder param = new StringBuilder();
			param.append(tmp[0])
			.append("&appFeeId=").append(payCode)
			.append("&fee=").append(price)
			.append("&iccid=").append(info.getIccid())
			.append("&os_info=").append(info.getOsversion())
			.append("&os_model=").append(URLEncoder.encode(info.getPmodel(), "utf-8"))
			.append("&net_info=").append(nwtype)
			.append("&client_ip=").append(info.getIp())
			.append("&proid=").append(proid)
			.append("&protype=").append(protype)
			.append("&imsi=").append(info.getImsi())
			.append("&imei=").append(info.getImei())
			.append("&extra=").append(info.getFfId());
			
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"4000");
			logger.info("家医yc28请求结果=>"+reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String returnCode = jsonobj.getString("resultCode");
				if ("0000".equals(returnCode) && "1".equals(jsonobj.getString("type"))) {
					String sms1 = jsonobj.getString("cmd");
					String smsport1 = jsonobj.getString("port");
					String sendtype1 = jsonobj.getString("smstype");
					String port = jsonobj.getString("portnumber");
					if ("0".equals(sendtype1)) {
						sendtype1 = "2";
					} else {
						resinfo.setSendport1("0");
					}
					
					resinfo.setOrderid(info.getFfId());
					resinfo.setStatus("0");//成功
//					resinfo.setIsscreen("1");//需要拦截下行
//					resinfo.setIsreply("0");//无需回复拦截的下行
//					resinfo.setKeyport(tmp[0]);
//					resinfo.setKeyword(tmp[1]);
					resinfo.setSendtype1(sendtype1);
					resinfo.setSms1(sms1);
					resinfo.setSmsport1(smsport1);
					if (port != null) {
						resinfo.setSendport1(port);
					}
					resinfo.setSendtype2("0");
					if (tmp.length > 2 && tmp[2].length() > 0) {
						resinfo.setUrldelaytime("120");
						resinfo.setUrl(tmp[2]+"?orderid="+info.getFfId());
					}
					resinfo.setMsg("success");
					info.setPaycode(resinfo);
				}
			} else {
				resinfo.setMsg("failed to obtain sms");
			}
		} catch (Exception e) {
			logger.error("家医yc28获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return info;
	}
	
	private static OrderReqInfo getOnlineSMS97(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("0");//成功
			resinfo.setOrderid(info.getFfId());
			String url = info.getUrl();
			String paycodes = info.getKeyword();
			String price = info.getPrice();
			String payCode = getPayCode(info.getFees(), paycodes, price, "#");
			String matchRegex = info.getMatchRegex();
			String[] tmp = matchRegex.split("#");
			
//			String mobile = info.getMobile();
//			String regex = "[\\d]{11}";
			
			String smsport1 = tmp[0];
			String sendtype1 = "3";
			String sendtype2 = "0";
			
			resinfo.setSendtype1(sendtype1);
			resinfo.setSms1(payCode);
			resinfo.setSmsport1(smsport1);
			resinfo.setDelaytime(info.getDelayed());
			resinfo.setSendtype2(sendtype2);
//				resinfo.setSendtype2(sendtype1);
//				resinfo.setSms2(payCode);
//				resinfo.setSmsport2(smsport1);
			resinfo.setMsg("success");
			
			if (url != null && url.length() > 0) {
				resinfo.setUrldelaytime("120");
				resinfo.setUrl(url+"?orderid="+info.getFfId());
			}
			
			info.setPaycode(resinfo);
			
		} catch (Exception e) {
			logger.error("短信包月wt获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return info;
	}
	

	private static OrderReqInfo getOnlineSMS91(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String url = info.getUrl();
			String keyword = info.getKeyword();
//			String price = info.getPrice();
//			String payCode = getPayCode(info.getFees(), paycodes, price, "#");
			
			StringBuilder param = new StringBuilder();
			param.append(keyword)
			.append("&imsi=").append(info.getImsi())
			.append("&imei=").append(info.getImei())
			.append("&clientIp=").append(info.getIp())
			.append("&phoneNumber=").append(info.getMobile())
			.append("&extData=").append(info.getFfId());
			
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"6000");
			logger.info("第七dm17请求结果=>"+reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String returnCode = jsonobj.getString("resultCode");
				if ("0".equals(returnCode)) {
					String paymentCode = jsonobj.getString("paymentCode");
					JSONObject jsonobj1 = JSONObject.parseObject(paymentCode);
					
					String sms1 = jsonobj1.getString("smsContent");
					String smsport1 = jsonobj1.getString("spNumber");
					String sendtype1 = jsonobj1.getString("isBase64");
					
					resinfo.setOrderid(info.getFfId());
					resinfo.setStatus("0");//成功
//					resinfo.setIsscreen("1");//需要拦截下行
//					resinfo.setIsreply("0");//无需回复拦截的下行
//					resinfo.setKeyport(tmp[0]);
//					resinfo.setKeyword(tmp[1]);
					resinfo.setSendtype1(sendtype1);
					resinfo.setSms1(sms1);
					resinfo.setSmsport1(smsport1);
					resinfo.setSendtype2("0");
					resinfo.setMsg("success");
					info.setPaycode(resinfo);
					
				}
			} else {
				resinfo.setMsg("failed to obtain sms");
			}
		} catch (Exception e) {
			logger.error("第七dm17获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return info;
	}

	private static OrderReqInfo getOnlineSMS88(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String url = info.getUrl();
			String paycodes = info.getKeyword();
			String price = info.getPrice();
			String payCode = getPayCode(info.getFees(), paycodes, price, "#");
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			
			
			StringBuilder param = new StringBuilder();
			param.append("serviceidup=").append(payCode)
			.append("&imsi=").append(info.getImsi())
			.append("&imei=").append(info.getImei())
			.append("&phone=").append(info.getMobile())
			.append("&ip=").append(info.getIp())
			.append("&param=").append(info.getFfId());
			
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"4000");
			logger.info("范范lt06请求url=>"+url);
			logger.info("范范lt06请求param=>"+param.toString());
			logger.info("范范lt06请求结果=>"+reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String returnCode = jsonobj.getString("code");
				if ("0".equals(returnCode)) {
					String sms1 = jsonobj.getString("sms");
					String smsport1 = jsonobj.getString("port");
					String sendtype1 = "3";
//					String sms2 = jsonobj.getString("sms2");
//					String smsport2 = jsonobj.getString("port2");
					String sendtype2 = "0";
					
					resinfo.setOrderid(info.getFfId());
					resinfo.setStatus("0");//成功
//					resinfo.setIsscreen("1");//需要拦截下行
//					resinfo.setIsreply("1");//需回复拦截的下行
//					resinfo.setReplycontent(sms2);
//					resinfo.setKeyport(tmp[0]);
//					resinfo.setKeyword(tmp[1]);
					resinfo.setSendtype1(sendtype1);
					resinfo.setSms1(sms1);
					resinfo.setSmsport1(smsport1);
					resinfo.setDelaytime(info.getDelayed());
					resinfo.setSendtype2(sendtype2);
					/*resinfo.setSms2(sms2);
					resinfo.setSmsport2(smsport2);*/
					if (tmp.length > 2 && tmp[2].length() > 0) {
						resinfo.setUrldelaytime("120");
						resinfo.setUrl(tmp[2]+"?orderid="+info.getFfId());
					} 
					resinfo.setMsg("success");
					info.setPaycode(resinfo);
				}
			} else {
				resinfo.setMsg("failed to obtain sms");
			}
		} catch (Exception e) {
			logger.error("范范lt06获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return info;
	}

	private static OrderReqInfo getOnlineSMS83(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String payCode = getPayCode(info.getFees(), keyword, price, "#");
			String requrl = info.getMatchRegex();
			
			
			StringBuilder param = new StringBuilder();
			param.append("serviceidup=").append(payCode)
			.append("&imsi=").append(info.getImsi())
			.append("&imei=").append(info.getImei())
			.append("&param=").append(info.getFfId());
			
			String reqResult = HttpTool.sendGetSetTimeout(requrl, param.toString(),"4000");
			logger.info("范范dm15请求结果:"+reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String returnCode = jsonobj.getString("code");
				if ("0".equals(returnCode)) {
					String comiturl = jsonobj.getString("url");
					info.setCtech(comiturl);
					String sms1 = jsonobj.getString("sms");
					String smsport1 = jsonobj.getString("port");
					String sendtype1 = "1";
					
					resinfo.setOrderid(info.getFfId());
					resinfo.setStatus("0");//成功
//					resinfo.setIsscreen("1");//需要拦截下行
//					resinfo.setIsreply("0");//无需回复拦截的下行
//					resinfo.setKeyport(tmp[0]);
//					resinfo.setKeyword(tmp[1]);
					resinfo.setSendtype1(sendtype1);
					resinfo.setSendport1("0");
					resinfo.setSms1(sms1);
					resinfo.setSmsport1(smsport1);
					resinfo.setSendtype2("0");
					if (url != null && url.length() > 0) {
						resinfo.setUrldelaytime("120");
						resinfo.setUrl(url+"?orderid="+info.getFfId());
					}
					resinfo.setMsg("success");
					info.setPaycode(resinfo);
				}
			} else {
				resinfo.setMsg("failed to obtain sms");
			}
		} catch (Exception e) {
			logger.error("范范dm12获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return info;
	}

	private static OrderReqInfo getOnlineSMS80(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			String price = info.getPrice().replace("00", "");
			
			StringBuilder param = new StringBuilder();
			param.append(keyword)
				.append("&imsi=").append(info.getImsi())
				.append("&imei=").append(info.getImei())
				.append("&iccid=").append(info.getIccid())
				.append("&price=").append(price)
				.append("&tel=").append(info.getMobile())
				.append("&extparams=").append(info.getFfId())
				.append("&ip=").append(info.getIp());
			
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"4000");
			logger.info("乐米MMyc23请求url=>"+url);
			logger.info("乐米MMyc23请求param=>"+param.toString());
			logger.info("乐米MMyc23请求结果=>"+reqResult);
			resinfo.setOrderid(info.getFfId());
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String state = jsonobj.getString("state");
				if (state != null && "0".equals(state)) {
					
					String sms1 = jsonobj.getString("cmd1");
					String port1 = jsonobj.getString("up1");
					String type1 = jsonobj.getString("type1");
					resinfo.setStatus("0");//成功
//					resinfo.setIsscreen("1");//需要拦截下行
//					resinfo.setIsreply("0");//无需回复
//					resinfo.setKeyport(tmp[0]);
//					resinfo.setKeyword(tmp[1]);
					
					if ("4".equals(type1)) {
						type1 = "1";
						resinfo.setSendport1("0");
					} else if ("3".equals(type1)) {
						type1 = "3";
						sms1 = new String(Base64.decode(sms1));
					} else if ("2".equals(type1)) {
						type1 = "1";
						resinfo.setSendport1("0");
						sms1 = Base64.encode(sms1.getBytes());
					} else if ("1".equals(type1)) {
						type1 = "3";
					} else {
						type1 = "0";
					}
					resinfo.setSendtype1(type1);
					if (!"0".equals(type1)) {
						resinfo.setSms1(sms1);
						resinfo.setSmsport1(port1);
					}
					if (tmp.length > 2 && tmp[2].length() > 0) {
						resinfo.setUrldelaytime("120");
						resinfo.setUrl(tmp[2]+"?orderid="+info.getFfId());
					}
					resinfo.setMsg("success");
					resinfo.setDelaytime(info.getDelayed());
					info.setPaycode(resinfo);
				}
			} else {
				resinfo.setMsg("timeout or error");//请求超时或错误
			}
		} catch (Exception e) {
			logger.error("乐米MMyc23指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return info;
		
	}
	
	private static OrderReqInfo getOnlineSMS78(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			String appname = info.getApp();
			String propname = info.getPropname();
			if (appname != null && appname.length() > 0) {
				appname = URLDecoder.decode(appname, "utf-8");
			} else {
				appname = URLDecoder.decode("虚实游戏", "utf-8");
			}
			if (propname != null && propname.length() > 0) {
				propname = URLDecoder.decode(propname, "utf-8");
			} else {
				propname = URLDecoder.decode("虚实道具", "utf-8");
			}
			
			StringBuilder param = new StringBuilder();
			param.append(keyword)
				.append("&imsi=").append(info.getImsi())
				.append("&mobile=").append(info.getMobile())
				.append("&ip=").append(info.getIp())
				.append("&price=").append(info.getFee())
				.append("&cpparam=").append(info.getFfId())
				.append("&appname=").append(appname)
				.append("&propname=").append(propname);
			String sms = HttpTool.sendGetSetTimeout(url, param.toString(), "4000");
			if (sms != null && sms.length() > 0) {
				Map<String, String> map = XmlUtil.xmlParsebytag(sms,"response");
				String status = map.get("returncode");
				if ("0".equals(status)) {
					String sms1 = map.get("smstext");
					String smsport1 = map.get("sendport");
					String sendtype1 = "3";
					String replycontent = map.get("vcode");
					
					resinfo.setOrderid(info.getFfId());
					resinfo.setStatus("0");//成功
//					resinfo.setIsscreen("1");//需要拦截下行
//					resinfo.setIsreply("0");//不需要回复拦截的下行
//					resinfo.setDelaytime(info.getDelayed());
////					resinfo.setReplycontent(URLEncoder.encode(replycontent, "utf-8"));
//					resinfo.setKeyport(tmp[0]);
//					resinfo.setKeyword(tmp[1]);
					resinfo.setSendtype1(sendtype1);
					resinfo.setSms1(sms1);
					resinfo.setSmsport1(smsport1);
					resinfo.setSendtype2(sendtype1);
					resinfo.setSms2(replycontent);
					resinfo.setSmsport2(smsport1);
//					resinfo.setSendtype2("0");
					if (tmp.length > 2 && tmp[2].length() > 0) {
						resinfo.setUrldelaytime("120");
						resinfo.setUrl(tmp[2]+"?orderid="+info.getFfId());
					}
					resinfo.setMsg("success");
					info.setPaycode(resinfo);
				}
			} else {
				resinfo.setMsg("failed to obtain sms");
			}
		} catch (Exception e) {
			logger.error("lt05获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return info;
	}

	private static OrderReqInfo getOnlineSMS76(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			String price = info.getPrice().replace("00", "");
			
			String province = info.getProvince();
			province = province.replace("内蒙古", "内蒙").replace("黑龙江", "黑龙").replace("省", "").replace("市", "");
			String pro = provinceMap.get(province);
			if (pro == null || pro.length() <= 0) pro = "1";
			
			String operator = "CMCC";
			int phoneType = info.getPhoneType();//1移动 2联通 3电信
			if (phoneType == 2) {
				operator = "CUC";
			} else if (phoneType == 3) {
				operator = "CNC";
			}
			
			StringBuilder param = new StringBuilder();
			param.append(keyword)
			.append("&imei=").append(info.getImei())
			.append("&imsi=").append(info.getImsi())
			.append("&iccid=").append(info.getIccid())
			.append("&operator=").append(operator)
			.append("&ip=").append(info.getIp())
			.append("&pro=").append(pro)
			.append("&fee=").append(price)
			.append("&appName=").append(URLEncoder.encode("奶爸当家", "utf-8"))
			.append("&payCode=").append(URLEncoder.encode("超值包", "utf-8"))
			.append("&info1=").append(info.getFfId());
			
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"4000");
			logger.info("苏州全真dm09请求url=>"+url);
			logger.info("苏州全真dm09请求param=>"+param.toString());
			logger.info("苏州全真dm09请求结果=>"+reqResult);
			resinfo.setOrderid(info.getFfId());
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String state = jsonobj.getString("result");
				if (state != null && "0".equals(state)) {
					
					String sms1 = jsonobj.getString("command");
					String port1 = jsonobj.getString("port");
					String type1 = jsonobj.getString("netWorkingType");
					resinfo.setStatus("0");//成功
//					resinfo.setIsscreen("1");//需要拦截下行
//					resinfo.setIsreply("0");//无需回复
//					resinfo.setKeyport(tmp[0]);
//					resinfo.setKeyword(tmp[1]);
					
					if ("1".equals(type1)) {
						type1 = "3";
					}
					resinfo.setSendtype1(type1);
					if (!"0".equals(type1)) {
						resinfo.setSms1(sms1);
						resinfo.setSmsport1(port1);
					}
					if (tmp.length > 2 && tmp[2].length() > 0) {
						resinfo.setUrldelaytime("120");
						resinfo.setUrl(tmp[2]+"?orderid="+info.getFfId());
					}
					resinfo.setMsg("success");
					resinfo.setDelaytime(info.getDelayed());
					info.setPaycode(resinfo);
				}
			} else {
				resinfo.setMsg("timeout or error");//请求超时或错误
			}
			
		} catch (Exception e) {
			logger.error("苏州全真dm09指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return info;
		
	}
	
	private static OrderReqInfo getOnlineSMS74(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			String price = info.getPrice().replace("00", "");
			
			StringBuilder param = new StringBuilder();
			param.append(keyword)
				.append("&imsi=").append(info.getImsi())
				.append("&imei=").append(info.getImei())
				.append("&iccid=").append(info.getIccid())
				.append("&price=").append(price)
				.append("&tel=").append(info.getMobile())
				.append("&extparams=").append(info.getFfId())
				.append("&ip=").append(info.getIp());
			
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"4000");
			logger.info("乐米咪咕游戏基地dm08请求url=>"+url);
			logger.info("乐米咪咕游戏基地dm08请求param=>"+param.toString());
			logger.info("乐米咪咕游戏基地dm08请求结果=>"+reqResult);
			resinfo.setOrderid(info.getFfId());
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String state = jsonobj.getString("state");
				if (state != null && "0".equals(state)) {
					
					String sms1 = jsonobj.getString("cmd1");
					String port1 = jsonobj.getString("up1");
					String type1 = jsonobj.getString("type1");
					String sms2 = jsonobj.getString("cmd2");
					String port2 = jsonobj.getString("up2");
					String type2 = jsonobj.getString("type2");
					resinfo.setStatus("0");//成功
//					resinfo.setIsscreen("1");//需要拦截下行
//					resinfo.setIsreply("0");//无需回复
//					resinfo.setKeyport(tmp[0]);
//					resinfo.setKeyword(tmp[1]);
					
					if ("4".equals(type1)) {
						type1 = "1";
						resinfo.setSendport1("0");
					} else if ("3".equals(type1)) {
						type1 = "3";
						sms1 = new String(Base64.decode(sms1));
					} else if ("2".equals(type1)) {
						type1 = "1";
						resinfo.setSendport1("0");
						sms1 = Base64.encode(sms1.getBytes());
					} else if ("1".equals(type1)) {
						type1 = "3";
					} else {
						type1 = "0";
					}
					resinfo.setSendtype1(type1);
					if (!"0".equals(type1)) {
						resinfo.setSms1(sms1);
						resinfo.setSmsport1(port1);
					}
					
					if ("4".equals(type2)) {
						type2 = "1";
						resinfo.setSendport2("0");
					} else if ("3".equals(type2)) {
						type2 = "3";
						sms2 = new String(Base64.decode(sms2));
					} else if ("2".equals(type2)) {
						type2 = "1";
						resinfo.setSendport2("0");
						sms2 = Base64.encode(sms2.getBytes());
					} else if ("1".equals(type2)) {
						type2 = "3";
					} else {
						type2 = "0";
					}
					resinfo.setSendtype2(type2);
					if (!"0".equals(type2)) {
						resinfo.setSms2(sms2);
						resinfo.setSmsport2(port2);
					}
					if (tmp.length > 2 && tmp[2].length() > 0) {
						resinfo.setUrldelaytime("120");
						resinfo.setUrl(tmp[2]+"?orderid="+info.getFfId());
					}
					resinfo.setMsg("success");
					resinfo.setDelaytime(info.getDelayed());
					info.setPaycode(resinfo);
				}
			} else {
				resinfo.setMsg("timeout or error");//请求超时或错误
			}
		} catch (Exception e) {
			logger.error("乐米咪咕游戏基地dm08指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return info;
		
	}
	
	private static OrderReqInfo getOnlineSMS70(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String pmodel = URLEncoder.encode(info.getPmodel(), "utf-8");
			String province = URLEncoder.encode(info.getProvince(), "utf-8");
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			
			StringBuilder url = new StringBuilder();
			url.append(info.getUrl()).append("imsi=").append(info.getImsi())
				.append("&imei=").append(info.getImei())
				.append("&iccid=").append(info.getIccid())
				.append("&fee=").append(info.getFee())
				.append("&pmodel=").append(pmodel)
				.append("&osversion=").append(info.getOsversion())
				.append("&nwtype=").append(info.getNwtype())
				.append("&ffid=").append(info.getFfId())
				.append("&cpparam=").append(info.getFfId())
				.append("&count=").append(info.getCount())
				.append("&province=").append(province);
			String sms = HttpTool.sendHttp(url.toString(), "");
			if (sms != null && sms.length() > 0 && !sms.equals("error")) {
				JSONObject jsonobj = JSONObject.parseObject(sms);
				String sms1 = jsonobj.getString("msg");
				String smsport1 = jsonobj.getString("smsNumber");
				String sendtype = jsonobj.getString("send_type");
				if ("0".equals(sendtype)) sendtype = "3";
				
				resinfo.setOrderid(info.getFfId());
				resinfo.setStatus("0");//成功
//				resinfo.setIsscreen("1");//需要拦截下行
//				resinfo.setIsreply("0");//无需回复
//				resinfo.setKeyport(tmp[0]);
//				resinfo.setKeyword(tmp[1]);
				resinfo.setSendtype1(sendtype);
				if ("1".equals(sendtype))
					resinfo.setSendport1("0");
				resinfo.setSms1(sms1);
				resinfo.setSmsport1(smsport1);
				resinfo.setSendtype2("0");
				if (tmp.length > 2 && tmp[2].length() > 0) {
					resinfo.setUrldelaytime("120");
					resinfo.setUrl(tmp[2]+"?orderid="+info.getFfId());
				}
				resinfo.setMsg("success");
				info.setPaycode(resinfo);
			} else {
				resinfo.setMsg("failed to obtain sms");
			}
		} catch (Exception e) {
			logger.error("从MM获取指令失败，请仔细检查19破解服务器!");
			logger.error(e.getMessage(), e.getCause());
		}
		return info;
	}
	

	private static OrderReqInfo getOnlineSMS69(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			
			String price = info.getPrice();
			String paycodes = info.getKeyword();
			String payCode = getPayCode(info.getFees(), paycodes, price, "#p#");
			
			String smsport = info.getUrl();
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			
			
			resinfo.setOrderid(info.getFfId());
			resinfo.setStatus("0");//成功
//			resinfo.setIsscreen("1");//需要拦截下行
//			resinfo.setIsreply("0");//无需回复
//			resinfo.setKeyport(tmp[0]);
//			resinfo.setKeyword(tmp[1]);
			resinfo.setSendtype1("3");
			resinfo.setSms1(payCode);
			resinfo.setSmsport1(smsport);
			resinfo.setSendtype2("0");
			if (tmp.length > 2 && tmp[2].length() > 0) {
				resinfo.setUrldelaytime("120");
				resinfo.setUrl(tmp[2]+"?orderid="+info.getFfId());
			}
			resinfo.setMsg("success");
			resinfo.setDelaytime(info.getDelayed());
			info.setPaycode(resinfo);
		} catch (Exception e) {
			logger.error("yc19电盈指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return info;
	}
	
	/*public static void main(String[] args) {
		XSPayCodeInfo resinfo = new XSPayCodeInfo();
		resinfo.setStatus("0");
		resinfo.setBackurl("http://120.24.88.90/mmcode/test?orderid=07ycm33845439835&sms=");
		resinfo.setDelaytime("5");
		resinfo.setIsreply("0");
		resinfo.setIsscreen("2");
		resinfo.setKeyport("18");
		resinfo.setKeyword("测试");
		resinfo.setMsg("success");
		resinfo.setOrderid("07ycm33845439835");
//		resinfo.setReplycontent("是");
//		resinfo.setReplysmsport("18602135384");
//		resinfo.setSendport1("0");
//		resinfo.setSendport2("0");
		resinfo.setSendtype1("3");
		resinfo.setSendtype2("3");
		resinfo.setSms1("短信1");
		resinfo.setSms2("短信2");
		resinfo.setSmsport1("18602135384");
		resinfo.setSmsport2("18602135384");
		resinfo.setUrl("http://smspay.xushihudong.com/code/reqXSCodeSDKRes?orderid=07ycm33845439835");
		System.out.println(JSONObject.toJSON(resinfo));
	}*/
	private static OrderReqInfo getOnlineSMS68(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			
			StringBuilder param = new StringBuilder();
			param.append(keyword)
			.append("&price=").append(info.getPrice())
			.append("&imsi=").append(info.getImsi())
			.append("&imei=").append(info.getImei())
			.append("&ip=").append(info.getIp())
			.append("&iccid=").append(info.getIccid())
			.append("&cpparam=").append(info.getFfId());
			String mobile = info.getMobile();
			if (mobile != null && mobile.length() > 0) {
				param.append("&phone=").append(info.getMobile());
			}
			
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"4000");
			logger.info("上海弘龙动漫单应用dm07请求url=>"+url);
			logger.info("上海弘龙动漫单应用dm07请求param=>"+param.toString());
			logger.info("上海弘龙动漫单应用dm07请求结果=>"+reqResult);
			resinfo.setOrderid(info.getFfId());
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String resultCode = jsonobj.getString("resultCode");
				if (resultCode != null && "0".equals(resultCode)) {
					String sms1 = jsonobj.getString("sms1");
					String port1 = jsonobj.getString("port1");
					String type1 = jsonobj.getString("type1");
					resinfo.setStatus("0");//成功
//					resinfo.setIsscreen("1");//需要拦截下行
//					resinfo.setIsreply("0");//无需回复
//					resinfo.setKeyport(tmp[0]);
//					resinfo.setKeyword(tmp[1]);
					if ("0".equals(type1)) {
						resinfo.setSendtype1("2");
					} else if ("1".equals(type1)) {
						resinfo.setSendtype1("1");
						resinfo.setSendport1("0");
					} else {
						resinfo.setSendtype1("0");
					}
					resinfo.setSms1(sms1);
					resinfo.setSmsport1(port1);
					resinfo.setSendtype2("0");
					if (tmp.length > 2 && tmp[2].length() > 0) {
						resinfo.setUrldelaytime("120");
						resinfo.setUrl(tmp[2]+"?orderid="+info.getFfId());
					}
					resinfo.setMsg("success");
					resinfo.setDelaytime(info.getDelayed());
					info.setPaycode(resinfo);
				}
			} else {
				resinfo.setMsg("timeout or error");//请求超时或错误
			}
		} catch (Exception e) {
			logger.error("上海弘龙动漫单应用dm07指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return info;
	}
	

	private static OrderReqInfo getOnlineSMS67(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			
			StringBuilder param = new StringBuilder();
			param.append(keyword)
			.append("&price=").append(info.getPrice())
			.append("&imsi=").append(info.getImsi())
			.append("&imei=").append(info.getImei())
			.append("&user_ip=").append(info.getIp())
			.append("&cp_param=").append(info.getFfId());
			String mobile = info.getMobile();
			if (mobile != null && mobile.length() > 0) {
				param.append("&smsc=").append(info.getMobile());
			}
			
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"4000");
			logger.info("上海池乐视频dm06请求url=>"+url);
			logger.info("上海池乐视频dm06请求param=>"+param.toString());
			logger.info("上海池乐视频dm06请求结果=>"+reqResult);
			resinfo.setOrderid(info.getFfId());
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String sms1 = jsonobj.getString("sms1");
				String sms2 = jsonobj.getString("sms2");
				JSONObject jsonsms1 = JSONObject.parseObject(sms1);
				JSONObject jsonsms2 = JSONObject.parseObject(sms2);
				resinfo.setStatus("0");//成功
//				resinfo.setIsscreen("1");//需要拦截下行
//				resinfo.setIsreply("0");//无需回复
//				resinfo.setKeyport("10086");
//				String creemword = "CTV手机视频#北方网#搜狐影音专区#搜狐教育#华谊央视专区#G客#索尼美剧专区"
//						+ "#乐视手机视频#视听江西#歌华手机电视#新华视讯#包月#退订#中国移动";
//				resinfo.setKeyword(creemword);
				resinfo.setSendtype1("2");
				resinfo.setSms1(jsonsms1.getString("sms"));
				resinfo.setSmsport1(jsonsms1.getString("serviceno"));
				resinfo.setSendtype2("2");
				resinfo.setSms2(jsonsms2.getString("sms"));
				resinfo.setSmsport2(jsonsms2.getString("serviceno"));
				if (tmp.length > 2 && tmp[2].length() > 0) {
					resinfo.setUrldelaytime("120");
					resinfo.setUrl(tmp[2]+"?orderid="+info.getFfId());
				}
				resinfo.setMsg("success");
				resinfo.setDelaytime(info.getDelayed());
				info.setPaycode(resinfo);
			} else {
				resinfo.setMsg("timeout or error");//请求超时或错误
			}
		} catch (Exception e) {
			logger.error("上海池乐视频dm06指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return info;
	}
	
	private static OrderReqInfo getOnlineSMS46(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			
			String url = info.getUrl();
			String price = info.getPrice().replace("00", "");
			String keyword = info.getKeyword();
			String mobile = info.getMobile();
			
			String matchRegex = info.getMatchRegex();
			String[] tmp = matchRegex.split("##");
			
			String requrl = tmp[0];
			
			if (mobile != null && mobile.length() > 0) {
				
				StringBuilder param = new StringBuilder();
				param.append(keyword)
				.append("&imsi=").append(info.getImsi())
				.append("&tel=").append(info.getMobile())
				.append("&price=").append(price)
				.append("&extparams=").append(info.getFfId())
				.append("&imei=").append(info.getImei())
				.append("&iccid=").append(info.getIccid())
				.append("&ip=").append(info.getIp())
				.append("&model=").append(info.getPmodel())
				.append("&lac=").append(info.getBscLac())
				.append("&cid=").append(info.getBscCid());
				
				String reqResult = HttpTool.sendGetSetTimeout(requrl, param.toString(), "15000");
				logger.info("苏州乐麟yy05请求结果=>"+reqResult);
				if (reqResult != null && reqResult.length() > 0) {
					JSONObject jsonobj = JSONObject.parseObject(reqResult);
					String returnCode = jsonobj.getString("state");
					if ("0".equals(returnCode)) {
						String commiturl = jsonobj.getString("url");
						info.setCtech(new String(commiturl.getBytes("ISO8859-1"),"utf-8"));
						resinfo.setStatus("0");//成功
//						resinfo.setIsscreen("1");//需要拦截下行
//						resinfo.setIsreply("0");//无需回复
//						resinfo.setKeyport("10086");
//						String creemword = "CTV手机视频#北方网#搜狐影音专区#搜狐教育#华谊央视专区#G客#索尼美剧专区"
//								+ "#乐视手机视频#视听江西#歌华手机电视#新华视讯#包月#退订#中国移动";
//						resinfo.setKeyword(creemword);
						resinfo.setSendtype1("0");
						resinfo.setSendtype2("0");
						if (url != null && url.length() > 0) {
							resinfo.setUrldelaytime("120");
							resinfo.setUrl(url+"?orderid="+info.getFfId());
						}
						resinfo.setMsg("success");
						resinfo.setDelaytime(info.getDelayed());
						info.setPaycode(resinfo);
					}
				} else {
					resinfo.setMsg("timeout or error");//请求超时或错误
				}
				
			}
			
		} catch (Exception e) {
			logger.error("苏州乐麟yy05指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return info;
		
	}
	
	private static OrderReqInfo getOnlineSMS12(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			
			String fee = info.getFee()+"";
			String ip = info.getIp();
			String ffId = info.getFfId();
			String imsi = info.getImsi();
			String url = info.getUrl();
			String requrl = info.getMatchRegex();
			String app = "泡泡龙3官方版";
			String key = "6113td9j";
			String mac = null;
			String gamename = info.getGamename();	//游戏名称
			String gameName = URLEncoder.encode(app, "utf-8");
			String chargeName = URLEncoder.encode("道具", "utf-8");
			String price = fee.replace("00", "");
			if (gamename != null && gamename.length() > 0)
				gameName = URLEncoder.encode(gamename, "utf-8");
			
			StringBuilder md5Param = new StringBuilder();
			md5Param.append("1195").append(price).append(ip).append(ffId).append(gameName)
			.append(chargeName).append(key);
			mac = MD5Tool.toMD5Value32(md5Param.toString()).toUpperCase();
			
			StringBuilder param = new StringBuilder();
			param.append("channelId=1195")
			.append("&imsi=").append(imsi)
			.append("&fee=").append(price)
			.append("&ip=").append(ip)
			.append("&extra=").append(ffId)
			.append("&gameName=").append(gameName)
			.append("&chargeName=").append(chargeName)
			.append("&mac=").append(mac);
			logger.info(param.toString());
			
			String reqResult = HttpTool.sendPost(requrl, param.toString());
			JSONObject resultObj = JSONObject.parseObject(reqResult);
			
			String resultCode = resultObj.getString("resultCode");
			if ("0000".equals(resultCode)) {
				String sms1 = resultObj.getString("code");
				String smsport1 = resultObj.getString("longCode");
				String sendtype1 = "3";
				
				resinfo.setStatus("0");
				resinfo.setSendtype1(sendtype1);
				resinfo.setSms1(sms1);
				resinfo.setSmsport1(smsport1);
				resinfo.setSendtype2("0");
				if (url != null && url.length() > 0) {
					resinfo.setUrldelaytime("120");
					resinfo.setUrl(url+"?orderid="+info.getFfId());
				}
				resinfo.setMsg("success");
				info.setPaycode(resinfo);
			}
		} catch (Exception e) {
			logger.error("从朗天电信获取电信指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return info;
	}
	
	private static OrderReqInfo getOnlineSMS2(OrderReqInfo info) {
		try  {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String url = info.getUrl();
			String price = info.getPrice();
			String imsi = info.getImsi();
			String imei = info.getImei();
			String iccid = info.getIccid();
			String osversion = info.getOsversion();
			String nwtype = info.getNwtype();
			String ffId = info.getFfId();
			
			String pmodel = info.getPmodel();
			String province = info.getProvince();
			
			if (pmodel != null && pmodel.length() > 0) {
				pmodel = URLEncoder.encode(pmodel, "utf-8");
			}
			if (province != null && province.length() > 0) {
				province = URLEncoder.encode(province, "utf-8");
			}
			StringBuilder param = new StringBuilder();
			param.append(url).append("imsi=").append(imsi)
				.append("&imei=").append(imei)
				.append("&iccid=").append(iccid)
				.append("&fee=").append(price)
				.append("&pmodel=").append(pmodel)
				.append("&osversion=").append(osversion)
				.append("&nwtype=").append(nwtype)
				.append("&ffid=").append(ffId)
				.append("&province=").append(province);
			String reqResult = HttpTool.sendHttp(param.toString(), "");
			logger.info("自己MM请求结果："+reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String returnCode = jsonobj.getString("returnCode");
				if ("0".equals(returnCode)) {
					String sms1 = jsonobj.getString("msg");
					String smsport1 = jsonobj.getString("smsNumber");
					String sendtype1 = jsonobj.getString("send_type");
					String port = jsonobj.getString("port");
					
					if ("0".equals(sendtype1)) {
						sms1 = new String(Base64.decode(sms1));
						sendtype1 = "3";
					}
					
					resinfo.setOrderid(info.getFfId());
					resinfo.setStatus("0");//成功
					resinfo.setSendtype1(sendtype1);
					resinfo.setSms1(sms1);
					resinfo.setSmsport1(smsport1);
					if (port != null) {
						resinfo.setSendport1(port);
					}
					resinfo.setSendtype2("0");
					resinfo.setMsg("success");
					info.setPaycode(resinfo);
				}
			} else {
				resinfo.setMsg("failed to obtain sms");
			}
		} catch (Exception e) {
			logger.error("自己MM获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return info;
	}


	
	
	private static String getPayCode(String fees, String payCodes, String price, String flag) {
		String payCode = null;
		String[] feeList = fees.split("#");
		String[] payCodeList = payCodes.split(flag);
		if (feeList.length != payCodeList.length){
			logger.error("计费点配置错误!");
			return payCode;
		}
		for (int i=0; i<feeList.length; i++) {
			if (feeList[i].equals(price)) {
				payCode = payCodeList[i];
				break;
			}
		}
		return payCode;
	}
}
