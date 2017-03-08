package com.mobanker.shanyidai.api.business.log;

import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.dto.log.ApiServiceLog;
import com.mobanker.shanyidai.dubbo.dto.log.ApiServiceLogDto;
import com.mobanker.shanyidai.dubbo.service.log.ApiServiceLogDubboService;
import com.mobanker.shanyidai.esb.common.logger.LogManager;
import com.mobanker.shanyidai.esb.common.logger.Logger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @desc:
 * @author: Richard Core
 * @create time: 2017/2/6 18:02
 */
@Service
public class ServiceLogBusinessImpl implements ServiceLogBusiness {
    public static final Logger LOGGER = LogManager.getSlf4jLogger(ServiceLogBusinessImpl.class);
    @Resource
    private ApiServiceLogDubboService apiServiceLogDubboService;

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
        try {
            ApiServiceLogDto logDto = BeanHelper.cloneBean(log, ApiServiceLogDto.class);
            apiServiceLogDubboService.saveLog(logDto);
        } catch (Exception e) {
            LOGGER.warn("api工程service模块日志记录异常" + e);
        }
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
        try {
            ApiServiceLogDto logDto = BeanHelper.cloneBean(log, ApiServiceLogDto.class);
            apiServiceLogDubboService.saveBusinessFlow(logDto);
        } catch (Exception e) {
            LOGGER.warn("api工程业务流水记录异常" + e);
        }
    }
}
