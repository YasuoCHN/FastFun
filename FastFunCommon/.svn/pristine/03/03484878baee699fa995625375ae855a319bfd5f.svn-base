/**
 * KLWPaySystem
 */
package com.klw.fastfun.pay.common.tool;

import java.util.Comparator;

import com.klw.fastfun.pay.common.domain.CodeInfo;

/**
 * @author klwplayer.com
 *
 * 2014年10月31日
 */
public class PriorityComparator<T> implements Comparator<T> {
	@Override
	public int compare(T o1, T o2) {
		int first = 0;
		int second = 0;
		if (o1 instanceof CodeInfo) {
			first = ((CodeInfo)o1).getPriority();
			second = ((CodeInfo)o2).getPriority();
		} 
		
		if (first < second) {
			return 1;
		} else if (first > second) {
			return -1;
		}
		return 0;
	}
}
