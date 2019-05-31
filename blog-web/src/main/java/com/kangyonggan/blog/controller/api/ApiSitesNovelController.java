package com.kangyonggan.blog.controller.api;

import com.github.pagehelper.PageInfo;
import com.kangyonggan.blog.annotation.PermissionMenu;
import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.NovelRequest;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.model.Novel;
import com.kangyonggan.blog.service.sites.NovelService;
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
@RequestMapping("api/sites/novel")
@Api(tags = "ApiSitesNovelController", description = "小说相关接口")
public class ApiSitesNovelController extends BaseController {

    @Autowired
    private NovelService novelService;

    /**
     * 查询小说列表
     *
     * @param novelRequest
     * @return
     */
    @PostMapping
    @PermissionMenu("SITES_NOVEL")
    @ApiOperation("查询小说列表")
    public Response list(NovelRequest novelRequest) {
        Response response = successResponse();
        List<Novel> novels = novelService.searchNovels(novelRequest);

        response.put("pageInfo", new PageInfo<>(novels));
        return response;
    }

    /**
     * 保存小说
     *
     * @param novel
     * @return
     */
    @PostMapping("save")
    @PermissionMenu("SITES_NOVEL")
    @ApiOperation("保存小说")
    public Response save(Novel novel) {
        novelService.saveNovel(novel);
        return successResponse();
    }

    /**
     * 更新小说
     *
     * @param novel
     * @return
     */
    @PostMapping("update")
    @PermissionMenu("SITES_NOVEL")
    @ApiOperation("更新小说")
    public Response update(Novel novel) {
        novelService.updateNovel(novel);
        return successResponse();
    }

    /**
     * 删除/恢复小说
     *
     * @param novelId
     * @return
     */
    @PostMapping("delete")
    @PermissionMenu("SITES_NOVEL")
    @ApiOperation("删除/恢复小说")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "novelId", value = "小说ID", required = true, example = "1"),
            @ApiImplicitParam(name = "isDeleted", value = "是否删除(0:恢复，1:删除)", required = true, example = "0")
    })
    public Response delete(Long novelId, Byte isDeleted) {
        Novel novel = new Novel();
        novel.setNovelId(novelId);
        novel.setIsDeleted(isDeleted);
        novelService.updateNovel(novel);

        return successResponse();
    }

    /**
     * 小说详情
     *
     * @param novelId
     * @return
     */
    @PostMapping("detail")
    @PermissionMenu("SITES_NOVEL")
    @ApiOperation("小说详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "novelId", value = "小说ID", required = true, example = "1"),
    })
    public Response detail(@RequestParam("novelId") Long novelId) {
        Response response = successResponse();

        response.put("novel", novelService.findNovelById(novelId));
        return response;
    }

}
