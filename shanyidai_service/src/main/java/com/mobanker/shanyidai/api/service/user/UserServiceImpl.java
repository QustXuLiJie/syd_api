package com.mobanker.shanyidai.api.service.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.mobanker.shanyidai.api.business.common.CommonBusiness;
import com.mobanker.shanyidai.api.business.redis.RedisBusiness;
import com.mobanker.shanyidai.api.business.system.UserDefaultBankBusiness;
import com.mobanker.shanyidai.api.business.user.UserBusiness;
import com.mobanker.shanyidai.api.business.user.UserSessionBusiness;
import com.mobanker.shanyidai.api.common.annotation.BusinessFlowAnnotation;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.constant.SystemConstant;
import com.mobanker.shanyidai.api.common.constant.ZkConfigConstant;
import com.mobanker.shanyidai.api.common.enums.RedisKeyEnum;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.common.tool.CommonUtil;
import com.mobanker.shanyidai.api.common.tool.DateKit;
import com.mobanker.shanyidai.api.dto.account.UserCodeBean;
import com.mobanker.shanyidai.api.dto.basedata.JobType;
import com.mobanker.shanyidai.api.dto.system.DefaultBank;
import com.mobanker.shanyidai.api.dto.system.Dictionary;
import com.mobanker.shanyidai.api.dto.user.*;
import com.mobanker.shanyidai.api.enums.BankCardTypeEnum;
import com.mobanker.shanyidai.api.enums.BankCardVerifyResultEnum;
import com.mobanker.shanyidai.api.service.system.SystemService;
import com.mobanker.shanyidai.api.service.user.util.UserBusinessConvertUtils;
import com.mobanker.shanyidai.dubbo.dto.user.BankCardDto;
import com.mobanker.shanyidai.dubbo.dto.user.BankCardParamDto;
import com.mobanker.shanyidai.dubbo.dto.user.UserBaseInfoDto;
import com.mobanker.shanyidai.dubbo.dto.user.UserContactDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import static com.mobanker.shanyidai.api.service.user.util.UserBusinessConvertUtils.*;

@Service
@BusinessFlowAnnotation
public class UserServiceImpl implements UserService {
    @Resource
    private UserBusiness userBusiness;
    @Resource
    private RedisBusiness redisBusiness;
    @Resource
    private CommonBusiness commonBusiness;
    @Resource
    private UserSessionBusiness userSessionBusiness;
    @Resource
    private UserAuthService userAuthService;
    @Resource
    private UserDefaultBankBusiness userDefaultBankBusiness;
    @Resource
    private SystemService systemService;

    @Override
    public Long getUserCode(String code) {
        UserCodeBean uc = redisBusiness.getValue(RedisKeyEnum.SYD_USER_CODE.getValue() + code, UserCodeBean.class);
        if (uc == null) {//调接口去查一下
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
            //return userId(code);
        }
        //获取过期时间
        Integer survivalTime = getSurvivalTime();
        //如果用户登录时间+过期时间大于等于当前时间，表示没有过期，需要去更新用户失效时间；如果小于当前时间，表示已经过期，需要重新登陆
        if (DateKit.getNowTime() > uc.getNowTime() + survivalTime) {
            return null;
        }
        //更新缓存中的用户生效时间
        addUserCode(code, uc.getUserId());
        return uc.getUserId();
        //TODO 是否需要查询数据库？？？
        //return userId(code);
    }

    private Long userId(String code) {
        Long userId = userSessionBusiness.checkUserSession(code, null);
        if (userId == null) {
            return null;
        } else {
            //更新缓存中的用户生效时间
            addUserCode(code, userId);
            return userId;
        }
    }

    @Override
    public void addUserCode(String code, Long userId) {
        UserCodeBean uc = new UserCodeBean();
        uc.setUserId(userId);
        uc.setNowTime(DateKit.getNowTime());
        //获取过期时间
        Integer survivalTime = getSurvivalTime();
        redisBusiness.setValue(RedisKeyEnum.SYD_USER_CODE.getValue() + code, JSON.toJSONString(uc), Integer.valueOf(survivalTime));
    }

    @Override
    public void removeUserCode(String code) {
        redisBusiness.delValue(RedisKeyEnum.SYD_USER_CODE.getValue() + code);
    }

