package com.mobanker.shanyidai.api.service.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mobanker.shanyidai.api.business.auth.AuthBusiness;
import com.mobanker.shanyidai.api.business.redis.RedisBusiness;
import com.mobanker.shanyidai.api.common.annotation.BusinessFlowAnnotation;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.constant.ZkConfigConstant;
import com.mobanker.shanyidai.api.common.enums.ExpiresEnum;
import com.mobanker.shanyidai.api.common.enums.StatusEnum;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.common.tool.CommonUtil;
import com.mobanker.shanyidai.api.dto.auth.*;
import com.mobanker.shanyidai.api.dto.upload.UploadResultDto;
import com.mobanker.shanyidai.api.dto.user.UserBasicInfoRsp;
import com.mobanker.shanyidai.api.dto.user.UserCompleteness;
import com.mobanker.shanyidai.api.dto.user.UserContactRsp;
import com.mobanker.shanyidai.api.dto.user.UserJobRsp;
import com.mobanker.shanyidai.api.enums.AuthProcessEnum;
import com.mobanker.shanyidai.api.enums.FaceAuthProcessEnum;
import com.mobanker.shanyidai.api.service.common.CommonService;
import com.mobanker.shanyidai.api.service.upload.UploadService;
import com.mobanker.shanyidai.api.service.user.util.AuthBusinessConvert;
import com.mobanker.shanyidai.api.service.user.util.FaceAuthPicParamEnum;
import com.mobanker.shanyidai.api.service.user.util.UserBusinessConvertUtils;
import com.mobanker.shanyidai.dubbo.dto.user.UserBaseInfoDto;
import com.mobanker.shanyidai.esb.common.logger.LogManager;
import com.mobanker.shanyidai.esb.common.logger.Logger;
import com.mobanker.shanyidai.esb.common.packet.ResponseBuilder;
import com.mobanker.shanyidai.esb.common.packet.ResponseEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

import static com.mobanker.shanyidai.api.service.user.util.UserBusinessConvertUtils.*;


/**
 * @author hantongyang
 * @version 1.0
 * @description
 * @date 创建时间：2016/12/21 15:50
 */
@Service
@BusinessFlowAnnotation
public class UserAuthServiceImpl implements UserAuthService {
    public static final Logger LOGGER = LogManager.getSlf4jLogger(UserAuthServiceImpl.class);
    public static final String REAL_NAME_SUCESS = "0";
    public static final String ALIPAYUSERSSTATUS = "-2";
    public static final String ALIPAYSCORE = "0";
    @Resource
    private AuthBusiness authBusiness;
    @Resource
    private UserService userService;
    @Resource
    private UploadService uploadService;
    @Resource
    private RedisBusiness redisBusiness;
    @Resource
    private UserAuthService userAuthService;
    @Resource
    private CommonService commonService;
    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.user.UserCompleteness
     * @description 资料认证进度
     * @author hantongyang
     * @time 2017/1/10 11:42
     * @method getCompleteness
     */
    @Override
    public UserCompleteness getCompleteness(SydRequest request) {
        if (request == null || request.getUserId() == null) {
            return initUserCompleteness(null, null, null, null, null);
        }
        Long userId = request.getUserId();
        //查询实名认证
        RealName realName = getRealName(userId);
        //查询身份识别
        AuthIdentityProcess identityAuthProcess = getIdentityAuthProcess(userId);

        //查询联系人/单位信息
        UserContactRsp contact = userService.getContact(userId);
        UserJobRsp job = userService.getJob(userId);
        //查询芝麻认证
        AlipayScore zhimaInfo = getZhimaInfo(request);
        return initUserCompleteness(realName, contact, job, zhimaInfo,identityAuthProcess);
    }

