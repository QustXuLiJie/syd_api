package com.mobanker.shanyidai.api.common.tool;


import com.alibaba.fastjson.JSONObject;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.logger.LogManager;
import com.mobanker.shanyidai.api.common.logger.Logger;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.esb.common.exception.EsbException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author Richard Core
 * @description 将Controller中传入SydRequest中的参数复制到业务bean中
 * @time 2016/12/16 10:23
 */
public class BeanHelper {
    public static final Logger logger = LogManager.getSlf4jLogger(BeanHelper.class);

    /**
     * @param request
     * @param br
     * @return void
     * @description 将产品渠道信息从request拷贝到dto中
     * @author Richard Core
     * @time 2016/12/12 18:31
     * @method packageBean
     */
    public static void packageBean(SydRequest request, SydRequest br) {
        if(br == null){
            br = new SydRequest();
        }
        br.setIp(request.getIp());
        br.setProduct(request.getProduct());
        br.setChannel(request.getChannel());
        br.setVersion(request.getVersion());
        br.setUserId(request.getUserId());
    }

    /**
     * @param content
     * @param clazz
     * @return T
     * @description 将content转化成指定类型的JavaBean
     * @author Richard Core
     * @time 2016/12/20 15:29
     * @method parseJson
     */
    public static <T> T parseJson(String content, Class<T> clazz) throws SydException {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        try {
            logger.info("将json参数转化为JavaBean,class:[{}]，content:[{}]", clazz.getSimpleName(), content);
            T t = JSONObject.parseObject(content, clazz);
            return t;
        } catch (Exception e) {
            logger.error("将json参数转化为JavaBean异常", e);
            throw new SydException(ReturnCode.PARAM_VALID.getCode(), ReturnCode.PARAM_VALID.getDesc(), e);
        }
    }

    /**
     * @param source
     * @param clazz
     * @return T
     * @description 属性拷贝
     * @author Richard Core
     * @time 2016/12/23 14:04
     * @method cloneBean
     */
    public static <T> T cloneBean(Object source, Class<T> clazz) {
        if (source == null) {
            return null;
        }
        try {
            logger.info("拷贝属性到指定的类型,class:[{}]，source:[{}]", clazz.getSimpleName(), source);
            T t = clazz.newInstance();
            BeanUtils.copyProperties(source, t);
            return t;
        } catch (Exception e) {
            logger.error("拷贝属性异常", e);
            throw new EsbException(ReturnCode.PARAM_VALID.getCode(), ReturnCode.PARAM_VALID.getDesc(), e);
        }
    }

}
