package com.mobanker.shanyidai.api.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/** 
 * @author: liuyafei
 * @date 创建时间：2016年9月6日
 * @version 1.0 
 * @parameter  
 * @return  
 */
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true)
public class UserEvidence implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3832848415274207409L;
	private String imageURL;//图片地址
	private String subType;//资料说明
	private String attesId;//id
}
