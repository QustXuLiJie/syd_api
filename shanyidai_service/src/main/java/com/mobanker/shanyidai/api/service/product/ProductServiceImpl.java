package com.mobanker.shanyidai.api.service.product;

import com.alibaba.fastjson.JSONObject;
import com.mobanker.shanyidai.api.business.common.CommonBusiness;
import com.mobanker.shanyidai.api.business.product.ProductBusiness;
import com.mobanker.shanyidai.api.business.redis.RedisBusiness;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.constant.SystemConstant;
import com.mobanker.shanyidai.api.common.constant.ZkConfigConstant;
import com.mobanker.shanyidai.api.common.enums.RedisKeyEnum;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.dto.product.ProductTemp;
import com.mobanker.shanyidai.esb.common.utils.BeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.math.BigDecimal;

import static com.mobanker.shanyidai.api.service.product.util.ProductConvertUtil.initProductParameter;
import static com.mobanker.shanyidai.api.service.product.util.ProductConvertUtil.setLimitAmount;
import static com.sun.corba.se.spi.activation.IIOP_CLEAR_TEXT.value;

/**
 * @author hantongyang
 * @description
 * @time 2016/12/30 14:03
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductBusiness productBusiness;
    @Resource
    private RedisBusiness redisBusiness;
    @Resource
    private CommonBusiness commonBusiness;

    /**
     * @description 查询产品模板，查询缓存中是否存在，存在先从缓存中获取，不存在再调用服务查找
     * @author hantongyang
     * @time 2016/12/30 14:06
     * @method getProductTemp
     * @param request
     * @return com.mobanker.shanyidai.api.dto.product.ProductTemp
     */
    @Override
    public ProductTemp getProductTemp(SydRequest request) {
        //1、查询缓存中是否存在产品模板
        ProductTemp product = redisBusiness.getValue(RedisKeyEnum.SYD_PRODUCT_INFO.getValue(), ProductTemp.class);
        if(product != null){
            return product;
        }
        //2、不存在产品模板，调用服务查询产品模板
        product = productBusiness.getProductById(initProductParameter(request));
        if(product == null){
            throw new SydException(ReturnCode.GET_PRODUCT_TEMP_ERROR);
        }
        //封装借款金额
        setLimitAmount(product);
        //封装借款期限
        setBorrowTimeLimit(product);
        //3、更新缓存中的产品模板数据
        Integer timeOut = Integer.valueOf(SystemConstant.REDIS_BASIC_TIMEOUT.getValue());
        redisBusiness.setValue(RedisKeyEnum.SYD_PRODUCT_INFO.getValue(), JSONObject.toJSONString(product), timeOut);
        return product;
    }

    /**
     * @description 封装最短、最长借款期限
     * @author hantongyang
     * @time 2017/1/17 20:08
     * @method setBorrowTimeLimit
     * @param product
     * @return void
     */
    private void setBorrowTimeLimit(ProductTemp product){
        String minTimeLimit = commonBusiness.getCacheSysConfig(ZkConfigConstant.SYD_BORROW_MIN_TIME_LIMIT.getZkValue());
        String maxTImeLimit = commonBusiness.getCacheSysConfig(ZkConfigConstant.SYD_BORROW_MAX_TIME_LIMIT.getZkValue());
        product.setMinTimeLimit(Integer.parseInt(minTimeLimit));
        product.setMaxTimeLimit(Integer.parseInt(maxTImeLimit));
    }
}
