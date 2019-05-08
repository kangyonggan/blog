package com.kangyonggan.blog.controller.web;

import com.kangyonggan.blog.constants.DictType;
import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.model.Article;
import com.kangyonggan.blog.model.Dict;
import com.kangyonggan.blog.model.Novel;
import com.kangyonggan.blog.service.sites.ArticleService;
import com.kangyonggan.blog.service.sites.NovelService;
import com.kangyonggan.blog.service.system.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author kangyonggan
 * @since 2019-04-24
 */
@RestController
@RequestMapping("/")
@Api(tags = "IndexController", description = "首页相关查询接口")
public class IndexController extends BaseController {

    @Autowired
    private DictService dictService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private NovelService novelService;

    /**
     * 查询导航
     *
     * @return
     */
    @GetMapping("navList")
    @ApiOperation("查询导航")
    public Response detail() {
        Response response = successResponse();
        List<Dict> navList = dictService.findDictsByDictType(DictType.NAV.getCode());

        response.put("navList", navList);
        return response;
    }

    /**
     * 查询首页文章
     *
     * @return
     */
    @GetMapping("indexArticles")
    @ApiOperation("查询首页文章")
    public Response indexArticles() {
        Response response = successResponse();
        List<Article> articles = articleService.searchArticles(1, 6);

        response.put("articles", articles);
        return response;
    }

    /**
     * 查询首页小说
     *
     * @return
     */
    @GetMapping("indexNovels")
    @ApiOperation("查询首页小说")
    public Response indexNovels() {
        Response response = successResponse();
        List<Novel> novels = novelService.searchNovels(1, 6);

        response.put("novels", novels);
        return response;
    }

}
