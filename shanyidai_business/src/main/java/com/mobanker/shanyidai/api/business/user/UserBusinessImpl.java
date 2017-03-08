package com.mobanker.shanyidai.api.business.user;


import com.alibaba.fastjson.JSONObject;
import com.mobanker.shanyidai.api.business.common.CommonBusiness;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.logger.LogManager;
import com.mobanker.shanyidai.api.common.logger.Logger;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.dto.user.BankCard;
import com.mobanker.shanyidai.api.dto.user.BankCardIssuer;
import com.mobanker.shanyidai.api.dto.user.BankCardVerifyResult;
import com.mobanker.shanyidai.api.enums.BankCardTypeEnum;
import com.mobanker.shanyidai.dubbo.dto.user.*;
import com.mobanker.shanyidai.dubbo.service.user.BankCardDubboService;
import com.mobanker.shanyidai.dubbo.service.user.UserContactDubboService;
import com.mobanker.shanyidai.dubbo.service.user.UserDubboService;
import com.mobanker.shanyidai.esb.common.constants.ErrorConstant;
import com.mobanker.shanyidai.esb.common.exception.EsbException;
import com.mobanker.shanyidai.esb.common.packet.ResponseBuilder;
import com.mobanker.shanyidai.esb.common.packet.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * user调dubbo
 *
 * @version 1.0
 * @author: liuyafei
 * @date 创建时间：2016年8月17日
 * @parameter
 * @return
 */
@Component
public class UserBusinessImpl implements UserBusiness {
    private Logger logger = LogManager.getSlf4jLogger(getClass());

    @Resource
    private CommonBusiness commonBusiness;
    @Resource
    private UserDubboService dubboUserService;
    @Resource
    private UserContactDubboService dubboUserContactService;
    @Resource
    private BankCardDubboService bankCardDubboService;


    /**
     * @param userId
     * @param fields
     * @return Map<String, Object>
     * @description 根据用户ID查询用户详情
     * @author hantongyang
     * @time 2016/12/27 16:22
     * @method getUserInfoByUserId
     */
    @Override
    public Map<String, Object> getUserInfoByUserId(Long userId, List<String> fields) {
        ResponseEntity result = null;
        try {
            result = dubboUserService.getUserInfoByUserId(userId, null);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        }
        if (!ResponseBuilder.isSuccess(result)) {
            throw new SydException(result.getError(), result.getMsg());
        }
        if (result.getData() == null) {
            return null;
        }
        //3、返回实名认证信息，并保存到缓存中
        Map<String, Object> map = (Map<String, Object>) result.getData();
        return map;
    }

    /**
     * @param dto
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     * @description 根据用户查询用户联系人信息
     * @author hantongyang
     * @time 2016/12/28 17:37
     * @method getUserContactByUserId
     */
    @Override
    public List<Map<String, String>> getUserContactByUserId(UserContactDto dto) {
        ResponseEntity result = dubboUserContactService.getContactByUserId(dto);
        if (!ResponseBuilder.isSuccess(result)) {
            throw new SydException(result.getError(), result.getMsg());
        }
        List<Map<String, String>> list = (List<Map<String, String>>) result.getData();
        return list;
    }

    /**
     * @param dto
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @description 保存用户联系人信息
     * @author hantongyang
     * @time 2016/12/28 17:37
     * @method addUserContact
     */
    @Override
    public Map<String, String> addUserContact(UserContactDto dto) {
        ResponseEntity result = null;
        try {
            result = dubboUserContactService.addContact(dto);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        }
        if (!ResponseBuilder.isSuccess(result)) {
            throw new SydException(result.getError(), result.getMsg());
        }
        Map<String, String> map = (Map<String, String>) result.getData();
        return map;
    }

    /**
     * @param dto
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @description 修改联系人信息
     * @author hantongyang
     * @time 2016/12/28 17:38
     * @method updateUserContact
     */
    @Override
    public Map<String, String> updateUserContact(UserContactDto dto) {
        ResponseEntity result = null;
        try {
            result = dubboUserContactService.updateContact(dto);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        }
        if (!ResponseBuilder.isSuccess(result)) {
            throw new SydException(result.getError(), result.getMsg());
        }
        Map<String, String> map = (Map<String, String>) result.getData();
        return map;
    }

