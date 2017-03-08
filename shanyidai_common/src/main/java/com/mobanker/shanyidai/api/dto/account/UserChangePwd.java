package com.mobanker.shanyidai.api.dto.account;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class UserChangePwd extends SydRequest {

	private static final long serialVersionUID = -2377685328580581209L;
	//用户id
	private Long userId;
	//手机号*
	private String oldPasswd;
	//密码*
	private String newPasswd;
	private String repeatPasswd;//再次输入密码*
}
