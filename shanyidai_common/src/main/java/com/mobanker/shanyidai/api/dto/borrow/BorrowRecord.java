package com.mobanker.shanyidai.api.dto.borrow;

import com.alibaba.fastjson.annotation.JSONField;
import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * @desc:
 * @author: Richard Core
 * @create time: 2017/1/22 19:55
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class BorrowRecord extends Entity {

    private String borrowNid; // 借款单号
    private String status; // 状态
    private String statusTitle;//状态的标题
    private Integer periodDays; // 每期天数
    private Float amount; // 借款金额
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date addtime; // 申请时间
}
