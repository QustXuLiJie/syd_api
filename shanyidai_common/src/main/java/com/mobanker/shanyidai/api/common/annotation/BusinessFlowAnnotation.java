package com.mobanker.shanyidai.api.common.annotation;

import java.lang.annotation.*;

/**
 * @author hantongyang
 * @description 流水日志注解，根据不同的枚举实体，保存不同的流水表
 * @time 2017/2/27 14:58
 */
@Target({ElementType.METHOD, ElementType.TYPE})//修饰方法的
@Retention(RetentionPolicy.RUNTIME)//运行期有效
public @interface BusinessFlowAnnotation {
}
