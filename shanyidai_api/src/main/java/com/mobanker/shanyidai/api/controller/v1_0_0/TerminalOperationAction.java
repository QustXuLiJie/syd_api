/**
 *
 */
package com.mobanker.shanyidai.api.controller.v1_0_0;

import com.mobanker.framework.constant.Constants;
import com.mobanker.shanyidai.api.common.annotation.SignLoginValid;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.enums.SignLoginEnum;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.ResponseEntity;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.service.terminal.GatherService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 终端设备上操作行为相关API
 *
 * @author chenjianping
 * @data 2016年12月9日
 */
@Controller
@RequestMapping(value = "operation")
public class TerminalOperationAction {

    @Resource
    private GatherService gatherService;

    @SignLoginValid(SignLoginEnum.SIGN)
    @RequestMapping(value = "/appOperationActivation", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object appOperationActivation(SydRequest request) {
        ResponseEntity response = null;
        try {
            gatherService.appOperationActivation(request);
            response = new ResponseEntity(Constants.System.OK);

        } catch (SydException e) {
            if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
                throw e;
            } else {
                throw new SydException(ReturnCode.ACTIVE_FAILED.getCode(), e.message, e);
            }
        } catch (Throwable e) {
            throw new SydException(ReturnCode.ACTIVE_FAILED, e);
        }
        return response;
    }
}

