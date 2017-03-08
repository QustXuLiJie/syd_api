package com.mobanker.shanyidai.api.dto.basedata;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @desc: 获取职业类型，展示使用
 * @author: Richard Core
 * @create time: 2017/3/3 16:22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class JobType implements Serializable {
    private Integer id;//job主键
    private Integer pid;//上级id
    private String name;//职业说明
    private List<JobType> child;//子级
}
