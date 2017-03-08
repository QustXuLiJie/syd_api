package com.mobanker.shanyidai.api.dto.user;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hantongyang
 * @description
 * @time 2016/12/28 20:24
 */
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true)
public class UserJobReq extends SydRequest {
    // 用户ID
    private Long userId;
    // 单位名称
    private String name;
    // 职位
    private String office;
    private String officeLevelName1;
    private String officeLevelName2;
    private String officeLevelName3;
    private String officeLevelId1;
    private String officeLevelId2;
    private String officeLevelId3;
    private String position; //职位名称
    // 单位电话
    private String telCountry;//电话区号
    private String telFixed;//固定号码
    private String telSuffix;//分机号
}
