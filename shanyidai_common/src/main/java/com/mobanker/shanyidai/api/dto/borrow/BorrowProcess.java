package com.mobanker.shanyidai.api.dto.borrow;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @desc: 借款进度描述
 * @author: Richard Core
 * @create time: 2017/1/14 13:40
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class BorrowProcess implements Serializable {
    private String statusDesc;//状态描述
    @JSONField(format = "MM-dd HH:mm")
    private Date operateDate;//操作时间
    private String tips;//操作描述
}
