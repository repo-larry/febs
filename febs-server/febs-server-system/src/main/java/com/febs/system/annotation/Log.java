package com.febs.system.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description:
 * @date: 2022/9/27
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

    /**
     * 日志描述
     */
    @AliasFor("desc")
    String value() default "";

    /**
     * 日志描述
     */
    @AliasFor("value")
    String desc() default "";

    /**
     * 是否忽略
     */
    boolean ignore() default false;
}
