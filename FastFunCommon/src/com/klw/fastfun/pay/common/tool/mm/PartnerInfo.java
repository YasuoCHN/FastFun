/**
 * ChinaMobileMarket
 * com.orange.game.china.mobile.market.domain
 * PartnerInfo.java
 */
package com.klw.fastfun.pay.common.tool.mm;

import java.io.Serializable;
import java.util.Date;

/**
 * 合作商
 * 
 * @author shaojieque
 * 
 *         2014-2-12
 */
public class PartnerInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private String appId;
	private String prefix;
	private String url;
	private String memo;
	private Date finishTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
