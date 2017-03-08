package com.mobanker.shanyidai.api.service.message;

import com.mobanker.shanyidai.api.common.enums.SmsTypeEnum;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.dto.message.SysMessage;
import com.mobanker.shanyidai.api.dto.message.ValidateSmsCaptcha;

import java.util.List;
import java.util.Map;

public interface MessageService {
    /**
     * 发送验证码
     *
     * @param request
     * @param appVersion
     * @return
     * @throws SydException
     */
    Map<String, String> sendSms(SydRequest request, String appVersion) throws SydException;

    /**
     * @param valicode
     * @return void
     * @description 验证验证码
     * @author Richard Core
     * @time 2016/12/26 19:50
     * @method checkSmsCaptcha
     */
    public void checkSmsCaptcha(ValidateSmsCaptcha valicode);

    /**
     * @param request
     * @param appVersion
     * @return void
     * @description 验证验证码
     * @author Richard Core
     * @time 2016/12/26 19:50
     * @method checkSmsCaptcha
     */
    public void checkSmsCaptcha(SydRequest request, String appVersion);

    /**
     * @param request
     * @param appVersion
     * @param phone
     * @param validCode
     * @param smsType
     * @return void
     * @description 验证验证码
     * @author Richard Core
     * @time 2016/12/27 19:56
     * @method checkValidCode
     */
    public void checkSmsCaptcha(SydRequest request, String appVersion, String phone, String validCode, SmsTypeEnum smsType);

    /**
     * @param request
     * @return java.util.List<com.mobanker.shanyidai.api.dto.message.SysMessage>
     * @description 查询系统消息
     * @author Richard Core
     * @time 2017/2/15 10:58
     * @method getSysMessage
     */
    public List<SysMessage> getSysMessage(SydRequest request);

    /**
     * @param request
     * @return void
     * @description 更新消息读取状态
     * @author Richard Core
     * @time 2017/2/15 10:59
     * @method updateReadStatus
     */
    public void updateReadStatus(SydRequest request);

    /**
     * @param request
     * @return int
     * @description 查询未读系统消息数
     * @author Richard Core
     * @time 2017/2/15 10:59
     * @method getUnreadSysMessageAmount
     */
    public long getUnreadSysMessageAmount(SydRequest request);
}
