package com.mobanker.shanyidai.api.enums;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author hantongyang
 * @description
 * @time 2017/1/13 9:45
 */
public enum QlBorrowTypeEnum {

    SHOUJIDAI_LOAN("6","手机贷单期"),
    SHANDIANDAI_LOAN("7","闪电贷"),
    PERIOD_LOAN("8","现金分期"),
    TONGCHENG_LOAN("9","同程"),
    LARGE_LOAN("10","大额贷"),
    YHFQ_INSTALLMENT("11","应花分期"),
    UZONE_INSTALLMENT("51", "u族分期"),
    ;

    private String typeCode;//状态编号
    private String typeMsg;//状态说明

    private QlBorrowTypeEnum(String typeCode, String typeMsg) {
        this.typeCode = typeCode;
        this.typeMsg = typeMsg;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public String getTypeMsg() {
        return typeMsg;
    }

    /**
     * @description 根据编码返回枚举实体，供Switch使用
     * @author hantongyang
     * @time 2017/1/14 16:04
     * @method getTypeEnum
     * @param typeCode
     * @return com.mobanker.shanyidai.api.enums.QlBorrowTypeEnum
     */
    public static QlBorrowTypeEnum getTypeEnum(String typeCode){
        if(StringUtils.isBlank(typeCode)){
            throw new SydException(ReturnCode.ENUM_ERROR);
        }
        for(QlBorrowTypeEnum typeEnum : QlBorrowTypeEnum.values()){
            if(typeEnum.getTypeCode().equals(typeCode)){
                return typeEnum;
            }
        }
        throw new SydException(ReturnCode.ENUM_ERROR);
    }
}
