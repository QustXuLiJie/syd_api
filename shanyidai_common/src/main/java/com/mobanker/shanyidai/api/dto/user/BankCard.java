package com.mobanker.shanyidai.api.dto.user;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class BankCard extends SydRequest {
    private String bankCardId;//银行卡编号（基础服务系统中卡主键）
    private String bankCardNo;//银行卡号
    private String bankName;//开户行名称
    private String phone;     //银行预留手机号
    private String cardType;//银行卡类型  枚举  C:贷记卡（信用卡） D:借记卡（储蓄卡）
    private String cardTypeDesc;//银行卡类型  枚举  C:贷记卡（信用卡） D:借记卡（储蓄卡）
    private int cardYstatus;//银行卡验证状态
    private Long userId;//用户userId
    private String payCard;//是否入账银行卡 0：否 1：是
    private String cardIcon; //银行卡的icon的url

    private String branchBankName;//cardBankBranchName 支行名称

}