package com.klw.fastfun.pay.common.domain;

import java.io.Serializable;
import java.util.List;

public class ResSDKBusi implements Serializable {
	/***
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**  */
	private List<ResSDKWap> wap;
	/**  */
	private List<ResSDKSms> sms;
	public List<ResSDKWap> getWap() {
		return wap;
	}
	public void setWap(List<ResSDKWap> wap) {
		this.wap = wap;
	}
	public List<ResSDKSms> getSms() {
		return sms;
	}
	public void setSms(List<ResSDKSms> sms) {
		this.sms = sms;
	}

}
