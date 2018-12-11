/**
 * MMFeeServer
 */
package com.klw.fastfun.pay.data.ao;

import java.util.List;

import com.klw.fastfun.pay.common.domain.CPFeeInfo;
import com.klw.fastfun.pay.common.domain.MMFeeInfo;

/**
 * @author klwplayer.com
 *
 * 2015年1月26日
 */
public class AlarmAO extends BaseAO {
	
	public MMFeeInfo getAppFee(String appId, String start, String end) {
		return alarmDAO.getAppFee(appId, start, end);
	}
	
	public MMFeeInfo queryFeeByAppId(String appId,String createTime) {
		return alarmDAO.queryFeeByAppId(appId,createTime);
	}
	
	public MMFeeInfo queryFeeByCon(String appId,String createTime, String channel) {
		return alarmDAO.queryFeeByCon(appId,createTime,channel);
	}
	
	public MMFeeInfo queryFeeBySpid(String spId,String createTime) {
		return alarmDAO.queryFeeBySpid(spId,createTime);
	}
	
	public List<CPFeeInfo> queryFeeByCpid(String cpId,String createTime) {
		return alarmDAO.queryFeeByCpid(cpId,createTime);
	}
	
	public MMFeeInfo queryCountBySpid(String spId,String createTime) {
		return alarmDAO.queryCountBySpid(spId,createTime);
	}
	
	public List<CPFeeInfo> queryCountByCpid(String cpId,String createTime) {
		return alarmDAO.queryCountByCpid(cpId,createTime);
	}
}
