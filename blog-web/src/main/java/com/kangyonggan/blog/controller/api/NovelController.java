package com.kangyonggan.blog.controller.api;

import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.NovelDto;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.dto.SearchRequest;
import com.kangyonggan.blog.model.Novel;
import com.kangyonggan.blog.service.sites.NovelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 小说相关查询接口
 *
 * @author kangyonggan
 * @since 2019-04-24
 */
@RestController
@RequestMapping("novel")
@Api(tags = "NovelController", description = "小说相关查询接口")
public class NovelController extends BaseController {

    @Autowired
    private NovelService novelService;

    /**
     * 小说列表查询
     *
     * @param searchRequest
     * @searchRequest
     */
    @PostMapping
    @ApiOperation("小说列表查询")
    public Response list(SearchRequest searchRequest) {
        Response response = successResponse();
        List<NovelDto> novels = novelService.searchNovels(searchRequest.getKey());

        response.put("novels", novels);
        return response;
    }

    /**
     * 最新小说
     *
     * @return
     */
    @GetMapping("new")
    @ApiOperation("最新小说")
    public Response view() {
        Response response = successResponse();

        // 最新小说
        List<Novel> novels = novelService.findNewNovels();

        response.put("novels", novels);
        return response;
    }

}
