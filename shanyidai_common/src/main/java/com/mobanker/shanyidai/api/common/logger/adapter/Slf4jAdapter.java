package com.mobanker.shanyidai.api.common.logger.adapter;

import com.mobanker.shanyidai.api.common.logger.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Richard Core
 * @description
 * @date 2016/12/26 13:38
 */
public class Slf4jAdapter implements Logger {

    private org.slf4j.Logger log;

    public Slf4jAdapter(Class<?> clazz) {
        log = LoggerFactory.getLogger(clazz);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mobanker.microsite.common.logger.Logger#info(java.lang.String)
     */
    @Override
    public void info(String msg) {
        log.info(msg);
    }

    @Override
    public void info(Throwable ex) {
        log.info(ex.getMessage(), ex);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mobanker.microsite.common.logger.Logger#info(java.lang.String,
     * java.lang.Object[])
     */
    @Override
    public void info(String msg, Object... params) {
        log.info(msg, params);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mobanker.microsite.common.logger.Logger#info(java.lang.String,
     * java.lang.Throwable)
     */
    @Override
    public void info(String msg, Throwable ex) {
        log.info(msg, ex);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mobanker.microsite.common.logger.Logger#debug(java.lang.String)
     */
    @Override
    public void debug(String msg) {
        log.debug(msg);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.mobanker.microsite.common.logger.Logger#debug(java.lang.Throwable)
     */
    @Override
    public void debug(Throwable ex) {
        log.debug(ex.getMessage(), ex);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mobanker.microsite.common.logger.Logger#debug(java.lang.String,
     * java.lang.Object[])
     */
    @Override
    public void debug(String msg, Object... params) {
        log.debug(msg, params);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mobanker.microsite.common.logger.Logger#debug(java.lang.String,
     * java.lang.Throwable)
     */
    @Override
    public void debug(String msg, Throwable ex) {
        log.debug(msg, ex);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mobanker.microsite.common.logger.Logger#error(java.lang.String)
     */
    @Override
    public void error(String msg) {
        log.error(msg);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.mobanker.microsite.common.logger.Logger#error(java.lang.Throwable)
     */
    @Override
    public void error(Throwable ex) {
        log.error(ex.getMessage(), ex);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mobanker.microsite.common.logger.Logger#error(java.lang.String,
     * java.lang.Object[])
     */
    @Override
    public void error(String msg, Object... params) {
        log.error(msg, params);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mobanker.microsite.common.logger.Logger#error(java.lang.String,
     * java.lang.Throwable)
     */
    @Override
    public void error(String msg, Throwable ex) {
        log.error(msg, ex);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mobanker.microsite.common.logger.Logger#warn(java.lang.String)
     */
    @Override
    public void warn(String msg) {
        log.warn(msg);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.mobanker.microsite.common.logger.Logger#warn(java.lang.Throwable)
     */
    @Override
    public void warn(Throwable ex) {
        log.warn(ex.getMessage(), ex);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mobanker.microsite.common.logger.Logger#warn(java.lang.String,
     * java.lang.Throwable)
     */
    @Override
    public void warn(String msg, Throwable ex) {
        log.warn(msg, ex);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mobanker.microsite.common.logger.Logger#warn(java.lang.String,
     * java.lang.Object[])
     */
    @Override
    public void warn(String msg, Object... params) {
        log.warn(msg, params);
    }
}
