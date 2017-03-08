package com.mobanker.shanyidai.api.common.constant;

/**
 * @author Richard Core
 * @description 异常或者返回码定义
 * 1、返回码8位
 * 2、前两位00 为特殊返回码 00000000 为调用成功
 * 3、前两位75 为异常或者错误码
 * 4、固定保留 0-100 系统异常
 * 5、参数处理100-199  包括参数异常 数据库异常 服务器异常 解密错误
 * 6、用户 200-299 用户判断 商户判断 短信判断
 * 7、业务 300-399 借款 还款
 * 8、闪宜贷从400-1000
 * @date 2016/12/12 15:10
 */
public enum ReturnCode {

    CODE_PREFIX("36", "CODE_PREFIX"),//ERROR_HEAD 异常开头


    //固定保留 0-100
    SUCCESS("00000000", "调用成功"),
    SYS_EXCEPTION(CODE_PREFIX.code + "000001", "系统异常"),
    SERVICE_TIMEOUT(CODE_PREFIX.code + "000002", "服务器连接超时"),
    DB_TIMEOUT(CODE_PREFIX.code + "000003", "数据库连接超时"),
    CACHE_TIMEOUT(CODE_PREFIX.code + "000004", "缓存连接超时"),
    MQ_TIMEOUT(CODE_PREFIX.code + "000005", "消息队列连接超时"),
    DB_CONNECT_SPEND(CODE_PREFIX.code + "000006", "数据库连接数用尽"),
    SERVICE_VALID(CODE_PREFIX.code + "000007", "找不到服务"),
    PARAM_VALID(CODE_PREFIX.code + "000008", "参数异常"),
    PARAM_REQUIRED(CODE_PREFIX.code + "000009", "缺少必填参数"),
    PARAM_OUT_RANGE(CODE_PREFIX.code + "000010", "参数值不在取值范围"),
    SIGN_VERIFY_FAILED(CODE_PREFIX.code + "000011", "签名验证失败"),
    REQUEST_TIMEOUT(CODE_PREFIX.code + "000012", "请求超时"),
    UNAUTH_REQUEST(CODE_PREFIX.code + "000013", "无权调用"),
    INTER_VERSION_NONSUPPORT(CODE_PREFIX.code + "000014", "接口版本不支持"),
    SERVICE_FAILED(CODE_PREFIX.code + "000015", "服务调用失败"),
    ENUM_ERROR(CODE_PREFIX.code + "000016", "枚举实例转化失败"),
    REDIS_ERROR(CODE_PREFIX.code + "000017", "缓存数据删除失败"),


    //参数处理100-199  包括参数异常 数据库异常 服务器异常 解密错误
    LOGIN_TIME_OUT(CODE_PREFIX.code + "000101", "会话超时"),
    FIRST_SHAKE_ERROR(CODE_PREFIX.code + "000102", "第一次握手失败"),
    SECOND_SHAKE_ERROR(CODE_PREFIX.code + "000103", "第二次握手失败"),
    UUID_ERROR(CODE_PREFIX.code + "000104", "uuid为空"),
    UUID_TIME_OUT(CODE_PREFIX.code + "000105", "uuid超时"),
    PARAM_CODE_ERROR(CODE_PREFIX.code + "000106", "授权码参数缺失"),
    PARAMS_VERISON(CODE_PREFIX.code + "000107", "应用版本号参数缺失!"),
    PARAMS_PRODUCT(CODE_PREFIX.code + "000108", "产品线参数缺失!"),
    PARAMS_CHANNEL(CODE_PREFIX.code + "000109", "渠道参数缺失!"),
    PARAMS_REQUESTIP(CODE_PREFIX.code + "000110", "请求源IP缺失!"),
    CONFIG_DATA_NULL(CODE_PREFIX.code + "000111", "配置系统参数获取失败!"),
    THREAD_NULL(CODE_PREFIX.code + "000112", "线程池获取失败!"),

