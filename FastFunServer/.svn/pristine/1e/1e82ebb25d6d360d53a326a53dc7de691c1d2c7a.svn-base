package com.klw.fastfun.pay.view.app.other.tools;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.juice.orange.game.handler.HttpRequest;
import com.juice.orange.game.handler.HttpResponse;
import com.klw.fastfun.pay.view.app.other.util.SHA256Util;

public class GatewayRequest { 
	
	/** 商户密钥 */
	private String key;
	
	/** 应答的参数 */
	private SortedMap<String,String> parameters; 
	
	/** 调试信息 */
	private String debugMsg;
	
	private HttpRequest request;
	
	private HttpResponse response;
	
	private String urlEncoding;
	
	/** 网关url地址 */
	private String gatewayUrl;
	
	public static int BULID_SIGN = 0;
	public static int VERIFY_SIGN = 1;
	
	public GatewayRequest(){};
	
	/**
	 * 构造函数
	 * @param request
	 * @param response
	 */
	public GatewayRequest(HttpRequest request,
			HttpResponse response, int type)  {
		this.request = request; 
		this.response = response;
		this.key = "";
		this.parameters = new TreeMap<String,String>();
		this.debugMsg = "";
		this.urlEncoding = "";
		
		this.gatewayUrl = "http://www.xxxx.com/entry.do";

		/*if(type==VERIFY_SIGN){
			Map<String,String[]> m = this.request.getParameterMap();
			Iterator<String> it = m.keySet().iterator();
			while (it.hasNext()) {
				String k = (String) it.next();
				String v = ((String[]) m.get(k))[0];			
				this.setParameter(k, v);
			}
		}*/
	}
	
	/**
	 * 构造函数
	 * @param request
	 * @param response
	 */
	public GatewayRequest(HttpRequest request,
			HttpResponse response)  {
		this.request = request; 
		this.response = response;
		this.key = "";
		this.parameters = new TreeMap<String,String>();
		this.debugMsg = "";
		this.urlEncoding = "";
	}
	
	/**
	*获取密钥
	*/
	public String getKey() {
		return key;
	}

	/**
	*设置密钥
	*/
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * 获取参数值
	 * @param parameter 参数名称
	 * @return String 
	 */
	public String getParameter(String parameter) {
		String s = (String)this.parameters.get(parameter); 
		return (null == s) ? "" : s;
	}
	
	/**
	 * 设置参数值
	 * @param parameter 参数名称
	 * @param parameterValue 参数值
	 */
	public void setParameter(String parameter, String parameterValue) {
		String v = "";
		if(null != parameterValue) {
			v = parameterValue.trim();
		}
		this.parameters.put(parameter, v);
	}
	
	/**
	 * 返回所有的参数
	 * @return SortedMap
	 */
	@SuppressWarnings("rawtypes")
	public SortedMap getAllParameters() {
		return this.parameters;
	}
	
	/**
	 * 使用SHA256算法验证签名。规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 * @return boolean
	 */
	@SuppressWarnings("rawtypes")
	public boolean verifySign() {
		StringBuffer sb = new StringBuffer();
		Set es = this.parameters.entrySet();
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			String v = (String)entry.getValue();
			if(!"sign".equals(k) && null != v && !"".equals(v)) {
				sb.append(k + "=" + v + "&");
			}
		}
		
		sb.append("key=" + this.getKey());
		
		//算出摘要
		//String enc = ToolUtil.getCharacterEncoding(this.request, this.response);
		String enc = "UTF-8";
		String sign = SHA256Util.SHA256Encode(sb.toString(), enc).toLowerCase();
		String merchantSign = this.getParameter("sign").toLowerCase();
		
		//debug信息
		this.setDebugMsg("请求参数:"+sb.toString()+",服务端签名:"+sign+",请求端签名:"+merchantSign+",enc="+enc);
		
