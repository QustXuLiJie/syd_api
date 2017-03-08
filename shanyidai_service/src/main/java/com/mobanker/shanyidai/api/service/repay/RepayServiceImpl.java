package com.mobanker.shanyidai.api.service.repay;

import com.alibaba.fastjson.JSONObject;
import com.mobanker.shanyidai.api.business.common.CommonBusiness;
import com.mobanker.shanyidai.api.business.redis.RedisBusiness;
import com.mobanker.shanyidai.api.business.repay.RepayBusiness;
import com.mobanker.shanyidai.api.common.annotation.BusinessFlowAnnotation;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.constant.ZkConfigConstant;
import com.mobanker.shanyidai.api.common.enums.RedisKeyEnum;
import com.mobanker.shanyidai.api.common.enums.StatusEnum;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.dto.borrow.BorrowInfoParam;
import com.mobanker.shanyidai.api.dto.borrow.BorrowRepay;
import com.mobanker.shanyidai.api.dto.borrow.OrderDetail;
import com.mobanker.shanyidai.api.dto.borrow.QlBorrowing;
import com.mobanker.shanyidai.api.dto.repay.OtherRepayParam;
import com.mobanker.shanyidai.api.dto.repay.PayBackInfo;
import com.mobanker.shanyidai.api.dto.repay.PayResult;
import com.mobanker.shanyidai.api.dto.repay.RepayParam;
import com.mobanker.shanyidai.api.enums.BorrowStatusEnum;
import com.mobanker.shanyidai.api.enums.PayResultStatus;
import com.mobanker.shanyidai.api.enums.PayTypeEnum;
import com.mobanker.shanyidai.api.enums.RepayTypeEnum;
import com.mobanker.shanyidai.api.service.borrow.BorrowService;
import com.mobanker.shanyidai.api.service.repay.util.RepayBizConvert;
import com.mobanker.shanyidai.esb.common.logger.LogManager;
import com.mobanker.shanyidai.esb.common.logger.Logger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @desc:
 * @author: Richard Core
 * @create time: 2017/1/10 14:19
 */
