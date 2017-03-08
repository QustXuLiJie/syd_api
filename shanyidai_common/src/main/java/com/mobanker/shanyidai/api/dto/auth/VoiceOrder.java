package com.mobanker.shanyidai.api.dto.auth;

import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @desc: 语音解析结果
 * @author: Richard Core
 * @create time: 2017/2/10 10:56
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class VoiceOrder extends Entity {
    private String channel;// kdxf,
    private String audioId;//音频id  1493870,
    private String orderNo;// 订单号     DKHJQ20170123140001,
    private String result;// 转义内容 语音内容
    private String status;//当status=1 表示查询音频成功。当status=0 表示查询音频失败。
    private String transerialsId;// 流水号 2017012315015794035
}