    //用户 200-399 用户判断 商户判断 短信判断
    REGISTER_FAILED(CODE_PREFIX.code + "000200", "注册异常"),
    PASSWD_ERROR(CODE_PREFIX.code + "000201", "密码不正确"),
    VERIFYCODE_ERROR(CODE_PREFIX.code + "000202", "验证码验证失败"),
    REGISTED_FAILED(CODE_PREFIX.code + "000203", "该手机号码已注册，请直接登录！"),
    LOGIN_FAILED(CODE_PREFIX.code + "000204", "登录异常"),
    LOGOUT_FAILED(CODE_PREFIX.code + "000205", "退出异常"),
    CHANGE_PASSWD_FAILED(CODE_PREFIX.code + "000206", "修改密码异常"),
    FORGET_PASSWD_FAILED(CODE_PREFIX.code + "000207", "重置密码异常"),
    UPDATE_PHONE_FAILED(CODE_PREFIX.code + "000208", "更新手机号异常"),
    SEND_SMS_FAILED(CODE_PREFIX.code + "000209", "发送短消息异常"),
    GET_UNREADCOUNT_FAILED(CODE_PREFIX.code + "000210", "获取未读消息异常"),
    LIST_MSG_FAILED(CODE_PREFIX.code + "000211", "获取消息异常"),
    SMS_TYPE_INVILID(CODE_PREFIX.code + "000212", "短信类型(type)错误"),
    PHONE_INVILID(CODE_PREFIX.code + "000213", "手机号码错误"),
    TEL_INVILID(CODE_PREFIX.code + "000213", "电话号码错误"),
    SENDTYPE_INVILID(CODE_PREFIX.code + "000214", "短信发送类型(sendType)错误"),
    GET_USERINFOCOMPLETE_FAILED(CODE_PREFIX.code + "000215", "获取身份信息异常"),
    UPLOAD_AVATAR_FAILED(CODE_PREFIX.code + "000217", "上传头像信息异常"),
    ADD_REALNAME_FAILED(CODE_PREFIX.code + "000218", "保存实名认证异常"),
    ADD_REALNAME_CARDID_FAILED(CODE_PREFIX.code + "000219", "身份证格式异常"),
    ADD_REALNAME_AUTH_FAILED(CODE_PREFIX.code + "000220", "实名认证异常"),
    GET_REALNAME_FAILED(CODE_PREFIX.code + "000221", "获取实名认证异常"),
    GET_ALIPAY_SCORE_FAILED(CODE_PREFIX.code + "000222", "获取芝麻认证异常"),
    SKIP_ALIPAY_AUTH_FAILED(CODE_PREFIX.code + "000223", "跳过芝麻认证异常"),
    SAVE_ALIPAY_SCORE_FAILED(CODE_PREFIX.code + "000224", "保存芝麻分异常"),
    AUTH_EDU_FAILED(CODE_PREFIX.code + "000225", "学历认证异常"),
    CARD_BIN_ERROR(CODE_PREFIX.code + "000226", "根据卡号获取发卡行信息异常"),
    ADD_DEBITCARD_FAILED(CODE_PREFIX.code + "000227", "添加银行卡信息异常"),
    GET_BANKLIST_FAILED(CODE_PREFIX.code + "000228", "获取银行卡信息异常"),
    GET_INDEXINFO_FAILED(CODE_PREFIX.code + "000229", "获取首页信息异常"),
    WRONG_PASSWORD(CODE_PREFIX.code + "000230", "密码错误！"),
    WRONG_VERIFYCODE(CODE_PREFIX.code + "000231", "验证码格式错误！"),
    NEWPWD_OLDPWD_SAME(CODE_PREFIX.code + "000232", "新密码和旧密码相同"),
    CALCULATE_EXCEPTION(CODE_PREFIX.code + "000233", "计算额度出错"),
    BORROWTYPE_ERROR(CODE_PREFIX.code + "000234", "借款类型不是单期借款(单期借款为2)"),
    ACCOUNT_ISVALID(CODE_PREFIX.code + "000235", "用户账户不能为空"),
    GET_CANBORROW_FAILED(CODE_PREFIX.code + "000236", "能否借款接口异常"),
    GET_SINGLEBORROWINFO_FAILED(CODE_PREFIX.code + "000237", "获取短期借款进单页面数据异常"),
    SINGLEBORROWCONFIRM_FAILED(CODE_PREFIX.code + "000238", "短期借款发起异常"),
    BORROWHISTORY_LIST_FAILED(CODE_PREFIX.code + "000239", "获取历史记录列表异常"),
    BORROWHISTORY_INFO_FAILED(CODE_PREFIX.code + "000240", "获取历史记录详情异常"),
    GET_BORROWPROGRESS_FAILED(CODE_PREFIX.code + "000241", "进度查询异常"),
    SUBCARDID_NOT_EQUALS(CODE_PREFIX.code + "000242", "身份证号后四位不匹配"),
    GET_REGISTERAGREEMENT_FAILED(CODE_PREFIX.code + "000243", "获取注册服务协议异常"),
    GET_PRIVACYPOLICYAGREEMENT_FAILED(CODE_PREFIX.code + "000244", "获取隐私政策协议异常"),
    GET_SINGLEAGREEMENTTRIPLE_FAILED(CODE_PREFIX.code + "000245", "获取单期三方借款协议异常"),
    GET_SINGLEAGREEMENTENTRUST_FAILED(CODE_PREFIX.code + "000246", "获取单期委托扣款协议书异常"),
    GET_BORROWREPAYLIST_FAILED(CODE_PREFIX.code + "000247", "获取还款记录列表异常"),
    GET_REPAYINFO_FAILED(CODE_PREFIX.code + "000248", "获取还款信息异常"),
    ONEKEYCONFIRM_FAILED(CODE_PREFIX.code + "000249", "一键还款确认接口异常"),
    ONEKEY_FAILED(CODE_PREFIX.code + "000250", "一键还款异常"),
    UNIONPAYSDK_FAILED(CODE_PREFIX.code + "000251", "银联还款异常"),
    GET_CYBERBANKURL_FAILED(CODE_PREFIX.code + "000252", "获取银联还款URL异常"),
    GET_BARREPAYURL_FAILED(CODE_PREFIX.code + "000253", "获取柜台还款URL异常"),
    ACTIVE_FAILED(CODE_PREFIX.code + "000254", "激活异常"),
    FEEDBACK_FAILED(CODE_PREFIX.code + "000255", "意见反馈接口异常"),
    CHECKVERSION_FAILED(CODE_PREFIX.code + "000256", "版本更新异常"),
    GET_HELP_URL_FAILED(CODE_PREFIX.code + "000257", "获取帮助地址异常"),
    GET_ABOUT_URL_FAILED(CODE_PREFIX.code + "000258", "获取关于地址异常"),
    BANKNAME_DEBITCARD_NOT_EQUALS(CODE_PREFIX.code + "000259", "请输入正确的借记卡号"),
    BANKNAME_CREDITCARDNUM_NOT_EQUALS(CODE_PREFIX.code + "000260", "请输入正确的信用卡号"),
    SECOND_CALCSINGLEAMOUNT_FAILED(CODE_PREFIX.code + "000261", "服务器正忙，请过会儿再试!"),
    GET_BORROW_FEE_INTRO_FAILED(CODE_PREFIX.code + "000262", "获取借款费率说明异常"),
    GET_VERIFY_DEBITCARD_FAILED(CODE_PREFIX.code + "000263", "获取银行卡验证信息异常"),
    BANKCARD_NOT_ADDED(CODE_PREFIX.code + "000264", "该银行卡尚未发起认证请求"),
    BANKCARD_VERIFY_FAILED(CODE_PREFIX.code + "000265", "银行卡校验失败，请核实信息"),
    BANKCARD_VERIFY_TIMEOUT(CODE_PREFIX.code + "000266", "银行卡验证超时，请稍候重试"),
    BANKCARD_VERIFY_EXCEPTION(CODE_PREFIX.code + "000267", "银行卡验证异常，请稍候重试"),
    BANKNAME_CARDNUM_NOT_EQUALS(CODE_PREFIX.code + "000268", "卡号和发卡行不匹配"),//000258 重复
    //单位信息、联系人
    ADD_CONTACTJOB_FAILED(CODE_PREFIX.code + "000269", "添加联系人/单位信息异常"),
    ADD_CONTACTJOB_NAME_FAILED(CODE_PREFIX.code + "000270", "单位信息异常"),
    ADD_CONTACTJOB_POSITION_TEL_FAILED(CODE_PREFIX.code + "000271", "单位联系方式信息异常"),
    ADD_CONTACTJOB_POSITION_OFFECT_FAILED(CODE_PREFIX.code + "000272", "职业类型信息异常"),
    ADD_CONTACTJOB_DIRECTCONTACT_NAME_FAILED(CODE_PREFIX.code + "000273", "直系联系人信息异常"),
    ADD_CONTACTJOB_DIRECTCONTACT_PHONE_FAILED(CODE_PREFIX.code + "000274", "直系联系人信息异常"),
    ADD_CONTACTJOB_EMERGENCYCONTACT_NAME_FAILED(CODE_PREFIX.code + "000275", "重要联系人信息异常"),
    ADD_CONTACTJOB_EMERGENCYCONTACT_PHONE_FAILED(CODE_PREFIX.code + "000276", "重要联系人信息异常"),
    GET_CONTACTJOB_FAILED(CODE_PREFIX.code + "000277", "获取联系人/单位信息异常"),
    GET_USERINFO_FAILED(CODE_PREFIX.code + "000278", "获取用户信息异常"),
    USER_LOCKED(CODE_PREFIX.code + "000279", "用户已锁定"),
    PHONE_PASSWORD_INVALID(CODE_PREFIX.code + "000280", "账号或密码有误！"),
    CAPTCHA_USE_NULL(CODE_PREFIX.code + "000281", "验证码用途参数缺失"),
    TEMPLATE_ID_INVALID(CODE_PREFIX.code + "000282", "不支持的模板编号"),
    TEMPLATE_ID_NULL(CODE_PREFIX.code + "000283", "模板编号参数缺失"),
    CAPTCHA_SEND_ERROR(CODE_PREFIX.code + "000283", "验证码发送失败"),
    PHONE_UNMATCH(CODE_PREFIX.code + "000284", "手机号与预留手机号不匹配!"),
    UPDATE_USER_FAIL(CODE_PREFIX.code + "000285", "用户更新失败!"),
    PHONE_INUSE(CODE_PREFIX.code + "000286", "手机号被注册，不能修改"),
    PHONE_NOTCHANGE(CODE_PREFIX.code + "000287", "手机号是同一个，不需要修改"),
    PHONE_UNREGISTED(CODE_PREFIX.code + "000288", "手机号没有注册过"),
    NO_BANK_CARD(CODE_PREFIX.code + "000289", "没有添加过银行卡"),
    BANK_NAME_ERROR(CODE_PREFIX.code + "000290", "根据卡号获取发卡行失败"),
    BANK_CARD_NO_PHONE_ERROR(CODE_PREFIX.code + "000291", "根据手机号或者银行卡号查询银行卡信息"),
    GET_PRODUCT_TEMP_ERROR(CODE_PREFIX.code + "000292", "获取产品模板失败"),
    ERROR_AUTH_TEL(CODE_PREFIX.code + "000293", "联系电话认证失败"),
    ERROR_USER_COMPLENTE(CODE_PREFIX.code + "000294", "获取资料认证进度失败"),
    //首页
    ERROR_GET_INDEX(CODE_PREFIX.code + "000295", "获取首页信息失败"),
    ERROR_GET_H5_URL(CODE_PREFIX.code + "000296", "获取H5链接失败"),
    ERROR_GET_H5_TYPE(CODE_PREFIX.code + "000297", "H5链接类型异常"),
    ERROR_GET_DICTIONARY(CODE_PREFIX.code + "000298", "获取数据字典异常"),
    GET_ALIPAY_CHEACK_AUTH_FAILED(CODE_PREFIX.code + "000299", "检验用户芝麻是否授权异常"),
    ERROR_GET_DEFAULT_BANK(CODE_PREFIX.code + "000303", "获取用户入账银行失败"),
    ERROR_SET_DEFAULT_BANK(CODE_PREFIX.code + "000304", "设置用户入账银行失败"),
    ADD_CONTACTJOB_POSITION_FAILED(CODE_PREFIX.code + "000305", "职位信息异常"),
    GET_ALIPAY_CHEACK_AUTHVALIDATE_FAILED(CODE_PREFIX.code+"000306","芝麻授权失败"),
    GET_ALIPAY_CHEACK_AUTHVOVERTIME_FAILED(CODE_PREFIX.code+"000307","芝麻授权过期"),
    GET_ALIPAY_AZHIMAAUTH_FAILED(CODE_PREFIX.code+"000308","芝麻认证失败"),
    GET_ALIPAY_AZHIMASCORE_FAILED(CODE_PREFIX.code+"000309","获取芝麻分失败"),
    //业务 400-599 借款
    ERROR_BORROW_CHECK_QL_BORROWING(CODE_PREFIX.code + "000400", "获取是否在前隆借款中失败"),
    ERROR_BORROW_GET_INFO(CODE_PREFIX.code + "000401", "获取借款详情失败"),
    ERROR_BORROW_NID_NULL(CODE_PREFIX.code + "000402", "借款单号为空"),
    ERROR_BORROW_HISTORY(CODE_PREFIX.code + "000403", "获取借款历史信息失败"),
    ERROR_BORROW_NID_FAILED(CODE_PREFIX.code + "000404", "借款单号无效"),
    ERROR_BORROW_QLBORROWING_SHOUJIDAI(CODE_PREFIX.code + "000405", "您当前在[手机贷]处于借款中"),
    ERROR_BORROW_QLBORROWING_UZONE(CODE_PREFIX.code + "000406", "您当前在[U族]处于借款中"),
    ERROR_BORROW_QLBORROWING_YHFQ(CODE_PREFIX.code + "000407", "您当前在[应花分期]处于借款中"),
    ERROR_BORROW_SCORE(CODE_PREFIX.code + "000408", "由于综合评分不足，暂无法借款。"),
    ERROR_BORROW_JOB_FAILED(CODE_PREFIX.code + "000409", "您的资料认证已过期"),
    ERROR_BORROW_STATUS(CODE_PREFIX.code + "000410", "借款单状态异常"),
    ERROR_CAPTCHA_NULL(CODE_PREFIX.code + "000411", "验证码为空"),
    ERROR_BORROW_AMOUNT(CODE_PREFIX.code + "000412", "借款金额异常"),
    ERROR_BORROW_FEE(CODE_PREFIX.code + "000413", "借款手续费异常"),
    ERROR_BORROW_DAYS(CODE_PREFIX.code + "000414", "借款天数异常"),
    ERROR_BORROW_COMP_LACK(CODE_PREFIX.code + "000415", "您的资料认证异常，无法借款"),
    ERROR_BORROW_QLBORROWING_QL(CODE_PREFIX.code + "000416", "您当前在[前隆]处于借款中"),
    ERROR_BORROW_QLBORROWING_SYD(CODE_PREFIX.code + "000417", "您当前在[闪宜贷]处于借款中"),
    ERROR_ADD_BORROW_ORDER(CODE_PREFIX.code + "000418", "保存借款单异常"),
    ERROR_BORROW_CALL_BACK(CODE_PREFIX.code + "000419", "借款单回调异常"),
    ERROR_BORROW_CALL_BACK_PARAM(CODE_PREFIX.code + "000420", "借款单回调参数异常"),
    GET_USER_INFO_BY_CARDNO_ERROR(CODE_PREFIX.code + "000421", "根据身份证号获取用户信息失败!"),
    CARD_NO_NULL(CODE_PREFIX.code + "000422", "身份证号为空"),
    CARD_NO_IS_AUTH(CODE_PREFIX.code + "000423", "身份证号已认证过"),
    COMMIT_AUTH_COUNT_ERROR(CODE_PREFIX.code + "000424", "提交验证次数已用完，请明天再试"),

