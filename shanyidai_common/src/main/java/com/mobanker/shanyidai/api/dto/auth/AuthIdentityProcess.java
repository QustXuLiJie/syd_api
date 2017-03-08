package com.mobanker.shanyidai.api.dto.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @desc: 身份识别进度
 * @author: Richard Core
 * @create time: 2017/2/27 20:32
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class AuthIdentityProcess implements Serializable {
    private Long userId;//用户id
    private String identityProcess;//身份识别进度
    private String identityProcessDesc;//身份识别进度描述
    private String faceProcess;//人脸识别进度
    private String faceProcessDesc;//人脸识别进度描述
    private String voiceProcess;//语音识别进度
    private String voiceProcessDesc;//语音识别进度描述
}
