package com.kangyonggan.blog.service.sites;

import com.kangyonggan.blog.model.Video;

import java.util.List;

/**
 * @author kangyonggan
 * @since 1/11/19
 */
public interface VideoService {
    /**
     * 查找全部视频
     *
     * @return
     */
    List<Video> findAllVideo();

    /**
     * 查找视频
     *
     * @param id
     * @return
     */
    Video findVideoById(Long id);

    /**
     * 更新视频
     *
     * @param video
     */
    void updateVideo(Video video);
}
