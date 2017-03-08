package com.mobanker.shanyidai.api.service.borrow.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mobanker.shanyidai.api.common.constant.AppConstants;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.enums.SmsTypeEnum;
import com.mobanker.shanyidai.api.common.enums.StatusEnum;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.logger.LogManager;
import com.mobanker.shanyidai.api.common.logger.Logger;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.common.tool.CommonUtil;
import com.mobanker.shanyidai.api.dto.borrow.*;
import com.mobanker.shanyidai.api.dto.message.ValidateSmsCaptcha;
import com.mobanker.shanyidai.api.dto.product.ProductTemp;
import com.mobanker.shanyidai.api.dto.repay.PayBackInfo;
import com.mobanker.shanyidai.api.dto.user.BankCard;
import com.mobanker.shanyidai.api.dto.user.UserBasicInfoRsp;
import com.mobanker.shanyidai.api.enums.BorrowProcessingEnum;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @desc:
 * @author: Richard Core
 * @create time: 2017/1/12 15:26
 */
public class BorrowBusinessConvert {
    public static final Logger logger = LogManager.getSlf4jLogger(BorrowBusinessConvert.class);

    /**
     * @param borrowHistory
     * @param statusEnum
     * @return java.util.List<com.mobanker.shanyidai.api.dto.borrow.BorrowProcess>
     * @description 封装借款进度
     * @author Richard Core
     * @time 2017/1/14 17:55
     * @method getBorrowProcess
     */
    public static List<BorrowProcess> getBorrowProcess(BorrowInfo borrowHistory, BorrowProcessingEnum statusEnum) {
        return getBorrowProcess(borrowHistory, statusEnum, null, true);
    }


    /**
     * @param borrowInfo
     * @param statusEnum
     * @param resultList
     * @param addFix
     * @return java.util.List<com.mobanker.shanyidai.api.dto.borrow.BorrowProcess>
     * @description 封装借款进度
     * @author Richard Core
     * @time 2017/1/14 17:55
     * @method getBorrowProcess
     */
    private static List<BorrowProcess> getBorrowProcess(BorrowInfo borrowInfo, BorrowProcessingEnum statusEnum, List<BorrowProcess> resultList, boolean addFix) {
        if (resultList == null) {
            resultList = new ArrayList<BorrowProcess>();
        }
        if (statusEnum == null || borrowInfo == null) {
            return resultList;
        }
        switch (statusEnum) {
            case REPAY_FAILED://放款成功 支付失败
                getFixProcess(resultList, BorrowProcessingEnum.REPAY_FAILED);
                //递归补充
                return getBorrowProcess(borrowInfo, BorrowProcessingEnum.LOAN_SUCCESS, resultList, false);
            case REPAY_PROCESSING://放款成功 支付正在处理
                getFixProcess(resultList, BorrowProcessingEnum.REPAY_PROCESSING);
                //递归补充
                return getBorrowProcess(borrowInfo, BorrowProcessingEnum.LOAN_SUCCESS, resultList, false);
            case REPAY_SUCCESS://放款成功，还款成功
            case REPAYMENT_SUCCESS://还款成功
                getRepaySuccessProcess(resultList, BorrowProcessingEnum.REPAYMENT_SUCCESS.getStatusDisplay(), borrowInfo.getFinishTime());
                //递归补充
                return getBorrowProcess(borrowInfo, BorrowProcessingEnum.LOAN_SUCCESS, resultList, false);
            case LOAN_SUCCESS://放款成功
                getLoanSuccessProcess(borrowInfo, resultList);
                //递归补充
                return getBorrowProcess(borrowInfo, BorrowProcessingEnum.AUDIT_SUCCESS, resultList, false);
            case LOAN_FAILED://放款失败
                getLeanFailProcess(borrowInfo, resultList);
                //递归补充
                return getBorrowProcess(borrowInfo, BorrowProcessingEnum.AUDIT_SUCCESS, resultList, false);
            case AUDIT_SUCCESS://审核通过
                getAuditSuccessProcess(borrowInfo, resultList, addFix);
                //递归补充
                return getBorrowProcess(borrowInfo, BorrowProcessingEnum.AUDITINT, resultList, false);
            case AUDIT_FAILED://审核失败
                getAuditFailProcess(borrowInfo, resultList);
                //递归补充
                return getBorrowProcess(borrowInfo, BorrowProcessingEnum.AUDITINT, resultList, false);
            case AUDITINT://
                getBorrowAuditInitProcess(borrowInfo, resultList, addFix);
                return resultList;
        }
        return resultList;
    }

