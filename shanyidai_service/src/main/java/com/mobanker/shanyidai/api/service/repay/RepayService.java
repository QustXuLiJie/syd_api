package com.mobanker.shanyidai.api.service.repay;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.dto.repay.PayBackInfo;
import com.mobanker.shanyidai.api.dto.repay.PayResult;

/**
 * @desc: 还款相关
 * @author: Richard Core
 * @create time: 2017/1/10 14:19
 */
public interface RepayService {

    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.repay.PayBackInfo
     * @description 还款信息
     * @author Richard Core
     * @time 2017/1/11 14:10
     * @method getBorrowRepayDetail
     */
    public PayBackInfo getBorrowRepayDetail(SydRequest request);

    PayBackInfo getRepayDetail(String nid, SydRequest request);

    /**
     * @param request
     * @return java.lang.Object
     * @description 还款接口（一键还款）
     * @author Richard Core
     * @time 2017/1/17 16:29
     * @method addOnekeyRepay
     */
    PayResult addOnekeyRepay(SydRequest request);

    /**
     * @param request
     * @return java.lang.Object
     * @description 百度钱包支付
     * @author Richard Core
     * @time 2017/1/23 10:09
     * @method addBaiduPay
     */
    public PayResult addBaiduPay(SydRequest request);

    /**
     * @param request
     * @return java.lang.Object
     * @description 还款接口（微信支付）
     * @author Richard Core
     * @time 2017/1/17 16:29
     * @method addWechatRepay
     */
    public PayResult addWechatRepay(SydRequest request);

    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.repay.PayBackInfo
     * @description 是否能还款
     * @author Richard Core
     * @time 2017/1/22 11:37
     * @method canRepay
     */
    Object canRepay(SydRequest request);

    /**
     * @param borrowNid
     * @return com.mobanker.shanyidai.api.dto.repay.PayResult
     * @description 获取还款状态
     * @author Richard Core
     * @time 2017/1/23 11:50
     * @method getRepayStatus
     */
    public PayResult getRepayStatus(String borrowNid);
}
