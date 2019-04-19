package com.kangyonggan.blog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 清除缓存，支持模糊匹配（*）
 *
 * @author kangyonggan
 * @since 2018/6/3 0003
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheDel {

    /**
     * 缓存的key。
     *
     * @return
     */
    String[] value();

}
