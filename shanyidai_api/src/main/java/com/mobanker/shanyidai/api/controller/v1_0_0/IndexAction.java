/**
 * 
 */
package com.mobanker.shanyidai.api.controller.v1_0_0;

import com.mobanker.shanyidai.api.common.annotation.SignLoginValid;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.enums.SignLoginEnum;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.service.borrow.BorrowService;
import com.mobanker.shanyidai.api.service.index.IndexService;
import com.mobanker.shanyidai.api.service.product.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 首页相关API
 * @author chenjianping
 * @data 2016年12月9日
 */
@Controller
@RequestMapping(value = "index")
public class IndexAction {

    @Resource
    private ProductService productService;
    @Resource
    private IndexService indexService;
    @Resource
    private BorrowService borrowService;

    /**
     * @description 获取产品模板信息
     * @author hantongyang
     * @time 2017/1/10 13:49
     * @method getProduct
     * @param request
     * @return java.lang.Object
     */
    @SignLoginValid(SignLoginEnum.SIGN)
    @RequestMapping(value = "getProduct", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getProduct(SydRequest request){
        Object data = null;
        try {
            data = productService.getProductTemp(request);
        } catch (SydException e) {
            if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
                throw e;
            } else {
                throw new SydException(ReturnCode.GET_PRODUCT_TEMP_ERROR.getCode(), e.message, e);
            }
        } catch (Throwable e) {
            throw new SydException(ReturnCode.GET_PRODUCT_TEMP_ERROR, e);
        }
        return data;
    }

    /**
     * @description 获取订单信息
     * @author hantongyang
     * @time 2017/1/10 14:03
     * @method getOrder
     * @param request
     * @return java.lang.Object
     */
    @RequestMapping(value = "getOrder", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getOrder(SydRequest request){
        Object data = null;
        try {
            data = borrowService.getBorrowInfo(request);
        } catch (SydException e) {
            if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
                throw e;
            } else {
                throw new SydException(ReturnCode.GET_PRODUCT_TEMP_ERROR.getCode(), e.message, e);
            }
        } catch (Throwable e) {
            throw new SydException(ReturnCode.GET_PRODUCT_TEMP_ERROR, e);
        }
        return data;
    }

    /**
     * @description 首页组装
     * @author hantongyang
     * @time 2017/1/10 14:19
     * @method index
     * @param request
     * @return java.lang.Object
     */
    @SignLoginValid(SignLoginEnum.SIGN)
    @RequestMapping(value = "getIndexInfo", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object index(SydRequest request){
        Object data = null;
        try {
            data = indexService.index(request);
        } catch (SydException e) {
            if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
                throw e;
            } else {
                throw new SydException(ReturnCode.ERROR_GET_INDEX.getCode(), e.message, e);
            }
        } catch (Throwable e) {
            throw new SydException(ReturnCode.ERROR_GET_INDEX, e);
        }
        return data;
    }
}