    public static void main(String[] args) {
        BorrowInfo borrowInfo = new BorrowInfo();
        Date date = new Date();
        borrowInfo.setAmount(800f);
        borrowInfo.setAddtime(date.getTime());
        borrowInfo.setFinishTime(date.getTime());
        borrowInfo.setBorrowSuccessTime(date.getTime());
        List<BorrowProcess> borrowProcess = getBorrowProcess(borrowInfo, BorrowProcessingEnum.REPAY_PROCESSING);
        for (BorrowProcess borrowProces : borrowProcess) {
            logger.info(borrowProces + "\n");
        }
    }

    private static void getFixProcess(List<BorrowProcess> resultList, BorrowProcessingEnum processingEnum) {
        BorrowProcess leanSuccess = new BorrowProcess();
        leanSuccess.setStatusDesc(processingEnum.getFixMsg());
        resultList.add(leanSuccess);
    }

    /**
     * @param resultList
     * @param statusDisplay
     * @param repayYesTime
     * @return void
     * @description 封装还款成功进度信息
     * @author Richard Core
     * @time 2017/1/14 17:38
     * @method getRepaySuccessProcess
     */
    private static void getRepaySuccessProcess(List<BorrowProcess> resultList, String statusDisplay, Long repayYesTime) {
        BorrowProcess repaySuccess = new BorrowProcess();
        repaySuccess.setStatusDesc(statusDisplay);
        //todo 2017/1/14 还款成功时间
        if (repayYesTime != null) {
            Date repaySuccessDate = new Date(repayYesTime);
            repaySuccess.setOperateDate(repaySuccessDate);
        }
        resultList.add(repaySuccess);
    }

    /**
     * @param borrowInfo
     * @param resultList
     * @return void
     * @description 获取下款成功进度提示
     * @author Richard Core
     * @time 2017/1/14 17:42
     * @method getLoanSuccessProcess
     */
    private static void getLoanSuccessProcess(BorrowInfo borrowInfo, List<BorrowProcess> resultList) {
        BorrowProcess leanSuccess = new BorrowProcess();
        leanSuccess.setStatusDesc(BorrowProcessingEnum.LOAN_SUCCESS.getStatusDisplay());
        // 2017/1/14 下款时间
        if(borrowInfo.getBorrowSuccessTime() != null){
            Date leanSuccessDate = new Date(borrowInfo.getBorrowSuccessTime());
            leanSuccess.setOperateDate(leanSuccessDate);
        }
        resultList.add(leanSuccess);
    }

    /**
     * @param borrowInfo
     * @param resultList
     * @return void
     * @description 获取下款失败进度提示
     * @author Richard Core
     * @time 2017/1/14 17:42
     * @method getLeanFailProcess
     */
    private static void getLeanFailProcess(BorrowInfo borrowInfo, List<BorrowProcess> resultList) {
        BorrowProcess leanFail = new BorrowProcess();
        leanFail.setStatusDesc(BorrowProcessingEnum.LOAN_FAILED.getStatusDisplay());
        // TODO: 2017/1/14 下款时间暂无
        if(borrowInfo.getBorrowSuccessTime() != null){
            Date FailDate = new Date(borrowInfo.getBorrowSuccessTime());
            leanFail.setOperateDate(FailDate);
        }
        resultList.add(leanFail);
    }

