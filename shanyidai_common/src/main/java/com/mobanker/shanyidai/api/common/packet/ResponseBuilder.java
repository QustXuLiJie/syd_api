package com.mobanker.shanyidai.api.common.packet;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;

import java.io.Serializable;

/**
 * @author hantongyang
 * @Time 2017-01-22
 * @desc 封装综合服务返回值
 */
public final class ResponseBuilder {
    /**
     * 返回成功.
     */
    public static final String RESPONSE_OK = "1";
    /**
     * 返回成功.
     */
    public static final String RESPONSE_FAIL = "0";


    /**
     * @param ReturnCode
     * @param e
     * @return ResponseEntity
     * @description 构造一个失败响应.
     * @author hantongyang
     * @time 2017/1/22 15:09
     * @method buildErrorResponse
     */
    public static ResponseEntity errorResponse(ReturnCode ReturnCode, Throwable e) {
        if (e instanceof SydException) {
            SydException ee = (SydException) e;
            return errorResponse(ee.errCode, ee.message);
        } else {
            return errorResponse(ReturnCode.getCode(), e.getMessage());
        }
    }

    /**
     * @param errorCode 失败错误编码
     * @param errorMsg  失败错误说明.
     * @return ResponseEntity
     * @description
     * @author hantongyang
     * @time 2017/1/22 15:09
     * @method buildErrorResponse
     */
    public static ResponseEntity errorResponse(String errorCode, String errorMsg) {
        ResponseEntity entity = new ResponseEntity();
        entity.setStatus(RESPONSE_FAIL);
        entity.setError(errorCode);
        entity.setMsg(errorMsg);
        return entity;
    }

    /**
     * @param ReturnCode
     * @return ResponseEntity
     * @description 构造一个失败响应.
     * @author hantongyang
     * @time 2017/1/22 15:10
     * @method buildErrorResponse
     */
    public static ResponseEntity errorResponse(ReturnCode ReturnCode) {
        return errorResponse(ReturnCode.getCode(), ReturnCode.getDesc());
    }


    /**
     * @param
     * @return ResponseEntity
     * @description 构造一个正常响应. 通常用于没有返回值的接口
     * @author hantongyang
     * @time 2017/1/22 15:15
     * @method buildNormalResponse
     */
    public static ResponseEntity normalResponse() {
        //规范：所有正常请求(status="1",error="00000000"),code与pageCount属性被废弃.
        return new ResponseEntity(RESPONSE_OK, ReturnCode.SUCCESS.getCode());
    }

    /**
     * @param data 接口返回值
     * @return ResponseEntity
     * @description 构造一个正常响应. 通常用于有返回值的接口
     * @author hantongyang
     * @time 2017/1/22 15:15
     * @method buildNormalResponse
     */
    public static <T> ResponseEntity normalResponse(T data) {
        ResponseEntity entity = normalResponse();
        entity.setData(data);
        return entity;
    }


    /**
     * @param entity
     * @return java.lang.Boolean
     * @description 判断接口是否通讯成功
     * @author hantongyang
     * @time 2017/1/22 15:23
     * @method isFinished
     */
    public static boolean isFinished(ResponseEntity entity) {
        if (entity == null) {
            throw new SydException(ReturnCode.SYS_EXCEPTION);
        }
        return RESPONSE_OK.equals(entity.getStatus());
    }

    /**
     * @param entity
     * @return boolean
     * @description 接口通讯成功，并且业务处理成功
     * @author hantongyang
     * @time 2017/1/22 15:26
     * @method isSuccess
     */
    public static boolean isSuccess(ResponseEntity entity) {
        if (entity == null) {
            throw new SydException(ReturnCode.SYS_EXCEPTION);
        }
        return isFinished(entity) && ReturnCode.SUCCESS.getCode().equals(entity.getError());
    }

    /**
     * @param entity
     * @return T
     * @description 获取接口返回值
     * @author hantongyang
     * @time 2017/1/22 15:27
     * @method getEntity
     */
    public static <T extends Serializable> T getEntity(ResponseEntity entity, Class<T> clazz) {
        if (entity == null) {
            return null;
        }
        if (isSuccess(entity)) {
            return (T) entity.getData();
        }
        return null;
    }
    /**
     * @param responseEntity 基础服务返回的responseEntity
     * @param ReturnCode 有问题时抛出的异常码
     * @param checkFinishOnly 是否只验证通讯是否成功
     * @return ResponseEntity
     * @description 检查服务返回值
     * @author hantongyang
     * @time 2017/1/22 13:40
     * @method checkResponseEntity
     */
    public static ResponseEntity checkResponseEntity(Object responseEntity, ReturnCode ReturnCode, boolean checkFinishOnly) {
        if (ReturnCode == null) {
            ReturnCode = ReturnCode.SERVICE_FAILED;
        }
        ResponseEntity clonedRsp = BeanHelper.cloneBean(responseEntity, ResponseEntity.class);
        //只验证调用是否成功
        if (checkFinishOnly) {
            if (!ResponseBuilder.isFinished(clonedRsp)) {
                throw new SydException(ReturnCode.getCode(), clonedRsp.getMsg());
            }
            return clonedRsp;
        }
        //验证调用成功，验证业务操作成功
        if (!ResponseBuilder.isSuccess(clonedRsp)) {
            throw new SydException(ReturnCode.getCode(), clonedRsp.getMsg());
        }
        return clonedRsp;
    }
    public static ResponseEntity checkResponseEntity(Object responseEntity, ReturnCode ReturnCode) {
        return checkResponseEntity(responseEntity, ReturnCode, false);
    }
    public static ResponseEntity checkResponseEntity(Object responseEntity, boolean checkFinishOnly) {
        return checkResponseEntity(responseEntity, ReturnCode.SERVICE_FAILED, checkFinishOnly);
    }
    public static ResponseEntity checkResponseEntity(Object responseEntity) {
        return checkResponseEntity(responseEntity, ReturnCode.SERVICE_FAILED, false);
    }
}
