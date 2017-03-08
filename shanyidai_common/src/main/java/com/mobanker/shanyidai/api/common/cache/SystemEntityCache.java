/**
 * 
 */
package com.mobanker.shanyidai.api.common.cache;

import com.mobanker.shanyidai.api.common.cache.bean.SystemConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统字典表数据缓存
 * 
 * @author hantongyang
 * @data 2016/12/22
 */
public class SystemEntityCache {
	private static SystemEntityCache config;
	private Map<String, SystemConfig> sysConfigMap = null;

	private SystemEntityCache() {
		sysConfigMap = new HashMap<String, SystemConfig>();
	}

	public static SystemEntityCache getInstance() {
		if (config == null) {
			config = new SystemEntityCache();
		}
		return config;
	}

	/**
	 * 通过key获取对应的值
	 * 
	 * @param key
	 * @return
	 */
	public String getConfigValue(String key) {
		SystemConfig config = sysConfigMap.get(key);
		if (config != null) {
			String value = config.getValue();
			return value;
		}
		return "";

	}

	/**
	 * 从内存根据Key获取配置信息对象
	 * 
	 * @param key
	 * @return
	 */
	public SystemConfig getSystemConfig(String key) {
		return sysConfigMap.get(key);
	}

	/**
	 * 将配置信息写入内存
	 * 
	 * @param key
	 * @param config
	 */
	public void setCacheConfig(String key, SystemConfig config) {
		SystemConfig cacheConfig = sysConfigMap.get(key);
		if (cacheConfig == null) {
			sysConfigMap.put(key, config);
		} else {
			sysConfigMap.remove(key);
			sysConfigMap.put(key, config);
		}
	}

	/**
	 * 将配置信息写入内存
	 * 
	 * @param key
	 * @param value
	 */
	public void putCache(String key, String value) {

		SystemConfig config = new SystemConfig(key, value);
		SystemConfig cacheConfig = sysConfigMap.get(key);
		if (cacheConfig == null) {
			sysConfigMap.put(key, config);
		} else {
			sysConfigMap.remove(key);
			sysConfigMap.put(key, config);
		}
	}
}
