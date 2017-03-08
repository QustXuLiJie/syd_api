package com.mobanker.shanyidai.api.controller.v1_0_0;

import com.mobanker.shanyidai.api.common.annotation.SignLoginValid;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.enums.SignLoginEnum;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.dto.user.BankCard;
import com.mobanker.shanyidai.api.dto.user.BankCardIssuer;
import com.mobanker.shanyidai.api.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户信息相关API
 *
 * @author chenjianping
 * @data 2016年12月9日
 */
@Controller
@RequestMapping(value = "user")
public class UserInfoAction {
    @Resource
    private UserService userService;

    /**
     * @description 查询联系人、单位信息
     * @author hantongyang
     * @time 2016/12/20 20:22
     * @method getContactJob
     */
    @RequestMapping(value = "getContactJob", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getContactJob(SydRequest request) {
        Object data = null;
        try {
            data = userService.getContactJob(request.getUserId());
        } catch (SydException e) {
            if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
                throw e;
            } else {
                throw new SydException(ReturnCode.GET_CONTACTJOB_FAILED.getCode(), e.message, e);
            }
        } catch (Throwable e) {
            throw new SydException(ReturnCode.GET_CONTACTJOB_FAILED, e);
        }
        return data;
    }

    /**
     * @description 保存联系人信息
     * @author hantongyang
     * @time 2016/12/20 16:43
     * @method addContactJob
     */
    @RequestMapping(value = "addContact", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object addContact(SydRequest request) {
        try {
            userService.addContact(request);
        } catch (SydException e) {
            if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
                throw e;
            } else {
                throw new SydException(ReturnCode.ADD_CONTACTJOB_FAILED.getCode(), e.message, e);
            }
        } catch (Throwable e) {
            throw new SydException(ReturnCode.ADD_CONTACTJOB_FAILED, e);
        }
        return null;
    }

    /**
     * @param request
     * @return java.lang.Object
     * @description 保存单位信息
     * @author hantongyang
     * @time 2016/12/28 20:15
     * @method addJob
     */
    @RequestMapping(value = "addJob", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object addJob(SydRequest request) {
        try {
            userService.addJob(request);
        } catch (SydException e) {
            if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
                throw e;
            } else {
                throw new SydException(ReturnCode.ADD_CONTACTJOB_FAILED.getCode(), e.message, e);
            }
        } catch (Throwable e) {
            throw new SydException(ReturnCode.ADD_CONTACTJOB_FAILED, e);
        }
        return null;
    }

    /**
     * @description 查询联系人信息
     * @author hantongyang
     * @time 2016/12/20 20:22
     * @method getContactJob
     */
    @RequestMapping(value = "getContact", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getContact(SydRequest request) {
        Object data = null;
        try {
            data = userService.getContact(request.getUserId());
        } catch (SydException e) {
            if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
                throw e;
            } else {
                throw new SydException(ReturnCode.GET_CONTACTJOB_FAILED.getCode(), e.message, e);
            }
        } catch (Throwable e) {
            throw new SydException(ReturnCode.GET_CONTACTJOB_FAILED, e);
        }
        return data;
    }

    /**
     * @description 查询单位信息
     * @author hantongyang
     * @time 2016/12/20 20:22
     * @method getContactJob
     */
    @RequestMapping(value = "getJob", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getJob(SydRequest request) {
        Object data = null;
        try {
            data = userService.getJob(request.getUserId());
        } catch (SydException e) {
            if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
                throw e;
            } else {
                throw new SydException(ReturnCode.GET_CONTACTJOB_FAILED.getCode(), e.message, e);
            }
        } catch (Throwable e) {
            throw new SydException(ReturnCode.GET_CONTACTJOB_FAILED, e);
        }
        return data;
    }

    /**
     * 添加银行卡
     *
     * @param request
     * @return
     * @throws SydException
     */
    @SignLoginValid(SignLoginEnum.ALL)
    @RequestMapping(value = "/addBankCard", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object addBankCard(SydRequest request, @RequestHeader String appVersion) throws SydException {
        try {
            userService.addBankCard(request, appVersion);
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.ADD_DEBITCARD_FAILED, e);
        }
        return null;
    }

    /**
     * 根据卡号查询发卡行信息
     *
     * @param request
     * @return
     */
    @SignLoginValid(SignLoginEnum.ALL)
    @RequestMapping(value = "/getBankName", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getBankName(SydRequest request) {
        try {
            BankCardIssuer bankName = userService.getBankName(request);
            return bankName;
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.CARD_BIN_ERROR, e);
        }
    }


    /**
     * 8.15 获取我的银行卡
     *
     * @param request
     * @return
     * @throws SydException
     */
    @SignLoginValid(SignLoginEnum.ALL)
    @RequestMapping(value = "/listBankCard", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object listBankCard(SydRequest request, @RequestHeader String appVersion) throws SydException {
        try {
            List<BankCard> result = userService.listBankCard(request);
            return result;
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.GET_BANKLIST_FAILED, e);
        }
    }

    /**
     * 获取我的银行卡数量
     *
     * @param request
     * @return
     * @throws SydException
     */
    @SignLoginValid(SignLoginEnum.ALL)
    @RequestMapping(value = "/bankCardAmount", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object bankCardAmount(SydRequest request) throws SydException {
        try {
            Integer result = userService.bankCardAmount(request);
            return result;
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.GET_BANKLIST_FAILED, e);
        }
    }

    /**
     * 8.16 绑定我的银行卡
     *
     * @param request
     * @return
     * @throws SydException
     */
    @RequestMapping(value = "/setPayCard", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object setPayCard(SydRequest request) throws SydException {
        try {
            userService.setPayCard(request);
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.ADD_DEBITCARD_FAILED, e);
        }
        return null;
    }

    /**
     * @description 根据用户ID查询用户默认入账银行卡信息
     * @author hantongyang
     * @time 2017/2/14 11:15
     * @method getDefaultBank
     * @param request
     * @return java.lang.Object
     */
    @RequestMapping(value = "/getDefaultCard", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getDefaultCard(SydRequest request) throws SydException {
        Object obj = null;
        try {
            obj = userService.getDefaultBankByUserId(request);
        } catch (SydException e) {
            throw e;
        } catch (Throwable e) {
            throw new SydException(ReturnCode.ERROR_GET_DEFAULT_BANK, e);
        }
        return obj;
    }
}
