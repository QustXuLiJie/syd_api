package com.mobanker.shanyidai.api.business.system;

import com.mobanker.shanyidai.api.dto.system.DefaultBank;

/**
 * @author hantongyang
 * @description
 * @time 2017/2/14 10:55
 */
public interface UserDefaultBankBusiness {

    /**
     * @description 根据用户ID查询用户默认银行卡信息
     * @author hantongyang
     * @time 2017/2/14 10:57
     * @method findDefaultBankByUserId
     * @param userId
     * @return com.mobanker.shanyidai.api.dto.system.DefaultBank
     */
    DefaultBank findDefaultBankByUserId(Long userId);

    /**
     * @description 设置用户入账银行卡
     * @author hantongyang
     * @time 2017/2/14 17:12
     * @method setDefaultBankByUserId
     * @param userId
     * @param bankCard
     * @return void
     */
    void setDefaultBankByUserId(Long userId, String bankCard);
}
