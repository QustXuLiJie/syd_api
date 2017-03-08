package com.mobanker.shanyidai.api.service.message;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mobanker.shanyidai.api.business.common.CommonBusiness;
import com.mobanker.shanyidai.api.business.message.MessageBusiness;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.constant.ZkConfigConstant;
import com.mobanker.shanyidai.api.common.enums.SmsTemplateIdEnum;
import com.mobanker.shanyidai.api.common.enums.SmsTypeEnum;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.logger.LogManager;
import com.mobanker.shanyidai.api.common.logger.Logger;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.common.tool.CommonUtil;
import com.mobanker.shanyidai.api.common.tool.StringKit;
import com.mobanker.shanyidai.api.dto.message.SmsMesage;
import com.mobanker.shanyidai.api.dto.message.SysMessage;
import com.mobanker.shanyidai.api.dto.message.SysMessageParam;
import com.mobanker.shanyidai.api.dto.message.ValidateSmsCaptcha;
import com.mobanker.shanyidai.dubbo.dto.message.SmsMesageParamDto;
import com.mobanker.shanyidai.dubbo.dto.message.ValidateSmsCaptchaParamDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mobanker.shanyidai.api.service.message.util.MessageBizConvert.checkValidateSmsCaptchaParam;

@Service
public class MessageServiceImpl implements MessageService {
    private static Logger logger = LogManager.getSlf4jLogger(MessageServiceImpl.class);
    @Resource
    private MessageBusiness messageBusiness;
    //    @Resource
//    private UserBusiness userBusiness;
    @Resource
    private CommonBusiness commonBusiness;

    /**
     * 发送验证码
     *
     * @param request
     * @param appVersion
     * @return
     * @throws SydException
     */
    @Override
    public Map<String, String> sendSms(SydRequest request, String appVersion) throws SydException {
        //参数获取
        SmsMesage smsParam = getSmsMesageSendParam(request, appVersion);
        //验证模板Id是否有效
        SmsTemplateIdEnum smsTemplateIdEnum = SmsTemplateIdEnum.getInstanceByType(smsParam.getType(), smsParam.getSendType());
        if (smsTemplateIdEnum == null) {
            throw new SydException(ReturnCode.TEMPLATE_ID_INVALID);
        }
        smsParam.setTemplateId(smsTemplateIdEnum.getId());
        SmsMesageParamDto smsMesageParamDto = BeanHelper.cloneBean(smsParam, SmsMesageParamDto.class);
        smsMesageParamDto.setFlagId(StringKit.getUUID());
        smsMesageParamDto.setPhone(smsParam.getPhone());
        smsMesageParamDto.setSystemId("app");
        smsMesageParamDto.setSystemCode(ReturnCode.CODE_PREFIX.getCode());


        String result = messageBusiness.sendSmsCaptcha(smsMesageParamDto);
        Map<String, String> realNameInfo = new HashMap<String, String>();
        realNameInfo.put("realnameFlag", result);
        return realNameInfo;
    }

    /**
     * @param request
     * @param appVersion
     * @return com.mobanker.shanyidai.api.dto.message.SmsMesage
     * @description 发送验证码参数获取
     * @author Richard Core
     * @time 2016/12/28 17:21
     * @method getSmsMesageSendParam
     */
    private SmsMesage getSmsMesageSendParam(SydRequest request, String appVersion) {
        SmsMesage smsParam = BeanHelper.parseJson(request.getContent(), SmsMesage.class);
        BeanHelper.packageBean(request, smsParam);
        CommonUtil.checkBaseParam(smsParam);
        CommonUtil.checkPhone(smsParam.getPhone());
        if (StringUtils.isBlank(smsParam.getType())) {
            throw new SydException(ReturnCode.SMS_TYPE_INVILID);
        }
        if (StringUtils.isBlank(smsParam.getSendType())) {
            throw new SydException(ReturnCode.SENDTYPE_INVILID);
        }
        return smsParam;
    }


    /**
     * @param request
     * @param appVersion
     * @return void
     * @description 验证验证码(requst中有手机号和验证码信息)
     * @author Richard Core
     * @time 2016/12/26 19:50
     * @method checkSmsCaptcha
     */
    @Override
    public void checkSmsCaptcha(SydRequest request, String appVersion) {
        //验证参数
        ValidateSmsCaptcha valicode = BeanHelper.parseJson(request.getContent(), ValidateSmsCaptcha.class);
        BeanHelper.packageBean(request, valicode);
        //验证码验证
        checkSmsCaptcha(valicode);
    }

