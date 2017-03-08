package com.mobanker.shanyidai.api.dto.auth;

import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hantongyang
 * @version 1.0
 * @description 实名认证返回结果实体
 * @date 创建时间：2016/12/26 11:03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class RealName extends Entity {
    private Long userId;//*
    private String realName; //姓名
    private String idCard; //身份证
    private Integer realnameTimes; //实名认证次数
    private String idCardAddress; //身份证地址
    private String realnameAllStatus; //实名认证状态
    private String idCardStatus; //身份证认证状态

    private String education; //学历
}
