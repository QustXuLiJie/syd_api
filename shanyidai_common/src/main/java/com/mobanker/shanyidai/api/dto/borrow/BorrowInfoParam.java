package com.mobanker.shanyidai.api.dto.borrow;

import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @desc:
 * @author: Richard Core
 * @create time: 2017/1/11 15:28
 */

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class BorrowInfoParam extends SydRequest {
//    private Long userId;//用户ID 必传 使用SydRequest中的userId
    private String borrowNid;//根据借款单号查询
    private Integer queryType;//1手机贷，2闪电贷，3单期(1+2)，4分期，不传全部  新增：5应花分期
    private Integer systemType;//用作标签显示1信审标签，2催收标签，3客服标签，不传默认公共标签(不需要的可以不传)
//    private String product;//产品：shoujidai/uzone；不传默认shoujidai 新增：应花分期：yhfq


}