    /**
     * @param request
     * @return void
     * @description 保存实名认证信息
     * @author hantongyang
     * @time 2016/12/21 11:39
     * @method addRealName
     */
    @Override
    public void addRealName(SydRequest request) {
        //1、参数验证
        if (request == null || StringUtils.isBlank(request.getContent())) {
            throw new SydException(ReturnCode.PARAM_VALID);
        }
        RealName realNameSet = BeanHelper.parseJson(request.getContent(), RealName.class);
        //1.1、验证相关参数
        UserBusinessConvertUtils.checkRealNameEmpty(realNameSet);
        CommonUtil.isCardId(realNameSet.getIdCard());
        //1.2、姓名在2-6个字符之间
        if (!CommonUtil.limitString(realNameSet.getRealName(), 2, 6)) {
            throw new SydException(ReturnCode.ADD_REALNAME_AUTH_FAILED);
        }
        //1.3、验证身份证
        if (!CommonUtil.isCardId(realNameSet.getIdCard())) {
            throw new SydException(ReturnCode.ADD_REALNAME_CARDID_FAILED);
        }
        //1.4、验证缓存中是否存在对应的用户信息，如果缓存中没有找到用户信息，查询接口是否有实名认证、学历认证过
        boolean isRealName = true;
        boolean isEdu = true;
        RealName bean = getRealName(request.getUserId());
        if (bean != null) {
            //判断实名认证信息是否有修改过
            if (ExpiresEnum.EFFECTIVE.getValue().equals(bean.getRealnameAllStatus())) {
                if (realNameSet.getRealName().equals(bean.getRealName())
                        && realNameSet.getIdCard().equals(bean.getIdCard())) {
                    isRealName = false;
                }
            }
            //判断学历信息是否有修改过
            if (realNameSet.getEducation().equals(bean.getEducation())) {
                isEdu = false;
            }
        }
        //1.5、验证身份证是否已经实名认证过。注：当前用户实名认证信息有改动的前提，才判断
        if(isRealName){
            UserBasicInfoRsp userBasicInfoRsp = userService.checkCardNo(realNameSet.getIdCard());
            if(userBasicInfoRsp != null){
                throw new SydException(ReturnCode.CARD_NO_IS_AUTH);
            }
        }
        //1.6 验证是否超出当日验证次数 TODO
        if(bean.getRealnameTimes() > 10){
            throw new SydException(ReturnCode.COMMIT_AUTH_COUNT_ERROR);
        }

        Map<String, Object> saveField = new HashMap<String, Object>();
        //2.1、调用认证系统，实名认证
        if (isRealName) {
            Object data = authBusiness.authRealName(realNameSet);
            if (data != null) {
                //封装需要修改的用户信息--实名认证信息
                Integer realNameTimes = bean == null ? null : bean.getRealnameTimes();
                initRealNameSaveFieldMap(saveField, (JSONObject) data, realNameTimes);
            }
        }
        //2.2、调用认证系统，认证学历
        if (isEdu) {
            Object data = authBusiness.authEdu(realNameSet);
            if (data != null) {
                //封装需要修改的用户信息--学历认证信息
                initEduSaveFieldMap(saveField, (JSONObject) data);
            }
        }
        //3、认证成功后，保存到数据库中
        if ((isRealName || isEdu) && (!saveField.isEmpty())) {
            //TODO
            UserBaseInfoDto dto = initUserBaseInfoDto(saveField, request);
            userService.updateUserInfo(dto);
        }
    }

