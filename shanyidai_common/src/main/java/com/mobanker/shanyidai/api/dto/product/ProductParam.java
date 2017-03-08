package com.mobanker.shanyidai.api.dto.product;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hantongyang
 * @description
 * @time 2016/12/30 17:27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class ProductParam extends SydRequest {

    private String period;
    private String productType;
    private String borrowType;
    private String merchant;
    private String channel;
    private String appVersion;
}
