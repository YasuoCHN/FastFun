/**
 * MMFeeServer
 */
package com.klw.fastfun.pay.data.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.juice.orange.game.database.ConnectionResource;
import com.juice.orange.game.database.IJuiceDBHandler;
import com.klw.fastfun.pay.common.domain.CPFeeInfo;
import com.klw.fastfun.pay.common.domain.MMFeeInfo;

/**
 * @author klwplayer.com
 *
 *         2015年1月26日
 */
public class AlarmDAO extends ConnectionResource {
	
	private static IJuiceDBHandler<MMFeeInfo> HANDLER_MM_INFO = new IJuiceDBHandler<MMFeeInfo>() {
		@Override
		public MMFeeInfo handler(ResultSet rs) throws SQLException {
			MMFeeInfo info = new MMFeeInfo();
			info.setMmId(rs.getString("mmId"));
			info.setFees(rs.getInt("fees"));
			return info;
		}
	};
	
	private static IJuiceDBHandler<CPFeeInfo> HANDLER_CPFEE_INFO = new IJuiceDBHandler<CPFeeInfo>() {
		@Override
		public CPFeeInfo handler(ResultSet rs) throws SQLException {
			CPFeeInfo info = new CPFeeInfo();
			info.setCpId(rs.getString("cp_id"));
			info.setFees(rs.getInt("fees"));
			info.setProvince(rs.getString("pro"));
			return info;
		}
	};
	
	public MMFeeInfo getAppFee(String appId, String start, String end) {
		StringBuilder sql = new StringBuilder();
		sql.append("select app_id mmId, sum(price) fees from t_mmpay_info ")
		.append("where app_id=? and create_time>? and create_time<? and order_id <> '00000000000000000000' ");
		return queryForObject(sql.toString(), HANDLER_MM_INFO, appId, start, end);
	}
	
	public MMFeeInfo queryFeeByAppId(String appId, String createTime) {
		StringBuilder sql = new StringBuilder();
		sql.append("select app_id mmId, sum(price) fees from t_mmpay_info ")
		.append("where app_id=? and create_time>? and order_id <> '00000000000000000000' ");
		return queryForObject(sql.toString(), HANDLER_MM_INFO, appId, createTime);
	}
	
	public MMFeeInfo queryFeeByCon(String appId, String createTime, String channel) {
		StringBuilder sql = new StringBuilder();
		sql.append("select app_id mmId, sum(price) fees from t_mmpay_info ")
		.append("where app_id=? and channel_id=? and create_time>? and order_id <> '00000000000000000000' ");
		return queryForObject(sql.toString(), HANDLER_MM_INFO, appId, channel, createTime);
	}
	
	public MMFeeInfo queryFeeBySpid(String spId, String createTime) {
		StringBuilder sql = new StringBuilder();
		sql.append("select sp_id mmId, sum(fee) fees from t_order_req_").append(createTime.substring(5,7))
		.append(" where sp_id=? and create_time>? and is_success=1 and syn_status<>4 ");
		return queryForObject(sql.toString(), HANDLER_MM_INFO, spId, createTime);
	}
	
	public List<CPFeeInfo> queryFeeByCpid(String cpId, String createTime) {
		StringBuilder sql = new StringBuilder();
		sql.append("select cp_id, left(province,2) pro, sum(fee) fees from t_order_req_").append(createTime.substring(5,7))
		.append(" where cp_id=? and create_time>? and is_success=1 and syn_status<>4 ")
		.append(" group by cp_id,pro ");
		return queryForList(sql.toString(), HANDLER_CPFEE_INFO, cpId, createTime);
	}
	
	public MMFeeInfo queryCountBySpid(String spId, String createTime) {
		StringBuilder sql = new StringBuilder();
		sql.append("select sp_id mmId, count(sp_id) fees from t_order_req_").append(createTime.substring(5,7))
		.append(" where sp_id=? and create_time>? and is_success=1 and syn_status<>4 ");
		return queryForObject(sql.toString(), HANDLER_MM_INFO, spId, createTime);
	}
	
	public List<CPFeeInfo> queryCountByCpid(String cpId, String createTime) {
		StringBuilder sql = new StringBuilder();
		sql.append("select cp_id, left(province,2) pro, count(cp_id) fees from t_order_req_").append(createTime.substring(5,7))
		.append(" where cp_id=? and create_time>? and is_success=1 and syn_status<>4 ")
		.append(" group by cp_id,pro ");
		return queryForList(sql.toString(), HANDLER_CPFEE_INFO, cpId, createTime);
	}
}
