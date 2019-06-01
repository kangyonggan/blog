package com.kangyonggan.blog.service.impl.sites;

import com.github.pagehelper.PageHelper;
import com.kangyonggan.blog.annotation.MethodLog;
import com.kangyonggan.blog.constants.YesNo;
import com.kangyonggan.blog.dto.AlbumRequest;
import com.kangyonggan.blog.model.Album;
import com.kangyonggan.blog.service.BaseService;
import com.kangyonggan.blog.service.sites.AlbumPhotoService;
import com.kangyonggan.blog.service.sites.AlbumService;
import com.kangyonggan.blog.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author kangyonggan
 * @since 1/10/19
 */
@Service
public class AlbumServiceImpl extends BaseService<Album> implements AlbumService {

    @Autowired
    private AlbumPhotoService albumPhotoService;

    @Override
    public List<Album> findAllAlbums() {
        Example example = new Example(Album.class);
        example.createCriteria().andEqualTo("isDeleted", YesNo.NO.getCode());

        example.setOrderByClause("sort asc");
        return myMapper.selectByExample(example);
    }

    @Override
    @MethodLog
    public Album findAlbumByAlbumId(Long albumId) {
        return myMapper.selectByPrimaryKey(albumId);
    }

    @Override
    @MethodLog
    public List<Album> searchAlbums(AlbumRequest albumRequest) {
        Example example = new Example(Album.class);
        Example.Criteria criteria = example.createCriteria();

        String albumName = albumRequest.getAlbumName();
        if (StringUtils.isNotEmpty(albumName)) {
            criteria.andLike("albumName", StringUtil.toLike(albumName));
        }

        String[] createdTime = albumRequest.getCreatedTime();
        if (createdTime != null && StringUtils.isNotEmpty(createdTime[0])) {
            criteria.andGreaterThanOrEqualTo("createdTime", createdTime[0]);
            criteria.andLessThanOrEqualTo("createdTime", createdTime[1]);
        }

        if (StringUtils.isNotEmpty(albumRequest.getSort())) {
            if (albumRequest.getOrder() == 0) {
                example.orderBy(albumRequest.getSort()).asc();
            } else {
                example.orderBy(albumRequest.getSort()).desc();
            }
        } else {
            example.orderBy("albumId").desc();
        }

        PageHelper.startPage(albumRequest.getPageNum(), albumRequest.getPageSize());
        return myMapper.selectByExample(example);
    }

    @Override
    @MethodLog
    public void saveAlbum(Album album) {
        myMapper.insertSelective(album);
    }

    @Override
    @MethodLog
    public void updateAlbum(Album album) {
        myMapper.updateByPrimaryKeySelective(album);
    }

    @Override
    @MethodLog
    @Transactional(rollbackFor = Exception.class)
    public void updateAlbumWithPhotos(Album album, String albumPhotos) {
        int size = 0;
        // 删除所有相册
        albumPhotoService.deletePhotosByAlbumId(album.getAlbumId());

        if (StringUtils.isNotEmpty(albumPhotos)) {
            String[] photos = albumPhotos.split(",");
            size = photos.length;

            // 保存新的相册
            albumPhotoService.saveAlbumPhotos(album.getAlbumId(), photos);
        }

        album.setSize(size);
        updateAlbum(album);
    }
}
