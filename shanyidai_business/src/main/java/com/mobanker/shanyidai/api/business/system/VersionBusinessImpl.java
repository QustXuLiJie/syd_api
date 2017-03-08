/**
 *
 */
package com.mobanker.shanyidai.api.business.system;

import org.springframework.stereotype.Service;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.dto.version.VersionReq;
import com.mobanker.shanyidai.api.dto.version.VersionRsp;
import com.mobanker.shanyidai.dubbo.dto.version.VersionParamDto;
import com.mobanker.shanyidai.dubbo.service.version.VersionDubboService;
import com.mobanker.shanyidai.esb.common.logger.LogManager;
import com.mobanker.shanyidai.esb.common.logger.Logger;
import com.mobanker.shanyidai.esb.common.packet.ResponseBuilder;
import com.mobanker.shanyidai.esb.common.packet.ResponseEntity;

import javax.annotation.Resource;

/**
 * 版本相关服务实现
 *
 * @author chenjianping
 * @data 2016年12月11日
 */
@Service
public class VersionBusinessImpl implements VersionBusiness {
    public static final Logger logger = LogManager.getSlf4jLogger(VersionBusinessImpl.class);
    @Resource
    private VersionDubboService versionDubboService;

    @Override
    public VersionRsp getVersion(VersionReq versionReq) {
        if (versionReq == null) {
            throw new SydException(ReturnCode.PARAM_VALID);
        }
        //拷贝参数
        VersionParamDto dto = BeanHelper.cloneBean(versionReq, VersionParamDto.class);
        ResponseEntity entity = null;
        try {
            entity = versionDubboService.getVersion(dto);
        }catch (Exception e){
            throw new SydException(ReturnCode.CHECKVERSION_FAILED.getCode(), e.getMessage());
        }
        if (!ResponseBuilder.isSuccess(entity)) {
            throw new SydException(entity.getError(), entity.getMsg());
        }
        VersionRsp result = BeanHelper.cloneBean(entity.getData(), VersionRsp.class);
        return result;
    }
}
