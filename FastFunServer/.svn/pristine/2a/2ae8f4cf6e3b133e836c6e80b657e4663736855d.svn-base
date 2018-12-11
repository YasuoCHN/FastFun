/**
 * FastFunServer
 */
package com.klw.fastfun.pay.view.app.service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.klw.fastfun.pay.common.tool.DateTool;
import com.klw.fastfun.pay.view.app.ActionAware;

/**
 * @author klwplayer.com
 *
 *         2015年3月31日
 */
public class TestBack extends ActionAware {
	public static final SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALq3jFZhW8us7o4opJMpnZLSwv1SCdAL6/0ip8DB1u0EfuTHPy6XS49PlbR3sVDFJvRzNF2Fc7tylWYDoOTztyapdV8Y2LnLI9W8B/aWtn0fhAzLyr2IFLhHmP6VcGrCWY0DYnL12wTo5jgvbqCLH/Q3DBvekD6q13pmIbxw5uJDAgMBAAECgYEAg+PlgJrS8cMc21QANGeAA9dWnwPVJJ1XO/6/ylviCphTXh0UF0ANwpRv5gNqP+iThLbf9XOv9WeR+nZzr2YsJzB4wJQHjF1LrH1HFdFwhul19BSjEXkzcyCl0/tfeIsxvhdNYp7IhznlitGDAvOZEb4a1+e+L5Y0Uhx3kQLC8VkCQQDrLg3IK9HXKLaUg3BtSs1H3p5yDXs8ytNemfiVVwB2dyBG/q+RGjUpgwXbgEG8OeuGgYXcoYClJ+OSl/jWrrn1AkEAyz8qiNTw6C9s8e8z4MyCdTDiKXTCmA/du07AXC08NK3VOvw27N4b/5NNt7WyR7N/vQTxSx8d6P8uPRzvynDwVwJAf1+/GSYajcTANmmF77uuuPkqqa0BjShxGHCeAQxQ9NiKQ8lW/2jJWEVdW8f4UmCwXnYyMQ/LpCeZiuECZgvWLQJBAK76lL30xeq/WcX4L+urZe1Kxor2UMnlbvnhVM6Gyzx3JAqCNS88EVK5GMG+ldwQ9zpeVPZdtbxwZPiMPw1EqkUCQGph1+LoYCffaaCZw4aCPitO1t8Dfa2V8aBedhVqyXb74EQ7rSJIDcG2SDua/U4s+hEFQ+dOepC3gQ300xLWI80=";

	private static Map<String, String> provinceMap = new HashMap<String, String>();
	private static Map<String, String> provinceLTMap = new HashMap<String, String>();
	static {
		provinceMap.put("01", "北京");
		provinceMap.put("02", "天津");
		provinceMap.put("03", "河北");
		provinceMap.put("04", "山西");
		provinceMap.put("05", "内蒙古");
		provinceMap.put("06", "辽宁");
		provinceMap.put("07", "吉林");
		provinceMap.put("08", "黑龙江");
		provinceMap.put("09", "上海");
		provinceMap.put("10", "江苏");
		provinceMap.put("11", "浙江");
		provinceMap.put("12", "安徽");
		provinceMap.put("13", "福建");
		provinceMap.put("14", "江西");
		provinceMap.put("15", "山东");
		provinceMap.put("16", "河南");
		provinceMap.put("17", "湖北");
		provinceMap.put("18", "湖南");
		provinceMap.put("19", "广东");
		provinceMap.put("20", "广西");
		provinceMap.put("21", "海南");
		provinceMap.put("22", "四川");
		provinceMap.put("23", "贵州");
		provinceMap.put("24", "云南");
		provinceMap.put("25", "西藏");
		provinceMap.put("26", "陕西");
		provinceMap.put("27", "甘肃");
		provinceMap.put("28", "青海");
		provinceMap.put("29", "宁夏");
		provinceMap.put("30", "新疆");
		provinceMap.put("31", "重庆");

		provinceLTMap.put("10", "内蒙古");
		provinceLTMap.put("11", "北京");
		provinceLTMap.put("13", "天津");
		provinceLTMap.put("17", "山东");
		provinceLTMap.put("18", "河北");
		provinceLTMap.put("19", "山西");
		provinceLTMap.put("30", "安徽");
		provinceLTMap.put("31", "上海");
		provinceLTMap.put("34", "江苏");
		provinceLTMap.put("36", "浙江");
		provinceLTMap.put("38", "福建");
		provinceLTMap.put("50", "海南");
		provinceLTMap.put("51", "广东");
		provinceLTMap.put("59", "广西");
		provinceLTMap.put("70", "青海");
		provinceLTMap.put("71", "湖北");
		provinceLTMap.put("74", "湖南");
		provinceLTMap.put("75", "江西");
		provinceLTMap.put("76", "河南");
		provinceLTMap.put("79", "西藏");
		provinceLTMap.put("81", "四川");
		provinceLTMap.put("83", "重庆");
		provinceLTMap.put("84", "陕西");
		provinceLTMap.put("85", "贵州");
		provinceLTMap.put("86", "云南");
		provinceLTMap.put("87", "甘肃");
		provinceLTMap.put("88", "宁夏");
		provinceLTMap.put("89", "新疆");
		provinceLTMap.put("90", "吉林");
		provinceLTMap.put("91", "辽宁");
		provinceLTMap.put("97", "黑龙江");

	}

