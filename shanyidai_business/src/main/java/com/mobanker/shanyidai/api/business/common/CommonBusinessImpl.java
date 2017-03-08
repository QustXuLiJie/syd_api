package com.mobanker.shanyidai.api.business.common;


import com.alibaba.dubbo.common.utils.StringUtils;
import com.mobanker.shanyidai.api.common.cache.SystemEntityCache;
import com.mobanker.shanyidai.api.common.cache.bean.SystemConfig;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.constant.SystemConstant;
import com.mobanker.shanyidai.api.common.constant.ZkConfigConstant;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.logger.LogManager;
import com.mobanker.shanyidai.api.common.logger.Logger;
import com.mobanker.shanyidai.api.common.tool.DateKit;
import com.mobanker.shanyidai.api.dto.system.HolidayBean;
import com.mobanker.shanyidai.esb.common.constants.ErrorConstant;
import com.mobanker.shanyidai.esb.common.exception.EsbException;
import com.mobanker.shanyidai.esb.common.packet.ResponseEntity;
import com.mobanker.zkc.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
public class CommonBusinessImpl implements CommonBusiness {
    private Logger logger = LogManager.getSlf4jLogger(getClass());

    /**
     * 检查返回实体校验状态
     */
    @Override
    public Object checkRetData(ResponseEntity re) throws SydException {
        Object obj = null;
        logger.info("调用Dubbo接口，响应成功，返回数据：{}", re);
        //返回值为空
        if (re == null) {
            throw new SydException(ReturnCode.SYS_EXCEPTION.getCode(), "调用Dubbo接口异常，返回数据为空！", null);
        }
        //有返回值，验证返回结果状态
        if (SystemConstant.OK.getValue().equals(re.getStatus())) {
            obj = re.getData();
        } else if (SystemConstant.FAIL.getValue().equals(re.getStatus())) {
            switch (re.getError()) {
                case "4000022": //[{"error":"4000022","msg":"银行名称与系统字典不匹配 !","status":"0"}]
                    throw new SydException(ReturnCode.BANKNAME_CARDNUM_NOT_EQUALS.getCode(), ReturnCode.BANKNAME_CARDNUM_NOT_EQUALS.getDesc(), null);
//				case "4000006": //[{"error":"4000006","msg":"验证码无效!","status":"0"}]
//					throw new KydException(Constants.VERIFYCODE_ERROR, Constants.VERIFYCODE_ERROR_MSG, null);
//				case "4000116": //[{"error":"4000116","msg":"该手机号码已注册，请直接登录！","status":"0"}]
//					throw new KydException(Constants.REGISTED_FAILED, Constants.REGISTED_FAILED_MSG, null);
////				case "4000009": //[{"error":"4000009","msg":"错误的用户名或密码!","status":"0"}]
////					throw new KydException(Constants.REGISTED_FAILED, Constants.REGISTED_FAILED_MSG, null);
                default:
                    throw new SydException(re.getError(), re.getMsg(), null);
            }
        }
        return obj;
    }

    @Override
    public void checkAuthentAccount(String account) throws SydException {
        if (StringUtils.isBlank(account)) {
            //参数有问题  用户名不能为空
            throw new SydException(ReturnCode.ACCOUNT_ISVALID.getCode(), ReturnCode.ACCOUNT_ISVALID.getDesc(), null);
        }
    }

    @Override
    public void checkAuthentPasswd(String password) throws SydException {
        if (StringUtils.isBlank(password)) {
            //参数有问题  密码不能为空
            throw new SydException(ReturnCode.WRONG_PASSWORD.getCode(), ReturnCode.WRONG_PASSWORD.getDesc(), null);
        }
    }

    @Override
    public void checkAuthentCaptcha(String captcha) throws SydException {
        if (StringUtils.isBlank(captcha) || captcha.length() < 4) {
            //参数有问题 验证码错误
            throw new SydException(ReturnCode.WRONG_VERIFYCODE.getCode(), ReturnCode.WRONG_VERIFYCODE.getDesc(), null);
        }
    }