    /**
     * @param userId
     * @return com.mobanker.shanyidai.api.dto.user.RealnameSet
     * @description 查询实名认证信息
     * @author hantongyang
     * @time 2016/12/21 11:39
     * @method getRealName
     */
    @Override
    public RealName getRealName(Long userId) {
        //判断参数是否为空
        if (userId == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        UserBasicInfoRsp bean = userService.getUserInfoByUserId(userId);
        if (bean == null) {
            return null;
//            throw new SydException(ReturnCode.GET_USERINFO_FAILED);
        }
        return initRealName(bean);
    }

    /**
     * @param userId
     * @return void
     * @description 判断是否已经完成实名认证 用户信息中实名认证状态是成功，并且能够查询到姓名和身份证号 认为是成功 否则是失败
     * @author Richard Core
     * @time 2017/1/4 21:28
     * @method validateRealNameResult
     */
    @Override
    public void validateRealNameResult(Long userId) {
        //没有用户Id认为是登录超时
        if (userId == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        UserBasicInfoRsp bean = userService.getUserInfoByUserId(userId);
        //用户信息中实名认证状态是成功，并且能够查询到姓名和身份证号 认为是成功 否则是失败
        if (bean == null ||
                !REAL_NAME_SUCESS.equals(bean.getRealnameAllStatus()) ||
                StringUtils.isBlank(bean.getRealname()) ||
                StringUtils.isBlank(bean.getIdCard())) {
            throw new SydException(ReturnCode.REAL_NAME_NOT_YET);
        }

    }

    /**
     * @param tel
     * @return boolean
     * @description 固话反查
     * @author hantongyang
     * @time 2017/1/5 21:19
     * @method validFixedConstrast
     */
    @Override
    public FixedTel validFixedConstrast(String tel) {
        ResponseEntity result = authBusiness.fixedConstrast(tel);
        if (ResponseBuilder.isSuccess(result)) {
            JSONObject jso = (JSONObject) result.getData();
            JSONArray array = (JSONArray) jso.get("detailInfoList");
            if (array == null || array.isEmpty()) {
                return null;
            }
            JSONObject json = (JSONObject) array.get(0);
            FixedTel bean = JSONObject.toJavaObject(json, FixedTel.class);
            return bean;
        }
        return null;
    }

    /**
     * @param request
     * @return void
     * @author xulijie
     * @method zhimaAuth
     * @description 芝麻认证接口
     * @time 14:14 2017/2/17
     */
    @Override
    public void zhimaAuth(SydRequest request) {
        CommonUtil.checkLoginStatus(request);
        //1.查询用户是否授权或授权是否已过期 失败结束芝麻认证
        CheckZhimaAuth checkZhimaAuth = (CheckZhimaAuth) userAuthService.checkZhimaAuth(request);
        //// TODO: 2017/2/17 由于挡板系统getAuthStatus返回为1，暂且这样写。咨询管管只有为0时才算过。
        if (!StatusEnum.VALID.getStatus().equals(checkZhimaAuth.getAuthStatus())) {
            throw new SydException(ReturnCode.GET_ALIPAY_CHEACK_AUTHVALIDATE_FAILED);
        }
        if (StatusEnum.VALID.getStatus().equals(checkZhimaAuth.getExpired())) {
            throw new SydException(ReturnCode.GET_ALIPAY_CHEACK_AUTHVOVERTIME_FAILED);
        }
        //2.去认证系统查询芝麻分
        //验证是否已经实名认证
        RealName realName = getRealName(request.getUserId());
        if (realName == null ||
                !REAL_NAME_SUCESS.equals(realName.getRealnameAllStatus()) ||
                StringUtils.isBlank(realName.getRealName()) ||
                StringUtils.isBlank(realName.getIdCard())) {
            throw new SydException(ReturnCode.REAL_NAME_NOT_YET);
        }
        //参数拼装
        ZhimaAuthParam param = BeanHelper.parseJson(request.getContent(), ZhimaAuthParam.class);

        param.setUserId(request.getUserId());
        param.setCertNo(realName.getIdCard());
        param.setName(realName.getRealName());
        param.setSences("shoujidai");//request.getProduct()
        String score = authBusiness.getAlipayScore(param);
        if (score == null) {
            throw new SydException(ReturnCode.GET_ALIPAY_AZHIMASCORE_FAILED);
        }
        //3.保存芝麻分
        //参数拼装
        AlipayScore alipayScore = new AlipayScore();
        alipayScore.setAlipayScore(score);
        alipayScore.setAlipayUsersStatus(checkZhimaAuth.getAuthStatus());
        alipayScore.setAlipaySkipAuthorize(REAL_NAME_SUCESS);
        if (alipayScore == null) {
            throw new SydException(ReturnCode.PARAM_VALID);
        }
        BeanHelper.packageBean(request, alipayScore);
        Map<String, Object> commonFields = UserBusinessConvertUtils.initCommonFields(request);
        //保存芝麻分
        authBusiness.saveAlipayScore(alipayScore, commonFields);
        //4.记录日志
    }

    /**
     * @param request
     * @return java.lang.String
     * @description 获取芝麻分
     * @author hantongyang
     * @time 2016/12/21 16:32
     * @method getAlipayScore
     */
    @Override
    public String getAlipayScore(SydRequest request) {
        CommonUtil.checkLoginStatus(request);
        //获取芝麻分
        return authBusiness.getAlipayScore(initZhimaAuthParam(request));
    }

    /**
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @description 获取芝麻链接
     * @author xulijie
     * @time 2017/1/20 11:32
     * @method getAuthURL
     */
    @Override
    public Object getZhimaURL(SydRequest request) {
        if (request.getUserId() == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        //验证用户ID
        UserBusinessConvertUtils.checkUserId(request);
        RealName realName = getRealName(request.getUserId());
        //验证是否已经实名认证
        if (realName == null ||
                !REAL_NAME_SUCESS.equals(realName.getRealnameAllStatus()) ||
                StringUtils.isBlank(realName.getRealName()) ||
                StringUtils.isBlank(realName.getIdCard())) {
            throw new SydException(ReturnCode.REAL_NAME_NOT_YET);
        }
        ZhimaBeanParam bean = BeanHelper.parseJson(request.getContent(), ZhimaBeanParam.class);
        bean.setName(realName.getRealName());
        bean.setCertNo(realName.getIdCard());
        bean.setChannel(request.getChannel());
        bean.setProduct(request.getProduct());
        //获取芝麻认证链接
        return authBusiness.getZhimaURL(bean);

    }

    /**
     * @param request
     * @return java.lang.Object
     * @description 检查用户是否已授权或授权是否过期
     * @author xulijie
     * @time 2017/1/23 19:38
     * @method checkZhimaAuth
     */
    public Object checkZhimaAuth(SydRequest request) {
        ZhimaBeanParam bean = BeanHelper.parseJson(request.getContent(), ZhimaBeanParam.class);
        BeanHelper.packageBean(request, bean);
        UserBusinessConvertUtils.checkUserId(request);
        RealName realName = getRealName(request.getUserId());
        bean.setCertNo(realName.getIdCard());
        bean.setName(realName.getRealName());
        return authBusiness.checkZhimaAuth(bean);
    }

    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.auth.AlipayScore
     * @description 保存芝麻信息 :跳过芝麻认证
     * @author Richard Core
     * @time 2017/1/23 20:51
     * @method saveZhimaInfo
     */
    @Override
    public AlipayScore saveZhimaInfo(SydRequest request) {
        //验证参数
        if (request.getUserId() == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        AlipayScore addAlipayScore = BeanHelper.parseJson(request.getContent(), AlipayScore.class);
        if (addAlipayScore == null || addAlipayScore.getAlipaySkipAuthorize() == null) {
            throw new SydException(ReturnCode.PARAM_REQUIRED);
        }
        addAlipayScore.setAlipayScore(ALIPAYSCORE);//跳过时芝麻分默认保存为0
        addAlipayScore.setAlipayUsersStatus(ALIPAYUSERSSTATUS);//认证状态默认为-2未处理
        RealName realName = getRealName(request.getUserId());
        if (realName == null ||
                !REAL_NAME_SUCESS.equals(realName.getRealnameAllStatus()) ||
                StringUtils.isBlank(realName.getRealName()) ||
                StringUtils.isBlank(realName.getIdCard())) {
            throw new SydException(ReturnCode.REAL_NAME_NOT_YET);
        }
        addAlipayScore.setCardId(realName.getIdCard());
        addAlipayScore.setRealName(realName.getRealName());
        BeanHelper.packageBean(request, addAlipayScore);
        Map<String, Object> commonFields = UserBusinessConvertUtils.initCommonFields(request);
        //保存芝麻分
        AlipayScore alipayScore = authBusiness.saveAlipayScore(addAlipayScore, commonFields);
        return alipayScore;
    }

    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.auth.AlipayScore
     * @description 获取芝麻分 和 是否跳过芝麻认证
     * @author Richard Core
     * @time 2017/1/23 20:51
     * @method getZhimaInfo
     */
    @Override
    public AlipayScore getZhimaInfo(SydRequest request) {
        if (request.getUserId() == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        AlipayScore alipayScore = authBusiness.getZhimaInfo(request);
        return alipayScore;
    }


    /**
     * @param request
     * @return ResponseEntity
     * @description 语音识别
     * @author liuhanqing
     * @time 2017/2/09 19:41
     * @method voiceAuth
     */
    @Override
    public VoiceAuthResult voiceAuth(SydRequest request, HttpServletRequest httpServletRequest) throws SydException {
        if (request == null || StringUtils.isBlank(request.getContent())) {
            throw new SydException(ReturnCode.PARAM_VALID);
        }
        Long userId = request.getUserId();
        if (userId == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        //验证是否有正在处理的语音识别数据
        checkVoiceAuthProcessing(userId);
        //验证是否超过当天认证次数阈值
//        checkAuthVoiceToday(userId);

        //验证是否进行过实名认证 并获取实名和手机号保存
        RealName realName = getRealName(userId);
        if (!UserBusinessConvertUtils.checkRealName(realName)) {
            throw new SydException(ReturnCode.REAL_INFO_INVALID);
        }
        String realNameStr = realName.getRealName();
        UserBasicInfoRsp userInfo = userService.getUserInfoByUserId(userId);
        VoiceAuthUploadParam uploadParam = BeanHelper.parseJson(request.getContent(), VoiceAuthUploadParam.class);
        uploadParam.setRealName(realNameStr);
        uploadParam.setPhone(userInfo.getPhone());
        uploadParam.setUserId(userId);
        uploadParam.setUploadBeginDate(new Date());

        //保存上传进度
        setVoiceUploadingProcessing(userId, AuthProcessEnum.PROCESS);
        //上传文件
        try {
            // 转型为MultipartHttpRequest：
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) httpServletRequest;
            // 从其中取出一个文件
            MultipartFile file = multipartRequest.getFile("file");
            UploadResultDto uploadResultDto = uploadService.uploadByMultipartFile(request, file);
            uploadParam.setFilePath(uploadResultDto.getUrl());
            //调用语音认证
            uploadParam.setFile(file);
            VoiceAuthResult voiceAuthResult = authBusiness.voiceAuth(uploadParam);
            //更新进度
            clearVoiceUploadingProcessing(userId);
            return voiceAuthResult;
        } catch (Exception e) {
            //清理进度
            clearVoiceUploadingProcessing(userId);
            throw e;
        }
    }

    /**
     * @param userId
     * @return void
     * @description 验证语音是否超过当天认证次数阈值
     * @author Richard Core
     * @time 2017/3/4 16:18
     * @method checkAuthVoiceToday
     */
    private void checkAuthVoiceToday(Long userId) {
        String timesAloud = commonService.getCacheSysConfig(ZkConfigConstant.SYD_AUTH_VOICE_TIMES_PER_DAY.getZkValue());
        int timesToday = authBusiness.getAuthVoiceTimesToday(userId);
        if (StringUtils.isNotBlank(timesAloud)) {
            Integer times = Integer.valueOf(timesAloud);
            if (timesToday >= times) {
                throw new SydException(ReturnCode.VOICE_AUTH_ERROR.getCode(),"您的认证操作频繁，明天再来试试吧");
            }
        }
    }

    /**
     * @param userId
     * @return void
     * @description 验证人脸识别是否超过当天认证次数阈值
     * @author Richard Core
     * @time 2017/3/4 16:18
     * @method checkAuthFaceToday
     */
    private void checkAuthFaceToday(Long userId) {
        String timesAloud = commonService.getCacheSysConfig(ZkConfigConstant.SYD_AUTH_FACE_TIMES_PER_DAY.getZkValue());
        int timesToday = authBusiness.getAuthFaceTimesToday(userId);
        if (StringUtils.isNotBlank(timesAloud)) {
            Integer times = Integer.valueOf(timesAloud);
            if (timesToday >= times) {
                throw new SydException(ReturnCode.FACE_AUTH_ERROR.getCode(), "您的认证操作频繁，明天再来试试吧");
            }
        }
    }

    /**
     * @param userId
     * @return void
     * @description 验证是否有正在处理的语音识别数据
     * @author Richard Core
     * @time 2017/2/27 14:13
     * @method checkVoiceAuthProcessing
     */
    private void checkVoiceAuthProcessing(Long userId) {
        if (userId == null) {
            return;
        }
        AuthProcess voiceProcess = null;
        try {
            voiceProcess = getVoiceCompareResultByUserId(userId);
        } catch (Exception e) {
            LOGGER.warn("验证是否有正在处理的语音识别数据异常" + e);
        }
        if (voiceProcess == null) {
            return;
        }
        switch (voiceProcess.getAuthProcess()) {
            case PROCESS://("PROCESS", "认证中"),
                throw new SydException(ReturnCode.VOICE_AUTH_ERROR.getCode(), "有语音正在认证，请稍后查看认证结果");
            case SUCCESS://("SUCCESS", "已认证"),
            case FAIL://("FAIL", "认证失败"),
            case NODATA://("AuthProcessEnum.NODATA", "未认证")
        }
    }


    /**
     * @param userId
     * @param authProcessEnum
     * @return void
     * @description 保存正在上传进度
     * @author Richard Core
     * @time 2017/2/20 21:32
     * @method setVoiceUploadingProcessing
     */
    private void setVoiceUploadingProcessing(Long userId, AuthProcessEnum authProcessEnum) {
        if (userId == null) {
            return;
        }
        AuthProcess result = new AuthProcess();
        String redisKey = AuthBusinessConvert.getAuthVoiceProcessRedisKey(userId);
        result.setUserId(userId);
        result.setAuthProcess(authProcessEnum);
        redisBusiness.setValue(redisKey, JSON.toJSONString(result));
    }

    /**
     * @param userId
     * @return void
     * @description 删除redis进度
     * @author Richard Core
     * @time 2017/2/20 21:29
     * @method clearVoiceUploadingProcessing
     */
    private void clearVoiceUploadingProcessing(Long userId) {
        if (userId == null) {
            return;
        }
        String redisKey = AuthBusinessConvert.getAuthVoiceProcessRedisKey(userId);
        redisBusiness.delValue(redisKey);
    }

    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.auth.AuthIdentityProcess
     * @description 获取语音识别进度结果
     * @author Richard Core
     * @time 2017/2/21 10:52
     * @method getVoiceProcessing
     */
    @Override
    public AuthIdentityProcess getVoiceProcessing(SydRequest request) {
        if (request == null || request.getUserId() == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        Long userId = request.getUserId();
        AuthProcess authProcess = getVoiceCompareResultByUserId(userId);
        AuthIdentityProcess identityProcess = new AuthIdentityProcess();
        identityProcess.setUserId(userId);
        if (authProcess == null) {
            identityProcess.setVoiceProcess(AuthProcessEnum.NODATA.getProcess());
            identityProcess.setFaceProcessDesc(AuthProcessEnum.NODATA.getDesc());
            return identityProcess;
        }
        identityProcess.setVoiceProcess(authProcess.getAuthProcess().getProcess());
        identityProcess.setFaceProcessDesc(authProcess.getAuthProcess().getDesc());
        return identityProcess;
    }

    /**
     * @param userId
     * @return com.mobanker.shanyidai.api.dto.auth.VoiceCompareResult
     * @description 获取语音识别进度结果
     * @author Richard Core
     * @time 2017/2/21 10:52
     * @method getVoiceProcessing
     */
    private AuthProcess getVoiceCompareResultByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        //从缓存中能获取到代表未解析完
        AuthProcess result = new AuthProcess();
        String repeatLoginKey = AuthBusinessConvert.getAuthVoiceProcessRedisKey(userId);
        String storeValue = redisBusiness.getValue(repeatLoginKey);
        if (StringUtils.isNotBlank(storeValue)) {
            result = BeanHelper.parseJson(storeValue, AuthProcess.class);
            return result;
        }
        AuthProcess authVoiceProcess = authBusiness.getAuthVoiceProcess(userId, result);
        if (AuthProcessEnum.SUCCESS.equals(authVoiceProcess.getAuthProcess())) {
            setVoiceUploadingProcessing(userId, AuthProcessEnum.SUCCESS);
        }
        return authVoiceProcess;
    }




    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.auth.VoiceOrder
     * @description 查询语音解析结果
     * @author Richard Core
     * @time 2017/2/10 13:49
     * @method findVoiceResult
     */
    @Override
    public VoiceOrder findVoiceResult(SydRequest request) {
        JSONObject jsonObject = JSONObject.parseObject(request.getContent());
        String orderid = jsonObject.getString("orderid");
        VoiceOrder voiceResult = authBusiness.findVoiceResult(orderid);
        return voiceResult;
    }

    /**
     * @param request
     * @param httpServletRequest
     * @return java.lang.Object
     * @description 人脸识别
     * @author Richard Core
     * @time 2017/2/20 16:42
     * @method faceAuth
     */
    @Override
    public Object faceAuth(SydRequest request, HttpServletRequest httpServletRequest) {
        if (request == null || StringUtils.isBlank(request.getContent())) {
            throw new SydException(ReturnCode.PARAM_VALID);
        }
        Long userId = request.getUserId();
        if (userId == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        //查询redis是否有活体认证正在进行 或者是已经成功  暂不清楚已经成功的是不是不允许再次认证，将成功状态排除
        AuthProcess authProcess = getFaceAuthRecordInCache(userId);
        if (authProcess != null && !AuthProcessEnum.SUCCESS.equals(authProcess.getAuthProcess())) {
            throw new SydException(ReturnCode.FACE_AUTH_ERROR.getCode(), authProcess.getAuthProcess().getDesc());
        }
        //判断人脸识别次数是否超过当天阈值
//        checkAuthFaceToday(userId);
        //验证是否有实名认证信息
        RealName realName = getRealName(userId);
        if (!UserBusinessConvertUtils.checkRealName(realName)) {
            throw new SydException(ReturnCode.REAL_INFO_INVALID);
        }
        //保存人脸识别认证进度到缓存
        saveFaceAuthProcess2Cache(userId, AuthProcessEnum.PROCESS);
        FaceAuthResult verifyResult = null;
        try {
            //人脸识别数据上传
            FaceAuthResult uploadResult = faceAuthUpload(request, httpServletRequest);
            //活体防伪
            faceAuthHackDetect(uploadResult.getLivenessDataId(), userId);
            //水印照片比对
            WatermarkVerifyParam param = new WatermarkVerifyParam();
            param.setLivenessDataId(uploadResult.getLivenessDataId());
            param.setIdCardNo(realName.getIdCard());
            param.setRealName(realName.getRealName());
            verifyResult = faceAuthWatermarkVerify(param, userId);
            verifyResult.setLivenessDataId(uploadResult.getLivenessDataId());
        } catch (Exception e) {
            //清除进度
            clearFaceAuthRecordInCache(userId);
            throw e;
        }
        return verifyResult;
    }

    /**
     * @param userId
     * @param authProcess
     * @return void
     * @description 保存人脸识别认证进度到缓存
     * @author Richard Core
     * @time 2017/2/23 13:38
     * @method saveFaceAuthProcess2Cache
     */
    private void saveFaceAuthProcess2Cache(Long userId, AuthProcessEnum authProcess) {
        String authFaceKey = AuthBusinessConvert.getAuthFaceRedisKey(userId);
        AuthProcess saveRecord = new AuthProcess();
        saveRecord.setUserId(userId);
        saveRecord.setAuthProcess(authProcess);
        redisBusiness.setValue(authFaceKey, JSON.toJSONString(saveRecord));
    }

    /**
     * @param userId
     * @return com.mobanker.shanyidai.api.dto.auth.AuthProcess
     * @description 获取缓存中人脸识别的进度信息
     * @author Richard Core
     * @time 2017/2/23 11:59
     * @method getFaceAuthRecordInCache
     */
    private AuthProcess getFaceAuthRecordInCache(Long userId) {
        String authFaceKey = AuthBusinessConvert.getAuthFaceRedisKey(userId);
        String storeValue = redisBusiness.getValue(authFaceKey);
        if (StringUtils.isBlank(storeValue)) {
            return null;
        }
        return BeanHelper.parseJson(storeValue, AuthProcess.class);
    }

    /**
     * @param userId
     * @return void
     * @description 清除缓存中人脸识别的进度信息
     * @author Richard Core
     * @time 2017/2/23 11:59
     * @method clearFaceAuthRecordInCache
     */
    private void clearFaceAuthRecordInCache(Long userId) {
        String authFaceKey = AuthBusinessConvert.getAuthFaceRedisKey(userId);
        redisBusiness.delValue(authFaceKey);
    }


    /**
     * @param request
     * @param httpServletRequest
     * @return com.mobanker.shanyidai.api.dto.auth.FaceAuthResult
     * @description 人脸识别文件上传
     * @author Richard Core
     * @time 2017/2/23 10:05
     * @method faceAuthUpload
     */
    private FaceAuthResult faceAuthUpload(SydRequest request, HttpServletRequest httpServletRequest) {
        //设置上传参数
        FaceAuthUploadParam uploadParam = new FaceAuthUploadParam();
        Long userId = request.getUserId();
        uploadParam.setUserId(userId);
        uploadParam.setMethodBeginDate(new Date());
        // 转型为MultipartHttpRequest：
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) httpServletRequest;
        //上传文件到阿里云
        List<String> picList = new ArrayList<String>();
        for (FaceAuthPicParamEnum picParam : FaceAuthPicParamEnum.values()) {
            MultipartFile file = multipartRequest.getFile(picParam.name());
            if (file == null || file.isEmpty()) {
                throw new SydException(ReturnCode.UPLOAD_FILE_NOT_NULL);
            }
            UploadResultDto uploadResultDto = uploadFile2Aliyun(request, file);
            if (uploadResultDto == null || StringUtils.isBlank(uploadResultDto.getUrl())) {
                throw new SydException(ReturnCode.FACE_AUTH_FILE_UPLOAD_ERROR);
            }
            picList.add(uploadResultDto.getUrl());
        }
        uploadParam.setPicPath(picList);
        // 从其中取出加密文件
        MultipartFile file = multipartRequest.getFile("file");
        File localFile = uploadService.cacheFile2Local(file);
        uploadParam.setFilePath(file.getOriginalFilename());
        FaceAuthResult result = authBusiness.faceAuthUpload(uploadParam, file);
        //保存人脸识别认证进度到缓存
        saveFaceAuthProcess2Cache(userId, AuthProcessEnum.PROCESS);
        if (localFile.exists()) {
            localFile.delete();
        }
        return result;
    }


    /**
     * @param request
     * @param file
     * @return com.mobanker.shanyidai.api.dto.upload.UploadResultDto
     * @description 上传文件到阿里云
     * @author Richard Core
     * @time 2017/2/21 11:24
     * @method uploadFile2Aliyun
     */
    private UploadResultDto uploadFile2Aliyun(SydRequest request, MultipartFile file) {
        //上传文件
        try {
            UploadResultDto uploadResultDto = uploadService.uploadByMultipartFile(request, file);
            return uploadResultDto;
        } catch (Exception e) {
            throw new SydException(ReturnCode.FACE_AUTH_FILE_UPLOAD_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * @param livenessDataId
     * @param userId
     * @return FaceAuthResult
     * @description 活体识别-活体防伪
     * @author Richard Core
     * @time 2017/2/22 21:16
     * @method faceAuthHackDetect
     */
    @Override
    public FaceAuthResult faceAuthHackDetect(String livenessDataId, Long userId) {
        FaceAuthResult result = authBusiness.faceAuthHackDetect(livenessDataId);
        return result;
    }

    /**
     * @param param
     * @param userId
     * @return FaceAuthResult
     * @description 活体数据与水印照片比对
     * @author Richard Core
     * @time 2017/2/22 17:21
     * @method faceAuthWatermarkVerify
     */
    @Override
    public FaceAuthResult faceAuthWatermarkVerify(WatermarkVerifyParam param, Long userId) {
        FaceAuthResult result = authBusiness.faceAuthWatermarkVerify(param);
        //清除人脸识别认证进度到缓存
        clearFaceAuthRecordInCache(userId);
        return result;
    }

    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.auth.AuthIdentityProcess
     * @description 查询人脸识别进度
     * @author Richard Core
     * @time 2017/2/23 20:18
     * @method getAuthFaceRecord
     */
    @Override
    public AuthIdentityProcess getAuthFaceRecord(SydRequest request) {
        if (request == null || request.getUserId() == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        Long userId = request.getUserId();
        AuthProcess authFaceProcess = getAuthFaceProcess(userId);
        AuthIdentityProcess authIdentityProcess = new AuthIdentityProcess();
        authIdentityProcess.setUserId(authFaceProcess.getUserId());
        authIdentityProcess.setFaceProcess(authFaceProcess.getAuthProcess().getProcess());
        authIdentityProcess.setFaceProcessDesc(authFaceProcess.getAuthProcess().getDesc());
        return authIdentityProcess;
    }

    /**
     * @param userId
     * @return com.mobanker.shanyidai.api.dto.auth.AuthProcess
     * @description 查询人脸识别进度
     * @author Richard Core
     * @time 2017/2/27 21:04
     * @method getAuthFaceProcess
     */
    private AuthProcess getAuthFaceProcess(Long userId) {
        //先从缓存中取，缓存中没有取数据库中记录
        AuthProcess redisRecord = getFaceAuthRecordInCache(userId);
        if (redisRecord != null) {
            return redisRecord;
        }
        FaceAuthProcessEnum instance = null;
        try {
            //查询数据库记录
            AuthFaceRecord authFaceRecord = authBusiness.getAuthFaceRecord(userId);
            if (authFaceRecord == null) {
                return mapAuthProcess(userId, AuthProcessEnum.NODATA);
            }
            instance = FaceAuthProcessEnum.getInstance(authFaceRecord.getProcess());
        } catch (Exception e) {
            return mapAuthProcess(userId, AuthProcessEnum.NODATA);
        }
        switch (instance) {
            case UPLOAD://("UPLOAD", "正在上传数据"),
            case HEAK_DETECT://("HEAK_DETECT", "正在进行防伪验证"),
            case VERIFY://("VERIFY", "正在认证"),
                return mapAuthProcess(userId, AuthProcessEnum.PROCESS);
            case UPLOAD_FAIL://("UPLOAD_FAIL", "上传识别数据失败"),
            case HEAK_FAIL://("HEAK_FAIL", "防伪认证失败"),
            case VERIFY_FAIL://("VERIFY_FAIL", "认证失败")
                return mapAuthProcess(userId, AuthProcessEnum.FAIL);
            case SUCCESS://("SUCCESS", "认证成功"),:
                //认证成功将记录放到缓存中
                saveFaceAuthProcess2Cache(userId, AuthProcessEnum.SUCCESS);
                return mapAuthProcess(userId, AuthProcessEnum.SUCCESS);
            default:
                return mapAuthProcess(userId, AuthProcessEnum.NODATA);
        }
    }

    /**
     * @param userId
     * @param authProcess
     * @return com.mobanker.shanyidai.api.dto.auth.AuthProcess
     * @description 封装redis的进度信息
     * @author Richard Core
     * @time 2017/2/27 21:05
     * @method mapAuthProcess
     */
    private AuthProcess mapAuthProcess(Long userId, AuthProcessEnum authProcess) {
        AuthProcess record = new AuthProcess();
        record.setUserId(userId);
        record.setAuthProcess(authProcess);
        return record;
    }

    /**
     * @param userId
     * @return java.lang.Object
     * @description 查询身份认证信息 包括人脸识别和语音识别进度
     * @author Richard Core
     * @time 2017/2/28 11:47
     * @method getIdentityAuthProcess
     */
    @Override
    public AuthIdentityProcess getIdentityAuthProcess(Long userId) {
        if (userId == null) {
            return mapIdentityProcess(userId, AuthProcessEnum.NODATA, AuthProcessEnum.NODATA, AuthProcessEnum.NODATA);
        }
        AuthProcess faceProcess = getAuthFaceProcess(userId);
        AuthProcess voiceProcess = getVoiceCompareResultByUserId(userId);
        if ((faceProcess == null || AuthProcessEnum.NODATA.equals(faceProcess.getAuthProcess()))
                && (voiceProcess == null || AuthProcessEnum.NODATA.equals(voiceProcess.getAuthProcess()))) {
            return mapIdentityProcess(userId, AuthProcessEnum.NODATA, AuthProcessEnum.NODATA, AuthProcessEnum.NODATA);
        }
        AuthProcessEnum faceEnum = faceProcess.getAuthProcess();
        AuthProcessEnum voiceEnum = voiceProcess.getAuthProcess();
        if (AuthProcessEnum.FAIL.equals(faceEnum) && AuthProcessEnum.FAIL.equals(voiceEnum)) {
            return mapIdentityProcess(userId, AuthProcessEnum.FAIL, faceEnum,voiceEnum);
        }
        if (AuthProcessEnum.SUCCESS.equals(faceEnum) && AuthProcessEnum.SUCCESS.equals(voiceEnum)) {
            return mapIdentityProcess(userId, AuthProcessEnum.SUCCESS, faceEnum, voiceEnum);
        } else {
            return mapIdentityProcess(userId, AuthProcessEnum.PROCESS, faceEnum, voiceEnum);
        }
    }

    /**
     * @param userId
     * @param identityProcess
     * @param faceProcess
     * @param voiceProcess
     * @return com.mobanker.shanyidai.api.dto.auth.AuthIdentityProcess
     * @description 封装身份认证进度信息
     * @author Richard Core
     * @time 2017/2/28 11:56
     * @method mapIdentityProcess
     */
    private AuthIdentityProcess mapIdentityProcess(Long userId, AuthProcessEnum identityProcess, AuthProcessEnum faceProcess, AuthProcessEnum voiceProcess) {
        AuthIdentityProcess result = new AuthIdentityProcess();
        result.setUserId(userId);
        result.setIdentityProcess(identityProcess.getProcess());
        result.setIdentityProcessDesc(identityProcess.getDesc());
        result.setFaceProcess(faceProcess.getProcess());
        result.setFaceProcessDesc(faceProcess.getDesc());
        result.setVoiceProcess(voiceProcess.getProcess());
        result.setVoiceProcessDesc(voiceProcess.getDesc());
        return result;

    }
}
