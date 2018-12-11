/**
 * Juice
 * com.juice.orange.game.bootstrap
 * ServerBootstrap.java
 */
package com.klw.fastfun.pay.view.app;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import bsh.Interpreter;

import com.juice.orange.game.bootstrap.BootstrapProperties;
import com.juice.orange.game.bootstrap.CachedProperties;
import com.juice.orange.game.bootstrap.DataBaseProperties;
import com.juice.orange.game.cached.MemcachedResource;
import com.juice.orange.game.cached.RedisResource;
import com.juice.orange.game.container.Container;
import com.juice.orange.game.log.LoggerFactory;
import com.juice.orange.game.rmi.JuiceRemoteManager;
import com.juice.orange.game.rmi.RemoteConfig;
import com.juice.orange.game.server.IJuiceServer;
import com.juice.orange.game.server.JuiceServers;

/**
 * @author shaojieque
 * 
 *         2013-4-18
 */
public class ServerBootstrap {
	public static Logger logger = LoggerFactory
			.getLogger(ServerBootstrap.class);
	/** 获取根目录 */
	public static String ROOT_DIR = System.getProperty("user.dir");

	/**
	 * 主函数
	 */
	public static void main(String[] args) throws Exception {
		// 读取服务器配置
		Interpreter interpreter = new Interpreter();
		interpreter.source(ROOT_DIR + File.separator + "script/juice.bsh");
		Object _database = interpreter.get("database");
		if (_database != null && _database instanceof DataBaseProperties) {
			// DataBaseProperties database = (DataBaseProperties) _database;
			// setupDataBase(database);
		}
		//
		Object _bootstrap = interpreter.get("bootstrap");
		if (_bootstrap != null && _bootstrap instanceof BootstrapProperties) {
			BootstrapProperties bootstrap = (BootstrapProperties) _bootstrap;
			setupBootstrap(bootstrap);
		}
		//
		Object _remotes = interpreter.get("remotes");
		if (_remotes != null) {
			RemoteConfig[] remotes = (RemoteConfig[]) _remotes;
			setupRemoteServer(remotes);
		}
		/*Object _cached = interpreter.get("cached");
		if (_cached!=null && _cached instanceof CachedProperties) {
			CachedProperties cached =  (CachedProperties) _cached;
			MemcachedResource.buildMemcached(cached.getMemcachedUrl());
		}*/
		Object _cacheds = interpreter.get("cacheds");
		if (_cacheds != null) {
			CachedProperties[] cacheds = (CachedProperties[]) _cacheds;
			List<CachedProperties> cps = new ArrayList<CachedProperties>();
			for (CachedProperties cp : cacheds) {
				if (cp.getCacheName().equals(CachedProperties.CACHE_MEMCACHE)) {
					// memchache缓存
					StringBuilder memcachedURL = new StringBuilder();
					memcachedURL.append(cp.getIp()).append(":").append(cp.getPort());
					MemcachedResource.buildMemcached(memcachedURL.toString());
				} else if (cp.getCacheName().equals(CachedProperties.CACHE_REDIS)) {
					// redis缓存
					cps.add(cp);
				}
			}
			
			if (!cps.isEmpty()) RedisResource.getInstance().buildRedis(cps);
		}
		
		Application app = new Application();
		app.init();
		findAppPID();
	}

	// 初始化服务器启动配置
	private static void setupBootstrap(BootstrapProperties bp) throws Exception {
		logger.info("Configuration Server bootstrap params.......");
		logger.info("Server protocol:\t" + bp.getProtocol());
		logger.info("Server port:\t" + bp.getPort());
		IJuiceServer server = JuiceServers.createWebServer(bp.getPort());
		server.setTransport(bp.getProtocol());
		server.start().get();
	}

	// 初始化配置远程服务器配置
	private static void setupRemoteServer(RemoteConfig[] configs)
			throws Exception {
		logger.info("Configuration Remote Server.......");
		for (RemoteConfig config : configs) {
			logger.info("Add Remote Server - Name:" + config.getName()
					+ ";Address:" + config.getAddress() + ";Port:"
					+ config.getPort());
			Container.addRemoteConfig(config);
		}
		JuiceRemoteManager.setupRemoteServer();
	}

	private static void findAppPID() throws Exception {
		String processName = java.lang.management.ManagementFactory
				.getRuntimeMXBean().getName();
		String pid = processName.split("@")[0];
		logger.info("Application PID:\t" + pid);
		FileWriter writer = new FileWriter(ROOT_DIR + File.separator
				+ "server.pid");
		writer.write(pid);
		writer.flush();
		writer.close();
	}
}
