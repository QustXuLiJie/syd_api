package com.mobanker.shanyidai.api.dto.security;

import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


/** 握手信息
 * @author: hanty
 * @date 创建时间 2016-12-12 20:12
 * @version 1.0
 * @parameter  
 * @return  
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class ShakeInfo extends Entity {

	private static final long serialVersionUID = 5354478638639351816L;
	private String code;
	private String key;
	private Long nowTime;     //当前时间戳
	private Integer effecttime=1800;//有效时常秒
	
}
