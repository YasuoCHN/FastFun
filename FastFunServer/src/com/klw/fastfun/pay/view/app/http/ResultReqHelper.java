package com.klw.fastfun.pay.view.app.http;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.juice.orange.game.log.LoggerFactory;
import com.klw.fastfun.pay.common.domain.LTPayResultH5Info;
import com.klw.fastfun.pay.common.domain.OrderReqInfo;
import com.klw.fastfun.pay.common.tool.AESOperator;
import com.klw.fastfun.pay.common.tool.CommonTool;
import com.klw.fastfun.pay.common.tool.ConstantDefine;
import com.klw.fastfun.pay.common.tool.HttpTool;
import com.klw.fastfun.pay.common.tool.HttpsTool;
import com.klw.fastfun.pay.common.tool.MD5Tool;
import com.klw.fastfun.pay.common.tool.SHAUtil;
import com.klw.fastfun.pay.common.tool.xdzf.MD5;
import com.klw.fastfun.pay.view.app.other.qmdyuril.PaySignUtil;
import com.klw.fastfun.pay.view.app.other.util.RSASignature;
import com.klw.fastfun.pay.view.app.wxpay.utils.CollectionUtil;
import com.klw.fastfun.pay.view.app.wxpay.utils.PayUtil;
import com.klw.fastfun.pay.view.app.wxpay.utils.XmlUtil;

/**
 * 计费结果查询
 * @author ouyangsu
 *
 */
public class ResultReqHelper {
	
	private static Logger logger = LoggerFactory.getLogger(ResultReqHelper.class);
	public static final String SER_RESULT_REQ = "serresultreq_";
	
	
	public static String getPayResult(OrderReqInfo info) {
		switch (info.getUrlNo()) {
		case ConstantDefine.URL_NO_SD25_JSON:
			return getOnlineSMS63(info);
		case ConstantDefine.URL_NO_SD26_JSON:
			return getOnlineSMS73(info);
		case ConstantDefine.URL_NO_SD27_JSON:
			return getOnlineSMS75(info);
		case ConstantDefine.URL_NO_SD31_JSON:
			return getOnlineSMS85(info);
		case ConstantDefine.URL_NO_SD49_JSON:
			return getOnlineSMS108(info);
		case ConstantDefine.URL_NO_SD50_JSON:
			return getOnlineSMS114(info);
		case ConstantDefine.URL_NO_SD52_JSON:
			return getOnlineSMS114(info);
		case ConstantDefine.URL_NO_SD55_JSON:
			return getOnlineSMS117(info);
		case ConstantDefine.URL_NO_SD68_JSON:
			return getOnlineSMS126(info);
		case ConstantDefine.URL_NO_SD75_JSON:
			return getOnlineSMS130(info);
		case ConstantDefine.URL_NO_SD77_JSON:
			return getOnlineSMS132(info);
		case ConstantDefine.URL_NO_SD83_JSON:
			return getOnlineSMS136(info);
		case ConstantDefine.URL_NO_SD84_JSON:
			return getOnlineSMS137(info);
		case ConstantDefine.URL_NO_SD94_JSON:
			return getOnlineSMS148(info);
		case ConstantDefine.URL_NO_SD97_JSON:
			return getOnlineSMS152(info);
		case ConstantDefine.URL_NO_SDE2_JSON:
			return getOnlineSMS126(info);
		case ConstantDefine.URL_NO_SDE3_JSON:
			return getOnlineSMS126(info);
		case ConstantDefine.URL_NO_SDF1_JSON:
			return getOnlineSMS171(info);
		default:
			break;
		}
		return null;
	}
	
