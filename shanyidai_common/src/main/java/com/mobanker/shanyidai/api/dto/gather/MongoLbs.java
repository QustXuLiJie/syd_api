package com.mobanker.shanyidai.api.dto.gather;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * @version 1.0
 * @author: liuyafei
 * @date 创建时间：2016年9月12日
 * @parameter
 * @return
 */
@Data
public class MongoLbs extends SydRequest implements Serializable {

    private String _id;
    private Long user_id;
    private Long addtime;//添加时间
    private String type;//操作类型
    private String lat;//经度
    private String lon;//纬度
    private String map;//地图
    private String device_id = "";
    private String device_id_type;//类型Android ios
    private String mac = "";
    private String idfa = "";
    private String imei = "";
    private String imsi = "";

    public void splitMobileDeviceId(String mobileDeviceId, String appType) {
        if (StringUtils.isNotBlank(mobileDeviceId)) {
            String[] msg = mobileDeviceId.split(",");
            if ("android".equals(appType)) {
                this.mac = msg[0];
                if (msg.length > 1) {
                    this.imei = msg[1];
                }
            } else if ("ios".equals(appType)) {
                this.idfa = msg[0];
            }
        }
    }
}
