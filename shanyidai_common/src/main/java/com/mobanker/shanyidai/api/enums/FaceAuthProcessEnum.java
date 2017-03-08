package com.mobanker.shanyidai.api.enums;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import org.apache.commons.lang.StringUtils;

/**
 * @desc: 人脸识别进度枚举 （闪宜贷项目使用）
 * @author: Richard Core
 * @create time: 2017/2/23 10:42
 */
public enum FaceAuthProcessEnum {
    UPLOAD("UPLOAD","正在上传数据"),
    HEAK_DETECT("HEAK_DETECT","正在进行防伪验证"),
    VERIFY("VERIFY", "正在认证"),
    SUCCESS("SUCCESS", "认证成功"),
    UPLOAD_FAIL("UPLOAD_FAIL", "上传识别数据失败"),
    HEAK_FAIL("HEAK_FAIL","防伪认证失败"),
    VERIFY_FAIL("VERIFY_FAIL", "认证失败")
    ;

    private String process;//进度状态
    private String desc;//进度说明

    public static FaceAuthProcessEnum getInstance(String process) {
        if (StringUtils.isBlank(process)) {
            throw new SydException(ReturnCode.ENUM_ERROR);
        }
        for (FaceAuthProcessEnum faceAuthProcessEnum : FaceAuthProcessEnum.values()) {
            if (faceAuthProcessEnum == null) {
                continue;
            }
            if (faceAuthProcessEnum.getProcess().equals(process)) {
                return faceAuthProcessEnum;
            }
        }
        throw new SydException(ReturnCode.ENUM_ERROR);
    }
    FaceAuthProcessEnum(String process, String desc) {
        this.process = process;
        this.desc = desc;
    }

    public String getProcess() {
        return process;
    }

    public String getDesc() {
        return desc;
    }
}
