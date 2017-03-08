package com.mobanker.shanyidai.api.dto.message;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
/**
 * 验证验证码
 * @author: R.Core
 * @date 创建时间：2016-12-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public final class ValidateSmsCaptcha extends SydRequest {

	/**
	 * 验证码用途.<br>
	 * 注册：register<br>
	 * 忘记密码：forgetPwd<br>
	 * 修改手机号：modifyPhone<br>
	 * 借款：borrow<br>
	 * 微信绑定手机号：bindWchat<br>
	 * 活动: activity<br>
	 * 找回支付密码：findPayPwd<br>
	 */
	private String captchaUse;
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 唯一编码
	 */
	private String captcha;

	private String osType;// 手机系统	可为空
	private String osVer;//手机系统版本	可为空
	private String deviceAll;//设备信息	可为空
	private String lat;//纬度		可为空
	private String lon;//经度		 可为空
	private String map;//经纬度来自地图		 可为空
}
