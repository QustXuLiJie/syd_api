package com.mobanker.shanyidai.api.dto.system;

import com.mobanker.shanyidai.api.common.dto.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 中国节假日信息
 * @author hantongyang
 * @date 2016/12/22
 */
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true)
public class HolidayBean extends Entity {
	private String year;//年份
	private String holidayDate;//日期
	private String holidayName;//节假日名称
	private int count;
}
