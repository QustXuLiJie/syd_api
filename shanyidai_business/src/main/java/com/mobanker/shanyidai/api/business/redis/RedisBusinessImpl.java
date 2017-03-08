package com.mobanker.shanyidai.api.business.redis;

import com.mobanker.framework.redis.template.RedisClientTemplate;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static org.apache.zookeeper.ZooDefs.OpCode.exists;


/**
 * 所有redis操作
 * 
 * @author: hanty
 * @date 创建时间：2016-12-13 13:50
 * @version 1.0
 * @parameter
 * @return
 */
@Component
public class RedisBusinessImpl implements RedisBusiness {

	@Resource
	private RedisClientTemplate redisClientTemplate;

	/**
	 * 获取redis值
	 * 
	 * @author: hanty
	 * @date 创建时间：2016-12-13 13:50
	 * @version 1.0
	 * @parameter
	 * @return
	 */
	public String getValue(String key) {
		return redisClientTemplate.get(key);
	}

	@Override
	public <T> T getValue(String key, Class<T> clazz) {
		String json = getValue(key);
		return BeanHelper.parseJson(json, clazz);
	}

	/**
	 * 设置reids值
	 * 
	 * @author: hanty
	 * @date 创建时间：2016-12-13 13:50
	 * @version 1.0
	 * @parameter
	 * @return
	 */
	public void setValue(String key, String msg) {
		setValue(key, msg, 0);

	}

	/**
	 * 获取redis值
	 * 
	 * @author: hanty
	 * @date 创建时间：2016-12-13 13:50
	 * @version 1.0
	 * @parameter
	 * @return
	 */
	public void setValue(String key, String msg, int time) {
		redisClientTemplate.set(key, msg);
		if (time != 0)
			redisClientTemplate.expire(key, time);
	}

	/**
	 * 删除redis值
	 * 
	 * @author: hanty
	 * @date 创建时间：2016-12-13 13:50
	 * @version 1.0
	 * @parameter
	 * @return
	 */
	public void delValue(String key) {
		if(exists(key)){
			redisClientTemplate.del(key);
		}
	}

	@Override
	public boolean exists(String key) {
		if (StringUtils.isBlank(key)) {
			return false;
		}
		return redisClientTemplate.exists(key);
	}

}
