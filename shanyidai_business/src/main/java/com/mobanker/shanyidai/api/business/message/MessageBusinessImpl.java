/**
 *
 */
package com.mobanker.shanyidai.api.business.message;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.logger.LogManager;
import com.mobanker.shanyidai.api.common.logger.Logger;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.dto.message.SysMessage;
import com.mobanker.shanyidai.api.dto.message.SysMessageParam;
import com.mobanker.shanyidai.dubbo.dto.message.SmsMesageParamDto;
import com.mobanker.shanyidai.dubbo.dto.message.SysMessageDto;
import com.mobanker.shanyidai.dubbo.dto.message.SysMessageParamsDto;
import com.mobanker.shanyidai.dubbo.dto.message.ValidateSmsCaptchaParamDto;
import com.mobanker.shanyidai.dubbo.service.message.MessageDubboService;
import com.mobanker.shanyidai.esb.common.exception.EsbException;
import com.mobanker.shanyidai.esb.common.packet.ResponseBuilder;
import com.mobanker.shanyidai.esb.common.packet.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * 消息相关业务处理
 *
 * @author chenjianping
 * @data 2016年12月11日
 */
@Service
public class MessageBusinessImpl implements MessageBusiness {
    private Logger logger = LogManager.getSlf4jLogger(getClass());
    @Resource
    private MessageDubboService messageDubboService;

    /**
     * @param smsMesageParamDto
     * @return java.lang.String
     * @description 发送验证码综合服务
     * @author Richard Core
     * @time 2016/12/26 22:25
     * @method sendSmsCaptcha
     */
    @Override
    public String sendSmsCaptcha(SmsMesageParamDto smsMesageParamDto) throws SydException {
        ResponseEntity responseEntity = null;
        try {
            responseEntity = messageDubboService.sendSmsCaptcha(smsMesageParamDto);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        } catch (Exception e1) {
            logger.error("发送验证码失败", e1);
            throw new SydException(ReturnCode.CAPTCHA_SEND_ERROR.getCode(), e1.getMessage());
        }
        if (!ResponseBuilder.isSuccess(responseEntity)) {
            logger.error("发送验证码服务调用失败，responseEntity：", responseEntity);
            throw new SydException(ReturnCode.CAPTCHA_SEND_ERROR.getCode(), responseEntity.getMsg());
        }
        return (String) responseEntity.getData();
    }

    /**
     * @param validateSmsCaptchaDto
     * @return void
     * @description 验证验证码服务封装
     * @author Richard Core
     * @time 2016/12/26 18:29
     * @method validateSmsCaptcha
     */
    @Override
    public void validateSmsCaptcha(ValidateSmsCaptchaParamDto validateSmsCaptchaDto) {
        ResponseEntity responseEntity = null;
        try {
            responseEntity = messageDubboService.validateSmsCaptcha(validateSmsCaptchaDto);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        } catch (Exception e1) {
            logger.error("验证码验证失败", e1);
            throw new SydException(ReturnCode.VERIFYCODE_ERROR, e1);
        }
        if (!ResponseBuilder.isSuccess(responseEntity)) {
            logger.error("验证码服务调用失败，responseEntity：", responseEntity);
            throw new SydException(ReturnCode.VERIFYCODE_ERROR.getCode(), responseEntity.getMsg());
        }
    }

    /**
     * @param paramDto
     * @return java.util.List<com.mobanker.shanyidai.dubbo.dto.message.SysMessageDto>
     * @description 查询系统消息
     * @author Richard Core
     * @time 2017/2/14 16:09
     * @method getSysMessage
     */
    @Override
    public List<SysMessage> getSysMessage(SysMessageParam paramDto) {
        //参数处理
        if (paramDto == null || paramDto.getUserId() == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        List<SysMessage> resultList = new ArrayList<SysMessage>();
        ResponseEntity responseEntity = null;
        try {
            SysMessageParamsDto paramsDto = BeanHelper.cloneBean(paramDto, SysMessageParamsDto.class);
            //查询服务
            responseEntity = messageDubboService.getSysMessage(paramsDto);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        } catch (Exception e) {
            throw new SydException(ReturnCode.SYS_MESSAGE_ERROR.getCode(), e.getMessage());
        }
        //返回值处理
        if (!ResponseBuilder.isSuccess(responseEntity)) {
            logger.warn("获取消息列表失败" + responseEntity.getError() + "：" + responseEntity.getMsg());
            return resultList;
        }
        List<SysMessageDto> dataList = (List<SysMessageDto>) responseEntity.getData();
        for (SysMessageDto sysMessageDto : dataList) {
            if (sysMessageDto == null) {
                continue;
            }
            SysMessage sysMessage = BeanHelper.cloneBean(sysMessageDto, SysMessage.class);
            resultList.add(sysMessage);
        }
        return resultList;
    }

    /**
     * @param ids
     * @return void
     * @description 更新消息读取状态
     * @author Richard Core
     * @time 2017/2/14 17:18
     * @method updateReadStatus
     */
    @Override
    public void updateReadStatus(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new SydException(ReturnCode.PARAM_REQUIRED);
        }
        ResponseEntity responseEntity = null;
        try {
            //查询服务
            responseEntity = messageDubboService.updateReadStatus(ids);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        } catch (Exception e) {
            throw new SydException(ReturnCode.SYS_MESSAGE_READ_ERROR.getCode(), e.getMessage());
        }
        //返回值处理
        if (!ResponseBuilder.isSuccess(responseEntity)) {
            throw new SydException(ReturnCode.SYS_MESSAGE_READ_ERROR.getCode(), responseEntity.getMsg());
        }
    }

    /**
     * @param paramDto
     * @return int
     * @description 查询未读系统消息数
     * @author Richard Core
     * @time 2017/2/14 17:36
     * @method getUnreadSysMessageAmount
     */
    @Override
    public long getUnreadSysMessageAmount(SysMessageParam paramDto) {
        //参数处理
        if (paramDto == null || paramDto.getUserId() == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        List<SysMessage> resultList = new ArrayList<SysMessage>();
        ResponseEntity responseEntity = null;
        try {
            SysMessageParamsDto paramsDto = BeanHelper.cloneBean(paramDto, SysMessageParamsDto.class);
            //查询服务
            responseEntity = messageDubboService.getUnreadSysMessageAmount(paramsDto);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        } catch (Exception e) {
            throw new SydException(ReturnCode.SYS_MESSAGE_ERROR.getCode(), e.getMessage());
        }
        //返回值处理
        if (!ResponseBuilder.isSuccess(responseEntity)) {
            logger.warn("获取消息列表失败" + responseEntity.getError() + "：" + responseEntity.getMsg());
            throw new SydException(ReturnCode.SYS_MESSAGE_UNREADCOUNT_ERROR.getCode(), "获取消息失败，请检查您的网络设置，并稍后重试");
        }

        Object data = responseEntity.getData();
        if (data == null) {
            return 0l;
        }
        return (long) data;
    }
}
