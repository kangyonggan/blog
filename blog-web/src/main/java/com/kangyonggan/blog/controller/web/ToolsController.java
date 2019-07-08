package com.kangyonggan.blog.controller.web;

import com.kangyonggan.blog.annotation.Secret;
import com.kangyonggan.blog.constants.DictType;
import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.BaZiDto;
import com.kangyonggan.blog.dto.IdNoDto;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.model.Dict;
import com.kangyonggan.blog.service.system.DictService;
import com.kangyonggan.blog.util.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private DictService dictService;

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
        fileName += "." + ext;
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
    public Response idNoQry(@RequestParam("idNo") String idNo) {
        Response response = successResponse();
        idNo = idNo.replaceAll("x", "X");

        boolean isIdNo15 = IdNoUtil.isIdCard15(idNo);
        if (isIdNo15) {
            idNo = IdNoUtil.convert15To18(idNo);
        }

        // 身份证号码(18位)
        response.put("idNo", idNo);
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

    /**
     * 生成电子印章
     *
     * @param name
     * @return
     * @throws Exception
     */
    @PostMapping("seal")
    public Response seal(@RequestParam("name") String name) throws Exception {
        Response response = successResponse();
        List<Dict> dicts = dictService.findDictsByDictType(DictType.FONT.getCode());
        List<String[]> result = new ArrayList<>();
        for (Dict dict : dicts) {
            String[] arr = new String[2];
            String fileName = fileHelper.genFileName("seal") + ".png";
            SealUtil.build(name, dict.getValue(), fileHelper.getFileUploadPath() + "seal/" + fileName);
            arr[0] = "upload/seal/" + fileName;
            arr[1] = dict.getValue();
            result.add(arr);
        }

        response.put("result", result);
        return response;
    }

    /**
     * 生成身份证
     *
     * @param idNoDto
     * @return
     */
    @PostMapping("idNoGen")
    public Response idNoGen(IdNoDto idNoDto) {
        Response response = successResponse();
        List<String> idNos = IdNoUtil.genIdCard(idNoDto);

        response.put("idNos", idNos);
        return response;
    }

    /**
     * 图片解析
     *
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("parseImg")
    @Secret(enable = false)
    public Response parseImg(MultipartFile file) throws Exception {
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

        // 解析
        Map<String, Object> resultMap = Images.parseImg(fileHelper.getFileUploadPath() + "temp/" + fileName + "." + ext);

        response.put("resultMap", resultMap);
        return response;
    }

    /**
     * 五行、八字
     *
     * @param baZiDto
     * @return
     */
    @PostMapping("bazi")
    public Response bazi(BaZiDto baZiDto) throws Exception {
        Response response = successResponse();

        if (baZiDto.getIsLunar()) {
            // 阴历转阳历
            String date = CalendarUtil.lunarToSolar(LocalDate.of(baZiDto.getYear(), baZiDto.getMonth(), baZiDto.getDay()).format(DateTimeFormatter.BASIC_ISO_DATE));
            baZiDto.setYear(Integer.parseInt(date.substring(0, 4)));
            baZiDto.setMonth(Integer.parseInt(date.substring(4, 6)));
            baZiDto.setDay(Integer.parseInt(date.substring(6, 8)));
        }

        String baZi = DestinyUtil.getEightWord(baZiDto.getYear(), baZiDto.getMonth(), baZiDto.getDay(), baZiDto.getHour());
        response.put("birthday", baZiDto.getYear() + "年" + baZiDto.getMonth() + "月" + baZiDto.getDay() + "日");
        response.put("baZi", baZi);
        response.put("shengXiao", DestinyUtil.getShengXiao(baZiDto.getYear()));
        response.put("xingZuo", DestinyUtil.getXingZuo(baZiDto.getMonth(), baZiDto.getDay()));

        String wuXing = DestinyUtil.getWuXing(baZi);
        response.put("wuXing", wuXing);
        response.put("que", DestinyUtil.wuxing(wuXing));

        String riGan = DestinyUtil.getDayColumn(baZiDto.getYear(), baZiDto.getMonth(), baZiDto.getDay()).substring(0, 1);
        String wuXingOfRiGan = DestinyUtil.getTianGanWuXing(riGan);
        response.put("yuShi", DestinyUtil.getYunShi(wuXingOfRiGan, baZiDto.getMonth()));

        return response;
    }
}
