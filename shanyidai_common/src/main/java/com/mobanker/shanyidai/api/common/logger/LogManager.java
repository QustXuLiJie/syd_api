/**
 *
 */
package com.mobanker.shanyidai.api.common.logger;

import com.mobanker.shanyidai.api.common.logger.adapter.Log4jAdapter;
import com.mobanker.shanyidai.api.common.logger.adapter.Slf4jAdapter;

/**
 * 日志管理工厂类
 *
 * @author chenjianping
 * @data 2016年12月12日
 */
public class LogManager {
    public static Logger getLogger(Class<?> clazz) {
        return new Log4jAdapter(clazz);
    }

    /**
     * @param clazz
     * @return com.mobanker.shanyidai.esb.common.logger.Logger
     * @description slf4j 日志统一封装
     * @author Richard Core
     * @time 2016/12/26 13:45
     * @method getSlf4jLogger
     */
    public static Logger getSlf4jLogger(Class<?> clazz) {
        return new Slf4jAdapter(clazz);
    }
}
