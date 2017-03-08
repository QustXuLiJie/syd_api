package com.mobanker.shanyidai.api.service.security;

import com.alibaba.fastjson.JSON;
import com.mobanker.shanyidai.api.business.redis.RedisBusiness;
import com.mobanker.shanyidai.api.common.constant.AppConstants;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.enums.RedisKeyEnum;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.logger.LogManager;
import com.mobanker.shanyidai.api.common.logger.Logger;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.common.security.RSA;
import com.mobanker.shanyidai.api.common.security.SHA256;
import com.mobanker.shanyidai.api.common.security.ThreeDes;
import com.mobanker.shanyidai.api.common.tool.DateKit;
import com.mobanker.shanyidai.api.common.tool.StringKit;
import com.mobanker.shanyidai.api.dto.security.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 所有握手信息处理
 *
 * @version 1.0
 * @author: hanty
 * @date 创建时间：2016年8月23日
 * @parameter
 * @return
 */
@Service
public class SecurityServiceImpl implements SecurityService {
    private Logger logger = LogManager.getSlf4jLogger(getClass());
    @Resource
    private RedisBusiness redisBusiness;

    /**
     * @return
     * @author: hanty
     * @date 创建时间 2016-12-13 10:39
     * @version 1.0
     * @parameter
     */
    @Override
    public FirstShakeInfo firstShake(SydRequest request) throws SydException {
        // TODO Auto-generated method stub
        FirstShakeReq shakeReq = JSON.parseObject(request.getContent(), FirstShakeReq.class);
        //校验
        if (!StringKit.checkStringLength(shakeReq.getNum1(), 32)) {
            throw new SydException(ReturnCode.FIRST_SHAKE_ERROR.getCode(), ReturnCode.FIRST_SHAKE_ERROR.getDesc(), null);
        }
        FirstShakeInfo firstShake = new FirstShakeInfo();
        firstShake.setNowTime(DateKit.getNowTime());
        firstShake.setNum2(StringKit.getUUID());
        firstShake.setUuid(StringKit.getUUID());
        firstShake.setPublicKey(AppConstants.RSA.PUBLIC_KEY);
        firstShake.setNum1(shakeReq.getNum1());
        redisBusiness.setValue(RedisKeyEnum.SYD_SECURITY_UUID.getValue() + firstShake.getUuid(), JSON.toJSONString(firstShake), firstShake.getEffecttime());
        firstShake.setNum1(null);
        return firstShake;
    }

    /**
     * @return
     * @author: hanty
     * @date 创建时间 2016-12-13 10:39
     * @version 1.0
     * @parameter
     */
    @Override
    public SecondShakeInfo secondShake(SydRequest request) throws SydException {
        // TODO Auto-generated method stub
        SecondShakeReq shakeReq = JSON.parseObject(request.getContent(), SecondShakeReq.class);
        //有校验
        if (!StringKit.checkStringLength(shakeReq.getUuid(), 32)) {
            throw new SydException(ReturnCode.SECOND_SHAKE_ERROR.getCode(), ReturnCode.SECOND_SHAKE_ERROR.getDesc(), null);
        }
        String msg = redisBusiness.getValue(RedisKeyEnum.SYD_SECURITY_UUID.getValue() + shakeReq.getUuid());
        if (StringUtils.isBlank(msg)) {//判断uuid是否存在
            throw new SydException(ReturnCode.SECOND_SHAKE_ERROR.getCode(), ReturnCode.SECOND_SHAKE_ERROR.getDesc(), null);
        }
        FirstShakeInfo firstShake = JSON.parseObject(msg, FirstShakeInfo.class);
        //判断是否超时
        if (DateKit.getNowTime() > firstShake.getNowTime() + firstShake.getEffecttime()) {

            throw new SydException(ReturnCode.SECOND_SHAKE_ERROR.getCode(), ReturnCode.SECOND_SHAKE_ERROR.getDesc(), null);
        }
        //验签
        String sha256 = firstShake.getNum1().substring(0, firstShake.getTailor()) + "," + shakeReq.getNum3();
        String serverSign = SHA256.getSHA256(sha256);
        if (!serverSign.equals(shakeReq.getSign())) {//验签失败
            logger.error("secondShake 验签失败");
            throw new SydException(ReturnCode.SECOND_SHAKE_ERROR.getCode(), ReturnCode.SECOND_SHAKE_ERROR.getDesc(), null);

        }
        //解密num3
        String serverNum3 = RSA.decryptByPrivateKey(shakeReq.getNum3(), AppConstants.RSA.PRIVATE_KEY);
        if (serverNum3 == null) {//解密失败
            logger.error("secondShake 解密失败");
            throw new SydException(ReturnCode.SECOND_SHAKE_ERROR.getCode(), ReturnCode.SECOND_SHAKE_ERROR.getDesc(), null);
        }

        SecondShakeInfo secondShake = new SecondShakeInfo();
        secondShake.setSecret(StringKit.getUUID());
        String serverCode = StringKit.getUUID();
        try {
            secondShake.setSignData(ThreeDes.encryptThreeDESECB(serverCode, secondShake.getSecret() + serverNum3));
        } catch (Exception e) {
            throw new SydException(ReturnCode.SECOND_SHAKE_ERROR.getCode(), ReturnCode.SECOND_SHAKE_ERROR.getDesc(), null);
        }
        ShakeInfo shakeInfo = new ShakeInfo();
        shakeInfo.setCode(serverCode);
        shakeInfo.setKey(firstShake.getNum1() + firstShake.getNum2() + serverNum3);
        shakeInfo.setNowTime(DateKit.getNowTime());
        redisBusiness.setValue(RedisKeyEnum.SYD_SECURITY_SIGN.getValue() + serverCode, JSON.toJSONString(shakeInfo), shakeInfo.getEffecttime());
        redisBusiness.delValue(RedisKeyEnum.SYD_SECURITY_UUID.getValue() + shakeReq.getUuid());
        return secondShake;
    }

    /**
     * 从redis根据uuid来去加密信息
     *
     * @return
     * @author: hanty
     * @date 创建时间 2016-12-13 10:39
     * @version 1.0
     * @parameter
     */
    @Override
    public ShakeInfo getThreeDesKey(String uuid) {
        // TODO Auto-generated method stub
        String msg = redisBusiness.getValue(RedisKeyEnum.SYD_SECURITY_SIGN.getValue() + uuid);
        if (StringUtils.isBlank(msg)) {
            return null;
        }
        ShakeInfo shakeInfo = JSON.parseObject(msg, ShakeInfo.class);
        redisBusiness.setValue(RedisKeyEnum.SYD_SECURITY_SIGN.getValue() + uuid, JSON.toJSONString(shakeInfo), shakeInfo.getEffecttime());
        return shakeInfo;
    }
}
