package com.kangyonggan.blog.controller.web;

import com.kangyonggan.blog.annotation.Secret;
import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.util.FileHelper;
import com.kangyonggan.blog.util.FileUpload;
import com.kangyonggan.blog.util.Images;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author kangyonggan
 * @since 2019-06-10
 */
@RestController
@RequestMapping("tools")
public class ToolsController extends BaseController {

    @Autowired
    private FileHelper fileHelper;

    /**
     * 生成缩略图
     *
     * @param file
     * @param width
     * @param height
     * @return
     * @throws Exception
     */
    @PostMapping("thumb")
    @ApiOperation("生成缩略图")
    @Secret(enable = false)
    public Response thumb(MultipartFile file, @RequestParam(name = "width", defaultValue = "400") Integer width, @RequestParam(value = "height", defaultValue = "300") int height) throws Exception {
        Response response = successResponse();

        if (file == null || file.isEmpty()) {
            return response.failure("图片内容为空！");
        }
        // 文件类型校验
        String ext = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();
        if (!"jpg,jpeg,png".contains(ext)) {
            return response.failure("请选择jpg、jpeg或者png格式的图片");
        }

        // 上传到本地
        String fileName = fileHelper.genFileName("temp");
        FileUpload.upload(fileHelper.getFileUploadPath() + "temp/", fileName, file);

        // 缩略图
        String thumbFileName = fileName + "_THUMB.png";
        fileName += "." + FilenameUtils.getExtension(file.getOriginalFilename());
        Images.thumb(fileHelper.getFileUploadPath() + "temp/" + fileName, fileHelper.getFileUploadPath() + "temp/" + thumbFileName, width, height);

        response.put("thumb", "upload/temp/" + thumbFileName);
        return response;
    }
}
