package com.mobanker.shanyidai.api.common.enums;

/**
 * @author Richard Core
 * @description 拦截方式声明：加解密和登录验证
 * @date 2016/12/12 14:34
 */
public enum SignLoginEnum {
	/**
	 * 校验加密，不校验登录
	 */
	SIGN,
	/**
	 * 校验登录，不校验加密
	 */
	LOGIN,
	/**
	 * 不校验加密和登录
	 */
	NOTALL,
	/**
	 * 默认，校验加密和登录
	 */
	ALL


}
