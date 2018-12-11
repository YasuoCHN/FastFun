package com.klw.fastfun.pay.data.ao;

import org.apache.log4j.Logger;

import com.juice.orange.game.cached.MemcachedResource;
import com.juice.orange.game.log.LoggerFactory;
import com.klw.fastfun.pay.common.domain.BaseStationInfo;
import com.klw.fastfun.pay.common.domain.CPInfo;
import com.klw.fastfun.pay.common.domain.CodeOnlyInfo;
import com.klw.fastfun.pay.common.domain.CustomInfo;
import com.klw.fastfun.pay.common.domain.MobileInfo;
import com.klw.fastfun.pay.common.domain.OrderReqInfo;
import com.klw.fastfun.pay.common.domain.PayLimitInfo;
import com.klw.fastfun.pay.common.domain.QQInfo;
import com.klw.fastfun.pay.common.domain.SDKUpdateInfo;
import com.klw.fastfun.pay.common.tool.CommonTool;

public class CommonAO extends BaseAO{
	private static Logger logger = LoggerFactory.getLogger(CommonAO.class);
	
	public static final String COM_PREFIX = "comid_";
	public static final String COM_DAYLIMIT = "comdaylimit_";
	public static final String COM_MONTHLIMIT = "commonthlimit_";
	public static final String COM_SDKUPDATE = "comsdkupdate";
	public static final String COM_MOBILE = "commobile_";
	public static final String COM_CODEONLY = "com_codeonly_";
	public static final String COM_MOB_PRO = "com_mob_pro_";
	public static final String COM_MOB = "com_mob_";
	
	public void addCustomInfo(CustomInfo info) {
		CustomInfo reqInfo = getCustomInfoByMobile(info);
		if (reqInfo == null) {
			commonDAO.addCustomInfo(info);
		} else {
			logger.info("该号码和验证码已存在");
		}
	}
	
	public void refreshCache() {
		MemcachedResource.flushAll();
	}
	
	public BaseStationInfo getBaseStation(OrderReqInfo reqInfo) {
		String key = reqInfo.getBscLac()+reqInfo.getBscCid();
		String indexKey = CommonTool.createIndexKey(CommonTool.FASTSER_BASSTA, key);
		BaseStationInfo info = MemcachedResource.get(indexKey);
		if (info != null) return info;
		
		info = commonDAO.getBaseStation(reqInfo);
		if (info != null) MemcachedResource.save(indexKey, info);
		return info;
	}
	
	public QQInfo queryQQInfo(OrderReqInfo reqInfo) {
		String key = reqInfo.getUsername() + reqInfo.getPassword();
		String indexKey = CommonTool.createIndexKey(CommonTool.FASTSER_BASSTA, key);
		QQInfo info = MemcachedResource.get(indexKey);
		if (info != null) return info;
		
		info = commonDAO.queryQQInfo(reqInfo);
		if (info != null) MemcachedResource.save(indexKey, info);
		return info;
	}
	
	public MobileInfo queryBlackMobile(String mobile) {
		String key = mobile;
		String indexKey = CommonTool.createIndexKey(CommonTool.FASTSER_BLAMOB, key);
		MobileInfo info = MemcachedResource.get(indexKey);
		if (info != null) return info;
		
		info = commonDAO.queryBlackMobile(mobile);
		if (info != null) MemcachedResource.save(indexKey, info);
		return info;
	}
	
	public CustomInfo getCustomInfo(CustomInfo reqInfo) {
		String indexKey = createIndexKey(COM_MOBILE+reqInfo.getImsi()+reqInfo.getLocalTab());
		CustomInfo info = MemcachedResource.get(indexKey);
		if (info != null) return info;
		
		info = commonDAO.getCustomInfo(reqInfo);
		if (info != null) MemcachedResource.save(indexKey, info);
		return info;
	}
	
	public CustomInfo getCustomInfoByMobile(CustomInfo reqInfo) {
		String indexKey = createIndexKey(COM_MOBILE+reqInfo.getMobile()+reqInfo.getSmsMessage()+reqInfo.getLocalTab());
		CustomInfo info = MemcachedResource.get(indexKey);
		if (info != null) return info;
		
		info = commonDAO.getCustomInfoByMobile(reqInfo);
		if (info != null) MemcachedResource.save(indexKey, info);
		return info;
	}
	
	public CustomInfo checkCustomerInfo(String imsi) {
		String indexKey = createIndexKey("black_"+imsi);
		CustomInfo info = MemcachedResource.get(indexKey);
		if (info != null) return info;
		
		info = commonDAO.checkCustomerInfo(imsi);
		if (info != null) MemcachedResource.save(indexKey, info);
		return info;
	}
	
