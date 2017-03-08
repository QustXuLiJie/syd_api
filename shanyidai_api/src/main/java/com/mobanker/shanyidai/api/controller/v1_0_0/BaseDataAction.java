package com.mobanker.shanyidai.api.controller.v1_0_0;


import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.service.basedata.BaseDataService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 获取职业类型列表、联系人关系列表、学历列表API
 *
 * @Created by zhouqianqian on 2017/2/8.
 */

@Controller
@RequestMapping(value = "basedata")
public class BaseDataAction {
    @Resource
    private BaseDataService baseDataService;


    /**
     * 获取职业类型
     *
     * @return java.lang.Object
     * @throws SydException
     * @method getJobType
     */

    @RequestMapping(value = "getJobTypeList", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getJobType(SydRequest request) throws SydException {
        try {
            String result = baseDataService.listJobType();
            return result;
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.ERROR_JOBTYPE_GET_INFO.getCode(), e.getMessage());
        }
    }


    /**
     * 获取联系人关系
     *
     * @return java.lang.Object
     * @throws SydException
     * @method getRelation
     */
    @RequestMapping(value = "getRelation", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getRelation(SydRequest request) throws SydException {
        try {
            String product = request.getProduct();
            String result = baseDataService.getRelation(product);
            return result;
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.ERROR_RELATION_GET_INFO.getCode(), e.getMessage());
        }
    }



    /**
     * 获取学历列表
     *
     * @return java.lang.Object
     * @throws SydException
     * @method getEducation
     */
    @RequestMapping(value = "getEducation", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getEducation(SydRequest request) throws SydException {
        try {
            String product = request.getProduct();
            String result = baseDataService.getEducation(product);
            return result;
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.ERROR_EDUCATION_GET_INFO.getCode(), e.getMessage());
        }
    }

}