    /**
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * 返回map字段说明：
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
    @Override
    public List<Map<String, String>> listBankCard(SydRequest request) {
        BankCardParamDto dto = BeanHelper.cloneBean(request, BankCardParamDto.class);
        dto.setUserId(request.getUserId());
        dto.setType(BankCardTypeEnum.DEBIT_CARD.getType());
        ResponseEntity result = null;
        try {
            result = bankCardDubboService.getBankCardList(dto);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        }
        if (!ResponseBuilder.isSuccess(result)) {
            logger.error("查询用户的银行卡列表失败", result);
            throw new SydException(result.getError(), result.getMsg());
        }
        return (List<Map<String, String>>) result.getData();
    }
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
    @Override
    public BankCardVerifyResult getBankCardVerify(BankCardDto dto){
        ResponseEntity result = null;
        try {
            result = bankCardDubboService.getBankCardVerify(dto);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        }
        if (!ResponseBuilder.isSuccess(result)) {
            logger.error("银行卡认证失败", result);
            throw new SydException(result.getError(), result.getMsg());
        }
        //处理返回结果
        if (result.getData() == null) {
            throw new SydException(ReturnCode.BANKCARD_VERIFY_EXCEPTION);
        }
        BankCardVerifyResult data = null;
        try {
            JSONObject jsonObject = (JSONObject) result.getData();
            String s = jsonObject.toJSONString();
            data = JSONObject.parseObject(s,BankCardVerifyResult.class);
        } catch (Exception e) {
            throw new SydException(ReturnCode.BANKCARD_VERIFY_EXCEPTION);
        }
        return data;
    }
    /**
     * @param param
     * @return
     * @description 添加银行卡
     * @author R.Core
     * @time 2016/12/20 20:14
     * @method getContactJob
     */
    @Override
    public void addBankCard(BankCardParamDto param) {
        ResponseEntity result = null;
        try {
            result = bankCardDubboService.addCardInfoByUserId(param);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        }
        if (!ResponseBuilder.isSuccess(result)) {
            logger.error("添加银行卡失败", result);
            throw new SydException(result.getError(), result.getMsg());
        }
    }

    /**
     * @param dto
     * @return CardBinInfoDto
     * @description 根据卡号查询发卡行信息
     * @author Richard Core
     * @time 2017/1/4 15:06
     * @method getCardBinByCardNo
     */
    @Override
    public BankCardIssuer getCardBinByCardNo(BankCardDto dto) {
        ResponseEntity result = null;
        try {
            result = bankCardDubboService.getCardBinByCardNo(dto);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        }
        if (!ResponseBuilder.isSuccess(result)) {
            logger.error("根据卡号获取发卡行信息失败", result);
            throw new SydException(result.getError(), result.getMsg());
        }
        if (result == null) {
            throw new SydException(ReturnCode.BANK_NAME_ERROR);
        }
        CardBinInfoDto cardBinInfo = (CardBinInfoDto) result.getData();

        BankCardIssuer issuer = new BankCardIssuer();
        issuer.setBankName(cardBinInfo.getBankName());
        issuer.setBankCode(cardBinInfo.getShortName());
        return issuer;
    }
    /**
     * @param dto
     * @return List<BankCard>
     * @description 根据手机号或者银行卡号查询银行卡信息
     * @author Richard Core
     * @time 2017/1/4 17:29
     * @method getCardByPhoneOrCardNo
     */
    @Override
    public List<BankCard> getCardByPhoneOrCardNo(BankCardDto dto) {
        ResponseEntity result = null;
        try {
            result = bankCardDubboService.getCardByPhoneOrCardNo(dto);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        }
        if (!ResponseBuilder.isSuccess(result)) {
            logger.error("添加银行卡失败", result);
            throw new SydException(result.getError(), result.getMsg());
        }


        List<BankCard> bankCardList = getBankCardBeans(result);
        return bankCardList;
    }

