package com.mobanker.shanyidai.api.dto.gather;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @description: 设备激活入参DTO
 * @author: xulijie
 * @create time: 9:53 2017/2/27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class AppOperationActivationReq extends SydRequest{
    private String addProduct;//产品来源

    private String mobileDeviceId;//android采集：mac,imei iOS采集：idfa,idfv
    private String appId;//APP ID
    private String udid;//IOS设备号
    private String idfa;//IOS广告标示符
    private String openudid;//OpenUDID
    private String ua;//用户终端设备标识：user_agent
    private String uaKey;//用户终端设备标识匹配关键字
    private String message;//
    private String source;//推广商
    private String nodeType;//激活节点位置
}
