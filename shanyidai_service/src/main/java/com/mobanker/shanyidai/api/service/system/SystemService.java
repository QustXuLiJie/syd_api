package com.mobanker.shanyidai.api.service.system;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.dto.system.ConfigBean;
import com.mobanker.shanyidai.api.dto.system.Dictionary;
import com.mobanker.shanyidai.api.dto.system.H5Url;

import java.util.List;
import java.util.Map;

/**
 * @author hantongyang
 * @description
 * @time 2017/1/20 18:33
 */
public interface SystemService {

    /**
     * @description 获取H5URL地址信息
     * @author hantongyang
     * @time 2017/1/20 18:36
     * @method getUrlByType
     * @param request
     * @return List<H5Url>
     */
    List<H5Url> getUrlByType(SydRequest request);

    /**
     * @description 根据类型查询数据字典列表
     * @author hantongyang
     * @time 2017/1/23 14:21
     * @method getDictionaryByType
     * @param request
     * @return List<Dictionary>
     */
    List<Dictionary> getDictionaryByType(SydRequest request);

    /**
     * @description 根据类型、编码查询数据字典
     * @author hantongyang
     * @time 2017/1/23 14:21
     * @method getDictionaryByCode
     * @param request
     * @return Dictionary
     */
    Dictionary getDictionaryByCode(SydRequest request);

    List<Dictionary> getDictionary(Dictionary dic);

    /**
     * @description 删除缓存中的数据
     * @author hantongyang
     * @time 2017/2/13 9:52
     * @method delRedisByCode
     * @param request
     * @return void
     */
    void delRedisByCode(SydRequest request);

    /**
     * @description 获取配置汇总
     * @author hantongyang
     * @time 2017/2/14 11:58
     * @method getConfig
     * @param
     * @return ConfigBean
     */
    ConfigBean getConfig();
}
