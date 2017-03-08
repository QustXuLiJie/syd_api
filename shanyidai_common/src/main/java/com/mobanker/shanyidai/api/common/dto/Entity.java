package com.mobanker.shanyidai.api.common.dto;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public class Entity implements Serializable {

	private static final long serialVersionUID = -2874235891466454007L;

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
