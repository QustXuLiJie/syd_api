package com.mobanker.shanyidai.api.dto.user;

import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class UserBasicInfoRsp extends Entity {

	private static final long serialVersionUID = 6503246181519682994L;

	private String phone; //手机号（用户名即登录账号）
	private String password; //密码
	private String userStatus; //用户状态

	private String realname; //真实姓名
	private String idCard; //身份证号
	private Integer realnameTimes; //实名认证次数
	private String idCardAddress; //身份证地址

	private Integer provinceId;
	private String province;
	private Integer cityId;
	private String city; //
	private Integer addressDistrict; //
	private String district;

	private String address; //住址
	private String addressFull;
	private String addressTel; //

	private String edu; //学历
	private String marriage; //婚姻

	private String sex; //性别
	private String birthday; //生日
	private String whetherLost; //是否失联

	private String jobTypeId; //工作类型id
	private String companyName; //公司名称
	private String companyTelCountry; //公司电话区号
	private String companyTelFixed; //公司电话固定号码
	private String companyTelSuffix; //公司电话分机号
	private String jobTitle; //工作职位

	private String jobInfoAllIsExpire; //工作信息是否有效期
	private String realnameAllStatus; //实名认证状态 0 实名认证过 1 表示没有认证过
	private String idCardStatus; //身份证号状态
}
