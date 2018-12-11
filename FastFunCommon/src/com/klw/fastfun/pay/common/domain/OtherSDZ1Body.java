package com.klw.fastfun.pay.common.domain;

public class OtherSDZ1Body {
	
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	/** 订单总金额 */
	private String orderAmount;
	/** 订单备注 */
	private String remark;
	/** 授权码 */
	private String authCode;
	/** 卡号 */
	private String cardNo;
	/** 商品信息 */
	private OtherSDZ1Products productInfos ;
	/** 支付类型 */
	private String payType;
	/** 公众号 */
	private String appId;
	/** 个人用户在接入方微信公众号下的唯一id */
	private String openId;
	/** 异步通知URL */
	private String backNoticeUrl;
	/** 用户端真实ip */
	private String spbillCreateIp;
	/** 场景信息 */
	private String wechatSceneInfo;
	/** 微信商户号 */
	private String wechatMercID;
	
	public String getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public OtherSDZ1Products getProductInfos() {
		return productInfos;
	}
	public void setProductInfos(OtherSDZ1Products productInfos) {
		this.productInfos = productInfos;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getBackNoticeUrl() {
		return backNoticeUrl;
	}
	public void setBackNoticeUrl(String backNoticeUrl) {
		this.backNoticeUrl = backNoticeUrl;
	}
	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}
	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}
	public String getWechatSceneInfo() {
		return wechatSceneInfo;
	}
	public void setWechatSceneInfo(String wechatSceneInfo) {
		this.wechatSceneInfo = wechatSceneInfo;
	}
	public String getWechatMercID() {
		return wechatMercID;
	}
	public void setWechatMercID(String wechatMercID) {
		this.wechatMercID = wechatMercID;
	}

}
