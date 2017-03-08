package com.mobanker.shanyidai.api.dto.account;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuyafei
 * @date 创建时间：2016年8月25日
 * @version 1.0
 * @parameter
 * @return
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class UserCodeBean extends SydRequest {

	private static final long serialVersionUID = 8703525749442250853L;
	private Long userId;
	private Long nowTime;
}
