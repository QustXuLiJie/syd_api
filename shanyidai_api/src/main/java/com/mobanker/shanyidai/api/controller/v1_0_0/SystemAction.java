package com.mobanker.shanyidai.api.controller.v1_0_0;

import com.mobanker.shanyidai.api.common.annotation.SignLoginValid;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.enums.SignLoginEnum;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.logger.LogManager;
import com.mobanker.shanyidai.api.common.logger.Logger;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.service.system.SystemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author hantongyang
 * @description 基础相关接口
 * @time 2017/1/20 18:32
 */
@Controller
@RequestMapping(value = "system")
public class SystemAction {

    private Logger logger = LogManager.getSlf4jLogger(SystemAction.class);

    @Resource
    private SystemService systemService;

    /**
     * @description 获取H5Url方法
     * @author hantongyang
     * @time 2017/1/20 18:35
     * @method getH5Url
     * @param request
     * @return java.lang.Object
     */
    @SignLoginValid(SignLoginEnum.SIGN)
    @RequestMapping(value = "getUrlByType", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getH5Url(SydRequest request){
        Object obj = null;
        try {
            obj = systemService.getUrlByType(request);
        } catch (SydException e) {
            if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
                throw e;
            } else {
                throw new SydException(ReturnCode.ERROR_GET_H5_URL.getCode(), e.message, e);
            }
        } catch (Throwable e) {
            throw new SydException(ReturnCode.ERROR_GET_H5_URL, e);
        }
        return obj;
    }

    /**
     * @description 根据类型查询数据字典列表
     * @author hantongyang
     * @time 2017/1/23 14:56
     * @method getDictionaryByType
     * @param request
     * @return java.lang.Object
     */
    @RequestMapping(value = "getDictionaryByType", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getDictionaryByType(SydRequest request){
        Object obj = null;
        try {
            obj = systemService.getDictionaryByType(request);
        } catch (SydException e) {
            if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
                throw e;
            } else {
                throw new SydException(ReturnCode.ERROR_GET_DICTIONARY.getCode(), e.message, e);
            }
        } catch (Throwable e) {
            throw new SydException(ReturnCode.ERROR_GET_DICTIONARY, e);
        }
        return obj;
    }

    /**
     * @description 根据类型、编码获取数据字典项
     * @author hantongyang
     * @time 2017/1/23 14:55
     * @method getDictionaryByCode
     * @param request
     * @return java.lang.Object
     */
    @RequestMapping(value = "getDictionaryByCode", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getDictionaryByCode(SydRequest request){
        Object obj = null;
        try {
            obj = systemService.getDictionaryByCode(request);
        } catch (SydException e) {
            if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
                throw e;
            } else {
                throw new SydException(ReturnCode.ERROR_GET_DICTIONARY.getCode(), e.message, e);
            }
        } catch (Throwable e) {
            throw new SydException(ReturnCode.ERROR_GET_DICTIONARY, e);
        }
        return obj;
    }

    /**
     * @description 删除缓存中的数据
     * @author hantongyang
     * @time 2017/2/13 9:55
     * @method delRedisByCode
     * @param request
     * @return java.lang.Object
     */
    @SignLoginValid(SignLoginEnum.LOGIN)
    @RequestMapping(value = "resetRedisByCode", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object delRedisByCode(SydRequest request){
        Object obj = null;
        try {
            systemService.delRedisByCode(request);
        } catch (SydException e) {
            if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
                throw e;
            } else {
                throw new SydException(ReturnCode.REDIS_ERROR.getCode(), e.message, e);
            }
        } catch (Throwable e) {
            throw new SydException(ReturnCode.REDIS_ERROR, e);
        }
        return obj;
    }

    /**
     * @description 获取配置信息
     * @author hantongyang
     * @time 2017/2/14 11:46
     * @method getConfig
     * @param
     * @return java.lang.Object
     */
    @SignLoginValid(SignLoginEnum.NOTALL)
    @RequestMapping(value = "getConfig", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object getConfig(){
        Object obj = null;
        try {
            obj = systemService.getConfig();
        } catch (SydException e) {
            if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
                throw e;
            } else {
                logger.error("======获取配置信息失败", e);
                throw new SydException(ReturnCode.CONFIG_DATA_NULL);
            }
        } catch (Throwable e) {
            logger.error("======获取配置信息失败", e);
            throw new SydException(ReturnCode.CONFIG_DATA_NULL);
        }
        return obj;
    }
}
