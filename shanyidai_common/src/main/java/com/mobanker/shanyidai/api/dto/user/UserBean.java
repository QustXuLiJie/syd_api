package com.mobanker.shanyidai.api.dto.user;

import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class UserBean extends Entity {

	private static final long serialVersionUID = -3925560694332114797L;

	// 用户ID
	private Long userId;

	private String userName;

}