    /**
     * @param request
     * @return void
     * @description 新增用户联系人信息
     * @author hantongyang
     * @time 2016/12/20 15:27
     * @method addContactJob
     */
    @Override
    public void addContact(SydRequest request) throws SydException {
        if (request == null || StringUtils.isBlank(request.getContent())) {
            throw new SydException(ReturnCode.PARAM_VALID.getCode(), ReturnCode.PARAM_VALID.getDesc(), null);
        }
        UserContactReq req = BeanHelper.parseJson(request.getContent(), UserContactReq.class);
        //参数验证
        CommonUtil.checkPhone(req.getPhoneNum1());
        CommonUtil.checkPhone(req.getPhoneNum2());
        //联系人姓名校验
        if (!CommonUtil.limitString(req.getContact1(), 2, 6)) {
            throw new SydException(ReturnCode.ADD_CONTACTJOB_DIRECTCONTACT_NAME_FAILED);
        }
        if (!CommonUtil.limitString(req.getContact2(), 2, 6)) {
            throw new SydException(ReturnCode.ADD_CONTACTJOB_EMERGENCYCONTACT_NAME_FAILED);
        }
        //验证直系联系人
        UserBusinessConvertUtils.checkDirectContact(req);
        //验证重要联系人
        UserBusinessConvertUtils.checkEmergencyContact(req);
        //判断是否需要更新联系人
        boolean isContact = false; //直系联系人是否需要更新标识
        boolean isEmergencyContact = false; //紧急联系人是否需要更新标识
        //1、用户联系人信息
        //1.1 查询用户直系联系人信息、紧急联系人信息
        UserContactRsp userContact = getContact(request.getUserId());
        if (userContact == null) {
            //跳过后续逻辑
            return;
        }
        //1.2 验证用户直系联系人是否存在
        if (StringUtils.isNotBlank(userContact.getContact1())) {
            //1.3 如果存在直系联系人信息，判断是否有改动过，没有改动返回false
            if (!req.getContact1().equals(userContact.getContact1()) || !req.getPhoneNum1().equals(userContact.getPhoneNum1())
                    || !req.getRelationship1().equals(userContact.getRelationship1())) {
                isContact = true;
            }
        } else {
            isContact = true;
        }
        Map<String, Object> contactMap = new HashMap<String, Object>();
        if (isContact) {
            //封装用户直系联系人
            contactMap.put("linkmanType", "contact");
            contactMap.put("linkman", req.getContact1());
            contactMap.put("linkmanPhone", req.getPhoneNum1());
            contactMap.put("linkmanRelationship", req.getRelationship1());
            contactMap.put("linkmanNum", "1");
        }
        //1.4 验证用户紧急联系人是否存在
        if (StringUtils.isNotBlank(userContact.getContact2())) {
            //1.5 如果存在紧急联系人信息，判断是否有过改动，没有改动直接跳出，否则封装紧急联系人信息
            if (!req.getContact2().equals(userContact.getContact2()) || !req.getPhoneNum2().equals(userContact.getPhoneNum2())
                    || !req.getRelationship2().equals(userContact.getRelationship2())) {
                isEmergencyContact = true;
            }
        } else {
            isEmergencyContact = true;
        }
        Map<String, Object> emergencyMap = new HashMap<String, Object>();
        if (isEmergencyContact) {
            //封装用户紧急联系人
            emergencyMap.put("linkmanType", "emergencyContact");
            emergencyMap.put("linkman", req.getContact2());
            emergencyMap.put("linkmanPhone", req.getPhoneNum2());
            emergencyMap.put("linkmanRelationship", req.getRelationship2());
            emergencyMap.put("linkmanNum", "2");
        }
        //1.6 调用接口保存联系人信息
        if (isContact || isEmergencyContact) {
            Long userId = request.getUserId();
            //声明保存联系人DTO
            UserContactDto dto = new UserContactDto();
            dto.setUserId(userId);
            List<Map<String, Object>> saveFields = new ArrayList<Map<String, Object>>();
            if (!contactMap.isEmpty()) {
                saveFields.add(contactMap);
            }
            if (!emergencyMap.isEmpty()) {
                saveFields.add(emergencyMap);
            }
            dto.setSaveFields(saveFields);
            dto.setCommonFields(initCommonFields(request));
            userBusiness.addUserContact(dto);
            redisBusiness.delValue(RedisKeyEnum.SYD_USER_CONTACT.getValue() + userId);
        }
    }