	private static Map<String, String> testImsiMap = new HashMap<String, String>();
	static {
		testImsiMap.put("460001261929455", "true");
		testImsiMap.put("460079005047370", "true");
		testImsiMap.put("460000383148352", "true");// 掌信拓维
		testImsiMap.put("460027141464450", "true");// 掌信拓维
		testImsiMap.put("460023107582358", "true");// 有乐通
		testImsiMap.put("460005180413361", "true");// 明日空间
		testImsiMap.put("460078027658863", "true");// 虚实
		testImsiMap.put("460020398641270", "true");// 酱油
		testImsiMap.put("460007082891181", "true");// 搜游互动
		testImsiMap.put("460016111035704", "true");// 考拉
		testImsiMap.put("460030495440302", "true");// 考拉
		testImsiMap.put("460017086211144", "true");// 艾阁
	}

	public static void main(String[] args) {
		
		System.out.println(DateTool.getTodayNum());

	}
	
	/*private static String formUpload(String urlStr, Map<String, String> textMap, 
            Map<String, String> fileMap) throws Exception {
        String res = "";
        HttpURLConnection conn = null;
        String boundary = "";
        
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", 
                    "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + boundary);
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            
            // text
            if (textMap != null) {
                StringBuffer strBuf = new StringBuffer();
                Iterator<Entry<String, String>> iter = textMap.entrySet().iterator();
                int i = 0;
                
                while (iter.hasNext()) {
                    Entry<String, String> entry = iter.next();
                    String inputName = entry.getKey();
                    String inputValue = entry.getValue();
                    
                    if (inputValue == null) {
                        continue;
                    }
                    
                    if (i == 0) {
                        strBuf.append("--").append(boundary).append("\r\n");
                        strBuf.append("Content-Disposition: form-data; name=\""
                                + inputName + "\"\r\n\r\n");
                        strBuf.append(inputValue);
                    } else {
                        strBuf.append("\r\n").append("--").append(boundary).append("\r\n");
                        strBuf.append("Content-Disposition: form-data; name=\""
                                + inputName + "\"\r\n\r\n");
                        strBuf.append(inputValue);
                    }

                    i++;
                }
                out.write(strBuf.toString().getBytes());
                System.out.println("1--------\r\n"+strBuf.toString());
            }

            // file
            if (fileMap != null) {
                Iterator<Entry<String, String>> iter = fileMap.entrySet().iterator();
                
                while (iter.hasNext()) {
                    Entry<String, String> entry = iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    
                    if (inputValue == null) {
                        continue;
                    }
                    
                    File file = new File(inputValue);
                    String filename = file.getName();
                    String contentType = new MimetypesFileTypeMap().getContentType(file);
                    if (contentType == null || contentType.equals("")) {
                        contentType = "application/octet-stream";
                    }

                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(boundary)
                            .append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\""
                            + inputName + "\"; filename=\"" + filename
                            + "\"\r\n");
                    strBuf.append("Content-Type: " + contentType + "\r\n\r\n");

                    out.write(strBuf.toString().getBytes());
                    System.out.println("2--------"+strBuf.toString());

                    DataInputStream in = new DataInputStream(new FileInputStream(file));
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    in.close();
                }
                
                StringBuffer strBuf = new StringBuffer();
                out.write(strBuf.toString().getBytes());
            }

            byte[] endData = ("\r\n--" + boundary + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 读取返回数据
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
            reader.close();
            reader = null;
        } catch (Exception e) {
            System.err.println("Send post request exception: " + e);
            throw e;
        } finally {
            if (conn != null) {
            	System.out.println();
            	System.out.println();
            	System.out.println("header");
            	for (int i = 0; ; i++) {
            		String header = conn.getHeaderField(i);
            		if (header == null) break;
            		System.out.println(conn.getHeaderFieldKey(i) + ":" + header);
            	}
            	Map<String, List<String>> map = conn.getRequestProperties();
            	System.out.println();
            	System.out.println();
            	System.out.println("getRequestProperties");
            	for(Entry<String, List<String>> entry : map.entrySet()) {
            		System.out.print("key= " + entry.getKey() + " and value= " );
            		List<String> list = entry.getValue();
            		for (String s : list) {
            			System.out.print(s+"||");
            		}
            		System.out.println();
            	}
            	
                conn.disconnect();
                conn = null;
            }
        }
        
        return res;
    }*/

