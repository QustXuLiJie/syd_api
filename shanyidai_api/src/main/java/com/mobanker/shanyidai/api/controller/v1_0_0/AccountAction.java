/**
 *
 */
package com.mobanker.shanyidai.api.controller.v1_0_0;

import com.mobanker.shanyidai.api.common.annotation.SignLoginValid;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.enums.SignLoginEnum;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.logger.LogManager;
import com.mobanker.shanyidai.api.common.logger.Logger;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.service.account.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 账户处理相关API
 * @author chenjianping
 * @data 2016年12月9日
 */
@Controller
@RequestMapping(value = "account")
public class AccountAction {
    private Logger logger = LogManager.getSlf4jLogger(AccountAction.class);
    @Resource
    private AccountService accountService;

    /**
     * @param request
     * @param appVersion
     * @return java.lang.Object
     * @description 登录
     * @author Richard Core
     * @time 2016/12/20 11:18
     * @method login
     */
    @SignLoginValid(SignLoginEnum.SIGN)
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object login(SydRequest request, @RequestHeader String appVersion) throws SydException {
        Object data = null;
        try {
            data = accountService.login(request, appVersion);
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.LOGIN_FAILED.getCode(), ReturnCode.LOGIN_FAILED.getDesc(), e);
        }
        return data;
    }

    /**
     * 注册
     *
     * @param request,   downloadChannel => channel,使用之前定义的下载渠道
     * @param appVersion
     * @return
     * @throws SydException
     */
    @SignLoginValid(SignLoginEnum.SIGN)
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object register(SydRequest request, @RequestHeader String appVersion, @RequestHeader String appType) throws SydException {
        Object data = null;
        try {
            data = accountService.register(request, appVersion, appType);
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.REGISTER_FAILED.getCode(), ReturnCode.REGISTER_FAILED.getDesc(), e);
        }
        return data;
    }

    /**
     * 退出
     *
     * @param request
     * @return
     * @throws SydException
     */
    @SignLoginValid(SignLoginEnum.ALL)
    @RequestMapping(value = "/logout", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object logout(SydRequest request, @RequestHeader String appVersion) throws SydException {
        try {
            accountService.logout(request, appVersion);
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.LOGOUT_FAILED.getCode(), ReturnCode.LOGOUT_FAILED.getDesc(), e);
        }
        return null;
    }

    /**
     * 修改登录密码
     *
     * @param request
     * @return
     * @throws SydException
     */
    @SignLoginValid(SignLoginEnum.ALL)
    @RequestMapping(value = "/setPassword", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object setPassword(SydRequest request, @RequestHeader String appVersion) throws SydException {
        try {
            accountService.setPassword(request, appVersion);
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.CHANGE_PASSWD_FAILED.getCode(), ReturnCode.CHANGE_PASSWD_FAILED.getDesc(), e);
        }
        return null;
    }

    /**
     * 忘记登录密码 找回密码 根据手机号和验证码修改登录密码
     *
     * @param request
     * @return
     * @throws SydException
     */
    @SignLoginValid(SignLoginEnum.SIGN)
    @RequestMapping(value = "/forgetPassword", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object forgetPassword(SydRequest request, @RequestHeader String appVersion) throws SydException {
        try {
            accountService.forgetPassword(request, appVersion);
        } catch (SydException e) {
            if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
                throw e;
            } else {
                throw new SydException(ReturnCode.FORGET_PASSWD_FAILED.getCode(), e.message, e);
            }
        } catch (Throwable e) {
            throw new SydException(ReturnCode.FORGET_PASSWD_FAILED.getCode(), ReturnCode.FORGET_PASSWD_FAILED.getDesc(), e);
        }
        return null;
    }

    /**
     * 修改手机号
     *
     * @param request
     * @return
     * @throws SydException
     */
    @SignLoginValid(SignLoginEnum.ALL)
    @RequestMapping(value = "/updatePhone", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object updatePhone(SydRequest request, @RequestHeader String appVersion) throws SydException {
        try {
            accountService.updatePhone(request, appVersion);
        } catch (SydException e) {
            if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
                throw e;
            } else {
                throw new SydException(ReturnCode.UPDATE_PHONE_FAILED.getCode(), e.message, e);
            }
        } catch (Throwable e) {
            throw new SydException(ReturnCode.UPDATE_PHONE_FAILED.getCode(), ReturnCode.UPDATE_PHONE_FAILED.getDesc(), e);
        }
        return null;
    }
}
