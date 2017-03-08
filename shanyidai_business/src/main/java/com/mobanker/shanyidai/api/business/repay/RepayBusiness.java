package com.mobanker.shanyidai.api.business.repay;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.dto.borrow.BorrowInfoParam;
import com.mobanker.shanyidai.api.dto.borrow.BorrowRepay;
import com.mobanker.shanyidai.api.dto.repay.OtherRepayParam;
import com.mobanker.shanyidai.api.dto.repay.PayBackInfo;
import com.mobanker.shanyidai.api.dto.repay.PayResult;
import com.mobanker.shanyidai.api.dto.repay.RepayParam;

import java.util.List;

/**
 * @desc:
 * @author: Richard Core
 * @create time: 2017/1/10 13:56
 */
public interface RepayBusiness {
    /**
     * @param orderId
     * @return com.mobanker.shanyidai.api.dto.repay.PayBackInfo
     * @description 我要还款
     * @author Richard Core
     * @time 2017/1/11 14:13
     * @method getBorrowRepayDetail
     */
    public PayBackInfo getBorrowRepayDetail(String orderId, SydRequest request);

    List<BorrowRepay> getRepayInfo(BorrowInfoParam param);

    /**
     * @param paramBean
     * @return java.lang.Object
     * @description 还款（一键还款和微信支付）
     * @author Richard Core
     * @time 2017/1/17 16:03
     * @method addBillsAndPeriodRepay
     */
    public PayResult addBillsAndPeriodRepay(RepayParam paramBean);

    /**
     * @param paramBean
     * @return java.lang.Object
     * @description 还款（百度钱包，易宝支付）
     * @author Richard Core
     * @time 2017/1/17 16:03
     * @method addBillsAndOtherRepay
     */
    public PayResult addBillsAndOtherRepay(OtherRepayParam paramBean);

    /**
     * @param borrowNid
     * @return repayType
     * @description 查询还款状态
     * @author Richard Core
     * @time 2017/1/17 16:03
     * @method getRepayStatus
     */
    public PayResult getRepayStatus(String borrowNid, String repayType);
}
