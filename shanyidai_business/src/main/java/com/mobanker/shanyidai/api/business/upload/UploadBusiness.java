/**
 * 
 */
package com.mobanker.shanyidai.api.business.upload;

import com.mobanker.shanyidai.api.dto.upload.UploadResultDto;
import com.mobanker.shanyidai.dubbo.dto.upload.FileActionDto;
/**
 * @author chenjianping
 * @data 2016年12月11日
 */
public interface UploadBusiness {

    /**
     * 上传文件
     */
    UploadResultDto uploadFile(FileActionDto fileDto);


    /**
     * 查询文件的dto
     * @param dto
     * @return
     */
    UploadResultDto queryFileByParams(FileActionDto dto);

}
