package com.mobanker.shanyidai.api.business.system;

import com.mobanker.shanyidai.api.dto.system.Dictionary;

import java.util.List;

/**
 * @author hantongyang
 * @description
 * @time 2017/1/23 14:14
 */
public interface DictionaryBusiness {

    List<Dictionary> getDictionaryByType(String type);

}
