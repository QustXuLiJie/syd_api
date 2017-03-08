/**
 * 
 */
package com.mobanker.shanyidai.api.controller.v1_0_0;

import com.mobanker.framework.constant.Constants;
import com.mobanker.shanyidai.api.common.annotation.SignLoginValid;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.enums.SignLoginEnum;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.ResponseEntity;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.service.user.UserAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 阿里芝麻信息相关API
 * 
 * @author chenjianping
 * @data 2016年12月9日
 */
@Controller
@RequestMapping(value = "user/alipay")
public class AlipayAction {

	@Resource
	private UserAuthService userAuthService;

	/**
	 * @api {post} /alipay/getZhima 读取芝麻分
	 * @apiVersion 4.0.0
	 * @apiName getZhima
	 * @apiGroup alipay
	 * @apiDescription 读取芝麻分
	 * @apiParam {String} code code码*
	 *
	 * @apiSuccess {Number} status 1,成功，0，失败
	 *
	 */
	@SignLoginValid(SignLoginEnum.ALL)
	@RequestMapping(value = "/getZhima", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Object getZhima(SydRequest request) {
		Object data = null;
		try{
			data = userAuthService.getAlipayScore(request);
		}catch (SydException e){
			if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
				throw e;
			} else {
				throw new SydException(ReturnCode.GET_ALIPAY_SCORE_FAILED.getCode(), e.message, e);
			}
		} catch (Throwable e) {
			throw new SydException(ReturnCode.GET_ALIPAY_SCORE_FAILED, e);
		}
		return data;
	}



	/**
	 * @param request
	 * @return java.lang.Object
	 * @description 保存芝麻信息 :芝麻分 和 是否跳过芝麻认证
	 * @author Richard Core
	 * @time 2017/1/23 20:14
	 * @method saveZhimaInfo
	 */
	@RequestMapping(value = "saveZhimaInfo", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Object saveZhimaInfo(SydRequest request) {

		Object data = null;
		try {
			data = userAuthService.saveZhimaInfo(request);
		} catch (SydException e) {
			throw new SydException(ReturnCode.SAVE_ALIPAY_SCORE_FAILED.getCode(), e.message, e);
		} catch (Throwable e) {
			throw new SydException(ReturnCode.SAVE_ALIPAY_SCORE_FAILED, e);
		}
		return data;
	}

	/**
	 * @param request
	 * @return java.lang.Object
	 * @description 获取芝麻分 和 是否跳过芝麻认证
	 * @author Richard Core
	 * @time 2017/1/23 20:13
	 * @method getZhimaInfo
	 */
	@RequestMapping(value = "getZhimaInfo", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Object getZhimaInfo(SydRequest request) {

		Object data = null;
		try {
			data = userAuthService.getZhimaInfo(request);
		} catch (SydException e) {
			throw e;
		} catch (Throwable e) {
			throw new SydException(ReturnCode.GET_ALIPAY_SCORE_FAILED, e);
		}
		return data;
	}

	/**
	 * @description 获取芝麻认证URL
	 * @author hantongyang
	 * @time 2017/1/24 9:24
	 * @method getZhimaURL
	 * @param request
	 * @return java.lang.Object
	 */
	@RequestMapping(value = "/getZhimaURL", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Object getZhimaURL(SydRequest request){
		Object data = null;
		try{
			data = userAuthService.getZhimaURL(request);
		}catch (SydException e){
			if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
				throw e;
			} else {
				throw new SydException(ReturnCode.GET_ALIPAY_SCORE_FAILED.getCode(), e.message, e);
			}
		} catch (Throwable e) {
			throw new SydException(ReturnCode.GET_ALIPAY_SCORE_FAILED, e);
		}
		return data;
	}

	/**
	 * @description 验证芝麻认证是否授权
	 * @author hantongyang
	 * @time 2017/1/24 9:25
	 * @method checkZhimaAuth
	 * @param request
	 * @return java.lang.Object
	 */
	@RequestMapping(value = "/checkZhimaAuth", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Object checkZhimaAuth(SydRequest request){
		Object data = null;
		try{
			data = userAuthService.checkZhimaAuth(request);
		}catch (SydException e){
			if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
				throw e;
			} else {
				throw new SydException(ReturnCode.GET_ALIPAY_CHEACK_AUTH_FAILED.getCode(), e.message, e);
			}
		} catch (Throwable e) {
			throw new SydException(ReturnCode.GET_ALIPAY_CHEACK_AUTH_FAILED, e);
		}
		return data;
	}
	/**
	 * @description 芝麻认证接口
	 * @author hantongyang
	 * @time 2017/1/24 9:25
	 * @method checkZhimaAuth
	 * @param request
	 * @return java.lang.Object
	 */
	@RequestMapping(value = "/zhimaAuth", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Object zhimaAuth(SydRequest request){
		ResponseEntity response = null;
		try {
			userAuthService.zhimaAuth(request);
			response = new ResponseEntity(Constants.System.OK);
		}catch (SydException e){
			if (e.errCode.startsWith(ReturnCode.CODE_PREFIX.getCode())) {
				throw e;
			} else {
				throw new SydException(ReturnCode.GET_ALIPAY_AZHIMAAUTH_FAILED.getCode(), e.message, e);
			}
		} catch (Throwable e) {
			throw new SydException(ReturnCode.GET_ALIPAY_AZHIMAAUTH_FAILED, e);
		}
		return response;
	}
}
