package com.klw.fastfun.pay.data.ao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.juice.orange.game.cached.MemcachedResource;
import com.klw.fastfun.pay.common.domain.CPInfo;
import com.klw.fastfun.pay.common.domain.CodeInfo;
import com.klw.fastfun.pay.common.domain.OrderReqInfo;
import com.klw.fastfun.pay.common.domain.PayCodeInfo;
import com.klw.fastfun.pay.common.domain.PayReqInfo;
import com.klw.fastfun.pay.common.tool.CommonTool;
import com.klw.fastfun.pay.common.tool.ConstantDefine;

/***
 * 
 * @author klwplayer.com
 *
 * 2015年3月30日
 */
public class CodeHelperAO extends BaseAO{
	public static final String SP_PREFIX = "spid_";
	
	public List<CodeInfo> collectAllCode(String localSpId, String province, String fee) {
		List<CodeInfo> codes = new ArrayList<CodeInfo>();
		
		String[] spIds = localSpId.split("\\#");
		for(String spId : spIds) {
			CodeInfo info = collectCodeInfo(spId, province);
			if (info != null) {
				if (isAdapter(info, province, fee)) codes.add(info);
			}
		}
		
		return codes;
	}
	
	public List<CodeInfo> collectSdkAllCode(String localSpId, String province) {
		List<CodeInfo> codes = new ArrayList<CodeInfo>();
		
		String[] spIds = localSpId.split("\\#");
		for(String spId : spIds) {
			CodeInfo info = collectCodeInfo(spId, province);
			if (info != null) {
				if (isSdkAdapter(info, province)) codes.add(info);
			}
		}
		
		return codes;
	}
	
	public Map<String,String> queryBalanceByCon(String cpid, String spid) {
		return codeHelperDAO.queryBalanceByCon(cpid, spid);
	}
	public String createOrder(Map<String,String> map) {
		return codeHelperDAO.createOrder(map);
	}
	public String querySpIdByCpId(String cpid) {
		return codeHelperDAO.querySpIdByCpId(cpid);
	}
	public Map<String,String> selectDaifu(String spid) {
		return codeHelperDAO.selectDaifu(spid);
	}
	public void updateOrder(Map<String,String> map) {
		codeHelperDAO.updateOrder(map);
	}
	
	public List<CodeInfo> collectklwAllCode(String localSpId, String province) {
		List<CodeInfo> codes = new ArrayList<CodeInfo>();
		
		String[] spIds = localSpId.split("\\#");
		for(String spId : spIds) {
			CodeInfo info = collectCodeInfo(spId, province);
			if (info != null) {
				if (isklwAdapter(info, province)) codes.add(info);
			}
		}
		
		return codes;
	}
	
	public List<CodeInfo> collectOtherCode(OrderReqInfo reqInfo, CPInfo cpInfo) {
		List<CodeInfo> codes = new ArrayList<CodeInfo>();
		
		String localSpId = cpInfo.getLocalSpId();
		String[] spIds = localSpId.split("\\#");
		for(String spId : spIds) {
			CodeInfo info = collectCodeInfo(spId,reqInfo.getProvince());
			if (info != null) {
				if (isklwAdapter(info, reqInfo.getProvince())) {
					String allowfee = info.getFee();
					if (allowfee != null && allowfee.length() > 0 && allowfee.matches(CommonTool.NUM_REGEX) 
							&& reqInfo.getFee() > CommonTool.convertInt(allowfee)) {
						continue;
					}
					codes.add(info);
				}
			}
		}
		
		return codes;
	}
	
	public List<CodeInfo> collectPointCode(OrderReqInfo reqInfo, CPInfo cpInfo) {
		List<CodeInfo> codes = new ArrayList<CodeInfo>();
		String spId = reqInfo.getSpId();
		String localSpId = cpInfo.getLocalSpId();
		if (spId != null && spId.length() > 0 && localSpId.contains(spId)) {
			CodeInfo info = collectCodeInfo(spId,reqInfo.getProvince());
			if (info != null) codes.add(info);
		}
		
		return codes;
	}
	
	public List<CodeInfo> collectCodes(String localSpId, String province, String fee, int serVer) {
		List<CodeInfo> codes = new ArrayList<CodeInfo>();
		
		String[] spIds = localSpId.split("\\#");
		for(String spId : spIds) {
			CodeInfo info = getCodeInfo(spId, serVer);
			if (info != null) {
				if (isAdapter(info, province, fee)) codes.add(info);
			}
		}
		
		return codes;
	}
	
	public List<CodeInfo> collectSpecifyCodes(String localSpId, String province, String fee, int serVer,
			String specify) {
		List<CodeInfo> codes = new ArrayList<CodeInfo>();
		
		String[] spIds = localSpId.split("\\#");
		for(String spId : spIds) {
			if (!spId.contains(specify)) continue;
			CodeInfo info = getCodeInfo(spId, serVer);
			if (info != null) {
				if (isAdapter(info, province, fee)) codes.add(info);
			}
		}
		
		return codes;
	}
	
