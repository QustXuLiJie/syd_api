package com.mobanker.shanyidai.api.dto.message;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/** 发送短信验证码
 * @author: liuyafei
 * @date 创建时间：2016年8月25日
 * @version 1.0 
 * @parameter  
 * @return  
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class UserSms extends SydRequest {
	private static final long serialVersionUID = 6395554404636235238L;
	private String userName;  //手机号*
	private String smsType;  //短信发送类型 voice：语音 sms：短信
	private String smsCode;   //短信类型smscode:注册resetpwcode：忘记密码resetphonecode：更换手机号 borrowcode：借款 activitycode：活动通用验证码 bindwchatcode：微信绑定验证码

}
