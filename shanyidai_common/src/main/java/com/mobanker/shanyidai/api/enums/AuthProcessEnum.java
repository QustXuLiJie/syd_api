package com.mobanker.shanyidai.api.enums;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import org.apache.commons.lang3.StringUtils;

/**
 * @desc: 认证进度枚举
 * @author: Richard Core
 * @create time: 2017/2/27 17:10
 */
public enum AuthProcessEnum {
    PROCESS("PROCESS", "认证中"),
    SUCCESS("SUCCESS", "已认证"),
    FAIL("FAIL", "认证失败"),
    NODATA("NODATA", "未认证");

    private String process;//进度
    private String desc;//说明

    AuthProcessEnum(String process, String desc) {
        this.process = process;
        this.desc = desc;
    }

    public static AuthProcessEnum getInstance(String process) {
        if (StringUtils.isBlank(process)) {
            throw new SydException(ReturnCode.ENUM_ERROR);
        }
        for (AuthProcessEnum enumItem : AuthProcessEnum.values()) {
            if (enumItem == null) {
                continue;
            }
            if (enumItem.getProcess().equals(process)) {
                return enumItem;
            }
        }
        throw new SydException(ReturnCode.ENUM_ERROR);
    }
    public String getProcess() {
        return process;
    }

    public String getDesc() {
        return desc;
    }
}
