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
public class UserDebitcardSet implements Serializable {

	private Long userId;//*
	private String ip;//*
	private String product;//*
	private String channel;//*
	private String cardNum;//借记卡卡号*
	private String bankName;//开户银行名称
	private String phoneNum;//用户绑定手机号
}
