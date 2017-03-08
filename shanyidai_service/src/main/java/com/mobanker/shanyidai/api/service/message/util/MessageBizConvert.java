package com.mobanker.shanyidai.api.service.message.util;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.common.tool.CommonUtil;
import com.mobanker.shanyidai.api.dto.message.ValidateSmsCaptcha;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Richard Core
 * @description 消息相关参数处理
 * @date 2016/12/26 18:23
 */
public class MessageBizConvert {
    /**
     * @param valicode
     * @return com.mobanker.shanyidai.api.dto.message.ValidateSmsCaptcha
     * @description 验证验证码方法参数
     * @author Richard Core
     * @time 2016/12/26 18:02
     * @method checkValidateSmsCaptchaParam
     */
    public static ValidateSmsCaptcha checkValidateSmsCaptchaParam(ValidateSmsCaptcha valicode) {

        if (valicode == null) {
            throw new SydException(ReturnCode.PARAM_REQUIRED);
        }

        CommonUtil.checkBaseParam(valicode);
        //验证手机号
        CommonUtil.checkPhone(valicode.getPhone());
        //验证验证码 格式
//        CommonUtil.checkVerifyCode(valicode.getCaptcha());
        if (StringUtils.isBlank(valicode.getCaptchaUse())) {
            throw new SydException(ReturnCode.CAPTCHA_USE_NULL);
        }
        return valicode;
    }
}
