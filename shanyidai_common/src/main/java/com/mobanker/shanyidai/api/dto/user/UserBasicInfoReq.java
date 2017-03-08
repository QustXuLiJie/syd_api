package com.mobanker.shanyidai.api.dto.user;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class UserBasicInfoReq extends SydRequest {

	private static final long serialVersionUID = -4913988817482438355L;

	private Integer provinceId;
	private Integer cityId;
	private Integer districtId;

	private String address;

	private String edu;
	private String marry;

}