		return merchantSign.equals(sign);
	}
	
	public static void main(String args []){
		
		String sb = "base64_memo=wvPAy7Xn19PJzM7x09DP3rmry762qbWlveHVyw==&base64_user_info=YmFua19hY2NfdHlwZT0wMCZ1c2VyX2lkPTgxMSZ1c2VyX25hbWU9x/G93MHBJnVzZXJfY2VydF90eXBlPTAwJnVzZXJfY2VydF9ubz00NDA2ODIxOTkzMDUwODYwMzc=&currency_type=RMB&freight=0&freight_curr=RMB&goods_amount=0.1&goods_amount_curr=RMB&notify_url=http://www.kk178.com/respond.php?code=epaylinks&out_trade_no=2015042446682_9581&partner=EC150420YL0001&return_url=http://www.kk178.com/respond.php?code=epaylinks&sign_type=SHA256&tax_amount=0.02&tax_amount_curr=RMB&total_fee=0.10&key=695b3069e537d1722dcf33f26478d0a3";
		String sign = SHA256Util.SHA256Encode(sb.toString(), "gbk").toLowerCase();
		System.out.println("sign:"+sign);
	}
	
	/**
	 * 返回处理结果给支付网关服务器。
	 * @param msg: Success or fail。
	 * @throws IOException 
	 */
	/*public void responseToGateway(String msg) throws IOException {
		String strHtml = msg;
		PrintWriter out = this.getHttpResponse().getWriter();
		out.println(strHtml);
		out.flush();
		out.close();

	}*/
	
	/**
	 * 获取uri编码
	 * @return String
	 */
	public String getUrlEncoding() {
		return urlEncoding;
	}

	/**
	 * 设置url编码
	 * @param uriEncoding
	 * @throws UnsupportedEncodingException
	 */
	/*public void setUrlEncoding(String uriEncoding)
			throws UnsupportedEncodingException {
		if (!"".equals(uriEncoding.trim())) {
			this.urlEncoding = uriEncoding;

			// 编码转换
			String enc = ToolUtil.getCharacterEncoding(request, response);
			Iterator it = this.parameters.keySet().iterator();
			while (it.hasNext()) {
				String k = (String) it.next();
				String v = this.getParameter(k);
				v = new String(v.getBytes(uriEncoding.trim()), enc);
				this.setParameter(k, v);
			}
		}
	}*/

	/**
	*获取调试信息
	*/
	public String getDebugMsg() {
		return debugMsg;
	}
	
	/**
	*设置调试信息
	*/
	protected void setDebugMsg(String debugInfo) {
		this.debugMsg = debugInfo;
	}
	
	protected HttpRequest getHttpRequest() {
		return this.request;
	}
	
	protected HttpResponse getHttpResponse() {
		return this.response;
	}
	
	/**
	 * 使用SHA256算法生成签名结果,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 */
	@SuppressWarnings("rawtypes")
	public void buildRequestSign() {
		StringBuffer sb = new StringBuffer();
		Set es = this.parameters.entrySet();
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			String v = (String)entry.getValue();
			if(null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + this.getKey());
		
		//String enc = ToolUtil.getCharacterEncoding(this.request, this.response);
		String enc = "UTF-8";
		String sign = SHA256Util.SHA256Encode(sb.toString(), enc).toLowerCase();
		
		this.setParameter("sign", sign);
		
		//调试信息
		this.setDebugMsg("签名参数:"+sb.toString() + ",sign:" + sign +" ;enc="+enc);
	}

	public String getGatewayUrl() {
		return gatewayUrl;
	}

	public void setGatewayUrl(String gatewayUrl) {
		this.gatewayUrl = gatewayUrl;
	}
	
	/**
	 * 获取请求的URL地址，此地址包含参数和签名串
	 * @return String
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("rawtypes")
	public String getRequestURL() throws UnsupportedEncodingException {
		
		this.buildRequestSign();
		
		StringBuffer sb = new StringBuffer();
		String enc = "UTF-8";
		Set es = this.parameters.entrySet();
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			String v = (String)entry.getValue();
			sb.append(k + "=" + URLEncoder.encode(v, enc) + "&");
		}
		
		//去掉最后一个&
		String reqPars = sb.substring(0, sb.lastIndexOf("&"));
		
		if(this.getGatewayUrl().indexOf("?")!=-1){
			int index = this.getGatewayUrl().indexOf("?");
			//客户参数，已经在setGatewayUrl()是包含进去，所以返回的时候，要去掉?号后的客户参数
			String noParamUrl = this.getGatewayUrl().substring(0,index);
			return noParamUrl + "?" + reqPars;
		}else{
			return this.getGatewayUrl() + "?" + reqPars;
		}
		
		
	}
	
	
	
}
