package com.mobanker.shanyidai.api.controller.v1_0_0;

import com.mobanker.shanyidai.api.common.annotation.SignLoginValid;
import com.mobanker.shanyidai.api.common.enums.SignLoginEnum;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.service.security.SecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 系统交互握手接口
 *
 * @version 1.0
 * @author: hanty
 * @date
 * @parameter
 * @return
 */
@Controller
@RequestMapping(value = "security")
public class SecurityContorller {

    @Resource
    private SecurityService securityService;

    /**
     * 第一次握手
     *
     * @return
     * @throws SydException
     * @author: hanty
     * @date 创建时间 2016-12-12 17:15
     * @version 1.0
     * @parameter
     */
    @SignLoginValid(SignLoginEnum.NOTALL)
    @RequestMapping(value = "firstShake", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object firstShake(SydRequest request) throws SydException {
        return securityService.firstShake(request);
    }

    /**
     * 第二次握手
     *
     * @return
     * @throws Exception
     * @author: hanty
     * @date 创建时间 2016-12-12 17:14
     * @version 1.0
     * @parameter
     */
    @SignLoginValid(SignLoginEnum.NOTALL)
    @RequestMapping(value = "secondShake", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object secondShake(SydRequest request) throws SydException {
        return securityService.secondShake(request);
    }
}
