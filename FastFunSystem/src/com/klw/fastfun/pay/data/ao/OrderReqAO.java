/**
 * FastFunSystem
 */
package com.klw.fastfun.pay.data.ao;

import java.util.List;

import com.juice.orange.game.cached.MemcachedResource;
import com.klw.fastfun.pay.common.domain.CodeInfo;
import com.klw.fastfun.pay.common.domain.OrderReqInfo;
import com.klw.fastfun.pay.common.domain.ResSDKFilter;
import com.klw.fastfun.pay.common.domain.ResSDKReport;
import com.klw.fastfun.pay.common.domain.SmsFilter;
import com.klw.fastfun.pay.common.tool.CommonTool;

/**
 * @author klwplayer.com
 *
 * 2015年3月30日
 */
public class OrderReqAO extends BaseAO {
	
	public static final String ORDER_COM_PREFIX = "_order_com";
	public static final String ORDER_SDK_PREFIX = "_order_sdk";
	
	/** 根据ff_id订单号查找对应的计费信息+date*/
	public OrderReqInfo queryOrderReqInfoDate(String ffId) {
		String key = createIndexKey(ORDER_COM_PREFIX, ffId);
		OrderReqInfo info = MemcachedResource.get(key);
		if (info != null) return info;
		
		info = orderReqDAO.queryOrderReqInfoDate(ffId);
		if (info != null)
			MemcachedResource.save(key, info);
		return info;
	}
	/** 根据ff_id订单号查找对应的计费信息*/
	public OrderReqInfo queryOrderReqInfo(String ffId) {
		String key = createIndexKey(ORDER_COM_PREFIX, ffId);
		OrderReqInfo info = MemcachedResource.get(key);
		if (info != null) return info;
		
		info = orderReqDAO.queryOrderReqInfo(ffId);
		if (info != null)
			MemcachedResource.save(key, info);
		return info;
	}
	
	public OrderReqInfo queryOrderInMemch(String ffId) {
		String key = createIndexKey(ORDER_COM_PREFIX, ffId);
		OrderReqInfo info = MemcachedResource.get(key);
		return info;
	}
	
	public void updateOrderInMemch(OrderReqInfo info) {
		String key = createIndexKey(ORDER_COM_PREFIX, info.getFfId());
		MemcachedResource.save(key, info);
	}
	
	/** 添加计费信息*/
	public void addklwOrderReqInfo(OrderReqInfo info) {
		orderReqDAO.addklwOrderReqInfo(info);
	}
	/** 添加计费信息日期*/
	public void addOrderReqInfoDate(OrderReqInfo info) {
		OrderReqInfo reqInfo = queryOrderReqInfo(info.getFfId());
		if (reqInfo == null) {
			orderReqDAO.addOrderReqInfoDate(info);
		}
	}
	/** 添加计费信息*/
	public void addOrderReqInfo(OrderReqInfo info) {
		OrderReqInfo reqInfo = queryOrderReqInfo(info.getFfId());
		if (reqInfo == null) {
			orderReqDAO.addOrderReqInfo(info);
		}
	}
	
	/** 添加计费信息*/
	public void addOrderReqInfoAll(OrderReqInfo info) {
		orderReqDAO.addOrderReqInfoAll(info);
	}
	
	/** 添加计费信息*/
	public void addOrderReqInfo1(OrderReqInfo info) {
		orderReqDAO.addOrderReqInfo1(info);
	}
	
	
	public void updateSynStatus(String ffId, int isSuccess, int synStatus) {
		orderReqDAO.updateSynStatus(ffId, isSuccess, synStatus);
		String key = createIndexKey(ORDER_COM_PREFIX, ffId);
		MemcachedResource.save(key, null);
	}
	
	public void updateOrderZFB(String ffId, String c, String sn, int isSuccess, int synStatus) {
		orderReqDAO.updateOrderZFB(ffId, c, sn, isSuccess, synStatus);
		String key = createIndexKey(ORDER_COM_PREFIX, ffId);
		MemcachedResource.save(key, null);
	}
	
