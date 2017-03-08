/**
 * 
 */
package com.mobanker.shanyidai.api.common.cache.bean;

import java.util.Date;

import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * yyd_system字典表缓存对象类
 * 
 * @author hantongyang
 * @data 2016/12/22
 */
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true)
public class SystemConfig extends Entity{
	public SystemConfig(String name, String value) {
		this.name = name;
		this.value = value;
		this.updateDate = new Date();
	}

	/**
	 * 变量名称
	 */
	private String name;
	/**
	 * 变量值
	 */
	private String value;
	/**
	 * 更新时间
	 */
	private Date updateDate;
}
