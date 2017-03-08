package com.mobanker.shanyidai.api.business.user;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.dto.user.BankCard;
import com.mobanker.shanyidai.api.dto.user.BankCardIssuer;
import com.mobanker.shanyidai.api.dto.user.BankCardVerifyResult;
import com.mobanker.shanyidai.dubbo.dto.user.*;

import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @author: R.Core
 * @date 创建时间：2016-12-14
 * @parameter
 * @return
 */
public interface UserBusiness {

    /**
     * @description 根据用户ID查询用户详情
     * @author hantongyang
     * @time 2016/12/27 16:22
     * @method getUserInfoByUserId
     * @param userId
     * @param fields
     * @return Map<String, Object>
     */
    Map<String, Object> getUserInfoByUserId(Long userId, List<String> fields);

    /**
     * @description 根据用户查询用户联系人信息
     * @author hantongyang
     * @time 2016/12/28 17:37
     * @method getUserContactByUserId
     * @param dto
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     */
    List<Map<String, String>> getUserContactByUserId(UserContactDto dto);

    /**
     * @description 保存用户联系人信息
     * @author hantongyang
     * @time 2016/12/28 17:37
     * @method addUserContact
     * @param dto
     * @return java.util.Map<java.lang.String,java.lang.String>
     */
    Map<String, String> addUserContact(UserContactDto dto);

    Map<String, String> updateUserContact(UserContactDto dto);

    /**
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     *  返回map字段说明：
     * userId;//用户标识
     * cardNum;//银行卡号
     * cardBankName;//银行卡银行名称
     * cardMobile;//银行卡银行预留手机号
     * cardYstatus;//银行卡验证状态
     * @description 获取我的银行卡列表
     * @author Richard Core
     * @time 2016/12/21 11:02
     * @method listBankCard
     */
    List<Map<String, String>> listBankCard(SydRequest request);

    /**
     * @param dto 字段 字段名 必填 类型 说明
     *            accountNo 银行卡号 M String 卡号
     *            idCard 身份证号 C String 3要素必传
     *            name 姓名 M String 姓名
     *            phone 手机号 C String 4元素必填
     * @return com.mobanker.shanyidai.esb.common.packet.ResponseEntity
     * @description 银行卡认证
     * @author Richard Core
     * @time 2017/1/3 16:34
     * @method getBankCardVerify
     */
    public BankCardVerifyResult getBankCardVerify(BankCardDto dto);

    /**
     * @param param
     * @return
     * @description 添加银行卡
     * @author R.Core
     * @time 2016/12/20 20:14
     * @method getContactJob
     */
    void addBankCard(BankCardParamDto param);

    /**
     * @param dto
     * @return CardBinInfoDto
     * @description 根据卡号查询发卡行信息
     * @author Richard Core
     * @time 2017/1/4 15:06
     * @method getCardBinByCardNo
     */
    public BankCardIssuer getCardBinByCardNo(BankCardDto dto);
    /**
     * @param dto
     * @return List<BankCard>
     * @description 根据手机号或者银行卡号查询银行卡信息
     * @author Richard Core
     * @time 2017/1/4 17:29
     * @method getCardByPhoneOrCardNo
     */
    public List<BankCard> getCardByPhoneOrCardNo(BankCardDto dto);

    /**
     * @param phone
     * @param fields
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @description 根据手机号查询用户信息
     * @author Richard Core
     * @time 2016/12/28 14:00
     * @method getUserInfoByPhone
     */
    public Map<String, String> getUserInfoByPhone(String phone, String... fields);
    /**
     * @param phone
     * @param fields
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @description 根据手机号查询用户信息
     * @author Richard Core
     * @time 2016/12/28 14:01
     * @method getUserInfoByPhone
     */
    Map<String, String> getUserInfoByPhone(String phone, List<String> fields);
    /**
     * @param baseInfoDto
     * @return void
     * @description 更新用户信息
     * @author Richard Core
     * @time 2016/12/27 21:19
     * @method updateUserInfo
     */
    void updateUserInfo(UserBaseInfoDto baseInfoDto);

    /**
     * @description 根据身份证号获取用户信息
     * @author hantongyang
     * @time 2017/3/4 14:55
     * @method getUserInfoByCardNo
     * @param cardNo
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    Map<String, Object> getUserInfoByCardNo(String cardNo);
}
