package com.mobanker.shanyidai.api.service.security;

import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.dto.security.FirstShakeInfo;
import com.mobanker.shanyidai.api.dto.security.SecondShakeInfo;
import com.mobanker.shanyidai.api.dto.security.ShakeInfo;

/**
 * @version 1.0
 * @author: hanty
 * @date 创建时间 2016-12-12 20:16
 * @parameter
 * @return
 */
public interface SecurityService {

    public FirstShakeInfo firstShake(SydRequest request) throws SydException;

    public SecondShakeInfo secondShake(SydRequest request) throws SydException;

    public ShakeInfo getThreeDesKey(String uuid);
}
