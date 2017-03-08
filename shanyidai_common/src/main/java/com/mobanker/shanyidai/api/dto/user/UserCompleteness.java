package com.mobanker.shanyidai.api.dto.user;

import com.mobanker.shanyidai.api.common.dto.Entity;
import com.mobanker.shanyidai.api.dto.auth.AuthIdentityProcess;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hantongyang
 * @description 用户认证进度DTO
 * @time 2017/1/10 11:39
 */
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true)
public class UserCompleteness extends Entity {

    private Long userId;
    private String completeProgress; //资料认证进度（样例：2/3）
    private int completeProgressCou; //资料认证进度（样例：2）
    private String realName; //是否存在借款 1：存在 0：不存在
//    private String identification; // 身份识别状态 0：未填写 1：已填写
    private String contactJob; //联系人/工作单位状态 0：未填写 1：已填写
    private String jobStatus; //单位信息 0：有效 1：失效
    private String zhima; //芝麻认证状态 0：未填写 1：已填写
    private String zhimaScore; //芝麻分
    private AuthIdentityProcess identityProcess;//身份认证进度，PROCESS "认证中" SUCCESS "已认证" FAIL "认证失败" NODATA"未认证"
}
