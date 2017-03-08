package com.mobanker.shanyidai.api.service.upload;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.dto.upload.UploadResultDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * Created by liuhanqing on 2017/1/9.
 * 上传文件服务
 */
public interface UploadService  {

    /**
     * 上传头像
     * @param request
     * @param httpServletRequest
     */
    UploadResultDto uploadFile(SydRequest request,HttpServletRequest httpServletRequest);

    /**
     * 查询文件
     * @param request
     * @return
     */
    UploadResultDto queryFileByParams(SydRequest request);

    /**
     * @param request
     * @param file
     * @return com.mobanker.shanyidai.api.dto.upload.UploadResultDto
     * @description 上传文件
     * @author Richard Core
     * @time 2017/2/24 15:48
     * @method uploadByMultipartFile
     */
    public UploadResultDto uploadByMultipartFile(SydRequest request, MultipartFile file);

    /**
     * @param file
     * @return java.io.File
     * @description 将文件缓存到本地
     * @author Richard Core
     * @time 2017/2/24 17:22
     * @method cacheFile2Local
     */
    public File cacheFile2Local(MultipartFile file);
}
