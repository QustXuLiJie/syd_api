package com.mobanker.shanyidai.api.business.gather;


import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.logger.LogManager;
import com.mobanker.shanyidai.api.common.logger.Logger;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.common.tool.DateKit;
import com.mobanker.shanyidai.api.dto.gather.*;
import com.mobanker.shanyidai.dubbo.dto.gather.*;
import com.mobanker.shanyidai.dubbo.service.gather.GatherDubboService;
import com.mobanker.shanyidai.esb.common.packet.ResponseBuilder;
import com.mobanker.shanyidai.esb.common.packet.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Richard Core
 * @description 信息采集
 * @time 2016/12/1 14:05
 */
@Service
public class GatherBusinessImpl implements GatherBusiness {
    private Logger logger = LogManager.getSlf4jLogger(getClass());

    @Resource
    private GatherDubboService gatherDubboService;

    /**
     * @param userId
     * @return java.lang.String
     * @author xulijie
     * @method phoneRecordLast
     * @description 获取最后一次采集通话记录的时间
     * @time 15:18 2017/2/15
     */
    @Override
    public String phoneRecordLast(Long userId) throws SydException {
        ResponseEntity response = null;
        try {
            response = gatherDubboService.getLastCallRecords(userId);
        } catch (Exception e) {
            logger.warn("获取最后一次采集通话记录的时间失败" + e);
        }
        if (!ResponseBuilder.isFinished(response)) {
            throw new SydException(response.getError(), response.getMsg());
        }
        //获取data
        String data = response.getData().toString();
        return data;
    }

    /**
     * @param userId
     * @return java.lang.String
     * @author xulijie
     * @method phoneSmsLast
     * @description 获取最后一次采集短信的时间
     * @time 15:17 2017/2/15
     */
    @Override
    public String phoneSmsLast(Long userId) throws SydException {
        ResponseEntity response = null;
        try {
            response = gatherDubboService.getLastSms(userId);
        } catch (Exception e) {
            logger.warn("获取最后一次采集短信的时间失败" + e);
        }
        if (!ResponseBuilder.isFinished(response)) {
            throw new SydException(response.getError(), response.getMsg());
        }
        String data = response.getData().toString();
        return data;
    }

    /**
     * @param userId
     * @return java.lang.String
     * @author xulijie
     * @method phoneContactLast
     * @description 获取最后一次采集通讯录信息的时间
     * @time 11:06 2017/2/14
     */
    @Override
    public String phoneContactLast(Long userId) throws SydException {
        ResponseEntity response = null;
        try {
            response = gatherDubboService.getLastContact(userId);
        } catch (Exception e) {
            logger.warn("获取最后一次采集通讯录的时间失败" + e);
        }
        if (!ResponseBuilder.isFinished(response)) {
            throw new SydException(response.getError(), response.getMsg());
        }
        String data = response.getData().toString();
        return data;
    }

    /**
     * @param mongoPhoneBook
     * @return void
     * @author xulijie
     * @method updatePhoneContact
     * @description 通讯录采集
     * @time 16:42 2017/2/10
     */
    @Override
    public void updatePhoneContact(MongoPhoneBook mongoPhoneBook) throws SydException {
        logger.debug("enter updatePhoneContact method, mongoPhoneBook:[{}]", mongoPhoneBook);
        //验证参数
        if (mongoPhoneBook == null) {
            logger.warn("需要添加的通讯录信息为空");
            return;
        }
        try {
            MongoPhoneBookDto mongoCallRecordsDto = BeanHelper.cloneBean(mongoPhoneBook, MongoPhoneBookDto.class);
            mongoCallRecordsDto.setAddtime(DateKit.getNowTime());
            gatherDubboService.savePhoneBook(mongoCallRecordsDto);
        } catch (Exception e) {
            logger.warn("添加采集的通讯录信息失败" + e);
        }
        logger.debug("exit updatePhoneContact method");
    }

