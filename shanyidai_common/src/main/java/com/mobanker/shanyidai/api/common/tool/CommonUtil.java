package com.mobanker.shanyidai.api.common.tool;


import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.logger.LogManager;
import com.mobanker.shanyidai.api.common.logger.Logger;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 消息记录
 *
 * @author: R.Core
 * @date 创建时间：2016-12-24
 */
public class CommonUtil {
    public static final Logger logger = LogManager.getSlf4jLogger(CommonUtil.class);

    /**
     * @param request
     * @return void
     * @description 验证是否登陆
     * @author hantongyang
     * @time 2017/1/16 17:04
     * @method checkLoginStatus
     */
    public static void checkLoginStatus(SydRequest request) {
        if (request == null) {
            throw new SydException(ReturnCode.PARAMS_ILLEGE);
        }
        if (request.getUserId() == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        if (StringUtils.isBlank(request.getContent())) {
            throw new SydException(ReturnCode.PARAMS_ILLEGE);
        }
    }

    /**
     * @param entity
     * @return void
     * @description 基本参数验证 包括 产品 渠道 请求ip 版本号
     * @author Richard Core
     * @time 2016/12/24 11:10
     * @method checkBaseParam
     */
    public static void checkBaseParam(SydRequest entity) {
        if (entity == null) {
            throw new SydException(ReturnCode.PARAMS_ILLEGE);
        }
        if (StringUtils.isBlank(entity.getChannel())) {
            throw new SydException(ReturnCode.PARAMS_CHANNEL);
        }
        if (StringUtils.isBlank(entity.getProduct())) {
            throw new SydException(ReturnCode.PARAMS_PRODUCT);
        }
        if (StringUtils.isBlank(entity.getVersion())) {
            throw new SydException(ReturnCode.PARAMS_VERISON);
        }
        if (StringUtils.isBlank(entity.getIp())) {
            throw new SydException(ReturnCode.PARAMS_REQUESTIP);
        }
    }

    /**
     * 验证电话号码 匹配格式：11位手机号码
     */
    public static void checkPhone(String str) throws SydException {
        if (StringUtils.isEmpty(str)) {
            logger.error("手机号为空");
            throw new SydException(ReturnCode.PHONE_INVILID, null);
        }
        String regex = "^(1(20|45|47)\\d{8})|(1(3|5|8)\\d{9})|(17\\d{9})$";
        if (!match(regex, str)) {
            logger.error("手机号格式错误", str);
            throw new SydException(ReturnCode.PHONE_INVILID, null);
        }
    }

    /**
     * 验证电话号码 匹配格式：8位数字，其余逻辑未定
     */
    public static void checkTel(String str) throws SydException {
        if (StringUtils.isEmpty(str)) {
            throw new SydException(ReturnCode.TEL_INVILID, null);
        }
        String regex = "^\\d{8}$";
        if (!match(regex, str)) {
            throw new SydException(ReturnCode.TEL_INVILID, null);
        }
    }

    /**
     * 校验密码
     */
    public static void checkPassword(String pwd) throws SydException {
        if (pwd == null || pwd.length() != 32) {
            logger.error("密码为空或者格式错误");
            throw new SydException(ReturnCode.WRONG_PASSWORD, null);
        }
    }


    /**
     * 身份证验证
     *
     * @param str 待验证的字符串
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isCardId(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        String regex = "([0-9]{17}([0-9]|X))|([0-9]{15})";
        return match(regex, str);
    }
    /**
     * @param str 待验证的字符串
     * @param min 最小长度
     * @param max 最大长度
     * @return boolean
     * @method limitString
     * @description 检验字符串长度是不是符合
     * @time 9:38 2017/2/17
     */
    public static boolean limitString(String str, int min, int max) {
        if (min<=str.length()&&str.length()<=max){
            return true;
        }
        return false;
    }

    /**
     * @param strNum
     * @return boolean
     * @description 判断字符串是否都是数字
     * @author hantongyang
     * @time 2017/1/5 17:17
     * @method isDigit
     */
    public static boolean isDigit(String strNum) {
        if (StringUtils.isBlank(strNum)) {
            return false;
        }
        String regex = "[0-9]{1,}";
        return match(regex, strNum);
    }

    /**
     * @param regex 正则表达式字符串
     * @param str   要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }


    /**
     * 版本号转换成数字
     *
     * @return
     * @author: liuyafei
     * @date 创建时间：2016年10月10日
     * @version 1.0
     * @parameter
     */
    public static int transVersionToNum(String version) {
        int num = 0;
        String[] msg = version.split("\\.");
        int len = msg.length;
        if (len >= 3) {
            num += Integer.parseInt(msg[2]);
        }
        if (len >= 2) {
            num += Integer.parseInt(msg[1]) * 10;
        }
        if (len >= 1) {
            num += Integer.parseInt(msg[0]) * 100;
        }
        return num;
    }

}
