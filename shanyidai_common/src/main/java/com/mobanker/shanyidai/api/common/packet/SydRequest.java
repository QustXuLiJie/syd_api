package com.mobanker.shanyidai.api.common.packet;

import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.*;

/**
 * @author Richard Core
 * @description
 * @date 2016/12/10 16:40
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class SydRequest extends Entity {

    private static final long serialVersionUID = 818086480212838279L;
    private String ip;     //客户端ip 建议服务端自己获取
    private String channel;//渠道（app/wechat/web/H5等）
    private String product;//产品（shoujidai/Uzone/shanyidai等）
    private String version; //版本（app版本）
    private String uuid;//客户端传过来的code（加解密使用）
    private String reqData;//客户端传过来的加密内容
    private String content;//如果是加密则返回是解密内容
    private Long userId;   //如果有需要登录则有userId
    private String flag;//


}