    /**
     * @param borrowInfo
     * @param resultList
     * @return void
     * @description 获取审核成功进度提示
     * @author Richard Core
     * @time 2017/1/14 17:42
     * @method getAuditSuccessProcess
     */
    private static void getAuditSuccessProcess(BorrowInfo borrowInfo, List<BorrowProcess> resultList, boolean addFix) {
        if (addFix) {
            BorrowProcess auditSuccessFix = new BorrowProcess();
            auditSuccessFix.setStatusDesc(BorrowProcessingEnum.AUDIT_SUCCESS.getFixMsg());
            // TODO: 2017/1/14 入账卡号
            auditSuccessFix.setTips("您的借款预计会在3分钟内到账\n请留意借记卡");
            resultList.add(auditSuccessFix);
        }
        BorrowProcess auditSuccess = new BorrowProcess();
        auditSuccess.setStatusDesc(BorrowProcessingEnum.AUDIT_SUCCESS.getStatusDisplay());
        // 2017/1/14 审核时间
        if(borrowInfo.getFinishTime() != null){
            Date auditDate = new Date(borrowInfo.getFinishTime());
            auditSuccess.setOperateDate(auditDate);
        }
        resultList.add(auditSuccess);
    }

    /**
     * @param borrowInfo
     * @param resultList
     * @return void
     * @description 获取审核失败进度提示
     * @author Richard Core
     * @time 2017/1/14 17:42
     * @method getAuditFailProcess
     */
    private static void getAuditFailProcess(BorrowInfo borrowInfo, List<BorrowProcess> resultList) {
        BorrowProcess auditFail = new BorrowProcess();
        auditFail.setStatusDesc(BorrowProcessingEnum.AUDIT_FAILED.getStatusDisplay());
        auditFail.setTips("由于综合评分不足，暂无法借款。\n" +
                "可能原因：1.授权信息不真实   2.有不良逾期记录");
        // 2017/1/14 审核时间
        if(borrowInfo.getFinishTime() != null){
            Date auditFailDate = new Date(borrowInfo.getFinishTime());
            auditFail.setOperateDate(auditFailDate);
        }
        resultList.add(auditFail);
    }

    /**
     * @param borrowInfo
     * @param resultList
     * @return void
     * @description 获取下单成功进度提示
     * @author Richard Core
     * @time 2017/1/14 17:42
     * @method getBorrowAuditInitProcess
     */
    private static void getBorrowAuditInitProcess(BorrowInfo borrowInfo, List<BorrowProcess> resultList, boolean addFix) {
        if (addFix) {
            BorrowProcess auditFix = new BorrowProcess();
            auditFix.setStatusDesc(BorrowProcessingEnum.AUDITINT.getFixMsg());
            auditFix.setTips("预计会在5分钟内完成审核，请您耐心等待");
            resultList.add(auditFix);
        }
        BorrowProcess auditProcess = new BorrowProcess();
        auditProcess.setStatusDesc(BorrowProcessingEnum.AUDITINT.getStatusDisplay());
        if(borrowInfo.getAddtime() != null){
            Date addDate = new Date(borrowInfo.getAddtime());
            auditProcess.setOperateDate(addDate);
        }
        // TODO: 2017/1/14 天数处理 金额处理
        Integer periodDays = borrowInfo.getPeriodDays();
        if (periodDays == null) {
            periodDays = 15;
        }
        Float amount = borrowInfo.getAmount();
        auditProcess.setTips("申请借款" + amount + "元，期限" + periodDays + "天");
        resultList.add(auditProcess);
    }

    /**
     * @param info
     * @return com.mobanker.shanyidai.api.dto.borrow.BorrowHistory
     * @description 根据订单详情，生成订单历史详情DTO
     * @author hantongyang
     * @time 2017/1/14 17:42
     * @method initBorrowHistory
     */
    public static BorrowHistory initBorrowHistory(BorrowInfo info) {
        if (info == null) {
            return null;
        }
        BorrowHistory history = BeanHelper.cloneBean(info, BorrowHistory.class);
        return history;
    }

    /**
     * @param request
     * @param nid
     * @return com.mobanker.shanyidai.api.dto.borrow.BorrowInfoParam
     * @description 封装查询历史订单详情
     * @author hantongyang
     * @time 2017/1/14 19:08
     * @method initBorrowInfoParam
     */
    public static BorrowInfoParam initBorrowInfoParam(SydRequest request, String nid) {
        BorrowInfoParam param = new BorrowInfoParam();
        BeanHelper.packageBean(request, param);
        param.setBorrowNid(nid);
        return param;
    }


