package com.mobanker.shanyidai.api.service.user.util;

import com.alibaba.fastjson.JSONObject;
import com.mobanker.shanyidai.api.common.enums.ExpiresEnum;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.constant.SystemConstant;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.common.tool.CommonUtil;
import com.mobanker.shanyidai.api.dto.auth.AlipayScore;
import com.mobanker.shanyidai.api.dto.auth.AuthIdentityProcess;
import com.mobanker.shanyidai.api.dto.auth.RealName;
import com.mobanker.shanyidai.api.dto.auth.ZhimaAuthParam;
import com.mobanker.shanyidai.api.dto.user.*;
import com.mobanker.shanyidai.api.enums.AuthProcessEnum;
import com.mobanker.shanyidai.api.enums.BankEnum;
import com.mobanker.shanyidai.dubbo.dto.user.UserBaseInfoDto;
import com.mobanker.shanyidai.esb.common.utils.BeanUtil;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hantongyang
 * @description 用户业务相关转换帮助类
 * @time 2016/12/21 11:55
 */
public class UserBusinessConvertUtils {


    /**
     * @param bankName
     * @return java.util.List<com.mobanker.shanyidai.api.dto.user.BankCardIssuer>
     * @description 获取发卡行信息
     * @author Richard Core
     * @time 2017/1/5 17:38
     * @method getBankIssuerList
     */
    public static List<BankCardIssuer> getBankIssuerList(String bankName) {
        if (StringUtils.isBlank(bankName)) {
            bankName = "";
        }
        List<BankCardIssuer> bankList = new ArrayList<BankCardIssuer>();
        for (BankEnum bankEnum : BankEnum.values()) {
            BankCardIssuer bank = new BankCardIssuer();
            bank.setBankName(bankEnum.getBankName());
            bank.setBankCode(bankEnum.getBankCode());
            if (bankEnum.getBankName().equals(bankName)) {
                bank.setSelected(true);
            }
            bankList.add(bank);
        }
        return bankList;
    }

    /**
     * @param req
     * @return void
     * @description 验证直系联系人
     * @author hantongyang
     * @time 2016/12/20 16:53
     * @method checkDirectContact
     */
    public static void checkDirectContact(UserContactReq req) {
        //重要联系人姓名
        if (StringUtils.isBlank(req.getContact1())) {
            throw new SydException(ReturnCode.ADD_CONTACTJOB_DIRECTCONTACT_NAME_FAILED);
        }
        //重要联系人电话
        if (StringUtils.isBlank(req.getPhoneNum1())) {
            throw new SydException(ReturnCode.ADD_CONTACTJOB_DIRECTCONTACT_PHONE_FAILED);
        }
        CommonUtil.checkPhone(req.getPhoneNum1());
    }

    /**
     * @param req
     * @return void
     * @description 验证重要联系人
     * @author hantongyang
     * @time 2016/12/20 16:54
     * @method checkEmergencyContact
     */
    public static void checkEmergencyContact(UserContactReq req) {
        //重要联系人姓名
        if (StringUtils.isBlank(req.getContact2())) {
            throw new SydException(ReturnCode.ADD_CONTACTJOB_EMERGENCYCONTACT_NAME_FAILED);
        }
        //重要联系人电话
        if (StringUtils.isBlank(req.getPhoneNum2())) {
            throw new SydException(ReturnCode.ADD_CONTACTJOB_EMERGENCYCONTACT_PHONE_FAILED);
        }
        CommonUtil.checkPhone(req.getPhoneNum2());
    }

