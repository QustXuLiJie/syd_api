package com.mobanker.shanyidai.api.service.version;

import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;

/**
 * @description: app版本更新
 * @author: xulijie
 * @create time: 19:30 2017/2/23
 */
public interface VersionService {

    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.version.VersionRsp
     * @author xulijie
     * @method getVersion
     * @description app版本更新相关
     * @time 20:36 2017/2/23
     */

    public Object getVersion(SydRequest request ) throws SydException;

}
