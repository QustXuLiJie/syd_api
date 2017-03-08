package com.mobanker.shanyidai.api.service.account;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mobanker.shanyidai.api.business.account.AccountBusiness;
import com.mobanker.shanyidai.api.business.common.CommonBusiness;
import com.mobanker.shanyidai.api.business.redis.RedisBusiness;
import com.mobanker.shanyidai.api.business.upload.UploadBusiness;
import com.mobanker.shanyidai.api.business.user.UserBusiness;
import com.mobanker.shanyidai.api.common.annotation.BusinessFlowAnnotation;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.constant.ZkConfigConstant;
import com.mobanker.shanyidai.api.common.enums.DatePattern;
import com.mobanker.shanyidai.api.common.enums.RedisKeyEnum;
import com.mobanker.shanyidai.api.common.enums.SmsTypeEnum;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.logger.LogManager;
import com.mobanker.shanyidai.api.common.logger.Logger;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.common.tool.CommonUtil;
import com.mobanker.shanyidai.api.common.tool.DateKit;
import com.mobanker.shanyidai.api.dto.account.UserLoginLock;
import com.mobanker.shanyidai.api.dto.account.UserRegRequest;
import com.mobanker.shanyidai.api.dto.upload.UploadResultDto;
import com.mobanker.shanyidai.api.dto.user.UserBaseInfo;
import com.mobanker.shanyidai.api.dto.user.UserBasicInfoRsp;
import com.mobanker.shanyidai.api.service.account.util.AccountBizConvert;
import com.mobanker.shanyidai.api.service.message.MessageService;
import com.mobanker.shanyidai.api.service.user.UserService;
import com.mobanker.shanyidai.api.service.user.util.UserBusinessConvertUtils;
import com.mobanker.shanyidai.dubbo.dto.upload.FileActionDto;
import com.mobanker.shanyidai.dubbo.dto.user.UserBaseInfoDto;
import com.mobanker.shanyidai.dubbo.dto.user.UserLoginDto;
import com.mobanker.shanyidai.dubbo.dto.user.UserRegisterDto;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Richard Core
 * @description
 * @date 2016/12/12 17:41
 */
@Service
@BusinessFlowAnnotation
public class AccountServiceImpl implements AccountService {
    private Logger logger = LogManager.getSlf4jLogger(getClass());
    @Resource
    private AccountBusiness accountBusiness;
    @Resource
    private UserService userService;
    @Resource
    private UserBusiness userBusiness;
    @Resource
    private RedisBusiness redisBusiness;
    @Resource
    private CommonBusiness commonBusiness;
    @Resource
    private MessageService messageService;
    @Resource
    private UploadBusiness uploadBusiness;

    /**
     * @param request
     * @param appVersion
     * @param appType
     * @return java.lang.Object
     * @description 注册
     * @author Richard Core
     * @time 2016/12/10 16:47
     * @method register
     */
    @Override
    public UserBaseInfo register(SydRequest request, String appVersion, String appType) throws SydException {
        //获取和验证参数
        UserRegRequest userRegRequest = AccountBizConvert.mapUserRegRequest(request);
        //验证验证码
        messageService.checkSmsCaptcha(request, appVersion,
                userRegRequest.getPhone(), userRegRequest.getVerifyCode(), SmsTypeEnum.REGISTER);
        //注册
        UserRegisterDto userRegisterBean = AccountBizConvert.mapRegistParam(request, userRegRequest, appVersion);
        accountBusiness.register(userRegisterBean);

        // 登录一次 记录code
        UserBaseInfo result = login(request, userRegisterBean.getPhone(), userRegisterBean.getPassword(), appVersion);

        return result;
    }


    /**
     * @param request
     * @param appVersion
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @description 登录
     * @author Richard Core
     * @time 2016/12/15 11:49
     * @method login
     */
    @Override
    public UserBaseInfo login(SydRequest request, String appVersion) throws SydException {
        //获取参数
        JSONObject jsonObj = JSON.parseObject(request.getContent());
        String phone = jsonObj.getString("phone");
        String password = jsonObj.getString("password");
        //登录
        UserBaseInfo result = login(request, phone, password, appVersion);
        return result;
    }

