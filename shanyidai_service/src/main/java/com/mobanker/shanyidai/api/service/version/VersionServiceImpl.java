package com.mobanker.shanyidai.api.service.version;

import com.mobanker.shanyidai.api.business.system.VersionBusiness;
import com.mobanker.shanyidai.api.common.constant.ReturnCode;
import com.mobanker.shanyidai.api.common.exception.SydException;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import com.mobanker.shanyidai.api.common.tool.BeanHelper;
import com.mobanker.shanyidai.api.dto.version.VersionReq;
import com.mobanker.shanyidai.api.dto.version.VersionRsp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description: app版本更新
 * @author: xulijie
 * @create time: 19:31 2017/2/23
 */
@Service
public class VersionServiceImpl implements VersionService {
    public static final String updateTure = "1";
    public static final String updateFalse = "0";
    public static final String updateForce = "FORCE";

    @Resource
    private VersionBusiness versionBusiness;

    /**
     * @param request
     * @return com.mobanker.shanyidai.api.dto.version.VersionRsp
     * @author xulijie
     * @method getVersion
     * @description app版本更新相关
     * @time 20:36 2017/2/23
     */
    @Override
    public VersionRsp getVersion(SydRequest request) throws SydException {
        VersionReq versionParam = BeanHelper.parseJson(request.getContent(), VersionReq.class);
        if (versionParam == null) {
            throw new SydException(ReturnCode.PARAM_VALID.getCode(), "版本更新参数为空");
        }
        //若type为null或者为android、ios之外的字符串，则默认为android类型
        if (versionParam.getType() != null && !(versionParam.getType().equalsIgnoreCase(VersionReq.TYPE_ANDROID)
                || versionParam.getType().equalsIgnoreCase(VersionReq.TYPE_IOS))) {
            versionParam.setType(VersionReq.TYPE_ANDROID);
        }
        VersionRsp versionRsp = versionBusiness.getVersion(versionParam);
        //版本比较
        //1.比较current和min的大小 自定义比较方法
        //2.如果min大需强制更新结束方法
        //3.如果current大不需要强制更新 但不确定是不是有更新
        //4.比较current和appVersion大小 自定义比较方法
        //5.如果current小于appVersion  返回可选更新
        //6.如果current等于appVersion 返回不需要更新
        //7.如果current大于appVersion 需确认如何操作
        if (versionRsp.getMinVersion().compareTo(versionParam.getCurrentVersion()) > 0) {
            //需要强制更新
            versionRsp.setUpdateForce(updateForce);
            versionRsp.setUpdate(updateTure);
        } else if (versionParam.getCurrentVersion().compareTo(versionRsp.getAppVersion()) < 0) {
            //需要更新
            versionRsp.setUpdate(updateTure);
        } else if (versionParam.getCurrentVersion().compareTo(versionRsp.getAppVersion()) == 0) {
            //不需要更新
            versionRsp.setUpdate(updateFalse);
        }
        return versionRsp;
    }
}
