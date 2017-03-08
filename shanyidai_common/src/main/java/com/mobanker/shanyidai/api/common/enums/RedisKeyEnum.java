package com.mobanker.shanyidai.api.common.enums;

/**
 * @author hantongyang
 * @version 1.0
 * @description 保存到Redis中的key值
 * @date 创建时间：2016/12/24 14:21
 */
public enum RedisKeyEnum {
    SYD_SECURITY_UUID("SYD:SECURITY:"), //握手UUID
    SYD_SECURITY_SIGN("SYD:SIGN:"), //握手SIGN
    SYD_USER_CODE("SYD:CODE:"), //用户CODE
    SYD_USER_LOGIN_FAIL("SYD:LOGINFAIL:"), //用户登录失败
    SYD_USER_BASE_INFO("SYD:USER:BASEINFO:"), //用户基础信息
    SYD_USER_INFO("SYD:USER:INFO:"), //用户详细信息，但是不包含联系人信息
    SYD_PRODUCT_INFO("SYD:PRODUCT:INFO:"), //产品模板信息
    SYD_USER_CONTACT("SYD:USER:CONTACT:"), //用户联系人信息
    SYD_ADD_BORROW_ORDER("SYD:BORROW:ADD:ORDER:"), //保存借款单时防止重复提交缓存
    SYD_REPAY_AVOID_REPEAT("SYD:REPAY:AVOID:REPEAT:"), //确认还款时防止重复提交缓存
    LOGIN_REPEAT("SYD:LOGIN:REPEAT:"), //重复登录剔除
    SYD_DICTIONARY_INFO("SYD:DICTIONARY:INFO:"), //数据字典信息
    SYD_JOBTYPE_MAP("SYD:BASEDATA:JOBMAP"),//职位类型信息取值使用
    SYD_JOBTYPE_INFO("SYD:BASEDATA:JOBTYPE"),//职位类型信息
    SYD_RELATION_INFO("SYD:BASEDATA:RELATION"),//联系人关系信息
    SYD_EDUCATION_INFO("SYD:BASEDATA:EDUCATION"),//学历列表信息
    SYD_CONFIG("SYD:CONFIG:INFO:"), //配置信息
    /*认证相关*/
    VOICE_AUTH("SYD:AUTH:VOICE:"), //语音识别验证
    FACE_AUTH("SYD:AUTH:FACE:"), //人脸识别验证

    ;
    private RedisKeyEnum(String value) {
        this.value = value;
    }

    /**
     * @param
     * @return java.lang.String
     * @description value 的get方法
     * @author hantongyang
     * @time 2016/12/24 14:23
     * @method getValue
     */
    public String getValue() {
        return value;
    }

    private String value;
}
