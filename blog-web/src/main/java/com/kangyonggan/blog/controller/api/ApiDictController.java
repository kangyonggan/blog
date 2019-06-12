package com.kangyonggan.blog.controller.api;

import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.service.system.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kangyonggan
 * @since 2019-05-28
 */
@RestController
@RequestMapping("api/dict")
@Api(tags = "ApiDictController", description = "字典相关接口")
public class ApiDictController extends BaseController {

    @Autowired
    private DictService dictService;

    /**
     * 查询字典列表
     *
     * @param dictType
     * @return
     */
    @PostMapping
    @ApiOperation("查询字典列表")
    @ApiImplicitParam(name = "dictType", value = "字典类型", required = true, example = "ID_TYPE")
    public Response dicts(String dictType) {
        Response response = successResponse();

        response.put("dicts", dictService.findDictsByDictType(dictType));
        return response;
    }

}
