/**
 *
 */
package com.mobanker.shanyidai.api.business.account;

import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.dto.user.UserBaseInfo;
import com.mobanker.shanyidai.dubbo.dto.user.UserLoginDto;
import com.mobanker.shanyidai.dubbo.dto.user.UserRegisterDto;

/**
 * 账户相关业务定义
 *
 * @author chenjianping
 * @data 2016年12月11日
 */
public interface AccountBusiness {
    /**
     * 注册
     *
     * @param userReg
     * @throws SydException
     */
    void register(UserRegisterDto userReg) throws SydException;

    /**
     * 登录
     *
     * @param userLogin
     * @return
     * @throws SydException
     */
    UserBaseInfo login(UserLoginDto userLogin);

}
