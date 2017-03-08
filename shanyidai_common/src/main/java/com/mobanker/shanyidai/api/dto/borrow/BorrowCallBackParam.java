package com.mobanker.shanyidai.api.dto.borrow;

import com.mobanker.shanyidai.api.common.dto.Entity;
import com.mobanker.shanyidai.api.common.packet.SydRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hantongyang
 * @description
 * @time 2017/1/19 10:57
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class BorrowCallBackParam extends SydRequest {
    private Long userId; //用户ID
    private String orderNid; //借款单号
    private String status; //借款单状态

}
