package com.klw.fastfun.pay.common.domain;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

public class PayInitJson implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 吴旭破解初始化 */
	private JSONObject mm2002;
	/** 自己破解初始化 */
	private JSONObject mm2003;
	
	public JSONObject getMm2002() {
		return mm2002;
	}
	public void setMm2002(JSONObject mm2002) {
		this.mm2002 = mm2002;
	}
	public JSONObject getMm2003() {
		return mm2003;
	}
	public void setMm2003(JSONObject mm2003) {
		this.mm2003 = mm2003;
	}


}
