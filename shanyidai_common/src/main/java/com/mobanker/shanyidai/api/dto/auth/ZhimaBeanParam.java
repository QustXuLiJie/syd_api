package com.mobanker.shanyidai.api.dto.auth;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.esb.common.utils.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author xulijie
 * @version 1.0
 * @description 芝麻认证传入实体
 * @date 创建时间：2016/12/26 11:03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class ZhimaBeanParam extends SydRequest {
    private String code;
    private String name;
    private String certNo;
    private String pageType;
    //private String sences;->private String product;//产品
    //private String channel;->private String channel;//渠道

}
