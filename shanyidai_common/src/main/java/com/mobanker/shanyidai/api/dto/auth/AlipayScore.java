package com.mobanker.shanyidai.api.dto.auth;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hantongyang
 * @version 1.0
 * @description
 * @date 创建时间：2016/12/21 17:38
 */
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true)
public class AlipayScore extends SydRequest {

    //用户姓名
    private String cardId;
    //用户身份证号
    private String realName;
    private String alipayScore;//芝麻分
    private String alipayUsersStatus;//认证状态 -2:未处理 -1:已处理 0:授权成功 1:身份证校验成功 2:身份证校验失败 3:手机号不匹配
    private String alipaySkipAuthorize;//是否跳过认证 1跳过，0未跳过
}
