package com.kangyonggan.blog.controller.api;

import com.kangyonggan.blog.annotation.PermissionLogin;
import com.kangyonggan.blog.constants.EnumUtil;
import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kangyonggan
 * @since 2019-05-28
 */
@RestController
@RequestMapping("api/enum")
@Api(tags = "ApiEnumController", description = "枚举相关接口")
public class ApiEnumController extends BaseController {

    /**
     * 查询枚举列表
     *
     * @param enumKey
     * @return
     */
    @PostMapping
    @PermissionLogin
    @ApiOperation("查询枚举列表")
    @ApiImplicitParam(name = "enumKey", value = "枚举的键", required = true, example = "DictType")
    public Response email(String enumKey) {
        Response response = successResponse();

        response.put("enumList", EnumUtil.getInstance().getEnumList(enumKey));
        return response;
    }

}
