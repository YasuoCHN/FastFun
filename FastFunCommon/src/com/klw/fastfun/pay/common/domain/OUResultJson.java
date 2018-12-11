package com.klw.fastfun.pay.common.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class OUResultJson implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 发送短信的方式0，text；1，data */
	private String type;
	/** 0成功 */
	private String status;
	/**  */
	private List<Map<String, String>> resultList;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<Map<String, String>> getResultList() {
		return resultList;
	}
	public void setResultList(List<Map<String, String>> resultList) {
		this.resultList = resultList;
	}
}
