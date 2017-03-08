package com.mobanker.shanyidai.api.dto.product;

import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author hantongyang
 * @description
 * @time 2016/12/30 14:04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class ProductTemp extends Entity {
    private String id;
    private Integer period; //分期类型
    private String merchant; //商户
    private String channel; //渠道
    private String appVersion; //当前版本号
    private BigDecimal periodFee; //服务费率
    private BigDecimal accrualFee; //利息费率
    private BigDecimal lateReminder; //逾期滞纳金
    private BigDecimal lateFee; //逾期费率
    private String limitAmount; //额度范围
    private String chargesRule; //收费规则
    private BigDecimal periodFeeApr; //固定费用
    private Integer minDays; //最小逾期天数
    private Integer maxDays; //最大逾期天数

    private BigDecimal minLimitAmount; //最小额度范围
    private BigDecimal maxLimitAmount; //最大额度范围
    private Integer minTimeLimit; //最短借款期限
    private Integer maxTimeLimit; //最长借款期限

}