    /**
     * @param req
     * @return void
     * @description 验证单位信息
     * @author hantongyang
     * @time 2016/12/20 16:54
     * @method checkPosition
     */
    public static void checkPosition(UserJobReq req) {
        //单位名称
        if (StringUtils.isBlank(req.getName())) {
            throw new SydException(ReturnCode.ADD_CONTACTJOB_NAME_FAILED);
        }
        //职位信息
        if (StringUtils.isBlank(req.getPosition())) {
            throw new SydException(ReturnCode.ADD_CONTACTJOB_POSITION_FAILED);
        }
        //职业类型
        if (StringUtils.isBlank(req.getOffice())) {
            throw new SydException(ReturnCode.ADD_CONTACTJOB_POSITION_OFFECT_FAILED);
        }
        //联系方式
        if (!CommonUtil.isDigit(req.getTelCountry()) || !CommonUtil.isDigit(req.getTelFixed())
                || !CommonUtil.isDigit(req.getTelSuffix())) {
            throw new SydException(ReturnCode.TEL_INVILID);
        }
        //自由职业不一定有电话，所以用00000000占位
        if("00000000".equals(req.getTelFixed())){
            //联系电话格式验证 是否是8位数字
            CommonUtil.checkTel(req.getTelFixed());
        }
    }

    /**
     * @param realNameSet
     * @return void
     * @description 验证实名认证参数是否合法
     * @author hantongyang
     * @time 2016/12/21 14:43
     * @method checkRealNameEmpty
     */
    public static void checkRealNameEmpty(RealName realNameSet) {
        //验证参数是否为空
        if (realNameSet == null) {
            throw new SydException(ReturnCode.PARAM_REQUIRED);
        }
        if (StringUtils.isBlank(realNameSet.getRealName()) || StringUtils.isBlank(realNameSet.getIdCard())
                || StringUtils.isBlank(realNameSet.getEducation())) {
            throw new SydException(ReturnCode.PARAM_REQUIRED);
        }
        //验证身份证
        if (!CommonUtil.isCardId(realNameSet.getIdCard())) {
            throw new SydException(ReturnCode.ADD_REALNAME_CARDID_FAILED);
        }
    }

