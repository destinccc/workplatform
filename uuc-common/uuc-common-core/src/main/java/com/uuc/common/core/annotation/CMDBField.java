package com.uuc.common.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CMDBField {
    /**
     * 对应元模型中的属性名称.
     */
    public String name();

    /**
     * 是否modelCode字段
     */
    public int isModelCode() default 0;

    /**
     * 是否需要转化时间格式
     */
    public int isDateFormat() default 0;

}
