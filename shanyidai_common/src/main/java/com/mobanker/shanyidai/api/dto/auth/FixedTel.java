package com.mobanker.shanyidai.api.dto.auth;

import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hantongyang
 * @description 固话反查DTO
 * @time 2017/1/9 15:13
 */
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true)
public class FixedTel extends Entity{

    private String areaCode; //区域
    private String address; //地址
    private String ownerName; //单位名称
}
