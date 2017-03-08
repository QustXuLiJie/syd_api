/**
 *
 */
package com.mobanker.shanyidai.api.business.repay;

import com.mobanker.shanyidai.api.business.borrow.BorrowBusiness;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.dto.borrow.BorrowHistory;
import com.mobanker.shanyidai.api.dto.borrow.BorrowInfoParam;
import com.mobanker.shanyidai.api.dto.borrow.BorrowRepay;
import com.mobanker.shanyidai.api.dto.repay.OtherRepayParam;
import com.mobanker.shanyidai.api.dto.repay.PayBackInfo;
import com.mobanker.shanyidai.api.dto.repay.PayResult;
import com.mobanker.shanyidai.api.dto.repay.RepayParam;
import com.mobanker.shanyidai.dubbo.dto.payback.OtherRepayParamDto;
import com.mobanker.shanyidai.dubbo.dto.payback.PayBackInfoDto;
import com.mobanker.shanyidai.dubbo.dto.payback.PayBackParamDto;
import com.mobanker.shanyidai.dubbo.dto.payback.RepayParamDto;
import com.mobanker.shanyidai.dubbo.service.payback.PayBackDubboService;
import com.mobanker.shanyidai.esb.common.exception.EsbException;
import com.mobanker.shanyidai.esb.common.logger.LogManager;
import com.mobanker.shanyidai.esb.common.logger.Logger;
import com.mobanker.shanyidai.esb.common.packet.ResponseBuilder;
import com.mobanker.shanyidai.esb.common.packet.ResponseEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @desc:
 * @author: Richard Core
 * @create time: 2017/1/10 13:56
 */
@Service
public class RepayBusinessImpl implements RepayBusiness {
    public static final Logger logger = LogManager.getSlf4jLogger(RepayBusinessImpl.class);
    @Resource
    private PayBackDubboService payBackDubboService;
    @Resource
    private BorrowBusiness borrowBusiness;
    /**
     * @param orderId
     * @param request
     * @return com.mobanker.shanyidai.api.dto.repay.PayBackInfo
     * @description 我要还款
     * @author Richard Core
     * @time 2017/1/11 14:13
     * @method PayBackInfo
     */
    @Override
    public PayBackInfo getBorrowRepayDetail(String orderId, SydRequest request) {
        //参数处理
        if (StringUtils.isBlank(orderId)) {
            throw new SydException(ReturnCode.ORDER_ID_NULL);
        }
        PayBackParamDto dto = BeanHelper.cloneBean(request, PayBackParamDto.class);
        dto.setBorrowNid(orderId);
        dto.setUserId(request.getUserId());
        ResponseEntity responseEntity = null;
        try {
            responseEntity = payBackDubboService.getBorrowRepayDetail(dto);
        } catch (EsbException e) {
            logger.error("查询还款信息失败", e);
            throw new SydException(e.errCode, e.message);
        } catch (Exception e1) {
            logger.error("查询还款信息失败", e1);
            throw new SydException(ReturnCode.PAY_BACK_ERROR.getCode(), e1.getMessage());
        }
        if (!ResponseBuilder.isSuccess(responseEntity)) {
            logger.error("查询还款信息失败", responseEntity);
            throw new SydException(responseEntity.getError(), responseEntity.getMsg());
        }
        if (responseEntity.getData() == null) {
            throw new SydException(ReturnCode.PAY_BACK_ERROR.getCode(),"财务服务没有返回数据");
        }
        PayBackInfoDto result = (PayBackInfoDto) responseEntity.getData();
        //返回值处理
        PayBackInfo info = BeanHelper.cloneBean(result, PayBackInfo.class);
        return info;
    }

    @Override
    public List<BorrowRepay> getRepayInfo(BorrowInfoParam param) {
        List<BorrowHistory> historyList = borrowBusiness.getHistoryList(param);
        if (historyList == null || historyList.isEmpty()) {
            return new ArrayList<BorrowRepay>();
        }
        BorrowHistory borrowHistory = historyList.get(0);
        if (borrowHistory == null) {
            return new ArrayList<BorrowRepay>();
        }
        return borrowHistory.getRepayList();
    }

    /**
     * @param paramBean
     * @return java.lang.Object
     * @description 还款（一键还款和微信支付）
     * @author Richard Core
     * @time 2017/1/17 16:03
     * @method addBillsAndPeriodRepay
     */
    @Override
    public PayResult addBillsAndPeriodRepay(RepayParam paramBean) {
        //参数验证和封装
        RepayParamDto repayParamDto = RepayDubboConvert.getRepayDubboParam(paramBean);
        //调用综合服务支付
        ResponseEntity responseEntity = null;
        try {
            responseEntity = payBackDubboService.addBillsAndPeriodRepay(repayParamDto);
        } catch (EsbException e) {
            logger.error("还款失败", e);
            throw new SydException(e.errCode, e.message,e);
        } catch (Exception e1) {
            logger.error("还款失败", e1);
            throw new SydException(ReturnCode.REPAY_ERROR.getCode(), e1.getMessage(),e1);
        }
        if (!ResponseBuilder.isSuccess(responseEntity)) {
            logger.error("还款失败", responseEntity);
            throw new SydException(responseEntity.getError(), responseEntity.getMsg());
        }
        return (PayResult) responseEntity.getData();
    }

    /**
     * @param paramBean
     * @return PayResult
     * @description 还款（百度钱包，易宝支付）
     * @author Richard Core
     * @time 2017/1/17 16:03
     * @method addBillsAndOtherRepay
     */
    @Override
    public PayResult addBillsAndOtherRepay(OtherRepayParam paramBean) {
        //参数验证和封装
        OtherRepayParamDto repayParamDto = RepayDubboConvert.getOtherRepayDubboParam(paramBean);
        //调用综合服务支付
        ResponseEntity responseEntity = null;
        try {
            responseEntity = payBackDubboService.addBillsAndOtherRepay(repayParamDto);
        } catch (EsbException e) {
            logger.error("还款失败", e);
            throw new SydException(e.errCode, e.message,e);
        } catch (Exception e1) {
            logger.error("还款失败", e1);
            throw new SydException(ReturnCode.REPAY_ERROR.getCode(), e1.getMessage(),e1);
        }
        if (!ResponseBuilder.isSuccess(responseEntity)) {
            logger.error("还款失败", responseEntity);
            throw new SydException(responseEntity.getError(), responseEntity.getMsg());
        }
        return (PayResult) responseEntity.getData();
    }

    /**
     * @param borrowNid
     * @return repayType
     * @description 查询还款状态
     * @author Richard Core
     * @time 2017/1/17 16:03
     * @method getRepayStatus
     */
    @Override
    public PayResult getRepayStatus(String borrowNid,String repayType) {
        //调用综合服务支付
        ResponseEntity responseEntity = null;
        try {
            responseEntity = payBackDubboService.getRepayStatus(borrowNid,repayType);
        } catch (EsbException e) {
            logger.error("查询还款状态失败", e);
            throw new SydException(e.errCode, e.message, e);
        } catch (Exception e1) {
            logger.error("查询还款状态失败", e1);
            throw new SydException(ReturnCode.pay_result_error.getCode(), e1.getMessage(), e1);
        }
        if (!ResponseBuilder.isSuccess(responseEntity)) {
            logger.error("查询还款状态失败", responseEntity);
            throw new SydException(responseEntity.getError(), responseEntity.getMsg());
        }
        return BeanHelper.cloneBean(responseEntity.getData(), PayResult.class);
    }



}
