/**
 * FastFunCommon
 */
package com.klw.fastfun.pay.common.json;

import java.io.Serializable;

/**
 * @author klwplayer.com
 *
 *         2015年3月31日
 */
public class SynOtherJson implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String orderid;
	private String ffid;
	private String cpparam;
	private String imsi;
	private String imei;
	private int fee;
	private String ip;
	private String status;
	private String phone;
	private String cpid;
	private String transid;
	private String sign;

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTransid() {
		return transid;
	}

	public void setTransid(String transid) {
		this.transid = transid;
	}

	public String getCpid() {
		return cpid;
	}

	public void setCpid(String cpid) {
		this.cpid = cpid;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getFfid() {
		return ffid;
	}

	public void setFfid(String ffid) {
		this.ffid = ffid;
	}

	public String getCpparam() {
		return cpparam;
	}

	public void setCpparam(String cpparam) {
		this.cpparam = cpparam;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
