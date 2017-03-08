package com.mobanker.shanyidai.api.business.product;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.dto.product.ProductParam;
import com.mobanker.shanyidai.api.dto.product.ProductTemp;
import com.mobanker.shanyidai.dubbo.dto.product.ProductDto;
import com.mobanker.shanyidai.dubbo.service.product.ProductDubboService;
import com.mobanker.shanyidai.esb.common.exception.EsbException;
import com.mobanker.shanyidai.esb.common.packet.ResponseBuilder;
import com.mobanker.shanyidai.esb.common.packet.ResponseEntity;
import com.mobanker.shanyidai.esb.common.utils.BeanUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hantongyang
 * @description
 * @time 2016/12/30 14:02
 */
@Service
public class ProductBusinessImpl implements ProductBusiness {

    @Resource
    private ProductDubboService dubboProductService;

    @Override
    public ProductTemp getProductById(ProductParam param) {
        if (param == null) {
            throw new SydException(ReturnCode.PARAM_REQUIRED);
        }
        ProductDto dto = BeanHelper.cloneBean(param, ProductDto.class);
        ResponseEntity result = null;
        try {
            result = dubboProductService.getProductTemp(dto);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        }
        if (!ResponseBuilder.isSuccess(result)) {
            throw new SydException(ReturnCode.SERVICE_FAILED.getCode(), result.getMsg());
        }
        //TODO 需要写后续的解析逻辑
        ProductTemp productTemp = BeanUtil.cloneBean(result.getData(), ProductTemp.class);
        return productTemp;
    }
}
