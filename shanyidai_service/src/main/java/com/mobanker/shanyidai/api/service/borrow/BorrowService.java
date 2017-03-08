package com.mobanker.shanyidai.api.service.borrow;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.dto.borrow.BorrowHistory;
import com.mobanker.shanyidai.api.dto.borrow.BorrowInfo;
import com.mobanker.shanyidai.api.dto.borrow.OrderDetail;
import com.mobanker.shanyidai.api.dto.borrow.QlBorrowing;

import java.util.List;

/**
 * @author hantongyang
 * @description 借款相关接口
 * @time 2017/1/10 14:48
 */
public interface BorrowService {

    /**
     * @description 是否能借款
     * @author hantongyang
     * @time 2017/1/10 14:52
     * @method canBorrow
     * @param request
     * @return boolean
     */
    void canBorrow(SydRequest request);

    /**
     * @description 根据订单号查询用户借款详情
     * @author hantongyang
     * @time 2017/1/12 13:49
     * @method getBorrowInfo
     * @param request
     * @return com.mobanker.shanyidai.api.dto.borrow.BorrowInfo
     */
    BorrowInfo getBorrowInfo(SydRequest request);
    /**
     * @param request
     * @return OrderDetail
     * @description 获取借还款详情
     * @author Richard Core
     * @time 2017/1/15 15:14
     * @method getOrderDetail
     */
    OrderDetail getOrderDetail(SydRequest request);

    /**
     * @param request
     * @param nid
     * @return com.mobanker.shanyidai.api.dto.borrow.OrderDetail
     * @description 获取订单借还款详情
     * @author Richard Core
     * @time 2017/1/17 19:47
     * @method getOrderDetail
     */
    public OrderDetail getOrderDetail(SydRequest request, String nid);

    /**
     * @description 根据订单号查询用户借款详情
     * @author hantongyang
     * @time 2017/1/14 17:18
     * @method getBorrowInfo
     * @param nid
     * @return com.mobanker.shanyidai.api.dto.borrow.BorrowInfo
     */
    BorrowInfo getBorrowInfo(String nid);

    /**
     * @description 获取在是否在前隆借款中
     * @author hantongyang
     * @time 2017/1/14 14:47
     * @method getQlBorrowing
     * @param request
     * @return com.mobanker.shanyidai.api.dto.borrow.QlBorrowing
     */
    QlBorrowing getQlBorrowing(SydRequest request);

    /**
     * @description 查询借款单历史
     * @author hantongyang
     * @time 2017/1/14 18:23
     * @method getBorrowHistory
     * @param request
     * @param nid
     * @return com.mobanker.shanyidai.api.dto.borrow.BorrowHistory
     */
    BorrowHistory getBorrowHistory(SydRequest request, String nid);

    /**
     * @description 查询借款单历史，如果不传借款单号，查询所有借款单
     * @author hantongyang
     * @time 2017/1/14 18:23
     * @method getBorrowHistoryList
     * @param request
     * @param nid
     * @return java.util.List
     */
    List<BorrowHistory> getBorrowHistoryList(SydRequest request, String nid);

    /**
     * @description 生成借款单
     * @author hantongyang
     * @time 2017/1/16 16:45
     * @method addBorrow
     * @param request
     * @return void
     */
    void addBorrow(SydRequest request);

    Object getHistoryList(SydRequest request);

    /**
     * @param request
     * @return java.lang.Integer
     * @description 借款次数
     * @author Richard Core
     * @time 2017/1/24 11:59
     * @method getBorrowTimes
     */
    Integer getBorrowTimes(SydRequest request);

    /**
     * @param request
     * @return java.lang.Object
     * @description 是否在闪宜贷借款中
     * @author Richard Core
     * @time 2017/2/6 9:57
     * @method sydBorrowing
     */
    boolean sydBorrowing(SydRequest request);
}
