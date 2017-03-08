package com.mobanker.shanyidai.api.dto.repay;

import com.alibaba.fastjson.annotation.JSONField;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @desc: 还款信息
 * @author: Richard Core
 * @create time: 2017/1/10 11:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class PayBackInfo extends SydRequest {
    private Long userId;//用户id
    private String borrowNid;//借款单Id
    private String payBackNid;//还款单id
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date repayTime;//应还时间
    private int lateDays;//逾期天数
    private BigDecimal lateInterest;//逾期利息
    private BigDecimal lateReminder;//逾期催收费
    private BigDecimal repayMoney;//应还总金额
    private int periodDays;//借款天数
    private BigDecimal borrowFee;//费用
    private BigDecimal exemptionAmount;//总减免金额
    private int status;//状态
    private Date repayYesTime;//实际还款时间
    private BigDecimal repayYesAmount;//实际还款金额
    private BigDecimal repayCapital;//本期应还本金
    private Date forceTime;//强扣时间
    private int repayPeriod;//期数
    private BigDecimal restRepayMoney;//剩余应还金额

}
