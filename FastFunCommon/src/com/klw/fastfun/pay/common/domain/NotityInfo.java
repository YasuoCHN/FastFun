package com.klw.fastfun.pay.common.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class NotityInfo implements Serializable{
	
	private String url;
	private String param;
	private int count;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

}
