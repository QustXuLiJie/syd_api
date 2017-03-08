package com.mobanker.shanyidai.api.enums;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author hantongyang
 * @description
 * @time 2017/1/14 15:59
 */
public enum ProductEnum {
    SHOUJIDAI("shoujidai", "手机贷"),
    UZONE("uzone", "U族"),
    YHFQ("yhfq", "应花分期"),
    SHANYIDAI("shanyidai", "闪宜贷"),
    ;

    private String productCode;
    private String productName;

    private ProductEnum(String productCode, String productName) {
        this.productCode = productCode;
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getProductName() {
        return productName;
    }

    /**
     * @description 根据产品编码获取产品枚举实体，供Switch使用
     * @author hantongyang
     * @time 2017/1/14 16:06
     * @method getProductEnum
     * @param productCode
     * @return com.mobanker.shanyidai.api.enums.ProductEnum
     */
    public static ProductEnum getProductEnum(String productCode){
        if(StringUtils.isBlank(productCode)){
            throw new SydException(ReturnCode.ENUM_ERROR);
        }
        for(ProductEnum productEnum : ProductEnum.values()){
            if(productEnum.getProductCode().equals(productCode)){
                return productEnum;
            }
        }
        throw new SydException(ReturnCode.ENUM_ERROR);
    }
}
