package com.mobanker.shanyidai.api.business.gather;


import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.dto.gather.*;
import com.mobanker.shanyidai.dubbo.dto.gather.MongoCallRecordsDto;


/**
 * @author Richard Core
 * @description 信息采集
 * @time 2016/12/1 14:05
 */
public interface GatherBusiness {

    String phoneRecordLast( Long userId) throws SydException;

    String phoneSmsLast(Long userId) throws SydException;

    String phoneContactLast(Long userId) throws SydException;

    void updatePhoneContact(MongoPhoneBook mongoPhoneBook) throws SydException;

    void updatePhoneCallrecords(MongoCallRecords mongoCallRecords) throws SydException;

    void updatePhoneSmsInfo(MongoSmsInfo mongoSmsInfo) throws SydException;

    void updateMobileDeviceInfo(MongoMobile mongoMobile);

    void updateLbs(MongoLbs mongoLbs) throws SydException;

}
