/**
 * 
 */
package com.mobanker.shanyidai.api.business.upload;

import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.logger.LogManager;
import com.mobanker.shanyidai.api.common.logger.Logger;
import com.mobanker.shanyidai.api.dto.upload.UploadResultDto;
import com.mobanker.shanyidai.dubbo.dto.upload.FileActionDto;
import com.mobanker.shanyidai.esb.common.packet.ResponseEntity;
import com.mobanker.shanyidai.dubbo.service.upload.UploadFileDubboService;
import com.mobanker.shanyidai.esb.common.exception.EsbException;
import com.mobanker.shanyidai.esb.common.packet.ResponseBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 用户文件及图片上传业务
 * 
 * @author chenjianping
 * @data 2016年12月11日
 */
@Service
public class UploadBusinessImpl implements UploadBusiness {

    private Logger logger = LogManager.getSlf4jLogger(getClass());

    @Resource
    UploadFileDubboService uploadFileDubboService;

    @Override
    public UploadResultDto uploadFile(FileActionDto fileDto) {

        ResponseEntity result;
        try {
            result = uploadFileDubboService.uploadFile(fileDto);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        }
        if (!ResponseBuilder.isSuccess(result)) {
            logger.error("上传文件失败", result);
            throw new SydException(result.getError(), result.getMsg());
        }

        Map<String,String> map = (Map<String, String>) result.getData();
        String url = map.get("fileDomain")+map.get("fileName");
        UploadResultDto dto = new UploadResultDto();
        dto.setUrl(url);
        dto.setId(map.get("id"));
        return dto;
    }

    @Override
    public UploadResultDto queryFileByParams(FileActionDto dto) {

        ResponseEntity result;
        try {
            result = uploadFileDubboService.queryFileByParams(dto);
        } catch (EsbException e) {
            throw new SydException(e.errCode, e.message);
        }
        if (!ResponseBuilder.isSuccess(result)) {
            logger.error("查询文件失败", result);
            throw new SydException(result.getError(), result.getMsg());
        }

        Map<String,String> map = (Map<String, String>) result.getData();
        UploadResultDto resultDto = new UploadResultDto();
        resultDto.setUrl(map.get("url"));
        return resultDto;

    }


}
