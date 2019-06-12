package com.kangyonggan.blog.controller.web;

import com.kangyonggan.blog.annotation.Secret;
import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.util.*;
import io.swagger.annotations.ApiOperation;
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
 * @since 2019-06-10
 */
@RestController
@RequestMapping("tools")
@Log4j2
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

    /**
     * 身份证查询
     *
     * @param idNo
     * @return
     */
    @PostMapping("idNoQry")
    @ApiOperation("生成缩略图")
    public Response idNoQry(@RequestParam("idNo") String idNo) {
        Response response = successResponse();
        idNo = idNo.replaceAll("x", "X");

        boolean isIdNo15 = IdNoUtil.isIdCard15(idNo);
        if (isIdNo15) {
            idNo = IdNoUtil.convert15To18(idNo);
        }

        // 原户籍地
        response.put("address", IdNoConstants.getArea(idNo.substring(0, 6)));
        // 出生年月
        response.put("birthday", IdNoUtil.getYearFromIdCard(idNo) + "年" + IdNoUtil.getMonthFromIdCard(idNo) + "月" + IdNoUtil.getDayFromIdCard(idNo) + "日");
        // 生肖
        response.put("shengXiao", DestinyUtil.getShengXiao(Integer.parseInt(IdNoUtil.getYearFromIdCard(idNo))));
        // 星座
        response.put("xingZuo", DestinyUtil.getXingZuo(Integer.parseInt(IdNoUtil.getMonthFromIdCard(idNo)), Integer.parseInt(IdNoUtil.getDayFromIdCard(idNo))));
        // 性别
        response.put("gender", IdNoUtil.getSexFromIdCard(idNo));

        return response;
    }

}
