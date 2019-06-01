package com.kangyonggan.blog.service.impl.sites;

import com.kangyonggan.blog.annotation.MethodLog;
import com.kangyonggan.blog.constants.YesNo;
import com.kangyonggan.blog.mapper.AlbumPhotoMapper;
import com.kangyonggan.blog.model.AlbumPhoto;
import com.kangyonggan.blog.service.BaseService;
import com.kangyonggan.blog.service.sites.AlbumPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author kangyonggan
 * @since 1/10/19
 */
@Service
public class AlbumPhotoServiceImpl extends BaseService<AlbumPhoto> implements AlbumPhotoService {

    @Autowired
    private AlbumPhotoMapper albumPhotoMapper;

    @Override
    public List<AlbumPhoto> findAlbumPhotos(Long albumId) {
        Example example = new Example(AlbumPhoto.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("albumId", albumId);
        criteria.andEqualTo("isDeleted", YesNo.NO.getCode());

        example.setOrderByClause("sort ASC, created_time DESC");
        return myMapper.selectByExample(example);
    }

    @Override
    @MethodLog
    public void deletePhotosByAlbumId(Long albumId) {
        AlbumPhoto albumPhoto = new AlbumPhoto();
        albumPhoto.setAlbumId(albumId);
        myMapper.delete(albumPhoto);
    }

    @Override
    @MethodLog
    public void saveAlbumPhotos(Long albumId, String[] photos) {
        albumPhotoMapper.insertAlbumPhotos(albumId, getThumbs(photos));
    }

    /**
     * 获取缩略图
     *
     * @param photos
     * @return
     */
    private List<Properties> getThumbs(String[] photos) {
        List<Properties> list = new ArrayList<>();
        for (int i = 0; i < photos.length; i++) {
            String photo = photos[i];
            Properties prop = new Properties();
            prop.setProperty("origin", photo);
            prop.setProperty("thumb", "photo/thumb" + photo.substring(5, photo.indexOf(".")) + "_THUMB.png");
            list.add(prop);
        }

        return list;
    }
}