	public SDKUpdateInfo getSDKUpdateParam(SDKUpdateInfo reqInfo) {
		String indexKey = createIndexKey(COM_SDKUPDATE+reqInfo.getId());
		SDKUpdateInfo info = MemcachedResource.get(indexKey);
		if (info != null) return info;
		
		info = commonDAO.getSDKUpdateParam(reqInfo);
		if (info != null) MemcachedResource.save(indexKey, info);
		return info;
	}
	
	public PayLimitInfo queryPaidByImsi(OrderReqInfo reqInfo) {
		return commonDAO.queryPaidByImsi(reqInfo);
	}
	
	public PayLimitInfo queryOtherPaidByImsi(OrderReqInfo reqInfo) {
		return commonDAO.queryOtherPaidByImsi(reqInfo);
	}
	
	public void saveExt(String ext) {
		if (ext != null && ext.length() > 0)
			MemcachedResource.save(ext, ext);
	}
	
	public String queryExt(String ffid) {
		return MemcachedResource.get(ffid);
	}
	
	private String createIndexKey(String spId) {
		StringBuilder index = new StringBuilder();
		index.append(COM_PREFIX).append(spId);
		return index.toString();
	}
	
	public CodeOnlyInfo queryCodeOnlyBySpid(String spId) {
		String indexKey = createIndexKey(COM_CODEONLY+spId);
		CodeOnlyInfo info = MemcachedResource.get(indexKey);
		if (info != null) return info;
		
		info = commonDAO.queryCodeOnlyBySpid(spId);
		if (info != null) MemcachedResource.save(indexKey, info);
		return info;
	}
	
	public OrderReqInfo queryOnlyOrder(OrderReqInfo info) {
		String indexKey = createIndexKey(COM_CODEONLY+info.getFfId()+info.getSpId());
		OrderReqInfo resinfo = MemcachedResource.get(indexKey);
		if (resinfo != null) return resinfo;
		
		resinfo = commonDAO.queryOnlyOrder(info);
		if (resinfo != null) MemcachedResource.save(indexKey, resinfo);
		return resinfo;
	}
	
	public void addOnlyOrder(OrderReqInfo info) {
		OrderReqInfo resInfo = queryOnlyOrder(info);
		if (resInfo != null) {
			logger.error(info.getSpId()+"通道代码订单号已存在,ffId=" + info.getFfId());
		} else {
			commonDAO.addOnlyOrder(info);
		}
	}
	
	public void addMobile12306(MobileInfo info) {
		MobileInfo resInfo = query12306commit(info.getPhone()+"");
		if (resInfo != null) {
			logger.error("手机号已存在,mobile=" + info.getPhone());
		} else {
			commonDAO.addMobile12306(info);
		}
	}
	
	public String getCpid(String pid) {
		String indexKey = createIndexKey(COM_CODEONLY+"zc"+pid);
		String resinfo = MemcachedResource.get(indexKey);
		if (resinfo != null) return resinfo;
		
		resinfo = commonDAO.getCpid(pid);
		if (resinfo != null) MemcachedResource.save(indexKey, resinfo);
		return resinfo;
	}
	
	public CPInfo getCpByPid(String pid) {
		String indexKey = createIndexKey(COM_CODEONLY+"zc"+pid);
		CPInfo resinfo = MemcachedResource.get(indexKey);
		if (resinfo != null) return resinfo;
		
		resinfo = commonDAO.getCpByPid(pid);
		if (resinfo != null) MemcachedResource.save(indexKey, resinfo);
		return resinfo;
	}
	
	
	
	public MobileInfo getProvinceByPhone(int mobile) {
		String indexKey = createIndexKey(COM_MOB_PRO+mobile);
		MobileInfo resinfo = MemcachedResource.get(indexKey);
		if (resinfo != null) return resinfo;
		
		resinfo = commonDAO.getProvinceByPhone(mobile);
		if (resinfo != null) MemcachedResource.save(indexKey, resinfo);
		return resinfo;
	}
	
	public MobileInfo query12306commit(String mobile) {
		String indexKey = createIndexKey(COM_MOB+mobile);
		MobileInfo resinfo = MemcachedResource.get(indexKey);
		if (resinfo != null) return resinfo;
		
		resinfo = commonDAO.query12306commit(mobile);
		if (resinfo != null) MemcachedResource.save(indexKey, resinfo);
		return resinfo;
	}
	
	public void removeMemcached(String key) {
		MemcachedResource.remove(key);
	}

}
