package com.mobanker.shanyidai.api.service.index.util;

import com.mobanker.shanyidai.api.dto.borrow.BorrowInfo;
import com.mobanker.shanyidai.api.dto.borrow.BorrowProcess;
import com.mobanker.shanyidai.api.dto.index.Index;
import com.mobanker.shanyidai.api.dto.product.ProductTemp;
import com.mobanker.shanyidai.api.dto.repay.PayBackInfo;
import com.mobanker.shanyidai.api.dto.user.UserCompleteness;
import com.mobanker.shanyidai.api.enums.BorrowProcessingEnum;

import java.util.List;

/**
 * @author hantongyang
 * @description
 * @time 2017/1/14 13:58
 */
public class IndexBusinessConvert {

    /**
     * @param temp
     * @param comp
     * @param borrow
     * @param payBack
     * @param borrowStatus
     * @param borrowCou
     * @param process
     * @return com.mobanker.shanyidai.api.dto.index.Index
     * @description 封装首页需要的信息
     * @author hantongyang
     * @time 2017/1/15 10:34
     * @method initIndex
     */
    public static Index initIndex(ProductTemp temp, UserCompleteness comp, BorrowInfo borrow,
                                  PayBackInfo payBack, String borrowStatus, int borrowCou, List<BorrowProcess> process, int bankCardAmount) {
        Index index = new Index();
        index.setProductTemp(temp); //产品模板
        index.setUserCompleteness(comp); //认证进度
        index.setBorrowStatus(borrowStatus); //借款单进度
        index.setBorrowInfo(borrow); //借款详情
        index.setPayBack(payBack); //还款信息
        index.setBorrowCount(borrowCou); //借款次数
        index.setBorrowProcesses(process); //借款单进度
        index.setBankCardAmount(bankCardAmount);//银行卡个数
        return index;
    }

    /**
     * @param status
     * @return java.lang.String
     * @description 封装借款单状态，供首页使用
     * @author hantongyang
     * @time 2017/1/15 11:04
     * @method initBorrowStatus
     */
    public static String initBorrowStatus(String status) {
        BorrowProcessingEnum processingEnum = BorrowProcessingEnum.getByStatus(status);
        switch (processingEnum) {
            case AUDITINT://  审核中
            case AUDIT_FAILED://审核失败
            case AUDIT_SUCCESS://审核通过
            case REPAY_PROCESSING://放款成功-还款处理中
                return "1";//展示订单详情，展示进度

            case LOAN_SUCCESS://放款成功-待还款
            case REPAY_FAILED://放款成功-还款失败
                return "2";//展示我要还款页面

            case LOAN_FAILED://放款失败
            case REPAYMENT_SUCCESS://还款成功
            case REPAY_SUCCESS://放款成功-已还款
            default:
                return "-1";//下单页面

        }
    }
}