    /**
     * @param request
     * @return void
     * @description 验证用户ID
     * @author hantongyang
     * @time 2016/12/21 16:59
     * @method checkAliPayByUserId
     */
    public static void checkUserId(SydRequest request) {
        if (request == null) {
            throw new SydException(ReturnCode.PARAM_VALID);
        }
        Long userId = request.getUserId();
        if (userId == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
    }

    /**
     * @param request
     * @return void
     * @description 验证基础参数是否为空
     * @author hantongyang
     * @time 2016/12/21 17:16
     * @method checkBasicParam
     */
    public static void checkBasicParam(SydRequest request) {
        if (StringUtils.isBlank(request.getChannel()) || StringUtils.isBlank(request.getProduct())) {
            throw new SydException(ReturnCode.PARAM_REQUIRED);
        }
    }

    /**
     * @param map
     * @return com.mobanker.shanyidai.api.dto.auth.RealName
     * @description 初始化实名认证信息
     * @author hantongyang
     * @time 2016/12/26 14:59
     * @method initRealName
     */
    public static UserBasicInfoRsp initUserBasicInfoRsp(Map<String, Object> map) {
        UserBasicInfoRsp bean = new UserBasicInfoRsp();
        bean.setRealname(map.get("realname") == null ? null : String.valueOf(map.get("realname")));
        bean.setPassword(map.get("password") == null ? null :String.valueOf(map.get("password")));
        bean.setRealnameTimes(map.get("realnameTimes") == null ? null :Integer.valueOf(String.valueOf(map.get("realnameTimes"))));
        bean.setIdCard(map.get("idCard") == null ? null :String.valueOf(map.get("idCard")));
        bean.setIdCardAddress(map.get("idCardAddress") == null ? null :String.valueOf(map.get("idCardAddress")));
        bean.setPhone(map.get("phone") == null ? null :String.valueOf(map.get("phone")));
        bean.setUserStatus(map.get("userStatus") == null ? null :String.valueOf(map.get("userStatus")));
        bean.setSex(map.get("sex") == null ? null :String.valueOf(map.get("sex")));
        bean.setBirthday(map.get("birthday") == null ? null :String.valueOf(map.get("birthday")));
        bean.setMarriage(map.get("marriage") == null ? null :String.valueOf(map.get("marriage")));
        bean.setJobTypeId(map.get("jobTypeId") == null ? null :String.valueOf(map.get("jobTypeId")));
        bean.setCompanyName(map.get("companyName") == null ? null :String.valueOf(map.get("companyName")));
        bean.setCompanyTelCountry(map.get("companyTelCountry") == null ? null :String.valueOf(map.get("companyTelCountry")));
        bean.setCompanyTelFixed(map.get("companyTelFixed") == null ? null :String.valueOf(map.get("companyTelFixed")));
        bean.setCompanyTelSuffix(map.get("companyTelSuffix") == null ? null :String.valueOf(map.get("companyTelSuffix")));
        bean.setJobTitle(map.get("jobTitle") == null ? null :String.valueOf(map.get("jobTitle")));
        bean.setJobInfoAllIsExpire(map.get("jobInfoAllIsExpire") == null ? null :String.valueOf(map.get("jobInfoAllIsExpire")));
        bean.setRealnameAllStatus(map.get("realnameAllStatus") == null ? null :String.valueOf(map.get("realnameAllStatus")));
        bean.setIdCardStatus(map.get("idCardStatus") == null ? null :String.valueOf(map.get("idCardStatus")));
        bean.setEdu(map.get("eduEducationDegree") == null ? null :String.valueOf(map.get("eduEducationDegree")));
        return bean;
    }

    /**
     * @param saveField
     * @param jso
     * @param realNameTimes
     * @return void
     * @description 封装需要修改的用户信息--实名认证信息
     * @author hantongyang
     * @time 2016/12/27 20:44
     * @method initRealNameSaveFieldMap
     */
    public static void initRealNameSaveFieldMap(Map<String, Object> saveField, JSONObject jso, Integer realNameTimes) {
        saveField.put("idCard", jso.getString("idCard"));
        saveField.put("realname", jso.getString("realName"));
        saveField.put("realnameTimes", realNameTimes == null ? 1 : realNameTimes.intValue() + 1);
    }

    /**
     * @param saveField
     * @param jso
     * @return void
     * @description 封装需要修改的用户信息--学历认证信息
     * @author hantongyang
     * @time 2016/12/27 20:45
     * @method initEduSaveFieldMap
     */
    public static void initEduSaveFieldMap(Map<String, Object> saveField, JSONObject jso) {
        saveField.put("eduGraduate", jso.getString("college")); //认证的学校名称
        saveField.put("eduEducationDegree", jso.getString("degree")); //认证的学历
        saveField.put("eduEnrolDate", jso.getString("startTime")); //认证的入学时间
        saveField.put("eduSpecialityName", jso.getString("specialty")); //认证的专业名称
        saveField.put("eduGraduateTime", jso.getString("graduateTime")); //认证的毕业时间
        saveField.put("eduStudyResult", jso.getString("studyResult")); //认证的毕业结论
        saveField.put("eduStudyStyle", jso.getString("studyType")); //认证的学历类型
    }

    /**
     * @param user
     * @return com.mobanker.shanyidai.api.dto.auth.RealName
     * @description 初始化实名认证信息
     * @author hantongyang
     * @time 2016/12/26 14:59
     * @method initRealName
     */
    public static RealName initRealName(UserBasicInfoRsp user) {
        RealName bean = BeanUtil.cloneBean(user, RealName.class);
        bean.setEducation(user.getEdu());
        bean.setRealName(user.getRealname());
        return bean;
    }

    /**
     * @param saveField
     * @param request
     * @return com.mobanker.shanyidai.dubbo.dto.user.UserBaseInfoDto
     * @description 初始化修改用户信息DTO
     * @author hantongyang
     * @time 2016/12/28 17:23
     * @method initUserBaseInfoDto
     */
    public static UserBaseInfoDto initUserBaseInfoDto(Map<String, Object> saveField, SydRequest request) {
        UserBaseInfoDto userDto = new UserBaseInfoDto();
        userDto.setUserId(request.getUserId());
        userDto.setSaveFields(saveField);
        userDto.setCommonFields(initCommonFields(request));
        return userDto;
    }

    /**
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @description 初始化公共字段，返回值类型是Map<String, Object>
     * @author hantongyang
     * @time 2016/12/28 21:05
     * @method initCommonFields
     */
    public static Map<String, Object> initCommonFields(SydRequest request) {
        Map<String, Object> commonFields = new HashMap<String, Object>();
        commonFields.put("remoteIp", request.getIp());
        commonFields.put("addChannel", request.getChannel());
        commonFields.put("addProduct", request.getProduct());
        commonFields.put("addVersion", request.getVersion());
        commonFields.put("addUser", "system");
        return commonFields;
    }

    /**
     * @param
     * @return java.util.List<java.lang.String>
     * @description 初始化查询联系人结果集
     * @author hantongyang
     * @time 2016/12/28 19:52
     * @method initUserContact
     */
    public static List<String> initUserContact() {
        List<String> dtoList = new ArrayList<String>();
        dtoList.add("linkman");
        dtoList.add("linkmanRelationship");
        dtoList.add("linkmanPhone");
        return dtoList;
    }

    /**
     * @param userJob
     * @param job
     * @return boolean
     * @description 判断是否修改过用户工作信息
     * @author hantongyang
     * @time 2016/12/29 14:12
     * @method isModifyJob
     */
    public static boolean isModifyJob(UserJobReq userJob, UserJobRsp job) {
        boolean isJob = false;
        if (job == null) {
            isJob = true;
        } else {
            //2.3 如果存在单位信息，验证单位信息是否有过改动，没有改动直接跳出，否则封装单位信息
            if (!userJob.getName().equals(job.getCompanyName()) || !userJob.getOffice().equals(job.getJobTypeId())
                    || !userJob.getTelCountry().equals(job.getCompanyTelCountry())
                    || !userJob.getTelFixed().equals(job.getCompanyTelFixed())
                    || !userJob.getTelSuffix().equals(job.getCompanyTelSuffix())
                    || !userJob.getPosition().equals(job.getPosition())) {
                isJob = true;
            }
        }
        return isJob;
    }

    /**
     * @param userJob
     * @param request
     * @return com.mobanker.shanyidai.dubbo.dto.user.UserBaseInfoDto
     * @description 初始化用户工作信息保存DTO
     * @author hantongyang
     * @time 2016/12/29 14:08
     * @method initUserJobSaveDto
     */
    public static UserBaseInfoDto initUserJobSaveDto(UserJobReq userJob, SydRequest request) {
        UserBaseInfoDto baseInfoDto = new UserBaseInfoDto();
        baseInfoDto.setUserId(request.getUserId());
        Map<String, Object> saveFileds = new HashMap<String, Object>();
        saveFileds.put("jobTypeId", userJob.getOffice());
        saveFileds.put("companyName", userJob.getName());
        saveFileds.put("jobTitle", userJob.getPosition());
        saveFileds.put("companyTelCountry", userJob.getTelCountry());
        saveFileds.put("companyTelFixed", userJob.getTelFixed());
        saveFileds.put("companyTelSuffix", userJob.getTelSuffix());
        baseInfoDto.setSaveFields(saveFileds);
        baseInfoDto.setCommonFields(initCommonFields(request));
        return baseInfoDto;
    }

    /**
     * @param realName
     * @param contact
     * @param job
     * @param zhima
     * @param identityAuthProcess
     * @return com.mobanker.shanyidai.api.dto.user.UserCompleteness
     * @description 初始化用户认证进度
     * @author hantongyang
     * @time 2017/1/10 12:01
     * @method initUserCompleteness
     */
    public static UserCompleteness initUserCompleteness(RealName realName, UserContactRsp contact, UserJobRsp job, AlipayScore zhima, AuthIdentityProcess identityAuthProcess) {
        UserCompleteness bean = new UserCompleteness();
        int completenessCou = 0;
        //判断实名认证
//        if(realName != null && ExpiresEnum.EFFECTIVE.getValue().equals(realName.getRealnameAllStatus())){
        if (checkRealName(realName)) {
            completenessCou++;
            bean.setRealName(SystemConstant.OK.getValue());
        } else {
            bean.setRealName(SystemConstant.FAIL.getValue());
        }
        //判断联系人/单位信息
//        if(contact != null && job != null){
        if (checkContactJob(contact, job)) {
            completenessCou++;
            bean.setContactJob(SystemConstant.OK.getValue());
            bean.setJobStatus(job.getJobInfoAllIsExpire());
        } else {
            bean.setContactJob(SystemConstant.FAIL.getValue());
        }
        //R.Core 身份识别进度
        bean.setIdentityProcess(identityAuthProcess);
        if (identityAuthProcess != null &&
                AuthProcessEnum.SUCCESS.getProcess().equals(identityAuthProcess.getIdentityProcess())) {
            completenessCou++;
        }

        //芝麻认证
        if (zhima != null) {
            //如果AlipaySkipAuthorize等于1，表示跳过芝麻认证；AlipayUsersStatus等于0表示认证成功，其余都是认证失败
            if (ExpiresEnum.EFFECTIVE.getValue().equals(zhima.getAlipayUsersStatus())
                    && ExpiresEnum.EFFECTIVE.getValue().equals(zhima.getAlipaySkipAuthorize())) {
                bean.setZhima("1");
                bean.setZhimaScore(zhima.getAlipayScore());
            } else if (ExpiresEnum.INVALID.getValue().equals(zhima.getAlipaySkipAuthorize())) {
                bean.setZhima("2"); //表示跳过认证
            } else {
                bean.setZhima("0"); //表示认证失败
            }
        }
        bean.setCompleteProgressCou(completenessCou);
        bean.setCompleteProgress(completenessCou + "/3");
        return bean;
    }

    /**
     * @param realName
     * @return boolean
     * @description 验证实名认证
     * @author hantongyang
     * @time 2017/2/23 11:58
     * @method checkRealName
     */
    public static boolean checkRealName(RealName realName) {
        boolean flag = Boolean.FALSE;
        if (realName == null) {
            return flag;
        }
        int statusCount = 0;
        //验证实名认证状态
        if (ExpiresEnum.EFFECTIVE.getValue().equals(realName.getRealnameAllStatus())) {
            statusCount += 1;
        }
        //验证身份证认证状态
        if (ExpiresEnum.EFFECTIVE.getValue().equals(realName.getRealnameAllStatus())) {
            statusCount += 1;
        }
        //验证学历认证状态
        if (StringUtils.isNotBlank(realName.getEducation())) {
            statusCount += 1;
        }
        if (statusCount == 3) {
            flag = Boolean.TRUE;
        }
        return flag;
    }

    /**
     * @param contact
     * @param job
     * @return boolean
     * @description 验证联系人、工作信息
     * @author hantongyang
     * @time 2017/2/23 14:03
     * @method checkContactJob
     */
    public static boolean checkContactJob(UserContactRsp contact, UserJobRsp job) {
        boolean flag = Boolean.FALSE;
        int statusCount = 0;
        //验证联系人
        if (contact != null) {
            if (StringUtils.isNotBlank(contact.getContact1()) && StringUtils.isNotBlank(contact.getContact2())) {
                statusCount += 1;
            }
        }
        //验证工作信息
        if (job != null) {
            if (job.getUserId() != null && StringUtils.isNotBlank(job.getJobTypeId()) && !"null".equals(job.getJobTypeId())) {
                statusCount += 1;
            }
        }
        if (statusCount == 2) {
            flag = Boolean.TRUE;
        }
        return flag;
    }

    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.auth.ZhimaAuthParam
     * @description 初始化芝麻认证查询参数
     * @author hantongyang
     * @time 2017/1/23 19:53
     * @method initZhimaAuthParam
     */
    public static ZhimaAuthParam initZhimaAuthParam(SydRequest request) {
        if (StringUtils.isBlank(request.getContent())) {
            throw new SydException(ReturnCode.PARAM_REQUIRED);
        }
        ZhimaAuthParam param = BeanHelper.parseJson(request.getContent(), ZhimaAuthParam.class);
        param.setUserId(request.getUserId());
        return param;
    }
}
