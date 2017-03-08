/**
 *
 */
package com.mobanker.shanyidai.api.controller.v1_0_0;

import com.mobanker.shanyidai.api.common.annotation.SignLoginValid;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.enums.SignLoginEnum;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.service.repay.RepayService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 还款处理相关API
 *
 * @author chenjianping
 * @data 2016年12月9日
 */
@Controller
@RequestMapping(value = "repay")
public class RepayAction {
    @Resource
    private RepayService payBackService;


    /**
     * @param request
     * @param appVersion
     * @return java.lang.Object
     * @description 我要还款
     * @author Richard Core
     * @time 2017/1/10 14:46
     * @method getBorrowRepayDetail
     */
    @SignLoginValid(SignLoginEnum.ALL)
    @RequestMapping(value = "/canRepay", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object canRepay(SydRequest request, @RequestHeader String appVersion) throws SydException {

        Object data = null;
        try {
            data = payBackService.canRepay(request);
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.PAY_BACK_ERROR, e);
        }
        return data;
    }
 /**
     * @param request
     * @param appVersion
     * @return java.lang.Object
     * @description 还款信息
     * @author Richard Core
     * @time 2017/1/10 14:46
     * @method getBorrowRepayDetail
     */
    @SignLoginValid(SignLoginEnum.ALL)
    @RequestMapping(value = "/repayDetail", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getRepayDetail(SydRequest request, @RequestHeader String appVersion) throws SydException {

        Object data = null;
        try {
            data = payBackService.getBorrowRepayDetail(request);
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.PAY_BACK_ERROR, e);
        }
        return data;
    }

    /**
     * @param request
     * @return java.lang.Object
     * @description 还款接口（一键还款）
     * @author Richard Core
     * @time 2017/1/17 16:29
     * @method addOnekeyRepay
     */
    @SignLoginValid(SignLoginEnum.ALL)
    @RequestMapping(value = "/oneKey", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object addOnekeyRepay(SydRequest request, @RequestHeader String appVersion) throws SydException {
        Object data = null;
        try {
            data = payBackService.addOnekeyRepay(request);
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.REPAY_ERROR, e);
        }
        return data;
    }
    /**
     * @param request
     * @return java.lang.Object
     * @description 还款接口（一键还款）
     * @author Richard Core
     * @time 2017/1/17 16:29
     * @method addOnekeyRepay
     */
    @SignLoginValid(SignLoginEnum.ALL)
    @RequestMapping(value = "/baiduPay", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object addBaiduPay(SydRequest request, @RequestHeader String appVersion) throws SydException {
        Object data = null;
        try {
            data = payBackService.addBaiduPay(request);
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.REPAY_ERROR, e);
        }
        return data;
    }
    /**
     * @param request
     * @return java.lang.Object
     * @description 还款接口（微信支付）
     * @author Richard Core
     * @time 2017/1/17 16:29
     * @method addOnekeyRepay
     */
    @SignLoginValid(SignLoginEnum.ALL)
    @RequestMapping(value = "/wechat", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object addWechatRepay(SydRequest request, @RequestHeader String appVersion) throws SydException {
        Object data = null;
        try {
            data = payBackService.addWechatRepay(request);
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.REPAY_ERROR, e);
        }
        return data;
    }


}