    /**
     * @param request
     * @param phone
     * @param password
     * @param appVersion
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @description request中没有手机号密码时使用
     * @author Richard Core
     * @time 2016/12/19 11:10
     * @method login
     */
    @Override
    public UserBaseInfo login(SydRequest request, String phone, String password, String appVersion) throws SydException {
        //参数校验
        CommonUtil.checkPhone(phone);
        CommonUtil.checkPassword(password);
        //验证用户锁定
        if (checkUserLock(phone)) {
            throw new SydException(ReturnCode.USER_LOCKED);
        }
        //封装参数
        UserLoginDto userLoginDto = AccountBizConvert.mapLoginEsbParam(request, phone, password, appVersion);
        //登录验证
        UserBaseInfo user = null;
        try {
            user = accountBusiness.login(userLoginDto);
        } catch (SydException e) {
            // 记录登录失败的次数 只有账号密码输入错误的时候记录
            if (ReturnCode.PHONE_PASSWORD_INVALID.getCode().equals(e.errCode)) {
                addUserLoginFailTimes(userLoginDto.getPhone());
            }
            throw e;
        }

        //获取用户头像
        try {
            FileActionDto fileActionDto = new FileActionDto();
            fileActionDto.setUserId(user.getUserId());
            fileActionDto.setFileType("avatar");
            UploadResultDto dto = uploadBusiness.queryFileByParams(fileActionDto);
            if (dto != null) {
                user.setAvatar(dto.getUrl());
            }
        } catch (Exception e) {
            logger.warn("获取头像失败" + e);
        }


        //生成code
        String code = generateCode(user, request);
        user.setCode(code);
        //重复登录剔除
        String repeatLoginKey = RedisKeyEnum.LOGIN_REPEAT.getValue() + user.getUserId();
        String storeCode = redisBusiness.getValue(repeatLoginKey);
        if (!StringUtils.isBlank(storeCode)) {
//            userService.removeUserCode(storeCode);
        }
        //记录登陆凭证code
        userService.addUserCode(user.getCode(), user.getUserId());
        redisBusiness.setValue(repeatLoginKey,user.getCode());
        //记录登陆用户信息
        String userStr = JSON.toJSONString(user);
        redisBusiness.setValue(RedisKeyEnum.SYD_USER_BASE_INFO.getValue() + user.getUserId(), userStr);
        request.setUserId(user.getUserId());
        return user;
    }

    /**
     * @param user
     * @param request
     * @return java.lang.String
     * @description 生成code
     * @author Richard Core
     * @time 2016/12/24 16:33
     * @method generateCode
     */
    private String generateCode(UserBaseInfo user, SydRequest request) {
        StringBuffer sb = new StringBuffer();
        sb.append(user.getUserId());
        sb.append(user.getPassword());
        sb.append(request.getChannel());
        sb.append(System.currentTimeMillis());
        String loginCode = getLoginCode(sb.toString());

        return loginCode;
    }

    public String getLoginCode(String str) {
        //1、再添加一段时间戳，在有重复code的时候，递归本方法重新生成的时候使用
        str += System.currentTimeMillis();
        //2:对整段str进行加密.
        byte[] base64 = Base64.encodeBase64(str.getBytes());
        String code = null;
        try {
            code = DigestUtils.md5Hex(base64);
            //code = new String(DigestUtils.md5(base64), "UTF-8");
        } catch (Exception e) {
            throw new SydException(ReturnCode.REQUEST_TIMEOUT);
        }
        //3:解决code的唯一性(前辍+code)
        if (redisBusiness.exists(RedisKeyEnum.SYD_USER_CODE.getValue() + code)) {
            return getLoginCode(str);
        }
        return code;
    }


