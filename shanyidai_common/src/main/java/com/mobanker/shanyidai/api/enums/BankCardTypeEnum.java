package com.mobanker.shanyidai.api.enums;

/**
 * @desc: 银行卡类型
 * @author: Richard Core
 * @create time: 2017/1/3 14:24
 */
public enum BankCardTypeEnum {
    DEBIT_CARD("debitCard","借记卡"),
    CREDIT_CARD("creditCard", "信用卡");

    private String type;//类型
    private String desc;//描述

    BankCardTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
