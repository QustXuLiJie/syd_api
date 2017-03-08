package com.mobanker.shanyidai.api.service.common;

import com.mobanker.shanyidai.api.business.common.CommonBusiness;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.constant.ZkConfigConstant;
import com.mobanker.shanyidai.api.common.exception.SydException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hantongyang
 * @description
 * @time 2016/12/28 17:08
 */
@Service
public class CommonServiceImpl implements CommonService {
    @Resource
    private CommonBusiness commonBusiness;

    /**
     * @description 从zk读取配置信息</br>@机制：先从数据库加载到缓存中，定时刷新缓存
     * @author hantongyang
     * @time 2016/12/22 14:31
     * @return String
     */
    @Override
    public String getCacheSysConfig(String key) {
        if(StringUtils.isBlank(key)){
            throw new SydException(ReturnCode.PARAM_REQUIRED);
        }
        return commonBusiness.getCacheSysConfig(key);
    }

    /**
     * @description 获取闪宜贷版本号。若是前端不传，则默认用微站默认配置的版本号
     * @author hantongyang
     * @time 2016/12/22 14:31
     * @method
     * @return
     */
    @Override
    public String getVersion() {
        String version = commonBusiness.getVersion(ZkConfigConstant.SYD_APP_VERSION.getZkValue());
        if(StringUtils.isBlank(version)){
            version = ZkConfigConstant.SYD_APP_VERSION.getDefaultValue();
        }
        return version;
    }
}
