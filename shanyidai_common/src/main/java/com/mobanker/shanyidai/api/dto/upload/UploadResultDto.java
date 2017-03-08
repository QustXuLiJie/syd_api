package com.mobanker.shanyidai.api.dto.upload;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author liuhanqing
 * @description 文件上传结果的dto
 * @time 2017/1/19.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class UploadResultDto {

    /**
     * 上传文件的url
     */
    private String url;

    /**
     * 文件的id
     */
    private String id;
}
