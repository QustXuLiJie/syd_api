package com.mobanker.shanyidai.api.service.log;

import com.mobanker.shanyidai.api.dto.log.ApiServiceLog;

/**
 * @desc:API系统service模块业务日志
 * @author: Richard Core
 * @create time: 2017/2/7 9:43
 */
public interface ServiceLogService {
    /**
     * @param log
     * @return void
     * @description API系统service模块业务日志
     * @author Richard Core
     * @time 2017/2/7 9:41
     * @method saveServiceLog
     */
    void saveServiceLog(ApiServiceLog log);

    /**
     * @description API系统保存业务流水
     * @author hantongyang
     * @time 2017/2/28 11:17
     * @method saveBusinessFlow
     * @param log
     * @return void
     */
    void saveBusinessFlow(ApiServiceLog log);
}
