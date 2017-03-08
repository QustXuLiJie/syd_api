package com.mobanker.shanyidai.api.service.account;

import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.dto.user.UserBaseInfo;

/**
 * @author Richard Core
 * @description
 * @date 2016/12/12 17:41
 */
public interface AccountService {
    /**
     * @param request
     * @param version
     * @param appType
     * @return java.lang.Object
     * @description 注册
     * @author Richard Core
     * @time 2016/12/10 16:47
     * @method register
     */
    UserBaseInfo register(SydRequest request, String version, String appType) throws SydException;

    /**
     * 登录 request中包含手机号和登录密码时 使用
     *
     * @param request
     * @param appVersion
     * @return
     * @throws SydException
     */
    UserBaseInfo login(SydRequest request, String appVersion) throws SydException;

    /**
     * @param request
     * @param phone
     * @param password
     * @param appVersion
     * @return UserBaseInfo
     * @description request中没有手机号密码时使用
     * @author Richard Core
     * @time 2016/12/19 11:10
     * @method login
     */
    UserBaseInfo login(SydRequest request, String phone, String password, String appVersion) throws SydException;

    /**
     * @param request
     * @param appVersion
     * @return void
     * @description request中有code时 退出登录
     * @author Richard Core
     * @time 2016/12/20 16:06
     * @method logout
     */
    void logout(SydRequest request, String appVersion) throws SydException;

    /**
     * @param request
     * @param code
     * @param appVersion
     * @return void
     * @description request中没有code时 退出登录
     * @author Richard Core
     * @time 2016/12/20 16:05
     * @method logout
     */
    void logout(SydRequest request, String code, String appVersion) throws SydException;

    /**
     * @param request
     * @param appVersion
     * @return void
     * @description 修改密码
     * @author Richard Core
     * @time 2016/12/20 18:32
     * @method setPassword
     */
    void setPassword(SydRequest request, String appVersion) throws SydException;

    /**
     * @param request
     * @param appVersion
     * @return void
     * @description 找回密码
     * @author Richard Core
     * @time 2016/12/20 18:33
     * @method forgetPassword
     */
    void forgetPassword(SydRequest request, String appVersion) throws SydException;

    /**
     * @param request
     * @param appVersion
     * @return void
     * @description 修改手机号
     * @author Richard Core
     * @time 2016/12/20 18:35
     * @method updatePhone
     */
    void updatePhone(SydRequest request, String appVersion) throws SydException;


}
