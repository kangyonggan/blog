package com.kangyonggan.blog.mapper;

import com.kangyonggan.blog.MyMapper;
import com.kangyonggan.blog.model.AlbumPhoto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Properties;

/**
 * @author kangyonggan
 * @since 8/8/18
 */
@Mapper
public interface AlbumPhotoMapper extends MyMapper<AlbumPhoto> {
    /**
     * 保存相册
     *
     * @param albumId
     * @param photos
     */
    void insertAlbumPhotos(@Param("albumId") Long albumId, @Param("photos") List<Properties> photos);
}