package com.mobanker.shanyidai.api.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/** 
 * @author: liuyafei
 * @date 创建时间：2016年8月29日
 * @version 1.0 
 * @parameter  
 * @return  
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class UserAvatar implements Serializable {
	
	
	private static final long serialVersionUID = -5336549709487915158L;
	private Long userId;   //用户id*
	private String ip;     //ip*
	private String channel;//渠道*
	private String product;//产品*
	private String fileName;//文件名称*
	

}
