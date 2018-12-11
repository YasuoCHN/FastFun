package com.klw.fastfun.pay.common.domain;

import java.io.Serializable;

public class PayConJson implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 发送短信的方式0，text；1，data */
	private String type;
	/** 短代地址 */
	private String address;
	/** 发送方式为1时的端口 */
	private String port;
	/** 短信内容，1时在服务端byte[]转成字符串*/
	private String content;
	/** 延时 */
	private String delayed;
	/** 重复请求短信地址 */
	private String url;
	/** 每条短信发送次数，默认1 */
	private String count;
	/** 等待时间毫秒 */
	private String wait_time;
	/** 力聚参数 */
	private String cpid;
	/** 力聚参数 */
	private String cpkey;
	/** 力聚参数 */
	private String channelid;
	/** 力聚参数 */
	private String pay_code;
	/** Wap1，支持DDO 计费地址获取url */
	private String url_1;
	/** Wap1，支持DDO 解析web得到的获取验证码地址 */
	private String url_2;
	/** Wap1，支持DDO 解析web得到的计费地址 */
	private String url_3;
	/** 1:get,2:post */
	private String req_method;
	/** 请求方式为post时的请求参数，为get时没有该参数，”#replacedate#”为需要替换的内容 */
	private String req_param;
	
	public String getReq_method() {
		return req_method;
	}
	public void setReq_method(String req_method) {
		this.req_method = req_method;
	}
	public String getReq_param() {
		return req_param;
	}
	public void setReq_param(String req_param) {
		this.req_param = req_param;
	}
	public String getUrl_1() {
		return url_1;
	}
	public void setUrl_1(String url_1) {
		this.url_1 = url_1;
	}
	public String getUrl_2() {
		return url_2;
	}
	public void setUrl_2(String url_2) {
		this.url_2 = url_2;
	}
	public String getUrl_3() {
		return url_3;
	}
	public void setUrl_3(String url_3) {
		this.url_3 = url_3;
	}
	public String getCpid() {
		return cpid;
	}
	public void setCpid(String cpid) {
		this.cpid = cpid;
	}
	public String getCpkey() {
		return cpkey;
	}
	public void setCpkey(String cpkey) {
		this.cpkey = cpkey;
	}
	public String getChannelid() {
		return channelid;
	}
	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}
	public String getPay_code() {
		return pay_code;
	}
	public void setPay_code(String pay_code) {
		this.pay_code = pay_code;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getWait_time() {
		return wait_time;
	}
	public void setWait_time(String wait_time) {
		this.wait_time = wait_time;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDelayed() {
		return delayed;
	}
	public void setDelayed(String delayed) {
		this.delayed = delayed;
	}
	

}
