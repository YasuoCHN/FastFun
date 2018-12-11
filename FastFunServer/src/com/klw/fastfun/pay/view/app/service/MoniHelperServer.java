/**
 * FastFunServer
 */
package com.klw.fastfun.pay.view.app.service;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.juice.orange.game.handler.HttpRequest;
import com.juice.orange.game.handler.HttpResponse;
import com.juice.orange.game.log.LoggerFactory;
import com.klw.fastfun.pay.common.domain.CPFeeInfo;
import com.klw.fastfun.pay.common.domain.CPInfo;
import com.klw.fastfun.pay.common.domain.CodeInfo;
import com.klw.fastfun.pay.common.domain.MMFeeInfo;
import com.klw.fastfun.pay.common.tool.CommonTool;
import com.klw.fastfun.pay.common.tool.ConstantDefine;
import com.klw.fastfun.pay.common.tool.HttpTool;
import com.klw.fastfun.pay.view.app.ActionAware;

/**
 * @author klwplayer.com
 *
 *         2015年3月30日
 */
public class MoniHelperServer extends ActionAware {
	private static Logger logger = LoggerFactory
			.getLogger(MoniHelperServer.class);
	public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * sp限量监控
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void monitporsp(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("monitporsp请求header信息："+request.allHeaders());
			String content = request.body();
			logger.info("monitporsp请求header信息："+content);
			JSONObject myObj = JSONObject.parseObject(content);
			String spId = myObj.getString("spId");
			String moniPro = myObj.getString("moniPro");
			int moniProValue = myObj.getIntValue("moniProValue");
			
			Calendar cal = Calendar.getInstance();
			String createTime = format.format(cal.getTime());
			MMFeeInfo feeInfo = null;
			String operation = null;
			if ("fee".equals(moniPro)) {
				feeInfo = commonAction.queryFeeBySpid(spId,createTime);
			} else if ("num".equals(moniPro)) {
				feeInfo = commonAction.queryCountBySpid(spId,createTime);
			}
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spId);
			if (moniProValue<=feeInfo.getFees()) {
				operation = ConstantDefine.CODE_CLOSE;
				if (codeInfo.getIsOpen() != 0) {
					executeSpConfig(spId,operation);
				}
			} else {
				operation = ConstantDefine.CODE_OPEN;
				if (codeInfo.getIsOpen() == 0) {
					executeSpConfig(spId,operation);
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content("ok").end();
	}
	
	/**
	 * sp限时监控
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void operationSpConfig(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("operationSpConfig请求header信息："+request.allHeaders());
			String content = request.body();
			logger.info("operationSpConfig请求header信息："+content);
			JSONObject myObj = JSONObject.parseObject(content);
			String spId = myObj.getString("spId");
			String operation = myObj.getString("execute");
			
			CodeInfo codeInfo = commonAction.queryCodeInfoBySpid(spId);
			
			if (ConstantDefine.CODE_CLOSE.equals(operation)) {
				if (codeInfo.getIsOpen() != 0) {
					executeSpConfig(spId,operation);
				}
			} else if (ConstantDefine.CODE_OPEN.equals(operation)) {
				if (codeInfo.getIsOpen() == 0) {
					executeSpConfig(spId,operation);
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content("ok").end();
	}
	
	/**
	 * cp限量监控
	 * http://smspay.xushihudong.com/monit/monitporcp
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void monitporcp(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("monitporcp请求header信息："+request.allHeaders());
			String content = request.body();
			logger.info("monitporcp请求header信息："+content);
			JSONObject myObj = JSONObject.parseObject(content);
			String cpId = myObj.getString("cpId");
			String moniPro = myObj.getString("moniPro");
			String moniProValues = myObj.getString("moniProValue");
			String moniProProvince = myObj.getString("moniProProvince");
			int moniProRadio = myObj.getIntValue("moniProRadio");
			
			if (moniProProvince != null && moniProProvince.length() > 0)
				moniProProvince = URLDecoder.decode(moniProProvince, "utf-8");
			
			Calendar cal = Calendar.getInstance();
			String createTime = format.format(cal.getTime());
			List<CPFeeInfo> feelist = null;
//			String operation = null;
			if ("fee".equals(moniPro)) {//监控值为金额
				feelist = commonAction.queryFeeByCpid(cpId,createTime);
			} else if ("num".equals(moniPro)) {//监控值为成功数
				feelist = commonAction.queryCountByCpid(cpId,createTime);
			}
			
			if (feelist != null && feelist.size() > 0) {
				CPInfo cp = commonAction.queryCP(cpId);
				if (cp != null && moniProProvince != null) {
					int currProValue = 0;
					if ("all".equals(moniProProvince)) {//所有省份
						int moniProValue = CommonTool.convertInt(moniProValues);
						for (CPFeeInfo feeInfo : feelist) {
							currProValue += feeInfo.getFees(); 
						}
						CPInfo updatecp = new CPInfo();
						updatecp.setCpId(cpId);
						updatecp.setSynOpen(-1);
						if (moniProValue <= currProValue && cp.getSynRadio() > 0) {//超量且同步没关暂停同步
							logger.info("cpid:" + cpId + ",超量且同步没关暂停同步");
							updatecp.setSynRadio(0);
							commonAction.updateCp(updatecp);
						} else if (moniProValue > currProValue && cp.getSynRadio() == 0) {//没到量且同步被关闭打开同步
							logger.info("cpid:" + cpId + ",没到量且同步被关闭打开同步");
							updatecp.setSynRadio(moniProRadio);
							commonAction.updateCp(updatecp);
						}
					} else {//区分省份更新
						String[] provinceArr = moniProProvince.split("#");
						String[] valueArr = moniProValues.split("#");
						String synProvince = "";
						String synNum = "";
						for (int i=0; (i<provinceArr.length && moniProProvince.length()>0); i++) {//检查监控省份是否超量
							for (CPFeeInfo feeInfo : feelist) {
 								if (provinceArr[i].contains(feeInfo.getProvince())) {
  									synProvince = synProvince + provinceArr[i] + "#";
 									if (CommonTool.convertInt(valueArr[i]) <= feeInfo.getFees()) {//超量
										synNum = synNum + "-" + cp.getSynRadio() + "#";
									} else {//没到量
										synNum = synNum + moniProRadio + "#";
									}
								}
							}
						}
						
						String currSynProvince = cp.getSynProvince() == null ? "" : cp.getSynProvince();//当前省份配置
						String currSynNum = cp.getSynNum() == null ? "" : cp.getSynNum();//当前分省扣量设置值
						String[] currSynProvinceArr = currSynProvince.split("#");
						String[] currSynNumArr = currSynNum.split("#");
						String[] newSynProvinceArr = synProvince.split("#");
						String[] newSynNumArr = synNum.split("#");
						boolean flag = false;
						boolean upflag = false;
						String upSynProvince = "";
						String upSynNum = "";
						
						//监控省份扣量值和当前省份有重复的替换当前省份扣量值设置
						for (int i=0; (i<currSynProvinceArr.length && currSynProvince.length() > 0); i++) {
							upSynProvince = upSynProvince + currSynProvinceArr[i] + "#";
							for (int j=0; j<newSynProvinceArr.length; j++) {
								if (currSynProvinceArr[i].equals(newSynProvinceArr[j]) 
										&& !currSynNumArr[i].equals(newSynNumArr[j])) {//监控省份在当前配置中存在且扣量值不相同
									upSynNum = upSynNum + newSynNumArr[j] + "#";
									flag = true;
									upflag = true;
									break;
								}
							}
							if (flag) {
								flag = false;
							} else {
								upSynNum = upSynNum + currSynNumArr[i] + "#";
							}
						}
						
						//将监控省份没在当前扣量配置中的省份追加入更新队列
						for (int i=0; (i<newSynProvinceArr.length && synProvince.length()>0); i++) {
							if (!upSynProvince.contains(newSynProvinceArr[i])) {
								upSynProvince = upSynProvince + newSynProvinceArr[i] + "#";
								upSynNum = upSynNum + newSynNumArr[i] + "#";
								upflag = true;
							}
						}
						
						if (upflag) {
							
							upSynProvince = upSynProvince.substring(0, upSynProvince.length()-1);
							upSynNum = upSynNum.substring(0, upSynNum.length()-1);
							
							CPInfo updatecp = new CPInfo();
							updatecp.setCpId(cpId);
							updatecp.setSynRadio(-1);
							updatecp.setSynOpen(1);
							updatecp.setSynProvince(upSynProvince);
							updatecp.setSynNum(upSynNum);
							logger.info("cpid:" + cpId + ",synProvince:"+upSynProvince
									+",synNum:"+upSynNum+"没到量且同步被关闭打开同步");
							commonAction.updateCp(updatecp);
						}
					}
					
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content("ok").end();
	}
	
	/**
	 * http://smspay.xushihudong.com/monit/operationCpConfig
	 * cp限时监控
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void operationCpConfig(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("operationCpConfig请求header信息："+request.allHeaders());
			String content = request.body();
			logger.info("operationCpConfig请求header信息："+content);
			JSONObject myObj = JSONObject.parseObject(content);
			String cpId = myObj.getString("cpId");
			String province = myObj.getString("province");
			String radio = myObj.getString("radio");
			String operation = myObj.getString("execute");
			
			if (province != null && province.length() > 0)
				province = URLDecoder.decode(province, "utf-8");
			if (radio == null || radio.length() <= 0)
				radio = "70";
			
			CPInfo cp = commonAction.queryCP(cpId);
			
			CPInfo updatecp = new CPInfo();
			updatecp.setCpId(cpId);
			updatecp.setSynRadio(-1);
			updatecp.setSynOpen(-1);
//			updatecp.setSynProvince(upSynProvince);
//			updatecp.setSynNum(upSynNum);
			
			if ("all".equals(province)) {//所有省份调整同步
				if (ConstantDefine.CODE_CLOSE.equals(operation)) {//暂停同步
					if (cp.getSynRadio() > 0) {//当前同步率与需要调整的同步率不同暂停同步
						logger.info("当前all同步率：" + cp.getSynRadio() + ",需调整同步率：0");
						updatecp.setSynRadio(0);
						commonAction.updateCp(updatecp);
					}
				} else if (ConstantDefine.CODE_OPEN.equals(operation)) {//打开同步
					if (cp.getSynRadio() == 0) {//当前同步率与需要调整的同步率不同打开同步
						logger.info("当前all同步率：" + cp.getSynRadio() + ",需调整同步率：" + radio);
						updatecp.setSynRadio(CommonTool.convertInt(radio));
						commonAction.updateCp(updatecp);
					}
				}
			} else {//分省调整同步
				String currSynProvince = cp.getSynProvince() == null ? "" : cp.getSynProvince();//当前省份配置
				String currSynNum = cp.getSynNum() == null ? "" : cp.getSynNum();//当前分省扣量设置值
				String[] currSynProvinceArr = currSynProvince.split("#");
				String[] currSynNumArr = currSynNum.split("#");
				String[] newSynProvinceArr = province.split("#");
				boolean flag = false;
				boolean upflag = false;
				String upSynProvince = "";
				String upSynNum = "";
				if (ConstantDefine.CODE_CLOSE.equals(operation)) {//暂停同步
					
					//监控省份扣量值和当前省份有重复的替换当前省份扣量值设置
					for (int i=0; (i<currSynProvinceArr.length && currSynProvince.length() > 0); i++) {
						upSynProvince = upSynProvince + currSynProvinceArr[i] + "#";
						for (int j=0; j<newSynProvinceArr.length; j++) {
							if (currSynProvinceArr[i].equals(newSynProvinceArr[j]) 
									&& (cp.getSynRadio() + CommonTool.convertInt(currSynNumArr[i]) > 0)) {
								//当前省份同步率大于0才关闭
								upSynNum = upSynNum + "-" + cp.getSynRadio() + "#";
								flag = true;
								if (!currSynNumArr[i].equals("-" + cp.getSynRadio())) {
									upflag = true;
								}
							}
						}
						if (flag) {
							flag = false;
						} else {
							upSynNum = upSynNum + currSynNumArr[i] + "#";
						}
					}
					
					//将监控省份没在当前扣量配置中的省份追加入更新队列
					for (int i=0; (i<newSynProvinceArr.length && province.length()>0); i++) {
						if (!upSynProvince.contains(newSynProvinceArr[i])) {
							upSynProvince = upSynProvince + newSynProvinceArr[i] + "#";
							upSynNum = upSynNum + "-" + cp.getSynRadio() + "#";
							upflag = true;
						}
					}
					
					if (upflag) {
						
						upSynProvince = upSynProvince.substring(0, upSynProvince.length()-1);
						upSynNum = upSynNum.substring(0, upSynNum.length()-1);
						
						updatecp.setSynOpen(1);
						updatecp.setSynProvince(upSynProvince);
						updatecp.setSynNum(upSynNum);
						logger.info("cpid:" + cpId + ",synProvince:"+upSynProvince
								+",synNum:"+upSynNum+"到时间且同步没关闭，关闭同步");
						commonAction.updateCp(updatecp);
					}
					
				} else if (ConstantDefine.CODE_OPEN.equals(operation)) {//打开同步
					
					//监控省份扣量值和当前省份有重复的替换当前省份扣量值设置
					for (int i=0; (i<currSynProvinceArr.length && currSynProvince.length() > 0); i++) {
						upSynProvince = upSynProvince + currSynProvinceArr[i] + "#";
						for (int j=0; j<newSynProvinceArr.length; j++) {
							if (currSynProvinceArr[i].equals(newSynProvinceArr[j]) 
									&& (cp.getSynRadio() + CommonTool.convertInt(currSynNumArr[i]) == 0)) {
								//当前省份同步率等于0才打开
								upSynNum = upSynNum + radio + "#";
								flag = true;
								if (!currSynNumArr[i].equals(radio)) {
									upflag = true;
								}
							}
						}
						if (flag) {
							flag = false;
						} else {
							upSynNum = upSynNum + currSynNumArr[i] + "#";
						}
					}
					
					//将监控省份没在当前扣量配置中的省份追加入更新队列
					for (int i=0; (i<newSynProvinceArr.length && province.length()>0); i++) {
						if (!upSynProvince.contains(newSynProvinceArr[i])) {
							upSynProvince = upSynProvince + newSynProvinceArr[i] + "#";
							upSynNum = upSynNum + radio + "#";
							upflag = true;
						}
					}
					
					if (upflag) {
						
						upSynProvince = upSynProvince.substring(0, upSynProvince.length()-1);
						upSynNum = upSynNum.substring(0, upSynNum.length()-1);
						
						updatecp.setSynOpen(1);
						updatecp.setSynProvince(upSynProvince);
						updatecp.setSynNum(upSynNum);
						logger.info("cpid:" + cpId + ",synProvince:"+upSynProvince
								+",synNum:"+upSynNum+"到时间且同步被关闭，打开同步");
						commonAction.updateCp(updatecp);
					}
					
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content("ok").end();
	}
	
	/**
	 * MM限量监控
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void monit(HttpRequest request, HttpResponse response)
			throws Exception {
		try {
			logger.info("monit请求header信息："+request.allHeaders());
			String content = request.body();
			logger.info("monit请求header信息："+content);
			JSONObject myObj = JSONObject.parseObject(content);
			int openNum = CommonTool.convertInt(myObj.getString("openNum"));
			int closeNum = CommonTool.convertInt(myObj.getString("closeNum"));
			int openCount = CommonTool.convertInt(myObj.getString("openCount"));

			Calendar cal = Calendar.getInstance();
			String createTime = format.format(cal.getTime());
			Map<String,String> resultMap = new HashMap<String, String>();
			List<Integer> priorityList = new ArrayList<Integer>();
			int bcloseNum = 0;
			int bopenNum = 0;

			for (int i = 0; i < openNum; i++) {
				String newAppId = myObj.getString("openNewAppId" + i);
				int notityFee = myObj.getIntValue("openNotityFee" + i);
				int priority = myObj.getIntValue("openPriority" + i);
				MMFeeInfo feeInfo = null;

				feeInfo = commonAction.queryFeeByAppId(newAppId, createTime);
				if (feeInfo.getFees() >= notityFee) {
					resultMap.put("closeAppId" + bcloseNum, newAppId);
					resultMap.put("closePriority" + bcloseNum, priority + "");
					priorityList.add(Integer.valueOf(priority));
					bcloseNum++;
				}
			}
			for (int j = 0; j < openCount; j++) {
				String appId = myObj.getString("openklwAppId" + j);
				String channel = myObj.getString("openChannel" + j);
				int notityNum = myObj.getIntValue("openNotityNum" + j);
				int priority = myObj.getIntValue("openklwPriority" + j);
				MMFeeInfo feeInfo = null;
				feeInfo = commonAction
						.queryFeeByCon(appId, createTime, channel);
				if (feeInfo.getFees() >= notityNum) {
					resultMap.put("closeAppId" + bcloseNum, appId);
					resultMap.put("closePriority" + bcloseNum, priority + "");
					priorityList.add(Integer.valueOf(priority));
					bcloseNum++;
				}
			}
			if (bcloseNum > 0) {
				resultMap.put("closeNum", bcloseNum + "");
				for (int k = 0; k < priorityList.size(); k++) {
					for (int i = 0; i < closeNum; i++) {
						String newAppId = myObj.getString("closeNewAppId" + i);
						int notityFee = myObj.getIntValue("closeNotityFee" + i);
						int priority = myObj.getIntValue("closePriority" + i);
						if (priority == ((Integer) priorityList.get(k))
								.intValue()) {
							MMFeeInfo feeInfo = null;

							feeInfo = commonAction.queryFeeByAppId(newAppId,
									createTime);
							if (feeInfo.getFees() < notityFee) {
								resultMap.put("openAppId" + bopenNum, newAppId);
								resultMap.put("openPriority" + bopenNum,
										priority + "");
								bopenNum++;
								if (bopenNum == bcloseNum)
									break;
							}
						}
					}
					if (bopenNum == bcloseNum)
						break;
				}
				resultMap.put("openNum", bopenNum + "");
				String param = JSON.toJSONString(resultMap);
				String url = "http://127.0.0.1:8081/mmcode/changeMMChannel";
				HttpTool.sendHttp(url, param);
				logger.info("MM强联网通道切换请求url=" + url);
				logger.info("MM强联网通道切换请求param=" + param);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		response.content("ok").end();
	}
	
	
	
	
	
	
	private void executeSpConfig(String spId, String operation) {
		CodeInfo info = new CodeInfo();
		info.setSpId(spId);
		if (ConstantDefine.CODE_CLOSE.equals(operation)) {
			info.setIsOpen(0);
		} else if (ConstantDefine.CODE_OPEN.equals(operation)) {
			info.setIsOpen(1);
		}
		commonAction.updateCodeInfo(info);
	}
	
}
