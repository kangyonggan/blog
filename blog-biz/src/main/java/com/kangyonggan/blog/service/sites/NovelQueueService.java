package com.kangyonggan.blog.service.sites;

import com.kangyonggan.blog.model.NovelQueue;

/**
 * @author kangyonggan
 * @since 2019/1/5 0005
 */
public interface NovelQueueService {

    /**
     * 判断小说是否在队列中
     *
     * @param novelId
     * @return
     */
    boolean exists(Long novelId);

    /**
     * 把小说放入队列
     *
     * @param novelId
     */
    void saveNovelQueue(long novelId);

    /**
     * 查找队列中的下一个小说
     *
     * @return
     */
    NovelQueue findNextNovel();

    /**
     * 更新完成
     *
     * @param novelId
     */
    void finished(Long novelId);

    /**
     * 查找小说最后更新队列
     *
     * @param novelId
     * @return
     */
    NovelQueue findNovelQueue(Long novelId);
}
