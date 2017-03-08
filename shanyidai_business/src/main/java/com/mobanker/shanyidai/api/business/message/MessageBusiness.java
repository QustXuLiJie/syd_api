/**
 *
 */
package com.mobanker.shanyidai.api.business.message;

import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.dto.message.SysMessage;
import com.mobanker.shanyidai.api.dto.message.SysMessageParam;
import com.mobanker.shanyidai.dubbo.dto.message.SmsMesageParamDto;
import com.mobanker.shanyidai.dubbo.dto.message.ValidateSmsCaptchaParamDto;

import java.util.List;

/**
 * 消息服务定义
 *
 * @author chenjianping
 * @data 2016年12月11日
 */
public interface MessageBusiness {

    /**
     * @param smsMesageParamDto
     * @return java.lang.String
     * @description 发送验证码综合服务
     * @author Richard Core
     * @time 2016/12/26 22:25
     * @method sendSmsCaptcha
     */
    String sendSmsCaptcha(SmsMesageParamDto smsMesageParamDto) throws SydException;

    /**
     * @param validateSmsCaptchaDto
     * @return void
     * @description 验证验证码服务封装
     * @author Richard Core
     * @time 2016/12/26 18:29
     * @method validateSmsCaptcha
     */
    void validateSmsCaptcha(ValidateSmsCaptchaParamDto validateSmsCaptchaDto);


    /**
     * @param paramDto
     * @return java.util.List<com.mobanker.shanyidai.dubbo.dto.message.SysMessageDto>
     * @description 查询系统消息
     * @author Richard Core
     * @time 2017/2/14 16:09
     * @method getSysMessage
     */
    public List<SysMessage> getSysMessage(SysMessageParam paramDto);
    /**
     * @param ids
     * @return void
     * @description 更新消息读取状态
     * @author Richard Core
     * @time 2017/2/14 17:18
     * @method updateReadStatus
     */
    public void updateReadStatus(List<String> ids);
    /**
     * @param paramDto
     * @return long
     * @description 查询未读系统消息数
     * @author Richard Core
     * @time 2017/2/14 17:36
     * @method getUnreadSysMessageAmount
     */
    public long getUnreadSysMessageAmount(SysMessageParam paramDto);
}
