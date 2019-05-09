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
}
