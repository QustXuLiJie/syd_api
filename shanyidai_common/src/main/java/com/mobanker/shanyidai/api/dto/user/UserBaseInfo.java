package com.mobanker.shanyidai.api.dto.user;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class UserBaseInfo extends SydRequest {

    private static final long serialVersionUID = -3925560694332114797L;
    private String code;  //登录code
    private Long userId;    // 用户ID
    private String userName;//手机号
    private String password; //登录密码
    private String realname;//真实姓名
    private String avatar; //头像地址

}
