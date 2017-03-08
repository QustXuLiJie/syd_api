package com.mobanker.shanyidai.api.dto.borrow;

import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hantongyang
 * @description
 * @time 2017/1/19 17:54
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class BorrowOrderDto extends Entity{
    //用户相关信息，不用传参
    private Long userId; //用户ID
    private String userName; //真实姓名
    private String phone; //手机号
    private String mobileInfo; //用户手机信息
    private String createUser;
    private String updateUser;
    //产品模板相关信息
    private String productId; //产品模板ID
    private BigDecimal borrowRate; //借款利率
    private BigDecimal borrowLimit; //额度
    private String chargesRule; //收费规则，前置、后置
    private BigDecimal periodFeeApr; //固定费用
    private Integer minDays; //最小逾期天数
    private Integer maxDays; //最大逾期天数
    private BigDecimal lateReminder; //逾期滞纳金
    private BigDecimal lateFee; //逾期费率
    //借款单相关信息
    private Long borrowId; //流水单号
    private String borrowNid; //借款单号
    private BigDecimal borrowAmount; //借款金额 必填
    private Integer borrowDays; //借款时间 必填
    private Date borrowTime; //借款时间 必填
    private String captcha; //验证码  必填
    //银行信息
    private String bankCard; //入账银行 必填
    private String bankName; //银行名称
    private Long provinceId; //省编码
    private Long cityId; //市编码
    private String bankBranchName; //银行卡开户行名称
    //系统信息
    private String ip;     //客户端ip 建议服务端自己获取
    private String channel;//渠道（app/wechat/web/H5等）
    private String product;//产品（shoujidai/Uzone/shanyidai等）
    private String version; //版本（app版本）
    private String userCookie; //用户Cookie
    private String code; //用户sessionCode
}
