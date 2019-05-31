package com.kangyonggan.blog.service.sites;

import com.kangyonggan.blog.dto.NovelDto;
import com.kangyonggan.blog.dto.NovelRequest;
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
     * @param key
     * @return
     */
    List<NovelDto> searchNovels(String key);

    /**
     * 搜索小说
     *
     * @param key
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<NovelDto> searchNovelsByPage(String key, Integer pageNum, Integer pageSize);

    /**
     * 查找最新小说
     *
     * @return
     */
    List<Novel> findNewNovels();

    /**
     * 查找小说
     *
     * @param novelId
     * @return
     */
    Novel findNovelById(Long novelId);

    /**
     * 如果没有线程在消费队列，则启动一个
     *
     * @param isCheck
     * @return
     */
    Long popOrCheck(boolean isCheck);

    /**
     * 搜索小说
     *
     * @param novelRequest
     * @return
     */
    List<Novel> searchNovels(NovelRequest novelRequest);

    /**
     * 保存小说
     *
     * @param novel
     */
    void saveNovel(Novel novel);

    /**
     * 更新小说
     *
     * @param novel
     */
    void updateNovel(Novel novel);

}
