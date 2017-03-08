package com.mobanker.shanyidai.api.dto.system;

import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hantongyang
 * @description
 * @time 2017/2/14 14:09
 */
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true)
public class ConfigBean extends Entity{
    private String accreditPhoneBook; //是否强制授权通讯录，1强制，0不强制
    private String customerServicesPhone; //客服电话
    private String onlineCustomerServiceUrl; //在线客服URL
}
