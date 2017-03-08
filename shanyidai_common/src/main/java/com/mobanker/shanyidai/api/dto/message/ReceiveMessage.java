package com.mobanker.shanyidai.api.dto.message;

import lombok.Data;

import java.io.Serializable;

/**
 * 消息记录
 *
 * @author: R.Core
 * @date 创建时间：2016-12-14
 */
@Data
public class ReceiveMessage implements Serializable {
    private static final long serialVersionUID = 3540132953568626043L;
    private Long id;
    //用户ID
    private Long userId;
    //状态
    private Integer status;
    //发送ID
    private Long sendId;
    //发送用户ID
    private Long sendUserId;
    //类型
    private String type;
    //接收ID
    private Long receiveId;
    //是否接收
    private Long receiveYes;
    //接收值
    private Long receiveValue;
    //名称
    private String name;
    //内容
    private String contents;
    //发送时间
    private Long addTime;
    //发送IP
    private String addIp;
    //消息类型
    private String messageType;
    //提醒级别
    private Integer remindLevel;

    private String hread;//是否已读

    private Long beginTime;//读取开始时间

    private Long queryTime;//查询时间


}
