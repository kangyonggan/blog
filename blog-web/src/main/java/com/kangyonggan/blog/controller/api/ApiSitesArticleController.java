package com.kangyonggan.blog.controller.api;

import com.github.pagehelper.PageInfo;
import com.kangyonggan.blog.annotation.PermissionMenu;
import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.ArticleRequest;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.model.Article;
import com.kangyonggan.blog.service.sites.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author kangyonggan
 * @since 2019-05-28
 */
@RestController
@RequestMapping("api/sites/article")
@Api(tags = "ApiSitesArticleController", description = "文章相关接口")
public class ApiSitesArticleController extends BaseController {

    @Autowired
    private ArticleService articleService;

    /**
     * 查询文章列表
     *
     * @param articleRequest
     * @return
     */
    @PostMapping
    @PermissionMenu("SITES_ARTICLE")
    @ApiOperation("查询文章列表")
    public Response list(ArticleRequest articleRequest) {
        Response response = successResponse();
        List<Article> articles = articleService.searchArticles(articleRequest);

        response.put("pageInfo", new PageInfo<>(articles));
        return response;
    }

    /**
     * 保存文章
     *
     * @param article
     * @return
     */
    @PostMapping("save")
    @PermissionMenu("SITES_ARTICLE")
    @ApiOperation("保存文章")
    public Response save(Article article) {
        article.setUserId(currentUserId());
        articleService.saveArticle(article);
        return successResponse();
    }

    /**
     * 更新文章
     *
     * @param article
     * @return
     */
    @PostMapping("update")
    @PermissionMenu("SITES_ARTICLE")
    @ApiOperation("更新文章")
    public Response update(Article article) {
        articleService.updateArticle(article);
        return successResponse();
    }

    /**
     * 删除/恢复文章
     *
     * @param articleId
     * @return
     */
    @PostMapping("delete")
    @PermissionMenu("SITES_ARTICLE")
    @ApiOperation("删除/恢复文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleId", value = "文章ID", required = true, example = "1"),
            @ApiImplicitParam(name = "isDeleted", value = "是否删除(0:恢复，1:删除)", required = true, example = "0")
    })
    public Response delete(Long articleId, Byte isDeleted) {
        Article article = new Article();
        article.setArticleId(articleId);
        article.setIsDeleted(isDeleted);
        articleService.updateArticle(article);

        return successResponse();
    }

    /**
     * 文章详情
     *
     * @param articleId
     * @return
     */
    @PostMapping("detail")
    @PermissionMenu("SITES_ARTICLE")
    @ApiOperation("文章详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleId", value = "文章ID", required = true, example = "1"),
    })
    public Response detail(@RequestParam("articleId") Long articleId) {
        Response response = successResponse();

        response.put("article", articleService.findArticleById(articleId));
        return response;
    }

}
