package com.klw.fastfun.pay.common.domain;

public class OtherSDZ1Head {
	
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	/** 交易码 */
	private String tranCode;
	/** 渠道版本 */
	private String channelVersion;
	/** 接口版本 */
	private String apiVersion;
	/** 渠道编号 */
	private String channelNo;
	/** 渠道日期 */
	private String channelDate;
	/** 渠道时间 */
	private String channelTime;
	/** 渠道流水号 */
	private String channelSerial;
	/** 用户ID */
	private String userId;
	/** 设备指纹 */
	private String deviceFingerPrint;
	/** 登陆Token */
	private String loginToken;
	public String getTranCode() {
		return tranCode;
	}
	public void setTranCode(String tranCode) {
		this.tranCode = tranCode;
	}
	public String getChannelVersion() {
		return channelVersion;
	}
	public void setChannelVersion(String channelVersion) {
		this.channelVersion = channelVersion;
	}
	public String getApiVersion() {
		return apiVersion;
	}
	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}
	public String getChannelNo() {
		return channelNo;
	}
	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}
	public String getChannelDate() {
		return channelDate;
	}
	public void setChannelDate(String channelDate) {
		this.channelDate = channelDate;
	}
	public String getChannelTime() {
		return channelTime;
	}
	public void setChannelTime(String channelTime) {
		this.channelTime = channelTime;
	}
	public String getChannelSerial() {
		return channelSerial;
	}
	public void setChannelSerial(String channelSerial) {
		this.channelSerial = channelSerial;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDeviceFingerPrint() {
		return deviceFingerPrint;
	}
	public void setDeviceFingerPrint(String deviceFingerPrint) {
		this.deviceFingerPrint = deviceFingerPrint;
	}
	public String getLoginToken() {
		return loginToken;
	}
	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}
	

}
