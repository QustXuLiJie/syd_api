package com.mobanker.shanyidai.api.dto.auth;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @desc:活体识别参数
 * @author: Richard Core
 * @create time: 2017/2/20 17:05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class FaceAuthUploadParam extends SydRequest {
    private String filePath;//上传到云端的路径
    private Date methodBeginDate;//方法开始调用时间 记录日志使用
    private List<String> picPath;//上传图片路径
}
