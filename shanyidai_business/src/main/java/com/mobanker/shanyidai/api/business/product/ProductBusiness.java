package com.mobanker.shanyidai.api.business.product;

import com.mobanker.shanyidai.api.dto.product.ProductParam;
import com.mobanker.shanyidai.api.dto.product.ProductTemp;

/**
 * @author hantongyang
 * @description
 * @time 2016/12/30 14:02
 */
public interface ProductBusiness {

    ProductTemp getProductById(ProductParam param);
}
