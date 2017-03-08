package com.mobanker.shanyidai.api.service.product;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.dto.product.ProductTemp;

/**
 * @author hantongyang
 * @description
 * @time 2016/12/30 14:03
 */
public interface ProductService {

    ProductTemp getProductTemp(SydRequest sydRequest);
}
