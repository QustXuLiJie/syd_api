package com.mobanker.shanyidai.api.dto.account;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class UserModifyPhone extends SydRequest {

	private static final long serialVersionUID = 1245364167799094703L;
	private Long userId; //userId
	private String password; //密码*
	private String code; //code*
	private String newPhoneNum;//新手机号*
	private String shortId; //身份证号后四位
	private String smsCode; //验证码*
}
