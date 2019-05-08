package com.kangyonggan.blog.controller.api;

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
 * 首页相关查询接口
 *
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
    @GetMapping("navs")
    @ApiOperation("查询导航")
    public Response navs() {
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
    @GetMapping("articles")
    @ApiOperation("查询首页文章")
    public Response articles() {
        Response response = successResponse();
        List<Article> articles = articleService.searchArticles(1, 6, null);

        response.put("articles", articles);
        return response;
    }

    /**
     * 查询首页小说
     *
     * @return
     */
    @GetMapping("novels")
    @ApiOperation("查询首页小说")
    public Response novels() {
        Response response = successResponse();
        List<Novel> novels = novelService.findNewNovels();

        response.put("novels", novels);
        return response;
    }

}
