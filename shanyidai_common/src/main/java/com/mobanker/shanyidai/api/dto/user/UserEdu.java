package com.mobanker.shanyidai.api.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/** 
 * @author: liuyafei
 * @date 创建时间：2016年8月30日
 * @version 1.0 
 * @parameter  
 * @return  
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class UserEdu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5566566566127622715L;
	private Long userId;  //userId*
	private String edu;  //学历*
	private String ip;        //ip*
	private String product;   //产品*
	private String channel;   //渠道*
}
