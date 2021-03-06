package com.klw.fastfun.pay.data.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.juice.orange.game.database.ConnectionResource;
import com.juice.orange.game.database.IJuiceDBHandler;
import com.klw.fastfun.pay.common.domain.BaseStationInfo;
import com.klw.fastfun.pay.common.domain.CPInfo;
import com.klw.fastfun.pay.common.domain.CodeOnlyInfo;
import com.klw.fastfun.pay.common.domain.CustomInfo;
import com.klw.fastfun.pay.common.domain.MobileInfo;
import com.klw.fastfun.pay.common.domain.OrderReqInfo;
import com.klw.fastfun.pay.common.domain.PayLimitInfo;
import com.klw.fastfun.pay.common.domain.QQInfo;
import com.klw.fastfun.pay.common.domain.SDKUpdateInfo;

public class CommonDAO extends ConnectionResource{
	
	private static IJuiceDBHandler<CustomInfo> HANDLER_COMMON_CUS = new IJuiceDBHandler<CustomInfo>() {
		@Override
		public CustomInfo handler(ResultSet rs) throws SQLException {
			CustomInfo info = new CustomInfo();
			info.setCity(rs.getString("city"));
			info.setIccid(rs.getString("iccid"));
			info.setImsi(rs.getString("imsi"));
			info.setMessageId(rs.getString("message_id"));
			info.setMobile(rs.getString("mobile"));
			info.setPortType(rs.getString("port_type"));
			info.setPre(rs.getString("pre"));
			info.setSmsMessage(rs.getString("sms_message"));
			info.setSrcNumber(rs.getString("src_number"));
			return info;
		}
	};
	private static IJuiceDBHandler<CustomInfo> HANDLER_COMMON_BLACKCUS = new IJuiceDBHandler<CustomInfo>() {
		@Override
		public CustomInfo handler(ResultSet rs) throws SQLException {
			CustomInfo info = new CustomInfo();
			info.setImsi(rs.getString("imsi"));
			return info;
		}
	};
	
	private static IJuiceDBHandler<PayLimitInfo> HANDLER_COMMON_PAID = new IJuiceDBHandler<PayLimitInfo>() {
		@Override
		public PayLimitInfo handler(ResultSet rs) throws SQLException {
			PayLimitInfo info = new PayLimitInfo();
			info.setImsi(rs.getString("imsi"));
			info.setPaidPrice(rs.getInt("paid_price"));
			return info;
		}
	};
	
	private static IJuiceDBHandler<SDKUpdateInfo> HANDLER_COMMON_SDKPARAM = new IJuiceDBHandler<SDKUpdateInfo>() {
		@Override
		public SDKUpdateInfo handler(ResultSet rs) throws SQLException {
			SDKUpdateInfo info = new SDKUpdateInfo();
			info.setLenght(rs.getString("len"));
			info.setMd5(rs.getString("md5"));
			info.setMinVer(rs.getString("min_ver"));
			info.setSdkVer(rs.getString("sdk_ver"));
			info.setUrl(rs.getString("url"));
			return info;
		}
	};
	
	private static IJuiceDBHandler<BaseStationInfo> HANDLER_COMMON_BASSTA = new IJuiceDBHandler<BaseStationInfo>() {
		@Override
		public BaseStationInfo handler(ResultSet rs) throws SQLException {
			BaseStationInfo info = new BaseStationInfo();
			info.setCid(rs.getString("cid"));
			info.setLac(rs.getString("lac"));
			info.setMemo(rs.getString("memo"));
			info.setProvince(rs.getString("province"));
			info.setCity(rs.getString("city"));
			return info;
		}
	};
	
	private static IJuiceDBHandler<CodeOnlyInfo> HANDLER_COMMON_ONLY = new IJuiceDBHandler<CodeOnlyInfo>() {
		@Override
		public CodeOnlyInfo handler(ResultSet rs) throws SQLException {
			CodeOnlyInfo info = new CodeOnlyInfo();
			info.setBackUrl(rs.getString("back_url"));
			info.setName(rs.getString("name"));
			info.setSpId(rs.getString("sp_id"));
			info.setReqMethod(rs.getInt("req_method"));
			info.setSynRadio(rs.getInt("syn_radio"));
			return info;
		}
	};
	
	private static IJuiceDBHandler<CPInfo> HANDLER_COMMON_CPPID = new IJuiceDBHandler<CPInfo>() {
		@Override
		public CPInfo handler(ResultSet rs) throws SQLException {
			CPInfo info = new CPInfo();
			info.setCpId(rs.getString("cpid"));
			info.setLocalSpId(rs.getString("sp_id"));
			return info;
		}
	};
	
