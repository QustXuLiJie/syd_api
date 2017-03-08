package com.mobanker.shanyidai.api.enums;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import org.apache.commons.lang.StringUtils;

/**
 * @desc: 支付结果
 * @author: Richard Core
 * @create time: 2017/1/23 14:07
 */
public enum PayResultStatus {
    NO_RECORD("-1","无还款记录"),
    PROCESSING("0","处理中"),
    PAY_SUCCESS("1", "成功"),
    PAY_FAILED("2","失败");

    private String statusCode;
    private String desc;

    public static PayResultStatus getInstance(String statusCode) {
        if (StringUtils.isBlank(statusCode)) {
            throw new SydException(ReturnCode.ENUM_ERROR);
        }
        for (PayResultStatus payResultStatus : PayResultStatus.values()) {
            if (payResultStatus.getStatusCode().equals(statusCode)) {
                return payResultStatus;
            }
        }
        throw new SydException(ReturnCode.ENUM_ERROR);
    }
    PayResultStatus(String statusCode, String desc) {
        this.statusCode = statusCode;
        this.desc = desc;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getDesc() {
        return desc;
    }
}
