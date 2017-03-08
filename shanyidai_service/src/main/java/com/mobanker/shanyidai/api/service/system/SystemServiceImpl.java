package com.mobanker.shanyidai.api.service.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mobanker.shanyidai.api.business.common.CommonBusiness;
import com.mobanker.shanyidai.api.business.redis.RedisBusiness;
import com.mobanker.shanyidai.api.business.system.DictionaryBusiness;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.constant.ZkConfigConstant;
import com.mobanker.shanyidai.api.common.enums.RedisKeyEnum;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.common.tool.CommonUtil;
import com.mobanker.shanyidai.api.dto.system.ConfigBean;
import com.mobanker.shanyidai.api.dto.system.Dictionary;
import com.mobanker.shanyidai.api.dto.system.H5Url;
import com.mobanker.shanyidai.api.enums.H5UrlEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

import static com.mobanker.shanyidai.api.common.constant.ZkConfigConstant.SYD_CUSTOMER_SERVICE_PHONE;

/**
 * @author hantongyang
 * @description
 * @time 2017/1/20 18:34
 */
@Service
public class SystemServiceImpl implements SystemService {
    @Resource
    private CommonBusiness commonBusiness;
    @Resource
    private DictionaryBusiness dictionaryBusiness;
    @Resource
    private RedisBusiness redisBusiness;

    /**
     * @description 获取H5URL地址信息
     * @author hantongyang
     * @time 2017/1/20 18:36
     * @method getUrlByType
     * @param request
     * @return List<H5Url>
     */
    @Override
    public List<H5Url> getUrlByType(SydRequest request) {
        //1、验证参数是否正确
        if(request == null){
            throw new SydException(ReturnCode.PARAMS_ILLEGE);
        }
        if(StringUtils.isBlank(request.getContent())){
            throw new SydException(ReturnCode.PARAMS_ILLEGE);
        }
        JSONObject json = JSONObject.parseObject(request.getContent());
        String type = json.getString("type");
        if(StringUtils.isBlank(type)){
            throw new SydException(ReturnCode.ERROR_GET_H5_TYPE);
        }
        //分割类型参数
        String[] splitType = type.split(",");
        if(splitType.length <= 0){
            throw new SydException(ReturnCode.ERROR_GET_H5_TYPE);
        }
        List<H5Url> urlList = new ArrayList<H5Url>();
        for (String s : splitType) {
            //2、根据Type验证是否是枚举中的类型
            H5UrlEnum urlEnum = H5UrlEnum.getByKey(s);
            if(urlEnum == null){
                throw new SydException(ReturnCode.ERROR_GET_H5_URL);
            }
            //3、根据参数获取配置系统数据
            String cacheSysConfig = commonBusiness.getCacheSysConfig(urlEnum.getCode());
            if(StringUtils.isBlank(cacheSysConfig)){
                throw new SydException(ReturnCode.ERROR_GET_H5_URL);
            }
            //封装结果集
            H5Url urlBean = new H5Url();
            urlBean.setType(s);
            urlBean.setUrl(cacheSysConfig);
            urlList.add(urlBean);
        }
        return urlList;
    }

    /**
     * @description 根据类型查询数据字典列表
     * @author hantongyang
     * @time 2017/1/23 14:21
     * @method getDictionaryByType
     * @param request
     * @return List<Dictionary>
     */
    @Override
    public List<Dictionary> getDictionaryByType(SydRequest request) {
        //验证参数
        CommonUtil.checkLoginStatus(request);
        Dictionary dic = BeanHelper.parseJson(request.getContent(), Dictionary.class);
        if(dic == null || StringUtils.isBlank(dic.getDicType())){
            throw new SydException(ReturnCode.PARAM_REQUIRED);
        }
        return getDictionary(dic);
    }

