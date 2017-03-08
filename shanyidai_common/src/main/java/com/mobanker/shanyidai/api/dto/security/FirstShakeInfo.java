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
public class FirstShakeInfo extends Entity {

	private static final long serialVersionUID = -6215801539107056747L;
	private String num1;
	private String num2;
	private Integer tailor = 8; //截取位数
	private String uuid; //客户端标识
	private Long nowTime; //当前时间戳
	private String publicKey; //公钥
	private Integer effecttime = 600;//有效时常秒
	private String signType = "RSA"; //加密类型(默认RSA)

}
