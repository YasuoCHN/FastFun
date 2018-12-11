package com.klw.fastfun.pay.common.domain;

import java.io.Serializable;
import java.util.List;

public class PayInitResJson implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 短信发送方式端口号及内容 */
	private List<PayConJson> init_sms;
	/** 力聚初始化信息 */
	private PayInitJson liju;
	public List<PayConJson> getInit_sms() {
		return init_sms;
	}
	public void setInit_sms(List<PayConJson> init_sms) {
		this.init_sms = init_sms;
	}
	public PayInitJson getLiju() {
		return liju;
	}
	public void setLiju(PayInitJson liju) {
		this.liju = liju;
	}

}
