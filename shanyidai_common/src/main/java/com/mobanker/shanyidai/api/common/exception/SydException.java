/**
 *
 */
package com.mobanker.shanyidai.api.common.exception;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;

/**
 * 业务异常处理类
 *
 * @author chenjianping
 * @data 2016年12月9日
 */
public class SydException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 8768211064581546909L;

    /**
     * 异常编码
     */
    public final String errCode;

    /**
     * 异常消息
     */
    public final String message;

    public SydException(Throwable e) {
        super(e);
        errCode = ReturnCode.SYS_EXCEPTION.getCode();
        message = e.getMessage();
    }

    public SydException(String errCode, String message) {
        super(message);
        this.errCode = errCode;
        this.message = message;
    }

    public SydException(String errCode, String message, Throwable e) {
        super(message, e);
        this.errCode = errCode;
        this.message = message;
    }

    public SydException(ReturnCode returnCode) {
        this(returnCode.getCode(), returnCode.getDesc());
    }

    public SydException(ReturnCode returnCode, Throwable e) {
        this(returnCode.getCode(), returnCode.getDesc(), e);
    }

}