    /**
     * @param mongoCallRecords
     * @return void
     * @author xulijie
     * @method updatePhoneCallrecords
     * @description 通话记录采集
     * @time 15:20 2017/2/10
     */
    @Override
    public void updatePhoneCallrecords(MongoCallRecords mongoCallRecords) throws SydException {
        logger.debug("enter updatePhoneCallrecords method, mongoCallRecords:[{}]", mongoCallRecords);
        //验证参数
        if (mongoCallRecords == null) {
            logger.warn("需要添加的通话记录信息为空");
            return;
        }
        try {
            MongoCallRecordsDto mongoCallRecordsDto = BeanHelper.cloneBean(mongoCallRecords, MongoCallRecordsDto.class);
            mongoCallRecordsDto.setAddtime(DateKit.getNowTime());
            gatherDubboService.saveCallRecords(mongoCallRecordsDto);
        } catch (Exception e) {
            logger.warn("添加采集的通话记录信息失败" + e);
        }
        logger.debug("exit updatePhoneCallrecords method");
    }

    /**
     * @param mongoPhoneSmsInfo
     * @return void
     * @author xulijie
     * @method updatePhoneSmsInfo
     * @description
     * @time 15:42 2017/2/10
     */
    @Override
    public void updatePhoneSmsInfo(MongoSmsInfo mongoPhoneSmsInfo) throws SydException {
        logger.debug("enter updatePhoneSmsInfo method, mongoPhoneSmsInfo:[{}]", mongoPhoneSmsInfo);
        //验证参数
        if (mongoPhoneSmsInfo == null) {
            logger.warn("需要添加的短信信息为空");
            return;
        }
        try {
            MongoSmsInfoDto mongoSmsInfoDto = BeanHelper.cloneBean(mongoPhoneSmsInfo, MongoSmsInfoDto.class);
            mongoSmsInfoDto.setAddtime(DateKit.getNowTime());
            gatherDubboService.savaSmsInfo(mongoSmsInfoDto);
        } catch (Exception e) {
            logger.warn("添加采集的短信信息失败" + e);
        }
        logger.debug("exit updatePhoneSmsInfo method");
    }

    /**
     * @param mongoMobile
     * @return void
     * @description 设备信息采集
     * @author Richard Core
     * @time 2017/2/8 16:07
     * @method updateMobileDeviceInfo
     */
    @Override
    public void updateMobileDeviceInfo(MongoMobile mongoMobile) {
        if (mongoMobile == null) {
            logger.warn("需要添加的设备信息为空");
            return;
        }
        try {
            MongoMobileDto mongoMobileDto = BeanHelper.cloneBean(mongoMobile, MongoMobileDto.class);
//            mongoMobileDto.setAdd_product(mongoMobile.getProduct());
//            mongoMobileDto.setAdd_channel(mongoMobile.getChannel());
            mongoMobileDto.setAddtime(DateKit.getNowTime());
            gatherDubboService.saveDevice(mongoMobileDto);
        } catch (Exception e) {
            logger.warn("添加采集的设备信息失败" + e);
        }
    }

    /**
     * @param mongoLbs
     * @return void
     * @author xulijie
     * @method updateLbs
     * @description lbs信息采集
     * @time 20:30 2017/2/9
     */
    @Override
    public void updateLbs(MongoLbs mongoLbs) throws SydException {
        logger.debug("enter updateLbs method, content:[{}]", mongoLbs);
        //验证参数
        if (mongoLbs == null) {
            logger.warn("需要添加的LBS信息为空");
            return;
        }
        try {
            MongoLbsDto mongoLbsDto = BeanHelper.cloneBean(mongoLbs, MongoLbsDto.class);
            mongoLbsDto.setAddtime(DateKit.getNowTime());
            gatherDubboService.saveLbs(mongoLbsDto);
        } catch (Exception e) {
            logger.warn("添加采集的设备信息失败" + e);
        }
    }

}
