package com.kangyonggan.blog.service;

import com.kangyonggan.blog.MyMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author kangyonggan
 * @since 16/5/23
 */
public abstract class BaseService<T> {

    @Autowired
    protected MyMapper<T> myMapper;

    /**
     * 判断是否存在
     *
     * @param entity
     * @return
     */
    public boolean exists(T entity) {
        return myMapper.selectCount(entity) > 0;
    }

}
