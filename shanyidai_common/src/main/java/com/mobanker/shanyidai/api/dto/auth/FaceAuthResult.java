package com.mobanker.shanyidai.api.dto.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @desc:
 * @author: Richard Core
 * @create time: 2017/2/20 17:19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class FaceAuthResult implements Serializable {
    private String livenessDataId;//使用file、url方式返回的图片的id
    private String requestId;//本地请求的id
    private String status;//     状态，正常为 OK 枚举  FaceAuthResultStatusEnum
    private String reason;//失败则会返回这个原因字段
    private String statusDesc;//状态描述
    private String transerialsId;//流水号
    private String channel;//认证渠道 kdxf shangtang==

    private String score;//分数。取值范围是0~1。值越大，表示被hack的可能性越大。
    private String hackScore;//防hack得分
    private String verifyScore;//置信度，值为 0~1，值越大表示两张照片是同一个人的可能性越大
    private String imageId;//使用file、url方式返回的公安部后台预留的带水印照片的id
}