    /**
     * @description 根据类型、编码查询数据字典
     * @author hantongyang
     * @time 2017/1/23 14:21
     * @method getDictionaryByCode
     * @param request
     * @return Dictionary
     */
    @Override
    public Dictionary getDictionaryByCode(SydRequest request) {
        //验证参数
        CommonUtil.checkLoginStatus(request);
        Dictionary dic = BeanHelper.parseJson(request.getContent(), Dictionary.class);
        if(dic == null || StringUtils.isBlank(dic.getDicType()) || StringUtils.isBlank(dic.getDicCode())){
            throw new SydException(ReturnCode.PARAM_REQUIRED);
        }
        //根据类型查询数据字典列表
        List<Dictionary> dictionaryList = getDictionary(dic);
        if(dictionaryList == null || dictionaryList.isEmpty()){
            return null;
        }
        //循环数据字典列表，获取匹配的数据字典项
        for (Dictionary dictionary : dictionaryList) {
            if(dictionary == null){
                continue;
            }
            if(dictionary.getDicCode().equals(dic.getDicCode())){
                return dictionary;
            }
        }
        return null;
    }

    /**
     * @description 查询数据字典列表
     * @author hantongyang
     * @time 2017/1/23 14:49
     * @method getDictionary
     * @param dic
     * @return java.util.List<com.mobanker.shanyidai.api.dto.system.Dictionary>
     */
    @Override
    public List<Dictionary> getDictionary(Dictionary dic){
        //查询缓存中是否有该类型的数据字典
        String value = redisBusiness.getValue(RedisKeyEnum.SYD_DICTIONARY_INFO + dic.getDicType());
        if(StringUtils.isNotBlank(value)){
            List<Dictionary> dtoList = JSONArray.parseArray(value, Dictionary.class);
            return dtoList;
        }
        //调用服务获取对应的数据字典
        List<Dictionary> list = dictionaryBusiness.getDictionaryByType(dic.getDicType());
        //保存到缓存中
        if(list == null || list.isEmpty()){
            return null;
        }
        redisBusiness.setValue(RedisKeyEnum.SYD_DICTIONARY_INFO + dic.getDicType(), JSONArray.toJSONString(list));
        return list;
    }

    /**
     * @description 删除缓存中的数据
     * @author hantongyang
     * @time 2017/2/13 9:52
     * @method delRedisByCode
     * @param request
     * @return void
     */
    @Override
    public void delRedisByCode(SydRequest request) {
        //验证参数
        CommonUtil.checkLoginStatus(request);
        JSONObject json = JSONObject.parseObject(request.getContent());
        String code = json.getString("redisCode");
        if(StringUtils.isBlank(code)){
            return;
        }
        redisBusiness.delValue(code);
    }

    /**
     * @description 获取配置汇总
     * @author hantongyang
     * @time 2017/2/14 11:58
     * @method getConfig
     * @param
     * @return ConfigBean
     */
    @Override
    public ConfigBean getConfig() {
        //查询缓存中是否有相关配置信息
        String config = redisBusiness.getValue(RedisKeyEnum.SYD_CONFIG.getValue());
        if(StringUtils.isNotBlank(config)){
            return BeanHelper.parseJson(config, ConfigBean.class);
        }
        ConfigBean bean = new ConfigBean();
        //是否强制授权通讯录
        String accreditPhoneBook = commonBusiness.getCacheSysConfig(ZkConfigConstant.SYD_ACCREDIT_PHONE_BOOK.getZkValue());
        bean.setAccreditPhoneBook(accreditPhoneBook);
        //客服电话
        String customerServicePhone = commonBusiness.getCacheSysConfig(SYD_CUSTOMER_SERVICE_PHONE.getZkValue());
        bean.setCustomerServicesPhone(customerServicePhone);
        //在线客服URL
        String onlineCustomerServiceUrl = commonBusiness.getCacheSysConfig(ZkConfigConstant.SYD_ONLINE_CUSTOMER_SERVICE_URL.getZkValue(),
                ReturnCode.CONFIG_DATA_NULL.getCode(), ReturnCode.CONFIG_DATA_NULL.getDesc());
        bean.setOnlineCustomerServiceUrl(onlineCustomerServiceUrl);
        //将配置信息保存到缓存中
        redisBusiness.setValue(RedisKeyEnum.SYD_CONFIG.getValue(), JSONObject.toJSONString(bean));
        return bean;
    }
}