    /**
     * @param phone
     * @return boolean
     * @description 验证用户是否被锁定
     * 如果被锁定计算当前时间是否已经超过锁定时间，超过则将该用户的锁定信息删除，否则返回true
     * 如果未被锁定，则返回false
     * @author hantongyang
     * @time 2016/12/24 14:30
     * @method checkUserLock
     */
    private boolean checkUserLock(String phone) {
        if (StringUtils.isBlank(phone)) {
            throw new SydException(ReturnCode.PARAM_REQUIRED);
        }
        UserLoginLock value = redisBusiness.getValue(RedisKeyEnum.SYD_USER_LOGIN_FAIL.getValue() + phone, UserLoginLock.class);
        if (value == null) {
            return Boolean.FALSE;
        }
        //判断上次输错是不是今天
        if (!checklockToday(phone, value.getLockTime())) {
            return Boolean.FALSE;
        }
        //判断是否是5次
        Integer count = getUserMaxLockNum();
        //如果输入错误次数小于5次，表示未被锁定
        if (value.getFailCount() < count) {
            return Boolean.FALSE;
        }
        //计算是否在锁定时间内
//        String lockTimeStr = commonBusiness.getCacheSysConfig(SystemConstant.SYD_LOGIN_LOCK_TIME.getValue());
//        Long time = StringUtils.isBlank(lockTimeStr) ? Long.parseLong(SystemConstant.LOGIN_LOCK_TIME.getValue()) : Long.parseLong(lockTimeStr);
//        //如果当前时间大于（锁定时的时间+锁定时长），表示已经解锁，删除缓存中的记录并返回false
//        if (DateKit.getNowTime().intValue() > value.getLockTime().intValue() + time.intValue()) {
//            redisBusiness.delValue(RedisKeyEnum.SYD_USER_LOGIN_FAIL.getValue() + phone);
//            return Boolean.FALSE;
//        }
        return Boolean.TRUE;
    }

    /**
     * @param phone
     * @param lockTime
     * @return boolean
     * @description 判断上次输错是不是今天
     * @author Richard Core
     * @time 2016/12/24 19:02
     * @method checklockToday
     */
    private boolean checklockToday(String phone, Long lockTime) {
        Date lockDate = new Date(lockTime * 1000);
        String lockDateStr = DateKit.formatDate(lockDate, DatePattern.DATE_NOSEP.getPattern());
        String nowDateStr = DateKit.formatDate(new Date(), DatePattern.DATE_NOSEP.getPattern());
        if (lockDateStr.equals(nowDateStr)) {
            redisBusiness.delValue(RedisKeyEnum.SYD_USER_LOGIN_FAIL.getValue() + phone);
            return true;
        }
        return false;
    }

    /**
     * @param
     * @return java.lang.Integer
     * @description 获取配置系统 登录失败次数
     * @author Richard Core
     * @time 2016/12/24 18:14
     * @method getUserMaxLockNum
     */
    private Integer getUserMaxLockNum() {
        String cacheSysConfig = null;
        try {
            cacheSysConfig = commonBusiness.getCacheSysConfig(ZkConfigConstant.SYD_LOGIN_FAIL_COUNT.getZkValue());
        } catch (Exception e) {
            throw new SydException(ReturnCode.CONFIG_DATA_NULL);
        }
        return Integer.parseInt(cacheSysConfig);
    }

    /**
     * @param phone
     * @return void
     * @description 增加失败次数
     * @author Richard Core
     * @time 2016/12/24 19:13
     * @method addUserLoginFailTimes
     */
    private void addUserLoginFailTimes(String phone) {
        UserLoginLock value = redisBusiness.getValue(RedisKeyEnum.SYD_USER_LOGIN_FAIL.getValue() + phone, UserLoginLock.class);
        Long lockTime = DateKit.getNowTime();
        int failCount = 1;
        //判断上次输错是不是今天，如果是当天，失败次数加1 如果不是当天，次数为1
        if (value != null && checklockToday(phone, value.getLockTime())) {
            failCount = value.getFailCount() + 1;
        }
        saveUserLoginLock(phone, failCount, lockTime);
    }

