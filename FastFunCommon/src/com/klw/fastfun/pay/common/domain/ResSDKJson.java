package com.klw.fastfun.pay.common.domain;

import java.io.Serializable;

public class ResSDKJson implements Serializable {
	/***
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 100 服务器异常  200 正常扣费设置  300 企信通设置(发送短信)  400 没有计费通道 */
	private String flag;
	/** 用户手机号码 */
	private String phnum;
	/** 指令获取结果 */
	private ResSDKResult result;
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getPhnum() {
		return phnum;
	}
	public void setPhnum(String phnum) {
		this.phnum = phnum;
	}
	public ResSDKResult getResult() {
		return result;
	}
	public void setResult(ResSDKResult result) {
		this.result = result;
	}

}
