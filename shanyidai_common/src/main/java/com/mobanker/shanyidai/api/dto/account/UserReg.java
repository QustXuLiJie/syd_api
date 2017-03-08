package com.mobanker.shanyidai.api.dto.account;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class UserReg extends SydRequest {
	private static final long serialVersionUID = -5301377347697660500L;
	//手机号*
	private String userName;
	//密码*
	private String password;
	//验证码*
	private String smsCode;
	/**
	 * 下载渠道，与基础的channel不一样
	 */
	private String downloadChannel;

}
