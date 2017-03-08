package com.mobanker.shanyidai.api.dto.repay;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Description:还款参数 (易宝，百度钱包)
 *
 * @author R.Core
 *         Create date: 2017年1月17日
 * @version v1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class OtherRepayParam extends SydRequest {
    private Long userId;//用户id
    private String repayUid;//还款渠道
    private String type;//还款产品
//    private String channel;//还款产品渠道 entity中取
//    private String userIp	;//String	Y	易宝不能为空,百度可为空用户ip;  entity中取
    private String debitcardNum;//还款银行卡号
    private String period;//还款期数
    private String payType;//支付类型 baidu  yibao
    private String requestId;//易宝支付不能为空
    private String goodsUrl;//（不支付） 商品在商户网站上的URL（百度支付）
    private String pageUrl;//用户点击该URL可以返回到商户网站；该URL也可以起到通知支付结果的作用（百度支付）

    private String borrowNid;//借款单号，查询支付结果使用
}
