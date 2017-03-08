package com.mobanker.shanyidai.api.dto.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/** 实名信息
 * @author: R.Core
 * @date 创建时间：2016年9月5日
 * @version 1.0 
 * @parameter  
 * @return  
 */
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true)
public class RealnameInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6503246181519682994L;
	
	private Long id;
	private Long userId;
	private String realName;
	private String userName;
	
	private String cardId;
	private String cardPic;
	private String cardPic1;
	private String cardPic2;
	private String realPic;
	private Integer realPicStatus;
	private String cardPicUrl;
	private String cardPic1Url;
	private String cardPic2Url;
	private String realPicUrl;
	//真实图片认证ID
	private String realPicAttesId;
	private Integer id5Status;
	private Integer status ;
	private String type;
	private String sex;
	private Integer verifyUserId ;
	private String verifyRemark;
	private String verifyTime;
	private Integer verifyId5UserId ;
	private String verifyId5Time;
	private String verifyId5Remark;
	private String addTime;
	private String addIp;
	private Integer cardIdStatus;
	private String idCardAddress;
	private Integer del;
	
	
	private Integer addressProvince;
	private String addressProvinceName;
	private String addressCityName;
	private String addressDistrictName;
	private Integer addressCity;
	private Integer addressDistrict;
	private String address;
	
	private String channel;
	
	private String product;
	
	private int completeStatus;//0:未完成,1:已完成
	
	private Integer lateDays;
	
	private Double repayCapital;
	
	private String edu;
	private String marriage;
	private String birthday;
	private String nation;
	
	private String fromChannel;//来源渠道
}
