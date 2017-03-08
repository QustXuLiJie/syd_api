package com.mobanker.shanyidai.api.dto.user;

import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hantongyang
 * @description
 * @time 2016/12/28 20:22
 */
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true)
public class UserContactRsp extends Entity {
    // 用户ID
    private Long userId;
    //直系联系人
    private String relationship1; //直系联系人关系
    private String phoneNum1; //直系联系人电话
    private String contact1; //直系联系人姓名
    //重要联系人
    private String relationship2; //重要联系人关系
    private String phoneNum2; //重要联系人电话
    private String contact2; //重要联系人姓名
}
