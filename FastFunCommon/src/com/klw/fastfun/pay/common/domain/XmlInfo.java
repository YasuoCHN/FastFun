package com.klw.fastfun.pay.common.domain;

import java.io.Serializable;

public class XmlInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String submit0;
	private String resultCode;
	private String getSMSVerifyCodeUrl;
	private String submitUrl;
	
	public String getSubmit0() {
		return submit0;
	}
	public void setSubmit0(String submit0) {
		this.submit0 = submit0;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getGetSMSVerifyCodeUrl() {
		return getSMSVerifyCodeUrl;
	}
	public void setGetSMSVerifyCodeUrl(String getSMSVerifyCodeUrl) {
		this.getSMSVerifyCodeUrl = getSMSVerifyCodeUrl;
	}
	public String getSubmitUrl() {
		return submitUrl;
	}
	public void setSubmitUrl(String submitUrl) {
		this.submitUrl = submitUrl;
	}
	

}
