package com.kangyonggan.blog.service.sites;

import com.kangyonggan.blog.model.Album;

import java.util.List;

/**
 * @author kangyonggan
 * @since 1/10/19
 */
public interface AlbumService {

    /**
     * 查找所有相册
     *
     * @return
     */
    List<Album> findAllAlbums();

    /**
     * 查找相册
     *
     * @param albumId
     * @return
     */
    Album findAlbumByAlbumId(Long albumId);
}
