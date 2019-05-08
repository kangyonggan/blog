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
    List<Article> searchArticles(int pageNum, int pageSize);
}
