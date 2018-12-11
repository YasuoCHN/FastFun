/**
 */
package com.klw.fastfun.pay.view.app;

import com.juice.orange.game.container.Container;
import com.juice.orange.game.handler.HttpRequest;
import com.klw.fastfun.pay.common.action.CommonAction;

/**
 * @author shaojieque 2015-1-14
 */
public class ActionAware extends Application {
	public static final String REMOTE_PREFIX_SYSTEM = "FastFunSystem";

	protected static CommonAction commonAction = (CommonAction) Container
			.createRemoteService(CommonAction.class, REMOTE_PREFIX_SYSTEM);
	
	
	
	protected String getRealIP(HttpRequest request) {
		String ip = request.header("X-Real-IP");
		if (ip == null || "unknown".equalsIgnoreCase(ip)) {
			ip = request.header("X-Forwarded-For");
			//
			if (ip != null && !"unknown".equalsIgnoreCase(ip)) {
				int index = ip.indexOf(',');
				if (index != -1) {
					ip = ip.substring(0, index);
				}
			}
		}
		//
		if (ip == null) {
			ip = request.remoteAddress().toString();
		}

		int port = ip.indexOf(":");
		if (port > 1) {
			ip = ip.substring(1, port);
		}
		return ip;
	}
}
