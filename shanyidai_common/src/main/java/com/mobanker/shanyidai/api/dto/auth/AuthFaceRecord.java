package com.mobanker.shanyidai.api.dto.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @desc:
 * @author: Richard Core
 * @create time: 2017/2/23 15:00
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class AuthFaceRecord implements Serializable {
    private Long id;   //id
    private Long userId;   //用户id
    private String livenessDataId;   //语音识别数据id
    private String process;   //认证进度，AuthFaceProcessEnum
    private String status;   //认证记录状态
    private String reason;   //失败原因
    private String filePath;   //活体数据阿里云路径
    private String score;   //防伪分数
    private String hackScore;   //防伪得分
    private String verifyScore;   //置信度
    private String imageId;   //带水印照片的id
    private String channel;   //认证渠道
    private String realName;   //真实姓名
    private String idCardNo;   //身份证号
    private String uploadReqId;   //上传请求id
    private String uploadSid;   //上传流水号
    private Date uploadBeginTime;   //上传开始时间
    private Date uploadEndTime;   //上传成功时间
    private Long uploadDuration;   //上传耗时
    private String hackReqId;   //防伪请求id
    private String hackSid;   //防伪流水号
    private Date hackBeginTime;   //防伪开始时间
    private Date hackEndTime;   //防伪认证成功时间
    private Long hackDuration;   //防伪认证耗时
    private String verifyReqId;   //认证请求id
    private String verifySid;   //认证流水号
    private Date verifyBeginTime;   //水印认证开始时间
    private Date verifyEndTime;   //水印认证成功时间
    private Long verifyDuration;   //水印认证耗时
    private String errorMsg;   //异常信息

    private Date createTime;   //创建时间
    private String createUser;   //创建人
    private Date updateTime;   //修改时间
    private String updateUser;   //修改人

    private String processDesc;//进度描述
    private List<String> picList;//认证图片地址
}
