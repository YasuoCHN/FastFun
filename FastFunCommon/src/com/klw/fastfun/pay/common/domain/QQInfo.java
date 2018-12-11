package com.klw.fastfun.pay.common.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class QQInfo implements Serializable {
	
	/** QQ号 */
	private String username;
	/** QQ密码 */
	private String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
