package com.mobanker.shanyidai.api.service.user;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.dto.auth.*;
import com.mobanker.shanyidai.api.dto.user.UserCompleteness;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hantongyang
 * @description 用户认证业务实现接口
 * @time 2016/12/21 15:41
 */
public interface UserAuthService {

    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.user.UserCompleteness
     * @description 资料认证进度
     * @author hantongyang
     * @time 2017/1/10 11:42
     * @method getCompleteness
     */
    UserCompleteness getCompleteness(SydRequest request);

    /**
     * @param request
     * @return
     * @description 保存实名认证信息
     * @author hantongyang
     * @time 2016/12/20 20:14
     * @method addRealName
     */
    public void addRealName(SydRequest request);

    /**
     * @param userId
     * @return RealName
     * @description 获取实名认证信息
     * @author hantongyang
     * @time 2016/12/20 20:14
     * @method getRealName
     */
    public RealName getRealName(Long userId);

    /**
     * @param userId
     * @return void
     * @description 判断是否已经完成实名认证 用户信息中实名认证状态是成功，并且能够查询到姓名和身份证号 认为是成功 否则是失败
     * @author Richard Core
     * @time 2017/1/4 21:28
     * @method validateRealNameResult
     */
    public void validateRealNameResult(Long userId);

    /**
     * @param tel
     * @return boolean
     * @description 固话反查
     * @author hantongyang
     * @time 2017/1/5 21:19
     * @method validFixedConstrast
     */
    public FixedTel validFixedConstrast(String tel);

    /**
     * @param request
     * @return void
     * @author xulijie
     * @method zhimaAuth
     * @description 芝麻认证接口
     * @time 14:14 2017/2/17
     */
    public void zhimaAuth(SydRequest request);

    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.auth.AlipayScore
     * @description 保存芝麻信息 :芝麻分 和 是否跳过芝麻认证
     * @author Richard Core
     * @time 2017/1/23 20:51
     * @method saveZhimaInfo
     */
    public String getAlipayScore(SydRequest request);

    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.auth.AlipayScore
     * @description 保存芝麻信息 :芝麻分 和 是否跳过芝麻认证
     * @author Richard Core
     * @time 2017/1/23 20:51
     * @method saveZhimaInfo
     */
    AlipayScore saveZhimaInfo(SydRequest request);

    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.auth.AlipayScore
     * @description 获取芝麻分 和 是否跳过芝麻认证
     * @author Richard Core
     * @time 2017/1/23 20:51
     * @method getZhimaInfo
     */
    AlipayScore getZhimaInfo(SydRequest request);

    /**
     * @param request
     * @return java.lang.Object
     * @description 检查用户是否已授权或授权是否过期
     * @author xulijie
     * @time 2017/1/23 19:38
     * @method checkZhimaAuth
     */
    public Object checkZhimaAuth(SydRequest request);

    /**
     * @param request
     * @return java.lang.Object
     * @description 检查用户是否已授权或授权是否过期
     * @author xulijie
     * @time 2017/1/23 19:38
     * @method getZhimaURL
     */
    public Object getZhimaURL(SydRequest request);

    /**
     * @param request
     * @return VoiceAuthResult
     * @description 语音识别
     * @author liuhanqing
     * @time 2017/2/09 19:41
     * @method voiceAuth
     */
    public VoiceAuthResult voiceAuth(SydRequest request, HttpServletRequest httpServletRequest);

    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.auth.VoiceOrder
     * @description 查询语音解析结果
     * @author Richard Core
     * @time 2017/2/10 13:49
     * @method findVoiceResult
     */
    public VoiceOrder findVoiceResult(SydRequest request);

    Object faceAuth(SydRequest request, HttpServletRequest httpServletRequest);

    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.auth.AuthIdentityProcess
     * @description 获取语音识别进度结果
     * @author Richard Core
     * @time 2017/2/21 10:52
     * @method getVoiceProcessing
     */
    public AuthIdentityProcess getVoiceProcessing(SydRequest request);

    /**
     * @param livenessDataId
     * @param userId
     * @return FaceAuthResult
     * @description 活体识别-活体防伪
     * @author Richard Core
     * @time 2017/2/22 21:16
     * @method faceAuthHackDetect
     */
    public FaceAuthResult faceAuthHackDetect(String livenessDataId, Long userId);

    /**
     * @param param
     * @param userId
     * @return FaceAuthResult
     * @description 活体数据与水印照片比对
     * @author Richard Core
     * @time 2017/2/22 17:21
     * @method faceAuthWatermarkVerify
     */
    public FaceAuthResult faceAuthWatermarkVerify(WatermarkVerifyParam param, Long userId);

    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.auth.AuthIdentityProcess
     * @description 查询人脸识别进度
     * @author Richard Core
     * @time 2017/2/23 20:18
     * @method getAuthFaceRecord
     */
    public AuthIdentityProcess getAuthFaceRecord(SydRequest request);

    /**
     * @param userId
     * @return java.lang.Object
     * @description 查询身份认证信息 包括人脸识别和语音识别进度
     * @author Richard Core
     * @time 2017/2/28 11:47
     * @method getIdentityAuthProcess
     */
    AuthIdentityProcess getIdentityAuthProcess(Long userId);
}
