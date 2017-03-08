/**
 *
 */
package com.mobanker.shanyidai.api.business.terminal;

import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.dto.gather.AppOperationActivationReq;

/**
 * 用户终端操作行为业务声明
 *
 * @author chenjianping
 * @data 2016年12月11日
 */
public interface TerminalOperationBusiness {

    void appOperationActivation(AppOperationActivationReq appOperationActivationReq) throws SydException;

}
