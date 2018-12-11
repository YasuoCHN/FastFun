/**
 * FastFunServer
 */
package com.klw.fastfun.pay.view.app.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jnewsdk.util.SignUtil;
import com.juice.orange.game.log.LoggerFactory;
import com.klw.fastfun.pay.common.domain.OrderReqInfo;
import com.klw.fastfun.pay.common.domain.PayConJson;
import com.klw.fastfun.pay.common.domain.PayContentJson;
import com.klw.fastfun.pay.common.domain.RTPayResJson;
import com.klw.fastfun.pay.common.domain.XSPayCodeInfo;
import com.klw.fastfun.pay.common.tool.AESOperator;
import com.klw.fastfun.pay.common.tool.Base64;
import com.klw.fastfun.pay.common.tool.CommonTool;
import com.klw.fastfun.pay.common.tool.ConstantDefine;
import com.klw.fastfun.pay.common.tool.DOMUtils;
import com.klw.fastfun.pay.common.tool.DateTool;
import com.klw.fastfun.pay.common.tool.DesUtil;
import com.klw.fastfun.pay.common.tool.HttpTool;
import com.klw.fastfun.pay.common.tool.HttpsTool;
import com.klw.fastfun.pay.common.tool.MD5Tool;
import com.klw.fastfun.pay.common.tool.SHAUtil;
import com.klw.fastfun.pay.common.tool.StringUtils;
import com.klw.fastfun.pay.common.tool.mm.XMLUtils;
import com.klw.fastfun.pay.common.tool.xdzf.MD5;
import com.klw.fastfun.pay.view.app.other.ght.model.pkg8583;
import com.klw.fastfun.pay.view.app.other.ght.util.Util;
import com.klw.fastfun.pay.view.app.other.mrutil.RSA;
import com.klw.fastfun.pay.view.app.other.mrutil.SignUtils;
import com.klw.fastfun.pay.view.app.other.qmdyuril.PaySignUtil;
import com.klw.fastfun.pay.view.app.other.tools.Base64PngToUrl;
import com.klw.fastfun.pay.view.app.other.util.HMacMD5;
import com.klw.fastfun.pay.view.app.other.util.RSASignature;
import com.klw.fastfun.pay.view.app.other.util.ToolUtil;
import com.klw.fastfun.pay.view.app.other.wg.PrePay;
import com.klw.fastfun.pay.view.app.wxpay.utils.CollectionUtil;
import com.klw.fastfun.pay.view.app.wxpay.utils.MD5Utils;
import com.klw.fastfun.pay.view.app.wxpay.utils.PayUtil;
import com.klw.fastfun.pay.view.app.wxpay.utils.XmlUtil;

/**
 * @author klwplayer.com
 *
 *         2015年3月31日
 */
public class CodeReqHelper {
	private static Logger logger = LoggerFactory.getLogger(CodeReqHelper.class);
	public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static Map<String, String> provinceMap = new HashMap<String, String>();
	private static Map<String, String> provinceRTMap = new HashMap<String, String>();
	private static Map<String, String> provinceYZWHMap = new HashMap<String, String>();
	private static Map<String, String> provincezyWHMap = new HashMap<String, String>();
	private static Map<String, String> provincezJDMap = new HashMap<String, String>();
	static {
		provinceMap.put("上海", "1");
		provinceMap.put("云南", "2");
		provinceMap.put("内蒙", "3");
		provinceMap.put("北京", "4");
		provinceMap.put("吉林", "5");
		provinceMap.put("四川", "6");
		provinceMap.put("天津", "7");
		provinceMap.put("宁夏", "8");
		provinceMap.put("安徽", "9");
		provinceMap.put("山东", "10");
		provinceMap.put("山西", "11");
		provinceMap.put("广东", "12");
		provinceMap.put("广西", "13");
		provinceMap.put("新疆", "14");
		provinceMap.put("江苏", "15");
		provinceMap.put("江西", "16");
		provinceMap.put("河北", "17");
		provinceMap.put("河南", "18");
		provinceMap.put("浙江", "19");
		provinceMap.put("海南", "20");
		provinceMap.put("湖北", "21");
		provinceMap.put("湖南", "22");
		provinceMap.put("甘肃", "23");
		provinceMap.put("福建", "24");
		provinceMap.put("西藏", "25");
		provinceMap.put("贵州", "26");
		provinceMap.put("辽宁", "27");
		provinceMap.put("重庆", "28");
		provinceMap.put("陕西", "29");
		provinceMap.put("青海", "30");
		provinceMap.put("黑龙", "31");

		provinceRTMap.put("安徽", "ah");
		provinceRTMap.put("北京", "bj");
		provinceRTMap.put("福建", "fj");
		provinceRTMap.put("甘肃", "gs");
		provinceRTMap.put("广东", "gd");
		provinceRTMap.put("广西", "gx");
		provinceRTMap.put("贵州", "gz");
		provinceRTMap.put("海南", "hain");
		provinceRTMap.put("河北", "heb");
		provinceRTMap.put("河南", "hen");
		provinceRTMap.put("黑龙江", "hlj");
		provinceRTMap.put("黑龙", "hlj");
		provinceRTMap.put("湖北", "hub");
		provinceRTMap.put("湖南", "hn");
		provinceRTMap.put("吉林", "jl");
		provinceRTMap.put("江苏", "js");
		provinceRTMap.put("江西", "jx");
		provinceRTMap.put("辽宁", "ln");
		provinceRTMap.put("内蒙古", "nmg");
		provinceRTMap.put("内蒙", "nmg");
		provinceRTMap.put("宁夏", "nx");
		provinceRTMap.put("青海", "qh");
		provinceRTMap.put("山东", "sd");
		provinceRTMap.put("山西", "sx");
		provinceRTMap.put("陕西", "shxi");
		provinceRTMap.put("上海", "sh");
		provinceRTMap.put("四川", "sc");
		provinceRTMap.put("天津", "tj");
		provinceRTMap.put("西藏", "xz");
		provinceRTMap.put("新疆", "xj");
		provinceRTMap.put("云南", "yn");
		provinceRTMap.put("浙江", "zj");
		provinceRTMap.put("重庆", "cq");
		provinceRTMap.put("其他", "oth");

		provinceYZWHMap.put("北京", "100");
		provinceYZWHMap.put("天津", "220");
		provinceYZWHMap.put("河北", "311");
		provinceYZWHMap.put("山西", "351");
		provinceYZWHMap.put("内蒙古", "471");
		provinceYZWHMap.put("内蒙", "471");
		provinceYZWHMap.put("辽宁", "240");
		provinceYZWHMap.put("吉林", "431");
		provinceYZWHMap.put("黑龙江", "451");
		provinceYZWHMap.put("黑龙", "451");
		provinceYZWHMap.put("上海", "210");
		provinceYZWHMap.put("江苏", "250");
		provinceYZWHMap.put("安徽", "551");
		provinceYZWHMap.put("福建", "591");
		provinceYZWHMap.put("江西", "791");
		provinceYZWHMap.put("山东", "531");
		provinceYZWHMap.put("河南", "371");
		provinceYZWHMap.put("湖北", "270");
		provinceYZWHMap.put("湖南", "731");
		provinceYZWHMap.put("广东", "200");
		provinceYZWHMap.put("广西", "771");
		provinceYZWHMap.put("海南", "898");
		provinceYZWHMap.put("重庆", "230");
		provinceYZWHMap.put("浙江", "571");
		provinceYZWHMap.put("四川", "280");
		provinceYZWHMap.put("贵州", "851");
		provinceYZWHMap.put("云南", "871");
		provinceYZWHMap.put("西藏", "891");
		provinceYZWHMap.put("陕西", "290");
		provinceYZWHMap.put("甘肃", "931");
		provinceYZWHMap.put("青海", "971");
		provinceYZWHMap.put("宁夏", "951");
		provinceYZWHMap.put("新疆", "991");

		provincezyWHMap.put("北京", "1");
		provincezyWHMap.put("天津", "2");
		provincezyWHMap.put("河北", "3");
		provincezyWHMap.put("山西", "4");
		provincezyWHMap.put("内蒙古", "5");
		provincezyWHMap.put("内蒙", "5");
		provincezyWHMap.put("辽宁", "6");
		provincezyWHMap.put("吉林", "7");
		provincezyWHMap.put("黑龙江", "8");
		provincezyWHMap.put("黑龙", "8");
		provincezyWHMap.put("上海", "9");
		provincezyWHMap.put("江苏", "10");
		provincezyWHMap.put("安徽", "11");
		provincezyWHMap.put("福建", "12");
		provincezyWHMap.put("江西", "13");
		provincezyWHMap.put("山东", "14");
		provincezyWHMap.put("河南", "15");
		provincezyWHMap.put("湖北", "16");
		provincezyWHMap.put("湖南", "17");
		provincezyWHMap.put("广东", "18");
		provincezyWHMap.put("广西", "19");
		provincezyWHMap.put("海南", "20");
		provincezyWHMap.put("重庆", "21");
		provincezyWHMap.put("浙江", "22");
		provincezyWHMap.put("四川", "23");
		provincezyWHMap.put("贵州", "24");
		provincezyWHMap.put("云南", "25");
		provincezyWHMap.put("西藏", "26");
		provincezyWHMap.put("陕西", "27");
		provincezyWHMap.put("甘肃", "28");
		provincezyWHMap.put("青海", "29");
		provincezyWHMap.put("宁夏", "30");
		provincezyWHMap.put("新疆", "31");

		provincezJDMap.put("湖北", "hb");
		provincezJDMap.put("江苏", "js");
		provincezJDMap.put("广东", "gd");
		provincezJDMap.put("湖南", "hn");
		provincezJDMap.put("浙江", "zj");
		provincezJDMap.put("山东", "sd");
		provincezJDMap.put("河南", "hn1");

		provincezJDMap.put("安徽", "ah");
		provincezJDMap.put("北京", "bj");
		provincezJDMap.put("福建", "fj");
		provincezJDMap.put("甘肃", "gs");
		provincezJDMap.put("广西", "gx");
		provincezJDMap.put("贵州", "gz");
		provincezJDMap.put("海南", "hain");
		provincezJDMap.put("河北", "heb");
		provincezJDMap.put("黑龙江", "hlj");
		provincezJDMap.put("黑龙", "hlj");
		provincezJDMap.put("吉林", "jl");
		provincezJDMap.put("江西", "jx");
		provincezJDMap.put("辽宁", "ln");
		provincezJDMap.put("内蒙古", "nmg");
		provincezJDMap.put("内蒙", "nmg");
		provincezJDMap.put("宁夏", "nx");
		provincezJDMap.put("青海", "qh");
		provincezJDMap.put("山西", "sx");
		provincezJDMap.put("陕西", "shxi");
		provincezJDMap.put("上海", "sh");
		provincezJDMap.put("四川", "sc");
		provincezJDMap.put("天津", "tj");
		provincezJDMap.put("西藏", "xz");
		provincezJDMap.put("新疆", "xj");
		provincezJDMap.put("云南", "yn");
		provincezJDMap.put("重庆", "cq");
		provincezJDMap.put("其他", "oth");
	}

	public static PayContentJson getklwSMSCode(OrderReqInfo info) {
		PayContentJson payContent = new PayContentJson();

		String spId = info.getSpId();
		String conJson = getSMSCode(info);

		if ("mm06".equals(spId)) {
			payContent.setPay_type(ConstantDefine.RES_CON_PAY_TYPE_2002);
			payContent.setPay_content(JSONArray.parseArray(conJson, JSONObject.class).get(0));
		} else if ("yc01".equals(spId) || "yc09".equals(spId) || "yc10".equals(spId) || "yc11".equals(spId)) {
			payContent.setPay_type(ConstantDefine.RES_CON_PAY_TYPE_2003);
			payContent.setPay_content(JSONObject.parseObject(conJson));
		}

		return payContent;
	}

//	public static String getSMSCode(String spURL, int urlNO, String ffId, String fee, String imsi, String imei,
//			String iccid, String pmodel, String osversion, String nwtype, String province, String ip,
//			String mobile) {
	public static String getSMSCode(OrderReqInfo info) {
		switch (info.getUrlNo()) {
		case ConstantDefine.URL_NO_999_JSON:
			return getOnlineSMS999(info);
		case ConstantDefine.URL_NO_KLW:
			return getOnlineSMS1(info);
		case ConstantDefine.URL_NO_WX:
			return getOnlineSMS2(info.getUrl(), info.getFfId(), info.getFee() + "", info.getImsi(), info.getImei(),
					info.getIccid(), info.getPmodel(), info.getOsversion(), info.getNwtype(), info.getProvince(),
					info.getMobile());
		case ConstantDefine.URL_NO_DMHM:
			return getOnlineSMS3(info.getUrl(), info.getFfId(), info.getFee() + "", info.getImsi(), info.getImei(),
					info.getIccid(), info.getPmodel(), info.getOsversion(), info.getNwtype(), info.getProvince(),
					info.getIp(), info.getMobile());
		case ConstantDefine.URL_NO_SWMTDL:
			return getOnlineSMS4(info.getUrl(), info.getFfId(), info.getFee() + "", info.getImsi(), info.getImei(),
					info.getIccid(), info.getPmodel(), info.getOsversion(), info.getNwtype(), info.getProvince(),
					info.getIp(), info.getMobile());
		case ConstantDefine.URL_NO_LDDX:
			return getOnlineSMS5(info.getUrl(), info.getFfId(), info.getFee() + "", info.getImsi(), info.getImei(),
					info.getIccid(), info.getPmodel(), info.getOsversion(), info.getNwtype(), info.getProvince(),
					info.getIp(), info.getMobile());
		case ConstantDefine.URL_NO_QXHD:
			return getOnlineSMS6(info.getUrl(), info.getFfId(), info.getFee() + "", info.getImsi(), info.getImei(),
					info.getIccid(), info.getPmodel(), info.getOsversion(), info.getNwtype(), info.getProvince(),
					info.getIp(), info.getMobile());
		case ConstantDefine.URL_NO_YJLT:
			return getOnlineSMS7(info.getUrl(), info.getFfId(), info.getFee() + "", info.getImsi(), info.getImei(),
					info.getIccid(), info.getPmodel(), info.getOsversion(), info.getNwtype(), info.getProvince(),
					info.getIp(), info.getMobile());
		case ConstantDefine.URL_NO_YCDX_JSON:
			return getOnlineSMS8(info);
		case ConstantDefine.URL_NO_EC02_JSON:
			return getOnlineSMS9(info);
		case ConstantDefine.URL_NO_EC01_JSON:
			return getOnlineSMS10(info);
		case ConstantDefine.URL_NO_SD01_JSON:
			return getOnlineSMS11(info);
		case ConstantDefine.URL_NO_DX02_JSON:
			return getOnlineSMS12(info);
		case ConstantDefine.URL_NO_LT02_JSON:
			return getOnlineSMS13(info);
		case ConstantDefine.URL_NO_QX01_JSON:
			return getOnlineSMS14(info);
		case ConstantDefine.URL_NO_QX02_JSON:
			return getOnlineSMS15(info);
		case ConstantDefine.URL_NO_QX03_JSON:
			return getOnlineSMS16(info);
		case ConstantDefine.URL_NO_YC09_JSON:
			return getOnlineSMS17(info);
		case ConstantDefine.URL_NO_YC10_JSON:
			return getOnlineSMS18(info);
		case ConstantDefine.URL_NO_QX04_JSON:
			return getOnlineSMS19(info);
		case ConstantDefine.URL_NO_EC03_JSON:
			return getOnlineSMS20(info);
		case ConstantDefine.URL_NO_SD02_JSON:
			return getOnlineSMS21(info);
		case ConstantDefine.URL_NO_QX05_JSON:
			return getOnlineSMS22(info);
		/*
		 * case ConstantDefine.URL_NO_QX06_JSON: return getOnlineSMS23(info);
		 */
		case ConstantDefine.URL_NO_QX07_JSON:
			return getOnlineSMS24(info);
		case ConstantDefine.URL_NO_QX08_JSON:
			return getOnlineSMS25(info);
		case ConstantDefine.URL_NO_EC05_JSON:
			return getOnlineSMS26(info);
		case ConstantDefine.URL_NO_DM02_JSON:
			return getOnlineSMS27(info);
		case ConstantDefine.URL_NO_QX09_JSON:
			return getOnlineSMS28(info);
		case ConstantDefine.URL_NO_EC06_JSON:
			return getOnlineSMS29(info);
		case ConstantDefine.URL_NO_EC07_JSON:
			return getOnlineSMS30(info);
		case ConstantDefine.URL_NO_YY02_JSON:
			return getOnlineSMS31(info);
		case ConstantDefine.URL_NO_QX10_JSON:
			return getOnlineSMS32(info);
		case ConstantDefine.URL_NO_QX11_JSON:
			return getOnlineSMS33(info);
		case ConstantDefine.URL_NO_YY03_JSON:
			return getOnlineSMS34(info);
		case ConstantDefine.URL_NO_DM04_JSON:
			return getOnlineSMS35(info);
		case ConstantDefine.URL_NO_YY04_JSON:
			return getOnlineSMS36(info);
		case ConstantDefine.URL_NO_YC13_JSON:
			return getOnlineSMS37(info);
		case ConstantDefine.URL_NO_YC14_JSON:
			return getOnlineSMS38(info);
		case ConstantDefine.URL_NO_YC15_JSON:
			return getOnlineSMS17(info);
		case ConstantDefine.URL_NO_YC16_JSON:
			return getOnlineSMS17(info);
		case ConstantDefine.URL_NO_SD05_JSON:
			return getOnlineSMS42(info);
		case ConstantDefine.URL_NO_DX03_JSON:
			return getOnlineSMS43(info);
		case ConstantDefine.URL_NO_DX04_JSON:
			return getOnlineSMS44(info);
		case ConstantDefine.URL_NO_SD06_JSON:
			return getOnlineSMS45(info);
		case ConstantDefine.URL_NO_YY05_JSON:
			return getOnlineSMS46(info);
		case ConstantDefine.URL_NO_DX08_JSON:
			return getOnlineSMS47(info);
		case ConstantDefine.URL_NO_SD07_JSON:
			return getOnlineSMS48(info);
		case ConstantDefine.URL_NO_SD08_JSON:
			return getOnlineSMS49(info);
		case ConstantDefine.URL_NO_EC10_JSON:
			return getOnlineSMS50(info);
		case ConstantDefine.URL_NO_EC11_JSON:
			return getOnlineSMS51(info);
		case ConstantDefine.URL_NO_SD09_JSON:
			return getOnlineSMS52(info);
		case ConstantDefine.URL_NO_SD15_JSON:
			return getOnlineSMS53(info);
		case ConstantDefine.URL_NO_LT04_JSON:
			return getOnlineSMS54(info);
		case ConstantDefine.URL_NO_SD17_JSON:
			return getOnlineSMS55(info);
		case ConstantDefine.URL_NO_DM05_JSON:
			return getOnlineSMS56(info);
		case ConstantDefine.URL_NO_SD18_JSON:
			return getOnlineSMS57(info);
		case ConstantDefine.URL_NO_SD19_JSON:
			return getOnlineSMS58(info);
		case ConstantDefine.URL_NO_SD20_JSON:
			return getOnlineSMS59(info);
		case ConstantDefine.URL_NO_SD21_JSON:
			return getOnlineSMS60(info);
		case ConstantDefine.URL_NO_SD23_JSON:
			return getOnlineSMS61(info);
		case ConstantDefine.URL_NO_SD24_JSON:
			return getOnlineSMS62(info);
		case ConstantDefine.URL_NO_SD25_JSON:
			return getOnlineSMS63(info);
		case ConstantDefine.URL_NO_YY06_JSON:
			return getOnlineSMS64(info);
		case ConstantDefine.URL_NO_YY07_JSON:
			return getOnlineSMS65(info);
		case ConstantDefine.URL_NO_DX09_JSON:
			return getOnlineSMS66(info);
		case ConstantDefine.URL_NO_DM06_JSON:
			return getOnlineSMS67(info);
		case ConstantDefine.URL_NO_DM07_JSON:
			return getOnlineSMS68(info);
		case ConstantDefine.URL_NO_YC19_JSON:
			return getOnlineSMS69(info);
		case ConstantDefine.URL_NO_YC20_JSON:
			return getOnlineSMS70(info);
		case ConstantDefine.URL_NO_YC21_JSON:
			return getOnlineSMS71(info);
		case ConstantDefine.URL_NO_EC13_JSON:
			return getOnlineSMS72(info);
		case ConstantDefine.URL_NO_SD26_JSON:
			return getOnlineSMS73(info);
		case ConstantDefine.URL_NO_DM08_JSON:
			return getOnlineSMS74(info);
		case ConstantDefine.URL_NO_SD27_JSON:
			return getOnlineSMS75(info);
		case ConstantDefine.URL_NO_DM09_JSON:
			return getOnlineSMS76(info);
		case ConstantDefine.URL_NO_DM10_JSON:
			return getOnlineSMS77(info);
		case ConstantDefine.URL_NO_LT05_JSON:
			return getOnlineSMS78(info);
		case ConstantDefine.URL_NO_SD28_JSON:
			return getOnlineSMS79(info);
		case ConstantDefine.URL_NO_YC23_JSON:
			return getOnlineSMS80(info);
		case ConstantDefine.URL_NO_YC24_JSON:
			return getOnlineSMS81(info);
		case ConstantDefine.URL_NO_DM11_JSON:
			return getOnlineSMS82(info);
		case ConstantDefine.URL_NO_DM15_JSON:
			return getOnlineSMS83(info);
		case ConstantDefine.URL_NO_SD29_JSON:
			return getOnlineSMS84(info);
		case ConstantDefine.URL_NO_SD31_JSON:
			return getOnlineSMS85(info);
		case ConstantDefine.URL_NO_SD32_JSON:
			return getOnlineSMS86(info);
		case ConstantDefine.URL_NO_SD33_JSON:
			return getOnlineSMS87(info);
		case ConstantDefine.URL_NO_LT06_JSON:
			return getOnlineSMS88(info);
		case ConstantDefine.URL_NO_SD34_JSON:
			return getOnlineSMS89(info);
		case ConstantDefine.URL_NO_DM16_JSON:
			return getOnlineSMS90(info);
		case ConstantDefine.URL_NO_DM17_JSON:
			return getOnlineSMS91(info);
		case ConstantDefine.URL_NO_YY08_JSON:
			return getOnlineSMS92(info);
		case ConstantDefine.URL_NO_SD44_JSON:
			return getOnlineSMS93(info);
		case ConstantDefine.URL_NO_SD45_JSON:
			return getOnlineSMS94(info);
		case ConstantDefine.URL_NO_YC26_JSON:
			return getOnlineSMS95(info);
		case ConstantDefine.URL_NO_LT07_JSON:
			return getOnlineSMS96(info);
		case ConstantDefine.URL_NO_YC28_JSON:
			return getOnlineSMS98(info);
		case ConstantDefine.URL_NO_DM19_JSON:
			return getOnlineSMS102(info);
		case ConstantDefine.URL_NO_YC29_JSON:
			return getOnlineSMS104(info);
		case ConstantDefine.URL_NO_LT09_JSON:
			return getOnlineSMS105(info);
		case ConstantDefine.URL_NO_SD47_JSON:
			return getOnlineSMS106(info);
		case ConstantDefine.URL_NO_SD48_JSON:
			return getOnlineSMS107(info);
		case ConstantDefine.URL_NO_SD49_JSON:
			return getOnlineSMS108(info);
		case ConstantDefine.URL_NO_YY09_JSON:
			return getOnlineSMS109(info);
		case ConstantDefine.URL_NO_YC31_JSON:
			return getOnlineSMS110(info);
		case ConstantDefine.URL_NO_YC33_JSON:
			return getOnlineSMS113(info);
		case ConstantDefine.URL_NO_SD50_JSON:
			return getOnlineSMS114(info);
		case ConstantDefine.URL_NO_SD52_JSON:
			return getOnlineSMS115(info);
		case ConstantDefine.URL_NO_SD54_JSON:
			return getOnlineSMS116(info);
		case ConstantDefine.URL_NO_SD55_JSON:
			return getOnlineSMS117(info);
		case ConstantDefine.URL_NO_SD57_JSON:
			return getOnlineSMS118(info);
		case ConstantDefine.URL_NO_SD59_JSON:
			return getOnlineSMS119(info);
		case ConstantDefine.URL_NO_SD61_JSON:
			return getOnlineSMS120(info);
		case ConstantDefine.URL_NO_SD62_JSON:
			return getOnlineSMS121(info);
		case ConstantDefine.URL_NO_SD63_JSON:
			return getOnlineSMS122(info);
		case ConstantDefine.URL_NO_SD64_JSON:
			return getOnlineSMS123(info);
		case ConstantDefine.URL_NO_SD66_JSON:
			return getOnlineSMS124(info);
		case ConstantDefine.URL_NO_YC35_JSON:
			return getOnlineSMS125(info);
		case ConstantDefine.URL_NO_SD68_JSON:
			return getOnlineSMS126(info);
		case ConstantDefine.URL_NO_SD69_JSON:
			return getOnlineSMS127(info);
		case ConstantDefine.URL_NO_YC36_JSON:
			return getOnlineSMS128(info);
		case ConstantDefine.URL_NO_SD75_JSON:
			return getOnlineSMS130(info);
		case ConstantDefine.URL_NO_DM35_JSON:
			return getOnlineSMS131(info);
		case ConstantDefine.URL_NO_SD77_JSON:
			return getOnlineSMS132(info);
		case ConstantDefine.URL_NO_SD79_JSON:
			return getOnlineSMS133(info);
		case ConstantDefine.URL_NO_SD80_JSON:
			return getOnlineSMS134(info);
		case ConstantDefine.URL_NO_SD82_JSON:
			return getOnlineSMS135(info);
		case ConstantDefine.URL_NO_SD83_JSON:
			return getOnlineSMS136(info);
		case ConstantDefine.URL_NO_SD84_JSON:
			return getOnlineSMS137(info);
		case ConstantDefine.URL_NO_SD81_JSON:
			return getOnlineSMS138(info);
		case ConstantDefine.URL_NO_SD85_JSON:
			return getOnlineSMS139(info);
		case ConstantDefine.URL_NO_SD87_JSON:
			return getOnlineSMS140(info);
		case ConstantDefine.URL_NO_SD90_JSON:
			return getOnlineSMS141(info);
		case ConstantDefine.URL_NO_SD91_JSON:
			return getOnlineSMS142(info);
		case ConstantDefine.URL_NO_LT11_JSON:
			return getOnlineSMS143(info);
		case ConstantDefine.URL_NO_SD93_JSON:
			return getOnlineSMS144(info);
		case ConstantDefine.URL_NO_YC37_JSON:
			return getOnlineSMS145(info);
		case ConstantDefine.URL_NO_YC38_JSON:
			return getOnlineSMS146(info);
		case ConstantDefine.URL_NO_YC39_JSON:
			return getOnlineSMS147(info);
		case ConstantDefine.URL_NO_SD94_JSON:
			return getOnlineSMS148(info);
		case ConstantDefine.URL_NO_YY11_JSON:
			return getOnlineSMS149(info);
		case ConstantDefine.URL_NO_SD95_JSON:
			return getOnlineSMS150(info);
		case ConstantDefine.URL_NO_SD96_JSON:
			return getOnlineSMS151(info);
		case ConstantDefine.URL_NO_SD97_JSON:
			return getOnlineSMS152(info);
		case ConstantDefine.URL_NO_SD100_JSON:
			return getOnlineSMS153(info);
		case ConstantDefine.URL_NO_SD101_JSON:
			return getOnlineSMS154(info);
//		case ConstantDefine.URL_NO_SDA4_JSON:等商户号下来
//			return getOnlineSMS155(info);
		case ConstantDefine.URL_NO_SDA5_JSON:
			return getOnlineSMS156(info);
		case ConstantDefine.URL_NO_SDA6_JSON:
			return getOnlineSMS157(info);
		case ConstantDefine.URL_NO_SDA7_JSON:
			return getOnlineSMS158(info);
		case ConstantDefine.URL_NO_SDD1_JSON:
			return getOnlineSMS159(info);
		case ConstantDefine.URL_NO_SDD2_JSON:
			return getOnlineSMS160(info);
		case ConstantDefine.URL_NO_SDZ2_JSON:
			return getOnlineSMS302(info);
		case ConstantDefine.URL_NO_SDD5_JSON:
			return getOnlineSMS161(info);
		case ConstantDefine.URL_NO_SDD6_JSON:
			return getOnlineSMS162(info);
		case ConstantDefine.URL_NO_SDD7_JSON:
			return getOnlineSMS163(info);
		case ConstantDefine.URL_NO_SDD8_JSON:
			return getOnlineSMS164(info);
		case ConstantDefine.URL_NO_SDD9_JSON:
			return getOnlineSMS165(info);
		case ConstantDefine.URL_NO_SDE1_JSON:
			return getOnlineSMS166(info);
		case ConstantDefine.URL_NO_SDE2_JSON:
			return getOnlineSMS167(info);
		case ConstantDefine.URL_NO_SDE3_JSON:
			return getOnlineSMS168(info);
		case ConstantDefine.URL_NO_YC41_JSON:
			return getOnlineSMS501(info);
		case ConstantDefine.URL_NO_LT12_JSON:
			return getOnlineSMS502(info);
		case ConstantDefine.URL_NO_YC42_JSON:
			return getOnlineSMS503(info);
		case ConstantDefine.URL_NO_SDE4_JSON:
			return getOnlineSMS169(info);
		case ConstantDefine.URL_NO_SDF1_JSON:
			return getOnlineSMS170(info);
		case ConstantDefine.URL_NO_SDF2_JSON:
			return getOnlineSMS171(info);
		case ConstantDefine.URL_NO_SDF3_JSON:
			return getOnlineSMS172(info);
		case ConstantDefine.URL_NO_SDF7_JSON:
			return getOnlineSMS173(info);
		case ConstantDefine.URL_NO_SDF8_JSON:
			return getOnlineSMS174(info);
		case 175:
			return getOnlineSMS175(info);
		case 176:
			return getOnlineSMS176(info);
		case 177:
			return getOnlineSMS177(info);
		case 178:
			return getOnlineSMS178(info);
		case 179:
			return getOnlineSMS179(info);
		case 180:
			return getOnlineSMS180(info);
		case 181:
			return getOnlineSMS181(info);
		case 182:
			return getOnlineSMS182(info);
		case 183:
			return getOnlineSMS183(info);
		case 184:
			return getOnlineSMS184(info);
		case 185:
			return getOnlineSMS185(info);
		case 186:
			return getOnlineSMS186(info);
		case 187:
			return getOnlineSMS187(info);
		case 188:
			return getOnlineSMS188(info);
		case 189:
			return getOnlineSMS189(info);
		case 190:
			return getOnlineSMS190(info);
		case 191:
			return getOnlineSMS191(info);
		case 192:
			return getOnlineSMS192(info);
		case 193:
			return getOnlineSMS193(info);
		case 194:
			return getOnlineSMS194(info);
		case 195:
			return getOnlineSMS195(info);
		case 196:
			return getOnlineSMS196(info);
		case 197:
			return getOnlineSMS197(info);
		case 198:
			return getOnlineSMS198(info);
		case 199:
			return getOnlineSMS199(info);
		case 200:
			return getOnlineSMS200(info);
		case 201:
			return getOnlineSMS201(info);

		case 202:
			return getOnlineSMS202(info);

		case 203:
			return getOnlineSMS203(info);
		case 204:
			return getOnlineSMS204(info);
			
		case 205:
			return getOnlineSMS205(info);
		case 206:
			return getOnlineSMS206(info);
		case 207:
			return getOnlineSMS207(info);
		case 208:
			return getOnlineSMS208(info);
		default:
			break;
		}
		return null;
	}

	public static void main(String[] args) {
		String timestamp = DateTool.getNow();
		System.out.println(timestamp.substring(0, 8));
		System.out.println(timestamp.substring(8, 14));
	}
	
	/**
	 * 
	 * 京东
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS208(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String mobile = info.getMobile();
			String keyword = info.getKeyword();

			StringBuilder urlparam = new StringBuilder();
			urlparam.append(keyword).append("&tel=").append(mobile);

			String reqResult = HttpTool.sendGetSetTimeout(url, urlparam.toString(), "5000");
			JSONObject json = JSONObject.parseObject(reqResult);
			String state = json.getString("msg");
//			String orderId2 = json.getString("orderId");
			logger.info("feixin:" + info.getSpId() + "请求结果：" + reqResult);

			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");

				result = JSONObject.toJSONString(resultMap);
			} else if ("1".equals(state)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");
//				resultMap.put("orderId", orderId2);
				result = JSONObject.toJSONString(resultMap);
			} else {
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.info("ELM:" + info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	/**
	 * 
	 * webqq新注册
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS207(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String mobile = info.getMobile();
			JSONObject jsonData = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			jsonArray.add(mobile);
			jsonData.put("data", jsonArray);//JSONObject对象中添加键值对
			
			String urlParam = jsonData.toJSONString();
			
			String reqResult = HttpTool.sendWEBQQBDPost(url, urlParam, "UTF-8", 5000);
			logger.info("webqq注册绑定:" + info.getSpId() + "请求结果：" + reqResult);

			Map<String, Object> resultMap = new HashMap<String, Object>();
			String reqResultCode = null;
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");

				result = JSONObject.toJSONString(resultMap);
			}else{
				JSONObject reqResultJson = JSONObject.parseObject(reqResult);
				reqResultCode = reqResultJson.getString("result");
			}
			if ("succ".equals(reqResultCode)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");

				result = JSONObject.toJSONString(resultMap);
			} else {
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.info("ELM:" + info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}
	/**
	 *  206 河北移动端
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS206(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String mobile = info.getMobile();
			String keyword = info.getKeyword();

			StringBuilder urlparam = new StringBuilder();
			urlparam.append(keyword).append("&phone=").append(mobile).append("&customId=").append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, urlparam.toString(), "5000");
			JSONObject json = JSONObject.parseObject(reqResult);
			String state = json.getString("code");
			String orderId2 = json.getString("orderId");
			logger.info("feixin:" + info.getSpId() + "请求结果：" + reqResult);

			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");

				result = JSONObject.toJSONString(resultMap);
			} else if ("0".equals(state)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");
				resultMap.put("orderId", orderId2);
				result = JSONObject.toJSONString(resultMap);
			} else {
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.info("ELM:" + info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	/**
	 * 
	 * 飞信
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS205(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String mobile = info.getMobile();
			String keyword = info.getKeyword();

			StringBuilder urlparam = new StringBuilder();
			urlparam.append(keyword).append("&phone=").append(mobile).append("&customId=").append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, urlparam.toString(), "5000");
			JSONObject json = JSONObject.parseObject(reqResult);
			String state = json.getString("code");
			String orderId2 = json.getString("orderId");
			logger.info("feixin:" + info.getSpId() + "请求结果：" + reqResult);

			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");

				result = JSONObject.toJSONString(resultMap);
			} else if ("0".equals(state)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");
				resultMap.put("orderId", orderId2);
				result = JSONObject.toJSONString(resultMap);
			} else {
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.info("ELM:" + info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}
	
	
	/**
	 * TB 胜达
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS204(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String mobile = info.getMobile();
			String keyword = info.getKeyword();

			StringBuilder urlparam = new StringBuilder();

			Map<String, Object> sendMap = new HashMap<String, Object>();
			sendMap.put("mobile", mobile);
			sendMap.put("msgId", mobile);
			sendMap.put("pid", keyword);
			String replys = JSONObject.toJSONString(sendMap);

			urlparam.append(keyword).append("&mobile=").append(mobile).append("&tcstr=").append(info.getFfId());

		    
			// 先进行一次接口调用和过滤筛选
			String[] urls =url.split("#");
			String url1 =urls[0];
			String  url2 =urls[1];
			if(!"0".equals(url2)){
				url2=url2+mobile;
				String filterOver = HttpTool.sendGetSetTime(url2, "5000");
				logger.info("过滤:" + info.getSpId() + "请求结果：" + filterOver);
				JSONObject json1 = JSONObject.parseObject(filterOver);
				String state1 = json1.getString("Success");
				
				Map<String, Object> resultMap1 = new HashMap<String, Object>();
				if (filterOver == null || filterOver.length() <= 0) {
					resultMap1.put("status", 2);
					resultMap1.put("orderid", info.getFfId());
					resultMap1.put("msg", "服务器超时请勿重复提交成功会有同步数据");
					result = JSONObject.toJSONString(resultMap1);
					return  result;
				} else if (!"false".equals(state1)) {
							resultMap1.put("status", 3);
							resultMap1.put("orderid", info.getFfId());
							resultMap1.put("msg", "fail");
							result = JSONObject.toJSONString(resultMap1);
							return  result;
				}
			}
			String reqResult = HttpTool.sendGetSetTimeout(url1, urlparam.toString(), "5000");
			logger.info("hqtb:" + info.getSpId() + "请求结果：" + reqResult);

			JSONObject resJson = JSONObject.parseObject(reqResult);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			String status = resJson.getString("Code");
			
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");
				result = JSONObject.toJSONString(resultMap);
			} else if ("1".equals(status)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");
				result = JSONObject.toJSONString(resultMap);
			} else {
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.info("ELM:" + info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	/**
	 * 
	 * 西瓜淘宝
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS203(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String mobile = info.getMobile();
			String keyword = info.getKeyword();

			StringBuilder urlparam = new StringBuilder();
			urlparam.append(keyword).append("&phone=").append(mobile).append("&imsi=").append(info.getImsi())
					.append("&extra=").append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, urlparam.toString(), "5000");
			JSONObject json = JSONObject.parseObject(reqResult);
			String state = json.getString("status");
			logger.info("西瓜淘宝:" + info.getSpId() + "请求结果：" + reqResult);

			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");

				result = JSONObject.toJSONString(resultMap);
			} else if ("0".equals(state)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");

				result = JSONObject.toJSONString(resultMap);
			} else {
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.info("ELM:" + info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	/**
	 * 
	 * 探探
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS202(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String mobile = info.getMobile();
			String keyword = info.getKeyword();

			StringBuilder urlparam = new StringBuilder();
			urlparam.append(keyword).append("&tel=").append(mobile);

			String reqResult = HttpTool.sendGetSetTimeout(url, urlparam.toString(), "5000");
			JSONObject json = JSONObject.parseObject(reqResult);
			String state = json.getString("msg");
			logger.info("wbjf:" + info.getSpId() + "请求结果：" + reqResult);

			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");

				result = JSONObject.toJSONString(resultMap);
			} else if ("1".equals(state)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");

				result = JSONObject.toJSONString(resultMap);
			} else {
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.info("ELM:" + info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	/**
	 * 
	 * TB
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS201(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String mobile = info.getMobile();
			String keyword = info.getKeyword();

			StringBuilder urlparam = new StringBuilder();
			urlparam.append(keyword).append("&phone=").append(mobile);

			String reqResult = HttpTool.sendGetSetTimeout(url, urlparam.toString(), "5000").trim();
			logger.info("wbjf:" + info.getSpId() + "请求结果：" + reqResult);

			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");

				result = JSONObject.toJSONString(resultMap);
			} else if ("ok".equals(reqResult)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");

				result = JSONObject.toJSONString(resultMap);
			} else {
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.info("ELM:" + info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	/**
	 * 
	 * 微博解封
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS200(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
//			String cpid = info.getCpId();
			String mobile = info.getMobile();
			String keyword = info.getKeyword();

			StringBuilder urlparam = new StringBuilder();
			urlparam.append(keyword).append("&telephone=").append(mobile);

			String reqResult = HttpTool.sendGetSetTimeout(url, urlparam.toString(), "5000");
			JSONObject json = JSONObject.parseObject(reqResult);
			String state = json.getString("code");
			logger.info("wbjf:" + info.getSpId() + "请求结果：" + reqResult);

			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");

				result = JSONObject.toJSONString(resultMap);
			} else if ("00".equals(state)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");

				result = JSONObject.toJSONString(resultMap);
			} else {
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.info("ELM:" + info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	/**
	 * 
	 * bkzfw,贝壳找房网
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS199(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String mobile = info.getMobile();
			String keyword = info.getKeyword();

			StringBuilder urlparam = new StringBuilder();
			urlparam.append(keyword).append("&mobile=").append(mobile).append("&imsi=").append(info.getImsi());

			String reqResult = HttpTool.sendGetSetTimeout(url, urlparam.toString(), "5000");
			JSONObject json = JSONObject.parseObject(reqResult);
			String state = json.getString("state");
			logger.info("BKZFW:" + info.getSpId() + "请求结果：" + reqResult);

			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");

				result = JSONObject.toJSONString(resultMap);
			} else if ("ok".equals(state)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");

				result = JSONObject.toJSONString(resultMap);
			} else {
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.info("ELM:" + info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	/**
	 * webqq翰臣
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS198(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String mobile = info.getMobile();
			String keyword = info.getKeyword();

			StringBuilder urlparam = new StringBuilder();
			urlparam.append(keyword).append("&mobile=").append(mobile);

			String reqResult = HttpTool.sendGetSetTimeout(url, urlparam.toString(), "5000");
			logger.info("WEBQQ:" + info.getSpId() + "请求结果：" + reqResult);

			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");

				result = JSONObject.toJSONString(resultMap);
			} else if ("OK".equals(reqResult)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");

				result = JSONObject.toJSONString(resultMap);
			} else {
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.info("ELM:" + info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	/**
	 * 瞿JD2
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS197(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String mobile = info.getMobile();
			String keyword = info.getKeyword();

			StringBuilder urlparam = new StringBuilder();

			Map<String, Object> sendMap = new HashMap<String, Object>();
			sendMap.put("mobile", mobile);
			sendMap.put("msgId", mobile);
			sendMap.put("pid", keyword);
			String replys = JSONObject.toJSONString(sendMap);

			urlparam.append(url).append(replys);

			String reqResult = HttpTool.sendPost(urlparam.toString(), 5000);
			logger.info("ELM:" + info.getSpId() + "请求结果：" + reqResult);

			JSONObject resJson = JSONObject.parseObject(reqResult);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			String status = resJson.getString("Success");
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");

				result = JSONObject.toJSONString(resultMap);
			} else if ("true".equals(status)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");

				result = JSONObject.toJSONString(resultMap);
			} else {
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.info("ELM:" + info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	/**
	 * MG沈总
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS196(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String mobile = info.getMobile();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}

			param.append("&phone=").append(mobile).append("&imsi=").append(info.getImsi());
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "5000");
			logger.info("MG:" + info.getSpId() + "请求结果：" + reqResult);

			JSONObject resJson = JSONObject.parseObject(reqResult);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			String status = resJson.getString("success");
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");

				result = JSONObject.toJSONString(resultMap);
			} else if ("true".equals(status)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");
				result = JSONObject.toJSONString(resultMap);
			} else {
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.info("ELM:" + info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	/**
	 * ELM
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS195(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String mobile = info.getMobile();
			String keyword = info.getKeyword();

			StringBuilder urlparam = new StringBuilder();

			Map<String, Object> sendMap = new HashMap<String, Object>();
			sendMap.put("mobile", mobile);
			sendMap.put("msgId", mobile);
			sendMap.put("pid", keyword);
			String replys = JSONObject.toJSONString(sendMap);

			urlparam.append(url).append(replys);

			String reqResult = HttpTool.sendPost(urlparam.toString(), 5000);
			logger.info("ELM:" + info.getSpId() + "请求结果：" + reqResult);

			JSONObject resJson = JSONObject.parseObject(reqResult);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			String status = resJson.getString("Success");
			if (reqResult == null || reqResult.length() <= 0) {
				resultMap.put("status", 2);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");

				result = JSONObject.toJSONString(resultMap);
			} else if ("true".equals(status)) {
				resultMap.put("status", 0);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "success");

				result = JSONObject.toJSONString(resultMap);
			} else {
				resultMap.put("status", 3);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "fail");
				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.info("ELM:" + info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	/**
	 * 把酒当歌工作室12306
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS194(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String mobile = info.getMobile();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}

			param.append("phone=").append(mobile).append("&code=").append(info.getCode());
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "5000");
			logger.info("12306十一" + info.getSpId() + "请求结果：" + reqResult);

			JSONObject resJson = JSONObject.parseObject(reqResult);
			String res = resJson.getString("result");
			if (reqResult == null || reqResult.length() <= 0) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("errNo", 2);
				resultMap.put("return", "false");
				resultMap.put("message", "服务器超时请勿重复提交成功会有同步数据");

				reqResult = JSONObject.toJSONString(resultMap);
			} else if ("0".equals(res)) {
				reqResult = "ok";
			}

			result = reqResult;
		} catch (Exception e) {
			logger.info("12306十一" + info.getSpId() + "指令失败!");

			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	/**
	 * 把酒当歌工作室12306
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS193(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String mobile = info.getMobile();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}

			param.append("mobile=").append(mobile).append("&smsContent=").append(info.getCode());
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "5000");
			logger.info("12306把酒当歌工作室" + info.getSpId() + "请求结果：" + reqResult);

			JSONObject resJson = JSONObject.parseObject(reqResult);
			String res = resJson.getString("return");
			if (reqResult == null || reqResult.length() <= 0) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("errNo", 2);
				resultMap.put("return", "false");
				resultMap.put("message", "服务器超时请勿重复提交成功会有同步数据");

				reqResult = JSONObject.toJSONString(resultMap);
			} else if ("true".equals(res)) {
				reqResult = "ok";
			}

			result = reqResult;
		} catch (Exception e) {
			logger.info("12306把酒当歌工作室" + info.getSpId() + "指令失败!");

			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	/**
	 * 瞿jd2
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS192(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String mobile = info.getMobile();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}
			param.append("mobile=").append(mobile);
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "5000");
			logger.info(info.getSpId() + "请求结果:" + reqResult);

			Map<String, String> resMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {

				if ("1".equals(reqResult)) {
					resMap.put("orderid", info.getFfId());
					resMap.put("msg", "success");
					resMap.put("status", "0");
				} else {
					resMap.put("orderid", info.getFfId());
					resMap.put("msg", "error");
					resMap.put("status", "1");
				}

			} else {
				resMap.put("orderid", info.getFfId());
				resMap.put("msg", "request time out");
				resMap.put("status", "2");
			}

			result = JSONObject.toJSONString(resMap);
		} catch (Exception e) {
			logger.error(info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	/**
	 * 陈wb
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS191(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String mobile = info.getMobile();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}
			param.append("phone=").append(mobile);
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "5000");
			logger.info(info.getSpId() + "请求结果:" + reqResult);

			Map<String, String> resMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String code = jsonobj.getString("code");
				if ("200".equals(code)) {
					resMap.put("orderid", info.getFfId());
					resMap.put("msg", "success");
					resMap.put("status", "0");
				} else {
					resMap.put("orderid", info.getFfId());
					resMap.put("msg", "error");
					resMap.put("status", "1");
				}

			} else {
				resMap.put("orderid", info.getFfId());
				resMap.put("msg", "request time out");
				resMap.put("status", "2");
			}

			result = JSONObject.toJSONString(resMap);
		} catch (Exception e) {
			logger.error(info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	/**
	 * jd瞿
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS190(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String mobile = info.getMobile();
			String keyword = info.getFees();

			String url2 = info.getMatchRegex();
			StringBuilder resUrl2 = new StringBuilder();
			resUrl2.append(url2).append("&p=").append(mobile);
			String reqResult2 = HttpTool.sendGet1(resUrl2.toString());
			logger.info("筛选结果:" + reqResult2);

			JSONObject jbj = new JSONObject();
			jbj.put("mobile", mobile);
			jbj.put("msgId", info.getFfId());
			jbj.put("pid", keyword);

			String param = jbj.toJSONString();
			StringBuilder resUrl = new StringBuilder();
			resUrl.append(url).append(param);
			String reqResult = null;

			Map<String, String> resMap = new HashMap<String, String>();
			resMap.put("orderid", info.getFfId());

			if ("1|null".equals(reqResult2)) {
				reqResult = HttpTool.sendPost(resUrl.toString(), 5000);
			} else {
				resMap.put("msg", "double");
				resMap.put("status", "1");
				result = JSONObject.toJSONString(resMap);
				return result;
			}
			logger.info(info.getSpId() + "请求结果:" + reqResult);
			if (reqResult != null & reqResult.length() > 0) {
				if ("OK".equals(reqResult)) {
					resMap.put("msg", "success");
					resMap.put("status", "0");
				} else {
					resMap.put("msg", "error");
					resMap.put("status", "2");
				}
			} else {
				resMap.put("msg", "request time out");
				resMap.put("status", "3");
			}
			result = JSONObject.toJSONString(resMap);

		} catch (Exception e) {
			logger.error(info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	/**
	 * 12306瞿(2)
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS189(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String mobile = info.getMobile();
			String keyword = info.getKeyword();
			String code = info.getCode();
			StringBuilder param = new StringBuilder();

			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}

			param.append("&telephone=").append(mobile).append("&code=").append(code);

			String reqResult = HttpTool.sendGet(url, param.toString());
			logger.info(info.getSpId() + "请求结果:" + reqResult);

			Map<String, String> resMap = new HashMap<String, String>();

			if (reqResult != null & reqResult.length() > 0) {
				if ("success".equals(reqResult)) {
					reqResult = "ok";
				} else {
					reqResult = "error";
				}
			} else {
				resMap.put("status", "1");
				resMap.put("orderid", info.getFfId());
				resMap.put("msg", "request time out");
				reqResult = JSONObject.toJSONString(resMap);
			}
			result = reqResult;

		} catch (Exception e) {
			logger.error(info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	/**
	 * QQ绑定安全中心
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS188(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String mobile = info.getMobile();
			String pwd = info.getImei();
			String qq = info.getImsi();

			JSONObject json = new JSONObject();
			JSONObject jsons = new JSONObject();
			json.put("qq", qq);
			json.put("pass", pwd);
			json.put("phone", mobile);

			JSONArray jsonArray = new JSONArray();
			jsonArray.add(json);

			jsons.put("data", jsonArray);
			String param = jsons.toJSONString();

			String reqResult = HttpTool.sendQQPost(url, param, 5000);
			logger.info(info.getSpId() + "请求结果:" + reqResult);

			Map<String, String> resMap = new HashMap<String, String>();

			if (reqResult != null & reqResult.length() > 0) {
				JSONObject js = JSONObject.parseObject(reqResult);
				String s = (String) js.get("result");
				if ("succ".equals(s)) {
					resMap.put("status", "1");
					resMap.put("orderid", info.getFfId());
					resMap.put("msg", "success");

				} else {
					resMap.put("status", "2");
					resMap.put("orderid", info.getFfId());
					resMap.put("msg", "error");
				}

			} else {
				resMap.put("status", "3");
				resMap.put("orderid", info.getFfId());
				resMap.put("msg", "request time out");
			}
			result = reqResult = JSONObject.toJSONString(resMap);

		} catch (Exception e) {
			logger.error(info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	/**
	 * 和通行证妮妮
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS187(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String[] str = keyword.split("#");

			StringBuilder param = new StringBuilder();

			param.append("service=").append(str[0]).append("&channelId=").append(str[1]).append("&phone=")
					.append(info.getMobile()).append("&customId=").append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "5000");
			logger.info(info.getSpId() + "请求结果:" + reqResult);

			Map<String, String> resMap = new HashMap<String, String>();

			if (reqResult != null & reqResult.length() > 0) {

				result = reqResult;

			} else {
				resMap.put("status", "1");
				resMap.put("orderid", info.getFfId());
				resMap.put("msg", "request time out");
				reqResult = JSONObject.toJSONString(resMap);
				result = reqResult;
			}

		} catch (Exception e) {
			logger.error(info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	/**
	 * 12306注册瞿项目一
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS186(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String time = DateTool.getSecondTimestampTwo(new Date());
			;
			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}
			param.append("&tel=").append(info.getMobile()).append("&smsContent=").append(info.getCode()).append("&ext=")
					.append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "3000");
			logger.info(info.getSpId() + "请求结果:" + reqResult);

			Map<String, String> resMap = new HashMap<String, String>();

			if (reqResult != null & reqResult.length() > 0) {
				if ("OK".equals(reqResult) || "ok".equals(reqResult)) {
					reqResult = "ok";
				} else {
					reqResult = "error";
				}
			} else {
				resMap.put("status", "1");
				resMap.put("orderid", info.getFfId());
				resMap.put("msg", "request time out");
				reqResult = JSONObject.toJSONString(resMap);
			}
			result = reqResult;

		} catch (Exception e) {
			logger.error(info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	/**
	 * 12306注册张项目一
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS185(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String time = DateTool.getSecondTimestampTwo(new Date());
			;
			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}
			param.append("&phone=").append(info.getMobile()).append("&msg=").append(info.getCode()).append("&time=")
					.append(time);

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "3000");
			logger.info(info.getSpId() + "请求结果:" + reqResult);

			Map<String, String> resMap = new HashMap<String, String>();

			if (reqResult != null & reqResult.length() > 0) {
				if ("1".equals(reqResult)) {
					reqResult = "ok";
				} else {
					reqResult = "error";
				}
			} else {
				resMap.put("status", "1");
				resMap.put("orderid", info.getFfId());
				resMap.put("msg", "request time out");
				reqResult = JSONObject.toJSONString(resMap);
			}
			result = reqResult;

		} catch (Exception e) {
			logger.error(info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	/**
	 * 12306注册杭州翰臣(未使用)
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS184(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}
			param.append("&phone=").append(info.getMobile()).append("&code=").append(info.getCode());
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "3000");
			logger.info(info.getSpId() + "请求结果:" + reqResult);

			Map<String, String> resMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {

				result = reqResult;

			} else {
				resMap.put("result", "1");
				resMap.put("orderId", info.getFfId());
				resMap.put("message", "request time out");
				result = JSONObject.toJSONString(resMap);
			}

		} catch (Exception e) {
			logger.error(info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	/**
	 * 微信账号注册手机号
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS183(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}
			param.append("&phone=").append(info.getMobile()).append("&customId=").append(info.getFfId());
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "3000");
			logger.info(info.getSpId() + "请求结果:" + reqResult);

			Map<String, String> resMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {

				result = reqResult;

			} else {
				resMap.put("code", "100");
				resMap.put("orderId", info.getFfId());
				resMap.put("message", "request time out");
				result = JSONObject.toJSONString(resMap);
			}

		} catch (Exception e) {
			logger.error(info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	/**
	 * 咪咕账号注册手机号提交
	 * 
	 * @param info
	 * @return
	 */

	private static String getOnlineSMS182(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}

			param.append("&phone=").append(info.getMobile()).append("&customId=").append(info.getFfId());
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "3000");
			logger.info(info.getSpId() + "请求结果:" + reqResult);

			Map<String, String> resMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {

				result = reqResult;

			} else {
				resMap.put("code", "1");
				resMap.put("orderid", info.getFfId());
				resMap.put("msg", "request time out");
				result = JSONObject.toJSONString(resMap);
			}

		} catch (Exception e) {
			logger.error(info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	/**
	 * 微博解封
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS181(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&mobile_sms=").append(info.getMobile()).append(",").append(info.getCode());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "8000");
			logger.info("12306注册" + info.getSpId() + "请求结果：" + reqResult);

			if (reqResult != null && reqResult.length() > 0) {
				reqResult = reqResult.replace("[", "");
				reqResult = reqResult.replace("]", "");
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String result2 = jsonobj.getString("result");

				if (result2.equals("true")) {
					reqResult = "ok";
				}
				if (result2.equals("false")) {
					reqResult = "false";
				}
			}

			if (reqResult == null || reqResult.length() <= 0) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("errNo", 2);
				resultMap.put("return", "false");
				resultMap.put("message", "服务器超时请勿重复提交成功会有同步数据");

				reqResult = JSONObject.toJSONString(resultMap);

			}

			result = reqResult;

		} catch (Exception e) {
			logger.error("12306注册" + info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	/**
	 * 微博解封
	 * 
	 * @param info
	 * @return
	 */
	private static String getOnlineSMS180(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&mobile=").append(info.getMobile()).append("&imsi=").append(info.getImsi());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "8000");
			logger.info("微博解封" + info.getSpId() + "请求结果：" + reqResult);

//			if(reqResult != null && reqResult.length() > 0){
//				JSONObject jsonobj = JSONObject.parseObject(reqResult);
//				String smsTask = jsonobj.getString("smsTask");
//				
//				if(smsTask.length()<=2){
//					reqResult = "error";
//				}
//			}

			if (reqResult == null || reqResult.length() <= 0) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("errNo", 2);
				resultMap.put("return", "false");
				resultMap.put("message", "服务器超时请勿重复提交成功会有同步数据");

				reqResult = JSONObject.toJSONString(resultMap);

			}

			result = reqResult;

		} catch (Exception e) {
			logger.error("微博解封" + info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	/**
	 * 快手手机号提交
	 * 
	 * @param info
	 * @return
	 */

	private static String getOnlineSMS179(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}

			param.append("phone=").append(info.getMobile());
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "3000");
			logger.info(info.getSpId() + "请求结果:" + reqResult);

			Map<String, String> resMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.startsWith("s")) {
				resMap.put("status", "0");
				resMap.put("orderid", info.getFfId());
				resMap.put("msg", "success");
				result = JSONObject.toJSONString(resMap);
			} else {
				resMap.put("status", "1");
				resMap.put("orderid", info.getFfId());
				resMap.put("msg", "request time out");
				result = JSONObject.toJSONString(resMap);
			}

		} catch (Exception e) {
			logger.error(info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS178(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}
			String province = info.getProvince();
			String area = provincezJDMap.get(province);
			if (!(area.equals("gd") || area.equals("js") || area.equals("hn") || area.equals("zj") || area.equals("sd")
					|| area.equals("hb") || area.equals("hn1"))) {
				area = "ys";
			}

			param.append("tel=").append(info.getMobile()).append("&area=").append(area).append("&ext=")
					.append(info.getFfId());
			;
			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "3000");
			logger.info("sd18请求结果:" + reqResult);

			Map<String, String> resMap = new HashMap<String, String>();
			if (reqResult != null) {
				resMap.put("status", "0");
				resMap.put("orderid", info.getFfId());
				resMap.put("msg", "success");
				result = JSONObject.toJSONString(resMap);
			} else {
				resMap.put("status", "1");
				resMap.put("orderid", info.getFfId());
				resMap.put("msg", "request time out");
				result = JSONObject.toJSONString(resMap);
			}

		} catch (Exception e) {
			logger.error("sd18指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

//	private static String getOnlineSMS178(OrderReqInfo info) {
//		String result = "error";
//		try  {
//			
//			String url = info.getUrl();
//			String keyword = info.getKeyword();
//			String price = info.getPrice();
//			String callbackurl = info.getCallbackurl();
//			
//			String province = info.getProvince();
//			String area = provincezJDMap.get(province);
//			if(!(area.equals("gd")||area.equals("js")||area.equals("hn")||
//					area.equals("zj")||area.equals("sd")||area.equals("hb")||
//					area.equals("hn1"))){
//				area="ys";
//			}
//			String tel = info.getMobile();
//
//			
//			StringBuilder param = new StringBuilder();
//			param.append("tel=").append(tel).append("&area=").append(area).append("&ext=").append(info.getFfId());
//			
//			String reqResult = HttpTool.sendGetSetTimeout(url,param.toString(), "7000");
//			logger.info(info.getSpId()+"请求结果:"+reqResult);
//			Map<String, String> payMap = new HashMap<String, String>();
//			if (reqResult != null && reqResult.length() > 0) {
//				JSONObject jsonobj = JSONObject.parseObject(reqResult);
//				String resultCode = jsonobj.getString("status");
//				if ("0".equals(resultCode)) {
//					String requrl = jsonobj.getString("data");
//					payMap.put("url", requrl);
//					payMap.put("status", "0");
//					payMap.put("orderid", info.getFfId());
//					payMap.put("spid", info.getSpId());
//					
//				} else {
//					payMap.put("status", "2");
//					payMap.put("msg", "back error "+resultCode);
//				}
//			} else {
//				payMap.put("status", "1");
//				payMap.put("msg", "request time out");
//			}
//			
//			result = JSONObject.toJSONString(payMap);
//			logger.info(info.getSpId()+"生成json:"+result);
//			
//		} catch (Exception e) {
//			logger.error(info.getSpId()+"指令失败!");
//			logger.error(e.getMessage(), e.getCause());
//		}
//		
//		return result;
//		
//	}
//	

	private static String getOnlineSMS177(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String callbackurl = info.getCallbackurl();

			String[] tmp = keyword.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String backurl = tmp[3];
			String pay_type = tmp[4];

			String timestamp = DateTool.getTimestamp(new Date());
			String time = timestamp.substring(0, timestamp.length() - 3);
			if (callbackurl == null || callbackurl.length() <= 0) {
				callbackurl = backurl;
			}

			String out_trade_no = info.getSpId() + info.getFfId();

			Map<String, Object> restmap = new HashMap<String, Object>();
			restmap.put("mch", mchid);
			restmap.put("key", out_trade_no);
			restmap.put("pay_type", pay_type);
			restmap.put("money", price);
			restmap.put("time", time);
			restmap.put("order_id", out_trade_no);
			restmap.put("notify_url", notityurl);
			restmap.put("return_url", callbackurl);

			StringBuilder signparam = new StringBuilder();
			signparam.append(out_trade_no).append(price).append(pay_type).append(time).append(mchid)
					.append(MD5.md5(appsecret, "utf-8"));

			String sign = MD5.md5(signparam.toString(), "utf-8");
//			restmap.put("sign",sign);

			String param = PaySignUtil.getParamStr(restmap);
			param = param + "&sign=" + sign;

			String reqResult = HttpsTool.sendGet(url + "?" + param, 7000);
			logger.info(info.getSpId() + "请求结果:" + reqResult);
			Map<String, String> payMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String resultCode = jsonobj.getString("ok");
				if ("true".equals(resultCode)) {
					String requrl = jsonobj.getString("data");
					payMap.put("url", requrl);
					payMap.put("status", "0");
					payMap.put("orderid", info.getFfId());
					payMap.put("spid", info.getSpId());
				} else {
					payMap.put("status", "2");
					payMap.put("msg", "back error " + resultCode);
				}
			} else {
				payMap.put("status", "1");
				payMap.put("msg", "request time out");
			}

			result = JSONObject.toJSONString(payMap);
			logger.info(info.getSpId() + "生成json:" + result);

		} catch (Exception e) {
			logger.error(info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS176(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String callbackurl = info.getCallbackurl();

			String[] tmp = keyword.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String backurl = tmp[3];
			String pay_type = tmp[4];

			if (callbackurl == null || callbackurl.length() <= 0) {
				callbackurl = backurl;
			}

			String out_trade_no = info.getSpId() + info.getFfId();

			Map<String, String> restmap = new HashMap<String, String>();
			restmap.put("pay_type", pay_type);
			restmap.put("mch_id", mchid);
			restmap.put("out_trade_no", out_trade_no);
			restmap.put("back_url", callbackurl);
			restmap.put("notify_url", notityurl);
			restmap.put("total_fee", price);
			restmap.put("ip", info.getIp());
			String sign = PayUtil.getSign(restmap, appsecret);
			restmap.put("sign", sign);

			String param = JSONObject.toJSONString(restmap);
			String reqResult = HttpTool.sendPost(url, param, "8000");

			logger.info(info.getSpId() + "请求结果:" + reqResult);
			Map<String, String> payMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String resultCode = jsonobj.getString("status");
				if ("200".equals(resultCode)) {
					String requrl = jsonobj.getString("pay_url");
					payMap.put("url", requrl);
					payMap.put("status", "0");
					payMap.put("orderid", info.getFfId());
					payMap.put("spid", info.getSpId());

				} else {
					payMap.put("status", "2");
					payMap.put("msg", "back error " + resultCode);
				}
			} else {
				payMap.put("status", "1");
				payMap.put("msg", "request time out");
			}

			result = JSONObject.toJSONString(payMap);
			logger.info(info.getSpId() + "生成json:" + result);

		} catch (Exception e) {
			logger.error(info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS175(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}
			param.append("tel=").append(info.getMobile());

			String reqResult = HttpTool.sendPost2(url, param.toString(), "10000");
			logger.info(info.getSpId() + "请求结果:" + reqResult);


			Map<String, String> resMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {
				resMap.put("status", "0");
				resMap.put("orderid", info.getFfId());
				resMap.put("msg", "success");
				result = JSONObject.toJSONString(resMap);
			} else {
				resMap.put("status", "1");
				resMap.put("orderid", info.getFfId());
				resMap.put("msg", "request time out");
				result = JSONObject.toJSONString(resMap);
			}

		} catch (Exception e) {
			logger.error(info.getSpId() + "指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS174(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}
			param.append("phone=").append(info.getMobile());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "3000");
			logger.info("微博请求结果:" + reqResult);

			Map<String, String> resMap = new HashMap<String, String>();
			if (reqResult != null) {
				resMap.put("status", "0");
				resMap.put("orderid", info.getFfId());
				resMap.put("msg", "success");
				result = JSONObject.toJSONString(resMap);
			} else {
				resMap.put("status", "1");
				resMap.put("orderid", info.getFfId());
				resMap.put("msg", "request time out");
				result = JSONObject.toJSONString(resMap);
			}

		} catch (Exception e) {
			logger.error("微博指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS173(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String callbackurl = info.getCallbackurl();

			BigDecimal b1 = new BigDecimal(price);
			BigDecimal b2 = new BigDecimal(100);
			price = b1.divide(b2).toString();

			String[] tmp = keyword.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String backurl = tmp[3];
			String type = tmp[4];

			if (callbackurl == null || callbackurl.length() <= 0) {
				callbackurl = backurl;
			}

			String out_trade_no = info.getSpId() + info.getFfId();

			/*
			 * Map<String, String> restmap = new HashMap<String, String>();
			 * restmap.put("parter",mchid); restmap.put("type",type);
			 * restmap.put("merId",mchid); restmap.put("orderId",out_trade_no);
			 * restmap.put("transDate",timestamp); restmap.put("transAmount",price);
			 * restmap.put("transCurrency",transCurrency);
			 * restmap.put("transChanlName",transChanlName);
			 * restmap.put("pageNotifyUrl",callbackurl);
			 * restmap.put("backNotifyUrl",notityurl);
			 */

			StringBuilder md5Param = new StringBuilder();
			md5Param.append("parter=").append(mchid).append("&type=").append(type).append("&value=").append(price)
					.append("&orderid=").append(out_trade_no).append("&callbackurl=").append(notityurl);

			String sign = MD5.md5(md5Param + appsecret, "GB2312").toLowerCase();
			md5Param.append("&sign=").append(sign);
			md5Param.append("&hrefbackurl=").append(callbackurl);
			md5Param.append("&payerIp=").append(info.getIp());
			String param = md5Param.toString();

			Map<String, String> payMap = new HashMap<String, String>();
			String requrl = url + "?" + param;
			payMap.put("url", requrl);
			payMap.put("status", "0");
			payMap.put("orderid", info.getFfId());
			payMap.put("spid", info.getSpId());

			result = JSONObject.toJSONString(payMap);
			logger.info("sdF7生成json:" + result);

		} catch (Exception e) {
			logger.error("sdF7指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS172(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String callbackurl = info.getCallbackurl();

			BigDecimal b1 = new BigDecimal(price);
			BigDecimal b2 = new BigDecimal(100);
			price = b1.divide(b2).toString();

			String[] tmp = keyword.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String backurl = tmp[3];
			String versionId = tmp[4];
			String transCurrency = tmp[5];
			String transChanlName = tmp[6];
			String businessType = tmp[7];
			String domain = tmp[8];

			if (callbackurl == null || callbackurl.length() <= 0) {
				callbackurl = backurl;
			}

			String timestamp = DateTool.getNow();
			String out_trade_no = info.getSpId() + info.getFfId();

			/*
			 * Map<String, String> restmap = new HashMap<String, String>();
			 * restmap.put("versionId",versionId); restmap.put("businessType",businessType);
			 * restmap.put("merId",mchid); restmap.put("orderId",out_trade_no);
			 * restmap.put("transDate",timestamp); restmap.put("transAmount",price);
			 * restmap.put("transCurrency",transCurrency);
			 * restmap.put("transChanlName",transChanlName);
			 * restmap.put("pageNotifyUrl",callbackurl);
			 * restmap.put("backNotifyUrl",notityurl);
			 */

			StringBuilder md5Param = new StringBuilder();
			md5Param.append("versionId=").append(versionId).append("&businessType=").append(businessType)
					.append("&merId=").append(mchid).append("&orderId=").append(out_trade_no).append("&transDate=")
					.append(timestamp).append("&transAmount=").append(price).append("&transCurrency=")
					.append(transCurrency).append("&transChanlName=").append(transChanlName).append("&pageNotifyUrl=")
					.append(callbackurl).append("&backNotifyUrl=").append(notityurl).append("&dev=");

			String sign = MD5.md5(md5Param + appsecret, "utf-8").toUpperCase();
			md5Param.append("&signData=").append(sign);
			String param = md5Param.toString();
			String reqResult = HttpTool.sendPost(url, param, "utf-8", 8000);

			logger.info("sdF3请求结果:" + reqResult);
			Map<String, String> payMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {
//				String[] reqtmp = reqResult.split("<head>");
//				String insertstr = "<head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />";
//				String htmlstr =  reqtmp[0] + insertstr + reqtmp[1];
				String requrl = parseURL3(reqResult, info, "request", domain, "utf-8");
				payMap.put("url", requrl);
				payMap.put("status", "0");
				payMap.put("orderid", info.getFfId());
				payMap.put("spid", info.getSpId());
				result = JSONObject.toJSONString(payMap);
			} else {
				payMap.put("status", "1");
				payMap.put("msg", "request time out");
			}

			result = JSONObject.toJSONString(payMap);
			logger.info("sdF3生成json:" + result);

		} catch (Exception e) {
			logger.error("sdF3指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS171(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String callbackurl = info.getCallbackurl();

			/*
			 * BigDecimal b1 = new BigDecimal(price); BigDecimal b2 = new BigDecimal(100);
			 * price = b1.divide(b2).toString();
			 */
			/*
			 * if ("9950".equals(price)) { price = "10000"; } else if
			 * ("19900".equals(price)) { price = "20000"; } else if ("29850".equals(price))
			 * { price = "30000"; } else if ("49750".equals(price)) { price = "50000"; }
			 * else { Map<String, String> temresmap = new HashMap<String, String>();
			 * temresmap.put("status", "5"); temresmap.put("msg",
			 * "price error, noly 9950,19900,29850,49750"); result =
			 * JSONObject.toJSONString(temresmap); return result; }
			 */

			String[] tmp = keyword.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String backurl = tmp[3];
			String paytype = tmp[4];
			String charSet = tmp[5];
			String versioNo = tmp[6];
			String signType = tmp[7];

			if (callbackurl == null || callbackurl.length() <= 0) {
				callbackurl = backurl;
			}

			String timestamp = DateTool.getNow();
			String out_trade_no = info.getSpId() + info.getFfId();

			Map<String, String> restmap = new HashMap<String, String>();
			restmap.put("charSet", charSet);
			restmap.put("versioNo", versioNo);
			restmap.put("signType", signType);
			restmap.put("returnUrl", callbackurl);
			restmap.put("notifyUrl", notityurl);
			restmap.put("ip", info.getIp());
			restmap.put("orderId", out_trade_no);
			restmap.put("appKey", mchid);
			restmap.put("product", info.getCpParam());
			restmap.put("timestamp", timestamp);
			restmap.put("price", price);
			restmap.put("payType", paytype);
			String md5Param = PayUtil.createQXSign(restmap, false, ",");
			String sign = MD5.md5(md5Param + "," + appsecret, "utf-8");
			String param = PayUtil.getFormData(restmap, false) + "&sign=" + sign;

			String reqResult = HttpTool.sendPost(url, param, "8000");

			logger.info("乐奕sdF1请求结果:" + reqResult);
			Map<String, String> payMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String resultCode = jsonobj.getString("status");
				if ("200".equals(resultCode)) {
					String requrl = jsonobj.getString("payUrl");
					/*
					 * String res = HttpTool.sendQXGet(requrl); String[] tmparr =
					 * res.split("action=\""); String s = tmparr[1]; int len = s.indexOf("\"");
					 * String newurl = s.substring(0,len); String decodeurl =
					 * Base64PngToUrl.decode(newurl);
					 * 
					 * payMap.put("url", decodeurl);
					 */
					payMap.put("url", requrl);
					payMap.put("status", "0");
					payMap.put("orderid", info.getFfId());
					payMap.put("spid", info.getSpId());

				} else {
					payMap.put("status", "2");
					payMap.put("msg", "back error " + resultCode);
				}
			} else {
				payMap.put("status", "1");
				payMap.put("msg", "request time out");
			}

			result = JSONObject.toJSONString(payMap);
			logger.info("乐奕sdF1生成json:" + result);

		} catch (Exception e) {
			logger.error("乐奕sdF1指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS170(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String callbackurl = info.getCallbackurl();

			String[] tmp = keyword.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String backurl = tmp[3];
			String pay_type = tmp[4];

			String timestamp = DateTool.getTimestamp(new Date());
			String time = timestamp.substring(0, timestamp.length() - 3);
			if (callbackurl == null || callbackurl.length() <= 0) {
				callbackurl = backurl;
			}

			String out_trade_no = info.getSpId() + info.getFfId();

			Map<String, Object> restmap = new HashMap<String, Object>();
			restmap.put("mch", mchid);
			restmap.put("key", out_trade_no);
			restmap.put("pay_type", pay_type);
			restmap.put("money", price);
			restmap.put("time", time);
			restmap.put("order_id", out_trade_no);
			restmap.put("notify_url", notityurl);
			restmap.put("return_url", callbackurl);

			StringBuilder signparam = new StringBuilder();
			signparam.append(out_trade_no).append(price).append(pay_type).append(time).append(mchid)
					.append(MD5.md5(appsecret, "utf-8"));

			String sign = MD5.md5(signparam.toString(), "utf-8");
//			restmap.put("sign",sign);

			String param = PaySignUtil.getParamStr(restmap);
			param = param + "&sign=" + sign;

			Map<String, String> payMap = new HashMap<String, String>();
			String requrl = url + "?" + param;
			payMap.put("url", requrl);
			payMap.put("status", "0");
			payMap.put("orderid", info.getFfId());
			payMap.put("spid", info.getSpId());

			/*
			 * String reqResult = HttpsTool.sendGet(url+"?"+param, 7000);
			 * logger.info("sdF1请求结果:"+reqResult); Map<String, String> payMap = new
			 * HashMap<String, String>(); if (reqResult != null && reqResult.length() > 0) {
			 * JSONObject jsonobj = JSONObject.parseObject(reqResult); String resultCode =
			 * jsonobj.getString("ok"); if ("true".equals(resultCode)) { String requrl =
			 * jsonobj.getString("data"); payMap.put("url", requrl); payMap.put("status",
			 * "0"); payMap.put("orderid", info.getFfId()); payMap.put("spid",
			 * info.getSpId()); } else { payMap.put("status", "2"); payMap.put("msg",
			 * "back error "+resultCode); } } else { payMap.put("status", "1");
			 * payMap.put("msg", "request time out"); }
			 */

			result = JSONObject.toJSONString(payMap);
			logger.info("sdF1生成json:" + result);

		} catch (Exception e) {
			logger.error("sdF1指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS169(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
//			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();

			String[] tmp = keyword.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String backurl = tmp[3];

			callbackurl = backurl;
			/*
			 * if (callbackurl == null || callbackurl.length() <= 0) { }
			 */

			String out_trade_no = info.getSpId() + info.getFfId();

			Map<String, Object> restmap = new HashMap<String, Object>();
			restmap.put("mchid", mchid);
			restmap.put("price", price);
			restmap.put("ip", info.getIp());
			restmap.put("out_trade_id", out_trade_no);
			restmap.put("notity_url", notityurl);
			restmap.put("callbackurl", callbackurl);
			restmap.put("app", "app");
			restmap.put("goodsname", "goodsname");

			StringBuilder signparam = new StringBuilder();
			signparam.append("fee=").append(price).append("&mchid=").append(mchid).append("&out_trade_id=")
					.append(out_trade_no).append("&key=").append(appsecret);

			String sign = MD5.md5(signparam.toString(), "utf-8");
//			restmap.put("sign",sign);

			String param = PaySignUtil.getParamStr(restmap);
			param = param + "&sign=" + sign;

			String reqResult = HttpTool.sendGetSetTimeout(url, param, "7000");
			logger.info("zwsfsdE4请求结果:" + reqResult);
			Map<String, String> payMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String resultCode = jsonobj.getString("status");
				if ("0".equals(resultCode)) {
					String requrl = jsonobj.getString("url");
					payMap.put("url", requrl);
					payMap.put("status", "0");
					payMap.put("orderid", info.getFfId());
					payMap.put("spid", info.getSpId());
				} else {
					payMap.put("status", "2");
					payMap.put("msg", "back error " + resultCode);
				}
			} else {
				payMap.put("status", "1");
				payMap.put("msg", "request time out");
			}

			result = JSONObject.toJSONString(payMap);
			logger.info("zwsfsdE4生成json:" + result);

		} catch (Exception e) {
			logger.error("zwsfsdE4指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS503(OrderReqInfo info) {
		String result = "error";
		try {
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();

			String mobile = info.getMobile();
			String ffId = info.getFfId();
			String out_trade_no = ffId.substring(0, 2) + ffId.substring(4);

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&order_id=").append(out_trade_no).append("&price=").append(price)
					.append("&iccid=").append(info.getIccid()).append("&ip=").append(info.getIp()).append("&imsi=")
					.append(info.getImsi()).append("&imei=").append(info.getImei());

			if (mobile != null && mobile.length() == 1) {
				param.append("&phone=").append(mobile);
			}

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "8000");
			logger.info("悦人yc42请求结果:" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String returnCode = jsonobj.getString("result");
				if ("success".equals(returnCode)) {
					String sms1 = jsonobj.getString("sms_content");
					String smsport1 = jsonobj.getString("sms_number");
					String sendtype1 = jsonobj.getString("sms_type");
					String port = jsonobj.getString("sms_dest");
					if ("data".equals(sendtype1)) {
						sendtype1 = "1";
					} else if ("text".equals(sendtype1)) {
						sendtype1 = "0";
					}

					Map<String, String> map = new HashMap<String, String>();
					map.put("msg", sms1);
					map.put("returnCode", "0");
					map.put("smsNumber", smsport1);
					map.put("send_type", sendtype1);
					if (port != null) {
						map.put("port", port);
					}
					result = JSONObject.toJSONString(map);
				}
			}
		} catch (Exception e) {
			logger.error("悦人yc42获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		logger.info("悦人yc42返回结果：" + result);
		return result;
	}

	private static String getOnlineSMS168(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
//			String app = info.getApp();
			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();
			String[] tmp = keyword.split("#");

			/*
			 * BigDecimal b1 = new BigDecimal(price); BigDecimal b2 = new BigDecimal(100);
			 * price = b1.divide(b2).toString();
			 */

			String merchantNo = tmp[0];
			String appsecret = ConstantDefine.JHZ_V_KEY;
			String notityurl = tmp[2];
			String payMethod = tmp[3];
			String backurl = tmp[4];
			String domain = tmp[5];

			String timestamp = DateTool.getTimestamp(new Date());
			String time = timestamp.substring(0, timestamp.length() - 3);
			if (callbackurl == null || callbackurl.length() <= 0) {
				callbackurl = backurl;
			}

			StringBuilder signparam = new StringBuilder();
			signparam.append(merchantNo).append("|").append(info.getFfId()).append("|").append(price).append("|")
					.append(callbackurl).append("|").append(notityurl).append("|").append(time).append("|").append("|")
					.append(goodsname).append("|").append("|");

			String sign = URLEncoder.encode(RSASignature.sign(signparam.toString(), appsecret), "utf-8");

			notityurl = URLEncoder.encode(notityurl, "utf-8");
			callbackurl = URLEncoder.encode(callbackurl, "utf-8");
			goodsname = URLEncoder.encode(goodsname, "utf-8");

			StringBuilder param = new StringBuilder();
			param.append("merchantNo=").append(merchantNo).append("&requestNo=").append(info.getFfId())
					.append("&amount=").append(price).append("&payMethod=").append(payMethod).append("&backUrl=")
					.append(notityurl).append("&pageUrl=").append(callbackurl).append("&payDate=").append(time)
					.append("&remark1=").append(goodsname).append("&remark2=").append("&remark3=").append("&signature=")
					.append(sign);
			if ("6013".equals(payMethod)) {
				param.append("&authCode=").append(info.getImei());
			} else if ("6006".equals(payMethod)) {
				param.append("&phoneNo=").append(info.getMobile()).append("&acctNo=").append(info.getImsi())
						.append("&customerName=").append(URLEncoder.encode(info.getOsversion(), "utf-8"))
						.append("&cerdNo=").append(info.getImei()).append("&cerdType=1");
			}

			String reqResult = HttpTool.sendPost(url, param.toString(), "5000");
			logger.info("金海哲sdE3请求结果:" + reqResult);
			Map<String, String> payMap = new HashMap<String, String>();
			/*
			 * String requrl = url + "?" + param.toString(); payMap.put("url", requrl);
			 * payMap.put("status", "0"); payMap.put("orderid", info.getFfId());
			 * payMap.put("spid", info.getSpId()); result = JSONObject.toJSONString(payMap);
			 */
			if (reqResult != null && reqResult.length() > 0) {
				if (reqResult.startsWith("{")) {
					JSONObject jsonobj = JSONObject.parseObject(reqResult);
					String msg = jsonobj.getString("msg");
					String status = jsonobj.getString("code");
					payMap.put("msg", msg);
					payMap.put("status", status);
					result = JSONObject.toJSONString(payMap);
				} else {
					String requrl = parseURL3(reqResult, info, "request", domain, "gbk");
					payMap.put("url", requrl);
					payMap.put("status", "0");
					payMap.put("orderid", info.getFfId());
					payMap.put("spid", info.getSpId());
					result = JSONObject.toJSONString(payMap);
				}
			} else {
				payMap.put("status", "1");
				payMap.put("orderid", "request time out");
				result = JSONObject.toJSONString(payMap);
			}
			logger.info("金海哲sdE3生成json:" + result);

		} catch (Exception e) {
			logger.error("金海哲sdE3指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS167(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
//			String app = info.getApp();
			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();
			String[] tmp = keyword.split("#");

			/*
			 * BigDecimal b1 = new BigDecimal(price); BigDecimal b2 = new BigDecimal(100);
			 * price = b1.divide(b2).toString();
			 */

			String merchantNo = tmp[0];
			String appsecret = ConstantDefine.JHZ_V_KEY;
			String notityurl = tmp[2];
			String payMethod = tmp[3];
			String backurl = tmp[4];
			String domain = tmp[5];

			String timestamp = DateTool.getTimestamp(new Date());
			String time = timestamp.substring(0, timestamp.length() - 3);
			if (callbackurl == null || callbackurl.length() <= 0 || "6013".equals(payMethod)) {
				callbackurl = backurl;
			}

			StringBuilder signparam = new StringBuilder();
			signparam.append(merchantNo).append("|").append(info.getFfId()).append("|").append(price).append("|")
					.append(callbackurl).append("|").append(notityurl).append("|").append(time).append("|").append("|")
					.append(goodsname).append("|").append("|");

			String sign = URLEncoder.encode(RSASignature.sign(signparam.toString(), appsecret), "utf-8");

			notityurl = URLEncoder.encode(notityurl, "utf-8");
			callbackurl = URLEncoder.encode(callbackurl, "utf-8");
			goodsname = URLEncoder.encode(goodsname, "utf-8");

			StringBuilder param = new StringBuilder();
			param.append("merchantNo=").append(merchantNo).append("&requestNo=").append(info.getFfId())
					.append("&amount=").append(price).append("&payMethod=").append(payMethod).append("&backUrl=")
					.append(notityurl).append("&pageUrl=").append(callbackurl).append("&payDate=").append(time)
					.append("&remark1=").append(goodsname).append("&remark2=").append("&remark3=").append("&signature=")
					.append(sign);
			if ("6013".equals(payMethod)) {
				param.append("&authCode=").append(info.getImei());
			} else if ("6006".equals(payMethod)) {
				param.append("&phoneNo=").append(info.getMobile()).append("&acctNo=").append(info.getImei())
						.append("&customerName=").append(info.getOsversion()).append("&cerdNo=").append(info.getImsi())
						.append("&cerdType=1");
			}

			Map<String, String> payMap = new HashMap<String, String>();
			String requrl = url + "?" + param.toString();
			payMap.put("url", requrl);
			payMap.put("status", "0");
			payMap.put("orderid", info.getFfId());
			payMap.put("spid", info.getSpId());
			result = JSONObject.toJSONString(payMap);
			logger.info("金海哲sdE2生成json:" + result);

		} catch (Exception e) {
			logger.error("金海哲sdE2指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS502(OrderReqInfo info) {
		String result = "error";
		try {
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();

			String matchRegex = info.getMatchRegex();

			String mobile = info.getMobile();
			if (mobile != null && mobile.length() == 11) {

				StringBuilder param = new StringBuilder();
				param.append(keyword).append("&phone=").append(mobile).append("&price=").append(price).append("&iccid=")
						.append(info.getIccid()).append("&clientIP=").append(info.getIp()).append("&imsi=")
						.append(info.getImsi()).append("&imei=").append(info.getImei()).append("&exData=")
						.append(info.getFfId());

				String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "4000");
				logger.info("叶慧lt12请求结果：" + reqResult);
				Map<String, String> payMap = new HashMap<String, String>();
				if (reqResult != null && reqResult.length() > 0) {
					JSONObject jsonobj = JSONObject.parseObject(reqResult);
					String returnCode = jsonobj.getString("status");
					String msg = jsonobj.getString("message");
					if ("000000".equals(returnCode)) {
						String traid = jsonobj.getString("orderId");
						String requrl = matchRegex + "?ffid=" + info.getFfId() + "&traid=" + traid + "&cpid="
								+ info.getCpId() + "&code=";
						payMap.put("orderid", info.getFfId());
						payMap.put("msg", msg);
						payMap.put("url", requrl);
						payMap.put("status", "0");
						result = JSONObject.toJSONString(payMap);
					}
				}
			}
		} catch (Exception e) {
			logger.error("叶慧lt12获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		logger.info("叶慧lt12返回结果：" + result);
		return result;
	}

	// 奇客-QQ钱包
	private static String getOnlineSMS166(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
//			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();

			String[] tmp = keyword.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String backurl = tmp[3];
			String SeqID = tmp[4];
			String NodeType = tmp[5];
			String NodeID = tmp[6];
			String Version = tmp[7];
			String TERMNO = tmp[8];
			String CommandID = tmp[9];
			String TRADETYPE = tmp[10];

			if (callbackurl == null || callbackurl.length() <= 0) {
				callbackurl = backurl;
			}

			String out_trade_no = info.getSpId() + info.getFfId();

			Map<String, Object> bodymap = new HashMap<String, Object>();
			bodymap.put("TRADETYPE", TRADETYPE);
			bodymap.put("AMT", price);
			bodymap.put("MERORDERID", out_trade_no);
			bodymap.put("RANDSTR", PayUtil.getNonceStr().substring(0, 20));
			bodymap.put("GOODSNAME", out_trade_no.substring(out_trade_no.length() - 15));
			bodymap.put("NOTIFY_URL", notityurl);
			bodymap.put("IP", info.getIp());

			String Body = JSONObject.toJSONString(bodymap);
			JSONObject jsonbody = JSONObject.parseObject(Body);
			String bodystr = PayUtil.GenSignBody(jsonbody);
			String signStr = bodystr.replace("/", "\\/") + "&key=" + appsecret;
			String sign = MD5.md5(signStr, "utf-8").toUpperCase();

			Map<String, Object> restmap = new HashMap<String, Object>();
			restmap.put("CommandID", CommandID);
			restmap.put("SeqID", SeqID);
			restmap.put("NodeType", NodeType);
			restmap.put("NodeID", NodeID);
			restmap.put("Version", Version);
			restmap.put("MERNO", mchid);
			restmap.put("TERMNO", TERMNO);
			restmap.put("Extends", info.getSpId());
			restmap.put("Body", jsonbody);
			restmap.put("Sign", sign);

			String param = JSONObject.toJSONString(restmap);

			String reqResult = HttpTool.sendPost(url, param, "7000");
			logger.info("奇客QQ钱包sdE1请求结果:" + reqResult);
			Map<String, String> payMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj1 = JSONObject.parseObject(reqResult);
				String resbody = jsonobj1.getString("Body");

				JSONObject jsonobj = JSONObject.parseObject(resbody);

				String resBodystr = PayUtil.GenSignBody(jsonobj);
				String resSignStr = resBodystr.replace("/", "\\/") + "&key=" + appsecret;
				String resSign = MD5.md5(resSignStr, "utf-8").toUpperCase();
				if (resSign.equals(jsonobj1.get("Sign"))) {
					String resultCode = jsonobj.getString("STATUS");
					if ("1".equals(resultCode)) {
						String requrl = jsonobj.getString("URL");
						payMap.put("url", requrl);
						payMap.put("status", "0");
						payMap.put("orderid", info.getFfId());
						payMap.put("spid", info.getSpId());
					} else {
						payMap.put("status", "2");
						payMap.put("msg", "back error " + resultCode);
					}
				} else {
					payMap.put("status", "3");
					payMap.put("msg", "sign error ");
				}
			} else {
				payMap.put("status", "1");
				payMap.put("msg", "request time out");
			}

			result = JSONObject.toJSONString(payMap);
			logger.info("奇客QQ钱包sdE1生成json:" + result);

		} catch (Exception e) {
			logger.error("奇客QQ钱包sdE1指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	// 美美-QQ钱包
	private static String getOnlineSMS165(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
//			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();

			String[] tmp = keyword.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String backurl = tmp[3];
			String p4_Cur = tmp[4];

			if (callbackurl == null || callbackurl.length() <= 0) {
				callbackurl = backurl;
			}

			BigDecimal b1 = new BigDecimal(price);
			BigDecimal b2 = new BigDecimal(100);
			price = b1.divide(b2).toString();

			String out_trade_no = info.getSpId() + info.getFfId();

			Map<String, Object> restmap = new HashMap<String, Object>();
			restmap.put("p1_MerchantNo", mchid);
			restmap.put("p2_OrderNo", out_trade_no);
			restmap.put("p3_Amount", price);
			restmap.put("p4_Cur", p4_Cur);
			restmap.put("p5_ProductName", info.getCpParam());
			restmap.put("p6_NotifyUrl", notityurl);

			StringBuilder signparam = new StringBuilder();
			signparam.append(mchid).append(out_trade_no).append(price).append(p4_Cur).append(info.getCpParam())
					.append(notityurl).append(appsecret);

			String sign = MD5.md5(signparam.toString(), "utf-8");
//			restmap.put("sign",sign);

			String param = PaySignUtil.getParamStr(restmap);
			param = param + "&sign=" + sign;

			String reqResult = HttpTool.sendPost(url, param, "7000");
			logger.info("美美QQ钱包sd09请求结果:" + reqResult);
			Map<String, String> payMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String resultCode = jsonobj.getString("ra_Status");
				if ("100".equals(resultCode)) {
					StringBuilder resSignparam = new StringBuilder();
					resSignparam.append(jsonobj.getString("r1_MerchantNo")).append(jsonobj.getString("r2_OrderNo"))
							.append(jsonobj.getString("r3_Amount")).append(jsonobj.getString("ra_Code"))
							.append(jsonobj.getString("ra_Status")).append(jsonobj.getString("rc_CodeMsg"))
							.append(appsecret);
					String resSign = MD5.md5(resSignparam.toString(), "utf-8");
					if (resSign.equals(jsonobj.getString("sign"))) {
						String requrl = jsonobj.getString("ra_Code");
						payMap.put("url", requrl);
						payMap.put("status", "0");
						payMap.put("orderid", info.getFfId());
						payMap.put("spid", info.getSpId());
					} else {
						payMap.put("status", "3");
						payMap.put("msg", "sign error ");
					}
				} else {
					payMap.put("status", "2");
					payMap.put("msg", "back error " + resultCode);
				}
			} else {
				payMap.put("status", "1");
				payMap.put("msg", "request time out");
			}

			result = JSONObject.toJSONString(payMap);
			logger.info("美美QQ钱包sd09生成json:" + result);

		} catch (Exception e) {
			logger.error("美美QQ钱包sd09指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	// 程亮-支付宝
	private static String getOnlineSMS164(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
//			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();

			String[] tmp = keyword.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String backurl = tmp[3];
			String bus_type = tmp[4];
			String pay_type = tmp[5];
			String version = tmp[6];

			if (callbackurl == null || callbackurl.length() <= 0) {
				callbackurl = backurl;
			}

			BigDecimal b1 = new BigDecimal(price);
			BigDecimal b2 = new BigDecimal(100);
			price = b1.divide(b2).toString();

			String out_trade_no = info.getSpId() + info.getFfId();
			String timestamp = DateTool.getNow();

			Map<String, Object> restmap = new HashMap<String, Object>();
			restmap.put("app_id", mchid);
			restmap.put("timestamp", timestamp);
			restmap.put("version", version);
			restmap.put("money", price);
			restmap.put("bus_type", bus_type);
			restmap.put("time_start", timestamp);
			restmap.put("body", info.getCpParam());
			restmap.put("out_trade_no", out_trade_no);
			restmap.put("notify_url", notityurl);
			restmap.put("pay_type", pay_type);
			restmap.put("callback_url", callbackurl);
			restmap.put("spbill_create_ip", info.getIp());

			String sign = PaySignUtil.getSign(restmap, appsecret);
			restmap.put("sign", sign);

			String param = JSONObject.toJSONString(restmap);

			String reqResult = HttpTool.sendPost(url, param, "7000");
			logger.info("支付宝sd08请求结果:" + reqResult);
			Map<String, String> payMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String resultCode = jsonobj.getString("code");
				if ("200".equals(resultCode)) {
					String data = jsonobj.getString("data");
					JSONObject jsonobj1 = JSONObject.parseObject(data);
					String requrl = jsonobj1.getString("pay_url");
					payMap.put("url", requrl);
					payMap.put("status", "0");
					payMap.put("orderid", info.getFfId());
					payMap.put("spid", info.getSpId());
				} else {
					payMap.put("status", "2");
					payMap.put("msg", "back error " + resultCode);
				}
			} else {
				payMap.put("status", "1");
				payMap.put("msg", "request time out");
			}

			result = JSONObject.toJSONString(payMap);
			logger.info("支付宝sd08生成json:" + result);

		} catch (Exception e) {
			logger.error("支付宝sd08指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	// 贝付-南京众誊
	private static String getOnlineSMS163(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
//			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();

			String[] tmp = keyword.split("#");

			String mchid = tmp[0];
//			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String backurl = tmp[3];
			String tranTp = tmp[4];
			String source = tmp[5];
			String orderTp = tmp[6];

			if (callbackurl == null || callbackurl.length() <= 0) {
				callbackurl = backurl;
			}

			String out_trade_id = info.getSpId() + info.getFfId();

			Map<String, Object> restmap = new HashMap<String, Object>();
			restmap.put("tranTp", tranTp);
			restmap.put("amount", price);
			restmap.put("subject", info.getFfId());
			restmap.put("notifyUrl", notityurl);
			restmap.put("orgOrderNo", out_trade_id);
			restmap.put("source", source);
			restmap.put("body", info.getFfId());
			restmap.put("account", mchid);
			restmap.put("orderTp", orderTp);

			String signparam = PaySignUtil.getParamStr(restmap);
			String signature = RSASignature.sign(signparam, ConstantDefine.NJZY_PRI_KEY, "utf-8");
			restmap.put("signature", signature);

			String param = JSONObject.toJSONString(restmap);

			String reqResult = HttpTool.sendKLWPost(url, param, "utf-8", 7000);
			logger.info("南京众誉sdD7请求结果:" + reqResult);
			Map<String, String> payMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String resultCode = jsonobj.getString("respCode");
				if ("200".equals(resultCode)) {
					Map<String, String> resmap = CommonTool.parseJson(jsonobj);
					String resSign = resmap.get("signature");
					resmap.remove("signature");
					String resSignparam = PaySignUtil.getParamStr2(resmap);
					boolean checksign = RSASignature.doCheck(resSignparam, resSign, ConstantDefine.NJZY_PUB_KEY,
							"utf-8");
					if (checksign) {
						String requrl = jsonobj.getString("codeUrl");

						payMap.put("url", requrl);
						payMap.put("status", "0");
						payMap.put("orderid", info.getFfId());
						payMap.put("spid", info.getSpId());
					} else {
						payMap.put("status", "3");
						payMap.put("msg", "sign error ");
					}
				} else {
					payMap.put("status", "2");
					payMap.put("msg", "back error " + resultCode);
				}
			} else {
				payMap.put("status", "1");
				payMap.put("msg", "request time out");
			}

			result = JSONObject.toJSONString(payMap);
			logger.info("南京众誉sdD7生成json:" + result);

		} catch (Exception e) {
			logger.error("南京众誉sdD7指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	// 联动优势-魔力小鸟
	private static String getOnlineSMS501(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String matchRegex = info.getMatchRegex();
			String payCode = getPayCode(info.getFees(), matchRegex, price, "#");
			String callbackurl = info.getCallbackurl();

			String[] tmp = keyword.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
//			String notityurl = tmp[2];
			String backurl = tmp[3];
			String amttype = tmp[4];
			String banktype = tmp[5];
			String platType = tmp[6];
			String upversion = tmp[7];
//			String commitmobile = tmp[8];
			String upmobile = tmp[9];
			String commitcode = tmp[10];

			if (callbackurl == null || callbackurl.length() <= 0) {
				callbackurl = backurl;
			}

			String mobile = info.getMobile();
			if (mobile != null && mobile.length() == 11) {
				String out_trade_id = info.getSpId() + info.getFfId();

				String timestamp = DateTool.getNow();
				String time = timestamp.substring(0, 8);

				Map<String, Object> restmap = new HashMap<String, Object>();
				restmap.put("merid", mchid);
				restmap.put("goodsid", payCode);
				restmap.put("mobileid", mobile);
				restmap.put("clientip", info.getIp());
				restmap.put("orderid", out_trade_id);
				restmap.put("orderdate", time);
				restmap.put("amount", price);
				restmap.put("amttype", amttype);
				restmap.put("banktype", banktype);
				restmap.put("returl", callbackurl);
				restmap.put("platType", platType);
				restmap.put("upversion", upversion);

				StringBuilder signparam = new StringBuilder();
				signparam.append("merid=").append(mchid).append("&goodsid=").append(payCode)
						.append("&goodsinfo=&mobileid=").append(mobile).append("&orderid=").append(out_trade_id)
						.append("&orderdate=").append(time).append("&amount=").append(price).append("&amttype=")
						.append(amttype).append("&banktype=").append(banktype).append("&platType=").append(platType)
						.append("&gateid=&returl=").append(callbackurl)
						.append("&merpriv=&expand=&channelid=&appid=&upversion=").append(upversion).append(appsecret);
				String sign = MD5.md5(signparam.toString(), "utf-8");

				String param = PaySignUtil.getParamStr(restmap);
				param = param + "&sign=" + sign;

				String reqResult = HttpTool.sendPost(url, param, "4000");
				logger.info("魔力小鸟yc41请求结果:" + reqResult);
				Map<String, String> payMap = new HashMap<String, String>();
				payMap.put("ffid", info.getFfId());
				if (reqResult != null && reqResult.length() > 0) {
					JSONObject jsonobj = JSONObject.parseObject(reqResult);
					String resultCode = jsonobj.getString("retCode");
					if ("0000".equals(resultCode)) {
						String paytype = jsonobj.getString("paytype");
						String mo = jsonobj.getString("mo");
						String called = jsonobj.getString("called");
						String mo2 = jsonobj.getString("mo2");
						String called2 = jsonobj.getString("called2");

						payMap.put("status", "0");
						payMap.put("msg", "success");
						payMap.put("paytype", paytype);
						if ("YZM".equals(paytype)) {
							String requrl = upmobile;
							Map<String, Object> restmap1 = new HashMap<String, Object>();
							restmap1.put("merid", mchid);
							restmap1.put("mobileid", mobile);
							restmap1.put("orderid", out_trade_id);
							restmap1.put("orderdate", time);
							restmap1.put("upversion", upversion);

							StringBuilder signparam1 = new StringBuilder();
							signparam1.append("merid=").append(mchid).append("&orderid=").append(out_trade_id)
									.append("&orderdate=").append(time).append("&upversion=").append(upversion)
									.append(appsecret);
							String sign1 = MD5.md5(signparam1.toString(), "utf-8");

							String param1 = PaySignUtil.getParamStr(restmap1);
							param1 = param1 + "&sign=" + sign1;

							String reqResult1 = HttpTool.sendPost(requrl, param1, "4000");
							if (reqResult1 != null && reqResult1.length() > 0) {
								JSONObject jsonobj1 = JSONObject.parseObject(reqResult1);
								String resultCode1 = jsonobj.getString("retCode");
								String msg1 = jsonobj.getString("retMsg");
								if ("0000".equals(resultCode1)) {
									String requrl1 = commitcode + "?ffid=" + info.getFfId() + "&cpid=" + info.getCpId()
											+ "&code=";
									payMap.put("status", "0");
									payMap.put("msg", msg1);
									payMap.put("url", requrl1);
								} else {
									payMap.put("status", "4");
									payMap.put("msg", msg1);
								}
							} else {
								payMap.put("status", "3");
								payMap.put("msg", "commit time out");
							}
						} else if ("SMS".equals(paytype)) {
							payMap.put("sms1", mo);
							payMap.put("smsport1", called);
							if (mo2 != null && mo2.length() > 0) {
								payMap.put("sms2", mo2);
								payMap.put("smsport2", called2);
							}
						}
					} else {
						payMap.put("status", "2");
						payMap.put("msg", "back error " + resultCode);
					}
				} else {
					payMap.put("status", "1");
					payMap.put("msg", "request time out");
				}
				result = JSONObject.toJSONString(payMap);
				logger.info("魔力小鸟yc41生成json:" + result);
			}

		} catch (Exception e) {
			logger.error("魔力小鸟yc41指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	// 博腾
	private static String getOnlineSMS162(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();

			String[] tmp = keyword.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String backurl = tmp[3];
			String paytypeid = tmp[4];
			String spare1 = tmp[5];
			String tcid = tmp[6];

			if (callbackurl == null || callbackurl.length() <= 0) {
				callbackurl = backurl;
			}

			String out_trade_id = info.getSpId() + info.getFfId();

			Map<String, Object> restmap = new HashMap<String, Object>();
			restmap.put("siteid", mchid);
			restmap.put("paytypeid", paytypeid);
			restmap.put("siteorderid", out_trade_id);
			restmap.put("paymoney", price);
			restmap.put("goodsname", info.getFfId());
			restmap.put("client_ip", info.getIp());
			restmap.put("thereport_url", notityurl);
			restmap.put("thenotify_url", callbackurl);
			restmap.put("tcid", tcid);
			restmap.put("spare1", spare1);

			StringBuilder signParam = new StringBuilder();
			signParam.append(mchid).append(out_trade_id).append(price).append(info.getFfId()).append(notityurl)
					.append(callbackurl).append(appsecret);
			String sign = MD5.md5(signParam.toString(), "utf-8");
			restmap.put("md5key", sign);

			String param = PaySignUtil.getParamStr(restmap);

			String reqResult = HttpTool.sendPost(url, param, "7000");
			logger.info("博腾sdD6请求结果:" + reqResult);
			Map<String, String> payMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String resultCode = jsonobj.getString("returncode");
				if ("100".equals(resultCode)) {
					String requrl = jsonobj.getString("h5url");
					String code_url = jsonobj.getString("code_url");
					if (requrl == null || !requrl.startsWith("http")) {
						String imgurl = jsonobj.getString("code_img_url");
						if (imgurl != null && imgurl.startsWith("http")) {
							requrl = Base64PngToUrl.decode(imgurl);
							payMap.put("urlimg", imgurl);
						}
					}
					if (requrl == null)
						requrl = code_url;

					payMap.put("url", requrl);
					payMap.put("status", "0");
					payMap.put("orderid", info.getFfId());
					payMap.put("spid", info.getSpId());
				} else {
					payMap.put("status", "2");
					payMap.put("msg", "back error " + resultCode);
				}
			} else {
				payMap.put("status", "1");
				payMap.put("msg", "request time out");
			}

			result = JSONObject.toJSONString(payMap);
			logger.info("博腾sdD6生成json:" + result);

		} catch (Exception e) {
			logger.error("博腾sdD6指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	// 智慧支付
	private static String getOnlineSMS161(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();

			String[] tmp = keyword.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String backurl = tmp[3];
			String signMethod = tmp[4];
			String version = tmp[5];
			String txnType = tmp[6];
			String txnSubType = tmp[7];
			String bizType = tmp[8];
			String accessType = tmp[9];
			String accessMode = tmp[10];
			String currency = tmp[11];
			String payType = tmp[12];
			String domain = tmp[13];

			if (callbackurl == null || callbackurl.length() <= 0) {
				callbackurl = backurl;
			}

			String timestamp = DateTool.getNow();
			String out_trade_id = info.getSpId() + info.getFfId();

			Map<String, Object> restmap = new HashMap<String, Object>();
			restmap.put("version", version);
			restmap.put("txnType", txnType);
			restmap.put("txnSubType", txnSubType);
			restmap.put("bizType", bizType);
			restmap.put("accessType", accessType);
			restmap.put("accessMode", accessMode);
			restmap.put("merId", mchid);
			restmap.put("merOrderId", out_trade_id);
			restmap.put("txnTime", timestamp);
			restmap.put("txnAmt", price);
			restmap.put("currency", currency);
			restmap.put("frontUrl", callbackurl);
			restmap.put("backUrl", notityurl);
			restmap.put("payType", payType);

			String signParam = PaySignUtil.getParamStr(restmap);
			String sign = SignUtil.sign(signMethod, signParam, appsecret, "UTF-8");
//			String sign = SignUtil.sign(signMethod, signParam, appsecret, "UTF-8");
			sign = URLEncoder.encode(sign, "utf-8");
			restmap.put("signMethod", signMethod);
			restmap.put("signature", sign);
			String param = PaySignUtil.getParamStr(restmap);

			String rescallurl = parseURL(url + "?" + param, info, "request", domain);
			Map<String, String> payMap = new HashMap<String, String>();
			payMap.put("url", rescallurl);
			payMap.put("status", "0");
			payMap.put("orderid", info.getFfId());
			payMap.put("spid", info.getSpId());
			result = JSONObject.toJSONString(payMap);

			logger.info("智慧sdD5生成json:" + result);

		} catch (Exception e) {
			logger.error("智慧sdD5指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	// 趣讯
	private static String getOnlineSMS302(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();

			BigDecimal b1 = new BigDecimal(price);
			BigDecimal b2 = new BigDecimal(100);
			price = b1.divide(b2).toString();

			String[] tmp = keyword.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String backurl = tmp[3];
			String paytype = tmp[4];

			if (callbackurl == null || callbackurl.length() <= 0) {
				callbackurl = backurl;
			}

			Map<String, Object> restmap = new HashMap<String, Object>();
			restmap.put("appKey", mchid);
			restmap.put("notifyUrl", notityurl);
			restmap.put("bussOrderNum", info.getFfId());
			restmap.put("payMoney", price);
			restmap.put("orderName", info.getCpParam());
			restmap.put("ip", info.getIp());
			restmap.put("returnUrl", callbackurl);
			restmap.put("remark", info.getSpId());
			restmap.put("channel", paytype);
			String sign = PaySignUtil.getSign(restmap, appsecret);
			String md5Param = PaySignUtil.getParamStr(restmap) + "&sign=" + sign;
			md5Param = URLEncoder.encode(md5Param, "utf-8");
			String param = "paramStr=" + md5Param;

			String reqResult = HttpsTool.sendPost(url, param, 7000);

			logger.info("趣讯sdZ2请求结果:" + reqResult);
			Map<String, String> payMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String resultCode = jsonobj.getString("resultCode");
				if ("200".equals(resultCode)) {
					String data = jsonobj.getString("Data");
					JSONObject jsonobj1 = JSONObject.parseObject(data);
					String requrl = jsonobj1.getString("qr_code");
					;

					payMap.put("url", requrl);
					payMap.put("status", "0");
					payMap.put("orderid", info.getFfId());
					payMap.put("spid", info.getSpId());

					/*
					 * String data = jsonobj.getString("data"); JSONObject jsondata =
					 * JSONObject.parseObject(data);
					 * 
					 * Map<String, String> resmap = CommonTool.parseJson(jsondata); String backsign
					 * = resmap.get("sign"); resmap.remove("sign"); String ressgin =
					 * PayUtil.getSign(resmap, appsecret);
					 * 
					 * if (backsign.equals(ressgin)) { } else { payMap.put("status", "3");
					 * payMap.put("msg", "checkout appsecret error"); }
					 */

				} else {
					payMap.put("status", "2");
					payMap.put("msg", "back error " + resultCode);
				}
			} else {
				payMap.put("status", "1");
				payMap.put("msg", "request time out");
			}

			result = JSONObject.toJSONString(payMap);
			logger.info("趣讯sdZ2生成json:" + result);

		} catch (Exception e) {
			logger.error("趣讯sdZ2指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	// 高汇通
	private static String getOnlineSMS160(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();
			String[] tmp = keyword.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String backurl = tmp[3];
			String src_code = tmp[4];
			String txcode = tmp[5];
			String version = tmp[6];
			String field003 = tmp[7];
			String field011 = tmp[8];
			String field031 = tmp[9];
			if (callbackurl == null || callbackurl.length() <= 0 || callbackurl.length() >= 100) {
				callbackurl = backurl;
			}
			String timestamp = DateTool.getNow();
			String out_trade_no = info.getSpId() + info.getFfId();

			pkg8583 pkg = new pkg8583();
			pkg.setTxcode(txcode);
			pkg.setTxdate(timestamp.substring(0, 8));
			pkg.setTxtime(timestamp.substring(8, 14));
			pkg.setVersion(version);
			pkg.setField003(field003);
			pkg.setField004(price);
			pkg.setField011(field011);
			pkg.setField031(field031);
			pkg.setField041(mchid);
			pkg.setField042(src_code);
			pkg.setField048(out_trade_no);
			pkg.setField125(out_trade_no + mchid);
			pkg.setField060(notityurl);

			String strForSign = pkg.toSignStr() + appsecret;
			String sign = Util.stringToMD5(strForSign, "utf-8").toUpperCase();
			pkg.setField128(sign.substring(0, 16));

			String param = pkg.toJson();

			String reqResult = HttpTool.sendPost(url, param, "4000");
			logger.info("高汇通支付宝sdD2请求结果:" + reqResult);
			Map<String, String> payMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String resultCode = jsonobj.getString("field039");
				if ("00".equals(resultCode)) {
					payMap.put("url", jsonobj.getString("field055"));
					payMap.put("status", "0");
					payMap.put("orderid", info.getFfId());
					payMap.put("spid", info.getSpId());

					/*
					 * String data = jsonobj.getString("data"); JSONObject jsondata =
					 * JSONObject.parseObject(data);
					 * 
					 * Map<String, String> resmap = CommonTool.parseJson(jsondata); String backsign
					 * = resmap.get("sign"); resmap.remove("sign"); String ressgin =
					 * PayUtil.getSign(resmap, appsecret);
					 * 
					 * if (backsign.equals(ressgin)) { } else { payMap.put("status", "3");
					 * payMap.put("msg", "checkout appsecret error"); }
					 */

				} else {
					payMap.put("status", "2");
					payMap.put("msg", "back error " + resultCode);
				}
			} else {
				payMap.put("status", "1");
				payMap.put("msg", "request time out");
			}

			result = JSONObject.toJSONString(payMap);
			logger.info("高汇通支付宝sdD2生成json:" + result);

		} catch (Exception e) {
			logger.error("高汇通支付宝sdD2指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	// QQ钱包-长盈
	private static String getOnlineSMS159(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();
			String[] tmp = keyword.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
//			String notityurl = tmp[2];
			String backurl = tmp[3];
			String src_code = tmp[4];
			String trade_type = tmp[5];
			if (callbackurl == null || callbackurl.length() <= 0 || callbackurl.length() >= 100) {
				callbackurl = backurl;
			}
			String timestamp = DateTool.getNow();
			String out_trade_no = info.getSpId() + info.getFfId();

			Map<String, String> parm = new HashMap<String, String>();
			parm.put("src_code", src_code);
			parm.put("out_trade_no", out_trade_no);
			parm.put("total_fee", price);
			parm.put("time_start", timestamp);
			parm.put("goods_name", goodsname);
			parm.put("trade_type", trade_type);
			parm.put("finish_url", callbackurl);
			parm.put("mchid", mchid);
			parm.put("sign", PayUtil.getSign(parm, appsecret));

			String param = PayUtil.getFormData(parm, false);
			String reqResult = HttpTool.sendPost(url, param, "7000");
			logger.info("长盈QQ钱包sdD1请求结果:" + reqResult);
			Map<String, String> payMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String resultCode = jsonobj.getString("respcd");
				if ("0000".equals(resultCode)) {
					String data = jsonobj.getString("data");
					JSONObject jsondata = JSONObject.parseObject(data);

					Map<String, String> resmap = CommonTool.parseJson(jsondata);
					String backsign = resmap.get("sign");
					resmap.remove("sign");
					String ressgin = PayUtil.getSign(resmap, appsecret);

					if (backsign.equals(ressgin)) {
						payMap.put("url", resmap.get("pay_params"));
						payMap.put("status", "0");
						payMap.put("orderid", info.getFfId());
						payMap.put("spid", info.getSpId());
					} else {
						payMap.put("status", "3");
						payMap.put("msg", "checkout appsecret error");
					}

				} else {
					payMap.put("status", "2");
					payMap.put("msg", "back error " + resultCode);
				}
			} else {
				payMap.put("status", "1");
				payMap.put("msg", "request time out");
			}

			result = JSONObject.toJSONString(payMap);
			logger.info("长盈QQ钱包sdD1生成json:" + result);

		} catch (Exception e) {
			logger.error("长盈QQ钱包sdD1指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	// QQ钱包
	private static String getOnlineSMS158(OrderReqInfo info) {
		String result = "error";
		try {
			String tmp[] = info.getKeyword().split("#");
			String match[] = info.getMatchRegex().split("#");
			String mch_id = tmp[0];// 商户号
			String mch_order_num = info.getFfId();// 订单号
			double amount = Double.valueOf(info.getFee()) / 100;
			String total_amount = new java.text.DecimalFormat("#.00").format(amount);
			String pay_type = tmp[2];// 支付类型
			String client_ip = info.getIp();
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
			String submit_time = format.format(date);// 时间
			String body = info.getPropname();// 名字
			String notify_url = match[0];// 通知地址
			String sign_type = tmp[3];// 签名方式
			String return_url = info.getCallbackurl() == null || info.getCallbackurl().length() == 0 ? match[1]
					: info.getCallbackurl();// 返回地址
			String params = info.getSpId();// spid;
			Map<String, String> reqMap = new HashMap<String, String>();
			reqMap.put("mch_id", mch_id);
			reqMap.put("mch_order_num", mch_order_num);
			reqMap.put("total_amount", total_amount);
			reqMap.put("pay_type", pay_type);
			reqMap.put("sign_type", sign_type);
			StringBuilder sb = new StringBuilder();
			String appsecret = tmp[1];
			sb.append("mch_id=").append(mch_id).append("&mch_order_num=").append(mch_order_num).append("&total_amount=")
					.append(total_amount).append("&pay_type=").append(pay_type).append("&sign_type=").append(sign_type)
					.append("&key=").append(appsecret);
			String sign = MD5Tool.toMD5Value(sb.toString()).toLowerCase();
			reqMap.put("sign", sign);
			reqMap.put("client_ip", client_ip);
			reqMap.put("submit_time", submit_time);
			reqMap.put("body", body);
			reqMap.put("notify_url", notify_url);
			reqMap.put("sign_type", sign_type);
			reqMap.put("return_url", return_url);
			reqMap.put("params", params);
			String strURL = PayUtil.getWebForm(reqMap);
			String reqResult = HttpTool.sendYZWHPost(info.getUrl(), strURL, 6000);
			if (reqResult != null && reqResult.length() > 0) {
				logger.info("QQ扫码sda7请求结果:" + reqResult);
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				Map<String, String> resMap = new HashMap<String, String>();
				for (Map.Entry<String, Object> entry : jsonobj.entrySet()) {
					String value = entry.getValue() + "";
					resMap.put(entry.getKey(), value);
				}
				String retcode = resMap.get("code");
				System.out.println(retcode);
				String data = resMap.get("data").substring(22);
				if ("0".equals(retcode)) {
					String ROOT_DIR = System.getProperty("user.dir");
					String path = ROOT_DIR + File.separator + "script/qq1.png";
					boolean flag = Base64PngToUrl.generateImage(data, path);
					if (flag) {
						String url = Base64PngToUrl.decode(path);
						logger.info("QQ扫码sda7请求结果:" + reqResult);
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("url", url);
						map.put("orderid", info.getFfId());
						map.put("status", 0);
						map.put("spid", info.getSpId());
						result = JSONObject.toJSONString(map);
					}
				}
			}
		} catch (Exception e) {
			logger.error("优乐技术sda6指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	// 微信
	private static String getOnlineSMS157(OrderReqInfo info) {
		String result = "error";
		try {
			String tmp[] = info.getKeyword().split("#");
			String match[] = info.getMatchRegex().split("#");
			String merchantCode = tmp[0];// 商户号
			String merchantOrderId = info.getSpId() + info.getFfId();// 订单号
			String fee = String.valueOf(info.getFee());// 金额
			String orderDesc = info.getPropname();// 订单描述
			String orderLife = tmp[3];// 订单有效期，单位为分钟, 大于等于5
			String frontUrl = info.getCallbackurl() == null || info.getCallbackurl().length() == 0 ? match[0]
					: info.getCallbackurl();// 支付完成后页面跳转地址
			String payType = tmp[2];// 支付方式
			String userIp = info.getIp();// ip地址
			String appsecret = tmp[1];
//			String return_url =info.getCallbackurl()==null||info.getCallbackurl().length()==0? match[1]:info.getCallbackurl();// 页面同步回调地址
			Map<String, String> reqMap = new HashMap<String, String>();
			reqMap.put("merchantCode", merchantCode);
			reqMap.put("merchantOrderId", merchantOrderId);
			reqMap.put("fee", fee);
			reqMap.put("orderDesc", orderDesc);
			reqMap.put("orderLife", orderLife);
			reqMap.put("frontUrl", frontUrl);
			reqMap.put("payType", payType);
			reqMap.put("userIp", userIp);
			String sign = PayUtil.getSign(reqMap, appsecret).toLowerCase();
			reqMap.put("sign", sign);
			String strURL = PayUtil.getWebForm(reqMap);
			String reqResult = HttpTool.sendPost(info.getUrl(), strURL);
			if (reqResult != null && reqResult.length() > 0) {
				logger.info("优乐技术 sda6请求结果:" + reqResult);
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				Map<String, String> resMap = new HashMap<String, String>();
				for (Map.Entry<String, Object> entry : jsonobj.entrySet()) {
					String value = entry.getValue() + "";
					resMap.put(entry.getKey(), value);
				}
				String retcode = resMap.get("respCode");
				if ("0".equals(retcode)) {
					String resultSign = resMap.get("sign");
					resMap.remove("sign");
					String mySign = PayUtil.getSign(resMap, appsecret);
					if (mySign.equals(resultSign)) {
						logger.info("优乐技术sda6请求结果:" + reqResult);
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("url", resMap.get("payUrl"));
						map.put("orderid", info.getFfId());
						map.put("status", 0);
						map.put("spid", info.getSpId());
						result = JSONObject.toJSONString(map);
					}
				}
			}
		} catch (Exception e) {
			logger.error("优乐技术sda6指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	// 钱匣子QQ钱包
	private static String getOnlineSMS156(OrderReqInfo info) {
		String result = "error";
		try {
			String tmp[] = info.getKeyword().split("#");
			String match[] = info.getMatchRegex().split("#");
			String orderid = info.getFfId();// 订单号
			String userid = tmp[0];// "1423";//商户号
			String total_fee = String.valueOf(info.getFee());// 金额，单位分
			String body = info.getPropname();// 商品名称
			String notifyUrl = match[0];// 回调地址
			String returnUrl = info.getCallbackurl() == null || info.getCallbackurl().length() == 0 ? match[1]
					: info.getCallbackurl();// 返回地址
			String paytype = tmp[2];// 支付类型
			String clientIp = info.getIp();// ip地址
			String appsecret = tmp[1];
			Map<String, String> resMap = new HashMap<String, String>();
			resMap.put("orderid", info.getSpId() + orderid);
			resMap.put("userid", userid);
			resMap.put("total_fee", total_fee);
			resMap.put("body", URLEncoder.encode(body, "utf-8"));
			resMap.put("notifyUrl", URLEncoder.encode(notifyUrl, "utf-8"));
			resMap.put("returnUrl", URLEncoder.encode(returnUrl, "utf-8"));
			resMap.put("paytype", paytype);
			resMap.put("clientIp", clientIp);
			String sign = PayUtil.getSign(resMap, appsecret).toLowerCase();// 签名
			resMap.put("notifyUrl", notifyUrl);
			resMap.put("returnUrl", returnUrl);
			resMap.put("body", body);
			resMap.put("sign", sign);
			String url = info.getUrl();
			String strURL = PayUtil.getWebForm(resMap);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("url", url + "?" + strURL);
			map.put("orderid", info.getFfId());
			map.put("status", 0);
			map.put("spid", info.getSpId());
			result = JSONObject.toJSONString(map);
		} catch (Exception e) {
			logger.error("钱匣子sda5指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	// 河南融惠QQ钱包
//	private static String getOnlineSMS155(OrderReqInfo info) {
//		String result = "error";
//		try {
//			String body=info.getPropname();
//			String charset="utf-8";
//			String defaultbank="QQPAY";//网银代码
//			String isApp="H5";//接入方式
//			String merchantId="";//商户ID;
//			String notifyUrl="";//通知地址
//			String orderNo=info.getFfId();//订单号
//			String paymentType="1";//支付类型，固定值为1
//			String paymethod="directPay";//支付方式，directPay：直连模式；bankPay：收银台模式
//			String returnUrl=info.getCallbackurl()!=null?info.getCallbackurl():info.getMatchRegex().split("#")[1];//返回地址
//			String service="online_pay";//固定值
//			String title=info.getPropname();//商品的名称
//			String totalFee=String.valueOf(Float.valueOf(info.getFee())/100);//订单金额，单位为RMB元
//			String signType="SHA";//签名方式
//			String sign="";//签名
//			String userIp=info.getIp();//ip
//			String appName=info.getPropname();//APP名称或WAP网站名
//			String appMsg=info.getApp();//IOS标识
//			String appType="";//客户端形式
//			String backUrl="";//（h5必填）成功页面
//			
//			String tmp[] = info.getKeyword().split("#");
//			String match[] = info.getMatchRegex().split("#");
//			String version = tmp[0];// 区别版本的参数
//			String agre_type = tmp[1];// 协议类型
//			String inst_no = tmp[2];// 入网机构号
//			String merch_id = tmp[3];// 入网商户号
//			String pay_type = tmp[4];// 支付方式
//			String commodity_name = info.getPropname();// 商品名称
//			String amount = info.getPrice();// 金额
//			String back_end_url = match[0];// 后台消息异步通知地址
//			String return_url = info.getCallbackurl() == null
//					|| info.getCallbackurl().length() == 0 ? match[1] : info
//					.getCallbackurl();// 页面同步回调地址
//			String appsecret = tmp[5];
//			String merch_order_no = info.getSpId() + info.getFfId();
//			Map<String, String> reqMap = new HashMap<String, String>();
//			reqMap.put("version", version);
//			reqMap.put("agre_type", agre_type);
//			reqMap.put("inst_no", inst_no);
//			reqMap.put("merch_id", merch_id);
//			reqMap.put("pay_type", pay_type);
//			reqMap.put("commodity_name", commodity_name);
//			reqMap.put("amount", amount);
//			reqMap.put("back_end_url", back_end_url);
//			reqMap.put("return_url", return_url);
//			reqMap.put("merch_order_no", merch_order_no);
//			String sign = PayUtil.getSign(reqMap, appsecret).toLowerCase();
//			reqMap.put("sign", sign);
//			String reqResult = HttpTool.sendPost(info.getUrl(),
//					JSON.toJSONString(reqMap));
//			if (reqResult != null && reqResult.length() > 0) {
//				logger.info("迅捷聚合支付 sd100请求结果:" + reqResult);
//				JSONObject jsonobj = JSONObject.parseObject(reqResult);
//				Map<String, String> resMap = new HashMap<String, String>();
//				for (Map.Entry<String, Object> entry : jsonobj.entrySet()) {
//					String value = entry.getValue() + "";
//					resMap.put(entry.getKey(), value);
//				}
//				String resultSign = resMap.get("sign");
//				resMap.remove("sign");
//				String mySign = PayUtil.getSign(resMap, appsecret)
//						.toLowerCase();
//				if (mySign.equals(resultSign)) {
//					String retcode = resMap.get("retcode");
//					if ("00".equals(retcode)) {
//						logger.info("迅捷聚合支付 sd100请求结果:" + reqResult);
//						Map<String, Object> map = new HashMap<String, Object>();
//						map.put("url", resMap.get("payInfo"));
//						map.put("orderid", info.getFfId());
//						map.put("status", 0);
//						map.put("spid", info.getSpId());
//						result = JSONObject.toJSONString(map);
//					}
//				}
//			}
//		} catch (Exception e) {
//			logger.error("迅捷聚合支付 sd100指令失败!");
//			logger.error(e.getMessage(), e.getCause());
//		}
//		return result;
//	}
	// 迅捷银联快捷支付
	private static String getOnlineSMS154(OrderReqInfo info) {
		// {txnType=01, body=测试商品描述,
		// accessMode=01, subject=测试商品标题,
		// merId=200000000000001, txnSubType=01,
		// customerInfo={"certifTp":"01","certify_id":"","customerNm":"","cvv2":"","expired":"","phoneNo":""},
		// version=1.0.0, txnAmt=1000,
		// currency=CNY, accNo=,
		// signMethod=MD5,
		// backUrl=http://192.168.11.212:8780/payTest/CallBackNotifyServlet,
		// payType=0002, bizType=000000, merResv1=,
		// merOrderId=20180205153838001, accessType=0,
		// txnTime=20180205153838}
		String result = "error";
		if (info.getPmodel() != null && info.getPmodel().length() > 0) {
			result = sendMessage154(info);
			return result;
		}
		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			String[] tmp = info.getKeyword().split("#");
			paramMap.put("bizType", tmp[0]);// 0000#01#100000520000024#1.0.0#0002#0#10000052#f6fd2f3d3387499a93388329a863c681#00#77
			paramMap.put("txnType", tmp[1]);
			paramMap.put("body", info.getPropname());
			paramMap.put("accessMode", tmp[1]);
			paramMap.put("subject", info.getPropname());
			paramMap.put("merId", tmp[2]);
			paramMap.put("txnSubType", tmp[1]);
			paramMap.put("version", tmp[3]);
			paramMap.put("txnAmt", info.getPrice());
			paramMap.put("currency", "CNY");
			paramMap.put("accNo", info.getPck());
			paramMap.put("backUrl", info.getMatchRegex());
			paramMap.put("payType", tmp[4]);
			paramMap.put("merOrderId", info.getFfId());
			paramMap.put("accessType", tmp[5]);
			paramMap.put("customerInfo", info.getCpParam());
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			paramMap.put("txnTime", format.format(date));
			paramMap.put("instNo", tmp[6]);
			String ownKey = tmp[7];
			String param = PayUtil.createSign(paramMap, false) + ownKey;
			System.out.println(param);
			String signature = MD5Utils.getMd524(param);
			paramMap.put("signMethod", "MD5");
			paramMap.put("signature", signature);
			// 对内容做Base64加密
			final String[] base64Keys = new String[] { "subject", "body", "remark" };
			// 对内容做Base64加密， 所有子域采用json数据格式
			final String[] base64JsonKeys = new String[] { "customerInfo", "accResv", "riskRateInfo", "billQueryInfo",
					"billDetailInfo" };
			for (int i = 0; i < base64Keys.length; i++) {
				String key = base64Keys[i];
				String value = (String) paramMap.get(key);
				if (value != null && value.length() > 0) {
					try {
						String text = new String(Base64.encode(value.getBytes("UTF-8")));
						// 更新请求参数
						paramMap.put(key, text);
					} catch (Exception e) {
					}
				}
			}
			for (int i = 0; i < base64JsonKeys.length; i++) {
				String key = base64JsonKeys[i];
				String value = (String) paramMap.get(key);
				if (value != null && value.length() > 0) {
					try {
						String text = new String(Base64.encode(value.getBytes("UTF-8")));
						// 更新请求参数
						paramMap.put(key, text);
					} catch (Exception e) {
					}
				}
			}
			// 提交请求
			String strURL = PayUtil.getWebForm(paramMap);
			String reqResult = HttpTool.sendPost(info.getUrl(), strURL);
			if (reqResult != null && reqResult.length() > 0) {
				logger.info("迅捷银联支付 sda2请求结果:" + reqResult);
				Map<String, String> resMap = CommonTool.parseYZ(reqResult);
				String resultSign = resMap.get("signature");
				String MySign = PayUtil.getXjSign(resMap, ownKey);
				if (resultSign != null && resultSign.length() > 0 && MySign.equals(resultSign)) {
					if ("0000".equals(resMap.get("respCode"))) {
						Map<String, String> messageMap = new HashMap<String, String>();
						messageMap.put("version", tmp[3]);
						messageMap.put("txnType", tmp[9]);
						messageMap.put("txnSubType", tmp[8]);
						messageMap.put("merId", tmp[2]);
						messageMap.put("tn", resMap.get("tn"));
						messageMap.put("instNo", tmp[6]);
						String messageParam = PayUtil.createSign(messageMap, false) + ownKey;
						String messageSignature = MD5Utils.getMd524(messageParam);
						messageMap.put("signature", messageSignature);
						messageMap.put("signMethod", "MD5");
						String messageStrURL = PayUtil.getWebForm(messageMap);
						String messageResult = HttpTool.sendPost(info.getUrl(), messageStrURL);
						if (messageResult != null && messageResult.length() > 0) {
							logger.info("迅捷银联支付 sda2短信请求结果:" + messageResult);
							Map<String, String> resMessageMap = CommonTool.parseYZ(messageResult);
							String resultMessageSign = resMessageMap.get("signature");
							String MyMessageSign = PayUtil.getXjSign(resMessageMap, ownKey);
							if (resultSign != null && resultSign.length() > 0
									&& MyMessageSign.equals(resultMessageSign)) {
								if ("0000".equals(resMessageMap.get("respCode"))) {
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("orderid", info.getFfId());
									map.put("traid", resMap.get("tn"));
									map.put("status", 0);
									map.put("spid", info.getSpId());
									result = JSONObject.toJSONString(map);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 迅捷聚合支付
	private static String sendMessage154(OrderReqInfo info) {
		String result = "error";
		try {
			// //0000#01#100000520000024#1.0.0#0002#0#10000052#f6fd2f3d3387499a93388329a863c681#00#77#02
			String tmp[] = info.getKeyword().split("#");
			String version = tmp[3];
			String txnType = tmp[2];
			String txnSubType = tmp[10];
			String merId = tmp[2];
			String tn = info.getPmodel();
			String smsCode = info.getCode();
			String instNo = tmp[6];
			String ownKey = tmp[7];
			Map<String, String> reqMap = new HashMap<String, String>();
			reqMap.put("version", version);
			reqMap.put("txnType", txnType);
			reqMap.put("txnSubType", txnSubType);
			reqMap.put("merId", merId);
			reqMap.put("tn", tn);
			reqMap.put("smsCode", smsCode);
			reqMap.put("instNo", instNo);
			String param = PayUtil.createSign(reqMap, false) + ownKey;
			System.out.println(param);
			String signature = MD5Utils.getMd524(param);
			reqMap.put("signMethod", "MD5");
			reqMap.put("signature", signature);
			String strURL = PayUtil.getWebForm(reqMap);
			String reqResult = HttpTool.sendPost(info.getUrl(), strURL);
			System.out.println(reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				logger.info("迅捷聚合支付 sda2请求结果:" + reqResult);
				Map<String, String> resMap = CommonTool.parseYZ(reqResult);
				String resultMessageSign = resMap.get("signature");
				String MyMessageSign = PayUtil.getXjSign(resMap, ownKey);
				if (resultMessageSign != null && resultMessageSign.length() > 0
						&& MyMessageSign.equals(resultMessageSign)) {
					if ("1111".equals(resMap.get("respCode"))) {
						logger.info("迅捷聚合支付 sda2请求结果:" + reqResult);
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("orderid", info.getFfId());
						map.put("status", 0);
						map.put("spid", info.getSpId());
						result = JSONObject.toJSONString(map);
					}
				}
			}
		} catch (Exception e) {
			logger.error("迅捷聚合支付 sda2指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	// 迅捷聚合支付
	private static String getOnlineSMS153(OrderReqInfo info) {
		String result = "error";
		try {
			String tmp[] = info.getKeyword().split("#");
			String match[] = info.getMatchRegex().split("#");
			String version = tmp[0];// 区别版本的参数
			String agre_type = tmp[1];// 协议类型
			String inst_no = tmp[2];// 入网机构号
			String merch_id = tmp[3];// 入网商户号
			String pay_type = tmp[4];// 支付方式
			String commodity_name = info.getPropname();// 商品名称
			String amount = info.getPrice();// 金额
			String back_end_url = match[0];// 后台消息异步通知地址
			String return_url = info.getCallbackurl() == null || info.getCallbackurl().length() == 0 ? match[1]
					: info.getCallbackurl();// 页面同步回调地址
			String appsecret = tmp[5];
			String merch_order_no = info.getSpId() + info.getFfId();
			Map<String, String> reqMap = new HashMap<String, String>();
			reqMap.put("version", version);
			reqMap.put("agre_type", agre_type);
			reqMap.put("inst_no", inst_no);
			reqMap.put("merch_id", merch_id);
			reqMap.put("pay_type", pay_type);
			reqMap.put("commodity_name", commodity_name);
			reqMap.put("amount", amount);
			reqMap.put("back_end_url", back_end_url);
			reqMap.put("return_url", return_url);
			reqMap.put("merch_order_no", merch_order_no);
			String sign = PayUtil.getSign(reqMap, appsecret).toLowerCase();
			reqMap.put("sign", sign);
			String reqResult = HttpTool.sendPost(info.getUrl(), JSON.toJSONString(reqMap));
			if (reqResult != null && reqResult.length() > 0) {
				logger.info("迅捷聚合支付 sd100请求结果:" + reqResult);
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				Map<String, String> resMap = new HashMap<String, String>();
				for (Map.Entry<String, Object> entry : jsonobj.entrySet()) {
					String value = entry.getValue() + "";
					resMap.put(entry.getKey(), value);
				}
				String resultSign = resMap.get("sign");
				resMap.remove("sign");
				String mySign = PayUtil.getSign(resMap, appsecret).toLowerCase();
				if (mySign.equals(resultSign)) {
					String retcode = resMap.get("retcode");
					if ("00".equals(retcode)) {
						logger.info("迅捷聚合支付 sd100请求结果:" + reqResult);
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("url", resMap.get("payInfo"));
						map.put("orderid", info.getFfId());
						map.put("status", 0);
						map.put("spid", info.getSpId());
						result = JSONObject.toJSONString(map);
					}
				}
			}
		} catch (Exception e) {
			logger.error("迅捷聚合支付 sd100指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	private static String getOnlineSMS152(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String app = info.getApp();
			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();
			String[] tmp = keyword.split("#");

			/*
			 * BigDecimal b1 = new BigDecimal(price); BigDecimal b2 = new BigDecimal(100);
			 * price = b1.divide(b2).toString();
			 */
			String orderid = info.getSpId() + info.getFfId();
			String merchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String payMethod = tmp[3];
			String backurl = tmp[4];

			/*
			 * String timestamp = DateTool.getTimestamp(new Date()); String time =
			 * timestamp.substring(0, timestamp.length()-3);
			 */
			if (callbackurl == null || callbackurl.length() <= 0) {
				callbackurl = backurl;
			}

			StringBuilder signparam = new StringBuilder();
			signparam.append(merchid).append("_").append(orderid).append("_").append(price).append("_")
					.append(appsecret);

			String sign = MD5Tool.toMD5Value(signparam.toString()).toLowerCase();

			notityurl = URLEncoder.encode(notityurl, "utf-8");
			callbackurl = URLEncoder.encode(callbackurl, "utf-8");
			goodsname = URLEncoder.encode(goodsname, "utf-8");

			StringBuilder param = new StringBuilder();
			param.append("companyId=").append(merchid).append("&userOrderId=").append(orderid).append("&payType=")
					.append(payMethod).append("&item=").append(goodsname).append("&fee=").append(price)
					.append("&callbackUrl=").append(callbackurl).append("&syncUrl=").append(notityurl).append("&sign=")
					.append(sign).append("&ip=").append(info.getIp());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "4000");
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String resultCode = jsonobj.getString("result");
				Map<String, String> payMap = new HashMap<String, String>();
				if ("0".equals(resultCode)) {
					String requrl = jsonobj.getString("param");
					payMap.put("url", requrl);
					payMap.put("status", "0");
					payMap.put("orderid", info.getFfId());
					payMap.put("spid", info.getSpId());
					result = JSONObject.toJSONString(payMap);
				}
			}
			logger.info("云贝支付sd97生成json:" + result);
		} catch (Exception e) {
			logger.error("云贝支付sd97指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	// mmspQQ钱包
	private static String getOnlineSMS151(OrderReqInfo info) {
		String result = "error";
		try {
			String keyword = info.getKeyword();
			String[] tmp = keyword.split("#");
			String CommandID = tmp[0];// 消息命令或响应类型qq正扫
			String SeqID = tmp[1];
			String NodeType = tmp[2];// 请求类型，填写默认值
			String NodeID = tmp[3];
			;// 请求编号，填写默认值
			String Version = tmp[4];// 通信协议版本号，本协议版本号
			String MERNO = tmp[5];// 商户号，平台分发
			String TERMNO = tmp[6];// 终端号
			String TRADETYPE = tmp[7];// 交易类型
			String AMT = info.getPrice();// 金额 分
			String MERORDERID = info.getSpId() + info.getFfId();// 商户订单号，必须唯一
			String RANDSTR = PayUtil.getNonceStr().substring(0, 20);// 随机字符串，不长于20
			String GOODSNAME = info.getPropname();// 商品名称
			String[] match = info.getMatchRegex().split("#");
			String NOTIFY_URL = match[0];
//			NOTIFY_URL=NOTIFY_URL.replace("/","\\/");
			Map<String, String> bodyMap = new LinkedHashMap<String, String>();
			bodyMap.put("AMT", AMT);
			bodyMap.put("GOODSNAME", GOODSNAME);
			bodyMap.put("MERORDERID", MERORDERID);
			bodyMap.put("NOTIFY_URL", NOTIFY_URL);
			bodyMap.put("RANDSTR", RANDSTR);
			bodyMap.put("TRADETYPE", TRADETYPE);
			String Body = JSON.toJSONString(bodyMap);
			System.out.println(Body);
			Map<String, Object> requestMap = new HashMap<String, Object>();
			requestMap.put("CommandID", CommandID);
			requestMap.put("SeqID", SeqID);
			requestMap.put("NodeType", NodeType);
			requestMap.put("NodeID", NodeID);
			requestMap.put("Version", Version);
			requestMap.put("MERNO", MERNO);
			requestMap.put("TERMNO", TERMNO);

//			String keyword = info.getKeyword();
//			String[] tmp = keyword.split("#");
			StringBuilder sb = new StringBuilder();
			String bodyStr = JSON.toJSONString(bodyMap).replace("/", "\\/");
			JSONObject jsonob = JSONObject.parseObject(bodyStr);
			requestMap.put("Body", jsonob);
			sb.append(JSON.toJSONString(bodyMap)).append("&key=").append(match[1]);
			String str = sb.toString().replace("/", "\\/");
			String sign = MD5.md5(str, "utf-8").toUpperCase();
			requestMap.put("Sign", sign);
			String param = JSON.toJSONString(requestMap);
			param = param.replace("/", "\\/");
			String reqResult = HttpTool.sendPost(info.getUrl(), param);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String resultSign = jsonobj.getString("Sign");
				String payParam = jsonobj.getString("Body");
				String md5Pra = payParam.replace("/", "\\/") + "&key=" + match[1];
				String mySign = MD5.md5(md5Pra, "utf-8").toUpperCase();
				String status = jsonobj.getString("RetCode");
				JSONObject payParamJson = JSONObject.parseObject(payParam);
				String status2 = payParamJson.getString("STATUS");
				if (mySign.equals(resultSign) && "1".equals(status) && "1".equals(status2)) {
					logger.info("mmsp sd96请求结果:" + reqResult);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("url", payParamJson.getString("URL"));
					map.put("orderid", info.getFfId());
					map.put("status", 0);
					map.put("spid", info.getSpId());
					result = JSONObject.toJSONString(map);
				}
			}
		} catch (Exception e) {
			logger.error("sd96指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	// 全名点游QQ钱包
	private static String getOnlineSMS150(OrderReqInfo info) {
		String result = "error";
		try {
			String keyword = info.getKeyword();
			String[] tmp = keyword.split("#");
			String appId = tmp[0];// 产品ID
			String partnerId = tmp[1];// 商户号
			String channelOrderId = info.getFfId();// 订单号
			String body = info.getPropname();// 产品名称
			String detail = info.getPropname();// 商品详细描述
			String totalFee = info.getPrice();// 价格
			String attach = info.getSpId();// 透传参数spId
			String payType = tmp[2];// 支付类型
			long timeStamp = System.currentTimeMillis();// 13位时间戳

			String notifyUrl = info.getMatchRegex().split("#")[0];// 通知地址
			String returnUrl = info.getCallbackurl() != null ? info.getCallbackurl()
					: info.getMatchRegex().split("#")[1];// 返回地址

			Map<String, Object> restmap = new HashMap<String, Object>();
			restmap.put("appId", appId);
			restmap.put("totalFee", totalFee);
			restmap.put("timeStamp", timeStamp);
			String md5Param = PaySignUtil.getParamStr(restmap) + "&key=" + tmp[3];
			String sign = MD5.md5(md5Param, "utf-8");

			restmap.put("partnerId", partnerId);
			restmap.put("channelOrderId", channelOrderId);
			restmap.put("body", URLEncoder.encode(body, "utf-8"));
			restmap.put("detail", URLEncoder.encode(detail, "utf-8"));
			restmap.put("attach", attach);
			restmap.put("payType", payType);
			restmap.put("notifyUrl", URLEncoder.encode(notifyUrl, "utf-8"));
			restmap.put("returnUrl", URLEncoder.encode(returnUrl, "utf-8"));
			String param = PaySignUtil.getParamStr(restmap);
			String reqResult = HttpTool.sendGet(info.getUrl(), param + "&sign=" + sign);

			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String status = jsonobj.getString("return_code");
				if ("0".equals(status)) {
					logger.info("全名点游sd95请求结果:" + reqResult);
					String payParam = jsonobj.getString("payParam");
					JSONObject payParamJson = JSONObject.parseObject(payParam);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("url", payParamJson.getString("pay_info"));
					map.put("orderid", info.getFfId());
					map.put("status", 0);
					map.put("spid", info.getSpId());
					result = JSONObject.toJSONString(map);
				}
			}

		} catch (Exception e) {
			logger.error("sd95指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	private static String getOnlineSMS149(OrderReqInfo info) {
		String result = "error";
		try {
			String url = info.getKeyword();
			String matchRegex = info.getMatchRegex();
			String price = info.getPrice();
			String province = info.getProvince();

			StringBuilder param = new StringBuilder();
			if (matchRegex != null && matchRegex.length() >= 0) {
				param.append(matchRegex);
			}
			param.append("province_id=").append(provincezyWHMap.get(province)).append("&price=").append(price)
					.append("&iccid=").append(info.getIccid()).append("&ip=").append(info.getIp()).append("&imsi=")
					.append(info.getImsi()).append("&imei=").append(info.getImei()).append("&mobile=")
					.append(info.getMobile()).append("&extra=").append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "4000");
			logger.info("掌上时代yy11请求结果：" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String returnCode = jsonobj.getString("result_code");
				if ("0".equals(returnCode)) {

					String sms1 = jsonobj.getString("sms");
					String smsport1 = jsonobj.getString("sms_port");
					String sendtype1 = "1";
					String port = "0";
					if ("0".equals(sendtype1)) {
						sms1 = new String(Base64.decode(sms1));
					}

					Map<String, String> map = new HashMap<String, String>();
					map.put("msg", sms1);
					map.put("returnCode", "0");
					map.put("smsNumber", smsport1);
					map.put("send_type", sendtype1);
					if (port != null) {
						map.put("port", port);
					}
					result = JSONObject.toJSONString(map);
				}
			}
		} catch (Exception e) {
			logger.error("掌上时代yy11获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		logger.info("掌上时代yy11返回结果：" + result);
		return result;
	}

	private static String getOnlineSMS148(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String price = info.getPrice();
//			String matchRegex = info.getMatchRegex();
//			String app = info.getApp();
//			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();
			String keyword = info.getKeyword();
			String[] tmp = keyword.split("#");
//			String[] tmp1 = matchRegex.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String service = tmp[3];
			String paytype = tmp[4];
			String signtype = tmp[5];
			String backurl = tmp[6];
			String charset = tmp[7];
			/*
			 * if (callbackurl == null || callbackurl.length() <= 0 || callbackurl.length()
			 * >255) { }
			 */
			if (callbackurl == null || callbackurl.length() <= 0 || callbackurl.length() > 255) {
				callbackurl = backurl;
			}

			Map<String, String> payMap = new HashMap<String, String>();
			Map<String, String> parm = new HashMap<String, String>();
			parm.put("method", service);
			parm.put("mchid", mchid);
			parm.put("randstr", PayUtil.getNonceStr());
			parm.put("orderno", info.getFfId());
			parm.put("orderip", info.getIp());
			parm.put("totalfee", price);
			parm.put("paytype", paytype);
			parm.put("body", info.getCpParam());
			parm.put("signtype", signtype);
			parm.put("charset", charset);
			parm.put("notifyurl", notityurl); // 微信服务器异步通知支付结果地址 下面的order/notify 方法
			parm.put("returnurl", callbackurl);
			parm.put("extra", info.getSpId());

			parm.put("sign", PayUtil.getSign(parm, appsecret));

			String param = JSONObject.toJSONString(parm);
			String restxml = HttpTool.sendPost(url, param, "4000");
			logger.info("阿信第三方支付sd94请求结果:" + restxml);
//			restmap = XmlUtil.xmlParse(restxml);

			if (restxml != null && restxml.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(restxml);
				Map<String, String> resMap = new HashMap<String, String>();
				for (Map.Entry<String, Object> entry : jsonobj.entrySet()) {
					String value = entry.getValue() + "";
					resMap.put(entry.getKey(), value);
				}
				String ressign = resMap.get("sign");
				resMap.remove("sign");
				String resbacksign = PayUtil.getSign(resMap, appsecret);
				if (resbacksign.equals(ressign)) {
					if ("1".equals(resMap.get("status"))) {
						String requrl = resMap.get("payurl");
						payMap.put("url", requrl);
						payMap.put("status", "0");
						payMap.put("orderid", info.getFfId());
						payMap.put("spid", info.getSpId());
						result = JSONObject.toJSONString(payMap);
					} else {
						payMap.put("status", "3");
						payMap.put("message", resMap.get("message"));
						result = JSONObject.toJSONString(payMap);
					}
				} else {
					payMap.put("status", "2");
					payMap.put("message", "sign error");
					result = JSONObject.toJSONString(payMap);
				}
			} else {
				payMap.put("status", "1");
				payMap.put("message", "request time out");
				result = JSONObject.toJSONString(payMap);
			}

			logger.info("阿信第三方支付sd94生成json:" + result);

		} catch (Exception e) {
			logger.error("阿信第三方支付sd94指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS147(OrderReqInfo info) {
		String result = "error";
		try {
			String url = info.getUrl();
			String price = info.getPrice();
			String keyword = info.getKeyword();
			String payCode = getPayCode(info.getFees(), keyword, price, "#");
			String matchregex = info.getMatchRegex();

			String ffId = info.getFfId();
			ffId = ffId.substring(4);

			StringBuilder param = new StringBuilder();
			param.append(matchregex).append(payCode).append(ffId).append("&ip=").append(info.getIp()).append("&imsi=")
					.append(info.getImsi()).append("&imei=").append(info.getImei()).append("&iccid=")
					.append(info.getIccid()).append("&price=").append(price);

			String mobile = info.getMobile();
			if (mobile != null && mobile.length() == 11) {
				param.append("&mb=").append(mobile);
			}

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "4000");
			logger.info("破晓yc39请求结果：" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String returnCode = jsonobj.getString("result");
				if ("0".equals(returnCode)) {
					String sms1 = jsonobj.getString("smsmsg");
					String smsport1 = jsonobj.getString("smsport");
					String sendtype1 = "0";
					String port = jsonobj.getString("smsdest");
					if (sms1 == null || sms1.length() <= 0) {
						sms1 = jsonobj.getString("smsbin");
						sendtype1 = "1";
					}
					if ("0".equals(sendtype1)) {
						sms1 = new String(Base64.decode(sms1));
					}

					Map<String, String> map = new HashMap<String, String>();
					map.put("msg", sms1);
					map.put("returnCode", "0");
					map.put("smsNumber", smsport1);
					map.put("send_type", sendtype1);
					if (port != null) {
						map.put("port", port);
					}
					result = JSONObject.toJSONString(map);
				}
			}
		} catch (Exception e) {
			logger.error("破晓yc39获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		logger.info("破晓yc39返回结果：" + result);
		return result;
	}

	private static String getOnlineSMS146(OrderReqInfo info) {
		String result = "error";
		try {
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String payCode = getPayCode(info.getFees(), keyword, price, "#");
			String matchregex = info.getMatchRegex();

			StringBuilder param = new StringBuilder();
			param.append(matchregex).append("&paycode=").append(payCode).append("&ip=").append(info.getIp())
					.append("&imsi=").append(info.getImsi()).append("&imei=").append(info.getImei()).append("&sid=")
					.append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "4000");
			logger.info("游发yc38请求结果：" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String returnCode = jsonobj.getString("result");
				if ("0".equals(returnCode)) {
					String sms1 = jsonobj.getString("smsmsg");
					String smsport1 = jsonobj.getString("smsport");
					String sendtype1 = "0";
					String port = jsonobj.getString("smsdest");
					if (sms1 == null || sms1.length() <= 0) {
						sms1 = jsonobj.getString("smsbin");
						sendtype1 = "1";
					}
					if ("0".equals(sendtype1)) {
						sms1 = new String(Base64.decode(sms1));
					}

					Map<String, String> map = new HashMap<String, String>();
					map.put("msg", sms1);
					map.put("returnCode", "0");
					map.put("smsNumber", smsport1);
					map.put("send_type", sendtype1);
					if (port != null) {
						map.put("port", port);
					}
					result = JSONObject.toJSONString(map);
				}
			}
		} catch (Exception e) {
			logger.error("游发yc38获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		logger.info("游发yc38返回结果：" + result);
		return result;
	}

	private static String getOnlineSMS145(OrderReqInfo info) {
		String result = "error";
		try {
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String province = info.getProvince();

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&province_id=").append(provincezyWHMap.get(province)).append("&price=")
					.append(price).append("&iccid=").append(info.getIccid()).append("&ip=").append(info.getIp())
					.append("&imsi=").append(info.getImsi()).append("&imei=").append(info.getImei()).append("&mobile=")
					.append(info.getMobile()).append("&extra=").append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "4000");
			logger.info("朱云yc37请求结果：" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String returnCode = jsonobj.getString("result_code");
				if ("0".equals(returnCode)) {
					String cmd_list = jsonobj.getString("cmd_list");
					JSONArray jsonarr = JSONArray.parseArray(cmd_list);
					JSONObject jsonobj1 = jsonarr.getJSONObject(0);

					String sms1 = jsonobj1.getString("sms");
					String smsport1 = jsonobj1.getString("port");
					String sendtype1 = jsonobj1.getString("decode_type");
					String port = jsonobj1.getString("portnumber");
					if ("0".equals(sendtype1)) {
						sms1 = new String(Base64.decode(sms1));
					}

					Map<String, String> map = new HashMap<String, String>();
					map.put("msg", sms1);
					map.put("returnCode", "0");
					map.put("smsNumber", smsport1);
					map.put("send_type", sendtype1);
					if (port != null) {
						map.put("port", port);
					}
					result = JSONObject.toJSONString(map);
				}
			}
		} catch (Exception e) {
			logger.error("朱云yc37获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		logger.info("朱云yc37返回结果：" + result);
		return result;
	}

	// 聚成支付微信H5
	private static String getOnlineSMS144(OrderReqInfo info) {
		String result = "error";
		try {
			String keyword = info.getKeyword();
			String[] tmp = keyword.split("#");
			Map<String, Object> restmap = new HashMap<String, Object>();
			restmap.put("payKey", tmp[0]);// 分配的商户号
			restmap.put("orderPrice", Float.valueOf(info.getPrice()) / 100);// 订单金额，单位：元
			restmap.put("outTradeNo", info.getFfId());// 商户订单号
			restmap.put("productType", tmp[1]);// 产品类型 10000103 微信 扫码支付
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			restmap.put("orderTime", sdf.format(date));// 下单时间
			restmap.put("productName", info.getPropname());// 支付产品名称
			restmap.put("orderIp", info.getIp());// ip
			restmap.put("returnUrl", info.getCallbackurl() == null ? tmp[2] : info.getCallbackurl());// 页面通知地址
			restmap.put("notifyUrl", tmp[3]);// 后台异步通知地址
			restmap.put("remark", info.getSpId());// 透传参数
			String param = PaySignUtil.getParamStr(restmap);
			String md5Param = PaySignUtil.getParamStr(restmap) + "&paySecret=" + tmp[4];
			String sign = MD5.md5(md5Param, "utf-8").toUpperCase();
			String reqResult = HttpTool.sendPost(info.getUrl(), param + "&sign=" + sign);
			System.out.println(info.getUrl() + "?" + param.toString());
			if (reqResult != null && reqResult.length() > 0) {
				@SuppressWarnings("unchecked")
				Map<String, String> resultMap = (Map<String, String>) JSON.parse(reqResult);

				String params = PaySignUtil.getParamStr2(resultMap) + "&paySecret=" + tmp[4];
				String resultSign = resultMap.get("sign");
				if (MD5.md5(params, "utf-8").toUpperCase().equals(resultSign)) {
					String status = resultMap.get("resultCode");
					if ("0000".equals(status)) {
						logger.info("聚成支付sd93请求结果:" + reqResult);
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("url", resultMap.get("payMessage"));
						map.put("orderid", info.getFfId());
						map.put("status", 0);
						map.put("spid", info.getSpId());
						result = JSONObject.toJSONString(map);
					}
				}
			}
		} catch (Exception e) {
			logger.error("sd93指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	// 迅鸿联通
	private static String getOnlineSMS143(OrderReqInfo info) {
		String result = "error";
		try {
			String imsi = info.getImsi();
			String imei = info.getImei();
			String mobile = info.getMobile();// 手机号
			String cid = info.getKeyword().split("#")[0];// 厂家提供id
			String price = String.valueOf(info.getFee());// 计费金额
			String vercode = info.getKeyword().split("#")[1];// 验证码
			String ip = info.getIp();// 手机ip
			String appName = info.getGamename();// 游戏名称
			String Cpfeename = info.getPropname();// 道具名称
			String iccid = info.getIccid();// iccid
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("imsi", imsi);
			map.put("imei", imei);
			map.put("mobile", mobile);
			map.put("cid", cid);
			map.put("price", price);
			map.put("vercode", vercode);
			map.put("ip", ip);
			map.put("appName", appName);
			map.put("Cpfeename", Cpfeename);
			map.put("iccid", iccid);
			map.put("cpserverparam", info.getFfId());
			String reqResult = HttpTool.sendPost(info.getUrl(), JSON.toJSONString(map));
			logger.error(reqResult);
			JSONObject resultObj = JSONObject.parseObject(reqResult);
			String status = resultObj.getString("returnCode");
			if ("00".equals(status)) {
				result = resultObj.toString();
			}
		} catch (Exception e) {
			logger.error("从迅鸿获取一次指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	// 付易通微信H5
	private static String getOnlineSMS142(OrderReqInfo info) {
		String result = "error";
		try {
			String keyword = info.getKeyword();
			String[] tmp = keyword.split("#");
			Map<String, String> restmap = new HashMap<String, String>();
			restmap.put("mch_id", tmp[0]);// 分配的商户号
			restmap.put("total_fee", info.getPrice());// 资费 单位分
			restmap.put("out_trade_no", info.getFfId());// 自定义订单号
			restmap.put("body", info.getPropname());// 商品描述
			restmap.put("deviceIp", info.getIp());// 客户端微信支付IP地址，很重要，必须真实
			restmap.put("attach", info.getSpId());// 自定义参数，订单状态回调时原样带回
			restmap.put("pay_type", tmp[1]);// webchat
			restmap.put("notify_url", tmp[2]);// notify_url
			if (info.getCallbackurl() != null && info.getCallbackurl().length() > 0)
				restmap.put("callback_url", info.getCallbackurl());// 前端页面跳转地址
			else
				restmap.put("callback_url", tmp[4]);// 返回地址
			restmap.put("key", tmp[3]);
			String param = PaySignUtil.getParamStr2(restmap);
			String sign = MD5.md5(param, "utf-8");
			String reqResult = HttpTool.sendPost(info.getUrl(), param + "&sign=" + sign);
			System.out.println(info.getUrl() + "?" + param.toString());
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String status = jsonobj.getString("status");
				if ("0".equals(status)) {
					logger.info("付易通sd91请求结果:" + reqResult);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("url", jsonobj.getString("payInfo"));
					map.put("orderid", info.getFfId());
					map.put("status", 0);
					map.put("spid", info.getSpId());
					result = JSONObject.toJSONString(map);
				}
			}
		} catch (Exception e) {
			logger.error("sd91指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	// 外放加冕QQH5
	private static String getOnlineSMS141(OrderReqInfo info) {
		String result = "error";
		try {
			String keyword = info.getKeyword();
			String[] tmp = keyword.split("#");
			String amount = String.valueOf((float) Integer.valueOf(info.getPrice()) / 100);// 金额
			String body = info.getPropname();// 商品的简要概述
			String mid = tmp[0];// 商户号
			String noise = String.valueOf(ToolUtil.buildRandom(10));
			String notifyUrl = tmp[1];// 接收支付通知的url
			String orderNo = info.getFfId();// 订单号
//			String subject=info.getPropname();//商品名称
			String type = tmp[2];// QQwallet 支付种类 tmp[3]秘钥
			System.out.println();
			StringBuilder param = new StringBuilder();
			param.append("amount=").append(amount).append("&body=").append(body).append("&mid=").append(mid)
					.append("&noise=").append(noise).append("&notifyUrl=").append(notifyUrl).append("&orderNo=")
					.append(info.getSpId() + orderNo).append("&subject=").append(orderNo).append("&type=").append(type);
			String stringSignTemp = param.append("&").append(tmp[3]).toString();
			String sign = MD5.md5(stringSignTemp, "utf-8").toUpperCase();
			String reqResult = HttpTool.sendPost(info.getUrl(), param.append("&sign=").append(sign).toString());
			logger.info("sd01请求结果：" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String status = jsonobj.getString("resultCode");
				if ("SUCCESS".equals(status)) {
					logger.info("外放加冕QQ钱包sd90请求结果:" + reqResult);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("url", jsonobj.getString("qrCode"));
					map.put("orderid", info.getFfId());
					map.put("status", 0);
					map.put("spid", info.getSpId());
					result = JSONObject.toJSONString(map);
				}
			}
		} catch (Exception e) {
			logger.error("sd90指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	private static String getOnlineSMS140(OrderReqInfo info) {
		String result = "error";
		try {
			String keyword = info.getKeyword();
			String[] tmp = keyword.split("#");
			StringBuilder param = new StringBuilder();
			param.append("amount=").append(info.getPrice()).append("&app_id=").append(tmp[0]).append("&extends=")
					.append(info.getSpId()).append("&notify_url=").append(tmp[1]).append("&order_id=")
					.append(info.getFfId()).append("&order_name=").append(info.getPropname());
			if (info.getCallbackurl() != null && info.getCallbackurl().length() > 0) {
				param.append("&return_url=").append(info.getCallbackurl());
			}

			String sign = HMacMD5.getHmacMd5Str(tmp[2], param.toString());//
			// .append("&sign=").append(sign);

			if (tmp[1] != null && tmp[1].length() > 0)
				tmp[1] = URLEncoder.encode(tmp[1], "utf-8");
			if (info.getCallbackurl() != null && info.getCallbackurl().length() > 0)
				info.setCallbackurl(URLEncoder.encode(info.getCallbackurl(), "utf-8"));
//			if (info.getPropname() != null && info.getPropname().length() > 0)
//				info.setPropname(URLEncoder.encode(info.getPropname(), "utf-8"));
			StringBuilder params = new StringBuilder();
			params.append("amount=").append(info.getPrice()).append("&app_id=").append(tmp[0]).append("&extends=")
					.append(info.getSpId()).append("&notify_url=").append(tmp[1]).append("&order_id=")
					.append(info.getFfId()).append("&order_name=").append(info.getPropname());
			if (info.getCallbackurl() != null && info.getCallbackurl().length() > 0) {
				params.append("&return_url=").append(info.getCallbackurl());
			}
			params.append("&sign=").append(sign);

			String reqResult = HttpTool.sendGetSetTimeout(info.getUrl(), params.toString(), "4000");
			logger.info("微信H5sd86请求结果:" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String status = jsonobj.getString("code");
				String requtl = jsonobj.getString("pay_url");
				String traid = jsonobj.getString("ppay_order");
				if ("0".equals(status)) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("url", requtl);
					map.put("orderid", info.getFfId());
					map.put("status", 0);
					map.put("spid", info.getSpId());
					map.put("traid", traid);
					result = JSONObject.toJSONString(map);
				}
			}
			logger.info("微信H5sd86生成json:" + result);

		} catch (Exception e) {
			logger.error("微信H5sd86指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	private static String getOnlineSMS139(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
//			String app = info.getApp();
			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();
			String[] tmp = keyword.split("#");

			/*
			 * BigDecimal b1 = new BigDecimal(price); BigDecimal b2 = new BigDecimal(100);
			 * price = b1.divide(b2).toString();
			 */

			String merchantNo = tmp[0];
			String appsecret = ConstantDefine.JHZ_V_KEY;
			String notityurl = tmp[2];
			String payMethod = tmp[3];
			String backurl = tmp[4];

			String timestamp = DateTool.getTimestamp(new Date());
			String time = timestamp.substring(0, timestamp.length() - 3);
			if (callbackurl == null || callbackurl.length() <= 0) {
				callbackurl = backurl;
			}

			StringBuilder signparam = new StringBuilder();
			signparam.append(merchantNo).append("|").append(info.getFfId()).append("|").append(price).append("|")
					.append(callbackurl).append("|").append(notityurl).append("|").append(time).append("|").append("|")
					.append(goodsname).append("|").append("|");

			String sign = URLEncoder.encode(RSASignature.sign(signparam.toString(), appsecret), "utf-8");

			notityurl = URLEncoder.encode(notityurl, "utf-8");
			callbackurl = URLEncoder.encode(callbackurl, "utf-8");
			goodsname = URLEncoder.encode(goodsname, "utf-8");

			StringBuilder param = new StringBuilder();
			param.append("merchantNo=").append(merchantNo).append("&requestNo=").append(info.getFfId())
					.append("&amount=").append(price).append("&payMethod=").append(payMethod).append("&backUrl=")
					.append(notityurl).append("&pageUrl=").append(callbackurl).append("&payDate=").append(time)
					.append("&remark1=").append(goodsname).append("&remark2=").append("&remark3=").append("&signature=")
					.append(sign);

			Map<String, String> payMap = new HashMap<String, String>();

			String requrl = url + "?" + param.toString();
			payMap.put("url", requrl);
			payMap.put("status", "0");
			payMap.put("orderid", info.getFfId());
			payMap.put("spid", info.getSpId());
			result = JSONObject.toJSONString(payMap);

			logger.info("金海哲sd85生成json:" + result);

		} catch (Exception e) {
			logger.error("金海哲sd85指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS138(OrderReqInfo info) {
		String result = "error";
		try {

			String keyword = info.getKeyword();
			String[] tmp = keyword.split("#");

			String url = tmp[8];

			String playerid = info.getImsi();
			String callbackurl = info.getCallbackurl();
			String app = info.getApp();
			String goodsname = info.getPropname();

			StringBuilder param = new StringBuilder();
			param.append("cpid=").append(info.getCpId()).append("&fee=").append(info.getPrice()).append("&ip=")
					.append(info.getIp()).append("&spid=").append(info.getSpId()).append("&cpparam=")
					.append(info.getCpParam());

			if (playerid != null && playerid.length() > 0)
				param.append("&playerid=").append(URLEncoder.encode(playerid, "utf-8"));
			if (callbackurl != null && callbackurl.length() > 0)
				param.append("&callbackurl=").append(URLEncoder.encode(callbackurl, "utf-8"));
			if (app != null && app.length() > 0)
				param.append("&app=").append(URLEncoder.encode(app, "utf-8"));
			if (goodsname != null && goodsname.length() > 0)
				param.append("&goodsname=").append(URLEncoder.encode(goodsname, "utf-8"));

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "4000");
			logger.info("微信H5sd81请求结果:" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String status = jsonobj.getString("status");
				String requtl = jsonobj.getString("url");
				String orderid = jsonobj.getString("orderid");
				if ("0".equals(status)) {
					info.setFfId(orderid);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("url", requtl);
					map.put("orderid", orderid);
					map.put("status", 0);
					map.put("spid", info.getSpId());
					result = JSONObject.toJSONString(map);
				}
			}
			logger.info("微信H5sd81生成json:" + result);

		} catch (Exception e) {
			logger.error("微信H5sd81指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	/**
	 * @author : Tractionice
	 * @version 创建时间：2017年11月29日 下午5:43:00
	 * @param mch :商户编号;key:商户秘钥;pay_type:支付类型;money:金额 分为单位;time:订单时间,秒为单位;order_id
	 *            :订单编号;return_url:支付后调用的url;notify_url :后台通知地址;sign:签名;extra:透传参数;
	 * @return
	 */
	private static String getOnlineSMS137(OrderReqInfo info) {
		String result = "error";
		try {
			long time = System.currentTimeMillis() / 1000;
			String keyword = info.getKeyword();
			String[] tmp = keyword.split("#");
			String mch = tmp[0];// 商户编号
			String key = tmp[1];// 商户秘钥
			String pay_type = tmp[2];// 支付类型
			String notify_url = tmp[3];// 后台通知地址
			String backurl = tmp[4];
			StringBuilder sign = new StringBuilder();
			sign.append(info.getFfId()).append(info.getPrice()).append(pay_type).append(time).append(mch)
					.append(MD5.md5(key, "utf-8"));
			String url = info.getUrl();
			if (info.getCallbackurl() == null || info.getCallbackurl().length() <= 0
					|| info.getCallbackurl().length() >= 100) {
				info.setCallbackurl(backurl);
			}
			StringBuilder param = new StringBuilder();
			param.append("mch=").append(mch).append("&money=").append(Integer.valueOf(info.getPrice())).append("&time=")
					.append((int) time).append("&sign=").append(MD5.md5(sign.toString(), "utf-8")).append("&order_id=")
					.append(info.getFfId()).append("&return_url=")
					.append(URLDecoder.decode(info.getCallbackurl(), "utf-8")).append("&notify_url=")
					.append(URLDecoder.decode(notify_url, "utf-8")).append("&pay_type=").append(pay_type)
					.append("&extra=").append(info.getSpId());
			String reqResult = HttpTool.sendGet(url, param.toString());
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String status = jsonobj.getString("ok");
				if ("true".equals(status)) {
					logger.info("微信H5sd84请求结果:" + reqResult);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("url", jsonobj.getString("data"));
					map.put("orderid", info.getFfId());
					map.put("status", 0);
					map.put("spid", info.getSpId());
					result = JSONObject.toJSONString(map);
				}
			}
		} catch (Exception e) {
			logger.error("sd84指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	private static String getOnlineSMS136(OrderReqInfo info) {
		String result = "error";
		try {
			String orderid = info.getFfId();
			String mz = info.getPrice();
			String spzdy = info.getSpId();
			String price = info.getPrice();
			String uid = info.getImsi();
			String url = info.getUrl();
			String spsuc = info.getCallbackurl();
			String productname = info.getPropname();
			String keyword = info.getKeyword();
			String[] tmp = keyword.split("#");
			String ordertype = tmp[0];// 微信支付 2
			String interfacetype = tmp[1];// WAP 4
			String sp = tmp[2];// "8879"
			String sppwd = tmp[3];// 023def1d839140a6a0
			String notifyurl = tmp[4];// notifyurl
			StringBuilder key = new StringBuilder();
			String banktype = tmp[5];// 银行卡类型：1、借记卡2、信用卡
			String domain = tmp[6];
			String backurl = tmp[7];
			key.append(sp).append(orderid).append(sppwd).append(mz).append(spsuc).append(ordertype)
					.append(interfacetype);
			String md5key = MD5.md5(key.toString(), "utf-8").toUpperCase();
			BigDecimal b1 = new BigDecimal(price);
			BigDecimal b2 = new BigDecimal(100);
			price = b1.divide(b2).toString();
			if (info.getCallbackurl() == null || info.getCallbackurl().length() <= 0
					|| info.getCallbackurl().length() >= 100) {
				info.setCallbackurl(backurl);
			}
			StringBuilder param = new StringBuilder();
			param.append("spid=").append(sp).append("&orderid=").append(orderid).append("&mz=").append(mz)
					.append("&spzdy=").append(spzdy).append("&uid=").append(uid).append("&spsuc=")
					.append(URLDecoder.decode(spsuc, "utf-8")).append("&ordertype=").append(ordertype)
					.append("&interfacetype=").append(interfacetype).append("&productname=").append(productname)
					.append("&sign=").append(md5key).append("&notifyurl=").append(URLDecoder.decode(notifyurl, "utf-8"))
					.append("&banktype=").append(banktype);
			String reqResult = HttpTool.sendGet(url, param.toString());
			if (reqResult != null && reqResult.length() > 0) {
				logger.info("微信H5sd83请求结果:" + reqResult);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("url", parseURL2(reqResult, info, "", domain));
				map.put("orderid", orderid);
				map.put("status", 0);
				map.put("spid", spzdy);
				result = JSONObject.toJSONString(map);
			}
		} catch (Exception e) {
			logger.error("sd83指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	private static String getOnlineSMS135(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String callbackurl = info.getCallbackurl();
			String[] tmp = keyword.split("#");

			String cpid = tmp[0];
			String notityurl = tmp[1];
			String backurl = tmp[2];
			if (callbackurl == null || callbackurl.length() <= 0 || callbackurl.length() >= 255) {
				callbackurl = backurl;
			}

			BigDecimal b1 = new BigDecimal(price);
			BigDecimal b2 = new BigDecimal(100);
			price = b1.divide(b2).toString();

			StringBuilder param = new StringBuilder();
			param.append("cpid=").append(cpid).append("&fee=").append(price).append("&cpparam=").append(info.getFfId())
					.append("&callbackurl=").append(callbackurl).append("&notifyUrl=").append(notityurl)
					.append("&goodsname=").append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "4000");
			logger.info("鸿游sd82请求结果:" + reqResult);
			Map<String, String> payMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String status = jsonobj.getString("status");
				if ("0".equals(status)) {
					String requrl = jsonobj.getString("url");
					payMap.put("url", requrl);
					payMap.put("status", "0");
					payMap.put("orderid", info.getFfId());
					payMap.put("spid", info.getSpId());
					result = JSONObject.toJSONString(payMap);
				}
			}

			logger.info("鸿游sd82生成json:" + result);

		} catch (Exception e) {
			logger.error("鸿游sd82指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS134(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String app = info.getApp();
			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();
			String[] tmp = keyword.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
//			String notityurl = tmp[2];
			String service = tmp[3];
			String device_info = tmp[4];
//			String mch_app_name = tmp[5];
//			String backurl = tmp[6];

			BigDecimal b1 = new BigDecimal(price);
			BigDecimal b2 = new BigDecimal(100);
			price = b1.divide(b2).toString();

			StringBuilder param = new StringBuilder();
			param.append("version=").append(service).append("&merid=").append(mchid).append("&mername=")
					.append(device_info).append("&merorderid=").append(info.getFfId()).append("&paymoney=")
					.append(price).append("&productname=").append(app).append("&productdesc=").append(goodsname)
					.append("&userid=").append(info.getFfId()).append("&username=").append("&email=").append("&phone=")
					.append("&extra=").append(info.getSpId()).append("&custom=").append("&redirecturl=");
			if (callbackurl != null && callbackurl.length() > 0) {
				param.append(callbackurl);
			}
			String sign = MD5.md5(param.toString() + appsecret, "utf-8");
			param.append("&md5=").append(sign);

			String reqResult = HttpTool.sendPost(url, param.toString(), "4000");
			logger.info("聚合富支付宝sd80请求结果:" + reqResult);
			Map<String, String> payMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {
				payMap.put("url", reqResult);
				payMap.put("status", "0");
				payMap.put("orderid", info.getFfId());
				payMap.put("spid", info.getSpId());
//				result = JSONObject.toJSONString(payMap);
				result = reqResult;
			}

			logger.info("聚合富支付宝sd80生成json:" + result);

		} catch (Exception e) {
			logger.error("聚合富支付宝sd80指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS133(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
//			String app = info.getApp();
			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();
			String[] tmp = keyword.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String service = tmp[3];
			String device_info = tmp[4];
//			String mch_app_name = tmp[5];
			String backurl = tmp[6];
			if (callbackurl == null || callbackurl.length() <= 0 || callbackurl.length() >= 100) {
				callbackurl = backurl;
			}
			String timestamp = DateTool.getTimestamp(new Date());
			String time = timestamp.substring(0, timestamp.length() - 3);
			String pay_order_id = info.getFfId().substring(0, 2) + time + CommonTool.createNum(8);
			Map<String, String> parm = new HashMap<String, String>();
			parm.put("partner", mchid);
//			parm.put("md5key", appsecret);
			parm.put("pay_service_type", device_info);
			parm.put("pay_date", time);
			parm.put("pay_order_id", pay_order_id);
			parm.put("pay_product_name", info.getFfId());
			parm.put("pay_amount", price);
			parm.put("pay_notify_url", notityurl); // 微信服务器异步通知支付结果地址 下面的order/notify 方法
			parm.put("pay_return_url", callbackurl); // 交易完成后跳转的 URL，需给绝对路径，255 字符内
			parm.put("pay_input_charset", service);
			parm.put("pay_sign", PayUtil.getSign(parm, appsecret));
			parm.put("pay_remark", info.getSpId());
			parm.put("pay_extend1", info.getSpId());
			parm.put("pay_extend2", info.getFfId());

			String param = PayUtil.getFormData(parm, false);
			/*
			 * String reqResult = HttpTool.sendPost(url, param, "4000");
			 * logger.info("微信H5sd79请求结果:"+reqResult);
			 */
			Map<String, String> payMap = new HashMap<String, String>();
			payMap.put("url", url + "?" + param);
			payMap.put("status", "0");
			payMap.put("orderid", info.getFfId());
			payMap.put("spid", info.getSpId());
			result = JSONObject.toJSONString(payMap);
			/*
			 * if (reqResult != null && reqResult.length() > 0) { result = reqResult; }
			 */

			logger.info("微信H5sd79生成json:" + result);

		} catch (Exception e) {
			logger.error("微信H5sd79指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS132(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String app = info.getApp();
			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();
			String[] tmp = keyword.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String service = tmp[3];
			String device_info = tmp[4];
			String mch_app_name = tmp[5];
			String mch_app_id = tmp[6];
			String domain = tmp[7];
			String backurl = domain + "/backurl.htm";
			if (callbackurl == null || callbackurl.length() <= 0 || callbackurl.length() > 255) {
				callbackurl = backurl;
			}
			String rescallurl = parseURL(callbackurl, info, "backurl", domain);

			Map<String, String> parm = new HashMap<String, String>();
			parm.put("pay_type", service);
			parm.put("merchant_id", mchid);
			parm.put("order_title", "充值");
			parm.put("merchant_order_no", info.getFfId());
			parm.put("additional_info", info.getSpId());
			parm.put("order_amt", price);
			parm.put("term_ip", info.getIp());
			parm.put("wap_type", device_info);
			parm.put("wap_app_title", mch_app_name);
			parm.put("wap_app_url", mch_app_id);
			parm.put("notify_url", notityurl); // 微信服务器异步通知支付结果地址 下面的order/notify 方法
			parm.put("back_url", rescallurl); // 交易完成后跳转的 URL，需给绝对路径，255 字符内
			parm.put("sign", PayUtil.getSign(parm, appsecret));

			rescallurl = URLEncoder.encode(rescallurl, "utf-8");

			String param = JSONObject.toJSONString(parm);
			String reqResult = HttpTool.sendKLWPost(url, param, "utf-8", 4000);
			logger.info("微信H5sd77请求结果:" + reqResult);
			Map<String, String> payMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String status = jsonobj.getString("code");
				Map<String, String> resMap = new HashMap<String, String>();
				for (Map.Entry<String, Object> entry : jsonobj.entrySet()) {
					String value = entry.getValue() + "";
					resMap.put(entry.getKey(), value);
				}
				String ressign = resMap.get("sign");
				resMap.remove("sign");

				String resbacksign = PayUtil.getSign(resMap, appsecret);
				if (resbacksign.equals(ressign)) {
					if (status != null && "0000".equals(status)) {
						String requrl = resMap.get("pay_url");
						requrl = URLDecoder.decode(requrl, "utf-8");
						requrl = requrl + "&redirect_url=" + rescallurl;
						String resurl = parseURL(requrl, info, "", domain);
						requrl = resurl;
						payMap.put("url", requrl);
						payMap.put("status", "0");
						payMap.put("orderid", info.getFfId());
//						payMap.put("traid", resMap.get("platform_order_no"));
						payMap.put("spid", info.getSpId());
						result = JSONObject.toJSONString(payMap);
					}
				}
			}
			logger.info("微信H5sd77生成json:" + result);

		} catch (Exception e) {
			logger.error("微信H5sd77指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS131(OrderReqInfo info) {
		String result = "error";
		try {
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice().replace("00", "");

			String province = info.getProvince();
			province = province.replace("内蒙古", "内蒙").replace("黑龙江", "黑龙").replace("省", "").replace("市", "");
			String pro = provinceMap.get(province);
			if (pro == null || pro.length() <= 0)
				pro = "1";

			String operator = "CMCC";
			int phoneType = info.getPhoneType();// 1移动 2联通 3电信
			if (phoneType == 2) {
				operator = "CUC";
			} else if (phoneType == 3) {
				operator = "CNC";
			}

			String mobile = info.getMobile();

			String imsi = info.getImsi();
			int imsilen = imsi.length();
			int count = 6;
			imsi = imsi.substring(0, imsilen - count) + CommonTool.createNum(count);

			String imei = info.getImei();
			int imeilen = imei.length();
			int ecount = 8;
			if (imei != null && imeilen > ecount) {
				imei = imei.substring(0, imeilen - ecount) + CommonTool.createNum(ecount);
			}

			String iccid = info.getIccid();
			int iccidlen = iccid.length();
			int iccount = 5;
			if (iccid != null && iccidlen > iccount) {
				iccid = iccid.substring(0, iccidlen - iccount) + CommonTool.createNum(iccount);
			}

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&imei=").append(imei).append("&imsi=").append(imsi).append("&iccid=")
					.append(iccid).append("&operator=").append(operator).append("&ip=").append(info.getIp())
					.append("&pro=").append(pro).append("&fee=").append(price).append("&appName=")
					.append(URLEncoder.encode("奶爸当家", "utf-8")).append("&payCode=")
					.append(URLEncoder.encode("超值包", "utf-8")).append("&info1=").append(info.getFfId());

			if (mobile != null && mobile.length() == 11) {
				param.append("&phone=").append(mobile);
			}

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "6000");
			logger.info("苏州全真dm35请求结果:" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String state = jsonobj.getString("result");
				if (state != null && "0".equals(state)) {

					String sms1 = jsonobj.getString("command");
					String port1 = jsonobj.getString("port");
					String type1 = jsonobj.getString("netWorkingType");
					String sms2 = jsonobj.getString("command1");
					if (sms2 == null || sms2.length() <= 0) {
						if ("1".equals(type1)) {
							type1 = "0";
						} else if ("2".equals(type1)) {
							type1 = "1";
						}

						Map<String, String> resultMap = new HashMap<String, String>();
						resultMap.put("msg", sms1);
						resultMap.put("send_type", type1);
						resultMap.put("smsNumber", port1);
						resultMap.put("port", "80");
						resultMap.put("returnCode", "0");

						result = JSONObject.toJSONString(resultMap);
					}
				}
			}
			logger.info("dm35请求返回生成json数据：" + result);

		} catch (Exception e) {
			logger.error("苏州全真dm35指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS130(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
//			String app = info.getApp();
			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();
			String[] tmp = keyword.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String service = tmp[3];
			String backurl = tmp[4];

			BigDecimal b1 = new BigDecimal(price);
			BigDecimal b2 = new BigDecimal(100);
			price = b1.divide(b2).toString();

			if (callbackurl == null || callbackurl.length() <= 0) {
				callbackurl = backurl;
			}

			Map<String, Object> map = new HashMap<String, Object>();
			// 支付测试签名示例
			map.put("payMoney", price);
			map.put("bussOrderNum", info.getFfId());
			map.put("notifyUrl", notityurl);
			map.put("appKey", mchid);// 测试appKey
			map.put("orderName", goodsname);
			map.put("returnUrl", callbackurl);
			map.put("ip", info.getIp());
			map.put("payPlatform", service);
			map.put("remark", info.getSpId());
			map.put("appType", "2");
			String sign = PaySignUtil.getSign(map, appsecret);// 测试密钥
			String paramStr = PaySignUtil.getParamStr(map) + "&sign=" + sign;
			String param = "paramStr=" + URLEncoder.encode(paramStr, "utf-8");

			Map<String, String> payMap = new HashMap<String, String>();
			String requrl = url + "?" + param;
			payMap.put("url", requrl);
			payMap.put("status", "0");
			payMap.put("orderid", info.getFfId());
			payMap.put("spid", info.getSpId());

			result = JSONObject.toJSONString(payMap);

			logger.info("全名点游sd75生成json:" + result);

		} catch (Exception e) {
			logger.error("全名点游sd75指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	private static String getOnlineSMS128(OrderReqInfo info) {
		String result = "error";
		try {
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String payCode = getPayCode(info.getFees(), keyword, price, "#");
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");

			StringBuilder param = new StringBuilder();
			param.append(tmp[0]).append("&paycode=").append(payCode).append("&fee=").append(price).append("&iccid=")
					.append(info.getIccid()).append("&ipaddr=").append(info.getIp()).append("&imsi=")
					.append(info.getImsi()).append("&imei=").append(info.getImei()).append("&extension=")
					.append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "5000");
			logger.info("普石yc36请求结果=>" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String returnCode = jsonobj.getString("retCode");
				Map<String, String> map = new HashMap<String, String>();
				if ("1".equals(returnCode)) {
					String sms1 = jsonobj.getString("mo");
					String smsport1 = jsonobj.getString("called");
					String sendtype1 = jsonobj.getString("smstype");
					String port = jsonobj.getString("smsdest");

					if ("1".equals(sendtype1)) {
						sendtype1 = "0";
					} else if ("2".equals(sendtype1)) {
						sendtype1 = "1";
					}

					map.put("msg", sms1);
					map.put("returnCode", "0");
					map.put("smsNumber", smsport1);
					map.put("send_type", sendtype1);
					if (port != null) {
						map.put("port", port);
					}
					result = JSONObject.toJSONString(map);
				} else if ("0".equals(returnCode)) {
					map.put("returnCode", "0");
					map.put("tradeId", info.getFfId());
				}
			}
		} catch (Exception e) {
			logger.error("普石yc36获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		logger.info("普石yc36返回结果：" + result);
		return result;
	}

	private static String getOnlineSMS127(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
//			String matchRegex = info.getMatchRegex();
			String app = info.getApp();
			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();
			String[] tmp = keyword.split("#");
//			String[] tmp1 = matchRegex.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String service = tmp[3];
			String backurl = tmp[4];
			/*
			 * String device_info = tmp[4]; String mch_app_name = tmp[5]; String mch_app_id
			 * = tmp[6];
			 */

			/*
			 * String payurl = tmp1[0]; // String gzhappid = tmp1[3]; String gzhappsecret =
			 * tmp1[3]; String gzhurl = tmp1[4];
			 */

			if (callbackurl == null || callbackurl.length() <= 0) {
				callbackurl = backurl;
			}

//			callbackurl = URLEncoder.encode(callbackurl, "utf-8");

			StringBuilder signparam = new StringBuilder();
			signparam.append("body=icon").append("&clientIp=").append(info.getIp()).append("&notifyUrl=")
					.append(URLEncoder.encode(notityurl, "utf-8")).append("&orderid=").append(info.getFfId())
					.append("&paytype=").append(service).append("&returnUrl=")
					.append(URLEncoder.encode(callbackurl, "utf-8")).append("&total_fee=").append(price)
					.append("&userid=").append(mchid).append("&key=").append(appsecret);
			System.out.println(signparam.toString());

			StringBuilder param = new StringBuilder();
			param.append("body=icon").append("&clientIp=").append(info.getIp()).append("&notifyUrl=").append(notityurl)
					.append("&orderid=").append(info.getFfId()).append("&paytype=").append(service)
					.append("&returnUrl=").append(callbackurl).append("&total_fee=").append(price).append("&userid=")
					.append(mchid);

			String sign = MD5.md5(signparam.toString(), "utf-8");

			/*
			 * Map<String, String> map = new HashMap<String, String>(); map.put("body",
			 * "icon"); map.put("clientIp", info.getIp()); map.put("notifyUrl", notityurl);
			 * map.put("orderid", info.getFfId()); map.put("paytype", service);
			 * map.put("returnUrl", callbackurl); map.put("total_fee", price);
			 * map.put("userid", mchid); map.put("sign", sign);
			 */

			param.append("&sign=").append(sign);

			String reqResult = HttpTool.sendPost(url, param.toString(), "5000");
			logger.info("1387sd69请求结果:" + reqResult);
			Map<String, String> payMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {
				payMap.put("url", reqResult);
				payMap.put("status", "0");
				payMap.put("orderid", info.getFfId());
				payMap.put("spid", info.getSpId());
//				result = JSONObject.toJSONString(payMap);
				result = reqResult;
			}

			logger.info("1387sd69生成json:" + result);

		} catch (Exception e) {
			logger.error("1387sd69指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS126(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
//			String app = info.getApp();
			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();
			String[] tmp = keyword.split("#");

			/*
			 * BigDecimal b1 = new BigDecimal(price); BigDecimal b2 = new BigDecimal(100);
			 * price = b1.divide(b2).toString();
			 */

			String merchantNo = tmp[0];
			String appsecret = ConstantDefine.JHZ_V_KEY;
			String notityurl = tmp[2];
			String payMethod = tmp[3];
			String backurl = tmp[4];

			String timestamp = DateTool.getTimestamp(new Date());
			String time = timestamp.substring(0, timestamp.length() - 3);
			if (callbackurl == null || callbackurl.length() <= 0) {
				callbackurl = backurl;
			}

			StringBuilder signparam = new StringBuilder();
			signparam.append(merchantNo).append("|").append(info.getFfId()).append("|").append(price).append("|")
					.append(callbackurl).append("|").append(notityurl).append("|").append(time).append("|").append("|")
					.append(goodsname).append("|").append("|");

			String sign = URLEncoder.encode(RSASignature.sign(signparam.toString(), appsecret), "utf-8");

			notityurl = URLEncoder.encode(notityurl, "utf-8");
			callbackurl = URLEncoder.encode(callbackurl, "utf-8");
			goodsname = URLEncoder.encode(goodsname, "utf-8");

			StringBuilder param = new StringBuilder();
			param.append("merchantNo=").append(merchantNo).append("&requestNo=").append(info.getFfId())
					.append("&amount=").append(price).append("&payMethod=").append(payMethod).append("&backUrl=")
					.append(notityurl).append("&pageUrl=").append(callbackurl).append("&payDate=").append(time)
					.append("&remark1=").append(goodsname).append("&remark2=").append("&remark3=").append("&signature=")
					.append(sign);

			String reqResult = HttpTool.sendPost(url, param.toString(), "5000");
			logger.info("金海哲sd68请求结果:" + reqResult);
			Map<String, String> payMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {

				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String backsign = jsonobj.getString("sign");
				String backQrCodeUrl = jsonobj.getString("backQrCodeUrl");
				String backOrderId = jsonobj.getString("backOrderId");
				StringBuilder backjson = new StringBuilder();
				backjson.append("{\"backQrCodeUrl\":\"").append(backQrCodeUrl).append("\",\"backOrderId\":\"")
						.append(backOrderId).append("\"}");
				boolean checkresult = RSASignature.verify(backjson.toString(), backsign, ConstantDefine.JHZ_P_KEY);
				if (checkresult) {
					String requrl = jsonobj.getString("backQrCodeUrl");
					String traid = jsonobj.getString("backOrderId");
					payMap.put("url", requrl);
					payMap.put("status", "0");
					payMap.put("orderid", info.getFfId());
					payMap.put("spid", info.getSpId());
					payMap.put("traid", traid);
					result = JSONObject.toJSONString(payMap);
				} else {
					logger.info("金海哲sd68验签失败！");
				}
			}

			logger.info("金海哲sd68生成json:" + result);

		} catch (Exception e) {
			logger.error("金海哲sd68指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS125(OrderReqInfo info) {
		String result = "error";
		try {

			String keyword = info.getKeyword();
			String price = info.getPrice();
			String payCode = getPayCode(info.getFees(), keyword, price, "#");
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			price = info.getPrice().replace("00", "");

			String url = tmp[0];

			String province = info.getProvince();
			province = province.replace("内蒙古", "内蒙").replace("黑龙江", "黑龙").replace("省", "").replace("市", "");
			String pro = provinceMap.get(province);
			if (pro == null || pro.length() <= 0)
				pro = "1";

			String operator = "CMCC";
			int phoneType = info.getPhoneType();// 1移动 2联通 3电信
			if (phoneType == 2) {
				operator = "CUC";
			} else if (phoneType == 3) {
				operator = "CNC";
			}

			String mobile = info.getMobile();

			StringBuilder param = new StringBuilder();
			param.append(tmp[1]).append(payCode).append(info.getFfId()).append("&imei=").append(info.getImei())
					.append("&imsi=").append(info.getImsi()).append("&iccid=").append(info.getIccid())
					.append("&operator=").append(operator).append("&ip=").append(info.getIp()).append("&pro=")
					.append(pro).append("&fee=").append(price).append("&appName=")
					.append(URLEncoder.encode("奶爸当家", "utf-8")).append("&payCode=")
					.append(URLEncoder.encode("超值包", "utf-8"));

			if (mobile != null && mobile.length() > 0) {
				param.append("&phone=").append(mobile);
			}

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "6000");
			logger.info("卓翌yc35请求url=>" + url);
			logger.info("卓翌yc35请求param=>" + param.toString());
			logger.info("卓翌yc35请求结果=>" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String state = jsonobj.getString("result");
				if (state != null && "0".equals(state)) {

					String sms1 = jsonobj.getString("command");
					String port1 = jsonobj.getString("port");
					String type1 = jsonobj.getString("netWorkingType");

					if ("1".equals(type1)) {
						type1 = "0";
					} else if ("2".equals(type1)) {
						type1 = "1";
					}

					Map<String, String> map = new HashMap<String, String>();
					map.put("msg", sms1);
					map.put("returnCode", "0");
					map.put("smsNumber", port1);
					map.put("tradeId", info.getFfId());
					map.put("send_type", type1);
					result = JSONObject.toJSONString(map);
				}
			}
			logger.info("卓翌yc35请求返回生成json数据：" + result);

		} catch (Exception e) {
			logger.error("卓翌yc35指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS124(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}
			param.append("phone=").append(info.getMobile()).append("&cpparam=").append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "4000");
			logger.info("咪咕注册sd66请求结果：" + reqResult);
			if (reqResult == null || reqResult.length() <= 0 || "error".equals(reqResult)) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("status", "2");
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");

				reqResult = JSONObject.toJSONString(resultMap);
			} else {
				Map<String, String> jsonMap = new HashMap<String, String>();
				JSONObject json = JSONObject.parseObject(reqResult);
				jsonMap.put("status", "0");
				jsonMap.put("orderid", info.getFfId());
				jsonMap.put("traid", json.getString("orderid"));

				reqResult = JSONObject.toJSONString(jsonMap);
			}

			result = reqResult;

		} catch (Exception e) {
			logger.error("咪咕注册sd66指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS123(OrderReqInfo info) {
		String result = "error";
		try {

			String requrl = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
//			String app = info.getApp();
//			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();
			String[] tmp = keyword.split("#");

			String appID = tmp[0];
			String appsecret = tmp[1];
//			String notityurl = tmp[2];
			String productNo = tmp[3];
			String payType = tmp[4];
			String backurl = tmp[5];

			if (callbackurl == null || callbackurl.length() <= 0) {
				callbackurl = backurl;
			}

			Map<String, String> payMap = new HashMap<String, String>();
			Map<String, String> jsonparm = new HashMap<String, String>();
			jsonparm.put("orderNo", info.getFfId());
			jsonparm.put("productNo", productNo);
			jsonparm.put("amount", price);
			jsonparm.put("payType", payType);
			jsonparm.put("userpara", info.getSpId());
			jsonparm.put("returnurl", callbackurl);
			Map<String, String> extparm = new HashMap<String, String>();
			extparm.put("clientIP", info.getIp());

			String ext = JSONObject.toJSONString(extparm);
			String json = JSONObject.toJSONString(jsonparm);
			String source = appID + "&" + json + "&" + ext;
			String sign = SHAUtil.getSign(SHAUtil.getHmacSHA1(source, appsecret));

			StringBuilder param = new StringBuilder();
			param.append("appID=").append(appID).append("&json=").append(URLEncoder.encode(json, "utf-8"))
					.append("&ext=").append(URLEncoder.encode(ext, "utf-8")).append("&sign=").append(sign);

			String reqResult = HttpTool.sendGetSetTimeout(requrl, param.toString(), "5000");
			logger.info("我赞第三方sd64请求结果:" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String resultCode = jsonobj.getString("statusCode");
				if (resultCode != null && "200".equals(resultCode)) {
					String url = jsonobj.getString("url");
					/*
					 * String resurl = parseURL(url, info, ""); url = resurl;
					 */
					payMap.put("url", url);
					payMap.put("status", "0");
					payMap.put("orderid", info.getFfId());
					payMap.put("spid", info.getSpId());
				} else if ("607".equals(resultCode)) {
					reqResult = HttpTool.sendGetSetTimeout(requrl, param.toString(), "5000");
					jsonobj = JSONObject.parseObject(reqResult);
					resultCode = jsonobj.getString("statusCode");
					logger.info("我赞第三方sd64第二次请求结果:" + reqResult);
					if (resultCode != null && "200".equals(resultCode)) {
						String url = jsonobj.getString("url");
						/*
						 * String resurl = parseURL(url, info, ""); url = resurl;
						 */
						payMap.put("url", url);
						payMap.put("status", "0");
						payMap.put("orderid", info.getFfId());
						payMap.put("spid", info.getSpId());
					} else if ("607".equals(resultCode)) {
						reqResult = HttpTool.sendGetSetTimeout(requrl, param.toString(), "5000");
						jsonobj = JSONObject.parseObject(reqResult);
						resultCode = jsonobj.getString("statusCode");
						logger.info("我赞第三方sd64第三次请求结果:" + reqResult);
						if (resultCode != null && "200".equals(resultCode)) {
							String url = jsonobj.getString("url");
							/*
							 * String resurl = parseURL(url, info, ""); url = resurl;
							 */
							payMap.put("url", url);
							payMap.put("status", "0");
							payMap.put("orderid", info.getFfId());
							payMap.put("spid", info.getSpId());
						} else {
							payMap.put("status", "1");
							payMap.put("msg", jsonobj.getString("statusInfo"));
						}
					} else {
						payMap.put("status", "1");
						payMap.put("msg", jsonobj.getString("statusInfo"));
					}
				} else {
					payMap.put("status", "1");
					payMap.put("msg", jsonobj.getString("statusInfo"));
				}
			} else {
				payMap.put("status", "-1");
				payMap.put("msg", info.getSpId() + "请求超时");
			}
			result = JSONObject.toJSONString(payMap);
			logger.info("我赞第三方sd64生成json:" + result);

		} catch (Exception e) {
			logger.error("我赞第三方sd64指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS122(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
//			String app = info.getApp();
			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();
			String[] tmp = keyword.split("#");

			BigDecimal b1 = new BigDecimal(price);
			BigDecimal b2 = new BigDecimal(100);
			price = b1.divide(b2).toString();

			String app_id = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String service = tmp[3];
			String backurl = tmp[4];

			String time = DateTool.getNow();
			if (callbackurl == null || callbackurl.length() <= 0) {
				callbackurl = backurl;
			}

			StringBuilder signparam = new StringBuilder();
			signparam.append("app_id=").append(app_id).append("&pay_type=").append(service).append("&order_id=")
					.append(info.getFfId()).append("&order_amt=").append(price).append("&notify_url=").append(notityurl)
					.append("&return_url=").append(callbackurl).append("&time_stamp=").append(time).append("&key=")
					.append(MD5.md5(appsecret, "utf-8"));

			String sign = MD5.md5(signparam.toString(), "utf-8");

			notityurl = URLEncoder.encode(notityurl, "utf-8");
			callbackurl = URLEncoder.encode(callbackurl, "utf-8");
			goodsname = URLEncoder.encode(goodsname, "utf-8");

			StringBuilder param = new StringBuilder();
			param.append("app_id=").append(app_id).append("&pay_type=").append(service).append("&order_id=")
					.append(info.getFfId()).append("&order_amt=").append(price).append("&notify_url=").append(notityurl)
					.append("&return_url=").append(callbackurl).append("&time_stamp=").append(time)
					.append("&goods_name=").append(goodsname).append("&extends=").append(info.getSpId())
					.append("&sign=").append(sign);

			String reqResult = HttpTool.sendPost(url, param.toString(), "5000");
			logger.info("加冕加QQ钱包sd63请求结果:" + reqResult);
			Map<String, String> payMap = new HashMap<String, String>();
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				if ("0".equals(jsonobj.getString("status_code"))) {
					String requrl = jsonobj.getString("pay_url");
					String traid = jsonobj.getString("pay_seq");
					payMap.put("url", requrl);
					payMap.put("status", "0");
					payMap.put("orderid", info.getFfId());
					payMap.put("spid", info.getSpId());
					payMap.put("traid", traid);
					result = JSONObject.toJSONString(payMap);
				}
			}

			logger.info("加冕加QQ钱包sd63生成json:" + result);

		} catch (Exception e) {
			logger.error("加冕加QQ钱包sd63指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS121(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&mobile=").append(info.getMobile()).append("|").append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "3000");
			logger.info("京东注册sd62请求结果：" + reqResult);
			if (reqResult == null || reqResult.length() <= 0) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("status", "2");
				resultMap.put("orderid", info.getFfId());
				resultMap.put("msg", "服务器超时请勿重复提交成功会有同步数据");

				reqResult = JSONObject.toJSONString(resultMap);
			} else {
				Map<String, String> jsonMap = new HashMap<String, String>();

				if ("success".equals(reqResult)) {
					jsonMap.put("status", "0");
					jsonMap.put("orderid", info.getFfId());
					jsonMap.put("msg", reqResult);
				} else {
					jsonMap.put("status", "1");
					jsonMap.put("orderid", info.getFfId());
					jsonMap.put("msg", reqResult);
				}
				reqResult = JSONObject.toJSONString(jsonMap);
			}

			result = reqResult;

		} catch (Exception e) {
			logger.error("京东注册sd62指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS120(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String[] tmp = keyword.split("\\#");
			String sendphone = tmp[0];
			String appsecret = tmp[1];

			String time = DateTool.convertDate(new Date());

			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("SendPhone", sendphone);
			paramMap.put("RecPhone", info.getMobile());
			paramMap.put("Content", info.getCode());
			paramMap.put("time", time);
			paramMap.put("userKey", appsecret);

			String param = JSONObject.toJSONString(paramMap);

			String reqResult = HttpTool.sendPost(url, param, "3000");
			logger.info("12306维新伟联sd61请求结果：" + reqResult);
			if (reqResult == null || reqResult.length() <= 0) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("errNo", 2);
				resultMap.put("return", "false");
				resultMap.put("message", "服务器超时请勿重复提交成功会有同步数据");

				reqResult = JSONObject.toJSONString(resultMap);
			} else {
//				JSONObject jsonobj = JSONObject.parseObject(reqResult);
//				String status = jsonobj.getString("State");

				reqResult = "ok";

			}

			result = reqResult;

		} catch (Exception e) {
			logger.error("12306维新伟联sd61指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS119(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();

			String timestamp = DateTool.getTimestamp(new Date());
			String time = timestamp.substring(0, timestamp.length() - 3);

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&time=").append(time).append("&phone=").append(info.getMobile())
					.append("&msg=").append(info.getCode());

			String reqResult = HttpTool.sendPost(url, param.toString(), "6000");
			logger.info("12306天津创诚sd59请求结果：" + reqResult);
			if (reqResult == null || reqResult.length() <= 0) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("errNo", 2);
				resultMap.put("return", "false");
				resultMap.put("message", "服务器超时请勿重复提交成功会有同步数据");

				reqResult = JSONObject.toJSONString(resultMap);
			} else if ("1".equals(reqResult)) {
				reqResult = "ok";
			}

			result = reqResult;

		} catch (Exception e) {
			logger.error("12306天津创诚sd59指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS118(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&phone=").append(info.getMobile()).append("&cpparam=").append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "8000");
			logger.info("微博解封sd57请求结果：" + reqResult);
			if (reqResult == null || reqResult.length() <= 0) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("errNo", 2);
				resultMap.put("return", "false");
				resultMap.put("message", "服务器超时请勿重复提交成功会有同步数据");

				reqResult = JSONObject.toJSONString(resultMap);
			}

			result = reqResult;

		} catch (Exception e) {
			logger.error("微博解封sd57指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS117(OrderReqInfo info) {
		String result = "error";
		try {

			String requrl = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
//			String app = info.getApp();
//			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();
			String[] tmp = keyword.split("#");

			String appID = tmp[0];
			String appsecret = tmp[1];
//			String notityurl = tmp[2];
			String productNo = tmp[3];
			String payType = tmp[4];
			String backurl = tmp[5];
			String domain = tmp[6];

			if (callbackurl == null || callbackurl.length() <= 0) {
				callbackurl = backurl;
			} else {
				String rescallurl = parseURL(callbackurl, info, "backurl", domain);
				callbackurl = rescallurl;
			}

			Map<String, String> payMap = new HashMap<String, String>();
			Map<String, String> jsonparm = new HashMap<String, String>();
			jsonparm.put("orderNo", info.getFfId());
			jsonparm.put("productNo", productNo);
			jsonparm.put("amount", price);
			jsonparm.put("payType", payType);
			jsonparm.put("userpara", info.getSpId());
			jsonparm.put("returnurl", callbackurl);
			Map<String, String> extparm = new HashMap<String, String>();
			extparm.put("clientIP", info.getIp());

			String ext = JSONObject.toJSONString(extparm);
			String json = JSONObject.toJSONString(jsonparm);
			String source = appID + "&" + json + "&" + ext;
			String sign = SHAUtil.getSign(SHAUtil.getHmacSHA1(source, appsecret));

			StringBuilder param = new StringBuilder();
			param.append("appID=").append(appID).append("&json=").append(URLEncoder.encode(json, "utf-8"))
					.append("&ext=").append(URLEncoder.encode(ext, "utf-8")).append("&sign=").append(sign);
			for (int i = 1; i < 6; i++) {
				String reqResult = HttpTool.sendGetSetTimeout(requrl, param.toString(), "5000");
				logger.info("我赞第三方sd55第" + i + "次请求结果:" + reqResult);
				if (reqResult != null && reqResult.length() > 0) {
					JSONObject jsonobj = JSONObject.parseObject(reqResult);
					String resultCode = jsonobj.getString("statusCode");
					if (resultCode != null && "200".equals(resultCode)) {
						String url = jsonobj.getString("url");
						String resurl = parseURL(url, info, "", domain);
						url = resurl;
						payMap.put("url", url);
						payMap.put("status", "0");
						payMap.put("orderid", info.getFfId());
						payMap.put("spid", info.getSpId());
						break;
					} else if ("607".equals(resultCode)) {

					} else {
						payMap.put("status", "1");
						payMap.put("msg", jsonobj.getString("statusInfo"));
						break;
					}
				}
			}
			result = JSONObject.toJSONString(payMap);
			logger.info("我赞第三方sd55生成json:" + result);

		} catch (Exception e) {
			logger.error("我赞第三方sd55指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS116(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
//			String matchRegex = info.getMatchRegex();
			String app = info.getApp();
			String goodsname = info.getPropname();
			String buyerLogonId = info.getBuyerLogonId();
//			String openid = info.getOpenid();
//			String israw = info.getReqFlag();
//			String callbackurl = info.getCallbackurl();
			String[] tmp = keyword.split("#");
//			String[] tmp1 = matchRegex.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String service = tmp[3];

			/*
			 * String payurl = tmp1[0]; // String gzhappid = tmp1[3]; String gzhappsecret =
			 * tmp1[3]; String gzhurl = tmp1[4];
			 */

			Map<String, String> restmap = null;
			try {
				Map<String, String> parm = new HashMap<String, String>();
				parm.put("service", service);
				parm.put("mch_id", mchid);
				parm.put("out_trade_no", info.getFfId());
				parm.put("body", app + "-" + goodsname);
				parm.put("attach", info.getSpId());
				parm.put("total_fee", price);
				parm.put("mch_create_ip", info.getIp());
				parm.put("notify_url", notityurl); // 微信服务器异步通知支付结果地址 下面的order/notify 方法
				parm.put("nonce_str", PayUtil.getNonceStr());
				parm.put("buyer_logon_id", buyerLogonId);
				/*
				 * parm.put("is_raw", israw); if (callbackurl != null && callbackurl.length() >
				 * 0 && callbackurl.length() < 255) { parm.put("callback_url", callbackurl);
				 * //交易完成后跳转的 URL，需给绝对路径，255 字符内 }
				 */
				parm.put("sign", PayUtil.getSign(parm, appsecret));

				String param = XmlUtil.xmlFormat(parm, false);
				String restxml = HttpTool.sendPost(url, param, "4000");
				logger.info("支付宝扫码sd50请求结果=>" + restxml);
				restmap = XmlUtil.xmlParse(restxml);
			} catch (Exception e) {
				logger.error("支付宝扫码sd50下单失败!");
			}

			Map<String, String> payMap = new HashMap<String, String>();
			if (CollectionUtil.isNotEmpty(restmap) && "0".equals(restmap.get("status"))
					&& "0".equals(restmap.get("result_code"))) {
				String ressign = restmap.get("sign");
				restmap.remove("sign");
				String ressigny = PayUtil.getSign(restmap, appsecret);
				if (ressigny.equals(ressign)) {
					String requrl = restmap.get("code_url");
//					String requrlimg = restmap.get("code_img_url");
					payMap.put("url", requrl);
//					payMap.put("urlimg", requrlimg);
					payMap.put("status", "0");
					payMap.put("orderid", info.getFfId());
					payMap.put("spid", info.getSpId());
					result = JSONObject.toJSONString(payMap);
				}
			}

			logger.info("支付宝扫码sd50生成json:" + result);

		} catch (Exception e) {
			logger.error("支付宝扫码sd50指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS115(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
//			String matchRegex = info.getMatchRegex();
//			String app = info.getApp();
//			String goodsname = info.getPropname();
//			String openid = info.getOpenid();
//			String israw = info.getReqFlag();
			String callbackurl = info.getCallbackurl();
			String[] tmp = keyword.split("#");
//			String[] tmp1 = matchRegex.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String service = tmp[3];
//			String device_info = tmp[4];
//			String mch_app_name = tmp[5];
//			String mch_app_id = tmp[6];

			/*
			 * String payurl = tmp1[0]; // String gzhappid = tmp1[3]; String gzhappsecret =
			 * tmp1[3]; String gzhurl = tmp1[4];
			 */

			Map<String, String> restmap = null;
			try {
				String out_trade_no = info.getSpId() + info.getFfId();
				Map<String, String> parm = new HashMap<String, String>();
				parm.put("service", service);
				parm.put("mch_id", mchid);
				parm.put("out_trade_no", out_trade_no);
				parm.put("body", info.getCpParam());
				parm.put("attach", info.getSpId());
				parm.put("total_fee", price);
				parm.put("mch_create_ip", info.getIp());
//				parm.put("device_info", device_info);
//				parm.put("mch_app_name", mch_app_name);
//				parm.put("mch_app_id", mch_app_id);
				parm.put("notify_url", notityurl); // 微信服务器异步通知支付结果地址 下面的order/notify 方法
				parm.put("nonce_str", PayUtil.getNonceStr());
//				parm.put("is_raw", israw);
				if (callbackurl != null && callbackurl.length() > 0 && callbackurl.length() < 255) {
					parm.put("callback_url", callbackurl); // 交易完成后跳转的 URL，需给绝对路径，255 字符内
				}
				parm.put("sign", PayUtil.getSign(parm, appsecret));

				String param = XmlUtil.xmlFormat(parm, false);

				Map<String, String> reqheadsmap = new HashMap<String, String>();
				reqheadsmap.put("Content-type", "text/xml");

				String restxml = HttpsTool.sendPost(url, param, 6000, reqheadsmap);
				logger.info("微信sdD3请求结果:" + restxml);
				restmap = XmlUtil.xmlParse(restxml);
			} catch (Exception e) {
				logger.error("微信sdD3下单失败!");
			}

			Map<String, String> payMap = new HashMap<String, String>();
			if (CollectionUtil.isNotEmpty(restmap) && "0".equals(restmap.get("status"))
					&& "0".equals(restmap.get("result_code"))) {
				String ressign = restmap.get("sign");
				restmap.remove("sign");
				String ressigny = PayUtil.getSign(restmap, appsecret);
				if (ressigny.equals(ressign)) {
					String requrl = restmap.get("code_url");
					String requrlimg = restmap.get("code_img_url");
					payMap.put("url", requrl);
					payMap.put("urlimg", requrlimg);
					payMap.put("status", "0");
					payMap.put("orderid", info.getFfId());
					payMap.put("spid", info.getSpId());
					result = JSONObject.toJSONString(payMap);
				}
			}

			logger.info("微信sdD3生成json:" + result);

		} catch (Exception e) {
			logger.error("微信sdD3指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS114(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
//			String matchRegex = info.getMatchRegex();
			String app = info.getApp();
			String goodsname = info.getPropname();
//			String openid = info.getOpenid();
//			String israw = info.getReqFlag();
//			String callbackurl = info.getCallbackurl();
			String[] tmp = keyword.split("#");
//			String[] tmp1 = matchRegex.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String service = tmp[3];

			/*
			 * String payurl = tmp1[0]; // String gzhappid = tmp1[3]; String gzhappsecret =
			 * tmp1[3]; String gzhurl = tmp1[4];
			 */

			Map<String, String> restmap = null;
			try {
				Map<String, String> parm = new HashMap<String, String>();
				parm.put("service", service);
				parm.put("mch_id", mchid);
				parm.put("out_trade_no", info.getFfId());
				parm.put("body", app + "-" + goodsname);
				parm.put("attach", info.getSpId());
				parm.put("total_fee", price);
				parm.put("mch_create_ip", info.getIp());
				parm.put("notify_url", notityurl); // 微信服务器异步通知支付结果地址 下面的order/notify 方法
				parm.put("nonce_str", PayUtil.getNonceStr());
				/*
				 * parm.put("is_raw", israw); if (callbackurl != null && callbackurl.length() >
				 * 0 && callbackurl.length() < 255) { parm.put("callback_url", callbackurl);
				 * //交易完成后跳转的 URL，需给绝对路径，255 字符内 }
				 */
				parm.put("sign", PayUtil.getSign(parm, appsecret));

				String param = XmlUtil.xmlFormat(parm, false);
				String restxml = HttpTool.sendPost(url, param, "4000");
				logger.info("支付宝扫码sd50请求结果=>" + restxml);
				restmap = XmlUtil.xmlParse(restxml);
			} catch (Exception e) {
				logger.error("支付宝扫码sd50下单失败!");
			}

			Map<String, String> payMap = new HashMap<String, String>();
			if (CollectionUtil.isNotEmpty(restmap) && "0".equals(restmap.get("status"))
					&& "0".equals(restmap.get("result_code"))) {
				String ressign = restmap.get("sign");
				restmap.remove("sign");
				String ressigny = PayUtil.getSign(restmap, appsecret);
				if (ressigny.equals(ressign)) {
					String requrl = restmap.get("code_url");
//					String requrlimg = restmap.get("code_img_url");
					payMap.put("url", requrl);
//					payMap.put("urlimg", requrlimg);
					payMap.put("status", "0");
					payMap.put("orderid", info.getFfId());
					payMap.put("spid", info.getSpId());
					result = JSONObject.toJSONString(payMap);
				}
			}

			logger.info("支付宝扫码sd50生成json:" + result);

		} catch (Exception e) {
			logger.error("支付宝扫码sd50指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS113(OrderReqInfo info) {
		String result = "error";
		try {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
//			String url = info.getUrl();
			String price = info.getPrice();
			String keyword = info.getKeyword();
			String payCode = getPayCode(info.getFees(), keyword, price, "#");

			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");

			String requrl = tmp[0];
//			String appsecret = tmp[1];
			String comparam = null;
			String commiturl = null;
			int len = tmp.length;
			if (len >= 3) {
				commiturl = tmp[2];
			} else if (len >= 2) {
				comparam = tmp[1];
			}

			StringBuilder param = new StringBuilder();
			param.append("paycode=").append(payCode).append("&imsi=").append(info.getImsi()).append("&cpparam=")
					.append(comparam + info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(requrl, param.toString(), "8000");
			logger.info("烧烤yc33请求结果:" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String resultCode = jsonobj.getString("code");
				if (resultCode != null && "1".equals(resultCode)) {
					JSONObject jsonobj1 = JSONObject.parseObject(jsonobj.getString("result"));
					String sms1 = jsonobj1.getString("command");
					String smsport1 = "1065842232";
					String sendtype1 = "1";
					String sendtype2 = "0";

					sms1 = Base64.encode(sms1.getBytes("utf-8"));

					resinfo.setOrderid(info.getFfId());
					resinfo.setStatus("0");// 成功
					resinfo.setSendtype1(sendtype1);
					resinfo.setSms1(sms1);
					resinfo.setSmsport1(smsport1);
					resinfo.setSendtype2(sendtype2);
					resinfo.setMsg("success");
				} else {
					resinfo.setMsg("failed to obtain sms");
				}
			}
			result = JSONObject.toJSONString(resinfo);
			logger.info("烧烤yc33请求返回生成json数据：" + result);

		} catch (Exception e) {
			logger.error("烧烤yc33指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;
	}

	private static String getOnlineSMS110(OrderReqInfo info) {
		String result = "error";
		try {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
//			String url = info.getUrl();
			String price = info.getPrice();
			String keyword = info.getKeyword();
			String payCode = getPayCode(info.getFees(), keyword, price, "#");
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");

			String requrl = tmp[0];
			String channel = tmp[1];

			StringBuilder param = new StringBuilder();
			param.append("channelid=").append(channel).append("&consumecode=").append(payCode).append("&imsi=")
					.append(info.getImsi()).append("&imei=").append(info.getImei()).append("&ip=").append(info.getIp())
					.append("&iccid=").append(info.getIccid()).append("&cpparam=").append(info.getFfId());
			String mobile = info.getMobile();
			if (mobile != null && mobile.length() > 7) {
				param.append("&tel=").append(info.getMobile());
			}

			String reqResult = HttpTool.sendGetSetTimeout(requrl, param.toString(), "5000");
			logger.info("玉品天成yc31请求结果:" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String resultCode = jsonobj.getString("resultCode");
				if (resultCode != null && "0".equals(resultCode)) {
					String sms1 = jsonobj.getString("sms");
					String smsport1 = jsonobj.getString("accessNo");
					String sendtype1 = jsonobj.getString("sendType");
					String sms2 = jsonobj.getString("sms2");
					String smsport2 = jsonobj.getString("accessNo2");
					String sendtype2 = jsonobj.getString("sendType2");
					if ("0".equals(sendtype1)) {
						sendtype1 = "2";
					}
					if ("0".equals(sendtype2)) {
						sendtype2 = "2";
					}

					resinfo.setOrderid(info.getFfId());
					resinfo.setStatus("0");// 成功
//					resinfo.setIsscreen("1");//需要拦截下行
//					resinfo.setIsreply("0");//无需回复
//					resinfo.setKeyport(tmp[0]);
//					resinfo.setKeyword(URLEncoder.encode(tmp[1], "utf-8"));
					resinfo.setSendtype1(sendtype1);
					resinfo.setSms1(sms1);
					resinfo.setSmsport1(smsport1);
					resinfo.setSendtype2(sendtype2);
					resinfo.setSms2(sms2);
					resinfo.setSmsport2(smsport2);
					resinfo.setDelaytime(info.getDelayed());
					resinfo.setMsg("success");
				} else {
					resinfo.setMsg("failed to obtain sms");
				}
			}
			result = JSONObject.toJSONString(resinfo);
			logger.info("玉品天成yc31请求返回生成json数据：" + result);

		} catch (Exception e) {
			logger.error("玉品天成yc31指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;
	}

	private static String getOnlineSMS109(OrderReqInfo info) {
		String result = "error";
		try {

//			String url = info.getUrl();
//			String price = info.getPrice().replace("00", "");
			String keyword = info.getKeyword();
			String matchRegex = info.getMatchRegex();
			String[] tmp = matchRegex.split("##");

			String requrl = tmp[0];
			String xscommiturl = tmp[1];
			String commiturl = tmp[2];

			Map<String, String> pzmap = CommonTool.parseYZ(keyword);

			String mobile = info.getMobile();
			if (mobile != null && mobile.length() > 7) {

				StringBuilder param = new StringBuilder();
				param.append("a=").append(pzmap.get("a")).append("&tel=").append(info.getMobile()).append("&cpparam=")
						.append(pzmap.get("cid")).append(info.getFfId());

				String reqResult = HttpTool.sendGetSetTimeout(requrl, param.toString(), "5000");
				logger.info("南京贝桥yy09请求结果：" + reqResult);
				if (reqResult != null && reqResult.length() > 0) {
					JSONObject jsonobj = JSONObject.parseObject(reqResult);
					String returnCode = jsonobj.getString("code");
					if ("200000".equals(returnCode)) {
						String sid = jsonobj.getString("sid");
						commiturl = commiturl + "?sid=" + sid + "&verifycode=";
						Map<String, String> map = new HashMap<String, String>();
						map.put("status", "0");
						map.put("orderid", info.getFfId());
						map.put("url", commiturl);
						map.put("requrl", xscommiturl + "?orderid=" + info.getFfId() + "&code=");
						result = JSONObject.toJSONString(map);
					}
				}

			}

		} catch (Exception e) {
			logger.error("南京贝桥yy09指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS108(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String matchRegex = info.getMatchRegex();
			String price = info.getPrice();
			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();
			String[] tmp = matchRegex.split("##");

			BigDecimal b1 = new BigDecimal(price);
			BigDecimal b2 = new BigDecimal(100);
			price = b1.divide(b2).toString();

			String appid = keyword;
			String stickerurl = tmp[0];
//			String appsecret = tmp[1];

			String spid = info.getSpId();
			if ("sd49".equals(spid) || "sd56".equals(spid)) {
				if (goodsname != null && "xh_body".equals(goodsname)) {
					logger.info("微信H5支付sd49请求支付黑名单");
					return result;
				}
			}

			StringBuilder stickerparam = new StringBuilder();
			stickerparam.append("appid=").append(appid);
			String stickerReqResult = HttpTool.sendPost(stickerurl, stickerparam.toString(), "5000");
			logger.info("微信H5支付sd49,sticker请求结果:" + stickerReqResult);
			if (stickerReqResult != null && stickerReqResult.length() > 0) {
				JSONObject stickerjson = JSONObject.parseObject(stickerReqResult);
				if ("0".equals(stickerjson.getString("result"))) {

					String backurl = "http://www.xushihudong.com/backurl.htm";
					if (callbackurl != null && callbackurl.length() > 0) {
						backurl = callbackurl;
					}

					String sticker = stickerjson.getString("sticker");
					Map<String, String> datamap = new HashMap<String, String>();
					datamap.put("attch", info.getFfId());
					datamap.put("gooddesc", goodsname);
					datamap.put("playerId", "playerId");
					datamap.put("price", price);
					datamap.put("appId", appid);
					datamap.put("channel", info.getSpId());
					datamap.put("callback_url", backurl);
					String token = MD5.md5(sticker, "utf-8");
					String data = AESOperator.encrypt(JSONObject.toJSONString(datamap), sticker, sticker);

					StringBuilder param = new StringBuilder();
					param.append("data=").append(data).append("&token=").append(token);

					String reqResult = HttpTool.sendPost(url, param.toString(), "5000");
					logger.info("微信H5支付sd49请求结果:" + reqResult);
					if (reqResult != null && reqResult.length() > 0) {
						reqResult = AESOperator.decrypt(reqResult, sticker, sticker);
						logger.info("微信H5支付sd49请求结果解码:" + reqResult);
						JSONObject jsonobj = JSONObject.parseObject(reqResult);
						if ("0".equals(jsonobj.getString("resultCode"))) {
							String requrl = jsonobj.getString("value");
							String out_trade_no = jsonobj.getString("out_trade_no");
							Map<String, String> payMap = new HashMap<String, String>();
							payMap.put("status", "0");
							payMap.put("traid", out_trade_no);
							payMap.put("orderid", info.getFfId());
							payMap.put("spid", info.getSpId());
							payMap.put("url", requrl);
							result = JSONObject.toJSONString(payMap);
						}
					}
				}
			} else {
				logger.error("微信H5支付sd49获取sticker失败");
			}

			logger.info("微信H5支付sd49生成json:" + result);

		} catch (Exception e) {
			logger.error("微信H5支付sd49指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;

	}

	private static String getOnlineSMS107(OrderReqInfo info) {
		String result = "error";
		try {
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String matchRegex = info.getMatchRegex();
//			String app = info.getApp();
			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();
			String[] tmp = matchRegex.split("##");
			String appsecret = tmp[1];

			DecimalFormat df2 = new DecimalFormat("0.00");
			BigDecimal b1 = new BigDecimal(price);
			BigDecimal b2 = new BigDecimal(100);
			price = df2.format(b1.divide(b2));

			StringBuilder signparam = new StringBuilder();
			signparam.append("order_no=").append(info.getFfId()).append("&order_amt=").append(price).append("&key=")
					.append(appsecret);
			String sign = MD5.md5(signparam.toString(), "utf-8").toLowerCase();

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&order_no=").append(info.getFfId()).append("&order_amt=").append(price)
					.append("&notify_url=").append(URLEncoder.encode(tmp[0], "utf-8")).append("&custom=")
					.append(info.getFfId() + info.getSpId()).append("&sign=").append(sign).append("&goods_name=")
					.append(goodsname);

			if (callbackurl != null && callbackurl.length() > 0) {
				param.append("&return_url=").append(URLEncoder.encode(callbackurl, "utf-8"));
			}

			String reqResult = HttpTool.sendPost(url, param.toString(), "5000");
			logger.info("云支付sd48请求结果:" + reqResult);
			if (reqResult != null && reqResult.length() > 0 && reqResult.startsWith("http")) {
				Map<String, String> resultMap = new HashMap<String, String>();
				resultMap.put("status", "0");
				resultMap.put("url", reqResult);
				resultMap.put("orderid", info.getFfId());
				resultMap.put("spid", info.getSpId());
				result = JSONObject.toJSONString(resultMap);
			}

			logger.info("云支付sd48生成json:" + result);

		} catch (Exception e) {
			logger.error("云支付sd48指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;
	}

	private static String getOnlineSMS106(OrderReqInfo info) {
		String result = "error";
		try {
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String matchRegex = info.getMatchRegex();
			String app = info.getApp();
			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&passInfo=").append(info.getFfId()).append("&price=").append(price)
					.append("&appName=").append(app).append("&prdName=").append(goodsname);

			if (callbackurl != null && callbackurl.length() > 0) {
				param.append("&backUrl=").append(callbackurl);
			}

			String reqResult = HttpTool.sendPost(url, param.toString(), "5000");
			logger.info("竹语sd47请求结果:" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String returnCode = jsonobj.getString("result");
				if ("200".equals(returnCode)) {
					String orderId = jsonobj.getString("orderId");
					String requrl = matchRegex + "?orderId=" + orderId;
					Map<String, String> resultMap = new HashMap<String, String>();
					resultMap.put("status", "0");
					resultMap.put("url", requrl);
					resultMap.put("orderid", info.getFfId());
					resultMap.put("spid", info.getSpId());
					result = JSONObject.toJSONString(resultMap);
				}
			}

			logger.info("竹语sd47生成json:" + result);

		} catch (Exception e) {
			logger.error("语sd47指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;
	}

	private static String getOnlineSMS105(OrderReqInfo info) {
		String result = "error";
		try {
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String payCode = getPayCode(info.getFees(), keyword, price, "#");
			String matchRegex = info.getMatchRegex();
			String[] tmp = matchRegex.split("##");

			StringBuilder param = new StringBuilder();
			param.append(tmp[0]).append(info.getFfId()).append("&imsi=").append(info.getImsi()).append("&imei=")
					.append(info.getImei()).append("&tel=").append(info.getMobile()).append("&vaccode=")
					.append(payCode);
			String sms = HttpTool.sendGetSetTimeout(url, param.toString(), "5000");
			logger.info("从lt09获取信息:" + sms);
			if (sms != null && sms.length() > 0) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("orderid", info.getFfId());
				map.put("status", "0");
				result = JSONObject.toJSONString(map);
			}
		} catch (Exception e) {
			logger.error("从lt09获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	private static String getOnlineSMS104(OrderReqInfo info) {
		String result = "error";
		try {
//			String url = info.getUrl();
//			String keyword = info.getKeyword();
			String price = info.getPrice();
			price = price.replace("00", "");
//			String payCode = getPayCode(info.getFees(), keyword, price, "#");
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");

			StringBuilder param = new StringBuilder();
			param.append("gameNo=1&operator=CMCC").append("&cpid=").append(tmp[1]).append("&fee=").append(price)
					.append("&ip=").append(info.getIp()).append("&phone=").append(info.getMobile()).append("&imsi=")
					.append(info.getImsi()).append("&imei=").append(info.getImei()).append("&cppram=")
					.append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(tmp[0], param.toString(), "8000");
			logger.info("竹语yc29请求结果=>" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String returnCode = jsonobj.getString("result");
				if ("0".equals(returnCode)) {
					String sms1 = jsonobj.getString("command");
					String smsport1 = jsonobj.getString("port");
					String sendtype1 = jsonobj.getString("type");

					if ("1".equals(sendtype1)) {
						sendtype1 = "0";
					} else if ("2".equals(sendtype1)) {
						sendtype1 = "0";
						sms1 = new String(Base64.decode(sms1));
					} else if ("3".equals(sendtype1)) {
						sendtype1 = "1";
					} else if ("4".equals(sendtype1)) {
						sendtype1 = "1";
						sms1 = Base64.encode(sms1.getBytes());
					}

					Map<String, String> map = new HashMap<String, String>();
					map.put("msg", sms1);
					map.put("returnCode", "0");
					map.put("smsNumber", smsport1);
					map.put("tradeId", info.getFfId());
					map.put("send_type", sendtype1);
					result = JSONObject.toJSONString(map);
				}
			}
		} catch (Exception e) {
			logger.error("竹语yc29获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		logger.info("竹语yc29返回结果：" + result);
		return result;
	}

	private static String getOnlineSMS102(OrderReqInfo info) {
		String result = "error";
		try {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String paycode = getPayCode(info.getFees(), keyword, price, "#");
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");

			String iccid = info.getIccid();
			String imsi = info.getImsi();
			String imei = info.getImei();
			String province = info.getProvince();

			if (iccid != null && iccid.length() > 10) {

				String nwtype = info.getNwtype();
				if (nwtype == null || nwtype.length() <= 0)
					nwtype = "WIFI";
				if (!"WIFI".equals(nwtype)) {
					nwtype = "CMWAP";
				}

				String version = "2.0.0";
				String msgID = "0";

//				String regex = "[\\d]{11}";
				if (imsi != null && imsi.length() > 0 && imei != null && imei.length() > 0) {

					Map<String, String> mapparam = new HashMap<String, String>();
					mapparam.put("consumercode", paycode);
					mapparam.put("chid", tmp[1]);
					mapparam.put("imsi", imsi);
					mapparam.put("imei", imei);
					mapparam.put("provinceId", provinceYZWHMap.get(province));
					mapparam.put("iccid", iccid);
					mapparam.put("cpparam", info.getFfId());
					mapparam.put("network", nwtype);
					mapparam.put("passwd", "");
					mapparam.put("clientIp", info.getIp());
					String data = Base64.encode(JSONObject.toJSONString(mapparam).getBytes());

					Map<String, String> paramap = new HashMap<String, String>();
					paramap.put("version", version);
					paramap.put("msgID", msgID);
					paramap.put("data", data);
					String param = JSONObject.toJSONString(paramap);

					String reqResult = HttpTool.sendYZWHPost(tmp[0], param, 18000);
					logger.info("易周文化dm19请求msgID" + msgID + "结果:" + reqResult);
					if (reqResult != null && reqResult.length() > 0) {
						JSONObject jsonobj = JSONObject.parseObject(reqResult);
						String returnCode = jsonobj.getString("error_code");
						if ("0".equals(returnCode)) {
							String guid = jsonobj.getString("guid");
							String sms1 = jsonobj.getString("smsreg");
							String smsport1 = jsonobj.getString("smsregport");
							String sendtype1 = jsonobj.getString("smsregtype");
							String sms2 = jsonobj.getString("smsorder");
							String smsport2 = jsonobj.getString("smsorderport");
							String sendtype2 = jsonobj.getString("smsordertype");
							/*
							 * if (sms1 != null && sms1.length() > 0) { sms1 =
							 * CommonTool.hexStringToStr(sms1); } if (sms2 != null && sms2.length() > 0) {
							 * sms2 = CommonTool.hexStringToStr(sms2); }
							 */
							if ("0".equals(sendtype1)) {
								sendtype1 = "3";
							}
							if ("0".equals(sendtype2)) {
								sendtype2 = "3";
							}

							resinfo.setOrderid(info.getFfId());
							resinfo.setStatus("0");// 成功
							resinfo.setSendtype1(sendtype1);
							resinfo.setSms1(sms1);
							resinfo.setSmsport1(smsport1);
							resinfo.setSendtype2(sendtype2);
							resinfo.setSms2(sms2);
							resinfo.setSmsport2(smsport2);
							resinfo.setMsg("success");

							msgID = "1";
							Map<String, String> mapparam1 = new HashMap<String, String>();
							mapparam1.put("guid", guid);
							String data1 = Base64.encode(JSONObject.toJSONString(mapparam1).getBytes());

							Map<String, String> paramap1 = new HashMap<String, String>();
							paramap1.put("version", version);
							paramap1.put("msgID", msgID);
							paramap1.put("data", data1);
							String param1 = JSONObject.toJSONString(paramap1);
							String reqResult1 = HttpTool.sendYZWHPost(tmp[0], param1, 300);
							logger.info("易周文化dm19请求msgID" + msgID + "结果:" + reqResult1);

							/*
							 * msgID = "3"; Map<String, String> mapparam3 = new HashMap<String, String>();
							 * mapparam3.put("guid", guid); String data3 =
							 * Base64.encode(JSONObject.toJSONString(mapparam3).getBytes());
							 * 
							 * Map<String, String> paramap3 = new HashMap<String, String>();
							 * paramap3.put("version", version); paramap3.put("msgID", msgID);
							 * paramap3.put("data", data1); String param3 =
							 * JSONObject.toJSONString(paramap3); String reqResult3 =
							 * HttpTool.sendYZWHPost(tmp[0], param3, 15000);
							 * logger.info("易周文化dm19请求msgID"+msgID+"结果:"+reqResult3);
							 */

							info.setApp(info.getApp() + "##" + guid);
							result = JSONObject.toJSONString(resinfo);
						} else {
							resinfo.setStatus("1");
							resinfo.setMsg("failed to obtain sms, error num:" + returnCode);
							result = JSONObject.toJSONString(resinfo);
						}
					} else {
						resinfo.setMsg("failed to obtain sms");
					}
				}
			}
		} catch (Exception e) {
			logger.error("易周文化dm19获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	private static String getOnlineSMS98(OrderReqInfo info) {
		String result = "error";
		try {
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String payCode = getPayCode(info.getFees(), keyword, price, "#");
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			String nwtype = info.getNwtype();
			if (!"WIFI".equals(nwtype))
				nwtype = "GPRS";
			String proid = "1";
			String protype = "1";

			StringBuilder param = new StringBuilder();
			param.append(tmp[0]).append("&appFeeId=").append(payCode).append("&fee=").append(price).append("&iccid=")
					.append(info.getIccid()).append("&os_info=").append(info.getOsversion()).append("&os_model=")
					.append(URLEncoder.encode(info.getPmodel(), "utf-8")).append("&net_info=").append(nwtype)
					.append("&client_ip=").append(info.getIp()).append("&proid=").append(proid).append("&protype=")
					.append(protype).append("&imsi=").append(info.getImsi()).append("&imei=").append(info.getImei())
					.append("&extra=").append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "8000");
			logger.info("家医yc28请求结果=>" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String returnCode = jsonobj.getString("resultCode");
				if ("0000".equals(returnCode) && "1".equals(jsonobj.getString("type"))) {
					String sms1 = jsonobj.getString("cmd");
					String smsport1 = jsonobj.getString("port");
					String sendtype1 = jsonobj.getString("smstype");
					String port = jsonobj.getString("portnumber");
					if ("0".equals(sendtype1)) {
						sms1 = new String(Base64.decode(sms1));
					}

					Map<String, String> map = new HashMap<String, String>();
					map.put("msg", sms1);
					map.put("returnCode", "0");
					map.put("smsNumber", smsport1);
					map.put("send_type", sendtype1);
					if (port != null) {
						map.put("port", port);
					}
					result = JSONObject.toJSONString(map);
				}
			}
		} catch (Exception e) {
			logger.error("家医yc28获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		logger.info("家医yc28返回结果：" + result);
		return result;
	}

	private static String getOnlineSMS96(OrderReqInfo info) {
		String result = "error";
		try {
			String url = info.getUrl();
			String keyword = info.getKeyword();
//			String matchRegex = info.getMatchRegex();

			StringBuilder param = new StringBuilder();
			param.append(keyword).append(info.getFfId()).append("&imsi=").append(info.getImsi()).append("&imei=")
					.append(info.getImei()).append("&fee=").append(info.getFee()).append("&phone=")
					.append(info.getMobile()).append("&ip=").append(info.getIp());
			String sms = HttpTool.sendGetSetTimeout(url, param.toString(), "8000");
			logger.info("从lt07获取信息:" + sms);
			if (sms != null && sms.length() > 0) {
				JSONObject json = JSONObject.parseObject(sms);
				String tranid = json.getString("orderid");
				Map<String, String> map = new HashMap<String, String>();
				map.put("orderid", info.getFfId());
				map.put("traid", tranid);
				map.put("status", "0");
				result = JSONObject.toJSONString(map);
			}
		} catch (Exception e) {
			logger.error("从lt07获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	private static String getOnlineSMS95(OrderReqInfo info) {
		try {
			String url = info.getUrl();
			String keyword = info.getKeyword();
			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&imsi=").append(info.getImsi()).append("&imei=").append(info.getImei())
					.append("&iccid=").append(info.getIccid()).append("&fee=").append(info.getFee()).append("&mobile=")
					.append(info.getMobile()).append("&cpparam=").append(info.getFfId()).append("&ip=")
					.append(info.getIp());
			String sms = HttpTool.sendGetSetTimeout(url, param.toString(), "8000");
			logger.info("从yc26获取信息:" + sms);
			if (sms != null && sms.length() > 0 && !sms.equals("error")) {
				return sms;
			}
		} catch (Exception e) {
			logger.error("从yc26获取MM指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return "error";
	}

	private static String getOnlineSMS94(OrderReqInfo info) {
		String result = "error";
		try {

			String price = info.getPrice();
			String url = info.getUrl();
			String keyword = info.getKeyword();
//			String signtype = "RSA";
			String[] tmp = keyword.split("#");
			String merchantno = tmp[0];// 商户编号
			String appno = tmp[1];// 应用编号
			String customerno = tmp[2];// 用户编号
			String sessionurl = tmp[3];// 建立会话接口
			String payurl = tmp[4];// 下单接口
			String notifyurl = tmp[5];// 异步通知地址
//			String callbackurl = info.getCallbackurl();
			DecimalFormat df2 = new DecimalFormat("0.00");
			BigDecimal b1 = new BigDecimal(price);
			BigDecimal b2 = new BigDecimal(100);
			price = df2.format(b1.divide(b2));

//			String merchantno = merchantno;			//商户编号
//			String appno = appno;					//应用编号
			String merchantorder = info.getFfId(); // 商户订单号
			String amount = price; // 交易金额
			String currency = "RMB"; // 币种
			String itemno = info.getCpId(); // 商品编号
			String itemname = info.getPropname(); // 商品名称
			String country = "CN";

			Map<String, String> json = new HashMap<String, String>();
			json.put("appno", appno);
			json.put("merchantno", merchantno);
			json.put("merchantorder", merchantorder);
			json.put("itemname", itemname);
			json.put("amount", amount);
			json.put("currency", currency);
			json.put("notifyurl", notifyurl);
			json.put("customerno", customerno);
			json.put("itemno", itemno);
			json.put("country", country);
			String signparam = JSONObject.toJSONString(json);

//	        String sign = SignHelper.privateKeySign(ConstantDefine.APPV_KEY, signparam);
			String sign = RSA.sign(signparam, ConstantDefine.APPV_KEY, "utf-8");
			String reqData = "transdata=" + URLEncoder.encode(signparam, "utf-8") + "&sign="
					+ URLEncoder.encode(sign, "utf-8") + "&signtype=RSA";// 组装请求参数
			String respData = HttpTool.sendKLWPost(url, reqData, "utf-8", 8000); // 请求验证服务端
			if (respData != null && respData.length() > 0) {
				Map<String, String> reslutMap = SignUtils.getParmters(respData);
				String signtype = reslutMap.get("signtype"); // "RSA";
				String transid = "";
				if (signtype == null) {
					// 验签失败
				} else {
					/*
					 * 调用验签接口
					 *
					 * 主要 目的 确定 收到的数据是我们 发的数据，是没有被非法改动的
					 */
					String respTransdata = URLDecoder.decode(reslutMap.get("transdata"), "utf-8");
					String signData = URLDecoder.decode(reslutMap.get("sign"), "utf-8");
					if (RSA.verify(respTransdata, signData, ConstantDefine.APPP_KEY, "utf-8")) {
						JSONObject obj = JSONObject.parseObject(respTransdata);
						transid = obj.getString("serialno");
						System.out.println("verify ok, transid------>" + transid);
					} else {
						System.out.println("verify fail, transid------>" + transid);
					}
					String sessionId = "";
					String paytype = "2";
					if (transid != null && transid != "") {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("serialno", transid);
						jsonObject.put("paytype", paytype);
						/*
						 * jsonObject.put("lang","CN"); jsonObject.put("cfgversion","100");
						 * jsonObject.put("version","pc_1.0.0");
						 * jsonObject.put("terminalid","1234567890");
						 */
						String content = jsonObject.toString();// 请求参数
						sign = RSA.sign(content, ConstantDefine.APPV_KEY, "utf-8");
						String data = "transdata=" + URLEncoder.encode(content, "utf-8") + "&sign="
								+ URLEncoder.encode(sign, "utf-8") + "&signtype=RSA";// 组装请求参数
//	                    respData = HttpUtils.sentHttpsPost(url, data, "utf-8");
						respData = HttpTool.sendKLWPost(sessionurl, data, "utf-8", 5000);
						reslutMap = SignUtils.getParmters(respData);
						signtype = reslutMap.get("signtype"); // "RSA";
						if (signtype != null) {
							respTransdata = URLDecoder.decode(reslutMap.get("transdata"), "utf-8");
							String signdata = URLDecoder.decode(reslutMap.get("sign"), "utf-8");
							if (RSA.verify(respTransdata, signdata, ConstantDefine.APPP_KEY, "utf-8")) {
								JSONObject jsonobj = JSONObject.parseObject(respTransdata);
								sessionId = jsonobj.getString("sessionid");

								JSONObject jsonObject1 = new JSONObject();
								jsonObject1.put("sessionid", sessionId);
								jsonObject1.put("paytype", paytype);
								content = jsonObject1.toString();
								sign = RSA.sign(content, ConstantDefine.APPV_KEY, "utf-8");
								data = "transdata=" + URLEncoder.encode(content, "utf-8") + "&sign="
										+ URLEncoder.encode(sign, "utf-8") + "&signtype=RSA";// 组装请求参数
								respData = HttpTool.sendKLWPost(payurl, data, "utf-8", 5000);
								reslutMap = SignUtils.getParmters(respData);
								signtype = reslutMap.get("signtype"); // "RSA";
								if (signtype != null) {
									respTransdata = URLDecoder.decode(reslutMap.get("transdata"), "utf-8");
									signdata = URLDecoder.decode(reslutMap.get("sign"), "utf-8");
									if (RSA.verify(respTransdata, signdata, ConstantDefine.APPP_KEY, "utf-8")) {
										JSONObject jsonobj2 = JSONObject.parseObject(respTransdata);
										String orderinfo = jsonobj2.getString("orderinfo");
										JSONObject orderJson = JSONObject.parseObject(orderinfo);
										String orderNo = orderJson.getString("orderno");
										JSONObject resultJson = JSONObject
												.parseObject(jsonobj2.getString("resultinfo"));
										String payParam = resultJson.getString("payparam");
										String payUrl = "";
										if (payParam != null && payParam.length() > 0) {
											payParam = new String(Base64.decode(payParam));
											JSONObject paramJson = JSONObject.parseObject(payParam);
											payUrl = paramJson.getString("url");
										}

										Map<String, String> map = new HashMap<String, String>();
										map.put("url", payUrl);
										map.put("traid", orderNo);
										map.put("orderid", info.getFfId());
										map.put("status", "0");
										map.put("spid", info.getSpId());
										result = JSONObject.toJSONString(map);
									}
								}
							} else {
								System.out.println("verify fail, sessionId------>" + sessionId);
							}
						}
					}
				}
			}

		} catch (Exception e) {
			logger.error("支付宝H5支付sd44指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS93(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String[] tmp = keyword.split("#");
			String merid = tmp[0];
			String appsecret = tmp[1];
			String backurl = tmp[2];

			String price = info.getPrice();
			DecimalFormat df2 = new DecimalFormat("0.00");
			BigDecimal b1 = new BigDecimal(price);
			BigDecimal b2 = new BigDecimal(100);
			price = df2.format(b1.divide(b2));

			String noncestr = PayUtil.getNonceStr();
			String orderTime = DateTool.getNow();

			StringBuilder param = new StringBuilder();
			param.append("merchantOutOrderNo=").append(info.getFfId() + info.getSpId().substring(2, 4))
					.append("&merid=").append(merid).append("&noncestr=").append(noncestr).append("&notifyUrl=")
					.append(backurl).append("&orderMoney=").append(price).append("&orderTime=").append(orderTime);

			StringBuilder signparam = new StringBuilder();
			signparam.append(param).append("&key=").append(appsecret);
			String sign = MD5.md5(signparam.toString(), "utf-8");

			param.append("&sign=").append(sign);

			String payurl = url + "?" + param.toString();
			Map<String, String> map = new HashMap<String, String>();
			map.put("url", payurl);
			map.put("traid", info.getFfId());
			map.put("orderid", info.getFfId());
			map.put("status", "0");
			map.put("spid", info.getSpId());
			result = JSONObject.toJSONString(map);
			/*
			 * String reqResult = HttpTool.sendPost(url, param.toString(),"8000");
			 * logger.info("支付宝H5sd44请求结果=>"+reqResult); if (reqResult != null &&
			 * reqResult.length() > 0) { JSONObject myObj =
			 * JSONObject.parseObject(reqResult); // result = reqResult; }
			 */

		} catch (Exception e) {
			logger.error("支付宝H5sd44指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;
	}

	private static String getOnlineSMS92(OrderReqInfo info) {

		String result = "{\"status\":\"-1\"}";
		try {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String url = info.getUrl();
			String keyword = info.getKeyword();
//			String price = info.getPrice();
//			String payCode = getPayCode(info.getFees(), paycodes, price, "#");
//			String matchregex = info.getMatchRegex();
			String pmodel = info.getPmodel();
			if (pmodel == null || pmodel.length() <= 0)
				pmodel = "GN9010";

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&imsi=").append(info.getImsi()).append("&imei=").append(info.getImei())
					.append("&pmodel=").append(URLEncoder.encode(pmodel, "utf-8")).append("&fee=")
					.append(info.getPrice()).append("&iccid=").append(info.getIccid()).append("&cpparam=")
					.append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "6000");
			logger.info("润土yy08请求结果=>" + reqResult);
			result = reqResult;
			logger.info("润土yy08请求返回生成json数据：" + result);
		} catch (Exception e) {
			logger.error("润土yy08获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	private static String getOnlineSMS91(OrderReqInfo info) {

		String result = "{\"status\":\"-1\"}";
		try {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String url = info.getUrl();
			String keyword = info.getKeyword();
//			String price = info.getPrice();
//			String payCode = getPayCode(info.getFees(), paycodes, price, "#");

			String imsi = info.getImsi();
			int imsilen = imsi.length();
			int count = 6;
			imsi = imsi.substring(0, imsilen - count) + CommonTool.createNum(count);

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&imsi=").append(imsi).append("&imei=").append(info.getImei())
					.append("&clientIp=").append(info.getIp()).append("&phoneNumber=").append(info.getMobile())
					.append("&extData=").append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "6000");
			logger.info("第七dm17请求结果=>" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String returnCode = jsonobj.getString("resultCode");
				if ("0".equals(returnCode)) {
					String paymentCode = jsonobj.getString("paymentCode");
					JSONObject jsonobj1 = JSONObject.parseObject(paymentCode);

					String msg = jsonobj1.getString("smsContent");
					String smsNumber = jsonobj1.getString("spNumber");
					String send_type = jsonobj1.getString("isBase64");
					Map<String, String> map = new HashMap<String, String>();
					map.put("returnCode", returnCode);
					map.put("msg", msg);
					map.put("smsNumber", smsNumber);
					map.put("send_type", send_type);
					map.put("port", "0");
					result = JSONObject.toJSONString(map);
				}
			}
			logger.info("第七dm17请求返回生成json数据：" + result);
		} catch (Exception e) {
			logger.error("第七dm17获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	private static String getOnlineSMS90(OrderReqInfo info) {

		String result = "{\"status\":\"-1\"}";
		try {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String url = info.getUrl();
//			String paycodes = info.getKeyword();
//			String price = info.getPrice();
//			String payCode = getPayCode(info.getFees(), paycodes, price, "#");
			String matchregex = info.getMatchRegex();
			String pmodel = info.getPmodel();
			if (pmodel == null || pmodel.length() <= 0)
				pmodel = "GN9010";
			String location = provinceRTMap.get(info.getProvince());
			if (location == null || location.length() <= 0)
				location = provinceRTMap.get("其他");

			StringBuilder param = new StringBuilder();
			param.append("bizkey=").append(matchregex).append("&imsi=").append(info.getImsi()).append("&imei=")
					.append(info.getImei()).append("&ua=").append(URLEncoder.encode(pmodel, "utf-8"))
					.append("&location=").append(location).append("&param=").append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "6000");
			logger.info("摩宝dm16请求结果=>" + reqResult);
			result = reqResult;
			logger.info("摩宝dm16请求返回生成json数据：" + result);
		} catch (Exception e) {
			logger.error("摩宝dm16获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	private static String getOnlineSMS89(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String payurl = info.getMatchRegex();
			String goodsname = info.getPropname();
			String callbackurl = info.getCallbackurl();
			String[] tmp = keyword.split("#");

			BigDecimal b1 = new BigDecimal(price);
			BigDecimal b2 = new BigDecimal(100);
			price = b1.divide(b2).toString();

			String appid = tmp[0];
			String appsecret = tmp[1];

			StringBuilder para = new StringBuilder();
			para.append("appid=").append(appid).append("&sign=").append(appsecret);
			String reqResult = HttpTool.sendPost(url, para.toString(), "3000");
			logger.info("支付宝H5支付sd34验证请求结果:" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				if ("0".equals(jsonobj.getString("result"))) {
					String sticker = jsonobj.getString("sticker");
					String backurl = "http://www.lewanplay.com/backurl.htm";
					if (callbackurl != null && callbackurl.length() > 0) {
						backurl = callbackurl;
					}
					String token = MD5Tool.toMD5Value32(sticker);
					Map<String, String> parammap = new HashMap<String, String>();
					parammap.put("playerId", "1");
					parammap.put("attch", info.getFfId());
					parammap.put("appId", appid);
					parammap.put("gooddesc", goodsname);
					parammap.put("channel", info.getSpId());
					parammap.put("price", price);
					parammap.put("show_url", backurl);
					String param = JSONObject.toJSONString(parammap);
					logger.info("支付宝H5支付sd34下单请求加密前参数:" + param);
					logger.info("加密token:" + token);
					String data = AESOperator.encrypt(param, sticker, sticker);
					StringBuilder payparam = new StringBuilder();
					payparam.append("data=").append(data).append("&token=").append(token);
					String payReqResult = HttpTool.sendPost(payurl, payparam.toString(), "5000");
					logger.info("支付宝H5支付sd34下单请求结果:" + payReqResult);
					if (payReqResult != null && payReqResult.length() > 0) {
						String jsonstr = AESOperator.decrypt(payReqResult, sticker, sticker);
						logger.info("支付宝H5支付sd34返回数据解密结果:" + jsonstr);
						JSONObject jsonobj1 = JSONObject.parseObject(jsonstr);
						Map<String, String> payMap = new HashMap<String, String>();
						payMap.put("status", "0");
						payMap.put("traid", jsonobj1.getString("out_trade_no"));
						payMap.put("orderid", info.getFfId());
						payMap.put("spid", info.getSpId());
						payMap.put("url", jsonobj1.getString("value"));
						result = JSONObject.toJSONString(payMap);
					}
				}
			}
			logger.info("支付宝H5支付sd34生成json:" + result);

		} catch (Exception e) {
			logger.error("支付宝H5支付sd34指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;

	}

	private static String getOnlineSMS88(OrderReqInfo info) {

		String result = "{\"status\":\"-1\"}";
		try {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String url = info.getUrl();
			String paycodes = info.getKeyword();
			String price = info.getPrice();
			String payCode = getPayCode(info.getFees(), paycodes, price, "#");
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");

			StringBuilder param = new StringBuilder();
			param.append("serviceidup=").append(payCode).append("&imsi=").append(info.getImsi()).append("&imei=")
					.append(info.getImei()).append("&phone=").append(info.getMobile()).append("&ip=")
					.append(info.getIp()).append("&param=").append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "6000");
			logger.info("范范lt06请求url=>" + url);
			logger.info("范范lt06请求param=>" + param.toString());
			logger.info("范范lt06请求结果=>" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String returnCode = jsonobj.getString("code");
				if ("0".equals(returnCode)) {
					String sms1 = jsonobj.getString("sms");
					String smsport1 = jsonobj.getString("port");
					String sendtype1 = "3";
					String sms2 = jsonobj.getString("sms2");
//					String smsport2 = jsonobj.getString("port2");
					String sendtype2 = "0";

					resinfo.setOrderid(info.getFfId());
					resinfo.setStatus("0");// 成功
					resinfo.setIsscreen("1");// 需要拦截下行
					resinfo.setIsreply("1");// 需回复拦截的下行
					resinfo.setReplycontent(sms2);
					resinfo.setKeyport(tmp[0]);
					resinfo.setKeyword(URLEncoder.encode(tmp[1], "utf-8"));
					resinfo.setSendtype1(sendtype1);
					resinfo.setSms1(sms1);
					resinfo.setSmsport1(smsport1);
					resinfo.setDelaytime(info.getDelayed());
					resinfo.setSendtype2(sendtype2);
					/*
					 * resinfo.setSms2(sms2); resinfo.setSmsport2(smsport2);
					 */
					resinfo.setMsg("success");
				}
			} else {
				resinfo.setMsg("failed to obtain sms");
			}
			result = JSONObject.toJSONString(resinfo);
			logger.info("范范lt06请求返回生成json数据：" + result);
		} catch (Exception e) {
			logger.error("范范lt06获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	private static String getOnlineSMS87(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
//			String matchRegex = info.getMatchRegex();
//			String app = info.getApp();
//			String goodsname = info.getPropname();
//			String callbackurl = info.getCallbackurl();
			String[] tmp = keyword.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
			String paycode = tmp[2];
			String order_sn = mchid + info.getFfId() + info.getSpId();

			StringBuilder signparam = new StringBuilder();
			signparam.append(paycode).append("&").append(order_sn).append("&").append(appsecret);
			String sign = MD5Tool.toMD5Value32(signparam.toString());

			StringBuilder param = new StringBuilder();
			param.append("mchid=").append(mchid).append("&feepoint=").append(paycode).append("&fee=").append(price)
					.append("&order_sn=").append(order_sn).append("&sign=").append(sign);
			String reqResult = HttpTool.sendPost(url, param.toString(), "5000");
			logger.info("微信公众号支付sd33请求结果=>" + reqResult);
			if (reqResult != null && reqResult.contains("weixin:")) {
				Map<String, String> payMap = new HashMap<String, String>();
				payMap.put("url", reqResult);
				payMap.put("status", "0");
				payMap.put("orderid", info.getFfId());
				payMap.put("spid", info.getSpId());
				result = JSONObject.toJSONString(payMap);
			} else {
				logger.error("微信公众号支付sd33获取支付连接失败！");
			}
			logger.info("微信公众号支付sd33生成json:" + result);

		} catch (Exception e) {
			logger.error("微信公众号支付sd33指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS86(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
//			String matchRegex = info.getMatchRegex();
			String app = info.getApp();
			String goodsname = info.getPropname();
			String openid = info.getOpenid();
			String israw = info.getReqFlag();
			String callbackurl = info.getCallbackurl();
			String[] tmp = keyword.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];

			Map<String, String> restmap = null;
			try {
				Map<String, String> parm = new HashMap<String, String>();
				parm.put("service", "pay.weixin.wappay");
				parm.put("mch_id", mchid);
				parm.put("out_trade_no", info.getFfId());
				parm.put("body", app + "-" + goodsname);
				parm.put("attach", info.getSpId());
				parm.put("total_fee", price);
				parm.put("mch_create_ip", info.getIp());
				parm.put("notify_url", notityurl); // 微信服务器异步通知支付结果地址 下面的order/notify 方法
				if (callbackurl != null && callbackurl.length() > 0 && callbackurl.length() < 255) {
					parm.put("callback_url", callbackurl); // 交易完成后跳转的 URL，需给绝对路径，255 字符内
				}

				parm.put("sub_openid", openid);
				parm.put("is_raw", israw);
				parm.put("nonce_str", PayUtil.getNonceStr());
				parm.put("sign", PayUtil.getSign(parm, appsecret));

				String param = XmlUtil.xmlFormat(parm, false);
				String restxml = HttpTool.sendPost(url, param, "8000");
				logger.info("微信公众号支付sd28请求结果=>" + restxml);
				restmap = XmlUtil.xmlParse(restxml);
			} catch (Exception e) {
				logger.error("微信公众号支付sd28下单失败!");
			}

			Map<String, String> payMap = new HashMap<String, String>();
			if (CollectionUtil.isNotEmpty(restmap) && "0".equals(restmap.get("status"))
					&& "0".equals(restmap.get("result_code"))) {
				String ressign = restmap.get("sign");
				restmap.remove("sign");
				String ressigny = PayUtil.getSign(restmap, appsecret);
				if (ressigny.equals(ressign)) {
					if ("0".equals(israw)) {
						String requrl = "?showwxpaytitle=1&token_id=" + restmap.get("token_id");
						payMap.put("url", requrl);
					} else if ("1".equals(israw)) {
						payMap.put("payinfo", Base64.encode(restmap.get("pay_info").getBytes()));
					}

					payMap.put("status", "0");
					payMap.put("orderid", info.getFfId());
					payMap.put("spid", info.getSpId());
					result = JSONObject.toJSONString(payMap);
				}
			}

			logger.info("微信公众号支付sd28生成json:" + result);

		} catch (Exception e) {
			logger.error("微信公众号支付sd28指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS85(OrderReqInfo info) {
		String result = "error";
		try {

			String keyword = info.getKeyword();
			String[] tmp = keyword.split("#");

			String url = tmp[9];

			String playerid = info.getImsi();
			String callbackurl = info.getCallbackurl();
			String app = info.getApp();
			String goodsname = info.getPropname();

			StringBuilder param = new StringBuilder();
			param.append("cpid=").append(info.getCpId()).append("&fee=").append(info.getPrice()).append("&ip=")
					.append(info.getIp()).append("&spid=").append(info.getSpId()).append("&cpparam=")
					.append(info.getCpParam());

			if (playerid != null && playerid.length() > 0)
				param.append("&playerid=").append(URLEncoder.encode(playerid, "utf-8"));
			if (callbackurl != null && callbackurl.length() > 0)
				param.append("&callbackurl=").append(URLEncoder.encode(callbackurl, "utf-8"));
			if (app != null && app.length() > 0)
				param.append("&app=").append(URLEncoder.encode(app, "utf-8"));
			if (goodsname != null && goodsname.length() > 0)
				param.append("&goodsname=").append(URLEncoder.encode(goodsname, "utf-8"));

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "4000");
			logger.info("微信H5sd81请求结果:" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String status = jsonobj.getString("status");
				String requtl = jsonobj.getString("url");
				String orderid = jsonobj.getString("orderid");
				if ("0".equals(status)) {
					info.setFfId(orderid);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("url", requtl);
					map.put("orderid", orderid);
					map.put("status", 0);
					map.put("spid", info.getSpId());
					result = JSONObject.toJSONString(map);
				}
			}
			logger.info("微信H5sd81生成json:" + result);

		} catch (Exception e) {
			logger.error("微信H5sd81指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;

	}

	private static String getOnlineSMS84(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String app = info.getApp();
			String goodsname = info.getPropname();
			String openid = info.getOpenid();

			try {
				Map<String, String> parm = CommonTool.parseYZ(keyword);
				parm.put("price", price);
				parm.put("linkId", openid);
				parm.put("bill_title", app);
				parm.put("bill_body", goodsname);

				String desparm = DesUtil.encryptDES(JSONObject.toJSONString(parm));
				String resjson = HttpTool.sendPost(url, desparm, "8000");
				logger.info("香蕉sd29请求url=>" + url);
				logger.info("香蕉sd29请求param=>" + desparm);
				logger.info("香蕉sd29请求结果=>" + resjson);
				Map<String, String> payMap = new HashMap<String, String>();
				if (resjson != null && resjson.length() > 0) {
					JSONObject jsonobj = JSONObject.parseObject(resjson);
					String resultCode = jsonobj.getString("resultCode");
					if ("200".equals(resultCode)) {
						JSONObject jsonorder = JSONObject.parseObject(jsonobj.getString("order"));
						payMap.put("url", jsonorder.getString("pay_link"));
						payMap.put("status", "0");
						payMap.put("orderid", info.getFfId());
						payMap.put("spid", info.getSpId());
						result = JSONObject.toJSONString(payMap);
					}
				}
			} catch (Exception e) {
				logger.error("香蕉公众号支付sd29下单失败!");
			}

			logger.info("香蕉公众号支付sd29生成json:" + result);

		} catch (Exception e) {
			logger.error("香蕉公众号支付sd29指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS83(OrderReqInfo info) {

		String result = "{\"status\":\"-1\"}";
		try {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String payCode = getPayCode(info.getFees(), keyword, price, "#");
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");

			StringBuilder param = new StringBuilder();
			param.append("serviceidup=").append(payCode).append("&imsi=").append(info.getImsi()).append("&imei=")
					.append(info.getImei()).append("&param=").append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "6000");
			logger.info("范范dm12请求url=>" + url);
			logger.info("范范dm12请求param=>" + param.toString());
			logger.info("范范dm12请求结果=>" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String returnCode = jsonobj.getString("code");
				if ("0".equals(returnCode)) {
					String sms1 = jsonobj.getString("sms");
					String smsport1 = jsonobj.getString("port");
					String sendtype1 = "1";

					resinfo.setOrderid(info.getFfId());
					resinfo.setStatus("0");// 成功
					resinfo.setIsscreen("1");// 需要拦截下行
					resinfo.setIsreply("0");// 无需回复拦截的下行
					resinfo.setKeyport(tmp[0]);
					resinfo.setKeyword(URLEncoder.encode(tmp[1], "utf-8"));
					resinfo.setSendtype1(sendtype1);
					resinfo.setSms1(sms1);
					resinfo.setSmsport1(smsport1);
					resinfo.setSendtype2("0");
					resinfo.setMsg("success");
				}
			} else {
				resinfo.setMsg("failed to obtain sms");
			}
			result = JSONObject.toJSONString(resinfo);
			logger.info("范范dm12请求返回生成json数据：" + result);
		} catch (Exception e) {
			logger.error("范范dm12获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	private static String getOnlineSMS82(OrderReqInfo info) {
		String result = "error";
		try {
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String payCode = getPayCode(info.getFees(), keyword, price, "#");
			String requrl = info.getMatchRegex();

			StringBuilder param = new StringBuilder();
			param.append("serviceidup=").append(payCode).append("&imsi=").append(info.getImsi()).append("&imei=")
					.append(info.getImei()).append("&param=").append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(requrl, param.toString(), "6000");
			logger.info("范范dm12请求结果：" + reqResult);

			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String returnCode = jsonobj.getString("code");
				if ("0".equals(returnCode)) {
					String msg = jsonobj.getString("sms");
					String smsNumber = jsonobj.getString("port");
					String commiturl = jsonobj.getString("url");
					String tradeId = info.getFfId();
					String send_type = "1";
					Map<String, String> map = new HashMap<String, String>();
					map.put("returnCode", returnCode);
					map.put("msg", msg);
					map.put("smsNumber", smsNumber);
					map.put("tradeId", tradeId);
					map.put("send_type", send_type);
					map.put("url", commiturl);
					result = JSONObject.toJSONString(map);
				}

			}
		} catch (Exception e) {
			logger.error("获取范范dm12指令失败");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	private static String getOnlineSMS81(OrderReqInfo info) {
		try {
			String province = URLEncoder.encode(info.getProvince(), "utf-8");

			StringBuilder url = new StringBuilder();
			url.append(info.getUrl()).append("imsi=").append(info.getImsi()).append("&imei=").append(info.getImei())
					.append("&price=").append(info.getFee()).append("&ext=").append(info.getFfId()).append("&province=")
					.append(province);
			String sms = HttpTool.sendHttp(url.toString(), "");
			if (sms != null && sms.length() > 0 && !sms.equals("error")) {
				return sms;
			}
		} catch (Exception e) {
			logger.error("从MM获取指令失败，请仔细检查19破解服务器!");
			logger.error(e.getMessage(), e.getCause());
		}
		return "error";
	}

	private static String getOnlineSMS80(OrderReqInfo info) {
		String result = "{\"status\":\"-1\"}";
		try {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			String price = info.getPrice().replace("00", "");

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&imsi=").append(info.getImsi()).append("&imei=").append(info.getImei())
					.append("&iccid=").append(info.getIccid()).append("&price=").append(price).append("&tel=")
					.append(info.getMobile()).append("&extparams=").append(info.getFfId()).append("&ip=")
					.append(info.getIp());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "8000");
			logger.info("乐米MMyc23请求url=>" + url);
			logger.info("乐米MMyc23请求param=>" + param.toString());
			logger.info("乐米MMyc23请求结果=>" + reqResult);
			resinfo.setOrderid(info.getFfId());
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String state = jsonobj.getString("state");
				if (state != null && "0".equals(state)) {

					String sms1 = jsonobj.getString("cmd1");
					String port1 = jsonobj.getString("up1");
					String type1 = jsonobj.getString("type1");
					resinfo.setStatus("0");// 成功
					resinfo.setIsscreen("1");// 需要拦截下行
					resinfo.setIsreply("0");// 无需回复
					resinfo.setKeyport(tmp[0]);
					resinfo.setKeyword(URLEncoder.encode(tmp[1], "utf-8"));

					if ("4".equals(type1)) {
						type1 = "1";
					} else if ("3".equals(type1)) {
						type1 = "3";
						sms1 = new String(Base64.decode(sms1));
					} else if ("2".equals(type1)) {
						type1 = "1";
						sms1 = Base64.encode(sms1.getBytes());
					} else if ("1".equals(type1)) {
						type1 = "3";
					} else {
						type1 = "0";
					}
					resinfo.setSendtype1(type1);
					if (!"0".equals(type1)) {
						resinfo.setSms1(sms1);
						resinfo.setSmsport1(port1);
					}

					resinfo.setMsg("success");
					resinfo.setDelaytime(info.getDelayed());
				}
			} else {
				resinfo.setMsg("timeout or error");// 请求超时或错误
			}
			result = JSONObject.toJSONString(resinfo);
			logger.info("yc23请求返回生成json数据：" + result);

		} catch (Exception e) {
			logger.error("乐米MMyc23指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS79(OrderReqInfo info) {

		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String matchRegex = info.getMatchRegex();
			String app = info.getApp();
			String goodsname = info.getPropname();
//			String openid = info.getOpenid();
			String israw = info.getReqFlag();
			String callbackurl = info.getCallbackurl();
			String[] tmp = keyword.split("#");
			String[] tmp1 = matchRegex.split("#");

			String mchid = tmp[0];
			String appsecret = tmp[1];
			String notityurl = tmp[2];
			String subappid = tmp[3];

			String payurl = tmp1[0];
//			String gzhappid = tmp1[3];
			String gzhappsecret = tmp1[3];
			String gzhurl = tmp1[4];
			StringBuilder gzhparam = new StringBuilder();
			gzhparam.append("appid=").append(subappid).append("&secret=").append(gzhappsecret).append("&code=")
					.append(info.getCode()).append("&grant_type=authorization_code");
			String gzhreqresult = HttpTool.sendGetSetTimeout(gzhurl, gzhparam.toString(), "3000");
			logger.info("微信公众号支付sd31gzhreqresult请求结果=>" + gzhreqresult);
			JSONObject gzhjson = JSONObject.parseObject(gzhreqresult);
			String openid = gzhjson.getString("openid");
			if (openid != null && openid.length() > 0) {

				Map<String, String> restmap = null;
				try {
					Map<String, String> parm = new HashMap<String, String>();
					parm.put("service", "pay.weixin.jspay");
					parm.put("mch_id", mchid);
					parm.put("out_trade_no", info.getFfId());
					parm.put("body", app + "-" + goodsname);
					parm.put("sub_openid", openid);
//				parm.put("sub_mch_id", mchid);
					parm.put("sub_appid", subappid);
					parm.put("attach", info.getSpId());
					parm.put("total_fee", price);
					parm.put("mch_create_ip", info.getIp());
					parm.put("notify_url", notityurl); // 微信服务器异步通知支付结果地址 下面的order/notify 方法
					parm.put("nonce_str", PayUtil.getNonceStr());
					parm.put("is_raw", israw);
					if (callbackurl != null && callbackurl.length() > 0 && callbackurl.length() < 255) {
						parm.put("callback_url", callbackurl); // 交易完成后跳转的 URL，需给绝对路径，255 字符内
					}
					parm.put("sign", PayUtil.getSign(parm, appsecret));

					String param = XmlUtil.xmlFormat(parm, false);
					String restxml = HttpTool.sendPost(url, param, "8000");
					logger.info("微信公众号支付sd31请求结果=>" + restxml);
					restmap = XmlUtil.xmlParse(restxml);
				} catch (Exception e) {
					logger.error("微信公众号支付sd31下单失败!");
				}

				Map<String, String> payMap = new HashMap<String, String>();
				if (CollectionUtil.isNotEmpty(restmap) && "0".equals(restmap.get("status"))
						&& "0".equals(restmap.get("result_code"))) {
					String ressign = restmap.get("sign");
					restmap.remove("sign");
					String ressigny = PayUtil.getSign(restmap, appsecret);
					if (ressigny.equals(ressign)) {
						String requrl = payurl + "?token_id=" + restmap.get("token_id");
//						result = parseHTML(requrl,info);
						if ("0".equals(israw)) {
							payMap.put("url", requrl);
						} else if ("1".equals(israw)) {
							payMap.put("payinfo", Base64.encode(restmap.get("pay_info").getBytes()));
						}

						payMap.put("status", "0");
						payMap.put("orderid", info.getFfId());
						payMap.put("spid", info.getSpId());
						result = JSONObject.toJSONString(payMap);
					}
				}
			}

			logger.info("微信公众号支付sd31生成json:" + result);

		} catch (Exception e) {
			logger.error("微信公众号支付sd31指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS78(OrderReqInfo info) {
		String result = "{\"status\":\"-1\"}";
		try {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			String appname = info.getApp();
			String propname = info.getPropname();
			if (appname != null && appname.length() > 0) {
				appname = URLDecoder.decode(appname, "utf-8");
			} else {
				appname = URLDecoder.decode("虚实游戏", "utf-8");
			}
			if (propname != null && propname.length() > 0) {
				propname = URLDecoder.decode(propname, "utf-8");
			} else {
				propname = URLDecoder.decode("虚实道具", "utf-8");
			}

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&imsi=").append(info.getImsi()).append("&mobile=").append(info.getMobile())
					.append("&ip=").append(info.getIp()).append("&price=").append(info.getFee()).append("&cpparam=")
					.append(info.getFfId()).append("&appname=").append(appname).append("&propname=").append(propname);
			String sms = HttpTool.sendGetSetTimeout(url, param.toString(), "5000");
			if (sms != null && sms.length() > 0) {
				Map<String, String> map = XmlUtil.xmlParsebytag(sms, "response");
				String status = map.get("returncode");
				if ("0".equals(status)) {
					String sms1 = map.get("smstext");
					String smsport1 = map.get("sendport");
					String sendtype1 = "3";
					String replycontent = map.get("vcode");

					resinfo.setOrderid(info.getFfId());
					resinfo.setStatus("0");// 成功
					resinfo.setIsscreen("1");// 需要拦截下行
					resinfo.setIsreply("0");// 不需要回复拦截的下行
					resinfo.setDelaytime(info.getDelayed());
//					resinfo.setReplycontent(URLEncoder.encode(replycontent, "utf-8"));
					resinfo.setKeyport(tmp[0]);
					resinfo.setKeyword(URLEncoder.encode(tmp[1], "utf-8"));
					resinfo.setSendtype1(sendtype1);
					resinfo.setSms1(sms1);
					resinfo.setSmsport1(smsport1);
					resinfo.setSendtype2(sendtype1);
					resinfo.setSms2(URLEncoder.encode(replycontent, "utf-8"));
					resinfo.setSmsport2(smsport1);
//					resinfo.setSendtype2("0");
					resinfo.setMsg("success");
				}
			} else {
				resinfo.setMsg("failed to obtain sms");
			}
			result = JSONObject.toJSONString(resinfo);
			logger.info("lt05请求返回生成json数据：" + result);
		} catch (Exception e) {
			logger.error("lt05获取指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	private static String getOnlineSMS77(OrderReqInfo info) {
		String result = "error";
		try {
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice().replace("00", "");

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&imsi=").append(info.getImsi()).append("&imei=").append(info.getImei())
					.append("&iccid=").append(info.getIccid()).append("&price=").append(price).append("&tel=")
					.append(info.getMobile()).append("&extparams=").append(info.getFfId()).append("&ip=")
					.append(info.getIp());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "8000");
			logger.info("苏州乐米dm10请求url=>" + url);
			logger.info("苏州乐米dm10请求param=>" + param.toString());
			logger.info("苏州乐米dm10请求结果=>" + reqResult);

			if (reqResult != null && reqResult.length() > 0) {
				result = reqResult;
			} else {
				result = "请求超时";
			}
		} catch (Exception e) {
			logger.error("获取苏州乐米dm10指令失败");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	private static String getOnlineSMS76(OrderReqInfo info) {
		String result = "{\"status\":\"-1\"}";
		try {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			String price = info.getPrice().replace("00", "");

			String province = info.getProvince();
			province = province.replace("内蒙古", "内蒙").replace("黑龙江", "黑龙").replace("省", "").replace("市", "");
			String pro = provinceMap.get(province);
			if (pro == null || pro.length() <= 0)
				pro = "1";

			String operator = "CMCC";
			int phoneType = info.getPhoneType();// 1移动 2联通 3电信
			if (phoneType == 2) {
				operator = "CUC";
			} else if (phoneType == 3) {
				operator = "CNC";
			}

			String imsi = info.getImsi();
			/*
			 * int imsilen = imsi.length(); int count = 6; imsi = imsi.substring(0,
			 * imsilen-count) + CommonTool.createNum(count);
			 */

			String mobile = info.getMobile();

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&imei=").append(info.getImei()).append("&imsi=").append(imsi)
					.append("&iccid=").append(info.getIccid()).append("&operator=").append(operator).append("&ip=")
					.append(info.getIp()).append("&pro=").append(pro).append("&fee=").append(price).append("&appName=")
					.append(URLEncoder.encode("奶爸当家", "utf-8")).append("&payCode=")
					.append(URLEncoder.encode("超值包", "utf-8")).append("&info1=").append(info.getFfId());

			if (mobile != null && mobile.length() == 11) {
				param.append("&phone=").append(mobile);
			}

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "6000");
			logger.info("苏州全真dm09请求url=>" + url);
			logger.info("苏州全真dm09请求param=>" + param.toString());
			logger.info("苏州全真dm09请求结果=>" + reqResult);
			resinfo.setOrderid(info.getFfId());
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String state = jsonobj.getString("result");
				if (state != null && "0".equals(state)) {

					String sms1 = jsonobj.getString("command");
					String port1 = jsonobj.getString("port");
					String type1 = jsonobj.getString("netWorkingType");
					String sms2 = jsonobj.getString("command1");
					String port2 = jsonobj.getString("port1");
					String type2 = jsonobj.getString("netWorkingType1");
					resinfo.setStatus("0");// 成功
					resinfo.setIsscreen("1");// 需要拦截下行
					resinfo.setIsreply("0");// 无需回复
					resinfo.setKeyport(tmp[0]);
					resinfo.setKeyword(URLEncoder.encode(tmp[1], "utf-8"));

					if ("1".equals(type1)) {
						type1 = "3";
					}
					resinfo.setSendtype1(type1);
					if (!"0".equals(type1)) {
						resinfo.setSms1(sms1);
						resinfo.setSmsport1(port1);
					}

					if (type2 != null && type2.length() > 0) {
						if ("1".equals(type2)) {
							type2 = "3";
						}
						resinfo.setSendtype2(type2);
						if (!"0".equals(type2)) {
							resinfo.setSms2(sms2);
							resinfo.setSmsport2(port2);
						}
					}

					resinfo.setMsg("success");
					resinfo.setDelaytime(info.getDelayed());
				}
			} else {
				resinfo.setMsg("timeout or error");// 请求超时或错误
			}
			result = JSONObject.toJSONString(resinfo);
			logger.info("dm09请求返回生成json数据：" + result);

		} catch (Exception e) {
			logger.error("苏州全真dm09指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS75(OrderReqInfo info) {
		String sms = "error";
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", "0");
		map.put("orderid", info.getFfId());
		map.put("spid", info.getSpId());
		sms = JSONObject.toJSONString(map);
		return sms;
	}

	private static String getOnlineSMS74(OrderReqInfo info) {
		String result = "{\"status\":\"-1\"}";
		try {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");
			String price = info.getPrice().replace("00", "");

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&imsi=").append(info.getImsi()).append("&imei=").append(info.getImei())
					.append("&iccid=").append(info.getIccid()).append("&price=").append(price).append("&tel=")
					.append(info.getMobile()).append("&extparams=").append(info.getFfId()).append("&ip=")
					.append(info.getIp());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "8000");
			logger.info("乐米咪咕游戏基地dm08请求url=>" + url);
			logger.info("乐米咪咕游戏基地dm08请求param=>" + param.toString());
			logger.info("乐米咪咕游戏基地dm08请求结果=>" + reqResult);
			resinfo.setOrderid(info.getFfId());
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String state = jsonobj.getString("state");
				if (state != null && "0".equals(state)) {

					String sms1 = jsonobj.getString("cmd1");
					String port1 = jsonobj.getString("up1");
					String type1 = jsonobj.getString("type1");
					String sms2 = jsonobj.getString("cmd2");
					String port2 = jsonobj.getString("up2");
					String type2 = jsonobj.getString("type2");
					resinfo.setStatus("0");// 成功
					resinfo.setIsscreen("1");// 需要拦截下行
					resinfo.setIsreply("0");// 无需回复
					resinfo.setKeyport(tmp[0]);
					resinfo.setKeyword(URLEncoder.encode(tmp[1], "utf-8"));

					if ("4".equals(type1)) {
						type1 = "1";
					} else if ("3".equals(type1)) {
						type1 = "3";
						sms1 = new String(Base64.decode(sms1));
					} else if ("2".equals(type1)) {
						type1 = "1";
						sms1 = Base64.encode(sms1.getBytes());
					} else if ("1".equals(type1)) {
						type1 = "3";
					} else {
						type1 = "0";
					}
					resinfo.setSendtype1(type1);
					if (!"0".equals(type1)) {
						resinfo.setSms1(sms1);
						resinfo.setSmsport1(port1);
					}

					if ("4".equals(type2)) {
						type2 = "1";
					} else if ("3".equals(type2)) {
						type2 = "3";
						sms2 = new String(Base64.decode(sms2));
					} else if ("2".equals(type2)) {
						type2 = "1";
						sms2 = Base64.encode(sms2.getBytes());
					} else if ("1".equals(type2)) {
						type2 = "3";
					} else {
						type2 = "0";
					}
					resinfo.setSendtype2(type2);
					if (!"0".equals(type2)) {
						resinfo.setSms2(sms2);
						resinfo.setSmsport2(port2);
					}

					resinfo.setMsg("success");
					resinfo.setDelaytime(info.getDelayed());
				}
			} else {
				resinfo.setMsg("timeout or error");// 请求超时或错误
			}
			result = JSONObject.toJSONString(resinfo);
			logger.info("dm08请求返回生成json数据：" + result);

		} catch (Exception e) {
			logger.error("乐米咪咕游戏基地dm08指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS73(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
//			String matchRegex = info.getMatchRegex();
			String app = info.getApp();
			String goodsname = info.getPropname();
			String[] tmp = keyword.split("#");

			String appid = tmp[0];
			String mchid = tmp[1];
			String appsecret = tmp[2];

			Map<String, String> restmap = null;
			boolean flag = true; // 是否订单创建成功
			try {
				Map<String, String> parm = new HashMap<String, String>();
				parm.put("appid", appid);
				parm.put("mch_id", mchid);
				parm.put("device_info", "WEB");
				parm.put("nonce_str", PayUtil.getNonceStr());
				parm.put("body", app + "-" + goodsname);
				parm.put("attach", "支付");
				parm.put("out_trade_no", info.getFfId());
				parm.put("total_fee", price);
				parm.put("spbill_create_ip", info.getIp());
				parm.put("notify_url", "http://120.24.88.90/fee/feewxappback"); // 微信服务器异步通知支付结果地址 下面的order/notify 方法
				parm.put("trade_type", "APP");
				parm.put("sign", PayUtil.getSign(parm, appsecret));

				String param = XmlUtil.xmlFormat(parm, false);
				String restxml = HttpTool.sendPost(url, param, "5000");
				logger.info("微信支付sd26请求结果=>" + restxml);
				restmap = XmlUtil.xmlParse(restxml);
			} catch (Exception e) {
				logger.error("微信支付sd26下单失败!");
			}

			Map<String, String> payMap = new HashMap<String, String>();
			if (CollectionUtil.isNotEmpty(restmap) && "SUCCESS".equals(restmap.get("result_code"))) {
				payMap.put("appid", appid);
				payMap.put("partnerid", mchid);
				payMap.put("prepayid", restmap.get("prepay_id"));
				payMap.put("package", "Sign=WXPay");
				payMap.put("noncestr", PayUtil.getNonceStr());
				payMap.put("timestamp", PayUtil.payTimestamp());

				try {
					payMap.put("sign", PayUtil.getSign(payMap, appsecret));
					payMap.put("status", "0");
					payMap.put("orderid", info.getFfId());
					payMap.put("spid", info.getSpId());
				} catch (Exception e) {
					flag = false;
				}
			}

			if (flag) {
				result = JSONObject.toJSONString(payMap);
			}
			logger.info("微信支付sd26生成json:" + result);

		} catch (Exception e) {
			logger.error("微信支付sd26指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS72(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
//			String matchRegex = info.getMatchRegex();

			price = price.replace("00", "");
			String mobileType = "YD";
			int phoneType = info.getPhoneType();
			if (phoneType == 2) {
				mobileType = "LT";
			} else if (phoneType == 3) {
				mobileType = "DX";
			}

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&MobileType=").append(mobileType).append("&Fee=").append(price)
					.append("&PayType=SMS&ServiceType=BY&Mobile=").append(info.getMobile()).append("&CPParam=")
					.append(info.getFfId()).append("&IMSI=").append(info.getImsi()).append("&IMEI=")
					.append(info.getImei()).append("&AppName=").append(info.getApp()).append("&ClientIP=")
					.append(info.getIp());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "1000");
			logger.info("啊呦喂ec13请求url=>" + url);
			logger.info("啊呦喂ec13请求param=>" + param.toString());
			logger.info("啊呦喂ec13请求结果=>" + reqResult);

			result = reqResult;

		} catch (Exception e) {
			logger.error("啊呦喂ec13指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS71(OrderReqInfo info) {
		String result = "error";
		try {
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice().replace("00", "");

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&imsi=").append(info.getImsi()).append("&imei=").append(info.getImei())
					.append("&iccid=").append(info.getIccid()).append("&price=").append(price).append("&tel=")
					.append(info.getMobile()).append("&extparams=").append(info.getFfId()).append("&ip=")
					.append(info.getIp());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "5000");
			logger.info("苏州乐米yc21请求结果:" + reqResult);

			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String state = jsonobj.getString("state");
				if ("0".equals(state)) {
					String up1 = jsonobj.getString("up1");
					String cmd1 = jsonobj.getString("cmd1");
					String type1 = jsonobj.getString("type1");
					String orderid = jsonobj.getString("orderid");
//					String down = jsonobj.getString("down");
//					String downkey = jsonobj.getString("downkey");
					String sendtype = "0";
					if ("4".equals(type1)) {
						sendtype = "1";
					} else if ("3".equals(type1)) {
						cmd1 = new String(Base64.decode(cmd1));
					} else if ("2".equals(type1)) {
						sendtype = "1";
						cmd1 = Base64.encode(cmd1.getBytes());
					}
					Map<String, String> map = new HashMap<String, String>();
					map.put("msg", cmd1);// 短信内容
					map.put("returnCode", state);// 返回结果
					map.put("smsNumber", up1);// 端口号
					map.put("tradeId", orderid);// 订单号
					map.put("send_type", sendtype);//
					result = JSONObject.toJSONString(map);
				}
			}
		} catch (Exception e) {
			logger.error("获取苏州乐米yc21指令失败");
			logger.error(e.getMessage(), e.getCause());
		}
		logger.info("苏州乐米yc21指令结果:" + result);
		return result;
	}

	private static String getOnlineSMS70(OrderReqInfo info) {
		String result = "{\"status\":\"-1\"}";
		try {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String pmodel = URLEncoder.encode(info.getPmodel(), "utf-8");
			String province = URLEncoder.encode(info.getProvince(), "utf-8");
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");

			StringBuilder url = new StringBuilder();
			url.append(info.getUrl()).append("imsi=").append(info.getImsi()).append("&imei=").append(info.getImei())
					.append("&iccid=").append(info.getIccid()).append("&fee=").append(info.getFee()).append("&pmodel=")
					.append(pmodel).append("&osversion=").append(info.getOsversion()).append("&nwtype=")
					.append(info.getNwtype()).append("&ffid=").append(info.getFfId()).append("&count=")
					.append(info.getCount()).append("&province=").append(province);
			String sms = HttpTool.sendHttp(url.toString(), "");
			if (sms != null && sms.length() > 0 && !sms.equals("error")) {
				JSONObject jsonobj = JSONObject.parseObject(sms);
				String sms1 = jsonobj.getString("msg");
				String smsport1 = jsonobj.getString("smsNumber");
				String sendtype = jsonobj.getString("send_type");
				if ("0".equals(sendtype))
					sendtype = "3";

				resinfo.setOrderid(info.getFfId());
				resinfo.setStatus("0");// 成功
				resinfo.setIsscreen("1");// 需要拦截下行
				resinfo.setIsreply("0");// 无需回复
				resinfo.setKeyport(tmp[0]);
				resinfo.setKeyword(URLEncoder.encode(tmp[1], "utf-8"));
				resinfo.setSendtype1(sendtype);
				resinfo.setSms1(sms1);
				resinfo.setSmsport1(smsport1);
				resinfo.setSendtype2("0");
				resinfo.setMsg("success");
			} else {
				resinfo.setMsg("failed to obtain sms");
			}
			result = JSONObject.toJSONString(resinfo);
			logger.info("yc20自家MM请求返回生成json数据：" + result);
		} catch (Exception e) {
			logger.error("从MM获取指令失败，请仔细检查19破解服务器!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	private static String getOnlineSMS69(OrderReqInfo info) {
		String result = "{\"status\":\"-1\"}";
		try {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");

			String price = info.getPrice();
			String paycodes = info.getKeyword();
			String payCode = getPayCode(info.getFees(), paycodes, price, "#p#");

			String smsport = info.getUrl();
			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");

			resinfo.setOrderid(info.getFfId());
			resinfo.setStatus("0");// 成功
			resinfo.setIsscreen("1");// 需要拦截下行
			resinfo.setIsreply("0");// 无需回复
			resinfo.setKeyport(tmp[0]);
			resinfo.setKeyword(URLEncoder.encode(tmp[1], "utf-8"));
			resinfo.setSendtype1("3");
			resinfo.setSms1(payCode);
			resinfo.setSmsport1(smsport);
			resinfo.setSendtype2("0");
			resinfo.setMsg("success");
			resinfo.setDelaytime(info.getDelayed());
			result = JSONObject.toJSONString(resinfo);
			logger.info("yc19电盈请求返回生成json数据：" + result);

		} catch (Exception e) {
			logger.error("yc19电盈指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;
	}

	private static String getOnlineSMS68(OrderReqInfo info) {
		String result = "error";
		try {
			String url = info.getUrl();
			String keyword = info.getKeyword();
//			String matchregex = info.getMatchRegex();
//			String[] tmp = matchregex.split("##");

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&price=").append(info.getPrice()).append("&imsi=").append(info.getImsi())
					.append("&imei=").append(info.getImei()).append("&ip=").append(info.getIp()).append("&iccid=")
					.append(info.getIccid()).append("&cpparam=").append(info.getFfId());
			String mobile = info.getMobile();
			if (mobile != null && mobile.length() > 0) {
				param.append("&phone=").append(info.getMobile());
			}

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "8000");
			logger.info("上海弘龙yc18请求结果=>" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String resultCode = jsonobj.getString("resultCode");
				if (resultCode != null && "0".equals(resultCode)) {
					String sms1 = jsonobj.getString("sms1");
					String port1 = jsonobj.getString("port1");
					String type1 = jsonobj.getString("type1");
					if ("0".equals(type1)) {
						sms1 = new String(Base64.decode(sms1));
					}

					Map<String, String> map = new HashMap<String, String>();
					map.put("msg", sms1);
					map.put("returnCode", "0");
					map.put("smsNumber", port1);
					if ("1065890010".equals(port1)) {
						map.put("port", "80");
					} else {
						map.put("port", "0");
					}
					map.put("send_type", type1);
					result = JSONObject.toJSONString(map);

				}
			}
			logger.info("yc18请求返回生成json数据：" + result);

		} catch (Exception e) {
			logger.error("上海弘龙yc18指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;
	}

	private static String getOnlineSMS67(OrderReqInfo info) {
		String result = "{\"status\":\"-1\"}";
		try {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");

			String url = info.getUrl();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&price=").append(info.getPrice()).append("&imsi=").append(info.getImsi())
					.append("&imei=").append(info.getImei()).append("&user_ip=").append(info.getIp())
					.append("&cp_param=").append(info.getFfId());
			String mobile = info.getMobile();
			if (mobile != null && mobile.length() > 0) {
				param.append("&smsc=").append(info.getMobile());
			}

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "8000");
			logger.info("上海池乐视频dm06请求url=>" + url);
			logger.info("上海池乐视频dm06请求param=>" + param.toString());
			logger.info("上海池乐视频dm06请求结果=>" + reqResult);
			resinfo.setOrderid(info.getFfId());
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String sms1 = jsonobj.getString("sms1");
				String sms2 = jsonobj.getString("sms2");
				JSONObject jsonsms1 = JSONObject.parseObject(sms1);
				JSONObject jsonsms2 = JSONObject.parseObject(sms2);
				resinfo.setStatus("0");// 成功
				resinfo.setIsscreen("1");// 需要拦截下行
				resinfo.setIsreply("0");// 无需回复
				resinfo.setKeyport("10086");
				String creemword = "CTV手机视频#北方网#搜狐影音专区#搜狐教育#华谊央视专区#G客#索尼美剧专区" + "#乐视手机视频#视听江西#歌华手机电视#新华视讯#包月#退订#中国移动";
				resinfo.setKeyword(URLEncoder.encode(creemword, "utf-8"));
				resinfo.setSendtype1("2");
				resinfo.setSms1(jsonsms1.getString("sms"));
				resinfo.setSmsport1(jsonsms1.getString("serviceno"));
				resinfo.setSendtype2("2");
				resinfo.setSms2(jsonsms2.getString("sms"));
				resinfo.setSmsport2(jsonsms2.getString("serviceno"));
				resinfo.setMsg("success");
				resinfo.setDelaytime(info.getDelayed());
			} else {
				resinfo.setMsg("timeout or error");// 请求超时或错误
			}
			result = JSONObject.toJSONString(resinfo);
			logger.info("dm06请求返回生成json数据：" + result);

		} catch (Exception e) {
			logger.error("上海池乐视频dm06指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;
	}

	private static String getOnlineSMS66(OrderReqInfo info) {
		String result = "error";
		try {
			String price = info.getPrice();
			String paycodes = info.getKeyword();
			String payCode = getPayCode(info.getFees(), paycodes, price);
			String mobile = info.getMobile();
			String imsi = info.getImsi();
			String imei = info.getImei();
			String ffId = info.getFfId();
			String ip = info.getIp();
			String reqNum = info.getNum();

			String url = info.getUrl();
			String key = "6nV6B2tn";
			String channelId = "57";
			String mac = null;

			StringBuilder md5Param = new StringBuilder();
			StringBuilder param = new StringBuilder();
			if ("2".equals(reqNum)) {
				md5Param.append(channelId).append(info.getOrderId()).append(info.getCode()).append(key);
				mac = MD5Tool.toMD5Value32(md5Param.toString()).toUpperCase();
				url = info.getMatchRegex();
				param.append("channelId=").append(channelId).append("&recordId=").append(info.getOrderId())
						.append("&vcode=").append(info.getCode()).append("&mac=").append(mac);
				String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "8000");
				logger.info("朗天电信包月dx09请求url2=>" + url);
				logger.info("朗天电信包月dx09请求param2=>" + param.toString());
				logger.info("朗天电信包月dx09请求结果=>" + reqResult);
				result = "ok";
			} else if ("1".equals(reqNum)) {
				md5Param.append(payCode).append(mobile).append(imsi).append(imei).append(channelId).append(key);
				mac = MD5Tool.toMD5Value32(md5Param.toString()).toUpperCase();
				param.append("channelId=").append(channelId).append("&imsi=").append(imsi).append("&payCode=")
						.append(payCode).append("&ip=").append(ip).append("&extra=").append(ffId).append("&phone=")
						.append(mobile).append("&imei=").append(imei).append("&mac=").append(mac);
				String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "8000");
				logger.info("朗天电信包月dx09请求url1=>" + url);
				logger.info("朗天电信包月dx09请求param1=>" + param.toString());
				logger.info("朗天电信包月dx09请求结果=>" + reqResult);
				JSONObject resultObj = JSONObject.parseObject(reqResult);
				String resultCode = resultObj.getString("resultCode");
				if ("00".equals(resultCode)) {
					String recordId = resultObj.getString("recordId");
					Map<String, String> map = new HashMap<String, String>();
					map.put("traid", ffId);
					map.put("orderid", recordId);
					map.put("status", "0");
					result = JSONObject.toJSONString(map);
				}
			}

			logger.info(param.toString());

		} catch (Exception e) {
			logger.error("从朗天电信获取电信指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	private static String getOnlineSMS65(OrderReqInfo info) {
		String result = "{\"state\": \"1\"}";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String matchRegex = info.getMatchRegex();

			StringBuilder param = new StringBuilder();
			param.append("&imsi=").append(info.getImsi()).append("&mobile=").append(info.getMobile()).append("&")
					.append(keyword).append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "10000");
			logger.info("深圳点之行yy06请求url=>" + url);
			logger.info("深圳点之行yy06请求param=>" + param.toString());
			logger.info("深圳点之行yy06请求结果=>" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String resul = jsonobj.getString("result");
				if ("true".equals(resul)) {
					String orderno = jsonobj.getString("orderno");
					String resurl = matchRegex + "?cp=423&orderid=" + orderno + "&smscode=[key]";
					Map<String, String> map = new HashMap<String, String>();
					map.put("orderid", orderno);
					map.put("state", "true");
					map.put("url", resurl);
					map.put("down", "10086");
					map.put("downkey", "数媒");
					result = JSONObject.toJSONString(map);
				}
			}

		} catch (Exception e) {
			logger.error("深圳点之行yy06指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS64(OrderReqInfo info) {
		String result = "{\"result\": \"false\"}";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String matchRegex = info.getMatchRegex();

			StringBuilder param = new StringBuilder();
			param.append("&imsi=").append(info.getImsi()).append("&mobile=").append(info.getMobile()).append("&")
					.append(keyword).append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "10000");
			logger.info("深圳点之行yy06请求url=>" + url);
			logger.info("深圳点之行yy06请求param=>" + param.toString());
			logger.info("深圳点之行yy06请求结果=>" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String resul = jsonobj.getString("result");
				if ("true".equals(resul)) {
					String orderno = jsonobj.getString("orderno");
					String resurl = matchRegex + "?cp=423&orderid=" + orderno + "&smscode=";
					Map<String, String> map = new HashMap<String, String>();
					map.put("result", "true");
					map.put("url", resurl);
					result = JSONObject.toJSONString(map);
				}
			}

		} catch (Exception e) {
			logger.error("深圳点之行yy06指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS63(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}
			param.append("phone=").append(info.getMobile()).append("&cpparam=").append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "3000");
			logger.info("微信注册sd25请求结果：" + reqResult);
			result = reqResult;

		} catch (Exception e) {
			logger.error("微信注册sd25指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS62(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&code=").append(info.getCode()).append("&phone=").append(info.getMobile())
					.append("&cpparam=").append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "200");
			logger.info("12306注册sd24请求url=>" + url);
			logger.info("12306注册sd24请求param=>" + param.toString());
			logger.info("12306注册sd24请求结果=>" + reqResult);
			result = "ok";

		} catch (Exception e) {
			logger.error("12306注册sd24指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS61(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String smscontent = info.getSmscontent();

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&username=").append(info.getUsername()).append("&password=")
					.append(info.getPassword()).append("&phone=").append(info.getMobile()).append("&cpparam=")
					.append(info.getFfId());
			if (smscontent != null && smscontent.length() > 0) {
				param.append("&smscontent=").append(URLEncoder.encode(smscontent, "utf-8"));
			}

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "500");
			logger.info("QQ注册sd23请求结果=>" + reqResult);
			result = "ok";

		} catch (Exception e) {
			logger.error("QQ注册sd23指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS60(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}
			param.append("mobile=").append(info.getMobile());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "3000");
			logger.info("sd21请求结果:" + reqResult);

			Map<String, String> resMap = new HashMap<String, String>();
			if (reqResult != null) {
				resMap.put("status", "0");
				resMap.put("orderid", info.getFfId());
				resMap.put("msg", "success");
				result = JSONObject.toJSONString(resMap);
			} else {
				resMap.put("status", "1");
				resMap.put("orderid", info.getFfId());
				resMap.put("msg", "request time out");
				result = JSONObject.toJSONString(resMap);
			}

		} catch (Exception e) {
			logger.error("sd21指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS59(OrderReqInfo info) {
		String result = "error";
		try {

			String keyword = info.getKeyword();
			String[] tmp = keyword.split("#");

			String url = tmp[3];

			StringBuilder param = new StringBuilder();
			param.append("cpid=").append(info.getCpId()).append("&orderid=").append(info.getFfId());
			Map<String, String> jsonMap = new HashMap<String, String>();
			jsonMap.put("status", "0");
			jsonMap.put("orderid", info.getFfId());
			jsonMap.put("url", url + "?" + param.toString());
			result = JSONObject.toJSONString(jsonMap);

		} catch (Exception e) {
			logger.error("sd20指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS58(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String reqNum = info.getNum();

			StringBuilder param = new StringBuilder();
			param.append("gn=tj&cpparam=").append(info.getFfId());
			if ("1".equals(reqNum)) {
				url = info.getKeyword();
				param.append("&hm=").append(info.getMobile() + "-" + info.getCode());
			} else {
				param.append("&hm=").append(info.getMobile());
			}

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "1000");
			logger.info("sd19请求url=>" + url);
			logger.info("sd19请求param=>" + param.toString());
			logger.info("sd19请求结果=>" + reqResult);

			if (reqResult != null) {
				if ("0".equals(reqNum)) {
					Map<String, String> jsonMap = new HashMap<String, String>();
					if ("tj_hm_ok".equals(reqResult)) {
						jsonMap.put("status", "0");
						jsonMap.put("orderid", info.getFfId());
						jsonMap.put("traid", info.getFfId());
						reqResult = JSONObject.toJSONString(jsonMap);
					} else {
						reqResult = "error";
					}
				} else {
					if ("tj_hm_ok".equals(reqResult)) {
						reqResult = "ok";
					} else {
						reqResult = "error";
					}
				}

			} else {
				reqResult = "error";
			}

			result = reqResult;

		} catch (Exception e) {
			logger.error("sd19指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS57(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}
			param.append("tel=").append(info.getMobile()).append("&ext=").append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "3000");
			logger.info("sd18请求结果:" + reqResult);

			Map<String, String> resMap = new HashMap<String, String>();
			if (reqResult != null) {
				resMap.put("status", "0");
				resMap.put("orderid", info.getFfId());
				resMap.put("msg", "success");
				result = JSONObject.toJSONString(resMap);
			} else {
				resMap.put("status", "1");
				resMap.put("orderid", info.getFfId());
				resMap.put("msg", "request time out");
				result = JSONObject.toJSONString(resMap);
			}

		} catch (Exception e) {
			logger.error("sd18指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS56(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			param.append("qid=").append(keyword).append("&imsi=").append(info.getImsi()).append("&imei=")
					.append(info.getImei()).append("&cp_param=").append(info.getCpParam());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "1000");
			logger.info("动漫10元包月dm05请求url=>" + url);
			logger.info("动漫10元包月dm05请求param=>" + param.toString());
			logger.info("动漫10元包月dm05请求结果=>" + reqResult);

			RTPayResJson resultjson = new RTPayResJson();
			if (reqResult == null || reqResult.length() <= 0) {
				resultjson.setStatus("999");
				resultjson.setMsg("服务器超时");
			} else {
				JSONObject jsonObj = JSONObject.parseObject(reqResult);
				String status = jsonObj.getString("status");
				if ("0".equals(status)) {
					String data = jsonObj.getString("data");
					JSONObject jsonObj1 = JSONObject.parseObject(data);
					String smsport1 = jsonObj1.getString("sms1");
					String sms1 = jsonObj1.getString("msgtxtenc");
					String sendtype1 = "1";
					String smsport2 = jsonObj1.getString("sms2");
					String sms2 = jsonObj1.getString("msgtxtenc2");
					String sendtype2 = "1";
					resultjson.setStatus(ConstantDefine.RES_SUCCESS);
					resultjson.setMsg(ConstantDefine.RES_SUCCESS_MSG);
					resultjson.setSendtype1(sendtype1);
					resultjson.setSendtype2(sendtype2);
					resultjson.setSms1(sms1);
					resultjson.setSms2(sms2);
					resultjson.setSmsport1(smsport1);
					resultjson.setSmsport2(smsport2);
				} else {
					resultjson.setStatus(ConstantDefine.ERROR_9006);
					resultjson.setMsg(ConstantDefine.ERROR_9006_MSG);
				}
			}

			result = JSONObject.toJSONString(resultjson);

		} catch (Exception e) {
			logger.error("动漫10元包月dm05指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS55(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}
			param.append("phone=").append(info.getMobile());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "3000");
			logger.info("sd17请求结果:" + reqResult);

			Map<String, String> resMap = new HashMap<String, String>();
			if (reqResult != null) {
				resMap.put("status", "0");
				resMap.put("orderid", info.getFfId());
				resMap.put("msg", "success");
				result = JSONObject.toJSONString(resMap);
			} else {
				resMap.put("status", "1");
				resMap.put("orderid", info.getFfId());
				resMap.put("msg", "request time out");
				result = JSONObject.toJSONString(resMap);
			}

		} catch (Exception e) {
			logger.error("sd17指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS54(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			param.append("sp=").append(keyword).append("&od=").append(info.getFfId()).append("&ext=")
					.append(info.getFfId()).append("&tel=").append(info.getMobile()).append("&iccid=")
					.append(info.getIccid()).append("&imei=").append(info.getImei()).append("&imsi=")
					.append(info.getImsi()).append("&clientip=").append(info.getIp()).append("&price=")
					.append(info.getFee()).append("&monthly=1");

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "1000");
			logger.info("杭州龙焰LT04请求url=>" + url);
			logger.info("杭州龙焰LT04请求param=>" + param.toString());
			logger.info("杭州龙焰LT04请求结果=>" + reqResult);

			JSONObject jsonObj = JSONObject.parseObject(reqResult);
			String status = jsonObj.getString("status");
			String orderid = jsonObj.getString("orderid");
			Map<String, String> jsonMap = new HashMap<String, String>();
			jsonMap.put("status", status);
			jsonMap.put("orderid", orderid);
			jsonMap.put("traid", info.getFfId());
			reqResult = JSONObject.toJSONString(jsonMap);

			result = reqResult;

		} catch (Exception e) {
			logger.error("杭州龙焰LT04指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS53(OrderReqInfo info) {
		String result = "error";
		try {
			String smscontent = info.getSmscontent();
			String username = info.getUsername();
			String password = info.getPassword();
			Map<String, String> map = new HashMap<String, String>();
			if (username == null || username.length() <= 0 || password == null || password.length() <= 0) {
				if (smscontent != null && smscontent.length() > 0) {

					String[] tmp = smscontent.split("。已开通密保");
					if (tmp.length != 2) {
						tmp = smscontent.split("。本手机号");
					}
					if (tmp.length == 2) {
						String[] tmp1 = tmp[0].split(",密码");
						if (tmp1.length == 2) {
							String[] tmp2 = tmp1[0].split("QQ号");
							if (tmp2.length == 2) {
								username = tmp2[1];
								password = tmp1[1];
							}
						}
					}

					if (username == null || username.length() <= 0) {
						String[] ntmp = smscontent.split(",密码");
						if (ntmp.length == 2) {
							String[] ntmp1 = ntmp[0].split("QQ号");
							if (ntmp1.length == 2) {
								username = ntmp1[1];
								password = ntmp[1].substring(0, ntmp[1].indexOf("。"));
							}
						}
					}
				}
				map.put("username", username);
				map.put("password", password);
				result = JSONObject.toJSONString(map);
			} else {
				result = "ok";
			}

		} catch (Exception e) {
			logger.error("文本QQ注册指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	/*
	 * public static void main(String[] args) { String username= null; String
	 * password= null; String smscontent=
	 * "您获得QQ号1573512084,密码ctn42872。欢迎使用QQ等腾讯业务。【腾讯科技】"; String[] ntmp =
	 * smscontent.split(",密码"); if (ntmp.length == 2) { String[] ntmp1 =
	 * ntmp[0].split("QQ号"); if (ntmp1.length == 2) { username = ntmp1[1]; password
	 * = ntmp[1].substring(0,ntmp[1].indexOf("。")); } }
	 * System.out.println(username); System.out.println(password); }
	 */
	private static String getOnlineSMS52(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String smscontent = info.getSmscontent();

			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}
			param.append("cpparam=").append(info.getFfId()).append("&phone=").append(info.getMobile());

			if (smscontent != null && smscontent.length() > 0) {
				param.append("&smsContent=").append(URLEncoder.encode(smscontent, "utf-8"));
			}

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "3000");
			logger.info("微信注册请求结果:" + reqResult);

			if (reqResult != null && !"error".equals(reqResult)) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String status = jsonobj.getString("status");
				if ("0".equals(status)) {
					result = "ok";
				} else {
					result = reqResult;
				}
			} else {
				result = reqResult;
			}

		} catch (Exception e) {
			logger.error("微信注册指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS51(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getPrice();
			String[] tmp = keyword.split("#");
			String mchid = tmp[0];
			String paytyep = tmp[1];
			String servicetype = tmp[2];
			String entype = tmp[3];

			price = price.replace("00", "");
			String mobileType = "YD";
			int phoneType = info.getPhoneType();
			if (phoneType == 2) {
				mobileType = "LT";
			} else if (phoneType == 3) {
				mobileType = "DX";
			}

			StringBuilder param = new StringBuilder();
			param.append("MchId=").append(mchid).append("&MobileType=").append(mobileType).append("&Fee=").append(price)
					.append("&PayType=").append(paytyep).append("&ServiceType=").append(servicetype).append("&Mobile=")
					.append(info.getMobile()).append("&CPParam=").append(info.getFfId()).append("&IMSI=")
					.append(info.getImsi()).append("&IMEI=").append(info.getImei()).append("&ICCID=")
					.append(info.getIccid()).append("&AppName=").append(info.getApp()).append("&ClientIP=")
					.append(info.getIp()).append("&EnType=").append(entype);

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "1000");
			logger.info("啊呦喂请求url=>" + url);
			logger.info("啊呦喂请求param=>" + param.toString());
			logger.info("啊呦喂请求结果=>" + reqResult);

			result = reqResult;

		} catch (Exception e) {
			logger.error("啊呦喂指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS50(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}
			param.append("imei=").append(info.getImei()).append("&imsi=").append(info.getImsi()).append("&PHGH1=")
					.append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "4000");
			logger.info("法治视界ec10请求结果:" + reqResult);
			result = reqResult;

		} catch (Exception e) {
			logger.error("法治视界ec10指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS49(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			if (keyword != null && keyword.length() > 0) {
				param.append(keyword);
			}

			param.append(info.getFfId()).append("&phone=").append(info.getMobile());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "2000");
			logger.info("sd08请求结果:" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String status = jsonobj.getString("code");
				Map<String, String> jsonMap = new HashMap<String, String>();
				if ("0".equals(status)) {
					String traid = jsonobj.getString("orderId");
					jsonMap.put("status", "0");
					jsonMap.put("orderid", info.getFfId());
					jsonMap.put("traid", traid);
				} else {
					String msg = jsonobj.getString("message");
					jsonMap.put("status", status);
					jsonMap.put("msg", msg);
				}
				reqResult = JSONObject.toJSONString(jsonMap);
				result = reqResult;
			}

		} catch (Exception e) {
			logger.error("sd08指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS48(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
//			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			param.append("phone=").append(info.getMobile()).append("&cpparam=").append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "3000");
			if (reqResult != null && reqResult.length() > 0 && !"error".equals(reqResult)) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String status = jsonobj.getString("status");
				Map<String, String> jsonMap = new HashMap<String, String>();
				if ("0".equals(status)) {
//					String traid = jsonobj.getString("orderid");
					jsonMap.put("status", "0");
					jsonMap.put("orderid", info.getFfId());
//					jsonMap.put("traid", traid);
				} else {
//					String msg = jsonobj.getString("message");
					jsonMap.put("status", status);
//					jsonMap.put("msg", msg);
				}
				result = JSONObject.toJSONString(jsonMap);
			}
			logger.info("QQ注册sd07请求结果：" + reqResult);

		} catch (Exception e) {
			logger.error("QQ注册sd07指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS47(OrderReqInfo info) {
		String result = "error";
		try {

			String cpid = info.getKeyword();
			String gameNo = info.getMatchRegex();
			String url = info.getUrl();
			String price = info.getFee() + "";
			price = price.replace("00", "");
			String province = info.getProvince();
			province = province.replace("内蒙古", "内蒙").replace("黑龙江", "黑龙").replace("省", "").replace("市", "");
			String pro = provinceMap.get(province);
			if (pro == null || pro.length() <= 0)
				pro = "1";

			StringBuilder param = new StringBuilder();
			param.append("imei=").append(info.getImei()).append("&imsi=").append(info.getImsi())
					.append("&operator=CNC&ip=").append(info.getIp()).append("&cpid=").append(cpid).append("&pro=")
					.append(pro).append("&fee=").append(price).append("&appName=")
					.append(URLEncoder.encode("奶爸当家", "utf-8")).append("&payCode=")
					.append(URLEncoder.encode("超值包", "utf-8")).append("&gameNo=").append(gameNo).append("&info1=")
					.append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "1000");
			logger.info("苏州宝象15元包月dx08请求url=>" + url);
			logger.info("苏州宝象15元包月dx08请求param=>" + param.toString());
			logger.info("苏州宝象15元包月dx08请求结果=>" + reqResult);
			result = reqResult;

		} catch (Exception e) {
			logger.error("苏州宝象15元包月dx08指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS46(OrderReqInfo info) {
		String result = "error";
		try {

//			String url = info.getUrl();
			String price = info.getPrice().replace("00", "");
			String keyword = info.getKeyword();
			String matchRegex = info.getMatchRegex();
			String[] tmp = matchRegex.split("##");

			String requrl = tmp[0];
			String xscommiturl = tmp[1];

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&imsi=").append(info.getImsi()).append("&tel=").append(info.getMobile())
					.append("&price=").append(price).append("&extparams=").append(info.getFfId()).append("&imei=")
					.append(info.getImei()).append("&iccid=").append(info.getIccid()).append("&ip=")
					.append(info.getIp()).append("&model=").append(info.getPmodel()).append("&lac=")
					.append(info.getBscLac()).append("&cid=").append(info.getBscCid());

			String reqResult = HttpTool.sendGetSetTimeout(requrl, param.toString(), "15000");
			logger.info("苏州乐麟yy05请求结果=>" + reqResult);
			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(reqResult);
				String returnCode = jsonobj.getString("state");
				if ("0".equals(returnCode)) {
					String commiturl = jsonobj.getString("url");
					Map<String, String> map = new HashMap<String, String>();
					map.put("status", "0");
					map.put("orderid", info.getFfId());
					map.put("url", new String(commiturl.getBytes("ISO8859-1"), "utf-8"));
					map.put("requrl", xscommiturl + "?orderid=" + info.getFfId() + "&code=");
					result = JSONObject.toJSONString(map);
				}
			}

		} catch (Exception e) {
			logger.error("苏州乐麟yy05指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;

	}

	private static String getOnlineSMS45(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&vcode=").append(info.getCode()).append("&telphone=").append(info.getMobile())
					.append("&spid=").append(info.getFfId());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "2000");
			logger.info("12306注册sd06请求结果:" + reqResult);
			result = "ok";

		} catch (Exception e) {
			logger.error("12306注册sd06指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;
	}

	private static String getOnlineSMS44(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();
			String fees = info.getFees();
			String paycodes = info.getKeyword();
			String commitUrl = info.getMatchRegex();
			String price = info.getFee() + "";
			String appId = info.getMsg();
			String paycode = "";
			String ffId = info.getFfId();

			String[] feeArr = fees.split("#");
			String[] paycodeArr = paycodes.split("#");
			for (int i = 0; i < feeArr.length; i++) {
				if (price.equals(feeArr[i]))
					paycode = paycodeArr[i];
			}

			StringBuilder param = new StringBuilder();
			param.append("cpid=shaojie&amount=").append(price).append("&imsi=").append(info.getImsi()).append("&imei=")
					.append(info.getImei()).append("&cpparam=").append(ffId).append("&channelid=").append(ffId)
					.append("&appid=").append(appId).append("&feeid=").append(paycode);

			String reqResult = HttpTool.sendGet(url, param.toString());
			logger.info("北京翼联dx04请求url=>" + url);
			logger.info("北京翼联dx04请求结果=>" + reqResult);

			JSONObject myObj = JSONObject.parseObject(reqResult);
			String resultCode = myObj.getString("ResultCode");
			if ("0".equals(resultCode)) {
				String orderId = myObj.getString("OrderId");
				Map<String, Object> resMap = new HashMap<String, Object>();
				resMap.put("ResultCode", 0);
				resMap.put("orderid", orderId);
				resMap.put("url", commitUrl);
				result = JSONObject.toJSONString(resMap);
			} else {
				result = reqResult;
			}

		} catch (Exception e) {
			logger.error("北京翼联dx04指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;
	}

	private static String getOnlineSMS43(OrderReqInfo info) {
		String result = "error";
		try {
			String price = info.getFee() + "";
			String fees = info.getFees();

			String smsNumbers = info.getUrl();
			String smscontents = info.getKeyword();
			String[] priceArr = fees.split("#");
			String[] smsNumberArr = smsNumbers.split("#");
			String[] smscontentArr = smscontents.split("#");

			String smsNumber = smsNumberArr[0];
			String smscontent = smscontentArr[0];
			for (int i = 0; i < priceArr.length; i++) {
				if (price.equals(priceArr[i])) {
					smsNumber = smsNumberArr[i];
					smscontent = smscontentArr[i];
					break;
				}
			}

			Map<String, String> resMap = new HashMap<String, String>();
			resMap.put("smsNumber", smsNumber);
			resMap.put("smscontent", smscontent + info.getFfId());
			resMap.put("code", "0");
			result = JSONObject.toJSONString(resMap);
		} catch (Exception e) {
			logger.error("北京翼联5元包月dx03指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;
	}

	private static String getOnlineSMS42(OrderReqInfo info) {
		String result = "error";
		try {

			Map<String, String> resMap = PrePay.reqPrePay(info);
			if (resMap != null && resMap.size() > 0) {
				result = JSONObject.toJSONString(resMap);
			}

		} catch (Exception e) {
			logger.error("高汇通sd05指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;
	}

	private static String getOnlineSMS38(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();

			StringBuilder param = new StringBuilder();
			param.append("appid=000889&imei=").append(info.getImei()).append("&sc=").append(info.getMobile())
					.append("&price=").append(info.getFee() / 100).append("&sc=").append(info.getMobile())
					.append("&imsi=").append(info.getImsi()).append("&extparams=").append(info.getFfId())
					.append("&iccid=").append(info.getIccid()).append("&ip=").append(info.getIp());

//			String reqResult = HttpTool.sendGet(url, param.toString());
			String reqResult = HttpTool.sendHttp2(url, param.toString());
			logger.info("苏州乐米yc14请求url=>" + url);
			logger.info("苏州乐米yc14请求结果=>" + reqResult);

			result = reqResult;

		} catch (Exception e) {
			logger.error("苏州乐米yc14指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;
	}

	private static String getOnlineSMS37(OrderReqInfo info) {
		String result = "error";
		try {

			String url = info.getUrl();

			StringBuilder param = new StringBuilder();
			param.append("appid=ZF0054&type=1&payid=").append(info.getPrice()).append("&nextfee=")
					.append(info.getPrice()).append("&productId=").append(info.getPck()).append("&productName=")
					.append(info.getApp()).append("&imei=").append(info.getImei()).append("&imsi=")
					.append(info.getImsi()).append("&mobile=").append(info.getMobile()).append("&iccid=")
					.append(info.getIccid()).append("&ip=").append(info.getIp()).append("&extra=")
					.append(info.getFfId());

//			String reqResult = HttpTool.sendGet(url, param.toString());
			String reqResult = HttpTool.sendHttp(url + "?" + param.toString(), "");
			logger.info("支付宝支付yc13请求url=>" + url);
			logger.info("支付宝支付yc13请求结果=>" + reqResult);

			result = reqResult;

		} catch (Exception e) {
			logger.error("支付宝支付yc13指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;
	}

	private static String getOnlineSMS36(OrderReqInfo info) {
		String result = "error";
		try {

			String sp = "szklw2";
			String od = info.getFfId();
			String ext = info.getFfId();
			String iccid = info.getIccid();
			String imei = info.getImei();
			String imsi = info.getImsi();
			String clientip = info.getIp();
			String price = info.getPrice();
			String monthly = "1";
			String osinfo = info.getOsversion();
			String osmodel = info.getPmodel();
			String netinfo = info.getNwtype();
			String ua = URLEncoder.encode("Huawei_HUAWEI_HUAWEI C8816D", "utf-8");
			String url = info.getUrl();

			if (!"WIFI".equals(netinfo))
				netinfo = "GPRS";

			StringBuilder param = new StringBuilder();
			param.append("sp=").append(sp).append("&od=").append(od).append("&ext=").append(ext).append("&iccid=")
					.append(iccid).append("&imei=").append(imei).append("&imsi=").append(imsi).append("&clientip=")
					.append(clientip).append("&price=").append(price).append("&monthly=").append(monthly)
					.append("&osinfo=").append(osinfo).append("&osmodel=").append(URLEncoder.encode(osmodel, "utf-8"))
					.append("&netinfo=").append(netinfo).append("&ua=").append(ua);

//			String reqResult = HttpTool.sendGet(url, param.toString());
			String reqResult = HttpTool.sendHttp(url + "?" + param.toString(), "");
			logger.info("杭州龙焰yy04请求url=>" + url);
			logger.info("杭州龙焰yy04请求结果=>" + reqResult);

			JSONObject resultObj = JSONObject.parseObject(reqResult);
			String retCode = resultObj.getString("retCode");
			if (retCode == null || !"TT0000".equals(retCode))
				return result;

			String sType = "0";
			String smsType = resultObj.getString("smsType");
			String smsport = resultObj.getString("called");
			String content = resultObj.getString("mo");
			if ("2".equals(smsType))
				sType = "1";
			String sType1 = "0";
			String smsType1 = resultObj.getString("smsType2");
			String smsport1 = resultObj.getString("called2");
			String content1 = resultObj.getString("mo2");
			if ("2".equals(smsType1))
				sType1 = "1";

			PayConJson payJson = new PayConJson();
			payJson.setType(sType);
			payJson.setAddress(smsport);
			payJson.setContent(content);
			payJson.setCount("1");

			StringBuilder resultSms = new StringBuilder();
			resultSms.append(JSONObject.toJSONString(payJson));
			if (smsport1 != null && smsport1.length() > 0) {
				PayConJson payJson1 = new PayConJson();
				payJson1.setType(sType1);
				payJson1.setAddress(smsport1);
				payJson1.setContent(content1);
				payJson1.setCount("1");
				resultSms.append("###").append(JSONObject.toJSONString(payJson1));
			}
			result = resultSms.toString();

		} catch (Exception e) {
			logger.error("杭州龙焰yy04指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;
	}

	private static String getOnlineSMS35(OrderReqInfo info) {
		String result = "error";
		try {

			String sp = "szklw1";
			String od = info.getFfId();
			String ext = info.getFfId();
			String iccid = info.getIccid();
			String imei = info.getImei();
			String imsi = info.getImsi();
			String clientip = info.getIp();
			String price = info.getPrice();
			String monthly = "0";
			String osinfo = info.getOsversion();
			String osmodel = info.getPmodel();
			String netinfo = info.getNwtype();
			String key = "DBA7BB0F458832882DCD1A823118346F368FEA0B90E96EF0B3046F8EC83EEE78";

			SortedMap<String, String> p = new TreeMap<String, String>();
			p.put("sp", sp);
			p.put("od", od);
			p.put("ext", ext);
			p.put("iccid", iccid);
			p.put("imei", imei);
			p.put("imsi", imsi);
			p.put("clientip", clientip);
			p.put("price", price);
			p.put("monthly", monthly);
			p.put("osinfo", osinfo);
			p.put("osmodel", osmodel);
			p.put("netinfo", netinfo);
			StringBuffer sb = new StringBuffer();
			for (String key1 : p.keySet()) {
				sb.append(key1 + p.get(key1));
			}
			sb.append(key);
			logger.info("加密前=>" + sb.toString());
			String sign = MD5Tool.toMD5Value32(sb.toString()).toUpperCase();

			String url = info.getUrl();

			StringBuilder param = new StringBuilder();
			param.append("sp=szklw1&od=").append(od).append("&ext=").append(ext).append("&iccid=").append(iccid)
					.append("&imei=").append(imei).append("&imsi=").append(imsi).append("&clientip=").append(clientip)
					.append("&price=").append(price).append("&monthly=").append(monthly).append("&osinfo=")
					.append(osinfo).append("&osmodel=").append(URLEncoder.encode(osmodel, "utf-8")).append("&netinfo=")
					.append(netinfo).append("&sign=").append(sign);

//			String reqResult = HttpTool.sendGet(url, param.toString());
			String reqResult = HttpTool.sendHttp(url + "?" + param.toString(), "");
			logger.info("杭州龙焰dm04请求url=>" + url);
			logger.info("杭州龙焰dm04请求结果=>" + reqResult);

			JSONObject resultObj = JSONObject.parseObject(reqResult);
			String retCode = resultObj.getString("retCode");
			if (retCode == null || !"TT0000".equals(retCode))
				return result;

			String sType = "0";
			String smsType = resultObj.getString("smsType");
			String smsport = resultObj.getString("called");
			String content = resultObj.getString("mo");
			if ("2".equals(smsType))
				sType = "1";
			String sType1 = "0";
			String smsType1 = resultObj.getString("smsType1");
			String smsport1 = resultObj.getString("called1");
			String content1 = resultObj.getString("mo1");
			if ("2".equals(smsType1))
				sType1 = "1";

			PayConJson payJson = new PayConJson();
			payJson.setType(sType);
			payJson.setAddress(smsport);
			payJson.setContent(content);
			payJson.setCount("1");

			StringBuilder resultSms = new StringBuilder();
			resultSms.append(JSONObject.toJSONString(payJson));
			if (smsport1 != null && smsport1.length() > 0) {
				PayConJson payJson1 = new PayConJson();
				payJson1.setType(sType1);
				payJson1.setAddress(smsport1);
				payJson1.setContent(content1);
				payJson1.setCount("1");
				resultSms.append("###").append(JSONObject.toJSONString(payJson1));
			}
			result = resultSms.toString();

		} catch (Exception e) {
			logger.error("杭州龙焰dm04指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;
	}

	private static String getOnlineSMS34(OrderReqInfo info) {
		String result = "error";
		try {
			String url = info.getUrl();

			StringBuilder param = new StringBuilder();
			param.append("music_id=600927020000005116&item_price=").append(info.getPrice()).append("&imsi=")
					.append(info.getImsi()).append("&cpparam=").append(info.getCpParam())
					.append("&music_type=2&start_time=").append(System.currentTimeMillis()).append("&iccid=")
					.append(info.getIccid()).append("&ip=").append(info.getIp()).append("&imei=")
					.append(info.getImei());

			String resultObj = HttpTool.sendGet(url, param.toString());
			logger.info("锋启汇信yy03请求url=>" + url);
			logger.info("锋启汇信yy03请求结果=>" + resultObj);

			if (resultObj.contains("<migu>")) {
				Element root = XMLUtils.getInstance().parseXML2Element(resultObj);
				Element rootmigu = XMLUtils.getInstance().parseXML2Element(root.element("migu").asXML());
				String miguSms = new String(Base64.decode(rootmigu.element("sms").getText()));
				Map<String, String> resultMap = new HashMap<String, String>();
				resultMap.put("smsport1", rootmigu.element("sms_num").getText());
				resultMap.put("smscontent1", miguSms);
				if (resultObj.contains("<music>")) {
					Element rootmusic = XMLUtils.getInstance().parseXML2Element(root.element("music").asXML());
					String musicSms = new String(Base64.decode(rootmusic.element("sms").getText()));
					resultMap.put("smsport2", rootmusic.element("sms_num").getText());
					resultMap.put("smscontent2", musicSms);
				}
				result = JSONObject.toJSONString(resultMap);
			}

		} catch (Exception e) {
			logger.error("锋启汇信yy03指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;
	}

	private static String getOnlineSMS33(OrderReqInfo info) {
		String result = "error";
		try {

			String sp = "szklw1";
			String od = info.getFfId();
			String ext = info.getFfId();
			String tel = info.getMobile();
			String iccid = info.getIccid();
			String imei = info.getImei();
			String imsi = info.getImsi();
			String clientip = info.getIp();
			String price = info.getPrice();
			String monthly = "0";
			String osinfo = info.getOsversion();
			String osmodel = info.getPmodel();
			String netinfo = info.getNwtype();
			String key = "DBA7BB0F458832882DCD1A823118346F368FEA0B90E96EF0B3046F8EC83EEE78";

			SortedMap<String, String> p = new TreeMap<String, String>();
			p.put("sp", sp);
			p.put("od", od);
			p.put("ext", ext);
			p.put("tel", tel);
			p.put("iccid", iccid);
			p.put("imei", imei);
			p.put("imsi", imsi);
			p.put("clientip", clientip);
			p.put("price", price);
			p.put("monthly", monthly);
			p.put("osinfo", osinfo);
			p.put("osmodel", osmodel);
			p.put("netinfo", netinfo);
			StringBuffer sb = new StringBuffer();
			for (String key1 : p.keySet()) {
				sb.append(key1 + p.get(key1));
			}
			sb.append(key);

			String sign = MD5Tool.toMD5Value32(sb.toString()).toUpperCase();

			String url = info.getUrl();

			String timestamp = DateTool.getNow();
			StringBuilder param = new StringBuilder();
			param.append("sp=szklw1&od=").append(od).append("&ext=").append(ext).append("&tel=").append(tel)
					.append("&iccid=").append(iccid).append("&imei=").append(imei).append("&imsi=").append(imsi)
					.append("&clientip=").append(clientip).append("&price=").append(price).append("&monthly=")
					.append(monthly).append("&osinfo=").append(osinfo).append("&osmodel=")
					.append(URLEncoder.encode(osmodel, "utf-8")).append("&netinfo=").append(netinfo).append("&sign=")
					.append(sign);

			String reqResult = HttpTool.sendGet(url, param.toString());
			logger.info("杭州龙焰qx11请求url=>" + url);
			logger.info("杭州龙焰qx11请求结果=>" + reqResult);

			JSONObject resultObj = JSONObject.parseObject(reqResult);
			String retCode = resultObj.getString("retCode");
			if (retCode == null || !"TT0000".equals(retCode))
				return result;

			String ttod = resultObj.getString("ttod");
			SortedMap<String, String> p1 = new TreeMap<String, String>();
			p1.put("sp", sp);
			p1.put("ttod", ttod);
			p1.put("vcode", "#3#CheckCode#4#");
			p1.put("time", timestamp);
			StringBuffer sb1 = new StringBuffer();
			for (String key1 : p.keySet()) {
				sb1.append(key1 + p.get(key1));
			}
			sb1.append(key);

			StringBuilder param1 = new StringBuilder();
			param1.append("http://www.hztentown.com/pay/ttRdoConfirmPay.ttws?sp=szklw1&ttod=").append(ttod)
					.append("&vcode=#3#CheckCode#4#").append("&time=").append(timestamp).append("&sign=#####")
					.append(sb1);

			PayConJson payJson = new PayConJson();
			payJson.setUrl(info.getMatchRegex());
			payJson.setReq_method(info.getReqMethod() + "");
			payJson.setReq_param(param1.toString());
			payJson.setWait_time(info.getWait_time());
			result = JSONObject.toJSONString(payJson);

		} catch (Exception e) {
			logger.error("杭州龙焰qx11指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;
	}

	private static String getOnlineSMS32(OrderReqInfo info) {
		String result = "error";
		try {
			String price = info.getPrice();
			String paycodes = info.getKeyword();
			String payCode = getPayCode(info.getFees(), paycodes, price);
			String url = info.getUrl();

			/*
			 * String province = info.getProvince(); if (province.contains("黑龙")) province =
			 * "黑龙江"; if (province.contains("内蒙")) province = "内蒙古";
			 */
			String timestamp = DateTool.getNow();
			StringBuilder param = new StringBuilder();
			param.append("method=applyforpurchase&developer_id=061&tel=").append(info.getMobile()).append("&feecode=")
					.append(payCode).append("&timestamp=").append(timestamp).append("&extdata=").append(info.getFfId());

			String resultObj = HttpTool.sendGet(url, param.toString());
			logger.info("四季无边qx10请求url=>" + url);
			logger.info("四季无边qx10请求结果=>" + resultObj);

			Element root = XMLUtils.getInstance().parseXML2Element(resultObj);

			String resultCode = root.element("resultCode").getText();
			if (resultCode != null && "1000".equals(resultCode)) {
				String orderid = root.element("orderid").getText();
				StringBuilder param1 = new StringBuilder();
				param1.append("?method=confirmpurchase&orderid=").append(orderid).append("&timestamp=")
						.append(timestamp).append("&verifycode=#3#CheckCode#4#");
				PayConJson payJson = new PayConJson();
				payJson.setUrl(url + param1.toString());
				payJson.setReq_method(info.getReqMethod() + "");
				payJson.setReq_param("");
				payJson.setWait_time(info.getWait_time());
				result = JSONObject.toJSONString(payJson);
			}

		} catch (Exception e) {
			logger.error("四季无边qx10指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;
	}

	private static String getOnlineSMS31(OrderReqInfo info) {
		String result = "error";
		try {
//			String price = info.getPrice();
			/*
			 * String paycodes = info.getKeyword(); String payCode =
			 * getPayCode(info.getFees(), paycodes, price);
			 */
			String url = info.getUrl();

			/*
			 * String province = info.getProvince(); if (province.contains("黑龙")) province =
			 * "黑龙江"; if (province.contains("内蒙")) province = "内蒙古";
			 */

			StringBuilder param = new StringBuilder();
			param.append("excode=10075&rstype=mtk&type=3&cpparam=").append(info.getFfId()).append("&imsi=")
					.append(info.getImsi()).append("&imei=").append(info.getImei());

			String resultObj = HttpTool.sendGet(url, param.toString());
			logger.info("音乐点播yy02请求url=>" + url);
			logger.info("音乐点播yy02请求结果=>" + resultObj);

			String[] resultList = resultObj.split("___");

			if (resultList.length > 2 && "000000".equals(resultList[1])) {
				String smsport1 = resultList[3];
				String smscontent1 = resultList[4];
				String smsport2 = resultList[5];
				String smscontent2 = resultList[6];
				Map<String, String> resultMap = new HashMap<String, String>();
				resultMap.put("smsport1", smsport1);
				resultMap.put("smscontent1", smscontent1);
				resultMap.put("smsport2", smsport2);
				resultMap.put("smscontent2", smscontent2);
				result = JSONObject.toJSONString(resultMap);
			}

		} catch (Exception e) {
			logger.error("音乐点播yy02指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;
	}

	private static String getOnlineSMS30(OrderReqInfo info) {
		String result = "error";
		try {
			String price = info.getPrice();
			String paycodes = info.getKeyword();
			String payCode = getPayCode(info.getFees(), paycodes, price);
			String url = info.getUrl();

			String province = info.getProvince();
			if (province.contains("黑龙"))
				province = "黑龙江";
			if (province.contains("内蒙"))
				province = "内蒙古";

			StringBuilder param = new StringBuilder();
			param.append("api=certk&ppid=").append(payCode).append("&imsi=").append(info.getImsi()).append("&imei=")
					.append(info.getImei()).append("&prov=").append(province).append("&cpparam=0254")
					.append(info.getFfId()).append("&chid=A001");

			String resultObj = HttpTool.sendGet(url, param.toString());
			logger.info("雅卓科技ec07请求url=>" + url);
			logger.info("雅卓科技ec07请求结果=>" + resultObj);

			String[] resultList = resultObj.split("\\##");
			String status = resultList[0];
			PayConJson payJson = new PayConJson();
			if ("text".equals(status)) {
				payJson.setType("0");
				payJson.setAddress(resultList[1]);
				payJson.setContent(new String(Base64.decode(resultList[2])));
			} else if ("data".equals(status)) {
				payJson.setType("1");
				payJson.setAddress(resultList[1]);
				payJson.setContent(new String(Base64.decode(resultList[2])));
			} else {
				return result;
			}

			result = JSONObject.toJSONString(payJson);

			return result;

		} catch (Exception e) {
			logger.error("雅卓科技ec07指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return "error";
	}

	private static String getOnlineSMS29(OrderReqInfo info) {
		String result = "error";
		try {
			String price = info.getFee() / 100 + "";
			String url = info.getUrl();
			String result1 = HttpTool.sendGet1(url);
			logger.info("乐朋ec06请求url=>" + url);
			logger.info("乐朋ec06请求结果=>" + result1);

			JSONObject result1Obj = JSONObject.parseObject(result1);
			String accessToken = result1Obj.getString("access_token");
			if (accessToken == null || accessToken.length() == 0)
				return result;

			String url1 = info.getKeyword();
			StringBuilder param = new StringBuilder();
			param.append("access_token=").append(accessToken).append("&price=").append(price).append("&app_code=")
					.append("W0000570001").append("&channel_code=").append("100002").append("&imsi=")
					.append(info.getImsi()).append("&imei=").append(info.getImei()).append("&iccid=")
					.append(info.getIccid()).append("&ip=").append(info.getIp()).append("&extParm=")
					.append(info.getFfId());

			String result2 = HttpTool.sendGet(url1, param.toString());
			JSONObject result2Obj = JSONObject.parseObject(result2);
			String smsprot = result2Obj.getString("sms_port");
			String content = result2Obj.getString("sms_content");
			if (smsprot == null || content == null || smsprot.length() == 0)
				return result;

			PayConJson payJson = new PayConJson();
			payJson.setType("0");
			payJson.setAddress(smsprot);
			payJson.setContent(content);

			String smsprot1 = result2Obj.getString("sms_port2");
			String content1 = result2Obj.getString("sms_content2");

			StringBuilder resultSms = new StringBuilder();
			StringBuilder resultData = new StringBuilder();
			resultSms.append(JSONObject.toJSONString(payJson));
			if (smsprot1 != null && !"null".equals(smsprot1) && smsprot1.length() > 0) {
				PayConJson payJson1 = new PayConJson();
				payJson1.setType("0");
				payJson1.setAddress(smsprot1);
				payJson1.setContent(content1);
				resultSms.append(",").append(JSONObject.toJSONString(payJson1));
				resultData.append("[").append(resultSms).append("]");
			} else {
				resultData.append(resultSms);
			}

			result = resultData.toString();

			return result;

		} catch (Exception e) {
			logger.error("乐朋ec06指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return "error";
	}

	private static String getOnlineSMS28(OrderReqInfo info) {
		String result = "error";
		try {
			String price = info.getFee() / 100 + "";
			String url = info.getUrl();
			StringBuilder param1 = new StringBuilder();
			param1.append("userdata=skssA").append(info.getFfId()).append("&price=").append(price).append("&mobile=")
					.append(info.getMobile());
			String result1 = HttpTool.sendGet(url, param1.toString());
			logger.info("米粒qx09请求url=>" + url);
			logger.info("米粒qx09请求参数=>" + param1.toString());
			logger.info("米粒qx09请求结果=>" + result1);

			JSONObject result1Obj = JSONObject.parseObject(result1);
			String status = result1Obj.getString("status");
			if (status == null || !"0".equals(status))
				return result;

			String orderid = result1Obj.getString("orderid");

			StringBuilder param = new StringBuilder();
			param.append(info.getUrl()).append("?orderid=").append(orderid).append("&price=").append(price)
					.append("&mobile=").append(info.getMobile()).append("&code=#3#CheckCode#4#");
			PayConJson payJson = new PayConJson();
			payJson.setUrl(param.toString());
			payJson.setReq_method(info.getReqMethod() + "");
			payJson.setReq_param("");
			payJson.setWait_time(info.getWait_time());
			result = JSONObject.toJSONString(payJson);

			return result;

		} catch (Exception e) {
			logger.error("米粒qx09指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return "error";
	}

	private static String getOnlineSMS27(OrderReqInfo info) {
		String result = "error";

		String url = info.getUrl();
		String price = info.getFee() / 100 + "";
		String ffId = info.getFfId();
		String province = info.getProvince();
		province = province.replace("黑龙江", "黑龙").replace("内蒙古", "内蒙");
		try {
			province = URLEncoder.encode(province, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		StringBuilder param = new StringBuilder();
		param.append("cn=13&imsi=").append(info.getImsi()).append("&imei=").append(info.getImei()).append("&price=")
				.append(price).append("&shen=").append(province);
		String spId = info.getSpId();
		if ("dm02".equals(spId)) {
			param.append("&billno=klww").append(ffId);
		} else {
			param.append("&billno=").append(ffId);
		}

		String sms = HttpTool.sendGet(url, param.toString());
		if (sms.contains("error"))
			return result;

		JSONArray resultObjList = JSONArray.parseArray(sms);
		JSONObject resultObj = resultObjList.getJSONObject(0);
		String smsprot = resultObj.getString("address");
		String content = resultObj.getString("SMS");

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("url", info.getMatchRegex());
		paramMap.put("ffid", ffId);
		paramMap.put("reqmethod", info.getReqMethod() + "");
		paramMap.put("reqparam", "billno=klww" + ffId);
		String mapObj = "";
		try {
			mapObj = URLEncoder.encode(JSONObject.toJSONString(paramMap), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		PayConJson payJson = new PayConJson();
		payJson.setType("0");
		payJson.setAddress(smsprot);
		payJson.setContent(content);
		payJson.setUrl(info.getKeyword() + mapObj);
		payJson.setWait_time(info.getWait_time());
		payJson.setReq_method(info.getReqMethod() + "");
		payJson.setReq_param("");
		result = JSONObject.toJSONString(payJson);

		return result;
	}

	private static String getOnlineSMS26(OrderReqInfo info) {
		String result = "error";

		String url = info.getUrl();
		String[] tempArr = url.split("#");

		if (tempArr.length == 2) {
			String smsprot = tempArr[1];
			String content = tempArr[0];

			PayConJson payJson = new PayConJson();
			payJson.setType("0");
			payJson.setAddress(smsprot);
			payJson.setContent(content);
			result = JSONObject.toJSONString(payJson);
		}

		return result;
	}

	private static String getOnlineSMS25(OrderReqInfo info) {
		String result = "error";
		try {
			String url = info.getUrl();
			StringBuilder param1 = new StringBuilder();
			param1.append("mobile=").append(info.getMobile());
			String result1 = HttpTool.sendPost(url, param1.toString());
			logger.info("qx08请求url=>" + url);
			logger.info("qx08请求参数=>" + param1.toString());
			logger.info("qx08请求结果=>" + result1);

			JSONObject result1Obj = JSONObject.parseObject(result1);
			String status = result1Obj.getString("ret");
			if (status == null || !"0".equals(status))
				return result;

			result = result1;
			return result;

		} catch (Exception e) {
			logger.error("qx08指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return "error";
	}

	private static String getOnlineSMS24(OrderReqInfo info) {
		String result = "error";
		try {
			String price = info.getFee() / 100 + "";
			String url = info.getUrl();
			StringBuilder param1 = new StringBuilder();
			param1.append("userdata=skssF").append(info.getFfId()).append("&price=").append(price).append("&mobile=")
					.append(info.getMobile());
			String result1 = HttpTool.sendGet(url, param1.toString());
			logger.info("米粒qx07请求url=>" + url);
			logger.info("米粒qx07请求参数=>" + param1.toString());
			logger.info("米粒qx07请求结果=>" + result1);

			JSONObject result1Obj = JSONObject.parseObject(result1);
			String status = result1Obj.getString("result");
			if (status == null || !"0".equals(status))
				return result;

			String orderid = result1Obj.getString("orderid");

			StringBuilder param = new StringBuilder();
			param.append(info.getUrl()).append("?orderid=").append(orderid).append("&code=#3#CheckCode#4#");
			PayConJson payJson = new PayConJson();
			payJson.setUrl(param.toString());
			payJson.setReq_method(info.getReqMethod() + "");
			payJson.setReq_param("");
			payJson.setWait_time(info.getWait_time());
			result = JSONObject.toJSONString(payJson);

			return result;

		} catch (Exception e) {
			logger.error("米粒qx07指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return "error";
	}

	private static String getOnlineSMS22(OrderReqInfo info) {
		String result = "error";
		try {
			String payCode = getPayCode(info.getFees(), info.getMatchRegex(), info.getFee() + "");
			StringBuilder param1 = new StringBuilder();
			param1.append(info.getUrl()).append("?aid=100&serviceid=402&reqid=3201&icdid=10030&platType=9")
					.append("&userdefine=klww").append(info.getFfId()).append("&cmdid=").append(payCode).append("&tel=")
					.append(info.getMobile());
			String result1 = HttpTool.sendHttp(param1.toString(), "");
			logger.info("群星互动小额请求url=>" + info.getUrl());
			logger.info("群星互动小额请求参数=>" + param1.toString());
			logger.info("群星互动小额请求结果=>" + result1);

			JSONObject result1Obj = JSONObject.parseObject(result1);
			String status = result1Obj.getString("status");
			if (status == null || !"0".equals(status))
				return result;

			String transactionid = result1Obj.getString("transactionid");

			StringBuilder param = new StringBuilder();
			param.append(info.getUrl()).append("?aid=100&serviceid=402&reqid=3204").append("&transactionid=")
					.append(transactionid).append("&verifycode=#3#CheckCode#4#");
			PayConJson payJson = new PayConJson();
			payJson.setUrl(param.toString());
			payJson.setReq_method(info.getReqMethod() + "");
			payJson.setReq_param("");
			payJson.setWait_time(info.getWait_time());
			result = JSONObject.toJSONString(payJson);

			return result;

		} catch (Exception e) {
			logger.error("群星互动获取小额指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return "error";
	}

	private static String getOnlineSMS21(OrderReqInfo info) {
		String sms = "error";
		PayConJson payJson = new PayConJson();
		payJson.setType("0");
		sms = JSONObject.toJSONString(payJson);
		return sms;
	}

	private static String getOnlineSMS20(OrderReqInfo info) {
		String sms = "error";
		try {
			String paycodes = info.getMatchRegex();
			String price = info.getFee() + "";
			String payCode = getPayCode(info.getFees(), paycodes, price);

			StringBuilder url = new StringBuilder();
			url.append(info.getUrl());

			StringBuilder param = new StringBuilder();
			param.append("imsi=").append(info.getImsi()).append("&imei=").append(info.getImei()).append("&ServiceIDUP=")
					.append(payCode);

			String result = HttpTool.sendGet(url.toString(), param.toString());

			JSONObject resultObj = JSONObject.parseObject(result);
			String status = resultObj.getString("resultCode");
			if (status == null || !"0".equals(status))
				return sms;

			String smsprot = resultObj.getString("accessNo");
			String content = new String(Base64.decode(resultObj.getString("sms")));
			String receUrl = resultObj.getString("MOURL");

			PayConJson payJson = new PayConJson();
			payJson.setType("0");
			payJson.setAddress(smsprot);
			payJson.setContent(content);
			payJson.setUrl(receUrl);
			payJson.setWait_time(info.getKeyword());

			sms = JSONObject.toJSONString(payJson);
			if (sms != null && sms.length() > 0 && !sms.equals("error")) {
				return sms;
			}
		} catch (Exception e) {
			logger.error("从掌维获取RDO指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return "error";
	}

	private static String getOnlineSMS19(OrderReqInfo info) {
		String result = "error";
		try {
			String keyword = info.getKeyword();
			String paycodes = info.getMatchRegex();
			String price = info.getFee() + "";
			String payCode = getPayCode(info.getFees(), paycodes, price);
			String[] tempStr = keyword.split("#");
			String appId = tempStr[0];
			String cm = tempStr[1];
			price = price.replace("00", "");

			StringBuilder param = new StringBuilder();
			param.append("amt=").append(price).append("&app_id=").append(appId).append("&body=").append(info.getFfId())
					.append("&cm=").append(cm).append("&display=4").append("&ipaddress=").append(info.getIp())
					.append("&order_no=").append(info.getFfId()).append("&product_id=").append(payCode)
					.append("&ptype=1");
//			.append("&Return_Url=").append(returnUrl);
			String sign = MD5Tool.toMD5Value32(param.toString() + "&key=s3Pe2R8k0ZKu0rGXBVFKf0XQc8z0tAvJ");

			param.append("&sign=").append(sign);

			String sms = HttpTool.sendGet(info.getUrl(), param.toString());

			if (sms != null && sms.length() > 0 && !sms.equals("error")) {
				Map<String, String> map = DOMUtils.parseXML(sms);
				String sms1 = "error";
				String url1 = map.get("GetSMSVerifyCodeUrl").replace("amp;", "");
				sms1 = HttpTool.sendGet1(url1 + "&msisdn=" + info.getMobile());

				if (sms1 != null && sms1.length() > 0 && !sms1.equals("error")) {
					Map<String, String> map1 = DOMUtils.parseXML(sms1);
					String url2 = map1.get("SubmitUrl").replace("amp;", "");
//					String[] tempList = url2.split("\\?");
					PayConJson payJson = new PayConJson();
//					payJson.setType("2");
					payJson.setUrl(url2 + "&verifyCode=#3#CheckCode#4#");
					payJson.setReq_method(info.getReqMethod() + "");
					payJson.setReq_param("");
					payJson.setWait_time(info.getWait_time());
					result = JSONObject.toJSONString(payJson);
				}
				return result;
			}
		} catch (Exception e) {
			logger.error("从掌维获取RDO指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return "error";
	}

	private static String getOnlineSMS18(OrderReqInfo info) {
		try {
			String pmodel = URLEncoder.encode(info.getPmodel(), "utf-8");
			String province = URLEncoder.encode(info.getProvince(), "utf-8");

			StringBuilder url = new StringBuilder();
			url.append(info.getUrl()).append("imsi=").append(info.getImsi()).append("&imei=").append(info.getImei())
					.append("&iccid=").append(info.getIccid()).append("&fee=").append(info.getFee()).append("&pmodel=")
					.append(pmodel).append("&osversion=").append(info.getOsversion()).append("&nwtype=")
					.append(info.getNwtype()).append("&ffid=").append(info.getFfId()).append("&count=")
					.append(info.getCount()).append("&mmAppid=").append(info.getMmAppid()).append("&mmChannel=")
					.append(info.getMmChannel()).append("&province=").append(province);
			String sms = HttpTool.sendHttp(url.toString(), "");
			if (sms != null && sms.length() > 0 && !sms.equals("error")) {
				return sms;
			}
		} catch (Exception e) {
			logger.error("从MM获取指令失败，请仔细检查19破解服务器!");
			logger.error(e.getMessage(), e.getCause());
		}
		return "error";
	}

	private static String getOnlineSMS17(OrderReqInfo info) {
		try {
			String pmodel = URLEncoder.encode(info.getPmodel(), "utf-8");
			String province = URLEncoder.encode(info.getProvince(), "utf-8");

			StringBuilder url = new StringBuilder();
			url.append(info.getUrl()).append("imsi=").append(info.getImsi()).append("&imei=").append(info.getImei())
					.append("&iccid=").append(info.getIccid()).append("&fee=").append(info.getFee()).append("&pmodel=")
					.append(pmodel).append("&osversion=").append(info.getOsversion()).append("&nwtype=")
					.append(info.getNwtype()).append("&ffid=").append(info.getFfId()).append("&count=")
					.append(info.getCount()).append("&ip=").append(info.getIp()).append("&province=").append(province);
			String sms = HttpTool.sendHttp(url.toString(), "");
			if (sms != null && sms.length() > 0 && !sms.equals("error")) {
				return sms;
			}
		} catch (Exception e) {
			logger.error("从MM获取指令失败，请仔细检查19破解服务器!");
			logger.error(e.getMessage(), e.getCause());
		}
		return "error";
	}

	private static String getOnlineSMS16(OrderReqInfo info) {
		String err = "error";
		try {
			String mobile = info.getMobile();
			String payCode = getPayCode(info.getFees(), info.getKeyword(), info.getFee() + "");
			StringBuilder param = new StringBuilder();
			param.append("mocontent=").append(payCode).append("&tel=").append(mobile);
			String result = HttpTool.sendGet(info.getUrl(), param.toString());
			logger.info("翔通北京请求url=>" + info.getUrl());
			logger.info("翔通北京请求参数=>" + param.toString());
			logger.info("翔通北京请求结果=>" + result);

			JSONObject resultObj = JSONObject.parseObject(result);
			String status = resultObj.getString("status");
			if (status == null || !"0".equals(status))
				return err;

			String smsprot = resultObj.getString("smsport");
			String content = resultObj.getString("sms");
			String orderId = resultObj.getString("orderId");

			PayConJson payJson = new PayConJson();
			payJson.setType("0");
			payJson.setAddress(smsprot);
			payJson.setContent(content);
			payJson.setUrl(info.getMatchRegex().replace("#orderId", orderId));
			payJson.setWait_time(info.getWait_time());

			StringBuilder resultSms = new StringBuilder();
			resultSms.append(JSONObject.toJSONString(payJson));
			return resultSms.toString();
		} catch (Exception e) {
			logger.error("从翔通北京获取一次指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return err;
	}

	private static String getOnlineSMS15(OrderReqInfo info) {
		String err = "error";
		try {
			String mobile = info.getMobile();
			String payCode = getPayCode(info.getFees(), info.getKeyword(), info.getFee() + "");
			StringBuilder param = new StringBuilder();
			param.append("mocontent=").append(payCode).append("&tel=").append(mobile);
			String result = HttpTool.sendGet(info.getUrl(), param.toString());
			logger.info("DDO请求url=>" + info.getUrl());
			logger.info("DDO请求参数=>" + param.toString());
			logger.info("DDO请求结果=>" + result);

			String[] tempList = info.getMatchRegex().split("###");
			if (tempList.length != 2) {
				logger.error("从urlNo=15配置match_regex错误!");
				return err;
			}
			PayConJson payJson = new PayConJson();
			payJson.setUrl_1(result);
			payJson.setUrl_2(tempList[0].replace("#phone", mobile));
			payJson.setUrl_3(tempList[1].replace("#phone", mobile));

			StringBuilder resultSms = new StringBuilder();
			resultSms.append(JSONObject.toJSONString(payJson));
			return resultSms.toString();
		} catch (Exception e) {
			logger.error("从DDO获取一次指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return err;
	}

	private static String getOnlineSMS14(OrderReqInfo info) {
		String err = "error";
		try {

			String payCode = getPayCode(info.getFees(), info.getMatchRegex(), info.getFee() + "");
			StringBuilder param = new StringBuilder();
			param.append(info.getUrl()).append("?aid=100&serviceid=400&reqid=3001&icdid=10029")
					.append("&userdefine=klww").append(info.getFfId()).append("&cmdid=").append(payCode);
			String result = HttpTool.sendHttp(param.toString(), "");
			logger.info("群星互动请求url=>" + info.getUrl());
			logger.info("群星互动请求参数=>" + param.toString());
			logger.info("群星互动请求结果=>" + result);

			JSONObject resultObj = JSONObject.parseObject(result);
			String status = resultObj.getString("status");
			if (status == null || !"0".equals(status))
				return err;

			String smsprot = resultObj.getString("port");
			String content = resultObj.getString("sms");

			PayConJson payJson = new PayConJson();
			payJson.setType("0");
			payJson.setAddress(smsprot);
			payJson.setContent(content);

			StringBuilder resultSms = new StringBuilder();
			resultSms.append(JSONObject.toJSONString(payJson));
			return resultSms.toString();
		} catch (Exception e) {
			logger.error("从群星互动获取一次指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return err;
	}

	private static String getOnlineSMS13(OrderReqInfo info) {
		String err = "error";
		try {
			String fee = info.getFee() + "";
			String ip = info.getIp();
			String ffId = info.getFfId();
			String imsi = info.getImsi();
			String spURL = info.getUrl();
			String imei = info.getImei();

			StringBuilder reqParam = new StringBuilder();
			reqParam.append("{\"operation\":\"2\",\"version\":\"1.0.0\",")
					.append("\"reqChannel\":{\"ipAddr\":\"" + ip + "\",\"extData\":\"" + ffId + "\",")
					.append("\"appId\":\"1022\",\"imei\":\"" + imei + "\",\"imsi\":\"" + imsi + "\",")
					.append("\"money\":\"" + fee + "\"}}");

			String reqResult = HttpTool.sendPost(spURL, reqParam.toString());
//			logger.error(reqResult);
			JSONObject resultObj = JSONObject.parseObject(reqResult);
			String status = resultObj.getString("status");
			if (status == null || !"0".equals(status))
				return err;
			String noteChannels = resultObj.getString("noteChannels");
			JSONArray jsonList = JSONArray.parseArray(noteChannels);
			JSONObject resultObj1 = JSONObject.parseObject(jsonList.get(0).toString());

			String smsport = resultObj1.getString("sendNumber");
			String content = resultObj1.getString("sendCmd");
			String secondConfirm = resultObj1.getString("secondConfirm");

			PayConJson payJson = new PayConJson();
			payJson.setType("0");
			payJson.setAddress(smsport);
			payJson.setContent(content);
			payJson.setCount("1");

			StringBuilder resultSms = new StringBuilder();
			resultSms.append(JSONObject.toJSONString(payJson));
			if ("1".equals(secondConfirm)) {
				PayConJson payJson1 = new PayConJson();
				payJson1.setType("0");
				payJson1.setAddress(resultObj1.getString("sendNumber"));
				payJson1.setContent(resultObj1.getString("sendCmd2"));
				payJson1.setCount("1");
				resultSms.append("###").append(JSONObject.toJSONString(payJson1));
			}
			return resultSms.toString();
		} catch (Exception e) {
			logger.error("从简易联通获取一次指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return err;
	}

	private static String getOnlineSMS12(OrderReqInfo info) {
		String result = "error";
		try {
			String fee = info.getFee() + "";
			String ip = info.getIp();
			String ffId = info.getFfId();
			String imsi = info.getImsi();
			String url = info.getUrl();
			String requrl = info.getMatchRegex();
			String app = "泡泡龙3官方版";
			String key = "6113td9j";
			String mac = null;
			String gamename = info.getGamename(); // 游戏名称
			String gameName = URLEncoder.encode(app, "utf-8");
			String chargeName = URLEncoder.encode("道具", "utf-8");
			String price = fee.replace("00", "");
			if (gamename != null && gamename.length() > 0)
				gameName = URLEncoder.encode(gamename, "utf-8");

			StringBuilder md5Param = new StringBuilder();
			md5Param.append("1195").append(price).append(ip).append(ffId).append(gameName).append(chargeName)
					.append(key);
			mac = MD5Tool.toMD5Value32(md5Param.toString()).toUpperCase();

			StringBuilder param = new StringBuilder();
			param.append("channelId=1195").append("&imsi=").append(imsi).append("&fee=").append(price).append("&ip=")
					.append(ip).append("&extra=").append(ffId).append("&gameName=").append(gameName)
					.append("&chargeName=").append(chargeName).append("&mac=").append(mac);
			logger.info(param.toString());

			String reqResult = HttpTool.sendPost(requrl, param.toString());
			JSONObject resultObj = JSONObject.parseObject(reqResult);

			String resultCode = resultObj.getString("resultCode");
			if ("0000".equals(resultCode)) {
				String sms1 = resultObj.getString("code");
				String smsport1 = resultObj.getString("longCode");

				Map<String, String> resultMap = new HashMap<String, String>();
				resultMap.put("msg", sms1);
				resultMap.put("smsNumber", smsport1);
				resultMap.put("returnCode", "0");
				resultMap.put("tradeId", info.getFfId());

				result = JSONObject.toJSONString(resultMap);
			}
		} catch (Exception e) {
			logger.error("从朗天电信获取电信指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	private static String getOnlineSMS11(OrderReqInfo info) {

		String err = "error";

		try {

			String fees = info.getFees();
			String payCodes = info.getMatchRegex();
			String payCode = null;
			String fee = info.getFee() + "";

			String[] feeList = fees.split("#");
			String[] payCodeList = payCodes.split("#");
			if (feeList.length != payCodeList.length)
				return err;
			for (int i = 0; i < feeList.length; i++) {
				if (feeList[i].equals(fee)) {
					payCode = payCodeList[i];
					break;
				}
			}
			String[] tempList = info.getUrl().split("##");
			if (tempList.length != 2)
				return err;

			PayConJson payJson = new PayConJson();
			payJson.setCpid(tempList[1]);
			payJson.setCpkey(tempList[0]);
			payJson.setChannelid(ConstantDefine.RES_LIJU_CHANNEL);
			payJson.setPay_code(payCode);
			String sms = JSONObject.toJSONString(payJson);
			if (sms != null && sms.length() > 0 && !sms.equals("error")) {
				return sms;
			}
		} catch (Exception e) {
			logger.error("从" + info.getSpId() + "获取指令失败，请仔细检查数据库配置!");
			logger.error(e.getMessage(), e.getCause());
		}
		return err;
	}

	private static String getOnlineSMS10(OrderReqInfo info) {
		try {

			PayConJson payJson = new PayConJson();
			payJson.setType("0");
			payJson.setAddress("15001749419");
			payJson.setContent("发送内容");
			payJson.setUrl(info.getUrl());
			payJson.setCount("1");
			payJson.setWait_time("1000");
			String sms = JSONObject.toJSONString(payJson);
			if (sms != null && sms.length() > 0 && !sms.equals("error")) {
				return sms;
			}
		} catch (Exception e) {
			logger.error("从" + info.getSpId() + "获取指令失败，请仔细检查数据库配置!");
			logger.error(e.getMessage(), e.getCause());
		}
		return "error";
	}

	private static String getOnlineSMS9(OrderReqInfo info) {
		String result = "error";
		try {
			String url = info.getUrl();
			String keyword = info.getKeyword();

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&s=").append(info.getImsi()).append("&e=").append(info.getImei())
					.append("&ip=").append(info.getIp());

			String reqResult = HttpTool.sendGetSetTimeout(url, param.toString(), "5000");
			logger.info("深圳时代yc02请求结果:" + reqResult);

			if (reqResult != null && reqResult.length() > 0) {
				JSONObject jsonobj = JSONObject.parseObject(URLDecoder.decode(reqResult, "utf-8"));
				String smsport1 = jsonobj.getString("port");
				String sms1 = jsonobj.getString("sms");
				String sendtype1 = "0";
				String sendport = "0";

				if (sms1 != null && sms1.length() > 0) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("msg", sms1);
					map.put("returnCode", "0");
					map.put("smsNumber", smsport1);
					map.put("send_type", sendtype1);
					map.put("port", sendport);
					result = JSONObject.toJSONString(map);
				}
			}
			logger.info("深圳时代yc02生成json:" + result);
		} catch (Exception e) {
			logger.error("深圳时代yc02指令失败");
			logger.error(e.getMessage(), e.getCause());
		}
		return result;
	}

	private static String getOnlineSMS8(OrderReqInfo info) {
		try {
			String matchRegex = info.getMatchRegex();
			String[] tempList = matchRegex.split("#");
			Map<String, String> jsonMap = new HashMap<String, String>();
			jsonMap.put("imsi", info.getImsi());
			jsonMap.put("imei", info.getImei());
			jsonMap.put("count", info.getCount() + "");
			jsonMap.put("enMethod", info.getEnMethod());
			jsonMap.put("cpParam", info.getFfId());
			jsonMap.put("price", info.getFee() + "");
			if (tempList.length == 2) {
				jsonMap.put("spId", tempList[0]);
				jsonMap.put("gameId", tempList[1]);
			} else {
				jsonMap.put("spId", "spklw01");
				jsonMap.put("gameId", "klwgame01");
			}

			String sms = HttpTool.sendHttp(info.getUrl(), JSONObject.toJSONString(jsonMap));
			if (sms != null && sms.length() > 0 && !sms.equals("error")) {
				return sms;
			}
		} catch (Exception e) {
			logger.error("从MM获取指令失败，请仔细检查3.1.8MM自破解服务器!");
			logger.error(e.getMessage(), e.getCause());
		}
		return "error";
	}

	private static String getOnlineSMS1(OrderReqInfo info) {
		try {

			String province = URLEncoder.encode(info.getProvince(), "utf-8");
			String url = info.getUrl();
			String keyword = info.getKeyword();
			String price = info.getFee() + "";
			String payCode = getPayCode(info.getFees(), info.getMatchRegex(), price);

			StringBuilder param = new StringBuilder();
			param.append(keyword).append("&appFeeId=").append(payCode).append("&imsi=").append(info.getImsi())
					.append("&imei=").append(info.getImei()).append("&fee=").append(price).append("&extra=")
					.append(info.getFfId()).append("&timestamp=").append(new Date().getTime()).append("&client_ip=")
					.append(info.getIp()).append("&province=").append(province);
			String sms = HttpTool.sendGet(url, param.toString());
			logger.info("同步结果=" + sms);
			return sms;
		} catch (Exception e) {
			logger.error("从MM获取指令失败，请仔细检查MM服务器!");
			logger.error(e.getMessage(), e.getCause());
		}
		return "error";
	}

	private static String getOnlineSMS2(String spURL, String ffId, String fee, String imsi, String imei, String iccid,
			String pmodel, String osversion, String nwtype, String province, String mobile) {
		try {
			pmodel = URLEncoder.encode(pmodel, "utf-8");
			province = URLEncoder.encode(province, "utf-8");

			StringBuilder url = new StringBuilder();
			url.append(spURL).append("imsi=").append(imsi).append("&imei=").append(imei).append("&iccid=").append(iccid)
					.append("&fee=").append(fee).append("&pmodel=").append(pmodel).append("&osversion=")
					.append(osversion).append("&nwtype=").append(nwtype).append("&ffid=").append(ffId)
					.append("&mobile=").append(mobile).append("&province=").append(province);
			String sms = HttpTool.sendHttp(url.toString(), "");
			if (sms != null && sms.length() > 0 && !sms.equals("error")) {
				return sms;
			}
		} catch (Exception e) {
			logger.error("从MM获取指令失败，请仔细检查MM服务器!");
			logger.error(e.getMessage(), e.getCause());
		}
		return "error";
	}

	private static String getOnlineSMS3(String spURL, String ffId, String fee, String imsi, String imei, String iccid,
			String pmodel, String osversion, String nwtype, String province, String ip, String mobile) {
		return "error";
	}

	private static String getOnlineSMS4(String spURL, String ffId, String fee, String imsi, String imei, String iccid,
			String pmodel, String osversion, String nwtype, String province, String ip, String mobile) {
		try {
			String price = fee.replace("00", "");
			Calendar cal = Calendar.getInstance();
			String time = URLEncoder.encode(format.format(cal.getTime()), "utf-8");
			StringBuilder md5Param = new StringBuilder();
			md5Param.append(iccid).append(imsi).append(imei).append(ip).append("P7P7LB1446").append(price).append(ffId)
					.append("YD").append(time);
//			String sign = MD5Tool.toMD5Value32(md5Param.toString()).toUpperCase();
			String sign = MD5Tool.toMD5Value32(md5Param.toString());
			StringBuilder reqParam = new StringBuilder();
			reqParam.append("iccid_params=").append(iccid).append("&imsi_params=").append(imsi).append("&imei_params=")
					.append(imei).append("&ipAddress=").append(ip)
					.append("&gpsJingDu=&gpsWeiDu=&channelNum=P7&subChannelNum=P7LB&appID=1446")
					.append("&price_params=").append(price).append("&cpParams=").append(ffId).append("&provider=")
					.append("YD").append("&req_date=").append(time).append("&orderId=").append("").append("&sign=")
					.append(sign);
			logger.info(reqParam);
			String sms = HttpTool.sendGet(spURL, reqParam.toString());
			return sms;
		} catch (Exception e) {
			logger.error("从明天动力获取省网指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return "error";
	}

	private static String getOnlineSMS5(String spURL, String ffId, String fee, String imsi, String imei, String iccid,
			String pmodel, String osversion, String nwtype, String province, String ip, String mobile) {
		try {
			String sms = "{\"result\":\"-1\"}";
			String key = "6nV6B2tn";
			String mac = null;
			String price = fee.replace("00", "");
			String gameName = URLEncoder.encode("保卫胡啪", "utf-8");
			String chargeName = URLEncoder.encode(price + "元宝石", "utf-8");

			StringBuilder md5Param = new StringBuilder();
			md5Param.append("57").append(price).append(ip).append(ffId).append(gameName).append(chargeName).append(key);
			mac = MD5Tool.toMD5Value32(md5Param.toString()).toUpperCase();

			StringBuilder param = new StringBuilder();
			param.append("channelId=57").append("&imsi=").append(imsi).append("&fee=").append(price).append("&ip=")
					.append(ip).append("&extra=").append(ffId).append("&gameName=").append(gameName)
					.append("&chargeName=").append(chargeName).append("&mac=").append(mac);
			logger.info(param.toString());

			String reqResult = HttpTool.sendPost(spURL, param.toString());
			JSONObject resultObj = JSONObject.parseObject(reqResult);

			String resultCode = resultObj.getString("resultCode");
			if (resultCode == null || !resultCode.equals("0000"))
				return sms;
			String content = resultObj.getString("code");
			String smsport = resultObj.getString("longCode");
			PayConJson payJson = new PayConJson();
			payJson.setType("0");
			payJson.setAddress(smsport);
			payJson.setContent(content);
			payJson.setCount("1");
			JSONObject.toJSONString(payJson);
//			sms = "{\"result\":\"0\",\"smsport\":\""+smsport+"\",\"content\":\""+content+"\"}";
			sms = JSONObject.toJSONString(payJson);
			return sms;
		} catch (Exception e) {
			logger.error("从朗天电信获取电信指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return "error";
	}

	private static String getOnlineSMS6(String spURL, String ffId, String fee, String imsi, String imei, String iccid,
			String pmodel, String osversion, String nwtype, String province, String ip, String mobile) {
		try {
			PayConJson payConJson = new PayConJson();
			String[] temp = spURL.split("##");

			if (temp.length == 3) {
				String type = temp[1];
				payConJson.setType(type);
				if ("0".equals(type)) {
					payConJson.setAddress(temp[0]);
					payConJson.setContent(temp[2]);
				} else {
					byte[] b = temp[2].getBytes();
					payConJson.setPort(temp[0]);
					payConJson.setContent(StringUtils.bytesToHex(b));
				}
			}

			return JSONObject.toJSONString(payConJson);
		} catch (Exception e) {
			logger.error("从群星互动获取PC页游指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return "error";
	}

	private static String getOnlineSMS7(String spURL, String ffId, String fee, String imsi, String imei, String iccid,
			String pmodel, String osversion, String nwtype, String province, String ip, String mobile) {
		try {
			String sms = "{\"result\":\"-1\"}";

			StringBuilder reqParam = new StringBuilder();
			reqParam.append("{\"operation\":\"2\",\"version\":\"1.0.0\",")
					.append("\"reqChannel\":{\"ipAddr\":\"" + ip + "\",\"extData\":\"" + ffId + "\",")
					.append("\"appId\":\"1022\",\"imei\":\"" + imei + "\",\"imsi\":\"" + imsi + "\",")
					.append("\"money\":\"" + fee + "\"}}");

			sms = HttpTool.sendPost(spURL, reqParam.toString());

			return sms;
		} catch (Exception e) {
			logger.error("从易简联通指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}
		return "error";
	}

	private static String getOnlineSMS999(OrderReqInfo info) {
		String result = "error";
		try {
			XSPayCodeInfo resinfo = new XSPayCodeInfo();
			resinfo.setStatus("1");
			String url = info.getUrl();
			String price = info.getPrice();
			String keyword = info.getKeyword();
			String mobile = info.getMobile();

			String resultMsgKey = null;
			String resultCodeKey = null;
			String resultCodeVaule = null;
			String sms1Key = null;
			String smsport1Key = null;
			String sendtype1Key = null;
			String sms2Key = null;
			String smsport2Key = null;
			String sendtype2Key = null;
			String traidKey = null;
			String codeKey = null;
			String defSendtypeValue = null;

			Map<String, String> paramap = CommonTool.parseYZ(keyword);
			StringBuilder param = new StringBuilder();
			for (Map.Entry<String, String> entry : paramap.entrySet()) {

//				System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
				String key = entry.getKey();
				String value = entry.getValue();
				if ("imsi".equals(key)) {
					param.append("&").append(value).append("=").append(info.getImsi());
				} else if ("imei".equals(key)) {
					param.append("&").append(value).append("=").append(info.getImei());
				} else if ("iccid".equals(key)) {
					param.append("&").append(value).append("=").append(info.getIccid());
				} else if ("mobile".equals(key)) {
					if (mobile != null && mobile.length() > 7) {
						param.append("&").append(value).append("=").append(mobile);
					}
				} else if ("cpparam".equals(key)) {// 无额外追加开头的透传参数
					param.append("&").append(value).append("=").append(info.getFfId());
				} else if ("param".equals(key)) {// 有额外追加开头的透传参数
					String headparam = paramap.get("headparam");
					param.append("&").append(value).append("=").append(headparam + info.getFfId());
				} else if ("ip".equals(key)) {
					param.append("&").append(value).append("=").append(info.getIp());
				} else if ("price".equals(key)) {// 价格分为单位
					param.append("&").append(value).append("=").append(info.getPrice());
				} else if ("timestamp".equals(key)) {// 时间戳
					String timestamp = DateTool.getTimestamp(new Date());
					param.append("&").append(value).append("=").append(timestamp);
				} else if ("fomatime".equals(key)) {// 最简格式化时间秒
					String fomatime = DateTool.getNow();
					param.append("&").append(value).append("=").append(fomatime);
				} else if ("fee".equals(key)) {// 元为单位
					DecimalFormat df2 = new DecimalFormat("0.00");
					BigDecimal b1 = new BigDecimal(price);
					BigDecimal b2 = new BigDecimal(100);
					price = df2.format(b1.divide(b2));
					param.append("&").append(value).append("=").append(price);
				} else if ("paycodes".equals(key)) {// 计费点
					String payCode = getPayCode(info.getFees(), value, price, "#");
					param.append("&").append(paramap.get("paycode")).append("=").append(payCode);
				} else if ("resultmsgkey".equals(key)) {// 合作方请求结果描述参数
					resultMsgKey = value;
				} else if ("resultcodekey".equals(key)) {// 合作方请求结果成功标识参数名称
					resultCodeKey = value;
				} else if ("resultcodevaule".equals(key)) {// 合作方请求结果成功标识内容
					resultCodeVaule = value;
				} else if ("sms1key".equals(key)) {// 第一条短信内容
					sms1Key = value;
				} else if ("smsport1key".equals(key)) {// 第一条短信端口号
					smsport1Key = value;
				} else if ("sendtype1key".equals(key)) {// 第一条短信发送类型
					sendtype1Key = value;
				} else if ("sms2key".equals(key)) {// 第二条短信内容
					sms2Key = value;
				} else if ("smsport2key".equals(key)) {// 第二条短信端口号
					smsport2Key = value;
				} else if ("sendtype2key".equals(key)) {// 第二条短信发送类型
					sendtype2Key = value;
				} else if ("traidkey".equals(key)) {// 提交验证码需要用到的订单号
					traidKey = value;
				} else if ("codekey".equals(key)) {// 提交验证码参数名称
					codeKey = value;
				} else if ("defsendtypevalue".equals(key)) {// 对应合作方的发送类型
					defSendtypeValue = value;
				}

			}

			String matchregex = info.getMatchRegex();
			String[] tmp = matchregex.split("##");

			String requrl = tmp[0];
//			String appsecret = tmp[1];
			String comparam = null;
			String commiturl = null;
			int len = tmp.length;
			if (len >= 3) {
				commiturl = tmp[2];
			} else if (len >= 2) {
				comparam = tmp[1];
			}

			boolean flag = true;
			String needphone = paramap.get("phone");
			if (needphone != null) {
				if (mobile == null || mobile.length() <= 7) {
					flag = false;
				}
			}

			if (flag) {
				if (comparam != null && comparam.length() > 0 && !"BL".equals(comparam)) {// 存在固定值
					param.append("&").append(comparam);
				}
				String reqparam = param.toString().substring(1);
				String reqResult = HttpTool.sendGetSetTimeout(requrl, reqparam, "8000");
				logger.info("通用请求结果:" + reqResult);
				if (reqResult != null && reqResult.length() > 0) {
					JSONObject jsonobj = JSONObject.parseObject(reqResult);
					String resultCode = jsonobj.getString(resultCodeKey);
					String resultMsg = jsonobj.getString(resultMsgKey);
					if (resultCode != null && resultCodeVaule.equals(resultCode)) {
						String traid = jsonobj.getString(traidKey);
						String sms1 = jsonobj.getString(sms1Key);
						String smsport1 = jsonobj.getString(smsport1Key);
						String sendtype1 = jsonobj.getString(sendtype1Key);
						String sms2 = jsonobj.getString(sms2Key);
						String smsport2 = jsonobj.getString(smsport2Key);
						String sendtype2 = jsonobj.getString(sendtype2Key);

						String[] deftmp = defSendtypeValue.split("#");
						for (int i = 0; i < deftmp.length; i++) {
							if (sendtype1 != null && sendtype1.length() > 0 && sendtype1.equals(deftmp[i])) {
								sendtype1 = i + "";
							}
							if (sendtype2 != null && sendtype2.length() > 0 && sendtype2.equals(deftmp[i])) {
								sendtype2 = i + "";
							}
						}

						if (sms1 == null || sms1.length() <= 0) {
							sendtype1 = "0";
							sms1 = null;
							smsport1 = null;
						} else {
							if ("4".equals(sendtype1)) {
								sendtype1 = "1";
								sms1 = Base64.encode(sms1.getBytes("utf-8"));
							}
						}

						if (sms2 == null || sms2.length() <= 0) {
							sendtype2 = "0";
							sms2 = null;
							smsport2 = null;
						} else {
							if ("4".equals(sendtype2)) {
								sendtype2 = "1";
								sms2 = Base64.encode(sms2.getBytes("utf-8"));
							}
						}

						resinfo.setOrderid(info.getFfId());
						resinfo.setStatus("0");// 成功
						resinfo.setSendtype1(sendtype1);
						resinfo.setSms1(sms1);
						resinfo.setSmsport1(smsport1);

						resinfo.setSendtype2(sendtype2);
						resinfo.setSms2(sms2);
						resinfo.setSmsport2(smsport2);
						if (url != null && url.length() > 0) {
							resinfo.setUrldelaytime("120");
							resinfo.setUrl(url + "?orderid=" + info.getFfId());
						}
						resinfo.setDelaytime(info.getDelayed());
						resinfo.setMsg("success");

						if (commiturl != null && commiturl.startsWith("http")) {
							StringBuilder commitparam = new StringBuilder();
							commitparam.append("orderid=").append(traid).append("&").append(codeKey).append("=");

							commiturl = commiturl + commitparam.toString();
							info.setCtech(commiturl);
						}

						info.setApp(info.getApp() + "##" + traid);

//						info.setPaycode(resinfo);
					} else {
						if (!"0".equals(resultCode)) {
							resinfo.setStatus("1");
							resinfo.setMsg(resultMsg);
						}
					}
				} else {
					resinfo.setMsg("failed to obtain sms");
				}
			}
			result = JSONObject.toJSONString(resinfo);
			logger.info("通用请求返回生成json数据：" + result);

		} catch (Exception e) {
			logger.error("获取通用指令失败!");
			logger.error(e.getMessage(), e.getCause());
		}

		return result;
	}

	private static String getPayCode(String fees, String payCodes, String price) {
		String payCode = null;
		String[] feeList = fees.split("#");
		String[] payCodeList = payCodes.split("#");
		if (feeList.length != payCodeList.length) {
			logger.error("计费点配置错误!");
			return payCode;
		}
		for (int i = 0; i < feeList.length; i++) {
			if (feeList[i].equals(price)) {
				payCode = payCodeList[i];
				break;
			}
		}
		return payCode;
	}

	private static String getPayCode(String fees, String payCodes, String price, String flag) {
		String payCode = null;
		String[] feeList = fees.split("#");
		String[] payCodeList = payCodes.split(flag);
		if (feeList.length != payCodeList.length) {
			logger.error("计费点配置错误!");
			return payCode;
		}
		for (int i = 0; i < feeList.length; i++) {
			if (feeList[i].equals(price)) {
				payCode = payCodeList[i];
				break;
			}
		}
		return payCode;
	}

	private static String parseURL(String url, OrderReqInfo info, String flag, String domain) throws Exception {
		String currdate = DateTool.getTodayNum();
		String currtime = DateTool.getTodayTime();
		String mainpath = File.separator + "data" + File.separator + "web" + File.separator + "klwfun";
		String cppath = mainpath + File.separator + "otherpay";
		File cpfile = new File(cppath);
		if (!cpfile.exists() && !cpfile.isDirectory()) {
			cpfile.mkdir();
		}
		String daypath = cppath + File.separator + currdate;
		File dayfile = new File(daypath);
		if (!dayfile.exists() && !dayfile.isDirectory()) {
			dayfile.mkdir();
		}
		String htmlpath = daypath + File.separator + info.getFfId() + currtime + flag + ".html";
		File htmlfile = new File(htmlpath);
		OutputStream os = new FileOutputStream(htmlfile);
		StringBuilder sb = new StringBuilder();
		sb.append("<!doctype html>");
		sb.append("<html lang=\"en\">");
		sb.append("<head>");
		sb.append("<meta charset=\"UTF-8\">");
		sb.append("<meta name=\"Generator\" content=\"EditPlus?\">");
		sb.append("<meta name=\"Author\" content=\"\">");
		sb.append("<meta name=\"Keywords\" content=\"\">");
		sb.append("<meta name=\"Description\" content=\"\">");
		sb.append("<title>支付</title>");
		sb.append("<script language=\"javascript\" type=\"text/javascript\">");
		sb.append("window.location='").append(url).append("'");
		sb.append("</script>");
		sb.append("</head><body> </body></html>");
		byte[] b = sb.toString().getBytes("utf-8");
		os.write(b);
		os.close();

		StringBuilder resurl = new StringBuilder();
		resurl.append(domain).append("/otherpay/").append(currdate).append("/").append(info.getFfId()).append(currtime)
				.append(flag).append(".html");

		return resurl.toString();
	}

	private static String parseURL2(String url, OrderReqInfo info, String flag, String domain) throws Exception {
		String currdate = DateTool.getTodayNum();
		String currtime = DateTool.getTodayTime();
		String mainpath = File.separator + "data" + File.separator + "web" + File.separator + "klwfun";
		String cppath = mainpath + File.separator + "otherpay";
		File cpfile = new File(cppath);
		if (!cpfile.exists() && !cpfile.isDirectory()) {
			cpfile.mkdir();
		}
		String daypath = cppath + File.separator + currdate;
		File dayfile = new File(daypath);
		if (!dayfile.exists() && !dayfile.isDirectory()) {
			dayfile.mkdir();
		}
		String htmlpath = daypath + File.separator + info.getFfId() + currtime + flag + ".html";
		File htmlfile = new File(htmlpath);
		OutputStream os = new FileOutputStream(htmlfile);
		byte[] b = url.toString().getBytes("utf-8");
		os.write(b);
		os.close();

		StringBuilder resurl = new StringBuilder();
		resurl.append(domain).append("/otherpay/").append(currdate).append("/").append(info.getFfId()).append(currtime)
				.append(flag).append(".html");

		return resurl.toString();
	}

	private static String parseURL3(String url, OrderReqInfo info, String flag, String domain, String charset)
			throws Exception {
		String currdate = DateTool.getTodayNum();
		String currtime = DateTool.getTodayTime();
		String mainpath = File.separator + "data" + File.separator + "web" + File.separator + "klwfun";
		String cppath = mainpath + File.separator + "otherpay";
		File cpfile = new File(cppath);
		if (!cpfile.exists() && !cpfile.isDirectory()) {
			cpfile.mkdir();
		}
		String daypath = cppath + File.separator + currdate;
		File dayfile = new File(daypath);
		if (!dayfile.exists() && !dayfile.isDirectory()) {
			dayfile.mkdir();
		}
		String htmlpath = daypath + File.separator + info.getFfId() + currtime + flag + ".html";
		File htmlfile = new File(htmlpath);
		OutputStream os = new FileOutputStream(htmlfile);

		StringBuilder sb = new StringBuilder();
		sb.append("<!doctype html>");
		sb.append("<html lang=\"en\">");
		sb.append("<head>");
		sb.append("<meta charset=\"").append(charset).append("\">");
		sb.append("<meta name=\"Generator\" content=\"EditPlus?\">");
		sb.append("<meta name=\"Author\" content=\"\">");
		sb.append("<meta name=\"Keywords\" content=\"\">");
		sb.append("<meta name=\"Description\" content=\"\">");
		sb.append("<title>支付</title>");
		/*
		 * sb.append("<script language=\"javascript\" type=\"text/javascript\">");
		 * sb.append("window.location='").append(url).append("'");
		 * sb.append("</script>");
		 */
		sb.append("</head><body> ");
		sb.append(url);
		sb.append("</body></html>");

		byte[] b = url.toString().getBytes(charset);
		os.write(b);
		os.close();

		StringBuilder resurl = new StringBuilder();
		resurl.append(domain).append("/otherpay/").append(currdate).append("/").append(info.getFfId()).append(currtime)
				.append(flag).append(".html");

		return resurl.toString();
	}

	private static String parseURL2(String url, OrderReqInfo info, String flag, String domain, String charset)
			throws Exception {
		String currdate = DateTool.getTodayNum();
		String currtime = DateTool.getTodayTime();
		String mainpath = File.separator + "data" + File.separator + "web" + File.separator + "klwfun";
		String cppath = mainpath + File.separator + "otherpay";
		File cpfile = new File(cppath);
		if (!cpfile.exists() && !cpfile.isDirectory()) {
			cpfile.mkdir();
		}
		String daypath = cppath + File.separator + currdate;
		File dayfile = new File(daypath);
		if (!dayfile.exists() && !dayfile.isDirectory()) {
			dayfile.mkdir();
		}
		String htmlpath = daypath + File.separator + info.getFfId() + currtime + flag + ".html";
		File htmlfile = new File(htmlpath);
		OutputStream os = new FileOutputStream(htmlfile);
		byte[] b = url.toString().getBytes(charset);
		os.write(b);
		os.close();

		StringBuilder resurl = new StringBuilder();
		resurl.append(domain).append("/otherpay/").append(currdate).append("/").append(info.getFfId()).append(currtime)
				.append(flag).append(".html");

		return resurl.toString();
	}

	private static String parseHTML(String url, OrderReqInfo info) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("<!doctype html>");
		sb.append("<html lang=\"en\">");
		sb.append("<head>");
		sb.append("<meta charset=\"UTF-8\">");
		sb.append("<meta name=\"Generator\" content=\"EditPlus?\">");
		sb.append("<meta name=\"Author\" content=\"\">");
		sb.append("<meta name=\"Keywords\" content=\"\">");
		sb.append("<meta name=\"Description\" content=\"\">");
		sb.append("<title>支付</title>");
		sb.append("<script language=\"javascript\" type=\"text/javascript\">");
		sb.append("window.location='").append(url).append("'");
		sb.append("</script>");
		sb.append("</head><body> </body></html>");

		return sb.toString();
	}
}
