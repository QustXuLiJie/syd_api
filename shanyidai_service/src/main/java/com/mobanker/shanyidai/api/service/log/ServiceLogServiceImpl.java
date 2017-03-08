package com.mobanker.shanyidai.api.service.log;

import com.mobanker.shanyidai.api.business.log.ServiceLogBusiness;
import com.mobanker.shanyidai.api.dto.log.ApiServiceLog;
import com.mobanker.shanyidai.esb.common.logger.LogManager;
import com.mobanker.shanyidai.esb.common.logger.Logger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @desc:API系统service模块业务日志
 * @author: Richard Core
 * @create time: 2017/2/7 9:43
 */
@Service
public class ServiceLogServiceImpl implements ServiceLogService {
    public static final Logger LOGGER = LogManager.getSlf4jLogger(ServiceLogServiceImpl.class);
    @Resource
    private ServiceLogBusiness serviceLogBusiness;

    /**
     * @param log
     * @return void
     * @description API系统service模块业务日志
     * @author Richard Core
     * @time 2017/2/7 9:41
     * @method saveServiceLog
     */
    @Override
    public void saveServiceLog(ApiServiceLog log) {
        if (log == null) {
            LOGGER.warn("api工程service模块日志记录参数为空");
            return;
        }
        serviceLogBusiness.saveServiceLog(log);
    }

    /**
     * @description API系统保存业务流水
     * @author hantongyang
     * @time 2017/2/28 11:17
     * @method saveBusinessFlow
     * @param log
     * @return void
     */
    @Override
    public void saveBusinessFlow(ApiServiceLog log) {
        if(log == null){
            LOGGER.warn("api工程业务流水记录参数为空");
            return;
        }
        serviceLogBusiness.saveBusinessFlow(log);
    }
}
