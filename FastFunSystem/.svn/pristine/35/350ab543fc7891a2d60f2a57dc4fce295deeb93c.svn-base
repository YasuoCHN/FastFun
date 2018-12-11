/**
 * FastFunSystem
 */
package com.klw.fastfun.pay.data.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.juice.orange.game.database.ConnectionResource;
import com.juice.orange.game.database.IJuiceDBHandler;
import com.klw.fastfun.pay.common.domain.CPInfo;

/**
 * @author klwplayer.com
 *
 * 2015年3月30日
 */
public class CPInfoDAO extends ConnectionResource {
	private static IJuiceDBHandler<CPInfo> HANDLER_CP_INFO = new IJuiceDBHandler<CPInfo>() {
		@Override
		public CPInfo handler(ResultSet rs) throws SQLException {
			CPInfo info = new CPInfo();
			info.setCpId(rs.getString("cp_id"));
			info.setName(rs.getString("name"));
			info.setCpFlag(rs.getString("cp_flag"));
			info.setIsOpen(rs.getInt("is_open"));
			info.setUrl(rs.getString("url"));
			info.setIsMagic(rs.getInt("is_magic"));
			info.setMagicRadio(rs.getInt("magic_radio"));
			info.setLocalSpId(rs.getString("local_sp_id"));
			info.setMagicSpId(rs.getString("magic_sp_id"));
			info.setSynRadio(rs.getInt("syn_radio"));
			info.setMemo(rs.getString("memo"));
			info.setBackMethod(rs.getInt("back_method"));
			info.setCityType(rs.getInt("city_type"));
			info.setCityHide(rs.getString("city_hide"));
			info.setSynOpen(rs.getInt("syn_open"));
			info.setSynProvince(rs.getString("syn_province"));
			info.setSynNum(rs.getString("syn_num"));
			return info;
		}
	};
	
	/** 根据cp_id查找对应的cp*/
	public CPInfo queryCPInfo(String cpId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from t_cp_info where cp_id=? and is_open>0");
		return queryForObject(sql.toString(), HANDLER_CP_INFO, cpId);
	}
	
	public void updateCp(CPInfo info) {
		String cpId = info.getCpId();
		if (cpId != null && cpId.length() > 0) {
			StringBuilder sql = new StringBuilder();
			sql.append("update t_cp_info ").append(" set is_show=1 ");
			if (info.getSynRadio() >= 0) {
				sql.append(", syn_radio=").append(info.getSynRadio());
			}
			if (info.getSynOpen() >= 0) {
				sql.append(", syn_open=").append(info.getSynOpen());
			}
			if (info.getSynProvince() != null) {
				sql.append(", syn_province='").append(info.getSynProvince()).append("'");
			}
			if (info.getSynNum() != null) {
				sql.append(", syn_num='").append(info.getSynNum()).append("'");
			}
			sql.append(" where cp_id='").append(cpId).append("'");
			executeUpdate(sql.toString());
		}
	}
}
