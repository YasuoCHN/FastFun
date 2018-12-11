/**
 * FastFunCommon
 */
package com.klw.fastfun.pay.common.exception;

/**
 * @author klwplayer.com
 *
 *         2015年4月4日
 */
public class ExceptionTool {
	public static StringBuffer getTraceInfo(Exception e) {
		StringBuffer sb = new StringBuffer();
		StackTraceElement[] stacks = e.getStackTrace();
		for (int i = 0; i < stacks.length; i++) {
			sb.append("class: ").append(stacks[i].getClassName())
					.append("; method: ").append(stacks[i].getMethodName())
					.append("; line: ").append(stacks[i].getLineNumber())
					.append(";  Exception: ").append("\n");
		}
		return sb;
	}

	public static String getExceptionMessage(Exception e) {
		String message = e.toString();
		if (message.lastIndexOf(":") != -1)
			message = message.substring(0, message.lastIndexOf(":"));
		return getTraceInfo(e).append(message).toString();
	}
}
