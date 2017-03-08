package com.mobanker.shanyidai.api.controller.v1_0_0;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.service.user.UserAuthService;
import com.mobanker.shanyidai.esb.common.exception.EsbException;
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
public class FaceAction {

    @Resource
    private UserAuthService userAuthService;

    /**
     * @param request
     * @param httpServletRequest
     * @return java.lang.Object
     * @description 人脸识别
     * @author Richard Core
     * @time 2017/2/20 16:17
     * @method faceAuth
     */
    @RequestMapping(value = "faceAuth", method = RequestMethod.POST)
    @ResponseBody
    public Object faceAuth(SydRequest request, HttpServletRequest httpServletRequest) {
        Object data = null;
        try {
            data = userAuthService.faceAuth(request, httpServletRequest);
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.FACE_AUTH_ERROR.getCode(), e.getMessage(), e);
        }
        return data;
    }

    /**
     * @param request
     * @return java.lang.Object
     * @description 获取人脸识别进度结果
     * @author Richard Core
     * @time 2017/2/10 14:00
     * @method findVoiceResult
     */
    @RequestMapping(value = "getFaceProcessing", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getFaceProcessing(SydRequest request) {
        Object data = null;
        try {
            data = userAuthService.getAuthFaceRecord(request);
        } catch (SydException e) {
            throw e;
        } catch (EsbException esb) {
            throw esb;
        } catch (Throwable te) {
            throw new SydException(ReturnCode.FACE_AUTH_PROCESS_ERROR.getCode(), te.getMessage(), te);
        }
        return data;
    }
}
