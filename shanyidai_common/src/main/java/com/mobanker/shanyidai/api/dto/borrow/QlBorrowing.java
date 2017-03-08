package com.mobanker.shanyidai.api.dto.borrow;

import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hantongyang
 * @description 是否在前隆借款中返回DTO
 * @time 2017/1/10 15:42
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class QlBorrowing extends Entity {

    private String borrowNid; //借款单号
    private Long addtime; //申请时间
    private int status; //借款状态：0审核中 1审核通过 2审核失败 3放款成功 4放款失败 5还款成功
    private Integer borrowType; //借款类型：6手机贷单期 7闪电贷 8现金分期 9同程 10大额贷 11应花分期 51 u族分期
    private String addProduct; //产品
    private String addChannel; //渠道
}
