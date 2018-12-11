package com.klw.fastfun.pay.view.app.http;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.juice.orange.game.log.LoggerFactory;
import com.klw.fastfun.pay.common.tool.Base64;
import com.klw.fastfun.pay.common.tool.HttpTool;
import com.klw.fastfun.pay.common.tool.xdzf.MD5;
import com.klw.fastfun.pay.view.app.other.qmdyuril.PaySignUtil;
import com.klw.fastfun.pay.view.app.other.util.RSASignature;
import com.klw.fastfun.pay.view.app.other.util.RSAUtil;
import com.klw.fastfun.pay.view.app.wxpay.utils.PayUtil;

public class WithdrawHelper {
	private static Logger logger = LoggerFactory.getLogger(WithdrawHelper.class);
	public static String getSMSCode(Map<String, String> reqMap) {
		String spId = (String) reqMap.get("spid");
		if ("sd85".equals(spId)) {
			// 金海哲代付
			return withdraw1(reqMap);
		} else if ("sd90".equals(spId)) {
			// 外放加冕QQ钱包
			return withdraw2(reqMap);
		} else if ("sd92".equals(spId)) {
			// 拙歌代付
			return withdraw3(reqMap);
		} else if ("sd95".equals(spId)) {
			// 全名点游
			return withdraw4(reqMap);
		} else if ("sd96".equals(spId)) {
			// MMSP
			return withdraw5(reqMap);
		} else if ("sd97".equals(spId)) {
			// MMSP
			return withdraw6(reqMap);
		} else {
			return "error";
		}

	}

