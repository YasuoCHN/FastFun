package com.klw.fastfun.pay.common.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RegisterRes implements Serializable{
	
	private String orderid;
	private String status;
	private String isscreen;
	private String keyword;
	private String keyport;
	private String isreply;
	private String delaytime;
	private String url;
	private String replycontent;
	private String sms;
	private String smsport;
	private String sendtype;
	private String spid;
	
	public String getSpid() {
		return spid;
	}
	public void setSpid(String spid) {
		this.spid = spid;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIsscreen() {
		return isscreen;
	}
	public void setIsscreen(String isscreen) {
		this.isscreen = isscreen;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getKeyport() {
		return keyport;
	}
	public void setKeyport(String keyport) {
		this.keyport = keyport;
	}
	public String getIsreply() {
		return isreply;
	}
	public void setIsreply(String isreply) {
		this.isreply = isreply;
	}
	public String getDelaytime() {
		return delaytime;
	}
	public void setDelaytime(String delaytime) {
		this.delaytime = delaytime;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getReplycontent() {
		return replycontent;
	}
	public void setReplycontent(String replycontent) {
		this.replycontent = replycontent;
	}
	public String getSms() {
		return sms;
	}
	public void setSms(String sms) {
		this.sms = sms;
	}
	public String getSmsport() {
		return smsport;
	}
	public void setSmsport(String smsport) {
		this.smsport = smsport;
	}
	public String getSendtype() {
		return sendtype;
	}
	public void setSendtype(String sendtype) {
		this.sendtype = sendtype;
	}

}
