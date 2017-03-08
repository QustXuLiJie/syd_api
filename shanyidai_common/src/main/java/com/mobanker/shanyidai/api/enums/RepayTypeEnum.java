package com.mobanker.shanyidai.api.enums;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import org.apache.commons.lang.StringUtils;

/**
 * @desc: 支付方式
 * @author: Richard Core
 * @create time: 2017/1/17 10:04
 */
public enum RepayTypeEnum {
    YINLIAN_SDK("9", "银联SDK"),
    YINLIAN_WAP("8", "银联WAP"),
    WECHAT("41", "微信"),
    ONE_KEy("20", "一键还款"),
    BAIDU_YIBAO("50", "百度钱包，易宝支付"),
    ;

    private String repayType;
    private String desc;

    public static RepayTypeEnum getInstance(String repayType) {
        if (StringUtils.isBlank(repayType)) {
            throw new SydException(ReturnCode.ENUM_ERROR.getCode(),"不支持的支付方式");
        }
        for (RepayTypeEnum repayTypeEnum : RepayTypeEnum.values()) {
            if (repayTypeEnum.getRepayType().equals(repayType)) {
                return repayTypeEnum;
            }
        }
        throw new SydException(ReturnCode.ENUM_ERROR.getCode(),"不支持的支付方式");
    }

    RepayTypeEnum(String repayType, String desc) {
        this.repayType = repayType;
        this.desc = desc;
    }

    public String getRepayType() {
        return repayType;
    }

    public String getDesc() {
        return desc;
    }
}
