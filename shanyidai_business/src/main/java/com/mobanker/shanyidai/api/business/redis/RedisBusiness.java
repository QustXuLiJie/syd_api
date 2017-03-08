package com.mobanker.shanyidai.api.business.redis;

/**
 * @author: hanty
 * @date 创建时间：2016-12-13 13:50
 * @version 1.0
 * @parameter
 * @return
 */
public interface RedisBusiness {

	public String getValue(String key);

	public <T>T getValue(String key, Class<T> clazz);

	public void setValue(String key, String msg);

	public void setValue(String key, String msg, int time);

	public void delValue(String key);

	public boolean exists(String key);
}