    //闪宜贷从600-699
    PARAMS_ILLEGE(CODE_PREFIX.code + "000601", "参数错误"),
    SIGN_ILLEGE(CODE_PREFIX.code + "000602", "加密验签错误"),
    BANK_CARD_NULL(CODE_PREFIX.code + "000604", "银行卡号为空或格式异常"),
    REAL_NAME_NOT_YET(CODE_PREFIX.code + "000605", "请先进行实名认证"),
    BANK_CARD_ADDED_BEFORE(CODE_PREFIX.code + "000606", "该卡已被添加过"),

    UPLOAD_FILE_NOT_NULL(CODE_PREFIX.code+"000607","上传文件为空"),
    UPLOAD_FILE_SAVE_ERROR(CODE_PREFIX.code+"000608","上传文件保存失败"),
    QUERY_FILE_ERROR(CODE_PREFIX.code+"000609","查询文件失败"),
    GATHER_INFO_ERROR(CODE_PREFIX.code+"000610","信息采集保存失败"),

    VOICE_AUTH_ERROR(CODE_PREFIX.code+"000611","语音识别失败"),
    VOICE_AUTH_FIEL_SAVE_ERROR(CODE_PREFIX.code+"000612","语音文件上传失败"),
    SYS_MESSAGE_ERROR(CODE_PREFIX.code+"000613","查询消息列表失败"),
    SYS_MESSAGE_READ_ERROR(CODE_PREFIX.code+"000614","更新已读标识失败"),
    SYS_MESSAGE_UNREADCOUNT_ERROR(CODE_PREFIX.code+"000615","查询未读消息数失败"),