	private static String withdraw6(Map<String, String> reqMap) {
		String sms = "error";
		try {
			String tmp[] = reqMap.get("keyWord").split("#");
			String version = tmp[0];// 代付类型tmp[0]  
			String agre_type = tmp[1];// D
			String pay_type = tmp[2];// 余额代付
			String inst_no = tmp[3];// 入网机构号
			String merch = reqMap.get("merNo");// 商户号
			String is_compay = tmp[4];// 对公对私0对公1对私
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String order_datetime = format.format(date);// 订单创建时间
			String amount = reqMap.get("amount");
			String merch_order_no = reqMap.get("orderNo");// 订单号
			String customer_name = reqMap.get("bankUserName");// 收款人姓名
			String customer_cert_type = tmp[5];// 证件类型
			String customer_cert_id = reqMap.get("idCard");// 身份证号
			String customer_phone = reqMap.get("phone");// 手机号
			String bank_no = reqMap.get("bankSettleno");// 联行号
			String bank_short_name = reqMap.get("bankShortName");
			String bank_name = reqMap.get("bankBranchName");// 支行名称
			String bank_card_no = reqMap.get("bankAccount");// 卡号
			String remark = reqMap.get("memo");// 备注
			Map<String, String> req = new HashMap<String, String>();
			req.put("version", version);
			req.put("agre_type", agre_type);
			req.put("pay_type", pay_type);
			req.put("inst_no", inst_no);
			req.put("merch_id", merch);
			req.put("is_compay", is_compay);
			req.put("order_datetime", order_datetime);
			req.put("amount", amount);
			req.put("merch_order_no", merch_order_no);
			req.put("customer_name", customer_name);
			req.put("customer_cert_type", customer_cert_type);
			req.put("customer_cert_id", customer_cert_id);
			req.put("customer_phone", customer_phone);
			req.put("bank_no", bank_no);
			req.put("bank_short_name", bank_short_name);
			req.put("bank_name", bank_name);
			req.put("bank_card_no", bank_card_no);
			req.put("remark", remark);
			String appsecret = reqMap.get("key");
			String sign = PayUtil.getSign(req, appsecret).toLowerCase();
			req.put("sign", sign);
			System.out.println(JSON.toJSONString(req));
			String reqResult = HttpTool.sendPost(reqMap.get("url"),
					JSON.toJSONString(req));
			if (reqResult != null && reqResult.length() > 0) {
				logger.info("迅捷聚合 sd97代付请求结果:" + reqResult);
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				Map<String, String> resMap = new HashMap<String, String>();
				for (Map.Entry<String, Object> entry : jsonobj.entrySet()) {
					String value = entry.getValue() + "";
					resMap.put(entry.getKey(), value);
				}
				String resultSign = resMap.get("sign");
				resMap.remove("sign");
				String mySign = PayUtil.getSign(resMap, appsecret)
						.toLowerCase();
				if (resultSign!=null&&mySign.equals(resultSign)) {
					String code = jsonobj.getString("retcode");
					if (!"00".equals(code)) {
						reqMap.put("operationStatus", "-1");
						return sms;
					} else {
						Map<String, String> successMap = new HashMap<String, String>();
						// 操作状态 0 新建未处理 1处理中 2处理成功 3处理失败4修改明细失败5没有收到返回值9异常状态
						reqMap.put("operationStatus", "1");
						successMap.put("codeMessage", "申请中");
						return JSON.toJSONString(successMap);
					}
				}
			}
		} catch (Exception e) {
			logger.error("sd97代付指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return sms;
	}

	private static String withdraw5(Map<String, String> reqMap) {
		String sms = "error";
		String tmp[] = reqMap.get("keyWord").split("#");
		String[] key = reqMap.get("key").split("#");
		String CMDID = tmp[0];// 代付类型
		String VERSION = tmp[1];// 版本
		String MERNO = reqMap.get("merNo");// 商户号
		String ORDERNO = reqMap.get("orderNo");// 订单号
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String REQTIME = format.format(date);
		int AMT = Integer.valueOf(reqMap.get("amount")) / 100;// 提现金额
		String BUSITYPE = tmp[2];// 业务类型
		String ACCNAME = reqMap.get("bankUserName");// 收款人姓名
		String ACCNO = reqMap.get("bankAccount");// 卡号
		String MEMO = reqMap.get("memo");// MEMO
		String ACCTYPE = tmp[3];// 借记卡
		String MOBILE = reqMap.get("phone");// 手机号
		String BANKNAME = reqMap.get("bankName");// 开户行全称
		String BANKPROV = reqMap.get("openProvince");// 开户行省份
		String BANKCITY = reqMap.get("openCity");// 开户行所在市
		String BANKSETTLENO = reqMap.get("bankSettleno");// 开户支行联行号
		String BANKBRANCHNAME = reqMap.get("bankBranchName");// 开户支行名称
		String IDCARD = reqMap.get("idCard");// 身份证号
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("CMDID", CMDID);
		paramMap.put("VERSION", VERSION);
		paramMap.put("MERNO", MERNO);
		paramMap.put("ORDERNO", ORDERNO);
		paramMap.put("REQTIME", REQTIME);
		paramMap.put("AMT", AMT);
		paramMap.put("BUSITYPE", BUSITYPE);
		paramMap.put("ACCNAME", ACCNAME);
		paramMap.put("ACCNO", ACCNO);
		paramMap.put("MEMO", MEMO);
		paramMap.put("ACCTYPE", ACCTYPE);
		paramMap.put("MOBILE", MOBILE);
		paramMap.put("BANKNAME", BANKNAME);
		paramMap.put("BANKSETTLENO", BANKSETTLENO);
		paramMap.put("BANKPROV", BANKPROV);
		paramMap.put("BANKCITY", BANKCITY);
		paramMap.put("BANKBRANCHNAME", BANKBRANCHNAME);
		paramMap.put("IDCARD", IDCARD);
		String publicKey = key[0];
		String ENCRYPT = null;
		try {
			ENCRYPT = RSAUtil.encryPublicData(JSON.toJSONString(paramMap),
					publicKey);
			// new
			// String(RSASignature.encryptByPublicKey(JSON.toJSONString(paramMap).getBytes("utf-8"),publicKey),"utf-8");
		} catch (Exception e) {
			throw new RuntimeException();
		}
		Map<String, Object> sendMap = new HashMap<String, Object>();
		sendMap.put("ENCRYPT", ENCRYPT);
		String privateKey = key[1];
		System.out.println(JSON.toJSONString(paramMap));
		String SIGN = RSASignature
				.sign(JSON.toJSONString(paramMap), privateKey);
		System.out.println(SIGN);
		sendMap.put("SIGN", SIGN);
		String reqResult = HttpTool.sendPost(reqMap.get("url"),
				JSON.toJSONString(sendMap));
		if (reqResult != null && reqResult.length() > 0) {
			logger.info("mmsp sd96请求结果:" + reqResult);
			JSONObject json = JSONObject.parseObject(reqResult);
			String code = json.getString("RETCODE");
			if (!"0000".equals(code)) {
				reqMap.put("operationStatus", "-1");
				return sms;
			} else {
				Map<String, String> successMap = new HashMap<String, String>();
				byte[] result = null;
				try {
					result = Base64.decode(json.getString("ENCRYPT"));
				} catch (UnsupportedEncodingException e1) {
					return sms;
				}
				JSONObject json1 = null;
				try {
					result = RSAUtil.decryptByPrivateKey(result, privateKey);
					json1 = JSONObject.parseObject(new String(result, "utf-8"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 操作状态 0 新建未处理 1处理中 2处理成功 3处理失败4修改明细失败5没有收到返回值9异常状态
				String PAYSTATE = json1.getString("PAYSTATE");
				if ("3".equals(PAYSTATE)) {
					reqMap.put("operationStatus", "-1");
					return sms;
				} else if ("2".equals(PAYSTATE)) {
					reqMap.put("operationStatus", "1");
					successMap.put("codeMessage", "申请中");

				} else if ("1".equals(PAYSTATE)) {
					reqMap.put("operationStatus", "2");
					successMap.put("codeMessage", "代付成功");
				}
				return JSON.toJSONString(successMap);
			}
		}
		return sms;
	}

	private static String withdraw4(Map<String, String> map) {
		// TODO Auto-generated method stub
		return null;
	}

	private static String withdraw3(Map<String, String> map) {
		// TODO Auto-generated method stub
		return null;
	}

	// 外放加冕QQ钱包
	private static String withdraw2(Map<String, String> reqMap) {
		// keyword:D0+01 代付类型 额度+卡账户类型
		String sms = "error";
		String mid = reqMap.get("merNo");// 商户号
		String orderNo = reqMap.get("orderNo");// 订单号
		int amount = Integer.valueOf(reqMap.get("amount")) / 100;// 提现金额
		String receiveName = reqMap.get("bankUserName");// 收款人姓名
		String openProvince = reqMap.get("openProvince");// 开户省
		String openCity = reqMap.get("openCity");// 开户市
		String bankCode = reqMap.get("bankType");// 银行类型
		String bankBranchName = reqMap.get("bankBranchName");// 支行名称
		String cardNo = reqMap.get("bankAccount");// 卡号
		String tmp[] = reqMap.get("keyWord").split("#");
		String type = tmp[0];// 代付类型 额度
		String cardAccountType = tmp[1];// 卡账户类型
		String noise = PayUtil.getNonceStr().substring(0, 10);
		// 生成随机字符串
		String key = reqMap.get("key");

		Map<String, String> map = new HashMap<String, String>();
		map.put("mid", mid);
		map.put("orderNo", orderNo);
		map.put("amount", String.valueOf(amount));
		map.put("receiveName", receiveName);
		map.put("openProvince", openProvince);
		map.put("openCity", openCity);
		map.put("bankCode", bankCode);
		map.put("bankBranchName", bankBranchName);
		map.put("cardNo", cardNo);
		map.put("type", type);
		map.put("cardAccountType", cardAccountType);
		map.put("noise", noise);
		String stringA = PaySignUtil.getParamStr2(map);
		String stringSignTemp = stringA + "&" + key;
		String sign = null;
		try {
			sign = MD5.md532(stringSignTemp).toUpperCase();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String reqResult = HttpTool.sendPost(reqMap.get("url"), stringSignTemp
				+ "&sign=" + sign);
		if (reqResult != null && reqResult.length() > 0) {
			logger.info("加冕sd90代付请求结果:" + reqResult);
			JSONObject json = JSONObject.parseObject(reqResult);

			Map<String, String> resultMap = new HashMap<String, String>();
			for (Map.Entry<String, Object> entry : json.entrySet()) {
				String value = entry.getValue() + "";
				resultMap.put(entry.getKey(), value);
			}
			String signValue = PaySignUtil.getParamStr2(resultMap);
			String code = json.getString("code");
			if ("FAIL".equals(code)) {
				// 退款，修改订单状态

			} else if ("SUCCESS".equals(code)) {
				// 处理中，修改订单状态
				String resultSign = json.getString("sign");
				// 操作状态 0 新建未处理 1处理中 2处理成功 3处理失败4修改明细失败5没有收到返回值9异常状态
				// 交易状态:1：受理中
				// 3：代付成功
				// 4：代付失败
				String mySign = null;
				try {
					mySign = MD5.md532(signValue + "&" + key).toUpperCase();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (mySign.equals(resultSign)) {
					Map<String, Object> successMap = new HashMap<String, Object>();
					String transState = json.getString("transState");
					if (transState == null) {
						// 退款，修改订单状态
						reqMap.put("operationStatus", "-1");
						return sms;
					} else if ("4".equals(transState)) {
						// 退款，修改订单状态
						reqMap.put("operationStatus", "-1");
						return sms;
					} else if ("3".equals(transState)) {
						// 代付成功
						reqMap.put("operationStatus", "2");
						successMap.put("codeMessage", "代付成功");
					} else if ("1".equals(transState)) {
						// 受理中
						reqMap.put("operationStatus", "1");
						successMap.put("codeMessage", "申请中");
						successMap.put("orderNo", orderNo);
					}
					successMap.put("amount", amount);
					successMap.put("code", "SUCCESS");
					successMap.put("orderNo", orderNo);
					return JSON.toJSONString(successMap);
				}
			}
		}
		System.out.println(reqResult);
		return sms;
	}

	private static String withdraw1(Map<String, String> map) {
		// TODO Auto-generated method stub
		return null;
	}

}
