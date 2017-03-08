package com.mobanker.shanyidai.api.dto.repay;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @desc:
 * @author: Richard Core
 * @create time: 2017/1/23 10:50
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class PayResult implements Serializable {
    private String statusCode;//-1无还款记录 0处理中 1 成功 2失败
    private String desc;//描述
}
