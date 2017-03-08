package com.mobanker.shanyidai.api.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mobanker.framework.constant.Constants;
import com.mobanker.shanyidai.api.common.annotation.SignLoginValid;
import com.mobanker.shanyidai.api.common.constant.AppConstants;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.constant.SystemConstant;
import com.mobanker.shanyidai.api.common.enums.SignLoginEnum;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.ResponseEntity;
import com.mobanker.shanyidai.api.common.security.ThreeDes;
import com.mobanker.shanyidai.api.common.tool.IPUtils;
import com.mobanker.shanyidai.api.common.tool.StringKit;
import com.mobanker.shanyidai.api.dto.security.ShakeInfo;
import com.mobanker.shanyidai.api.service.common.CommonService;
import com.mobanker.shanyidai.api.service.security.SecurityService;
import com.mobanker.shanyidai.api.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author: hanty
 * @date 创建时间：2016-12-14 16:20
 * @version 1.0
 * @parameter
 * @return
 */
public class SignLoginAop {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	private UserService userService;
	@Resource
	private SecurityService securityService;
	@Resource
	private CommonService commonService;

	/**
	 * 处理运行异常的切面
	 *
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws SydException
	 *
	 * */
	public Object validSign(ProceedingJoinPoint pjp) throws Throwable {
		Object retVal = null;
		Object returnMessage = null;
//		String className = pjp.getTarget().getClass().getSimpleName();
		String methodName = pjp.getSignature().getName();
		long startDate = (new Date()).getTime();
		String serial = StringKit.getUUID();
		try {

			Object[] args = pjp.getArgs();
			RequestAttributes ra = RequestContextHolder.getRequestAttributes();
			ServletRequestAttributes sra = (ServletRequestAttributes) ra;
			HttpServletRequest request = sra.getRequest();

			Class<?> classTarget = pjp.getTarget().getClass();
			Class<?>[] par = ((MethodSignature) pjp.getSignature()).getParameterTypes();
			Method objMethod = classTarget.getMethod(methodName, par);
			SignLoginValid auth = objMethod.getAnnotation(SignLoginValid.class);
			String valid = "ALL";
			if (auth != null) {
				valid = auth.value().toString();
			}
			if(!SignLoginEnum.NOTALL.name().equals(valid) || args.length>0){
				Object obj = args[0];
				Class cls = obj.getClass();

				JSONObject jso = JSONObject.parseObject(JSONObject.toJSONString(obj));
				jso.put(AppConstants.DICT.IP, IPUtils.getIp(request));
				jso.put(AppConstants.DICT.CHANNELENV, SystemConstant.APP_CHANNEL.getValue());
				jso.put(AppConstants.DICT.PRODUCTENV, SystemConstant.PRODUCT.getValue());
				jso.put(AppConstants.DICT.VERSIONENV, commonService.getVersion());
				String msg = jso.getString(AppConstants.DICT.REQDATA);
				//判断解密
				if (isSign(jso.getString(AppConstants.DICT.IP))
						&& (SignLoginEnum.ALL.name().equals(valid) || SignLoginEnum.SIGN.name().equals(valid))) {
					if (StringUtils.isBlank(msg)) {
						returnMessage = new ResponseEntity(Constants.System.FAIL, ReturnCode.PARAMS_ILLEGE.getCode(), ReturnCode.PARAMS_ILLEGE.getDesc(), null);
						return returnMessage;
					}

					String uuid = jso.getString(AppConstants.DICT.REQUUID);
					logger.debug("uuid", uuid);
					if (!StringKit.checkStringLength(uuid, 32)) {
						returnMessage = new ResponseEntity(Constants.System.FAIL, ReturnCode.UUID_ERROR.getCode(), ReturnCode.UUID_ERROR.getDesc(), null);
						return returnMessage;
					}
					//取加密
					ShakeInfo shakeInfo = securityService.getThreeDesKey(uuid);
					if (shakeInfo == null) {
						returnMessage = new ResponseEntity(Constants.System.FAIL, ReturnCode.UUID_TIME_OUT.getCode(), ReturnCode.UUID_TIME_OUT.getDesc(), null);
						return returnMessage;
					}
					String content = ThreeDes.decryptThreeDESECB(msg, shakeInfo.getKey());
					if (StringUtils.isBlank(content)) {
						returnMessage = new ResponseEntity(Constants.System.FAIL, ReturnCode.SIGN_VERIFY_FAILED.getCode(), ReturnCode.SIGN_VERIFY_FAILED.getDesc(), null);
						return returnMessage;
					}
					jso.put(AppConstants.DICT.REQCONTENT, content);
				} else {
					jso.put(AppConstants.DICT.REQCONTENT, msg);
				}

				//判断code
				if (SignLoginEnum.ALL.name().equals(valid)|| SignLoginEnum.LOGIN.name().equals(valid)) {
					if (StringUtils.isBlank(msg)) {
						returnMessage = new ResponseEntity(Constants.System.FAIL, ReturnCode.PARAMS_ILLEGE.getCode(), ReturnCode.PARAMS_ILLEGE.getDesc(), null);
						return returnMessage;
					}
					//获取内容中的CODE
					String content = jso.getString(AppConstants.DICT.REQCONTENT);
					String code = JSON.parseObject(content).getString(AppConstants.DICT.REQCODE);
					//验证用户是否过期，未过期则更新过期时间
					Long userId = userService.getUserCode(code);
					if (userId == null) {
						returnMessage = new ResponseEntity(Constants.System.FAIL, ReturnCode.LOGIN_TIME_OUT.getCode(), ReturnCode.LOGIN_TIME_OUT.getDesc(), null);
						return returnMessage;
					}
					jso.put(AppConstants.DICT.REQUSERID, userId);
				}else{
					if(StringUtils.isNotBlank(msg)) {
						String content = jso.getString(AppConstants.DICT.REQCONTENT);
						String code = JSON.parseObject(content).getString(AppConstants.DICT.REQCODE);
						if (StringUtils.isNotBlank(code)) {
							Long userId = userService.getUserCode(code);
							if (userId == null) {
								returnMessage = new ResponseEntity(Constants.System.FAIL, ReturnCode.LOGIN_TIME_OUT.getCode(), ReturnCode.LOGIN_TIME_OUT.getDesc(), null);
								return returnMessage;
							}
							jso.put(AppConstants.DICT.REQUSERID, userId);
						}
					}
				}

				args[0] = JSONObject.parseObject(jso.toJSONString(), cls);
			}
			if(args.length>0){
				logger.info("\n###################serial:{} methodName:{} all request######################\n\n{}"
								+ "\n\n################### methodName:{} end request######################",serial,methodName,
						JSONObject.toJSONString(args[0]),methodName);
			}
			retVal = pjp.proceed(args);
		} catch (Throwable e1) {
			logger.error("发生业务异常:{}", e1);
			throw e1;
		}finally{
			long endDate = (new Date()).getTime();
			logger.info("\n###################serial:{}  methodName:{} all result######################\n\n{}"
							+ "\n\n################### methodName:{} end res  ult######################\n总耗时:{}毫秒",serial,methodName,
					JSONObject.toJSONString(retVal),methodName,endDate-startDate);
		}
		return retVal;
	}

	/**
	 * 本地调试时使用
	 * @param ip
	 * @return
	 */
	private boolean isSign(String ip){
		//TODO
		if("127.0.0.1".equals(ip)){
			return false;
		}
		return true;
	}
}
