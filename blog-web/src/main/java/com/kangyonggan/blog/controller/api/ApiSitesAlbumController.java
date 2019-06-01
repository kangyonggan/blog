package com.kangyonggan.blog.controller.api;

import com.github.pagehelper.PageInfo;
import com.kangyonggan.blog.annotation.PermissionMenu;
import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.AlbumRequest;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.model.Album;
import com.kangyonggan.blog.model.AlbumPhoto;
import com.kangyonggan.blog.service.sites.AlbumPhotoService;
import com.kangyonggan.blog.service.sites.AlbumService;
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
@RequestMapping("api/sites/album")
@Api(tags = "ApiSitesAlbumController", description = "相册相关接口")
public class ApiSitesAlbumController extends BaseController {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private AlbumPhotoService albumPhotoService;

    /**
     * 查询相册列表
     *
     * @param albumRequest
     * @return
     */
    @PostMapping
    @PermissionMenu("SITES_ALBUM")
    @ApiOperation("查询相册列表")
    public Response list(AlbumRequest albumRequest) {
        Response response = successResponse();
        List<Album> albums = albumService.searchAlbums(albumRequest);

        response.put("pageInfo", new PageInfo<>(albums));
        return response;
    }

    /**
     * 保存相册
     *
     * @param album
     * @return
     */
    @PostMapping("save")
    @PermissionMenu("SITES_ALBUM")
    @ApiOperation("保存相册")
    public Response save(Album album) {
        album.setUserId(currentUserId());

        albumService.saveAlbum(album);
        return successResponse();
    }

    /**
     * 更新相册
     *
     * @param album
     * @param albumPhotos
     * @return
     */
    @PostMapping("update")
    @PermissionMenu("SITES_ALBUM")
    @ApiOperation("更新相册")
    public Response update(Album album, String albumPhotos) {
        albumService.updateAlbumWithPhotos(album, albumPhotos);
        return successResponse();
    }

    /**
     * 删除/恢复相册
     *
     * @param albumId
     * @return
     */
    @PostMapping("delete")
    @PermissionMenu("SITES_ALBUM")
    @ApiOperation("删除/恢复相册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "albumId", value = "相册ID", required = true, example = "1"),
            @ApiImplicitParam(name = "isDeleted", value = "是否删除(0:恢复，1:删除)", required = true, example = "0")
    })
    public Response delete(Long albumId, Byte isDeleted) {
        Album album = new Album();
        album.setAlbumId(albumId);
        album.setIsDeleted(isDeleted);
        albumService.updateAlbum(album);

        return successResponse();
    }

    /**
     * 相册详情
     *
     * @param albumId
     * @return
     */
    @PostMapping("detail")
    @PermissionMenu("SITES_ALBUM")
    @ApiOperation("相册详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "albumId", value = "相册ID", required = true, example = "1"),
    })
    public Response detail(@RequestParam("albumId") Long albumId) {
        Response response = successResponse();

        List<AlbumPhoto> albumPhotos = albumPhotoService.findAlbumPhotos(albumId);

        response.put("album", albumService.findAlbumByAlbumId(albumId));
        response.put("albumPhotos", albumPhotos);
        return response;
    }

}
