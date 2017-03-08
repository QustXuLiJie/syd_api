package com.mobanker.shanyidai.api.dto.account;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class UserLogin extends SydRequest {

	private static final long serialVersionUID = 4396457187442865945L;
	//手机号*
	private String userName;
	//密码*
	private String password;
}
