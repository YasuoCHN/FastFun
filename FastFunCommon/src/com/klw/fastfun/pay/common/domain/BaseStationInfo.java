package com.klw.fastfun.pay.common.domain;

import java.io.Serializable;

public class BaseStationInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 基站信息 */
	private String lac;
	/** 基站信息 */
	private String cid;
	/** 详细描述 */
	private String memo;
	/** 省份 */
	private String province;
	/** 城市 */
	private String city;
	
	public String getLac() {
		return lac;
	}
	public void setLac(String lac) {
		this.lac = lac;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

}
