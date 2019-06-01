package com.kangyonggan.blog.service.sites;

import com.kangyonggan.blog.dto.AlbumRequest;
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

    /**
     * 搜索相册
     *
     * @param albumRequest
     * @return
     */
    List<Album> searchAlbums(AlbumRequest albumRequest);

    /**
     * 保存相册
     *
     * @param album
     */
    void saveAlbum(Album album);

    /**
     * 更新相册
     *
     * @param album
     */
    void updateAlbum(Album album);

    /**
     * 更新相册
     *
     * @param album
     * @param albumPhotos
     */
    void updateAlbumWithPhotos(Album album, String albumPhotos);
}
