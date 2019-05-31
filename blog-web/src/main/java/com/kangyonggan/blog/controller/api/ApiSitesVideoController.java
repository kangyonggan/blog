package com.kangyonggan.blog.controller.api;

import com.github.pagehelper.PageInfo;
import com.kangyonggan.blog.annotation.PermissionMenu;
import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.dto.VideoRequest;
import com.kangyonggan.blog.model.Video;
import com.kangyonggan.blog.service.sites.VideoService;
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
@RequestMapping("api/sites/video")
@Api(tags = "ApiSitesVideoController", description = "视频相关接口")
public class ApiSitesVideoController extends BaseController {

    @Autowired
    private VideoService videoService;

    /**
     * 查询视频列表
     *
     * @param videoRequest
     * @return
     */
    @PostMapping
    @PermissionMenu("SITES_VIDEO")
    @ApiOperation("查询视频列表")
    public Response list(VideoRequest videoRequest) {
        Response response = successResponse();
        List<Video> videos = videoService.searchVideos(videoRequest);

        response.put("pageInfo", new PageInfo<>(videos));
        return response;
    }

    /**
     * 保存视频
     *
     * @param video
     * @return
     */
    @PostMapping("save")
    @PermissionMenu("SITES_VIDEO")
    @ApiOperation("保存视频")
    public Response save(Video video) {
        video.setUserId(currentUserId());

        // 允许全屏
        video.setContent(video.getContent().replace("'allowfullscreen'", "allowfullscreen"));
        // http转https
        video.setContent(video.getContent().replace("http://", "https://"));

        videoService.saveVideo(video);
        return successResponse();
    }

    /**
     * 更新视频
     *
     * @param video
     * @return
     */
    @PostMapping("update")
    @PermissionMenu("SITES_VIDEO")
    @ApiOperation("更新视频")
    public Response update(Video video) {
        videoService.updateVideo(video);
        return successResponse();
    }

    /**
     * 删除/恢复视频
     *
     * @param videoId
     * @return
     */
    @PostMapping("delete")
    @PermissionMenu("SITES_VIDEO")
    @ApiOperation("删除/恢复视频")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "videoId", value = "视频ID", required = true, example = "1"),
            @ApiImplicitParam(name = "isDeleted", value = "是否删除(0:恢复，1:删除)", required = true, example = "0")
    })
    public Response delete(Long videoId, Byte isDeleted) {
        Video video = new Video();
        video.setVideoId(videoId);
        video.setIsDeleted(isDeleted);
        videoService.updateVideo(video);

        return successResponse();
    }

    /**
     * 视频详情
     *
     * @param videoId
     * @return
     */
    @PostMapping("detail")
    @PermissionMenu("SITES_VIDEO")
    @ApiOperation("视频详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "videoId", value = "视频ID", required = true, example = "1"),
    })
    public Response detail(@RequestParam("videoId") Long videoId) {
        Response response = successResponse();

        response.put("video", videoService.findVideoById(videoId));
        return response;
    }

}
