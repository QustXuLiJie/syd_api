/**
 * 
 */
package com.mobanker.shanyidai.api.business.borrow;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.dto.borrow.*;

import java.util.List;

/**
 * @author chenjianping
 * @data 2016年12月11日
 */
public interface BorrowBusiness {

    /**
     * @param param
     * @return java.util.List<com.mobanker.shanyidai.api.dto.borrow.BorrowHistory>
     * @description 查询借款历史
     * @author Richard Core
     * @time 2017/1/12 15:32
     * @method getHistoryList
     */
    public List<BorrowHistory> getHistoryList(BorrowInfoParam param);

    /**
     * @param request
     * @return boolean
     * @description 是否在前隆借款中，即能否借款
     * @author hantongyang
     * @time 2017/1/10 15:46
     * @method checkIsQLBorrowIng
     */
    QlBorrowing checkIsQLBorrowIng(SydRequest request);

    /**
     * @description 根据订单号获取借款详情
     * @author hantongyang
     * @time 2017/1/12 14:33
     * @method getBorrowInfo
     * @param nid
     * @return com.mobanker.shanyidai.api.dto.borrow.BorrowInfo
     */
    BorrowInfo getBorrowInfo(String nid);

    /**
     * @description 创建下款单
     * @author hantongyang
     * @time 2017/1/17 10:56
     * @method addBorrowOrder
     * @param dto
     * @return void
     */
    void addBorrowOrder(BorrowOrderDto dto);
}
