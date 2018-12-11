package com.klw.fastfun.pay.common.tool.mm;

/**
 * @author shaojieque 
 * 2013-10-23
 */
public class AppOrderQueryResp {
	private String MsgType;
	private String Version;
	private String hRet;
	private Order_Charge_Schema OrderInfo;

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public String getVersion() {
		return Version;
	}

	public void setVersion(String version) {
		Version = version;
	}

	public String gethRet() {
		return hRet;
	}

	public void sethRet(String hRet) {
		this.hRet = hRet;
	}

	public Order_Charge_Schema getOrderInfo() {
		return OrderInfo;
	}

	public void setOrderInfo(Order_Charge_Schema orderInfo) {
		OrderInfo = orderInfo;
	}
}
