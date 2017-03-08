package com.mobanker.shanyidai.api.dto.account;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class UserAvatar extends SydRequest {
	private static final long serialVersionUID = -4458266473572972445L;
	private Long userId; //用户id*
	private String fileName;//文件名称*
}
