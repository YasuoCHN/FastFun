package com.klw.fastfun.pay.common.tool.mm;

/**
 * @author shaojieque 
 * 2013-10-23
 */
public class Order_Charge_Schema {
	private String OrderID;
	private String SPServiceID;
	private String UserID;
	private String StartTime;
	private String ExpiredTime;
	private String ValidTimes;

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	public String getSPServiceID() {
		return SPServiceID;
	}

	public void setSPServiceID(String sPServiceID) {
		SPServiceID = sPServiceID;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public String getExpiredTime() {
		return ExpiredTime;
	}

	public void setExpiredTime(String expiredTime) {
		ExpiredTime = expiredTime;
	}

	public String getValidTimes() {
		return ValidTimes;
	}

	public void setValidTimes(String validTimes) {
		ValidTimes = validTimes;
	}
}
