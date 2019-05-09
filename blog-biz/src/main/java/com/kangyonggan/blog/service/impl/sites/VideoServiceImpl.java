package com.kangyonggan.blog.service.impl.sites;

import com.kangyonggan.blog.annotation.MethodLog;
import com.kangyonggan.blog.constants.YesNo;
import com.kangyonggan.blog.model.Video;
import com.kangyonggan.blog.service.BaseService;
import com.kangyonggan.blog.service.sites.VideoService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author kangyonggan
 * @since 1/11/19
 */
@Service
public class VideoServiceImpl extends BaseService<Video> implements VideoService {

    @Override
    public List<Video> findAllVideo() {
        Example example = new Example(Video.class);
        example.createCriteria().andEqualTo("isDeleted", YesNo.NO.getCode());

        example.setOrderByClause("video_id DESC");

        return myMapper.selectByExample(example);
    }

    @Override
    @MethodLog
    public Video findVideoById(Long id) {
        return myMapper.selectByPrimaryKey(id);
    }

    @Override
    @MethodLog
    public void updateVideo(Video video) {
        myMapper.updateByPrimaryKeySelective(video);
    }
}
