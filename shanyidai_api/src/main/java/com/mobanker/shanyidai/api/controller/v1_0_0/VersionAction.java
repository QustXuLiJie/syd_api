/**
 *
 */
package com.mobanker.shanyidai.api.controller.v1_0_0;

import com.mobanker.shanyidai.api.common.annotation.SignLoginValid;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.enums.SignLoginEnum;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.service.version.VersionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 版本信息管理相关API
 *
 * @author chenjianping
 * @data 2016年12月9日
 */
@Controller
@RequestMapping(value = "version")
public class VersionAction {

    @Resource
    private VersionService versionService;

    /**
     * @param request
     * @return java.lang.Object
     * @author xulijie
     * @method getAppVersion
     * @description 获取APP版本更新接口
     * @time 19:25 2017/2/23
     */
    @SignLoginValid(SignLoginEnum.SIGN)
    @RequestMapping(value = "/getAppVersion", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getAppVersion(SydRequest request ) {
        Object data = null;
        try {
            data = versionService.getVersion(request);
        } catch (SydException e) {
            if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
                throw e;
            } else {
                throw new SydException(ReturnCode.CHECKVERSION_FAILED.getCode(), e.message, e);
            }
        } catch (Throwable e) {
            throw new SydException(ReturnCode.CHECKVERSION_FAILED, e);
        }
        return data;
    }
}