    /**
     * @param result
     * @return java.util.List<com.mobanker.shanyidai.api.dto.user.BankCard>
     * @description 返回值封装 将map中的返回信息封装到bean中
     * @author Richard Core
     * @time 2017/1/4 17:46
     * @method getBankCardBeans
     */
    private List<BankCard> getBankCardBeans(ResponseEntity result) {
        List<BankCard> bankCardList = new ArrayList<BankCard>();
        if (result == null || result.getData() == null) {
            return bankCardList;
        }
        List<Map<String, String>> mapList = (List<Map<String, String>>) result.getData();
        if (mapList.isEmpty()) {
            return bankCardList;
        }
        for (Map<String, String> map : mapList) {
            if (map == null || map.isEmpty()) {
                continue;
            }
            BankCard bankCard = new BankCard();
            try {
                bankCard.setCardYstatus(Integer.valueOf(map.get("cardYstatus")));
            } catch (NumberFormatException e) {
                bankCard.setCardYstatus(0);
            }
            bankCard.setBankName(map.get("cardBankName"));
            bankCard.setBankCardNo(map.get("cardNum"));
            bankCard.setBankCardId(map.get("cardId"));
            bankCard.setPhone(map.get("cardMobile"));
            bankCardList.add(bankCard);
        }
        return bankCardList;
    }

    /**
     * @param baseInfoDto
     * @return void
     * @description 更新用户信息
     * @author Richard Core
     * @time 2016/12/27 21:19
     * @method updateUserInfo
     */
    @Override
    public void updateUserInfo(UserBaseInfoDto baseInfoDto) {
        ResponseEntity responseEntity = null;
        try {
            responseEntity = dubboUserService.updateUserInfo(baseInfoDto);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message, e);
        } catch (Exception e) {
            throw new SydException(ReturnCode.SERVICE_VALID, e);
        }
        if (!ResponseBuilder.isSuccess(responseEntity)) {
            throw new SydException(responseEntity.getError(), responseEntity.getMsg());
        }
//        if (responseEntity.getData() == null) {
//            throw new SydException(ReturnCode.UPDATE_USER_FAIL);
//        }
//        Map<String, String> userInfo = (Map<String, String>) responseEntity.getData();
//
//        return userInfo;
    }

    /**
     * @param phone
     * @param fields
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @description 根据手机号查询用户信息
     * @author Richard Core
     * @time 2016/12/28 14:01
     * @method getUserInfoByPhone
     */
    @Override
    public Map<String, String> getUserInfoByPhone(String phone, List<String> fields) {
        ResponseEntity responseEntity = null;
        try {
            //fields中userId参数不用加，直接可以获取到
            responseEntity = dubboUserService.getUserInfoByPhone(phone, fields);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message, e);
        } catch (Exception e) {
            throw new SydException(ReturnCode.GET_USERINFO_FAILED, e);
        }
        if (!ResponseBuilder.isSuccess(responseEntity)) {
            if (ErrorConstant.PHONE_UNREGIST.getCode().equals(responseEntity.getError())) {
                throw new SydException(ReturnCode.PHONE_UNREGISTED.getCode(), responseEntity.getMsg());
            }
            throw new SydException(ReturnCode.GET_USERINFO_FAILED.getCode(), responseEntity.getMsg());
        }
        Map<String, String> userInfo = (Map<String, String>) responseEntity.getData();
        return userInfo;
    }

    /**
     * @param phone
     * @param fields
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @description 根据手机号查询用户信息
     * @author Richard Core
     * @time 2016/12/28 14:00
     * @method getUserInfoByPhone
     */
    @Override
    public Map<String, String> getUserInfoByPhone(String phone, String... fields) {
        return getUserInfoByPhone(phone, Arrays.asList(fields));
    }

    /**
     * @description 根据身份证号获取用户信息
     * @author hantongyang
     * @time 2017/3/4 14:55
     * @method getUserInfoByCardNo
     * @param cardNo
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @Override
    public Map<String, Object> getUserInfoByCardNo(String cardNo) {
        ResponseEntity responseEntity = null;
        try {
            //fields中userId参数不用加，直接可以获取到
            responseEntity = dubboUserService.getUserInfoByCardNo(cardNo);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message, e);
        } catch (Exception e) {
            throw new SydException(ReturnCode.GET_USER_INFO_BY_CARDNO_ERROR, e);
        }
        if (!ResponseBuilder.isSuccess(responseEntity)) {
            throw new SydException(ReturnCode.GET_USER_INFO_BY_CARDNO_ERROR.getCode(), responseEntity.getMsg());
        }
        Map<String, Object> userInfo = (Map<String, Object>) responseEntity.getData();
        return userInfo;
    }
}
