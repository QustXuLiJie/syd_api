package com.mobanker.shanyidai.api.common.constant;

import com.mobanker.shanyidai.api.common.exception.SydException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author hantongyang
 * @description 配置中心参数KEY，以及默认数据
 * @time 2016/12/30 15:37
 */
public enum ZkConfigConstant {

    SYD_APP_VERSION("shanyidai_app_version", "1.0"), //app版本号
    SYSTEM_TIMEOUT("system.timeout", "7200"), //系统过期时间
    SYD_LOGIN_FAIL_COUNT("syd_login_fail_count", "5"), //闪宜贷登陆错误次数限制
    SYD_LOGIN_LOCK_TIME("syd_login_lock_time", "7200"), //闪宜贷登陆锁定时长（秒）
    SYD_USER_INFO_TIMEOUT("user.info.timeout", "3600"), //闪宜贷用户信息缓存时长（秒）
    VERIFY_CODE_LENGTH("verify_code_length", "4"), //验证码长度
    SYD_ADD_BORROW_ORDER_PARAM("add_borrow_order_param", "180"), //借款单缓存时长（秒）,默认3分钟
    SYD_BORROW_MIN_TIME_LIMIT("borrow_min_time_limit", "7"), //最短借款期限
    SYD_BORROW_MAX_TIME_LIMIT("borrow_max_time_limit", "15"), //最长借款期限
    SYD_REPAY_AVOID_REPEAT("syd_repay_avoid_repeat", "180"), //确认还款防重复提交缓存时长（秒）,默认3分钟
    SYD_AUTH_FACE_TIMES_PER_DAY("syd_auth_face_times_per_day", "10"), //每天最多进行人脸识别次数
    SYD_AUTH_VOICE_TIMES_PER_DAY("syd_auth_voice_times_per_day", "10"), //每天最多进行语音识别次数

    SYD_API_SERVICE_LOG_GATE("api_service_log_gate", "1"), //API工程业务成功日志是否记录闸口
    SYD_API_BUSINESS_FLOW("api_business_flow", "1"), //API工程业务流水是否记录闸口
    SYD_TEMP_FILE_PATH("repos.dest", "E:/var/statics"), //闪宜贷上传文件的缓存路径
    SYD_CUSTOMER_SERVICE_PHONE("customer_services_phone", ""), //客服电话
    SYD_ACCREDIT_PHONE_BOOK("accredit_phone_book", "1"), //是否强制授权通讯录
    SYD_ONLINE_CUSTOMER_SERVICE_URL("online_customer_service_url", ""), //在线客服URL
    ;

    private String zkValue;
    private String defaultValue;

    ZkConfigConstant(String zkValue, String defaultValue) {
        this.zkValue = zkValue;
        this.defaultValue = defaultValue;
    }

    public String getZkValue() {
        return zkValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * @description 根据枚举的Key获取对应的枚举
     * @author hantongyang
     * @time 2017/1/17 10:07
     * @method getByKey
     * @param key
     * @return com.mobanker.shanyidai.api.common.constant.ZkConfigConstant
     */
    public static ZkConfigConstant getByKey(String key){
        if(StringUtils.isBlank(key)){
            throw new SydException(ReturnCode.ENUM_ERROR);
        }
        for(ZkConfigConstant config : ZkConfigConstant.values()){
            if(config.getZkValue().equals(key)){
                return config;
            }
        }
        throw new SydException(ReturnCode.ENUM_ERROR);
    }
}