    /**
     * @param phone
     * @param failCount
     * @param lockTime
     * @return void
     * @description 保存用户登录失败信息
     * @author hantongyang
     * @time 2016/12/24 15:20
     * @method saveUserLoginLock
     */
    private void saveUserLoginLock(String phone, int failCount, Long lockTime) {
        UserLoginLock bean = new UserLoginLock();
        bean.setPhone(phone);
        //查询配置系统中的次数
        Integer count = getUserMaxLockNum();
        //如果错误次数大于等于配置系统中配置的错误次数，则表示需要锁定用户，更新锁定时间为当前时间、缓存过期时间为当前时间
        if (failCount >= count) {
            bean.setFailCount(count);
            lockTime = DateKit.getNowTime();
        }
        bean.setLockTime(lockTime);
        redisBusiness.setValue(RedisKeyEnum.SYD_USER_LOGIN_FAIL.getValue() + phone, JSONObject.toJSONString(bean), lockTime.intValue());
    }

    /**
     * @param request
     * @param appVersion
     * @return void
     * @description request中有code时 退出登录
     * @author Richard Core
     * @time 2016/12/20 16:06
     * @method logout
     */
    @Override
    public void logout(SydRequest request, String appVersion) throws SydException {
        JSONObject jsonObj = JSON.parseObject(request.getContent());
        String code = jsonObj.getString("code");

        logout(request, code, appVersion);
    }

    /**
     * @param request
     * @param code
     * @param appVersion
     * @return void
     * @description request中没有code时 退出登录
     * @author Richard Core
     * @time 2016/12/20 16:05
     * @method logout
     */
    @Override
    public void logout(SydRequest request, String code, String appVersion) throws SydException {
//        UserLogout userLogout = new UserLogout();
//        userLogout.setCode(code);
//        userLogout.setUserId(request.getUserId());
//        BeanHelper.packageBean(request, userLogout, appVersion);
//
//        accountBusiness.logout(userLogout);
        //将code移出缓存
        userService.removeUserCode(code);
    }

