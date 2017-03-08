package com.mobanker.shanyidai.api.business.system;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.logger.LogManager;
import com.mobanker.shanyidai.api.common.logger.Logger;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.dto.system.DefaultBank;
import com.mobanker.shanyidai.dubbo.service.system.UserDefaultBankDubboService;
import com.mobanker.shanyidai.esb.common.exception.EsbException;
import com.mobanker.shanyidai.esb.common.packet.ResponseBuilder;
import com.mobanker.shanyidai.esb.common.packet.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hantongyang
 * @description
 * @time 2017/2/14 10:57
 */
@Service
public class UserDefaultBankBusinessImpl implements UserDefaultBankBusiness {

    public static final Logger logger = LogManager.getSlf4jLogger(UserDefaultBankBusinessImpl.class);

    @Resource
    private UserDefaultBankDubboService defaultBankDubboService;

    /**
     * @description 根据用户ID查询用户默认银行卡信息
     * @author hantongyang
     * @time 2017/2/14 10:57
     * @method findDefaultBankByUserId
     * @param userId
     * @return com.mobanker.shanyidai.api.dto.system.DefaultBank
     */
    @Override
    public DefaultBank findDefaultBankByUserId(Long userId) {
        ResponseEntity response = null;
        try {
            response = defaultBankDubboService.findDefaultBankByUserId(userId);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        } catch (Exception e1) {
            logger.error("======根据用户ID查询用户默认银行卡信息失败", e1);
            throw new SydException(ReturnCode.ERROR_GET_DEFAULT_BANK);
        }
        if(!ResponseBuilder.isSuccess(response)){
            throw new SydException(response.getError(), response.getMsg());
        }
        DefaultBank bank = BeanHelper.cloneBean(response.getData(), DefaultBank.class);
        return bank;
    }

    /**
     * @description 设置用户入账银行卡
     * @author hantongyang
     * @time 2017/2/14 17:12
     * @method setDefaultBankByUserId
     * @param userId
     * @param bankCard
     * @return void
     */
    @Override
    public void setDefaultBankByUserId(Long userId, String bankCard) {
        ResponseEntity response = null;
        try {
            response = defaultBankDubboService.setDefaultBankByUserId(userId, bankCard);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        } catch (Exception e1) {
            logger.error("======设置用户入账银行卡失败", e1);
            throw new SydException(ReturnCode.ERROR_SET_DEFAULT_BANK);
        }
        if(!ResponseBuilder.isSuccess(response)){
            throw new SydException(response.getError(), response.getMsg());
        }
    }
}
