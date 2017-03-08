package com.mobanker.shanyidai.api.common.enums;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Richard Core
 * @description 短信类型
 * @date 2016/12/26 17:06
 */
public enum SmsTypeEnum {

    REGISTER("register", "smscode", "注册"),
    FORGET_PWD("forgetPwd", "resetpwcode", "忘记密码"),
    borrow("borrow", "borrowcode", "借款"),
    MODIFY_PHONE("modifyPhone", "resetphonecode", "修改手机号");

    private String smsType;//类型
    private String smsCode;//code
    private String desc;//描述

    /**
     * @param smsType
     * @return com.mobanker.shanyidai.api.common.enums.SmsTypeEnum
     * @description 根据类型获取枚举实例
     * @author Richard Core
     * @time 2016/12/26 17:58
     * @method getInstanceByType
     */
    public static SmsTypeEnum getInstanceByType(String smsType) {
        if (StringUtils.isBlank(smsType)) {
            throw new SydException(ReturnCode.SMS_TYPE_INVILID);
        }
        for (SmsTypeEnum smsTypeEnum : values()) {
            if (smsTypeEnum.getSmsType().equals(smsType)) {
                return smsTypeEnum;
            }
        }
        throw new SydException(ReturnCode.SMS_TYPE_INVILID);
    }

    SmsTypeEnum(String smsType, String smsCode, String desc) {
        this.smsType = smsType;
        this.smsCode = smsCode;
        this.desc = desc;
    }

    public String getSmsType() {
        return smsType;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public String getDesc() {
        return desc;
    }
}
