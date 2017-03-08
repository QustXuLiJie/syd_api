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
public class UserDebitCard implements Serializable {

	
	private static final long serialVersionUID = 2886022401341990141L;
	private Long id;
	// 用户ID
	private Long userId;
	
	private String realName;
	private String cardId;
	// 借记卡卡号
	private String debitcardNum;
	private Integer debitcardPic;
	private Long verifyUserId;
	private String verifyRemark;
	private String verifyTime;
	private String addTime;
	
	// 借记卡开户行
	private String bankName;
	// 借记卡支行
	private String bankBranchName;
	// 借记卡省份
	private String province="";
	// 借记卡城市
	private String city="";
	
	private Integer provinceId;
	private Integer cityId;
	// 是否是最后一次借款的账号
	private Integer isLastRepay;
	private Integer bstatus;
	private String bindId;
	//是否支持一键还款
	private String isRepay;
	

	// 添加yyd_approve_debitcard状态
	private Integer status;
	private Integer ystatus;
	private String mstatus;
	private Integer debitcardNumStatus;
	private Integer debitcardPicStatus;
	
	private String mobileNo;
	private String addIp;
	
	private String channel;
	
	private String product;
}