    /**
     * @param payBackInfo
     * @param orderDetail
     * @return com.mobanker.shanyidai.api.dto.borrow.OrderDetail
     * @description 还款信息封装
     * @author Richard Core
     * @time 2017/1/15 17:03
     * @method mapRepayDetail2OrderDetail
     */
    public static OrderDetail mapRepayDetail2OrderDetail(PayBackInfo payBackInfo, OrderDetail orderDetail) {
        if (payBackInfo == null) {
            return orderDetail;
        }
        orderDetail.setRepayStatus(String.valueOf(payBackInfo.getStatus()));//还款状态	status 当前状态0未还款，1已还款（见接口说明之“List还款详情”字段，以此判断订单是否还清）
        orderDetail.setRepayYesAmount(payBackInfo.getRepayYesAmount());//已还金额
        orderDetail.setRepayAmount(payBackInfo.getRepayMoney()); // 应还款金额
        orderDetail.setLateReminder(payBackInfo.getLateReminder()); //	lateReminder	Float	N	滞纳金
        orderDetail.setLateInterest(payBackInfo.getLateInterest());//逾期费 	lateInterest	Float	N	逾期罚息
        orderDetail.setLateDays(payBackInfo.getLateDays()); //逾期天数	lateDays	Integer	N	逾期天数
        orderDetail.setExemptionAmount(payBackInfo.getExemptionAmount());//减免金额	exemptionAmount
        orderDetail.setRepayTime(payBackInfo.getRepayTime());//还款日 	repayTime	Long	N	应还时间
        //获取剩余还款天数
        if (!StatusEnum.VALID.getStatus().equals(String.valueOf(payBackInfo.getStatus())) &&
                payBackInfo.getLateDays() <= 0) {
            Integer remainDay = Integer.valueOf(String.valueOf(((payBackInfo.getRepayTime().getTime() - System.currentTimeMillis()) / 1000 / 60 / 60 / 24)));
            orderDetail.setRemainDays(remainDay);
        }
        return orderDetail;
    }

    /**
     * @param borrowInfo
     * @param orderDetail
     * @return void
     * @description 将借款信息封装到订单详情中
     * @author Richard Core
     * @time 2017/1/15 17:03
     * @method mapBorrowInfo2OrderDetail
     */
    public static void mapBorrowInfo2OrderDetail(BorrowInfo borrowInfo, OrderDetail orderDetail) {
        BorrowProcessingEnum borrowStatusEnum = BorrowProcessingEnum.getByStatus(String.valueOf(borrowInfo.getStatus()));
        orderDetail.setBorrowStatus(Integer.valueOf(borrowStatusEnum.getStatusCode()));
        orderDetail.setBorrowStatusDesc(borrowStatusEnum.getStatusTitle());//标题显示
        if(borrowInfo.getAddtime() != null){
            orderDetail.setAddtime(new Date(borrowInfo.getAddtime()));//借款时间	addtime  申请成功时间
        }
        if(borrowInfo.getFinishTime() != null){
            orderDetail.setFinishTime(new Date(borrowInfo.getFinishTime())); //审核（成功/失败）时间	finishTime // 结案时间
        }
        if(borrowInfo.getBorrowSuccessTime() != null){
            orderDetail.setBorrowSuccessTime(new Date(borrowInfo.getBorrowSuccessTime())); //下款成功时间	borrowSuccessTime // 借款成功时间
        }
        if(borrowInfo.getFinishTime() != null){
            orderDetail.setRepayYesTime(new Date(borrowInfo.getFinishTime())); //还款完成时间	 //	repayYesTime	Long	Y	实还时间
        }
        //进度信息
        List<BorrowProcess> borrowProcess = BorrowBusinessConvert.getBorrowProcess(borrowInfo, borrowStatusEnum);
        orderDetail.setProcessList(borrowProcess);
    }

    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.borrow.BorrowOrderParam
     * @description 初始化借款单参数
     * @author hantongyang
     * @time 2017/1/19 16:49
     * @method initBorrowOrderParam4Content
     */
    public static BorrowOrderParam initBorrowOrderParam4Content(SydRequest request) {
        JSONObject json = JSONObject.parseObject(request.getContent());
        BorrowOrderParam param = BeanHelper.cloneBean(request, BorrowOrderParam.class);
        param.setBorrowTime(new Date());
        param.setBankCard(json.getString("bankCard"));
        param.setCaptcha(json.getString("captcha"));
        param.setBorrowAmount(json.getBigDecimal("borrowAmount"));
        param.setBorrowDays(json.getInteger("borrowDays"));
        param.setMobileInfo(json.getString("mobileInfo"));
        return param;
    }

