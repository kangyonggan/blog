package com.kangyonggan.blog.controller.api;

import com.kangyonggan.blog.constants.NovelSource;
import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.NovelDto;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.dto.SearchRequest;
import com.kangyonggan.blog.model.Novel;
import com.kangyonggan.blog.model.NovelQueue;
import com.kangyonggan.blog.model.Section;
import com.kangyonggan.blog.service.sites.NovelQueueService;
import com.kangyonggan.blog.service.sites.NovelService;
import com.kangyonggan.blog.service.sites.SectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private SectionService sectionService;

    @Autowired
    private NovelQueueService novelQueueService;

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

    /**
     * 小说详情
     *
     * @param novelId
     * @return
     */
    @PostMapping("detail")
    @ApiOperation("小说详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "novelId", value = "小说ID", required = true, example = "1"),
    })
    public Response detail(@RequestParam("novelId") Long novelId) {
        Response response = successResponse();
        Novel novel = novelService.findNovelById(novelId);
        Section lastSection = sectionService.findLastSection(novelId);
        NovelQueue novelQueue = novelQueueService.findNovelQueue(novelId);
        List<Section> lastSections = sectionService.findLastSections(novelId);
        List<Section> sections = sectionService.findSections(novelId);
        String source = NovelSource.getUrlByCode(novel.getSource());

        response.put("novel", novel);
        response.put("lastSection", lastSection);
        response.put("novelQueue", novelQueue);
        response.put("lastSections", lastSections);
        response.put("sections", sections);
        response.put("source", source);
        return response;
    }

    /**
     * 更新小说
     *
     * @param novelId
     * @return
     */
    @PostMapping("pull")
    @ApiOperation("更新小说")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "novelId", value = "小说ID", required = true, example = "1"),
    })
    public Response pull(@RequestParam("novelId") Long novelId) {
        Response response = successResponse();

        if (novelQueueService.exists(novelId)) {
            return response.failure("小说已经加入更新队列，无需重复操作");
        }
        novelService.pullNovels(String.valueOf(novelId));

        return response;
    }

}
