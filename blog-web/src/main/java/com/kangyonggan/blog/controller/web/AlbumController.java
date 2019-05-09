package com.kangyonggan.blog.controller.web;

import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.model.Album;
import com.kangyonggan.blog.model.AlbumPhoto;
import com.kangyonggan.blog.service.sites.AlbumPhotoService;
import com.kangyonggan.blog.service.sites.AlbumService;
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
@RequestMapping("album")
@Api(tags = "AlbumController", description = "相册相关查询接口")
public class AlbumController extends BaseController {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private AlbumPhotoService albumPhotoService;

    /**
     * 相册列表查询
     *
     * @return
     */
    @GetMapping
    @ApiOperation("相册列表查询")
    public Response list() {
        Response response = successResponse();
        List<Album> albums = albumService.findAllAlbums();

        response.put("albums", albums);
        return response;
    }

    /**
     * 相册详情
     *
     * @param albumId
     * @return
     */
    @PostMapping("detail")
    @ApiOperation("相册详情")
    public Response detail(@RequestParam("albumId") Long albumId) {
        Response response = successResponse();
        Album album = albumService.findAlbumByAlbumId(albumId);
        List<AlbumPhoto> albumPhotos = albumPhotoService.findAlbumPhotos(albumId);

        response.put("albumPhotos", albumPhotos);
        response.put("album", album);
        return response;
    }

}
