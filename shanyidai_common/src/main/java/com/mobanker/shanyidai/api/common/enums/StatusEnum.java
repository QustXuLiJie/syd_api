package com.mobanker.shanyidai.api.common.enums;

/**
 * @desc: 简单的 0/1 状态枚举
 * @author: Richard Core
 * @create time: 2017/1/12 19:47
 */
public enum StatusEnum {
    VALID("1", "有效"),
    INVALID("0", "无效");

    private String status;
    private String desc;

    StatusEnum(String status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
