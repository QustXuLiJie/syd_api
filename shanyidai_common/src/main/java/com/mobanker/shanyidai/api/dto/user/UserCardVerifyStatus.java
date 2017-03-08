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
public class UserCardVerifyStatus implements Serializable {

	
	private static final long serialVersionUID = -3243890214983991364L;
	private String cardNo;//卡号
	private String bankName;//开户银行名称
	/**
	 * 认证状态： -1::未认证  0:认证中 1:成功 2:无效 3:超时 4:其他原因
	 */
	private String status;
	/**
	 * 更新时间，备用
	 */
	private Long updateTime;
}
