package com.klw.fastfun.pay.data.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.juice.orange.game.database.ConnectionResource;
import com.juice.orange.game.database.IJuiceDBHandler;
import com.klw.fastfun.pay.common.domain.CodeInfo;
import com.klw.fastfun.pay.common.domain.PayCodeInfo;
import com.klw.fastfun.pay.common.domain.PayReqInfo;

public class CodeHelperDAO extends ConnectionResource{
	private static IJuiceDBHandler<CodeInfo> HANDLER_CODE_INFO = new IJuiceDBHandler<CodeInfo>() {
		@Override
		public CodeInfo handler(ResultSet rs) throws SQLException {
			CodeInfo info = new CodeInfo();
			info.setSpId(rs.getString("sp_id"));
			info.setName(rs.getString("name"));
			info.setSpFlag(rs.getString("sp_flag"));
			info.setFee(rs.getString("fee"));
			info.setUrl(rs.getString("url"));
			info.setUrlNo(rs.getInt("url_no"));
			info.setPriority(rs.getInt("priority"));
			info.setIsOpen(rs.getInt("is_open"));
			info.setProvinceType(rs.getInt("province_type"));
			info.setProvinceHide(rs.getString("province_hide"));
			info.setProvinceOpen(rs.getString("province_open"));
			info.setMemo(rs.getString("memo"));
			info.setKeyword(rs.getString("keyword"));
			info.setMatchRegex(rs.getString("match_regex"));
			info.setDelayed(rs.getString("delayed"));
			info.setReqMethod(rs.getInt("req_method"));
			return info;
		}
	};
	
	private static IJuiceDBHandler<PayCodeInfo> HANDLER_PAY_CODE_INFO = new IJuiceDBHandler<PayCodeInfo>() {
		@Override
		public PayCodeInfo handler(ResultSet rs) throws SQLException {
			PayCodeInfo info = new PayCodeInfo();
			info.setBillId(rs.getString("bill_id"));
			info.setBillType(rs.getInt("bill_type"));
			info.setCodeNO(rs.getString("code_no"));
			info.setCodeType(rs.getInt("code_type"));
			info.setPrice(rs.getString("price"));
			info.setUrl(rs.getString("url"));
			info.setUrlNO(rs.getInt("url_no"));
			info.setPriority(rs.getInt("priority"));
			info.setProvinceType(rs.getInt("province_type"));
			info.setProvinceHide(rs.getString("province_hide"));
			info.setProvinceOpen(rs.getString("province_open"));
			info.setExtData(rs.getString("extData"));
			info.setCompany(rs.getString("company"));
			info.setMemo(rs.getString("memo"));
			info.setServerVersion(rs.getInt("server_version"));
			info.setDayLimit(rs.getInt("day_limit"));
			info.setMonthLimit(rs.getInt("month_limit"));
			info.setPayLimitOpen(rs.getInt("pay_limit_open"));
			info.setReporturl(rs.getString("reporturl"));
			info.setNewurl(rs.getString("newurl"));
			info.setFilterapp(rs.getString("filterapp"));
			info.setDelay(rs.getString("delay"));
			info.setErrtime(rs.getString("errtime"));
			info.setFilterPort(rs.getString("filter_port"));
			info.setrType(rs.getString("r_type"));
			info.setMatch(rs.getString("match"));
			info.setrAddr(rs.getString("r_addr"));
			info.setrCont(rs.getString("r_cont"));
			info.setrStart(rs.getString("r_start"));
			info.setrEnd(rs.getString("r_end"));
			info.setWaptype(rs.getString("waptype"));
			info.setConfig(rs.getString("config"));
			info.setPersent(rs.getString("persent"));
			info.setStep(rs.getString("step"));
			info.setIsSave(rs.getString("is_save"));
			info.setWaittime(rs.getString("waittime"));
			info.setDeductionnum(rs.getString("deductionnum"));
			info.setDeductioncommand(rs.getString("deductioncommand"));
			info.setNeedmt(rs.getString("needmt"));
			info.setOtherPort(rs.getString("other_port"));
			info.setDownnum(rs.getString("downnum"));
			return info;
		}
	};
	
