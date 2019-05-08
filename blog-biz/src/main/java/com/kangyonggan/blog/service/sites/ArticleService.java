package com.kangyonggan.blog.service.sites;

import com.kangyonggan.blog.model.Article;

import java.util.List;

/**
 * @author kangyonggan
 * @since 2019-05-08
 */
public interface ArticleService {
    /**
     * 搜索文章
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<Article> searchArticles(Integer pageNum, Integer pageSize);

    /**
     * 大家都在看
     *
     * @return
     */
    List<Article> findViewArticles();

    /**
     * 查找文章
     *
     * @param articleId
     * @return
     */
    Article findArticleById(Long articleId);

    /**
     * 更新文章
     *
     * @param article
     */
    void updateArticle(Article article);
}
