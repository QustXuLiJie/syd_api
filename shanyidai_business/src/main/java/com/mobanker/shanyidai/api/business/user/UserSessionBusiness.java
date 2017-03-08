package com.mobanker.shanyidai.api.business.user;

/**
 * @author hantongyang
 * @version 1.0
 * @description
 * @date 创建时间：2016/12/22 20:50
 */
public interface UserSessionBusiness {

    public Long checkUserSession(String code, String channel);
}