	private static IJuiceDBHandler<Map<String,String>> HANDLER_BALANCE_STR = new IJuiceDBHandler<Map<String,String>>() {
		@Override
		public Map<String,String> handler(ResultSet rs) throws SQLException {
			Map<String,String> map=new HashMap<String,String>();
			map.put("beforeBalance", rs.getString("beforeBalance"));
			map.put("todayBalance", rs.getString("todayBalance"));
			return map;
		}
	};
	private static IJuiceDBHandler<String> HANDLER_SPID_STR = new IJuiceDBHandler<String>() {
		@Override
		public String handler(ResultSet rs) throws SQLException {
			return rs.getString("local_sp_id");
		}
	};
	private static IJuiceDBHandler<String> HANDLER_CREATEORDER_STR = new IJuiceDBHandler<String>() {
		@Override
		public String handler(ResultSet rs) throws SQLException {
			return rs.getString("t_success");
		}
	};
	private static IJuiceDBHandler<String> HANDLER_UPDATEORDER_STR = new IJuiceDBHandler<String>() {
		@Override
		public String handler(ResultSet rs) throws SQLException {
			return rs.getString("1");
		}
	};
	private static IJuiceDBHandler<Map<String,String>> HANDLER_SPINFO_MAP = new IJuiceDBHandler<Map<String,String>>() {
		@Override
		public Map<String,String> handler(ResultSet rs) throws SQLException {
			Map<String,String> map=new HashMap<String,String>();
			//return rs.getString("t_success");
			map.put("url", rs.getString("url"));
			map.put("merNo",rs.getString("mer_no"));
			map.put("key",rs.getString("key"));
			map.put("keyWord",rs.getString("keyword"));
			return map;
		}
	};
	/** 根据cpid查找对应的spid*/
	public String querySpIdByCpId(String cpid) {
		StringBuilder sql = new StringBuilder();
		sql.append("select local_sp_id from t_cp_info where cp_id=?");
		return queryForObject(sql.toString(), HANDLER_SPID_STR, cpid);
	}
	/** 根据cpid,spid查找对应的取款金额*/
	public Map<String,String> queryBalanceByCon(String cpid, String spid) {
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(beforetime_balance) beforeBalance,sum(today_balance)*0.8 todayBalance")
		.append(" from t_balance  ")
		.append(" where cp_id=? and sp_id=?");
		return queryForObject(sql.toString(), HANDLER_BALANCE_STR, cpid, spid);
	}
	//扣费并生成订单
	public String createOrder(Map<String,String> map){
		String sql="call create_order(?,?,?,?,?,?,?,?,?,?,?)";
		System.out.println(sql);
		System.out.println(map.get("cpid")+map.get("amount")+map.get("spid")+map.get("orderNo")+map.get("bankName")+map.get("bankAccount")+map.get("bankUserName")+map.get("operationStatus")+map.get("serviceCharge")+map.get("memo"));
		return queryForObject(sql, HANDLER_CREATEORDER_STR, map.get("cpid"), map.get("amount"),map.get("spid"),map.get("orderNo"),map.get("bankName"),map.get("bankAccount"),map.get("bankUserName"),map.get("operationStatus"),map.get("serviceCharge"),map.get("memo"),map.get("type"));
	}
	//修改订单状态
	public String updateOrder(Map<String,String> map){
		String sql="CALL update_order(?,?,?,?,?,?,?)";
		System.out.println(map.get("cpid")+map.get("cpid")+map.get("orderNo")+map.get("operationStatus")+map.get("amount")+map.get("serviceCharge")+map.get("type"));
		return queryForObject(sql, HANDLER_UPDATEORDER_STR, map.get("cpid"), map.get("spid"),map.get("orderNo"),map.get("operationStatus"),map.get("amount"),map.get("serviceCharge"),map.get("type"));
	}
	//查找代付通道信息
	public Map<String,String> selectDaifu(String spid){
		StringBuilder sql=new StringBuilder();
		sql.append("select url,mer_no,`key`,keyword from t_withdraw where sp_id=?");
		return queryForObject(sql.toString(), HANDLER_SPINFO_MAP,spid);
	}
	/** 根据sp_id查找对应的sp*/
	public CodeInfo queryCodeBySpId(String spId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ").append(localTable(spId))
		.append(" where sp_id=? and is_open>0");
		return queryForObject(sql.toString(), HANDLER_CODE_INFO, spId);
	}
	
	/** 根据sp_id查找对应的sp*/
	public CodeInfo queryCodeBySpid(String spId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ").append(localTable(spId))
		.append(" where sp_id=? ");
		return queryForObject(sql.toString(), HANDLER_CODE_INFO, spId);
	}
	
	/** 根据sp_id查找对应的sp*/
	public CodeInfo queryCodeInfo(String spId, int serVer) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ").append(localTable(spId))
		.append(" where sp_id=? and server_version=? and is_open>0");
		return queryForObject(sql.toString(), HANDLER_CODE_INFO, spId, serVer);
	}
	
	/** */
	public List<PayCodeInfo> getPayCodes(PayReqInfo info) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from t_pay_code ")
		.append(" where is_open>0 and code_type=? order by priority desc ");
		return queryForList(sql.toString(), HANDLER_PAY_CODE_INFO, info.getCarrierType());
	}

	public void updateCodeInfo(CodeInfo info) {
		StringBuilder sql = new StringBuilder();
		sql.append("update ").append(localTable(info.getSpId())).append(" set is_open=").append(info.getIsOpen())
		.append(" where sp_id='").append(info.getSpId()).append("'");
		executeUpdate(sql.toString());
	}
	
	
	
	private String localTable(String spId) {
		String prefix = spId.substring(0, 2);
		
		StringBuilder tableStr = new StringBuilder();
		tableStr.append("t_code").append("_").append(prefix);
		return tableStr.toString();
	}
	
}
