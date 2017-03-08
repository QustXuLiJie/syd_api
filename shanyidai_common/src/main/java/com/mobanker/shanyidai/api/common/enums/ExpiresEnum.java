package com.mobanker.shanyidai.api.common.enums;

/**
 * @author hantongyang
 * @version 1.0
 * @description 是否有效枚举类
 * @date 创建时间：2016/12/26 20:12
 */
public enum ExpiresEnum {

    INVALID("1"),// 无效
    EFFECTIVE("0"); // 默认，有效

    private ExpiresEnum(String value) {
        this.value = value;
    }

    /**
     * @param
     * @return java.lang.String
     * @description value 的get方法
     * @author Richard Core
     * @time 2016/12/10 15:47
     * @method getValue
     */
    public String getValue() {
        return value;
    }

    private String value;
}