	public void updateOrderData(String mobile, String ffId, int isSuccess, int synStatus) {
		orderReqDAO.updateOrderData(mobile, ffId, isSuccess, synStatus);
		String key = createIndexKey(ORDER_COM_PREFIX, ffId);
		MemcachedResource.save(key, null);
	}
	
	public void updateSDKResult(String ffId, String sdkResult) {
		orderReqDAO.updateSDKResult(ffId, sdkResult);
		String key = createIndexKey(ORDER_COM_PREFIX, ffId);
		MemcachedResource.save(key, null);
	}
	
	public void addResSDKReport(ResSDKReport info) {
		orderReqDAO.addResSDKReport(info);
	}
	
	public List<ResSDKFilter> queryFilterBySpid(String spId) {
		String key = createIndexKey(ORDER_COM_PREFIX, spId);
		List<ResSDKFilter> info = MemcachedResource.get(key);
		if (info != null)
			return info;
		
		info = orderReqDAO.queryFilterBySpid(spId);
		if (info != null)
			MemcachedResource.save(key, info);
		return info;
	}
	
	public List<SmsFilter> querySmsFilterBySpid(String spId) {
		String key = createIndexKey(ORDER_SDK_PREFIX, spId);
		List<SmsFilter> info = MemcachedResource.get(key);
		if (info != null)
			return info;

		info = orderReqDAO.querySmsFilterBySpid(spId);
		if (info != null)
			MemcachedResource.save(key, info);
		return info;
	}
	
	
	private String createIndexKey(String key, String value) {
		StringBuilder index = new StringBuilder();
		index.append(value).append(key);
		return index.toString();
	}

	public void updateOrderKL(String cpid, String isSuccess, int synStatus) {
		orderReqDAO.updateOrderKL(cpid, isSuccess, synStatus);
	}
	
	public void updateOrderInfo(OrderReqInfo info) {
		orderReqDAO.updateOrderInfo(info);
		OrderReqInfo resinfo = queryOrderInMemch(info.getFfId());
		if (resinfo != null) {
			if (info.getIsSuccess() > 0) {
				resinfo.setIsSuccess(info.getIsSuccess());
			}
			if (info.getMobile() != null) {
				resinfo.setMobile(info.getMobile());
			}
			if (info.getSpId() != null) {
				resinfo.setSpId(info.getSpId());
			}
			if (info.getCtech() != null) {
				resinfo.setCtech(info.getCtech());
			}
			if (info.getCpParam() != null) {
				resinfo.setCpParam(info.getCpParam());
			}
			if (info.getPmodel() != null) {
				resinfo.setPmodel(info.getPmodel());
			}
			if (info.getPck() != null) {
				resinfo.setPck(info.getPck());
			}
			if (info.getApp() != null) {
				resinfo.setApp(info.getApp());
			}
			if (info.getSdkVer() != null) {
				resinfo.setSdkVer(info.getSdkVer());
			}
			if (info.getSynStatus() != 0) {
				resinfo.setSynStatus(info.getSynStatus());
			}
			if (info.getNewffid() != null) {
				resinfo.setNewffid(info.getNewffid());
			}
			updateOrderInMemch(resinfo);
		}
	}
	public void updateOrderInfoDate(OrderReqInfo info) {
		orderReqDAO.updateOrderInfoDate(info);
		OrderReqInfo resinfo = queryOrderInMemch(info.getFfId());
		if (resinfo != null) {
			if (info.getIsSuccess() > 0) {
				resinfo.setIsSuccess(info.getIsSuccess());
			}
			if (info.getMobile() != null) {
				resinfo.setMobile(info.getMobile());
			}
			if (info.getSpId() != null) {
				resinfo.setSpId(info.getSpId());
			}
			if (info.getCtech() != null) {
				resinfo.setCtech(info.getCtech());
			}
			if (info.getCpParam() != null) {
				resinfo.setCpParam(info.getCpParam());
			}
			if (info.getPmodel() != null) {
				resinfo.setPmodel(info.getPmodel());
			}
			if (info.getPck() != null) {
				resinfo.setPck(info.getPck());
			}
			if (info.getApp() != null) {
				resinfo.setApp(info.getApp());
			}
			if (info.getSdkVer() != null) {
				resinfo.setSdkVer(info.getSdkVer());
			}
			if (info.getSynStatus() != 0) {
				resinfo.setSynStatus(info.getSynStatus());
			}
			if (info.getNewffid() != null) {
				resinfo.setNewffid(info.getNewffid());
			}
			updateOrderInMemch(resinfo);
		}
	}
	
