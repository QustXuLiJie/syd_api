package com.mobanker.shanyidai.api.business.system;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.dto.system.Dictionary;
import com.mobanker.shanyidai.dubbo.dto.system.DataDictionaryDto;
import com.mobanker.shanyidai.dubbo.service.system.DataDictionaryDubboService;
import com.mobanker.shanyidai.esb.common.exception.EsbException;
import com.mobanker.shanyidai.esb.common.packet.ResponseBuilder;
import com.mobanker.shanyidai.esb.common.packet.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hantongyang
 * @description
 * @time 2017/1/23 14:14
 */
@Service
public class DictionaryBusinessImpl implements DictionaryBusiness {

    @Resource
    private DataDictionaryDubboService dubboDictionaryService;

    /**
     * @description 根据类型查询数据字典列表
     * @author hantongyang
     * @time 2017/1/23 14:16
     * @method getDictionaryByType
     * @param type
     * @return java.util.List<com.mobanker.shanyidai.api.dto.system.Dictionary>
     */
    @Override
    public List<Dictionary> getDictionaryByType(String type) {
        ResponseEntity response = null;
        try {
            response = dubboDictionaryService.getDictionaryByType(type);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        } catch (Exception e1) {
            throw new SydException(ReturnCode.ERROR_GET_DICTIONARY.getCode(), e1.getMessage());
        }
        if(!ResponseBuilder.isSuccess(response)){
            throw new SydException(response.getError(), response.getMsg());
        }
        List<Dictionary> dicList = new ArrayList<Dictionary>();
        for(DataDictionaryDto dto : (List<DataDictionaryDto>)response.getData()){
            if(dto == null){
                continue;
            }
            dicList.add(BeanHelper.cloneBean(dto, Dictionary.class));
        }
        return dicList;
    }
}
