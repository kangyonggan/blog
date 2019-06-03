package com.kangyonggan.blog.service.sites;

import com.kangyonggan.blog.dto.ArticleRequest;
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
     * @param articleRequest
     * @return
     */
    List<Article> searchArticles(ArticleRequest articleRequest);
    /**
     * 搜索文章
     *
     * @param pageNum
     * @param pageSize
     * @param key
     * @return
     */
    List<Article> searchArticles(Integer pageNum, Integer pageSize, String key);

    /**
     * 预搜索
     *
     * @param key
     * @return
     */
    List<Article> preSearchArticles(String key);

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

    /**
     * 保存文章
     *
     * @param article
     */
    void saveArticle(Article article);
}
