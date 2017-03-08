package com.mobanker.shanyidai.api.dto.borrow;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class BorrowHistory extends SydRequest {
	
	// 用户ID
	private Long userId;
	// 借款单号
	private String borrowNid;
	// 状态
	private Integer status;
	// 审核阶段 初审中，终审中，复核中，电核中，放款中，还款中等
	private String verifyPhase;
	// 借款类型
	private Integer borrowType;
	// 借款期数
	private Integer period;
	// 每期天数
	private Integer periodDays;
	// 借款金额
	private Float amount;
	// 借款成功时间
	private Long borrowSuccessTime;
	// 到账金额
	private Float paidInAmount;
	// 应收手续费
	private Float poundage;
	// 申请时间
	private Long addtime;
	// 结案时间
	private Long finishTime;
	// 拒绝原因ID
	private Integer reasonFall;
	// 拒绝原因名称
	private String reasonFallName;
	// 产品
	private String addProduct;
	// 来源
	private String addChannel;
	// 标签
	private String labelShort;
	// 审核备注
	private String verifyRemark;
	// 结案人ID
	private Long verifyUserid;
	// 结案人
	private String verifyUsername;
	// 冻结状态
	private Integer freezeStatus;
	// APP版本
	private String appVersion;
	// 还款详情
	private List<BorrowRepay> repayList;
	// 还款明细详情
	private List<RepayDetail> repayDetails;
	// 逾期天数
	private Integer lateDays;
	// 实际还款金额
	private Float repayYesAmount;
	// 应还款金额
	private Float repayAmount;
	// 预计还款时间
	private Long repayTime;
	// 还款时间
	private Long repayYesTime;
	//客服失败原因,字符串，逗号间隔
	private String customerReasonFall;
	//客服失败原因名称
	private String customerReasonFallName;
}
