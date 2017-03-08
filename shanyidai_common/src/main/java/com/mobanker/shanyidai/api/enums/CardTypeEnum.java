package com.mobanker.shanyidai.api.enums;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Richard Core
 * @description 银行卡类型
 * @date 2016/12/21 11:44
 */
public enum CardTypeEnum {
    DEBIT_CARD("D", "借记卡（储蓄卡）"),
    CREDIT_CARD("C", "贷记卡（信用卡）");

    private String type;//类型
    private String desc;//描述

    CardTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    /**
     * @param type
     * @return com.mobanker.shanyidai.api.enums.CardTypeEnum
     * @description 获取枚举实例
     * @author Richard Core
     * @time 2016/12/21 13:40
     * @method getInstance
     */
    public static CardTypeEnum getInstance(String type) {
        if (StringUtils.isBlank(type)) {
            throw new SydException(ReturnCode.ENUM_ERROR);
        }
        for (CardTypeEnum typeEnum : values()) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum;
            }
        }
        throw new SydException(ReturnCode.ENUM_ERROR);
    }
    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
