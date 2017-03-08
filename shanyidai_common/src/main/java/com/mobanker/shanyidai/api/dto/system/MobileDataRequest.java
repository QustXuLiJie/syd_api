package com.mobanker.shanyidai.api.dto.system;

import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class MobileDataRequest extends Entity {
	private static final long serialVersionUID = -1955868142771974230L;
	//Y String(32) 手机IP地址(建议由服务端自行记录，手机端采集的是局域网地址没有意义)
	private String mobileIp;
	//Y String(255) 手机系统( 包含系统名称和版本号比如：iOS_9.3.3,Android_4.4.0)
	private String mobileOs;
	//Y String(255) 网络类型
	private String mobileNetworkType;
	//Y String(255) 
	//deveice_id信息  信息格式：
	//android采集：mac,imei
	//iOS采集：idfa,idfv
	private String mobileDeviceId;
	//Y String(11) 手机当前信息
	private String mobileTime;
	//Y String(255) MAC地址(android传，iOS获取不到)
	private String mobileMac;
	//Y String(255) IMSI信息(android 传，iOS获取不到)
	private String mobileImsi;

	private String mobileImei;
	private String mobileIdfa;

	//Y String(255) 手机名称
	private String mobileName;
	//Y String(11) 当前手机手机号(android可获取)
	private String mobileNumber;
	//Y String(255) 手机型号
	private String mobileModel;
	//Y String(255) 手机型号
	private String mobileType;
	private String channel;
	private String appVersion;
	private String appChannel;
	private String appPorduct;
	private String appIp;
	/**
	 * 图片列表
	 */
	private Object photos;
	/**
	 * app名称列表
	 */
	private Object appNames;
	/**
	 * 那个地方传上来的信息
	 */
	private String type;
	/**
	 * QQ列表
	 */
	private Object qqNumbers;
	
	public void splitMobileDeviceId(String mobileDeviceId, String appType) {
		if (StringUtils.isNotBlank(mobileDeviceId)) {
			String[] msg = mobileDeviceId.split(",");
			if ("android".equals(appType)) {
				this.mobileMac = msg[0];
				if (msg.length > 1) {
					this.mobileImei = msg[1];
				}
			} else if ("ios".equals(appType)) {
				this.mobileIdfa = msg[0];
			}
		}
	}

}
