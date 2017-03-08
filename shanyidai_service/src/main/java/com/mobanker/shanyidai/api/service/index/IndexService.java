package com.mobanker.shanyidai.api.service.index;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.dto.index.Index;

/**
 * @author hantongyang
 * @description 首页相关接口
 * @time 2017/1/10 14:21
 */
public interface IndexService {

    Index index(SydRequest request);
}
