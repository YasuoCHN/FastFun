package com.klw.fastfun.pay.common.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LTPayResultH5Info implements Serializable {
	
	private String appid;
	private String goodsname;
	private String pcorderid;
	private String currency;
	private String pcuserid;
	private String pcprivateinfo;
	private String notifyurl;
	private String transid;
	private String result;
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getTransid() {
		return transid;
	}
	public void setTransid(String transid) {
		this.transid = transid;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getGoodsname() {
		return goodsname;
	}
	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}
	public String getPcorderid() {
		return pcorderid;
	}
	public void setPcorderid(String pcorderid) {
		this.pcorderid = pcorderid;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getPcuserid() {
		return pcuserid;
	}
	public void setPcuserid(String pcuserid) {
		this.pcuserid = pcuserid;
	}
	public String getPcprivateinfo() {
		return pcprivateinfo;
	}
	public void setPcprivateinfo(String pcprivateinfo) {
		this.pcprivateinfo = pcprivateinfo;
	}
	public String getNotifyurl() {
		return notifyurl;
	}
	public void setNotifyurl(String notifyurl) {
		this.notifyurl = notifyurl;
	}

}
