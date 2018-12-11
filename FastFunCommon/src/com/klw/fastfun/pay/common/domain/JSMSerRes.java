package com.klw.fastfun.pay.common.domain;

import java.io.Serializable;

public class JSMSerRes implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String userId;//用户id

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

}
