package com.mobanker.shanyidai.api.service.terminal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mobanker.shanyidai.api.business.gather.GatherBusiness;
import com.mobanker.shanyidai.api.business.terminal.TerminalOperationBusiness;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.logger.LogManager;
import com.mobanker.shanyidai.api.common.logger.Logger;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.common.tool.CommonUtil;
import com.mobanker.shanyidai.api.dto.gather.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Richard Core
 * @description 信息采集
 * @time 2016/12/1 14:05
 */
@Service
public class GatherServiceImpl implements GatherService {
    private Logger logger = LogManager.getSlf4jLogger(getClass());
    @Resource
    private GatherBusiness gatherBusiness;
    @Resource
    private TerminalOperationBusiness terminalOperationBusiness;

    /**
     * @param request
     * @return java.lang.Object
     * @author xulijie
     * @method phoneRecordLast
     * @description 获取最后一次采集通话记录的时间
     * @time 15:13 2017/2/15
     */
    @Override
    public Object phoneRecordLast(SydRequest request) throws SydException {
        //参数验证
        CommonUtil.checkLoginStatus(request);
        //获取时间
        String lastTime = gatherBusiness.phoneRecordLast(request.getUserId());
        //参数封装返回
        Map<String, String> result = new HashMap<String, String>();
        result.put("lastTime", lastTime);
        return result;
    }

    /**
     * @param request
     * @return java.lang.Object
     * @author xulijie
     * @method phoneSmsLast
     * @description 获取最后一次采集短信信息的时间
     * @time 15:13 2017/2/15
     */
    @Override
    public Object phoneSmsLast(SydRequest request) throws SydException {
        CommonUtil.checkLoginStatus(request);
        String lastTime = gatherBusiness.phoneSmsLast(request.getUserId());
        Map<String, String> result = new HashMap<String, String>();
        result.put("lastTime", lastTime);
        return result;
    }

    /**
     * @param request
     * @return java.lang.Object
     * @author xulijie
     * @method phoneContactLast
     * @description 获取最后一次采集通讯录信息的时间
     * @time 15:11 2017/2/15
     */
    @Override
    public Object phoneContactLast(SydRequest request) throws SydException {
        CommonUtil.checkLoginStatus(request);
        String lastTime = gatherBusiness.phoneContactLast(request.getUserId());
        Map<String, String> result = new HashMap<String, String>();
        result.put("lastTime", lastTime);
        return result;
    }

    /**
     * @param request
     * @return void
     * @author xulijie
     * @method updatePhoneContact
     * @description 通讯录信息上传
     * @time 16:39 2017/2/10
     */
    @Override
    public void updatePhoneContact(SydRequest request) throws SydException {
        //验证登录
        CommonUtil.checkLoginStatus(request);
        //参数转换
        JSONObject jso = JSON.parseObject(request.getContent());
        JSONArray array = jso.getJSONArray("content");
        String device_id = jso.getString("device_id");
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            MongoPhoneBook mongoPhoneBook = BeanHelper.parseJson(object.toString(), MongoPhoneBook.class);
            mongoPhoneBook.setUser_id(request.getUserId());
            mongoPhoneBook.setDevice_id(device_id);
            gatherBusiness.updatePhoneContact(mongoPhoneBook);
        }
    }

    /**
     * @param request
     * @return void
     * @author xulijie
     * @method updatePhoneSmsInfo
     * @description 短信信息采集
     * @time 15:27 2017/2/10
     */
    @Override
    public void updatePhoneSmsInfo(SydRequest request) throws SydException {
        //验证参数
        CommonUtil.checkLoginStatus(request);
        JSONObject jso = JSON.parseObject(request.getContent());
        JSONArray array = jso.getJSONArray("content");
        String device_id = jso.getString("device_id");
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            MongoSmsInfo mongoSmsInfo = BeanHelper.parseJson(object.toString(), MongoSmsInfo.class);
            mongoSmsInfo.setDevice_id(device_id);
            mongoSmsInfo.setUser_id(request.getUserId());
            gatherBusiness.updatePhoneSmsInfo(mongoSmsInfo);
        }
    }

    /**
     * @param request
     * @return void
     * @author xulijie
     * @method updatePhoneCallrecords
     * @description 通话记录信息采集
     * @time 15:21 2017/2/10
     */
    @Override
    public void updatePhoneCallrecords(SydRequest request) throws SydException {
        CommonUtil.checkLoginStatus(request);
        JSONObject jso = JSON.parseObject(request.getContent());
        JSONArray array = jso.getJSONArray("content");
        String device_id = jso.getString("device_id");
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            MongoCallRecords mongoCallRecords = BeanHelper.parseJson(object.toString(), MongoCallRecords.class);
            mongoCallRecords.setUser_id(request.getUserId());
            mongoCallRecords.setDevice_id(device_id);
            gatherBusiness.updatePhoneCallrecords(mongoCallRecords);
        }
    }

    /**
     * @param request
     * @return void
     * @description 保存设备采集信息
     * @author Richard Core
     * @time 2017/2/8 16:16
     * @method updateMobileDeviceInfo
     */
    @Override
    public void updateMobileDeviceInfo(SydRequest request) {
        CommonUtil.checkLoginStatus(request);
        MongoMobile mongoMobile = BeanHelper.parseJson(request.getContent(), MongoMobile.class);
        BeanHelper.packageBean(request, mongoMobile);
        mongoMobile.setUser_id(request.getUserId());
        gatherBusiness.updateMobileDeviceInfo(mongoMobile);
    }

    /**
     * @param request
     * @return void
     * @author xulijie
     * @method updateLbs
     * @description lbs信息采集
     * @time 15:02 2017/2/9
     */
    @Override
    public void updateLbs(SydRequest request) throws SydException {
        //验证userID是否过期
        CommonUtil.checkLoginStatus(request);
        MongoLbs mongoLbs = BeanHelper.parseJson(request.getContent(), MongoLbs.class);
        BeanHelper.packageBean(request, mongoLbs);
        mongoLbs.setUser_id(request.getUserId());
        gatherBusiness.updateLbs(mongoLbs);
    }

    /**
     * @param request
     * @return void
     * @author xulijie
     * @method appOperationActivation
     * @description app激活信息
     * @time 10:28 2017/2/27
     */
    @Override
    public void appOperationActivation(SydRequest request) throws SydException {
        if (request.getContent() == null) {
            throw new SydException(ReturnCode.PARAM_VALID.getCode(), "设备激活参数异常");
        }
        AppOperationActivationReq appOperationActivationReq = BeanHelper.parseJson(request.getContent(), AppOperationActivationReq.class);
        BeanHelper.packageBean(request, appOperationActivationReq);
        appOperationActivationReq.setAddProduct(request.getProduct());
        terminalOperationBusiness.appOperationActivation(appOperationActivationReq);
    }
}
