package com.mobanker.shanyidai.api.dto.repay;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Description:还款参数
 *
 * @author R.Core
 *         Create date: 2017年1月17日
 * @version v1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class RepayParam extends SydRequest {
//    private String userId;//用户id 采用sydRequest中的userId
    private String repayUid;//还款渠道
//    private String type;//还款产品  采用sydRequest中的product
    //    private String channel;//还款产品渠道 采用sydRequest中的channel
    private String debitcardNum;//还款银行卡号
    private String frontUrl;//回调接口地址(暂未使用)
    private String period;//还款期数
//    private String spbillCreateIp;//APP和网页支付提交用户端ip 采用sydRequest中的ip
    private String openId;//用户在商户appid下的唯一标识

    private String borrowNid;//借款单Id  app传入，验证参数使用
}
