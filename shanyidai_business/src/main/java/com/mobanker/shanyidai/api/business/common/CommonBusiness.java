package com.mobanker.shanyidai.api.business.common;

import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.esb.common.packet.ResponseEntity;
import com.mobanker.shanyidai.api.dto.system.HolidayBean;

/**
 * @author Richard Core
 * @description 公共方法
 * @date 2016/12/12 20:08
 */
public interface CommonBusiness {

    Object checkRetData(ResponseEntity re) throws SydException;

    void checkAuthentAccount(String account) throws SydException;

    void checkAuthentPasswd(String passwd) throws SydException;

    void checkAuthentCaptcha(String captcha) throws SydException;

    /**
     * @description 从zk读取配置信息</br>@机制：先从数据库加载到缓存中，定时刷新缓存
     * @author hantongyang
     * @time 2016/12/22 14:31
     * @return String
     */
    String getCacheSysConfig(String key);

    String getCacheSysConfig(String key, String errorCode, String errorMsg);

    /**
     * @description 获取闪宜贷版本号。若是前端不传，则默认用微站默认配置的版本号
     * @author hantongyang
     * @time 2016/12/22 14:31
     * @method
     * @param version
     * @return
     */
    public String getVersion(String version);

    /**
     * @description 校验日期是否节假日
     * @auther hantongyang
     * @param year
     * @return
     * @date 2016/12/22
     */
    public HolidayBean getHoliday(String year);

    /**
     * 读取字典表的配置信息</br>
     * @机制：先从数据库加载到缓存中，定时刷新缓存
     * @data 2016年7月8日
     * @param key
     * @return String
     */
    public String getSysConfig(String key);

    /**
     * 是否外部渠道
     * @auther hantongyang
     * @param channel
     * @return
     * @date 2016年7月26日 下午3:44:49
     */
    public boolean isOutChannel(String channel);
}

