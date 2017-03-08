package com.mobanker.shanyidai.api.dto.system;

import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hantongyang
 * @description
 * @time 2017/1/23 15:38
 */
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true)
public class H5Url extends Entity {
    private String type;
    private String url;
}