    /**
     * @param request
     * @return void
     * @description 新增用户单位信息
     * @author hantongyang
     * @time 2016/12/28 20:16
     * @method addJob
     */
    @Override
    public void addJob(SydRequest request) {
        if (request == null || StringUtils.isBlank(request.getContent())) {
            throw new SydException(ReturnCode.PARAM_VALID);
        }
        UserJobReq userJob = BeanHelper.parseJson(request.getContent(), UserJobReq.class);
        //单位电话区号-电话-分机；目前只验证中间是8位
        CommonUtil.checkTel(userJob.getTelFixed());
        //单位名称2-25个字符
        if (!CommonUtil.limitString(userJob.getName(), 0, 25)) {
            throw new SydException(ReturnCode.ADD_CONTACTJOB_NAME_FAILED);
        }
        //职位2-12个字符
        if (!CommonUtil.limitString(userJob.getPosition(), 2, 12)) {
            throw new SydException(ReturnCode.ADD_CONTACTJOB_POSITION_FAILED);
        }
        //验证单位信息
        UserBusinessConvertUtils.checkPosition(userJob);
        //2、用户单位信息
        //2.1 查询用户单位信息
        UserJobRsp job = getJob(request.getUserId());
        //2.2 判断用户单位信息是否存在
        boolean isJob = isModifyJob(userJob, job);
        //2.4 调用接口更新用户单位信息
        if (isJob) {
            //update by hanty;2017-01-19; 会议讨论固话验证现在已经废弃
//            //验证办公电话是否正确
//            String tel = userJob.getTelCountry() + userJob.getTelFixed();
//            FixedTel fixedTel = userAuthService.validFixedConstrast(tel);
//            //验证查询结果是否为空，或者单位名称是否一致
//            if(fixedTel == null || !fixedTel.getOwnerName().equals(userJob.getName())){
//                throw new SydException(ReturnCode.ERROR_AUTH_TEL);
//            }
            //更新用户工作信息
            updateUserInfo(initUserJobSaveDto(userJob, request));
        }
    }

