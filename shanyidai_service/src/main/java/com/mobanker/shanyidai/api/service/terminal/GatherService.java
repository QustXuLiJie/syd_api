package com.mobanker.shanyidai.api.service.terminal;


import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;

/**
 * @author Richard Core
 * @description 信息采集
 * @time 2016/12/1 14:05
 */
public interface GatherService {
    /**
     * @param request
     * @return java.lang.Object
     * @author xulijie
     * @method phoneRecordLast
     * @description 获取最后一次采集通话记录的时间
     * @time 15:13 2017/2/15
     */
    Object phoneRecordLast(SydRequest request) throws SydException;

    /**
     * @param request
     * @return java.lang.Object
     * @author xulijie
     * @method phoneSmsLast
     * @description 获取最后一次采集短信信息的时间
     * @time 15:13 2017/2/15
     */
    Object phoneSmsLast(SydRequest request) throws SydException;

    /**
     * @param request
     * @return java.lang.Object
     * @author xulijie
     * @method phoneContactLast
     * @description 获取最后一次采集通讯录信息的时间
     * @time 15:11 2017/2/15
     */
    Object phoneContactLast(SydRequest request) throws SydException;

    /**
     * @param request
     * @return void
     * @author xulijie
     * @method updatePhoneContact
     * @description
     * @time 16:39 2017/2/10
     */
    void updatePhoneContact(SydRequest request) throws SydException;

    /**
     * @param request
     * @return void
     * @author xulijie
     * @method updatePhoneSmsInfo
     * @description 短信信息采集
     * @time 15:27 2017/2/10
     */
    void updatePhoneSmsInfo(SydRequest request) throws SydException;

    /**
     * @param request
     * @return void
     * @author xulijie
     * @method updatePhoneCallrecords
     * @description 通话记录信息采集
     * @time 15:21 2017/2/10
     */
    void updatePhoneCallrecords(SydRequest request) throws SydException;

    /**
     * @param request
     * @return void
     * @description 保存设备采集信息
     * @author Richard Core
     * @time 2017/2/8 16:16
     * @method updateMobileDeviceInfo
     */
    void updateMobileDeviceInfo(SydRequest request);

    /**
     * @param request
     * @return void
     * @author xulijie
     * @method updateLbs
     * @description lbs信息采集
     * @time 15:02 2017/2/9
     */
    void updateLbs(SydRequest request) throws SydException;

    /**
     * @param request
     * @return void
     * @author xulijie
     * @method appOperationActivation
     * @description app激活信息
     * @time 10:28 2017/2/27
     */
    void appOperationActivation(SydRequest request) throws SydException;

}
