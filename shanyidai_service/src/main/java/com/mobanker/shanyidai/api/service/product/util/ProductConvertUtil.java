package com.mobanker.shanyidai.api.service.product.util;

import com.mobanker.shanyidai.api.common.constant.SystemConstant;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.dto.product.ProductParam;
import com.mobanker.shanyidai.api.dto.product.ProductTemp;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @author hantongyang
 * @description
 * @time 2016/12/30 18:15
 */
public class ProductConvertUtil {

    /**
     * @description 初始化产品模板查询实体
     * @author hantongyang
     * @time 2016/12/30 18:14
     * @method initProductParameter
     * @param request
     * @return com.mobanker.shanyidai.api.dto.product.ProductParam
     */
    public static ProductParam initProductParameter(SydRequest request){
        ProductParam param = new ProductParam();
        param.setAppVersion(request.getVersion());
        param.setPeriod(SystemConstant.PRODUCT_PERIOD.getValue());
        param.setBorrowType(SystemConstant.BORROW_TYPE.getValue());
        param.setProductType(request.getProduct());
        param.setMerchant(request.getChannel());
        param.setChannel(request.getChannel());
        return param;
    }

    /**
     * @description 封装最大、最小借款金额
     * @author hantongyang
     * @time 2017/1/17 20:08
     * @method setLimitAmount
     * @param product
     * @return void
     */
    public static void setLimitAmount(ProductTemp product){
        //由于接口返回的是字符串类型的借款金额范围，拆分成金额类型的借款金额区间
        if(StringUtils.isNotBlank(product.getLimitAmount())){
            String[] limits = product.getLimitAmount().split("-");
            if(limits.length > 1){
                product.setMinLimitAmount(new BigDecimal(limits[0]));
                product.setMaxLimitAmount(new BigDecimal(limits[1]));
            }
        }
    }

}