    /**
     * @param valicode
     * @return void
     * @description 验证验证码
     * @author Richard Core
     * @time 2016/12/26 19:50
     * @method checkSmsCaptcha
     */
    @Override
    public void checkSmsCaptcha(ValidateSmsCaptcha valicode) {
        //验证参数
        checkValidateSmsCaptchaParam(valicode);
        checkVerifyCodeParam(valicode.getCaptcha());
        //封装验证的Dto参数
        ValidateSmsCaptchaParamDto validateSmsCaptchaDto = BeanHelper.cloneBean(valicode, ValidateSmsCaptchaParamDto.class);
        //封装验证的综合服务方法
        messageBusiness.validateSmsCaptcha(validateSmsCaptchaDto);
    }

    /**
     * @param
     * @return int
     * @description 获取验证码位数
     * @author Richard Core
     * @time 2016/12/28 16:15
     * @method getSmsVerifyCodeLength
     */
    public int getSmsVerifyCodeLength() {
        String cacheSysConfig = null;
        try {
            cacheSysConfig = commonBusiness.getCacheSysConfig(ZkConfigConstant.VERIFY_CODE_LENGTH.getZkValue());
        } catch (Exception e) {
            throw new SydException(ReturnCode.CONFIG_DATA_NULL);
        }
        return Integer.parseInt(cacheSysConfig);
    }

    /**
     * @param
     * @return verifyCode
     * @description 获取验证码位数
     * @author Richard Core
     * @time 2016/12/28 16:15
     * @method getSmsVerifyCodeLength
     */
    public void checkVerifyCodeParam(String verifyCode) {
        if (verifyCode == null) {
            logger.error("验证码为空");
            throw new SydException(ReturnCode.WRONG_VERIFYCODE.getCode(), ReturnCode.WRONG_VERIFYCODE.getDesc(), null);
        }
        int length = getSmsVerifyCodeLength();
        if (verifyCode.length() != length) {
            logger.error("验证码为空或者格式错误");
            throw new SydException(ReturnCode.WRONG_VERIFYCODE.getCode(), ReturnCode.WRONG_VERIFYCODE.getDesc(), null);
        }
    }

    /**
     * @param request
     * @param appVersion
     * @param phone
     * @param validCode
     * @param smsType
     * @return void
     * @description 验证验证码
     * @author Richard Core
     * @time 2016/12/27 19:56
     * @method checkValidCode
     */
    @Override
    public void checkSmsCaptcha(SydRequest request, String appVersion, String phone, String validCode, SmsTypeEnum smsType) {
        //设置参数
        ValidateSmsCaptcha valicode = new ValidateSmsCaptcha();
        valicode.setPhone(phone);
        valicode.setCaptcha(validCode);
        //设置用途
        valicode.setCaptchaUse(smsType.getSmsType());
        BeanHelper.packageBean(request, valicode);
        //验证方法
        checkSmsCaptcha(valicode);
    }


    /**
     * @param request
     * @return java.util.List<com.mobanker.shanyidai.api.dto.message.SysMessage>
     * @description 查询系统消息
     * @author Richard Core
     * @time 2017/2/15 10:58
     * @method getSysMessage
     */
    @Override
    public List<SysMessage> getSysMessage(SydRequest request) {

        if (request == null || request.getUserId() == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        SysMessageParam paramDto = BeanHelper.parseJson(request.getContent(), SysMessageParam.class);
        BeanHelper.packageBean(request,paramDto);
        return messageBusiness.getSysMessage(paramDto);
    }

    /**
     * @param request
     * @return void
     * @description 更新消息读取状态
     * @author Richard Core
     * @time 2017/2/15 10:59
     * @method updateReadStatus
     */
    @Override
    public void updateReadStatus(SydRequest request) {
        if (request == null || request.getUserId() == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        JSONObject jsonObject = JSONObject.parseObject(request.getContent());

        List<String> ids = JSONArray.parseArray(JSONObject.toJSONString(jsonObject.get("ids")), String.class);
        messageBusiness.updateReadStatus(ids);
    }

    /**
     * @param request
     * @return int
     * @description 查询未读系统消息数
     * @author Richard Core
     * @time 2017/2/15 10:59
     * @method getUnreadSysMessageAmount
     */
    @Override
    public long getUnreadSysMessageAmount(SydRequest request) {
        if (request == null || request.getUserId() == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        SysMessageParam paramDto = BeanHelper.parseJson(request.getContent(), SysMessageParam.class);
        BeanHelper.packageBean(request,paramDto);
        return messageBusiness.getUnreadSysMessageAmount(paramDto);
    }
}
