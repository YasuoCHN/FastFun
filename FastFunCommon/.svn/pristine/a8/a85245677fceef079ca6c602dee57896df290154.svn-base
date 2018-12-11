package com.klw.fastfun.pay.common.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SmsFilter implements Serializable{
	
	/** 0:无需拦截下行，1:需要拦截下行，2：拦截的下行通过backurl参数中的链接传回后台 */
	private String isscreen;
	/** isscreen不等于0时存在，下行拦截端口号，多组端口号拦截用“#”号分隔，模糊匹配以给定的内容为开头的端口号全部拦截 */
	private String keyport;
	/** isscreen不等于0时存在，下行拦截关键字，需要匹配多组关键字用“#”号分隔，模糊匹配 */
	private String keyword;
	/** isscreen不等于0时存在，2：回复指定的端口号，1：需要回复拦截的下行，0：无需回复 */
	private String isreply;
	/** isreplay=1或2时存在，回复内容  */
	private String replycontent;
	/** isreplay=2时存在，回复指定端口号 */
	private String replysmsport;
	/** isscreen=2时存在，通过连接传回拦截的下行内容连接后添加orderid&sms=url编码后的短信内容，返回小写ok表示收到上传的内容 */
	private String backurl;
	/** 日限 */
	private int day_limit;
	/** 月限 */
	private int month_limit;
	/** 日月限开关 */
	private int pay_limit_open;
	
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
	public String getIsscreen() {
		return isscreen;
	}
	public void setIsscreen(String isscreen) {
		this.isscreen = isscreen;
	}
	public String getKeyport() {
		return keyport;
	}
	public void setKeyport(String keyport) {
		this.keyport = keyport;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getIsreply() {
		return isreply;
	}
	public void setIsreply(String isreply) {
		this.isreply = isreply;
	}
	public String getReplycontent() {
		return replycontent;
	}
	public void setReplycontent(String replycontent) {
		this.replycontent = replycontent;
	}
	public String getReplysmsport() {
		return replysmsport;
	}
	public void setReplysmsport(String replysmsport) {
		this.replysmsport = replysmsport;
	}
	public String getBackurl() {
		return backurl;
	}
	public void setBackurl(String backurl) {
		this.backurl = backurl;
	}
	
}
