package com.kangyonggan.blog.service.impl.sites;

import com.kangyonggan.blog.constants.YesNo;
import com.kangyonggan.blog.model.Album;
import com.kangyonggan.blog.service.BaseService;
import com.kangyonggan.blog.service.sites.AlbumService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author kangyonggan
 * @since 1/10/19
 */
@Service
public class AlbumServiceImpl extends BaseService<Album> implements AlbumService {

    @Override
    public List<Album> findAllAlbums() {
        Example example = new Example(Album.class);
        example.createCriteria().andEqualTo("isDeleted", YesNo.NO.getCode());

        example.setOrderByClause("sort asc");
        return myMapper.selectByExample(example);
    }

    @Override
    public Album findAlbumByAlbumId(Long albumId) {
        return myMapper.selectByPrimaryKey(albumId);
    }
}
