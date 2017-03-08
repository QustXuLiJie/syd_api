/**
 *
 */
package com.mobanker.shanyidai.api.controller.v1_0_0;

import com.mobanker.shanyidai.api.common.annotation.SignLoginValid;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.enums.SignLoginEnum;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.service.user.UserAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 认证处理相关API
 *
 * @author chenjianping
 * @data 2016年12月9日
 */
@Controller
@RequestMapping(value = "user/auth")
public class AuthAction {

    @Resource
    private UserAuthService userAuthService;

    /**
     * @param request
     * @return java.lang.Object
     * @description 用户资料认证进度
     * @author hantongyang
     * @time 2017/1/10 11:45
     * @method getCompleteness
     */
    @SignLoginValid(SignLoginEnum.ALL)
    @RequestMapping(value = "getCompleteness", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getCompleteness(SydRequest request) {
        Object data = null;
        try {
            data = userAuthService.getCompleteness(request);
        } catch (SydException e) {
            if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
                throw e;
            } else {
                throw new SydException(ReturnCode.ERROR_USER_COMPLENTE.getCode(), e.message, e);
            }
        } catch (Throwable e) {
            throw new SydException(ReturnCode.ERROR_USER_COMPLENTE, e);
        }
        return data;
    }

    /**
     * @param request
     * @return java.lang.Object
     * @description 保存实名认证信息
     * @author hantongyang
     * @time 2016/12/21 10:43
     * @method addRealName
     */
    @RequestMapping(value = "addRealName", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object addRealName(SydRequest request) {
        try {
            userAuthService.addRealName(request);
        } catch (SydException e) {
            if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
                throw e;
            } else {
                throw new SydException(ReturnCode.ADD_REALNAME_FAILED.getCode(), e.message, e);
            }
        } catch (Throwable e) {
            throw new SydException(ReturnCode.ADD_REALNAME_FAILED, e);
        }
        return null;
    }

    /**
     * @param request
     * @return java.lang.Object
     * @description 查询实名认证信息
     * @author hantongyang
     * @time 2016/12/21 10:43
     * @method getRealName
     */
    @RequestMapping(value = "getRealName", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getRealName(SydRequest request) {
        Object data = null;
        try {
            data = userAuthService.getRealName(request.getUserId());
        } catch (SydException e) {
            if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
                throw e;
            } else {
                throw new SydException(ReturnCode.GET_REALNAME_FAILED.getCode(), e.message, e);
            }
        } catch (Throwable e) {
            throw new SydException(ReturnCode.GET_REALNAME_FAILED, e);
        }
        return data;
    }
    /**
     * @param request
     * @return java.lang.Object
     * @description 查询身份认证信息 包括人脸识别和语音识别进度
     * @author hantongyang
     * @time 2016/12/21 10:43
     * @method getRealName
     */
    @RequestMapping(value = "getIdentityAuthProcess", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getIdentityAuthProcess(SydRequest request) {
        Object data = null;
        try {
            data = userAuthService.getIdentityAuthProcess(request.getUserId());
        } catch (SydException e) {
                throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.AUTH_IDENTITY_PROCESS_ERROR, e);
        }
        return data;
    }

}
