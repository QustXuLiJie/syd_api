package com.mobanker.shanyidai.api.service.user;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.dto.system.DefaultBank;
import com.mobanker.shanyidai.api.dto.user.*;
import com.mobanker.shanyidai.dubbo.dto.user.CardBinInfoDto;
import com.mobanker.shanyidai.dubbo.dto.user.UserBaseInfoDto;

import java.util.List;

/**
 * @version 1.0
 * @author: R.Core
 * @date 创建时间：2016年12月15日
 * @parameter
 * @return
 */
public interface UserService {

    /**
     * 根据code来获取userid
     *
     * @return
     * @author: R.Core
     * @date 创建时间：2016年12月15日
     * @version 1.0
     * @parameter
     */
    public Long getUserCode(String code);

    /**
     * 设置code和UserId保存到redis中
     *
     * @return
     * @author: R.Core
     * @date 创建时间：2016年12月15日
     * @version 1.0
     * @parameter
     */
    public void addUserCode(String code, Long userId);

    /**
     * 从redis中删除指定code的记录
     *
     * @return
     * @author: R.Core
     * @date 创建时间：2016年12月15日
     * @version 1.0
     * @parameter
     */
    public void removeUserCode(String code);

    /**
     * @param request
     * @return
     * @description 保存联系人信息
     * @author hantongyang
     * @time 2016/12/20 15:25
     * @method addContact
     */
    void addContact(SydRequest request);

    /**
     * @param request
     * @return
     * @description 保存单位信息
     * @author hantongyang
     * @time 2016/12/20 15:25
     * @method addJob
     */
    void addJob(SydRequest request);

    /**
     * @param userId
     * @return com.mobanker.shanyidai.api.dto.user.UserContactRsp
     * @description 查询联系人信息
     * @author hantongyang
     * @time 2016/12/20 20:14
     * @method getContact
     */
    UserContactRsp getContact(Long userId);

    /**
     * @param userId
     * @return com.mobanker.shanyidai.api.dto.user.UserJobRsp
     * @description 查询单位信息
     * @author hantongyang
     * @time 2016/12/20 20:14
     * @method getContact
     */
    UserJobRsp getJob(Long userId);

    UserContactJobRsp getContactJob(Long userId);

    /**
     * @param request
     * @param appVersion
     * @return
     * @description 添加银行卡
     * @author R.Core
     * @time 2016/12/20 20:14
     * @method getContactJob
     */
    void addBankCard(SydRequest request, String appVersion);

    /**
     * @param request
     * @return List<BankCard>
     * @description 获取我的银行卡列表
     * @author Richard Core
     * @time 2016/12/21 11:02
     * @method listBankCard
     */
    List<BankCard> listBankCard(SydRequest request);

    /**
     * @param request
     * @return void
     * @description 绑定银行卡 选择入账银行卡
     * @author Richard Core
     * @time 2016/12/21 11:03
     * @method setPayCard
     */
    void setPayCard(SydRequest request);

    /**
     * @param userId
     * @return com.mobanker.shanyidai.api.dto.user.UserBasicInfoRsp
     * @description 根据用户ID查询用户详情，先取缓存中的用户详情，缓存中没有再调用接口获取
     * @author hantongyang
     * @time 2016/12/27 16:46
     * @method getUserInfoByUserId
     */
    UserBasicInfoRsp getUserInfoByUserId(Long userId);

    /**
     * @param userId
     * @param fields
     * @return com.mobanker.shanyidai.api.dto.user.UserBasicInfoRsp
     * @description 根据用户ID查询用户详情 不走缓存
     * @author hantongyang
     * @time 2016/12/27 16:46
     * @method getUserInfoByUserId
     */
    public UserBasicInfoRsp getUserInfoByUserId(Long userId, String... fields);

    /**
     * @param baseInfoDto
     * @return com.mobanker.shanyidai.api.dto.user.UserBasicInfoRsp
     * @description 更新用户信息
     * @author Richard Core
     * @time 2016/12/27 21:22
     * @method updateUserInfo
     */
    public void updateUserInfo(UserBaseInfoDto baseInfoDto);

    /**
     * @param request
     * @return String
     * @description 根据卡号查询发卡行信息
     * @author Richard Core
     * @time 2017/1/4 16:32
     * @method getBankName
     */
    public BankCardIssuer getBankName(SydRequest request);

    /**
     * @param request
     * @return java.lang.Integer
     * @description 获取银行卡数量
     * @author Richard Core
     * @time 2017/1/24 11:55
     * @method bankCardAmount
     */
    Integer bankCardAmount(SydRequest request);

    /**
     * @description 根据用户ID获取用户默认入账银行卡
     * @author hantongyang
     * @time 2017/2/14 11:04
     * @method getDefaultBankByUserId
     * @param request
     * @return com.mobanker.shanyidai.api.dto.system.DefaultBank
     */
    DefaultBank getDefaultBankByUserId(SydRequest request);

    /**
     * @description 验证用户身份证号是否已经实名认证
     * @author hantongyang
     * @time 2017/3/4 15:13
     * @method checkCardNo
     * @param cardNo
     * @return boolean
     */
    UserBasicInfoRsp checkCardNo(String cardNo);
}
