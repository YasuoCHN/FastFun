/**
 * FastFunSystem
 */
package com.klw.fastfun.pay.common.action.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.juice.orange.game.cached.MemcachedResource;
import com.juice.orange.game.log.LoggerFactory;
import com.klw.fastfun.pay.common.action.CommonAction;
import com.klw.fastfun.pay.common.domain.BaseStationInfo;
import com.klw.fastfun.pay.common.domain.CPFeeInfo;
import com.klw.fastfun.pay.common.domain.CPInfo;
import com.klw.fastfun.pay.common.domain.CodeInfo;
import com.klw.fastfun.pay.common.domain.CodeOnlyInfo;
import com.klw.fastfun.pay.common.domain.CustomInfo;
import com.klw.fastfun.pay.common.domain.MMFeeInfo;
import com.klw.fastfun.pay.common.domain.MMPayInfo;
import com.klw.fastfun.pay.common.domain.MobileInfo;
import com.klw.fastfun.pay.common.domain.OrderReqInfo;
import com.klw.fastfun.pay.common.domain.PayCodeInfo;
import com.klw.fastfun.pay.common.domain.PayLimitInfo;
import com.klw.fastfun.pay.common.domain.PayReqInfo;
import com.klw.fastfun.pay.common.domain.QQInfo;
import com.klw.fastfun.pay.common.domain.ResSDKBusi;
import com.klw.fastfun.pay.common.domain.ResSDKFilter;
import com.klw.fastfun.pay.common.domain.ResSDKJson;
import com.klw.fastfun.pay.common.domain.ResSDKReport;
import com.klw.fastfun.pay.common.domain.ResSDKResult;
import com.klw.fastfun.pay.common.domain.ResSDKSms;
import com.klw.fastfun.pay.common.domain.ResSDKWap;
import com.klw.fastfun.pay.common.domain.SDKUpdateInfo;
import com.klw.fastfun.pay.common.domain.SmsFilter;
import com.klw.fastfun.pay.common.tool.CommonTool;
import com.klw.fastfun.pay.common.tool.ConstantDefine;
import com.klw.fastfun.pay.common.tool.DateTool;
import com.klw.fastfun.pay.common.tool.HttpTool;
import com.klw.fastfun.pay.common.tool.PriorityComparator;
import com.klw.fastfun.pay.common.transport.CodeT;
import com.klw.fastfun.pay.common.transport.OrderT;
import com.klw.fastfun.pay.data.Application;
import com.klw.fastfun.pay.data.utils.OnlineSMSTool;

/**
 * @author klwplayer.com
 *
 * 2015年3月30日
 */
public class CommonActionImpl extends Application implements CommonAction {
	private static Logger logger = LoggerFactory
			.getLogger(CommonActionImpl.class);
	
	public static final String ACTION_FORWARD = "action_forward_";
	
	/***
	 * 获取指令
	 */
	public void addklwOrderReqInfo(OrderReqInfo reqInfo) { 
		
		reqInfo.setIsSyn(ConstantDefine.SYN_TRUE);
		reqInfo.setSynStatus(ConstantDefine.SYN_STATUS_DEFAULT);
		orderReqAO().addklwOrderReqInfo(reqInfo);
		String indexKeyOrder = CommonTool.createIndexKey(CommonTool.FASTSER_ORDER, reqInfo.getFfId());
		MemcachedResource.save(indexKeyOrder, reqInfo);
	}
	
	/***
	 * 获取指令
	 */
	public void addxsOrderReqInfo(OrderReqInfo reqInfo) { 
		
		reqInfo.setIsSyn(ConstantDefine.SYN_TRUE);
		reqInfo.setSynStatus(ConstantDefine.SYN_STATUS_DEFAULT);
		orderReqAO().addOrderReqInfo(reqInfo);
		String indexKeyOrder = CommonTool.createIndexKey(CommonTool.FASTSER_ORDER, reqInfo.getFfId());
		MemcachedResource.save(indexKeyOrder, reqInfo);
	}
	/***
	 * 添加订单(日期)
	 */
	public void addOrderReqInfoDate(CodeT ct, OrderReqInfo reqInfo) { 
//		CodeInfo code = codes.get(0);
//		String ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), "oo");
		reqInfo.setFfId(ct.getFfId());
		
		reqInfo.setIsSyn(ConstantDefine.SYN_TRUE);
//		reqInfo.setSynStatus(ConstantDefine.SYN_STATUS_DEFAULT);
		orderReqAO().addOrderReqInfoDate(reqInfo);
//		String indexKeyOrder = CommonTool.createIndexKey(CommonTool.FASTSER_ORDER, reqInfo.getFfId());
//		MemcachedResource.save(indexKeyOrder, reqInfo);
	}
	/***
	 * 添加订单
	 */
	public void addOrderReqInfo(CodeT ct, OrderReqInfo reqInfo) { 
//		CodeInfo code = codes.get(0);
//		String ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), "oo");
		reqInfo.setFfId(ct.getFfId());
		
		reqInfo.setIsSyn(ConstantDefine.SYN_TRUE);
//		reqInfo.setSynStatus(ConstantDefine.SYN_STATUS_DEFAULT);
		orderReqAO().addOrderReqInfo(reqInfo);
//		String indexKeyOrder = CommonTool.createIndexKey(CommonTool.FASTSER_ORDER, reqInfo.getFfId());
//		MemcachedResource.save(indexKeyOrder, reqInfo);
	}
	
	/***
	 * 添加订单
	 */
	public void addMobile12306(MobileInfo info) {
		commonAO().addMobile12306(info);
	}
	
	public void addOtherOrderReqInfo(OrderReqInfo reqInfo) {
		reqInfo.setIsSyn(ConstantDefine.SYN_TRUE);
		orderReqAO().addOrderReqInfo(reqInfo);
		String indexKeyOrder = CommonTool.createIndexKey(CommonTool.FASTSER_ORDER, reqInfo.getFfId());
		MemcachedResource.save(indexKeyOrder, reqInfo);
	}
	
	public void addPollOrderReqInfo(OrderReqInfo reqInfo) {
		reqInfo.setIsSyn(ConstantDefine.SYN_TRUE);
		orderReqAO().addOrderReqInfo(reqInfo);
		String indexKeyOrder = CommonTool.createIndexKey(CommonTool.FASTSER_ORDER, reqInfo.getFfId());
		MemcachedResource.save(indexKeyOrder, reqInfo);
		
		String indexKeytj = CommonTool.createIndexKey(CommonTool.FASTSER_POLLKEY, 
				reqInfo.getSpId()+reqInfo.getCpId());
		String indexKey = MemcachedResource.get(indexKeytj);
		if (indexKey != null && indexKey.length() > 0) {
			List<CodeInfo> codes = MemcachedResource.get(indexKey);
			boolean flag = false;
			if (codes != null && codes.size() > 0) {
				for (CodeInfo code : codes) {
					if (code.getSpId().equals(reqInfo.getSpId())) {
						code.setPriority(code.getPriority() + 1);
						flag = true;
						break;
					}
				}
				if (flag) {
					MemcachedResource.save(indexKey, codes);
				} else {
					CodeInfo newcode = new CodeInfo();
					newcode.setSpId(reqInfo.getSpId());
					newcode.setPriority(1);
					codes.add(newcode);
					MemcachedResource.save(indexKey, codes);
				}
			}
		}
	}
	
	/***
	 * 添加咪咕订单
	 */
	public void addMiGuOrderReqInfo(OrderReqInfo reqInfo) {
		reqInfo.setIsSyn(ConstantDefine.SYN_TRUE);
		orderReqAO().addOrderReqInfo(reqInfo);
		String indexKeyOrder = CommonTool.createIndexKey(CommonTool.FASTSER_MGORDER, reqInfo.getImsi());
		MemcachedResource.save(indexKeyOrder, reqInfo);
		
		String indexKeytj = CommonTool.createIndexKey(CommonTool.FASTSER_MGKEY, 
				reqInfo.getSpId()+reqInfo.getCpId());
		String indexKey = MemcachedResource.get(indexKeytj);
		if (indexKey != null && indexKey.length() > 0) {
			List<CodeInfo> codes = MemcachedResource.get(indexKey);
			boolean flag = false;
			if (codes != null && codes.size() > 0) {
				for (CodeInfo code : codes) {
					if (code.getSpId().equals(reqInfo.getSpId())) {
						code.setPriority(code.getPriority() + 1);
						flag = true;
						break;
					}
				}
				if (flag) {
					MemcachedResource.save(indexKey, codes);
				} else {
					CodeInfo newcode = new CodeInfo();
					newcode.setSpId(reqInfo.getSpId());
					newcode.setPriority(1);
					codes.add(newcode);
					MemcachedResource.save(indexKey, codes);
				}
			}
		}
	}
	
