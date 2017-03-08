package com.mobanker.shanyidai.api.business.basedata;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.dubbo.service.basedata.BaseDataDubboService;
import com.mobanker.shanyidai.esb.common.exception.EsbException;
import com.mobanker.shanyidai.esb.common.packet.ResponseBuilder;
import com.mobanker.shanyidai.esb.common.packet.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 获取职业类型列表
 *
 * Created by zhouqianqian on 2017/2/9.
 */
@Service
public class BaseDataBusinessImpl implements BaseDataBusiness {
    @Resource
    private BaseDataDubboService baseDataDubboService;

    /**
     * @return String
     * @description 获取职位列表
     * @author zhouqianqian
     * @method listJobType
     */
    @Override
    public String listJobType() {
        //List<Map<String, Object>>
        ResponseEntity result = null;
        try {
            result = baseDataDubboService.getJobTypeList();
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        } catch (Exception e) {
            throw new SydException(ReturnCode.ERROR_JOBTYPE_GET_INFO.getCode(), e.getMessage());
        }
        if (!ResponseBuilder.isSuccess(result)) {
            throw new SydException(result.getError(), result.getMsg());
        }
        if (result.getData() == null) {
            return null;
        }

        //返回职位类型列表信息
//        String list = JSON.toJSONString(result.getData());
//        return list;
        return (String)result.getData();
    }
    /**
     * @return String
     * @description 获取联系人关系
     * @author zhouqianqian
     * @method listJobType
     */
    @Override
    public String getRelation(String product) {
        //List<Map<String, Object>>
        ResponseEntity result = null;
        try {
            result = baseDataDubboService.getRelation(product);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        } catch (Exception e) {
            throw new SydException(ReturnCode.ERROR_RELATION_GET_INFO.getCode(), e.getMessage());
        }
        if (!ResponseBuilder.isSuccess(result)) {
            throw new SydException(result.getError(), result.getMsg());
        }
        if (result.getData() == null) {
            return null;
        }

        //返回联系人关系列表信息，并保存到缓存中
//        String list = JSON.toJSONString(result.getData());
        return (String)result.getData();
    }


    /**
     * @return String
     * @description 获取学历列表
     * @author zhouqianqian
     * @method getEducation
     */
    @Override
    public String getEducation(String product) {
        //List<Map<String, Object>>
        ResponseEntity result = null;
        try {
            result = baseDataDubboService.getEducation(product);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        } catch (Exception e) {
            throw new SydException(ReturnCode.ERROR_EDUCATION_GET_INFO.getCode(), e.getMessage());
        }
        if (!ResponseBuilder.isSuccess(result)) {
            throw new SydException(result.getError(), result.getMsg());
        }
        if (result.getData() == null) {
            return null;
        }

        //返回联系人关系列表信息，并保存到缓存中
//        String list = JSON.toJSONString(result.getData());
        return (String)result.getData();
    }

}
