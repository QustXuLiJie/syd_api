package com.mobanker.shanyidai.api.enums;

/**
 * @desc: 查询订单历史查询类型
 * @author: Richard Core
 * @create time: 2017/1/15 15:36
 */
public enum BorrowTypeEnum {
    SHOUJIDAI(1,"手机贷"),
    SHANDIANDAI(2,"闪电贷"),
    DANQI(3,"单期"),
    FENQI(4,"分期"),
    YHFQ(5,"应花分期");

    private int queryType;
    private String desc;

    BorrowTypeEnum(int queryType, String desc) {
        this.queryType = queryType;
        this.desc = desc;
    }

    public int getQueryType() {
        return queryType;
    }

    public String getDesc() {
        return desc;
    }
}