	/***
	 * 获取指令
	 */
	public void addOrderReqInfoAll(OrderReqInfo reqInfo) { 
		orderReqAO().addOrderReqInfoAll(reqInfo);
	}
	
	public void addOrderReqInfo1(CodeT ct, String cpId, String ip, String province, String fee, 
			String imei, String imsi, String iccid, String mobile,String cpparam, String pmodel, 
			String osversion, String nwtype, String ctech) { 
//		CodeInfo code = codes.get(0);
		OrderReqInfo reqInfo = new OrderReqInfo();
//		String ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), "oo");
		reqInfo.setFfId(ct.getFfId());
		reqInfo.setCpId(cpId);
		reqInfo.setImei(imei);
		reqInfo.setImsi(imsi);
		reqInfo.setIccid(iccid);
		reqInfo.setMobile(mobile);
		reqInfo.setSpId(ct.getSpId());
		reqInfo.setPmodel(pmodel);
		reqInfo.setOsversion(osversion);
		reqInfo.setNwtype(nwtype);
		reqInfo.setCtech(ctech);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setIp(ip);
		reqInfo.setProvince(province);
		reqInfo.setCpParam(cpparam);
		
		reqInfo.setIsSyn(ConstantDefine.SYN_TRUE);
		reqInfo.setSynStatus(ConstantDefine.SYN_STATUS_DEFAULT);
		orderReqAO().addOrderReqInfo1(reqInfo);
		
	}
	
	/***
	 * 获取指令
	 */
	public CodeT getPayCode(OrderReqInfo reqInfo) { 
		
		CodeT ct = new CodeT();
		// 第一步：查找CP信息
		CPInfo cpInfo = cpInfoAO().queryCPInfo(reqInfo.getCpId());
		if (cpInfo == null) {//cpid非我方分配或关闭了该cpid被禁止计费
			ct.setMsgNum(ConstantDefine.CODE_MATCH_1_NUM);
			ct.setMsg(ConstantDefine.CODE_MATCH_1_MSG);
		} else {
			
			//依据ip屏蔽具体地市或省份
			String city = reqInfo.getCity();
			if (city != null) {
				String cityHides = cpInfo.getCityHide();
				if (cpInfo.getCityType() == 1 && cityHides !=null) {
					String[] cityList = cityHides.split("#");
					for (int i=0; i<cityList.length; i++) {
						String cityHide = cityList[i];
						if (city.contains(cityHide)) return null;
					}
				}
			}
			
			// 第二步：根据CP指定的SP匹配最佳通道
			CodeInfo code = codeHelperAO().collectCodeInfo(cpInfo.getLocalSpId(), reqInfo.getProvince());
			List<CodeInfo> codes = new ArrayList<CodeInfo>();
			codes.add(code);
			String ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), codes.get(0).getSpFlag());
			
			ct.setFfId(ffId);
			ct.setCpName(cpInfo.getName());
			ct.setCodes(codes);
		}
		
		return ct;
	}
	
	/***
	 * 获取sdk指令
	 */
	public CodeT getSdkPayCodes(OrderReqInfo reqInfo) { 
		
		CodeT ct = new CodeT();
		//0表示成功
		ct.setStatus("-1");
		
		// 第一步：查找CP信息
		CPInfo cpInfo = cpInfoAO().queryCPInfo(reqInfo.getCpId());
		if (cpInfo == null) {
			ct.setStatus("1");
			ct.setMsg("cpid："+reqInfo.getCpId()+",没有配置或打开");
			return ct;
		}
		
		String city = reqInfo.getCity();
		if (city != null) {
			String cityHides = cpInfo.getCityHide();
			if (cpInfo.getCityType() == 1 && cityHides !=null) {
				String[] cityList = cityHides.split("#");
				for (int i=0; i<cityList.length; i++) {
					String cityHide = cityList[i];
					if (city.contains(cityHide)) {
						ct.setStatus("2");
						ct.setMsg("cpid："+reqInfo.getCpId()+"," + city + "属于屏蔽地市");
						return ct;
					}
				}
			}
		}
		
		String orderid = reqInfo.getOrderId();
		String spid = reqInfo.getSpId();
		String localspid = cpInfo.getLocalSpId();
		
		List<CodeInfo> codes = codeHelperAO().collectSdkAllCode(localspid, reqInfo.getProvince());
		if (codes.isEmpty()) {
			ct.setStatus("5");
			ct.setMsg("localspid："+localspid+"中,没有找到能使用的计费通道");
			return ct;
		}
		if (codes.size() > 1) Collections.sort(codes, new PriorityComparator<CodeInfo>());
		String ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), "oo");
		
		if (orderid != null && orderid.length() > 0) {
			if (1 == reqInfo.getIsSuccess()) {
				ct.setStatus("4");
				ct.setMsg("ffid："+orderid+",订单已经成功计费");
				return ct;
			} else {
				List<CodeInfo> ncodes = new ArrayList<CodeInfo>();
				boolean flag = false;
				for (CodeInfo codeinfo : codes) {//循环请求时已经计费过的通道不再获取指令
					if (flag) {
						ncodes.add(codeinfo);
					}
					if (codeinfo.getSpId().equals(spid)) {
						flag = true;
					}
				}
				if (ncodes.size() > 0) {
					ct.setStatus("0");
					ct.setFfId(reqInfo.getFfId());
					ct.setCpName(cpInfo.getName());
					ct.setCodes(ncodes);
					ct.setCp(cpInfo);
					ct.setMsg("成功");
				} else {
					ct.setStatus("3");
					ct.setMsg("cpid："+reqInfo.getCpId()+",没有比spid：" + spid +",更低优先级通道");
				}
			}
		} else {
			ct.setStatus("0");
			ct.setFfId(ffId);
			ct.setCpName(cpInfo.getName());
			ct.setCodes(codes);
			ct.setCp(cpInfo);
			ct.setMsg("成功");
		}
		
		return ct;
	}
	
	/***
	 * 获取指令
	 */
	public CodeT getPayCodes(OrderReqInfo reqInfo) { 
		
		String key = reqInfo.getCpId()+reqInfo.getFee()+reqInfo.getProvince()+reqInfo.getCity();
		String indexKeyCt = CommonTool.createIndexKey(CommonTool.FASTSER_PREFIX, key);
		CodeT ct = MemcachedResource.get(indexKeyCt);
		if (ct != null) return ct;
		
		// 第一步：查找CP信息
		CPInfo cpInfo = cpInfoAO().queryCPInfo(reqInfo.getCpId());
		if (cpInfo == null) return null;
		//将现有省份与屏蔽省份对比，如果存在于屏蔽省份中则返回null
		String city = reqInfo.getCity();
		if (city != null) {
			String cityHides = cpInfo.getCityHide();
			if (cpInfo.getCityType() == 1 && cityHides !=null) {
				String[] cityList = cityHides.split("#");
				for (int i=0; i<cityList.length; i++) {
					String cityHide = cityList[i];
					if (city.contains(cityHide)) return null;
				}
			}
		}
		
		// 第二步：根据CP指定的SP匹配最佳通道
		List<CodeInfo> codes = codeHelperAO().collectAllCode(cpInfo.getLocalSpId(), reqInfo.getProvince(), 
				reqInfo.getFee()+"");
		//通道为空返回null
		if (codes.isEmpty()) return null;
		//通道不为空将通道排序，保存至缓存
		if (codes.size() > 1) Collections.sort(codes, new PriorityComparator<CodeInfo>());
		String ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), "oo");
		ct = new CodeT();
		ct.setFfId(ffId);
		ct.setCpName(cpInfo.getName());
		ct.setCodes(codes);
		ct.setCp(cpInfo);
		
		MemcachedResource.save(indexKeyCt, ct);
		return ct;
	}
	
	/***
	 * 获取指令
	 */
	public CodeT getMiGuPayCodes(OrderReqInfo reqInfo) { 
		
//		String key = reqInfo.getCpId()+reqInfo.getFee()+reqInfo.getProvince()+reqInfo.getCity();
//		String indexKeyCt = CommonTool.createIndexKey(CommonTool.FASTSER_PREFIX, key);
//		CodeT ct = MemcachedResource.get(indexKeyCt);
//		if (ct != null) return ct;
		
		CodeT ct = new CodeT();
		// 第一步：查找CP信息
		CPInfo cpInfo = cpInfoAO().queryCPInfo(reqInfo.getCpId());
		if (cpInfo == null) return null;
		
		String city = reqInfo.getCity();
		if (city != null) {
			String cityHides = cpInfo.getCityHide();
			if (cpInfo.getCityType() == 1 && cityHides !=null) {
				String[] cityList = cityHides.split("#");
				for (int i=0; i<cityList.length; i++) {
					String cityHide = cityList[i];
					if (city.contains(cityHide)) return null;
				}
			}
		}
		
		// 第二步：根据CP指定的SP匹配最佳通道
		List<CodeInfo> codes = codeHelperAO().collectAllCode(cpInfo.getLocalSpId(), reqInfo.getProvince(), 
				reqInfo.getFee()+"");
		
		if (codes.isEmpty()) return null;
		if (codes.size() > 1) {
			OrderReqInfo mginfo = orderReqAO().queryMiGuOrder(reqInfo);//imsi当天最后一次取到的通道
			if (mginfo != null) {//再次取指令优先取上次取到的通道
				for (CodeInfo code : codes) {
					if (code.getSpId().equals(mginfo.getSpId())) {
						code.setPriority(code.getPriority() + ConstantDefine.CODE_PRIORITY);
						break;
					}
				}
			} else {//该imsi当天第一次取指令
				String querycon = "";
				int len = codes.size();
				for (int i=0; i<len; i++) {//cpid对应打开的通道
					if (i==0) {
						querycon = codes.get(i).getSpId();
					}  else {
						querycon = querycon + "#" + codes.get(i).getSpId();
					}
				}
				//获取cpid对应打开通道和通道当天取指令次数
				List<CodeInfo> newcodes = orderReqAO().queryMiGuOrderList(querycon, reqInfo);
				for (CodeInfo code : newcodes) {
					logger.info("测试spid:"+code.getSpId()+",priority:"+code.getPriority());
				}
				if (newcodes != null) {
					int newlen = newcodes.size();
					if (newlen == len) {//所有通道都获取过指令
						Collections.sort(newcodes, new PriorityComparator<CodeInfo>());
						for (CodeInfo code : codes) {//优先从获取指令次数最少的通道获取
							if (code.getSpId().equals(newcodes.get(newlen-1).getSpId())) {
								code.setPriority(code.getPriority() + ConstantDefine.CODE_PRIORITY);
								break;
							}
						}
					} else {
						boolean flag = false;
						for (CodeInfo code : codes) {//优先从没有获取过指令的通道获取
							for (CodeInfo newcode : newcodes) {
								if (code.getSpId().equals(newcode.getSpId())) {
									flag = true;
								}
							}
							if (!flag) {
								code.setPriority(code.getPriority() + ConstantDefine.CODE_PRIORITY);
								break;
							} else {
								flag = false;
							}
						}
					}
				}
			}
			
			Collections.sort(codes, new PriorityComparator<CodeInfo>());
		}
		String ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), "oo");
		
		ct = new CodeT();
		ct.setFfId(ffId);
		ct.setCpName(cpInfo.getName());
		ct.setCodes(codes);
		ct.setCp(cpInfo);
		
