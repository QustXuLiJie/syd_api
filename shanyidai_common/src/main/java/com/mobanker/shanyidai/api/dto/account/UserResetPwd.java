package com.mobanker.shanyidai.api.dto.account;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class UserResetPwd extends SydRequest {

	private static final long serialVersionUID = -7851598983685838172L;
	//手机号*
	private String userName;
	//密码*
	private String newPassword;
	//验证码*
	private String smsCode;
	//身份证后四位（此字段在发送短信验证码后判断是否传输）*
	private String subCardId;
}
