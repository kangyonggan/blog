package com.kangyonggan.blog.mapper;

import com.kangyonggan.blog.MyMapper;
import com.kangyonggan.blog.dto.NovelDto;
import com.kangyonggan.blog.model.Novel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author kangyonggan
 * @since 8/8/18
 */
public interface NovelMapper extends MyMapper<Novel> {

    /**
     * 搜索小说
     *
     * @param key
     * @return
     */
    List<NovelDto> searchNovels(@Param("key") String key);
}