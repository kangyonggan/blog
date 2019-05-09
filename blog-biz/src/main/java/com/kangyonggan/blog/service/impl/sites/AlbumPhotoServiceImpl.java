package com.kangyonggan.blog.service.impl.sites;

import com.kangyonggan.blog.constants.YesNo;
import com.kangyonggan.blog.model.AlbumPhoto;
import com.kangyonggan.blog.service.BaseService;
import com.kangyonggan.blog.service.sites.AlbumPhotoService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author kangyonggan
 * @since 1/10/19
 */
@Service
public class AlbumPhotoServiceImpl extends BaseService<AlbumPhoto> implements AlbumPhotoService {

    @Override
    public List<AlbumPhoto> findAlbumPhotos(Long albumId) {
        Example example = new Example(AlbumPhoto.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("albumId", albumId);
        criteria.andEqualTo("isDeleted", YesNo.NO.getCode());

        example.setOrderByClause("sort ASC, created_time DESC");
        return myMapper.selectByExample(example);
    }
}
