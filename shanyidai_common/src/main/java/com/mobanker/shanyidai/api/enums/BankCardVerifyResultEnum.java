package com.mobanker.shanyidai.api.enums;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import org.apache.commons.lang3.StringUtils;

/**
 * @desc: 银行卡认证结果枚举
 * @author: Richard Core
 * @create time: 2017/1/4 20:35
 */
public enum BankCardVerifyResultEnum {
    PROCEING("D","表示处理中"),
    VALID("T","表示有效的"),
    INVALID("F","表示无效的"),
    INVERIFY("N","表示无法认证的"),
    TIMEOUT("P","表示网络连接超时");


    private String value;//认证结果
    private String desc;//描述

    public static BankCardVerifyResultEnum getInstance(String value) {
        if (StringUtils.isBlank(value)) {
            throw new SydException(ReturnCode.ENUM_ERROR);
        }
        for (BankCardVerifyResultEnum enums : BankCardVerifyResultEnum.values()) {
            if (enums.getValue().equals(value)) {
                return enums;
            }
        }
        throw new SydException(ReturnCode.BANKCARD_VERIFY_FAILED);
    }


    BankCardVerifyResultEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
