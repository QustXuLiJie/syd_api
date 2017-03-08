package com.mobanker.shanyidai.api.dto.gather;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class TerminalInfo extends SydRequest {

	private static final long serialVersionUID = 19970321630133600L;
	private Long userId; //用户id
	private String terminalInfo;//终端信息json
}