	private static IJuiceDBHandler<String> HANDLER_COMMON_STR = new IJuiceDBHandler<String>() {
		@Override
		public String handler(ResultSet rs) throws SQLException {
			return rs.getString("cpid");
		}
	};
	
	private static IJuiceDBHandler<OrderReqInfo> HANDLER_COMMON_ONLYORDER = new IJuiceDBHandler<OrderReqInfo>() {
		@Override
		public OrderReqInfo handler(ResultSet rs) throws SQLException {
			OrderReqInfo info = new OrderReqInfo();
			info.setFfId(rs.getString("ff_id"));
			return info;
		}
	};
	
	private static IJuiceDBHandler<QQInfo> HANDLER_COMMON_QQ = new IJuiceDBHandler<QQInfo>() {
		@Override
		public QQInfo handler(ResultSet rs) throws SQLException {
			QQInfo info = new QQInfo();
			info.setUsername(rs.getString("username"));
			return info;
		}
	};
	
	private static IJuiceDBHandler<MobileInfo> HANDLER_COMMON_MOB_PRO = new IJuiceDBHandler<MobileInfo>() {
		@Override
		public MobileInfo handler(ResultSet rs) throws SQLException {
			MobileInfo info = new MobileInfo();
			info.setMobile(rs.getInt("mobile"));
			info.setProvince(rs.getString("province"));
			info.setCity(rs.getString("city"));
			info.setCorp(rs.getString("corp"));
			info.setAreaCode(rs.getInt("area_code"));
			info.setPostCode(rs.getInt("post_code"));
			return info;
		}
	};
	 
	private static IJuiceDBHandler<MobileInfo> HANDLER_COMMON_MOB = new IJuiceDBHandler<MobileInfo>() {
		@Override
		public MobileInfo handler(ResultSet rs) throws SQLException {
			MobileInfo info = new MobileInfo();
			info.setPhone(rs.getString("mobile"));
			return info;
		}
	};
	
	
	/** 查询手机黑名单*/
	public MobileInfo queryBlackMobile(String mobile) {
		StringBuilder sql = new StringBuilder();
		sql.append("select mobile from t_black_mobile where mobile=? ");
		return queryForObject(sql.toString(), HANDLER_COMMON_MOB, mobile);
	}
	
