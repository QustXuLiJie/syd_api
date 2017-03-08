/**
 * 
 */
package com.mobanker.shanyidai.api.controller.v1_0_0;

import com.mobanker.shanyidai.api.common.constant.SystemConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mobanker.shanyidai.api.common.packet.ResponseEntity;

/**
 * 活动处理相关API
 * 
 * @author chenjianping
 * @data 2016年12月9日
 */
@Controller
@RequestMapping(value = "activity")
public class ActivityAction {

	@RequestMapping(value = "/getActivityList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity getActivityList() {
		// 业务代码
		return new ResponseEntity(SystemConstant.OK.getValue());
	}
}
