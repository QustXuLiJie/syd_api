/**
 *
 */
package com.mobanker.shanyidai.api.business.auth;

import com.alibaba.fastjson.JSONObject;
import com.mobanker.shanyidai.api.business.user.UserBusiness;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.dto.auth.*;
import com.mobanker.shanyidai.api.enums.AuthProcessEnum;
import com.mobanker.shanyidai.dubbo.dto.auth.*;
import com.mobanker.shanyidai.dubbo.service.auth.AuthDubboService;
import com.mobanker.shanyidai.dubbo.service.auth.AuthFaceDubboService;
import com.mobanker.shanyidai.dubbo.service.auth.RecognizeDubboService;
import com.mobanker.shanyidai.dubbo.service.user.UserDubboService;
import com.mobanker.shanyidai.esb.common.constants.EsbVoiceAuthStatusEnum;
import com.mobanker.shanyidai.esb.common.exception.EsbException;
import com.mobanker.shanyidai.esb.common.packet.ResponseBuilder;
import com.mobanker.shanyidai.esb.common.packet.ResponseEntity;
import com.mobanker.shanyidai.esb.common.utils.BeanUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 认证相关业务
 *
 * @author chenjianping
 * @data 2016年12月11日
 */
@Service
public class AuthBusinessImpl implements AuthBusiness {

    @Resource
    private UserBusiness userBusiness;
    @Resource
    private UserDubboService dubboUserService;
    @Resource
    private AuthDubboService dubboUserAuthService;

    //识别相关业务
    @Resource
    private RecognizeDubboService recognizeDubboService;

    @Resource
    private AuthFaceDubboService authFaceDubboService;

    /**
     * @param bean
     * @return Object
     * @description 实名认证
     * @author hantongyang
     * @time 2016/12/26 20:43
     * @method authRealName
     */
    @Override
    public Object authRealName(RealName bean) {
        RealNameDto dto = BeanUtil.cloneBean(bean, RealNameDto.class);
        ResponseEntity responseEntity = null;
        try {
            responseEntity = dubboUserAuthService.authRealName(dto);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        }
        if (!ResponseBuilder.isSuccess(responseEntity)) {
            throw new SydException(ReturnCode.ADD_REALNAME_AUTH_FAILED.getCode(), responseEntity.getMsg());
        }
        return responseEntity.getData();
    }

    /**
     * @param bean
     * @return Object
     * @description 学历认证
     * @author hantongyang
     * @time 2016/12/26 22:07
     * @method authEdu
     */
    @Override
    public Object authEdu(RealName bean) {
        RealNameDto dto = BeanUtil.cloneBean(bean, RealNameDto.class);
        ResponseEntity responseEntity = null;
        try {
            responseEntity = dubboUserAuthService.authEdu(dto);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        }
        if (!ResponseBuilder.isSuccess(responseEntity)) {
            throw new SydException(ReturnCode.AUTH_EDU_FAILED.getCode(), responseEntity.getMsg());
        }
        return responseEntity.getData();
    }

    /**
     * @param tel
     * @return java.lang.Object
     * @description 固话反查
     * @author hantongyang
     * @time 2017/1/5 21:00
     * @method fixedConstrast
     */
    @Override
    public ResponseEntity fixedConstrast(String tel) {
        if (StringUtils.isBlank(tel)) {
            throw new SydException(ReturnCode.ERROR_AUTH_TEL);
        }
        ResponseEntity responseEntity = null;
        try {
            responseEntity = dubboUserAuthService.fixedConstrast(tel);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        }
        if (!ResponseBuilder.isSuccess(responseEntity)) {
            throw new SydException(ReturnCode.ERROR_AUTH_TEL.getCode(), responseEntity.getMsg());
        }
        return responseEntity;
    }


