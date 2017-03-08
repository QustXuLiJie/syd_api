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
public class SecondShakeInfo extends Entity {

	private static final long serialVersionUID = -4017825548951603355L;
	private String secret;  //服务端产生密钥
	private String signData;//3des加密产生的数据
	
}
