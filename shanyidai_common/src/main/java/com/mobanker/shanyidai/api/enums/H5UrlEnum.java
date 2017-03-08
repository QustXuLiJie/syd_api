package com.mobanker.shanyidai.api.enums;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import org.apache.commons.lang.StringUtils;

/**
 * @author hantongyang
 * @description H5Url枚举
 * @time 2017/1/22 9:39
 */
public enum H5UrlEnum {
    REGISTRATION_AGREEMENT("ra", "h5_url_registration_agreement", "注册服务协议"),
    PRIVACY_POLICY_AGREEMENT("ppa", "h5_url_privacy_policy_agreement", "隐私政策"),
    THREE_PARTY_LOAN_AGREEMENT("tpla", "h5_url_three_party_loan_agreement", "三方借款协议"),
    SINGLE_AGREEMENT_ENTRUST("sgn", "h5_url_single_agreement_entrust", "单期<委托扣款协议书>"),
    USRE_JOB("job", "h5_url_user_job", "工作信息URL"),
    ZHIMA_INFO("zhima", "h5_url_zhima_info", "芝麻信用"),
    ABOUT_SHANYIDAI("about", "h5_url_about_shanyidai", "关于卡宜贷"),
    HELP("help", "h5_url_help", "常见问题"),
    REPAY_INFO("repay", "h5_url_repay_info", "还款信息"),
    CYBER_BANK_URL("cyberBank", "h5_url_cyber_bank", "网银还款web页（从还款页面获取）"),
    BAR_REPAY_URL("barRepay", "h5_url_bar_repay", "柜台还款web页(从还款页面获取)"),
    CUSTOMER_SERVICE_URL("cs", "h5_url_customer_service", "客户服务"),
    MESSAGE_DETAIL_URL("md", "h5_message_detail_url", "消息详情"),
    ;

    private String key;
    private String code;
    private String name;

    H5UrlEnum(String key, String code, String name) {
        this.key = key;
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    /**
     * @description 根据key获取H5Url枚举
     * @author hantongyang
     * @time 2017/1/22 9:46
     * @method getByKey
     * @param key
     * @return com.mobanker.shanyidai.api.enums.H5UrlEnum
     */
    public static H5UrlEnum getByKey(String key){
        if(StringUtils.isBlank(key)){
            return null;
        }
        for (H5UrlEnum urlEnum : H5UrlEnum.values()){
            if(urlEnum.getKey().equals(key)){
                return urlEnum;
            }
        }
        return null;
    }
}
