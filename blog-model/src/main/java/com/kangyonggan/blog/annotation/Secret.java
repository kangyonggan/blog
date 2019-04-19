package com.kangyonggan.blog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author kangyonggan
 * @since 2018/6/3 0003
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Secret {

    /**
     * 是否启用加密
     *
     * @return
     */
    boolean enable() default true;

}
