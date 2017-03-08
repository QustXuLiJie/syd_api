package com.mobanker.shanyidai.api.dto.gather;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.management.remote.SubjectDelegationPermission;

/**
 * @author: xulijie
 * @description: 通话记录信息采集
 * @create time: 14:06 2017/2/10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class MongoCallRecords extends SydRequest {
    private String _id;
    private Long user_id;//code
    private Long addtime;//添加时间
    private String phone;//号码
    private int type;//呼叫类型：1–呼入 2–呼出 3–错过未接 4–语音邮件
    private String name;//姓名
    private String calltime;//通话时间
    private String duration;//通话时长
    private String device_id = "";
    private String device_id_type;//类型Android ios
    private String mac = "";
    private String idfa = "";
    private String imei = "";
    private String imsi = "";
}
