package com.kangyonggan.blog.controller.api;

import com.kangyonggan.blog.annotation.Secret;
import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.util.FileHelper;
import com.kangyonggan.blog.util.FileUpload;
import com.kangyonggan.blog.util.Images;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author kangyonggan
 * @since 5/4/18
 */
@RestController
@RequestMapping("api/upload")
@Log4j2
@Secret(enable = false)
public class ApiUploadController extends BaseController {

    @Autowired
    private FileHelper fileHelper;

    /**
     * 相册图片上传
     *
     * @param file 文件
     * @return 响应
     */
    @PostMapping("album")
    public Response album(@RequestParam("file") MultipartFile file) {
        Response response = successResponse();
        try {
            // 原图
            String originFileName = fileHelper.genFileName("photo");
            // 缩略图
            String thumbFileName = originFileName + "_THUMB.png";

            FileUpload.upload(fileHelper.getFileUploadPath() + "photo/", originFileName, file);
            originFileName += "." + FilenameUtils.getExtension(file.getOriginalFilename());
            Images.thumb(fileHelper.getFileUploadPath() + "photo/" + originFileName, fileHelper.getFileUploadPath() + "photo/thumb/" + thumbFileName, 195, 133);

            response.put("originFileName", "upload/photo/" + originFileName);
        } catch (Exception e) {
            response.failure(e.getMessage());
            log.error("相册图片上传失败", e);
        }

        return response;
    }
}
