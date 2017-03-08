/**
 * 
 */
package com.mobanker.shanyidai.api.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 安全处理拦截器
 * 
 * @author chenjianping
 * @data 2016年12月11日
 */
public class SecurityIntercept extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		return true;
	}
}
