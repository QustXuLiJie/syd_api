package com.mobanker.shanyidai.api.dto.borrow;

import com.mobanker.shanyidai.api.common.dto.Entity;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hantongyang
 * @description 借款单参数
 * @time 2017/1/16 17:16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class BorrowOrderParam extends SydRequest {
    private String mobileInfo; //用户手机信息 选填
    //借款单相关信息
    private BigDecimal borrowAmount; //借款金额 必填
    private Integer borrowDays; //借款时间 必填
    private Date borrowTime; //借款时间 必填
    private String captcha; //验证码  必填
    //银行信息
    private String bankCard; //入账银行 必填
}
