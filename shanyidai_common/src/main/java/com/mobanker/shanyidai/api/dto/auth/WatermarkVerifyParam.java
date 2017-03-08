package com.mobanker.shanyidai.api.dto.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @desc:
 * @author: Richard Core
 * @create time: 2017/2/22 14:22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class WatermarkVerifyParam implements Serializable {
    private String livenessDataId;//使用file、url方式返回的图片的id

    private String realName;//真实姓名
    private String idCardNo;//身份证号
}