	/*public static void main(String[] args) {
		String url = "http://u3.yqunion.com/utopay/api/queryOrder.pay";
		String param = "companyId=39&userOrderId=sd9701sdc00698619094&sign=7254f5a31172ef6c759e8f8a1ce80c40";
		String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "4000", "utf-8");
		System.out.println(reqResult);
	}*/
	private static String getOnlineSMS171(OrderReqInfo info) {
		// 1–支付成功； 2–待支付； 3 - 支付失败； 4 - 取消订单； 5 - 支付超时； 6 - 退款； 7 - 支付中； 9 - 其他错误
		String result = "{\"result\":\"-1\"}";
		try  {
			
			String url = info.getMatchRegex();
			String keyword = info.getKeyword();
			String[] tmp = keyword.split("#");
			
			String orderid = info.getSpId()+info.getFfId();
			String merchid = tmp[0];
			String appsecret = tmp[1];
			
			StringBuilder reqsignparam = new StringBuilder();
			reqsignparam.append(orderid)
			.append("_").append(appsecret);
			
			String reqsign = MD5Tool.toMD5Value(reqsignparam.toString()).toLowerCase();
			
			Map<String, String> restmap = new HashMap<String, String>();
			try {
				StringBuilder param = new StringBuilder();
				param.append("companyId=").append(merchid)
				.append("&userOrderId=").append(orderid)
				.append("&sign=").append(reqsign);
				
				String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "4000", "gb2312");
				logger.info("云贝支付sd97支付结果查询："+reqResult);
				if (reqResult != null && reqResult.length() > 0) {
					JSONObject jsonobj = JSONObject.parseObject(reqResult);
					for (Map.Entry<String,Object> entry :  jsonobj.entrySet()) {
						restmap.put( entry.getKey(), (String)entry.getValue());
					}
				}
			} catch (Exception e) {
				logger.error("云贝支付sd97支付结果查询失败!");
			}
			
			StringBuilder signparam = new StringBuilder();
			signparam.append("fee=").append(info.getFee())
			.append("&orderid=").append(info.getFfId());
			
			Map<String, String> resmap = new HashMap<String, String>();
			if (CollectionUtil.isNotEmpty(restmap)) {
				String state = restmap.get("status");
				String status = "9";
				if ("0".equals(state)) {
					status = "1";
				} else if ("1".equals(state)) {
					status = "3";
				} else if ("-1".equals(state)) {
					status = "2";
				}
				resmap.put("result", status);
				signparam.append("&result="+status);
			}
			
			
			if (resmap != null && resmap.size() > 0) {
				String cpid = info.getCpId();
				if (cpid == null || cpid.length() <= 2) {
					cpid = "mrjm";
				} else {
					cpid = cpid.substring(0,cpid.length()-2);
				}
				String key = ConstantDefine.MD5_KEY1 + cpid + ConstantDefine.MD5_KEY2;
				signparam.append("&key=").append(key);
				String sign = MD5.md5(signparam.toString(), "utf-8");
				
				resmap.put("fee", info.getFee()+"");
				resmap.put("orderid", info.getFfId());
				resmap.put("sign", sign);
				
				result = JSONObject.toJSONString(resmap);
			}
			
			logger.info("云贝支付sd97支付结果查询json:"+result);
			
		} catch (Exception e) {
			logger.error("云贝支付sd97支付结果查询失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
	private static String getOnlineSMS152(OrderReqInfo info) {
		// 1–支付成功； 2–待支付； 3 - 支付失败； 4 - 取消订单； 5 - 支付超时； 6 - 退款； 7 - 支付中； 9 - 其他错误
		String result = "{\"result\":\"-1\"}";
		try  {
			
			String url = info.getMatchRegex();
			String keyword = info.getKeyword();
			String[] tmp = keyword.split("#");
			
			String orderid = info.getSpId()+info.getFfId();
			String merchid = tmp[0];
			String appsecret = tmp[1];
			
			StringBuilder reqsignparam = new StringBuilder();
			reqsignparam.append(orderid)
			.append("_").append(appsecret);
			
			String reqsign = MD5Tool.toMD5Value(reqsignparam.toString()).toLowerCase();
			
			Map<String, String> restmap = new HashMap<String, String>();
			try {
				StringBuilder param = new StringBuilder();
				param.append("companyId=").append(merchid)
				.append("&userOrderId=").append(orderid)
				.append("&sign=").append(reqsign);
				
				String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "4000", "gb2312");
				logger.info("云贝支付sd97支付结果查询："+reqResult);
				if (reqResult != null && reqResult.length() > 0) {
					JSONObject jsonobj = JSONObject.parseObject(reqResult);
					for (Map.Entry<String,Object> entry :  jsonobj.entrySet()) {
						restmap.put( entry.getKey(), (String)entry.getValue());
					}
				}
			} catch (Exception e) {
				logger.error("云贝支付sd97支付结果查询失败!");
			}
			
			StringBuilder signparam = new StringBuilder();
			signparam.append("fee=").append(info.getFee())
			.append("&orderid=").append(info.getFfId());
			
			Map<String, String> resmap = new HashMap<String, String>();
			if (CollectionUtil.isNotEmpty(restmap)) {
				String state = restmap.get("status");
				String status = "9";
				if ("0".equals(state)) {
					status = "1";
				} else if ("1".equals(state)) {
					status = "3";
				} else if ("-1".equals(state)) {
					status = "2";
				}
				resmap.put("result", status);
				signparam.append("&result="+status);
			}
			
			
			if (resmap != null && resmap.size() > 0) {
				String cpid = info.getCpId();
				if (cpid == null || cpid.length() <= 2) {
					cpid = "mrjm";
				} else {
					cpid = cpid.substring(0,cpid.length()-2);
				}
				String key = ConstantDefine.MD5_KEY1 + cpid + ConstantDefine.MD5_KEY2;
				signparam.append("&key=").append(key);
				String sign = MD5.md5(signparam.toString(), "utf-8");
				
				resmap.put("fee", info.getFee()+"");
				resmap.put("orderid", info.getFfId());
				resmap.put("sign", sign);
				
				result = JSONObject.toJSONString(resmap);
			}
			
			logger.info("云贝支付sd97支付结果查询json:"+result);
			
		} catch (Exception e) {
			logger.error("云贝支付sd97支付结果查询失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
	private static String getOnlineSMS148(OrderReqInfo info) {
		// 1–支付成功； 2–待支付； 3 - 支付失败； 4 - 取消订单； 5 - 支付超时； 6 - 退款； 7 - 支付中； 9 - 其他错误
		String result = "{\"result\":\"-1\"}";
		try  {
			
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String[] tmp = keyword.split("#");
			
			String mchid = tmp[0];
			String appsecret = tmp[1];
			String service = "api.order.query";
			
			Map<String, String> restmap = new HashMap<String, String>();
			try {
				Map<String, String> parm = new HashMap<String, String>();
				parm.put("method", service);
				parm.put("mchid", mchid);
				parm.put("orderno", info.getFfId());
				
				parm.put("sign", PayUtil.getSign(parm, appsecret));
				
				String param = JSONObject.toJSONString(parm);
				String reqResult = HttpTool.sendPost(url, param, "4000");
				logger.info("阿信sd94支付结果查询："+reqResult);
				if (reqResult != null && reqResult.length() > 0) {
					JSONObject jsonobj = JSONObject.parseObject(reqResult);
					for (Map.Entry<String,Object> entry :  jsonobj.entrySet()) {
						restmap.put( entry.getKey(), (String)entry.getValue());
					}
				}
			} catch (Exception e) {
				logger.error("阿信sd94支付结果查询失败!");
			}
			
			StringBuilder signparam = new StringBuilder();
			signparam.append("fee=").append(info.getFee())
			.append("&orderid=").append(info.getFfId());
			
			Map<String, String> resmap = new HashMap<String, String>();
			if (CollectionUtil.isNotEmpty(restmap)) {
				String ressign = restmap.get("sign");
				restmap.remove("sign");
				String ressigny = PayUtil.getSign(restmap, appsecret);
				if (ressigny.equals(ressign)) {
					
					String state = restmap.get("status");
					String status = "9";
					if ("1".equals(state)) {
						status = "1";
					}
					resmap.put("result", status);
					signparam.append("&result="+status);
				}
			}
			
			
			if (resmap != null && resmap.size() > 0) {
				String cpid = info.getCpId();
				if (cpid == null || cpid.length() <= 2) {
					cpid = "mrjm";
				} else {
					cpid = cpid.substring(0,cpid.length()-2);
				}
				String key = ConstantDefine.MD5_KEY1 + cpid + ConstantDefine.MD5_KEY2;
				signparam.append("&key=").append(key);
				String sign = MD5.md5(signparam.toString(), "utf-8");
				
				resmap.put("fee", info.getFee()+"");
				resmap.put("orderid", info.getFfId());
				resmap.put("sign", sign);
				
				result = JSONObject.toJSONString(resmap);
			}
			
			logger.info("阿信sd94支付结果查询json:"+result);
			
		} catch (Exception e) {
			logger.error("阿信sd94支付结果查询失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
	/**
	 * @author : Tractionice
	 * @version 创建时间：2017年11月30日 下午2:52:04
	 * @param order_id:商户订单号(不超过50位);money:支付金额 单位为分;mch:商户注册获得的商务号;key;pay_type:微信H5为wxhtml;sign:签名;time:时间戳;
	 * @return 查询结果
	 */
	private static String getOnlineSMS137(OrderReqInfo info) {
		String result = "{\"result\":\"-1\"}";
		try  {
			//url:info.getUrl();
			//info.getMatchRegex(); http://api.ayame.com.cn/lqpay/showquery
			//info.getOrderId();
			//info.getFee();
			String[] tmp = info.getKeyword().split("#");
			//String mch = tmp[0];// 商户编号
			//String key = tmp[1];// 商户秘钥
			//String pay_type = tmp[2];// 支付类型
			long time = System.currentTimeMillis() / 1000;
			//下单签名方式：md5(mch+order_id+money+pay_type+time+md5(key))，+是连接的意思，里面取值，例如待签名的数据为a=1,b=2,则字符串为12
			String sign=MD5.md5(new StringBuilder()
			.append(tmp[0])
			.append(info.getFfId())
			.append(info.getFee())
			.append(tmp[2])
			.append(time)
			.append(MD5.md5(tmp[1], "utf-8")).toString(), "utf-8");
			StringBuilder param=new StringBuilder();
			param.append("order_id=").append(info.getFfId())
			.append("&money=").append(info.getFee())
			.append("&mch=").append(tmp[0]).append("&key=")
			.append(tmp[1]).append("&pay_type=").append(tmp[2])
			.append("&sign=").append(sign).append("&time=")
			.append(time);
			String reqResult = HttpTool.sendGet(info.getMatchRegex(), param.toString());
			logger.info("惠付sd84支付结果查询："+reqResult);
			if(reqResult!=null&&reqResult.length()>0){
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("orderid",info.getFfId());
				map.put("fee", info.getFee());
				String status = jsonobj.getString("data");
				if("0".equals(status)){
					status="3";
				}
				StringBuilder sig=new StringBuilder();
				sig.append("fee=").append(info.getFee())
				.append("&orderid=")
				.append(info.getFfId())
				.append("&result=")
				.append(status);
				map.put("sign",MD5.md5(sig.toString(),"utf-8"));
				map.put("result", status);
				result = JSONObject.toJSONString(map);
			}
		} catch (Exception e) {
			logger.error("惠付sd84支付结果查询失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;	
	}
	
	/**
	 * @author : Tractionice
	 * @version 创建时间：2017年11月30日 下午4:55:20
	 * @param spid orderid sign
	 * @return 
	 */
	private static String getOnlineSMS136(OrderReqInfo info){
		String result="{\"result\":\"-1\"}";
		try{
			//获取spid
			String spId=info.getKeyword().split("#")[2];
			//签名
			String sign=new StringBuilder().append(spId).append(info.getFfId()).append(info.getKeyword().split("#")[3]).toString();
			sign=MD5.md5(sign,"utf-8");
			//定义参数
			StringBuilder param=new StringBuilder();
			param.append("spid=").append(spId).append("&orderid=").append(info.getFfId()).append("&sign=").append(sign);
			//发送请求
			String reqResult = HttpTool.sendGet(info.getMatchRegex(), param.toString());
			logger.info("盈华迅方sd83支付结果查询："+reqResult);
			if(reqResult!=null&&reqResult.length()>0){
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("orderid",info.getFfId());
				map.put("fee", info.getFee());
				String status = jsonobj.getString("code");
				String msg=jsonobj.getString("msg");
				if("success".equals(status)){
					status="1";
				}else if("支付失败".equals(msg)){
					status="3";
				}else{
					status="9";
				}
				StringBuilder sig=new StringBuilder();
				sig.append("fee=").append(info.getFee())
				.append("&orderid=").append(info.getFfId())
				.append("&result=").append(status);
				map.put("sign",MD5.md5(sig.toString(),"utf-8"));
				map.put("result", status);
				result = JSONObject.toJSONString(map);
			}
		}catch(Exception e){
			logger.error("盈华迅方sd83支付结果查询失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}
	private static String getOnlineSMS132(OrderReqInfo info) {
		// 1–支付成功； 2–待支付； 3 - 支付失败； 4 - 取消订单； 5 - 支付超时； 6 - 退款； 7 - 支付中； 9 - 其他错误
		String result = "{\"result\":\"-1\"}";
		try  {
			
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String[] tmp = keyword.split("#");
			
			String mchid = tmp[0];
			String appsecret = tmp[1];
			
			Map<String, String> restmap = new HashMap<String, String>();
			try {
				Map<String, String> parm = new HashMap<String, String>();
				parm.put("merchant_id", mchid);
				parm.put("merchant_order_no", info.getFfId());
				parm.put("sign", PayUtil.getSign(parm, appsecret));
				
				String param = JSONObject.toJSONString(parm);
				String reqResult = HttpTool.sendKLWPost(url, param, "utf-8", 4000);
				logger.info("富有sd77支付结果查询："+reqResult);
				if (reqResult != null && reqResult.length() > 0) {
					JSONObject jsonobj = JSONObject.parseObject(reqResult);
					for (Map.Entry<String,Object> entry :  jsonobj.entrySet()) {
						restmap.put( entry.getKey(), (String)entry.getValue());
					}
				}
			} catch (Exception e) {
				logger.error("富有sd77支付结果查询失败!");
			}
			
			StringBuilder signparam = new StringBuilder();
			signparam.append("fee=").append(info.getFee())
			.append("&orderid=").append(info.getFfId());
			
			Map<String, String> resmap = new HashMap<String, String>();
			if (CollectionUtil.isNotEmpty(restmap) && "0000".equals(restmap.get("code"))
					&& "0".equals(restmap.get("result_code"))) {
				String ressign = restmap.get("sign");
				restmap.remove("sign");
				String ressigny = PayUtil.getSign(restmap, appsecret);
				if (ressigny.equals(ressign)) {
					
					String state = restmap.get("pay_status");
					String status = "9";
					if ("1".equals(state)) {
						status = "1";
					}
					resmap.put("result", status);
					signparam.append("&result="+status);
				}
			}
			
			
			if (resmap != null && resmap.size() > 0) {
				String cpid = info.getCpId();
				if (cpid == null || cpid.length() <= 2) {
					cpid = "mrjm";
				} else {
					cpid = cpid.substring(0,cpid.length()-2);
				}
				String key = ConstantDefine.MD5_KEY1 + cpid + ConstantDefine.MD5_KEY2;
				signparam.append("&key=").append(key);
				String sign = MD5.md5(signparam.toString(), "utf-8");
				
				resmap.put("fee", info.getFee()+"");
				resmap.put("orderid", info.getFfId());
				resmap.put("sign", sign);
				
				result = JSONObject.toJSONString(resmap);
			}
			
			logger.info("富有sd77支付结果查询json:"+result);
			
		} catch (Exception e) {
			logger.error("富有sd77支付结果查询失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
	private static String getOnlineSMS130(OrderReqInfo info) {
		// 1–支付成功； 2–待支付； 3 - 支付失败； 4 - 取消订单； 5 - 支付超时； 6 - 退款； 7 - 支付中； 9 - 其他错误
		String result = "{\"result\":\"-1\"}";
		try  {
			
			String url = info.getMatchRegex();
			String keyword = info.getKeyword();
			String[] tmp = keyword.split("#");
			
			String mchid = tmp[0];
			String appsecret = tmp[1];
			
			StringBuilder signparam = new StringBuilder();
			signparam.append("fee=").append(info.getFee())
			.append("&orderid=").append(info.getFfId());
			
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("bussOrderNum", info.getFfId());
			map.put("appKey", mchid);//测试appKey
			String sign =PaySignUtil.getSign(map,appsecret);//测试密钥
			String paramStr = PaySignUtil.getParamStr(map)+"&sign="+sign;
			String param = "paramStr=" + URLEncoder.encode(paramStr, "utf-8");
			
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "5000");
			logger.info("全名点游sd75请求结果:"+reqResult);
			Map<String, String> resmap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String resultCode = jsonobj.getString("resultCode");
				if (resultCode != null) {
					String status = "9";
					if ("2012".equals(resultCode)) {
						status = "2";
					} else if ("200".equals(resultCode)) {
						status = "1";
					} else if ("2011".equals(resultCode)) {
						status = "3";
					}
					resmap.put("result", status);
					signparam.append("&result="+status);
				}
			}
			
			if (resmap != null && resmap.size() > 0) {
				String cpid = info.getCpId();
				if (cpid == null || cpid.length() <= 2) {
					cpid = "mrjm";
				} else {
					cpid = cpid.substring(0,cpid.length()-2);
				}
				String key = ConstantDefine.MD5_KEY1 + cpid + ConstantDefine.MD5_KEY2;
				signparam.append("&key=").append(key);
				String nsign = MD5.md5(signparam.toString(), "utf-8");
				
				resmap.put("fee", info.getFee()+"");
				resmap.put("orderid", info.getFfId());
				resmap.put("sign", nsign);
				
				result = JSONObject.toJSONString(resmap);
			}
			
			logger.info("全名点游sd75结果查询json:"+result);
			
		} catch (Exception e) {
			logger.error("全名点游sd75指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}
	
	private static String getOnlineSMS126(OrderReqInfo info) {
		// 1–支付成功； 2–待支付； 3 - 支付失败； 4 - 取消订单； 5 - 支付超时； 6 - 退款； 7 - 支付中； 9 - 其他错误
		String result = "{\"result\":\"-1\"}";
		try  {
			
			String url = info.getMatchRegex();
			String keyword = info.getKeyword();
			String[] tmp = keyword.split("#");
			
			String merchantNo = tmp[0];
			String appsecret = ConstantDefine.JHZ_V_KEY;
			
			StringBuilder signpara = new StringBuilder();
			signpara.append(merchantNo)
			.append("|").append(info.getFfId());
			
			String sign = URLEncoder.encode(RSASignature.sign(signpara.toString(), appsecret), "utf-8");
			
			StringBuilder param = new StringBuilder();
			param.append("merchantNo=").append(merchantNo)
			.append("&requestNo=").append(info.getFfId())
			.append("&signature=").append(sign);
			
			StringBuilder signparam = new StringBuilder();
			signparam.append("fee=").append(info.getFee())
			.append("&orderid=").append(info.getFfId());
			
			String reqResult = HttpTool.sendPost(url, param.toString(), "5000");
			logger.info("金海哲sd68请求结果:"+reqResult);
			Map<String, String> resmap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String resultCode = jsonobj.getString("result_code");
				if (resultCode != null) {
					String status = "9";
					if ("7002".equals(resultCode)) {
						status = "2";
					} else if ("1000".equals(resultCode)) {
						status = "1";
					} else if ("9003".equals(resultCode)) {
						status = "3";
					} else if ("7004".equals(resultCode)) {
						status = "5";
					}
					resmap.put("result", status);
					signparam.append("&result="+status);
				}
			}
			
			if (resmap != null && resmap.size() > 0) {
				String cpid = info.getCpId();
				if (cpid == null || cpid.length() <= 2) {
					cpid = "mrjm";
				} else {
					cpid = cpid.substring(0,cpid.length()-2);
				}
				String key = ConstantDefine.MD5_KEY1 + cpid + ConstantDefine.MD5_KEY2;
				signparam.append("&key=").append(key);
				String nsign = MD5.md5(signparam.toString(), "utf-8");
				
				resmap.put("fee", info.getFee()+"");
				resmap.put("orderid", info.getFfId());
				resmap.put("sign", nsign);
				
				result = JSONObject.toJSONString(resmap);
			}
			
			logger.info("金海哲sd68结果查询json:"+result);
			
		} catch (Exception e) {
			logger.error("金海哲sd68结果查询失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
	private static String getOnlineSMS117(OrderReqInfo info) {
		// 1–支付成功； 2–待支付； 3 - 支付失败； 4 - 取消订单； 5 - 支付超时； 6 - 退款； 7 - 支付中； 9 - 其他错误
		String result = "{\"result\":\"-1\"}";
		try  {
			
			String url = info.getMatchRegex();
			String keyword = info.getKeyword();
			String[] tmp = keyword.split("#");
			
			String appID = tmp[0];
			String appsecret = tmp[1];
			
			String nonceStr = PayUtil.getNonceStr();
			String source = appID +"&" + info.getFfId() + "&" + nonceStr;
			String sign = SHAUtil.getSign(SHAUtil.getHmacSHA1(source, appsecret));
			
			StringBuilder param = new StringBuilder();
			param.append("appID=").append(appID)
			.append("&orders=").append(info.getFfId())
			.append("&nonceStr=").append(nonceStr)
			.append("&sign=").append(sign);
			
			StringBuilder signparam = new StringBuilder();
			signparam.append("fee=").append(info.getFee())
			.append("&orderid=").append(info.getFfId());
			
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(),"5000");
			logger.info("我赞第三方sd55请求结果:"+reqResult);
			Map<String, String> resmap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String resultCode = jsonobj.getString("statusCode");
				if (resultCode != null && "200".equals(resultCode)) {
					JSONArray jsonArr = JSONArray.parseArray(jsonobj.getString("orders"));
					JSONObject jsonobj1 = jsonArr.getJSONObject(0);
					String state = jsonobj1.getString("status");
					String status = "9";
					if ("0".equals(state)) {
						status = "2";
					} else if ("1".equals(state)) {
						status = "1";
					} else if ("2".equals(state)) {
						status = "3";
					}
					resmap.put("result", status);
					signparam.append("&result="+status);
				}
			}
			
			if (resmap != null && resmap.size() > 0) {
				String cpid = info.getCpId();
				if (cpid == null || cpid.length() <= 2) {
					cpid = "mrjm";
				} else {
					cpid = cpid.substring(0,cpid.length()-2);
				}
				String key = ConstantDefine.MD5_KEY1 + cpid + ConstantDefine.MD5_KEY2;
				signparam.append("&key=").append(key);
				String nsign = MD5.md5(signparam.toString(), "utf-8");
				
				resmap.put("fee", info.getFee()+"");
				resmap.put("orderid", info.getFfId());
				resmap.put("sign", nsign);
				
				result = JSONObject.toJSONString(resmap);
			}
			
			logger.info("我赞第三方支付sd55结果查询json:"+result);
			
		} catch (Exception e) {
			logger.error("我赞第三方支付sd55结果查询失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
	private static String getOnlineSMS114(OrderReqInfo info) {
		// 1–支付成功； 2–待支付； 3 - 支付失败； 4 - 取消订单； 5 - 支付超时； 6 - 退款； 7 - 支付中； 9 - 其他错误
		String result = "{\"result\":\"-1\"}";
		try  {
			
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String[] tmp = keyword.split("#");
			
			String mchid = tmp[0];
			String appsecret = tmp[1];
			String service = "unified.trade.query";
			
			Map<String, String> restmap = null;
			try {
				Map<String, String> parm = new HashMap<String, String>();
				parm.put("service", service);
				parm.put("mch_id", mchid);
				parm.put("out_trade_no", info.getFfId());
				parm.put("nonce_str", PayUtil.getNonceStr());
				parm.put("sign", PayUtil.getSign(parm, appsecret));
				
				String param = XmlUtil.xmlFormat(parm, false);
				String restxml = HttpTool.sendPost(url, param, "4000");
				logger.info("支付宝扫码sd50支付结果查询："+restxml);
				restmap = XmlUtil.xmlParse(restxml);
			} catch (Exception e) {
				logger.error("支付宝扫码sd50支付结果查询失败!");
			}
			
			StringBuilder signparam = new StringBuilder();
			signparam.append("fee=").append(info.getFee())
			.append("&orderid=").append(info.getFfId());
			
			Map<String, String> resmap = new HashMap<String, String>();
			if (CollectionUtil.isNotEmpty(restmap) && "0".equals(restmap.get("status"))
					&& "0".equals(restmap.get("result_code"))) {
				String ressign = restmap.get("sign");
				restmap.remove("sign");
				String ressigny = PayUtil.getSign(restmap, appsecret);
				if (ressigny.equals(ressign)) {
					
					String state = restmap.get("trade_state");
					String status = "9";
					if ("SUCCESS".equals(state)) {
						status = "1";
					} else if ("REFUND".equals(state)) {
						status = "6";
					} else if ("NOTPAY".equals(state)) {
						status = "2";
					} else if ("CLOSED".equals(state)) {
						status = "5";
					} else if ("REVOKED".equals(state) || "REVOK".equals(state)) {
						status = "4";
					} else if ("USERPAYING".equals(state)) {
						status = "7";
					} else if ("PAYERROR".equals(state)) {
						status = "3";
					}
					resmap.put("result", status);
					signparam.append("&result="+status);
				}
			}
			
			
			
			
			if (resmap != null && resmap.size() > 0) {
				String cpid = info.getCpId();
				if (cpid == null || cpid.length() <= 2) {
					cpid = "mrjm";
				} else {
					cpid = cpid.substring(0,cpid.length()-2);
				}
				String key = ConstantDefine.MD5_KEY1 + cpid + ConstantDefine.MD5_KEY2;
				signparam.append("&key=").append(key);
				String sign = MD5.md5(signparam.toString(), "utf-8");
				
				resmap.put("fee", info.getFee()+"");
				resmap.put("orderid", info.getFfId());
				resmap.put("sign", sign);
				
				result = JSONObject.toJSONString(resmap);
			}
			
			logger.info("支付宝扫码sd50支付结果查询json:"+result);
			
		} catch (Exception e) {
			logger.error("支付宝扫码sd50支付结果查询失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
	private static String getOnlineSMS108(OrderReqInfo info) {
		// 1–支付成功； 2–待支付； 3 - 支付失败； 4 - 取消订单； 5 - 支付超时； 6 - 退款； 7 - 支付中； 9 - 其他错误
		String result = "{\"result\":\"-1\"}";
		try  {
			
			String keyword = info.getKeyword();
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			
			String stickerurl = tmp[0];
			String url = tmp[2];
			
			Map<String, String> resmap = new HashMap<String, String>();
			StringBuilder signparam = new StringBuilder();
			
			StringBuilder stickerparam = new StringBuilder();
			stickerparam.append("appid=").append(keyword);
			String stickerReqResult = HttpTool.sendPost(stickerurl, stickerparam.toString(), "5000");
			logger.info("微信H5支付sd49结果查询,sticker请求结果:"+stickerReqResult);
			if (stickerReqResult != null && stickerReqResult.length() > 0) {
				JSONObject stickerjson = JSONObject.parseObject(stickerReqResult);
				if ("0".equals(stickerjson.getString("result"))) {
					String sticker = stickerjson.getString("sticker");
					Map<String, String> datamap = new HashMap<String, String>();
					datamap.put("attch", info.getFfId());
					datamap.put("appid", keyword);
					String token = MD5.md5(sticker, "utf-8");
					String data = AESOperator.encrypt(JSONObject.toJSONString(datamap), sticker, sticker);
					
					StringBuilder param = new StringBuilder();
					param.append("data=").append(data)
					.append("&token=").append(token);
					
					String reqResult = HttpTool.sendPost(url, param.toString(), "5000");
					logger.info("微信H5支付sd49请求支付结果:"+reqResult);
					if (reqResult != null && reqResult.length() > 0) {
						signparam.append("fee=").append(info.getFee())
						.append("&orderid=").append(info.getFfId());
						reqResult = AESOperator.decrypt(reqResult, sticker, sticker);
						JSONObject jsonobj = JSONObject.parseObject(reqResult);
						if ("0".equals(jsonobj.getString("resultCode"))) {
							String values = jsonobj.getString("values");
							JSONArray jsonarr = JSONArray.parseArray(values);
							JSONObject jsonvalue = jsonarr.getJSONObject(0);
							if ("0".equals(jsonvalue.getString("result"))) {
								resmap.put("result", "1");
								signparam.append("&result=1");
							} else {
								resmap.put("result", "9");
								signparam.append("&result=9");
							}
						} else {
							resmap.put("result", "9");
							signparam.append("&result=9");
						}
					}
				}
				
			}
			
			if (resmap != null && resmap.size() > 0) {
				String cpid = info.getCpId();
				if (cpid == null || cpid.length() <= 2) {
					cpid = "mrjm";
				} else {
					cpid = cpid.substring(0,cpid.length()-2);
				}
				String key = ConstantDefine.MD5_KEY1 + cpid + ConstantDefine.MD5_KEY2;
				signparam.append("&key=").append(key);
				String sign = MD5.md5(signparam.toString(), "utf-8");
				
				resmap.put("fee", info.getFee()+"");
				resmap.put("orderid", info.getFfId());
				resmap.put("sign", sign);
				
				result = JSONObject.toJSONString(resmap);
			}
			
			logger.info("微信H5支付sd49支付结果查询json:"+result);
			
		} catch (Exception e) {
			logger.error("微信H5支付sd49支付结果查询失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
	private static String getOnlineSMS85(OrderReqInfo info) {
		// 1–支付成功； 2–待支付； 3 - 支付失败； 4 - 取消订单； 5 - 支付超时； 6 - 退款； 7 - 支付中； 9 - 其他错误
		String result = "{\"result\":\"-1\"}";
		try  {
			
			String url = info.getMatchRegex();
			String keyword = info.getKeyword();
			String[] tmp = keyword.split("#");
			
			String mchid = tmp[0];
			String appsecret = tmp[1];
			String appid = tmp[3];
			
			Map<String, String> parm = new HashMap<String, String>();
			parm.put("appid", appid);
			parm.put("mch_id", mchid);
			parm.put("nonce_str", PayUtil.getNonceStr());
			parm.put("out_trade_no", info.getFfId());
			parm.put("sign", PayUtil.getSign(parm, appsecret));
			
			String param = XmlUtil.xmlFormat(parm, false);
			String restxml = HttpTool.sendPost(url, param, "5000");
			logger.info("微信公H5支付sd31查询结果:"+restxml);
			Map<String, String> restmap = null;
			restmap = XmlUtil.xmlParse(restxml);
			
			Map<String, String> map = new HashMap<String, String>();
			if (CollectionUtil.isNotEmpty(restmap) && "SUCCESS".equals(restmap.get("return_code"))
					&& "SUCCESS".equals(restmap.get("result_code"))) {
				String ressign = restmap.get("sign");
				restmap.remove("sign");
				String ressigny = PayUtil.getSign(restmap, appsecret);
				if (ressigny.equals(ressign)) {
					String state = restmap.get("trade_state");
					String status = "9";
					if ("SUCCESS".equals(state)) {
						status = "1";
					} else if ("REFUND".equals(state)) {
						status = "6";
					} else if ("NOTPAY".equals(state)) {
						status = "2";
					} else if ("CLOSED".equals(state)) {
						status = "5";
					} else if ("REVOKED".equals(state)) {
						status = "4";
					} else if ("USERPAYING".equals(state)) {
						status = "7";
					} else if ("PAYERROR".equals(state)) {
						status = "3";
					}
					StringBuilder sig=new StringBuilder();
					sig.append("fee=").append(info.getFee())
					.append("&orderid=").append(info.getFfId())
					.append("&result=").append(status);
					map.put("orderid",info.getFfId());
					map.put("fee", info.getFee()+"");
					map.put("sign",MD5.md5(sig.toString(),"utf-8"));
					map.put("result", status);
					result = JSONObject.toJSONString(map);
				} else {
					result = "{\"result\":\"-1\"}";
					logger.error("微信支付sd31支付结果查询失败订单号："+info.getFfId());
				}
			}
			
			logger.info("微信H5支付sd31支付结果查询json:"+result);
			
		} catch (Exception e) {
			logger.error("微信H5支付sd31支付结果查询失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
	}
	
	private static String getOnlineSMS75(OrderReqInfo info) {
		// 1–待支付； 2–支付成功； 3 - 支付失败； 4 - 取消订单； 5 - 支付超时； 6 - 退款； 7 - 支付中； 9 - 其他错误
		String result = "{\"result\":\"-1\"}";
		try  {
			
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			
			String url = tmp[0];
			String sandbox = tmp[1];
			
			String param = String.format(Locale.CHINA,"{\"receipt-data\":\"" + info.getTraid()+"\"}");
			String reqResult = HttpTool.sendPost(url, param, "3000");
			logger.info("IOS支付sd27支付结果查询url=>"+url);
//			logger.info("IOS支付sd27支付结果查询param=>"+param.toString());
			logger.info("IOS支付sd27支付结果查询结果=>"+reqResult);
			Map<String, String> map = new HashMap<String, String>();
			if (reqResult == null || reqResult.length() <= 0 || reqResult.contains("\"status\":21007")) {
				reqResult = HttpTool.sendPost(sandbox, param, "3000");
				logger.info("测试IOS支付sd27支付结果查询url=>"+sandbox);
//				logger.info("测试IOS支付sd27支付结果查询param=>"+param.toString());
				logger.info("测试IOS支付sd27支付结果查询结果=>"+reqResult);
				if (reqResult != null && reqResult.length() > 0) {
					JSONObject jsonobj =JSONObject.parseObject(reqResult);
					String status = jsonobj.getString("status");
					if ("0".equals(status) || "21007".equals(status)) {
						map.put("result", "1");
					} else {
						map.put("result", "9");
					}
				}
			} else {
				JSONObject jsonobj =JSONObject.parseObject(reqResult);
				String status = jsonobj.getString("status");
				if ("0".equals(status)) {
					map.put("result", "1");
				} else {
					map.put("result", "9");
				}
			}
			
			if (map != null && map.size() > 0) {
				result = JSONObject.toJSONString(map);
			}
			
			logger.info("IOS支付sd27支付结果查询json:"+result);
			
		} catch (Exception e) {
			logger.error("IOS支付sd27支付结果查询失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}

	private static String getOnlineSMS73(OrderReqInfo info) {
		// 1–支付成功； 2–待支付； 3 - 支付失败； 4 - 取消订单； 5 - 支付超时； 6 - 退款； 7 - 支付中； 9 - 其他错误
		String result = "{\"result\":\"-1\"}";
		try  {
			
			String url = info.getMatchRegex();
			String keyword = info.getKeyword();
			String[] tmp = keyword.split("#");
			
			String appid = tmp[0];
			String mchid = tmp[1];
			String appsecret = tmp[2];
			
			
			
			Map<String, String> restmap = null;
			try {
				Map<String, String> parm = new HashMap<String, String>();
				parm.put("appid", appid);
				parm.put("mch_id", mchid);
//				parm.put("transaction_id", tradeid);
				parm.put("out_trade_no", info.getFfId());
				parm.put("nonce_str", PayUtil.getNonceStr());
				parm.put("sign", PayUtil.getSign(parm, appsecret));

				String param = XmlUtil.xmlFormat(parm, false);
				String restxml = HttpTool.sendPost(url, param, "8000");
				restmap = XmlUtil.xmlParse(restxml);
			} catch (Exception e) {
				logger.error("微信支付sd26支付结果查询异常!");
			}

			Map<String, String> map = new HashMap<String, String>();
			if (CollectionUtil.isNotEmpty(restmap) && "SUCCESS".equals(restmap.get("result_code"))) {
				// 订单查询成功 处理业务逻辑
				String state = restmap.get("trade_state");
				String status = "9";
				if ("SUCCESS".equals(state)) {
					status = "1";
				} else if ("REFUND".equals(state)) {
					status = "6";
				} else if ("NOTPAY".equals(state)) {
					status = "2";
				} else if ("CLOSED".equals(state)) {
					status = "5";
				} else if ("REVOKED".equals(state)) {
					status = "4";
				} else if ("USERPAYING".equals(state)) {
					status = "7";
				} else if ("PAYERROR".equals(state)) {
					status = "3";
				}
				map.put("result", status);
				result = JSONObject.toJSONString(map);
			} else {
				result = "{\"result\":\"-1\"}";
				logger.error("微信支付sd26支付结果查询失败订单号："+info.getFfId());
			}
			
			logger.info("微信支付sd26支付结果查询json:"+result);
			
		} catch (Exception e) {
			logger.error("微信支付sd26支付结果查询失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}
	
	private static String getOnlineSMS63(OrderReqInfo info) {
		// 1–支付成功； 2–待支付； 3 - 支付失败； 4 - 取消订单； 5 - 支付超时； 6 - 退款； 8 - 短时间查询次数过多；9 - 其他错误
		String result = "{\"result\":\"-1\"}";
		try  {
			
			/*String indexKey = CommonTool.createIndexKey(CommonTool.FASTSER_BASSTA, info.getFfId());
			int count = MemcachedResource.get(indexKey);
			if (count < 15) {
				count += 1;
				MemcachedResource.save(indexKey, count);*/
				
			String keyword = info.getKeyword();
			String[] tmp = keyword.split("#");
			String appid = tmp[0];
			String appsecret = tmp[1];
			
			String matchRegex = info.getMatchRegex();
			String[] tmp1 = matchRegex.split("#");
			String url = tmp1[0];
			
			String signtype = "MD5";
			
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
			logger.info("微信H5支付sd25支付结果查询url=>"+url);
			logger.info("微信H5支付sd25支付结果查询param=>"+param.toString());
			logger.info("微信H5支付sd25支付结果查询结果=>"+reqResult);
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
				logger.error("微信支付sd25支付结果查询失败订单号："+info.getFfId());
			}
			/*} else {
				logger.error("微信H5支付sd25订单：" + info.getFfId() + "，查询次数超过15次！");
				result = "{\"result\":\"-2\"}";
			}*/
			
			
		} catch (Exception e) {
			logger.error("微信H5支付sd25支付结果查询失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		
		return result;
		
	}

}
