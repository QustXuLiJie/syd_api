/**
 *
 */
package com.mobanker.shanyidai.api.common.tool;

import javax.servlet.http.HttpServletRequest;

/**
 * IP解析处理相关工具类
 * @author R.Core
 *
 */
public class IPUtils {

	/**
	 * 获取用户真实IP地址
	 * @date 2015年12月14日
	 * @param request
	 * @return
	 */
	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

}
