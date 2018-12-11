package com.klw.fastfun.pay.common.domain;

import java.io.Serializable;

public class ReplyJson implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 验证码收件地址 */
	private String re_addr;
	/** 发送地址 */
	private String se_addr;
	/** 回复内容 */
	private String content;
	/** 回复正则表达式，回复内容为空，则调用正则表达式 */
	private String regular;
	
	public String getRe_addr() {
		return re_addr;
	}
	public void setRe_addr(String re_addr) {
		this.re_addr = re_addr;
	}
	public String getSe_addr() {
		return se_addr;
	}
	public void setSe_addr(String se_addr) {
		this.se_addr = se_addr;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRegular() {
		return regular;
	}
	public void setRegular(String regular) {
		this.regular = regular;
	}
	
}
