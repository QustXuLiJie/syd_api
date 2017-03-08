package com.mobanker.shanyidai.api.service.account.util;

import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.common.tool.CommonUtil;
import com.mobanker.shanyidai.api.dto.account.UserRegRequest;
import com.mobanker.shanyidai.dubbo.dto.user.UserLoginDto;
import com.mobanker.shanyidai.dubbo.dto.user.UserRegisterDto;

/**
 * @author Richard Core
 * @description 账户相关参数封装
 * @date 2016/12/23 20:35
 */
public class AccountBizConvert {

    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.account.UserRegRequest
     * @description 获取和验证参数
     * @author Richard Core
     * @time 2016/12/19 9:23
     * @method mapUserReg
     */
    public static UserRegRequest mapUserRegRequest(SydRequest request) throws SydException {
        //获取参数
        UserRegRequest userRegRequest = BeanHelper.parseJson(request.getContent(), UserRegRequest.class);

        //参数校验
        CommonUtil.checkPhone(userRegRequest.getPhone());
        CommonUtil.checkPassword(userRegRequest.getPassword());
        return userRegRequest;
    }
    /**
     * @param request
     * @param userRegRequest
     * @param appVersion
     * @return com.mobanker.shanyidai.api.dto.account.UserReg
     * @description 封装注册综合服务参数
     * @author Richard Core
     * @time 2016/12/19 10:22
     * @method mapUserReg
     */
    public static UserRegisterDto mapRegistParam(SydRequest request, UserRegRequest userRegRequest, String appVersion) throws SydException {
        //注册用户
        UserRegisterDto userRegisterBean = new UserRegisterDto();
        userRegisterBean.setPhone(userRegRequest.getPhone());
        userRegisterBean.setPassword(userRegRequest.getPassword());
        userRegisterBean.setValidCode(userRegRequest.getVerifyCode());
        userRegisterBean.setIp(request.getIp());
        userRegisterBean.setChannel(userRegRequest.getChannel());
        userRegisterBean.setProduct(request.getProduct());
        userRegisterBean.setVersion(appVersion);
        return userRegisterBean;
    }

    /**
     * @param request
     * @param phone
     * @param password
     * @param appVersion
     * @return com.mobanker.shanyidai.dubbo.dto.user.UserLoginDto
     * @description 封装登录综合服务的参数
     * @author Richard Core
     * @time 2016/12/24 15:40
     * @method mapLoginEsbParam
     */
    public static UserLoginDto mapLoginEsbParam(SydRequest request, String phone, String password, String appVersion) {
        UserLoginDto userLogin = new UserLoginDto();
        userLogin.setPhone(phone);
        userLogin.setPassword(password);
        userLogin.setIp(request.getIp());
        userLogin.setChannel(request.getChannel());
        userLogin.setProduct(request.getProduct());
        userLogin.setVersion(appVersion);
        return userLogin;
    }
}