    /**
     * @param userId
     * @return com.mobanker.shanyidai.api.dto.user.UserContactRsp
     * @description
     * @author hantongyang
     * @time 2016/12/20 20:14
     * @method getContactJob
     */
    @Override
    public UserContactRsp getContact(Long userId) {
        //判断参数是否为空
        if (userId == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        UserContactRsp userContactRsp = redisBusiness.getValue(RedisKeyEnum.SYD_USER_CONTACT.getValue() + userId, UserContactRsp.class);
        if (userContactRsp != null) {
            return userContactRsp;
        }
        userContactRsp = new UserContactRsp();
        userContactRsp.setUserId(userId);
        //TODO 调用查询用户联系人/单位信息接口
        //1、获取直系联系人
        Map<String, String> contact = getContact(userId, "contact");
        //将用户直系联系人信息保存到DTO中
        if (contact != null && !contact.isEmpty()) {
            userContactRsp.setContact1(contact.get("linkman"));
            userContactRsp.setPhoneNum1(contact.get("linkmanPhone"));
            userContactRsp.setRelationship1(contact.get("linkmanRelationship"));
        }
        //2、获取重要联系人
        Map<String, String> emergencyContact = getContact(userId, "emergencyContact");
        //将用户紧急联系人信息保存到DTO中
        if (emergencyContact != null && !emergencyContact.isEmpty()) {
            userContactRsp.setContact2(emergencyContact.get("linkman"));
            userContactRsp.setPhoneNum2(emergencyContact.get("linkmanPhone"));
            userContactRsp.setRelationship2(emergencyContact.get("linkmanRelationship"));
        }
        redisBusiness.setValue(RedisKeyEnum.SYD_USER_CONTACT.getValue() + userId, JSONObject.toJSONString(userContactRsp));
        return userContactRsp;
    }

    /**
     * @param userId
     * @return com.mobanker.shanyidai.api.dto.user.UserJobRsp
     * @description 根据用户ID查询用户单位信息
     * @author hantongyang
     * @time 2016/12/28 20:32
     * @method getJob
     */
    @Override
    public UserJobRsp getJob(Long userId) {
        UserBasicInfoRsp userInfo = getUserInfoByUserId(userId);
        if (userInfo == null) {
            return null;
        }
        UserJobRsp rsp = BeanHelper.cloneBean(userInfo, UserJobRsp.class);
        rsp.setPosition(userInfo.getJobTitle());
        //封装三层单位信息
        return convertJobInfo(rsp);
    }

    /**
     * @param rsp
     * @return com.mobanker.shanyidai.api.dto.user.UserJobRsp
     * @description 封装三层单位信息
     * @author Richard Core
     * @time 2017/3/4 11:22
     * @method convertJobInfo
     */
    private UserJobRsp convertJobInfo(UserJobRsp rsp) {
        if (StringUtils.isBlank(rsp.getJobTypeId())) {
            return rsp;
        }
        Integer job3Id = Integer.valueOf(rsp.getJobTypeId());
        Map<Integer, JobType> jobMap = getJobMapInRedis();
        JobType job1 = convertJobType(job3Id, jobMap);
        if (job1 == null) {
            return rsp;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(job1.getName());
        rsp.setOfficeLevelId1(job1.getId().toString());
        rsp.setOfficeLevelName1(job1.getName());
        List<JobType> child1 = job1.getChild();
        if (child1 == null || child1.isEmpty()) {
            rsp.setOffice(sb.toString());
            return rsp;
        }

        JobType job2 = child1.get(0);
        rsp.setOfficeLevelId2(job2.getId().toString());
        rsp.setOfficeLevelName2(job2.getName());
        sb.append("-").append(job2.getName());
        List<JobType> child2 = job2.getChild();
        if (child2 == null || child2.isEmpty()) {
            rsp.setOffice(sb.toString());
            return rsp;
        }
        JobType job3 = child2.get(0);
        rsp.setOfficeLevelId3(job3.getId().toString());
        rsp.setOfficeLevelName3(job3.getName());
        sb.append("-").append(job3.getName());
        return rsp;
    }

    /**
     * @param jobId
     * @param jobMap
     * @return com.mobanker.shanyidai.api.dto.basedata.JobType
     * @description 查询三层单位结构
     * @author Richard Core
     * @time 2017/3/4 11:22
     * @method convertJobType
     */
    private JobType convertJobType(Integer jobId, Map<Integer, JobType> jobMap) {
        if (jobId == null || jobMap == null || jobMap.isEmpty()) {
            return null;
        }
        //取最低级 取不到返回 空
        JobType curJob = getJobTypeFromMap(jobId, jobMap);
        if (curJob == null) {
            return null;
        }
        JobType jobType = new JobType();
        jobType = curJob;
        //取第二级，取不到返回最低级
        JobType parJob = getJobTypeFromMap(curJob.getPid(),jobMap);
        if (parJob == null) {
            return jobType;
        }
        parJob.setChild(Arrays.asList(curJob));
        //取最高级，取不到返回第二级
        JobType grandJob = getJobTypeFromMap(parJob.getPid(),jobMap);
        if (grandJob == null) {
            return parJob;
        }
        grandJob.setChild(Arrays.asList(parJob));
        return grandJob;
    }

    private JobType getJobTypeFromMap(Integer jobId, Map<Integer, JobType> jobMap) {
        return JSONObject.parseObject(JSONObject.toJSONString(jobMap.get(jobId)),JobType.class);
    }

    /**
     * @param
     * @return java.util.Map<java.lang.Integer,com.mobanker.shanyidai.api.dto.basedata.JobType>
     * @description 获取缓存中的单位信息map
     * @author Richard Core
     * @time 2017/3/4 11:23
     * @method getJobMapInRedis
     */
    private Map<Integer, JobType> getJobMapInRedis() {
        String value = redisBusiness.getValue(RedisKeyEnum.SYD_JOBTYPE_MAP.getValue());
        Map<Integer, JobType> jobMap = BeanHelper.parseJson(value, Map.class);
        return jobMap;
    }

    /**
     * @param userId
     * @return com.mobanker.shanyidai.api.dto.user.UserContactJobRsp
     * @description 根据用户ID查询联系人、单位信息
     * @author hantongyang
     * @time 2017/1/23 16:11
     * @method getContactJob
     */
    @Override
    public UserContactJobRsp getContactJob(Long userId) {
        UserContactJobRsp bean = new UserContactJobRsp();
        bean.setUserContact(getContact(userId));
        bean.setUserJob(getJob(userId));
        return bean;
    }

    /**
     * @param userId
     * @param type
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @description 获取用户联系人信息
     * @author hantongyang
     * @time 2016/12/28 19:46
     * @method getContact
     */
    private Map<String, String> getContact(Long userId, String type) {
        UserContactDto dto = new UserContactDto();
        dto.setUserId(userId);
        dto.setType(type);
        dto.setFields(initUserContact());
        List<Map<String, String>> resultList = userBusiness.getUserContactByUserId(dto);
        if (resultList == null || resultList.isEmpty()) {
            return null;
        }
        //由于返回结构中会包含无用的数据集，需要解析list，获取实际结果
        for (Map<String, String> map : resultList) {
            if (map != null && !map.isEmpty()) {
                if (StringUtils.isNotBlank(map.get("linkman"))) {
                    return map;
                }
            }
        }
        return null;
    }


    /**
     * @param request
     * @param appVersion
     * @return
     * @description 添加银行卡
     * @author R.Core
     * @time 2016/12/20 20:14
     * @method getContactJob
     */
    @Override
    public void addBankCard(SydRequest request, String appVersion) {
        //1.1、获取参数 参数校验
        BankCard addParam = getBankCardInputParam(request);
        //1.2、校验卡号和发卡行名称是否匹配
        checkBankCardNoMatchesName(request, addParam);
        //2、验证用户是否实名认证过 获取实名信息 姓名 和身份证号 手机号
        UserBasicInfoRsp userInfo = checkAddBankCardUserRealNameInfo(request, addParam);
        //3、验证银行卡是否已经添加过
        checkBankCardAddedBefore(request, addParam);
        //4、卡认证
        bankCardVerify(request, addParam, userInfo);
        //5、添加银行卡
        addBankCard(request, addParam);
    }

    /**
     * @param request
     * @param addParam
     * @return void
     * @description 校验卡号和发卡行名称是否匹配
     * @author Richard Core
     * @time 2017/1/22 16:06
     * @method checkBankCardNoMatchesName
     */
    public void checkBankCardNoMatchesName(SydRequest request, BankCard addParam) {
        BankCardDto bankCardDto = BeanHelper.cloneBean(request, BankCardDto.class);
        bankCardDto.setBankCardNo(addParam.getBankCardNo());
        BankCardIssuer result = userBusiness.getCardBinByCardNo(bankCardDto);
        if (result != null && !result.getBankName().equals(addParam.getBankName())) {
            throw new SydException(ReturnCode.BANKNAME_CARDNUM_NOT_EQUALS);
        }
    }

    /**
     * @param request
     * @param addParam
     * @return void
     * @description 添加银行卡卡的方法
     * @author Richard Core
     * @time 2017/1/4 21:56
     * @method addBankCard
     */

    private void addBankCard(SydRequest request, BankCard addParam) {
        BankCardParamDto params = BeanHelper.cloneBean(request, BankCardParamDto.class);
        params.setType(BankCardTypeEnum.DEBIT_CARD.getType());
        params.setUserId(request.getUserId());

        Map<String, Object> commonFields = UserBusinessConvertUtils.initCommonFields(request);
        params.setCommonFields(commonFields);
        Map<String, Object> saveFields = new HashMap<>();
        saveFields.put("cardNum", addParam.getBankCardNo());
        saveFields.put("cardBankName", addParam.getBankName());
        saveFields.put("cardMobile", addParam.getPhone());
        saveFields.put("cardYstatus", addParam.getCardYstatus());
        params.setSaveFields(saveFields);

        //添加银行卡
        userBusiness.addBankCard(params);
    }

    /**
     * @param request
     * @param addParam
     * @param userInfo
     * @return void
     * @description 银行卡认证方法
     * @author Richard Core
     * @time 2017/1/4 21:58
     * @method bankCardVerify
     */
    private void bankCardVerify(SydRequest request, BankCard addParam, UserBasicInfoRsp userInfo) {
        BankCardDto verifyParam = BeanHelper.cloneBean(request, BankCardDto.class);
        verifyParam.setBankCardNo(addParam.getBankCardNo());
        verifyParam.setIdCard(userInfo.getIdCard());
        verifyParam.setPhone(userInfo.getPhone());
        verifyParam.setRealName(userInfo.getRealname());
        BankCardVerifyResult bankCardVerify = userBusiness.getBankCardVerify(verifyParam);
        //验证认证结果
        if (bankCardVerify == null) {
            throw new SydException(ReturnCode.BANKCARD_VERIFY_EXCEPTION);
        }
        BankCardVerifyResultEnum instance = BankCardVerifyResultEnum.getInstance(bankCardVerify.getResult());
        switch (instance) {
            case VALID:
                //验证通过
                break;
            default:
                //验证不通过
                throw new SydException(ReturnCode.BANKCARD_VERIFY_FAILED.getCode(), instance.getDesc());
        }
    }

    /**
     * @param request
     * @param addParam
     * @return void
     * @description 验证银行卡是否已经添加过
     * @author Richard Core
     * @time 2017/1/4 21:58
     * @method checkBankCardAddedBefore
     */
    private void checkBankCardAddedBefore(SydRequest request, BankCard addParam) {
        BankCardDto checkBankParam = BeanHelper.cloneBean(request, BankCardDto.class);
        checkBankParam.setBankCardNo(addParam.getBankCardNo());
        checkBankParam.setUserId(request.getUserId());
        checkBankParam.setPhone(addParam.getPhone());
        checkBankParam.setCardType(BankCardTypeEnum.DEBIT_CARD.getType());
        List<BankCard> checkCardResult = userBusiness.getCardByPhoneOrCardNo(checkBankParam);
        if (checkCardResult != null && !checkCardResult.isEmpty()) {
            throw new SydException(ReturnCode.BANK_CARD_ADDED_BEFORE);
        }
    }

    /**
     * @param request
     * @param addParam
     * @return com.mobanker.shanyidai.api.dto.user.UserBasicInfoRsp
     * @description 验证填写的信息是否属实，验证是否已经实名认证过
     * @author Richard Core
     * @time 2017/1/4 21:59
     * @method checkAddBankCardUserRealNameInfo
     */
    private UserBasicInfoRsp checkAddBankCardUserRealNameInfo(SydRequest request, BankCard addParam) {
        userAuthService.validateRealNameResult(request.getUserId());
        UserBasicInfoRsp userInfo = getUserInfoByUserId(request.getUserId());
        if (userInfo == null) {
            throw new SydException(ReturnCode.GET_USERINFO_FAILED);
        }
        //验证填写手机号和预留手机号是否一致
        if (!addParam.getPhone().equals(userInfo.getPhone())) {
            throw new SydException(ReturnCode.PHONE_UNMATCH);
        }
        return userInfo;
    }

    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.user.BankCard
     * @description 验证添加银行卡参数
     * @author Richard Core
     * @time 2017/1/4 21:59
     * @method getBankCardInputParam
     */
    private BankCard getBankCardInputParam(SydRequest request) {
        BankCard addParam = BeanHelper.parseJson(request.getContent(), BankCard.class);
        CommonUtil.checkPhone(addParam.getPhone());
        //todo 银行卡号是否需要验证 初步验证 全数字 16-19位
        if (!(CommonUtil.isDigit(addParam.getBankCardNo()) && CommonUtil.limitString(addParam.getBankCardNo(), 16, 19))) {
            throw new SydException(ReturnCode.ADD_DEBITCARD_FAILED);
        }


        if (request.getUserId() == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        return addParam;
    }


    /**
     * @param request
     * @return List<BankCard>
     * @description 获取我的银行卡列表
     * @author Richard Core
     * @time 2016/12/21 11:02
     * @method listBankCard
     */
    @Override
    public List<BankCard> listBankCard(SydRequest request) {
        //验证参数
        if (request.getUserId() == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        //获取发卡行列表
        Map<String, Dictionary> sydSupportBank = getSydSupportBank(request);
        //获取银行卡
        List<Map<String, String>> result = userBusiness.listBankCard(request);
        //封装返回值
        List<BankCard> bankCardList = getBankCardBean(request, result, sydSupportBank);
        return bankCardList;
    }

    /**
     * 获取闪宜贷支持的发卡行
     *
     * @param request
     * @return
     */
    private Map<String, Dictionary> getSydSupportBank(SydRequest request) {
        List<Dictionary> dicList = systemService.getDictionaryByType(request);
        Map<String, Dictionary> dictionaryMap = new HashMap<>();
        for (Dictionary dictionary : dicList) {
            dictionaryMap.put(dictionary.getDicName(), dictionary);
        }
        return dictionaryMap;
    }

    /**
     * @param result
     * @return java.util.List<com.mobanker.shanyidai.api.dto.user.BankCard>
     * @description 将map中的信息封装在对象中返回
     * @author Richard Core
     * @time 2017/1/3 14:21
     * @method getBankCardBean
     */
    private List<BankCard> getBankCardBean(SydRequest request, List<Map<String, String>> result, Map<String, Dictionary> dictionaryMap) {
        List<BankCard> bankCardList = new ArrayList<BankCard>();
        if (result == null || result.isEmpty()) {
            return bankCardList;
        }
        //获取用户默认入账银行卡
        DefaultBank defaultBankByUserId = userDefaultBankBusiness.findDefaultBankByUserId(request.getUserId());
        for (Map<String, String> map : result) {
            //拼装银行卡列表参数
            Dictionary dictionary = dictionaryMap.get(map.get("cardBankName"));
            //银行卡列表中有 但是当前不支持的发卡行的卡片,不在闪宜贷产品展示
            if (dictionary == null || StringUtils.isEmpty(dictionary.getDicContent())) {
                continue;
            }
            BankCard bankCard = new BankCard();
            bankCard.setBankName(map.get("cardBankName"));//银行卡银行名称
            bankCard.setBankCardId(map.get("cardId"));
            bankCard.setUserId(Long.valueOf(map.get("userId")));//用户标识
            bankCard.setBankCardNo(map.get("cardNum"));//银行卡号
            bankCard.setPhone(map.get("cardMobile"));//银行卡银行预留手机号
            bankCard.setCardYstatus(Integer.valueOf(map.get("cardYstatus")));//银行卡验证状态
            bankCard.setCardType(BankCardTypeEnum.DEBIT_CARD.getType());
            bankCard.setCardTypeDesc(BankCardTypeEnum.DEBIT_CARD.getDesc());
            bankCard.setBranchBankName(map.get("cardBankBranchName"));//支行名称 2017-1-22  R.Core 借款下单时使用
            JSONObject dicContentObject = JSON.parseObject(dictionary.getDicContent());
            bankCard.setCardIcon(dicContentObject.getString("icon"));
            // 2017/1/5 入账银行卡 暂时没地方获取
            bankCard.setPayCard("0");
            if (defaultBankByUserId != null) {
                //银行卡列表中有 但是当前不支持的发卡行的卡片,不在闪宜贷产品展示
                if (defaultBankByUserId.getCreditBankCard().equals(bankCard.getBankCardNo())) {
                    bankCard.setPayCard(SystemConstant.OK.getValue());
                }
            }
            bankCardList.add(bankCard);
        }
        return bankCardList;
    }


    /**
     * @param request
     * @return void
     * @description 绑定银行卡 选择入账银行卡
     * @author hantongyang
     * @time 2016/12/21 11:03
     * @method setPayCard
     */
    @Override
    public void setPayCard(SydRequest request) {
        CommonUtil.checkLoginStatus(request);
        //获取参数
        BankCard bankCard = BeanHelper.parseJson(request.getContent(), BankCard.class);
        //绑定
        userDefaultBankBusiness.setDefaultBankByUserId(request.getUserId(), bankCard.getBankCardNo());
    }

    /**
     * @param userId
     * @param fields
     * @return com.mobanker.shanyidai.api.dto.user.UserBasicInfoRsp
     * @description 根据用户ID查询用户详情，先取缓存中的用户详情，缓存中没有再调用接口获取
     * @author hantongyang
     * @time 2016/12/27 16:46
     * @method getUserInfoByUserId
     */
    @Override
    public UserBasicInfoRsp getUserInfoByUserId(Long userId, String... fields) {
        if (fields == null) {
            return getUserInfoByUserId(userId);
        }
        return getUserBasicInfoBean(userId, Arrays.asList(fields));
    }


    /**
     * @param userId
     * @return com.mobanker.shanyidai.api.dto.user.UserBasicInfoRsp
     * @description 根据用户ID查询用户详情，不走缓存
     * @author hantongyang
     * @time 2016/12/27 16:46
     * @method getUserInfoByUserId
     */
    @Override
    public UserBasicInfoRsp getUserInfoByUserId(Long userId) {
        if (userId == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        //1、查询缓存中是否有用户的详情，有用户信息则直接使用缓存中的用户
        UserBasicInfoRsp bean = redisBusiness.getValue(RedisKeyEnum.SYD_USER_INFO.getValue() + userId, UserBasicInfoRsp.class);
        if (bean != null) {
            return bean;
        }
        //2、根据用户ID查询用户是否认证过，如果报错或者结果为空，表示未进行实名认证，返回空值
        bean = getUserBasicInfoBean(userId, null);
        if (bean == null) {
            return null;
        }
        //保存到redis中//3、返回实名认证信息，并保存到缓存中
        String cacheSysConfig = null;
        try {
            cacheSysConfig = commonBusiness.getCacheSysConfig(ZkConfigConstant.SYD_USER_INFO_TIMEOUT.getZkValue());
        } catch (Exception e) {
            throw new SydException(ReturnCode.CONFIG_DATA_NULL);
        }
        redisBusiness.setValue(RedisKeyEnum.SYD_USER_INFO.getValue() + userId, JSONObject.toJSONString(bean), Integer.parseInt(cacheSysConfig));
        return bean;
    }

    /**
     * @param userId
     * @param fields
     * @return com.mobanker.shanyidai.api.dto.user.UserBasicInfoRsp
     * @description 根据用户ID查询用户详情
     * @author Richard Core
     * @time 2016/12/27 21:01
     * @method getUserBasicInfoBean
     */
    private UserBasicInfoRsp getUserBasicInfoBean(Long userId, List<String> fields) {
        Map<String, Object> result = userBusiness.getUserInfoByUserId(userId, fields);
        if (result == null || result.isEmpty()) {
            return null;
        }
        return initUserBasicInfoRsp(result);
    }

    /**
     * @param
     * @return java.lang.Integer
     * @description 获取zk配置中心保存的系统过期时间，如果配置中心没有获取到，则使用系统默认的过期时间
     * @author hantongyang
     * @time 2016/12/22 16:03
     * @method getSurvivalTime
     */
    private Integer getSurvivalTime() {
        //获取配置中心过期时间
        String cacheSysConfig = null;
        try {
            cacheSysConfig = commonBusiness.getCacheSysConfig(ZkConfigConstant.SYSTEM_TIMEOUT.getZkValue());
        } catch (Exception e) {
            throw new SydException(ReturnCode.CONFIG_DATA_NULL);
        }
        return Integer.valueOf(cacheSysConfig);
    }

    /**
     * @param baseInfoDto
     * @return void
     * @description 更新用户信息
     * @author Richard Core
     * @time 2016/12/27 21:22
     * @method updateUserInfo
     */
    @Override
    public void updateUserInfo(UserBaseInfoDto baseInfoDto) {
        userBusiness.updateUserInfo(baseInfoDto);
        //清空缓存中的用户信息
        redisBusiness.delValue(RedisKeyEnum.SYD_USER_INFO.getValue() + baseInfoDto.getUserId());
    }

    /**
     * @param request
     * @return String
     * @description 根据卡号查询发卡行信息
     * @author Richard Core
     * @time 2017/1/4 16:32
     * @method getBankName
     */
    @Override
    public BankCardIssuer getBankName(SydRequest request) {
        //获取参数
        JSONObject jsonObject = JSONObject.parseObject(request.getContent());
        String bankCardNo = jsonObject.getString("bankCardNo");
        if (StringUtils.isBlank(bankCardNo)) {
            throw new SydException(ReturnCode.BANK_CARD_NULL);
        }
        BankCardDto bankCardDto = BeanHelper.cloneBean(request, BankCardDto.class);
        bankCardDto.setBankCardNo(bankCardNo);

        BankCardIssuer result = userBusiness.getCardBinByCardNo(bankCardDto);
        if (result == null) {
            throw new SydException(ReturnCode.BANKCARD_VERIFY_FAILED);
        }
        return result;
    }

    /**
     * @param request
     * @return java.lang.Integer
     * @description 获取银行卡数量
     * @author Richard Core
     * @time 2017/1/24 11:55
     * @method bankCardAmount
     */
    @Override
    public Integer bankCardAmount(SydRequest request) {
        //获取银行卡
        List<Map<String, String>> result = userBusiness.listBankCard(request);
        if (result == null || result.isEmpty()) {
            return 0;
        }
        return result.size();
    }

    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.system.DefaultBank
     * @description 根据用户ID获取用户默认入账银行卡
     * @author hantongyang
     * @time 2017/2/14 11:04
     * @method getDefaultBankByUserId
     */
    @Override
    public DefaultBank getDefaultBankByUserId(SydRequest request) {
        //验证参数
        CommonUtil.checkLoginStatus(request);
        return userDefaultBankBusiness.findDefaultBankByUserId(request.getUserId());
    }

    /**
     * @description 验证用户身份证号是否已经实名认证
     * @author hantongyang
     * @time 2017/3/4 15:13
     * @method checkCardNo
     * @param cardNo
     * @return UserBasicInfoRsp
     */
    @Override
    public UserBasicInfoRsp checkCardNo(String cardNo) {
        //验证参数
        if(StringUtils.isBlank(cardNo)){
            throw new SydException(ReturnCode.CARD_NO_NULL);
        }
        Map<String, Object> userInfoMap = userBusiness.getUserInfoByCardNo(cardNo);
        if(userInfoMap == null || userInfoMap.isEmpty()){
            return null;
        }
        return initUserBasicInfoRsp(userInfoMap);
    }
}
