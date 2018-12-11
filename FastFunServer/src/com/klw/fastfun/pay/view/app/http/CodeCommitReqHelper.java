package com.klw.fastfun.pay.view.app.http;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.juice.orange.game.log.LoggerFactory;
import com.klw.fastfun.pay.common.domain.OrderReqInfo;
import com.klw.fastfun.pay.common.tool.ConstantDefine;
import com.klw.fastfun.pay.common.tool.DateTool;
import com.klw.fastfun.pay.common.tool.HttpTool;
import com.klw.fastfun.pay.common.tool.MD5Tool;

/**
 * 退款
 * @author ouyangsu
 *
 */
public class CodeCommitReqHelper {
	
	private static Logger logger = LoggerFactory.getLogger(CodeCommitReqHelper.class);
	
	
	public static String commitCodeResult(OrderReqInfo info) {
		switch (info.getUrlNo()) {
		case ConstantDefine.URL_NO_YY05_JSON:
			return getOnlineSMS46(info);
		case ConstantDefine.URL_NO_SD07_JSON:
			return getOnlineSMS48(info);
		case ConstantDefine.URL_NO_SD08_JSON:
			return getOnlineSMS49(info);
		case ConstantDefine.URL_NO_SD17_JSON:
			return getOnlineSMS55(info);
		case ConstantDefine.URL_NO_SD18_JSON:
			return getOnlineSMS57(info);
		case ConstantDefine.URL_NO_SD20_JSON:
			return getOnlineSMS59(info);
		case ConstantDefine.URL_NO_SD21_JSON:
			return getOnlineSMS60(info);
		case ConstantDefine.URL_NO_SD25_JSON:
			return getOnlineSMS63(info);
		case ConstantDefine.URL_NO_DM12_JSON:
			return getOnlineSMS82(info);
		case ConstantDefine.URL_NO_DM15_JSON:
			return getOnlineSMS83(info);
		case ConstantDefine.URL_NO_LT07_JSON:
			return getOnlineSMS96(info);
		case ConstantDefine.URL_NO_LT08_JSON:
			return getOnlineSMS100(info);
		case ConstantDefine.URL_NO_LT09_JSON:
			return getOnlineSMS105(info);
		case ConstantDefine.URL_NO_YY09_JSON:
			return getOnlineSMS109(info);
		case ConstantDefine.URL_NO_YY10_JSON:
			return getOnlineSMS112(info);
		case ConstantDefine.URL_NO_SD62_JSON:
			return getOnlineSMS121(info);
		case ConstantDefine.URL_NO_SD66_JSON:
			return getOnlineSMS124(info);
		case ConstantDefine.URL_NO_SDF8_JSON:
			return getOnlineSMS174(info);
		case 175:
			return getOnlineSMS175(info);
		case 178:
			return getOnlineSMS178(info);
		case 179:
			return getOnlineSMS179(info);
		case 182:
			return getOnlineSMS182(info);
		case 187:
			return getOnlineSMS187(info);
		case 188:
			return getOnlineSMS188(info);
		case 190:
			return getOnlineSMS190(info);
		case 191:
			return getOnlineSMS191(info);
		case 192:
			return getOnlineSMS192(info);
		case 195:
			return getOnlineSMS195(info);
		case 196:
			return getOnlineSMS196(info);
		case 197:
			return getOnlineSMS197(info);
		case 198:
			return getOnlineSMS198(info);
		case 199:
			return getOnlineSMS199(info);
		case 200:
			return getOnlineSMS200(info);
		case 201:
			return getOnlineSMS201(info);
			
		case 202:
			return getOnlineSMS202(info);
			
		case 203:
			return getOnlineSMS203(info);
		case 204:
			return getOnlineSMS204(info);
			
		case 205:
			return getOnlineSMS205(info);
		case 206:
			return getOnlineSMS206(info);
		case 207:
			return getOnlineSMS207(info);
		case 208:
			return getOnlineSMS208(info);
		default:
			break;
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		/*String smscontent = "本次短信验证码：915345（如非本人操作，请忽略本短信）【中国移动　咪咕动漫】";
		String[] arr = smscontent.split("（如非本人");
		String[] arr1 = arr[0].split("验证码：");
		System.out.println(arr1[1]);*/
		String url ="http://139.129.99.233:180/up_code.html";
		String param = "username=test&password=test&sid=yzm5&phone=18796419551&code=980678&cpparam=01sdj91393933363";
		String result = HttpTool.simpleSendHttp(url+"?"+param, "gb2312");
		System.out.println(result);
	}
	
	
	/**
	 *  京东接口对接
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS208(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getMatchRegex();
			String mobile =info.getMobile();
			String keyword = info.getKeyword();
//			String orderid2 = info.getPmodel();
			
			StringBuilder urlparam = new StringBuilder();
			urlparam.append(keyword).append("&tel=").append(mobile).append("&code=").append(info.getCode());
			
			String reqResult = HttpTool.sendGetSetTimeout(url, urlparam.toString(), "5000");
			JSONObject json = JSONObject.parseObject(reqResult);
			String state = json.getString("msg");
			logger.info("BKZFW:"+info.getSpId()+"请求结果："+reqResult);
			
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");
				
				result = JSONObject.toJSONString(resultMap);
			} else if ("1".equals(state)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");
				
				result = JSONObject.toJSONString(resultMap);
			} else 
			{
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.info("ELM:"+info.getSpId()+"指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}
	

	
	
	/**
	 * webqq绑定
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS207(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getMatchRegex();
			String mobile =info.getMobile();
			
			String code = info.getCode();
			
			JSONObject jsonobj = new JSONObject();
			JSONObject reqParamJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			jsonobj.put("code", code);
			jsonobj.put("phone", mobile);
			jsonArray.add(jsonobj);
			reqParamJson.put("data", jsonArray);
			
			String reqParamStr = reqParamJson.toJSONString();
			String reqResult = HttpTool.sendWEBQQBDPost(url, reqParamStr, "UTF-8", 4000);
			logger.info("BKZFW:"+info.getSpId()+"请求结果："+reqResult);
			
			String state= null;
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");
				
				result = JSONObject.toJSONString(resultMap);
			}else{
				JSONObject json = JSONObject.parseObject(reqResult);
				state = json.getString("result");
			}
			 if ("succ".equals(state)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");
				
				result = JSONObject.toJSONString(resultMap);
			} else {
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.info("ELM:"+info.getSpId()+"指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}
	/**
	 *  河北移动端接口对接
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS206(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getMatchRegex();
//			String mobile =info.getMobile();
			String keyword = info.getKeyword();
//			String  cpid = info.getCpId();
			String orderid2 = info.getPmodel();
			
			StringBuilder urlparam = new StringBuilder();
			urlparam.append(keyword).append("&orderId=").append(orderid2).append("&msgCode=").append(info.getCode());
			
			String reqResult = HttpTool.sendGetSetTimeout(url, urlparam.toString(), "5000");
			JSONObject json = JSONObject.parseObject(reqResult);
			String state = json.getString("code");
			logger.info("BKZFW:"+info.getSpId()+"请求结果："+reqResult);
			
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");
				
				result = JSONObject.toJSONString(resultMap);
			} else if ("0".equals(state)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");
				
				result = JSONObject.toJSONString(resultMap);
			} else 
			{
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.info("ELM:"+info.getSpId()+"指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}
	/**
	 * 飞信
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS205(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getMatchRegex();
//			String mobile =info.getMobile();
			String keyword = info.getKeyword();
//			String  cpid = info.getCpId();
		
			String orderid2 = info.getPmodel();
			
			StringBuilder urlparam = new StringBuilder();
			urlparam.append(keyword).append("&orderId=").append(orderid2).append("&msgCode=").append(info.getCode());
			
			String reqResult = HttpTool.sendGetSetTimeout(url, urlparam.toString(), "5000");
			JSONObject json = JSONObject.parseObject(reqResult);
			String state = json.getString("code");
			logger.info("BKZFW:"+info.getSpId()+"请求结果："+reqResult);
			
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");
				
				result = JSONObject.toJSONString(resultMap);
			} else if ("0".equals(state)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");
				
				result = JSONObject.toJSONString(resultMap);
			} else 
			{
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.info("ELM:"+info.getSpId()+"指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}
	
	/**
	 * 淘宝胜达
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS204(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getMatchRegex();
			String mobile =info.getMobile();
			String keyword = info.getKeyword();
			String  cpid = info.getCpId();
			
			StringBuilder urlparam = new StringBuilder();
			urlparam.append(keyword).append("&mobile=").append(mobile).append("&content=").append(info.getCode()).append("&tcstr=").append(info.getFfId());
			
			String reqResult = HttpTool.sendGetSetTimeout(url, urlparam.toString(), "5000");
			JSONObject json = JSONObject.parseObject(reqResult);
			String state = json.getString("Code");
			logger.info("BKZFW:"+info.getSpId()+"请求结果："+reqResult);
			
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");
				
				result = JSONObject.toJSONString(resultMap);
			} else if ("1".equals(state)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");
				
				result = JSONObject.toJSONString(resultMap);
			} else 
			{
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.info("ELM:"+info.getSpId()+"指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}
	
	/**
	 * 西瓜淘宝
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS203(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url1 = info.getMatchRegex();
			String[] url2 = url1.split("#");
			String url;
 
//			if (exprotType.equals("西瓜淘宝2")) {
//				url = url2[1];
//			}else {
				url = url2[0];
//			}
			System.out.println("西瓜淘宝的请求网址是： "+url);
			
			String mobile =info.getMobile();
			String codes = info.getCode();
			String  cpid = info.getCpId();
			String keyword = info.getKeyword();
			
			StringBuilder urlparam = new StringBuilder();
			urlparam.append(keyword).append("&phone=").append(mobile).append("&code=").append(codes);
			String reqResult = HttpTool.sendGetSetTimeout(url, urlparam.toString(), "5000");
			JSONObject json = JSONObject.parseObject(reqResult);
			String state = json.getString("status");
			logger.info("西瓜淘宝:"+info.getSpId()+"请求结果："+reqResult);
			
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");
				
				result = JSONObject.toJSONString(resultMap);
			} else if ("0".equals(state)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");
				
				result = JSONObject.toJSONString(resultMap);
			} else 
			{
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.info("ELM:"+info.getSpId()+"指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}
	
	
	/**
	 * 探探复制版
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS202(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getMatchRegex();
			String mobile =info.getMobile();
			String codes = info.getCode();
			String  cpid = info.getCpId();
			String keyword = info.getKeyword();
			
			StringBuilder urlparam = new StringBuilder();
			urlparam.append(keyword).append("&tel=").append(mobile).append("&code=").append(codes).append("&cpid=").append(cpid);
			String reqResult = HttpTool.sendGetSetTimeout(url, urlparam.toString(), "5000");
			JSONObject json = JSONObject.parseObject(reqResult);
			String state = json.getString("msg");
			logger.info("探探:"+info.getSpId()+"请求结果："+reqResult);
			
		    if (reqResult != null &&  reqResult.length() > 0) {
		    	result = "ok";
			}else {
				result = "error";
			}
		} catch (Exception e) {
			logger.info("ELM:"+info.getSpId()+"指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}
	
	
	
	/**
	 * 探探
	 * @param info
	 * @return
	 */
	/*
	private static String getOnlineSMS202(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getMatchRegex();
			String mobile =info.getMobile();
			String codes = info.getCode();
			String  cpid = info.getCpId();
			String keyword = info.getKeyword();
			
			StringBuilder urlparam = new StringBuilder();
			urlparam.append(keyword).append("&tel=").append(mobile).append("&code=").append(codes).append("&cpid=").append(cpid);
			String reqResult = HttpTool.sendGetSetTimeout(url, urlparam.toString(), "5000");
			JSONObject json = JSONObject.parseObject(reqResult);
			String state = json.getString("msg");
			logger.info("探探:"+info.getSpId()+"请求结果："+reqResult);
			
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");
				
				result = JSONObject.toJSONString(resultMap);
			} else if ("1".equals(state)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");
				
				result = JSONObject.toJSONString(resultMap);
			} else 
			{
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.info("ELM:"+info.getSpId()+"指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}
	*/
	
	
	/**
	 * TB
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS201(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getMatchRegex();
			String mobile =info.getMobile();
			String codes = info.getCode();
			String  cpid = info.getCpId();
			String keyword = info.getKeyword();
			
			StringBuilder urlparam = new StringBuilder();
			urlparam.append(keyword).append("&phone=").append(mobile).append("&code=").append(codes).append("&cpid=").append(cpid);
			String reqResult = HttpTool.sendGetSetTimeout(url, urlparam.toString(), "5000").trim();
			logger.info("WEBQQ:"+info.getSpId()+"请求结果："+reqResult);
			
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");
				
				result = JSONObject.toJSONString(resultMap);
			} else if ("ok".equals(reqResult)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");
				
				result = JSONObject.toJSONString(resultMap);
			}else{
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.error(info.getSpId()+"指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
	
	
	
	//  微博解封
	private static String getOnlineSMS200(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getUrl();
			String mobile =info.getMobile();
			String keyword = info.getKeyword();
			String  cpid = info.getCpId();
			
			StringBuilder urlparam = new StringBuilder();
			urlparam.append(keyword).append("&telephone=").append(mobile);
			
			String reqResult = HttpTool.sendGetSetTimeout(url, urlparam.toString(), "5000");
			JSONObject json = JSONObject.parseObject(reqResult);
			String state = json.getString("code");
			logger.info("BKZFW:"+info.getSpId()+"请求结果："+reqResult);
			
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");
				
				result = JSONObject.toJSONString(resultMap);
			} else if ("00".equals(state)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");
				
				result = JSONObject.toJSONString(resultMap);
			} else 
			{
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.info("ELM:"+info.getSpId()+"指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}
	
	/**
	 * 
	 * bkzfw,贝壳找房网
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS199(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getUrl();
			String mobile =info.getMobile();
			String keyword = info.getKeyword();
			
			
			StringBuilder urlparam = new StringBuilder();
			urlparam.append(keyword).append("&mobile=").append(mobile).append("&imsi=").append(info.getImsi()).append("&Sms=").append(info.getCode());
			
			
			
			String reqResult = HttpTool.sendGetSetTimeout(url, urlparam.toString(), "5000");
			JSONObject json = JSONObject.parseObject(reqResult);
			String state = json.getString("state");
			logger.info("BKZFW:"+info.getSpId()+"请求结果："+reqResult);
			
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");
				
				result = JSONObject.toJSONString(resultMap);
			} else if ("ok".equals(state)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");
				
				result = JSONObject.toJSONString(resultMap);
			}else{
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.info("ELM:"+info.getSpId()+"指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}
	
	
	
	/**
	 * webqq
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS198(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getMatchRegex();
			String mobile =info.getMobile();
			String codes = info.getCode();
			
			String keyword = info.getKeyword();
			
			StringBuilder urlparam = new StringBuilder();
			urlparam.append(keyword).append("&mobile=").append(mobile).append("&vcode=").append(codes);
			String reqResult = HttpTool.sendGetSetTimeout(url, urlparam.toString(), "5000");
			logger.info("WEBQQ:"+info.getSpId()+"请求结果："+reqResult);
			
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");
				
				result = JSONObject.toJSONString(resultMap);
			} else if ("OK".equals(reqResult)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");
				
				result = JSONObject.toJSONString(resultMap);
			}else{
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.error(info.getSpId()+"指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	/**
	 * 瞿JD2验证码
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS197(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getMatchRegex();
			String mobile =info.getMobile();
			String codes = info.getCode();
			
			String keyword = info.getKeyword();
			
			StringBuilder urlparam = new StringBuilder();
			
			Map<String, Object> sendMap = new HashMap<String, Object>();
			sendMap.put("mobile", mobile);
			sendMap.put("msgId", mobile);
			sendMap.put("state", "0");
			sendMap.put("pid", keyword);
			sendMap.put("content", codes);
			sendMap.put("replyTime", null);
			
			String replys = JSONObject.toJSONString(sendMap,SerializerFeature.WriteMapNullValue);
			
			urlparam.append(url).append(replys);
			String reqResult = HttpTool.sendPost(urlparam.toString(), 5000);
			logger.info(info.getSpId()+"请求结果:"+reqResult);
			JSONObject jsob = JSONObject.parseObject(reqResult);
			String status = jsob.getString("Success");
			if (reqResult != null && reqResult.length() > 0) {
				
				if("true".equals(status)){
					result = "ok";
				}else{
					result = "error";
				}
				
			} else {
				result = "request time out";
			}
			
		} catch (Exception e) {
			logger.error(info.getSpId()+"指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}
	/**
	 * MG验证码沈总
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS196(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getMatchRegex();
			String mobile =info.getMobile();
			String codes = info.getCode();
			
			String keyword = info.getKeyword();
			
			StringBuilder param = new StringBuilder();
			if(keyword != null && keyword.length() > 0){
				param.append(keyword);
			}
			param.append("&phone=").append(mobile).append("&vcode=").append(codes);
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"5000");
			logger.info(info.getSpId()+"请求结果:"+reqResult);
			
			JSONObject resJson = JSONObject.parseObject(reqResult);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			String status = resJson.getString("success"); 
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");
				
				result = JSONObject.toJSONString(resultMap);
			} else if ("true".equals(status)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");
				result = JSONObject.toJSONString(resultMap);
			}else{
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.error(info.getSpId()+"指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	/**
	 * ELM验证码
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS195(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getMatchRegex();
			String mobile =info.getMobile();
			String codes = info.getCode();
			
			String keyword = info.getKeyword();
			
			StringBuilder urlparam = new StringBuilder();
			
			Map<String, Object> sendMap = new HashMap<String, Object>();
			sendMap.put("mobile", mobile);
			sendMap.put("msgId", mobile);
			sendMap.put("state", "0");
			sendMap.put("pid", keyword);
			sendMap.put("content", codes);
			sendMap.put("replyTime", null);
			
			String replys = JSONObject.toJSONString(sendMap,SerializerFeature.WriteMapNullValue);
			
			urlparam.append(url).append(replys);
			String reqResult = HttpTool.sendPost(urlparam.toString(), 5000);
			logger.info(info.getSpId()+"请求结果:"+reqResult);
			
			JSONObject resJson = JSONObject.parseObject(reqResult);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			String status = resJson.getString("Success"); 
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");
				
				result = JSONObject.toJSONString(resultMap);
			} else if ("true".equals(status)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");
				result = JSONObject.toJSONString(resultMap);
			}else{
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.error(info.getSpId()+"指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	/**
	 * jd瞿验证码
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS192(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getUrl();
			String mobile =info.getMobile();
			String codes = info.getCode();
			
			String keyword = info.getKeyword();
			
			StringBuilder param = new StringBuilder();
			if(keyword != null && keyword.length() > 0){
				param.append(keyword);
			}
			param.append("mobile=").append(mobile).append("&msg=").append(codes);
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"5000");
			logger.info(info.getSpId()+"请求结果:"+reqResult);
			
			Map<String, String> resMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {
				
				if ("1".equals(reqResult)) {
					result="ok";
				}else{
					result="error";
				}
				
			}else{
				result="request time out";
				
			}
		} catch (Exception e) {
			logger.error(info.getSpId()+"指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	/**
	 * jd瞿验证码
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS191(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getMatchRegex();
			String mobile =info.getMobile();
			String codes = info.getCode();
			
			String keyword = info.getKeyword();
			
			StringBuilder param = new StringBuilder();
			if(keyword != null && keyword.length() > 0){
				param.append(keyword);
			}
			param.append("phone=").append(mobile).append("&verify=").append(codes);
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"5000");
			logger.info(info.getSpId()+"请求结果:"+reqResult);
			
			Map<String, String> resMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String code = jsonobj.getString("code");
				if ("200".equals(code)) {
					resMap.put("orderid", info.getFfId());
					resMap.put("msg", "success");
					resMap.put("status", "0");
				}else{
					resMap.put("orderid", info.getFfId());
					resMap.put("msg", "error");
					resMap.put("status", "1");
				}
				
			}else{
				resMap.put("orderid", info.getFfId());
				resMap.put("msg", "request time out");
				resMap.put("status", "2");
			}
		
			result = JSONObject.toJSONString(resMap);
		} catch (Exception e) {
			logger.error(info.getSpId()+"指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	/**
	 * jd瞿验证码
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS190(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getKeyword();
			String mobile =info.getMobile();
			String codes = info.getCode();
			String time = DateTool.getTime();
			String keyword = info.getFees();
			JSONObject json = new JSONObject();
	
			json.put("content", codes);
			json.put("mobile", mobile);
			json.put("msgId", info.getFfId());
			json.put("state", 0);
			json.put("replyTime", URLEncoder.encode(time,"utf-8"));
			json.put("pid", keyword);
			String param = json.toJSONString();
			
			StringBuilder strb = new StringBuilder();
			strb.append(url).append(param);
			
			String reqResult = HttpTool.sendPost(strb.toString(), 5000);
			
			logger.info(info.getSpId()+"请求结果:"+reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				
				if("OK".equals(reqResult)){
					result = "ok";
				}else{
					result = "error";
				}
				
			} else {
				result = "request time out";
			}
			
		} catch (Exception e) {
			logger.error(info.getSpId()+"指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
	/**
	 * qq绑定安全中心验证码
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS188(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getMatchRegex();
			String mobile =info.getMobile();
			String codes = info.getCode();
			
			JSONObject json = new JSONObject();
			JSONObject jsons = new JSONObject();
			json.put("code", codes);
			json.put("phone", mobile);
			
			JSONArray jsonArray = new JSONArray();
			jsonArray.add(json);
			
			jsons.put("data", jsonArray);
			String param = jsons.toJSONString();
			
			String reqResult = HttpTool.sendQQPost(url, param.toString(),5000);
			logger.info(info.getSpId()+"请求结果:"+reqResult);
			
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String code = jsonobj.getString("result");
				if("succ".equals(code)){
					result = "ok";
				}else{
					result = "error";
				}
				
			} else {
				result = "request time out";
			}
			
		} catch (Exception e) {
			logger.error(info.getSpId()+"指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	/**
	 * 和通行证验证码
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS187(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getUrl();
			String keyword = info.getKeyword();
			
			String[] str=keyword.split("#");
			
			StringBuilder param = new StringBuilder();
			
			param.append("service=").append(str[2])
			.append("&phone=").append(info.getMobile())
			.append("&msgCode=").append(info.getCode()).append("&orderId=").append(info.getPmodel());
			
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"3000");
			logger.info(info.getSpId()+"请求结果:"+reqResult);
			
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String code = jsonobj.getString("code");
				if(code.equals("0")){
					result = "ok";
				}else{
					result = "error";
				}
				
			} else {
				result = "request time out";
			}
			
		} catch (Exception e) {
			logger.error(info.getSpId()+"指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	/**
	 * MG验证码
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS182(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getUrl();
			String keyword = info.getKeyword();
			
			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}
			param.append("&phone=").append(info.getMobile())
			.append("&msgCode=").append(info.getCode()).append("&orderId=").append(info.getPmodel());
			
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"3000");
			logger.info(info.getSpId()+"请求结果:"+reqResult);
			
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String code = jsonobj.getString("code");
				if(code.equals("0")){
					result = "ok";
				}else{
					result = "error";
				}
				
			} else {
				result = "error";
			}
			
		} catch (Exception e) {
			logger.error(info.getSpId()+"指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	/**
	 * ks验证码
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS179(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getMatchRegex();
			String keyword = info.getKeyword();
			
			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}
			param.append("phone=").append(info.getMobile())
			.append("&vcode=").append(info.getCode());
			
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"3000");
			logger.info(info.getSpId()+"请求结果:"+reqResult);
			
			if (reqResult != null && reqResult.startsWith("s")) {
				result = "ok";
			} else {
				result = "error";
			}
			
		} catch (Exception e) {
			logger.error(info.getSpId()+"指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	/**
	 * jd验证码
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS178(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getMatchRegex();
			String keyword = info.getKeyword();
			
			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}
			param.append("tel=").append(info.getMobile())
			.append("&ext=").append(info.getFfId())
			.append("&smsContent=").append(info.getCode());
			
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"3000");
			logger.info("sd33请求结果:"+reqResult);
			
			if (reqResult != null) {
				result = "ok";
			} else {
				result = "error";
			}
			
		} catch (Exception e) {
			logger.error("sd33指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
//	private static String getOnlineSMS178(OrderReqInfo info) {
//		String result = "error";
//		try  {
//			
//			String url = info.getMatchRegex();
//			String tel = info.getMobile();
//			String smsContent = info.getCode();
//			String keyword = info.getKeyword();
//			
//			StringBuilder param = new StringBuilder();
//			if (keyword != null && keyword.length() > 0) {
//				param.append(keyword);
//			}
//		
//			param.append("tel=").append(tel)
//			.append("&smsContent=").append(smsContent);
//			
//			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "3000");
//			logger.info(info.getSpId()+"请求结果:"+reqResult);
//			if (reqResult != null) {
//				result = "ok";
//			} else {
//				result = "error";
//			}
//			
//			
//			
//		} catch (Exception e) {
//			logger.error(info.getSpId()+"指令失败!");
//			logger.error(e.getMessage(), e.getCause());
//		}
//		
//		return result;
//		
//	}
	
	private static String getOnlineSMS175(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getMatchRegex();
			String keyword = info.getKeyword();
			
			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}
			boolean codeResult= info.getCode().matches("[0-9]+");
			if(!codeResult){
				result = "error";
				return result;
			}
			param.append("telphone=").append(info.getMobile())
			.append("&verify_code=").append(info.getCode());
			
			String reqResult = HttpTool.sendPost2(url, param.toString(),"3000");
			logger.info(info.getSpId()+"请求结果:"+reqResult);
			
			if (reqResult != null && reqResult.length() > 0) {
				
				result = "ok";
			} else {
				result = "error";
			}
			
		} catch (Exception e) {
			logger.error(info.getSpId()+"指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}
	
	private static String getOnlineSMS174(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getMatchRegex();
			String keyword = info.getKeyword();
			
			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}
			param.append("phone=").append(info.getMobile())
			.append("&code=").append(info.getCode());
			
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"3000");
			logger.info("微博请求结果:"+reqResult);
			
			if (reqResult != null) {
				result = reqResult;
			} else {
				result = "request time out";
			}
			
		} catch (Exception e) {
			logger.error("微博指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
	private static String getOnlineSMS124(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getMatchRegex();
			String keyword = info.getKeyword();
			String traid = info.getTraid();
			
//			String timestamp = DateTool.getTimestamp(new Date());
			
			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}
			param.append("phone=").append(info.getMobile())
			.append("&code=").append(info.getCode());
			if (traid != null && traid.length() > 0) {
				param.append("&orderid=").append(traid);
			}
			
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "4000");
			logger.info("咪咕注册sd66请求结果："+reqResult);
			if (reqResult == null || reqResult.length() <= 0) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("status", "2");
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");
				
				reqResult = JSONObject.toJSONString(resultMap);
			} else {
				reqResult = "ok";
			}
			
			result = reqResult;
			
		} catch (Exception e) {
			logger.error("咪咕注册sd66指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
	private static String getOnlineSMS121(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getUrl();
			String matchRegex = info.getMatchRegex();
			
			StringBuilder param = new StringBuilder();
			param.append(matchRegex)
			.append("&mobile=").append(info.getMobile())
			.append("&msg=").append(URLEncoder.encode(info.getSmscontent(), "utf-8"));
			
			String reqResult = HttpTool.sendPost(url, param.toString(), "3000");
			logger.info("京东注册sd62请求结果："+reqResult);
			if (reqResult == null || reqResult.length() <= 0) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("status", "2");
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");
				
				reqResult = JSONObject.toJSONString(resultMap);
			} else {
				if ("success".equals(reqResult)) {
					reqResult = "ok";
				}
			}
			
			result = reqResult;
			
		} catch (Exception e) {
			logger.error("京东注册sd62指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
	private static String getOnlineSMS112(OrderReqInfo info) {
		String result = "error";
		try  {
			String url = info.getCtech();
			String smscontent = info.getSmscontent();
			if (smscontent == null) return result;
			String[] codestr = smscontent.split("（移动统一");
			String[] codestr1 = codestr[0].split("验证码：");
			String code = codestr1[1];
			
			String[] tmp = url.split("\\?");
			if (tmp.length == 2) {
				url = tmp[0];
				String param = tmp[1] + code;
				String reqResult = HttpTool.sendGetSetTimeout(url, param,"1000");
				logger.info("yy10验证码提交结果:"+reqResult);
				result = "ok";
			}
			
		} catch (Exception e) {
			logger.error("yy10指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
	}
	
	private static String getOnlineSMS109(OrderReqInfo info) {
		String result = "error";
		try  {
			String url = info.getCtech();
			String smscontent = info.getSmscontent();
			String code = info.getCode();
			if (code == null) {
				if (smscontent == null) return result;
				String[] codestr = smscontent.split("，仅用于");
				String[] codestr1 = codestr[0].split("验证码");
				code = codestr1[1];
			}
			
			String[] tmp = url.split("\\?");
			if (tmp.length == 2) {
				url = tmp[0];
				String param = tmp[1] + code;
				String reqResult = HttpTool.sendGetSetTimeout(url, param,"1000");
				logger.info("yy09验证码提交结果:"+reqResult);
				result = "ok";
			}
			
		} catch (Exception e) {
			logger.error("yy09指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
	}
	
	private static String getOnlineSMS105(OrderReqInfo info) {
		String result = "error";
		try  {
			String matchRegex = info.getMatchRegex();
			String[] tmp = matchRegex.split("##");
			String url = tmp[1];
			
			StringBuilder param = new StringBuilder();
			param.append("imsi=").append(info.getImsi())
			.append("&vcode=").append(info.getSmscontent());
			String sms = HttpTool.sendGetSetTimeout(url, param.toString(), "5000");
			logger.info("lt09提交验证码结果:" + sms);
			result = "ok";
		} catch (Exception e) {
			logger.error("lt09提交验证码失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}
	
	private static String getOnlineSMS100(OrderReqInfo info) {
		String result = "error";
		try  {
//			String url = info.getUrl();
			String keyword = info.getKeyword();
			String matchRegex = info.getMatchRegex();
			String[] tmp = matchRegex.split("##");
			
			String url = tmp[1];
			
			String smscontent = info.getSmscontent();
			if (smscontent == null) return result;
			
			String[] arr = smscontent.split("，请填到");
			String[] arr1 = arr[0].split("验证码为");
			
			
			StringBuilder param = new StringBuilder();
			param.append(keyword)
			.append("&paymentUser=").append(info.getMobile())
			.append("&price=").append(info.getFee())
			.append("&paymentcodesms=").append(arr1[1])
			.append("&outTradeNo=").append(info.getCtech());
			String sms = HttpTool.sendGetSetTimeout(url, param.toString(), "5000");
			logger.info("稻香lt08提交验证码结果:" + sms);
			result = "ok";
		} catch (Exception e) {
			logger.error("稻香lt08提交验证码失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}
	
	private static String getOnlineSMS96(OrderReqInfo info) {
		String result = "error";
		try  {
//			String url = info.getUrl();
//			String keyword = info.getKeyword();
			String matchRegex = info.getMatchRegex();
			
			String url = matchRegex;
			
			StringBuilder param = new StringBuilder();
			param.append("pid=645&orderid=").append(info.getTraid())
			.append("&verifyCode=").append(info.getCode());
			String sms = HttpTool.sendGetSetTimeout(url, param.toString(), "5000");
			logger.info("lt07提交验证码结果:" + sms);
			result = "ok";
		} catch (Exception e) {
			logger.error("lt07提交验证码失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}
	
	private static String getOnlineSMS83(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getCtech();
			String smscontent = info.getSmscontent();
			if (smscontent == null) return result;
			String[] codestr = smscontent.split("（如非本人");
			String[] codestr1 = codestr[0].split("验证码：");
			String code = codestr1[1];
			
			String[] tmp = url.split("\\?");
			if (tmp.length == 2) {
				url = tmp[0];
				String param = tmp[1] + code;
				String reqResult = HttpTool.sendGetSetTimeout(url, param,"1000");
				logger.info("dm15验证码提交结果:"+reqResult);
				result = "ok";
			}
			
		} catch (Exception e) {
			logger.error("dm15指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
	private static String getOnlineSMS82(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getCtech();
			String smscontent = info.getSmscontent();
			if (smscontent == null) return result;
			
			String code = smscontent;
			
			String[] tmp = url.split("\\?");
			if (tmp.length == 2) {
				url = tmp[0];
				String param = tmp[1] + code;
				String reqResult = HttpTool.sendGetSetTimeout(url, param,"3000");
				logger.info("dm15验证码提交结果:"+reqResult);
				result = "ok";
			}
			
		} catch (Exception e) {
			logger.error("dm12指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
	private static String getOnlineSMS63(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getMatchRegex();
			String keyword = info.getKeyword();
			
			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}
			param.append("orderid=").append(info.getPmodel())
			.append("&code=").append(info.getCode());
			
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"3000");
			logger.info("sd25请求结果:"+reqResult);
			
			if (reqResult != null) {
				result = reqResult;
			} else {
				result = "request time out";
			}
			
		} catch (Exception e) {
			logger.error("sd25指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
	private static String getOnlineSMS60(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getMatchRegex();
			String keyword = info.getKeyword();
			
			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}
			param.append("mobile=").append(info.getMobile())
			.append("&vcode=").append(info.getCode());
			
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"3000");
			logger.info("sd21请求结果:"+reqResult);
			
			if (reqResult != null) {
				result = "ok";
			} else {
				result = "error";
			}
			
		} catch (Exception e) {
			logger.error("sd21指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
	private static String getOnlineSMS59(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String body = info.getBody();
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String[] tmp = keyword.split("#");
			
			if (body == null || body.length() <= 0) return result;
			
			String account = tmp[0];
			String username = tmp[1];
			String password = tmp[2];
			String date = DateTool.getNow().substring(0,8);
			
			String token = MD5Tool.toMD5Value(username+password+date);
			
			/*Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("account", account);
			paramMap.put("Token", token);
			paramMap.put("fname", info.getFfId());
			paramMap.put("fdata", URLDecoder.decode(info.getBody(), "utf-8"));
			String param = JSONObject.toJSONString(paramMap);*/
			System.out.println(token);
			StringBuilder param = new StringBuilder();
			param.append("account=").append(account)
			.append("&Token=").append(token)
			.append("&fname=").append(info.getFfId())
			.append("&fdata=").append(URLDecoder.decode(info.getBody(), "utf-8"));
			
			String reqResult = HttpTool.sendPost(url, param.toString(), "5000");
			logger.info("sd20请求结果:"+reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				result = "ok";
			}
			
		} catch (Exception e) {
			logger.error("sd20指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
	private static String getOnlineSMS57(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getMatchRegex();
			String keyword = info.getKeyword();
			
			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}
			param.append("tel=").append(info.getMobile())
			.append("&ext=").append(info.getFfId())
			.append("&smsContent=").append(info.getCode());
			
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"3000");
			logger.info("sd18请求结果:"+reqResult);
			
			if (reqResult != null) {
				result = "ok";
			} else {
				result = "error";
			}
			
		} catch (Exception e) {
			logger.error("sd18指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
	private static String getOnlineSMS55(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getMatchRegex();
			String keyword = info.getKeyword();
			
			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}
			param.append("phone=").append(info.getMobile())
			.append("&vcode=").append(info.getCode());
			
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"3000");
			logger.info("sd17请求结果:"+reqResult);
			
			if (reqResult != null) {
				result = "ok";
			} else {
				result = "error";
			}
			
		} catch (Exception e) {
			logger.error("sd17指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
	private static String getOnlineSMS49(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getUrl();
			String matchRegex = info.getMatchRegex();
			
			StringBuilder param = new StringBuilder();
			param.append(matchRegex)
			.append("msgCode=").append(info.getCode())
			.append("&phone=").append(info.getMobile())
			.append("&orderId=").append(info.getTraid());
			
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"2000");
			logger.info("sd08请求结果:"+reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String status = jsonobj.getString("code");
				Map<String, String> jsonMap = new HashMap<String, String>();
				if ("0".equals(status)) {
					reqResult = "ok";
				} else {
					String msg = jsonobj.getString("message");
					jsonMap.put("status", status);
					jsonMap.put("msg", msg);
					reqResult = JSONObject.toJSONString(jsonMap);
				}
				result = reqResult;
			}
			
		} catch (Exception e) {
			logger.error("sd08指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
	private static String getOnlineSMS48(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String url = info.getMatchRegex();
			String keyword = info.getKeyword();
			String[] tmp = keyword.split("_");
			String num = info.getNum();
			
			StringBuilder param = new StringBuilder();
			if ("1".equals(num)) {
				param.append(tmp[0]);
			} else if ("2".equals(num)) {
				param.append(tmp[1]);
			} else if ("3".equals(num)) {
				param.append(tmp[2]);
			}
			param.append("phone=").append(info.getMobile())
			.append("&code=").append(info.getCode())
			.append("&cpparam=").append(info.getFfId());
			
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"3000","gb2312");
			 /*if (reqResult != null && reqResult.length() > 0 && !"error".equals(reqResult)) {
				 JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String status = jsonobj.getString("status");
				Map<String, String> jsonMap = new HashMap<String, String>();
				if ("0".equals(status)) {
//					String traid = jsonobj.getString("orderid");
					jsonMap.put("status", "0");
					jsonMap.put("orderid", info.getFfId());
//					jsonMap.put("traid", traid);
				} else {
//					String msg = jsonobj.getString("message");
					jsonMap.put("status", status);
//					jsonMap.put("msg", msg);
				}
			 }*/
			logger.info("QQ注册sd07请求结果："+reqResult);
			result = reqResult;
			
		} catch (Exception e) {
			logger.error("QQ注册sd07指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
	private static String getOnlineSMS46(OrderReqInfo info) {
		String result = "error";
		try  {
			
			String code = info.getCode();
			String smscontent = info.getSmscontent();
			String requrl = info.getCtech();
			
			if (smscontent != null && smscontent.length() > 0 && smscontent.contains("验证码")) {
				String[] codestr = smscontent.split("（如非本人操作");
				String[] codestr1 = codestr[0].split("验证码：");
				code = codestr1[1];
			}
			
			if (requrl != null && requrl.startsWith("http")) {
				requrl = requrl.replace("[key]", code);
				String reqResult = HttpTool.sendGetSetTime(requrl, "5000");
				logger.info("苏州乐麟yy05请求结果:"+reqResult);
				result = reqResult;
			}
			
		} catch (Exception e) {
			logger.error("苏州乐麟yy05指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
	
	
	
	/*private static String parseHTML(String url, OrderReqInfo info) throws Exception {
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
	}*/

}
