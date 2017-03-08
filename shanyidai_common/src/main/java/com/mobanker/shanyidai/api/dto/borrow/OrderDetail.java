package com.mobanker.shanyidai.api.dto.borrow;

import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @desc: 借还款详情
 * @author: Richard Core
 * @create time: 2017/1/15 14:17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class OrderDetail extends Entity {

    private Long userId;// 用户ID
    private String borrowNid;  // 借款单号
    //----借款信息 borrowHistory--------
    private Integer borrowStatus; //借款状态	status（见下面备注）
    private String borrowStatusDesc; //借款状态描述
    private Float amount;//借款金额	amount
    private Integer borrowDays;// 借款天数 period*periodDays
    private Float poundage;// 手续费		poundage
    private String bankCard;//入账银行卡		无
    private String bankName;//银行卡发卡行	无

    //----还款信息 borrowRepay--------
    private String repayStatus;//还款状态	status 当前状态0未还款，1已还款（见接口说明之“List还款详情”字段，以此判断订单是否还清）
    private BigDecimal repayYesAmount;//已还金额
    private BigDecimal repayAmount; // 应还款金额
    private BigDecimal lateReminder; //	lateReminder	Bigdecimal	N	滞纳金
    private BigDecimal lateInterest;//逾期费 	lateInterest	Bigdecimal	N	逾期罚息
    private Integer lateDays; //逾期天数	lateDays	Integer	N	逾期天数
    private BigDecimal exemptionAmount;//减免金额	exemptionAmount
    private Date repayTime;//还款日 	repayTime	Long	N	应还时间

    //----处理进度 borrowHistory--------
    private Date addtime;//借款时间	addtime  申请成功时间
    private Date finishTime; //审核（成功/失败）时间	finishTime // 结案时间
    private Date borrowSuccessTime; //下款成功时间	borrowSuccessTime // 借款成功时间
    private Date repayYesTime; //还款完成时间	 //	repayYesTime	Long	Y	实还时间

    private Integer remainDays;//剩余还款天数	repayTime-服务器当前日期（如果逾期天数不为0，无需计算）
    private List<BorrowProcess> processList;//借款进度展示
}
