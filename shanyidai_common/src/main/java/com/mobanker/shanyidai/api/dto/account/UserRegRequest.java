package com.mobanker.shanyidai.api.dto.account;

import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class UserRegRequest extends Entity {

	private static final long serialVersionUID = -6309030803658752705L;
	//N  手机号码
	private String phone;
	//N  密码（6~10位数字和字母组合） 请求传入的是加密后32位密码
	private String password;
	//N  6位数字短信验证码
	private String verifyCode;


	/**
	 * 下载渠道，对应客户端传过来的channel，不使用头信息中传过来的downloadChannel
	 */
	private String channel;
}