    /**
     * @param param
     * @return java.lang.String
     * @description 查询阿里的芝麻分
     * @author hantongyang
     * @time 2016/12/21 16:22
     * @method getAlipayScore
     */
    @Override
    public String getAlipayScore(ZhimaAuthParam param) {
        if (param == null || param.getUserId() == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        //调用获取芝麻分
        ZhimaAuthParamDto dto = BeanHelper.cloneBean(param, ZhimaAuthParamDto.class);
        ResponseEntity entity = null;
        try {
            entity = dubboUserAuthService.getZhimaScore(dto);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        }
        if (!ResponseBuilder.isSuccess(entity)) {
            throw new SydException(entity.getError(), entity.getMsg());
        }
        return (String) entity.getData();
    }

    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.auth.AlipayScore
     * @description 查询芝麻分 和 是否跳过芝麻认证
     * @author Richard Core
     * @time 2017/1/23 21:22
     * @method getAlipayScore
     */
    @Override
    public AlipayScore getZhimaInfo(SydRequest request) {
        CreditInfoParamsDto params = new CreditInfoParamsDto();
        params.setUserId(request.getUserId());
        params.setType(request.getProduct());
        List<String> fieldList = new ArrayList<String>();
        fieldList.add("alipayScore");
        fieldList.add("alipaySkipAuthorize");
        fieldList.add("alipayUsersStatus");
        params.setFields(fieldList);

        ResponseEntity result = null;
        try {
            result = dubboUserAuthService.getAlipayInfoByUserId(params);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        }
        if (!ResponseBuilder.isFinished(result)) {
            throw new SydException(result.getError(), result.getMsg());
        }
        AlipayScore alipayScore = new AlipayScore();
        Object data = result.getData();
        if (data == null) {
            return alipayScore;
        }
        Map<String, String> map = (Map<String, String>) data;
        alipayScore.setAlipayScore(map.get("alipayScore"));
        alipayScore.setAlipaySkipAuthorize(map.get("alipaySkipAuthorize"));
        alipayScore.setAlipayUsersStatus(map.get("alipayUsersStatus"));

        return alipayScore;
    }


    /**
     * @param alipayScore
     * @param commonFields
     * @return com.mobanker.shanyidai.api.dto.auth.AlipayScore
     * @description 保存芝麻信息 :芝麻分 和 是否跳过芝麻认证
     * @author Richard Core
     * @time 2017/1/23 20:47
     * @method saveAlipayScore
     */
    @Override
    public AlipayScore saveAlipayScore(AlipayScore alipayScore, Map<String, Object> commonFields) {
        CreditInfoParamsDto params = new CreditInfoParamsDto();
        params.setUserId(alipayScore.getUserId());
        params.setType(alipayScore.getProduct());
        params.setRealName(alipayScore.getRealName());
        params.setCardId(alipayScore.getCardId());
        Map<String, Object> saveFields = new HashMap<>();
        saveFields.put("alipayScore", alipayScore.getAlipayScore());
        saveFields.put("alipayUsersStatus", alipayScore.getAlipayUsersStatus());
        saveFields.put("alipaySkipAuthorize", alipayScore.getAlipaySkipAuthorize());
        params.setSaveFields(saveFields);
        params.setCommonFields(commonFields);
        ResponseEntity result = null;
        try {
            result = dubboUserAuthService.saveCreditInfoByUserId(params);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        }
        if (!ResponseBuilder.isFinished(result)) {
            throw new SydException(result.getError(), result.getMsg());
        }
        AlipayScore alipayScoreResult = new AlipayScore();
        Object data = result.getData();
        if (data == null) {
            return alipayScoreResult;
        }
        Map<String, String> map = (Map<String, String>) data;
        alipayScoreResult.setAlipayScore(map.get("alipayScore"));
        alipayScoreResult.setAlipaySkipAuthorize(map.get("alipaySkipAuthorize"));
        alipayScoreResult.setAlipayUsersStatus(map.get("alipayUsersStatus"));
        return alipayScoreResult;
    }

    /**
     * @param bean
     * @return java.lang.Object
     * @description 获取芝麻认证的URL
     * @author xulijie
     * @time 2017/1/20 15:38
     * @method getZhimaURL
     */
    @Override
    public Object getZhimaURL(ZhimaBeanParam bean) {
        ZhimaAuthParamDto dto = BeanHelper.cloneBean(bean, ZhimaAuthParamDto.class);
        dto.setSences(dto.getProduct());
        dto.setPageType("h5");
        ResponseEntity result = null;
        try {
            result = dubboUserAuthService.getZhimaURL(dto);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        }
        if (!ResponseBuilder.isSuccess(result)) {
            throw new SydException(result.getError(), result.getMsg());
        }
        ZhimaAuthDto responseDto = (ZhimaAuthDto) result.getData();
        return responseDto;
    }

    /**
     * @param bean
     * @return java.lang.Object
     * @description 检查用户是否已授权或授权是否过期
     * @author xulijie
     * @time 2017/1/23 19:38
     * @method checkZhimaAuth
     */
    @Override
    public Object checkZhimaAuth(ZhimaBeanParam bean) {
        ZhimaAuthParamDto dto = BeanHelper.cloneBean(bean, ZhimaAuthParamDto.class);
        //TODO 目前用的是手机贷
        dto.setSences("shoujidai");//dto.getProduct()
//        dto.setPageType("h5");
        ResponseEntity result = null;
        try {
            result = dubboUserAuthService.checkZhimaAuth(dto);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        }
        if (!ResponseBuilder.isSuccess(result)) {
            throw new SydException(result.getError(), result.getMsg());
        }
        ZhimaAuthDto zhimaAuthDto = (ZhimaAuthDto) result.getData();
        CheckZhimaAuth checkZhimaAuth = BeanHelper.cloneBean(zhimaAuthDto, CheckZhimaAuth.class);
        return checkZhimaAuth;
    }

    /**
     * @param uploadParam
     * @return VoiceAuthResult
     * @description 语音识别
     * @author liuhanqing
     * @time 2017/2/09 17:59
     * @method voiceAuth
     */
    @Override
    public VoiceAuthResult voiceAuth(VoiceAuthUploadParam uploadParam) {
        VoiceAuthDto paramDto = BeanHelper.cloneBean(uploadParam, VoiceAuthDto.class);
        ResponseEntity result = null;
        try {
            result = recognizeDubboService.voiceAuth(paramDto);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        }
        if (!ResponseBuilder.isSuccess(result)) {
            throw new SydException(result.getError(), result.getMsg());
        }
        VoiceAuthResult resultDto = BeanHelper.parseJson(JSONObject.toJSON(result.getData()).toString(), VoiceAuthResult.class);
        return resultDto;
    }

    /**
     * @param orderId
     * @return com.mobanker.shanyidai.api.dto.auth.VoiceOrder
     * @description 查询语音解析结果
     * @author Richard Core
     * @time 2017/2/10 13:49
     * @method findVoiceResult
     */
    @Override
    public VoiceOrder findVoiceResult(String orderId) {
        ResponseEntity result = null;
        try {
            result = recognizeDubboService.findVoiceOrder(orderId);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        } catch (Exception e) {
            throw new SydException(ReturnCode.QUERY_KDXF_ORDER_ERROR.getCode(), e.getMessage());
        }
        if (!ResponseBuilder.isSuccess(result)) {
            throw new SydException(result.getError(), result.getMsg());
        }
        if (result.getData() == null) {
            throw new SydException(ReturnCode.QUERY_KDXF_ORDER_ERROR.getCode(), "返回解析结果为空");
        }
        VoiceOrder voiceOrder = BeanHelper.parseJson(JSONObject.toJSON(result.getData()).toString(), VoiceOrder.class);

        return voiceOrder;
    }

    /**
     * @param param
     * @param file
     * @return com.mobanker.shanyidai.api.dto.auth.FaceAuthResult
     * @description 人脸识别上传认证文件
     * @author Richard Core
     * @time 2017/2/21 20:07
     * @method faceAuthUpload
     */
    @Override
    public FaceAuthResult faceAuthUpload(FaceAuthUploadParam param, MultipartFile file) {
//        if (param == null || file == null || StringUtils.isBlank(param.getFilePath())) {
//            throw new SydException(ReturnCode.PARAM_REQUIRED);
//        }
        ResponseEntity result = null;
        try {
            FaceAuthUploadParamDto paramDto = BeanHelper.cloneBean(param, FaceAuthUploadParamDto.class);
            result = authFaceDubboService.faceAuthUpload(paramDto, file);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        }
        if (!ResponseBuilder.isSuccess(result)) {
            throw new SydException(result.getError(), result.getMsg());
        }
        FaceAuthResult resultDto = BeanHelper.parseJson(JSONObject.toJSON(result.getData()).toString(), FaceAuthResult.class);
        return resultDto;
    }

    /**
     * @param userId
     * @return com.mobanker.shanyidai.api.dto.auth.AuthVoiceRecord
     * @description 查询语音识别进度
     * @author Richard Core
     * @time 2017/2/20 21:16
     * @method findAuthVoiceProcess
     */
    @Override
    public AuthVoiceRecord findAuthVoiceProcess(Long userId) {
        ResponseEntity result = null;
        try {
            result = recognizeDubboService.findVoiceAuthProcessByUserId(userId);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        } catch (Exception e) {
            throw new SydException(ReturnCode.AUTH_PROCESS_ERROR.getCode(), e.getMessage(), e);
        }
        if (!ResponseBuilder.isSuccess(result)) {
            throw new SydException(result.getError(), result.getMsg());
        }
        if (result.getData() == null) {
            throw new SydException(ReturnCode.AUTH_PROCESS_ERROR.getCode(), "返回认证进度为空");
        }
        AuthVoiceRecord voiceOrder = BeanHelper.parseJson(JSONObject.toJSON(result.getData()).toString(), AuthVoiceRecord.class);

        return voiceOrder;
    }

    /**
     * @param livenessDataId
     * @return com.mobanker.shanyidai.api.dto.auth.AuthVoiceRecord
     * @description 活体识别-活体防伪
     * @author Richard Core
     * @time 2017/2/22 21:16
     * @method faceAuthHackDetect
     */
    @Override
    public FaceAuthResult faceAuthHackDetect(String livenessDataId) {
        if (StringUtils.isBlank(livenessDataId)) {
            throw new SydException(ReturnCode.FACE_AUTH_DATA_ID_FAILD);
        }
        ResponseEntity result = null;
        try {
            result = authFaceDubboService.faceAuthHackDetect(livenessDataId);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        } catch (Exception e) {
            throw new SydException(ReturnCode.FACE_AUTH_HACK_DETECT_FAILD.getCode(), e.getMessage(), e);
        }
        if (!ResponseBuilder.isSuccess(result)) {
            throw new SydException(result.getError(), result.getMsg());
        }
        FaceAuthResult resultDto = BeanHelper.parseJson(JSONObject.toJSON(result.getData()).toString(), FaceAuthResult.class);
        return resultDto;
    }

    /**
     * @param param
     * @return ResponseEntity
     * @description 活体数据与水印照片比对
     * @author Richard Core
     * @time 2017/2/22 17:21
     * @method faceAuthWatermarkVerify
     */
    @Override
    public FaceAuthResult faceAuthWatermarkVerify(WatermarkVerifyParam param) {
        if (param == null || StringUtils.isBlank(param.getLivenessDataId())) {
            throw new SydException(ReturnCode.FACE_AUTH_DATA_ID_FAILD);
        }
        if (StringUtils.isBlank(param.getRealName()) || StringUtils.isBlank(param.getIdCardNo())) {
            throw new SydException(ReturnCode.REAL_INFO_INVALID);
        }
        ResponseEntity result = null;
        try {
            WatermarkVerifyParamDto paramDto = BeanHelper.cloneBean(param, WatermarkVerifyParamDto.class);
            result = authFaceDubboService.faceAuthWatermarkVerify(paramDto);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        } catch (Exception e) {
            throw new SydException(ReturnCode.FACE_AUTH_WATERMARK_VERIFY_FAILD.getCode(), e.getMessage(), e);
        }
        if (!ResponseBuilder.isSuccess(result)) {
            throw new SydException(result.getError(), result.getMsg());
        }
        FaceAuthResult resultDto = BeanHelper.parseJson(JSONObject.toJSON(result.getData()).toString(), FaceAuthResult.class);
        return resultDto;
    }

    /**
     * @param userId
     * @return com.mobanker.shanyidai.api.dto.auth.AuthFaceRecord
     * @description 查询人脸识别进度
     * @author Richard Core
     * @time 2017/2/23 20:18
     * @method getAuthFaceRecord
     */
    @Override
    public AuthFaceRecord getAuthFaceRecord(Long userId) {
        if (userId == null) {
            return null;
        }
        ResponseEntity responseEntity = null;
        try {
            responseEntity = authFaceDubboService.getAuthFaceprocess(userId);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        } catch (Exception e) {
            throw new SydException(ReturnCode.FACE_AUTH_PROCESS_ERROR.getCode(), e.getMessage(), e);
        }
        if (!ResponseBuilder.isSuccess(responseEntity)) {
            throw new SydException(responseEntity.getError(), responseEntity.getMsg());
        }
        AuthFaceRecord resultDto = BeanHelper.parseJson(JSONObject.toJSON(responseEntity.getData()).toString(), AuthFaceRecord.class);
        return resultDto;
    }

    /**
     * @param userId
     * @param result
     * @return com.mobanker.shanyidai.api.dto.auth.AuthProcess
     * @description 封装详细进度到资料完整度中
     * @author Richard Core
     * @time 2017/3/4 10:21
     * @method getAuthVoiceProcess
     */
    @Override
    public AuthProcess getAuthVoiceProcess(Long userId, AuthProcess result) {
        EsbVoiceAuthStatusEnum instance = null;
        try {
            //缓存中没有，从数据库中查询
            AuthVoiceRecord authVoiceProcess = findAuthVoiceProcess(userId);
            result.setUserId(userId);
            if (authVoiceProcess == null) {
                result.setAuthProcess(AuthProcessEnum.NODATA);
                return result;
            }
            instance = EsbVoiceAuthStatusEnum.getInstance(authVoiceProcess.getStatus());
        } catch (Exception e) {
            result.setAuthProcess(AuthProcessEnum.NODATA);
            return result;
        }
        switch (instance) {
            case UPLOADING://("uploading", "语音正在上传"),
            case UPLOADED://("uploaded", "语音已上传，正在解析"),
            case CONTRASTING://("contrasting", "语音已解析完毕，正在对比"),
                result.setAuthProcess(AuthProcessEnum.PROCESS);
                return result;
            case SUCCESS://("success", "语音对比成功"),
            case MANUAL_SUCCESS://("manual_success", "讯飞异常，批量成功"),认证成功将结果放到缓存中
                result.setAuthProcess(AuthProcessEnum.SUCCESS);
                return result;
            case UPLOAD_FAIL://("upload_fail", "上传语音失败"),
            case TRANS_FAIL://("trans_fail", "语音解析失败"),
            case TRANS_TIMEOUT://("trans_timeout", "语音解析超时"),
            case FAIL://("fail", "语音对比不通过"),
            case TIMEOUT_FAIL:
                result.setAuthProcess(AuthProcessEnum.FAIL);
                return result;
            case NOT_EXIST://("not_exist", "没有记录");
                result.setAuthProcess(AuthProcessEnum.NODATA);
                return result;
        }
        return result;
    }

    /**
     * @param userId
     * @return int
     * @description 查询语音识别当天认证次数
     * @author Richard Core
     * @time 2017/3/4 16:18
     * @method getAuthVoiceTimesToday
     */
    @Override
    public int getAuthVoiceTimesToday(Long userId) {
        if (userId == null) {
            return 0;
        }
        ResponseEntity responseEntity = null;
        try {
            responseEntity = recognizeDubboService.getAuthVoiceTimesToday(userId);
        } catch (EsbException esb) {
            throw new SydException(esb.errCode, esb.message);
        } catch (Exception e) {
            throw new SydException(ReturnCode.VOICE_AUTH_ERROR.getCode(), e.getMessage());
        }
        if (!ResponseBuilder.isSuccess(responseEntity)) {
            throw new SydException(responseEntity.getError(), responseEntity.getMsg());
        }
        if (responseEntity.getData() == null) {
            return 0;
        }
        return Integer.valueOf(String.valueOf(responseEntity.getData()));
    }

    /**
     * @param userId
     * @return int
     * @description 查询人脸识别当天认证次数
     * @author Richard Core
     * @time 2017/3/4 16:18
     * @method checkAuthFaceToday
     */
    @Override
    public int getAuthFaceTimesToday(Long userId) {
        if (userId == null) {
            return 0;
        }
        ResponseEntity responseEntity = null;
        try {
            responseEntity = authFaceDubboService.getAuthFaceTimesToday(userId);
        } catch (EsbException esb) {
            throw new SydException(esb.errCode, esb.message);
        } catch (Exception e) {
            throw new SydException(ReturnCode.FACE_AUTH_ERROR.getCode(), e.getMessage());
        }
        if (!ResponseBuilder.isSuccess(responseEntity)) {
            throw new SydException(responseEntity.getError(), responseEntity.getMsg());
        }
        if (responseEntity.getData() == null) {
            return 0;
        }
        return Integer.valueOf(String.valueOf(responseEntity.getData()));
    }
}
