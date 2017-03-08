/**
 *
 */
package com.mobanker.shanyidai.api.business.terminal;

import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.logger.LogManager;
import com.mobanker.shanyidai.api.common.logger.Logger;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.dto.gather.AppOperationActivationReq;
import com.mobanker.shanyidai.dubbo.dto.gather.AppOperationActivationDto;
import com.mobanker.shanyidai.dubbo.service.gather.GatherDubboService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户终端操作行为业务实现
 *
 * @author chenjianping
 * @data 2016年12月11日
 */
@Service
public class TerminalOperationBusinessImpl implements TerminalOperationBusiness {

    private Logger logger = LogManager.getSlf4jLogger(getClass());
    @Resource
    private GatherDubboService gatherDubboService;

    /**
     * @param appOperationActivationReq
     * @return void
     * @author xulijie
     * @method appOperationActivation
     * @description app激活信息
     * @time 10:28 2017/2/27
     */
    @Override
    public void appOperationActivation(AppOperationActivationReq appOperationActivationReq) throws SydException {
        //验证参数
        if (appOperationActivationReq == null) {
            logger.warn("app激活信息为空");
            return;
        }
        try {
            AppOperationActivationDto appOperationActivationDto = BeanHelper.cloneBean(appOperationActivationReq, AppOperationActivationDto.class);
            gatherDubboService.appOperationActivation(appOperationActivationDto);
        } catch (Exception e) {
            logger.warn("app激活信息采集失败" + e);
        }
    }
}
