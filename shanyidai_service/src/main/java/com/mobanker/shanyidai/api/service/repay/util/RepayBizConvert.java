package com.mobanker.shanyidai.api.service.repay.util;

import com.mobanker.shanyidai.api.dto.repay.PayBackInfo;

import java.util.Date;

/**
 * @desc: 还款相关辅助类
 * @author: Richard Core
 * @create time: 2017/1/18 15:10
 */
public class RepayBizConvert {
    /**
     * @param userId
     * @return com.mobanker.shanyidai.api.dto.repay.PayBackInfo
     * @description 封装防重复提交参数
     * @author Richard Core
     * @time 2017/1/18 15:18
     * @method initRedisParam2AvoidRepeatPay
     */
    public static PayBackInfo initRedisParam2AvoidRepeatPay(Long userId){
        PayBackInfo param = new PayBackInfo();
        param.setUserId(userId);
        param.setRepayYesTime(new Date());
        return param;
    }
}
