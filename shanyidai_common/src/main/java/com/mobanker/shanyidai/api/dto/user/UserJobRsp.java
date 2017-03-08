package com.mobanker.shanyidai.api.dto.user;

import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hantongyang
 * @description
 * @time 2016/12/28 20:23
 */
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true)
public class UserJobRsp extends Entity{
    // 用户ID
    private Long userId;
    // 单位名称
    private String jobTypeId; //工作类型id
    private String companyName; //公司名称
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
    private String companyTelCountry;//电话区号
    private String companyTelFixed;//固定号码
    private String companyTelSuffix;//分机号

    private String jobInfoAllIsExpire; //工作信息是否有效期
}
