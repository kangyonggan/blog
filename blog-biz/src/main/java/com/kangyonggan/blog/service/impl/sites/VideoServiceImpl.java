package com.kangyonggan.blog.service.impl.sites;

import com.github.pagehelper.PageHelper;
import com.kangyonggan.blog.annotation.MethodLog;
import com.kangyonggan.blog.constants.YesNo;
import com.kangyonggan.blog.dto.VideoRequest;
import com.kangyonggan.blog.model.Video;
import com.kangyonggan.blog.service.BaseService;
import com.kangyonggan.blog.service.sites.VideoService;
import com.kangyonggan.blog.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
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

    @Override
    @MethodLog
    public List<Video> searchVideos(VideoRequest videoRequest) {
        Example example = new Example(Video.class);
        Example.Criteria criteria = example.createCriteria();

        String title = videoRequest.getTitle();
        if (StringUtils.isNotEmpty(title)) {
            criteria.andLike("title", StringUtil.toLike(title));
        }

        String[] createdTime = videoRequest.getCreatedTime();
        if (createdTime != null && StringUtils.isNotEmpty(createdTime[0])) {
            criteria.andGreaterThanOrEqualTo("createdTime", createdTime[0]);
            criteria.andLessThanOrEqualTo("createdTime", createdTime[1]);
        }

        if (StringUtils.isNotEmpty(videoRequest.getSort())) {
            if (videoRequest.getOrder() == 0) {
                example.orderBy(videoRequest.getSort()).asc();
            } else {
                example.orderBy(videoRequest.getSort()).desc();
            }
        } else {
            example.orderBy("videoId").desc();
        }

        PageHelper.startPage(videoRequest.getPageNum(), videoRequest.getPageSize());
        return myMapper.selectByExample(example);
    }

    @Override
    @MethodLog
    public void saveVideo(Video video) {
        myMapper.insertSelective(video);
    }
}
