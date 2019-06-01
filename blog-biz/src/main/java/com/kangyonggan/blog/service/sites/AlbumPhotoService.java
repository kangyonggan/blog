package com.kangyonggan.blog.service.sites;

import com.kangyonggan.blog.model.AlbumPhoto;

import java.util.List;

/**
 * @author kangyonggan
 * @since 1/10/19
 */
public interface AlbumPhotoService {

    /**
     * 查询全部相片
     *
     * @param albumId
     * @return
     */
    List<AlbumPhoto> findAlbumPhotos(Long albumId);

    /**
     * 删除相册
     *
     * @param albumId
     */
    void deletePhotosByAlbumId(Long albumId);

    /**
     * 保存相册
     *
     * @param albumId
     * @param photos
     */
    void saveAlbumPhotos(Long albumId, String[] photos);
}
