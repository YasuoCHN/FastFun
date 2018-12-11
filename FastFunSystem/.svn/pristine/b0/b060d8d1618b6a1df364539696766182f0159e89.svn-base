package com.klw.fastfun.pay.data;

import org.apache.log4j.Logger;

import com.juice.orange.game.log.LoggerFactory;
import com.klw.fastfun.pay.data.ao.AlarmAO;
import com.klw.fastfun.pay.data.ao.CPInfoAO;
import com.klw.fastfun.pay.data.ao.CodeHelperAO;
import com.klw.fastfun.pay.data.ao.CommonAO;
import com.klw.fastfun.pay.data.ao.MMPayInfoAO;
import com.klw.fastfun.pay.data.ao.OrderReqAO;

public class Application {

	private static Logger logger = LoggerFactory.getLogger(Application.class);

	private static CodeHelperAO codeHelperAO;
	private static CommonAO commonAO;
	private static CPInfoAO cpInfoAO;
	private static OrderReqAO orderReqAO;
	private static MMPayInfoAO mmPayInfoAO;
	private static AlarmAO alarmAO;

	public void init() {
		logger.info("初始化AO...");
		codeHelperAO = new CodeHelperAO();
		commonAO = new CommonAO();
		cpInfoAO = new CPInfoAO();
		orderReqAO = new OrderReqAO();
		mmPayInfoAO = new MMPayInfoAO();
		alarmAO = new AlarmAO();
	}

	public CodeHelperAO codeHelperAO() {
		return codeHelperAO;
	}

	public CommonAO commonAO() {
		return commonAO;
	}

	public CPInfoAO cpInfoAO() {
		return cpInfoAO;
	}

	public OrderReqAO orderReqAO() {
		return orderReqAO;
	}

	public MMPayInfoAO mmPayInfoAO() {
		return mmPayInfoAO;
	}
	
	public AlarmAO alarmAO() {
		return alarmAO;
	}
}
