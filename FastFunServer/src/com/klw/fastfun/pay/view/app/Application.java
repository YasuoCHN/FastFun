package com.klw.fastfun.pay.view.app;

import org.apache.log4j.Logger;

import com.juice.orange.game.container.Container;
import com.juice.orange.game.log.LoggerFactory;
import com.klw.fastfun.pay.view.app.service.CodeHelperServer;
import com.klw.fastfun.pay.view.app.service.FeeBack;
import com.klw.fastfun.pay.view.app.service.MoniHelperServer;
import com.klw.fastfun.pay.view.app.service.TestBack;

/**
 * @author shaojieque 2013-5-3
 */
public class Application {
	private static Logger logger = LoggerFactory.getLogger(Application.class);

	//
	public void init() {
		initServer();
		//
	}

	public void initServer() {
		logger.info("Init servers......");
		// 用户服务
		Container.registerServer("code", new CodeHelperServer());
		Container.registerServer("fee", new FeeBack());
		Container.registerServer("common", new TestBack());
		Container.registerServer("monit", new MoniHelperServer());
	}
	
}
