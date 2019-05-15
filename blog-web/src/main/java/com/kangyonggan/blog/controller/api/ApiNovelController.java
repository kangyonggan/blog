package com.kangyonggan.blog.controller.api;

import com.github.pagehelper.PageInfo;
import com.kangyonggan.blog.annotation.Secret;
import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.NovelDto;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.model.Novel;
import com.kangyonggan.blog.model.Section;
import com.kangyonggan.blog.service.sites.NovelQueueService;
import com.kangyonggan.blog.service.sites.NovelService;
import com.kangyonggan.blog.service.sites.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author kangyonggan
 * @since 2019/1/5 0005
 */
@RestController
@RequestMapping("api/novel")
@Secret(enable = false)
public class ApiNovelController extends BaseController {

    @Autowired
    private NovelService novelService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private NovelQueueService novelQueueService;

    /**
     * 小说列表
     *
     * @return
     */
    @GetMapping
    public Response list() {
        Response response = successResponse();
        List<NovelDto> novels = novelService.searchNovels(null);

        response.put("novels", novels);
        return response;
    }

    /**
     * 章节列表
     *
     * @param novelId
     * @param pageNum
     * @return
     */
    @GetMapping("{novelId:[\\d]+}")
    public Response list(@PathVariable("novelId") Long novelId,
                         @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        Response response = successResponse();
        Novel novel = novelService.findNovelById(novelId);
        List<Section> sections = sectionService.findSectionsByPage(novelId, pageNum);
        PageInfo<Section> pageInfo = new PageInfo<>(sections);

        response.put("pageInfo", pageInfo);
        response.put("novelName", novel.getName());
        return response;
    }

    /**
     * 章节详情
     *
     * @param sectionId
     * @return
     */
    @GetMapping("section/{sectionId:[\\d]+}")
    public Response sectionDetail(@PathVariable("sectionId") Long sectionId) {
        Response response = successResponse();

        Section section = sectionService.findSectionById(sectionId);
        Section prevSection = sectionService.findPrevSection(section.getNovelId(), sectionId);
        Section nextSection = sectionService.findNextSection(section.getNovelId(), sectionId);

        response.put("section", section);
        response.put("prevSection", prevSection);
        response.put("nextSection", nextSection);
        return response;
    }

    /**
     * 更新小说
     *
     * @param novelId 小说ID
     * @return 响应
     */
    @GetMapping("{novelId:[\\d]+}/pull")
    public Response pull(@PathVariable("novelId") Long novelId) {
        Response response = successResponse();
        if (novelQueueService.exists(novelId)) {
            novelService.popOrCheck(true);
            return response.failure("小说已经加入更新队列，无需重复操作");
        }
        novelService.pullNovels(String.valueOf(novelId));
        return response;
    }
}