	/** 查询QQ号*/
	public QQInfo queryQQInfo(OrderReqInfo info) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from t_qqzh_info ")
		.append(" where username=").append(info.getUsername())
		.append(" and password='").append(info.getPassword()).append("'");
		return queryForObject(sql.toString(), HANDLER_COMMON_QQ);
	}
	
	/** 根据手机号查询省份*/
	public MobileInfo getProvinceByPhone(int mobile) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from t_mobile_info ")
		.append(" where mobile=? limit 0,1 ");
		return queryForObject(sql.toString(), HANDLER_COMMON_MOB_PRO, mobile);
	}
	
	/** 根据手机号查询省份*/
	public MobileInfo query12306commit(String mobile) {
		StringBuilder sql = new StringBuilder();
		sql.append("select mobile from t_mobile_12306 ")
		.append(" where mobile=? limit 0,1 ");
		return queryForObject(sql.toString(), HANDLER_COMMON_MOB, mobile);
	}
	
	public void addCustomInfo(CustomInfo info) {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into t_custom_").append(info.getLocalTab())
		.append(" (mobile, message_id, sms_message, src_number, city, pre, port_type, imsi, iccid, ")
		.append("imsi_flag, create_time, update_time) ")
		.append("values(?,?,?,?,?,?,?,?,?,?,now(),now())");
		saveOrUpdate(sql.toString(), info.getMobile(), info.getMessageId(), info.getSmsMessage(),
				info.getSrcNumber(), info.getCity(), info.getPre(), info.getPortType(), info.getImsi(),
				info.getIccid(), info.getImsiFlag());
	}
	
	/** 根据imsi查找对应的客户信息*/
	public CustomInfo getCustomInfo(CustomInfo info) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from t_custom_").append(info.getLocalTab())
		.append(" where imsi=?");
		return queryForObject(sql.toString(), HANDLER_COMMON_CUS, info.getImsi());
	}
	
	/** 根据imsi查找对应的客户信息*/
	public CustomInfo getCustomInfoByMobile(CustomInfo info) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from t_custom_").append(info.getLocalTab())
		.append(" where mobile=? and sms_message=? ");
		return queryForObject(sql.toString(), HANDLER_COMMON_CUS, info.getMobile(), info.getSmsMessage());
	}
	
	/** 获取基站信息*/
	public BaseStationInfo getBaseStation(OrderReqInfo info) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from t_base_station_info")
		.append(" where lac=? and cid=? limit 0,1");
		return queryForObject(sql.toString(), HANDLER_COMMON_BASSTA, info.getBscLac(), info.getBscCid());
	}
	
	/** 根据imsi查找黑名单用户*/
	public CustomInfo checkCustomerInfo(String imsi) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from t_black_customer where imsi=? ");
		return queryForObject(sql.toString(), HANDLER_COMMON_BLACKCUS, imsi);
	}
	
	/** 查询sdk更新参数 */
	public SDKUpdateInfo getSDKUpdateParam(SDKUpdateInfo info) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from t_sdk_param where id=?");
		return queryForObject(sql.toString(), HANDLER_COMMON_SDKPARAM, info.getId());
	}
	
	/** 根据imsi查找用户日限*/
	public PayLimitInfo queryPaidByImsi(OrderReqInfo info) {
		StringBuilder sql = new StringBuilder();
		sql.append("select imsi,sum(fee) paid_price from ").append(localTable(info.getFfId()))
		.append(" where imsi=? and sp_id=? and create_time>? and syn_status<>4 and is_success=1 ");
		return queryForObject(sql.toString(), HANDLER_COMMON_PAID, info.getImsi(), info.getSpId(),
				info.getStartTime());
	}
	
	/** 根据imsi查找用户日限*/
	public PayLimitInfo queryOtherPaidByImsi(OrderReqInfo info) {
		StringBuilder sql = new StringBuilder();
		sql.append("select imsi,count(*) paid_price from ").append(localTable(info.getFfId()))
		.append(" where imsi=? and sp_id=? and create_time>? and syn_status<>4 and is_success=1 ");
		return queryForObject(sql.toString(), HANDLER_COMMON_PAID, info.getImsi(), info.getSpId(),
				info.getStartTime());
	}
	
	public CodeOnlyInfo queryCodeOnlyBySpid(String spId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select sp_id,name,back_url,syn_radio,req_method ")
		.append("from t_code_only ")
		.append("where sp_id=? ");
		return queryForObject(sql.toString(), HANDLER_COMMON_ONLY, spId);
	}
	
	public OrderReqInfo queryOnlyOrder(OrderReqInfo info) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ff_id ")
		.append("from t_order_only ")
		.append("where ff_id=? and sp_id=? ");
		return queryForObject(sql.toString(), HANDLER_COMMON_ONLYORDER, info.getFfId(), info.getSpId());
	}
	
	public String getCpid(String pid) {
		StringBuilder sql = new StringBuilder();
		sql.append("select cpid from t_pid_cpid where pid=? ");
		return queryForObject(sql.toString(), HANDLER_COMMON_STR, pid);
	}
	
	public CPInfo getCpByPid(String pid) {
		StringBuilder sql = new StringBuilder();
		sql.append("select cpid,sp_id from t_pid_cpid where pid=? ");
		return queryForObject(sql.toString(), HANDLER_COMMON_CPPID, pid);
	}
	
	public void addOnlyOrder(OrderReqInfo info) {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into t_order_only ")
		.append("(ff_id, cp_id, imsi, imei, iccid, mobile, sp_id, pmodel, osversion, nwtype, ctech, fee, ip, ")
		.append("province, cp_param, is_syn, is_success, pck, app, sdk_ver,create_time, update_time) ")
		.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now())");
		saveOrUpdate(sql.toString(), info.getFfId(), info.getCpId(), info.getImsi(), info.getImei(),
				info.getIccid(), info.getMobile(), info.getSpId(), info.getPmodel(), info.getOsversion(), 
				info.getNwtype(), info.getCtech(), info.getFee(), info.getIp(), info.getProvince(), info.getCpParam(), 
				info.getIsSyn(), info.getIsSuccess(), info.getPck(), info.getApp(), info.getSdkVer());
	}
	
	public void addMobile12306(MobileInfo info) {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into t_mobile_12306 ")
		.append("(mobile) ")
		.append("values(?)");
		saveOrUpdate(sql.toString(), info.getPhone());
	}
	
	public void updateOnlyOrder(OrderReqInfo info) {
		StringBuilder sql = new StringBuilder();
		sql.append("update t_order_only set is_success=?,update_time=now() where ff_id=? and sp_id=? ");
		saveOrUpdate(sql.toString(), info.getIsSuccess(), info.getFfId(), info.getSpId());
	}
	
	
	private String localTable(String ffId) {
		String month = ffId.substring(0, 2);
		
		StringBuilder tableStr = new StringBuilder();
		tableStr.append("t_order_req").append("_").append(month);
		return tableStr.toString();
	}

}
