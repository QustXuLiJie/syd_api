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
import com.mobanker.shanyidai.api.dto.message.SysMessage;
import com.mobanker.shanyidai.api.service.message.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 消息处理相关API
 *
 * @author chenjianping
 * @data 2016年12月9日
 */
@Controller
@RequestMapping(value = "message")
public class MessageAction {
    private Logger logger = LogManager.getSlf4jLogger(getClass());
    @Resource
    private MessageService messageService;

    /**
     * @param request
     * @param appVersion
     * @return java.lang.Object
     * @description 发送短信验证码  发送类型： sms：短信  voice：语音
     * @author Richard Core
     * @time 2016/12/13 9:48
     * @method sendSms
     */
    @SignLoginValid(SignLoginEnum.SIGN)
    @RequestMapping(value = "/sendSms", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object sendSms(SydRequest request, @RequestHeader String appVersion) throws SydException {
        try {
            Map<String, String> result = messageService.sendSms(request, appVersion);
            return result;
        } catch (SydException e) {
            throw e;
        } catch (Exception e) {
            throw new SydException(ReturnCode.SEND_SMS_FAILED.getCode(), ReturnCode.SEND_SMS_FAILED.getDesc(), e);
        }
    }

    @SignLoginValid(SignLoginEnum.SIGN)
    @RequestMapping(value = "/varifyCode", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object validateSmsCaptcha(SydRequest request, @RequestHeader String appVersion) {
        try {
            messageService.checkSmsCaptcha(request, appVersion);
            return null;
        } catch (SydException e) {
            throw e;
        } catch (Exception e) {
            throw new SydException(ReturnCode.SEND_SMS_FAILED.getCode(), ReturnCode.SEND_SMS_FAILED.getDesc(), e);
        }
    }

    /**
     * @param request
     * @param appVersion
     * @return java.lang.Object
     * @description 获取系统消息列表
     * @author Richard Core
     * @time 2017/2/15 11:14
     * @method sysMsgList
     */
    @SignLoginValid(SignLoginEnum.ALL)
    @RequestMapping(value = "/sysMsgList", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object sysMsgList(SydRequest request, @RequestHeader String appVersion) {
        try {
            List<SysMessage> sysMessage = messageService.getSysMessage(request);
            return sysMessage;
        } catch (SydException e) {
            throw e;
        } catch (Exception e) {
            throw new SydException(ReturnCode.SYS_MESSAGE_ERROR.getCode(), ReturnCode.SEND_SMS_FAILED.getDesc(), e);
        }
    }
    /**
     * @param request
     * @param appVersion
     * @return java.lang.Object
     * @description 更新消息读取状态
     * @author Richard Core
     * @time 2017/2/15 11:14
     * @method sysMsgList
     */
    @SignLoginValid(SignLoginEnum.ALL)
    @RequestMapping(value = "/updateReadStatus", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object updateReadStatus(SydRequest request, @RequestHeader String appVersion) {
        try {
            messageService.updateReadStatus(request);
            return null;
        } catch (SydException e) {
            throw e;
        } catch (Exception e) {
            throw new SydException(ReturnCode.SYS_MESSAGE_READ_ERROR.getCode(), ReturnCode.SEND_SMS_FAILED.getDesc(), e);
        }
    }
    /**
     * @param request
     * @param appVersion
     * @return java.lang.Object
     * @description 获取未读系统消息数量
     * @author Richard Core
     * @time 2017/2/15 11:14
     * @method sysMsgList
     */
    @SignLoginValid(SignLoginEnum.ALL)
    @RequestMapping(value = "/unreadAmount", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object unreadAmount(SydRequest request, @RequestHeader String appVersion) {
        try {
            long result = messageService.getUnreadSysMessageAmount(request);
            return result;
        } catch (SydException e) {
            throw e;
        } catch (Exception e) {
            throw new SydException(ReturnCode.SYS_MESSAGE_UNREADCOUNT_ERROR.getCode(), ReturnCode.SEND_SMS_FAILED.getDesc(), e);
        }
    }
}
