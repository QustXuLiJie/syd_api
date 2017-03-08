package com.mobanker.shanyidai.api.dto.message;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class SysMessageParam extends SydRequest {
    private Long userId;//userId
//    public ProductType product;//产品线 取shayidai
    public Integer readStatus;//读取状态  1 已读 0 未读
    private Date startDate;//查询开始时间
    private Date endDate;//查询结束时间
    private List<String> ids;//消息id
//    private String remoteIp; //取Entity中的ip
}
