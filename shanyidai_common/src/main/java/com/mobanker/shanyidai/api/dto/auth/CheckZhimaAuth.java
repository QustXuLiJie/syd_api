package com.mobanker.shanyidai.api.dto.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: xulijie
 * @description:
 * @create time: 10:29 2017/2/17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class CheckZhimaAuth {
    private String authStatus;    //授权状态，-2:未处理 -1:已处理 0:授权成功 1:身份证校验成功 2:身份证校验失败 3:手机号不匹配
    private String expired;    //1 过期 ，0未过期
    private String openId;//1 过期 ，0未过期
    private String certNo;//授权码
    private String name;//身份证号
    private String sences;//姓名
    private String authTime;//场景
    private String expireTime;//授权时间
    private String isOld;//授权过期时间
}
