package com.mobanker.shanyidai.api.common.constant;

/**
 * @author Richard Core
 * @description
 * @date 2016/12/12 10:15
 */
public enum SystemConstant {
    CHARACTER_ENCODING("UTF-8"),//编码
    OK("1"),// 成功 OK
    FAIL("0"), // 失败 FAIL
    SUCCESS("00000000"),
    PRODUCT("shanyidai"),//产品 PRODUCT
    APP_CHANNEL("app"),
    WCHAT_CHANNEL("wchat"),
    REDIS_BASIC_TIMEOUT("600"), //Redis缓存公共失效时间
    BORROW_TYPE("MICROLOAN"), //产品模板-借款类型
    PRODUCT_PERIOD("1"), //产品模板-期数

    ;

    private SystemConstant(String value) {
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