    /**
     * @param param
     * @param productTemp
     * @return void
     * @description 验证借款单参数
     * @author hantongyang
     * @time 2017/1/16 20:24
     * @method checkBorrowOrderParam
     */
    public static void checkBorrowOrderParam(BorrowOrderParam param, ProductTemp productTemp) {
        //1 借款金额
        if (param.getBorrowAmount() == null || param.getBorrowAmount().doubleValue() <= 0) {
            throw new SydException(ReturnCode.ERROR_BORROW_AMOUNT);
        }
        //验证是否在产品模板的配置金额区间内
        if (productTemp.getMinLimitAmount() != null && productTemp.getMaxLimitAmount() != null) {
            if (productTemp.getMinLimitAmount().doubleValue() > param.getBorrowAmount().doubleValue()
                    || productTemp.getMaxLimitAmount().doubleValue() < param.getBorrowAmount().doubleValue()) {
                throw new SydException(ReturnCode.ERROR_BORROW_AMOUNT);
            }
        }
        //2 借款天数
        if (param.getBorrowDays() <= 0) {
            throw new SydException(ReturnCode.ERROR_BORROW_DAYS);
        }
        //验证是否在产品模板的配置天数区间内
        if (productTemp.getMinTimeLimit() != null && productTemp.getMaxTimeLimit() != null) {
            if (param.getBorrowDays() < productTemp.getMinTimeLimit()
                    || param.getBorrowDays() > productTemp.getMaxTimeLimit()) {
                throw new SydException(ReturnCode.ERROR_BORROW_DAYS);
            }
        }
        //3 入账银行 验证是否为空、是否都是数字
        if (!CommonUtil.isDigit(param.getBankCard())) {
            throw new SydException(ReturnCode.BANK_CARD_NULL);
        }
        //4 验证码
        if (StringUtils.isBlank(param.getCaptcha())) {
            throw new SydException(ReturnCode.ERROR_CAPTCHA_NULL);
        }
    }

    /**
     * @param phone
     * @param userId
     * @param captcha
     * @return com.mobanker.shanyidai.api.dto.message.ValidateSmsCaptcha
     * @description 封装验证码参数
     * @author hantongyang
     * @time 2017/1/16 21:19
     * @method initValidateSmsCaptcha
     */
    public static ValidateSmsCaptcha initValidateSmsCaptcha(String phone, Long userId, String captcha, SydRequest request) {
        //封装验证码参数
        ValidateSmsCaptcha smsBean = new ValidateSmsCaptcha();
        BeanHelper.packageBean(request, smsBean);
        smsBean.setPhone(phone);
        smsBean.setUserId(userId);
        smsBean.setCaptcha(captcha);
        smsBean.setCaptchaUse(SmsTypeEnum.borrow.getSmsType());
        return smsBean;
    }

    /**
     * @param bankCards
     * @param card
     * @return BankCard
     * @description 循环列表，验证银行卡号是否在列表中
     * @author hantongyang
     * @time 2017/1/16 21:16
     * @method checkBank
     */
    public static BankCard checkAndCallBank(List<BankCard> bankCards, String card) {
        boolean checkBank = true;
        BankCard bean = new BankCard();
        for (BankCard bankCard : bankCards) {
            if (bankCard == null) {
                continue;
            }
            if (bankCard.getBankCardNo().equals(card)) {
                checkBank = false;
                break;
            }
        }
        if (checkBank) {
            throw new SydException(ReturnCode.BANKNAME_CARDNUM_NOT_EQUALS);
        }
        return bean;
    }

