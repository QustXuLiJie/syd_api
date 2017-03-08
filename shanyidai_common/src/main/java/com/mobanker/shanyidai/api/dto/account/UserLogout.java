package com.mobanker.shanyidai.api.dto.account;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class UserLogout extends SydRequest {

	private static final long serialVersionUID = 9078567034767715242L;
	//code*
	private String code;
	//用户id*
	private Long userId;
}
