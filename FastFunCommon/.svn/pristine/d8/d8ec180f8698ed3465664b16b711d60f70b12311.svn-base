package com.klw.fastfun.pay.common.domain;

import java.io.Serializable;
import java.util.List;

public class PayResultJson implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 结果代码，0为成功 */
	private String code;
	/** 结果代码中文描述 */
	private String code_explain;
	/** 短代内容JSONArray，可以包含多个不同支付 */
	private List<PayContentJson> content;
	/** 订单号 */
	private String order_id;
	/** 0，不弹窗；1，弹窗  */
	private int dialog;
	
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public int getDialog() {
		return dialog;
	}
	public void setDialog(int dialog) {
		this.dialog = dialog;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCode_explain() {
		return code_explain;
	}
	public void setCode_explain(String code_explain) {
		this.code_explain = code_explain;
	}
	public List<PayContentJson> getContent() {
		return content;
	}
	public void setContent(List<PayContentJson> content) {
		this.content = content;
	}
	

}