    /**
     * @param userId
     * @return com.mobanker.shanyidai.api.dto.borrow.BorrowOrderParam
     * @description 封装缓存中保存的借款单对象
     * @author hantongyang
     * @time 2017/1/17 9:59
     * @method initRedisBorrowOrderParam
     */
    public static BorrowOrderParam initRedisBorrowOrderParam(Long userId) {
        BorrowOrderParam param = new BorrowOrderParam();
        param.setUserId(userId);
        param.setBorrowTime(new Date());
        return param;
    }

    /**
     * @param param
     * @return void
     * @description 验证借款单回调参数
     * @author hantongyang
     * @time 2017/1/19 11:13
     * @method checkBorrowCallBackParam
     */
    public static void checkBorrowCallBackParam(BorrowCallBackParam param) {
        if (param == null) {
            throw new SydException(ReturnCode.ERROR_BORROW_CALL_BACK_PARAM);
        }
        if (StringUtils.isBlank(param.getOrderNid())) {
            throw new SydException(ReturnCode.ERROR_BORROW_NID_NULL);
        }
        if (StringUtils.isBlank(param.getStatus())) {
            throw new SydException(ReturnCode.ERROR_BORROW_CALL_BACK_PARAM);
        }
    }

    /**
     * @param param
     * @param productTemp
     * @param user
     * @param bankCard
     * @param request
     * @return com.mobanker.shanyidai.api.dto.borrow.BorrowOrderDto
     * @description 初始化借款单DTO实体
     * @author hantongyang
     * @time 2017/1/19 18:13
     * @method initBorrowOrderDto
     */
    public static BorrowOrderDto initBorrowOrderDto(BorrowOrderParam param, ProductTemp productTemp, UserBasicInfoRsp user, BankCard bankCard, SydRequest request) {
        BorrowOrderDto dto = BeanHelper.cloneBean(param, BorrowOrderDto.class);
        dto.setProductId(productTemp.getId());
        dto.setUserId(request.getUserId());
        dto.setPhone(user.getPhone());
        dto.setUserName(user.getRealname());
        dto.setCreateUser(request.getUserId().toString());
        dto.setUpdateUser(request.getUserId().toString());
        //添加用户基础信息
        dto.setCode(JSON.parseObject(request.getContent()).getString(AppConstants.DICT.REQCODE));
        //添加产品模板参数
        initProductTemp(dto, productTemp);
        //添加银行卡参数
        initBank(dto, bankCard);
        return dto;
    }

    /**
     * @param dto
     * @param productTemp
     * @return void
     * @description 初始化产品模板参数
     * @author hantongyang
     * @time 2017/1/22 14:48
     * @method initProductTemp
     */
    private static void initProductTemp(BorrowOrderDto dto, ProductTemp productTemp) {
        if (productTemp != null) {
            dto.setProductId(productTemp.getId());
            dto.setBorrowLimit(productTemp.getMaxLimitAmount());
            dto.setBorrowRate(productTemp.getPeriodFee());
            dto.setChargesRule(productTemp.getChargesRule());
            dto.setMinDays(productTemp.getMinTimeLimit());
            dto.setMaxDays(productTemp.getMaxTimeLimit());
            dto.setLateReminder(productTemp.getLateReminder());
            dto.setLateFee(productTemp.getLateFee());
            //借款费用=借款金额*借款时间*借款利息
            if (dto.getBorrowAmount() != null && dto.getBorrowDays() != null && productTemp.getPeriodFee() != null) {
                double feeApr = dto.getBorrowAmount().doubleValue() * dto.getBorrowDays().doubleValue() * productTemp.getPeriodFee().doubleValue();
                dto.setPeriodFeeApr(new BigDecimal(feeApr));
            }
        }
    }

    /**
     * @param dto
     * @param bankCard
     * @return void
     * @description 初始化银行卡参数
     * @author hantongyang
     * @time 2017/1/22 14:48
     * @method initBank
     */
    private static void initBank(BorrowOrderDto dto, BankCard bankCard) {
        if (bankCard != null) {
            dto.setBankName(bankCard.getBankName());
            dto.setBankBranchName(bankCard.getBankName());
            dto.setProvinceId(null);
            dto.setCityId(null);
        }
    }
}
