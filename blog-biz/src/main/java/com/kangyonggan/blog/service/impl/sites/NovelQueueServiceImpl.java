package com.kangyonggan.blog.service.impl.sites;

import com.github.pagehelper.PageHelper;
import com.kangyonggan.blog.annotation.MethodLog;
import com.kangyonggan.blog.constants.NovelQueueStatus;
import com.kangyonggan.blog.model.NovelQueue;
import com.kangyonggan.blog.service.BaseService;
import com.kangyonggan.blog.service.sites.NovelQueueService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;

/**
 * @author kangyonggan
 * @since 2019-05-08
 */
@Service
public class NovelQueueServiceImpl extends BaseService<NovelQueue> implements NovelQueueService {
    @Override
    public boolean exists(Long novelId) {
        Example example = new Example(NovelQueue.class);
        example.createCriteria().andEqualTo("novelId", novelId)
                .andIn("status", Arrays.asList(NovelQueueStatus.I.getCode(), NovelQueueStatus.N.getCode()));

        return myMapper.selectCountByExample(example) > 0;
    }

    @Override
    @MethodLog
    public void saveNovelQueue(long novelId) {
        NovelQueue novelQueue = new NovelQueue();
        novelQueue.setNovelId(novelId);

        myMapper.insertSelective(novelQueue);
    }

    @Override
    @MethodLog
    public NovelQueue findNextNovel() {
        Example example = new Example(NovelQueue.class);
        example.createCriteria().andIn("status", Arrays.asList(NovelQueueStatus.I.getCode(), NovelQueueStatus.N.getCode()));

        example.selectProperties("novelId", "status");
        example.setOrderByClause("status asc, queue_id asc");

        PageHelper.startPage(1, 1);
        NovelQueue novelQueue = myMapper.selectOneByExample(example);

        if (novelQueue != null && novelQueue.getStatus().equals(NovelQueueStatus.N.getCode())) {
            // 改为更新中
            NovelQueue queue = new NovelQueue();
            queue.setStatus(NovelQueueStatus.I.getCode());
            example = new Example(NovelQueue.class);
            example.createCriteria().andEqualTo("novelId", novelQueue.getNovelId()).andEqualTo("status", NovelQueueStatus.N.getCode());
            myMapper.updateByExampleSelective(queue, example);
        }

        return novelQueue;
    }

    @Override
    @MethodLog
    public void finished(Long novelId) {
        Example example = new Example(NovelQueue.class);
        example.createCriteria().andEqualTo("novelId", novelId).andEqualTo("status", NovelQueueStatus.I.getCode());

        NovelQueue queue = new NovelQueue();
        queue.setStatus(NovelQueueStatus.Y.getCode());
        myMapper.updateByExampleSelective(queue, example);
    }

}
