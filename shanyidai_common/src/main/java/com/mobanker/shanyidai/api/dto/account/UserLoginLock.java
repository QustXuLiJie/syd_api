package com.mobanker.shanyidai.api.dto.account;

import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hantongyang
 * @version 1.0
 * @description
 * @date 创建时间：2016/12/24 14:39
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class UserLoginLock extends Entity {

    private String phone; //手机号
    private int failCount; //错 误次数
    private Long lockTime; //锁定时间，单位秒  更新时间
}
