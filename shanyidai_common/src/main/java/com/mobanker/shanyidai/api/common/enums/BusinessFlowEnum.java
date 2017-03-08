package com.mobanker.shanyidai.api.common.enums;

/**
 * @author hantongyang
 * @description
 * @time 2017/2/28 11:58
 */
public enum BusinessFlowEnum {
    LOGIN("login", ""),
    REGISTER("register", ""),
    UPDATE_PHONE("updatePhone", ""),
    UPDATE_PASSWD("setPassword", ""),
    FORGET_PASSWORD("forgetPassword", ""),
    VOICE_AUTH("voiceAuth", ""),
    VOICE_PROCESS("getVoiceProcessing", ""),
    VOICE_FIND_RESULT("findVoiceResult", ""),
    FACE_AUTH("faceAuth", ""),
    FACE_PROCESS("getFaceProcessing", ""),
    CONTART_AUTH("addContact", ""),
    JOB_AUTH("addJob", ""),
    REALNAME_AUTH("addRealName", ""),
    REALNAME_FIND("getRealName", ""),
    COMPLETENESS("getCompleteness", ""),
    BANKCARD_AUTH("addBankCard", ""),
    BANKCARD_SETPAYCARD("setPayCard", ""),
    BORROW_CANBORROW("canBorrow", ""),
    BORROW_SYDBORROW("sydBorrowing", ""),
    BORROW_ADDBORROW("addBorrow", ""),
    REPAY_CANREPAY("canRepay", ""),
    REPAY_ADDONEKEY("addOnekeyRepay", ""),
    REPAY_ADDBAIDU("addBaiduPay", ""),
    REPAY_ADDWECHAT("addWechatRepay", ""),
    ;

    private String method;
    private String daoName;

    BusinessFlowEnum(String method, String daoName) {
        this.method = method;
        this.daoName = daoName;
    }

    public String getMethod() {
        return method;
    }

    public String getDaoName() {
        return daoName;
    }
}