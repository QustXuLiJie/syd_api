/**
 *
 */
package com.mobanker.shanyidai.api.controller.v1_0_0;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.service.borrow.BorrowService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 借款处理
 *
 * @author chenjianping
 * @data 2016年12月9日
 */
@Controller
@RequestMapping(value = "borrow")
public class BorrowAction {

    @Resource
    private BorrowService borrowService;

    /**
     * @param request
     * @return java.lang.Object
     * @description 是否能借款
     * @author hantongyang
     * @time 2017/1/10 18:00
     * @method canBorrow
     */
    @RequestMapping(value = "canBorrow", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object canBorrow(SydRequest request) {
        try {
            borrowService.canBorrow(request);
        } catch (SydException e) {
            if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
                throw e;
            } else {
                throw new SydException(ReturnCode.ERROR_BORROW_CHECK_QL_BORROWING.getCode(), e.message, e);
            }
        } catch (Throwable e) {
            throw new SydException(ReturnCode.ERROR_BORROW_CHECK_QL_BORROWING, e);
        }
        return null;
    }

    /**
     * @param request
     * @return java.lang.Object
     * @description 是否在闪宜贷借款中
     * @author Richard Core
     * @time 2017/2/6 9:55
     * @method sydBorrowing
     */
    @RequestMapping(value = "sydBorrowing", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object sydBorrowing(SydRequest request) {
        try {
            return borrowService.sydBorrowing(request);
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.ERROR_BORROW_CHECK_QL_BORROWING.getCode(), e.getMessage());
        }
    }

    /**
     * @param request
     * @return java.lang.Object
     * @description 获取借款详情
     * @author hantongyang
     * @time 2017/1/10 18:00
     * @method canBorrow
     */
    @RequestMapping(value = "getBorrowInfo", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getBorrowInfo(SydRequest request) {
        Object data = null;
        try {
            data = borrowService.getBorrowInfo(request);
        } catch (SydException e) {
            if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
                throw e;
            } else {
                throw new SydException(ReturnCode.ERROR_BORROW_GET_INFO.getCode(), e.message, e);
            }
        } catch (Throwable e) {
            throw new SydException(ReturnCode.ERROR_BORROW_GET_INFO, e);
        }
        return data;
    }

    /**
     * @param request
     * @return java.lang.Object
     * @description 获取借款次数
     * @author R.Core
     * @time 2017/1/24 12:00
     * @method getBorrowTimes
     */
    @RequestMapping(value = "getBorrowTimes", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getBorrowTimes(SydRequest request) {
        Object data = null;
        try {
            data = borrowService.getBorrowTimes(request);
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.ERROR_BORROW_GET_INFO, e);
        }
        return data;
    }

    /**
     * @param request
     * @return java.lang.Object
     * @description 获取借还款详情
     * @author R.Core
     * @time 2017/1/15 18:00
     * @method canBorrow
     */
    @RequestMapping(value = "borrowDetail", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getBorrowDetail(SydRequest request) {
        Object data = null;
        try {
            data = borrowService.getOrderDetail(request);
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.ERROR_BORROW_GET_INFO, e);
        }
        return data;
    }

    /**
     * @param request
     * @return java.lang.Object
     * @description 借款记录
     * @author R.Core
     * @time 2017/1/22 18:00
     * @method history
     */
    @RequestMapping(value = "history", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getHistoryList(SydRequest request) {
        Object data = null;
        try {
            data = borrowService.getHistoryList(request);
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.ERROR_BORROW_GET_INFO, e);
        }
        return data;
    }

    /**
     * @param request
     * @return java.lang.Object
     * @description 生成借款单
     * @author hantongyang
     * @time 2017/1/16 16:42
     * @method addBorrow
     */
    @RequestMapping(value = "addBorrow", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object addBorrow(SydRequest request) {
        try {
            borrowService.addBorrow(request);
        } catch (SydException e) {
            if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
                throw e;
            } else {
                throw new SydException(ReturnCode.ERROR_BORROW_GET_INFO.getCode(), e.message, e);
            }
        } catch (Throwable e) {
            throw new SydException(ReturnCode.ERROR_BORROW_GET_INFO, e);
        }
        return null;
    }

}
