/**
 * FastFunSystem
 */
package com.klw.fastfun.pay.data.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.juice.orange.game.database.ConnectionResource;
import com.juice.orange.game.database.IJuiceDBHandler;
import com.klw.fastfun.pay.common.domain.MMPayInfo;

/**
 * @author klwplayer.com
 *
 * 2015年3月31日
 */
public class MMPayInfoDAO extends ConnectionResource {
	
	//==============================================================================================
	private static IJuiceDBHandler<MMPayInfo> HANDLE_MMPAY_INFO = new IJuiceDBHandler<MMPayInfo>() {
		@Override
		public MMPayInfo handler(ResultSet rs) throws SQLException {
			MMPayInfo mo = new MMPayInfo();
			mo.setId(rs.getInt("id"));
			mo.setOrderId(rs.getString("order_id"));
			mo.setTradeId(rs.getString("trade_id"));
			mo.setAppId(rs.getString("app_id"));
			mo.setPrice(rs.getInt("price"));
			mo.setNum(rs.getInt("num"));
			mo.setTotalPrice(rs.getInt("total_price"));
			return mo;
		}
	};
	
	/**
	 * 添加订单
	 */
	public void addMMPayInfo(MMPayInfo order) {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into t_mmpay_info(order_id, trade_id, app_id, ");
		sql.append("channel_id, action_id, action_time, msisdn, msisdn_fee, ");
		sql.append("pay_code, price, num, total_price, ext, result_status,create_time, update_time) ");
		sql.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now())");
		saveOrUpdate(sql.toString(), order.getOrderId(),
				order.getTradeId(),
				order.getAppId(),
				order.getChannelId(),
				order.getActionId(),
				order.getActionTime(),
				order.getMsisdn(),
				order.getMsisdnFee(),
				order.getPayCode(),
				order.getPrice(),
				order.getNum(),
				order.getTotalPrice(),
				order.getExt(),
				order.getResultStatus());
	}
	
	public MMPayInfo queryMMPayInfo(String orderId, String tradeId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from t_mmpay_info where order_id=? and trade_id=?");
		return queryForObject(sql.toString(), HANDLE_MMPAY_INFO, orderId, tradeId);
	}
}
