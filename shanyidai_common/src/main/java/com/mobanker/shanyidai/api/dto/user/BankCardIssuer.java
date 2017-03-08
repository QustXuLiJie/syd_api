package com.mobanker.shanyidai.api.dto.user;

import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @desc:
 * @author: Richard Core
 * @create time: 2017/1/5 17:16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class BankCardIssuer extends Entity{
    private String bankCode;//发卡行编号
    private String bankName;//名称
    private String bankImg;//银行图标
    private boolean selected;//是否是入账银行卡
}
