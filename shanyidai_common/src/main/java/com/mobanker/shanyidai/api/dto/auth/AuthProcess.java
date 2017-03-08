package com.mobanker.shanyidai.api.dto.auth;

import com.mobanker.shanyidai.api.enums.AuthProcessEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @desc:人脸识别和语音识别进度  redis 缓存类
 * @author: Richard Core
 * @create time: 2017/2/23 10:39
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class AuthProcess implements Serializable {
    private Long userId;//用户id
    private AuthProcessEnum authProcess;//认证进度

}
