package com.computer.ecommerce.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /**
     * 操作模块
     */
    String module() default "";

    /**
     * 操作类型
     */
    String type() default "";

    /**
     * 操作描述
     */
    String description() default "";

    /**
     * 是否保存请求参数
     */
    boolean saveRequest() default true;

    /**
     * 是否保存响应结果
     */
    boolean saveResponse() default false;

    /**
     * 是否忽略（用于排除某些方法）
     */
    boolean ignore() default false;
}