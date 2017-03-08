package com.mobanker.shanyidai.api.dto.user;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @desc:
 * @author: Richard Core
 * @create time: 2017/1/4 20:32
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class BankCardVerifyResult extends SydRequest{
    private String result;//结果 “D”表示处理中，“T”表示有效的，”F”表示无效的，“N”表示无法认证的，”P”表示网络连接超时
    private String resultMsg;//返回结果信息
    private String verifiyType;//银行卡验证对内鹏元，考拉卡和韩鑫卡、神州融、有贝、信源博睿、普惠统一整合输入输出
}
