/**
 * FastFunSystem
 */
package com.klw.fastfun.pay.data.ao;

import com.juice.orange.game.cached.MemcachedResource;
import com.klw.fastfun.pay.common.domain.CPInfo;

/**
 * @author klwplayer.com
 *
 * 2015年3月30日
 */
public class CPInfoAO extends BaseAO {
	public static final String cp_info_index = "cpid_";
	
	public CPInfo queryCPInfo(String cpId) {
		String indexKey = createIndexKey(cpId);
		CPInfo info = MemcachedResource.get(indexKey);
		
		if (info != null) return info;
		
		info = cpInfoDAO.queryCPInfo(cpId);
		if (info != null) MemcachedResource.save(indexKey, info);
		
		return info;
	}
	
	public void updateCp(CPInfo info) {
		cpInfoDAO.updateCp(info);
	}
	
	private String createIndexKey(String cpId) {
		StringBuilder index = new StringBuilder();
		index.append(cp_info_index).append(cpId);
		return index.toString();
	}
}
