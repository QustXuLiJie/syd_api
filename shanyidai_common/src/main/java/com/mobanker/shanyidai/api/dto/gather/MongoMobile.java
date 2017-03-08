package com.mobanker.shanyidai.api.dto.gather;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @version 1.0
 * @description： 手机设备信息采集
 * @author: R.Core
 * @date 2017-2-8 10:36:56
 * @parameter
 * @return
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class MongoMobile extends SydRequest {

    //必传信息
    private String _id;//
    private Long user_id;//用户id
    private Long addtime;//添加时间精确到秒
    private String type;//使用位置：register：注册 login：登录 borrow：借款
    //激活/注册/登录/申请借款
    private String mobileMac;//MAC地址(Android传)
    private String mobileImsi;//IMSI信息(Android 传)
    private String mobileNumber;//当前手机手机号(Android传)
    private String mobileDeviceId;//android采集：mac,imei iOS采集：idfa,idfv
    private String networkStatus;//网络连接状态
    private String mobileIp;//手机IP地址
    private Object qqNumbers;//qq列表(Android传)

    //注册/登录/申请借款
    private String mobileModel;//手机型号

    //注册成功/申请借款
    private Object appNames;//已安装的app列表
    private Object openingAppNames;//手机上打开的APP
    private String cardNumbers;//手机卡个数
    private String startTime;//上次启动时间
    private String networkType;//网络类型
    private String processor;//处理器型号
    private String CPURate;//CPU主频
    private String cameraFront;//前置摄像头
    private String cameraBack;//后置摄像头
    private String screenSize;//屏幕尺寸
    private String screenResolution;//屏幕分辨率

    //登录/申请借款
    private Object photos;//照片目录
//	private String add_channel;
//	private Long login_time;

}
