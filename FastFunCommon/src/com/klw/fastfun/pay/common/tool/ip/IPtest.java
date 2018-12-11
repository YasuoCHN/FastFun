package com.klw.fastfun.pay.common.tool.ip;

import java.io.File;

public class IPtest {
	public static final String data_path = System.getProperty("user.dir") + File.separator + "data";
	
	public static void main(String[] args) {
		IPSeeker ip = new IPSeeker("QQWry.dat", "data");
		System.out.println(ip.getIPLocation("119.188.101.84").getCountry());
		System.out.println(ip.getIPLocation("119.188.101.84").getArea());
	}
	
	//
	private static IPSeeker seeker;
	private static IPtest test;
	private IPtest(){}
	
	public static IPtest getInstance(){
		if (test==null) {
			test = new IPtest();
			test.init();
		}
		return test;
	}
	
	public void init() {
		seeker = new IPSeeker("QQWry.dat", "data");
	}
	
	public String queryProvince(String ip) {
		String country = seeker.getIPLocation(ip).getCountry();
		if (country==null) return "all";
		return country.substring(0, 2);
	}
	
	public String queryCountry(String ip) {
		return seeker.getIPLocation(ip).getCountry();
	}
	
	public String queryArea(String ip) {
		return seeker.getIPLocation(ip).getArea();
	}
}