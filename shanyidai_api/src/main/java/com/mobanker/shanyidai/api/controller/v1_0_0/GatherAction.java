package com.mobanker.shanyidai.api.controller.v1_0_0;

import com.mobanker.framework.constant.Constants;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.logger.LogManager;
import com.mobanker.shanyidai.api.common.logger.Logger;
import com.mobanker.shanyidai.api.common.packet.ResponseEntity;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.service.terminal.GatherService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 信息采集
 *
 * @author R.Core
 */
@Controller
@RequestMapping(value = "gather")
public class GatherAction {
    private Logger logger = LogManager.getSlf4jLogger(getClass());
    @Resource
    private GatherService gatherService;

    /**
     * 获取最后一次采集通话记录的时间
     *
     * @return
     * @author: R.Core
     * @date 创建时间：2016-12-19
     * @version 1.0
     * @parameter
     */
    @RequestMapping(value = "/phoneRecordLast", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object phoneRecordLast(SydRequest request) throws SydException {
        Object result = null;
        try {
            result = gatherService.phoneRecordLast(request);
        } catch (Throwable e) {
            throw e;
        }
        return result;
    }

    /**
     * 获取最后一次采集短信记录的时间
     *
     * @return
     * @author: R.Core
     * @date 创建时间：2016-12-19
     * @version 1.0
     * @parameter
     */
    @RequestMapping(value = "/phoneSmsLast", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object phoneSmsLast(SydRequest request) throws SydException {
        Object result = null;
        try {
            result = gatherService.phoneSmsLast(request);
        } catch (Throwable e) {
            throw e;
        }
        return result;
    }

    /**
     * 获取最后一次采集通讯录的时间
     *
     * @return
     * @author: R.Core
     * @date 创建时间：2016-12-19
     * @version 1.0
     * @parameter
     */
    @RequestMapping(value = "/phoneContactLast", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object phoneContactLast(SydRequest request) throws SydException {
        Object result = null;
        try {
            result = gatherService.phoneContactLast(request);
        } catch (Throwable e) {
            throw e;
        }
        return result;
    }

    /**
     * 上传通讯录
     *
     * @return
     * @author: R.Core
     * @date 创建时间：2016-12-19
     * @version 1.0
     * @parameter
     */
    @RequestMapping(value = "/updatePhoneContact", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object updatePhoneContact(SydRequest request) throws SydException {
        ResponseEntity response = null;
        try {
            gatherService.updatePhoneContact(request);
            response = new ResponseEntity(Constants.System.OK);
        } catch (Throwable e) {
            throw e;
        }
        return response;
    }

    /**
     * 上传通话记录
     *
     * @return
     * @author: R.Core
     * @date 创建时间：2016-12-19
     * @version 1.0
     * @parameter
     */
    @RequestMapping(value = "/updatePhoneCallrecords", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object updatePhoneCallrecords(SydRequest request) throws SydException {
        ResponseEntity response = null;
        try {
            gatherService.updatePhoneCallrecords(request);
            response = new ResponseEntity(Constants.System.OK);
        } catch (Throwable e) {
            throw e;
        }
        return response;
    }

    /**
     * 上传短信记录
     *
     * @return
     * @author: R.Core
     * @date 创建时间：2016-12-19
     * @version 1.0
     * @parameter
     */
    @RequestMapping(value = "/updatePhoneSmsPost", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object updatePhoneSmsPost(SydRequest request) throws SydException {
        ResponseEntity response = null;
        try {
            gatherService.updatePhoneSmsInfo(request);
            response = new ResponseEntity(Constants.System.OK);
        } catch (Throwable e) {
            throw e;
        }
        return response;
    }

    /**
     * 上传终端信息
     *
     * @return
     * @author: R.Core
     * @date 创建时间：2016-12-19
     * @version 1.0
     * @parameter
     */
    @RequestMapping(value = "/deviceInfo", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object updateMobileDeviceInfo(SydRequest request) throws SydException {
        ResponseEntity response = null;
        try {
            gatherService.updateMobileDeviceInfo(request);
            response = new ResponseEntity(Constants.System.OK);

        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.GATHER_INFO_ERROR.getCode(), e.getMessage());
        }
        return response;
    }

    /**
     * 上传lbs
     *
     * @return
     * @author: R.Core
     * @date 创建时间：2016-12-19
     * @version 1.0
     * @parameter
     */
    @RequestMapping(value = "/updateLbs", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object updateLbs(SydRequest request) throws SydException {
        ResponseEntity response = null;
        try {
            gatherService.updateLbs(request);
            response = new ResponseEntity(Constants.System.OK);
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.GATHER_INFO_ERROR.getCode(), e.getMessage());
        }
        return response;
    }
}
