package com.mobanker.shanyidai.api.service.user.util;

import com.mobanker.shanyidai.api.common.enums.RedisKeyEnum;

/**
 * @desc:人脸/语音识别相关参数转化
 * @author: Richard Core
 * @create time: 2017/2/23 11:52
 */
public class AuthBusinessConvert {

    /**
     * @param userId
     * @return java.lang.String
     * @description 获取缓存中人脸识别进度的key
     * @author Richard Core
     * @time 2017/2/23 11:53
     * @method getAuthFaceRedisKey
     */
    public static String getAuthFaceRedisKey(Long userId) {
        return RedisKeyEnum.FACE_AUTH.getValue() + userId;
    }
    /**
     * @param userId
     * @return java.lang.String
     * @description 获取缓存中语音识别进度的key
     * @author Richard Core
     * @time 2017/2/23 11:53
     * @method getAuthFaceRedisKey
     */
    public static String getAuthVoiceProcessRedisKey(Long userId) {
        return RedisKeyEnum.VOICE_AUTH.getValue() + userId;
    }
}
