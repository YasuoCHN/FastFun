/**
 * FastFunCommon
 */
package com.klw.fastfun.pay.common.transport;

import java.io.Serializable;

import com.klw.fastfun.pay.common.domain.CPInfo;
import com.klw.fastfun.pay.common.domain.OrderReqInfo;

/**
 * @author klwplayer.com
 *
 *         2015年3月31日
 */
public class OrderT implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private OrderReqInfo order;
	private CPInfo cp;

	public OrderReqInfo getOrder() {
		return order;
	}

	public void setOrder(OrderReqInfo order) {
		this.order = order;
	}

	public CPInfo getCp() {
		return cp;
	}

	public void setCp(CPInfo cp) {
		this.cp = cp;
	}
}