	public void updateOrderByOther(OrderReqInfo info) {
		orderReqDAO.updateOrderByOther(info);
		String key = createIndexKey(ORDER_COM_PREFIX, info.getFfId());
		MemcachedResource.save(key, null);
	}
	
	public OrderReqInfo queryOrderByMobile(String mobile, String ffId) {
		String indexKey = createIndexKey(ORDER_COM_PREFIX, mobile + ffId.substring(0,2));
		OrderReqInfo resinfo = MemcachedResource.get(indexKey);
		if (resinfo != null) return resinfo;
		
		resinfo = orderReqDAO.queryOrderByMobile(mobile, ffId);
		if (resinfo != null) MemcachedResource.save(indexKey, resinfo);
		return resinfo;
	}
	
	public OrderReqInfo queryMiGuOrder(OrderReqInfo info) {
		String indexKey = CommonTool.createIndexKey(CommonTool.FASTSER_MGORDER, info.getImsi());
		OrderReqInfo resinfo = MemcachedResource.get(indexKey);
		if (resinfo != null) return resinfo;
		
		resinfo = orderReqDAO.queryMiGuOrder(info);
		if (resinfo != null) MemcachedResource.save(indexKey, resinfo);
		return resinfo;
	}
	
	public List<CodeInfo> queryMiGuOrderList(String querycon, OrderReqInfo info) {
		String indexKey = CommonTool.createIndexKey(CommonTool.FASTSER_MGORDER, querycon);
		List<CodeInfo> codes = MemcachedResource.get(indexKey);
		if (codes != null) return codes;
		
		codes = orderReqDAO.queryMiGuOrderList(querycon.replace("#", "','"));
		if (codes != null) {
			MemcachedResource.save(indexKey, codes);
			String[] tmp = querycon.split("#");
			for (String s : tmp) {
				MemcachedResource.save(CommonTool.FASTSER_MGKEY+s+info.getCpId(), indexKey);
			}
		}
		return codes;
	}
	
	public List<CodeInfo> queryPollOrderList(String querycon, OrderReqInfo info) {
		String indexKey = CommonTool.createIndexKey(CommonTool.FASTSER_POLLORDER, querycon);
		List<CodeInfo> codes = MemcachedResource.get(indexKey);
		if (codes != null) return codes;
		
		codes = orderReqDAO.queryMiGuOrderList(querycon.replace("#", "','"));
		if (codes != null) {
			MemcachedResource.save(indexKey, codes);
			String[] tmp = querycon.split("#");
			for (String s : tmp) {
				MemcachedResource.save(CommonTool.FASTSER_POLLKEY+s+info.getCpId(), indexKey);
			}
		}
		return codes;
	}
	
	public OrderReqInfo queryOrderByCondition(OrderReqInfo info) {
		String key = info.getMobile() + info.getFfId().substring(0,2) + info.getCpId() + info.getSpId();
		String code = info.getCode();
		if (code != null && code.length() > 0) {
			key = key + code;
		}
		String indexKey = createIndexKey(ORDER_COM_PREFIX, key);
		OrderReqInfo resinfo = MemcachedResource.get(indexKey);
		if (resinfo != null) return resinfo;
		
		resinfo = orderReqDAO.queryOrderByCondition(info);
		if (resinfo != null) MemcachedResource.save(indexKey, resinfo);
		return resinfo;
	}
}
