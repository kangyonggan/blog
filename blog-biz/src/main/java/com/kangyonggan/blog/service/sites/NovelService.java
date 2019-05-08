package com.kangyonggan.blog.service.sites;

import com.kangyonggan.blog.model.Novel;

import java.util.List;

/**
 * @author kangyonggan
 * @since 1/4/19
 */
public interface NovelService {

    /**
     * 批量拉取小说
     *
     * @param novelIds
     */
    void pullNovels(String novelIds);

    /**
     * 搜索小说
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<Novel> searchNovels(int pageNum, int pageSize);
}
