package com.mobanker.shanyidai.api.business.repay;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.dto.repay.OtherRepayParam;
import com.mobanker.shanyidai.api.dto.repay.RepayParam;
import com.mobanker.shanyidai.dubbo.dto.payback.OtherRepayParamDto;
import com.mobanker.shanyidai.dubbo.dto.payback.RepayParamDto;
import org.apache.commons.lang.StringUtils;

/**
 * @desc:
 * @author: Richard Core
 * @create time: 2017/1/17 16:05
 */
public class RepayDubboConvert {
    /**
     * @param paramBean
     * @return com.mobanker.shanyidai.dubbo.dto.payback.RepayParamDto
     * @description 还款支付接口参数验证和封装
     * @author Richard Core
     * @time 2017/1/17 16:07
     * @method getRepayDubboParam
     */
    public static RepayParamDto getRepayDubboParam(RepayParam paramBean) {
        if (paramBean == null) {
            throw new SydException(ReturnCode.REPAY_ERROR.getCode(), "还款参数为空");
        }
        if (paramBean.getUserId() == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        if (StringUtils.isBlank(paramBean.getRepayUid())) {
            throw new SydException(ReturnCode.REPAY_TYPE_NULL);
        }
        RepayParamDto repayParamDto = BeanHelper.cloneBean(paramBean, RepayParamDto.class);
        repayParamDto.setUserId(paramBean.getUserId());
        repayParamDto.setRepayUid(paramBean.getRepayUid());
        repayParamDto.setType(paramBean.getProduct());
        repayParamDto.setChannel(paramBean.getChannel());
        repayParamDto.setSpbillCreateIp(paramBean.getIp());
        //默认单期还款
        if (StringUtils.isBlank(repayParamDto.getPeriod())) {
            repayParamDto.setPeriod("1");
        }
        return repayParamDto;
    }


    /**
     * @param paramBean
     * @return com.mobanker.shanyidai.dubbo.dto.payback.OtherRepayParamDto
     * @description 验证封装易宝和百度钱包还款参数
     * @author Richard Core
     * @time 2017/1/22 21:13
     * @method getOtherRepayDubboParam
     */
    public static OtherRepayParamDto getOtherRepayDubboParam(OtherRepayParam paramBean) {
        if (paramBean == null) {
            throw new SydException(ReturnCode.REPAY_ERROR.getCode(), "还款参数为空");
        }
        if (paramBean.getUserId() == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        if (StringUtils.isBlank(paramBean.getRepayUid())) {
            throw new SydException(ReturnCode.REPAY_TYPE_NULL);
        }
        if (StringUtils.isBlank(paramBean.getPayType())) {
            throw new SydException(ReturnCode.REPAY_TYPE_NULL);
        }
        OtherRepayParamDto repayParamDto = BeanHelper.cloneBean(paramBean, OtherRepayParamDto.class);
//        repayParamDto.setUserId(paramBean.getUserId());
//        repayParamDto.setRepayUid(paramBean.getRepayUid());
        repayParamDto.setType(paramBean.getProduct());
//        repayParamDto.setChannel(paramBean.getChannel());
//        repayParamDto.setIp(paramBean.getIp());
        //默认单期还款
        if (StringUtils.isBlank(repayParamDto.getPeriod())) {
            repayParamDto.setPeriod("1");
        }
        return repayParamDto;
    }
}