    /**
     * @param request
     * @param appVersion
     * @return void
     * @description 修改密码
     * @author Richard Core
     * @time 2016/12/20 18:32
     * @method setPassword
     */
    @Override
    public void setPassword(SydRequest request, String appVersion) throws SydException {
        //获取参数
        JSONObject jsonObj = JSON.parseObject(request.getContent());
        String code = jsonObj.getString("code");
        String oldPasswd = jsonObj.getString("oldPassword");
        String newPasswd = jsonObj.getString("newPassword");
        Long userId = request.getUserId();

        //参数校验
        CommonUtil.checkPassword(oldPasswd);
        CommonUtil.checkPassword(newPasswd);
        if (userId == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        //新旧密码不能一样
        if (oldPasswd.equals(newPasswd)) {
            throw new SydException(ReturnCode.NEWPWD_OLDPWD_SAME);
        }
        //参数配置
        UserBaseInfoDto baseInfoDto = BeanHelper.cloneBean(request, UserBaseInfoDto.class);
        baseInfoDto.setVersion(appVersion);
        baseInfoDto.setUserId(userId);
        Map<String, Object> saveFields = new HashMap<String, Object>();
        saveFields.put("password", newPasswd);
        baseInfoDto.setSaveFields(saveFields);
        Map<String, Object> commonFields = UserBusinessConvertUtils.initCommonFields(request);
        baseInfoDto.setCommonFields(commonFields);
        userService.updateUserInfo(baseInfoDto);
        //退出登录
        logout(request, code, appVersion);
    }

    /**
     * @param request
     * @param appVersion
     * @return void
     * @description 找回密码
     * @author Richard Core
     * @time 2016/12/20 18:33
     * @method forgetPassword
     */
    @Override
    public void forgetPassword(SydRequest request, String appVersion) throws SydException {
        //参数获取
        JSONObject jsonObj = JSON.parseObject(request.getContent());
        String phone = jsonObj.getString("phone");
        String newPassword = jsonObj.getString("newPassword");
        String verifyCode = jsonObj.getString("verifyCode");
//        String subCardId = jsonObj.getString("subCardId"); //暂未使用

        //参数校验
        CommonUtil.checkPhone(phone);
        CommonUtil.checkPassword(newPassword);
        //根据手机号获取用户信息
        Map<String, String> userInfo = userBusiness.getUserInfoByPhone(phone, "phone");
        if (userInfo == null || StringUtils.isBlank(userInfo.get("userId"))) {
            throw new SydException(ReturnCode.GET_USERINFO_FAILED);
        }
        long userId = Long.valueOf(userInfo.get("userId"));
        //校验验证码
        messageService.checkSmsCaptcha(request, appVersion, phone, verifyCode, SmsTypeEnum.FORGET_PWD);

        //更新参数配置
        UserBaseInfoDto baseInfoDto = BeanHelper.cloneBean(request, UserBaseInfoDto.class);
        baseInfoDto.setVersion(appVersion);
        baseInfoDto.setUserId(userId);
        Map<String, Object> saveFields = new HashMap<String, Object>();
        saveFields.put("password", newPassword);
        baseInfoDto.setSaveFields(saveFields);
        Map<String, Object> commonFields = UserBusinessConvertUtils.initCommonFields(request);
        baseInfoDto.setCommonFields(commonFields);
        userService.updateUserInfo(baseInfoDto);
    }


    /**
     * @param request
     * @param appVersion
     * @return void
     * @description 修改手机号
     * @author Richard Core
     * @time 2016/12/20 18:35
     * @method updatePhone
     */
    @Override
    public void updatePhone(SydRequest request, String appVersion) throws SydException {
        //参数获取
        JSONObject jsonObj = JSON.parseObject(request.getContent());
        String code = jsonObj.getString("code");
        String phone = jsonObj.getString("phone");
        String password = jsonObj.getString("password");
        String verifyCode = jsonObj.getString("verifyCode");
//        String subCardId = jsonObj.getString("subCardId");
        //参数验证
        CommonUtil.checkPhone(phone);
        CommonUtil.checkPassword(password);
        Long userId = request.getUserId();
        if (userId == null) {
            throw new SydException(ReturnCode.LOGIN_TIME_OUT);
        }
        //校验验证码
        messageService.checkSmsCaptcha(request, appVersion,
                phone, verifyCode, SmsTypeEnum.MODIFY_PHONE);
        //根据手机号获取用户信息
        checkPhoneInuse(phone, userId);
        //验证用户id和密码是否匹配
        UserBasicInfoRsp userInfo = userService.getUserInfoByUserId(userId, "password");
        if (userInfo == null || StringUtils.isBlank(userInfo.getPassword())) {
            throw new SydException(ReturnCode.GET_USERINFO_FAILED);
        }
        if (!password.equals(userInfo.getPassword())) {
            throw new SydException(ReturnCode.WRONG_PASSWORD);
        }
        //参数配置
        UserBaseInfoDto baseInfoDto = BeanHelper.cloneBean(request, UserBaseInfoDto.class);
        baseInfoDto.setVersion(appVersion);
        baseInfoDto.setUserId(userId);
        Map<String, Object> saveFields = new HashMap<String, Object>();
        saveFields.put("phone", phone);
        baseInfoDto.setSaveFields(saveFields);
        Map<String, Object> commonFields = UserBusinessConvertUtils.initCommonFields(request);
        baseInfoDto.setCommonFields(commonFields);
        userService.updateUserInfo(baseInfoDto);
        //退出登录
        logout(request, code, appVersion);
    }

    private void checkPhoneInuse(String phone, Long userId) {
        Map<String, String> userInfo = null;
        //根据手机号查询用户，异常则认为没有查到用户
        try {
            userInfo = userBusiness.getUserInfoByPhone(phone, "phone");
        } catch (SydException e) {
            //没有查到用户认为可以修改 只有返回没有查到用户的返回码 才是正常业务码，其他都是异常码 不能捕获
            if (ReturnCode.PHONE_UNREGISTED.getCode().equals(e.errCode)) {
                return;
            }
            throw e;
        }
        if (userInfo == null || StringUtils.isBlank(userInfo.get("userId"))) {
            return;
        }
        //查到用户就不能修改
        long userId2 = Long.valueOf(userInfo.get("userId"));
        if (!userId.equals(userId2)) {
            throw new SydException(ReturnCode.PHONE_INUSE);
        } else {
            throw new SydException(ReturnCode.PHONE_NOTCHANGE);
        }
    }
}
