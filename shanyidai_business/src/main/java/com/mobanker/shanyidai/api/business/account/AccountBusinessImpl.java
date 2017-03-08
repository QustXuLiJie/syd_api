/**
 *
 */
package com.mobanker.shanyidai.api.business.account;

import com.mobanker.shanyidai.api.business.common.CommonBusiness;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.logger.LogManager;
import com.mobanker.shanyidai.api.common.logger.Logger;
import com.mobanker.shanyidai.api.dto.user.UserBaseInfo;
import com.mobanker.shanyidai.dubbo.dto.user.UserLoginDto;
import com.mobanker.shanyidai.dubbo.dto.user.UserRegisterDto;
import com.mobanker.shanyidai.dubbo.service.user.AccountDubboService;
import com.mobanker.shanyidai.esb.common.constants.ErrorConstant;
import com.mobanker.shanyidai.esb.common.exception.EsbException;
import com.mobanker.shanyidai.esb.common.packet.ResponseBuilder;
import com.mobanker.shanyidai.esb.common.packet.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;


/**
 * 账户相关业务实现
 *
 * @author chenjianping
 * @data 2016年12月11日
 */
@Service
public class AccountBusinessImpl implements AccountBusiness {
    private Logger logger = LogManager.getSlf4jLogger(getClass());
    @Resource
    private AccountDubboService accountDubboService;
    @Resource
    private CommonBusiness commonBusiness;

    /**
     * @param userRegisterDto
     * @return void
     * @description 注册综合服务
     * @author Richard Core
     * @time 2016/12/12 19:47
     * @method register
     */
    @Override
    public void register(UserRegisterDto userRegisterDto) throws SydException {
        logger.debug("enter register method, userReg:[{}]", userRegisterDto);
        //调用综合服务
        ResponseEntity responseEntity = null;
        try {
            responseEntity = accountDubboService.userRegister(userRegisterDto);
        } catch (EsbException e) {
            //这里只转化业务异常，其他异常统一处理   只处理业务异常是为了将具体异常信息暴露给用户
            throw new SydException(ReturnCode.REGISTER_FAILED.getCode(), e.message, e);
        }
        //验证返回参数
        if (!ResponseBuilder.isSuccess(responseEntity)) {
            // 账号已注册过的判断
            if (ErrorConstant.ERROR_EXIST_PHONE.getCode().equals(responseEntity.getError())) {
                throw new SydException(ReturnCode.REGISTED_FAILED);
            }
            throw new SydException(ReturnCode.REGISTED_FAILED.getCode(),responseEntity.getMsg());
        }

        logger.debug("exit register method");
    }


    /**
     * @param userLogin
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @description 登录
     * @author Richard Core
     * @time 2016/12/12 19:48
     * @method login
     */
    @Override
    public UserBaseInfo login(UserLoginDto userLogin) throws SydException {
        logger.debug("enter login method, userLogin:[{}]", userLogin);
        ResponseEntity responseEntity = null;
        try {
            responseEntity = accountDubboService.userLogin(userLogin);
        } catch (EsbException e) {
            //这里只转化业务异常，其他异常统一处理   只处理业务异常是为了将具体异常信息暴露给用户
            throw new SydException(e.errCode, e.message);
        }
        //验证返回参数
        if (!ResponseBuilder.isSuccess(responseEntity)) {
            // 账号密码输入错误的时候 需要记录失败的次数 替换异常为
            if (ErrorConstant.PHONE_PASSWORD_INVALID.getCode().equals(responseEntity.getError())) {
                throw new SydException(ReturnCode.PHONE_PASSWORD_INVALID);
            }
            throw new SydException(ReturnCode.LOGIN_FAILED.getCode(),responseEntity.getMsg());
        }
        Object data = responseEntity.getData();
        if (data == null) {
            logger.error("登录服务返回数据为空");
            throw new SydException(ReturnCode.LOGIN_FAILED);
        }
        Map<String, Object> result = (Map<String, Object>) data;
        UserBaseInfo user = new UserBaseInfo();
        user.setUserId(Long.valueOf(String.valueOf(result.get("userId"))));
        user.setUserName(String.valueOf(result.get("phone")));
        user.setPassword(String.valueOf(result.get("password")));
        user.setRealname(String.valueOf(result.get("realname")));

//        //获取头像
//        re = sydUserService.getAvatar(Long.valueOf(loginDto.getUserId()));
//        Object avatar = checkRetData(re);
//        result.put("avatar", avatar == null ? "" : avatar);
        logger.debug("exit login method, result:[{}]", user);

        return user;
    }


}
