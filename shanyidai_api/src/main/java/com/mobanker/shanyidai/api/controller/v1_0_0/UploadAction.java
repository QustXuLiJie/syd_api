/**
 * 
 */
package com.mobanker.shanyidai.api.controller.v1_0_0;
import com.mobanker.shanyidai.api.common.annotation.SignLoginValid;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.enums.SignLoginEnum;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.logger.LogManager;
import com.mobanker.shanyidai.api.common.logger.Logger;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.service.upload.UploadService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 文件上传相关API
 * 
 * @author chenjianping
 * @data 2016年12月9日
 */
@Controller
@RequestMapping(value = "upload")
public class UploadAction {
	private static final Logger logger = LogManager.getSlf4jLogger(UploadAction.class);

	@Resource
	UploadService uploadService;

	/**
	 * 文件上传   multipart/form-data
	 */
	@SignLoginValid(SignLoginEnum.ALL)
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST,produces = "application/json")
	@ResponseBody
	public Object uploadFile(SydRequest request,HttpServletRequest httpServletRequest) {
		Object data = null;
		try {
			data = uploadService.uploadFile(request,httpServletRequest);
		} catch (SydException e) {
			throw e;
		} catch (Throwable e) {
			throw new SydException(ReturnCode.UPLOAD_AVATAR_FAILED.getCode(), ReturnCode.UPLOAD_AVATAR_FAILED.getDesc(), e);
		}
		return data;
	}

	@SignLoginValid(SignLoginEnum.ALL)
	@RequestMapping(value = "/queryFileByParam", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Object queryFile(SydRequest request){

		Object data = null;
		try {
			data = uploadService.queryFileByParams(request);
		} catch (SydException e) {
			throw e;
		} catch (Throwable e) {
			throw new SydException(ReturnCode.QUERY_FILE_ERROR.getCode(), ReturnCode.QUERY_FILE_ERROR.getDesc(), e);
		}
		return data;
	}
}