    //业务 700-799 还款
    PAY_BACK_ERROR(CODE_PREFIX.code + "000700", "查询还款信息失败"),
    ORDER_ID_NULL(CODE_PREFIX.code + "000701", "借款单号为空"),
    REPAY_ERROR(CODE_PREFIX.code + "000702", "还款失败"),
    REPAY_TYPE_NULL(CODE_PREFIX.code + "000703", "还款方式为空"),
    REPAY_PARAM_ERROR(CODE_PREFIX.code + "000704", "还款参数异常"),
    TO_REPAY_ORDER_ERROR(CODE_PREFIX.code + "000705", "没有查到需要还款的订单信息"),
    REPEAT_PAY_ERROR(CODE_PREFIX.code + "000706", "友情提示：还款时多关注扣款明细，避免重复扣款"),
    CAN_REPAY_ORDER_STATUS(CODE_PREFIX.code + "000707", "没有找到需要还款的订单"),
    REPAY_ALREADY(CODE_PREFIX.code + "000708", "该订单已还款"),
    pay_result_error(CODE_PREFIX.code + "000709", "查询支付结果失败"),


    //人脸识别 800-850
    FACE_AUTH_ERROR(CODE_PREFIX.code+"000800","人脸识别失败"),
    FACE_AUTH_FILE_UPLOAD_ERROR(CODE_PREFIX.code+"000801","人脸识别上传云端失败"),
    FACE_AUTH_ENCODING_ERROR(CODE_PREFIX.code+"000802","参数非UTF-8编码"),
    FACE_AUTH_DOWNLOAD_TIMEOUT(CODE_PREFIX.code+"000803","网络地址图片获取超时"),
    FACE_AUTH_DOWNLOAD_ERROR(CODE_PREFIX.code+"000804","网络地址图片获取失败"),
    FACE_AUTH_WRONG_LIVENESS_DATA(CODE_PREFIX.code+"000805","liveness_data 出错"),
    FACE_AUTH_INVALID_ARGUMENT(CODE_PREFIX.code+"000806","请求参数错误"),
    FACE_AUTH_NOT_FOUND(CODE_PREFIX.code+"000807","请求路径错误"),
    FACE_AUTH_INTERNAL_ERROR(CODE_PREFIX.code+"000808","服务器内部错误"),
    FACE_AUTH_HACK_DETECT_FAILD(CODE_PREFIX.code + "000810", "人脸识别活体防伪认证失败"),
    FACE_AUTH_WATERMARK_VERIFY_FAILD(CODE_PREFIX.code + "000811", "人脸识别水印照片比对失败"),
    FACE_AUTH_DATA_ID_FAILD(CODE_PREFIX.code + "000812", "人脸识别订单编号为空"),
    REAL_INFO_INVALID(CODE_PREFIX.code + "000813", "实名认证信息失效"),
    FACE_AUTH_PROCESS_ERROR(CODE_PREFIX.code+"000814","人脸识别进度查询失败"),

    //语音识别851-900
    QUERY_KDXF_ORDER_ID_NULL(CODE_PREFIX.code + "000851","语音订单编号为空"),
    QUERY_KDXF_ORDER_ERROR(CODE_PREFIX.code + "000852","获取语音识别解析结果失败"),
    SUBMIT_KDXF_ORDER_ERROR(CODE_PREFIX.code + "000853","上传语音识别信息失败"),
    AUTH_PROCESS_ERROR(CODE_PREFIX.code + "000854","查询语音识别进度信息失败"),
    AUTH_IDENTITY_PROCESS_ERROR(CODE_PREFIX.code + "000855","查询身份认证进度信息失败"),



    //基础数据
    ERROR_JOBTYPE_GET_INFO(CODE_PREFIX.code + "000901", "获取职位类型列表失败"),
    ERROR_RELATION_GET_INFO(CODE_PREFIX.code + "000902", "获取联系人关系列表失败"),
    ERROR_EDUCATION_GET_INFO(CODE_PREFIX.code + "000903", "获取学历列表失败"),
    ;


    private String code;//异常或者返回码
    private String desc;//描述

    private ReturnCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public java.lang.String getCode() {
        return code;
    }

    public java.lang.String getDesc() {
        return desc;
    }
}
