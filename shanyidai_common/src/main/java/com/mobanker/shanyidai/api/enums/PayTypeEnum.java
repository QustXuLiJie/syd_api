package com.mobanker.shanyidai.api.enums;

/**
 * @desc: 支付方式
 * @author: Richard Core
 * @create time: 2017/1/22 14:16
 */
public enum PayTypeEnum {
    BAIDU("baidu","百度钱包"),
    YIBAO("yibao","易宝")
    ;

    private String type;
    private String desc;

    PayTypeEnum(String type, String desc) {
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
