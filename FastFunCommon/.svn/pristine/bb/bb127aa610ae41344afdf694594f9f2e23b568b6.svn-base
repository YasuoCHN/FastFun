package com.klw.fastfun.pay.common.domain;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class PayContentJson implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 支付类型 */
	private String pay_type;
	/** 返回具体支付内容，JSONObject */
	private JSONObject pay_content;
	/** 屏蔽关键字，多个关键字用英文逗号,分隔 */
	private List<ResSDKFilter> filterrsp;
	/** 延时 */
	private String delayed;
	/** 服务器订单号 */
	private String order_id;
	/** 请求次数 */
	private String num;
	
	
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	public JSONObject getPay_content() {
		return pay_content;
	}
	public void setPay_content(JSONObject pay_content) {
		this.pay_content = pay_content;
	}
	public String getDelayed() {
		return delayed;
	}
	public void setDelayed(String delayed) {
		this.delayed = delayed;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public List<ResSDKFilter> getFilterrsp() {
		return filterrsp;
	}
	public void setFilterrsp(List<ResSDKFilter> filterrsp) {
		this.filterrsp = filterrsp;
	}
	
}
