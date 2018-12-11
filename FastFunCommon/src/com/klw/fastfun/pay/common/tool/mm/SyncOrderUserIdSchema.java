/**
 * ChinaMobileMarket
 * com.orange.game.china.mobile.market.domain
 * SyncOrderUserIdSchema.java
 */
package com.klw.fastfun.pay.common.tool.mm;

/**
 * @author shaojieque
 * 2013-10-23
 */
public class SyncOrderUserIdSchema {
	/**
	 * 用户标识类型
	1：用手机号标识
	 */
	private String UserIDType;
	/**
	 * 用户手机号码(不带+86)
	 */
	private String MSISDN;
	 
	public String getUserIDType() {
		return UserIDType;
	}
	public void setUserIDType(String userIDType) {
		UserIDType = userIDType;
	}
	public String getMSISDN() {
		return MSISDN;
	}
	public void setMSISDN(String mSISDN) {
		MSISDN = mSISDN;
	}
}