	public CodeInfo collectCodeInfo(String spId, String province) {
		String indexKey = createIndexKey(spId+"_"+province);
		CodeInfo info = MemcachedResource.get(indexKey);
		if (info != null) return info;
		
		info = codeHelperDAO.queryCodeBySpId(spId);
		if (info != null) MemcachedResource.save(indexKey, info);
		
		return info;
	}
	
	public CodeInfo queryCodeInfoBySpid(String spId) {
		String indexKey = createIndexKey(spId+"_all");
		CodeInfo info = MemcachedResource.get(indexKey);
		if (info != null) return info;
		
		info = codeHelperDAO.queryCodeBySpid(spId);//关闭的通道也会查询
		if (info != null) MemcachedResource.save(indexKey, info);
		
		return info;
	}
	
	public void updateCodeInfo(CodeInfo info){
		codeHelperDAO.updateCodeInfo(info);
		MemcachedResource.flushAll();
	}
	
	public CodeInfo getCodeInfo(String spId, int serVer) {
		String indexKey = createIndexKey(spId+"_"+serVer);
		CodeInfo info = MemcachedResource.get(indexKey);
		if (info != null) return info;
		
		info = codeHelperDAO.queryCodeInfo(spId, serVer);
		if (info != null) MemcachedResource.save(indexKey, info);
		
		return info;
	}
	
	public PayCodeInfo getPayCodes(PayReqInfo reqInfo) {
		String indexKey = createIndexKey(reqInfo.getProvince()+"_"+reqInfo.getPrice());
		PayCodeInfo info = MemcachedResource.get(indexKey);
		if (info != null) return info;
		
		List<PayCodeInfo> infoList = codeHelperDAO.getPayCodes(reqInfo);
		
		if (infoList != null && infoList.size() > 0) {
			for (int i=0; i<infoList.size(); i++) {
				boolean flag = isAdapter1(infoList.get(i),reqInfo.getProvince(),reqInfo.getPrice());
				if (flag) {
					info = infoList.get(i);
					break;
				}
			}
		}
		if (info != null) MemcachedResource.save(indexKey, info);
		
		return info;
	}
	
	/**
	 * 根据省份和价格判断通道是否满足要求
	 */
	public boolean isAdapter(CodeInfo info, String province, String fee) {
		boolean result = false;
		
		if (fee == null || info == null) return result;
		
		// 1:判断省份是否合适
		if (info.getProvinceType() == ConstantDefine.USE_TYPE_HIDE) {
			result = info.getProvinceHide().contains(province)?false:true;
		} else if (info.getProvinceType() == ConstantDefine.USE_TYPE_OPEN) {
			result = info.getProvinceOpen().contains(province)?true:false;
		} else {
			result = true;
		}
		
		if (!result) return result;
		// 2:判断价格是否合适
		return info.getFee().contains(fee+"#");
	}
	
	/**
	 * 根据省份判断通道是否满足要求
	 */
	public boolean isSdkAdapter(CodeInfo info, String province) {
		boolean result = false;
		
		// 1:判断省份是否合适
		if (info.getProvinceType() == ConstantDefine.USE_TYPE_HIDE) {
			result = info.getProvinceHide().contains(province)?false:true;
		} else if (info.getProvinceType() == ConstantDefine.USE_TYPE_OPEN) {
			result = info.getProvinceOpen().contains(province)?true:false;
		} else {
			result = true;
		}
		
		return result;
	}
	
	/**
	 * 根据省份判断通道是否满足要求
	 */
	public boolean isklwAdapter(CodeInfo info, String province) {
		boolean result = false;
		
		if (info == null) return result;
		
		// 1:判断省份是否合适
		if (info.getProvinceType() == ConstantDefine.USE_TYPE_HIDE) {
			result = info.getProvinceHide().contains(province)?false:true;
		} else if (info.getProvinceType() == ConstantDefine.USE_TYPE_OPEN) {
			result = info.getProvinceOpen().contains(province)?true:false;
		} else {
			result = true;
		}
		
		return result;
	}
	
	/**
	 * 根据省份和价格判断通道是否满足要求
	 */
	public boolean isAdapter1(PayCodeInfo info, String province, String fee) {
		boolean result = false;
		
		if (fee == null || info == null) return result;
		
		// 1:判断省份是否合适
		if (info.getProvinceType() == ConstantDefine.USE_TYPE_HIDE) {
			result = info.getProvinceHide().contains(province)?false:true;
		} else if (info.getProvinceType() == ConstantDefine.USE_TYPE_OPEN) {
			result = info.getProvinceOpen().contains(province)?true:false;
		} else {
			result = true;
		}
		 
		if (!result) return result;
		// 2:判断价格是否合适
		return info.getPrice().contains(fee+"#");
	}
	
	private String createIndexKey(String spId) {
		StringBuilder index = new StringBuilder();
		index.append(SP_PREFIX).append(spId);
		return index.toString();
	}
	
}
