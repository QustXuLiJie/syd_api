/**
 *
 */
package com.mobanker.shanyidai.api.business.auth;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.dto.auth.*;
import com.mobanker.shanyidai.esb.common.packet.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 认证业务声明
 *
 * @author chenjianping
 * @data 2016年12月11日
 */
public interface AuthBusiness {

    /**
     * @param bean
     * @return Object
     * @description 实名认证
     * @author hantongyang
     * @time 2016/12/26 20:43
     * @method authRealName
     */
    Object authRealName(RealName bean);

    /**
     * @param bean
     * @return Object
     * @description 学历认证
     * @author hantongyang
     * @time 2016/12/26 22:07
     * @method authEdu
     */
    Object authEdu(RealName bean);

    /**
     * @param tel
     * @return java.lang.Object
     * @description 固话反查
     * @author hantongyang
     * @time 2017/1/5 21:00
     * @method fixedConstrast
     */
    ResponseEntity fixedConstrast(String tel);

    /**
     * @param param
     * @return java.lang.String
     * @description 查询阿里的芝麻分
     * @author hantongyang
     * @time 2016/12/21 16:22
     * @method getAlipayScore
     */
    public String getAlipayScore(ZhimaAuthParam param);
    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.auth.AlipayScore
     * @description 查询芝麻分 和 是否跳过芝麻认证
     * @author Richard Core
     * @time 2017/1/23 21:22
     * @method getAlipayScore
     */
    public AlipayScore getZhimaInfo(SydRequest request);

    /**
     * @param bean
     * @return java.lang.Object
     * @description 获取芝麻认证的URL
     * @author xulijie
     * @time 2017/1/20 15:38
     * @method getZhimaURL
     */
    Object getZhimaURL(ZhimaBeanParam bean);

    /**
     * @param bean
     * @return java.lang.Object
     * @description 检查用户是否已授权或授权是否过期
     * @author xulijie
     * @time 2017/1/23 19:38
     * @method checkZhimaAuth
     */
    Object checkZhimaAuth(ZhimaBeanParam bean);

    /**
     * @param alipayScore
     * @return void
     * @description 保存用户芝麻分
     * @author hantongyang
     * @time 2016/12/21 17:43
     * @method saveAlipayScore
     */
    public AlipayScore saveAlipayScore(AlipayScore alipayScore, Map<String, Object> commonFields);

    /**
     * @param uploadParam
     * @return VoiceAuthResult
     * @description 语音识别
     * @author liuhanqing
     * @time 2017/2/09 17:59
     * @method voiceAuth
     */
    public VoiceAuthResult voiceAuth(VoiceAuthUploadParam uploadParam);

    /**
     * @param orderId
     * @return com.mobanker.shanyidai.api.dto.auth.VoiceOrder
     * @description 查询语音解析结果
     * @author Richard Core
     * @time 2017/2/10 13:49
     * @method findVoiceResult
     */
    public VoiceOrder findVoiceResult(String orderId);

    /**
     * @param param
     * @param file
     * @return com.mobanker.shanyidai.api.dto.auth.FaceAuthResult
     * @description 人脸识别上传认证文件
     * @author Richard Core
     * @time 2017/2/21 20:07
     * @method faceAuthUpload
     */
    FaceAuthResult faceAuthUpload(FaceAuthUploadParam param, MultipartFile file);

    /**
     * @param userId
     * @return com.mobanker.shanyidai.api.dto.auth.AuthVoiceRecord
     * @description 查询语音识别进度
     * @author Richard Core
     * @time 2017/2/20 21:16
     * @method findAuthVoiceProcess
     */
    public AuthVoiceRecord findAuthVoiceProcess(Long userId);

    /**
     * @param livenessDataId
     * @return com.mobanker.shanyidai.api.dto.auth.AuthVoiceRecord
     * @description 活体识别-活体防伪
     * @author Richard Core
     * @time 2017/2/22 21:16
     * @method faceAuthHackDetect
     */
    public FaceAuthResult faceAuthHackDetect(String livenessDataId);

    /**
     * @param param
     * @return ResponseEntity
     * @description 活体数据与水印照片比对
     * @author Richard Core
     * @time 2017/2/22 17:21
     * @method faceAuthWatermarkVerify
     */
    public FaceAuthResult faceAuthWatermarkVerify(WatermarkVerifyParam param);

    /**
     * @param userId
     * @return com.mobanker.shanyidai.api.dto.auth.AuthFaceRecord
     * @description 查询人脸识别进度
     * @author Richard Core
     * @time 2017/2/23 20:18
     * @method getAuthFaceRecord
     */
    public AuthFaceRecord getAuthFaceRecord(Long userId);

    /**
     * @param userId
     * @param result
     * @return com.mobanker.shanyidai.api.dto.auth.AuthProcess
     * @description 封装详细进度到资料完整度中
     * @author Richard Core
     * @time 2017/3/4 10:21
     * @method getAuthVoiceProcess
     */
    AuthProcess getAuthVoiceProcess(Long userId, AuthProcess result);

    /**
     * @param userId
     * @return int
     * @description 查询语音识别当天认证次数
     * @author Richard Core
     * @time 2017/3/4 16:18
     * @method getAuthVoiceTimesToday
     */
    int getAuthVoiceTimesToday(Long userId);
    /**
     * @param userId
     * @return int
     * @description 查询人脸识别当天认证次数
     * @author Richard Core
     * @time 2017/3/4 16:18
     * @method checkAuthFaceToday
     */
    int getAuthFaceTimesToday(Long userId);
}
