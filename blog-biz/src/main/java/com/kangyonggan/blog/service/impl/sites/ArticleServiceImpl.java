package com.kangyonggan.blog.service.impl.sites;

import com.github.pagehelper.PageHelper;
import com.kangyonggan.blog.annotation.MethodLog;
import com.kangyonggan.blog.constants.YesNo;
import com.kangyonggan.blog.dto.ArticleRequest;
import com.kangyonggan.blog.model.Article;
import com.kangyonggan.blog.service.BaseService;
import com.kangyonggan.blog.service.sites.ArticleService;
import com.kangyonggan.blog.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author kangyonggan
 * @since 2019-05-08
 */
@Service
public class ArticleServiceImpl extends BaseService<Article> implements ArticleService {

    @Override
    @MethodLog
    public List<Article> searchArticles(ArticleRequest articleRequest) {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();

        String title = articleRequest.getTitle();
        if (StringUtils.isNotEmpty(title)) {
            criteria.andLike("title", StringUtil.toLike(title));
        }

        String[] createdTime = articleRequest.getCreatedTime();
        if (createdTime != null && StringUtils.isNotEmpty(createdTime[0])) {
            criteria.andGreaterThanOrEqualTo("createdTime", createdTime[0]);
            criteria.andLessThanOrEqualTo("createdTime", createdTime[1]);
        }

        if (StringUtils.isNotEmpty(articleRequest.getSort())) {
            if (articleRequest.getOrder() == 0) {
                example.orderBy(articleRequest.getSort()).asc();
            } else {
                example.orderBy(articleRequest.getSort()).desc();
            }
        } else {
            example.orderBy("createdTime").desc();
        }

        PageHelper.startPage(articleRequest.getPageNum(), articleRequest.getPageSize());
        return myMapper.selectByExample(example);
    }

    @Override
    @MethodLog
    public List<Article> searchArticles(Integer pageNum, Integer pageSize, String key) {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("isDeleted", YesNo.NO.getCode());
        if (StringUtils.isNotEmpty(key)) {
            criteria.andLike("title", StringUtil.toLike(key));
        }

        example.selectProperties("articleId", "title", "summary", "viewNum", "createdTime", "content");
        example.orderBy("createdTime").desc();
        PageHelper.startPage(pageNum, pageSize);

        return myMapper.selectByExample(example);
    }

    @Override
    @MethodLog
    public List<Article> preSearchArticles(String key) {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("isDeleted", YesNo.NO.getCode());
        if (StringUtils.isNotEmpty(key)) {
            criteria.andLike("title", StringUtil.toLike(key));
        }

        example.selectProperties("articleId", "title");
        example.orderBy("createdTime").desc();
        PageHelper.startPage(1, 13);
        return myMapper.selectByExample(example);
    }

    @Override
    @MethodLog
    public List<Article> findViewArticles() {
        Example example = new Example(Article.class);
        example.createCriteria().andEqualTo("isDeleted", YesNo.NO.getCode());

        example.selectProperties("articleId", "title", "summary", "viewNum", "createdTime");
        example.orderBy("viewNum").desc();
        PageHelper.startPage(1, 20);

        return myMapper.selectByExample(example);
    }

    @Override
    @MethodLog
    public Article findArticleById(Long articleId) {
        return myMapper.selectByPrimaryKey(articleId);
    }

    @Override
    @MethodLog
    public void updateArticle(Article article) {
        myMapper.updateByPrimaryKeySelective(article);
    }

    @Override
    @MethodLog
    public void saveArticle(Article article) {
        myMapper.insertSelective(article);
    }
}
