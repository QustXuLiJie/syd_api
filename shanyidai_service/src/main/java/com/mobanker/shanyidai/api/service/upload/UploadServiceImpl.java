package com.mobanker.shanyidai.api.service.upload;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mobanker.shanyidai.api.business.common.CommonBusiness;
import com.mobanker.shanyidai.api.business.upload.UploadBusiness;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.dto.upload.UploadResultDto;
import com.mobanker.shanyidai.dubbo.dto.upload.FileActionDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * Created by liuhanqing on 2017/1/9.
 */
@Service
public class UploadServiceImpl implements UploadService {

    @Resource
    UploadBusiness uploadBusiness;

    @Resource
    CommonBusiness commonBusiness;

    /**
     * 文件上传
     *
     * @param request
     * @return
     */
    @Override
    public UploadResultDto uploadFile(SydRequest request, HttpServletRequest httpServletRequest) {
        // 转型为MultipartHttpRequest：
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) httpServletRequest;
        // 从其中取出一个文件
        MultipartFile file = multipartRequest.getFile("file");
        return uploadByMultipartFile(request, file);
    }

    /**
     * @param request
     * @param file
     * @return com.mobanker.shanyidai.api.dto.upload.UploadResultDto
     * @description 上传文件
     * @author Richard Core
     * @time 2017/2/24 15:48
     * @method uploadByMultipartFile
     */
    @Override
    public UploadResultDto uploadByMultipartFile(SydRequest request, MultipartFile file) {
        JSONObject jsonObject = JSON.parseObject(request.getContent());
        String fileType = jsonObject.getString("fileType");
        long userId = request.getUserId();
        return uploadSingleFile(fileType, userId,request.getIp(), file);

    }

    /**
     * @param fileType
     * @param userId
     * @param ip
     * @param file
     * @return com.mobanker.shanyidai.api.dto.upload.UploadResultDto
     * @description 上传文件
     * @author Richard Core
     * @time 2017/2/24 17:20
     * @method uploadSingleFile
     */
    private UploadResultDto uploadSingleFile(String fileType, long userId, String ip, MultipartFile file) {
        //再调用dubbo服务之前必须将MultipartFile 文件转换为file文件，不然无法传参
        if (file == null || file.isEmpty()) {
            throw new SydException(ReturnCode.UPLOAD_FILE_NOT_NULL);
        }
        //将文件缓存到本地
        File localFile = cacheFile2Local(file);
        //设置上传参数
        FileActionDto uploadFileDto = new FileActionDto();
        uploadFileDto.setFile(file);
        uploadFileDto.setFileType(fileType);
        uploadFileDto.setUserId(userId);
        uploadFileDto.setRemoteIp(ip);
        uploadFileDto.setTempFilePath(localFile.getAbsolutePath());

        UploadResultDto dto = null;
        try {
            dto = uploadBusiness.uploadFile(uploadFileDto);
        } catch (Exception e) {
            throw e;
        }finally {
            if (localFile.exists()) {
                localFile.delete();
            }
        }

        return dto;
    }

    /**
     * @param file
     * @return java.io.File
     * @description 将文件缓存到本地
     * @author Richard Core
     * @time 2017/2/24 17:22
     * @method cacheFile2Local
     */
    @Override
    public File cacheFile2Local(MultipartFile file) {
        String localFilePath = getFileLocalPath(file);
        File localFile = new File(localFilePath);
        try {
            if (!localFile.getParentFile().exists()) {
                localFile.getParentFile().mkdirs();
            }
            file.transferTo(localFile);
        } catch (Exception e) {
            if (localFile.exists()) {
                localFile.delete();
            }
            throw new SydException(ReturnCode.UPLOAD_FILE_SAVE_ERROR.getCode(), ReturnCode.UPLOAD_FILE_SAVE_ERROR.getDesc(), e);
        }
        return localFile;
    }

    /**
     * @param file
     * @return java.lang.String
     * @description 获取本地缓存文件路径
     * @author Richard Core
     * @time 2017/2/24 18:05
     * @method getFileLocalPath
     */
    private String getFileLocalPath(MultipartFile file) {
        String localFileDir = getLocalCacheFileDir();
        String fileName = file.getOriginalFilename();
        return localFileDir + "/" + fileName;
    }

    /**
     * @return java.lang.String
     * @description 获取本地缓存文件基本路径
     * @author Richard Core
     * @time 2017/2/24 18:05
     * @method getFileLocalPath
     */
    private String getLocalCacheFileDir() {
        //        String localFileDir = commonBusiness.getCacheSysConfig(ZkConfigConstant.SYD_TEMP_FILE_PATH.getZkValue());
////        //TODO 本地测试使用
        return "E:/var/statics";
    }


    @Override
    public UploadResultDto queryFileByParams(SydRequest request) {

        JSONObject jsonObject = JSON.parseObject(request.getContent());
        String type = jsonObject.getString("fileType");
        long userId = jsonObject.getLong("userId");

        FileActionDto fileActionDto = new FileActionDto();
        fileActionDto.setUserId(userId);
        fileActionDto.setFileType(type);

        UploadResultDto resultDto = uploadBusiness.queryFileByParams(fileActionDto);

        return resultDto;
    }
}
