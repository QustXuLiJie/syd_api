package com.mobanker.shanyidai.api.business.borrow.util;

import com.mobanker.shanyidai.api.dto.borrow.BorrowHistory;
import com.mobanker.shanyidai.api.dto.borrow.BorrowRepay;
import com.mobanker.shanyidai.api.dto.borrow.RepayDetail;
import com.mobanker.shanyidai.dubbo.dto.borrow.BorrowHistoryDto;
import com.mobanker.shanyidai.dubbo.dto.borrow.BorrowHistoryRepayDto;
import com.mobanker.shanyidai.dubbo.dto.borrow.RepayDetailDto;
import com.mobanker.shanyidai.esb.common.packet.ResponseEntity;
import com.mobanker.shanyidai.esb.common.utils.BeanUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @desc:
 * @author: Richard Core
 * @create time: 2017/1/12 15:26
 */
public class BorrowDubboConvert {

    /**
     * @param responseEntity
     * @return java.util.List<com.mobanker.shanyidai.api.dto.borrow.BorrowHistory>
     * @description 封装借款历史
     * @author Richard Core
     * @time 2017/1/12 15:26
     * @method getBorrowHistoryDtos
     */
    public static List<BorrowHistory> getBorrowHistoryDtos(ResponseEntity responseEntity) {
        List<BorrowHistoryDto> dataList = (List<BorrowHistoryDto>) responseEntity.getData();
        if (dataList == null || dataList.isEmpty()) {
            return new ArrayList<BorrowHistory>();
        }
        List<BorrowHistory> resultList = new ArrayList<BorrowHistory>();
        for (BorrowHistoryDto borrowHistoryBean : dataList) {
            if (borrowHistoryBean == null) {
                continue;
            }
            BorrowHistory dto = BeanUtil.cloneBean(borrowHistoryBean, BorrowHistory.class);
            //封装还款信息
//            setRepayList(borrowHistoryBean, dto);
            //封装还款详情
//            setRepayDetailList(borrowHistoryBean, dto);
            resultList.add(dto);
        }
        return resultList;
    }
    /**
     * @return void
     * @description 封装还款信息
     * @author Richard Core
     * @time 2017/1/12 9:58
     * @method setRepayList
     */
    public static void setRepayList(BorrowHistoryDto borrowHistoryBean, BorrowHistory dto) {
        List<BorrowRepay> repayDtoList = new ArrayList<BorrowRepay>();
        List<BorrowHistoryRepayDto> repayList = borrowHistoryBean.getRepayList();
        if (repayList == null || repayList.isEmpty()) {
            return;
        }
        for (BorrowHistoryRepayDto repayBean : repayList) {
            if (repayBean == null) {
                continue;
            }
            BorrowRepay borrowHistoryRepayDto = BeanUtil.cloneBean(repayBean, BorrowRepay.class);
            repayDtoList.add(borrowHistoryRepayDto);
        }
        dto.setRepayList(repayDtoList);
    }

    /**
     * @param borrowHistoryBean
     * @param dto
     * @return void
     * @description 封装还款详情
     * @author Richard Core
     * @time 2017/1/12 9:59
     * @method setRepayDetailList
     */
    public static void setRepayDetailList(BorrowHistoryDto borrowHistoryBean, BorrowHistory dto) {
        List<RepayDetailDto> repayDetails = borrowHistoryBean.getRepayDetails();
        List<RepayDetail> detailList = new ArrayList<RepayDetail>();
        if (repayDetails == null || repayDetails.isEmpty()) {
            return;
        }
        for (RepayDetailDto repayDetail : repayDetails) {
            if (repayDetail == null) {
                continue;
            }
            RepayDetail repayDetailDto = BeanUtil.cloneBean(repayDetail, RepayDetail.class);
            detailList.add(repayDetailDto);
        }
        dto.setRepayDetails(detailList);
    }
}
