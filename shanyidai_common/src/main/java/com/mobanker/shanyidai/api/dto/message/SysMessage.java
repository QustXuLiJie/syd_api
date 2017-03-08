package com.mobanker.shanyidai.api.dto.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class SysMessage implements Serializable {
	private static final long serialVersionUID = 332986629673307790L;
	private Long id;//Id
	private Long userId;//用户ID
	private int status;//发送状态
	private String name;//标题
	private String contents;//内容
	private String messageType;//消息类型
	private Date readTime;//读取时间
	private String readStatus;//读取状态 1 已读 0 未读
	private String hread;//读取状态 Isread 已读 unread未读
	private Date createTime;//创建时间
	private String createUser;//创建人
	private Date updateTime;//创建人
	private String updateUser;//创建人
	private String remoteIp;//远程ip
	private String serverIp;//服务器ip
	private String product;//产品
	private String systemCode;//系统编码
	private int usetype;//短信类型 1通知 2营销 3催收 5验证码 6预警
	private String flagId;//唯一码
}
