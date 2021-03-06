package com.kangyonggan.blog.controller.web;

import com.github.pagehelper.PageInfo;
import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.dto.SearchRequest;
import com.kangyonggan.blog.model.Article;
import com.kangyonggan.blog.service.sites.ArticleService;
import com.kangyonggan.blog.util.Collections3;
import com.kangyonggan.blog.util.MarkdownUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章相关查询接口
 *
 * @author kangyonggan
 * @since 2019-04-24
 */
@RestController
@RequestMapping("article")
@Api(tags = "ArticleController", description = "文章相关查询接口")
public class ArticleController extends BaseController {

    @Autowired
    private ArticleService articleService;

    /**
     * 文章列表查询
     *
     * @param searchRequest
     * @return
     */
    @PostMapping
    @ApiOperation("文章列表查询")
    public Response list(SearchRequest searchRequest) {
        Response response = successResponse();
        List<Article> articles = articleService.searchArticles(
                searchRequest.getPageNum(), searchRequest.getPageSize(), searchRequest.getKey());
        PageInfo<Article> pageInfo = new PageInfo<>(articles);

        response.put("pageInfo", pageInfo);
        return response;
    }

    /**
     * 预查询文章
     *
     * @param key
     * @return
     */
    @PostMapping("preSearch")
    @ApiOperation("预查询文章")
    @ApiImplicitParam(name = "key", value = "搜索关键字", required = true, example = "java")
    public Response preSearch(String key) {
        Response response = successResponse();
        List<Article> articles = articleService.preSearchArticles(key);

        response.put("preList", articles);
        return response;
    }

    /**
     * 大家都在看
     *
     * @return
     */
    @GetMapping("view")
    @ApiOperation("大家都在看")
    public Response view() {
        Response response = successResponse();

        // 大家都在看
        List<Article> articles = articleService.findViewArticles();

        response.put("articles", articles);
        return response;
    }

    /**
     * 文章详情
     *
     * @param articleId
     * @return
     */
    @PostMapping("detail")
    @ApiOperation("文章详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleId", value = "文章ID", required = true, example = "1"),
    })
    public Response detail(@RequestParam("articleId") Long articleId) {
        Response response = successResponse();
        Article article = articleService.findArticleById(articleId);
        article.setViewNum(article.getViewNum() + 1);
        articleService.updateArticle(article);

        response.put("article", article);
        response.put("tocs", MarkdownUtil.genTocs(article.getContent()));
        return response;
    }
}
