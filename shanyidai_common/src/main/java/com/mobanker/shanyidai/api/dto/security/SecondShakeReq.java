package com.mobanker.shanyidai.api.dto.security;

import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: hanty
 * @date 创建时间 2016-12-12 20:12
 * @version 1.0 
 * @parameter  
 * @return  
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class SecondShakeReq extends Entity {
	private static final long serialVersionUID = -6562604102819392518L;
	private String uuid;
	private String num3;
	private String sign;
}
