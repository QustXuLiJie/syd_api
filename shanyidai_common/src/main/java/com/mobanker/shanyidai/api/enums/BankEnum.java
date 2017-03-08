package com.mobanker.shanyidai.api.enums;

/**
 * desc:银行信息枚举
 * author: Richard Core
 * create time: 2017/1/5 17:23
 */
public enum BankEnum {
    ICBC("ICBC","中国工商银行"),
    CCB("CCB","中国建设银行"),
    ABC("ABC","中国农业银行"),
    PSBC("PSBC","中国邮政储蓄银行"),
    BOC("BOC","中国银行"),
    CMBC("CMBC","招商银行"),
    BCOMM("BCOMM","交通银行"),
    PABC("PABC","平安银行"),
    CMBA("CMBA","中国民生银行"),
    CEBBANK("CEBBANK","中国光大银行"),
    CITIC("CITIC","中信银行"),
    CIB("CIB","兴业银行"),
    SPDB("SPDB","上海浦东发展银行"),
    BOB("BOB","北京银行"),
    BOS("BOS","上海银行")

    ;

    private String bankCode;//银行编号
    private String bankName;//银行名称



    BankEnum(String bankCode, String bankName) {
        this.bankCode = bankCode;
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public String getBankName() {
        return bankName;
    }
}