	/**
	 * @description 从zk读取配置信息
	 * @author hantongyang
	 * @time 2016/12/22 15:35
	 * @method getCacheSysConfig
	 * @param key
	 * @return java.lang.String
	 */
	@Override
	public String getCacheSysConfig(String key) {
		String value = CacheManager.getActiveMapValue(key);
		if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
			logger.error("警告：配置系统缺少对key[{}]的配置，采用默认配置参数", key);
			value = ZkConfigConstant.getByKey(key).getDefaultValue();
		}
		return value;
	}

	/**
	 * @description 从zk读取配置的URL信息，为空时返回自定义异常
	 * @author hantongyang
	 * @time 2017/1/17 10:26
	 * @method getCacheSysConfig
	 * @param key
	 * @param errorCode
	 * @param errorMsg
	 * @return java.lang.String
	 */
	@Override
	public String getCacheSysConfig(String key, String errorCode, String errorMsg) {
		String value = null;
		try {
			value = CacheManager.getActiveMapValue(key);
		} catch (Exception e) {
			logger.warn("配置系统获取配置失败" + e);
		}
		if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
			if(StringUtils.isBlank(errorCode) && StringUtils.isBlank(errorMsg)){
				throw new SydException(ReturnCode.CONFIG_DATA_NULL);
			}else{
				throw new SydException(errorCode, errorMsg);
			}
		}
		return value;
	}

	/**
	 * @description 获取版本号
	 * @author hantongyang
	 * @time 2016/12/22 20:18
	 * @method getVersion
	 * @param version
	 * @return java.lang.String
	 */
	@Override
	public String getVersion(String version) {
		if (org.apache.commons.lang3.StringUtils.isBlank(version)) {
			version = ZkConfigConstant.SYD_APP_VERSION.getZkValue();
		}
		String value = getCacheSysConfig(version);
		return (org.apache.commons.lang3.StringUtils.isBlank(value) ? ZkConfigConstant.SYD_APP_VERSION.getDefaultValue() : value);
	}

	/**
	 * @description 校验日期是否节假日
	 * @author hantongyang
	 * @time 2016/12/22 15:34
	 * @method getHoliday
	 * @param year
	 * @return com.mobanker.shanyidai.api.dto.system.HolidayBean
	 */
	@Override
	public HolidayBean getHoliday(String year) {
		throw new UnsupportedOperationException("暂未实现");
//        return systemDao.getHoliday(year);
	}

	/**
	 * @description 读取字典表的配置信息
	 * @author hantongyang
	 * @time 2016/12/22 15:32
	 * @method getSysConfig
	 * @param key
	 * @return java.lang.String
	 */
	@Override
	public String getSysConfig(String key) {
		SystemEntityCache cacheSys = SystemEntityCache.getInstance();
		String value = cacheSys.getConfigValue(key);
		if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
			// 从数据库加载，并保存到内存
			value = getDBSysVaribleValue(key);
			cacheSys.putCache(key, value);
		} else {
			SystemConfig sysConfig = cacheSys.getSystemConfig(key);
			// 超过一分钟重新加载一次
			if (sysConfig.getUpdateDate() == null || sysConfig.getUpdateDate().before(DateKit.getSubSecondDate(60))) {
				value = getDBSysVaribleValue(key);
				cacheSys.putCache(key, value);
			}
		}
		return value;
	}

	/**
	 * @description
	 * @author hantongyang
	 * @time 2016/12/22 15:32
	 * @method getDBSysVaribleValue
	 * @param nid
	 * @return java.lang.String
	 */
	@Transactional(readOnly = true)
	private String getDBSysVaribleValue(String nid) {
		throw new UnsupportedOperationException("暂未实现");
//        String value = systemDao.getSystemValue(nid);
//        return StringUtils.isBlank(value) ? "" : value;
	}

	/**
	 * 是否外部渠道
	 *
	 * @param channel
	 * @return
	 * @auther hantongyang
	 * @date 2016/12/22
	 */
	@Override
	public boolean isOutChannel(String channel) {
		if (SystemConstant.APP_CHANNEL.getValue().equals(channel)) {
			return false;
		} else if (SystemConstant.WCHAT_CHANNEL.getValue().equals(channel)) {
			return false;
		} else {
			return true;
		}
	}
}
