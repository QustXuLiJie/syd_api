package com.mobanker.shanyidai.api.dto.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author liuhanqing
 * @description 语音识别的结果
 * @time 2017/2/14.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class VoiceAuthResult {
    private String channel;// kdxf,
    private String orderNo;// 订单号     DKHJQ20170123140001,
    private String orderStatus;// 订单状态
    private String orderType;
    private String transerialsId;// 流水号 2017012315015794035
    private String resultMsg;

}