//		MemcachedResource.save(indexKeyCt, ct);
		return ct;
	}
	
	/***
	 * 获取指令
	 */
	public CodeT getklwPayCodes(OrderReqInfo reqInfo) { 
		
		CodeT ct = new CodeT();
		// 第一步：查找CP信息
		CPInfo cpInfo = cpInfoAO().queryCPInfo(reqInfo.getCpId());
		if (cpInfo == null) {//cpid非我方分配或关闭了该cpid被禁止计费
			ct.setMsgNum(ConstantDefine.CODE_MATCH_1_NUM);
			ct.setMsg(ConstantDefine.CODE_MATCH_1_MSG);
		} else {
			
			//依据ip屏蔽具体地市或省份
			String city = reqInfo.getCity();
			if (city != null) {
				String cityHides = cpInfo.getCityHide();
				if (cpInfo.getCityType() == 1 && cityHides !=null) {
					String[] cityList = cityHides.split("#");
					for (int i=0; i<cityList.length; i++) {
						String cityHide = cityList[i];
						if (city.contains(cityHide)) return null;
					}
				}
			}
			
			int isMix = cpInfo.getIsMix();
			if (isMix == 1) {//允许拼接代码
				
				// 第二步：根据CP指定的SP匹配对应省份能计费的通道
				List<CodeInfo> codes = codeHelperAO().collectklwAllCode(cpInfo.getLocalSpId(), reqInfo.getProvince());
				
				if (codes.isEmpty()) {
					ct.setMsgNum(ConstantDefine.CODE_MATCH_2_NUM);
					ct.setMsg(ConstantDefine.CODE_MATCH_2_MSG);
					return ct;
				}
				if (codes.size() > 1) Collections.sort(codes, new PriorityComparator<CodeInfo>());
//				StringBuilder tffId = CommonTool.getklwOrderNO(DateTool.getMonth(), "zh");
				
//				ct.setFfId(tffId.toString());
				ct.setCpName(cpInfo.getName());
				int totalFee = reqInfo.getFee();
				List<CodeInfo> newcodes = new ArrayList<CodeInfo>();
				for (CodeInfo code : codes) {
					if (totalFee <= 0) break;
					int payNum = 0;
					int maxFee = getMaxFee(code.getFee(),totalFee);
					if (maxFee == 0) continue;
					
					int allowPay = getAllowPayFee(reqInfo);
					if (totalFee < allowPay) continue;
//					payNum = allowPay/maxFee;
					payNum = 1;
					if (payNum > 0) {
						code.setPayNum(payNum);
						newcodes.add(code);
						totalFee = totalFee - maxFee*payNum;
					}
				}
				ct.setCodes(newcodes);
				
			} else {//不允许拼接代码
				// 第二步：根据CP指定的SP匹配最佳通道
				List<CodeInfo> codes = codeHelperAO().collectAllCode(cpInfo.getLocalSpId(), reqInfo.getProvince(), 
						reqInfo.getFee()+"");
				
				if (codes.isEmpty()) return null;
				if (codes.size() > 1) Collections.sort(codes, new PriorityComparator<CodeInfo>());
				List<CodeInfo> newcodes = new ArrayList<CodeInfo>();
				newcodes.add(codes.get(0));
//				String ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), codes.get(0).getSpFlag());
				
//				ct.setFfId(ffId);
				ct.setCpName(cpInfo.getName());
				ct.setCodes(newcodes);
			}
			
		}
		
		return ct;
	}
	
	private int getMaxFee(String fees, int totalFee) {
		String[] feeList = fees.split("#");
		int fee = 0;
		int temp = 0;
		for (int i=feeList.length-1; i>=0; i--) {
			if (feeList[i].length()>0) {
				temp = CommonTool.convertInt(feeList[i]);
				if (totalFee > temp) {
					fee = temp;
					break;
				}
			}
		}
		return fee;
	}
	
	/***
	 * 获取基站信息
	 */
	public BaseStationInfo getBaseStation(OrderReqInfo reqInfo) { 
		
		// 第一步：查找CP信息
		CPInfo cpInfo = cpInfoAO().queryCPInfo(reqInfo.getCpId());
		if (cpInfo == null) return null;
		if (cpInfo.getCityType() != 1) return null;
		
		String lac = reqInfo.getBscLac();
		String cid = reqInfo.getBscCid();
		if (lac == null || lac.length() <= 0 || cid == null || cid.length() <= 0) {
			logger.error("基站信息获取错误lac=" + lac + "  cid=" + cid);
			return new BaseStationInfo();
		}
		
		BaseStationInfo bs = commonAO().getBaseStation(reqInfo);
		
		return bs;
	}
	
	/***
	 * 获取第三方指令指令
	 */
	public CodeT getPointPayCodes(OrderReqInfo reqInfo) { 
		
		String key = reqInfo.getCpId()+reqInfo.getSpId()+reqInfo.getProvince()+reqInfo.getCity();
		String indexKeyCt = CommonTool.createIndexKey(CommonTool.FASTSER_PREFIX, key);
		CodeT ct = MemcachedResource.get(indexKeyCt);
		if (ct != null) return ct;
		
		// 第一步：查找CP信息
		CPInfo cpInfo = cpInfoAO().queryCPInfo(reqInfo.getCpId());
		if (cpInfo == null) return null;
		
		// 第二步：根据CP指定的SP匹配最佳通道
		List<CodeInfo> codes = codeHelperAO().collectPointCode(reqInfo, cpInfo);
		
		if (codes.isEmpty()) return null;
		if (codes.size() > 1) Collections.sort(codes, new PriorityComparator<CodeInfo>());
		
		ct = new CodeT();
		ct.setCpName(cpInfo.getName());
		ct.setCodes(codes);
		
		MemcachedResource.save(indexKeyCt, ct);
		return ct;
	}
	
	public MobileInfo query12306commit(String mobile) {
		MobileInfo info = commonAO().query12306commit(mobile);
		return info;
	}
	
	/***
	 * 获取第三方指令指令
	 */
	public CodeT getOtherPayCodes(OrderReqInfo reqInfo) { 
		
		String key = reqInfo.getCpId()+reqInfo.getSpId()+reqInfo.getProvince()+reqInfo.getCity()+reqInfo.getFee();
		String indexKeyCt = CommonTool.createIndexKey(CommonTool.FASTSER_PREFIX, key);
		CodeT ct = MemcachedResource.get(indexKeyCt);
		if (ct != null) return ct;
		
		// 第一步：查找CP信息
		CPInfo cpInfo = cpInfoAO().queryCPInfo(reqInfo.getCpId());
		if (cpInfo == null) return null;
		
		// 第二步：根据CP指定的SP匹配最佳通道
		List<CodeInfo> codes = codeHelperAO().collectOtherCode(reqInfo, cpInfo);
		
		if (codes.isEmpty()) return null;
		if (codes.size() > 1) Collections.sort(codes, new PriorityComparator<CodeInfo>());
		
		ct = new CodeT();
		ct.setCpName(cpInfo.getName());
		ct.setCodes(codes);
		ct.setCp(cpInfo);
		
		MemcachedResource.save(indexKeyCt, ct);
		return ct;
	}
	public static void main(String[] args) {
		int a= 0/43;
		System.out.println(a);
	}
	/***
	 * 固定优先级轮询 获取第三方指令指令
	 */
	public CodeT getPollOtherPayCodes(OrderReqInfo reqInfo) { 
		
		// 第一步：查找CP信息
		CPInfo cpInfo = cpInfoAO().queryCPInfo(reqInfo.getCpId());
		if (cpInfo == null) return null;
		
		// 第二步：根据CP指定的SP匹配最佳通道
		List<CodeInfo> codes = codeHelperAO().collectOtherCode(reqInfo, cpInfo);
		
		if (codes.isEmpty()) return null;
		if (codes.size() > 1) {
			Collections.sort(codes, new PriorityComparator<CodeInfo>());
			
			Random random = new Random();
			int ran = random.nextInt(100);
			
			int totalPriority = 0;
			for (CodeInfo code : codes) {
				totalPriority += code.getPriority();
			}
			if (totalPriority > 0) {
				for (int i=0; i<codes.size(); i++) {
					int priority = codes.get(i).getPriority();
					int percentNum = (priority * 100)/totalPriority;
					if (ran <= percentNum) {
						if (i != 0) {
							CommonTool.swap2(codes, i, 0);
						}
						break;
					} else {
						ran = ran - percentNum;
					}
				}
			}
		}
		
		CodeT ct = new CodeT();
		ct.setCpName(cpInfo.getName());
		ct.setCodes(codes);
		ct.setCp(cpInfo);
		
		return ct;
	}
	
	/***
	 * 轮询获取第三方指令指令
	 */
	public CodeT pollOtherPayCodes(OrderReqInfo reqInfo) { 
		
		// 第一步：查找CP信息
		CPInfo cpInfo = cpInfoAO().queryCPInfo(reqInfo.getCpId());
		if (cpInfo == null) return null;
		
		// 第二步：根据CP指定的SP匹配最佳通道
		List<CodeInfo> codes = codeHelperAO().collectOtherCode(reqInfo, cpInfo);
		
		if (codes.isEmpty()) return null;
		if (codes.size() > 1) {
			String querycon = "";
			int len = codes.size();
			for (int i=0; i<len; i++) {//cpid对应打开的通道
				if (i==0) {
					querycon = codes.get(i).getSpId();
				}  else {
					querycon = querycon + "#" + codes.get(i).getSpId();
				}
			}
			//获取cpid对应打开通道和通道当天取指令次数
			List<CodeInfo> newcodes = orderReqAO().queryPollOrderList(querycon, reqInfo);
			for (CodeInfo code : newcodes) {
				logger.info("第三方spid:"+code.getSpId()+",reqNum:"+code.getPriority());
			}
			if (newcodes != null) {
				int newlen = newcodes.size();
				if (newlen == len) {//所有通道都获取过指令
					Collections.sort(newcodes, new PriorityComparator<CodeInfo>());
					Collections.sort(codes, new PriorityComparator<CodeInfo>());
					
					int upPriority = codes.get(0).getPriority();
					int lowPriority = 1;
					int upReqNum = 1;
					int lowReqNum = 1;
					int share = 1;
					int increasePriority = 0;
					for (int i=0; i<len; i++) {//蒋各通道请求数赋值
						for (CodeInfo newcode : newcodes) {
							if (newcode.getSpId().equals(codes.get(i).getSpId())) {
								codes.get(i).setPayNum(newcode.getPriority());
							}
						}
						if (i==0) {
							upPriority = codes.get(i).getPriority();
							upReqNum = codes.get(i).getPayNum();
						} else {
							lowPriority = codes.get(i).getPriority();
							lowReqNum = codes.get(i).getPayNum();
							
							share = (upReqNum+lowReqNum)/(upPriority+lowPriority);
							if (share > 0) {
								increasePriority = (upReqNum - lowReqNum)/share;
								codes.get(i).setIncreasePriority(increasePriority);
							}
						}
					}
					
					
				} else {
					boolean flag = false;
					for (CodeInfo code : codes) {//优先从没有获取过指令的通道获取
						for (CodeInfo newcode : newcodes) {
							if (code.getSpId().equals(newcode.getSpId())) {
								flag = true;
							}
						}
						if (!flag) {
							code.setIncreasePriority(ConstantDefine.CODE_PRIORITY);
							break;
						} else {
							flag = false;
						}
					}
				}
			}
			
			for (CodeInfo code : codes) {
				code.setPriority(code.getIncreasePriority() + code.getPriority());
			}
			Collections.sort(codes, new PriorityComparator<CodeInfo>());
		}
		
		CodeT ct = new CodeT();
		ct.setCpName(cpInfo.getName());
		ct.setCodes(codes);
		ct.setCp(cpInfo);
		
		return ct;
	}
	
	/***
	 * 获取指令
	 */
	public CodeT getCodes(String cpId, String ip, String province, String fee, 
			String imei, String imsi, String iccid, String mobile,String cpparam, String pmodel, 
			String osversion, String nwtype, String ctech) { 
		// 第一步：查找CP信息
		CPInfo cpInfo = cpInfoAO().queryCPInfo(cpId);
		if (cpInfo == null) return null;
		// 第二步：根据CP指定的SP匹配最佳通道
		List<CodeInfo> codes = codeHelperAO().collectAllCode(cpInfo.getLocalSpId(), province, fee);
		
		if (codes.isEmpty()) return null;
		if (codes.size() > 1) Collections.sort(codes, new PriorityComparator<CodeInfo>());
		String ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), "oo");
		
		CodeT ct = new CodeT();
		ct.setFfId(ffId);
		ct.setCpName(cpInfo.getName());
		ct.setCodes(codes);
		ct.setCp(cpInfo);
		
		return ct;
	}
	
	/***
	 * 获取指令
	 */
	public CodeT getCode(String cpId, String ip, String province, String fee, 
			String imei, String imsi, String iccid, String mobile,String cpparam, String pmodel, 
			String osversion, String nwtype, String ctech, int serVer, String sdkVer) { 
		// 第一步：查找CP信息
		CPInfo cpInfo = cpInfoAO().queryCPInfo(cpId);
		if (cpInfo == null) return null;
		// 第二步：根据CP指定的SP匹配最佳通道
		List<CodeInfo> codes = codeHelperAO().collectCodes(cpInfo.getLocalSpId(), province, fee, serVer);
		
		if (codes.isEmpty()) return null;
		if (codes.size() > 1) Collections.sort(codes, new PriorityComparator<CodeInfo>());
		CodeInfo code = codes.get(0);
		// 第三步：保存订单信息
		OrderReqInfo reqInfo = new OrderReqInfo();
		String ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), code.getSpFlag());
		reqInfo.setFfId(ffId);
		reqInfo.setCpId(cpId);
		reqInfo.setImei(imei);
		reqInfo.setImsi(imsi);
		reqInfo.setIccid(iccid);
		reqInfo.setMobile(mobile);
		reqInfo.setSpId(code.getSpId());
		reqInfo.setPmodel(pmodel);
		reqInfo.setOsversion(osversion);
		reqInfo.setNwtype(nwtype);
		reqInfo.setCtech(ctech);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setIp(ip);
		reqInfo.setProvince(province);
		reqInfo.setCpParam(cpparam);
		reqInfo.setSdkVer(sdkVer);
		
		reqInfo.setIsSyn(ConstantDefine.SYN_TRUE);
		reqInfo.setSynStatus(ConstantDefine.SYN_STATUS_DEFAULT);
		orderReqAO().addOrderReqInfo(reqInfo);
		
		// 第四步:返回指令信息
		CodeT ct = new CodeT();
		ct.setFfId(ffId);
		ct.setCpName(cpInfo.getName());
		ct.setSpName(code.getName());
		ct.setUrl(code.getUrl());
		ct.setUrlNO(code.getUrlNo());
		ct.setSpId(code.getSpId());
		ct.setCp(cpInfo);
		return ct;
	}

	@Override
	public void addMMPayInfo(MMPayInfo info) {
		mmPayInfoAO().addMMPayInfo(info);
	}

	@Override
	public OrderT queryOrderT(String ffId) {
		String indexKeyOrder = CommonTool.createIndexKey(CommonTool.FASTSER_ORDER, ffId);
		OrderReqInfo order = MemcachedResource.get(indexKeyOrder);
		if (order == null) order = orderReqAO().queryOrderReqInfo(ffId);
		if (order == null) return null;
		
		String dayKey = CommonTool.getImsiMapKey(order, "D"+DateTool.getTodayNum());
		String indexDaylimit = CommonTool.createIndexKey(CommonTool.FASTSER_DAYLIMIT,dayKey);
		PayLimitInfo dayLimit = MemcachedResource.get(indexDaylimit);
		if (dayLimit != null) {
			dayLimit.setPaidPrice(dayLimit.getPaidPrice()+order.getFee());
		} else {
			dayLimit = new PayLimitInfo();
			dayLimit.setImsi(order.getImsi());
			dayLimit.setPaidPrice(order.getFee());
		}
		MemcachedResource.save(indexDaylimit, dayLimit);
		
		String monthKey = CommonTool.getImsiMapKey(order, "M"+DateTool.getMonFirstDayNum());
		String indexMonthlimit = CommonTool.createIndexKey(CommonTool.FASTSER_DAYLIMIT,monthKey);
		PayLimitInfo monthLimit = MemcachedResource.get(indexMonthlimit);
		if (monthLimit != null) {
			monthLimit.setPaidPrice(monthLimit.getPaidPrice()+order.getFee());
		} else {
			monthLimit = new PayLimitInfo();
			monthLimit.setImsi(order.getImsi());
			monthLimit.setPaidPrice(order.getFee());
		}
		MemcachedResource.save(indexMonthlimit, monthLimit);
		
		CPInfo cp = cpInfoAO().queryCPInfo(order.getCpId());
		
		OrderT ot = new OrderT();
		ot.setOrder(order);
		ot.setCp(cp);
		return ot;
	}
	@Override
	public OrderT queryOrderTDate(String ffId) {
		String indexKeyOrder = CommonTool.createIndexKey(CommonTool.FASTSER_ORDER, ffId);
		OrderReqInfo order = MemcachedResource.get(indexKeyOrder);
		if (order == null) order = orderReqAO().queryOrderReqInfoDate(ffId);
		if (order == null) return null;
		
		String dayKey = CommonTool.getImsiMapKey(order, "D"+DateTool.getTodayNum());
		String indexDaylimit = CommonTool.createIndexKey(CommonTool.FASTSER_DAYLIMIT,dayKey);
		PayLimitInfo dayLimit = MemcachedResource.get(indexDaylimit);
		if (dayLimit != null) {
			dayLimit.setPaidPrice(dayLimit.getPaidPrice()+order.getFee());
		} else {
			dayLimit = new PayLimitInfo();
			dayLimit.setImsi(order.getImsi());
			dayLimit.setPaidPrice(order.getFee());
		}
		MemcachedResource.save(indexDaylimit, dayLimit);
		
		String monthKey = CommonTool.getImsiMapKey(order, "M"+DateTool.getMonFirstDayNum());
		String indexMonthlimit = CommonTool.createIndexKey(CommonTool.FASTSER_DAYLIMIT,monthKey);
		PayLimitInfo monthLimit = MemcachedResource.get(indexMonthlimit);
		if (monthLimit != null) {
			monthLimit.setPaidPrice(monthLimit.getPaidPrice()+order.getFee());
		} else {
			monthLimit = new PayLimitInfo();
			monthLimit.setImsi(order.getImsi());
			monthLimit.setPaidPrice(order.getFee());
		}
		MemcachedResource.save(indexMonthlimit, monthLimit);
		
		CPInfo cp = cpInfoAO().queryCPInfo(order.getCpId());
		
		OrderT ot = new OrderT();
		ot.setOrder(order);
		ot.setCp(cp);
		return ot;
	}
	@Override
	public void updateOrder(String ffId, boolean isSuc, int synStatus) {
		int isSuccess = isSuc ? 1 : -1;
		orderReqAO().updateSynStatus(ffId, isSuccess, synStatus);
	}
	
	@Override
	public void updateOrderZFB(String ffId, String ctech, String sn, int isSuccess, int synStatus) {
		orderReqAO().updateOrderZFB(ffId, ctech, sn, isSuccess, synStatus);
	}
	
	
	/*@Override
	public void changeCatchData(OrderReqInfo info) {
		int fee = info.getFee();
		int dayToalFee = fee;
		int monthToalFee = fee;
		String dayKey = CommonTool.getImsiMapKey(info, "D"+DateTool.getTodayNum());
		String monthKey = CommonTool.getImsiMapKey(info, "M"+DateTool.getMonFirstDayNum());
		String dayFee = CommonTool.imsiMap.get(dayKey);
		String monthFee = CommonTool.imsiMap.get(monthKey);
		if (dayFee != null) dayToalFee = dayToalFee + CommonTool.convertInt(dayFee);
		if (monthFee != null) monthToalFee = monthToalFee + CommonTool.convertInt(monthFee);
		CommonTool.imsiMap.put(dayKey, dayToalFee+"");
		CommonTool.imsiMap.put(monthKey, monthToalFee+"");
	}*/

	@Override
	public void updateOrderData(String mobile, String ffId, boolean isSuc, int synStatus) {
		int isSuccess = isSuc ? 1 : -1;
		orderReqAO().updateOrderData(mobile, ffId, isSuccess, synStatus);
	}
	
	@Override
	public MMFeeInfo queryFeeByAppId(String appId, String createTime) {
		return alarmAO().queryFeeByAppId(appId, createTime);
	}

	@Override
	public MMFeeInfo queryFeeByCon(String appId, String createTime, String channel) {
		return alarmAO().queryFeeByCon(appId, createTime, channel);
	}

	@Override
	public CodeT getSpecifyCode(String cpId, String ip, String province,
			String fee, String imei, String imsi, String iccid, String mobile,
			String cpparam, String pmodel, String osversion, String nwtype,
			String ctech, int serVer, String specify) {
		
		// 第一步：查找CP信息
		CPInfo cpInfo = cpInfoAO().queryCPInfo(cpId);
		if (cpInfo == null) return null;
		// 第二步：根据CP指定的SP匹配最佳通道
		List<CodeInfo> codes = codeHelperAO().collectSpecifyCodes(cpInfo.getLocalSpId(), province, 
				fee, serVer, specify);
		
		if (codes.isEmpty()) return null;
		if (codes.size() > 1) Collections.sort(codes, new PriorityComparator<CodeInfo>());
		CodeInfo code = codes.get(0);
		// 第三步：保存订单信息
		OrderReqInfo reqInfo = new OrderReqInfo();
		String ffId = CommonTool.genrateOrderNO(DateTool.getMonth(), code.getSpFlag());
		reqInfo.setFfId(ffId);
		reqInfo.setCpId(cpId);
		reqInfo.setImei(imei);
		reqInfo.setImsi(imsi);
		reqInfo.setIccid(iccid);
		reqInfo.setMobile(mobile);
		reqInfo.setSpId(code.getSpId());
		reqInfo.setPmodel(pmodel);
		reqInfo.setOsversion(osversion);
		reqInfo.setNwtype(nwtype);
		reqInfo.setCtech(ctech);
		reqInfo.setFee(CommonTool.convertInt(fee));
		reqInfo.setIp(ip);
		reqInfo.setProvince(province);
		reqInfo.setCpParam(cpparam);
		
		reqInfo.setIsSyn(ConstantDefine.SYN_TRUE);
		reqInfo.setSynStatus(ConstantDefine.SYN_STATUS_DEFAULT);
		orderReqAO().addOrderReqInfo(reqInfo);
		
		// 第四步:返回指令信息
		CodeT ct = new CodeT();
		ct.setFfId(ffId);
		ct.setCpName(cpInfo.getName());
		ct.setSpName(code.getName());
		ct.setUrl(code.getUrl());
		ct.setUrlNO(code.getUrlNo());
		return ct;
	}

	@Override
	public void addCustomInfo(CustomInfo info) {
		commonAO().addCustomInfo(info);
	}
	
	@Override
	public void refreshCache() {
		commonAO().refreshCache();
	}

	@Override
	public CustomInfo getCustomInfo(CustomInfo info) {
		return commonAO().getCustomInfo(info);
	}
	
	@Override
	public CustomInfo checkCustomerInfo(String imsi) {
		return commonAO().checkCustomerInfo(imsi);
	}

	@Override
	public SDKUpdateInfo getSDKUpdateParam(SDKUpdateInfo info) {
		return commonAO().getSDKUpdateParam(info);
	}

	@Override
	public String getNewPayCode(PayReqInfo info) {
		PayCodeInfo payInfo = codeHelperAO().getPayCodes(info);
		ResSDKJson resSDKJson = new ResSDKJson();
		if (payInfo == null) {
			resSDKJson.setFlag("400");//没有可用的计费通道
			resSDKJson.setPhnum(info.getPhnum());
		} else {
			ResSDKResult resSDKResult = new ResSDKResult();
			List<ResSDKWap> resSDKWapList = new ArrayList<ResSDKWap>();
			List<ResSDKSms> resSDKSmsList = new ArrayList<ResSDKSms>();
			List<ResSDKFilter> resSDKFilterList = new ArrayList<ResSDKFilter>();
			ResSDKBusi resSDKBusi = new ResSDKBusi();
			ResSDKWap resSDKWap = new ResSDKWap();
			ResSDKSms resSDKSms = new ResSDKSms();
			ResSDKFilter resSDKFilter = new ResSDKFilter();
			CodeT ct = new CodeT();
			ct.setFfId(info.getFfId());
			ct.setSpId(payInfo.getBillId());
			String imsi = info.getImsi();
			String iccid = info.getIccid();
			int billType = payInfo.getBillType();
			if (billType == 1) {//1 wap包月  2 短代   3 在线获取   4 请求其他端口
				if (info.getPhnum() == null) {
					CustomInfo customInfo = new CustomInfo();
					customInfo.setImsi(imsi);
					customInfo.setIccid(iccid);
					customInfo.setLocalTab(CommonTool.getImsiSub(imsi));
					CustomInfo resCusInfo = commonAO().getCustomInfo(customInfo);
					if (resCusInfo == null) {
						resSDKJson.setFlag("300");//企信通设置(发送短信)
						resSDKResult.setSmstonum(ConstantDefine.RES_QXT_PORT);
						resSDKResult.setSmstocon(ConstantDefine.RES_QXT_SMS+imsi+"#1#"+iccid);
						resSDKJson.setResult(resSDKResult);
						
						addOrderReqInfo1(ct, info.getCpId(), info.getIp(), info.getProvince(), info.getPrice(), 
								info.getImei(), imsi, iccid, null, null, info.getBand(), info.getOsver(), 
								info.getApn(), null);
					} else {
						resSDKJson.setFlag("200");
						resSDKJson.setPhnum(resCusInfo.getMobile());
						
						resSDKResult = setResult(payInfo);
						
						resSDKWap = setWap(payInfo, info);
						String param = "mocontent=ZGHLS2974&tel="+resCusInfo.getMobile();
						String config = HttpTool.sendGet(payInfo.getUrl(), param);
						resSDKWap.setConfig(config);
						resSDKWapList.add(resSDKWap);
						resSDKBusi.setWap(resSDKWapList);
						resSDKResult.setBusiness(resSDKBusi);
						
						resSDKFilter = setFilter(payInfo);
						resSDKFilterList.add(resSDKFilter);
						resSDKResult.setFilter_rsp(resSDKFilterList);
						resSDKJson.setResult(resSDKResult);
						
						ct.setSpId(payInfo.getBillId());
						addOrderReqInfo1(ct, info.getCpId(), info.getIp(), info.getProvince(), info.getPrice(), 
								info.getImei(), imsi, iccid, null, null, info.getBand(), info.getOsver(), 
								info.getApn(), null);
					}
				} else {
					resSDKJson.setFlag("200");
					resSDKJson.setPhnum(info.getPhnum());
					
					resSDKResult = setResult(payInfo);
					
					resSDKWap = setWap(payInfo, info);
					resSDKWapList.add(resSDKWap);
					resSDKBusi.setWap(resSDKWapList);
					resSDKResult.setBusiness(resSDKBusi);
					
					resSDKFilter = setFilter(payInfo);
					resSDKFilterList.add(resSDKFilter);
					resSDKResult.setFilter_rsp(resSDKFilterList);
					resSDKJson.setResult(resSDKResult);
					
					ct.setSpId(payInfo.getBillId());
					addOrderReqInfo1(ct, info.getCpId(), info.getIp(), info.getProvince(), info.getPrice(), 
							info.getImei(), imsi, iccid, null, null, info.getBand(), info.getOsver(), 
							info.getApn(), null);
				}
			} else if (billType == 2) {//短代
				resSDKJson.setFlag("200");
				resSDKJson.setPhnum(info.getPhnum());
				
				resSDKResult = setResult(payInfo);
				
				resSDKSms = setSms(payInfo, info);
				resSDKSmsList.add(resSDKSms);
				resSDKBusi.setSms(resSDKSmsList);
				resSDKResult.setBusiness(resSDKBusi);
				
				resSDKFilter = setFilter(payInfo);
				resSDKFilterList.add(resSDKFilter);
				resSDKResult.setFilter_rsp(resSDKFilterList);
				resSDKJson.setResult(resSDKResult);
				
				ct.setSpId(payInfo.getBillId());
				addOrderReqInfo1(ct, info.getCpId(), info.getIp(), info.getProvince(), info.getPrice(), 
						info.getImei(), imsi, iccid, null, null, info.getBand(), info.getOsver(), 
						info.getApn(), null);
			} else if (billType == 3) {//在线获取指令
				resSDKJson.setFlag("200");
				resSDKJson.setPhnum(info.getPhnum());
				
				PayCodeInfo resultPayCode = getSmsPayCode(payInfo, info);
				
				resSDKSms = setSms(resultPayCode, info);
				resSDKSmsList.add(resSDKSms);
				resSDKBusi.setSms(resSDKSmsList);
				resSDKResult.setBusiness(resSDKBusi);
				
				resSDKFilter = setFilter(payInfo);
				resSDKFilterList.add(resSDKFilter);
				resSDKResult.setFilter_rsp(resSDKFilterList);
				resSDKJson.setResult(resSDKResult);
				
				ct.setSpId(payInfo.getBillId());
				addOrderReqInfo1(ct, info.getCpId(), info.getIp(), info.getProvince(), info.getPrice(), 
						info.getImei(), imsi, iccid, null, null, info.getBand(), info.getOsver(), 
						info.getApn(), null);
				
			}
		}
		return JSONObject.toJSONString(resSDKJson);
	}
	
	private PayCodeInfo getSmsPayCode(PayCodeInfo payInfo, PayReqInfo info) {
		PayCodeInfo resultPayCode = new PayCodeInfo();
		switch (payInfo.getUrlNO()) {
		case 2:
			resultPayCode = OnlineSMSTool.getOnlineSMS2(payInfo, info);
			break;
		default:
			break;
		}
		return resultPayCode;
	}
	
	private ResSDKSms setSms(PayCodeInfo payInfo, PayReqInfo info) {
		ResSDKSms resSDKSms = new ResSDKSms();
		resSDKSms.setOrderid(info.getFfId());
		resSDKSms.setCount("1");
		resSDKSms.setDeductionnum(payInfo.getDeductionnum());
		resSDKSms.setDeductioncommand(payInfo.getDeductioncommand());
		resSDKSms.setNeedmt(payInfo.getNeedmt());
		resSDKSms.setIsanto(info.getIsauto());
		return resSDKSms;
	}
	
	private ResSDKResult setResult(PayCodeInfo payInfo) {
		ResSDKResult resSDKResult = new ResSDKResult();
		resSDKResult.setVersionNo("1");
		resSDKResult.setReportUrl(payInfo.getReporturl());
		resSDKResult.setFilterApp(payInfo.getFilterapp());
		resSDKResult.setNew_url(payInfo.getNewurl());
		return resSDKResult;
	}
	
	private ResSDKWap setWap(PayCodeInfo payInfo, PayReqInfo info) {
		ResSDKWap resSDKWap = new ResSDKWap();
		resSDKWap.setOrderid(info.getFfId());
		resSDKWap.setWapid("001");
		resSDKWap.setConfig(payInfo.getConfig());
		resSDKWap.setUa(info.getBand());//手机品牌
		resSDKWap.setDownnum(payInfo.getDownnum());
		resSDKWap.setIs_save(payInfo.getIsSave());
		resSDKWap.setIsanto(info.getIsauto());
		return resSDKWap;
	}

	private ResSDKFilter setFilter(PayCodeInfo payInfo) {
		ResSDKFilter resSDKFilter = new ResSDKFilter();
		resSDKFilter.setFilter_port(payInfo.getFilterPort());
		resSDKFilter.setR_type(payInfo.getrType());
		resSDKFilter.setMatch(payInfo.getMatch());
		resSDKFilter.setR_addr(payInfo.getrAddr());
		resSDKFilter.setR_cont(payInfo.getrCont());
		resSDKFilter.setR_start(payInfo.getrStart());
		resSDKFilter.setR_end(payInfo.getrEnd());
		resSDKFilter.setOrderid(payInfo.getOrderid());
		resSDKFilter.setGetkeyword("0");
		return resSDKFilter;
	}
	
	@Override
	public void updateSDKResult(String ffId, String sdkResult) {
		orderReqAO().updateSDKResult(ffId, sdkResult);
	}

	@Override
	public void addResSDKReport(ResSDKReport info) {
		orderReqAO().addResSDKReport(info);
	}

	private int getAllowPayFee(OrderReqInfo info) {
		List<ResSDKFilter> resultFilterList = orderReqAO().queryFilterBySpid(info.getSpId());
		int allowPayFee = 999;
		int allowMonthPay = 0;
		int allowDayPay = 0;
		if (resultFilterList != null && resultFilterList.size()>0) {
			ResSDKFilter filter = resultFilterList.get(0);
			if (filter.getPay_limit_open() == 1) {
				info.setStartTime(DateTool.getMonFirstDay());
				String monthKey = CommonTool.getImsiMapKey(info, "M"+DateTool.getMonFirstDayNum());
				String indexMonthlimit = CommonTool.createIndexKey(CommonTool.FASTSER_DAYLIMIT,monthKey);
				PayLimitInfo monthLimit = MemcachedResource.get(indexMonthlimit);
				if (monthLimit == null) {
					monthLimit = commonAO().queryPaidByImsi(info);
					MemcachedResource.save(indexMonthlimit, monthLimit);
				}
				
				info.setStartTime(DateTool.getToday());
				String dayKey = CommonTool.getImsiMapKey(info, "D"+DateTool.getTodayNum());
				String indexDaylimit = CommonTool.createIndexKey(CommonTool.FASTSER_DAYLIMIT,dayKey);
				
				if (monthLimit != null ) {
					allowMonthPay = filter.getMonth_limit() - monthLimit.getPaidPrice();
					if (allowMonthPay > 0) {
						
						PayLimitInfo dayLimit = MemcachedResource.get(indexDaylimit);
						if (dayLimit == null) {
							dayLimit = commonAO().queryPaidByImsi(info);
							MemcachedResource.save(indexDaylimit, dayLimit);
						}
						
						if (dayLimit != null) {
							allowDayPay = filter.getDay_limit() - dayLimit.getPaidPrice();
						} else {
							allowDayPay = filter.getDay_limit();
						}
					}
				} else {
					allowMonthPay = filter.getMonth_limit();
					PayLimitInfo dayLimit = MemcachedResource.get(indexDaylimit);
					if (dayLimit == null) {
						dayLimit = commonAO().queryPaidByImsi(info);
						MemcachedResource.save(indexDaylimit, dayLimit);
					}
					
					if (dayLimit != null) {
						allowDayPay = filter.getDay_limit() - dayLimit.getPaidPrice();
					} else {
						allowDayPay = filter.getDay_limit();
					}
				}
				
				if (allowDayPay > 0 && allowMonthPay > 0) {
					if (allowDayPay <= allowMonthPay) {
						allowPayFee = allowDayPay;
					} else {
						allowPayFee = allowMonthPay;
					}
				} else {
					allowPayFee = 0;
				}
				
			}
		}
		
		return allowPayFee;
	}
	
	@Override
	public List<ResSDKFilter> queryFilterBySpid(OrderReqInfo reqInfo) {
		List<ResSDKFilter> resultFilterList = orderReqAO().queryFilterBySpid(reqInfo.getSpId());
		if (resultFilterList != null && resultFilterList.size()>0) {
			ResSDKFilter filter = resultFilterList.get(0);
			if (filter.getPay_limit_open() == 1) {
				reqInfo.setStartTime(DateTool.getToday());
				String dayKey = CommonTool.getImsiMapKey(reqInfo, "D"+DateTool.getTodayNum());
				String indexDaylimit = CommonTool.createIndexKey(CommonTool.FASTSER_DAYLIMIT,dayKey);
				PayLimitInfo dayLimit = MemcachedResource.get(indexDaylimit);
				if (dayLimit == null) {
					dayLimit = commonAO().queryPaidByImsi(reqInfo);
					MemcachedResource.save(indexDaylimit, dayLimit);
				}
				reqInfo.setStartTime(DateTool.getMonFirstDay());
				String monthKey = CommonTool.getImsiMapKey(reqInfo, "M"+DateTool.getMonFirstDayNum());
				String indexMonthlimit = CommonTool.createIndexKey(CommonTool.FASTSER_DAYLIMIT,monthKey);
				PayLimitInfo monthLimit = MemcachedResource.get(indexMonthlimit);
				if (monthLimit == null) {
					monthLimit = commonAO().queryPaidByImsi(reqInfo);
					MemcachedResource.save(indexMonthlimit, monthLimit);
				}
				if (monthLimit != null ) {
					if (monthLimit.getPaidPrice() >= filter.getMonth_limit()) {
						return null;
					} else if ( dayLimit != null && dayLimit.getPaidPrice() >= filter.getDay_limit()) 
						return null;
				}
			}
		}
		return resultFilterList;
	}
	
	@Override
	public List<ResSDKFilter> queryOtherFilterBySpid(OrderReqInfo reqInfo) {
		List<ResSDKFilter> resultFilterList = orderReqAO().queryFilterBySpid(reqInfo.getSpId());
		if (resultFilterList != null && resultFilterList.size()>0) {
			ResSDKFilter filter = resultFilterList.get(0);
			if (filter.getPay_limit_open() == 1) {
				reqInfo.setStartTime(DateTool.getToday());
				String dayKey = CommonTool.getImsiMapKey(reqInfo, "D"+DateTool.getTodayNum());
				String indexDaylimit = CommonTool.createIndexKey(CommonTool.FASTSER_DAYLIMIT,dayKey);
				PayLimitInfo dayLimit = MemcachedResource.get(indexDaylimit);
				if (dayLimit == null) {
					dayLimit = commonAO().queryOtherPaidByImsi(reqInfo);
					MemcachedResource.save(indexDaylimit, dayLimit);
				}
				if ( dayLimit != null && dayLimit.getPaidPrice() >= filter.getDay_limit()) 
					return resultFilterList;
			}
		}
		return null;
	}
	
	@Override
	public List<SmsFilter> querySmsFilterBySpid(OrderReqInfo reqInfo) {
		List<SmsFilter> resultFilterList = orderReqAO().querySmsFilterBySpid(reqInfo.getSpId());
		if (resultFilterList != null && resultFilterList.size()>0) {
			SmsFilter filter = resultFilterList.get(0);
			if (filter.getPay_limit_open() == 1) {
				reqInfo.setStartTime(DateTool.getToday());
				String dayKey = CommonTool.getImsiMapKey(reqInfo, "SDKD"+DateTool.getTodayNum());
				String indexDaylimit = CommonTool.createIndexKey(CommonTool.FASTSER_DAYLIMIT,dayKey);
				PayLimitInfo dayLimit = MemcachedResource.get(indexDaylimit);
				if (dayLimit == null) {
					dayLimit = commonAO().queryPaidByImsi(reqInfo);
					MemcachedResource.save(indexDaylimit, dayLimit);
				}
				reqInfo.setStartTime(DateTool.getMonFirstDay());
				String monthKey = CommonTool.getImsiMapKey(reqInfo, "SDKM"+DateTool.getMonFirstDayNum());
				String indexMonthlimit = CommonTool.createIndexKey(CommonTool.FASTSER_DAYLIMIT,monthKey);
				PayLimitInfo monthLimit = MemcachedResource.get(indexMonthlimit);
				if (monthLimit == null) {
					monthLimit = commonAO().queryPaidByImsi(reqInfo);
					MemcachedResource.save(indexMonthlimit, monthLimit);
				}
				if (monthLimit != null ) {
					if (monthLimit.getPaidPrice() >= filter.getMonth_limit()) {
						return null;
					} else if ( dayLimit != null && dayLimit.getPaidPrice() >= filter.getDay_limit()) 
						return null;
				}
			}
		}
		return resultFilterList;
	}
	
	public CustomInfo getCustominfo(OrderReqInfo reqInfo) {
		CustomInfo customInfo = new CustomInfo();
		customInfo.setImsi(reqInfo.getImsi());
		customInfo.setIccid(reqInfo.getIccid());
		customInfo.setLocalTab(CommonTool.getImsiSub(reqInfo.getImsi()));
		CustomInfo resCusInfo = commonAO().getCustomInfo(customInfo);
		return resCusInfo;
	}

	@Override
	public void saveExt(String ext) {
		commonAO().saveExt(ext);
	}
	
	@Override
	public String queryExt(String ffid) {
		return commonAO().queryExt(ffid);
	}
	
	@Override
	public CPInfo queryCP(String cpId) {
		return cpInfoAO().queryCPInfo(cpId);
	}
	
	@Override
	public void reqForward(String json) {
		JSONObject resultObj = JSONObject.parseObject(json);
		String url = resultObj.getString("url");
		String ffId = resultObj.getString("ffid");
		String reqMethod = resultObj.getString("reqmethod");
		String reqParam = resultObj.getString("reqparam");
		
		String indexKey = CommonTool.createIndexKey(ACTION_FORWARD,ffId);
		String reqFlag = MemcachedResource.get(indexKey);
		if (reqFlag != null && "1".equals(reqFlag)) return;
		
		String result = "";
		if ("1".equals(reqMethod)) {
			result = HttpTool.sendGetSetTimeout(url,reqParam,"500");
			MemcachedResource.save(indexKey,"1");
		} else if ("2".equals(reqMethod)) {
			result = HttpTool.sendPost(url, reqParam, "500");
			MemcachedResource.save(indexKey,"1");
		}
		
		logger.info("reqForward请求url=" + url);
		logger.info("reqForward请求reqparam=" + reqParam);
		logger.info("reqForward请求result=" + result);
	}

	@Override
	public String getMobile(OrderReqInfo reqInfo) {
		String result = "error";
		CustomInfo resCusInfo = getCustominfo(reqInfo);
		String imsi = reqInfo.getImsi();
		Map<String, String> resultMap = new HashMap<String, String>();
		if (resCusInfo != null) {
			resultMap.put("type", "1");
			resultMap.put("imsi", imsi);
			resultMap.put("mobile", resCusInfo.getMobile());
			resultMap.put("result", "success");
			result = JSONObject.toJSONString(resultMap);
		} else {
			String indexKey = "qxtmobile_"+DateTool.getToday()+imsi;
			String scount = MemcachedResource.get(indexKey);
			int count = 0;
			if (scount != null) count = new Integer(scount);
			
			if (count < 3) {
				resultMap.put("type", "2");
				resultMap.put("port", ConstantDefine.RES_QXT_PORT);
				String content = ConstantDefine.RES_QXT_SMS+imsi+"#"+reqInfo.getAimsi()+"#"+reqInfo.getIccid();
				resultMap.put("sms", content);
				resultMap.put("result", "success");
				result = JSONObject.toJSONString(resultMap);
				count++;
				MemcachedResource.save(indexKey, count+"");
			}
		}
		
		return result;
	}
	
	@Override
	public QQInfo queryQQInfo(OrderReqInfo reqInfo) {
		QQInfo qq = commonAO().queryQQInfo(reqInfo);
		return qq;
	}
	
	@Override
	public boolean queryBlackMobile(String mobile) {
		MobileInfo info = commonAO().queryBlackMobile(mobile);
		boolean flag = false;
		if (info != null) flag = true;
		return flag;
	}

	@Override
	public CPInfo getCPInfo(OrderReqInfo reqInfo) {
		CPInfo cpInfo = cpInfoAO().queryCPInfo(reqInfo.getCpId());
		return cpInfo;
	}

	@Override
	public List<ResSDKFilter> queryFilterBySpid(String spId) {
		List<ResSDKFilter> resultFilterList = orderReqAO().queryFilterBySpid(spId);
		return resultFilterList;
	}

	@Override
	public void updateOrderKL(String cpid, String isSuc, int synStatus) {
		orderReqAO().updateOrderKL(cpid, isSuc, synStatus);
	}

	@Override
	public CodeOnlyInfo queryCodeOnlyBySpid(String spId) {
		return commonAO().queryCodeOnlyBySpid(spId);
	}

	@Override
	public void addOnlyOrder(OrderReqInfo info) {
		commonAO().addOnlyOrder(info);
	}
	
	@Override
	public void updateCp(CPInfo info) {
		cpInfoAO().updateCp(info);
		commonAO().refreshCache();
	}

	@Override
	public void updateOrderInfo(OrderReqInfo info) {
		orderReqAO().updateOrderInfo(info);
		String indexKeyOrder = CommonTool.createIndexKey(CommonTool.FASTSER_ORDER, info.getFfId());
		MemcachedResource.remove(indexKeyOrder);
	}
	@Override
	public void updateOrderInfoDate(OrderReqInfo info) {
		orderReqAO().updateOrderInfoDate(info);
		String indexKeyOrder = CommonTool.createIndexKey(CommonTool.FASTSER_ORDER, info.getFfId());
		MemcachedResource.remove(indexKeyOrder);
	}

	@Override
	public OrderReqInfo queryOrderByMobile(String mobile, String ffId) {
		return orderReqAO().queryOrderByMobile(mobile,ffId);
	}
	
	@Override
	public String getCpid(String pid) {
		return commonAO().getCpid(pid);
	}

	@Override
	public CPInfo getCpByPid(String pid) {
		return commonAO().getCpByPid(pid);
	}
	
	@Override
	public MMFeeInfo queryFeeBySpid(String spId, String createTime) {
		return alarmAO().queryFeeBySpid(spId,createTime);
	}

	@Override
	public List<CPFeeInfo> queryFeeByCpid(String cpId, String createTime) {
		return alarmAO().queryFeeByCpid(cpId,createTime);
	}
	
	@Override
	public MMFeeInfo queryCountBySpid(String spId, String createTime) {
		return alarmAO().queryCountBySpid(spId,createTime);
	}

	@Override
	public List<CPFeeInfo> queryCountByCpid(String cpId, String createTime) {
		return alarmAO().queryCountByCpid(cpId,createTime);
	}

	@Override
	public CodeInfo queryCodeInfoBySpid(String spId) {
		return codeHelperAO().queryCodeInfoBySpid(spId);
	}

	@Override
	public void updateCodeInfo(CodeInfo info) {
		codeHelperAO().updateCodeInfo(info);
	}

	@Override
	public OrderReqInfo queryOrderByCondition(OrderReqInfo info) {
		return orderReqAO().queryOrderByCondition(info);
	}

	@Override
	public OrderReqInfo queryOrderReqInfo(String ffId) {
		return orderReqAO().queryOrderReqInfo(ffId);
	}
	
	@Override
	public OrderReqInfo queryOrderReqInfoDate(String ffId) {
		return orderReqAO().queryOrderReqInfoDate(ffId);
	}

	@Override
	public void updateOrderByOther(OrderReqInfo info) {
		orderReqAO().updateOrderByOther(info);
	}

	@Override
	public MobileInfo getProvinceByPhone(int mobile) {
		return commonAO().getProvinceByPhone(mobile);
	}
	
	@Override
	public void removeMemcached(String key) {
		commonAO().removeMemcached(key);
	}

	@Override
	public Map<String,String> queryBalanceByCon(String cpid, String spid) {
		return codeHelperAO().queryBalanceByCon(cpid,spid);
	}
	@Override
	public String createOrder(Map<String,String> map) {
		return codeHelperAO().createOrder(map);
	}

	@Override
	public String querySpIdByCpId(String cpid) {
		return codeHelperAO().querySpIdByCpId(cpid);
	}

	@Override
	public Map<String, String> selectDaifu(String spid) {
		return codeHelperAO().selectDaifu(spid);
	}

	@Override
	public void updateOrder(Map<String, String> map) {
		codeHelperAO().updateOrder(map);
		
	}

}
