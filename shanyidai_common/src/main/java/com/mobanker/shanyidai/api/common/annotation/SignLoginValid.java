package com.mobanker.shanyidai.api.common.annotation;


import com.mobanker.shanyidai.api.common.enums.SignLoginEnum;

import java.lang.annotation.*;

/**
 * 定义加密登录校验
 * 
 * @author: liuyafei
 * @date 创建时间：2016年8月24日
 * @version 1.0
 * @parameter
 * @return
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SignLoginValid {
	SignLoginEnum value() default SignLoginEnum.ALL;
}
