package com.mobanker.shanyidai.api.controller.v1_0_0;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.service.user.UserAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author hantongyang
 * @description
 * @time 2017/2/27 14:27
 */
@Controller
@RequestMapping(value = "user/auth")
public class VoiceAction {

    @Resource
    private UserAuthService userAuthService;

    /**
     * @param request
     * @return java.lang.Object
     * @description 语音验证
     * @author liuhanqing
     * @time 2016/2/9 20:43
     * @method getRealName
     */
    @RequestMapping(value = "voiceAuth", method = RequestMethod.POST)
    @ResponseBody
    public Object voiceAuth(SydRequest request, HttpServletRequest httpServletRequest) {

        Object data = null;
        try {
            data = userAuthService.voiceAuth(request, httpServletRequest);
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.VOICE_AUTH_ERROR.getCode(), ReturnCode.VOICE_AUTH_ERROR.getDesc(), e);
        }
        return data;
    }

    /**
     * @param request
     * @return java.lang.Object
     * @description 查询语音解析结果
     * @author Richard Core
     * @time 2017/2/10 14:00
     * @method findVoiceResult
     */
    @RequestMapping(value = "findVoiceResult", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object findVoiceResult(SydRequest request) {
        Object data = null;
        try {
            data = userAuthService.findVoiceResult(request);
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.GET_REALNAME_FAILED.getCode(), e.getMessage());
        }
        return data;
    }

    /**
     * @param request
     * @return java.lang.Object
     * @description 获取语音识别进度结果
     * @author Richard Core
     * @time 2017/2/10 14:00
     * @method findVoiceResult
     */
    @RequestMapping(value = "getVoiceProcessing", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getVoiceProcessing(SydRequest request) {
        Object data = null;
        try {
            data = userAuthService.getVoiceProcessing(request);
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.AUTH_PROCESS_ERROR.getCode(), e.getMessage());
        }
        return data;
    }
}
