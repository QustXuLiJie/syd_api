package com.mobanker.shanyidai.api.service.common;

/**
 * @author hantongyang
 * @description
 * @time 2016/12/28 17:08
 */
public interface CommonService {

    /**
     * @description 从zk读取配置信息</br>@机制：先从数据库加载到缓存中，定时刷新缓存
     * @author hantongyang
     * @time 2016/12/22 14:31
     * @return String
     */
    String getCacheSysConfig(String key);

    /**
     * @description 获取闪宜贷版本号。若是前端不传，则默认用微站默认配置的版本号
     * @author hantongyang
     * @time 2016/12/22 14:31
     * @method
     * @return
     */
    String getVersion();
}
