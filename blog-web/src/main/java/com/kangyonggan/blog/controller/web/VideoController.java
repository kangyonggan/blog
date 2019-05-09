package com.kangyonggan.blog.controller.web;

import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.model.Video;
import com.kangyonggan.blog.service.sites.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author kangyonggan
 * @since 1/10/19
 */
@RestController
@RequestMapping("video")
@Api(tags = "VideoController", description = "视频相关查询接口")
public class VideoController extends BaseController {

    @Autowired
    private VideoService videoService;

    /**
     * 视频列表查询
     *
     * @return
     */
    @GetMapping
    @ApiOperation("视频列表查询")
    public Response list() {
        Response response = successResponse();
        List<Video> videos = videoService.findAllVideo();

        response.put("videos", videos);
        return response;
    }

    /**
     * 视频详情
     *
     * @param videoId
     * @return
     */
    @PostMapping("detail")
    @ApiOperation("视频详情")
    public Response detail(@RequestParam("videoId") Long videoId) {
        Response response = successResponse();
        Video video = videoService.findVideoById(videoId);
        video.setViewNum(video.getViewNum() + 1);
        videoService.updateVideo(video);

        response.put("video", video);
        return response;
    }

}
