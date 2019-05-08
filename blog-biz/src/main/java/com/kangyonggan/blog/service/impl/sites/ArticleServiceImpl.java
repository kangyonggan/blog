package com.kangyonggan.blog.service.impl.sites;

import com.github.pagehelper.PageHelper;
import com.kangyonggan.blog.annotation.MethodLog;
import com.kangyonggan.blog.constants.YesNo;
import com.kangyonggan.blog.model.Article;
import com.kangyonggan.blog.service.BaseService;
import com.kangyonggan.blog.service.sites.ArticleService;
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
    public List<Article> searchArticles(Integer pageNum, Integer pageSize) {
        Example example = new Example(Article.class);
        example.createCriteria().andEqualTo("isDeleted", YesNo.NO.getCode());

        example.selectProperties("articleId", "title", "summary", "viewNum", "createdTime");
        example.orderBy("articleId").desc();
        PageHelper.startPage(pageNum, pageSize);

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
}
