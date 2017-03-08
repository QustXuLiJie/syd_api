package com.mobanker.shanyidai.api.dto.auth;

import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hantongyang
 * @description
 * @time 2017/1/23 19:19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class ZhimaAuthParam extends Entity {
    private Long userId;
    private String code;
    private String name;
    private String certNo;
    private String sences;//取entity中product字段
}
