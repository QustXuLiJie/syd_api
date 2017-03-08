package com.mobanker.shanyidai.api.dto.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hantongyang
 * @version 1.0
 * @description
 * @date 创建时间：2016/12/21 18:18
 */
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true)
public class ZhimaInfo {

    private String isAuth; //是否已芝麻授权 0：未授权 1：已授权
    private String isSkip; //是否已跳过 0：未跳过 1：已跳过
    private String isExpires; //是否过期 0：未过期 1：已过期
    private String canSkip; //是否可以跳过芝麻授权 0：不可以 1：可以
    private String authTips; //芝麻信息
}