	/**
	 * 订单请求接口
	 * <p>
	 * Market平台 ---> 开发者服务器 开发者服务器
	 * </p>
	 * 
	 * http://120.24.88.90/common/compay?imei=867838027501365&imsi=
	 * 460028871755583&iccid=89860018245401755837&fee=200&onlypay=szcp101
	 *//*
	public void compay(HttpRequest request, HttpResponse response)
			throws Exception {
		logger.info("请求header信息：" + request.allHeaders());

		String cpId = request.getParam("onlypay");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String fee = request.getParam("fee");
		String param = request.getParam("extra");

		String province = null;
		String iccid = request.getParam("iccid");
		if (iccid != null && iccid.length() > 10) {
			logger.info("用户访问iccid=" + iccid);
			province = provinceMap.get(iccid.substring(8, 10));
			logger.info("用户省份=" + province);
		}

		String ip = request.getParam("ip");
		ip = ip == null ? getRealIP(request) : ip;
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
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setIp(ip);
		reqInfo.setProvince(province);
		reqInfo.setIccid(iccid);
		reqInfo.setCity(city);
		reqInfo.setCpParam(param);

		String sms = "error";
		PayResultJson resultJson = new PayResultJson();
		String result = "error";
		int codeType = CommonTool.locateOperator(imsi);// 1移动 2联通 3电信
		boolean flag = true;
		String spId = "oo01";
		try {
			String ffId = null;
			CodeT ct = commonAction.getPayCodes(reqInfo);// 获取对应cpId打开的计费通道
			if (ct == null) {
				ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), "oo");
				logger.warn("CPID=" + cpId + " 省份=" + province + " 计费点=" + fee
						+ " 没有匹配到计费通道！");
				ct = new CodeT();
				ct.setFfId(ffId);
				reqInfo.setSpId(spId);
				reqInfo.setIsSuccess(9001);
				commonAction.addOrderReqInfo(ct, reqInfo);
				response.content(result).end();
				return;
			} else {
				ffId = ct.getFfId().substring(0, 10);
				String delayed = null;
				String spName = null;
				reqInfo.setSpId(spId);
				for (CodeInfo info : ct.getCodes()) {
					spId = info.getSpId();
					if ("yc01".equals(spId)) {
						ffId = CommonTool.genrateOrderNO(DateTool.getMonth(),
								"").substring(0, 10);
					} else {
						ffId = CommonTool.genrateOrderNO(DateTool.getMonth(),
								info.getSpFlag());
					}
					ct.setFfId(ffId);
					logger.info("通道号spid=" + spId);
					if ("dx".equals(info.getSpFlag()) && codeType != 3)
						continue;
					if ("lt".equals(info.getSpFlag()) && codeType != 2)
						continue;
					if (codeType != 1 && !"lt".equals(info.getSpFlag())
							&& !"dx".equals(info.getSpFlag())
							&& !"sd".equals(info.getSpFlag()))
						continue;

					delayed = info.getDelayed();
					reqInfo.setWait_time(delayed);

					reqInfo.setFfId(ffId);
					reqInfo.setUrl(info.getUrl());
					reqInfo.setUrlNo(info.getUrlNo());
					reqInfo.setReqMethod(info.getReqMethod());
					reqInfo.setKeyword(info.getKeyword());
					reqInfo.setMatchRegex(info.getMatchRegex());
					reqInfo.setFees(info.getFee());
					reqInfo.setSpId(spId);

					sms = TestCodeReqHelper.getSMSCode(reqInfo);

				}

				if ("error".equals(sms)) {
					reqInfo.setIsSuccess(9002);
					resultJson.setCode(ConstantDefine.ERROR_9002);
					resultJson.setCode_explain(URLEncoder.encode(
							ConstantDefine.ERROR_9002_MSG, "utf-8"));

				} else {
					result = sms;
				}

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

				commonAction.addOrderReqInfo(ct, reqInfo);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content(result).end();

	}

	*//**
	 * 订单通知接口
	 * 
	 * http://120.24.88.90/common/getBackDDOData
	 *//*
	public void getBackDDOData(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("getBackDDOData请求header信息：" + request.allHeaders());
			String content = request.body();
			logger.info("getBackDDOData同步信息：" + content);
			JSONObject myObj = JSONObject.parseObject(content);

			String code = myObj.getString("code");
			// String msg = myObj.getString("msg");
			// String orderId = myObj.getString("orderId");
			String mobile = myObj.getString("mobile");
			// String price = myObj.getString("price");
			// String chargeCode = myObj.getString("chargeCode");
			String transmissionData = myObj.getString("transmissionData");

			if (code != null && "0".equals(code))
				processOrder(transmissionData, mobile, true);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();

	}

	*//**
	 * 订单通知接口
	 * 
	 * http://120.24.88.90/common/reqdataback
	 *//*
	public void reqdataback(HttpRequest request, HttpResponse response)
			throws JuiceException {
		try {
			logger.info("请求header信息：" + request.allHeaders());

			String ffId = request.getParam("cpparam");
			String mobile = request.getParam("tel");

			if (ffId != null && ffId.length() > 0)
				processOrder(ffId, mobile, true);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
		response.content("ok").end();

	}

	*//**
	 * 处理订单
	 *//*
	private void processOrder(String ffId, String mobile, boolean isSuc) {
		try {
			int synStatus = ConstantDefine.SYN_STATUS_DEFAULT;
			if (!isSuc) {
				// commonAction.updateOrder(ffId, isSuc, synStatus);
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
			order.setMobile(mobile);

			if (order.getIsSyn() == ConstantDefine.SYN_TRUE) {
				String flag = testImsiMap.get(order.getImsi());
				if (!"true".equals(flag)) {
					String newCpId = cp.getCpId() + order.getSpId();
					int synRadio = cp.getSynRadio();
					logger.info("同步率synRadio：" + synRadio);
					CPParam cpp = CommonTool.getCPParam(newCpId, synRadio);

					AtomicInteger ati = cpp.getAti();
					boolean isSyn = CommonTool.isContain(cpp.getVc(),
							ati.incrementAndGet());
					if (isSyn) {
						if (cp.getUrl() != null && cp.getUrl().length() > 0) {
							// 同步
							String result = notityResult(cp, ffId, order);

							logger.info("给CP的同步结果：" + result);
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
					CommonTool.updateCPParam(newCpId, cpp);
				} else {
					String result = notityResult(cp, ffId, order);

					logger.info("给CP的同步结果：" + result);
					if (result.equals("success")) {
						synStatus = ConstantDefine.SYN_STATUS_TRUE;
					} else {
						synStatus = ConstantDefine.SYN_STATUS_FALSE;
					}
				}
			}

			commonAction.updateOrderData(mobile, ffId, isSuc, synStatus);
		} catch (Exception e) {
			logger.error(ExceptionTool.getExceptionMessage(e));
		}
	}

	private String notityResult(CPInfo cp, String ffId, OrderReqInfo order) {
		String result = "";
		String param = "";
		String cpId = cp.getCpId();
		if (cp.getBackMethod() == 1) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("orderid", ffId);
			map.put("imsi", order.getImsi());
			map.put("imei", order.getImei());
			map.put("param", order.getCpParam());
			map.put("mobile", order.getMobile());
			map.put("price", order.getFee() + "");
			param = JSON.toJSONString(map);
			if ("jy01".equals(cpId)) {
				result = HttpTool.sendKLWPost(cp.getUrl(), param, "utf-8", 500);
			} else {
				result = HttpTool.sendPost(cp.getUrl(), param);
			}
			// System.out.println(result);
		} else {
			StringBuilder msg = new StringBuilder();
			msg.append("imsi=").append(order.getImsi()).append("&imei=")
					.append(order.getImei()).append("&orderid=").append(ffId)
					.append("&param=").append(order.getCpParam())
					.append("&mobile=").append(order.getMobile())
					.append("&price=").append(order.getFee());
			param = msg.toString();
			result = HttpTool.sendGetSetTimeout(cp.getUrl(), param, "500");
		}
		return result;
	}*/

}
