package com.mobanker.shanyidai.api.dto.index;

import com.mobanker.shanyidai.api.common.dto.Entity;
import com.mobanker.shanyidai.api.dto.borrow.BorrowInfo;
import com.mobanker.shanyidai.api.dto.borrow.BorrowProcess;
import com.mobanker.shanyidai.api.dto.borrow.BorrowRepay;
import com.mobanker.shanyidai.api.dto.product.ProductTemp;
import com.mobanker.shanyidai.api.dto.repay.PayBackInfo;
import com.mobanker.shanyidai.api.dto.user.UserCompleteness;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author hantongyang
 * @description 首页实体
 * @time 2017/1/10 14:32
 */
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true)
public class Index extends Entity {

    private ProductTemp productTemp; //产品模板信息
    private UserCompleteness userCompleteness; //资料认证进度信息
    private BorrowInfo borrowInfo; //借款详情，包含还款信息、逾期信息
    private PayBackInfo payBack; //还款详情
    private int borrowCount; //借款次数
    private String borrowStatus; //借款单状态 1：表示借款信息，对应的原始状态有0、1、2； 2：表示还款信息，对应的原始状态有3； -1表示其余状态
    private List<BorrowProcess> borrowProcesses; //借款单进度
    private int bankCardAmount; //获取银行卡个数

}
