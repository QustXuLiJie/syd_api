package com.mobanker.shanyidai.api.dto.version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @description: app版本更新输出参数
 * @author: xulijie
 * @create time: 20:26 2017/2/23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class VersionRsp implements Serializable {

    //是不是有更新 0：没有 1：有更新
    private String update;
    //app版本号
    private String appVersion;
    //路径
    private String appUrl;
    //APP版本描述
    private String appDesc;
    //是否强制更新，FORCE强制更新，UNFORCE不强制更新，FORBID禁止更新
    private String updateForce;
    //系统最低兼容手机系统版本
    private String minVersion;
}