@Service
@BusinessFlowAnnotation
public class RepayServiceImpl implements RepayService {
    public static final Logger logger = LogManager.getSlf4jLogger(RepayServiceImpl.class);
    @Resource
    private RepayBusiness repayBusiness;
    @Resource
    private BorrowService borrowService;
    @Resource
    private RedisBusiness redisBusiness;
    @Resource
    private CommonBusiness commonBusiness;

    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.repay.PayBackInfo
     * @description 还款信息
     * @author Richard Core
     * @time 2017/1/11 14:10
     * @method getBorrowRepayDetail
     */
    @Override
    public PayBackInfo getBorrowRepayDetail(SydRequest request) {
        //如果未登录
        if (request == null || request.getUserId() == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        String borrowNid = JSONObject.parseObject(request.getContent()).getString("borrowNid");
        if (StringUtils.isBlank(borrowNid)) {
            throw new SydException(ReturnCode.ERROR_BORROW_NID_NULL);
        }
        //查询还款信息
        PayBackInfo repayDetail = getRepayDetail(borrowNid, request);
        return repayDetail;
    }


    /**
     * @param param
     * @return com.mobanker.shanyidai.api.dto.borrow.BorrowRepay
     * @description 根据用户ID、订单号查询对应的还款详情
     * @author hantongyang
     * @time 2017/1/15 10:24
     * @method getRepayInfo
     */
    private BorrowRepay getRepayInfo(BorrowInfoParam param) {
        if (param == null) {
            throw new SydException(ReturnCode.PAY_BACK_ERROR);
        }
        if (param.getUserId() == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        if (StringUtils.isBlank(param.getBorrowNid())) {
            throw new SydException(ReturnCode.ORDER_ID_NULL);
        }
        //获取还款信息
        List<BorrowRepay> repayList = repayBusiness.getRepayInfo(param);
        if (repayList == null || repayList.isEmpty()) {
            return null;
        }
        BorrowRepay borrowRepay = repayList.get(0);
        if (borrowRepay == null) {
            return null;
        }
        //获取剩余还款天数
        if (!StatusEnum.VALID.getStatus().equals(borrowRepay.getStatus()) &&
                borrowRepay.getLateDays() <= 0) {
            Integer remainDay = Integer.valueOf(String.valueOf(((borrowRepay.getRepayTime() - System.currentTimeMillis()) / 1000 / 60 / 60 / 24)));
            borrowRepay.setRemainDays(remainDay);
        }
        return borrowRepay;
    }

    /**
     * @param borrowNid
     * @param request
     * @return com.mobanker.shanyidai.api.dto.repay.PayBackInfo
     * @description 我要还款
     * @author hantongyang
     * @time 2017/1/15 10:29
     * @method getRepayDetail
     */
    @Override
    public PayBackInfo getRepayDetail(String borrowNid, SydRequest request) {
        if (StringUtils.isBlank(borrowNid)) {
            throw new SydException(ReturnCode.ERROR_BORROW_NID_NULL);
        }
        //获取还款信息
        PayBackInfo payBackInfo = repayBusiness.getBorrowRepayDetail(borrowNid, request);
        return payBackInfo;
    }

    /**
     * @param request
     * @return java.lang.Object
     * @description 还款接口（一键还款）
     * @author Richard Core
     * @time 2017/1/17 16:29
     * @method addBillsAndPeriodRepay
     */
    @Override
    public PayResult addOnekeyRepay(SydRequest request) {
        //1 参数验证
        RepayParam repayParam = BeanHelper.parseJson(request.getContent(), RepayParam.class);
        if (repayParam == null) {
            throw new SydException(ReturnCode.REPAY_PARAM_ERROR.getCode(), "还款参数为空");
        }
        BeanHelper.packageBean(request, repayParam);
        repayParam.setRepayUid(RepayTypeEnum.ONE_KEy.getRepayType());
        return addSingleRepay(request, repayParam);
    }

    /**
     * @param request
     * @return java.lang.Object
     * @description 还款接口（微信支付）
     * @author Richard Core
     * @time 2017/1/17 16:29
     * @method addWechatRepay
     */
    @Override
    public PayResult addWechatRepay(SydRequest request) {
        //1 参数验证
        RepayParam repayParam = BeanHelper.parseJson(request.getContent(), RepayParam.class);
        if (repayParam == null) {
            throw new SydException(ReturnCode.REPAY_PARAM_ERROR.getCode(), "还款参数为空");
        }
        BeanHelper.packageBean(request, repayParam);
        repayParam.setRepayUid(RepayTypeEnum.WECHAT.getRepayType());
        return addSingleRepay(request, repayParam);
    }

    /**
     * @param request
     * @param repayParam
     * @return java.lang.Object
     * @description 单期还款接口（一键还款，微信支付）
     * @author Richard Core
     * @time 2017/1/22 10:50
     * @method addSingleRepay
     */
    public PayResult addSingleRepay(SydRequest request, RepayParam repayParam) {
        if (repayParam == null) {
            throw new SydException(ReturnCode.REPAY_PARAM_ERROR.getCode(), "还款参数为空");
        }
        Long userId = repayParam.getUserId();
        if (userId == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        //检查是否已选还款方式
        if (StringUtils.isBlank(repayParam.getRepayUid())) {
            throw new SydException(ReturnCode.REPAY_TYPE_NULL);
        }
        RepayTypeEnum repayTypeEnum = RepayTypeEnum.getInstance(repayParam.getRepayUid());
        //2 检查是否有借款单未还 参数userId
        QlBorrowing qlBorrowing = borrowService.getQlBorrowing(request);
        if (qlBorrowing == null) {
            throw new SydException(ReturnCode.TO_REPAY_ORDER_ERROR);
        }
        if (!BorrowStatusEnum.LOAN_SUCCESS.getStatusCode().equals(qlBorrowing.getStatus())) {
            throw new SydException(ReturnCode.TO_REPAY_ORDER_ERROR);
        }
        repayParam.setBorrowNid(qlBorrowing.getBorrowNid());
        //3 验证借款信息 一键还款需要查询入账银行卡号
        switch (repayTypeEnum) {
            case ONE_KEy:
                OrderDetail orderDetail = borrowService.getOrderDetail(request, qlBorrowing.getBorrowNid());
                if (orderDetail == null || StringUtils.isBlank(orderDetail.getBankCard())) {
                    throw new SydException(ReturnCode.ERROR_BORROW_GET_INFO);
                }
                repayParam.setDebitcardNum(orderDetail.getBankCard());
        }
        //4 支付
        //4.1 防重复点击 将用户userId和订单号缓存到redis中，验证是否
        PayBackInfo repayFromRedis = getRepayFromRedis(userId);
        if (repayFromRedis != null) {
            throw new SydException(ReturnCode.REPEAT_PAY_ERROR);
        }
        //4.2 记录userId 防止下次重复提交
        saveRepay2Redis(userId);
        //4.3 根据支付方式支付
        PayResult result = null;
        try {
            result = repayBusiness.addBillsAndPeriodRepay(repayParam);
        } catch (Exception e) {
            logger.warn("还款失败，清除防重复缓存信息,userId=" + userId);
            //4.4 还款失败，清除防重复缓存信息
            deleteRepayFromRedis(userId);
            throw e;
        }

        return result;
    }

    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.repay.PayBackInfo
     * @description 是否能还款
     * @author Richard Core
     * @time 2017/1/22 11:37
     * @method canRepay
     */
    @Override
    public PayBackInfo canRepay(SydRequest request) {
        //是否在前隆有借款，是否是闪宜贷的借款
        QlBorrowing qlBorrowing = borrowService.getQlBorrowing(request);
        //是否是待还款（下款成功状态）
        if (qlBorrowing == null ||
                !BorrowStatusEnum.LOAN_SUCCESS.getStatusCode().equals(
                        String.valueOf(qlBorrowing.getStatus()))) {
            throw new SydException(ReturnCode.CAN_REPAY_ORDER_STATUS);
        }
        //查询还款信息
        PayBackInfo repayDetail = getRepayDetail(qlBorrowing.getBorrowNid(), request);
        if (repayDetail == null) {
            throw new SydException(ReturnCode.GET_REPAYINFO_FAILED);
        }
        if (!StatusEnum.INVALID.getStatus().equals(repayDetail.getStatus())) {
            throw new SydException(ReturnCode.REPAY_ALREADY);
        }
        //验证是否有付款请求正在处理
        PayResult repayStatus = getRepayStatus(qlBorrowing.getBorrowNid());
        if (repayStatus == null) {
            return repayDetail;
        }
        if (PayResultStatus.PAY_SUCCESS.getStatusCode().equals(repayStatus.getStatusCode())) {
            throw new SydException(ReturnCode.REPEAT_PAY_ERROR);
        }
        if (PayResultStatus.PROCESSING.getStatusCode().equals(repayStatus.getStatusCode())) {
            throw new SydException(ReturnCode.REPEAT_PAY_ERROR);
        }

        return repayDetail;
    }

    /**
     * @param userId
     * @return void
     * @description 保存还款信息到缓存
     * @author Richard Core
     * @time 2017/1/18 15:21
     * @method saveRepay2Redis
     */
    private void saveRepay2Redis(Long userId) {
        Integer lastTime = null;
        try {
            lastTime = Integer.parseInt(commonBusiness.getCacheSysConfig(ZkConfigConstant.SYD_REPAY_AVOID_REPEAT.getZkValue()));
        } catch (Exception e) {
            lastTime = Integer.valueOf(ZkConfigConstant.SYD_REPAY_AVOID_REPEAT.getDefaultValue());
            logger.warn("从配置系统获取防重复还款缓存时间失败，将采用默认时间：" + lastTime);
        }
        String redisParam = JSONObject.toJSONString(RepayBizConvert.initRedisParam2AvoidRepeatPay(userId));
        try {
            redisBusiness.setValue(RedisKeyEnum.SYD_REPAY_AVOID_REPEAT.getValue() + userId, redisParam, lastTime);
        } catch (Exception e) {
            logger.warn("添加防重复还款信息到redis失败，用户编号：" + userId);
        }
    }

    /**
     * @param userId
     * @return void
     * @description 清除缓存中防重复提交还款信息
     * @author Richard Core
     * @time 2017/1/18 15:28
     * @method deleteRepayFromRedis
     */
    private void deleteRepayFromRedis(Long userId) {
        try {
            redisBusiness.delValue(RedisKeyEnum.SYD_REPAY_AVOID_REPEAT.getValue() + userId);
        } catch (Exception e) {
            logger.warn("清除防重复还款信息到redis失败，用户编号：" + userId);
        }
    }

    /**
     * @param userId
     * @return void
     * @description 获取缓存中防重复提交还款信息
     * @author Richard Core
     * @time 2017/1/18 15:28
     * @method deleteRepayFromRedis
     */
    private PayBackInfo getRepayFromRedis(Long userId) {
        try {
            String value = redisBusiness.getValue(RedisKeyEnum.SYD_REPAY_AVOID_REPEAT.getValue() + userId);
            PayBackInfo payBackInfo = BeanHelper.parseJson(value, PayBackInfo.class);
            return payBackInfo;
        } catch (Exception e) {
            logger.warn("获取防重复还款信息到redis失败，用户编号：" + userId);
        }
        return null;
    }

    /**
     * @param request
     * @return java.lang.Object
     * @description 百度钱包支付
     * @author Richard Core
     * @time 2017/1/23 10:09
     * @method addBaiduPay
     */
    @Override
    public PayResult addBaiduPay(SydRequest request) {
        OtherRepayParam otherRepayParam = BeanHelper.parseJson(request.getContent(), OtherRepayParam.class);
        if (otherRepayParam == null) {
            throw new SydException(ReturnCode.REPAY_PARAM_ERROR.getCode(), "还款参数为空");
        }
        BeanHelper.packageBean(request,otherRepayParam);
        otherRepayParam.setRepayUid(RepayTypeEnum.BAIDU_YIBAO.getRepayType());
        otherRepayParam.setPayType(PayTypeEnum.BAIDU.getType());
        return addBillsAndOtherRepay(request, otherRepayParam);
    }

    /**
     * @param request
     * @return java.lang.Object
     * @description 还款（百度钱包，易宝支付）
     * @author Richard Core
     * @time 2017/1/17 16:03
     * @method addBillsAndOtherRepay
     */
    private PayResult addBillsAndOtherRepay(SydRequest request, OtherRepayParam otherRepayParam) {
        if (otherRepayParam == null) {
            throw new SydException(ReturnCode.REPAY_PARAM_ERROR.getCode(), "还款参数为空");
        }
        Long userId = otherRepayParam.getUserId();
        if (userId == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        //检查是否已选还款方式 支付方式不能为空 支付类型不能为空
        if (StringUtils.isBlank(otherRepayParam.getRepayUid())) {
            throw new SydException(ReturnCode.REPAY_TYPE_NULL);
        }
        if (StringUtils.isBlank(otherRepayParam.getPayType())) {
            throw new SydException(ReturnCode.REPAY_TYPE_NULL.getCode(),"支付类型为空");
        }
        RepayTypeEnum repayTypeEnum = RepayTypeEnum.getInstance(otherRepayParam.getRepayUid());
        //2 检查是否有借款单未还 参数userId
//        QlBorrowing qlBorrowing = borrowService.getQlBorrowing(request);
//        if (qlBorrowing == null || !BorrowStatusEnum.LOAN_SUCCESS.getStatusCode().equals(qlBorrowing.getStatus())) {
//            throw new SydException(ReturnCode.TO_REPAY_ORDER_ERROR);
//        }
//        //3 验证借款信息 查询入账银行卡号
//        OrderDetail orderDetail = borrowService.getOrderDetail(request, qlBorrowing.getBorrowNid());
//        if (orderDetail == null || StringUtils.isBlank(orderDetail.getBankCard())) {
//            throw new SydException(ReturnCode.ERROR_BORROW_GET_INFO);
//        }
//        otherRepayParam.setDebitcardNum(orderDetail.getBankCard());
//        otherRepayParam.setBorrowNid(qlBorrowing.getBorrowNid());

        //4 支付
        //4.1 防重复点击 将用户userId和订单号缓存到redis中，验证是否
        PayBackInfo repayFromRedis = getRepayFromRedis(userId);
        if (repayFromRedis != null) {
            throw new SydException(ReturnCode.REPEAT_PAY_ERROR);
        }
        //4.2 记录userId 防止下次重复提交
        saveRepay2Redis(userId);
        //4.3 根据支付方式支付
        PayResult result = null;
        try {
            result = repayBusiness.addBillsAndOtherRepay(otherRepayParam);
        } catch (Exception e) {
            logger.warn("还款失败，清除防重复缓存信息,userId=" + userId);
            //4.4 还款失败，清除防重复缓存信息
            deleteRepayFromRedis(userId);
            throw e;
        }

        return result;
    }

    /**
     * @param borrowNid
     * @return com.mobanker.shanyidai.api.dto.repay.PayResult
     * @description 获取还款状态
     * @author Richard Core
     * @time 2017/1/23 11:50
     * @method getRepayStatus
     */
    @Override
    public PayResult getRepayStatus(String borrowNid) {
        PayResult repayStatus = repayBusiness.getRepayStatus(borrowNid, null);
        return repayStatus;
    }
}
