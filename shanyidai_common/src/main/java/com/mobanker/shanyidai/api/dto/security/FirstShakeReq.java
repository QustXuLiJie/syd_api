package com.mobanker.shanyidai.api.dto.security;

import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: hanty
 * @date 创建时间 2016-12-12 20:16
 * @version 1.0 
 * @parameter  
 * @return  
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class FirstShakeReq extends Entity {
	private static final long serialVersionUID = -5282651417804582377L;
	private String num1;
}
