package com.mobanker.shanyidai.api.business.user;

import com.mobanker.shanyidai.dubbo.service.user.UserSessionService;
import com.mobanker.shanyidai.esb.common.packet.ResponseEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hantongyang
 * @version 1.0
 * @description
 * @date 创建时间：2016/12/22 20:49
 */
@Service
public class UserSessionBusinessImpl implements UserSessionBusiness {

    @Resource
    private UserSessionService userSessionService;

    @Override
    public Long checkUserSession(String code, String channel) {
        ResponseEntity entity = null;
        //如果渠道为空，则根据CODE查询综合服务
        if(StringUtils.isBlank(channel)){
            entity = userSessionService.getUserSessionBy(code);
        }else{
            entity = userSessionService.getUserSessionBy(code, channel);
        }
        //判断结果是否为空
        if(entity != null && entity.getData() != null){
            return (Long) entity.getData();
        }
        return null;
    }
}
