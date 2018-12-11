/**
 * FastFunSystem
 */
package com.klw.fastfun.pay.data.ao;

import org.apache.log4j.Logger;

import com.juice.orange.game.cached.MemcachedResource;
import com.juice.orange.game.log.LoggerFactory;
import com.klw.fastfun.pay.common.domain.MMPayInfo;

/**
 * @author klwplayer.com
 *
 *         2015年3月31日
 */
public class MMPayInfoAO extends BaseAO {
	private static Logger logger = LoggerFactory.getLogger(MMPayInfoAO.class);

	public static final String MM_PREFIX = "mmorder_";

	public void addMMPayInfo(MMPayInfo info) {
		MMPayInfo existOrder = queryMMPaynInfo(info.getOrderId(),
				info.getTradeId());
		if (existOrder != null) {
			logger.error("MM订单号已存在,orderId=" + info.getOrderId());
		} else {
			mmPayInfoDAO.addMMPayInfo(info);
		}
	}

	public MMPayInfo queryMMPaynInfo(String orderId, String tradeId) {
		String key = createIndexKey(MM_PREFIX, orderId, tradeId);
		MMPayInfo info = MemcachedResource.get(key);
		if (info != null)
			return info;

		info = mmPayInfoDAO.queryMMPayInfo(orderId, tradeId);
		if (info != null)
			MemcachedResource.save(key, info);
		return info;
	}

	private String createIndexKey(String key, String appId, String channel) {
		StringBuilder index = new StringBuilder();
		index.append(appId).append(channel).append(key);
		return index.toString();
	}
}
