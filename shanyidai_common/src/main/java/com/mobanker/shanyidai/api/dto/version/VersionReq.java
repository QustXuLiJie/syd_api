package com.mobanker.shanyidai.api.dto.version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @description: app版本更新传入参数
 * @author: xulijie
 * @create time: 19:48 2017/2/23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class VersionReq {

    public static final String TYPE_ANDROID = "android";
    public static final String TYPE_IOS = "ios";
    //当前版本号
    private String currentVersion;
    //类型：android、ios,默认：android
    private String type;
    //产品
    private String product;
}
