package com.mobanker.shanyidai.api.dto.auth;

import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @desc: 语音识别
 * @author: Richard Core
 * @create time: 2017/2/13 19:55
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class VoiceCompareResult extends Entity {
    private Long userId;//用户id
    private String orderNo;// 订单号     DKHJQ20170123140001,
    private String voiceContent;// 转义内容 语音内容
    private String status;//进度状态
    private String statusDesc;//状态描述
}
