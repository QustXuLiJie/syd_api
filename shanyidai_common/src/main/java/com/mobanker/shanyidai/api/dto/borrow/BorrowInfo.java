package com.mobanker.shanyidai.api.dto.borrow;

import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hantongyang
 * @description
 * @time 2017/1/12 13:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class BorrowInfo extends Entity {

    private Long userId; //用户ID
    private String borrowNid; //借款单号
    private String labelShort; //标签(多个标签逗号分隔)
    private Integer status; //借款状态0借款中，1审核通过，2审核失败，3放款成功，4放款失败，5还款成功(暂时单期没有)
    private String verifyPhase; //审核阶段：初审中，终审中，复核中，电核中，放款中，还款中等
    private Integer borrowType; //借款类型：6、手机贷单期 7、手机贷小额 8、手机贷分期 12、应花分期
    private Integer period; //借款期数
    private Integer periodDays; //每期天数
    private Float amount; //借款金额
    private Long borrowSuccessTime; //借款成功时间
    private Float paidInAmount; //到账金额
    private Float poundage; //应收手续费
    private Long addtime; //申请时间
    private Long finishTime; //结案时间
    private Integer reasonFall; //审核失败原因ID
    private String reasonFallName; //审核失败原因内容
    private String addProduct; //产品 新增：yhfq
    private String addChannel; //渠道 新增：3c

    private String statusTitle;//状态标题
}
