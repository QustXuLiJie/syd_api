package com.mobanker.shanyidai.api.common.enums;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Richard Core
 * @description
 * @date 2016/12/26 21:50
 */
public enum SmsTemplateIdEnum {
    REGISTER_SMS_UZONE("register_sms_uzone","register","sms", "注册验证码"),
    REGISTER_VOICE_UZONE("register_voice_uzone","register","voice", "注册语音验证码"),
    BORROW_SMS_UZONE("borrow_sms_uzone","borrow","sms", "借款验证码"),
    BORROW_VOICE_UZONE("borrow_voice_uzone","borrow","voice", "借款语音验证码"),
    MODIFYPHONE_SMS_UZONE("modifyPhone_sms_uzone","modifyPhone","sms", "修改手机号验证码"),
    MODIFYPHONE_VOICE_UZONE("modifyPhone_voice_uzone","modifyPhone","voice", "修改手机号语音验证码"),
    FORGETPWD_SMS_UZONE("forgetPwd_sms_uzone","forgetPwd","sms", "忘记密码验证码"),
    FORGETPWD_VOICE_UZONE("forgetPwd_voice_uzone","forgetPwd","voice", "忘记密码语音验证码");

    private String id;//模板id
    private String type;//操作位置
    private String sendType;//发送类型
    private String desc;//描述

    /**
     * @param id
     * @return SmsTemplateIdEnum
     * @description 根据类型获取枚举实例
     * @author Richard Core
     * @time 2016/12/26 17:58
     * @method getInstance
     */
    public static SmsTemplateIdEnum getInstance(String id) {
        if (StringUtils.isBlank(id)) {
            throw new SydException(ReturnCode.TEMPLATE_ID_INVALID);
        }
        for (SmsTemplateIdEnum enums : values()) {
            if (enums.getId().equals(id)) {
                return enums;
            }
        }
        throw new SydException(ReturnCode.TEMPLATE_ID_INVALID);
    }

    /**
     * @param type
     * @param sendType
     * @return com.mobanker.shanyidai.api.common.enums.SmsTemplateIdEnum
     * @description 根据操作类型和发送方式获取模板id
     * @author Richard Core
     * @time 2016/12/28 17:13
     * @method getInstanceByType
     */
    public static SmsTemplateIdEnum getInstanceByType(String type,String sendType) {
        if (StringUtils.isBlank(type) ||
                StringUtils.isBlank(sendType)) {
            throw new SydException(ReturnCode.TEMPLATE_ID_INVALID);
        }
        for (SmsTemplateIdEnum enums : values()) {
            if (enums.getType().equalsIgnoreCase(type)
                    && enums.getSendType().equalsIgnoreCase(sendType)) {
                return enums;
            }
        }
        throw new SydException(ReturnCode.TEMPLATE_ID_INVALID);
    }

    SmsTemplateIdEnum(String id, String type, String sendType, String desc) {
        this.id = id;
        this.type = type;
        this.sendType = sendType;
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getSendType() {
        return sendType;
    }

    public String getDesc() {
        return desc;
    }
}
