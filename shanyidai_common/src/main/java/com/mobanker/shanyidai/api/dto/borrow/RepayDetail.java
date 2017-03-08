package com.mobanker.shanyidai.api.dto.borrow;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class RepayDetail extends SydRequest {

	private String amount;
	private String borrowNid;
	private String channelName;
	private String repayStatus;
	private String repayTime;
	private String weixinFee;
}
