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
    public List<Article> searchArticles(int pageNum, int pageSize) {
        Example example = new Example(Article.class);
        example.createCriteria().andEqualTo("isDeleted", YesNo.NO.getCode());

        example.selectProperties("articleId", "title", "summary", "createdTime");
        example.orderBy("articleId").desc();
        PageHelper.startPage(pageNum, pageSize);

        return myMapper.selectByExample(example);
    }
}
